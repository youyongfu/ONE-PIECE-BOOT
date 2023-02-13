package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.dto.SysMenuDto;
import com.you.entity.SysMenu;

import java.util.List;

/**
 * 菜单服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenuDto> getCurrentUserNav(Long userId);
}
