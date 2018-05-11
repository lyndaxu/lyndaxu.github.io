package com.liu.test;

public class EnhancerTest {
	public static void main(String[] args) throws Exception {  
        Hello hello = new Hello();  
        ProxyFactory cglibProxy = new ProxyFactory();  
        Hello proxy = (Hello) cglibProxy.createProxy(hello);  
        String result=proxy.sayHello(true);  
        System.out.println(result);  
    }  
}
