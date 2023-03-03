package com.you.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysOrganization;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysOrganizationMapper;
import com.you.service.SysOrganizationService;
import com.you.service.SysUploadFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
            if(sysOrganization.getParentId() == 0L){
                menuTree.add(sysOrganization);
            }

            //依次获取子节点
            list().forEach(sysOrganizationChild -> {
                if(sysOrganization.getId() == sysOrganizationChild.getParentId()){
                    sysOrganization.getChildren().add(sysOrganizationChild);
                }
            });
        });
        return  menuTree;
    }

    /**
     * 根据id获取组织
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(Long id) {
        SysOrganization sysOrganization = getById(id);

        List<SysUploadFile> fileList = new ArrayList<>();
        if(StringUtils.isNotBlank(sysOrganization.getSign())){
            String[] signs = sysOrganization.getSign().split(",");
            for (String FileId : signs) {
                fileList.add(sysUploadFileService.getById(FileId));
            }
        }

        return ResultBean.success(MapUtil.builder().put("organization",sysOrganization).put("fileList",fileList).build());
    }

    /**
     * 根据id删除组织
     * @param id
     * @return
     */
    @Override
    public ResultBean delete(Long id) {
        //判断该菜单是否存在子菜单，存在无法删除
        int count = count(new QueryWrapper<SysOrganization>().eq("parent_id", id));
        if (count > 0) {
            return ResultBean.fail("请先删除子组织");
        }

        //删除菜单
        removeById(id);

        return ResultBean.success();
    }


}
