package com.liu.test;

import java.net.URL;

import javax.servlet.Filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.liu.sysUser.domain.User;
import com.liu.sysUser.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"classpath:applicationContext.xml" })  
public class TestUserService {  
  
	private static final Logger LOGGER = LoggerFactory.getLogger(TestUserService.class);
  
    @Autowired  
    private UserService userService;  
  
      
    @Test  
    public void testQueryById1(){  
    	//try {
			
   		User userInfo = userService.getUsers(1);  
  		LOGGER.info(JSON.toJSON(userInfo).toString());  
		//} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
		//}
        LOGGER.debug("dffgnmj,.yjkl;;lkjvn;lfun------------------ceshi");
    } 
    //这个test可以确定是那个jar包发生冲突，然后再去到pom.xml里面去设置<scope>provided</scope>
    @Test 
    public void get(){ 
        URL url = Filter.class.getProtectionDomain().getCodeSource().getLocation();
        String str = "path:"+url.getPath()+"  name:"+url.getFile();
        LOGGER.info(">>>>>>>>>>>>>>>>>"+str+"<<<<<<<<<<<<<<<<<<<");
    }
}  

