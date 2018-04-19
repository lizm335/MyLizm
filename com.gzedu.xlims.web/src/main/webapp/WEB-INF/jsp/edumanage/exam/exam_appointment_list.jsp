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
var ctx = "${ctx}";
$(function() {
	$('#examBatchCode').change(function(){
		var examBatchCode = $('#examBatchCode').val();
		if(examBatchCode!=''){
			$.get("${ctx}/exam/new/batch/queryExamPoint",{examBatchCode:examBatchCode},function(data,status){
				$('#examPoint').empty();
				$("#examPoint").append("<option value=''>请选择</option>");
				$.each(data, function (i) {
					if ("${param['search_EQ_examPoint']}" == data[i].id) {
						$("#examPoint").append("<option selected='selected' value='"+data[i].id+"'>"+data[i].name+"</option>");
					} else {
						$("#examPoint").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
					}
	        	});
				$('#examPoint').selectpicker('refresh'); 
			  },"json");
		} else {
			$('#examPoint').empty();
			$("#examPoint").append("<option value=''>请选择</option>");
			$('#examPoint').selectpicker('refresh'); 
		}
	});
	$('#examBatchCode').change();
})

function importConfirm() {
	if($('#file').val() == '') {
		alert('请先选择文件');
		return false;
	}
	$("#importForm").submit();
}

function importConfirm22() {
	console.log("sdf");
	if($('#file').val() == '') {
		alert('请先选择文件');
		return false;
	} 
	$("#importForm").attr("action","importAppointmentRecord2");
	$("#importForm").submit();
}   

function importPointConfirm() {
	if($('#file2').val() == '') {
		alert('请先选择文件');
		return false;
	}
	$("#importPointForm").submit();
}
</script>

</head>
<body class="inner-page-body">
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li class="active">考试预约</li>
	</ol>
