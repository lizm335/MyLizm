<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任平台</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<div class="pull-left">
	    	您所在位置：
	  	</div>
		<ol class="breadcrumb">
			<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="javascript:;">考试组织</a></li>
			<li class="active">考试计划</li>
		</ol>
	</section>
		<section class="content">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li class="active"><a href="${ctx}/home/class/exam/batch/list" target="_self">考试计划</a></li>
					<li><a href="${ctx}/home/class/exam/plan/list" target="_self">开考科目</a></li>
					<li><a href="${ctx}/home/class/exam/appointment/list" target="_self">考试预约</a></li>
					<li><a href="${ctx}/home/class/exam/studentroom/list" target="_self">排考安排</a></li>
					<li><a href="${ctx}/home/class/exam/cost/list" target="_self">补考缴费</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab_top_1">
					<form id="listForm" class="form-horizontal">
					 	<input type="hidden" name="search_EQ_planStatus" value="">
						<!-- 搜索栏 start -->
						<div class="box box-border">
							<div class="box-body">
								<div class="row pad-t15">
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">计划编号</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="search_LIKE_examBatchCode" value="${param.search_LIKE_examBatchCode}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">计划名称</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="search_LIKE_name" value="${param.search_LIKE_name}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<label class="control-label col-sm-3">学期</label>
										<div class="col-sm-9">
											<select name="search_EQ_gradeId" class="selectpicker show-tick form-control" 
												data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${gradeMap}" var="map">
													<option value="${map.key}"<c:if test="${map.key == param.search_EQ_gradeId}">selected='selected'</c:if> >${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="box-footer">
								<div class="pull-right">
									<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
								</div>
								<div class="pull-right margin_r15">
									<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
								</div>
							</div>
						</div>
						<!-- 搜索栏 end -->
			
						<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
						<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
						
						<div class="box box-border margin-bottom-none">
							<!-- 按钮栏 start -->
							<div class="box-header with-border">
								<h3 class="box-title pad-t5">计划列表</h3>
							</div>
							<!-- 按钮栏 end -->	
			
							<!-- 列表内容 start -->
							<div class="box-body">
								<div class="filter-tabs filter-tabs2 clearfix">
									<ul class="list-unstyled">
										<li lang=":input[name='search_EQ_planStatus']" <c:if test="${empty param['search_EQ_planStatus']}">class="actived"</c:if>>全部(${preAudit + auditNotPass + auditPass + overdue})</li>
										<li value="1" role=":input[name='search_EQ_planStatus']"  <c:if test="${param['search_EQ_planStatus'] == 1 }">class="actived"</c:if>>待审核(${preAudit})</li>
										<li value="2" role=":input[name='search_EQ_planStatus']"  <c:if test="${param['search_EQ_planStatus'] == 2 }">class="actived"</c:if>>审核不通过(${auditNotPass})</li>
										<li value="3" role=":input[name='search_EQ_planStatus']"  <c:if test="${param['search_EQ_planStatus'] == 3 }">class="actived"</c:if>>已发布(${auditPass})</li>
										<li value="4" role=":input[name='search_EQ_planStatus']"  <c:if test="${param['search_EQ_planStatus'] == 4 }">class="actived"</c:if>>已过期(${overdue})</li>
									</ul>
								</div>
								<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
									<div class="row">
										<div class="col-sm-12">
											<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
												<thead>
													<tr>
														<th>计划编号</th>
														<th>计划名称</th>
														<th>学期</th>
														<th>开考科目设置时间</th>
														<th>考试预约时间</th>
														<th>排考时间</th>
														<th>考试时间</th>
														<th>学习成绩登记截止时间</th>
														<th>状态</th>
														<th>操作</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td>${entity.examBatchCode}</td>
																<td>${entity.name}</td>
																<td>${gradeMap[entity.gradeId]}</td>
																<td>
																	<fmt:formatDate value="${entity.planSt}" type="date" pattern="yyyy-MM-dd"/><br>
																	至<br>
																	<fmt:formatDate value="${entity.planEnd}" type="date" pattern="yyyy-MM-dd"/>
																</td>
																<td>
																	<fmt:formatDate value="${entity.bookSt}" type="date" pattern="yyyy-MM-dd"/><br>
																	至<br>
																	<fmt:formatDate value="${entity.bookEnd}" type="date" pattern="yyyy-MM-dd"/>
																	<c:if test="${not empty entity.booksSt && not empty entity.booksEnd}">
																		<br/><br/>
																		<fmt:formatDate value="${entity.booksSt}" type="date" pattern="yyyy-MM-dd"/><br>
																		至<br>
																		<fmt:formatDate value="${entity.booksEnd}" type="date" pattern="yyyy-MM-dd"/>
																	</c:if>
																</td>
																<td>
																	<fmt:formatDate value="${entity.arrangeSt}" type="date" pattern="yyyy-MM-dd"/><br>
																	至<br>
																	<fmt:formatDate value="${entity.arrangeEnd}" type="date" pattern="yyyy-MM-dd"/>
																</td>
																<td>
																	网考、大作业考试时间：
																	<fmt:formatDate value="${entity.onlineSt}" type="date" pattern="yyyy-MM-dd"/>
																	至
																	<fmt:formatDate value="${entity.onlineEnd}" type="date" pattern="yyyy-MM-dd"/>
																	<br>
																	省网考考试时间：
																	<fmt:formatDate value="${entity.provinceOnlineSt}" type="date" pattern="yyyy-MM-dd"/>
																	至
																	<fmt:formatDate value="${entity.provinceOnlineEnd}" type="date" pattern="yyyy-MM-dd"/>
																	<br>
																	笔考考试时间：
																	<fmt:formatDate value="${entity.paperSt}" type="date" pattern="yyyy-MM-dd"/>
																	至
																	<fmt:formatDate value="${entity.paperEnd}" type="date" pattern="yyyy-MM-dd"/>
																	<br>
																	机考考试时间：
																	<c:choose>
																		<c:when test="${not empty entity.machineSt && not empty entity.machineEnd }">
																			<fmt:formatDate value="${entity.machineSt}" type="date" pattern="yyyy-MM-dd"/>
																			至
																			<fmt:formatDate value="${entity.machineEnd}" type="date" pattern="yyyy-MM-dd"/>
																		</c:when>
																		<c:otherwise>
																			<span class="text-orange">待定</span>
																		</c:otherwise>
																	</c:choose>
																	<br>
																	学位英语报考时间：
																	<c:choose>
																		<c:when test="${not empty entity.xwyyBookSt && not empty entity.xwyyBookEnd }">
																			<fmt:formatDate value="${entity.xwyyBookSt}" type="date" pattern="yyyy-MM-dd"/>
																			至
																			<fmt:formatDate value="${entity.xwyyBookEnd}" type="date" pattern="yyyy-MM-dd"/>
																		</c:when>
																		<c:otherwise>
																			<span class="text-orange">待定</span>
																		</c:otherwise>
																	</c:choose>
																	<br>
																	本科统考预约时间：
																	<c:choose>
																		<c:when test="${not empty entity.bktkBookSt && not empty entity.bktkBookEnd }">
																			<fmt:formatDate value="${entity.bktkBookSt}" type="date" pattern="yyyy-MM-dd"/>
																			至
																			<fmt:formatDate value="${entity.bktkBookEnd}" type="date" pattern="yyyy-MM-dd"/>
																		</c:when>
																		<c:otherwise>
																			<span class="text-orange">待定</span>
																		</c:otherwise>
																	</c:choose>
																	<br>
																	形考任务登记截止时间：
																	<fmt:formatDate value="${entity.shapeEnd}" type="date" pattern="yyyy-MM-dd"/>
																	<br>
																	论文截止时间：
																	<fmt:formatDate value="${entity.thesisEnd}" type="date" pattern="yyyy-MM-dd"/>
																	<br>
																	报告截止时间：
																	<fmt:formatDate value="${entity.reportEnd}" type="date" pattern="yyyy-MM-dd"/>
																	<br>
																</td>
																<td>
																	<fmt:formatDate value="${entity.recordEnd}" type="date" pattern="yyyy-MM-dd"/>
																</td>
																<td>
											            			<c:choose>
											            				<c:when test="${entity.planStatus == 1}">
											            					<span class="text-orange">待审核</span>
											            				</c:when>
											            				<c:when test="${entity.planStatus == 2}">
											            					<span class="text-red">审核不通过</span>
											            				</c:when>
											            				<c:when test="${entity.planStatus == 3}">
											            					<span class="text-green">已发布</span>
											            				</c:when>
											            				<c:when test="${entity.planStatus == 4}">
											            					<span class="gray9">已过期</span>
											            				</c:when>
											            			</c:choose>
																</td>
																<td>
																	<div class="data-operion">
												            			<a href="view/${entity.examBatchId}" class="operion-item" data-toggle="tooltip" title="查看明细"><i class="fa fa-view-more"></i></a>
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
							<!-- 列表内容 end -->
						</div>
					</form>	
					</div>
				</div>
			</div>
	</section>
	<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
</html>
