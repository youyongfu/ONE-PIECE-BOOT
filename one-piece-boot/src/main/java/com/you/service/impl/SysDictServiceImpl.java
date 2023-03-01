package com.you.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.common.ResultBean;
import com.you.entity.SysDict;
import com.you.mapper.SysDictMapper;
import com.you.service.SysDictService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典服务实现类
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {


    /**
     * 获取数据字典
     * @return
     */
    @Override
    public List<SysDict> treeList() {

        List<SysDict> dictTree = new ArrayList<>();

        List<SysDict> sysDictList = list();
        sysDictList.forEach(sysDict -> {
            //获取最外层父节点
            if(sysDict.getParentId() == 0L){
                dictTree.add(sysDict);
            }

            //依次获取子节点
            sysDictList.forEach(sysDictChild -> {
                if(sysDict.getId() == sysDictChild.getParentId()){
                    sysDict.getChildren().add(sysDictChild);
                }
            });
        });
        return  dictTree;
    }

    /**
     * 删除数据字典
     * @param id
     * @return
     */
    @Override
    public ResultBean delete(Long id) {
        //判断该菜单是否存在子菜单，存在无法删除
        int count = count(new QueryWrapper<SysDict>().eq("parent_id", id));
        if (count > 0) {
            return ResultBean.fail("请先删除子数据字典");
        }

        //删除菜单
        removeById(id);

        return ResultBean.success();
    }
}
