package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysTimeline;

/**
 * 时间线管理服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysTimelineService extends IService<SysTimeline> {

    ResultBean listPage(Integer current, Integer size);
}
