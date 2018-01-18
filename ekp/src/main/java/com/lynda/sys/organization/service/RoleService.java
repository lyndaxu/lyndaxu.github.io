package com.lynda.sys.organization.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lynda.common.model.organization.URole;
import com.lynda.common.plugin.Page;
import com.lynda.core.mybatis.page.Pagination;
import com.lynda.sys.organization.bo.RolePermissionAllocationBo;

public interface RoleService {

	int deleteByPrimaryKey(Long id);

    int insert(URole record);

    int insertSelective(URole record);

    URole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(URole record);

    int updateByPrimaryKey(URole record);

	Map<String, Object> deleteRoleById(String ids);

	//根据用户ID查询角色（role），放入到Authorization里。
	Set<String> findRoleByUserId(Long userId);

	List<URole> findNowAllPermission();
	//初始化数据
	void initData();

	List<URole> findPage(Page page);

	List<RolePermissionAllocationBo> findAllRoleAndPermission(Page page);
}
