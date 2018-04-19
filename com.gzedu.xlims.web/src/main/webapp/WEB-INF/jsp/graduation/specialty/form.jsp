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
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
	//参考： http://bv.doc.javake.cn/examples/ 
    $('#inputForm').bootstrapValidator({
            fields: {
            	xxdm: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                xxmc: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                'gjtSchoolInfo.linkTel': {
                    validators: {
                        notEmpty: "required",
                        phone : "required"
                    }
                },
                'gjtSchoolInfo.linkMan': {
                    validators: {
                        notEmpty: "required",
                    }
                },
                /* 'gjtSchoolInfo.linkMail': {
                    validators: {
                        notEmpty: "required",
                        emailAddress: "required"
                    }
                }, */
                'gjtSchoolInfo.appid': {
                    validators: {
                        notEmpty: "required",
                    }
                }
            }
        });
	
    var value1='${gjtGraduationAdvisers1Value}';
    var value2='${gjtGraduationAdvisers2Value}';
    var value3='${gjtGraduationAdvisers3Value}';
    $('#gjtGraduationAdvisers1').selectpicker();
    $('#gjtGraduationAdvisers1').selectpicker('val', value1.split(','));
    $('#gjtGraduationAdvisers2').selectpicker();
    $('#gjtGraduationAdvisers2').selectpicker('val', value2.split(','));
    $('#gjtGraduationAdvisers3').selectpicker();
    $('#gjtGraduationAdvisers3').selectpicker('val', value3.split(','));
})

function uploadCallback() {
	$("#exampleA").attr("href", $("#example").val());
	$("#exampleA").html($("#exampleName").val());
}

function addPlan() {
	$("#addPlanForm").find("input[name!='settingId']").val("");
    $('#defenceTeachers1').selectpicker('val', '');
    $('#defenceTeachers2').selectpicker('val', '');
	$("#trafficGuidance").text("");
	$("#addPlanModal").modal({backdrop: 'static', keyboard: false});
}

function submit() {
	var beginTime = document.getElementById("beginTime").value;
	if (beginTime == "") 
	{
		alert("请选择开始时间！"); 
		document.getElementById("beginTime").focus();
		return false;
	}

	var endTime = document.getElementById("endTime").value;
	if (endTime == "") 
	{
		alert("请选择结束时间！"); 
		document.getElementById("endTime").focus();
		return false;
	}

	var defenceType = document.getElementById("defenceType").value;
	if (defenceType == "") 
	{
		alert("请选择答辩形式！"); 
		document.getElementById("defenceType").focus();
		return false;
	}

	var defenceTeachers1 = document.getElementById("defenceTeachers1").value;
	if (defenceTeachers1 == "") 
	{
		alert("请选择主答辩老师！"); 
		document.getElementById("defenceTeachers1").focus();
		return false;
	}

	var defenceTeachers2 = document.getElementById("defenceTeachers2").value;
	if (defenceTeachers2 == "") 
	{
		alert("请选择辅答辩老师！"); 
		document.getElementById("defenceTeachers2").focus();
		return false;
	}

	var defenceNum = document.getElementById("defenceNum").value;
	if (defenceNum == "") 
	{
		alert("请输入答辩人数！"); 
		document.getElementById("defenceNum").focus();
		return false;
	}
	
	$("#submitB").attr({"disabled":"disabled"});
	$("#addPlanForm").submit();
}

function deletePlan(planId) {
	$.confirm({
        title: '提示',
        content: '确认删除？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
        	 $.post("deletePlan",{"planId":planId},function(data){
        		if(data.successful){
        			showMessage(data);
        			window.location.href = "update?batchId=${entity.gjtGraduationBatch.batchId}&specialtyId=${entity.specialtyId}"
        					+ "&trainingLevel=${entity.trainingLevel}&specialtyName=${entity.specialtyName}&trainingName=${entity.trainingName}"
        					+ "&gradeId=${entity.gradeId}&gradeName=${entity.gradeName}&showDefenceSetting=true";
        		}else{
        			showMessage(data);
        		}
        },"json"); 
        } 
    });
}

