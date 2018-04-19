<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>成绩查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	
	if($('#search_EQ_type').val() == ''){
		$('#search_EQ_type').val(2);
		$('#tabType').val(0);
		$('#listForm').submit();
	}
	
	//导出
	$("[data-role='export']").click(function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'export',
		  width:600,
		  height:400,
		  zIndex:11000,
		  content: 'url:'+$(this).attr('href')
		});
	});
	
	//排考
	$('[data-role="start"]').click(function(event) {
		var $overlay=$(".overlay");
		var $that=$(this);
		var $targetBox=$("[data-id='"+$that.data("target")+"']");

		$overlay.show();
		$that.addClass("disabled").prop('disabled', true);
		var examBatchCode = $('#examBatchCode').val();
		
		$.post("plan",{examBatchCode:examBatchCode},function(data,status){
			if(data.successful){
				$overlay.hide();
				$('#planModal').modal('hide');
				alert("自动排考成功!");
				$('#listForm').submit();
				/*  $targetBox.find('[data-role="txt"]').hide().end()
				.find('[data-role="result"]').show().end()
				.find('[data-role="result-btn"]').show();
				$that.hide().siblings('[data-role="end"]').removeClass('hide-block');  */
			}else{
				alert("注意注意：自动排考失败!"+data.message);
				//window.location.href = '/exam/new/student/room/list';
			}
		  },"json");
		
	});
})



function tabClick1(id){
	$('#search_EQ_type').val(id);
	$('#listForm').submit();
}

function tabClick2(id){
	$('#tabType').val(id);
	$('#listForm').submit();
}
</script>

</head>
<body style="background-color:#ecf0f5;">
		
		<section class="content">
		<form id="listForm" class="form-horizontal" action="list.html">
		<input type="hidden" id="search_EQ_type" name="search_EQ_type" value="${param.search_EQ_type}">
		<input type="hidden" id="tabType" name="tabType" value="${tabType}">
		
		
			<div class="nav-tabs-custom no-margin">
				
				<ul class="nav nav-tabs nav-tabs-lg">
					<li <c:if test="${param.search_EQ_type==2}">class="active"</c:if>><a href="#" data-toggle="tab" onclick="tabClick1(2)">笔试</a></li>
					<li <c:if test="${param.search_EQ_type==3}">class="active"</c:if>><a href="#" data-toggle="tab" onclick="tabClick1(3)">机考</a></li>
				</ul> 
			
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
							
						</div>
						 
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试批次</label>
								<div class="col-sm-9">
									<select name="search_EQ_examPlanNew.examBatchCode" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<c:forEach items="${batchMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==param['search_EQ_examPlanNew.examBatchCode']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div> 
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">考试科目</label>
								<div class="col-sm-9">
									<select name="search_EQ_examPlanNew.subjectCode" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${subjectMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==param['search_EQ_examPlanNew.subjectCode']}">selected='selected'</c:if> >${map.value}</option>
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
				
				<div class="box box-border margin-bottom-none">
					<!-- 按钮栏 start -->
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">排考列表</h3>
						<div class="pull-right">
							<a href="exportRoomSeat" role="button" class="btn btn-default btn-sm margin_r10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出考场签到表</a>

							<a href="导出排考明细表.html" role="button" class="btn btn-default btn-sm margin_r10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出排考明细表</a>
							
							<a href="#" class="btn btn-default btn-sm" data-toggle="modal" data-target="#planModal" >
										<i class="fa fa-fw fa-cubes"></i> 批量排考</a>
										
						</div>
					</div> 
					<!-- 按钮栏 end -->	
	
					<!-- 列表内容 start -->
					<div class="box-body">
							<div class="filter-tabs clearfix">
								<ul class="list-unstyled">
									<li <c:if test="${tabType==0}">class="actived"</c:if> onclick="tabClick2(0)">
									未排考<c:if test="${tabType==0}"><%-- (${pageInfo.getTotalElements()}) --%></c:if></li>
									<li <c:if test="${tabType==1}">class="actived"</c:if> onclick="tabClick2(1)">
									已排考<c:if test="${tabType==1}"><%-- (${pageInfo.getTotalElements()}) --%></c:if></li>
								</ul> 
							</div>
							<div class="table-responsive">
								<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
										<thead>
											<tr> 
												<th><input type="checkbox" class="select-all"></th>
												<th>个人信息</th>
												<th>报读信息</th>
												<th>考试批次</th>
												<th>考试科目</th>
												<th>考试时间</th>
												<th>考试形式</th>
												<th>试卷号</th>
												<th>考点</th> 
												<th>考场</th>
												<th>座位号</th>
												<th>排考状态</th>
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
															</div>
														</td>
														<td>
															${entity.examPlanNew.examBatchNew.name}<br>
															<span class="gray9">${entity.examPlanNew.examBatchNew.examBatchCode}</span>
														</td>
														<td>
															${entity.examPlanNew.examSubjectNew.name}<br>
															<span class="gray9">${entity.examPlanNew.examSubjectNew.subjectCode}</span>
														</td>
														<td>
															<fmt:formatDate value="${entity.examPlanNew.examSt}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/><br>
															<fmt:formatDate value="${entity.examPlanNew.examEnd}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
														</td>
														<td>
															<c:if test="${entity.examPlanNew.type==1}">网考 </c:if>
															<c:if test="${entity.examPlanNew.type==2}">笔试</c:if>
															<c:if test="${entity.examPlanNew.type==3}">机考</c:if>
														</td>
														<td>
															${entity.examPlanNew.examSubjectNew.examNo}
														</td>														
														<td>
															${entity.gjtExamStudentRoomNew.gjtExamRoomNew.examPonitNew.name}
														</td>
														<td>
															${entity.gjtExamStudentRoomNew.gjtExamRoomNew.name}
														</td>
														<td>
															${entity.gjtExamStudentRoomNew.seatNo}
														</td>
														<td>
															<c:if test="${entity.gjtExamStudentRoomNew!=null}">
																<span class="text-green">已排考</span>
															</c:if>
															<c:if test="${entity.gjtExamStudentRoomNew==null}">
																<span class="text-red">待排考
															</c:if>
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
	
	<div id="planModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	            <div class="modal-content" >
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
						<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="start" data-target="box-2">开始排考</button>
						<button type="button" class="btn btn-success min-width-90px margin_l15 hide-block" data-role="end" data-dismiss="modal">完成排考</button>
	                </div>
	                
	                <div class="overlay" style="display:none;">
						<div class="uploading-txt text-center">
							<i class="fa fa-refresh fa-spin vertical-mid"></i>
							<span class="inlineblock left10">数据交互中，请稍后...</span>
						</div>
			        </div>
	            </div><!-- /.modal-content -->
	    </div><!-- /.modal-dialog -->
	</div>
</body>

</html>
