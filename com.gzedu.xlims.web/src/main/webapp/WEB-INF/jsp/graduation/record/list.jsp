<%@page import="com.gzedu.xlims.pojo.system.StudyYear"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<c:set var="ctx">${pageContext.request.contextPath}</c:set>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
    var value1='${param.search_EQ_applyDegree}';
    $('#applyDegree').selectpicker();
    $('#applyDegree').selectpicker('val', value1);
    
    var value2='${param.search_EQ_status}';
    $('#status').selectpicker();
    $('#status').selectpicker('val', value2);
    
    var value3='${param.search_EQ_graduationCondition}';
    $('#graduationCondition').selectpicker();
    $('#graduationCondition').selectpicker('val', value3);
    
    var value4='${param.search_EQ_degreeCondition}';
    $('#degreeCondition').selectpicker();
    $('#degreeCondition').selectpicker('val', value4);
})

function accept(applyId) {
	$.confirm({
        title: '提示',
        content: '确认受理？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
          	 $.get("accept",{applyId:applyId},function(data){
            		if(data.successful){
            			window.location.reload();
            		}else{
            			alert(data.message);
            		}
            },"json"); 
        } 
    });
}

function showFirstTrial(applyId, applyDegree) {
	$("#applyId").val(applyId);
	if (applyDegree == 1) {
		$("#degreeConditionDiv").show();
	} else {
		$("#degreeConditionDiv").hide();
	}
	$("#trialModal").modal();
}

function firstTrial() {
	$ .ajax({
		  type: "POST",
		  url: $('#trialForm').attr("action"),
		  data: $('#trialForm').serialize(),
		  dataType: "json",
		  cache: false,
		  success: function(data) {
			  if(data.successful){
      			window.location.reload();
	      	  }else{
	      		alert(data.message);
	      	  }
		  },
		  error: function() {
			  alert("请求超时！");
		  }
	});
}

function showFinalTrial(applyId) {
	$("#applyId2").val(applyId);
	$("#finalTrialModal").modal();
}


function finalTrial() {
	$ .ajax({
		  type: "POST",
		  url: $('#finalTrialForm').attr("action"),
		  data: $('#finalTrialForm').serialize(),
		  dataType: "json",
		  cache: false,
		  success: function(data) {
			  if(data.successful){
    			window.location.reload();
	      	  }else{
	      		alert(data.message);
	      	  }
		  },
		  error: function() {
			  alert("请求超时！");
		  }
	});
}

function showUploadCertificate(applyId, graduationCertificateNo, graduationCertificateUrl, degreeCertificateNo, degreeCertificateUrl, applyDegree) {
	$("#applyId3").val(applyId);
	$("#graduationCertificateNo").val(graduationCertificateNo);
	$("#graduationCertificateUrl").val(graduationCertificateUrl);
	$("#graduationCertificateImg").attr("src", graduationCertificateUrl);
	$("#degreeCertificateNo").val(degreeCertificateNo);
	$("#degreeCertificateUrl").val(degreeCertificateUrl);
	$("#degreeCertificateImg").attr("src", degreeCertificateUrl);
	
	if (applyDegree == 1) {
		$("#degreeCertificateNoDiv").show();
		$("#degreeCertificateUrlDiv").show();
	} else {
		$("#degreeCertificateNoDiv").hide();
		$("#degreeCertificateUrlDiv").hide();
	}
	
	$("#uploadCertificateModal").modal();
}

function uploadCertificate() {
	$ .ajax({
		  type: "POST",
		  url: $('#uploadCertificateForm').attr("action"),
		  data: $('#uploadCertificateForm').serialize(),
		  dataType: "json",
		  cache: false,
		  success: function(data) {
			  if(data.successful){
  				window.location.reload();
	      	  }else{
	      		alert(data.message);
	      	  }
		  },
		  error: function() {
			  alert("请求超时！");
		  }
	});
}

