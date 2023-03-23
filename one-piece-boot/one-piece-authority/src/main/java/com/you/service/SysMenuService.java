package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysMenu;

import java.security.Principal;
import java.util.List;

/**
 * 菜单服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysMenuService extends IService<SysMenu> {

    ResultBean nav(Principal principal);

    ResultBean listPage(Integer current, Integer size);

    ResultBean getChildrenList(Long id);

    List<SysMenu> treeList();

    ResultBean saveMenu(SysMenu sysMenu);

    ResultBean updateMenu(SysMenu sysMenu);

    ResultBean delete(Long id);

}
