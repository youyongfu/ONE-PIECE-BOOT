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
import com.you.constant.ChapterConstant;
import com.you.constant.CommonConstant;
import com.you.entity.SysChapter;
import com.you.entity.SysChapterSections;
import com.you.entity.SysClobContent;
import com.you.mapper.SysChapterMapper;
import com.you.service.SysChapterSectionsService;
import com.you.service.SysChapterService;
import com.you.service.SysClobContentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 篇章服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysChapterServiceImpl extends ServiceImpl<SysChapterMapper, SysChapter> implements SysChapterService {

    @Resource
    private SysChapterMapper sysChapterMapper;
    @Resource
    private SysChapterSectionsService sysChapterSectionsService;
    @Resource
    private SysClobContentService sysClobContentService;

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
        Page<SysChapter> page= new Page(current,size);

        //分页获取数据
        Page<SysChapter> pageData = sysChapterMapper.selectPage(page, queryWrapper);

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

        return ResultBean.success(sysChapterMapper.selectList(queryWrapper));
    }

    /**
     * 新增
     * @param sysChapter
     * @return
     */
    @Override
    public ResultBean saveChapter(SysChapter sysChapter) {
        //保存剧集信息
        String chapterId = UUID.randomUUID().toString().replaceAll("-","");
        sysChapter.setId(chapterId);
        sysChapter.setCreatedTime(new Date());
        save(sysChapter);

        //保存篇章内容信息
        saveOrUpdateCotent(sysChapter, CommonConstant.SAVE_OPERATE);

        //保存章节信息
        saveOrUpdateChapterSections(sysChapter, CommonConstant.SAVE_OPERATE);

        return ResultBean.success(sysChapter);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(String id) {
        MapBuilder<Object, Object> map = MapUtil.builder();

        //获取剧集信息
        SysChapter sysChapter = getById(id);

        //获取剧集登场角色
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("chapter_id",id);
        sysChapter.setSysChapterSectionsList(sysChapterSectionsService.list(queryWrapper));

        map.put("chapter",sysChapter);

        //获取果实内容信息
        List<SysClobContent> devilnutContentList = sysClobContentService.contentListByOwnerId(id);
        devilnutContentList.forEach(conten ->{
            map.put(conten.getType(),conten.getContent());
        });

        return ResultBean.success(map.build());
    }

    /**
     * 更新
     * @param sysChapter
     * @return
     */
    @Override
    public ResultBean updateChapter(SysChapter sysChapter) {
        //更新操作
        sysChapter.setUpdatedTime(new Date());
        updateById(sysChapter);

        //保存篇章内容信息
        saveOrUpdateCotent(sysChapter, CommonConstant.UPDATE_OPERATE);

        //更新剧集登场角色
        saveOrUpdateChapterSections(sysChapter, CommonConstant.UPDATE_OPERATE);

        return ResultBean.success(sysChapter);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public ResultBean deleteChapter(String id) {
        //删除
        removeById(id);

        //删除剧集登场角色
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("chapter_id",id);
        sysChapterSectionsService.remove(queryWrapper);

        //删除篇章内容信息
        sysClobContentService.removeContentByOwnerId(id);

        return ResultBean.success();
    }

    /**
     * 保存、更新篇章内容
     * @param sysChapter
     */
    private void saveOrUpdateCotent(SysChapter sysChapter, String type) {
        String chapterId = sysChapter.getId();
        if(CommonConstant.SAVE_OPERATE.equals(type)){
            //保存
            List<SysClobContent> contentList = new ArrayList<>();
            contentList.add(sysClobContentService.assemblyData(chapterId,sysChapter.getContent(), ChapterConstant.CONTENT_TYPE));
            sysClobContentService.saveBatch(contentList);
        }else {
            //更新
            List<SysClobContent> chapterContentList = sysClobContentService.contentListByOwnerId(chapterId);
            chapterContentList.forEach(content ->{
                if(ChapterConstant.CONTENT_TYPE.equals(content.getType())){
                    content.setContent(sysChapter.getContent());
                }
            });
            sysClobContentService.updateBatchById(chapterContentList);
        }

    }

    /**
     * 保存/更新章节信息
     * @param sysChapter
     */
    private void saveOrUpdateChapterSections(SysChapter sysChapter, String type) {
        String chapterId = sysChapter.getId();
        if(CommonConstant.SAVE_OPERATE.equals(type)){
            //保存
            sysChapter.getSysChapterSectionsList().forEach(sysChapterSections -> {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                sysChapterSections.setId(id);
                sysChapterSections.setChapterId(chapterId);
            });
            sysChapterSectionsService.saveBatch(sysChapter.getSysChapterSectionsList());
        }else {
            //更新
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("chapter_id",chapterId);
            List<SysChapterSections> addList = new ArrayList<>();
            List<SysChapterSections> updateList = new ArrayList<>();
            List<SysChapterSections> deleteList = sysChapterSectionsService.list(queryWrapper);
            List<String> deleteIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(deleteList)){
                deleteIdList = deleteList.stream().map(m -> m.getId()).collect(Collectors.toList());
            }
            List<SysChapterSections> sysEpisodesCharacterList = sysChapter.getSysChapterSectionsList();
            List<String> finalDeleteIdList = deleteIdList;
            sysEpisodesCharacterList.forEach(sysEpisodesCharacter -> {
                if(StringUtils.isBlank(sysEpisodesCharacter.getId())){            //新增
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    sysEpisodesCharacter.setId(id);
                    sysEpisodesCharacter.setChapterId(chapterId);
                    addList.add(sysEpisodesCharacter);
                }else {
                    updateList.add(sysEpisodesCharacter);       //更新
                    finalDeleteIdList.remove(sysEpisodesCharacter.getId());    //删除
                }
            });
            sysChapterSectionsService.saveBatch(addList);
            sysChapterSectionsService.updateBatchById(updateList);
            sysChapterSectionsService.removeByIds(finalDeleteIdList);
        }
    }

}
