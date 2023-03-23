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
import com.you.constant.OrganizationConstant;
import com.you.constant.OssConstant;
import com.you.entity.*;
import com.you.mapper.SysOrganizationMapper;
import com.you.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织管理服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization> implements SysOrganizationService {

    @Resource
    private SysOrganizationMapper sysOrganizationMapper;
    @Resource
    private SysUploadFileService sysUploadFileService;
    @Resource
    private SysClobContentService sysClobContentService;
    @Resource
    private SysUploadFileRecordService sysUploadFileRecordService;
    @Resource
    private SysOrganizationRelationService sysOrganizationRelationService;
    @Resource
    private SysFigureExperienceService sysFigureExperienceService;

    /**
     * 分页获取组织列表
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
            String nature = jsonObject.getString("nature");
            queryWrapper.eq(StrUtil.isNotBlank(nature), "nature", nature);
            String parentId = jsonObject.getString("parentId");
            queryWrapper.eq(StrUtil.isNotBlank(parentId), "parent_id", parentId);
        }

        //分页插件
        Page<SysOrganization> page= new Page(current,size);

        //分页获取数据
        Page<SysOrganization> pageData = sysOrganizationMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 获取组织树形数据
     * @return
     */
    @Override
    public List<SysOrganization> treeList() {
        List<SysOrganization> menuTree = new ArrayList<>();
        list().forEach(sysOrganization -> {
            //获取最外层父节点
            if(sysOrganization.getParentId().equals("0")){
                menuTree.add(sysOrganization);
            }

            //依次获取子节点
            list().forEach(sysOrganizationChild -> {
                if(sysOrganization.getId().equals(sysOrganizationChild.getParentId())){
                    sysOrganization.getChildren().add(sysOrganizationChild);
                }
            });
        });
        return  menuTree;
    }

    /**
     * 获取所有组织
     * @return
     */
    @Override
    public ResultBean getAll() {
        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("created_time");

        return ResultBean.success(sysOrganizationMapper.selectList(queryWrapper));
    }

    /**
     * 新增组织
     * @param sysOrganization
     * @return
     */
    @Override
    public ResultBean saveOrganization(SysOrganization sysOrganization) {
        //保存组织信息
        String organizationId = UUID.randomUUID().toString().replaceAll("-","");
        sysOrganization.setId(organizationId);
        //未选择上级菜单，则默认为添加目录
        if(StringUtils.isBlank(sysOrganization.getParentId())){
            sysOrganization.setParentId("0");
        }
        sysOrganization.setCreatedTime(new Date());
        save(sysOrganization);

        //保存组织关系
        saveOrUpdateOrganizationRelation(sysOrganization, CommonConstant.SAVE_OPERATE);

        //保存组织内容信息
        saveOrUpdateContent(sysOrganization,CommonConstant.SAVE_OPERATE);

        return ResultBean.success(sysOrganization);
    }

    /**
     * 根据id获取组织
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(String id) {

        MapBuilder<Object, Object> map = MapUtil.builder();

        //获取组织信息
        SysOrganization sysOrganization = getById(id);

        //获取组织关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("owner_id",id);
        sysOrganization.setSysOrganizationRelationList(sysOrganizationRelationService.list(queryWrapper));

        map.put("organization",sysOrganization);

        //获取上传文件信息
        List<SysUploadFile> fileList = sysUploadFileService.getFileInfo(OssConstant.ORGANIZATION_TYPE,sysOrganization.getId());
        map.put("fileList",fileList);

        //获取组织内容信息
        List<SysClobContent> organizationContentList = sysClobContentService.contentListByOwnerId(id);
        organizationContentList.forEach(conten ->{
            map.put(conten.getType(),conten.getContent());
        });

        return ResultBean.success(map.build());
    }

    /**
     * 更新组织
     * @param sysOrganization
     * @return
     */
    @Override
    public ResultBean updateOrganization(SysOrganization sysOrganization) {
        //更新操作
        if(StringUtils.isBlank(sysOrganization.getParentId())){
            sysOrganization.setParentId("0");
        }
        sysOrganization.setUpdatedTime(new Date());
        updateById(sysOrganization);

        //更新组织关系
        saveOrUpdateOrganizationRelation(sysOrganization,CommonConstant.UPDATE_OPERATE);

        //更新组织内容信息
        saveOrUpdateContent(sysOrganization,CommonConstant.UPDATE_OPERATE);

        //删除已保存的组织文件关系
        if(StringUtils.isNotBlank(sysOrganization.getFileIds())){
            List<String> fileIds = Arrays.asList(sysOrganization.getFileIds().split(","));
            sysUploadFileRecordService.deleteFileRecord(OssConstant.ORGANIZATION_TYPE,fileIds);
        }

        return ResultBean.success(sysOrganization);
    }

    /**
     * 根据id删除组织
     * @param id
     * @return
     */
    @Override
    public ResultBean delete(String id) {
        //判断该菜单是否存在子菜单，存在无法删除
        int count = count(new QueryWrapper<SysOrganization>().eq("parent_id", id));
        if (count > 0) {
            return ResultBean.fail("该组织存在下属组织，无法删除！");
        }

        count = sysFigureExperienceService.count(new QueryWrapper<SysFigureExperience>().eq("organization_id", id));
        if (count > 0) {
            return ResultBean.fail("该组织已被人物引用，无法删除！");
        }

        count = sysOrganizationRelationService.count(new QueryWrapper<SysOrganizationRelation>().eq("relation_organization_id", id));
        if (count > 0) {
            return ResultBean.fail("该组织已被其他组织引用，无法删除！");
        }

        //删除组织
        removeById(id);

        //删除组织关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("owner_id",id);
        sysOrganizationRelationService.remove(queryWrapper);

        //删除组织内容信息
        sysClobContentService.removeContentByOwnerId(id);

        return ResultBean.success();
    }

    /**
     * 保存/更新组织关系
     * @param sysOrganization
     * @param type
     */
    private void saveOrUpdateOrganizationRelation(SysOrganization sysOrganization, String type) {
        String organizationId = sysOrganization.getId();
        if(CommonConstant.SAVE_OPERATE.equals(type)){
            //保存
            sysOrganization.getSysOrganizationRelationList().forEach(sysOrganizationRelation -> {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                sysOrganizationRelation.setId(id);
                sysOrganizationRelation.setOwnerId(organizationId);
            });
            sysOrganizationRelationService.saveBatch(sysOrganization.getSysOrganizationRelationList());
        }else {
            //更新
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("owner_id",organizationId);
            List<SysOrganizationRelation> addList = new ArrayList<>();
            List<SysOrganizationRelation> updateList = new ArrayList<>();
            List<SysOrganizationRelation> deleteList = sysOrganizationRelationService.list(queryWrapper);
            List<String> deleteIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(deleteList)){
                deleteIdList = deleteList.stream().map(m -> m.getId()).collect(Collectors.toList());
            }
            List<SysOrganizationRelation> sysOrganizationRelationList = sysOrganization.getSysOrganizationRelationList();
            List<String> finalDeleteIdList = deleteIdList;
            sysOrganizationRelationList.forEach(sysOrganizationRelation -> {
                if(StringUtils.isBlank(sysOrganizationRelation.getId())){            //新增
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    sysOrganizationRelation.setId(id);
                    sysOrganizationRelation.setOwnerId(organizationId);
                    addList.add(sysOrganizationRelation);
                }else {
                    updateList.add(sysOrganizationRelation);       //更新
                    finalDeleteIdList.remove(sysOrganizationRelation.getId());    //删除
                }
            });
            sysOrganizationRelationService.saveBatch(addList);
            sysOrganizationRelationService.updateBatchById(updateList);
            sysOrganizationRelationService.removeByIds(finalDeleteIdList);
        }

    }

    /**
     * 保存/更新组织内容
     * @param sysOrganization
     * @param type
     */
    private void saveOrUpdateContent(SysOrganization sysOrganization, String type) {
        String organizationId = sysOrganization.getId();
        if(CommonConstant.SAVE_OPERATE.equals(type)) {
            //保存
            List<SysClobContent> contentList = new ArrayList<>();
            contentList.add(sysClobContentService.assemblyData(organizationId,sysOrganization.getBackground(), OrganizationConstant.BACKGROUND_TYPE));
            contentList.add(sysClobContentService.assemblyData(organizationId,sysOrganization.getExperience(), OrganizationConstant.EXPERIENCE_TYPE));
            contentList.add(sysClobContentService.assemblyData(organizationId,sysOrganization.getCivilization(), OrganizationConstant.CIVILIZATION_TYPE));
            sysClobContentService.saveBatch(contentList);
        }else {
            //更新
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("organization_id",organizationId);
            List<SysClobContent> organizationContentList = sysClobContentService.contentListByOwnerId(organizationId);
            organizationContentList.forEach(content ->{
                if(OrganizationConstant.BACKGROUND_TYPE.equals(content.getType())){
                    content.setContent(sysOrganization.getBackground());
                }else if(OrganizationConstant.EXPERIENCE_TYPE.equals(content.getType())){
                    content.setContent(sysOrganization.getExperience());
                }else if(OrganizationConstant.CIVILIZATION_TYPE.equals(content.getType())){
                    content.setContent(sysOrganization.getCivilization());
                }
            });
            sysClobContentService.updateBatchById(organizationContentList);
        }
    }
}
