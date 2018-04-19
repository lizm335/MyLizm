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
	<!-- Main content -->
	<section class="content-header">

		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">系统管理</a></li>
			<li class="active">操作管理</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal" method="post">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">平台类型</label>
								<div class="col-sm-9">
									<select  name="search_EQ_platfromType"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<option value="1" <c:if test="${param['search_EQ_platfromType']==1}">selected='selected'</c:if>>管理平台</option>
										<option value="3" <c:if test="${param['search_EQ_platfromType']==3}">selected='selected'</c:if>>班主任平台</option>
										<option value="5" <c:if test="${param['search_EQ_platfromType']==5}">selected='selected'</c:if>>学习空间</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<label class="control-label col-sm-3">关键字</label>
							<div class="col-sm-9">
								<input class="form-control" type="text" name="search_LIKE_username" value="${param.search_LIKE_username}" placeholder="用户名、用户操作">
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<label class="control-label col-sm-3">请求方法</label>
							<div class="col-sm-9">
								<input class="form-control" type="text" name="search_LIKE_method" value="${param.search_LIKE_method}" placeholder="请求方法">
							</div>
						</div>
					</div>
					<div class="row reset-form-horizontal clearbox">
						<div class="col-sm-4 col-xs-6">
							<label class="control-label col-sm-3">请求参数</label>
							<div class="col-sm-9">
								<input class="form-control" type="text" name="search_LIKE_params" value="${param.search_LIKE_params}" placeholder="请求参数">
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<label class="control-label col-sm-3">执行时长</label>
							<div class="col-sm-9">
								<select  name="search_GTE_time" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<option value="30" <c:if test="${param['search_GTE_time']==30}">selected='selected'</c:if>>30s以上 [严重警告]</option>
									<option value="10" <c:if test="${param['search_GTE_time']==10}">selected='selected'</c:if>>10-30s [警告]</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer">
					<div class="btn-wrap">
						<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
			</div>

			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

			<div class="box">
				<div class="box-body">
					<div class="row">
						<div class="col-sm-12">
							<table class="table table-bordered table-striped table-container text-center table-font" style="word-break: break-all;">
								<thead>
									<tr>
										<%--<th>ID</th>--%>
										<th>用户名</th>
										<th>用户操作</th>
										<th style="width: 15%">请求方法</th>
										<th style="width: 30%">请求参数</th>
										<th>执行时长(毫秒)</th>
										<th style="width: 15%">错误信息</th>
										<th>IP地址</th>
										<th>创建时间</th>
									</tr>
								</thead>
								<tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.content}" var="entity">
										<tr>
											<%--<td>${entity.id}</td>--%>
											<td>${entity.username}</td>
											<td>${entity.operation}</td>
											<td>${entity.method}</td>
											<td>
												<div class="scroll-box" style="max-height:200px;">
												${entity.params}
												</div>
											</td>
											<td>
												<c:choose>
													<c:when test="${entity.time>=30000}">
														<span class="text-red">${entity.time}</span>
													</c:when>
													<c:when test="${entity.time>=10000}">
														<span class="text-orange">${entity.time}</span>
													</c:when>
													<c:otherwise>
														${entity.time}
													</c:otherwise>
												</c:choose>
											</td>
											<td>
												<div class="scroll-box" style="max-height:200px;">
														${entity.error}
												</div>
											</td>
											<td>${entity.ip}</td>
											<td><fmt:formatDate value="${entity.createDate}" type="both" /></td>
										</tr>
										</c:forEach>
									</c:when>
									<c:otherwise><tr><td colspan="9">暂无数据！</td></tr></c:otherwise>
								</c:choose>
								</tbody>
							</table>
						</div>
					</div>
					<tags:pagination page="${pageInfo}" paginationSize="5" />
				</div>
			</div>
		</form>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>
