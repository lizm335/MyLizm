<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>排考安排</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {

	var type = '${param.search_EQ_examPlanNew.type}';
	if (!type) {
		type = 8;
	}
	
	$('#examBatchCode').change(function(){
		var examBatchCode = $('#examBatchCode').val();
		if(examBatchCode!=''){
			$.get(ctx+"/exam/new/plan/queryExamPlansByExamBatchCode",{examBatchCode:examBatchCode},function(data,status){
				$('#examPlanId').empty();
				$("#examPlanId").append("<option value=''>请选择</option>");
				$.each(data, function (i) {
					if (examPlanId == data[i].id) {
						$("#examPlanId").append("<option selected='selected' value='"+data[i].id+"'>"+data[i].name+"</option>");
					} else {
						$("#examPlanId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
					}
	        	});
				$('#examPlanId').selectpicker('refresh'); 
			  },"json");
		} else {
			$('#examPlanId').empty();
			$("#examPlanId").append("<option value=''>请选择</option>");
			$('#examPlanId').selectpicker('refresh'); 
		}
	});
	$('#examBatchCode').change();
	
	$("[data-role='export']").click(function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'export',
		  width:600,
		  height:550,
		  zIndex:11000,
		  content: 'url:'+$(this).attr('href')
		});
	});
	
	$(".auto-arrangement").click(function(event) {
		$.mydialog({
		  id:'arrangement',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:planForm?examType='+type
		});
	});
	
	$(".btn-import").click(function(event) {
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:importExamStudentRoomForm'
		});
	});
	
	
})

</script>

</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试管理</a></li>
		<li class="active">排考安排</li>
	</ol>
</section>
		
