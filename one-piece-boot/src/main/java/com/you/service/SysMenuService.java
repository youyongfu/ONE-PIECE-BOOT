package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
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

    List<SysMenu> treeList();

    ResultBean delete(Long id);

    ResultBean listPage(Integer current, Integer size);

    ResultBean getChildrenList(Long id);
}
