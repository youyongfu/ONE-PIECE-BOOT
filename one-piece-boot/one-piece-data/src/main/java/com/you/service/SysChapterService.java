package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.common.ResultBean;
import com.you.entity.SysChapter;

/**
 * 篇章服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysChapterService extends IService<SysChapter> {

    ResultBean listPage(String keyword, Integer current, Integer size);

    ResultBean getAll();

    ResultBean saveChapter(SysChapter sysChapter);

    ResultBean getInfoById(String id);

    ResultBean updateChapter(SysChapter sysChapter);

    ResultBean deleteChapter(String id);
}
