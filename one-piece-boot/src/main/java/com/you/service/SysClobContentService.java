package com.you.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.you.entity.SysClobContent;

import java.util.List;

/**
 * 大字段内容服务类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
public interface SysClobContentService extends IService<SysClobContent> {

    SysClobContent assemblyData(String ownerId, String content, String type);

    List<SysClobContent> contentListByOwnerId(String ownerId);

    void removeContentByOwnerId(String id);
}
