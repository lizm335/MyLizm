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
		<li><a href="${ctx}/admin/workStatistics/list">工作统计</a></li>
		<li class="active">工作统计详情</li>
	</ol>
</section>

<section class="content">
	<div class="box">
		<div class="box-body">
			<input type="hidden" name="userId" value="${info.id}" />
			<div class="media pad">
				<div class="media-left" style="padding-right:25px;">
					<img id ="headImgId" src="${not empty employeeInfo.zp ? employeeInfo.zp : ctx.concat('/static/images/headImg04.png')}" class="img-circle" style="width:112px;height:112px;" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'">
					<a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
						<i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
            <span class="gray9">
            交流
            </span>
					</a>
				</div>
				<div class="media-body">
					<h3 class="margin_t10">
						${info.realName}
					</h3>
					<div class="row">
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>帐号:</b> <span>${info.loginAccount}</span>
						</div>
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>手机:</b>
							<span>${info.sjh}</span>
						</div>
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>角色:</b> <span>${info.roleName}</span>
						</div>
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>院校:</b> <span>${info.orgName}</span>
						</div>
					</div>
				</div>
			</div>

		</div>
		<div class="box-footer">
			<table class="table no-border no-margin">
				<tbody>
				<tr>
					<td>
						<div class="f24 text-center">${info.totalTodo}</div>
						<div class="text-center gray6">待办事项</div>
					</td>
					<td style="border-left:1px solid #e5e5e5">
						<div class="f24 text-center"><fmt:formatNumber value="${info.reversionRate*100}" pattern="0" />%</div>
						<div class="text-center gray6">答疑回复率</div>
					</td>
					<td style="border-left:1px solid #e5e5e5">
						<div class="f24 text-center"><fmt:formatNumber value="${info.reversionInTimeRate*100}" pattern="0" />%</div>
						<div class="text-center gray6">答疑回复及时率</div>
					</td>
					<td style="border-left:1px solid #e5e5e5">
						<div class="f24 text-center">${info.loginCount}</div>
						<div class="text-center gray6">登录总次数</div>
					</td>
					<td style="border-left:1px solid #e5e5e5">
						<div class="f24 text-center"><fmt:formatNumber value="${info.totalLoginTime*0.1*10/60}" pattern="0.0" />小时</div>
						<div class="text-center gray6">登录总时长</div>
					</td>
					<td style="border-left:1px solid #e5e5e5">
						<div class="f24 text-center" style="line-height:1">
							<fmt:formatDate value="${info.lastLoginTime}" pattern="yyyy-MM-dd" /><br>
							<small class="f14"><fmt:formatDate value="${info.lastLoginTime}" pattern="HH:mm:ss" /></small>
						</div>
						<div class="text-center gray6">最近登录日期</div>
					</td>
					<c:if test="${info.isOnline=='Y'}">
					<td style="border-left:1px solid #e5e5e5">
						<div class="f24 text-center text-green">
							在线
						</div>
						<div class="text-center gray6">当前在线状态</div>
					</td>
					</c:if>
					<c:if test="${info.isOnline=='N'}">
					<td style="border-left:1px solid #e5e5e5">
						<div class="f24 text-center" style="line-height:1">
							离线<br>
							<small class="f14">${empty info.leftDay ? '（从未登录）' : '（'.concat(info.leftDay).concat('天未登录）')}</small>
						</div>
						<div class="text-center gray6">当前在线状态</div>
					</td>
					</c:if>
				</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div class="box no-border margin-bottom-none">
		<div class="nav-tabs-custom no-margin">
			<shiro:hasPermission name="/admin/workStatistics/list$exportWorkStatisticsDetail">
				<div class="pull-right pad-r10 pad-t10">
					<a href="${ctx}/excelExport/validateSmsCode/1?formAction=/admin/workStatistics/exportWorkStatisticsDetail" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出工作详情</a>
				</div>
			</shiro:hasPermission>
			<ul class="nav nav-tabs nav-tabs-lg">
				<li class="active"><a href="#tab_notice_1" data-toggle="tab">工作统计</a></li>
				<li><a href="#tab_notice_2" data-toggle="tab">工作动态</a></li>
				<li><a href="#tab_notice_3" data-toggle="tab">登录统计</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="tab_notice_1">
					<h3 class="cnt-box-title f16 text-bold margin_b10">待办事项</h3>
					<table class="table-gray-th text-center">
						<thead>
						<tr>
							<th>代办项</th>
							<th>待办内容</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach var="item" items="${todoList}">
						<tr>
							<td>${item.title}</td>
							<td>${item.total} 个</td>
						</tr>
						</c:forEach>
						</tbody>
					</table>

					<h3 class="cnt-box-title f16 text-bold margin_b10 margin_t20">答疑服务</h3>
					<table class="table-gray-th text-center">
						<thead>
						<tr>
							<th>待解答</th>
							<th>已解答</th>
							<th>答疑总数</th>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td>
								<a href="${ctx}/lcmsubject/homeList.html?type=N&userId=${info.id}" class="text-underline" data-role="view-more">${countMutualSubject.totalNotReversion}</a>
								<div class="text-orange">
									<small>
										（${countMutualSubject.totalNotReversionInTimeOut} 个超过24小时未回答）
									</small>
								</div>
							</td>
							<td>
								<a href="${ctx}/lcmsubject/homeList.html?type=Y&userId=${info.id}" class="text-underline" data-role="view-more">${countMutualSubject.totalReversion}</a>
								<div class="text-orange">
									<small>
										（${countMutualSubject.totalReversionInTimeOut} 个超过24小时解答）
									</small>
								</div>
							</td>
							<td>
								<a href="${ctx}/lcmsubject/homeList.html?type=N&userId=${info.id}" class="text-underline" data-role="view-more">${countMutualSubject.total}</a>
							</td>
						</tr>
						</tbody>
					</table>
				</div>
				<div class="tab-pane " id="tab_notice_2">
					<div class="box no-border no-shadow no-margin">
						<div class="box-header with-border no-pad-left no-pad-right">
							<div class="row stu-info-status">
								<div class="col-xs-3">
									<div class="pad-t10">
										<input name="datetime" class="form-control" type="text" data-role="datetime" value="${datetime}">
									</div>
								</div>
								<div class="col-xs-3">
									<div id="earliestLoginDt" class="f24 text-center"></div>
									<div class="text-center gray6">登录时间</div>
								</div>
								<div class="col-xs-3">
									<div id="latestLogoutDt" class="f24 text-center"></div>
									<div class="text-center gray6">退出时间</div>
								</div>
								<div class="col-xs-3">
									<div id="totalLoginTime" class="f24 text-center"></div>
									<div class="text-center gray6">在线时长</div>
								</div>
							</div>
						</div>
						<div class="box-body">
							<div class="approval-list approval-list-2 approval-list-asc clearfix margin_b20 margin_t10">

							</div>
						</div>
					</div>
				</div>
				<div class="tab-pane" id="tab_notice_3">
					<h3 class="cnt-box-title f16 text-bold margin_b10 margin_t20">登录情况</h3>
					<div class="embed-responsive embed-responsive-16by9">
						<iframe src="${ctx }/admin/workStatistics/viewLoginLog/${info.id}" class="embed-responsive-item" frameborder="0" scrolling="auto" allowtransparency="true"></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/home/workStatistics/work_statistics_view.js"></script>
</body>
</html>
