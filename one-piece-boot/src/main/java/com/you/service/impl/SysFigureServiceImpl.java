package com.you.service.impl;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.constant.FigureConstant;
import com.you.constant.OssConstant;
import com.you.entity.*;
import com.you.mapper.SysFigureMapper;
import com.you.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    private SysClobContentService sysClobContentService;
    @Resource
    private SysFigureExperienceService sysFigureExperienceService;
    @Resource
    private SysFigureRelationService sysFigureRelationService;
    @Resource
    private SysFigureWarRecordService sysFigureWarRecordService;
    @Resource
    private SysFigureDevilnutService sysFigureDevilnutService;
    @Resource
    private SysFigureIslandsService sysFigureIslandsService;
    @Resource
    private SysFigureWeaponService sysFigureWeaponService;
    @Resource
    private SysUploadFileService sysUploadFileService;
    @Resource
    private SysUploadFileRecordService sysUploadFileRecordService;

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
            String sex = jsonObject.getString("sex");
            queryWrapper.eq(StrUtil.isNotBlank(sex), "sex", sex);
        }

        //分页插件
        Page<SysFigure> page= new Page(current,size);

        //分页获取数据
        Page<SysFigure> pageData = sysFigureMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 获取所有数据
     * @return
     */
    @Override
    public ResultBean getAll() {
        //条件构造器
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("created_time");
        return ResultBean.success(list(queryWrapper));
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

        //保存人物岛屿关系
        saveOrUpdateFigureIslands(sysFigure);

        //保存人物果实关系
        saveOrUpdateFigureDevilnut(sysFigure);

        //保存人物武器关系
        saveOrUpdateFigureWeapon(sysFigure,"save");

        //保存人物经历
        saveOrUpdateFigureExperience(sysFigure,"save");

        //保存人物人际关系
        saveOrUpdateFigureRelation(sysFigure,"save");

        //保存人物对战记录
        saveOrUpdateFigureWarRecord(sysFigure,"save");

        //保存组织内容信息
        saveOrUpdateFigureContent(sysFigure,"save");

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

        //获取人物经历
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("figure_id",id);
        sysFigure.setSysFigureExperienceList(sysFigureExperienceService.list(queryWrapper));

        //获取人物人际关系
        sysFigure.setSysFigureRelationList(sysFigureRelationService.list(queryWrapper));

        //获取人物对战记录
        sysFigure.setSysFigureWarRecordList(sysFigureWarRecordService.list(queryWrapper));

        map.put("figure",sysFigure);

        //获取霸气
        List<String> overbearingList = Arrays.asList(sysFigure.getOverbearing().split(","));
        map.put("overbearingList",overbearingList);

        //获取恶魔果实
        List<String> devilnutList = new ArrayList<>();
        List<SysFigureDevilnut> figureDevilnutByFigureList = sysFigureDevilnutService.list(queryWrapper);
        figureDevilnutByFigureList.forEach(figureDevilnutByFigure -> {
            String devilnutId = figureDevilnutByFigure.getDevilnutId();
            devilnutList.add(devilnutId);
        });
        map.put("devilnutList",devilnutList);

        //获取所属岛屿
        List<String> islandsList = new ArrayList<>();
        List<SysFigureIslands> figureIslandsList = sysFigureIslandsService.list(queryWrapper);
        figureIslandsList.forEach(figureIslands -> {
            String islandsId = figureIslands.getIslandsId();
            islandsList.add(islandsId);
        });
        map.put("islandsList",islandsList);

        //获取所属武器
        List<String> weaponList = new ArrayList<>();
        List<SysFigureWeapon> figureWeaponList = sysFigureWeaponService.list(queryWrapper);
        figureWeaponList.forEach(figureWeapon -> {
            String weaponId = figureWeapon.getWeaponId();
            weaponList.add(weaponId);
        });
        map.put("weaponList",weaponList);

        //获取人物文件信息
        List<SysUploadFile> fileList = sysUploadFileService.getFileInfo(OssConstant.FIGURE_TYPE,id);
        map.put("fileList",fileList);

        //获取人物内容信息
        List<SysClobContent> figureContentList = sysClobContentService.contentListByOwnerId(id);
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

        //更新人物岛屿关系
        saveOrUpdateFigureIslands(sysFigure);

        //更新人物果实关系
        saveOrUpdateFigureDevilnut(sysFigure);

        //更新人物武器关系
        saveOrUpdateFigureWeapon(sysFigure,"update");

        //更新人物经历
        saveOrUpdateFigureExperience(sysFigure,"update");

        //更新人际关系
        saveOrUpdateFigureRelation(sysFigure,"update");

        //更新对战记录
        saveOrUpdateFigureWarRecord(sysFigure,"update");

        //更新人物内容信息
        saveOrUpdateFigureContent(sysFigure,"update");

        //删除已保存的人物文件关系
        if(StringUtils.isNotBlank(sysFigure.getFileIds())){
            List<String> fileIds = Arrays.asList(sysFigure.getFileIds().split(","));
            sysUploadFileRecordService.deleteFileRecord(OssConstant.FIGURE_TYPE,fileIds);
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

        //删除人物岛屿关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("figure_id",id);
        sysFigureIslandsService.remove(queryWrapper);

        //删除人物果实关系
        sysFigureDevilnutService.remove(queryWrapper);

        //删除人物武器关系
        sysFigureWeaponService.remove(queryWrapper);

        //删除人物经历
        sysFigureExperienceService.remove(queryWrapper);

        //删除人际关系
        sysFigureRelationService.remove(queryWrapper);

        //删除对战记录
        sysFigureWarRecordService.remove(queryWrapper);

        //删除人物内容信息
        sysClobContentService.removeContentByOwnerId(id);

        return ResultBean.success();
    }

    /**
     * 保存/更新人物岛屿关系
     * @param sysFigure
     */
    private void saveOrUpdateFigureIslands(SysFigure sysFigure){

        //已保存的人物岛屿关系
        String figureId = sysFigure.getId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("figure_id",figureId);
        List<SysFigureIslands> sysFigureIslandsList = sysFigureIslandsService.list(queryWrapper);

        //页面勾选的岛屿
        String islandsIds = sysFigure.getIslandsIds();

        List<SysFigureIslands> sysFigureIslandsAddList = new ArrayList<>();
        if(StringUtils.isNotBlank(islandsIds) && CollectionUtils.isEmpty(sysFigureIslandsList)){
            //页面勾选岛屿但已保存的人物岛屿关系为空，则新增人物岛屿关系
            String[] islandsIdList = islandsIds.split(",");
            for (String islandsId:islandsIdList){
                SysFigureIslands sysFigureIslands = new SysFigureIslands();
                sysFigureIslands.setFigureId(figureId);
                sysFigureIslands.setIslandsId(islandsId);
                sysFigureIslandsAddList.add(sysFigureIslands);
            }
        }else if (StringUtils.isBlank(islandsIds) && CollectionUtils.isNotEmpty(sysFigureIslandsList)){
            //页面未勾选岛屿但有已保存的人物岛屿关系，则删除已保存的人物岛屿关系
            sysFigureIslandsService.remove(queryWrapper);
        }else if(StringUtils.isNotBlank(islandsIds) && CollectionUtils.isNotEmpty(sysFigureIslandsList)){
            //页面勾选岛屿且有已保存的人物岛屿关系
            List<String> islandsIdList = Arrays.asList(islandsIds.split(","));
            List<String> islandsIdListDb = sysFigureIslandsList.stream().map(sysFigureIslands -> sysFigureIslands.getIslandsId()).collect(Collectors.toList());

        }
        sysFigureIslandsService.saveBatch(sysFigureIslandsAddList);
    }

    /**
     * 保存/更新人物果实关系
     * @param sysFigure
     */
    private void saveOrUpdateFigureDevilnut(SysFigure sysFigure){

        //已保存的人物果实关系
        String figureId = sysFigure.getId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("figure_id",figureId);
        List<SysFigureDevilnut> sysFigureDevilnutList = sysFigureDevilnutService.list(queryWrapper);

        //页面勾选的果实
        String devilnutIds = sysFigure.getDevilnutIds();

        List<SysFigureDevilnut> sysFigureDevilnutAddList = new ArrayList<>();
        if(StringUtils.isNotBlank(devilnutIds) && CollectionUtils.isEmpty(sysFigureDevilnutList)){
            //页面勾选果实但已保存的人物果实关系为空，则新增人物果实关系
            String[] devilnuts = devilnutIds.split(",");
            for (String devilnutId:devilnuts){
                SysFigureDevilnut sysFigureDevilnut = new SysFigureDevilnut();
                sysFigureDevilnut.setFigureId(figureId);
                sysFigureDevilnut.setDevilnutId(devilnutId);
                sysFigureDevilnutAddList.add(sysFigureDevilnut);
            }
        }else if (StringUtils.isBlank(devilnutIds) && CollectionUtils.isNotEmpty(sysFigureDevilnutList)){
            //页面未勾选果实但有已保存的人物果实关系，则删除已保存的人物果实关系
            sysFigureDevilnutService.remove(queryWrapper);
        }else if(StringUtils.isNotBlank(devilnutIds) && CollectionUtils.isNotEmpty(sysFigureDevilnutList)){
            //页面勾选果实且有已保存的人物果实关系
            List<String> devilnutIdList = Arrays.asList(devilnutIds.split(","));
            List<String> devilnutIdListDb = sysFigureDevilnutList.stream().map(sysFigureDevilnut -> sysFigureDevilnut.getDevilnutId()).collect(Collectors.toList());

        }
        sysFigureDevilnutService.saveBatch(sysFigureDevilnutAddList);
    }

    /**
     * 保存/更新人物武器关系
     * @param sysFigure
     */
    private void saveOrUpdateFigureWeapon(SysFigure sysFigure,String type){
        String figureId = sysFigure.getId();
        String weaponIds = sysFigure.getWeaponIds();
        if("save".equals(type)){
            //保存
            if(StringUtils.isNotBlank(weaponIds)){
                String[] weaponIdList = weaponIds.split(",");
                List<SysFigureWeapon> sysFigureWeaponList = new ArrayList<>();
                for (String weaponId:weaponIdList){
                    SysFigureWeapon sysFigureWeapon = new SysFigureWeapon();
                    sysFigureWeapon.setFigureId(figureId);
                    sysFigureWeapon.setWeaponId(weaponId);
                    sysFigureWeaponList.add(sysFigureWeapon);
                }
                sysFigureWeaponService.saveBatch(sysFigureWeaponList);
            }
        }else {
            //更新
        }
    }

    /**
     * 保存/更新人物经历
     * @param sysFigure
     */
    private void saveOrUpdateFigureExperience(SysFigure sysFigure,String type) {
        String figureId = sysFigure.getId();
        if("save".equals(type)){
            //保存
            sysFigure.getSysFigureExperienceList().forEach(sysFigureExperience -> {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                sysFigureExperience.setId(id);
                sysFigureExperience.setFigureId(figureId);
            });
            sysFigureExperienceService.saveBatch(sysFigure.getSysFigureExperienceList());
        }else {
            //更新
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("figure_id",figureId);
            List<SysFigureExperience> sysFigureExperienceAddList = new ArrayList<>();
            List<SysFigureExperience> sysFigureExperienceUpdateList = new ArrayList<>();
            List<SysFigureExperience> sysFigureExperienceDeleteList = sysFigureExperienceService.list(queryWrapper);
            List<String> deleteIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(sysFigureExperienceDeleteList)){
                deleteIdList = sysFigureExperienceDeleteList.stream().map(m -> m.getId()).collect(Collectors.toList());
            }
            List<SysFigureExperience> sysFigureExperienceList = sysFigure.getSysFigureExperienceList();
            List<String> finalDeleteIdList = deleteIdList;
            sysFigureExperienceList.forEach(sysFigureExperience -> {
                if(StringUtils.isBlank(sysFigureExperience.getId())){            //新增
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    sysFigureExperience.setId(id);
                    sysFigureExperience.setFigureId(figureId);
                    sysFigureExperienceAddList.add(sysFigureExperience);
                }else {
                    sysFigureExperienceUpdateList.add(sysFigureExperience);       //更新
                    finalDeleteIdList.remove(sysFigureExperience.getId());    //删除
                }
            });
            sysFigureExperienceService.saveBatch(sysFigureExperienceAddList);
            sysFigureExperienceService.updateBatchById(sysFigureExperienceUpdateList);
            sysFigureExperienceService.removeByIds(finalDeleteIdList);
        }
    }

    /**
     * 保存/更新人物人际关系
     * @param sysFigure
     */
    private void saveOrUpdateFigureRelation(SysFigure sysFigure,String type) {
        String figureId = sysFigure.getId();
        if("save".equals(type)){
            //保存
            sysFigure.getSysFigureRelationList().forEach(sysFigureRelation -> {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                sysFigureRelation.setId(id);
                sysFigureRelation.setFigureId(figureId);
            });
            sysFigureRelationService.saveBatch(sysFigure.getSysFigureRelationList());
        }else {
            //更新
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("figure_id",figureId);
            List<SysFigureRelation> sysFigureRelationAddList = new ArrayList<>();
            List<SysFigureRelation> sysFigureRelationUpdateList = new ArrayList<>();
            List<SysFigureRelation> sysFigureRelationDeleteList = sysFigureRelationService.list(queryWrapper);
            List<String> deleteRelationIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(sysFigureRelationDeleteList)){
                deleteRelationIdList = sysFigureRelationDeleteList.stream().map(m -> m.getId()).collect(Collectors.toList());
            }
            List<SysFigureRelation> sysFigureRelationList = sysFigure.getSysFigureRelationList();
            List<String> finalDeleteRelationIdList = deleteRelationIdList;
            sysFigureRelationList.forEach(sysFigureRelation -> {
                if(StringUtils.isBlank(sysFigureRelation.getId())){            //新增
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    sysFigureRelation.setId(id);
                    sysFigureRelation.setFigureId(figureId);
                    sysFigureRelationAddList.add(sysFigureRelation);
                }else {
                    sysFigureRelationUpdateList.add(sysFigureRelation);       //更新
                    finalDeleteRelationIdList.remove(sysFigureRelation.getId());    //删除
                }
            });
            sysFigureRelationService.saveBatch(sysFigureRelationAddList);
            sysFigureRelationService.updateBatchById(sysFigureRelationUpdateList);
            sysFigureRelationService.removeByIds(finalDeleteRelationIdList);
        }
    }

    /**
     * 保存/更新人物对战记录
     * @param sysFigure
     */
    private void saveOrUpdateFigureWarRecord(SysFigure sysFigure,String type) {
        String figureId = sysFigure.getId();
        if("save".equals(type)){
            //保存
            sysFigure.getSysFigureWarRecordList().forEach(sysFigureWarRecord -> {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                sysFigureWarRecord.setId(id);
                sysFigureWarRecord.setFigureId(figureId);
            });
            sysFigureWarRecordService.saveBatch(sysFigure.getSysFigureWarRecordList());
        }else {
            //更新
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("figure_id",figureId);
            List<SysFigureWarRecord> sysFigureWarRecordAddList = new ArrayList<>();
            List<SysFigureWarRecord> sysFigureWarRecordUpdateList = new ArrayList<>();
            List<SysFigureWarRecord> sysFigureWarRecordDeleteList = sysFigureWarRecordService.list(queryWrapper);
            List<String> deleteWarIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(sysFigureWarRecordDeleteList)){
                deleteWarIdList = sysFigureWarRecordDeleteList.stream().map(m -> m.getId()).collect(Collectors.toList());
            }
            List<SysFigureWarRecord> sysFigureWarRecordList = sysFigure.getSysFigureWarRecordList();
            List<String> finalDeleteWarIdList = deleteWarIdList;
            sysFigureWarRecordList.forEach(sysFigureWarRecord -> {
                if(StringUtils.isBlank(sysFigureWarRecord.getId())){            //新增
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    sysFigureWarRecord.setId(id);
                    sysFigureWarRecord.setFigureId(figureId);
                    sysFigureWarRecordAddList.add(sysFigureWarRecord);
                }else {
                    sysFigureWarRecordUpdateList.add(sysFigureWarRecord);       //更新
                    finalDeleteWarIdList.remove(sysFigureWarRecord.getId());    //删除
                }
            });
            sysFigureWarRecordService.saveBatch(sysFigureWarRecordAddList);
            sysFigureWarRecordService.updateBatchById(sysFigureWarRecordUpdateList);
            sysFigureWarRecordService.removeByIds(finalDeleteWarIdList);
        }
    }

    /**
     * 保存/更新人物内容
     * @param sysFigure
     */
    private void saveOrUpdateFigureContent(SysFigure sysFigure,String type) {
        String figureId = sysFigure.getId();
        if("save".equals(type)){
            //保存
            List<SysClobContent> contentList = new ArrayList<>();
            contentList.add(sysClobContentService.assemblyData(figureId,sysFigure.getBackground(), FigureConstant.BACKGROUND_TYPE));
            contentList.add(sysClobContentService.assemblyData(figureId,sysFigure.getImage(), FigureConstant.IMAGE_TYPE));
            contentList.add(sysClobContentService.assemblyData(figureId,sysFigure.getLife(), FigureConstant.LIFE_TYPE));
            contentList.add(sysClobContentService.assemblyData(figureId,sysFigure.getAbility(), FigureConstant.ABILITY_TYPE));
            contentList.add(sysClobContentService.assemblyData(figureId,sysFigure.getExperience(), FigureConstant.EXPERIENCE_TYPE));
            sysClobContentService.saveBatch(contentList);
        }else {
            //更新
            List<SysClobContent> figureContentList = sysClobContentService.contentListByOwnerId(figureId);
            figureContentList.forEach(content ->{
                if(FigureConstant.BACKGROUND_TYPE.equals(content.getType())){
                    content.setContent(sysFigure.getBackground());
                }else if(FigureConstant.IMAGE_TYPE.equals(content.getType())){
                    content.setContent(sysFigure.getImage());
                }else if(FigureConstant.LIFE_TYPE.equals(content.getType())){
                    content.setContent(sysFigure.getLife());
                }else if(FigureConstant.ABILITY_TYPE.equals(content.getType())){
                    content.setContent(sysFigure.getAbility());
                }else if(FigureConstant.EXPERIENCE_TYPE.equals(content.getType())){
                    content.setContent(sysFigure.getExperience());
                }
            });
            sysClobContentService.updateBatchById(figureContentList);
        }
    }

}
