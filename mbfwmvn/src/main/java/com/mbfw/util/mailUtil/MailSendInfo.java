package com.mbfw.util.mailUtil;

import java.util.HashSet;
import java.util.Properties;

/**  
* 发送邮件需要使用的基本信息  
*/
public class MailSendInfo {
	// 发送邮件的服务器的IP和端口   
    private String mailServerHost;   
    private String mailServerPort;   
      
    /**
     *  邮件发送者的地址 
     *    
     */
    private String fromAddress;   
      
    /**
     *  邮件主送人的地址  
     *  集合对象必须是SysOrgPerson 或者（String邮件地址 字符串）
     *  HashSet可防止重复邮件地址出现
     */
    private HashSet<?> toAddress;   
    /**
     * 邮件抄送人的地址
     * 集合对象必须是SysOrgPerson 或者（String邮件地址 字符串）   
     * HashSet可防止重复邮件地址出现
     */
    private HashSet<?> ccAddress;   
    /**
     *  邮件密送人的地址   
     *  集合对象必须是SysOrgPerson 或者（String邮件地址 字符串）
     *  HashSet可防止重复邮件地址出现
     */
    private HashSet<?> bccAddress;   
      
    // 登陆邮件发送服务器的用户名和密码   
    private String userName;   
    private String password;   
      
    // 是否需要身份验证   
    private boolean validate = false;   
      
    // 邮件主题   
    private String subject;   
      
    // 邮件的文本内容   
    private String content;   
      
    /**
     * 默认给设置邮件的相关服务信息
     * 这个是中信证券OA邮件服务器
     * 如果再发送邮件时想换服务器可以重新给这几个属性赋值
     */
    public MailSendInfo(){
    	this.mailServerHost = "smtp.citicsinfo.com";
    	this.mailServerPort = "25";
    	this.validate = true;
    	this.userName = "oa@citics.com";
    	this.password = "Citics_000A";
    	this.fromAddress = "oa@citics.com";
    }
    
    /**  
      * 获得邮件会话属性  
      */   
    public Properties getProperties(){   
      Properties p = new Properties();   
      p.put("mail.smtp.host", this.mailServerHost);   
      p.put("mail.smtp.port", this.mailServerPort);   
      p.put("mail.smtp.auth", validate ? "true" : "false");   
      return p;   
    }   
    public String getMailServerHost() {   
      return mailServerHost;   
    }   
    public void setMailServerHost(String mailServerHost) {   
      this.mailServerHost = mailServerHost;   
    }  
    public String getMailServerPort() {   
      return mailServerPort;   
    }  
    public void setMailServerPort(String mailServerPort) {   
      this.mailServerPort = mailServerPort;   
    }  
    public boolean isValidate() {   
      return validate;   
    }  
    public void setValidate(boolean validate) {   
      this.validate = validate;   
    }  
    public String getFromAddress() {   
      return fromAddress;   
    }   
    public void setFromAddress(String fromAddress) {   
      this.fromAddress = fromAddress;   
    }  
    public String getPassword() {   
      return password;   
    }  
    public void setPassword(String password) {   
      this.password = password;   
    }  
    public HashSet<?> getToAddress() {   
      return toAddress;   
    }   
    public void setToAddress(HashSet<?> toAddress) {   
      this.toAddress = toAddress;   
    }   
    public HashSet<?> getCcAddress() {   
    	return ccAddress;   
    }   
    public void setCcAddress(HashSet<?> ccAddress) {   
    	this.ccAddress = ccAddress;   
    }   
    public HashSet<?> getBccAddress() {   
    	return bccAddress;   
    }   
    public void setBccAddress(HashSet<?> bccAddress) {   
    	this.bccAddress = bccAddress;   
    }   
    public String getUserName() {   
      return userName;   
    }  
    public void setUserName(String userName) {   
      this.userName = userName;   
    }  
    public String getSubject() {   
      return subject;   
    }  
    public void setSubject(String subject) {   
      this.subject = subject;   
    }  
    public String getContent() {   
      return content;   
    }  
    public void setContent(String textContent) {   
      this.content = textContent;   
    }   

}
