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
import com.you.constant.OrganizationConstant;
import com.you.constant.OssConstant;
import com.you.entity.SysOrganization;
import com.you.entity.SysOrganizationContent;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysOrganizationMapper;
import com.you.service.SysOrganizationContentService;
import com.you.service.SysOrganizationService;
import com.you.service.SysUploadFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    private SysOrganizationContentService sysOrganizationContentService;

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

        //保存组织内容信息
        List<SysOrganizationContent> contentList = new ArrayList<>();
        contentList.add(assemblyData(organizationId,sysOrganization.getBackground(), OrganizationConstant.BACKGROUND_TYPE));
        contentList.add(assemblyData(organizationId,sysOrganization.getExperience(), OrganizationConstant.EXPERIENCE_TYPE));
        contentList.add(assemblyData(organizationId,sysOrganization.getCivilization(), OrganizationConstant.CIVILIZATION_TYPE));
        sysOrganizationContentService.saveBatch(contentList);

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
        map.put("organization",sysOrganization);

        //获取上传文件信息
        List<SysUploadFile> fileList = sysUploadFileService.getFileRecord(OssConstant.ORGANIZATION_TYPE,sysOrganization.getId());
        map.put("fileList",fileList);

        //获取组织内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("organization_id",id);
        List<SysOrganizationContent> organizationContentList = sysOrganizationContentService.list(queryWrapper);
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
        String organizationId = sysOrganization.getId();
        if(StringUtils.isBlank(sysOrganization.getParentId())){
            sysOrganization.setParentId("0");
        }
        sysOrganization.setUpdatedTime(new Date());
        updateById(sysOrganization);

        //更新组织内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("organization_id",organizationId);
        List<SysOrganizationContent> organizationContentList = sysOrganizationContentService.list(queryWrapper);
        organizationContentList.forEach(content ->{
            if(OrganizationConstant.BACKGROUND_TYPE.equals(content.getType())){
                content.setContent(sysOrganization.getBackground());
            }else if(OrganizationConstant.EXPERIENCE_TYPE.equals(content.getType())){
                content.setContent(sysOrganization.getExperience());
            }else if(OrganizationConstant.CIVILIZATION_TYPE.equals(content.getType())){
                content.setContent(sysOrganization.getCivilization());
            }
        });
        sysOrganizationContentService.updateBatchById(organizationContentList);


        //删除已保存的组织文件关系
        if(StringUtils.isNotBlank(sysOrganization.getFileIds())){
            List<String> fileIds = Arrays.asList(sysOrganization.getFileIds().split(","));
            sysUploadFileService.deleteFileRecord(OssConstant.ORGANIZATION_TYPE,fileIds);
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
            return ResultBean.fail("请先删除子组织");
        }

        //删除组织
        removeById(id);

        //删除组织内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("organization_id",id);
        sysOrganizationContentService.remove(queryWrapper);

        return ResultBean.success();
    }

    /**
     * 组装内容数据
     * @param organizationId
     * @param content
     * @param type
     * @return
     */
    private SysOrganizationContent assemblyData(String organizationId,String content,String type){
        SysOrganizationContent organizationContent = new SysOrganizationContent();
        organizationContent.setId(UUID.randomUUID().toString().replaceAll("-",""));
        organizationContent.setOrganizationId(organizationId);
        organizationContent.setContent(content);
        organizationContent.setType(type);
        return organizationContent;
    }

}
