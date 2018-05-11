<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>登陆页</title>
    <link rel="stylesheet" href="css/login.css"/>
</head>
<body>
<div id="login-index">
    <div class="logo"></div>
    <!--中间内容开始-->
    <div class="login-box">
        <div class="login-ui">
            <table cellpadding="0" cellspacing="0" class="login-table">
                <tr>
                    <td class="login-ui-top"></td>
                </tr>
                <tr>
                    <td class="login-ui-cot">
                        <div class="error-box">
                            抱歉！您没有发现请求的网页，请联系系统管理员！
                            <br />请从 <a href="http://www.baidu.com">企业运作支持平台-业务支持-投行管理平台 </a>重新进入！   
                            <%--<br>或者回到 <a href="login.jsp">首页</a>--%>
                            <%--<span id="time"><font color='red'>5</font></span> 秒后自动回到 <a href="login.jsp">首页</a>--%>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <!--中间内容结束-->
    <!--footer begin-->
    <div class="footer">
    </div>
    <!--footer end-->
</div>
<%--<script language="javascript">--%>
    <%--var i=5;--%>
    <%--setInterval("settime()",1000);--%>
    <%--function settime()--%>
    <%--{--%>
        <%--i--;--%>
        <%--document.getElementById('time').innerHTML="<font color='red'>"+i+"</font>";--%>
        <%--if(i<=1)--%>
        <%--{--%>
            <%--location.href="login.jsp"; //若重定向，则再次跳转时应该加上工程名--%>
        <%--}--%>
    <%--}--%>
<%--</script>--%>
</body>
</html>
