<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-学籍计划</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
	$(function() {

	})
	function choiceXJ(flag){
		//$('#listForm .btn-reset').trigger('click');
		$("#auditState").val('');
		$("#auditState").val(flag);
		$("#listForm").submit();
	}
	</script>
</head>
<body class="inner-page-body">
	<section class="content-header clearfix">
		<ol class="breadcrumb oh">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">学籍管理</a></li>
			<li class="active">学籍计划</li>
		</ol>
	</section>

	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html" method="post">
		<input id="auditState" type="hidden" name="search_EQ_auditState" value="${param.search_EQ_auditState}">
		<div class="box">
			<div class="box-body">
				<div class="row pad-t15">
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">计划编号</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_EQ_rollPlanNo" value="${param.search_EQ_rollPlanNo }">
							</div>
						</div>
					</div>
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">学期</label>
							<div class="col-sm-9">
								<select  name="search_EQ_gjtGrade.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==((not empty param['search_EQ_gjtGrade.gradeId']) ? param['search_EQ_gjtGrade.gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</div>

			</div><!-- /.box-body -->
			<div class="box-footer">
				<div class="pull-right">
					<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
				</div>
				<div class="pull-right margin_r15">
					<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
				</div>
			</div><!-- /.box-footer-->
		</div>

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">计划列表</h3>
					<div class="btn-wrap fl">
						<shiro:hasPermission name="/edumanage/rollPlan/list$create">
						<a href="create" class="btn btn-default btn-sm btn-add">
							<i class="fa fa-fw fa-plus"></i> 新增学籍计划</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="box-body">
					<div class="filter-tabs clearfix">
						<ul class="list-unstyled">
							<li <c:if test="${empty param.search_EQ_auditState}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${not empty countAuditStateMap[''] ? countAuditStateMap[''] : 0})</li>
							<li <c:if test="${param.search_EQ_auditState == '0'}">class="actived"</c:if> value="0" onclick="choiceXJ('0')">待审核(${not empty countAuditStateMap['0'] ? countAuditStateMap['0'] : 0})</li>
							<li <c:if test="${param.search_EQ_auditState == '2'}">class="actived"</c:if> value="2" onclick="choiceXJ('2')">审核不通过(${not empty countAuditStateMap['2'] ? countAuditStateMap['2'] : 0})</li>
							<li <c:if test="${param.search_EQ_auditState == '1'}">class="actived"</c:if> value="1" onclick="choiceXJ('1')">已发布(${not empty countAuditStateMap['1'] ? countAuditStateMap['1'] : 0})</li>
						</ul>
					</div>
					<div class="table-responsive" style="overflow: hidden">
						<table class="table table-bordered table-striped vertical-mid table-font">
							<thead>
								<tr>
									<!-- <th><input type="checkbox" class="select-all" id="selectAll"></th> -->
									<th class="text-center">学期</th>
									<th class="text-center">计划编号</th>
									<th class="text-center">计划名称</th>
									<th class="text-center">正式学籍注册时间</th>
									<th class="text-center">跟读学籍注册时间</th>
									<th class="text-center">状态</th>
									<th class="text-center">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageInfo.content }" var="info">
									<tr>
										<%-- <td><input type="checkbox" value="${student.studentId }" name="ids" class="checkbox"></td> --%>
										<td>
											${info.gjtGrade.gradeName}
										</td>
										<td>
											${info.rollPlanNo}
										</td>
										<td class="text-center">
											${info.rollPlanTitle}
										</td>
										<td class="text-center">
											<table class="table table-bordered" style="margin-bottom: 0px;">
												<tr>
													<td>时间1</td>
													<td><fmt:formatDate value="${info.officialBeginDt}" pattern="yyyy-MM-dd HH:mm" /> ~ <fmt:formatDate value="${info.officialEndDt}" pattern="yyyy-MM-dd HH:mm" /></td>
												</tr>
												<tr>
													<td>时间2</td>
													<td><fmt:formatDate value="${info.officialBeginDt2}" pattern="yyyy-MM-dd HH:mm" /> ~ <fmt:formatDate value="${info.officialEndDt2}" pattern="yyyy-MM-dd HH:mm" /></td>
												</tr>
											</table>
										</td>
										<td class="text-center">
											<fmt:formatDate value="${info.followBeginDt}" pattern="yyyy-MM-dd HH:mm" /> ~ <fmt:formatDate value="${info.followEndDt}" pattern="yyyy-MM-dd HH:mm" />
										</td>
										<td class="text-center">
											<c:choose>
												<c:when test="${info.auditState==1}"><span class="text-green">已发布</span></c:when>
												<c:when test="${info.auditState==2}"><span class="text-red">审核不通过</c:when>
												<c:otherwise>
													<span class="text-orange">待审核</span>
												</c:otherwise>
											</c:choose>
										</td>
										<td class="text-center">
											<c:if test="${info.auditState==0}">
												<shiro:hasPermission name="/edumanage/rollPlan/list$approval">
												<a href="view/${info.id}?action=audit" class="operion-item" data-toggle="tooltip" title="审核学籍计划"><i class="fa fa-fw fa-shxxjl"></i></a>
												</shiro:hasPermission>
											</c:if>
											<c:if test="${info.auditState==0||info.auditState==2}">
												<shiro:hasPermission name="/edumanage/rollPlan/list$update">
												<a href="update/${info.id}?action=update" class="operion-item" data-toggle="tooltip" title="修改学籍计划"><i class="fa fa-fw fa-edit"></i></a>
												</shiro:hasPermission>
											</c:if>
											<shiro:hasPermission name="/edumanage/rollPlan/list$view">
											<a href="view/${info.id}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
											</shiro:hasPermission>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>
			</div>
		</form>
	</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>
