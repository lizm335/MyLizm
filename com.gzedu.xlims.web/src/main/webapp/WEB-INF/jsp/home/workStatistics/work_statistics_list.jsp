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
		<li><a href="#">首页</a></li>
		<li class="active">工作统计</li>
	</ol>
</section>

<section class="content">
	<form id="listForm" class="form-horizontal" method="post">
		<input type="hidden" name="search_STUDY_STATUS" value="${param.search_STUDY_STATUS}" id="study_status">
		<div class="box">
			<div class="box-body">

				<div class="row pad-t15">
					<div class="col-md-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">帐号</label>
							<div class="col-sm-9">
								<input class="form-control" type="text" name="search_LIKE_loginAccount" value="${param.search_LIKE_loginAccount}">
							</div>
						</div>
					</div>
					<div class="col-md-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">姓名</label>
							<div class="col-sm-9">
								<input class="form-control" type="text" name="search_LIKE_realName" value="${param.search_LIKE_realName}">
							</div>
						</div>
					</div>
					<div class="col-md-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">角色</label>
							<div class="col-sm-9">
								<select name="search_EQ_priRoleInfo.roleId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">全部</option>
									<c:forEach items="${roles}" var="role">
										<option value="${role.key}" <c:if test="${role.key == param['search_EQ_priRoleInfo.roleId']}">selected = 'selected'</c:if>>${role.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="box-footer text-right">
				<button type="submit" class="btn min-width-90px btn-primary margin_r10" id="submit_buttion">搜索</button>
				<button type="reset" class="btn min-width-90px btn-default">重置</button>
			</div>
		</div>

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title pad-t5">计划列表</h3>
				<shiro:hasPermission name="/admin/workStatistics/list$exportWorkStatistics">
					<div class="pull-right no-margin">
						<a href="${ctx}/excelExport/validateSmsCode/${pageInfo.totalElements}?formAction=/admin/workStatistics/exportWorkStatistics" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出工作统计表</a>
					</div>
				</shiro:hasPermission>
				<shiro:hasPermission name="/admin/workStatistics/list$exportWorkStatisticsDetail">
					<div class="pull-right no-margin">
						<a href="${ctx}/excelExport/validateSmsCode/${pageInfo.totalElements}?formAction=/admin/workStatistics/exportWorkStatisticsDetail" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出工作统计明细表</a>
					</div>
				</shiro:hasPermission>
				<shiro:hasPermission name="/admin/workStatistics/list$exportLoginSituation">
					<div class="pull-right no-margin">
						<a href="${ctx}/admin/workStatistics/exportLoginSituation/${pageInfo.totalElements}?formAction=/admin/workStatistics/exportLoginSituation" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出登录情况明细表</a>
					</div>
				</shiro:hasPermission>
			</div>

			<div class="box-body">
				<div class="filter-tabs clearfix">
					<ul class="list-unstyled">
						<li <c:if test="${empty param.search_STUDY_STATUS}">class="actived"</c:if> id="data_btn_all">全部<c:if test="${not empty totalAll}">(${totalAll})</c:if></li>
						<li <c:if test="${ param.search_STUDY_STATUS eq '0'}">class="actived"</c:if> id="data_btn_1">在线<c:if test="${not empty total0}">(${total0})</c:if></li>
						<li <c:if test="${ param.search_STUDY_STATUS eq '1'}">class="actived"</c:if> id="data_btn_7">7天以上未登录<c:if test="${not empty total1}">(${total1})</c:if></li>
						<li <c:if test="${ param.search_STUDY_STATUS eq '2'}">class="actived"</c:if> id="data_btn_7_3">3天以上未登录<c:if test="${not empty total2}">(${total2})</c:if></li>
						<li <c:if test="${ param.search_STUDY_STATUS eq '3'}">class="actived"</c:if> id="data_btn_3_0">3天内未登录<c:if test="${not empty total3}">(${total3})</c:if></li>
						<li <c:if test="${ param.search_STUDY_STATUS eq '4'}">class="actived"</c:if> id="data_btn_0">从未登录<c:if test="${not empty total4}">(${total4})</c:if></li>
					</ul>
				</div>
				<div class="table-responsive">
					<table class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
						<thead>
							<tr>
								<th>用户信息</th>
								<th>角色</th>
								<th>待办事项</th>
								<th>答疑回复率</th>
								<th>答疑回复及时率</th>
								<th>登录总次数</th>
								<th>登录总时长</th>
								<th>最近登录日期</th>
								<th>当前在线状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="info">
									<tr>
										<td>
											<div class="text-left">
												姓名：${info.realName}<br>
												帐号：${info.loginAccount}<br>
												手机号：${info.sjh}<br>
												院校：${info.orgName}
											</div>
										</td>
										<td>${info.roleName}</td>
										<td>${info.totalTodo}</td>
										<td><fmt:formatNumber value="${info.reversionRate*100}" pattern="0" />%</td>
										<td><fmt:formatNumber value="${info.reversionInTimeRate*100}" pattern="0" />%</td>
										<td>${info.loginCount}</td>
										<td><fmt:formatNumber value="${info.totalLoginTime*0.1*10/60}" pattern="0.0" />小时</td>
										<td><fmt:formatDate value="${info.lastLoginTime}" type="both" /></td>
										<c:if test="${info.isOnline=='Y'}">
										<td class="text-green">在线</td>
										</c:if>
										<c:if test="${info.isOnline=='N'}">
										<td class="gray9">
											离线 <br>
											${empty info.leftDay ? '（从未登录）' : '（'.concat(info.leftDay).concat('天未登录）')}
										</td>
										</c:if>
										<td>
											<div class="data-operion">
												<shiro:hasPermission name="/admin/workStatistics/list$view">
													<a href="view/${info.id}" class="operion-item operion-view" val="${info.id}" data-toggle="tooltip" title="查看">
														<i class="fa fa-fw fa-view-more"></i>
													</a>
												</shiro:hasPermission>
											</div>
										</td>
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
			</div>
		</div>
	</form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/home/workStatistics/work_statistics_list.js"></script>
</body>
</html>
