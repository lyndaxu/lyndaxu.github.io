package com.lynda.common.dao.organization;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lynda.common.model.organization.URole;
import com.lynda.common.plugin.Page;
import com.lynda.sys.organization.bo.RolePermissionAllocationBo;

public interface URoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(URole record);

    int insertSelective(URole record);

    URole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(URole record);

    int updateByPrimaryKey(URole record);

	Set<String> findRoleByUserId(Long id);

	List<URole> findNowAllPermission(Map<String, Object> map);
	
	void initData();

	List<URole> findAll(Page page);

	List<RolePermissionAllocationBo> findAllRoleAndPermission(Page page);
}