package com.lynda.common.dao.organization;

import java.util.List;
import java.util.Set;

import com.lynda.common.model.organization.UPermission;
import com.lynda.common.plugin.Page;
import com.lynda.sys.organization.bo.UPermissionBo;

public interface UPermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UPermission record);

    int insertSelective(UPermission record);

    UPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UPermission record);

    int updateByPrimaryKey(UPermission record);

	List<UPermissionBo> selectPermissionById(Long id);
	//根据用户ID获取权限的Set集合
	Set<String> findPermissionByUserId(Long id);

	List<UPermission> findAll(Page page);
}