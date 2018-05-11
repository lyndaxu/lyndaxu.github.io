package com.liu.sysUser.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.liu.sysUser.domain.User;
import com.liu.sysUser.service.UserService;

@Controller
@Scope("prototype")
@RequestMapping("/sysUser")
public class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	UserService userService;
	@RequestMapping(value="/login.do",method={RequestMethod.GET,RequestMethod.POST})  
	public String dologin(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String name=request.getParameter("userName");
		String password=request.getParameter("userPassword");
		User uservo=new User();
		uservo.setUserName(name);
		uservo.setUserPassword(password);
		uservo=userService.selectUser(uservo);
		HttpSession session = request.getSession(true);
		if(uservo.getUserId()!=null){
			session.setAttribute("user", uservo);
			return "redirect:error/404.jsp";
		}else{
			System.out.println("查不到信息");
			LOGGER.info("-------------------");
			return "redirect:error/404.jsp";
		}
	}  
}
