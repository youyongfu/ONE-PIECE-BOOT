package com.you.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysIslands;
import com.you.entity.SysUploadFile;
import com.you.mapper.SysIslandsMapper;
import com.you.service.SysIslandsService;
import com.you.service.SysUploadFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 岛屿管理服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysIslandsServiceImpl extends ServiceImpl<SysIslandsMapper, SysIslands> implements SysIslandsService {

    @Resource
    private SysIslandsMapper sysIslandsMapper;

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
        Page<SysIslands> page= new Page(current,size);

        //分页获取数据
        Page<SysIslands> pageData = sysIslandsMapper.selectPage(page, queryWrapper);

        return ResultBean.success(pageData);
    }

    /**
     * 根据id获取岛屿
     * @param id
     * @return
     */
    @Override
    public ResultBean getInfoById(Long id) {
        SysIslands sysIslands = getById(id);

        List<SysUploadFile> fileList = new ArrayList<>();
        if(StringUtils.isNotBlank(sysIslands.getPicture())){
            String[] pictures = sysIslands.getPicture().split(",");
            for (String FileId : pictures) {
                fileList.add(sysUploadFileService.getById(FileId));
            }
        }

        return ResultBean.success(MapUtil.builder().put("islands",sysIslands).put("fileList",fileList).build());
    }

    /**
     * 获取树形数据
     * @return
     */
    @Override
    public List<SysIslands> treeList() {
        List<SysIslands> tree = new ArrayList<>();
        list().forEach(sysIslands -> {
            //获取最外层父节点
            if(sysIslands.getParentId() == 0L){
                tree.add(sysIslands);
            }

            //依次获取子节点
            list().forEach(sysIslandsChild -> {
                if(sysIslands.getId() == sysIslandsChild.getParentId()){
                    sysIslands.getChildren().add(sysIslandsChild);
                }
            });
        });
        return  tree;
    }
}
