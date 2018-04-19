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
		<section class="content-header clearfix">
			<ol class="breadcrumb oh">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">论文管理</a></li>
				<li class="active">毕业安排</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-xs-6 col-md-4">
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
							<div class="col-xs-6 col-md-4">
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
							<div class="col-xs-6 col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">培养层次</label>
									<div class="col-sm-9">
										<select name="search_EQ_trainingLevel" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${trainingLevelMap}" var="trainingLevel">
												<option value="${trainingLevel.key}"  <c:if test="${trainingLevel.key==param['search_EQ_trainingLevel']}">selected='selected'</c:if>>${trainingLevel.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-xs-6 col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">状态</label>
									<div class="col-sm-9">
										<select name="search_EQ_status" id="status" class="selectpicker form-control">
											<option value="0">全部</option>
											<option value="1" <c:if test="${param.search_EQ_status == 1}">selected="selected"</c:if> >未开始</option>
											<option value="2" <c:if test="${param.search_EQ_status == 2}">selected="selected"</c:if> >进行中</option>
											<option value="3" <c:if test="${param.search_EQ_status == 3}">selected="selected"</c:if> >已结束</option>
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
				
				<div class="box margin-bottom-none">
					<div class="box-body">
						<div class="filter-tabs filter-tabs2 clearfix">
							<ul class="list-unstyled">
								<li lang=":input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${notStart + starting + end})</li>
								<li value="1" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 1 }">class="actived"</c:if>>未开始(${notStart})</li>
								<li value="2" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 2 }">class="actived"</c:if>>进行中(${starting})</li>
								<li value="3" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 3 }">class="actived"</c:if>>已结束(${end})</li>
							</ul>
						</div>
						<div class="table-responsive">
									<table class="table table-bordered table-hover table-font table-striped vertical-mid text-center">
										<thead>
											<tr>
												<th>论文批次</th>
											    <th>专业</th>
											    <th>培养层次</th>
											    <th>论文申请人数</th>
											    <th>答辩人数</th>
											    <th>社会实践申请人数</th>
											    <th>通过/未通过</th>
											    <th>论文指导老师</th>
											    <th>论文答辩老师</th>
											    <th>社会实践老师</th>
											    <th>状态</th>
											    <th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<c:set var="applyThesisCount" value="0"></c:set>
															<c:set var="defenceThesisCount" value="0"></c:set>
															<c:set var="applyPracticeCount" value="0"></c:set>
															
															<c:set var="pass" value="0"></c:set>
															<c:set var="noPass" value="0"></c:set>
															
															<c:set var="guideTeacherCount" value="0"></c:set>
															<c:set var="defenceTeacherCount" value="0"></c:set>
															<c:set var="practiceTeacherCount" value="0"></c:set>
														
															<c:if test="${not empty entity['applyList']}">
																<c:forEach items="${entity['applyList']}" var="apply">
																	<c:if test="${apply.applyType == 1}">
																		<c:set var="applyThesisCount" value="${applyThesisCount + 1}"></c:set>
																		
																		<c:if test="${apply.needDefence == 1}">
																			<c:set var="defenceThesisCount" value="${defenceThesisCount + 1}"></c:set>
																		</c:if>
																	</c:if>
																	
																	<c:if test="${apply.applyType == 2}">
																		<c:set var="applyPracticeCount" value="${applyPracticeCount + 1}"></c:set>
																	</c:if>
																	
																	<c:if test="${apply.status == 13}">
																		<c:set var="pass" value="${pass + 1}"></c:set>
																	</c:if>
																	<c:if test="${apply.status != 13}">
																		<c:set var="noPass" value="${noPass + 1}"></c:set>
																	</c:if>
																</c:forEach>
															</c:if>
															
															<c:if test="${not empty entity['adviserList']}">
																<c:forEach items="${entity['adviserList']}" var="adviser">
																	<c:if test="${adviser.adviserType == 1}">
																		<c:set var="guideTeacherCount" value="${guideTeacherCount + 1}"></c:set>
																	</c:if>
																	<c:if test="${adviser.adviserType == 2}">
																		<c:set var="defenceTeacherCount" value="${defenceTeacherCount + 1}"></c:set>
																	</c:if>
																	<c:if test="${adviser.adviserType == 3}">
																		<c:set var="practiceTeacherCount" value="${practiceTeacherCount + 1}"></c:set>
																	</c:if>
																</c:forEach>
															</c:if>
															
															<tr>
																<td>
																	${entity['batchName']}<br>
																	${entity['batchCode']}
																</td>
																<td>${entity['specialtyName']}</td>
																<td>${entity['trainingName']}</td>
																<td>${applyThesisCount}</td>
																<td>
																	<c:if test="${entity['trainingLevel'] == 2 || entity['trainingLevel'] == 4}">
																		${defenceThesisCount}
																	</c:if>
																	<c:if test="${entity['trainingLevel'] != 2 && entity['trainingLevel'] != 4}">
																		--
																	</c:if>
																</td>
																<td>${applyPracticeCount}</td>
																<td>${pass}/${noPass}</td>
																<c:choose>
																	<c:when test="${not empty entity['adviserList']}">
																		<td>${guideTeacherCount}</td>
																		<td>${defenceTeacherCount}</td>
																		<td>${practiceTeacherCount}</td>
																	</c:when>
																	<c:otherwise>
																		<td class="text-red">未设置</td>
																		<td class="text-red">未设置</td>
																		<td class="text-red">未设置</td>
																	</c:otherwise>
																</c:choose>
																<td>
																	<c:if test="${entity['status'] == 1}">
																		<span class="text-red">未开始</span>
																	</c:if>
																	<c:if test="${entity['status'] == 2}">
																		<span class="text-light-blue">进行中</span>
																	</c:if>
																	<c:if test="${entity['status'] == 3}">
																		<span>已结束</span>
																	</c:if>
																</td>
																<td>
																	<div class="data-operion">
																		<shiro:hasPermission name="/graduation/specialty/list$update">
																			<c:if test="${entity['status'] == 1 || entity['status'] == 2}">
																				<a href="update?batchId=${entity['batchId']}&specialtyId=${entity['specialtyId']}&trainingLevel=${entity['trainingLevel']}&specialtyName=${entity['specialtyName']}&trainingName=${entity['trainingName']}&gradeId=${entity['gradeId']}&gradeName=${entity['gradeName']}"
																					class="operion-item"  data-toggle="tooltip" title="设置">
																					<i class="fa fa-fw fa-gear"></i></a> 
																			</c:if>
																		</shiro:hasPermission>
																		<c:if test="${entity['status'] == 3}">
																			--
																		</c:if>
																	</div>
																</td> 
															</tr>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr>
														<td align="center" colspan="12">暂无数据</td>
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
