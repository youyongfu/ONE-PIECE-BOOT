package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysIslands;

import java.util.List;

/**
 * 岛屿管理服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysIslandsService extends IService<SysIslands> {

    ResultBean listPage(String keyword, Integer current, Integer size);

    ResultBean getInfoById(Long id);

    List<SysIslands> treeList();
}
