<%@ page import="com.gzedu.xlims.common.AppConfig" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- <%@ page import="com.gzedu.xlims.common.AppUtil"%> --%>
<c:set var="ctx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<c:set var="css"><%=AppConfig.getProperty("cssconfig")%></c:set>
<c:set var="css_nocdn"><%=AppConfig.getProperty("cssconfig_nocdn")%></c:set>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

<!-- Bootstrap 3.3.5 -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/bootstrap/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${css_nocdn}/ouchgzee_com/platform/xllms_css/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins   folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/skins/skin-blue.min.css">
<!-- bootstrap-select  More beautiful--> 
 <link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/bootstrap/css/bootstrap-select.min.css"	>
 <!-- Select2 -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/select2/select2.min.css">
<!-- iCheck for checkboxes and radio inputs -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/iCheck/all.css">
<!-- Theme style -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/AdminLTE.min.css">
<!-- jquery-ui -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/jQueryUI/css/jquery-ui.css">
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/reset.css">

<!-- datepicker --> 
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xlbzr_css/plugins/datepicker/datepicker3.css">


<!--[if !IE]><!--> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQuery/jQuery-2.1.4.min.js"></script> 
<!--<![endif]--> 

<!--[if lt IE 9]>
		<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQuery/jquery-1.12.4.min.js"></script>
	<![endif]-->

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="${ctx}/static/js/html5shiv.min.js"></script>
<script src="${ctx}/static/js/respond.min.js"></script>
<![endif]-->

<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQueryUI/jquery-ui.min.js"></script> 

<script src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/bootstrap.min.js"></script> 
<!-- bootstrap-select  More beautiful--> 
<script src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/bootstrap-select.js"></script>
<!-- Select2 --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/select2/select2.full.min.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/select2/i18n/zh-CN.js"></script> 
<!-- iCheck 1.0.1 -->
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/iCheck/icheck.min.js"></script>
<!-- SlimScroll 1.3.0 --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/slimScroll/jquery.slimscroll.min.js"></script> 
<!-- ckeditor --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/ckeditor/ckeditor.js"></script>
<!-- custom-model --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/custom-model/custom-model.js"></script>  
<!-- AdminLTE App --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/dist/js/app.js"></script> 
<!-- common js --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/dist/js/common.js"></script> 
<!-- custom-model --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/custom-model/custom-model.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/timepicker/bootstrap-timepicker.min.js"></script>

<!-- bootstrapValidator --> 
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/dist/js/bootstrapValidator.js"></script>

<!-- datepicker -->
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>

<!-- JQuery-Validform -->
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/JQuery-Validform/Validform_v5.3.2_min.js"></script>


<!-- page js --> 
<script src="${ctx}/static/js/page/list.js?v=1"></script>


<script type="application/javascript">
	var ctx = '${ctx }';
</script>



