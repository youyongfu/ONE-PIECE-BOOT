package com.you.service.impl;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.constant.CommonConstant;
import com.you.entity.SysEpisodes;
import com.you.entity.SysEpisodesCharacter;
import com.you.mapper.SysEpisodesMapper;
import com.you.service.SysEpisodesCharacterService;
import com.you.service.SysEpisodesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 剧集服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysEpisodesServiceImpl extends ServiceImpl<SysEpisodesMapper, SysEpisodes> implements SysEpisodesService {

    @Resource
    private SysEpisodesMapper sysEpisodesMapper;
    @Resource
    private SysEpisodesCharacterService sysEpisodesCharacterService;

    /**
     * 分页获取列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public ResultBean listPage(String keyword, Integer current, Integer size) {

        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("created_time");

        //添加条件
        JSONObject jsonObject = JSONObject.parseObject(keyword);
        if(jsonObject.size() > 0){
            String name = jsonObject.getString("name");
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }

        //分页插件
        Page<SysEpisodes> page= new Page(current,size);

        //分页获取数据
        Page<SysEpisodes> pageData = sysEpisodesMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 获取所有数据
     * @return
     */
    @Override
    public ResultBean getAll() {
        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("created_time");

        return ResultBean.success(sysEpisodesMapper.selectList(queryWrapper));
    }

    /**
     * 新增
     * @param sysEpisodes
     * @return
     */
    @Override
    public ResultBean saveEpisodes(SysEpisodes sysEpisodes) {
        //保存剧集信息
        String episodesId = UUID.randomUUID().toString().replaceAll("-","");
        sysEpisodes.setId(episodesId);
        sysEpisodes.setCreatedTime(new Date());
        save(sysEpisodes);

        //保存剧集登场角色
        saveOrUpdateEpisodesCharacter(sysEpisodes, CommonConstant.SAVE_OPERATE);

        return ResultBean.success(sysEpisodes);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(String id) {
        MapBuilder<Object, Object> map = MapUtil.builder();

        //获取剧集信息
        SysEpisodes sysEpisodes = getById(id);

        //获取剧集登场角色
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("episodes_id",id);
        queryWrapper.orderByAsc("sort_number");
        sysEpisodes.setSysEpisodesCharacterList(sysEpisodesCharacterService.list(queryWrapper));

        map.put("episodes",sysEpisodes);

        return ResultBean.success(map.build());
    }

    /**
     * 更新
     * @param sysEpisodes
     * @return
     */
    @Override
    public ResultBean updateEpisodes(SysEpisodes sysEpisodes) {
        //更新操作
        sysEpisodes.setUpdatedTime(new Date());
        updateById(sysEpisodes);

        //更新剧集登场角色
        saveOrUpdateEpisodesCharacter(sysEpisodes, CommonConstant.UPDATE_OPERATE);

        return ResultBean.success(sysEpisodes);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public ResultBean deleteEpisodes(String id) {
        //删除
        removeById(id);

        //删除剧集登场角色
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("episodes_id",id);
        sysEpisodesCharacterService.remove(queryWrapper);

        return ResultBean.success();
    }

    /**
     * 保存/更新剧集登场角色
     * @param sysEpisodes
     */
    private void saveOrUpdateEpisodesCharacter(SysEpisodes sysEpisodes, String type) {
        String episodesId = sysEpisodes.getId();
        if(CommonConstant.SAVE_OPERATE.equals(type)){
            //保存
            sysEpisodes.getSysEpisodesCharacterList().forEach(sysEpisodesCharacter -> {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                sysEpisodesCharacter.setId(id);
                sysEpisodesCharacter.setEpisodesId(episodesId);
            });
            sysEpisodesCharacterService.saveBatch(sysEpisodes.getSysEpisodesCharacterList());
        }else {
            //更新
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("episodes_id",episodesId);
            List<SysEpisodesCharacter> addList = new ArrayList<>();
            List<SysEpisodesCharacter> updateList = new ArrayList<>();
            List<SysEpisodesCharacter> deleteList = sysEpisodesCharacterService.list(queryWrapper);
            List<String> deleteIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(deleteList)){
                deleteIdList = deleteList.stream().map(m -> m.getId()).collect(Collectors.toList());
            }
            List<SysEpisodesCharacter> sysEpisodesCharacterList = sysEpisodes.getSysEpisodesCharacterList();
            List<String> finalDeleteIdList = deleteIdList;
            sysEpisodesCharacterList.forEach(sysEpisodesCharacter -> {
                if(StringUtils.isBlank(sysEpisodesCharacter.getId())){            //新增
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    sysEpisodesCharacter.setId(id);
                    sysEpisodesCharacter.setEpisodesId(episodesId);
                    addList.add(sysEpisodesCharacter);
                }else {
                    updateList.add(sysEpisodesCharacter);       //更新
                    finalDeleteIdList.remove(sysEpisodesCharacter.getId());    //删除
                }
            });
            sysEpisodesCharacterService.saveBatch(addList);
            sysEpisodesCharacterService.updateBatchById(updateList);
            sysEpisodesCharacterService.removeByIds(finalDeleteIdList);
        }
    }

}
