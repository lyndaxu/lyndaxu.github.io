package com.lynda.common.dao.organization;

import java.util.List;
import java.util.Map;

import com.lynda.common.model.organization.UUser;
import com.lynda.common.plugin.Page;
import com.lynda.sys.organization.bo.URoleBo;
import com.lynda.sys.organization.bo.UserRoleAllocationBo;

public interface UUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UUser record);

    int insertSelective(UUser record);

    UUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UUser record);

    int updateByPrimaryKey(UUser record);

	UUser login(Map<String, Object> map);

	UUser findUserByEmail(String email);

	List<URoleBo> selectRoleByUserId(Long id);
	List<UUser> findByPage(Map<String, Object> resultMap);

	List<UUser> findAll(Page page);

	List<UserRoleAllocationBo> findAllUserAndRole(Page page);
}