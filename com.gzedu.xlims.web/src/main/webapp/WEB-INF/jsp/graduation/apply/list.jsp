<%@page import="com.gzedu.xlims.pojo.system.StudyYear"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
    var value1='${param.search_EQ_status1}';
    $('#status1').selectpicker();
    $('#status1').selectpicker('val', value1);
    
    var value2='${param.search_EQ_status2}';
    $('#status2').selectpicker();
    $('#status2').selectpicker('val', value2);
    
    var value3='${param.search_EQ_needDefence}';
    $('#needDefence').selectpicker();
    $('#needDefence').selectpicker('val', value3);
    
    var value4='${param.search_EQ_applyDegree}';
    $('#applyDegree').selectpicker();
    $('#applyDegree').selectpicker('val', value4);
})

function exportInfo() {
	window.location.href='${ctx}/graduation/apply/export?' + $('#listForm').serialize();
}

function importConfirm() {
	var actionName = ctx+"/graduation/apply/importConfirm";
	var downloadFileUrl = ctx+"/graduation/apply/exportTeacherConfirmThesis?fileName=学院定稿导入表.xls";
	var content1 = "为了方便你的工作，我们已经准备好了待定稿的《学院定稿导入表》<br>的标准模板，你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《学院定稿导入表》";
	excelImport(actionName, "file", "graduationApply", downloadFileUrl, null, "批量定稿", null, null, null, content1, content2);
}

function importReject() {
	var actionName = ctx+"/graduation/apply/importReject";
	var downloadFileUrl = ctx+"/graduation/apply/exportTeacherConfirmThesis?fileName=审核不通过导入表.xls";
	var content1 = "为了方便你的工作，我们已经准备好了待定稿的《审核不通过导入表》<br>的标准模板，你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《教材导入表》";
	excelImport(actionName, "file", "graduationApply", downloadFileUrl, null, "审核不通过", null, null, null, content1, content2);
}

function importReviewScore() {
	var actionName = ctx+"/graduation/apply/importReviewScore";
	var downloadFileUrl = ctx+"/graduation/apply/exportReviewThesis?fileName=初评成绩导入表.xls";
	var content1 = "为了方便你的工作，我们已经准备好了初评中的《初评成绩导入表》<br>的标准模板，你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《初评成绩导入表》";
	excelImport(actionName, "file", "graduationApply", downloadFileUrl, null, "导入成绩", null, null, null, content1, content2);
}

function importModifyDefence() {
	var actionName = ctx+"/graduation/apply/importModifyDefence";
	var downloadFileUrl = ctx+"/excelImport/downloadModel?name=答辩方式导入表.xls";
	var content1 = "为了方便你的工作，我们已经准备好了《答辩方式导入表》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《答辩方式导入表》";
	excelImport(actionName, "file", "graduationApply", downloadFileUrl, null, "设置答辩方式", null, null, null, content1, content2);
}
</script>

