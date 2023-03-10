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
import com.you.constant.WeaponConstant;
import com.you.entity.SysUploadFile;
import com.you.entity.SysWeapon;
import com.you.entity.SysWeaponContent;
import com.you.mapper.SysWeaponMapper;
import com.you.service.SysUploadFileService;
import com.you.service.SysWeaponContentService;
import com.you.service.SysWeaponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 武器大全服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysWeaponServiceImpl extends ServiceImpl<SysWeaponMapper, SysWeapon> implements SysWeaponService {

    @Resource
    private SysWeaponMapper sysWeaponMapper;
    @Resource
    private SysWeaponContentService sysWeaponContentService;
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
        Page<SysWeapon> page= new Page(current,size);

        //分页获取数据
        Page<SysWeapon> pageData = sysWeaponMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 新增
     * @param sysWeapon
     * @return
     */
    @Override
    public ResultBean saveWeapon(SysWeapon sysWeapon) {
        //保存武器信息
        String weaponId = UUID.randomUUID().toString().replaceAll("-","");
        sysWeapon.setId(weaponId);
        sysWeapon.setCreatedTime(new Date());
        save(sysWeapon);

        //保存武器内容信息
        List<SysWeaponContent> contentList = new ArrayList<>();
        contentList.add(assemblyData(weaponId,sysWeapon.getOrigin(), WeaponConstant.ORIGIN_TYPE));
        contentList.add(assemblyData(weaponId,sysWeapon.getModelling(), WeaponConstant.MODELLING_TYPE));
        sysWeaponContentService.saveBatch(contentList);

        return ResultBean.success(sysWeapon);
    }

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(String id) {
        MapBuilder<Object, Object> map = MapUtil.builder();

        //获取武器信息
        SysWeapon sysWeapon = getById(id);
        map.put("weapon",sysWeapon);

        //获取上传文件信息
        List<SysUploadFile> fileList = sysUploadFileService.getFileRecord(OssConstant.WEAPON_TYPE,sysWeapon.getId());
        map.put("fileList",fileList);

        //获取武器内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("weapon_id",id);
        List<SysWeaponContent> weaponContentList = sysWeaponContentService.list(queryWrapper);
        weaponContentList.forEach(conten ->{
            map.put(conten.getType(),conten.getContent());
        });

        return ResultBean.success(map.build());
    }

    /**
     * 更新
     * @param sysWeapon
     * @return
     */
    @Override
    public ResultBean updateWeapon(SysWeapon sysWeapon) {
        //更新操作
        String weaponId = sysWeapon.getId();
        sysWeapon.setUpdatedTime(new Date());
        updateById(sysWeapon);

        //更新船只内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("weapon_id",weaponId);
        List<SysWeaponContent> weaponContentList = sysWeaponContentService.list(queryWrapper);
        weaponContentList.forEach(content ->{
            if(WeaponConstant.ORIGIN_TYPE.equals(content.getType())){
                content.setContent(sysWeapon.getOrigin());
            }else if(WeaponConstant.MODELLING_TYPE.equals(content.getType())){
                content.setContent(sysWeapon.getModelling());
            }
        });
        sysWeaponContentService.updateBatchById(weaponContentList);


        //删除已保存的武器文件关系
        if(StringUtils.isNotBlank(sysWeapon.getFileIds())){
            List<String> fileIds = Arrays.asList(sysWeapon.getFileIds().split(","));
            sysUploadFileService.deleteFileRecord(OssConstant.SHIPPING_TYPE,fileIds);
        }

        return ResultBean.success(sysWeapon);
    }

    /**
     * 根据id删除武器
     * @param id
     * @return
     */
    @Override
    public ResultBean deleteWeapon(String id) {
        //删除武器
        removeById(id);

        //删除武器内容信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("weapon_id",id);
        sysWeaponContentService.remove(queryWrapper);

        return ResultBean.success();
    }

    /**
     * 组装内容数据
     * @param weaponId
     * @param content
     * @param type
     * @return
     */
    private SysWeaponContent assemblyData(String weaponId, String content, String type){
        SysWeaponContent sysWeaponContent = new SysWeaponContent();
        sysWeaponContent.setId(UUID.randomUUID().toString().replaceAll("-",""));
        sysWeaponContent.setWeaponId(weaponId);
        sysWeaponContent.setContent(content);
        sysWeaponContent.setType(type);
        return sysWeaponContent;
    }
}