function confirmPlan(batchId, specialtyId, trainingLevel) {
	$.confirm({
        title: '提示',
        content: '确认分配？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
        	 $.post("dispatch",{"batchId":batchId, "specialtyId":specialtyId, "trainingLevel":trainingLevel},function(data){
        		if(data.successful){
        			showMessage(data);
        			window.location.href = "update?batchId=${entity.gjtGraduationBatch.batchId}&specialtyId=${entity.specialtyId}"
        					+ "&trainingLevel=${entity.trainingLevel}&specialtyName=${entity.specialtyName}&trainingName=${entity.trainingName}"
        					+ "&gradeId=${entity.gradeId}&gradeName=${entity.gradeName}&showDefenceSetting=true";
        		}else{
        			showMessage(data);
        		}
        },"json"); 
        } 
    });
}
</script> 

</head>
<body class="inner-page-body">

<jsp:include page="/eefileupload/upload.jsp" />

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li><a href="#">毕业安排</a></li>
		<li class="active">专业毕业设置</li>
	</ol>
</section>

<section class="content">
              <div class="box no-margin">
				<ul id="myTab" class="nav nav-tabs">
				    <c:if test="${empty param.showDefenceSetting }">
				    <li class="active">
				    </c:if>
				    <c:if test="${not empty param.showDefenceSetting }">
				    <li>
				    </c:if>
				        <a href="#baseSetting" data-toggle="tab">基础设置</a>
				    </li>
				    <c:if test="${not empty param.showDefenceSetting }">
				    <li class="active">
				    </c:if>
				    <c:if test="${empty param.showDefenceSetting }">
				    <li>
				    </c:if>
				    	<a href="#defenceSetting" data-toggle="tab">论文答辩安排</a>
				    </li>
				</ul>
				
				<div id="myTabContent" class="tab-content" style="padding-top: 10px;">
					<c:if test="${empty param.showDefenceSetting }">
					<div class="tab-pane fade in active" id="baseSetting">
					</c:if>
					<c:if test="${not empty param.showDefenceSetting }">
					<div class="tab-pane fade" id="baseSetting">
					</c:if>
		                <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/graduation/specialty/${action}" method="post">
			                <input id="action" type="hidden" name="action" value="${action}">
			                <input type="hidden" name="settingId" value="${entity.settingId}">
			                <input type="hidden" name="specialtyId" value="${entity.specialtyId}">
			                <input type="hidden" name="trainingLevel" value="${entity.trainingLevel}">
			                <input type="hidden" name="gradeId" value="${entity.gradeId}">
			                <input type="hidden" name="gjtGraduationBatch.batchId" value="${entity.gjtGraduationBatch.batchId}">
		                    <div class="box-body pad20">
		                    	<div class="form-horizontal reset-form-horizontal">
									<div class="form-group"> 
										<label class="col-sm-3 control-label">专业名称:</label>
										<div class="col-sm-6">
											<input type="text" disabled="disabled" name="specialtyName" class="form-control" value="${entity.specialtyName}"/>
										</div>
									</div>
									<div class="form-group"> 
										<label class="col-sm-3 control-label">层次:</label>
										<div class="col-sm-6">
											<input type="text" disabled="disabled" name="trainingName" class="form-control" value="${entity.trainingName}"/>
										</div>
									</div>
									<div class="form-group"> 
										<label class="col-sm-3 control-label">学期:</label>
										<div class="col-sm-6">
											<input type="text" disabled="disabled" name="gradeName" class="form-control" value="${entity.gradeName}"/>
										</div>
									</div>
									<div class="form-group"> 
										<label class="col-sm-3 control-label"><small class="text-red">*</small>论文参考选题:</label>
										<div class="col-sm-6">
											<textarea rows="5" cols="30" class="form-control" name="topic" placeholder="请填写论文参考选题">${entity.topic}</textarea>
										</div>
									</div>
									<div class="form-group"> 
										<label class="col-sm-3 control-label"><small class="text-red">*</small>开题报告示例:</label>
										<div class="col-sm-6">
											<input type="hidden" id="example" name="example" class="form-control" value="${entity.example}"/>
											<input type="hidden" id="exampleName" name="exampleName" class="form-control" value="${entity.exampleName}"/>
											<span class="upload-file-lbl vertical-mid margin_r10">
												<a id="exampleA" href="${entity.example}" target="_blank">${entity.exampleName}</a>
											</span>
											<button class="btn btn-default btn-sm" name="headPortrait" type="button" onclick="javascript:uploadFile('exampleName','example','doc|docx|wps', null, uploadCallback)">上传</button>
										</div>
									</div>
									
									<c:set var="applyThesisCount" value="0"></c:set>
									<c:set var="defenceThesisCount" value="0"></c:set>
									<c:set var="applyPracticeCount" value="0"></c:set>
									<c:if test="${not empty applyList}">
										<c:forEach items="${applyList}" var="apply">
											<c:if test="${apply.applyType == 1}">
												<c:set var="applyThesisCount" value="${applyThesisCount + 1}"></c:set>
												
												<c:if test="${apply.needDefence == 1}">
													<c:set var="defenceThesisCount" value="${defenceThesisCount + 1}"></c:set>
												</c:if>
											</c:if>
											
											<c:if test="${apply.applyType == 2}">
												<c:set var="applyPracticeCount" value="${applyPracticeCount + 1}"></c:set>
											</c:if>
										</c:forEach>
									</c:if>
									
									<div class="form-group"> 
										<label class="col-sm-3 control-label">论文申请人数:</label>
										<div class="col-sm-6">
											<p class="form-control-static">
												${applyThesisCount}人
												<span class="text-red">按照当前设置的指导老师指导人数限制，本专业需配备指导老师 
														<c:if test="${applyThesisCount % graduationBatch.guideLimitNum == 0}"><fmt:formatNumber type="number" value="${applyThesisCount / graduationBatch.guideLimitNum}" maxFractionDigits="0"/></c:if>
														<c:if test="${applyThesisCount % graduationBatch.guideLimitNum != 0}"><fmt:formatNumber type="number" value="${(applyThesisCount / graduationBatch.guideLimitNum) + 1}" maxFractionDigits="0"/></c:if>
													人
												</span>
											</p>
										</div>
									</div>
									
									<div class="form-group"> 
										<label class="col-sm-3 control-label"><small class="text-red">*</small>论文指导老师:</label>
										<div class="col-sm-6">
											<select id="gjtGraduationAdvisers1" name="advisers1"
												class="selectpicker show-tick form-control" data-size="5" data-live-search="true" multiple="multiple">
												<c:forEach items="${gjtGraduationAdvisers1}" var="map">
													<option value="${map.employeeId}">${map.xm}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									
									<div class="form-group"> 
										<label class="col-sm-3 control-label">社会实践申请人数:</label>
										<div class="col-sm-6">
											<p class="form-control-static">
												${applyPracticeCount}人
											<span class="text-red">按照当前设置的指导老师指导人数限制，本专业需配备指导老师 
													<c:if test="${applyPracticeCount % graduationBatch.practiceLimitNum == 0}"><fmt:formatNumber type="number" value="${applyPracticeCount / graduationBatch.practiceLimitNum}" maxFractionDigits="0"/></c:if>
													<c:if test="${applyPracticeCount % graduationBatch.practiceLimitNum != 0}"><fmt:formatNumber type="number" value="${(applyPracticeCount / graduationBatch.practiceLimitNum) + 1}" maxFractionDigits="0"/></c:if>
												人
											</span>
											</p>
										</div>
									</div>
									
									<div class="form-group"> 
										<label class="col-sm-3 control-label"><small class="text-red">*</small>社会实践指导老师:</label>
										<div class="col-sm-6">
											<select id="gjtGraduationAdvisers3" name="advisers3"
												class="selectpicker show-tick form-control" data-size="5" data-live-search="true" multiple="multiple">
												<c:forEach items="${gjtGraduationAdvisers3}" var="map">
													<option value="${map.employeeId}">${map.xm}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									
									<div class="form-group"> 
										<label class="col-sm-3 control-label">论文答辩人数:</label>
										<div class="col-sm-6">
											<p class="form-control-static">
												${defenceThesisCount}人
											</p>
										</div>
									</div>
									
									<div class="form-group"> 
										<label class="col-sm-3 control-label"><small class="text-red">*</small>论文答辩老师:</label>
										<div class="col-sm-6">
											<select id="gjtGraduationAdvisers2" name="advisers2"
												class="selectpicker show-tick form-control" data-size="5" data-live-search="true" multiple="multiple">
												<c:forEach items="${gjtGraduationAdvisers2}" var="map">
													<option value="${map.employeeId}">${map.xm}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>	
		                  </div>
		
		                  <div class="row">
		                  	<div class="col-sm-6 col-sm-offset-3">
			                    <button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">确认分配</button>
		    	                <button id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
		                  	</div>
		                  </div>
		                  </form>
		          	  </div>
		          	  
		          	  <c:if test="${not empty param.showDefenceSetting }">
					  <div class="tab-pane fade in active" id="defenceSetting">
					  </c:if>
					  <c:if test="${empty param.showDefenceSetting }">
					  <div class="tab-pane fade" id="defenceSetting">
					  </c:if>
		          	  	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
						<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
		          	  	<div class="box margin-bottom-none">
							<div class="box-header with-border">
								<div class="pull-right no-margin">
										<a href="javascript:addPlan()" class="btn btn-default btn-sm">
												<i class="fa fa-fw fa-plus"></i> 添加</a>
								</div>
							</div>
		
							<div class="box-body">
								<div class="table-responsive">
											<table class="table table-bordered table-hover table-font vertical-mid text-center">
												<thead>
													<tr>
														<th>时间段</th>
													    <th>答辩方式</th>
													    <th>答辩场地</th>
													    <th>答辩老师</th>
													    <th>安排人数</th>
													    <th>操作</th>
													</tr>
												</thead>
												<tbody>
													<c:choose>
														<c:when test="${not empty entity.gjtGraduationDefencePlans}">
															<c:forEach items="${entity.gjtGraduationDefencePlans}" var="defencePlan">
																<c:if test="${not empty defencePlan}">
																	<tr>
																		<td>
																			<fmt:formatDate value="${defencePlan.beginTime}" pattern="yyyy.MM.dd HH:mm"/>
																			-
																			<fmt:formatDate value="${defencePlan.endTime}" pattern="HH:mm"/>
																			<input type="hidden" name="beginTime" value="<fmt:formatDate value='${defencePlan.beginTime}' pattern='yyyy-MM-dd HH:mm'/>" >
																			<input type="hidden" name="endTime" value="<fmt:formatDate value='${defencePlan.endTime}' pattern='yyyy-MM-dd HH:mm'/>" >
																		</td>
																		<td>
																			<c:choose>
																				<c:when test="${defencePlan.defenceType == 1}">
																					现场答辩
																				</c:when>
																				<c:when test="${defencePlan.defenceType == 2}">
																					远程答辩
																				</c:when>
																			</c:choose>
																			<input type="hidden" name="defenceType" value="${defencePlan.defenceType}">
																		</td>
																		<td>
																			${defencePlan.defencePlace}
																			<input type="hidden" name="defencePlace" value="${defencePlan.defencePlace}">
																		</td>
																		<td>
																			<div>主答辩老师：
																				<c:set var="defenceTeachers1" value="" />
																				<c:choose>
																					<c:when test="${not empty defencePlan.gjtGraduationDefenceTeachers1}">
																						<c:forEach items="${defencePlan.gjtGraduationDefenceTeachers1}" var="teacher" varStatus="status">
																							<c:choose>
																								<c:when test="${status.last}">
																									${teacher.teacher.xm}
																									<c:set var="defenceTeachers1" value="${defenceTeachers1}${teacher.teacher.employeeId}" />
																								</c:when>
																								<c:otherwise>
																									${teacher.teacher.xm}、
																									<c:set var="defenceTeachers1" value="${defenceTeachers1}${teacher.teacher.employeeId}," />
																								</c:otherwise>
																							</c:choose>
																						</c:forEach>
																					</c:when>
																					<c:otherwise>
																						--
																					</c:otherwise>
																				</c:choose>
																				<input type="hidden" name="defenceTeachers1" value="${defenceTeachers1}">
																			</div>
												                            <div>辅答辩老师：
												                            	<c:set var="defenceTeachers2" value="" />
																				<c:choose>
																					<c:when test="${not empty defencePlan.gjtGraduationDefenceTeachers2}">
																						<c:forEach items="${defencePlan.gjtGraduationDefenceTeachers2}" var="teacher" varStatus="status">
																							<c:choose>
																								<c:when test="${status.last}">
																									${teacher.teacher.xm}
																									<c:set var="defenceTeachers2" value="${defenceTeachers2}${teacher.teacher.employeeId}" />
																								</c:when>
																								<c:otherwise>
																									${teacher.teacher.xm}、
																									<c:set var="defenceTeachers2" value="${defenceTeachers2}${teacher.teacher.employeeId}," />
																								</c:otherwise>
																							</c:choose>
																						</c:forEach>
																					</c:when>
																					<c:otherwise>
																						--
																					</c:otherwise>
																				</c:choose>
																				<input type="hidden" name="defenceTeachers2" value="${defenceTeachers2}">
												                            </div>
																		</td>
																		<td>
																			${defencePlan.defenceNum}
																			<input type="hidden" name="defenceNum" value="${defencePlan.defenceNum}">
																			<input type="hidden" name="trafficGuidance" value="${defencePlan.trafficGuidance}">
																			<input type="hidden" name="planId" value="${defencePlan.planId}">
																		</td>
																		<td>
																				<a href="javascript:void(0)"
																					class="operion-item operion-edit"  data-toggle="tooltip" title="编辑">
																					<i class="fa fa-fw fa-edit"></i></a> 
																				<a href="javascript:deletePlan('${defencePlan.planId}')"
																					class="operion-item operion-del"
																					 data-toggle="tooltip" title="删除" data-tempTitle="删除">
																					<i class="fa fa-fw fa-trash-o text-red"></i></a>
																		</td> 
																	</tr>
																</c:if>
															</c:forEach>
														</c:when>
														<c:otherwise>
															<tr>
																<td align="center" colspan="6">暂无数据</td>
															</tr>
														</c:otherwise>
													</c:choose>
												</tbody>
											</table>
								</div>
							</div>
		
		                  <div class="row">
		                  	<div class="col-sm-6 col-sm-offset-1">
			                    <button id="btn-submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit" onclick="confirmPlan('${entity.gjtGraduationBatch.batchId}', '${entity.specialtyId}', '${entity.trainingLevel}')">确认分配</button>
		    	                <button id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">返回</button>
		                  	</div>
		                  </div>
						</div>
		          	  </div>
                  </div>
                  
              </div>
