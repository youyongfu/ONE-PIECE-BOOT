package com.you.service.impl;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.constant.FigureConstant;
import com.you.constant.OssConstant;
import com.you.entity.SysFigure;
import com.you.entity.SysFigureContent;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysFigureMapper;
import com.you.service.SysFigureContentService;
import com.you.service.SysFigureService;
import com.you.service.SysUploadFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 人物大全服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysFigureServiceImpl extends ServiceImpl<SysFigureMapper, SysFigure> implements SysFigureService {

    @Resource
    private SysFigureMapper sysFigureMapper;
    @Resource
    private SysFigureContentService sysFigureContentService;
    @Resource
    private SysUploadFileService sysUploadFileService;

    /**
     * 分页获取列表
     * @param current
     * @param size
     * @return
     */
    @Override
    public ResultBean listPage(String keyword, Integer current, Integer size) {

        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("created_time");

        //添加条件
        JSONObject jsonObject = JSONObject.parseObject(keyword);
        if(jsonObject.size() > 0){
            String name = jsonObject.getString("name");
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }

        //分页插件
        Page<SysFigure> page= new Page(current,size);

        //分页获取数据
        Page<SysFigure> pageData = sysFigureMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 新增
     * @param sysFigure
     * @return
     */
    @Override
    public ResultBean saveFigure(SysFigure sysFigure) {
        //保存人物信息
        String figureId = UUID.randomUUID().toString().replaceAll("-","");
        sysFigure.setId(figureId);
        sysFigure.setCreatedTime(new Date());
        save(sysFigure);

        //保存组织内容信息
        List<SysFigureContent> contentList = new ArrayList<>();
        contentList.add(assemblyData(figureId,sysFigure.getBackground(), FigureConstant.BACKGROUND_TYPE));
        contentList.add(assemblyData(figureId,sysFigure.getImage(), FigureConstant.IMAGE_TYPE));
        contentList.add(assemblyData(figureId,sysFigure.getLife(), FigureConstant.LIFE_TYPE));
        contentList.add(assemblyData(figureId,sysFigure.getAbility(), FigureConstant.ABILITY_TYPE));
        contentList.add(assemblyData(figureId,sysFigure.getExperience(), FigureConstant.EXPERIENCE_TYPE));
        contentList.add(assemblyData(figureId,sysFigure.getInterpersonalRelationship(), FigureConstant.INTERPERSONAL_RELATIONSHIP_TYPE));
        contentList.add(assemblyData(figureId,sysFigure.getWarRecord(), FigureConstant.WAR_RECORD_TYPE));
        sysFigureContentService.saveBatch(contentList);

        return ResultBean.success(sysFigure);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(String id) {
        MapBuilder<Object, Object> map = MapUtil.builder();

        //获取人物信息
        SysFigure sysFigure = getById(id);
        map.put("figure",sysFigure);

        //获取人物文件信息
        List<SysUploadFile> fileList = sysUploadFileService.getFileRecord(OssConstant.FIGURE_TYPE,sysFigure.getId());
        map.put("fileList",fileList);

        //获取人物内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("figure_id",id);
        List<SysFigureContent> figureContentList = sysFigureContentService.list(queryWrapper);
        figureContentList.forEach(conten ->{
            map.put(conten.getType(),conten.getContent());
        });

        return ResultBean.success(map.build());
    }

    /**
     * 更新
     * @param sysFigure
     * @return
     */
    @Override
    public ResultBean updateFigure(SysFigure sysFigure) {
        //更新操作
        String figureId = sysFigure.getId();
        sysFigure.setUpdatedTime(new Date());
        updateById(sysFigure);

        //更新人物内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("figure_id",figureId);
        List<SysFigureContent> figureContentList = sysFigureContentService.list(queryWrapper);
        figureContentList.forEach(content ->{
            if(FigureConstant.BACKGROUND_TYPE.equals(content.getType())){
                content.setContent(sysFigure.getBackground());
            }else if(FigureConstant.IMAGE_TYPE.equals(content.getType())){
                content.setContent(sysFigure.getImage());
            }else if(FigureConstant.LIFE_TYPE.equals(content.getType())){
                content.setContent(sysFigure.getLife());
            }else if(FigureConstant.ABILITY_TYPE.equals(content.getType())){
                content.setContent(sysFigure.getAbility());
            }else if(FigureConstant.INTERPERSONAL_RELATIONSHIP_TYPE.equals(content.getType())){
                content.setContent(sysFigure.getInterpersonalRelationship());
            }else if(FigureConstant.WAR_RECORD_TYPE.equals(content.getType())){
                content.setContent(sysFigure.getWarRecord());
            }else if(FigureConstant.EXPERIENCE_TYPE.equals(content.getType())){
                content.setContent(sysFigure.getExperience());
            }
        });
        sysFigureContentService.updateBatchById(figureContentList);


        //删除已保存的人物文件关系
        if(StringUtils.isNotBlank(sysFigure.getFileIds())){
            List<String> fileIds = Arrays.asList(sysFigure.getFileIds().split(","));
            sysUploadFileService.deleteFileRecord(OssConstant.FIGURE_TYPE,fileIds);
        }

        return ResultBean.success(sysFigure);
    }

    /**
     * 根据id删除人物
     * @param id
     * @return
     */
    @Override
    public ResultBean deleteFigure(String id) {
        //删除人物
        removeById(id);

        //删除人物内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("figure_id",id);
        sysFigureContentService.remove(queryWrapper);

        return ResultBean.success();
    }

    /**
     * 组装内容数据
     * @param figureId
     * @param content
     * @param type
     * @return
     */
    private SysFigureContent assemblyData(String figureId,String content,String type){
        SysFigureContent sysFigureContent = new SysFigureContent();
        sysFigureContent.setId(UUID.randomUUID().toString().replaceAll("-",""));
        sysFigureContent.setFigureId(figureId);
        sysFigureContent.setContent(content);
        sysFigureContent.setType(type);
        return sysFigureContent;
    }
}
