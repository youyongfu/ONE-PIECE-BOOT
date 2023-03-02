package com.you.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysDict;

import java.util.List;

/**
 * 数据字典服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysDictService extends IService<SysDict> {

    Page<SysDict> listPage(String keyword, Integer current, Integer size);

    List<SysDict> getChildrenList(Long id);

    List<SysDict> treeList();

    ResultBean delete(Long id);

}
