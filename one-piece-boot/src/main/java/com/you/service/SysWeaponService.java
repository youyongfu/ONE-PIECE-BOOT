package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysWeapon;

/**
 * 武器大全服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysWeaponService extends IService<SysWeapon> {

    ResultBean listPage(String keyword, Integer current, Integer size);

    ResultBean getInfoById(Long id);

}
