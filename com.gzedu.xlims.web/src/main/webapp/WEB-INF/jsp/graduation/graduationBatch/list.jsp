<%@page import="com.gzedu.xlims.pojo.system.StudyYear"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%
	int nowCode = StudyYear.getNowCode();
	request.setAttribute("nowCode", nowCode);
%>

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
				<li><a href="#">论文管理</a></li>
				<li class="active">毕业批次</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-xs-6 col-md-4">
									<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">批次号</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_batchCode" value="${param.search_LIKE_batchCode}">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">批次名称</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_batchName" value="${param.search_LIKE_batchName}">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学期</label>
									<div class="col-sm-9">
										<select name="search_EQ_gradeId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${termMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gradeId']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-xs-6 col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">状态</label>
									<div class="col-sm-9">
										<select name="status" id="status" class="selectpicker form-control">
											<option value="0">全部</option>
											<option value="1" <c:if test="${param.status == 1}">selected="selected"</c:if> >未开始</option>
											<option value="2" <c:if test="${param.status == 2}">selected="selected"</c:if> >进行中</option>
											<option value="3" <c:if test="${param.status == 3}">selected="selected"</c:if> >已结束</option>
										</select>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer">
<!-- 						<div class="search-more-in">
							高级搜索<i class="fa fa-fw fa-caret-down"></i>
						</div> -->
						<div class="btn-wrap">
							<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="btn-wrap">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
				</div>
				
				
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">批次列表</h3>
						<div class="pull-right no-margin">
							<shiro:hasPermission name="/graduation/graduationBatch/list$create">
								<a href="create" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新增批次</a>
							</shiro:hasPermission>
						</div>
						
					</div>

					<div class="box-body">
								<div class="filter-tabs filter-tabs2 clearfix">
									<ul class="list-unstyled">
										<li lang=":input[name='status']" <c:if test="${empty param['status']}">class="actived"</c:if>>全部(${notStart + starting + end})</li>
										<li value="1" role=":input[name='status']" <c:if test="${param['status'] == 1 }">class="actived"</c:if>>未开始(${notStart})</li>
										<li value="2" role=":input[name='status']" <c:if test="${param['status'] == 2 }">class="actived"</c:if>>进行中(${starting})</li>
										<li value="3" role=":input[name='status']" <c:if test="${param['status'] == 3 }">class="actived"</c:if>>已结束(${end})</li>
									</ul>
								</div>
								<div class="table-responsive">
									<table class="table table-bordered table-hover table-font vertical-mid text-center">
										<thead>
											<tr>
												<th>批次号</th>
											    <th>批次名称</th>
											    <th>学期</th>
											    <th colspan="2">毕业论文时间安排</th>
											    <th colspan="2">社会实践时间安排</th>
											    <th>状态</th>
											    <th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td rowspan="7">${entity.batchCode}</td>
																<td rowspan="7">${entity.batchName}</td>
																<td rowspan="7">${termMap[entity.gradeId]} </td>
																<td>论文申请时间</td>
																<td>${entity.applyThesisBeginDt} ~ ${entity.applyThesisEndDt}</td>
																<td>社会实践申请时间</td>
																<td>${entity.applyPracticeBeginDt} ~ ${entity.applyPracticeEndDt}</td>
																<td rowspan="7">
																	<c:if test="${entity.status == 1}">
																		<span class="text-red">未开始</span>
																	</c:if>
																	<c:if test="${entity.status == 2}">
																		<span class="text-yellow">进行中</span>
																	</c:if>
																	<c:if test="${entity.status == 3}">
																		<span>已结束</span>
																	</c:if>
																</td>
																<td rowspan="7">
																	<c:if test="${entity.status == 1 || entity.status == 2}">
																		<shiro:hasPermission name="/graduation/graduationBatch/list$update">
																			<a href="update/${entity.batchId}"
																				class="operion-item operion-edit" data-toggle="tooltip" title="编辑">
																				<i class="fa fa-edit"></i></a> 
																		</shiro:hasPermission>
																		<shiro:hasPermission name="/graduation/graduationBatch/list$delete">
																			<a href="javascript:void(0);"
																				class="operion-item operion-del del-one" val="${entity.batchId}" data-toggle="tooltip"
																				title="删除" data-tempTitle="删除">
																				<i class="fa fa-fw fa-trash-o text-red"></i></a>
																		</shiro:hasPermission>
																	</c:if>
																	<c:if test="${entity.status == 3}">
																		--
																	</c:if>
																</td> 
															</tr>
															<tr>
																<td>提交开题截止时间</td>
																<td>${entity.submitProposeEndDt}</td>
																<td>初稿提交截止时间</td>
																<td>${entity.submitPracticeEndDt}</td>
															</tr>
															<tr>
																<td>开题定稿截止时间</td>
																<td>${entity.confirmProposeEndDt}</td>
																<td>实践定稿截止时间</td>
																<td>${entity.confirmPracticeEndDt}</td>
															</tr>
															<tr>
																<td>初稿提交截止时间</td>
																<td>${entity.submitThesisEndDt}</td>
																<td>实践写作评分时间</td>
																<td>${entity.reviewPracticeDt}</td>
															</tr>
															<tr>
																<td>论文定稿截止时间</td>
																<td>${entity.confirmThesisEndDt}</td>
																<td></td>
																<td></td>
															</tr>
															<tr>
																<td>论文初评时间</td>
																<td>${entity.reviewThesisDt}</td>
																<td></td>
																<td></td>
															</tr>
															<tr>
																<td>论文答辩时间</td>
																<td>${entity.defenceThesisDt}</td>
																<td></td>
																<td></td>
															</tr>
														</c:if>
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
								</div>
							<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>
		</form>
		</section>
		
		<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
		
</body>
</html>