</section>

		<div id="addPlanModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addPlanLabel" aria-hidden="true"> 
	    	<div class="modal-dialog">
	    		<div class="modal-content">
			        <div class="modal-header"> 
			            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" id="addPlanLabel">论文答辩安排</h4>
			        </div> 
			        <div class="modal-body"> 
			        	<form method="post" id="addPlanForm" class="form-horizontal" role="form" action="${ctx}/graduation/specialty/savePlan.do">
							<input type="hidden" id="planId" name="planId" value=""/>
							<input type="hidden" id="settingId" name="settingId" value="${entity.settingId}"/>
							<div class="box-body">
								<div class="form-group"> 
									<label class="col-sm-3 control-label">专业名称:</label>
									<div class="col-sm-8">
										<span class="form-control">${entity.specialtyName}</span>
									</div>
								</div>
								
								<div class="form-group"> 
									<label class="col-sm-3 control-label"><small class="text-red">*</small>时间段：</label>
									<div class="col-sm-8">
										<div class="input-group input-custom-daterange">
											<input type="text" id="beginTime" name="beginTime" class="form-control single-datetime">
											<span class="input-group-addon">－</span>
											<input type="text" id="endTime" name="endTime" class="form-control single-datetime">
										</div>
									</div>
								</div>
							
								<div class="form-group">
									<label class="col-sm-3 control-label"><small class="text-red">*</small>答辩形式：</label>
									<div class="col-sm-8">
										<select id="defenceType" name="defenceType" class="selectpicker show-tick form-control" data-size="5">
											<option value="1">现场答辩</option>
											<option value="2">远程答辩</option>
										</select>
									</div>
								</div>
							
								<div class="form-group">
									<label class="col-sm-3 control-label"><small class="text-red">*</small>主答辩老师:</label>
									<div class="col-sm-8">
										<select id="defenceTeachers1" name="defenceTeachers1" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<c:forEach items="${gjtGraduationAdvisers2}" var="map">
												<option value="${map.employeeId}">${map.xm}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							
								<div class="form-group">
									<label class="col-sm-3 control-label"><small class="text-red">*</small>辅答辩老师:</label>
									<div class="col-sm-8">
										<select id="defenceTeachers2" name="defenceTeachers2" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" multiple="multiple">
											<c:forEach items="${gjtGraduationAdvisers2}" var="map">
												<option value="${map.employeeId}">${map.xm}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group"> 
									<label class="col-sm-3 control-label">答辩场地:</label>
									<div class="col-sm-8">
										<input type="text" id="defencePlace" name="defencePlace" class="form-control" value=""/>
									</div>
								</div>
								
								<div class="form-group"> 
									<label class="col-sm-3 control-label">交通指引:</label>
									<div class="col-sm-8">
										<textarea rows="5" cols="30" class="form-control" id="trafficGuidance" name="trafficGuidance"></textarea>
									</div>
								</div>
								
								<div class="form-group"> 
									<label class="col-sm-3 control-label"><small class="text-red">*</small>答辩人数:</label>
									<div class="col-sm-8">
										<input type="text" id="defenceNum" name="defenceNum" class="form-control" value=""/>
									</div>
								</div>
							</div>
		                </form>
			        </div> 
			        <div class="modal-footer"> 
			        	<button type="button" id="submitB" onclick="submit()" class="btn btn-success min-width-90px margin_r15 btn-save-edit">确定</button>
			            <a href="#" class="btn btn-default min-width-90px btn-cancel-edit" data-dismiss="modal">关闭</a> 
			        </div> 
	        	</div>
	        </div>
	    </div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
