package com.you.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.constant.UserConstant;
import com.you.dto.PasswordDto;
import com.you.entity.SysRole;
import com.you.entity.SysUploadFile;
import com.you.entity.SysUser;
import com.you.entity.SysUserRole;
import com.you.mapper.SysRoleMapper;
import com.you.mapper.SysUserMapper;
import com.you.service.AuthorityService;
import com.you.service.SysUploadFileService;
import com.you.service.SysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private AuthorityService authorityService;
    @Resource
    private SysUploadFileService uploadFileService;

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
     * 获取用户信息
     * @return
     */
    @Override
    public ResultBean userInfo(Principal principal) {
        SysUser sysUser = getByUsername(principal.getName());

        //获取头像信息
        String url = "";
        if(StringUtils.isNotBlank(sysUser.getUploadFileId())){
            SysUploadFile sysUploadFile = uploadFileService.getById(sysUser.getUploadFileId());
            if(sysUploadFile != null){
                url = sysUploadFile.getUrl();
            }
        }

        return ResultBean.success(MapUtil.builder().put("id",sysUser.getId()).put("username",sysUser.getUsername()).put("avatar",url).build());
    }



    /**
     * 修改密码
     * @param passwordDto
     * @param principal
     * @return
     */
    @Override
    public ResultBean updatePassword(PasswordDto passwordDto, Principal principal) {
        //比对旧密码
        SysUser sysUser = getByUsername(principal.getName());
        boolean matches = bCryptPasswordEncoder.matches(passwordDto.getCurrentPass(),sysUser.getPassword());
        if(!matches){
            return ResultBean.fail("旧密码不正确");
        }

        //更新密码
        sysUser.setPassword(bCryptPasswordEncoder.encode(passwordDto.getPassword()));
        sysUser.setUpdatedTime(new Date());
        updateById(sysUser);
        return ResultBean.success("密码修改成功");
    }

    /**
     * 重置密码
     * @param userId
     * @return
     */
    @Override
    public ResultBean rePass(Long userId) {
        SysUser sysUser = getById(userId);
        sysUser.setPassword(bCryptPasswordEncoder.encode(UserConstant.DEFULT_PASSWORD));
        sysUser.setUpdatedTime(new Date());
        updateById(sysUser);
        return ResultBean.success();
    }

    /**
     * 上传头像
     * @param file
     * @param name
     * @return
     */
    @Override
    public ResultBean uploadAvatar(MultipartFile file, String name,String type) {

        //上传图片
        SysUploadFile sysUploadFile = uploadFileService.uploadFile(file,type,true);

        //保存上传文件id
        SysUser sysUser = getByUsername(name);
        sysUser.setUploadFileId(sysUploadFile.getId());
        updateById(sysUser);

        return ResultBean.success(sysUploadFile.getUrl());
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
        queryWrapper.orderByAsc("created_time");

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
     * 添加用户
     * @param sysUser
     * @return
     */
    @Override
    public ResultBean saveUser(SysUser sysUser) {
        //判断用户名是否已存在
        SysUser oldUser = getByUsername(sysUser.getUsername());
        if(oldUser != null){
            return ResultBean.fail("用户名已存在，无法新增！");
        }
        sysUser.setCreatedTime(new Date());
        sysUser.setPassword(bCryptPasswordEncoder.encode(UserConstant.DEFULT_PASSWORD));
        save(sysUser);
        return ResultBean.success(sysUser);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(Long id) {

        SysUser sysUser = getById(id);

        //获取该用户的角色信息
        List<SysRole> sysRoleList = sysRoleMapper.getRoleInfoByUserId(id);
        if(CollectionUtils.isNotEmpty(sysRoleList)){
            List<Long> roleds = sysRoleList.stream().map(m -> m.getId()).collect(Collectors.toList());
            sysUser.setRoleIds(roleds);
        }

        return ResultBean.success(sysUser);
    }

    /**
     * 更新用户
     * @param sysUser
     * @return
     */
    @Override
    public ResultBean updateUser(SysUser sysUser) {
        //更新操作
        sysUser.setUpdatedTime(new Date());
        updateById(sysUser);

        return ResultBean.success(sysUser);
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @Override
    public ResultBean delete(Long[] ids) {

        //删除角色
        List<Long> idList = Arrays.asList(ids);
        removeByIds(idList);

        //删除用户角色关系
        sysUserMapper.deleteUserRoleByUserId(idList);

        return ResultBean.success();
    }

    /**
     * 分配角色
     * @param id
     * @param roleIds
     * @return
     */
    @Override
    public ResultBean role(Long id, Long[] roleIds) {

        //组装前端勾选的用户角色信息
        List<SysUserRole> userRoleList = new ArrayList<>();
        Arrays.stream(roleIds).forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(id);
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        });

        //删除原来的用户角色关系
        sysUserMapper.deleteUserRoleByUserId(Arrays.asList(id));

        //保存现在的用户角色关系
        if(CollectionUtils.isNotEmpty(userRoleList)){
            sysUserMapper.batcSaveUserRole(userRoleList);
        }

        // 删除权限缓存
        SysUser sysUser = getById(id);
        authorityService.clearUserAuthorityInfo(sysUser.getUsername());

        return ResultBean.success(roleIds);
    }

}