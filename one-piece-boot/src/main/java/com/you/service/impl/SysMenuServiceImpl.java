package com.you.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.dto.SysMenuDto;
import com.you.entity.SysMenu;
import com.you.mapper.SysMenuMapper;
import com.you.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    /**
     * 获取当前用户导航信息
     * @return
     */
    @Override
    public List<SysMenuDto> getCurrentUserNav(Long userId) {
        //获取当前用户导航信息
        List<SysMenu> sysMenuList = sysMenuMapper.getMenuByUserId(userId);

        //转成树形结构
        List<SysMenu> menuTree= buildMenuTree(sysMenuList);

        return convert(menuTree);
    }

    /**
     * 获取当前用户菜单列表
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> list(Long userId) {
        //获取当前用户导航信息
        List<SysMenu> sysMenuList = sysMenuMapper.getMenuByUserId(userId);

        //转成树形结构
        List<SysMenu> menuTree= buildMenuTree(sysMenuList);

        return menuTree;
    }

    /**
     * 构建菜单树形数据
     * @return
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> sysMenuList){
        List<SysMenu> menuTree = new ArrayList<>();
        sysMenuList.forEach(sysMenu -> {
            //获取最外层父节点
            if(sysMenu.getParentId() == 0L){
                menuTree.add(sysMenu);
            }

            //依次获取子节点
            sysMenuList.forEach(sysMenuChild -> {
                if(sysMenu.getId() == sysMenuChild.getParentId()){
                    sysMenu.getChildren().add(sysMenuChild);
                }
            });
        });
        return  menuTree;
    }

    /**
     * 实体转DTO
     * @param menuTree
     * @return
     */
    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();
        menuTree.forEach(m -> {
            SysMenuDto dto = new SysMenuDto();
            dto.setId(m.getId());
            dto.setName(m.getName());
            dto.setIcon(m.getIcon());
            dto.setPath(m.getPath());
            dto.setPerms(m.getPerms());
            dto.setComponent(m.getComponent());

            if (m.getChildren().size() > 0) {
                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }
            menuDtos.add(dto);
        });
        return menuDtos;
    }
}
