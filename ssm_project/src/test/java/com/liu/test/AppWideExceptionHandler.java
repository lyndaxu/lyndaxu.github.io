package com.liu.test;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppWideExceptionHandler {
	@ExceptionHandler(Exception.class)
	public String duolicateSplittleHandler(){
		System.out.println("测试控制器异常通知");
		return null;
		
	}
}
