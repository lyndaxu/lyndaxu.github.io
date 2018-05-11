<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>ssm_project项目管理系统登录</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
	$('.clockpicker').clockpicker();
	</script>
  </head>
  
  <body style="text-align: center;">
  <h1 style="color: red;">这是测试ssm用的</h1>
  <form action="findUser.do" method="post">
    This is my JSP page. <br>
    <span>用户名：</span><input id="userName" value=""><br>
    <span>密码：</span><input id="password" value=""><br>
    <div>
     <input type="text" id="startTime" class="dateInput" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly" name="subTime"/>
   <a href="javascript:void(0);"><img
								onclick="WdatePicker({el:'startTime'})" src="images/icon/date.png"
								style="cursor: pointer;" /></a>
								</div>
	<div class="input-group clockpicker">
    <input type="text" class="form-control" value="09:30">
    <span class="input-group-addon">
        <span class="glyphicon glyphicon-time"></span>
    </span>
	</div>
    <input id="but" type="submit" value="登录">
    </form>
    
  </body>
  
</html>
