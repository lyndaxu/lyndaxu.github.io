package com.liu.sysUser.service;

import com.liu.sysUser.domain.User;

public interface UserService {
     
	    User getUsers(Integer id);  
	      
		boolean doUserLogin(User user);

		User selectUser(User uservo);  
}
