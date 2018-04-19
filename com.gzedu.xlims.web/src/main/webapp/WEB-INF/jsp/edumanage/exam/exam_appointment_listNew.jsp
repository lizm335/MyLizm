<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.Date"%>
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
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试管理</a></li>
		<li class="active">考试预约</li>
	</ol>
</section>
		
		<section class="content">
			<form id="listForm" class="form-horizontal">
				<!-- 搜索栏 start -->
				<div class="box">
					<div class="box-body">
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_student.xm"  value="${param['search_LIKE_student.xm']}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_student.xh"   value="${param['search_EQ_student.xh']}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">学期</label>
								<div class="col-sm-9">
									<select name="search_EQ_student.gjtGrade.gradeId" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${termMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key == param['search_EQ_student.gjtGrade.gradeId']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						
						<div class="row reset-form-horizontal clearbox">
						
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试计划</label>
								<div class="col-sm-9">
									<select id="examBatchCode" name="search_EQ_examPlanNew.examBatchCode" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true">
										<option value="">请选择</option>
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
										<option value=''>请选择</option>
									</select>
								</div>
							</div>
						
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试形式</label>
								<div class="col-sm-9">
									<select name="search_EQ_examPlanNew.type" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<option value="" >请选择</option>
										<c:forEach items="${examTypeMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==param['search_EQ_examPlanNew.type']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
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
										<c:forEach items="${studyCenterMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==param['search_EQ_student.gjtStudyCenter.id']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
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
				<!-- 搜索栏 end -->
				
				<div class="box">
					<!-- 按钮栏 start -->
					<div class="box-header with-border">
						<div class="fr">
							<shiro:hasPermission name="/exam/new/appointment/list$exportPointAppointment">
								<div class="btn-wrap fl">
									<a href="#" class="btn btn-default btn-sm" data-toggle="modal" data-target="#exportModal2">
											<i class="fa fa-fw fa-download"></i> 导出考点预约数据</a>
								</div>
							</shiro:hasPermission>
							
							<shiro:hasPermission name="/exam/new/appointment/list$importPointAppointment">
								<div class="btn-wrap fl">
									<a href="#" class="btn btn-default btn-sm btn-import2" >
											<i class="fa fa-fw fa-upload"></i> 导入考点预约数据</a>
								</div> 
							</shiro:hasPermission>
							
							<shiro:hasPermission name="/exam/new/appointment/list$exportAppointment">
								<div class="btn-wrap fl">
									<a href="${ctx}/excelExport/validateSmsCode/${pageInfo.totalElements}?formAction=/exam/new/appointment/export/plan" class="btn btn-default btn-sm" data-role="export">
											<i class="fa fa-fw fa-download"></i> 导出考试预约数据</a>
								</div>
							</shiro:hasPermission>
							
							<shiro:hasPermission name="/exam/new/appointment/list$importAppointment">
								<div class="btn-wrap fl">
									<a href="#" class="btn btn-default btn-sm btn-import" >
											<i class="fa fa-fw fa-upload"></i> 导入考试预约数据</a>
								</div> 
							</shiro:hasPermission>
						</div>
					</div>
					<!-- 按钮栏 end -->	
	
					<!-- 列表内容 start -->
					<div class="box-body">
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
												<th>个人信息</th>
												<th>报读信息</th>
												<th>考试计划</th>
												<th>考试科目</th>
												<th>考试形式</th>
												<th>预约考点</th>
												<th>考试预约时间</th>
												<th>是否补考</th>
												<th>补考费用</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pageInfo.content}" var="entity">
												<c:if test="${not empty entity}">
													<tr>
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
															${entity.examPlanNew.examPlanName}(${entity.examPlanNew.examNo})
															<%-- <c:forEach items="${entity.examPlanNew.gjtCourseList}" var="c">
																${c.kcmc}<span class="gray9">（${c.kch}）</span><br>
															</c:forEach> --%>
														<td>
															${examTypeMap[entity.examPlanNew.type]}
														</td>
														<td>
															${entity.examPointName}
														</td>
														<td>
															<fmt:formatDate value="${entity.createdDt}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
														</td>
														<c:choose>
															<c:when test="${entity.isResit}">
																<td>
																	是
																</td>
																<td>
																	${entity.resitCost}
																</td> 
															</c:when>
															<c:otherwise>
																<td>
																	否
																</td>
																<td>
																	--
																</td> 
															</c:otherwise>
														</c:choose>
													</tr>
												</c:if>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<tags:pagination page="${pageInfo}" paginationSize="11" />
						</div>
					</div>
					<!-- 列表内容 end -->
				</div>
			</form>
	</section>
	
	<%-- <div id="exportModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="uploadForm" name="uploadForm" action="${ctx}/exam/new/appointment/export/plan" method="post" target="temp_target" enctype="multipart/form-data">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">导出考试预约数据</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div>
	                		<p>
	                		请选择需要导出考试预约数据的考试计划
		                    </p>    
							<div class="col-sm-9">
								<select id="m_examBatchCode" name="examBatchCode" class="selectpicker show-tick form-control"
									data-size="5" data-live-search="true">
									<option value="" >请选择</option>
									<c:forEach items="${batchMap}" var="map">
										<option value="${map.key}">${map.value}</option>
									</c:forEach>
								</select>
							</div>
							
						</div>
	                </div>
	                <br>
	                <br>
	                <br>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">取消</button>
	                    <button type="button" class="btn btn-primary" id="plan_export">导出</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div> --%>
	
	<div id="exportModal2" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2">
	    <div class="modal-dialog">
	        <form id="uploadForm2" name="uploadForm" action="${ctx}/exam/new/appointment/export/point" method="post" target="temp_target" enctype="multipart/form-data">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">导出考点预约数据</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div>
	                		<p>
	                		请选择需要导出考点预约数据的考试计划
		                    </p>    
							<div class="col-sm-9">
								<select id="m_examBatchCode2" name="examBatchCode" class="selectpicker show-tick form-control"
									data-size="5" data-live-search="true">
									<option value="" >请选择</option>
									<c:forEach items="${batchMap}" var="map">
										<option value="${map.key}">${map.value}</option>
									</c:forEach>
								</select>
							</div>
							
						</div>
	                </div>
	                <br>
	                <br>
	                <br>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">取消</button>
	                    <button type="button" class="btn btn-primary" id="point_export">导出</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
<script type="text/javascript">
var examPlanId = '${param["search_EQ_examPlanNew.examPlanId"]}';
</script>
<%
Date time = new Date();
%>
<script src="${ctx}/static/js/exam/exam_appointment_list.js?time=<%=time%>"></script>
</html>
