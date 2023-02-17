package com.you.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysUser;
import com.you.mapper.SysUserMapper;
import com.you.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    /**
     * 分页获取用户列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public ResultBean listPage(String keyword, Integer current, Integer size) {
        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();

        //添加条件
        JSONObject jsonObject = JSONObject.parseObject(keyword);
        if(jsonObject.size() > 0){
            String name = jsonObject.getString("name");
            queryWrapper.like(StrUtil.isNotBlank(name), "username", name);
        }

        //分页插件
        Page<SysUser> page= new Page(current,size);

        //分页获取数据
        Page<SysUser> pageData = sysUserMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public ResultBean delete(Long id) {
        //删除角色
        removeById(id);

        //删除角色用户关系
        sysUserMapper.deleteUserRoleByUserId(id);

        return ResultBean.success();
    }

}