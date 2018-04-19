<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!doctype html>
<!--[if lte IE 7]><html class="ie67"><![endif]-->
<!--[if IE 8]><html class="ie8"><![endif]-->
<!--[if gte IE 9]><html><![endif]-->
<!--[if !IE]><!--><html><!--<![endif]-->
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
	<title>登录</title>
	<link type="text/css" rel="stylesheet" href="${css}/common/v2/common.css" />
	<link type="text/css" rel="stylesheet" href="${css}/ouchgzee_com/person_center/v3.0.1/login/style/login.css" />
	<link rel="shortcut icon" type="image/x-icon" href="${ctx }/static/images/favicon.ico">
	<!--[if lt IE 9]>
	<script type="text/javascript" src="${css}/common/js/resetHTML5_forIE.js"></script>
	<![endif]-->
</head>
<body>
<header class="header">
	<div class="login-center padding_t20 padding_b20">
		<div class="logo ">
			<img src="${css}/ouchgzee_com/person_center/v3.0.1/login/images/logo.png">
		</div>
	</div>
</header>

<section class="content">
	<div class="login-outer login-center">
		<div class="login-box">
			<div class="inner">
				<form id="loginForm" name="loginForm" action="${ctx}/coach/teacher/login" method="post">
					<input type="hidden" id="msg" name="msg" value="${msg}${loginErrorMessage }">
					<div class="login-wrap">
						<h1 class="login-title">辅导老师登录</h1>
						<div style="height:20px;">
							<div class="error-tips" style="display:none;line-height:1;">
								<img src="${css}/ouchgzee_com/person_center/v3.0.1/style/images/error.png">
								<span class="font_red f12">验证提示</span>
							</div>
						</div>
						<div class="input-group clearfix">
							<div class="input-group-addon">
								<i class="icon icon-user"></i>
							</div>
							<div class="input-control">
								<input id="username" name="username" value="" class="form-control" placeholder="用户名" type="text">
							</div>
						</div>
						<div class="input-group clearfix">
							<div class="input-group-addon">
								<i class="icon icon-psw"></i>
							</div>
							<div class="input-control">
								<input id="password" name="password" value="" class="form-control" placeholder="密码" type="password">
							</div>
						</div>
						<div>
							<div class="input-group clearfix inline-block vertical-middle" style="width:155px;">
								<div class="input-group-addon">
									<i class="icon icon-tips"></i>
								</div>
								<div class="input-control">
									<input id="rCode" name="rCode" value="" class="form-control" placeholder="验证码" type="text" style="width:90px !ie;">
								</div>
							</div>
							<%-- <div class="inline-block yzm">
								<img id="vCodeImg" src="${ctx}/vCode?w=111&h=43" width="111" height="43">
							</div>
							<div class="inline-block vertical-middle">
								<a href="javascript:void(0);" class="gray6" onclick="changeImg();">换一张</a>
							</div> --%>
						</div>
						<div class="margin_t20">
							<button type="submit" class="login-btn">登录</button>
						</div>
						<div class="clearfix margin_t15">
							<div class="dropdown fr">
								<div class="dropdown-toggle">
									<i class="icon icon-tel vertical-middle margin_r5"></i>
									<u>App下载</u>
								</div>
								<div class="dropdown-menu">
									<img src="${css}/ouchgzee_com/person_center/v3.0.1/login/images/qcode.png" class="vertical-middle margin_r10" style="width: 126px;">
									<div align="center">
										扫码下载
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="mark"></div>
		</div>
	</div>
</section>

<footer class="footer">
	<div class="login-center">
		<div align="center" class="padding_t20 padding_b15">
			主办单位：国家开放大学（广州）（广州广播电视大学）<br>
			运营单位：国家开放大学在线教育运营服务中心<br>
			<!-- 京ICP备05064925号 -->
		</div>
	</div>
</footer>

<script src="${css}/ouchgzee_com/person_center/v3.0.1/js/require.js" data-require-path="${css}/ouchgzee_com/person_center/v3.0.1/js/"></script>

<!--页面初始化-->
<script type="text/javascript">
	require(['jquery'], function ($) {
		$('.dropdown').on('mouseenter',function(event) {
			$(this).children('.dropdown-menu').show();
		}).on('mouseleave', function(event) {
			$(this).children('.dropdown-menu').hide();
		});

		$(window).resize(function(event) {
			var $loginOuter=$('.login-outer');
			var $html=$('html');

			$loginOuter.height($(window).height()-$('.header').height()-$('.footer').height());

			if($html.hasClass('ie8') || $html.hasClass('ie67')){
				if($(window).width()<=1200){
					$('.login-center').width(980);
					$('.login-box').addClass('.login-box-sm');
				}
				else{
					$('.login-center').width(1200);
					$('.login-box').removeClass('.login-box-sm');
				}
			}
		}).trigger('resize');

		$(document).ready(function(){
			if(window != top) {
				top.location.href=location.href;
			}

			var msg =  $('#msg').val();
			if(msg != ""){
				$('.error-tips').show();
				$('.error-tips .font_red').text(msg);
				$("#password").val("");
				$("#password").focus().select().addClass('valid-error');
			}else{
				$("#username").focus().select();
			}
		});

		$('#loginForm').submit(function () {
			$(":input").removeClass('valid-error');
			var b=$("#username").val();
			if(""==b){
				$('.error-tips').show();
				$('.error-tips .font_red').text("请输入用户名!");
				$("#username").focus().select().addClass('valid-error');
				return false;
			}
			var a=$("#password").val();
			if(""==a){
				$('.error-tips').show();
				$('.error-tips .font_red').text("请输入密码!");
				$("#password").focus().select().addClass('valid-error');
				return false;
			}
			/* var c=$("#rCode").val();
			if(""==c){
				$('.error-tips').show();
				$('.error-tips .font_red').text("请输入验证码!");
				$("#rCode").focus().select().addClass('valid-error');
				return false;
			} */
			return true;
		});

	});

	function changeImg(){
		document.getElementById("vCodeImg").src="${ctx}/vCode?w=111&h=43&" + Math.random();
	}
</script>
</body>
</html>
