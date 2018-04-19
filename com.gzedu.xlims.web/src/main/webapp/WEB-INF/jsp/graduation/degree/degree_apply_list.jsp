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
    function exportInfo() {
		window.location.href = '${ctx}/graduation/apply/export?' + $('#listForm').serialize();
    }

</script>

</head>
<body class="inner-page-body">

	<!-- Main content -->
	<section class="content-header">

		<ol class="breadcrumb">
			<li>
				<a href="#">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">毕业管理</a>
			</li>
			<li class="active">学位申请</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal">
			<div class="box">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">毕业计划</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtGraduationPlan.id" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${graduationPlanMap}" var="plan">
											<option value="${plan.key}"  <c:if test="${plan.key==param['search_EQ_gjtGraduationPlan.id']}">selected='selected'</c:if>>${plan.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_gjtStudentInfo.xh" value="${param['search_EQ_gjtStudentInfo.xh']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">层次</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${pyccMap}" var="trainingLevel">
											<option value="${trainingLevel.key}"  <c:if test="${trainingLevel.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if>>${trainingLevel.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学期</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${gradeMap}" var="grade">
											<option value="${grade.key}"  <c:if test="${grade.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']}">selected='selected'</c:if>>${grade.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">专业</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyBaseId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${specialtyMap}" var="specialty">
											<option value="${specialty.key}"  <c:if test="${specialty.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyBaseId']}">selected='selected'</c:if>>${specialty.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</div>

					<div id="more-search" class="row collapse <c:if test="${not empty param['search_EQ_gjtStudentInfo.sfzh'] || not empty param['search_EQ_gjtStudentInfo.gjtStudyCenter.id'] || not empty param.search_LIKE_scCo }">in</c:if>">
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">身份证号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_gjtStudentInfo.sfzh" value="${param['search_EQ_gjtStudentInfo.sfzh'] }">
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学习中心</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.gjtStudyCenter.id" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${studyCenterMap}" var="map">
											<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtStudyCenter.id']}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">电子注册号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_scCo" value="${param.search_LIKE_scCo }">
								</div>
							</div>
						</div>
					</div>

				</div><!-- /.box-body -->
				<div class="box-footer text-right">
					<button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
					<button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
					<div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">
						高级搜索<i class="fa fa-fw fa-angle-up "></i>
					</div>
				</div>
			</div>

			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">申请列表</h3>
					<div class="pull-right">
						<shiro:hasPermission name="/graduation/degreemanager/degreeApplyList$downloadDegreeApplyFile">
							<a href="${ctx}/graduation/degreemanager/downloadReqFile" role="button" class="btn btn-default btn-sm margin_r10" data-role="import"><i class="fa fa-fw fa-download"></i> 批量下载学位申请材料</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="/graduation/degreemanager/degreeApplyList$exportDegreeApply">
							<button class="btn btn-default btn-sm margin_r10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出学位申请记录</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="/graduation/degreemanager/degreeApplyList$importGraduationAudit">
							<a href="${ctx}/graduation/degreemanager/importApplyFlowRecord" role="button" class="btn btn-default btn-sm margin_r10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 导入审核记录</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="box-body">
					<div class="filter-tabs clearfix">
						<input name="search_EQ_auditState" type="hidden" value="${param.search_EQ_auditState}" />
						<ul class="list-unstyled" id="state">
							<li <c:if test="${empty(param.search_EQ_auditState)}">class="actived"</c:if>  data-state="">全部（${all}）</li>
							<li <c:if test="${param.search_EQ_auditState=='0'}">class="actived"</c:if> data-state="0">待审核（${audit}）</li>
							<li <c:if test="${param.search_EQ_auditState=='1'}">class="actived"</c:if> data-state="1">审核通过（${pass}）</li>
							<li <c:if test="${param.search_EQ_auditState=='2'}">class="actived"</c:if> data-state="2">审核不通过（${notpass}）</li>
							<li <c:if test="${param.search_EQ_auditState=='3'}">class="actived"</c:if> data-state="3">已发放学位证书（${receive}）</li>
						</ul>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered vertical-mid text-center table-font">
							<thead class="with-bg-gray">
								<tr>
									<th>毕业计划</th>
									<th>个人信息</th>
									<th>报读信息</th>
									<th>电子注册号</th>
									<th>申请院校</th>
									<th>是否满足学位申请条件</th>
									<th>学位状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageInfo.content}" var="info">
									<tr>
										<td>
											${info.gjtGraduationPlan.graPlanTitle }
										</td>
										<td>
											<ul class="list-unstyled text-left">
												<li>姓名：${info.gjtStudentInfo.xm}</li>
												<li>学号：${info.gjtStudentInfo.xh}</li>
												<li>手机：
														<shiro:hasPermission name="/personal/index$privacyJurisdiction">
															${info.gjtStudentInfo.sjh}
														</shiro:hasPermission>
														<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
															<c:if test="${not empty info.gjtStudentInfo.sjh }">
															${fn:substring(info.gjtStudentInfo.sjh,0, 3)}******${fn:substring(info.gjtStudentInfo.sjh,8, (info.gjtStudentInfo.sjh).length())}
															</c:if>
														</shiro:lacksPermission>
												
												</li>
												<li>身份证：
													<shiro:hasPermission name="/personal/index$privacyJurisdiction">
														${info.gjtStudentInfo.sfzh}
													</shiro:hasPermission>
													<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
														<c:if test="${not empty info.gjtStudentInfo.sfzh }">
														${fn:substring(info.gjtStudentInfo.sfzh,0, 4)}******${fn:substring(info.gjtStudentInfo.sfzh,14, (info.gjtStudentInfo.sfzh).length())}
														</c:if>
													</shiro:lacksPermission>
												</li>
											</ul>
										</td>
										<td>
											<ul class="list-unstyled text-left">
												<li>层次：<dic:getLabel typeCode="TrainingLevel" code="${info.gjtStudentInfo.pycc }" /></li>
												<li>学期：${info.gjtStudentInfo.gjtGrade.gradeName}</li>
												<li>专业：${info.gjtStudentInfo.gjtSpecialty.zymc}</li>
												<li>学习中心：${info.gjtStudentInfo.gjtStudyCenter.scName}</li>
											</ul>
										</td>
										<td>
											--
										</td>
										<td>${info.gjtDegreeCollege.collegeName }</td>
										<td>
											${info.graduationCondition==1?'<span class="text-green">已满足</span>':'<span class="text-orange">未满足</span>'}
										</td>
										<td class="text-orange">
											<c:choose>
											<c:when test="${info.auditState==0}"><span class="text-orange">待审核</span></c:when>
											<c:when test="${info.auditState==1}"><span class="text-green">审核通过</span></c:when>
											<c:when test="${info.auditState==2}"><span class="text-red">审核不通过</span></c:when>
											</c:choose>
										</td>
										<td class="text-center">
											<shiro:hasPermission name="/graduation/degreemanager/degreeApplyList$view">
												<a href="view?applyId=${info.applyId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-view-more"></i></a>
											</shiro:hasPermission>
											<shiro:hasPermission name="/graduation/degreemanager/degreeApplyList$downloadDegreeApplyFile">
												<a href="download?search_EQ_studentId=${info.gjtStudentInfo.studentId}" class="operion-item" data-toggle="tooltip" title="下载毕业生登记表"><i class="fa fa-download"></i></a>
											</shiro:hasPermission>
										</td>
									</tr>
								</c:forEach>
								<c:if test="${empty(pageInfo.content)}">
									<tr>
										<td colspan="8">暂无数据</td>
									</tr>
								</c:if>
							</tbody>
						</table>
						<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>
			</div>
		</form>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
	$("#more-search").on('shown.bs.collapse', function(event) {
	    event.preventDefault();
	    $('[data-target="#more-search"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
	}).on('hidden.bs.collapse', function(event) {
	    event.preventDefault();
	    $('[data-target="#more-search"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
	});

	// 导入
	$('[data-role="import"]').click(function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:'+$(this).attr('href')
		});
	});

	// 导出
	$('[data-role="export"]').click(function(event) {
	    event.preventDefault();
	  $.mydialog({
	    id:'export',
	    width:600,
	    height:415,
	    zIndex:11000,
	    content: 'url:${ctx}/graduation/degreemanager/exportApplyFlowRecord'
	  });
	});

	$('#state li').click(function(){
		$('[name="search_EQ_auditState"]').val($(this).data('state'));
		listForm.submit();
	});
    </script>
</body>
</html>
