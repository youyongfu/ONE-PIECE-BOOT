package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysOrganization;

import java.util.List;

/**
 * 组织管理服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysOrganizationService extends IService<SysOrganization> {

    ResultBean listPage(String keyword, Integer current, Integer size);

    List<SysOrganization> treeList();

    ResultBean getAll();

    ResultBean saveOrganization(SysOrganization sysOrganization);

    ResultBean getInfoById(String id);

    ResultBean updateOrganization(SysOrganization sysOrganization);

    ResultBean delete(String id);
}