<section class="content">
	<form id="listForm" class="form-horizontal" action="list.html">
		<input type="hidden" id="search_EQ_examPlanNew.type" name="search_EQ_examPlanNew.type" value="${param['search_EQ_examPlanNew.type']}">
		<input type="hidden" name="search_EQ_status" value="${param.search_EQ_status}">
		<div class="nav-tabs-custom no-margin">
			
			<ul class="nav nav-tabs nav-tabs-lg">
				<li <c:if test="${param['search_EQ_examPlanNew.type'] == 8 || empty param['search_EQ_examPlanNew.type']}">class="active"</c:if>><a href="list?search_EQ_examPlanNew.type=8" target="_self">笔试</a></li>
				<li <c:if test="${param['search_EQ_examPlanNew.type'] == 11}">class="active"</c:if>><a href="list?search_EQ_examPlanNew.type=11" target="_self">机考</a></li>
			</ul> 
			<div class="tab-content">
				<!-- 搜索栏 start --> 
				<div class="box box-border">
					<div class="box-body"> 
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_student.xm"   value="${param['search_LIKE_student.xm']}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_student.xh"   value="${param['search_LIKE_student.xh']}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">学期</label>
								<div class="col-sm-9">
									<select name="search_EQ_student.nj" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${termMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key == param['search_EQ_student.nj']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						
						<div class="row reset-form-horizontal clearbox">
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">专业</label>
								<div class="col-sm-9">
									<select name="search_EQ_student.major" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${specialtyMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key == param['search_EQ_student.major']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试计划</label>
								<div class="col-sm-9">
									<select id="examBatchCode" name="search_EQ_examPlanNew.examBatchCode" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${batchMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key == examBatchCode}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div> 
							</div>
						
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试科目</label>
								<div class="col-sm-9">
									<select id="examPlanId" name="search_EQ_examPlanNew.examPlanId" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true">
										
									</select>
								</div>
							</div>
							
						</div>
						
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">学习中心</label>
								<div class="col-sm-9">
									<select name="search_EQ_student.gjtStudyCenter.id" class="selectpicker show-tick form-control" 
										data-size="10" data-live-search="true">
										<option value="" >请选择</option>
										<c:forEach items="${schoolInfoMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==param['search_EQ_student.gjtStudyCenter.id']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</div>
					
					<div class="box-footer text-right">
			          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
			          <button type="button" class="btn min-width-90px btn-default" id="btn-reset">重置</button>
			        </div>
				</div>
				<!-- 搜索栏 end --> 
				 
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				
				<div class="box box-border margin-bottom-none">
					<!-- 按钮栏 start -->
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">排考列表</h3>
						<div class="pull-right">
							<shiro:hasPermission name="/exam/new/student/room/list$batchDeleteArrangement">
								<a href="javascript:void(0);" class="btn btn-default btn-sm margin_r10 del-checked">
										<i class="fa fa-fw fa-trash-o"></i> 批量删除排考
								</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/exam/new/student/room/list$autoArrangement">
								<a href="javascript:void(0)" class="btn btn-default btn-sm margin_r10 auto-arrangement" >
											<i class="fa fa-fw fa-cubes"></i> 自动排考</a>
							</shiro:hasPermission>			
							<shiro:hasPermission name="/exam/new/student/room/list$importArrangement">
								<a href="#" class="btn btn-default btn-sm margin_r10 btn-import" >
											<i class="fa fa-fw fa-download"></i> 导入排考数据</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/exam/new/student/room/list$exportRoomSeat">
								<a href="exportRoomSeat" class="btn btn-default btn-sm margin_r10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出考场签到表</a>
							</shiro:hasPermission>
							
							<shiro:hasPermission name="/exam/new/student/room/list$exportArrangement">
								<a href="exportRoomSeat2" class="btn btn-default btn-sm" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出排考明细表</a>
							</shiro:hasPermission>
							 
										
						</div>
					</div> 
					<!-- 按钮栏 end -->	
	
					<!-- 列表内容 start -->
					<div class="box-body">
						<div class="filter-tabs filter-tabs2 clearfix">
							<ul class="list-unstyled">
								<li lang=":input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${all})</li>
								<li value="1" role=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_status'] == 1 }">class="actived"</c:if>>已排考(${hasArrange})</li>
								<li value="0" role=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_status'] == '0' }">class="actived"</c:if>>未排考(${notArrange})</li>
							</ul>
						</div>
						<div class="table-responsive">
							<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
									<tr> 
										<th><input type="checkbox" class="select-all"></th>
										<th>个人信息</th>
										<th>报读信息</th>
										<th>考试计划</th>
										<th>考试科目</th>
										<th>考试形式</th>
										<th>试卷号</th>
										<th>考点</th> 
										<th>考场</th>
										<th>座位号</th>
										<th>排考状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pageInfo.content}" var="entity">
										<c:if test="${not empty entity}">
											<tr>
												<td>
													<c:if test="${not empty entity.gjtExamStudentRoomNew }">
														<input class="checkbox" type="checkbox" name="ids" data-name="check-id"
															data-id="${entity.appointmentId}"
															value="${entity.appointmentId}">
													</c:if>
												</td>
												<td>
													<div class="text-left">
														姓名:  ${entity.student.xm}<br>
														学号:  ${entity.student.xh}<br>
														<shiro:hasPermission name="/personal/index$privacyJurisdiction">
														手机号: ${entity.student.sjh}
														</shiro:hasPermission>
													</div>
												</td>
												<td>
													<div class="text-left">
														层次: ${pyccMap[entity.student.pycc]} <br>
														年级: ${entity.student.gjtGrade.gjtYear.name} <br>
														学期: ${entity.student.gjtGrade.gradeName} <br>
														专业: ${entity.student.gjtSpecialty.zymc} <br>
														学习中心: ${entity.student.gjtStudyCenter.scName} <br>
													</div>
												</td>
												<td>
													${entity.examPlanNew.examBatchNew.name}<br>
													<span class="gray9">${entity.examPlanNew.examBatchNew.examBatchCode}</span>
												</td>
												<td>
													${entity.examPlanNew.examPlanName}
													<%-- <c:forEach items="${entity.examPlanNew.gjtCourseList}" var="c">
														${c.kcmc}<span class="gray9">（${c.kch}）</span><br>
													</c:forEach> --%>
												</td>
												<td>
													${examTypeMap[entity.examPlanNew.type]}
												</td>
												<td>
													${entity.examPlanNew.examNo}
												</td>														
												<td>
													<c:choose>
														<c:when test="${not empty entity.gjtExamStudentRoomNew && not empty entity.gjtExamStudentRoomNew.gjtExamPointNew && not empty entity.gjtExamStudentRoomNew.gjtExamPointNew.name}">
															${entity.gjtExamStudentRoomNew.gjtExamPointNew.name }
														</c:when>
														<c:otherwise>
															${entity.examPointName}
														</c:otherwise>
													</c:choose>
												</td>
												<td>
													<c:if test="${not empty entity.gjtExamStudentRoomNew.gjtExamRoomNew}">
														${entity.gjtExamStudentRoomNew.gjtExamRoomNew.name}
													</c:if>
												</td>
												<td>
													${entity.gjtExamStudentRoomNew.seatNo}
												</td>
												<td>
													<c:choose>
														<c:when test="${entity.status == 1}">
															<span class="text-green">已排考</span>
														</c:when>
														<c:otherwise>
															<span class="text-red">未排考</span>
														</c:otherwise>
													</c:choose>
												</td> 
												<td>
													<shiro:hasPermission name="/exam/new/student/room/list$view">
														<a href="${ctx}/exam/new/appointment/view/${entity.appointmentId}" class="operion-item" data-toggle="tooltip" title="查看明细"><i class="fa fa-view-more"></i></a>
													</shiro:hasPermission>
													<c:if test="${entity.status == 1}">
														<shiro:hasPermission name="/exam/new/student/room/list$delete">
															<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${entity.appointmentId}" data-toggle="tooltip" title="删除排考"><i class="fa fa-trash-o text-red"></i></a>
														</shiro:hasPermission>
													</c:if>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
							<tags:pagination page="${pageInfo}" paginationSize="13" />
						</div>
					</div>
					
				</div>
			</div>
		</div>
	</form>
</section>
	
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>

</html>
