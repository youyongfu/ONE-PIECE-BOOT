package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysEpisodes;

/**
 * 剧集服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysEpisodesService extends IService<SysEpisodes> {

    ResultBean listPage(String keyword, Integer current, Integer size);

    ResultBean getAll();

    ResultBean saveEpisodes(SysEpisodes sysEpisodes);

    ResultBean getInfoById(String id);

    ResultBean updateEpisodes(SysEpisodes sysEpisodes);

    ResultBean deleteEpisodes(String id);
}
