<%@page import="com.gzedu.xlims.pojo.system.StudyYear"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<c:set var="ctx">${pageContext.request.contextPath}</c:set>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
		$(function() {
			$('#search_EQ_status2').change(function () {
				$("#status").val($(this).val());
			});
		})
		function choiceXJ(flag){
			//$('#listForm .btn-reset').trigger('click');
			$("#status").val('');
			$("#status").val(flag);
			$("#listForm").submit();
		}
	</script>
</head>
<body class="inner-page-body">
		
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">毕业管理</a></li>
			<li class="active">毕业计划</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal">
			<input id="status" type="hidden" name="search_EQ_status" value="${param.search_EQ_status}">
			<div class="box box-border">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">计划编号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_graPlanNo" value="${param['search_EQ_graPlanNo']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">计划名称</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_graPlanTitle" value="${param['search_LIKE_graPlanTitle']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学期</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${gradeMap}" var="grade">
											<option value="${grade.key}"  <c:if test="${grade.key==param['search_EQ_gjtGrade.gradeId']}">selected='selected'</c:if>>${grade.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">状态</label>
								<div class="col-sm-9">
									<select id="search_EQ_status2" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<option value="3" <c:if test="${param.search_EQ_status == '3'}">selected='selected'</c:if>>未开始</option>
										<option value="4" <c:if test="${param.search_EQ_status == '4'}">selected='selected'</c:if>>进行中</option>
										<option value="5" <c:if test="${param.search_EQ_status == '5'}">selected='selected'</c:if>>已结束</option>
									</select>
								</div>
							</div>
						</div>
					</div>

				</div><!-- /.box-body -->
				<div class="box-footer text-right">
					<button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
					<button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
				</div><!-- /.box-footer-->
			</div>
				
			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
			<div class="box box-border margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">信息列表</h3>
					<shiro:hasPermission name="/graduation/plan/list$create">
						<div class="pull-right no-margin">
							<a href="create" class="btn btn-default btn-sm margin_l10" data-role="import"><i class="fa fa-fw fa-plus"></i> 新增计划</a>
						</div>
				   </shiro:hasPermission>
				</div>

				<div class="box-body">
					<div class="filter-tabs clearfix">
						<ul class="list-unstyled">
							<li <c:if test="${empty param.search_EQ_status}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${not empty countAuditStateMap[''] ? countAuditStateMap[''] : 0})</li>
							<li <c:if test="${param.search_EQ_status == '3'}">class="actived"</c:if> onclick="choiceXJ('3')">未开始(${not empty countAuditStateMap['3'] ? countAuditStateMap['3'] : 0})</li>
							<li <c:if test="${param.search_EQ_status == '4'}">class="actived"</c:if> onclick="choiceXJ('4')">进行中(${not empty countAuditStateMap['4'] ? countAuditStateMap['4'] : 0})</li>
							<li <c:if test="${param.search_EQ_status == '5'}">class="actived"</c:if> onclick="choiceXJ('5')">已结束(${not empty countAuditStateMap['5'] ? countAuditStateMap['5'] : 0})</li>
							<li <c:if test="${param.search_EQ_status == '0'}">class="actived"</c:if> onclick="choiceXJ('0')">待审核(${not empty countAuditStateMap['0'] ? countAuditStateMap['0'] : 0})</li>
							<li <c:if test="${param.search_EQ_status == '2'}">class="actived"</c:if> onclick="choiceXJ('2')">审核不通过(${not empty countAuditStateMap['2'] ? countAuditStateMap['2'] : 0})</li>
						</ul>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
								<tr>
									<th>计划编号</th>
									<th>计划名称</th>
									<th>学期</th>
									<th>毕业时间安排</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.content}" var="info">
											<tr>
													<%-- <td><input type="checkbox" value="${student.studentId }" name="ids" class="checkbox"></td> --%>
												<td>
														${info.graPlanNo}
												</td>
												<td class="text-center">
														${info.graPlanTitle}
												</td>
												<td>
														${info.gjtGrade.gradeName}
												</td>
												<td class="text-center">
													<table class="table table-bordered" style="margin-bottom: 0px;">
														<tr>
															<td>毕业申请时间</td>
															<td><fmt:formatDate value="${info.graApplyBeginDt}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${info.graApplyEndDt}" pattern="yyyy-MM-dd" /></td>
														</tr>
														<tr>
															<td>学位申请时间</td>
															<td><fmt:formatDate value="${info.degreeApplyBeginDt}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${info.degreeApplyEndDt}" pattern="yyyy-MM-dd" /></td>
														</tr>
														<tr>
															<td>毕业审核时间</td>
															<td><fmt:formatDate value="${info.graAuditBeginDt}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${info.graAuditEndDt}" pattern="yyyy-MM-dd" /></td>
														</tr>
														<tr>
															<td>学位审核时间</td>
															<td><fmt:formatDate value="${info.degreeAuditBeginDt}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${info.degreeAuditEndDt}" pattern="yyyy-MM-dd" /></td>
														</tr>
														<tr>
															<td>学位意向退回时间</td>
															<td><fmt:formatDate value="${info.degreeBackBeginDt}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${info.degreeBackEndDt}" pattern="yyyy-MM-dd" /></td>
														</tr>
														<tr>
															<td>毕业证书领取时间</td>
															<td><fmt:formatDate value="${info.graCertReceiveBeginDt}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${info.graCertReceiveEndDt}" pattern="yyyy-MM-dd" /></td>
														</tr>
														<tr>
															<td>毕业档案领取时间</td>
															<td><fmt:formatDate value="${info.graArchivesReceiveBeginDt}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${info.graArchivesReceiveEndDt}" pattern="yyyy-MM-dd" /></td>
														</tr>
														<tr>
															<td>学位证书领取时间</td>
															<td><fmt:formatDate value="${info.degreeCertReceiveBeginDt}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${info.degreeCertReceiveEndDt}" pattern="yyyy-MM-dd" /></td>
														</tr>
													</table>
												</td>
												<td class="text-center">
													<c:choose>
														<c:when test="${info.status==2}"><span class="text-red">审核不通过</c:when>
														<c:when test="${info.status==3}"><span class="text-green">未开始</span></c:when>
														<c:when test="${info.status==4}"><span class="text-green">进行中</span></c:when>
														<c:when test="${info.status==5}"><span class="text-green">已结束</span></c:when>
														<c:otherwise>
															<span class="text-orange">待审核</span>
														</c:otherwise>
													</c:choose>
												</td>
												<td class="text-center">
													<c:if test="${info.auditState==0}">
														<shiro:hasPermission name="/graduation/plan/list$approval">
															<a href="view/${info.id}?action=audit" class="operion-item" data-toggle="tooltip" title="审核毕业计划"><i class="fa fa-fw fa-shxxjl"></i></a>
														</shiro:hasPermission>
													</c:if>
													<shiro:hasPermission name="/graduation/plan/list$view">
														<a href="view/${info.id}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
													</shiro:hasPermission>
													<c:if test="${info.auditState==0||info.auditState==2}">
														<shiro:hasPermission name="/graduation/plan/list$update">
															<a href="update/${info.id}" class="operion-item" data-toggle="tooltip" title="修改毕业计划"><i class="fa fa-fw fa-edit"></i></a>
														</shiro:hasPermission>
													</c:if>
													<shiro:hasPermission name="/graduation/plan/list$delete">
														<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${info.id}" title="删除" data-temptitle="删除"><i class="fa fa-fw fa-trash-o"></i></a>
													</shiro:hasPermission>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td align="center" colspan="9">暂无数据</td>
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
 
 	<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>