function clearImage(img, url) {
	$("#"+img).attr("src", "");
	$("#"+url).val("");
}

function completed(applyId) {
	$.confirm({
        title: '提示',
        content: '确认学员领取毕业证原件？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
          	 $.get("completed",{applyId:applyId},function(data){
            		if(data.successful){
            			window.location.reload();
            		}else{
            			alert(data.message);
            		}
            },"json"); 
        } 
    });
}

function exportInfo() {
	window.location.href='${ctx}/graduation/record/export?' + $('#listForm').serialize();
}

function importConfirm() {
	if($('#file').val() == '') {
		alert('请先选择文件');
		return false;
	}
	
	var s = $("#file").val();
	var a = s.lastIndexOf('.');
	var t = s.substr(a+1,s.length);
	if(t !="xls"){
		alert('请选择Excel2003文件');
		return false;
	}
	
	$('#uploadForm').submit();
}

function importFinalTrial() {
	var actionName = ctx+"/graduation/record/importFinalTrial";
	var downloadFileUrl = ctx+"/excelImport/downloadModel?name=终审结果导入表.xls";
	var content1 = "为了方便你的工作，我们已经准备好了《终审结果导入表》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《终审结果导入表》";
	excelImport(actionName, "file", "graduationRecord", downloadFileUrl, null, "批量录入终审结果", null, null, null, content1, content2);
}
</script>

