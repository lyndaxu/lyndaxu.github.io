<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> <!--输出,条件,迭代标签库-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="fmt"%> <!--数据格式化标签库-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="sql"%> <!--数据库相关标签库-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="fn"%> <!--常用函数标签库-->
<%@ page isELIgnored="false"%> <!--支持EL表达式，不设的话，EL表达式不会解析-->
<%
	//这个项目路径 如：/ssm_project（项目名）
    String path = request.getContextPath();
	//项目所在服务的地址--本地的话如：http://localhost:8080/ssm_project/
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ page language="java" pageEncoding="UTF-8" %>
<meta http-equiv="X-UA-Compatible" content="IE=IE9"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<!-- <link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css"> -->
  <!-- HTML5 Shim 和 Respond.js 用于让 IE8 支持 HTML5元素和媒体查询 -->
      <!-- 注意： 如果通过 file://  引入 Respond.js 文件，则该文件无法起效果 -->
      <!--[if lt IE 9]>
         <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
         <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
      <![endif]-->
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="jquery/jquery-3.2.1.min.js"></script>
<!-- core start-->
<script type="text/javascript" src="jscomponent/common/json.js"></script>
<script type="text/javascript" src="jscomponent/common/nz.prototype.js"></script>
<script type="text/javascript" src="jscomponent/common/linkage.js"></script>
<script type="text/javascript" src="jscomponent/common/year_and_month_select.js"></script>
<script type="text/javascript" src="jscomponent/common/nz_js_commonUtil.js"></script>
<!-- core end-->

<!-- dialog start-->
<script type="text/javascript" src="jscomponent/lhgdialog/lhgdialog/lhgdialog.js"></script>

<!-- WdatePicker start-->
<script type="text/javascript" src="jscomponent/datePicker/WdatePicker.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="bootstrap/js/bootstrap.min.js"></script>


<script type="text/javascript" >
window.onerror=function(){
	return true;
}
</script>
