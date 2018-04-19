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
			<li class="active">开考科目</li>
		</ol>
	</section>
		<section class="content">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li><a href="${ctx}/home/class/exam/batch/list" target="_self">考试计划</a></li>
					<li class="active"><a href="${ctx}/home/class/exam/plan/list" target="_self">开考科目</a></li>
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
											<label class="control-label col-sm-3">考试计划</label>
											<div class="col-sm-9">
												<select name="search_EQ_examBatchCode" id="search_EQ_examBatchCode" class="selectpicker show-tick form-control" 
													data-size="5" data-live-search="true">
													<option value="" >请选择</option>
													<c:forEach items="${batchMap}" var="map">
														<option value="${map.key}"<c:if test="${map.key==examBatchCode}">selected='selected'</c:if> >${map.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">科目名称</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="search_LIKE_examPlanName" value="${param['search_LIKE_examPlanName']}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">课程</label>
											<div class="col-sm-9">
												<select name="search_EQ_gjtCourseList.courseId" class="selectpicker show-tick form-control"
													data-size="5" data-live-search="true">
													<option value="" selected="selected">请选择</option>
													<c:forEach items="${courseList}" var="c">
														<option value="${c.courseId}"<c:if test="${c.courseId==param['search_EQ_gjtCourseList.courseId']}">selected='selected'</c:if> >${c.kcmc}（课程号：${c.kch}）</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">试卷号</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="search_LIKE_examNo" value="${param['search_LIKE_examNo']}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">状态</label>
											<div class="col-sm-9">
												<select name="search_EQ_status" id="pstatus" class="selectpicker show-tick form-control"
													data-size="5" data-live-search="true">
													<option value="" selected="selected">请选择</option>
													<option value="1"<c:if test="${param['search_EQ_status']==1}">selected='selected'</c:if> >未开始</option>
													<option value="2"<c:if test="${param['search_EQ_status']==2}">selected='selected'</c:if> >预约中</option>
													<option value="3"<c:if test="${param['search_EQ_status']==3}">selected='selected'</c:if> >待考试</option>
													<option value="4"<c:if test="${param['search_EQ_status']==4}">selected='selected'</c:if> >考试中</option>
													<option value="5"<c:if test="${param['search_EQ_status']==5}">selected='selected'</c:if> >已结束</option>
												</select>
											</div>
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
								<h3 class="box-title pad-t5">开考科目列表</h3>
								<div class="fr">
									<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-export">
										<i class="fa fa-fw fa-download"></i>导出开考科目
									</a>
								</div>
							</div>
							<!-- 按钮栏 end -->	
			
							<!-- 列表内容 start -->
							<div class="box-body">
								<div class="filter-tabs filter-tabs2 clearfix">
									<ul class="list-unstyled">
										<li lang=":input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${all})</li>
										<li value="1" role=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_status'] == 1 }">class="actived"</c:if>>未开始(${notStart})</li>
										<li value="2" role=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_status'] == 2 }">class="actived"</c:if>>预约中(${booking})</li>
										<li value="3" role=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_status'] == 3 }">class="actived"</c:if>>待考试(${preExam})</li>
										<li value="4" role=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_status'] == 4 }">class="actived"</c:if>>考试中(${examing})</li>
										<li value="5" role=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_status'] == 5 }">class="actived"</c:if>>已结束(${end})</li>
									</ul>
								</div>
								<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
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
														<th>状态</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td>
																	${entity.examBatchNew.name}<br>
																	<span class="gray9">${entity.examBatchNew.examBatchCode}</span>
																</td>
																<td>
																	<div class="text-left">
																		科目名称：${entity.examPlanName} <br>
																		试卷号：${entity.examNo} <br>
																		考试形式：${examTypeMap[entity.type]}
																	</div>
																</td>
																<td>
																	<c:forEach items="${entity.gjtCourseList}" var="c">
																		${c.kcmc}<span class="gray9">（${c.kch}）</span><br>
																	</c:forEach>
																</td>
																<td>
																	<c:choose>
																		<c:when test="${fn:length(entity.gjtSpecialtyList) == 0}">
																			通用
																		</c:when>
																		<c:otherwise>
																			<c:forEach items="${entity.gjtSpecialtyList}" var="s">
																				${s.zymc}<span class="gray9">(${s.ruleCode})</span><br>
																			</c:forEach>
																		</c:otherwise>
																	</c:choose>
																</td>
																<td>
																	<c:choose>
																		<c:when test="${empty entity.xkPercent }">
																			<span class="text-orange">待定</span>
																		</c:when>
																		<c:otherwise>
																			${entity.xkPercent}%
																		</c:otherwise>
																	</c:choose>
																</td>
																<td>
																	<fmt:formatDate value="${entity.examSt}" type="date" pattern="yyyy-MM-dd HH:mm"/><br>
																	至<br>
																	<fmt:formatDate value="${entity.examEnd}" type="date" pattern="yyyy-MM-dd HH:mm"/>
																</td>
																<td>${examStyleMap[entity.examStyle]}</td>
																<td>
																	<c:choose>
																		<c:when test="${entity.examPlanOrder == 1}">
																			个人预约
																		</c:when>
																		<c:when test="${entity.examPlanOrder == 2}">
																			集体预约
																		</c:when>
																	</c:choose>
																</td>
																<td>
											            			<c:choose>
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
											            					<span class="text-green">考试中</span>
											            				</c:when>
											            				<c:when test="${entity.status == 5}">
											            					<span class="gray9">已结束</span>
											            				</c:when>
											            				<c:otherwise>--</c:otherwise>
											            			</c:choose>
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
	
	<script type="text/javascript">
		$(function() {
			$(".btn-export").click(function(event) {
				var url = "${ctx}/home/class/exam/plan/export?" + $("#listForm").serialize();
				window.open(url);
			});
		});

	</script>
</body>
</html>
