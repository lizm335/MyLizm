<%@page import="java.util.Date"%>
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

	<section class="content-header">
		<ol class="breadcrumb">
			<li>
				<a href="javascript:;"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="javascript:;">考试管理</a>
			</li>
			<li class="active">开考科目</li>
		</ol>
	</section>

	<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li <c:if test="${subjectType eq '1' }">class="active"</c:if>>
					<a href="${ctx}/exam/new/plan/list?search_EQ_subjectType=1" target="_self">中央课程科目</a>
				</li>
				<li <c:if test="${subjectType eq '2' }">class="active"</c:if>>
					<a href="${ctx}/exam/new/plan/list?search_EQ_subjectType=2" target="_self">替换课程科目</a>
				</li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<form id="listForm" action="list">
						<input type="hidden" value="${subjectType}" name="search_EQ_subjectType" id="subjectType"/>
						<div class="box box-border">
							<div class="box-body">
								<div class="form-horizontal">
									<div class="row pad-t15">
										<div class="col-md-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">考试计划</label>
												<div class="col-sm-9">
													<select name="search_EQ_examBatchCode" id="search_EQ_examBatchCode" class="selectpicker show-tick form-control" data-size="5"
														data-live-search="true">
														<option value="">请选择</option>
														<c:forEach items="${batchMap}" var="map">
															<option value="${map.key}" <c:if test="${map.key==examBatchCode}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>

										<div class="col-md-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">开考科目名称</label>
												<div class="col-sm-9">
													<input class="form-control" type="text" name="search_LIKE_examPlanName" value="${param['search_LIKE_examPlanName']}">
												</div>
											</div>
										</div>

										<div class="col-md-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">课程</label>
												<div class="col-sm-9">
													<select name="search_EQ_gjtCourseList.courseId" class="selectpicker show-tick form-control" data-size="10" data-live-search="true">
														<option value="" selected="selected">请选择</option>
														<c:forEach items="${courseList}" var="c">
															<option value="${c.courseId}" <c:if test="${c.courseId==param['search_EQ_gjtCourseList.courseId']}">selected='selected'</c:if>>${c.kcmc}（课程号：${c.kch}）</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>

										<div class="col-md-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">试卷号</label>
												<div class="col-sm-9">
													<input class="form-control" type="text" name="search_LIKE_examNo" value="${param['search_LIKE_examNo']}">
												</div>
											</div>
										</div>
										<div class="col-md-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">状态</label>
												<div class="col-sm-9">
													<select name="search_EQ_status" id="pstatus" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
														<option value="" selected="selected">请选择</option>
														<option value="1" <c:if test="${param['search_EQ_status']==1}">selected='selected'</c:if>>未开始</option>
														<option value="2" <c:if test="${param['search_EQ_status']==2}">selected='selected'</c:if>>预约中</option>
														<option value="3" <c:if test="${param['search_EQ_status']==3}">selected='selected'</c:if>>待考试</option>
														<option value="4" <c:if test="${param['search_EQ_status']==4}">selected='selected'</c:if>>考试中</option>
														<option value="5" <c:if test="${param['search_EQ_status']==5}">selected='selected'</c:if>>已结束</option>
													</select>
												</div>
											</div>
										</div>
										<div class="col-md-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">考试形式</label>
												<div class="col-sm-9">
													<select name="search_EQ_type" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
														<option value="">请选择</option>
														<c:forEach items="${examTypeMap}" var="map">
															<option value="${map.key}" <c:if test="${map.key==param['search_EQ_type']}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</div>
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

						<div class="box">
							<!-- 按钮栏 start -->
							<div class="box-header with-border">
								<div class="fr">
									<shiro:hasPermission name="/exam/new/plan/list$autoExamPlan">
										<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-autoExamPlan"> 
											<i class="fa fa-fw fa-download"></i> 自动生成开考科目
										</a>

									</shiro:hasPermission>
									<shiro:hasPermission name="/exam/new/plan/list$export">
										<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-export margin_l10"> 
										<i class="fa fa-fw fa-download"></i> 导出开考科目
										</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="/exam/new/plan/list$import">
										<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-import margin_l10"> 
										<i class="fa fa-fw fa-upload"></i> 导入开考科目
										</a>

									</shiro:hasPermission>
									<shiro:hasPermission name="/exam/new/plan/list$create">
										<a href="create/0?examType=${exam_type}&subjectType=${subjectType}" class="btn btn-sm btn-default btn-add margin_l10"> 
										<i class="fa fa-fw fa-plus"></i> 新增开考科目
										</a>
									</shiro:hasPermission>
								</div>
							</div>
							<!-- 按钮栏 end -->

							<!-- 列表内容 start -->
							<div class="box-body">
								<div class="filter-tabs filter-tabs2 clearfix">
									<ul class="list-unstyled">
										<li lang=":input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${all})</li>
										<li value="1" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 1 }">class="actived"</c:if>>未开始(${notStart})</li>
										<li value="2" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 2 }">class="actived"</c:if>>预约中(${booking})</li>
										<li value="3" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 3 }">class="actived"</c:if>>待考试(${preExam})</li>
										<li value="4" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 4 }">class="actived"</c:if>>考试中(${examing})</li>
										<li value="5" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 5 }">class="actived"</c:if>>已结束(${end})</li>
									</ul>
								</div>
								<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
									<div class="row">
										<div class="col-sm-6"></div>
										<div class="col-sm-6"></div>
									</div>

									<div class="row">
										<div class="col-sm-12">
											<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
												<thead>
													<tr>
														<th>考试计划</th>
														<th>开考科目</th>
														<th>课程</th>
														<th>指定专业</th>
														<th>形考比例</th>
														<th>考试时间</th>
														<th>考试方式</th>
														<th>考试预约方式</th>
														<th>考试预约限制</th>
														<th>状态</th>
														<th>操作</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td>${entity.examBatchNew.name}<br> <span class="gray9">${entity.examBatchNew.examBatchCode}</span>
																</td>
																<td>科目名称：${entity.examPlanName} <br> 试卷号：${entity.examNo} <br> 考试形式：${examTypeMap[entity.type]}
																</td>
																<td><c:forEach items="${entity.gjtCourseList}" var="c">
																${c.kcmc}<span class="gray9">（${c.kch}）</span>
																		<br>
																	</c:forEach></td>
																<td><c:choose>
																		<c:when test="${fn:length(entity.gjtSpecialtyList) == 0}">
																	通用
																</c:when>
																		<c:otherwise>
																			<c:forEach items="${entity.gjtSpecialtyList}" var="s">
																		${s.zymc}<span class="gray9">(${s.ruleCode})</span>
																				<br>
																			</c:forEach>
																		</c:otherwise>
																	</c:choose></td>
																<td><c:choose>
																		<c:when test="${empty entity.xkPercent }">
																			<span class="text-orange">待定</span>
																		</c:when>
																		<c:otherwise>
																	${entity.xkPercent}%
																</c:otherwise>
																	</c:choose></td>
																<td><fmt:formatDate value="${entity.examSt}" type="date" pattern="yyyy-MM-dd HH:mm" /><br> 至<br> <fmt:formatDate
																		value="${entity.examEnd}" type="date" pattern="yyyy-MM-dd HH:mm" /></td>
																<td>${examStyleMap[entity.examStyle]}</td>
																<td><c:choose>
																		<c:when test="${entity.examPlanOrder == 1}">
																	个人预约
																</c:when>
																		<c:when test="${entity.examPlanOrder == 2}">
																	集体预约
																</c:when>
																	</c:choose></td>
																<td><c:choose>
																		<c:when test="${entity.examPlanLimit > 0}">
																	大于等于${entity.examPlanLimit}分
																</c:when>
																		<c:when test="${entity.examPlanLimit == 0}">
																	无分数限制
																</c:when>
																		<c:when test="${empty entity.examPlanLimit }">
																	--
																</c:when>
																	</c:choose></td>
																<td><c:choose>
																		<c:when test="${entity.status == 1}">
																			<span class="text-red">未开始</span>
																		</c:when>
																		<c:when test="${entity.status == 2}">
																			<span class="text-green">预约中</span>
																		</c:when>
																		<c:when test="${entity.status == 3}">
																			<span class="text-orange">待考试</span>
																		</c:when>
																		<c:when test="${entity.status == 4}">
																			<span class="gray9">考试中</span>
																		</c:when>
																		<c:when test="${entity.status == 5}">
									            					已结束
									            				</c:when>
																		<c:otherwise>--</c:otherwise>
																	</c:choose></td>
																<td>
																	<div class="data-operion">
																		<!-- 考试中也可以让修改，不然老是修改数据，要累死程序员 -->
																		<%--entity.status != 4 && --%>
																		<%--<c:if test="${entity.status != 5}">--%>
																		<shiro:hasPermission name="/exam/new/plan/list$update">
																			<a href="update/${entity.examPlanId}" class="operion-item operion-edit" title="编辑"> <i class="fa fa-fw fa-edit"></i>
																			</a>
																		</shiro:hasPermission>
																		<%--</c:if>--%>
																		<c:if test="${entity.status == 1}">
																			<shiro:hasPermission name="/exam/new/plan/list$delete">
																				<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${entity.examPlanId}" data-toggle="tooltip" title="删除">
																					<i class="fa fa-trash-o text-red"></i>
																				</a>
																			</shiro:hasPermission>
																		</c:if>
																	</div>
																</td>
															</tr>
														</c:if>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</div>
									<tags:pagination page="${pageInfo}" paginationSize="5" />
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
<%
	Date time = new Date();
%>
<script src="${ctx}/static/js/exam/exam_plan_list.js?time=<%=time%>"></script>

</html>
