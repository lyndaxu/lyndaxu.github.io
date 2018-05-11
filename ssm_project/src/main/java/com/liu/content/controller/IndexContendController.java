package com.liu.content.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/indexContent")
public class IndexContendController {
	/**
	 * @author newze
	 * @return 
	 * @exception
	 * 这个是登陆跳转后的首页
	 * 
	 */
	@RequestMapping("/index.do")
	public String forwardManIndex(){
		System.out.println("---------------------");
		return "index.html";
	}
}
