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
		<li class="active">考试预约</li>
	</ol>
</section>
		
		<!-- Main content -->

	<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li><a href="${ctx}/home/class/exam/batch/list" target="_self">考试计划</a></li>
				<li><a href="${ctx}/home/class/exam/plan/list" target="_self">开考科目</a></li>
				<li class="active"><a href="${ctx}/home/class/exam/appointment/list" target="_self">考试预约</a></li>
				<li><a href="${ctx}/home/class/exam/studentroom/list" target="_self">排考安排</a></li>
				<li><a href="${ctx}/home/class/exam/cost/list" target="_self">补考缴费</a></li>
			</ul>
			<div class="tab-content">
			<div class="tab-pane active" id="tab_top_1">
			<form id="listForm" class="form-horizontal">
				<input type="hidden" name="search_EQ_appointmentStatus" value="">
				<input type="hidden" name="search_EQ_pointStatus" value="">
				<!-- 搜索栏 start -->
				<div class="box box-border">
					<div class="box-body">
						<div class="row pad-t15">
							 <div class="col-sm-4">
							 	<div class="form-group">
									<label class="control-label col-sm-3">姓名</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_xm" value="${param.search_LIKE_xm}">
									</div>
								</div>
							</div>
							 <div class="col-sm-4">
							 	<div class="form-group">
									<label class="control-label col-sm-3">学号</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_xh" value="${param.search_LIKE_xh}">
									</div>
								</div>
							</div>
							
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3">考试计划</label>
									<div class="col-sm-9">
										<select name="search_EQ_examBatchCode" class="selectpicker show-tick form-control" 
											data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
											<c:forEach items="${batchMap}" var="map">
												<option value="${map.key}"<c:if test="${map.key == examBatchCode}">selected='selected'</c:if> >${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3">学期</label>
									<div class="col-sm-9">
										<select name="search_EQ_gradeId" class="selectpicker show-tick form-control" 
											data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${gradeMap}" var="map">
												<option value="${map.key}"<c:if test="${map.key == param.search_EQ_gradeId}">selected='selected'</c:if> >${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3">专业</label>
									<div class="col-sm-9">
										<select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" 
											data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${specialtyMap}" var="map">
												<option value="${map.key}"<c:if test="${map.key == param.search_EQ_specialtyId}">selected='selected'</c:if> >${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3">层次</label>
									<div class="col-sm-9">
										<select name="search_EQ_pycc" class="selectpicker show-tick form-control" 
											data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}"<c:if test="${map.key == param.search_EQ_pycc}">selected='selected'</c:if> >${map.value}</option>
											</c:forEach>
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
			</form>	
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box box-border margin-bottom-none">

					<!-- 按钮栏 start -->
					<div class="box-header with-border">
						<div class="fr">
								<div class="btn-wrap fl">
									<a href="#" class="btn btn-default btn-sm" data-toggle="modal" data-target="#exportModal" >
										<i class="fa fa-fw fa-download"></i> 导出考试预约数据</a>
								</div>
						</div>
					</div>
					<!-- 按钮栏 end -->

					<!-- 列表内容 start -->
					<div class="box-body">
						<div class="filter-tabs filter-tabs2 clearfix">
							<ul class="list-unstyled">
								<li lang=":input[name='search_EQ_appointmentStatus'],:input[name='search_EQ_pointStatus']" <c:if test="${empty param['search_EQ_appointmentStatus'] && empty param['search_EQ_pointStatus']}">class="actived"</c:if>>全部(${appointmentStatus0 + appointmentStatus1})</li>
								<li value="0" role=":input[name='search_EQ_appointmentStatus']" lang=":input[name='search_EQ_pointStatus']"  <c:if test="${param['search_EQ_appointmentStatus'] == '0' }">class="actived"</c:if>>未预约科目(${appointmentStatus0})</li>
								<li value="1" role=":input[name='search_EQ_appointmentStatus']" lang=":input[name='search_EQ_pointStatus']"  <c:if test="${param['search_EQ_appointmentStatus'] == '1' }">class="actived"</c:if>>已预约科目(${appointmentStatus1})</li>
								<c:if test="${isCurrent}">
								<li value="0" role=":input[name='search_EQ_pointStatus']"  lang=":input[name='search_EQ_appointmentStatus']" <c:if test="${param['search_EQ_pointStatus'] == '0' }">class="actived"</c:if>>未预约考点(${pointStatus0})</li>
								<li value="1" role=":input[name='search_EQ_pointStatus']"  lang=":input[name='search_EQ_appointmentStatus']" <c:if test="${param['search_EQ_pointStatus'] == '1' }">class="actived"</c:if>>已预约考点(${pointStatus1})</li>
								</c:if>
							</ul>
						</div>
						<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
							<div class="row">
								<div class="col-sm-12">
									<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
										<thead>
											<tr>
												<th>个人信息</th>
												<th>报读信息</th>
												<th>考试计划</th>
												<th>考试预约时间</th>
												<c:if test="${isCurrent}">
												<th>可预约科目</th>
												</c:if>
												<th>已预约科目</th>
												<c:if test="${isCurrent}">
												<th>考点预约状态</th>
												</c:if>
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
																		姓名:  ${entity.xm}<br>
																		学号:  ${entity.xh}<br>
																		手机号: ${entity.sjh}
																	</div>
																</td>
																<td>
																	<div class="text-left">
																		层次: ${entity.pycc} <br>
																		年级: ${entity.year} <br>
																		学期: ${entity.grade} <br>
																		专业: ${entity.specialty}
																	</div>
																</td>
																<td>
																	${examBatch.name} <br>
																	<span class="gray9">${examBatch.examBatchCode}</span>
																</td>
																<td>
																	<fmt:formatDate value="${examBatch.bookSt}" type="date" pattern="yyyy-MM-dd"/><br>
																	至<br>
																	<fmt:formatDate value="${examBatch.bookEnd}" type="date" pattern="yyyy-MM-dd"/>
																</td>
																<c:if test="${isCurrent}">
																<td>
																	${entity.count1}
																</td>
																</c:if>
																<td>
																	${entity.count2}
																</td>
																<c:if test="${isCurrent}">
																<td>
																	<c:choose>
																		<c:when test="${entity.pointStatus == 0}">
																			<span class="text-orange">未预约</span>
																		</c:when>
																		<c:otherwise>
																			<span class="text-green">已预约</span>
																		</c:otherwise>
																	</c:choose>
																</td>
																</c:if>
																<td>
																	<div class="data-operion">
												            			<a href="view?studentId=${entity.studentId}&&examBatchCode=${examBatch.examBatchCode}&&isCurrent=${isCurrent}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-view-more"></i></a>
																	</div>
																</td> 
															</tr>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr><td colspan="10">暂无数据</td></tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</div>
							<c:if test="${not empty pageInfo}">
								<tags:pagination page="${pageInfo}" paginationSize="5" />
							</c:if>
						</div>
					</div>
					<!-- 列表内容 end -->
				</div>
			</div>
			</div>
		</div>
	</section>


<div id="exportModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog">
		<form id="uploadForm" name="uploadForm" action="${ctx}/home/class/exam/appointment/export/plan" method="post" target="temp_target" enctype="multipart/form-data">
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
</div>


<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
    $(function() {
        // 导出预约
        $('#plan_export').click(function(){
            var examBatchCode = $('#m_examBatchCode').val();
            if(!examBatchCode) {
                alert('请先选择考试计划');
                return;
            }
            $('#uploadForm').submit();
		});
    });

</script>
</body>
</html>
