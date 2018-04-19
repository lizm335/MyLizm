<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>毕业生登记表</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="javascript:;">毕业管理</a></li>
			<li class="active">毕业生登记表</li>
		</ol>
	</section>
		
	<section class="content">
		<form id="listForm" class="form-horizontal" action="list.html" method="post">
			<input type="hidden" name="search_EQ_expressSignState" value="${param['search_EQ_expressSignState']}">
				<!-- 搜索栏 start --> 
				<div class="box">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">毕业计划</label>
									<div class="col-sm-9">
										<select name="search_EQ_graduationPlanId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${graduationPlanMap}" var="plan">
												<option value="${plan.key}"  <c:if test="${plan.key==param['search_EQ_graduationPlanId'] || plan.key==graduationPlanId }">selected='selected'</c:if>>${plan.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
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
										<input class="form-control" type="text" name="search_EQ_gjtStudentInfo.xh" value="${param['search_EQ_gjtStudentInfo.xh']}">
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">层次</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${pyccMap}" var="trainingLevel">
												<option value="${trainingLevel.key}"  <c:if test="${trainingLevel.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if>>${trainingLevel.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学期</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${gradeMap}" var="grade">
												<option value="${grade.key}"  <c:if test="${grade.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']}">selected='selected'</c:if>>${grade.value}</option>
											</c:forEach>
										</select>
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
						</div>

						<div id="more-search" class="row collapse <c:if test="${not empty param['search_EQ_gjtStudentInfo.sfzh'] || not empty param['search_EQ_gjtStudentInfo.gjtStudyCenter.id'] }">in</c:if>">
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">身份证号</label>
									<div class="col-sm-9">
										<input type="text" class="form-control" name="search_EQ_gjtStudentInfo.sfzh" value="${param['search_EQ_gjtStudentInfo.sfzh'] }">
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学习中心</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.gjtStudyCenter.id" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${studyCenterMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtStudyCenter.id']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<div class="box-footer text-right">
			          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
			          <button type="button" class="btn min-width-90px btn-default btn-reset" id="btn-reset">重置</button>
						<div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">高级搜索<i class="fa fa-fw fa-angle-up <c:if test="${not empty param.search_EQ_sfzh || not empty param.search_EQ_userType }">fa-angle-down</c:if>"></i> </div>
			        </div>
				</div>
				<!-- 搜索栏 end --> 
				 
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>


				<div class="box margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">毕业生登记表</h3>
						<div class="pull-right no-margin">
							<shiro:hasPermission name="/graduation/register/list$init">
								<a class="btn btn-default btn-sm margin_l10" data-role="import"><i class="fa fa-fw fa-spinner"></i> 初始化本期毕业学员</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/graduation/register/list$addressManage">
								<a class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-cog"></i> 收件地址管理</a>
							</shiro:hasPermission>
						</div>
					</div>
	
					<!-- 列表内容 start -->
					<div class="box-body">
							<div class="filter-tabs clearfix">
								<ul class="list-unstyled">
									<li <c:if test="${empty param.search_EQ_expressSignState}">class="actived"</c:if> val="" class="choiceState">全部(${countStateMap['']})</li>
									<li <c:if test="${param.search_EQ_expressSignState == '0'}">class="actived"</c:if> val="0" class="choiceState">未寄送(${countStateMap['0']})</li>
									<li <c:if test="${param.search_EQ_expressSignState == 1}">class="actived"</c:if> val="1" class="choiceState">寄送中(${countStateMap['1']})</li>
									<li <c:if test="${param.search_EQ_expressSignState == 2}">class="actived"</c:if> val="2" class="choiceState">已签收(${countStateMap['2']})</li>
								</ul>
							</div>
							<div class="table-responsive" style="overflow: hidden">
								<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
										<thead>
											<tr> 
												<th>个人信息</th>
												<th>报读信息</th>
												<th>签收状态</th>
												<th>签收时间</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pageInfo.content}" var="info">
												<c:if test="${not empty info}">
													<tr>
														<td>
															<ul class="list-unstyled text-left">
																<li>姓名：${info.gjtStudentInfo.xm}</li>
																<li>学号：${info.gjtStudentInfo.xh}</li>
																<li>手机：

																	<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																		${info.gjtStudentInfo.sjh}
																	</shiro:hasPermission>
																	<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
																		<c:if test="${not empty info.gjtStudentInfo.sjh }">
																			${fn:substring(info.gjtStudentInfo.sjh,0, 3)}******${fn:substring(info.gjtStudentInfo.sjh,8, (info.gjtStudentInfo.sjh).length())}
																		</c:if>
																	</shiro:lacksPermission>

																</li>
																<li>身份证：
																	<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																		${info.gjtStudentInfo.sfzh}
																	</shiro:hasPermission>
																	<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
																		<c:if test="${not empty info.gjtStudentInfo.sfzh }">
																			${fn:substring(info.gjtStudentInfo.sfzh,0, 4)}******${fn:substring(info.gjtStudentInfo.sfzh,14, (info.gjtStudentInfo.sfzh).length())}
																		</c:if>
																	</shiro:lacksPermission>
																</li>
															</ul>
														</td>
														<td>
															<ul class="list-unstyled text-left">
																<li>层次：<dic:getLabel typeCode="TrainingLevel" code="${info.gjtStudentInfo.pycc }" /></li>
																<li>学期：${info.gjtStudentInfo.gjtGrade.gradeName}</li>
																<li>专业：${info.gjtStudentInfo.gjtSpecialty.zymc}</li>
																<li>学习中心：${info.gjtStudentInfo.gjtStudyCenter.scName}</li>
															</ul>
														</td>
														<td>
															<c:choose>
																<c:when test="${info.expressSignState == 0}">
																	<span class="text-yellow">未寄送</span>
																</c:when>
																<c:when test="${info.expressSignState == 1}">
																	<span class="text-light-blue">寄送中</span>
																</c:when>
																<c:when test="${info.expressSignState == 2}">
																	<span class="text-green">已签收</span>
																</c:when>
															</c:choose>
														</td>
														<td>
															<c:choose>
																<c:when test="${info.expressSignState == 2}">
																	<fmt:formatDate value="${info.expressSignDt}" type="both"/>
																</c:when>
																<c:otherwise>
																	--
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<div class="data-operion">
																<c:if test="${info.expressSignState != 2}">
																	<shiro:hasPermission name="/graduation/register/list$express">
																		<a href="javascript:void(0);" val="${info.registerId}" action="express" class="btn btn-primary btn-sm">已签收</a>
																		<%--<a href="javascript:void(0);" val="${info.registerId}" action="express" class="operion-item" data-toggle="已签收" title="已签收"><i class="fa fa-fw fa-check-circle"></i></a>--%>
																	</shiro:hasPermission>
																</c:if>
															</div>
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

	<!-- 批改报告 -->
	<div id="formModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog" style="height: 400px; width: 700px; z-index: 9999">
	        <form id="addressManageForm" name="uploadForm" class="form-horizontal" action="" method="post">
				<input name="id" type="hidden" />
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">收件地址管理</h4>
	                </div>
	                <div class="modal-body">
						<div class="box-body">
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>收货人</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<input type="text" class="form-control" name="receiver" value="" placeholder="收货人" >
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>手机号码</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<input type="text" class="form-control" name="mobile" value="" placeholder="手机号码">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>所在地区</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<div class="row">
										<div class="col-sm-4">
											<select id="province" name="provinceCode" class="select2 form-control" data-size="8" data-live-search="true">
												<option value="" selected="selected"></option>
											</select>
										</div>
										<div class="col-sm-4">
											<select id="city" name="cityCode" class="select2 form-control" data-size="8" data-live-search="true">
												<option value="" selected="selected"></option>
											</select>
										</div>
										<div class="col-sm-4">
											<select id="district" name="areaCode" class="select2 form-control" data-size="8"  data-live-search="true">
												<option value="" selected="selected"></option>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>详细地址</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<input type="text" class="form-control" name="address" value="" placeholder="详细地址" >
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>邮政编码</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<input type="text" class="form-control" name="postcode" value="" maxlength="6" placeholder="邮政编码" >
								</div>
							</div>
						</div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                    <button type="button" class="btn btn-primary" id="save">确定</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<%
		long time = System.currentTimeMillis();
	%>
	<script src="${ctx}/static/js/graduation/register/graduation_register_list.js?time=<%=time%>"></script>
</body>
</html>
