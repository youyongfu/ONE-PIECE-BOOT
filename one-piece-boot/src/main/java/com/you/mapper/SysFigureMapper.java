package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.SysFigure;
import com.you.entity.SysFigureDevilnut;
import com.you.entity.SysFigureIslands;
import com.you.entity.SysFigureWeapon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 人物大全Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Mapper
public interface SysFigureMapper extends BaseMapper<SysFigure> {

    void batchSaveFigureDevilnut(List<SysFigureDevilnut> sysFigureDevilnutList);

    List<SysFigureDevilnut> getFigureDevilnutByFigureId(String figureId);

    void deleteFigureDevilnutByFigureId(String figureId);

    void batchSaveFigureIslands(List<SysFigureIslands> sysFigureIslandsList);

    List<SysFigureIslands> getFigureIslandsByFigureId(String figureId);

    void deleteFigureIslandsByFigureId(String figureId);

    void batchSaveFigureWeapon(List<SysFigureWeapon> sysFigureWeaponList);

    List<SysFigureWeapon> getFigureWeaponByFigureId(String figureId);

    void deleteFigureWeaponByFigureId(String figureId);

}
