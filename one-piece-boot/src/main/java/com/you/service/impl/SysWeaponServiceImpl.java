package com.you.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysUploadFile;
import com.you.entity.SysWeapon;
import com.you.mapper.SysWeaponMapper;
import com.you.service.SysUploadFileService;
import com.you.service.SysWeaponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
     * 根据id获取果实
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(Long id) {
        SysWeapon sysWeapon = getById(id);

        List<SysUploadFile> fileList = new ArrayList<>();
        if(StringUtils.isNotBlank(sysWeapon.getPicture())){
            String[] pictures = sysWeapon.getPicture().split(",");
            for (String FileId : pictures) {
                fileList.add(sysUploadFileService.getById(FileId));
            }
        }

        return ResultBean.success(MapUtil.builder().put("weapon",sysWeapon).put("fileList",fileList).build());
    }

}
