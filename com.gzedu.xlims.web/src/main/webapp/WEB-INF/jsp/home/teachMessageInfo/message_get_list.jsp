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
</script>

</head>
<body class="inner-page-body">
	<!-- Main content -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">主页</a>
			</li>
			<li class="active">通知公告管理</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal" action="list">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li class="active">
						<a href="${ctx}/admin/teachMessageInfo/list" target="_self">我接收的通知</a>
					</li>
					<li>
						<a href="${ctx}/admin/teachMessageInfo/putList" target="_self">我发布的通知</a>
					</li>
				</ul>
				<div class="tab-content">
					<div class="box box-border">
						<div class="box-body">
							<div class="row pad-t15">
								<div class="col-md-4">
									<label class="control-label col-sm-3">标题</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_gjtMessageInfo.infoTheme" value="${param['search_LIKE_gjtMessageInfo.infoTheme']}">
									</div>
								</div>
								<div class="col-md-4">
									<label class="control-label col-sm-3">通知类型</label>
									<div class="col-sm-9">
										<select id="basic" name="search_EQ_gjtMessageInfo.infoType" class="selectpicker show-tick form-control" data-size="8"
											data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${infoTypeMap}" var="map">
												<option value="${map.key}" <c:if test="${param['search_EQ_gjtMessageInfo.infoType']==map.key}">selected</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="box-footer">
							<div class="btn-wrap">
								<button type="reset" class="btn btn-default btn-reset">重置</button>
							</div>
							<div class="btn-wrap">
								<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
							</div>
						</div>
					</div>
					<div class="box box-border">
						<div class="box-body">
							<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
								<div class="row">
									<div class="col-sm-6"></div>
									<div class="col-sm-6"></div>
								</div>
								<div class="row">
									<div class="col-sm-12">
										<table class="table table-bordered table-striped table-container">
											<thead>
												<tr>
													<th><input type="checkbox" class="select-all"></th>
													<th>标题</th>
													<th>通知类型</th>
													<th>发送人</th>
													<th>发送时间</th>
													<th>是否已读</th>
													<th>详情</th>
												</tr>
											</thead>
											<tbody>
												<c:choose>
													<c:when test="${not empty pageInfo.content}">
														<c:forEach items="${pageInfo.content}" var="info">
															<c:if test="${not empty info}">
																<tr>
																	<td><input class="checkbox" type="checkbox" name="ids" data-id="${info.messageId}" data-name="check-id"
																		value="${info.messageId}"></td>
																	<td>${info.isStick==true?'【置顶】':'' }${info.infoTheme }</td>
																	<td>${infoTypeMap[info.infoType]}</td>
																	<td>${info.putUserName }</td>
																	<td>${info.createdDt}</td>
																	<td>
																		<c:if test="${info.isEnabled=='0' }">
																			<font class="text-red">未读</font>
																		</c:if>
																		<c:if test="${info.isEnabled=='1' }">
																			<font class="text-green">已读</font>
																		</c:if>
																	</td>
																	<td>
																		<div class="data-operion">
																			<div class="data-operion">
																				<a href="detail?id=${info.messageId}" class="operion-item operion-view" data-toggle="tooltip" title="" data-original-title="查看详情"><i
																					class="fa fa-fw fa-view-more"></i></a>
																			</div>
																		</div>
																	</td>
																</tr>
															</c:if>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<tr>
															<td align="center" colspan="7">暂无数据</td>
														</tr>
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<tags:pagination page="${pageInfo}" paginationSize="5" />
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