</head>
<body class="inner-page-body">
		
		<!-- Main content -->
		<section class="content-header">
			
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">论文管理</a></li>
				<li class="active">毕业学员</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<input type="hidden" name="orgId" value="${param.orgId}">
				<div class="box">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">姓名</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_xm" value="${param.search_LIKE_xm}">
									</div>
								</div>
							</div>
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学号</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_xh" value="${param.search_LIKE_xh}">
									</div>
								</div>
							</div>
							<div class="col-md-4 col-xs-6">
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
							
							<div class="col-md-4 col-xs-6">
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
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">毕业论文状态</label>
									<div class="col-sm-9">
										<select id="status1" name="search_EQ_status1" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${thesisStatusMap}" var="thesisStatus">
												<option value="${thesisStatus.key}">${thesisStatus.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">社会实践状态</label>
									<div class="col-sm-9">
										<select id="status2" name="search_EQ_status2" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${practiceStatusMap}" var="practiceStatus">
												<option value="${practiceStatus.key}">${practiceStatus.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学期</label>
									<div class="col-sm-9">
										<select name="search_EQ_gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${gradeMap}" var="grade">
												<option value="${grade.key}"  <c:if test="${grade.key==param['search_EQ_gradeId']}">selected='selected'</c:if>>${grade.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">培养层次</label>
									<div class="col-sm-9">
										<select name="search_EQ_pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${trainingLevelMap}" var="trainingLevel">
												<option value="${trainingLevel.key}"  <c:if test="${trainingLevel.key==param['search_EQ_pycc']}">selected='selected'</c:if>>${trainingLevel.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">答辩形式</label>
									<div class="col-sm-9">
										<select id="needDefence" name="search_EQ_needDefence" class="selectpicker form-control">
											<option value="" selected="selected">请选择</option>
											<option value="0" >无需答辩</option>
											<option value="1" >现场答辩</option>
											<option value="2" >远程答辩</option>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-md-4 col-xs-6">
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
						</div>
							
					</div>
					<div class="box-footer text-right">
						<button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
          				<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
				</div>
				
				
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">学员列表</h3>
						<div class="pull-right no-margin">
							<shiro:hasPermission name="/graduation/apply/list$batchConfirmThesis">
								<a href="javascript:importConfirm()" class="btn btn-default btn-sm">
										<i class="fa fa-fw fa-sign-in"></i> 批量定稿</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/graduation/apply/list$batchApprovalFailed">
								<a href="javascript:importReject()" class="btn btn-default btn-sm margin_l10">
										<i class="fa fa-fw fa-sign-in"></i> 批量审核不通过</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/graduation/apply/list$importScore">
								<a href="javascript:importReviewScore()" class="btn btn-default btn-sm margin_l10" data-role="import">
										<i class="fa fa-fw fa-sign-in"></i> 导入成绩</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/graduation/apply/list$settingDefence">
								<a href="javascript:importModifyDefence()" class="btn btn-default btn-sm margin_l10" data-role="set">
										<i class="fa fa-fw fa-gear"></i> 设置答辩方式</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/graduation/apply/list$exportGraduationApply">
								<a href="javascript:exportInfo()" class="btn btn-default btn-sm margin_l10" data-role="export">
										<i class="fa fa-fw fa-sign-out"></i> 导出毕业申请学生明细</a>
							</shiro:hasPermission>
						</div>
						
					</div>

					<div class="box-body">
						<div class="table-responsive">
									<table class="table table-bordered table-striped vertical-mid table-font">
										<thead>
											<tr>
												<th class="text-center" rowspan="2">个人信息</th>
											    <th class="text-center" rowspan="2">报读信息</th>
											    <th class="text-center" rowspan="2">论文批次</th>
											    <th class="text-center" colspan="6">毕业论文</th>
											    <th class="text-center" colspan="3">社会实践</th>
											    <th class="text-center" rowspan="2">操作</th>
											</tr>
											<tr>
												<th class="text-center">答辩形式</th>
											    <th class="text-center">指导老师</th>
											    <th class="text-center">答辩老师</th>
											    <th class="text-center">初评成绩</th>
											    <th class="text-center">答辩成绩</th>
											    <th class="text-center">状态</th>
											    <th class="text-center">指导老师</th>
											    <th class="text-center">实践成绩</th>
											    <th class="text-center">状态</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td>
																	姓名：${entity.studentName}
																	<c:if test="${entity.applyDegree1 == 1}">
																		<img alt="需要学位" title="需要学位" src="${ctx}/static/images/degree.png">
																	</c:if>
																	<br/>
																	学号：${entity.studentCode}<br/>
																	<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																	手机：${entity.phone}<br/>
																	</shiro:hasPermission>
																	邮箱：${entity.email}
																</td>
																<td>
																	层次：${entity.trainingLevel}<br/>
																	年级：${entity.grade}<br/>
																	专业：${entity.specialtyName}
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.batchId1}">
																			${entity.batchName1}<br/>
																			<span class="gray9">${entity.batchCode1}</span>
																		</c:when>
																		<c:otherwise>
																			${entity.batchName2}<br/>
																			<span class="gray9">${entity.batchCode2}</span>
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.applyId1}">
																			<c:if test="${entity.needDefence1 == 0}">
																				无需答辩
																			</c:if>
																			<c:if test="${entity.needDefence1 == 1}">
																				现场答辩
																			</c:if>
																			<c:if test="${entity.needDefence1 == 2}">
																				远程答辩
																			</c:if>
																		</c:when>
																		<c:otherwise>
																			--
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.guideTeacher1}">
																			${entity.guideTeacherName1}
																		</c:when>
																		<c:otherwise>
																			--
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.defenceTeacher1}">
																			${entity.defenceTeacherName1}
																		</c:when>
																		<c:otherwise>
																			--
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.reviewScore1}">
																			${entity.reviewScore1}
																		</c:when>
																		<c:otherwise>
																			--
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.defenceScore1}">
																			${entity.defenceScore1}
																		</c:when>
																		<c:otherwise>
																			--
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.applyId1}">
																			<c:forEach items="${thesisStatusMap}" var="thesisStatus">
																				<c:if test="${thesisStatus.key == entity.status1 }">${thesisStatus.value}</c:if>
																			</c:forEach>
																		</c:when>
																		<c:otherwise>
																			未申请
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.guideTeacher2}">
																			${entity.guideTeacherName2}
																		</c:when>
																		<c:otherwise>
																			--
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.reviewScore2}">
																			${entity.reviewScore2}
																		</c:when>
																		<c:otherwise>
																			--
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<c:choose>
																		<c:when test="${not empty entity.applyId2}">
																			<c:forEach items="${practiceStatusMap}" var="practiceStatus">
																				<c:if test="${practiceStatus.key == entity.status2 }">${practiceStatus.value}</c:if>
																			</c:forEach>
																		</c:when>
																		<c:otherwise>
																			未申请
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="text-center">
																	<shiro:hasPermission name="/graduation/apply/list$view">
																		<c:choose>
																			<c:when test="${not empty entity.batchId1}">
																				<div class="data-operion">
																					<a href="view?studentId=${entity.studentId}&batchId=${entity.batchId1}"
																						class="operion-item operion-view" data-toggle="tooltip" title="查看">
																						<i class="fa fa-fw fa-view-more"></i></a> 
																				</div>
																			</c:when>
																			<c:otherwise>
																				<div class="data-operion">
																					<a href="view?studentId=${entity.studentId}&batchId=${entity.batchId2}"
																						class="operion-item operion-view" data-toggle="tooltip" title="查看">
																						<i class="fa fa-fw fa-view-more"></i></a> 
																				</div>
																			</c:otherwise>
																		</c:choose>
																	</shiro:hasPermission>
																</td> 
															</tr>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr>
														<td align="center" colspan="13">暂无数据</td>
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
