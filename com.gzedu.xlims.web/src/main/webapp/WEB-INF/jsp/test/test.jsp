<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统 - 测试</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
		$(function() {

			$("[data-role='import']").click(function(event) {
				$.mydialog({
					id:'import',
					width:600,
					height:415,
					zIndex:11000,
					content: 'url:toImport.html'
				});
			});

		});
	</script>
</head>
<body class="inner-page-body">
	<section class="content-header clearfix">
		<ol class="breadcrumb oh">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">测试</a></li>
			<li class="active">测试</li>
		</ol>
	</section>

	<section class="content" data-id="0">
		<a class="btn btn-default btn-sm margin_l10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 批量导入学员信息下载入学通知书</a>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>
