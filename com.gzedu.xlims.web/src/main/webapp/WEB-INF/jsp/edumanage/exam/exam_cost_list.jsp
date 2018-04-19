<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>补考缴费</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
		var examPlanId = '${param["search_EQ_examPlanNew.examPlanId"]}';
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

			$('#cost_export').click(function(){
				var examBatchCode = $('#m_examBatchCode').val();
				if(!examBatchCode) {
					alert('请先选择考试计划');
					return;
				}
				$('#uploadForm').submit();
			});
		})
	</script>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试管理</a></li>
		<li class="active">补考缴费</li>
	</ol>
</section>
		
		<section class="content">
		<form id="listForm" class="form-horizontal" action="list.html">
				<!-- 搜索栏 start --> 
				<div class="box">
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
									<input class="form-control" type="text" name="search_EQ_student.xh"   value="${param['search_EQ_student.xh']}">
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
									<select id="examBatchCode" name="search_EQ_examBatchCode" class="selectpicker show-tick form-control"
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
										data-size="10" data-live-search="true">
										<option value=''>请选择</option>
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
						<h3 class="box-title pad-t5">缴费列表</h3>
						<div class="pull-right">
							<shiro:hasPermission name="/exam/new/cost/list$export">
								<a href="#" class="btn btn-default btn-sm margin_r10"  data-toggle="modal" data-target="#exportModal"><i class="fa fa-fw fa-download"></i> 导出补考缴费记录</a>
							</shiro:hasPermission>
						</div>
					</div> 
					<!-- 按钮栏 end -->	
	
					<!-- 列表内容 start -->
					<div class="box-body">
							<div class="table-responsive">
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
															</div>
														</td>
														<td>
															${entity.examBatchNew.name}<br>
															<span class="gray9">${entity.examBatchNew.examBatchCode}</span>
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
															<fmt:formatDate value="${entity.payDate}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
														</td>
													</tr>
												</c:if>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<tags:pagination page="${pageInfo}" paginationSize="13" />
						</div>
					<!-- 列表内容 end -->
				</div>
			</form>
		</div>	
	</section>
	
	<div id="exportModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="uploadForm" name="uploadForm" action="${ctx}/exam/new/cost/export" method="post">
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
	
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>

</html>
