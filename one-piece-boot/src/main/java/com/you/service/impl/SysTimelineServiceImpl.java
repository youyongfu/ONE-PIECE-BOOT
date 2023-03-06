package com.you.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysTimeline;
import com.you.mapper.SysTimelineMapper;
import com.you.service.SysTimelineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 时间线管理服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysTimelineServiceImpl extends ServiceImpl<SysTimelineMapper, SysTimeline> implements SysTimelineService {

    @Resource
    private SysTimelineMapper sysTimelineMapper;

    /**
     * 分页获取列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public ResultBean listPage(Integer current, Integer size) {

        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("created_time");

        //分页插件
        Page<SysTimeline> page= new Page(current,size);

        //分页获取数据
        Page<SysTimeline> pageData = sysTimelineMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

}
