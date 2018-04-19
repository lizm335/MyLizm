<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>登录情况</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body>

<section class="content">
	<form id="listForm" class="form-horizontal">
		<div class="table-responsive">
			<table class="table-gray-th text-center">
				<thead>
					<tr>
						<th>登录IP</th>
						<th>登录城市</th>
						<th>登录系统</th>
						<th>登录浏览器</th>
						<th>登录时间</th>
						<th>退出时间</th>
						<th>在线时长</th>
					</tr>
				</thead>
				<tbody>
				<c:choose>
					<c:when test="${not empty pageInfo.content}">
						<c:forEach items="${pageInfo.content}" var="info">
							<tr>
								<td>${info.loginIp}</td>
								<td>${info.loginAddress}</td>
								<td>${info.os}</td>
								<td>${info.browser}</td>
								<td><fmt:formatDate value="${info.createdDt}" type="both" /></td>
								<td><fmt:formatDate value="${info.updatedDt}" type="both" /></td>
								<td><fmt:formatNumber value="${info.loginTime/60}" pattern="0" />小时<fmt:formatNumber value="${info.loginTime%60}" pattern="0" />分</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td align="center" colspan="7">暂无数据</td>
						</tr>
					</c:otherwise>
				</c:choose>
				</tbody>
			</table>
			<tags:pagination page="${pageInfo}" paginationSize="5" />
		</div>
	</form>
</section>

</body>
</html>
