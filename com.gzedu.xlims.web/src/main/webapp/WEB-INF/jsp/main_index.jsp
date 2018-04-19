<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
<section class="content">
	<div class="text-center">
		<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/home.jpg">
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
	window.onload=function(){
		top.$(".overlay-wrapper.loading").hide();
	}
</script>
</body>
</html>