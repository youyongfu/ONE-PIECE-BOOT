package com.you.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.dto.SysMenuDto;
import com.you.entity.SysMenu;
import com.you.entity.SysUser;
import com.you.mapper.SysMenuMapper;
import com.you.service.AuthorityService;
import com.you.service.SysMenuService;
import com.you.service.SysUserService;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @Resource
    private SysUserService sysUserService;

    /**
     * 获取当前用户导航和权限信息
     * @param principal
     * @return
     */
    @Override
    public ResultBean nav(Principal principal) {
        //获取用户信息
        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        //获取权限信息
        String authorityInfo = authorityService.getUserAuthorityInfo(sysUser.getId());
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

        //获取导航信息
        List<SysMenu> sysMenuList = sysMenuMapper.getMenuByUserId(sysUser.getId());

        //转成树形结构
        List<SysMenu> menuTree= buildMenuTree(sysMenuList);

        return ResultBean.success(MapUtil.builder().put("menuList",convert(menuTree)).put("permList", authorityInfoArray).map());
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
        queryWrapper.orderByAsc("order_num");
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
    public ResultBean getChildrenList(String id) {

        //获取子菜单
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",id);
        queryWrapper.orderByAsc("order_num");
        List<SysMenu> list = sysMenuMapper.selectList(queryWrapper);

        //判断是否存在子菜单
        list.forEach(sysMenu -> {
            Boolean hasChildren = hasChildren(sysMenu.getId());
            sysMenu.setHasChildren(hasChildren);
        });
        return ResultBean.success(list);
    }

    /**
     * 获取菜单列表
     * @return
     */
    @Override
    public List<SysMenu> treeList() {
        List<SysMenu> allList = list();
        //根节点
        List<SysMenu> root = new ArrayList<SysMenu>();
        for (SysMenu nav : allList) {
            if (nav.getParentId().equals("0")){     //父节点是0的，为根节点。
                root.add(nav);
            }
        }

        //为根菜单设置子菜单，getClild是递归调用的
        for (SysMenu nav : root) {
            List<SysMenu> childList = getChild(nav.getId(), allList);
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
    public List<SysMenu> getChild(String id,List<SysMenu> allList){
        //子菜单
        List<SysMenu> childList = new ArrayList<SysMenu>();
        for (SysMenu nav : allList) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            if (nav.getParentId().equals(id)){
                childList.add(nav);
            }
        }
        //递归
        for (SysMenu nav : childList) {
            nav.setChildren(getChild(nav.getId(), allList));
        }
        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childList.size() == 0 ){
            return new ArrayList<SysMenu>();
        }
        return childList;
    }

    /**
     * 保存菜单
     * @param sysMenu
     * @return
     */
    @Override
    public ResultBean saveMenu(SysMenu sysMenu) {
        String menuId = UUID.randomUUID().toString().replaceAll("-","");
        sysMenu.setId(menuId);
        //未选择上级菜单，则默认为添加目录
        if(StringUtil.isNullOrEmpty(sysMenu.getParentId())){
            sysMenu.setParentId("0");
        }
        sysMenu.setCreatedTime(new Date());
        save(sysMenu);
        return ResultBean.success(sysMenu);
    }

    /**
     * 更新菜单
     * @param sysMenu
     * @return
     */
    @Override
    public ResultBean updateMenu(SysMenu sysMenu) {
        //更新操作
        sysMenu.setUpdatedTime(new Date());
        //未选择上级菜单，则默认为添加目录
        if(StringUtil.isNullOrEmpty(sysMenu.getParentId())){
            sysMenu.setParentId("0");
        }
        updateById(sysMenu);

        // 清除所有与该菜单相关的权限缓存
        authorityService.clearUserAuthorityInfoByMenuId(sysMenu.getId());

        return ResultBean.success(sysMenu);
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Override
    public ResultBean delete(String id) {
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
     * 判断是否存在子菜单
     * @param id
     * @return
     */
    private Boolean hasChildren(String id){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",id);
        Integer count = sysMenuMapper.selectCount(queryWrapper);
        return count > 0;
    }

    /**
     * 构建菜单树形数据
     * @return
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> sysMenuList){
        List<SysMenu> menuTree = new ArrayList<>();
        sysMenuList.forEach(sysMenu -> {
            //获取最外层父节点
            if(sysMenu.getParentId().equals("0")){
                menuTree.add(sysMenu);
            }

            //依次获取子节点
            sysMenuList.forEach(sysMenuChild -> {
                if(sysMenu.getId().equals(sysMenuChild.getParentId())){
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
