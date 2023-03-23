package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.dto.PasswordDto;
import com.you.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

/**
 * 用户服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/6
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    ResultBean userInfo(Principal principal);

    ResultBean updatePassword(PasswordDto passwordDto, Principal principal);

    ResultBean rePass(Long userId);

    ResultBean uploadAvatar(MultipartFile file, String name,String type);

    ResultBean listPage(String keyword, Integer current, Integer size);

    ResultBean saveUser(SysUser sysUser);

    ResultBean getInfoById(Long id);

    ResultBean updateUser(SysUser sysUser);

    ResultBean delete(Long[] ids);

    ResultBean role(Long id, Long[] roleIds);

}
