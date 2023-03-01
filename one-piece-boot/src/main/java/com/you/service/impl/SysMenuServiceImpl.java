package com.you.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.dto.SysMenuDto;
import com.you.entity.SysMenu;
import com.you.mapper.SysMenuMapper;
import com.you.service.AuthorityService;
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
    private AuthorityService authorityService;
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
     * 分页获取一级菜单列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public ResultBean listPage(Integer current, Integer size) {

        //获取一级菜单列表
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",0L);
        Page<SysMenu> page= new Page(current,size);
        Page<SysMenu> pageData = sysMenuMapper.selectPage(page, queryWrapper);

        //判断是否存在子菜单
        pageData.getRecords().forEach(sysMenu -> {
            Boolean hasChildren = hasChildren(sysMenu.getId());
            sysMenu.setHasChildren(hasChildren);
        });

        return ResultBean.success(pageData);
    }

    /**
     * 获取子菜单列表
     * @param id
     * @return
     */
    @Override
    public ResultBean getChildrenList(Long id) {

        //获取子菜单
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",id);
        List<SysMenu> list = sysMenuMapper.selectList(queryWrapper);

        //判断是否存在子菜单
        list.forEach(sysMenu -> {
            Boolean hasChildren = hasChildren(sysMenu.getId());
            sysMenu.setHasChildren(hasChildren);
        });
        return ResultBean.success(list);
    }

    /**
     * 判断是否存在子菜单
     * @param id
     * @return
     */
    private Boolean hasChildren(Long id){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",id);
        Integer count = sysMenuMapper.selectCount(queryWrapper);
        return count > 0;
    }

    /**
     * 获取菜单列表
     * @return
     */
    @Override
    public List<SysMenu> treeList() {
        return buildMenuTree(list());
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Override
    public ResultBean delete(Long id) {
        //判断该菜单是否存在子菜单，存在无法删除
        int count = count(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if (count > 0) {
            return ResultBean.fail("请先删除子菜单");
        }

        // 清除所有与该菜单相关的权限缓存
        authorityService.clearUserAuthorityInfoByMenuId(id);

        //删除菜单
        removeById(id);

        //删除角色菜单关系
        sysMenuMapper.deleteRoleMenuByMenuId(id);

        return ResultBean.success();
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
