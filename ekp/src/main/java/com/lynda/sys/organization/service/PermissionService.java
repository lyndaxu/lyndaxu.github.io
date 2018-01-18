package com.lynda.sys.organization.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lynda.common.model.organization.UPermission;
import com.lynda.common.plugin.Page;
import com.lynda.core.mybatis.page.Pagination;
import com.lynda.sys.organization.bo.UPermissionBo;

public interface PermissionService {

	int deleteByPrimaryKey(Long id);

	UPermission insert(UPermission record);

    UPermission insertSelective(UPermission record);

    UPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UPermission record);

    int updateByPrimaryKey(UPermission record);

	Map<String, Object> deletePermissionById(String ids);

	List<UPermission> findPage(Page page);
	
	List<UPermissionBo> selectPermissionById(Long id);

	Map<String, Object> addPermission2Role(Long roleId,String ids);

	Map<String, Object> deleteByRids(String roleIds);
	//根据用户ID查询权限（permission），放入到Authorization里。
	Set<String> findPermissionByUserId(Long userId);
}
