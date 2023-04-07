package com.you.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysDict;
import com.you.mapper.SysDictMapper;
import com.you.service.SysDictService;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 数据字典服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Resource
    private SysDictMapper sysDictMapper;

    /**
     * 分页获取一级数据字典列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public Page<SysDict> listPage(String keyword,Integer current, Integer size) {

        //获取一级数据字典列表
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",0L);
        queryWrapper.orderByAsc("created_time");

        //添加条件
        JSONObject jsonObject = JSONObject.parseObject(keyword);
        if(jsonObject.size() > 0){
            String name = jsonObject.getString("name");
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }

        //分页插件
        Page<SysDict> page= new Page(current,size);

        //分页获取数据
        Page<SysDict> pageData = sysDictMapper.selectPage(page, queryWrapper);

        //判断是否存在子数据字典
        pageData.getRecords().forEach(sysDict -> {
            Boolean hasChildren = hasChildren(sysDict.getId());
            sysDict.setHasChildren(hasChildren);
        });

        return pageData;
    }

    /**
     * 获取子数据字典列表
     * @param id
     * @return
     */
    @Override
    public List<SysDict> getChildrenList(String id) {

        //获取子数据字典
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",id);
        queryWrapper.orderByAsc("created_time");
        List<SysDict> list = sysDictMapper.selectList(queryWrapper);

        //判断是否存在子数据字典
        list.forEach(sysDict -> {
            Boolean hasChildren = hasChildren(sysDict.getId());
            sysDict.setHasChildren(hasChildren);
        });
        return list;
    }

    /**
     * 获取数据字典
     * @return
     */
    @Override
    public List<SysDict> treeList() {

        List<SysDict> allList = list();
        //根节点
        List<SysDict> root = new ArrayList<SysDict>();
        for (SysDict nav : allList) {
            if (nav.getParentId().equals("0")){     //父节点是0的，为根节点。
                root.add(nav);
            }
        }

        //为根菜单设置子菜单，getClild是递归调用的
        for (SysDict nav : root) {
            List<SysDict> childList = getChild(nav.getId(), allList);
            nav.setChildren(childList);//给根节点设置子节点
        }

        return  root;
    }

    /**
     * 获取子节点
     * @param id 父节点id
     * @param allList 所有列表
     * @return 每个根节点下，所有子菜单列表
     */
    public List<SysDict> getChild(String id,List<SysDict> allList){
        //子菜单
        List<SysDict> childList = new ArrayList<SysDict>();
        for (SysDict nav : allList) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            if (nav.getParentId().equals(id)){
                childList.add(nav);
            }
        }
        //递归
        for (SysDict nav : childList) {
            nav.setChildren(getChild(nav.getId(), allList));
        }
        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childList.size() == 0 ){
            return new ArrayList<SysDict>();
        }
        return childList;
    }

    /**
     * 保存数据字典
     * @param sysDict
     * @return
     */
    @Override
    public ResultBean saveDict(SysDict sysDict) {
        String id = UUID.randomUUID().toString().replaceAll("-","");
        sysDict.setId(id);
        //未选择上级菜单，则默认为添加目录
        if(StringUtil.isNullOrEmpty(sysDict.getParentId())){
            sysDict.setParentId("0");
        }
        sysDict.setCreatedTime(new Date());
        save(sysDict);
        return ResultBean.success(sysDict);
    }

    /**
     * 更新数据字典
     * @param sysDict
     * @return
     */
    @Override
    public ResultBean updateDict(SysDict sysDict) {
        //更新操作
        //未选择上级菜单，则默认为添加目录
        if(StringUtil.isNullOrEmpty(sysDict.getParentId())){
            sysDict.setParentId("0");
        }
        sysDict.setUpdatedTime(new Date());
        updateById(sysDict);
        return ResultBean.success(sysDict);
    }

    /**
     * 删除数据字典
     * @param id
     * @return
     */
    @Override
    public ResultBean delete(String id) {
        //判断该菜单是否存在子数据字典，存在无法删除
        int count = count(new QueryWrapper<SysDict>().eq("parent_id", id));
        if (count > 0) {
            return ResultBean.fail("请先删除子数据字典");
        }

        //删除数据字典
        removeById(id);

        return ResultBean.success();
    }

    /**
     * 根据编码获取数据字典
     * @param code
     * @return
     */
    @Override
    public ResultBean getListByCode(String code) {
        //获取父字典
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",code);
        SysDict sysDictParent = sysDictMapper.selectOne(queryWrapper);
        if(sysDictParent != null){
            //获取子字典
            QueryWrapper query = new QueryWrapper();
            query.eq("parent_id",sysDictParent.getId());
            List<SysDict> sysDictList = sysDictMapper.selectList(query);
            return ResultBean.success(sysDictList);
        }
        return ResultBean.fail("查无此字典数据");
    }

    /**
     * 判断是否存在子数据字典
     * @param id
     * @return
     */
    private Boolean hasChildren(String id){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",id);
        Integer count = sysDictMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
