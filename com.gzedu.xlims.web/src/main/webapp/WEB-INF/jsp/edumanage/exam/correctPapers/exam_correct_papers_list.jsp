<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>报告批改</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="javascript:;">考试管理</a></li>
			<li class="active">报告批改</li>
		</ol>
	</section>
		
	<section class="content">
		<form id="listForm" class="form-horizontal" action="list.html" method="post">
			<input type="hidden" name="search_EQ_correctState" value="${param['search_EQ_correctState']}">
				<!-- 搜索栏 start --> 
				<div class="box">
					<div class="box-body"> 
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xm"   value="${param['search_LIKE_gjtStudentInfo.xm']}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_gjtStudentInfo.xh"   value="${param['search_EQ_gjtStudentInfo.xh']}">
								</div>
							</div>

							<div class="col-md-4">
								<label class="control-label col-sm-3">学期</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.nj" class="selectpicker show-tick form-control"
											data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${termMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key == param['search_EQ_gjtStudentInfo.nj']}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						
						<div class="row reset-form-horizontal clearbox">
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">专业</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.major" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${specialtyMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key == param['search_EQ_gjtStudentInfo.major']}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试计划</label>
								<div class="col-sm-9">
									<select id="examBatchCode" name="search_EQ_examBatchCode" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${batchMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key == examBatchCode}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div> 
							</div>

							<div class="col-md-4">
								<label class="control-label col-sm-3">考试科目</label>
								<div class="col-sm-9">
									<select id="examPlanId" name="search_EQ_gjtExamPlanNew.examPlanId" class="selectpicker show-tick form-control"
											data-size="10" data-live-search="true">
										<option value=''>请选择</option>
									</select>
								</div>
							</div>

						</div>
					</div>
					
					<div class="box-footer text-right">
			          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
			          <button type="button" class="btn min-width-90px btn-default btn-reset" id="btn-reset">重置</button>
			        </div>
				</div>
				<!-- 搜索栏 end --> 
				 
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>


				<div class="box margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">报告批改列表</h3>
						<div class="pull-right no-margin">
							<shiro:hasPermission name="/exam/new/correctPapers/list$export">
								<a class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出成绩</a>
							</shiro:hasPermission>
						</div>
					</div>
	
					<!-- 列表内容 start -->
					<div class="box-body">
							<div class="filter-tabs clearfix">
								<ul class="list-unstyled">
									<li <c:if test="${empty param.search_EQ_correctState}">class="actived"</c:if> val="" class="choiceState">全部(${countStateMap['']})</li>
									<li <c:if test="${param.search_EQ_correctState == '0'}">class="actived"</c:if> val="0" class="choiceState">待评分(${countStateMap['0']})</li>
									<li <c:if test="${param.search_EQ_correctState == 1}">class="actived"</c:if> val="1" class="choiceState">已通过(${countStateMap['1']})</li>
									<li <c:if test="${param.search_EQ_correctState == 2}">class="actived"</c:if> val="2" class="choiceState">未通过(${countStateMap['2']})</li>
								</ul>
							</div>
							<div class="table-responsive" style="overflow: hidden">
								<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
										<thead>
											<tr> 
												<th>个人信息</th>
												<th>报读信息</th>
												<th>考试计划</th>
												<th>考试科目</th>
												<th>考试形式</th>
												<th>上传报告</th>
												<th>报告成绩(分)</th>
												<th>报告状态</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pageInfo.content}" var="info">
												<c:if test="${not empty info}">
													<tr>
														<td>
															<div class="text-left">
																<span name="avatar" style="display: none;"><img src="${not empty info.gjtStudentInfo.avatar ? info.gjtStudentInfo.avatar : ctx.concat('/static/images/headImg04.png')}" class="img-circle" style="width:112px;height:112px;" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'"></span>
																姓名:  <span name="xm">${info.gjtStudentInfo.xm}</span><br>
																学号:  <span name="xh">${info.gjtStudentInfo.xh}</span><br>
																手机号: 
																<span name="sjh">
																<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																	${info.gjtStudentInfo.sjh}
																</shiro:hasPermission>
																<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
																	<c:if test="${not empty info.gjtStudentInfo.sjh }">
																		${fn:substring(info.gjtStudentInfo.sjh,0, 3)}******${fn:substring(info.gjtStudentInfo.sjh,8, (info.gjtStudentInfo.sjh).length())}
																	</c:if>
																</shiro:lacksPermission>
																</span>
															</div>
														</td>
														<td>
															<div class="text-left">
																层次: <span name="pycc"><dic:getLabel typeCode="TrainingLevel" code="${info.gjtStudentInfo.pycc}"/></span> <br>
																年级: <span name="yearName">${info.gjtStudentInfo.gjtGrade.gjtYear.name}</span> <br>
																学期: <span name="gradeName">${info.gjtStudentInfo.gjtGrade.gradeName}</span> <br>
																专业: <span name="zymc">${info.gjtStudentInfo.gjtSpecialty.zymc}</span> <br>
															</div>
														</td>
														<td>
															<span name="examBatchName">${info.gjtExamPlanNew.examBatchNew.name}</span><br>
															<span name="examBatchCode" class="gray9">${info.examBatchCode}</span>
														</td>
														<td>
															<span name="examPlanName">${info.gjtExamPlanNew.examPlanName}</span><br>
															<span name="examNo" class="gray9">${info.gjtExamPlanNew.examNo}</span>
														</td>
														<td>
															<span name="type"><dic:getLabel typeCode="ExaminationMode" code="${info.gjtExamPlanNew.type}" /></span>
														</td>
														<td>
															<span name="papersFile"><a href="http://eezxyl.gzedu.com?furl=${info.papersFile}" data-role="addon-preview">${info.papersFileName}</a></span>
														</td>
														<td>
															${not empty info.score ? info.score : '--'}
														</td>
														<td>
															<c:choose>
																<c:when test="${info.correctState == 0}">
																	<span class="text-yellow">待评分</span>
																</c:when>
																<c:when test="${info.correctState == 1}">
																	<span class="text-green">已通过</span>
																</c:when>
																<c:when test="${info.correctState == 2}">
																	<span class="text-red">未通过</span>
																</c:when>
																<c:otherwise>
																	<span class="text-yellow">待上传</span>
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<div class="data-operion">
																<c:choose>
																	<c:when test="${info.correctState == 0}">
																		<shiro:hasPermission name="/exam/new/correctPapers/list$approval">
																			<a href="javascript:void(0);" val="${info.id}" action="audit" class="operion-item" data-toggle="tooltip" title="批改报告"><i class="fa fa-fw fa-shxxjl"></i></a>
																		</shiro:hasPermission>
																	</c:when>
																	<c:otherwise>
																		<!-- <shiro:hasPermission name="/exam/new/correctPapers/list$view">
																			<a href="javascript:void(0);" val="${info.id}" action="view" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
																		</shiro:hasPermission> -->
																	</c:otherwise>
																</c:choose>
															</div>
														</td>
													</tr>
												</c:if>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<tags:pagination page="${pageInfo}" paginationSize="13" />
						</div>
					<!-- 列表内容 end -->
				</div>
			</form>
		</div>	
	</section>

	<!-- 批改报告 -->
	<div id="formModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog" style="height: 400px; width: 700px; z-index: 9999">
	        <form id="correctPapersForm" name="uploadForm" class="form-horizontal" action="" method="post">
				<input name="id" type="hidden" />
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">批改报告</h4>
	                </div>
	                <div class="modal-body">
						<div class="box-body">
							<div class="row stu-info-status">
								<div class="col-xs-4">
									<span name="avatar"></span>
									<div class="row">
										<div class="col-sm-12">
											<b>姓名:</b> <span name="xm"></span>
										</div>
										<div class="col-sm-12">
											<b>学号:</b> <span name="xh"></span>
										</div>
										<div class="col-sm-12">
											<b>手机:</b>
											<span name="sjh"></span>
										</div>
										<div class="col-sm-12">
											<b>层次:</b> <span name="pycc"></span>
										</div>
										<div class="col-sm-12">
											<b>年级:</b> <span name="yearName"></span>
										</div>
										<div class="col-sm-12">
											<b>学期:</b> <span name="gradeName"></span>
										</div>
										<div class="col-sm-12">
											<b>专业:</b> <span name="zymc"></span>
										</div>
									</div>
								</div>
								<div class="col-xs-8">
									请为学员的报告评分：
									<div class="row">
										<div class="form-group">
											<label class="col-sm-4 control-label">考试计划:</label>
											<div class="col-sm-8">
												<span name="examBatchName"></span> <span name="examBatchCode" class="gray9"></span>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-4 control-label">考试科目:</label>
											<div class="col-sm-8">
												<span name="examPlanName"></span> <span name="examNo" class="gray9"></span>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-4 control-label">考试方式:</label>
											<div class="col-sm-8">
												<span name="type"></span>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-4 control-label">上传报告:</label>
											<div class="col-sm-8">
												<span name="papersFile"></span>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-4 control-label"><small class="text-red">*</small>报告成绩:</label>
											<div class="col-sm-6">
												<div class="input-group">
													<input type="text" class="form-control" name="score"
														   onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')">
													<span class="input-group-addon">分</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                    <button type="button" class="btn btn-primary" id="save">确定</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<%
		long time = System.currentTimeMillis();
	%>
	<script type="text/javascript">
        var examPlanId = '${param["search_EQ_gjtExamPlanNew.examPlanId"]}';
	</script>
	<script src="${ctx}/static/js/exam/exam_correct_papers_form.js?time=<%=time%>"></script>
</body>
</html>
