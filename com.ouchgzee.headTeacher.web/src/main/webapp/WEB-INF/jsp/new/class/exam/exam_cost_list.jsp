<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任平台</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<div class="pull-left">
	    	您所在位置：
	  	</div>
		<ol class="breadcrumb">
			<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="javascript:;">考试组织</a></li>
			<li class="active">补考缴费</li>
		</ol>
	</section>
		<section class="content">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li><a href="${ctx}/home/class/exam/batch/list" target="_self">考试计划</a></li>
					<li><a href="${ctx}/home/class/exam/plan/list" target="_self">开考科目</a></li>
					<li><a href="${ctx}/home/class/exam/appointment/list" target="_self">考试预约</a></li>
					<li><a href="${ctx}/home/class/exam/studentroom/list" target="_self">排考安排</a></li>
					<li class="active"><a href="${ctx}/home/class/exam/cost/list" target="_self">补考缴费</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab_top_1">
					<form id="listForm" class="form-horizontal">
					 	<input type="hidden" name="search_EQ_planStatus" value="">
						<!-- 搜索栏 start -->
						<div class="box box-border">
							<div class="box-body">
								<div class="row pad-t15">
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">姓名</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="search_LIKE_student.xm"   value="${param['search_LIKE_student.xm']}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">学号</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="search_LIKE_student.xh"   value="${param['search_LIKE_student.xh']}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
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
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">考试计划</label>
											<div class="col-sm-9">
												<select id="examBatchCode" name="search_EQ_examBatchCode" class="selectpicker show-tick form-control"
													data-size="5" data-live-search="true">
													<option value="" selected="selected">请选择</option>
													<c:forEach items="${batchMap}" var="map">
														<option value="${map.key}"<c:if test="${map.key == examBatchCode}">selected='selected'</c:if> >${map.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">考试科目</label>
											<div class="col-sm-9">
												<select id="examPlanId" name="search_EQ_examPlanNew.examPlanId" class="selectpicker show-tick form-control"
													data-size="5" data-live-search="true">
													
												</select>
											</div>
										</div>
									</div>
									
								</div>
							</div>
							<div class="box-footer">
								<div class="pull-right">
									<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
								</div>
								<div class="pull-right margin_r15">
									<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
								</div>
							</div>
						</div>
						<!-- 搜索栏 end -->
			
						<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
						<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
						
						<div class="box box-border margin-bottom-none">
							<!-- 按钮栏 start -->
							<div class="box-header with-border">
								<h3 class="box-title pad-t5">补考缴费列表</h3>
								<div class="fr">
									<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-export">
										<i class="fa fa-fw fa-download"></i>导出补考缴费记录
									</a>
								</div>
							</div>
							<!-- 按钮栏 end -->	
			
							<!-- 列表内容 start -->
							<div class="box-body">
								<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
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
														<th>是否补考</th>
														<th>补考费用</th> 
														<th>缴费状态</th>
														<th>缴费时间</th>
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
																		手机号: ${entity.student.sjh}
																	</div>
																</td>
																<td>
																	<div class="text-left">
																		层次: ${pyccMap[entity.student.pycc]} <br>
																		年级: ${entity.student.gjtGrade.gjtYear.name} <br>
																		学期: ${entity.student.gjtGrade.gradeName} <br>
																		专业: ${entity.student.gjtSpecialty.zymc} <br>
																	</div>
																</td>
																<td>
																	${entity.examBatchNew.name}<br>
																	<span class="gray9">${entity.examBatchNew.examBatchCode}</span>
																</td>
																<td>
																	${entity.examPlanNew.examPlanName}
																</td>
																<td>
																	${examTypeMap[entity.examPlanNew.type]}
																</td>
																<td>
																	<c:choose>
																		<c:when test="${entity.makeup == '1'}">
																			是
																		</c:when>
																		<c:otherwise>
																			否
																		</c:otherwise>
																	</c:choose>
																</td>														
																<td>
																	${entity.courseCost}
																</td>
																<td>
																	<c:choose>
																		<c:when test="${entity.payStatus == '0'}">
																			<span class="text-green">已缴费</span>
																		</c:when>
																		<c:otherwise>
																			<span class="text-red">未缴费</span>
																		</c:otherwise>
																	</c:choose>
																</td>
																<td>
																	<fmt:formatDate value="${entity.updatedDt}" type="date" pattern="yyyy-MM-dd HH:mm"/>
																</td>
															</tr>
														</c:if>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</div>
									<tags:pagination page="${pageInfo}" paginationSize="5" />
								</div>
							</div>
							<!-- 列表内容 end -->
						</div>
					</form>	
					</div>
				</div>
			</div>
	</section>
	
	<div id="exportModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="uploadForm" name="uploadForm" action="${ctx}/home/class/exam/cost/export" method="post">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">导出补考缴费记录</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div>
	                		<p>
	                		请选择需要导出补考缴费记录的考试计划
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
	                    <button type="button" class="btn btn-primary" id="cost_export">导出</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	
	<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
	
	<script type="text/javascript">
	$(function() {
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
		
		$(".btn-export").click(function(event) {
			var url = "${ctx}/home/class/exam/cost/export?" + $("#listForm").serialize();
			window.open(url);
		});
	})
	
	</script>
</body>
</html>
