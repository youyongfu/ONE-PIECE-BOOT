package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysDevilnut;

/**
 * 果实图鉴服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysDevilnutService extends IService<SysDevilnut> {

    ResultBean listPage(String keyword, Integer current, Integer size);

    ResultBean getAll();

    ResultBean saveDevilnut(SysDevilnut sysDevilnut);

    ResultBean getInfoById(String id);

    ResultBean updateDevilnut(SysDevilnut sysDevilnut);

    ResultBean deleteDevilnut(String id);
}
