package com.you.controller;

import com.you.common.ResultBean;
import com.you.entity.SysDict;
import com.you.service.SysDictService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 数据字典控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Resource
    private SysDictService sysDictService;

    /**
     * 获取数据字典列表
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:dict:list')")   //查看权限
    public ResultBean list(){
        return ResultBean.success(sysDictService.treeList());
    }

    /**
     * 添加数据字典
     * @param sysDict
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:dict:save')")      //提交权限
    public ResultBean save(@Validated @RequestBody SysDict sysDict) {
        //未选择上级菜单，则默认为添加目录
        if(sysDict.getParentId() == null){
            sysDict.setParentId(0L);
        }
        sysDict.setCreatedTime(new Date());
        sysDictService.save(sysDict);
        return ResultBean.success(sysDict);
    }

    /**
     * 根据id获取数据字典
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:dict:list')")
    public ResultBean info(@PathVariable(name = "id") Long id) {
        return ResultBean.success(sysDictService.getById(id));
    }

    /**
     * 更新数据字典
     * @param sysDict
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:dict:update')")      //更新权限
    public ResultBean update(@Validated @RequestBody SysDict sysDict) {
        //更新操作
        sysDict.setUpdatedTime(new Date());
        sysDictService.updateById(sysDict);

        return ResultBean.success(sysDict);
    }

    /**
     * 根据id删除数据字典
     * @param id
     * @return
     */
    @Transactional
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public ResultBean delete(@PathVariable("id") Long id) {
        return sysDictService.delete(id);
    }
}

