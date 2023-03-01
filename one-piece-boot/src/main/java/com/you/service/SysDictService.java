package com.you.service;

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

    List<SysDict> treeList();

    ResultBean delete(Long id);

    ResultBean listPage(String keyword,Integer current, Integer size);

    ResultBean getChildrenList(Long id);
}