$('.single-datetime').daterangepicker({
    singleDatePicker: true,
    showDropdowns : true,
    timePicker : true, //是否显示小时和分钟
    timePickerIncrement : 5, //时间的增量，单位为分钟
    timePicker12Hour : false, //是否使用12小时制来显示时间
    format : 'YYYY-MM-DD HH:mm', //控件中from和to 显示的日期格式
    locale : {
        applyLabel : '确定',
        cancelLabel : '取消',
        daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
        monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
        firstDay : 1
    }
});

$(".operion-edit").click(function() {
	$("#addPlanForm").find("input[name!='settingId']").val("");
	
	var $parentTr = $(this).closest("tr");
	$("#planId").val($parentTr.find("input[name='planId']").val());
	$("#beginTime").val($parentTr.find("input[name='beginTime']").val());
	$("#endTime").val($parentTr.find("input[name='endTime']").val());
	$("#defenceType").val($parentTr.find("input[name='defenceType']").val());
    $('#defenceTeachers1').selectpicker('val', $parentTr.find("input[name='defenceTeachers1']").val().split(','));
    $('#defenceTeachers2').selectpicker('val', $parentTr.find("input[name='defenceTeachers2']").val().split(','));
	$("#defencePlace").val($parentTr.find("input[name='defencePlace']").val());
	$("#trafficGuidance").text($parentTr.find("input[name='trafficGuidance']").val());
	$("#defenceNum").val($parentTr.find("input[name='defenceNum']").val());
	
	$("#addPlanModal").modal({backdrop: 'static', keyboard: false});
});
</script>

</body>

</html>