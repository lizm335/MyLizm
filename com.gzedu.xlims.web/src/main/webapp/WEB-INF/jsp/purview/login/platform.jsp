<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="../../../jsp/common/taglibs.jsp" %>

<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>综合管理系统</title>
	<link rel="stylesheet" type="text/css" href="http://css.eenet.com/admin/style/login_default/base.css" />
	<link rel="stylesheet" type="text/css" href="http://css.eenet.com/admin/style/skin06/skin.css" />
	<script type="text/javascript" src="${css}/common/js/jquery.js"></script>
	<script type="text/javascript" src="http://css.eenet.com/admin/js/login.js"></script>
	<!--[if lte IE 6]>
	<script type="text/javascript" src="${css}/common/js/DD_belatedPNG_0.0.8a.js"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix('.bg_png, .login_contianer, .ico_lock, .ico_user, .login_header .left, .login_header .right, .login_header .mid, .login_header .mid1, .login_content .left, .login_content .right, .login_bottom .left, .login_bottom .right, .login_bottom .mid');
	</script>
	<![endif]-->
	<style type="text/css">
		::-webkit-scrollbar-track{
			-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
			background-color: #F5F5F5;
		}
		::-webkit-scrollbar{width: 8px;height:8px;background-color: #F5F5F5;}
		::-webkit-scrollbar-thumb{background-color: #666;}
	</style>
</head>

<body>

<div class="body_container" id="bodyContainer">
	<div class="loading" id="loading"></div>

	<div class="login_main hide" id="loginMain">

		<div class="main_contianer" style="background:#fff; left:auto; top:0px; right:0; margin:0px; padding-right:5%; overflow-y:auto; overflow-x:hidden;">

			<div class="login_contianer">

				<div class="login_header">
					<div class="mid" style="background:none;">
						<c:if test="${empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginHeadLogo }">
						<span class="logo"><img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/temp/login-page-logo.png" class="bg_png" style="max-width: 345px;" /></span>
						</c:if>
						<c:if test="${not empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginHeadLogo }">
							<span class="logo"><img src="${sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginHeadLogo }" class="bg_png" style="max-width: 345px;" /></span>
						</c:if>
						<span class="text">&nbsp;&nbsp;综合管理系统</span>
					</div>
				</div>

				<div class="sorts">

					<div class="sorts_header"><h3>院长</h3><i></i></div>
					<ul>
						<li  class="redirects" date-name="admin" date-url="${ctx}/yz/login" ><i class="icon_default"></i>院长管理平台<b></b></li>
					</ul>
					<div class="sorts_header"><h3>教学教务管理</h3><i></i></div>
					<ul>
						<li  class="redirects" date-name="admin" date-url="${ctx}/jw/login" ><i class="icon_default"></i>教务管理平台<b></b></li>
						<li  class="redirects" date-name="admin" date-url="${ctx}/jx/login" ><i class="icon_default"></i>教学管理平台<b></b></li>
						<li  class="redirects" date-name="admin" date-url="${ctx}/xj/login" ><i class="icon_default"></i>学籍管理平台<b></b></li>
						<li  class="redirects" date-name="admin" date-url="${ctx}/kw/login" ><i class="icon_default"></i>考务管理平台<b></b></li>
						<li  class="redirects" date-name="admin" date-url="${ctx}/jc/login" ><i class="icon_default"></i>教材管理平台<b></b></li>
						<li  class="redirects" date-name="admin" date-url="${ctx}/by/login" ><i class="icon_default"></i>毕业管理平台<b></b></li>
					</ul>
					<div class="sorts_header"><h3>学支管理</h3><i></i></div>
					<ul>
						<li  class="redirects" date-name="admin" date-url="${ctx}/xz/login" ><i class="icon_default"></i>学院学支管理平台<b></b></li>
						<li  class="redirects" date-name="admin" date-url="${ctx}/xxzx/login" ><i class="icon_default"></i>学习中心管理平台<b></b></li>
						<li  class="redirects" date-name="admin" date-url="${ctx}/xxzxxz/login" ><i class="icon_default"></i>学习中心学支管理平台<b></b></li>
						<li  class="redirects" date-name="admin" date-url="${ctx}/zsdxz/login" ><i class="icon_default"></i>招生点学支管理平台<b></b></li>
					</ul>
					<div class="sorts_header"><h3>运营管理</h3><i></i></div>
					<ul>
						<li  class="redirects" date-name="admin" date-url="${ctx}/yy/login" ><i class="icon_default"></i>运营管理平台<b></b></li>
					</ul>

				</div>

			</div>

		</div>

	</div>

	<div class="bg_main hide" id="mainBg">
		<c:if test="${empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginBackground }">
			<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/temp/login-page-banner.png" orgwidth="1920" orgheight="1024" />
		</c:if>
		<c:if test="${not empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginBackground }">
			<img src="${sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginBackground }" orgwidth="1920" orgheight="1024" />
		</c:if>
	</div>
</div>
<script type="text/javascript" language="javascript">
	$(document).ready(function() {
		$higt = $(window).height();
		$widt = $(window).width();
		$(".main_contianer").height($higt);
		$(".main_contianer").width($widt*.5);

		$(window).resize(function(){
			$higt = $(window).height();
			$widt = $(window).width();
			$(".main_contianer").height($higt);
			$(".main_contianer").width($widt*.5);

		});

		$(".redirects").click(function(){
			var url = $(this).attr("date-url");
			window.open(url,$(this).attr("date-name"),"",true);//从新窗口打开
		});
	});
</script>

</body>
</html>
