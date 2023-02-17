package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysRole;

/**
 * 角色服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysRoleService extends IService<SysRole> {

    ResultBean delete(Long id);

    ResultBean listPage(String keyword, Integer current, Integer size);
}
