package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysFigure;

/**
 * 人物大全服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysFigureService extends IService<SysFigure> {

    ResultBean listPage(String keyword, Integer current, Integer size);

    ResultBean saveFigure(SysFigure sysFigure);

    ResultBean getInfoById(String id);

    ResultBean updateFigure(SysFigure sysFigure);

    ResultBean deleteFigure(String id);
}
