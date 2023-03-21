package com.you.service.impl;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.constant.DevilnutConstant;
import com.you.constant.OssConstant;
import com.you.entity.SysClobContent;
import com.you.entity.SysDevilnut;
import com.you.entity.SysFigureDevilnut;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysDevilnutMapper;
import com.you.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 果实图鉴服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysDevilnutServiceImpl extends ServiceImpl<SysDevilnutMapper, SysDevilnut> implements SysDevilnutService {

    @Resource
    private SysDevilnutMapper sysDevilnutMapper;
    @Resource
    private SysClobContentService sysClobContentService;
    @Resource
    private SysUploadFileService sysUploadFileService;
    @Resource
    private SysUploadFileRecordService sysUploadFileRecordService;
    @Resource
    private SysFigureDevilnutService sysFigureDevilnutService;

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
            String category = jsonObject.getString("category");
            queryWrapper.eq(StrUtil.isNotBlank(category), "category", category);
        }

        //分页插件
        Page<SysDevilnut> page= new Page(current,size);

        //分页获取数据
        Page<SysDevilnut> pageData = sysDevilnutMapper.selectPage(page, queryWrapper);

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

        return ResultBean.success(sysDevilnutMapper.selectList(queryWrapper));
    }

    /**
     * 新增
     * @param sysDevilnut
     * @return
     */
    @Override
    public ResultBean saveDevilnut(SysDevilnut sysDevilnut) {
        //保存果实信息
        String devilnutId = UUID.randomUUID().toString().replaceAll("-","");
        sysDevilnut.setId(devilnutId);
        sysDevilnut.setCreatedTime(new Date());
        save(sysDevilnut);

        //保存果实内容信息
        saveOrUpdateCotent(sysDevilnut,"save");

        return ResultBean.success(sysDevilnut);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(String id) {
        MapBuilder<Object, Object> map = MapUtil.builder();

        //获取果实信息
        SysDevilnut sysDevilnut = getById(id);
        map.put("devilnut",sysDevilnut);

        //获取上传文件信息
        List<SysUploadFile> fileList = sysUploadFileService.getFileInfo(OssConstant.DEVILNUT_TYPE,sysDevilnut.getId());
        map.put("fileList",fileList);

        //获取果实内容信息
        List<SysClobContent> devilnutContentList = sysClobContentService.contentListByOwnerId(id);
        devilnutContentList.forEach(conten ->{
            map.put(conten.getType(),conten.getContent());
        });

        return ResultBean.success(map.build());
    }

    /**
     * 更新
     * @param sysDevilnut
     * @return
     */
    @Override
    public ResultBean updateDevilnut(SysDevilnut sysDevilnut) {
        //更新操作
        sysDevilnut.setUpdatedTime(new Date());
        updateById(sysDevilnut);

        //更新果实内容信息
        saveOrUpdateCotent(sysDevilnut,"update");

        //删除已保存的果实文件关系
        if(StringUtils.isNotBlank(sysDevilnut.getFileIds())){
            List<String> fileIds = Arrays.asList(sysDevilnut.getFileIds().split(","));
            sysUploadFileRecordService.deleteFileRecord(OssConstant.DEVILNUT_TYPE,fileIds);
        }

        return ResultBean.success(sysDevilnut);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public ResultBean deleteDevilnut(String id) {

        int count = sysFigureDevilnutService.count(new QueryWrapper<SysFigureDevilnut>().eq("devilnut_id", id));
        if (count > 0) {
            return ResultBean.fail("该果实已被人物引用，无法删除！");
        }

        //删除果实
        removeById(id);

        //删除果实内容信息
        sysClobContentService.removeContentByOwnerId(id);

        return ResultBean.success();
    }

    /**
     * 保存、更新果实内容
     * @param sysDevilnut
     */
    private void saveOrUpdateCotent(SysDevilnut sysDevilnut,String type) {
        String devilnutId = sysDevilnut.getId();
        if("save".equals(type)){
            //保存
            List<SysClobContent> contentList = new ArrayList<>();
            contentList.add(sysClobContentService.assemblyData(devilnutId,sysDevilnut.getAbility(), DevilnutConstant.ABILITY_TYPE));
            contentList.add(sysClobContentService.assemblyData(devilnutId,sysDevilnut.getMove(), DevilnutConstant.MOVE_TYPE));
            sysClobContentService.saveBatch(contentList);
        }else {
            //更新
            List<SysClobContent> devilnutContentList = sysClobContentService.contentListByOwnerId(devilnutId);
            devilnutContentList.forEach(content ->{
                if(DevilnutConstant.ABILITY_TYPE.equals(content.getType())){
                    content.setContent(sysDevilnut.getAbility());
                }else if(DevilnutConstant.MOVE_TYPE.equals(content.getType())){
                    content.setContent(sysDevilnut.getMove());
                }
            });
            sysClobContentService.updateBatchById(devilnutContentList);
        }

    }

}
