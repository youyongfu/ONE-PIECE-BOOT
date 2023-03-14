package com.you.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.you.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 上传文件Mapper接口
 * @author yyf
 * @version 1.0
 * @date 2023/2/9
 */
@Mapper
public interface SysUploadFileMapper extends BaseMapper<SysUploadFile> {

    void saveOrganizationFileRecord(SysOrganizationFile sysOrganizationFile);

    List<SysUploadFile> getOrganizationFileRecord(String organizationId);

    List<SysUploadFile> deleteOrganizationFileRecord(List<String> fileIds);

    void saveDevilnutFileRecord(SysDevilnutFile sysDevilnutFile);

    List<SysUploadFile> getDevilnutFileRecord(String devilnutId);

    List<SysUploadFile> deleteDevilnutFileRecord(List<String> fileIds);

    void saveIslandsFileRecord(SysIslandsFile sysIslandsFile);

    List<SysUploadFile> getIslandsFileRecord(String islandsId);

    List<SysUploadFile> deleteIslandsFileRecord(List<String> fileIds);

    void saveShippingFileRecord(SysShippingFile sysShippingFile);

    List<SysUploadFile> getShippingFileRecord(String shippingId);

    List<SysUploadFile> deleteShippingFileRecord(List<String> fileIds);

    void saveWeaponFileRecord(SysWeaponFile sysWeaponFile);

    List<SysUploadFile> getWeaponFileRecord(String weaponId);

    List<SysUploadFile> deleteWeaponFileRecord(List<String> fileIds);
}
