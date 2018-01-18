package com.lynda.sys.organization.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;

import com.lynda.common.model.organization.UUser;
import com.lynda.common.plugin.Page;
import com.lynda.core.mybatis.page.Pagination;
import com.lynda.sys.organization.bo.URoleBo;
import com.lynda.sys.organization.bo.UserRoleAllocationBo;

public interface UUserService {

	int deleteByPrimaryKey(Long id);

	UUser insert(UUser record);

    UUser insertSelective(UUser record);

    UUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UUser record);

    int updateByPrimaryKey(UUser record);
    
    UUser login(String email ,String pswd);

	UUser findUserByEmail(String email);

	Map<String, Object> deleteUserById(String ids);

	Map<String, Object> updateForbidUserById(Long id, Long status);

	List<URoleBo> selectRoleByUserId(Long id);

	Map<String, Object> addRole2User(Long userId, String ids);

	Map<String, Object> deleteRoleByUserIds(String userIds);

	List<UUser> findByPage(Page page);

	List<UserRoleAllocationBo> findAllUserAndRole(Page page);
}
