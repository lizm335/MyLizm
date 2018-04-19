<%@ page contentType="text/html; charset=UTF-8"%>
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
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="${ctx }/admin/messageInfo/list"">短信通知</a>
			</li>
			<li class="active">短信通知统计</li>
		</ol>
	</section>

		<section class="content">
				<form id="listForm" class="form-horizontal">
			<div class="bg-white">
				<ul class="nav nav-tabs bg-f2f2f2" data-role="top-nav">
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/mobileMessage/view/${mobileMessageId}" style="border-left-color: transparent;">详情</a>
					</li>
					<li class="active">
						<a class="flat gray no-margin" href="#tab_top_2" data-toggle="tab">统计</a>
					</li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab_top_1">
						<div class="pad20">
							<div class="box box-border">
								<div class="box-body">
									<div class="row pad-t15">
									
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">姓名</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_LIKE_gjtUserAccount.realName" value="${param['search_LIKE_gjtUserAccount.realName'] }">
													<input type="hidden"  name="mobileMessageId" id="mobileMessageId" value="${mobileMessageId }">
													<input type="hidden"  name="search_EQ_sendStauts" value="${param.search_EQ_sendStauts }">
												</div>
											</div>
										</div>
										
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">账号/学号</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_EQ_gjtUserAccount.loginAccount" value="${param['search_EQ_gjtUserAccount.loginAccount'] }">
												</div>
											</div>
										</div>
										
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">手机号</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_EQ_gjtUserAccount.sjh" value="${param['search_EQ_gjtUserAccount.sjh'] }">
												</div>
											</div>
										</div>
										
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">层次</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick" name="search_EQ_gjtUserAccount.gjtStudentInfo.pycc">
														<option value="">全部层次</option>
														<c:forEach items="${pyccMap }" var="map">
															<option value="${map.key }" <c:if test="${param['search_EQ_gjtUserAccount.gjtStudentInfo.pycc'] eq map.key }">selected</c:if>>${map.value }</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">学期</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick" name="search_EQ_gjtUserAccount.gjtStudentInfo.nj" data-size="8" data-live-search="true">
														<option value="">全部学期</option>
														<c:forEach items="${gradeMap }" var="map">
															<option value="${map.key }" <c:if test="${param['search_EQ_gjtUserAccount.gjtStudentInfo.nj'] eq map.key }">selected</c:if>>${map.value }</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">专业</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick" name="search_EQ_gjtUserAccount.gjtStudentInfo.major" data-size="8" data-live-search="true">
														<option value="">全部专业</option>
														<c:forEach items="${specialtyMap }" var="map">
															<option value="${map.key }" <c:if test="${param['search_EQ_gjtUserAccount.gjtStudentInfo.major'] eq map.key }">selected</c:if>>${map.value }</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- /.box-body -->
								<div class="box-footer">
									<div class="pull-right">
										<button type="button" class="btn min-width-90px btn-default">重置</button>
									</div>
									<div class="pull-right margin_r15">
										<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
									</div>
								</div>
								<!-- /.box-footer-->
							</div>

							<div class="box box-border margin-bottom-none">
								<div class="box-body">
									<div class="filter-tabs clearfix filter-tabs2">
										<ul class="list-unstyled">
											<li lang=":input[name='search_EQ_sendStauts']" <c:if test="${empty param.search_EQ_sendStauts}">class="actived"</c:if>> 全部(${waitCount+failedCount+successfulCount })</li>
											<li value="0" role=":input[name='search_EQ_sendStauts']"  <c:if test="${param.search_EQ_sendStauts == '0' }">class="actived"</c:if>>发送成功(${successfulCount })</li>
											<li value="1" role=":input[name='search_EQ_sendStauts']" <c:if test="${param.search_EQ_sendStauts == '1' }">class="actived"</c:if>>发送失败(${failedCount})</li>
											<li value="2" role=":input[name='search_EQ_sendStauts']"  <c:if test="${param.search_EQ_sendStauts == '2' }">class="actived"</c:if>>待发送(${waitCount})</li>
										</ul>
									</div>
									<div class="table-responsive">
										<table class="table table-bordered table-striped vertical-mid text-center table-font">
											<thead>
												<tr>
													<th>头像</th>
													<th>个人信息</th>
													<th>报读信息</th>
													<th>发送时间</th>
													<th>状态</th>
												</tr>
											</thead>
											<tbody>
												<c:choose>
													<c:when test="${not empty pageInfo.content}">
														<c:forEach items="${pageInfo.content}" var="info">
															<tr>
																<td>
																	<c:if test="${not empty info.gjtUserAccount.gjtStudentInfo.avatar}">
																	<img src="${info.gjtUserAccount.gjtStudentInfo.avatar}" class="img-circle" width="50" height="50">
																	</c:if>
																	<c:if test="${empty info.gjtUserAccount.gjtStudentInfo.avatar}">
																		<img src="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" width="50" height="50">
																	</c:if>
																</td>
																<td>
																	<ul class="list-unstyled text-left">
																		<li>姓名：${info.gjtUserAccount.realName}</li>
																		<li>类型：${userTypeMap[info.gjtUserAccount.gjtStudentInfo.userType]}</li>
																		<li>学号：${info.gjtUserAccount.loginAccount}</li>
																		<li>学籍：${xjztMap[info.gjtUserAccount.gjtStudentInfo.xjzt]}</li>
																		<li>手机：${info.gjtUserAccount.sjh}</li>
																	</ul>
																</td>
																<td>
																	<ul class="list-unstyled text-left">
																		<li>层次：${pyccMap[info.gjtUserAccount.gjtStudentInfo.pycc]}</li>
																		<li>学期：${info.gjtUserAccount.gjtStudentInfo.gjtGrade.gradeName}</li>
																		<li>专业：${info.gjtUserAccount.gjtStudentInfo.gjtSpecialty.zymc}</li>
																	</ul>
																</td>
																<td>
																	<c:if test="${not empty info.sendTime }">
																		<fmt:formatDate value="${info.sendTime }" type="both"/>
																	</c:if> 
																</td>
																<td>
																	<c:if test="${info.sendStauts eq '0'}">
																		<span class="text-orange">待发送</span>
																	</c:if>
																	
																	<c:if test="${info.sendStauts eq '1'}">
																		<span class="text-green">发送成功</span>
																	</c:if>
																	
																	<c:if test="${info.sendStauts eq '2'}">
																		<span class="text-red">发送失败</span>
																	</c:if>
																</td>
															</tr>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<tr>
															<td align="center" colspan="5">暂无数据</td>
														</tr>
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
										<div class="page-container clearfix margin_r10 left10">
											<tags:pagination page="${pageInfo}" paginationSize="5" />
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		</section>
	<!-- 底部 -->
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>


</body>
</html>

