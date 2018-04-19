<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 服务记录</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		服务记录
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">服务记录</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

	<div class="nav-tabs-custom reset-nav-tabs-custom" id="notice-tabs" style="margin-bottom: 0px;">
		<div class="pull-right margin_r10 margin_t10">
			<a href="${ctx}/home/class/serviceManager/toCreateService" role="button" class="btn btn-default btn-add"><i class="fa fa-fw fa-plus"></i>  新增服务</a>
		</div>
		<ul class="nav nav-tabs nav-tabs-lg">
			<c:choose>
				<c:when test="${flag eq '1'}">
					<li><a href="${ctx}/home/class/serviceManager/list">未结束(${unCount})</a></li>
					<li class="active"><a href="${ctx}/home/class/serviceManager/overList" data-toggle="tab">已结束(${overCount})</a></li>
				</c:when>
				<c:otherwise>
					<li class="active"><a href="${ctx}/home/class/serviceManager/list" data-toggle="tab">未结束(${unCount})</a></li>
					<li><a href="${ctx}/home/class/serviceManager/overList">已结束(${overCount})</a></li>
				</c:otherwise>
			</c:choose>
			
		</ul>
		<div class="tab-content">
			<form class="form-horizontal">
				<div class="box box-border no-shadow">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-sm-5">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">服务主题</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_title" value="${param['search_LIKE_title']}" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="control-label col-sm-2 text-nowrap">服务时间</label>
									<div class="col-sm-9">
										<div class="input-group input-daterange full-width">
											<input type="text" name="search_GTE_starttime" value="${param.search_GTE_starttime}" class="form-control">
											<div class="input-group-addon no-border">
												至
											</div>
											<input type="text" name="search_LTE_starttime" value="${param.search_LTE_starttime}" class="form-control">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div><!-- /.box-body -->
					<div class="box-footer">
						<div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
						<div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
					</div><!-- /.box-footer-->
				</div>
			</form>

			<div class="table-responsive">
				<table id="dtable" class="table table-bordered table-striped table-container text-center">
					<thead>
						<tr>
							<th><input type="checkbox" class="select-all"></th>
							<th>主题</th>
							<th>服务次数</th>
							<th>开始时间</th>
							<c:if test="${flag eq '1'}">
							<th>结束时间</th>
							</c:if>
							<th>当前总耗时</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${not empty infos && infos.numberOfElements > 0}">
							<c:forEach items="${infos.content}" var="info">
								<c:if test="${not empty info}">
									<tr>
										<td><input type="checkbox" name="ids"
												   data-id="${info.serviceid}" data-name="check-id"
												   value="${info.serviceid}"></td>
										<td>${info.title}</td>
										<td>${info.colRecordNum}</td>
										<td><fmt:formatDate value="${info.starttime}" pattern="yyyy-MM-dd HH:mm" /></td>
										<c:if test="${flag eq '1'}">
										<td><fmt:formatDate value="${info.endtime}" pattern="yyyy-MM-dd HH:mm" /></td>
										</c:if>
										<td>
											<c:if test="${info.totaltime/60/60 >= 1}">
												<fmt:formatNumber value="${info.totaltime/60/60-0.49}" maxFractionDigits="0" />小时
											</c:if>
											<fmt:formatNumber value="${info.totaltime/60%60}" maxFractionDigits="0" />分钟
										</td>
										<td>
											<div class="data-operion">
												<a href="view/${info.serviceid}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
											</div>
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td align="center" colspan="15">暂无数据</td>
							</tr>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>

				<tags:pagination page="${infos}" paginationSize="10" />
			</div>
		</div>
	</div><!-- nav-tabs-custom -->
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
</html>
