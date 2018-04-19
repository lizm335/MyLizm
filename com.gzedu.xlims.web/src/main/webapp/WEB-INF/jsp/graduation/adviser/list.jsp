<%@page import="com.gzedu.xlims.pojo.system.StudyYear"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
})
</script>

</head>
<body class="inner-page-body">
		
		<!-- Main content -->
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">论文管理</a></li>
				<li class="active">毕业老师</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<input type="hidden" name="adviserType" value="${param.adviserType}">
				<div class="box">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">账号</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_loginAccount" value="${param.search_LIKE_loginAccount}">
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">姓名</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_teacherName" value="${param.search_LIKE_teacherName}">
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">论文批次</label>
									<div class="col-sm-9">
										<select name="search_EQ_batchId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${batchMap}" var="batch">
												<option value="${batch.key}"  <c:if test="${batch.key==param['search_EQ_batchId']}">selected='selected'</c:if>>${batch.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业</label>
									<div class="col-sm-9">
										<select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${specialtyMap}" var="specialty">
												<option value="${specialty.key}"  <c:if test="${specialty.key==param['search_EQ_specialtyId']}">selected='selected'</c:if>>${specialty.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer">
						<div class="pull-right">
							<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="pull-right margin_r15">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
				</div>
				
				
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="nav-tabs-custom">
					<ul class="nav nav-tabs nav-tabs-lg">
				      <li <c:if test="${param.adviserType == 1}">class="active"</c:if>><a href="list?adviserType=1" target="_self">论文指导老师</a></li>
				      <li <c:if test="${param.adviserType == 2}">class="active"</c:if>><a href="list?adviserType=2" target="_self">论文答辩老师</a></li>
				      <li <c:if test="${param.adviserType == 3}">class="active"</c:if>><a href="list?adviserType=3" target="_self">社会实践老师</a></li>
				    </ul>
				    <div class="tab-content">
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
									<tr>
										<th>论文批次</th>
									    <th>账号</th>
									    <th>姓名</th>
									    <th>指导专业</th>
									    <th>
									    	<c:choose>
									    		<c:when test="${param.adviserType == 2}">答辩学员总数</c:when>
									    		<c:otherwise>指导学员总数</c:otherwise>
									    	</c:choose>
									    </th>
									    <th>已通过学员</th>
									    <th>未通过学员</th>
									    <th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${not empty pageInfo.content}">
											<c:forEach items="${pageInfo.content}" var="entity">
												<c:if test="${not empty entity}">
													<tr>
														<td>
															${entity.batchName}<br/>
															<span class="gray9">（${entity.batchCode}）</span>
														</td>
														<td>
															${entity.loginAccount}
														</td>
														<td>
															${entity.teacherName}
														</td>
														<td>
															<c:set value="${fn:split(entity.specialtyNames, ',')}" var="specialtyNames" />
															<c:forEach items="${specialtyNames}" var="specialtyName">
																${specialtyName} <br />
															</c:forEach>
														</td>
														<td>
															${entity.totalCount}
														</td>
														<td>
															${entity.completedCount}
														</td>
														<td>
															${entity.noCompletedCount}
														</td>
														<td>
															<shiro:hasPermission name="/graduation/adviser/list?adviserType=1$view">
																<c:choose>
														    		<c:when test="${param.adviserType == 1}">
														    			<a href="viewStudentList?teacherId=${entity.teacherId}&applyType=1&teacherName=${entity.teacherName}"
																			class="operion-item operion-view" data-toggle="tooltip" title="查看">
																			<i class="fa fa-fw fa-view-more"></i></a> 
														    		</c:when>
														    		<c:when test="${param.adviserType == 3}">
														    			<a href="viewStudentList?teacherId=${entity.teacherId}&applyType=2&teacherName=${entity.teacherName}"
																			class="operion-item operion-view" data-toggle="tooltip" title="查看">
																			<i class="fa fa-fw fa-view-more"></i></a> 
														    		</c:when>
														    	</c:choose>
													    	</shiro:hasPermission>
														</td> 
													</tr>
												</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td align="center" colspan="8">暂无数据</td>
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
</body>
</html>
