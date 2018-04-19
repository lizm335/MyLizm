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
				<a href="${ctx }/admin/messageInfo/list"">通知公告</a>
			</li>
			<li class="active">通知详情</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm">
			<div class="bg-white">
				<ul class="nav nav-tabs bg-f2f2f2" data-role="top-nav">
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/view/${messageId}" style="border-left-color: transparent;">详情</a>
					</li>
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/queryPutListById?id=${messageId}">统计</a>
					</li>
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/commentList?id=${messageId}">评论</a>
					</li>
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/likeList?id=${messageId}">点赞</a>
					</li>
					<li class="active">
						<a class="flat gray no-margin" href="#tab_top_5" data-toggle="tab">反馈</a>
					</li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab_top_1">
						<div class="pad20">
							<div>
								<div class="box-border pad15 margin_b20">
									<div class="f18 margin_b10">请对本活动反馈意见</div>
									<dl class="spe-list no-margin">
										<c:forEach items="${list }" var="item">
											<dt class="text-no-bold f16">
												<i class="fa fa-fw fa-circle-thin"></i> ${item.name }
											</dt>
											<dd class="margin_t5">
												<div class="progress">
													<div class="progress-bar progress-bar-primary" style="width: ${item.percentage}%">
														<span>${item.num }人（${item.percentage }%）</span>
													</div>
												</div>
											</dd>
										</c:forEach>
									</dl>
								</div>

								<div class="box box-border">
									<div class="box-body">
										<div class="row pad-t15">
											<div class="col-sm-4">
												<div class="form-group">
													<label class="control-label col-sm-3 text-nowrap">姓名</label>
													<div class="col-sm-9">
														<input type="text" class="form-control" name="search_LIKE_userName" value="${param.search_LIKE_userName }"> <input type="hidden"
															class="form-control" name="id" id="messageId" value="${messageId }"> <input type="hidden" class="form-control"
															name="search_EQ_isTickling" value="${param.search_EQ_isTickling }">
													</div>
												</div>
											</div>
											<div class="col-sm-4">
												<div class="form-group">
													<label class="control-label col-sm-3 text-nowrap">账号/学号</label>
													<div class="col-sm-9">
														<input type="text" class="form-control" name="search_EQ_loginAccount" value="${param.search_EQ_loginAccount }">
													</div>
												</div>
											</div>
											<div class="col-sm-4">
												<div class="form-group">
													<label class="control-label col-sm-3 text-nowrap">层次</label>
													<div class="col-sm-9">
														<select class="form-control selectpicker show-tick" name="search_EQ_pycc">
															<option value="">全部层次</option>
															<c:forEach items="${pyccMap }" var="map">
																<option value="${map.key }" <c:if test="${param.search_EQ_pycc eq map.key }">selected</c:if>>${map.value }</option>
															</c:forEach>
														</select>
													</div>
												</div>
											</div>
											<div class="col-sm-4">
												<div class="form-group">
													<label class="control-label col-sm-3 text-nowrap">学期</label>
													<div class="col-sm-9">
														<select class="form-control selectpicker show-tick" name="search_EQ_gradeId" data-size="8" data-live-search="true">
															<option value="">全部学期</option>
															<c:forEach items="${gradeMap }" var="map">
																<option value="${map.key }" <c:if test="${param.search_EQ_gradeId eq map.key }">selected</c:if>>${map.value }</option>
															</c:forEach>
														</select>
													</div>
												</div>
											</div>
											<div class="col-sm-4">
												<div class="form-group">
													<label class="control-label col-sm-3 text-nowrap">专业</label>
													<div class="col-sm-9">
														<select class="form-control selectpicker show-tick" name="search_EQ_specialtyId" data-size="8" data-live-search="true">
															<option value="">全部专业</option>
															<c:forEach items="${specialtyMap }" var="map">
																<option value="${map.key }" <c:if test="${param.search_EQ_specialtyId eq map.key }">selected</c:if>>${map.value }</option>
															</c:forEach>
														</select>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="box-footer">
										<div class="pull-right">
											<button type="button" class="btn min-width-90px btn-default">重置</button>
										</div>
										<div class="pull-right margin_r15">
											<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
										</div>
									</div>
								</div>
								<div class="box box-border margin-bottom-none">
									<div class="box-header with-border">
										<h3 class="box-title pad-t5">信息列表</h3>
										<div class="pull-right"></div>
									</div>
									<div class="box-body">
										<div class="filter-tabs clearfix filter-tabs2">
											<ul class="list-unstyled">
												<li lang=":input[name='search_EQ_isTickling']" <c:if test="${empty param.search_EQ_isTickling}">class="actived"</c:if>>
													全部(${noTickting+yesTickting})</li>
												<li value="0" role=":input[name='search_EQ_isTickling']" <c:if test="${param.search_EQ_isTickling == '0' }">class="actived"</c:if>>未反馈(${noTickting })</li>
												<li value="1" role=":input[name='search_EQ_isTickling']" <c:if test="${param.search_EQ_isTickling == '1' }">class="actived"</c:if>>已反馈(${yesTickting})</li>
											</ul>
										</div>
										<div class="table-responsive">
											<table class="table table-bordered table-striped vertical-mid text-center table-font">
												<thead>
													<tr>
														<th>个人信息</th>
														<th>报读信息</th>
														<th>状态</th>
														<th>反馈时间</th>
														<th>操作</th>
													</tr>
												</thead>
												<tbody>
													<c:choose>
														<c:when test="${not empty pageInfo.content}">
															<c:forEach items="${pageInfo.content}" var="info">
																<tr>
																	<td>
																		<ul class="list-unstyled text-left">
																			<li>姓名：${info.REAL_NAME}</li>
																			<li>角色：${info.ROLE_NAME}</li>
																			<li>帐号：${info.LOGIN_ACCOUNT}</li>
																		</ul>
																	</td>
																	<td>
																		<ul class="list-unstyled text-left">
																			<li>层次：${info.PYCC}</li>
																			<li>学期：${info.GRADE_NAME}</li>
																			<li>专业：${info.ZYMC}</li>
																		</ul>
																	</td>
																	<td>
																		<c:if test="${info.IS_TICKLING=='1' }">
																			<div class="text-green">
																				已反馈
																			</div>
																		</c:if> <c:if test="${info.IS_TICKLING=='0' }">
																			<div class="text-red">未反馈</div>
																		</c:if></td>
																	<td>
																		${info.FEEDBACK_DT }
																	</td>
																	<td>
																		${ticktingNameMap[info.FEEDBACK_TYPE] } 
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
			</div>
		</form>
	</section>

	<!-- 底部 -->
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>

