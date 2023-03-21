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
import com.you.constant.IslandsConstant;
import com.you.constant.OssConstant;
import com.you.entity.*;
import com.you.mapper.SysIslandsMapper;
import com.you.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 岛屿管理服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysIslandsServiceImpl extends ServiceImpl<SysIslandsMapper, SysIslands> implements SysIslandsService {

    @Resource
    private SysIslandsMapper sysIslandsMapper;
    @Resource
    private SysClobContentService sysClobContentService;
    @Resource
    private SysUploadFileService sysUploadFileService;
    @Resource
    private SysUploadFileRecordService sysUploadFileRecordService;
    @Resource
    private SysOrganizationService sysOrganizationService;
    @Resource
    private SysFigureIslandsService sysFigureIslandsService;

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
        Page<SysIslands> page= new Page(current,size);

        //分页获取数据
        Page<SysIslands> pageData = sysIslandsMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 获取树形数据
     * @return
     */
    @Override
    public List<SysIslands> treeList() {
        List<SysIslands> tree = new ArrayList<>();
        list().forEach(sysIslands -> {
            //获取最外层父节点
            if(sysIslands.getParentId().equals("0")){
                tree.add(sysIslands);
            }

            //依次获取子节点
            list().forEach(sysIslandsChild -> {
                if(sysIslands.getId() == sysIslandsChild.getParentId()){
                    sysIslands.getChildren().add(sysIslandsChild);
                }
            });
        });
        return  tree;
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
        return ResultBean.success(sysIslandsMapper.selectList(queryWrapper));
    }

    /**
     * 保存
     * @param sysIslands
     * @return
     */
    @Override
    public ResultBean saveIslands(SysIslands sysIslands) {
        //保存岛屿信息
        String islandsId = UUID.randomUUID().toString().replaceAll("-","");
        sysIslands.setId(islandsId);
        //未选择上级菜单，则默认为添加一级岛屿
        if(StringUtils.isBlank(sysIslands.getParentId())){
            sysIslands.setParentId("0");
        }
        sysIslands.setCreatedTime(new Date());
        save(sysIslands);

        //保存岛屿内容信息
        saveOrUpdateContent(sysIslands,"save");

        return ResultBean.success(sysIslands);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(String id) {
        MapBuilder<Object, Object> map = MapUtil.builder();

        //获取岛屿信息
        SysIslands sysIslands = getById(id);
        map.put("islands",sysIslands);

        //获取上传文件信息
        List<SysUploadFile> fileList = sysUploadFileService.getFileInfo(OssConstant.ISLANDS_TYPE,sysIslands.getId());
        map.put("fileList",fileList);

        //获取岛屿内容信息
        List<SysClobContent> islandsContentList = sysClobContentService.contentListByOwnerId(id);
        islandsContentList.forEach(conten ->{
            map.put(conten.getType(),conten.getContent());
        });

        return ResultBean.success(map.build());
    }

    /**
     * 更新
     * @param sysIslands
     * @return
     */
    @Override
    public ResultBean updateIslands(SysIslands sysIslands) {
        //更新操作
        if(StringUtils.isBlank(sysIslands.getParentId())){
            sysIslands.setParentId("0");
        }
        sysIslands.setUpdatedTime(new Date());
        updateById(sysIslands);

        //更新岛屿内容信息
        saveOrUpdateContent(sysIslands,"update");

        //删除已保存的岛屿文件关系
        if(StringUtils.isNotBlank(sysIslands.getFileIds())){
            List<String> fileIds = Arrays.asList(sysIslands.getFileIds().split(","));
            sysUploadFileRecordService.deleteFileRecord(OssConstant.ISLANDS_TYPE,fileIds);
        }

        return ResultBean.success(sysIslands);
    }

    /**
     * 根据id删除岛屿
     * @param id
     * @return
     */
    @Override
    public ResultBean deleteIslands(String id) {
        //判断该菜单是否存在子岛屿，存在无法删除
        int count = count(new QueryWrapper<SysIslands>().eq("parent_id", id));
        if (count > 0) {
            return ResultBean.fail("该岛屿存在下属岛屿，无法删除！");
        }

        count = sysOrganizationService.count(new QueryWrapper<SysOrganization>().eq("headquarters", id));
        if (count > 0) {
            return ResultBean.fail("该岛屿被组织引用，无法删除！");
        }

        count = sysFigureIslandsService.count(new QueryWrapper<SysFigureIslands>().eq("islands_id", id));
        if (count > 0) {
            return ResultBean.fail("该岛屿被人物引用，无法删除！");
        }

        //删除岛屿
        removeById(id);

        //删除岛屿内容信息
        sysClobContentService.removeContentByOwnerId(id);

        return ResultBean.success();
    }

    /**
     * 保存/更新岛屿内容信息
     * @param sysIslands
     * @param type
     */
    private void saveOrUpdateContent(SysIslands sysIslands, String type) {
        String islandsId = sysIslands.getId();
        if("save".equals(type)){
            //保存
            List<SysClobContent> contentList = new ArrayList<>();
            contentList.add(sysClobContentService.assemblyData(islandsId,sysIslands.getSource(), IslandsConstant.SOURCE_TYPE));
            contentList.add(sysClobContentService.assemblyData(islandsId,sysIslands.getGeography(), IslandsConstant.GEOGRAPHY_TYPE));
            contentList.add(sysClobContentService.assemblyData(islandsId,sysIslands.getAppearances(), IslandsConstant.APPEARANCES_TYPE));
            sysClobContentService.saveBatch(contentList);
        }else {
            //更新
            List<SysClobContent> islandsContentList = sysClobContentService.contentListByOwnerId(islandsId);
            islandsContentList.forEach(content ->{
                if(IslandsConstant.SOURCE_TYPE.equals(content.getType())){
                    content.setContent(sysIslands.getSource());
                }else if(IslandsConstant.GEOGRAPHY_TYPE.equals(content.getType())){
                    content.setContent(sysIslands.getGeography());
                }else if(IslandsConstant.APPEARANCES_TYPE.equals(content.getType())){
                    content.setContent(sysIslands.getAppearances());
                }
            });
            sysClobContentService.updateBatchById(islandsContentList);
        }
    }
}
