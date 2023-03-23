package com.you.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.entity.SysClobContent;
import com.you.mapper.SysClobContentMapper;
import com.you.service.SysClobContentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 人物内容服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysClobContentServiceImpl extends ServiceImpl<SysClobContentMapper, SysClobContent> implements SysClobContentService {

    /**
     * 组装内容数据
     * @param ownerId
     * @param content
     * @param type
     * @return
     */
    @Override
    public SysClobContent assemblyData(String ownerId, String content, String type){
        SysClobContent sysClobContent = new SysClobContent();
        sysClobContent.setId(UUID.randomUUID().toString().replaceAll("-",""));
        sysClobContent.setOwnerId(ownerId);
        sysClobContent.setContent(content);
        sysClobContent.setType(type);
        return sysClobContent;
    }

    /**
     * 根据ownerId获取内容
     * @param ownerId
     * @return
     */
    @Override
    public List<SysClobContent> contentListByOwnerId(String ownerId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("owner_id",ownerId);
        return list(queryWrapper);
    }

    /**
     * 根据ownerId删除内容
     * @param ownerId
     */
    @Override
    public void removeContentByOwnerId(String ownerId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("owner_id",ownerId);
        remove(queryWrapper);
    }

}
