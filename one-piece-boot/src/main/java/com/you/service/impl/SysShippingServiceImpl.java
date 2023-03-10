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
import com.you.constant.OssConstant;
import com.you.constant.ShippingConstant;
import com.you.entity.SysShipping;
import com.you.entity.SysShippingContent;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysShippingMapper;
import com.you.service.SysShippingContentService;
import com.you.service.SysShippingService;
import com.you.service.SysUploadFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 船只管理服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysShippingServiceImpl extends ServiceImpl<SysShippingMapper, SysShipping> implements SysShippingService {

    @Resource
    private SysShippingMapper sysShippingMapper;
    @Resource
    private SysShippingContentService sysShippingContentService;
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
        Page<SysShipping> page= new Page(current,size);

        //分页获取数据
        Page<SysShipping> pageData = sysShippingMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 新增
     * @param sysShipping
     * @return
     */
    @Override
    public ResultBean saveShipping(SysShipping sysShipping) {
        //保存船只信息
        String shippingId = UUID.randomUUID().toString().replaceAll("-","");
        sysShipping.setId(shippingId);
        sysShipping.setCreatedTime(new Date());
        save(sysShipping);

        //保存组织内容信息
        List<SysShippingContent> contentList = new ArrayList<>();
        contentList.add(assemblyData(shippingId,sysShipping.getBackground(), ShippingConstant.BACKGROUND_TYPE));
        contentList.add(assemblyData(shippingId,sysShipping.getAppearance(), ShippingConstant.APPEARANCE_TYPE));
        contentList.add(assemblyData(shippingId,sysShipping.getFunction(), ShippingConstant.FUNCTION_TYPE));
        contentList.add(assemblyData(shippingId,sysShipping.getExperience(), ShippingConstant.EXPERIENCE_TYPE));
        sysShippingContentService.saveBatch(contentList);

        return ResultBean.success(sysShipping);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(String id) {
        MapBuilder<Object, Object> map = MapUtil.builder();

        //获取船只信息
        SysShipping sysShipping = getById(id);
        map.put("shipping",sysShipping);

        //获取上传文件信息
        List<SysUploadFile> fileList = sysUploadFileService.getFileRecord(OssConstant.SHIPPING_TYPE,sysShipping.getId());
        map.put("fileList",fileList);

        //获取组织内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("shipping_id",id);
        List<SysShippingContent> shippingContentList = sysShippingContentService.list(queryWrapper);
        shippingContentList.forEach(conten ->{
            map.put(conten.getType(),conten.getContent());
        });

        return ResultBean.success(map.build());
    }

    /**
     * 更新
     * @param sysShipping
     * @return
     */
    @Override
    public ResultBean updateShipping(SysShipping sysShipping) {
        //更新操作
        String shippingId = sysShipping.getId();
        sysShipping.setUpdatedTime(new Date());
        updateById(sysShipping);

        //更新船只内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("shipping_id",shippingId);
        List<SysShippingContent> shippingContentList = sysShippingContentService.list(queryWrapper);
        shippingContentList.forEach(content ->{
            if(ShippingConstant.BACKGROUND_TYPE.equals(content.getType())){
                content.setContent(sysShipping.getBackground());
            }else if(ShippingConstant.APPEARANCE_TYPE.equals(content.getType())){
                content.setContent(sysShipping.getAppearance());
            }else if(ShippingConstant.FUNCTION_TYPE.equals(content.getType())){
                content.setContent(sysShipping.getFunction());
            }else if(ShippingConstant.EXPERIENCE_TYPE.equals(content.getType())){
                content.setContent(sysShipping.getExperience());
            }
        });
        sysShippingContentService.updateBatchById(shippingContentList);


        //删除已保存的船只文件关系
        if(StringUtils.isNotBlank(sysShipping.getFileIds())){
            List<String> fileIds = Arrays.asList(sysShipping.getFileIds().split(","));
            sysUploadFileService.deleteFileRecord(OssConstant.SHIPPING_TYPE,fileIds);
        }

        return ResultBean.success(sysShipping);
    }

    /**
     * 根据id删除船只
     * @param id
     * @return
     */
    @Override
    public ResultBean deleteShipping(String id) {
        //删除船只
        removeById(id);

        //删除船只内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("shipping_id",id);
        sysShippingContentService.remove(queryWrapper);

        return ResultBean.success();
    }

    /**
     * 组装内容数据
     * @param shippingId
     * @param content
     * @param type
     * @return
     */
    private SysShippingContent assemblyData(String shippingId, String content, String type){
        SysShippingContent sysShippingContent = new SysShippingContent();
        sysShippingContent.setId(UUID.randomUUID().toString().replaceAll("-",""));
        sysShippingContent.setShippingId(shippingId);
        sysShippingContent.setContent(content);
        sysShippingContent.setType(type);
        return sysShippingContent;
    }
}
