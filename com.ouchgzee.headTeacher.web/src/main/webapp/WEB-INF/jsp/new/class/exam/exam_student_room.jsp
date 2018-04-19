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
			<li class="active">排考安排</li>
		</ol>
	</section>
		<section class="content">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li><a href="${ctx}/home/class/exam/batch/list" target="_self">考试计划</a></li>
					<li><a href="${ctx}/home/class/exam/plan/list" target="_self">开考科目</a></li>
					<li><a href="${ctx}/home/class/exam/appointment/list" target="_self">考试预约</a></li>
					<li class="active"><a href="${ctx}/home/class/exam/studentroom/list" target="_self">排考安排</a></li>
					<li><a href="${ctx}/home/class/exam/cost/list" target="_self">补考缴费</a></li>
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
											<label class="control-label col-sm-3">考试计划</label>
											<div class="col-sm-9">
												<select name="EXAM_BATCH_CODE" id="exam_batch_code" class="selectpicker show-tick form-control" 
													data-size="5" data-live-search="true">
													<option value="all" >请选择</option>
													<c:forEach items="${batchMap}" var="map">
														<option value="${map.key}"<c:if test="${map.key==EXAM_BATCH_CODE}">selected='selected'</c:if> >${map.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">姓名</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="XM" value="${param.XM}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">学号</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="XH" value="${param.XH}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">试卷号</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="EXAM_NO" value="${param.EXAM_NO}">
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">考试方式</label>
											<div class="col-sm-9">
												<select name="TYPE" id="type" class="selectpicker show-tick form-control"
													data-size="5" data-live-search="true">
													<option value="" selected="selected">请选择</option>
													<option value="8"<c:if test="${param.TYPE==8}">selected='selected'</c:if> >笔试</option>
													<option value="11"<c:if test="${param.TYPE==11}">selected='selected'</c:if> >机考</option>
												</select>
											</div>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-sm-3">排考状态</label>
											<div class="col-sm-9">
												<select name="STATUS" id="status" class="selectpicker show-tick form-control"
													data-size="5" data-live-search="true">
													<option value="" selected="selected">请选择</option>
													<option value="1"<c:if test="${param.STATUS==1}">selected='selected'</c:if> >已排考</option>
													<option value="0"<c:if test="${param.STATUS==0}">selected='selected'</c:if> >未排考</option>
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
			
						<div class="box box-border margin-bottom-none">
							<!-- 按钮栏 start -->
							<div class="box-header with-border">
								<h3 class="box-title pad-t5">排考安排列表</h3>
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
																<td>
																	<div class="text-left">
																		姓名：${entity.XM} <br>
																		学号：${entity.XH} <br>
																		手机号：${entity.SJH}
																	</div>
																</td>
																<td>
																	<div class="text-left">
																		层次：${entity.PYCC_NAME} <br>
																		年级：${entity.YEAR_NAME} <br>
																		学期：${entity.GRADE_NAME}<br>
																		专业：${entity.ZYMC}<br>
																	</div>
																</td>
																<td>
																	${entity.EXAM_BATCH_NAME}<br>
																	<span class="gray9">${entity.EXAM_BATCH_CODE}</span>
																</td>
																<td>
																	${entity.EXAM_PLAN_NAME}
																</td>
																<td>
																	${entity.EXAM_TYPE_NAME}
																</td>
																<td>
																	${entity.EXAM_NO}
																</td>
																<td>
																	<c:choose>
																		<c:when test="${not empty entity.EXAM_POINT_NAME}">
																			${entity.EXAM_POINT_NAME}
																		</c:when>
																		<c:otherwise>
																			${entity.APP_EXAM_POINT_NAME}
																		</c:otherwise>
																	</c:choose>
																</td>
																<td>
																	${entity.EXAM_ROOM_NAME}
																</td>
																<td>
																	${entity.SEAT_NO}
																</td>
																<td>
																	<c:choose>
																		<c:when test="${entity.STATUS eq '1'}">
																			<span class="text-green">已排考</span>
																		</c:when>
																		<c:otherwise>
																			<span class="gray9">未排考</span>
																		</c:otherwise>
																	</c:choose>
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
	<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
</html>