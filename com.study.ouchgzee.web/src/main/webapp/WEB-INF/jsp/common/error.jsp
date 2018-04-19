<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<c:set var="ctx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
	
	</section>

	<!-- Main content -->
	<section class="content">

	<div class="error-page">
		<h2 class="headline text-red">出错啦！</h2>
		<div class="error-content">
			<h3>
				<i class="fa fa-warning text-red"></i> Oops! Something went wrong.
			</h3>
			<p>
				${rs.strMessage}
			</p>
		</div>
	</div>
	<!-- /.error-page -->
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->
