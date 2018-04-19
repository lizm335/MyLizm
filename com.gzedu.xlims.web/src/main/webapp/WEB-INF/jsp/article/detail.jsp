<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>文章管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<!-- ckeditor -->
<script src="${ctx }/static/plugins/ckeditor/ckeditor.js"></script>
<script type="text/javascript">
	
</script>


</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px"
			data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="${ctx}/article/list">文章管理</a></li>
			<li class="active">文章详情</li>
		</ol>
	</section>
	<section class="content">
		<div class="box no-margin">
			<div class="box-body pad20">
				<article class="article">
					<h1 class="article-title">${gjtArticle.title}</h1>
					<div>${gjtArticle.content}</div>
					<div>附件:<a href="${gjtArticle.fileUrl}">${gjtArticle.fileName}</div>
				</article>
			</div>
		</div>
	</section>
</body>
</html>