</section>
		
		<section class="content">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs">
				  <li <c:if test="${exam_type==1}">class="active"</c:if>><a href="list?search_EQ_type=1" target="_self" >网考</a></li>
				  <li <c:if test="${exam_type==2}">class="active"</c:if>><a href="list?search_EQ_type=2" target="_self" >笔考</a></li>
				  <li <c:if test="${exam_type==3}">class="active"</c:if>><a href="list?search_EQ_type=3" target="_self" >机考</a></li>
				  <li <c:if test="${exam_type==4}">class="active"</c:if>><a href="list?search_EQ_type=4" target="_self" >形考</a></li>
				  <li <c:if test="${exam_type==5}">class="active"</c:if>><a href="list?search_EQ_type=5" target="_self" >网考（省）科目</a></li>
				</ul>
			
		
			<form id="listForm" class="form-horizontal">
				<input type="hidden" name="search_EQ_type" id="search_EQ_type" value="${exam_type}"  />
			
				<!-- 搜索栏 start -->
				<div class="box">
					<div class="box-body">
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_student.xm" id="search_LIKE_student.xm" value="${param['search_LIKE_student.xm']}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_student.xh" id="search_LIKE_student.xh" value="${param['search_LIKE_student.xh']}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">排考状态</label>
								<div class="col-sm-9">
									<select name="search_EQ_status" id="search_EQ_status" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<option value="-1" selected="selected">请选择</option>
										<option value="0" <c:if test="${param['search_EQ_status']==0}">selected='selected'</c:if> >待排考</option>
										<option value="1" <c:if test="${param['search_EQ_status']==1}">selected='selected'</c:if> >已排考</option>
									</select>
								</div>
							</div>
						</div>
						
						<div class="row reset-form-horizontal clearbox">
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试科目</label>
								<div class="col-sm-9">
									<select name="search_EQ_examPlanNew.subjectCode" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${subjectList}" var="subject">
											<option value="${subject.subjectCode}"<c:if test="${subject.subjectCode==param['search_EQ_examPlanNew.subjectCode']}">selected='selected'</c:if> >${subject.name}（试卷号：${subject.examNo}）</option>
										</c:forEach>
									</select>
								</div>
							</div>
						
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试批次</label>
								<div class="col-sm-9">
									<select id="examBatchCode" name="search_EQ_examPlanNew.examBatchCode" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${batchMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==param['search_EQ_examPlanNew.examBatchCode']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						
							<div class="col-md-4">
								<label class="control-label col-sm-3">预约考点</label>
								<div class="col-sm-9">
									<select id="examPoint" name="search_EQ_examPoint" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
									</select>
								</div>
							</div>
							
						</div>
					</div>
					<div class="box-footer">
<!-- 						<div class="search-more-in">
							高级搜索<i class="fa fa-fw fa-caret-down"></i>
						</div> -->
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
							<div class="btn-wrap fl">
								<a href="#" class="btn btn-default btn-sm margin_r10" data-toggle="modal" id="point_export">
										<i class="fa fa-fw fa-sign-out"></i> 导出预约考点数据</a>
							</div>
						
							<div class="btn-wrap fl">
								<a href="#" class="btn btn-default btn-sm margin_r10" data-toggle="modal" data-target="#exportModal" >
										<i class="fa fa-fw fa-sign-out"></i> 导出预约考试数据</a>
							</div>
							
							<div class="btn-wrap fl">
								<a href="#" class="btn btn-default btn-sm margin_r10" data-toggle="modal" data-target="#importModal" >
										<i class="fa fa-fw fa-download"></i> 导入预约考试数据</a>
							</div> 
							
							<div class="btn-wrap fl">
								<a href="#" class="btn btn-default btn-sm margin_r10" data-toggle="modal" data-target="#importPointModal" >
										<i class="fa fa-fw fa-download"></i> 导入预约考点数据</a>
							</div> 
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
												<th><input type="checkbox" class="select-all"></th>
												<th>个人信息</th>
												<th>报读信息</th>
												<th>考试批次</th>
												<th>考试科目</th>
												<th>考试形式</th>
												<th>预约考点</th>
												<th>考试预约时间</th>
												<th>排考状态</th>
												<th>是否补考</th>
												<th>补考费用</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pageInfo.content}" var="entity">
												<c:if test="${not empty entity}">
													<tr>
														<td><input class="checkbox" type="checkbox" name="ids"
															data-id="${entity.appointmentId}" data-name="check-id"
															value="${entity.appointmentId}"></td>
														<td>
															姓名:  ${entity.student.xm}<br>
															学号:  ${entity.student.xh}<br>
															<shiro:hasPermission name="/personal/index$privacyJurisdiction">
															手机号: ${entity.student.sjh}
															</shiro:hasPermission>
														</td>
														<td>
															<c:forEach items="${studentViewMap}" var="map">
																<c:if test="${map.key==entity.studentId}">
																	层次: ${trainingLevelMap[map.value['pycc']]} <br> 
																</c:if>
																<c:if test="${map.key==entity.studentId}">
																	年级: ${map.value['gradeName']} <br>
																</c:if>
																<c:if test="${map.key==entity.studentId}">
																	专业: ${map.value['zymc']} <br>
																</c:if>
															</c:forEach>
														</td>
														<td>
															${entity.examPlanNew.examBatchNew.name}<br>
															${entity.examPlanNew.examBatchNew.examBatchCode}
														</td>
														<td>${entity.examPlanNew.examSubjectNew.name}<br>
															${entity.examPlanNew.examSubjectNew.subjectCode}</td>
														<td>
															<c:if test="${entity.examPlanNew.type==1}">网考 </c:if>
															<c:if test="${entity.examPlanNew.type==2}">笔考</c:if>
															<c:if test="${entity.examPlanNew.type==3}">机考</c:if>
															<c:if test="${entity.examPlanNew.type==4}">形考</c:if>
															<c:if test="${entity.examPlanNew.type==5}">网考（省）</c:if>
														</td>
														<td>
															<%-- ${entity.gjtExamStudentRoomNew.gjtExamRoomNew.examPonitNew.name} --%>
															<c:forEach items="${appointPointMap}" var="map">
																<c:if test="${map.key==entity.studentId}">
																	${map.value}  
																</c:if>
															</c:forEach>
														</td>														
														<td>
															<fmt:formatDate value="${entity.updatedDt}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
														</td>
														<td>
															<c:if test="${entity.gjtExamStudentRoomNew!=null}">
																<span class="text-green">已排考</span>
															</c:if>
															<c:if test="${entity.gjtExamStudentRoomNew==null}">
																<span class="text-red">待排考
															</c:if>
														</td>
														<td>
															<!-- todo -->
														</td>
														<td>
															<!-- todo -->
														</td> 
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
		</div>	
	</section>
	
	<div id="exportModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="uploadForm" name="uploadForm" action="${ctx}/exam/new/appointment/export/plan" method="post" target="temp_target" enctype="multipart/form-data">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">导出预约考试数据</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div>
	                		<p>
	                		请选择需要导出预约考试数据的考试批次
		                    </p>    
							<div class="col-sm-9">
								<select id="examBatchCode" name="examBatchCode" class="selectpicker show-tick form-control" 
									data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
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
	</div>
	
	<!-- upload form start -->
	<div id="importModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="importForm" name="importForm" action="importAppointmentRecord" method="post" target="temp_target" enctype="multipart/form-data">
	        	<input type="hidden" name="examType" value="${exam_type}"  />
	            <div class="modal-content"> 
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">导入学员课程预约</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div>
	                		<p>
	                		请选择考试批次，并点击下载按钮下载导入模版
		                    </p>    
							<div class="col-sm-9">
								<select name="batchCode" class="selectpicker show-tick form-control" 
									data-size="10" data-live-search="true">
									<c:forEach items="${batchMap}" var="map">
										<option value="${map.key}">${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<br><br>
						<div>
							<p>
								<a href="${ctx}/excel/download?name=学员预约课程导入表模板.xlsx" target="_blank" class="btn btn-xs btn-success">
		                            <i class="fa fa-fw fa-download"></i> 下载导入模版
		                        </a>
		                   </p>
						</div> 
	                     
	                    <input name="file" id="file" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
	                    <div class="progress">　
	                        <div style="width: 0%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="0" role="progressbar" class="progress-bar progress-bar-success progress-bar-striped active">
	                            <span class="sr-only">0% Complete</span>
	                        </div>
	                    </div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">关闭</button>
	                    <button type="button" class="btn btn-primary pull-left" onclick="importConfirm22()">上传(无课程id)</button>
	                    <button type="button" class="btn btn-primary" onclick="importConfirm()">上传</button>
	                </div> 
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	<!-- upload form end -->
	
	<!-- upload form start -->
	<div id="importPointModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="importPointForm" name="importForm" action="importAppointmentPointRecord" method="post" target="temp_target" enctype="multipart/form-data">
	        	<input type="hidden" name="examType" value="${exam_type}"  />
	            <div class="modal-content"> 
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">导入学员考点预约</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div> 
	                		<p>
	                		请选择考试批次，并点击下载按钮下载导入模版
		                    </p>    
							<div class="col-sm-9">
								<select name="batchCode" class="selectpicker show-tick form-control" 
									data-size="10" data-live-search="true">
									<c:forEach items="${batchMap}" var="map">
										<option value="${map.key}">${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<br><br>
						<div>
							<p>
								<a href="${ctx}/excel/download?name=考生预约考点导入模板.xlsx" target="_blank" class="btn btn-xs btn-success">
		                            <i class="fa fa-fw fa-download"></i> 下载导入模版
		                        </a>
		                   </p>
						</div> 
	                     
	                    <input name="file" id="file" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
	                    <div class="progress">　
	                        <div style="width: 0%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="0" role="progressbar" class="progress-bar progress-bar-success progress-bar-striped active">
	                            <span class="sr-only">0% Complete</span>
	                        </div>
	                    </div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">关闭</button>
	                    <button type="button" class="btn btn-primary" onclick="importPointConfirm()">上传</button>
	                </div> 
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	<!-- upload form end -->
</body>

<script src="${ctx}/static/js/exam/exam_appointment_list.js"></script>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
