package com.liu.sysUser.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liu.sysUser.dao.UserMapper;
import com.liu.sysUser.domain.User;
import com.liu.sysUser.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired  
    private UserMapper userInfoMapper;  
  
    public User getUsers(Integer id) {  
        return userInfoMapper.selectUserById(id);  
    }  
  
	public boolean doUserLogin(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User selectUser(User user) {
		User users = userInfoMapper.selectUser(user);
		return users;
	}  
}
