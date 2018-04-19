<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
<meta name="viewport" content="initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta content="telephone=no" name="format-detection">
<meta content="email=no" name="format-detection">
<title>支付--同步通知</title>
<link rel="stylesheet" href="http://css.gzedu.com/gzdd_ouchgzee_com/platform/student/mobile/css/sm.min.css">
<style type="text/css">
	body{
		position: static;
		overflow: auto;
    	-webkit-overflow-scrolling: touch;
    	background:#fff;
	}
  	img{
	  max-width: 100%;
	}
</style>
</head>
<body style="text-align: center;">
	<h1>支付成功</h1><br/>
	<p>
	<a href="javascript:Phone.paySuccess();" >关闭</a>
</p>
	<!-- <script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script> -->
</body>
</html>