</head>
<body class="inner-page-body">
		
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">毕业管理</a></li>
				<li class="active">毕业学员管理</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box box-border">
					<div class="box-body">
						<div class="row pad-t15">
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
										<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xh" value="${param['search_LIKE_gjtStudentInfo.xh']}">
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${specialtyMap}" var="specialty">
												<option value="${specialty.key}"  <c:if test="${specialty.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${specialty.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">年级</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.gjtSignup.gjtEnrollBatch.gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${gradeMap}" var="grade">
												<option value="${grade.key}"  <c:if test="${grade.key==param['search_EQ_gjtStudentInfo.gjtSignup.gjtEnrollBatch.gjtGrade.gradeId']}">selected='selected'</c:if>>${grade.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">培养层次</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${trainingLevelMap}" var="trainingLevel">
												<option value="${trainingLevel.key}"  <c:if test="${trainingLevel.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if>>${trainingLevel.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">毕业状态</label>
									<div class="col-sm-9">
										<select id="status" name="search_EQ_status" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${recordStatusMap}" var="recordStatus">
												<option value="${recordStatus.key}">${recordStatus.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">毕业条件</label>
									<div class="col-sm-9">
										<select id="graduationCondition" name="search_EQ_graduationCondition" class="selectpicker form-control">
											<option value="" selected="selected">请选择</option>
											<option value="0" >未达标</option>
											<option value="1" >已达标</option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">申请学位</label>
									<div class="col-sm-9">
										<select id="applyDegree" name="search_EQ_applyDegree" class="selectpicker form-control">
											<option value="" selected="selected">请选择</option>
											<option value="0" >否</option>
											<option value="1" >是</option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学位条件</label>
									<div class="col-sm-9">
										<select id="degreeCondition" name="search_EQ_degreeCondition" class="selectpicker form-control">
											<option value="" selected="selected">请选择</option>
											<option value="0" >未达标</option>
											<option value="1" >已达标</option>
										</select>
									</div>
								</div>
							</div>
						</div>
							
					</div>
					<div class="box-footer text-right">
						<button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
						<button type="reset" class="btn min-width-90px btn-default">重置</button>
					</div>
				</div>
				
				
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box box-border margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">毕业学员列表</h3>
						<div class="pull-right">
							<a href="javascript:importFinalTrial()" class="btn btn-default btn-sm margin_r10">
									<i class="fa fa-fw fa-shxxjl f16"></i> 批量录入终审结果</a>
							<a href="javascript:exportInfo()" class="btn btn-default btn-sm">
									<i class="fa fa-fw fa-sign-out"></i> 批量导出</a>
						</div>
						
					</div>

					<div class="box-body">
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
									<tr>
										<th>个人信息</th>
									    <th>报读信息</th>
									    <th>毕业状态</th>
									    <th>毕业条件</th>
									    <th>学位状态</th>
									    <th>学位条件</th>
									    <th>电子证书状态</th>
									    <th>证书编号</th>
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
															<div class="text-left">
																姓名：${entity.gjtStudentInfo.xm}<br/>
																学号：${entity.gjtStudentInfo.xh}<br/>
																<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																手机：${entity.gjtStudentInfo.sjh}
																</shiro:hasPermission>
															</div>
														</td>
														<td>
															<div class="text-left">
																层次：${trainingLevelMap[entity.gjtStudentInfo.pycc]}<br/>
																年级：${gradeMap[entity.gjtStudentInfo.gjtGrade.gradeId]}<br/>
																专业：${specialtyMap[entity.gjtStudentInfo.gjtSpecialty.specialtyId]}
															</div>
														</td>
														<td>${recordStatusMap[entity.isReceive]}</td>
														<td>
															<c:choose>
																<c:when test="${not empty entity.graduationCondition}">
																	<c:choose>
																		<c:when test="${entity.graduationCondition == 0}">
																			毕业条件：未达标
																		</c:when>
																		<c:otherwise>
																			毕业条件：已达标
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	毕业条件：--
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<c:choose>
																<c:when test="${entity.applyDegree == 0}">
																	未申请学位
																</c:when>
																<c:otherwise>
																	申请学位
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<%--<c:choose>
																<c:when test="${not empty entity.degreeCondition}">
																	<c:choose>
																		<c:when test="${entity.degreeCondition == 0}">
																			学位条件：未达标
																		</c:when>
																		<c:otherwise>
																			学位条件：已达标
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	学位条件：--
																</c:otherwise>
															</c:choose>--%>
														</td>
														<td>
															<c:choose>
																<c:when test="${entity.isReceive > 3}">
																	<c:choose>
																		<c:when test="${not empty entity.graduationCertificateUrl}">
																			已上传
																		</c:when>
																		<c:otherwise>
																			待上传
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	--
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<c:choose>
																<c:when test="${entity.isReceive > 3}">
																	<c:choose>
																		<c:when test="${not empty entity.graduationCertificateNo}">
																			毕业证书：${entity.graduationCertificateNo}
																		</c:when>
																		<c:otherwise>
																			毕业证书：待上传
																		</c:otherwise>
																	</c:choose>
																	<c:if test="${entity.applyDegree == 1}">
																		<br>
																		<c:choose>
																			<c:when test="${not empty entity.degreeCertificateNo}">
																				学位证书：${entity.degreeCertificateNo}
																			</c:when>
																			<c:otherwise>
																				学位证书：待上传
																			</c:otherwise>
																		</c:choose>
																	</c:if>
																</c:when>
																<c:otherwise>
																	--
																</c:otherwise>
															</c:choose>
														</td>
														<td>
																	<div class="data-operion">
																		<a href="view?recordId=${entity.applyId}"
																			class="operion-item operion-view" data-toggle="tooltip" title="查看学员详情">
																			<i class="fa fa-fw fa-view-more"></i></a> 
																		
																		<a href="http://www.study.tt.ouchgzee.com/view/viewGraduationRegister.do?studentId=${entity.gjtStudentInfo.studentId}"
																			class="operion-item operion-view" data-toggle="tooltip" title="预览毕业生登记表" target="_blank">
																			<i class="fa fa-fw fa-eye"></i></a> 	
																			
																		<a href="download?studentId=${entity.gjtStudentInfo.studentId}"
																			class="operion-item operion-view" data-toggle="tooltip" title="下载毕业生登记表">
																			<i class="fa fa-fw fa-download"></i></a> 
																			
																		<c:if test="${entity.isReceive == 0}">
																			<a href="javascript:accept('${entity.applyId}')"
																				class="operion-item operion-view" data-toggle="tooltip" title="受理毕业申请">
																				<i class="fa fa-fw fa-pencil-square-o"></i></a> 
																		</c:if>
																			
																		<c:if test="${entity.isReceive == 1}">
																			<a href="javascript:showFirstTrial('${entity.applyId}', '${entity.applyDegree}')"
																				class="operion-item operion-view" data-toggle="tooltip" title="初审">
																				<i class="fa fa-fw fa-pencil-square-o"></i></a> 
																		</c:if>
																			
																		<c:if test="${entity.isReceive > 0 && entity.isReceive < 5}">
																			<a href="javascript:showFinalTrial('${entity.applyId}')"
																				class="operion-item operion-view" data-toggle="tooltip" title="录入终审结果">
																				<i class="fa fa-fw fa-simulated-login"></i></a> 
																		</c:if>
																			
																		<c:if test="${entity.isReceive > 3}">
																			<a href="javascript:showUploadCertificate('${entity.applyId}', '${entity.graduationCertificateNo}', '${entity.graduationCertificateUrl}', '${entity.degreeCertificateNo}', '${entity.degreeCertificateUrl}', '${entity.applyDegree}')"
																				class="operion-item operion-view" data-toggle="tooltip" title="上传电子证件">
																				<i class="fa fa-fw fa-upload"></i></a> 
																		</c:if>
																			
																		<c:if test="${entity.isReceive == 4}">
																			<a href="javascript:completed('${entity.applyId}')"
																				class="operion-item operion-view" data-toggle="tooltip" title="确认学员领取毕业证原件">
																				<i class="fa fa-fw fa-jssq"></i></a> 
																		</c:if>
																	</div>
														</td> 
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
							<tags:pagination page="${pageInfo}" paginationSize="5" />
						</div>
					</div>
				</div>
		</form>
		</section>

<div id="trialModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="trialLabel" aria-hidden="true"> 
 	<div class="modal-dialog">
 		<div class="modal-content">
     <div class="modal-header"> 
         <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
         <h4 class="modal-title" id="trialLabel">审核</h4>
     </div> 
     <div class="modal-body"> 
     	<form method="post" id="trialForm" class="form-horizontal" role="form" action="${ctx}/graduation/record/firstTrial">
			<input type="hidden" id="applyId" name="applyId" value=""/>
			<div class="box-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">毕业条件:</label>
					<div class="col-sm-8">
						<select id="graduationCondition" name="graduationCondition" class="selectpicker show-tick form-control" data-size="5">
							<option value="0">未达标</option>
							<option value="1" selected="selected">已达标</option>
						</select>
					</div>
				</div>
				<div class="form-group" id="degreeConditionDiv">
					<label class="col-sm-3 control-label">学位条件:</label>
					<div class="col-sm-8">
						<select id="degreeCondition" name="degreeCondition" class="selectpicker show-tick form-control" data-size="5">
							<option value="0">未达标</option>
							<option value="1" selected="selected">已达标</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">初审结果:</label>
					<div class="col-sm-8">
						<select id="firstTrial" name="firstTrial" class="selectpicker show-tick form-control" data-size="5">
							<option value="0">未通过</option>
							<option value="1">通过</option>
							<option value="2" selected="selected">待定</option>
						</select>
					</div>
				</div>
			</div>
         </form>
     </div> 
     <div class="modal-footer"> 
     	<button type="button" onclick="firstTrial()" class="btn btn-success">确定</button>
        <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
     </div> 
     	</div>
     </div>
 </div>
 
 <div id="finalTrialModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="finalTrialLabel" aria-hidden="true"> 
 	<div class="modal-dialog">
 		<div class="modal-content">
     <div class="modal-header"> 
         <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
         <h4 class="modal-title" id="finalTrialLabel">最终审核</h4>
     </div> 
     <div class="modal-body"> 
     	<form method="post" id="finalTrialForm" class="form-horizontal" role="form" action="${ctx}/graduation/record/finalTrial">
			<input type="hidden" id="applyId2" name="applyId" value=""/>
			<div class="box-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">终审结果:</label>
					<div class="col-sm-8">
						<select id="finalTrial" name="finalTrial" class="selectpicker show-tick form-control" data-size="5">
							<option value="0">未通过</option>
							<option value="1" selected="selected">通过</option>
						</select>
					</div>
				</div>
			</div>
         </form>
     </div> 
     <div class="modal-footer"> 
     	<button type="button" onclick="finalTrial()" class="btn btn-success">确定</button>
        <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
     </div> 
     	</div>
     </div>
 </div>
 
 <div id="uploadCertificateModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="uploadCertificateLabel" aria-hidden="true"> 
 	<div class="modal-dialog">
 		<div class="modal-content">
     <div class="modal-header"> 
         <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
         <h4 class="modal-title" id="uploadCertificateLabel">上传电子证件</h4>
     </div> 
     <div class="modal-body"> 
     	<form method="post" id="uploadCertificateForm" class="form-horizontal" role="form" action="${ctx}/graduation/record/uploadCertificate">
			<input type="hidden" id="applyId3" name="applyId" value=""/>
			<div class="box-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">毕业证书编号:</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" id="graduationCertificateNo" name="graduationCertificateNo" value="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">毕业证书:</label>
					<div class="col-sm-8">
						<input type="hidden" id="graduationCertificateUrl" name="graduationCertificateUrl" class="form-control" value=""/>
						<img id="graduationCertificateImg" alt="请上传" src="" width="60" height="60">
						<button class="btn btn-default" type="button" onclick="javascript:uploadImage('graduationCertificateImg','graduationCertificateUrl')">上传</button>
						<button class="btn btn-default" type="button" onclick="javascript:clearImage('graduationCertificateImg','graduationCertificateUrl')">清空</button>
					</div>
				</div>
				<div class="form-group" id="degreeCertificateNoDiv">
					<label class="col-sm-3 control-label">学位证书编号:</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" id="degreeCertificateNo" name="degreeCertificateNo" value="">
					</div>
				</div>
				<div class="form-group" id="degreeCertificateUrlDiv">
					<label class="col-sm-3 control-label">学位证书:</label>
					<div class="col-sm-8">
						<input type="hidden" id="degreeCertificateUrl" name="degreeCertificateUrl" class="form-control" value=""/>
						<img id="degreeCertificateImg" alt="请上传" src="" width="60" height="60">
						<button class="btn btn-default" type="button" onclick="javascript:uploadImage('degreeCertificateImg','degreeCertificateUrl')">上传</button>
						<button class="btn btn-default" type="button" onclick="javascript:clearImage('degreeCertificateImg','degreeCertificateUrl')">清空</button>
					</div>
				</div>
			</div>
         </form>
     </div> 
     <div class="modal-footer"> 
     	<button type="button" onclick="uploadCertificate()" class="btn btn-success">确定</button>
        <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
     </div> 
     	</div>
     </div>
 </div>
		
	<!-- upload form start -->
	<div id="importModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="uploadForm" name="uploadForm" action="importFinalTrial" method="post" target="temp_target" enctype="multipart/form-data">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                    <h4 class="modal-title">批量录入终审结果</h4>
	                </div>
	                <div class="modal-body">
	                    <input name=file id="file" type="file" />
	                </div>
	                <div class="modal-footer">
	                    <button type="button" id="confirmB" onclick="importConfirm()" class="btn btn-success">上传</button>
	            		<a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	<!-- upload form end -->
 
<jsp:include page="/eefileupload/upload.jsp" />
 
 	<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>
