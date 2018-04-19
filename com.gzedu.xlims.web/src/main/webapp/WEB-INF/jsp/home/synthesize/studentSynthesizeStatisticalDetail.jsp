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
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off" onclick="history.back();">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">工作台</a></li>
		<li><a href="${ctx}/admin/home/studentSynthesizeStatistical">学生综合信息查询</a></li>
		<li class="active">详情</li>
	</ol>
</section>

<section class="content">
	<iframe id="iframeDetail" src="${url }&simulation=1" frameborder="0" scrolling="auto" allowtransparency="true" width="100%" height="3000px"></iframe>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>