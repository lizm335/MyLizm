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
$(function() {
})

function view(studentId, batchId) {
	/* $ .ajax({
	  type: "GET",
	  url: "${ctx}/graduation/adviser/viewStudentRecord",
	  data: {"studentId":studentId, "batchId":batchId, "applyType":1},
	  cache: false,
	  success: function(html) {
		  $("#modalBody").html(html);
		  $("#viewStudentRecord").modal();
	  },
	  error: function() {
		  alert("请求超时！");
	  }
	}); */
	var url="${ctx}/graduation/adviser/viewStudentRecord?studentId="+studentId+"&batchId="+batchId+"&applyType=${param.applyType}";
	$.mydialog({
		id:'export',
		width:'80%',
		height:550,
		zIndex:11000,
		content: 'url:'+url
	});
}
</script>

</head>
<body class="inner-page-body">
		
		<!-- Main content -->
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">论文管理</a></li>
				<li class="active">${param.teacherName}指导学生列表</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<input type="hidden" name="teacherId" value="${param.teacherId}">
				<input type="hidden" name="teacherName" value="${param.teacherName}">
				<input type="hidden" name="applyType" value="${param.applyType}">
				<div class="box">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学号</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xh" value="${param['search_LIKE_gjtStudentInfo.xh']}">
									</div>
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">姓名</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
									</div>
								</div>
							</div>
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">论文批次</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtGraduationBatch.batchId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${batchMap}" var="batch">
												<option value="${batch.key}"  <c:if test="${batch.key==param['search_EQ_gjtGraduationBatch.batchId']}">selected='selected'</c:if>>${batch.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-sm-4">
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
						</div>
					</div>
					<div class="box-footer">
						<div class="pull-right">
							<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="pull-right margin_r15">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
				</div>
				
				
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="nav-tabs-custom">
					<ul class="nav nav-tabs nav-tabs-lg">
				      <li <c:if test="${param.applyType == 1}">class="active"</c:if>><a href="viewStudentList?applyType=1&teacherId=${param.teacherId}&teacherName=${param.teacherName}" target="_self">论文指导</a></li>
				      <li <c:if test="${param.applyType == 2}">class="active"</c:if>><a href="viewStudentList?applyType=2&teacherId=${param.teacherId}&teacherName=${param.teacherName}" target="_self">社会实践指导</a></li>
				    </ul>
					
				    <div class="tab-content">
						
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
									<tr>
										<th>个人信息</th>
									    <th>报读信息</th>
									    <th>论文批次</th>
									    <c:choose>
									    	<c:when test="${param.applyType == 1}">
									    		<th>初评成绩</th>
									    	</c:when>
									    	<c:when test="${param.applyType == 2}">
									    		<th>实践成绩</th>
									    	</c:when>
									    </c:choose>
									    <th>状态</th>
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
																手机：${entity.gjtStudentInfo.sjh}<br/>
																</shiro:hasPermission>
																邮箱：${entity.gjtStudentInfo.dzxx}
															</div>
														</td>
														<td>
															<div class="text-left">
																层次：${trainingLevelMap[entity.gjtStudentInfo.pycc]}<br/>
																年级：${entity.gjtStudentInfo.gjtGrade.gradeName}<br/>
																专业：${entity.gjtStudentInfo.gjtSpecialty.zymc}
															</div>
														</td>
														<td>
															${entity.gjtGraduationBatch.batchName}<br/>
															<span class="gray9">${entity.gjtGraduationBatch.batchCode}</span>
														</td>
														<td>
															<c:choose>
																<c:when test="${not empty entity.reviewScore}">${entity.reviewScore}</c:when>
																<c:otherwise>--</c:otherwise>
															</c:choose>
														</td>
														<td>
															${statusMap[entity.status]}
														</td>
														<td>
															<shiro:hasPermission name="/graduation/adviser/viewStudentList?applyType=1$view">
																<a href="javascript:view('${entity.gjtStudentInfo.studentId}', '${entity.gjtGraduationBatch.batchId}')"
																	class="operion-item operion-view" data-toggle="tooltip" title="查看">
																	<i class="fa fa-fw fa-view-more"></i></a> 
															</shiro:hasPermission>
														</td> 
													</tr>
												</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td align="center" colspan="8">暂无数据</td>
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
		
	    <div id="viewStudentRecord" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
	    	<div class="modal-dialog">
	    		<div class="modal-content">
	        <div class="modal-header"> 
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">指导详情</h4>
	        </div> 
	        <div id="modalBody" class="modal-body"> 
	        </div> 
	        <div class="modal-footer"> 
	            <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
	        </div> 
	        	</div>
	        </div>
	    </div> 
</body>
</html>
