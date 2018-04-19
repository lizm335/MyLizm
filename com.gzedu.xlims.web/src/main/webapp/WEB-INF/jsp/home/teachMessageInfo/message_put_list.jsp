<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>我发布的通知</title>
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
		<form id="listForm" class="form-horizontal" action="putList">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li>
						<a href="${ctx}/admin/teachMessageInfo/list" target="_self">我接收的通知</a>
					</li>
					<li class="active">
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
										<input class="form-control" type="text" name="search_LIKE_infoTheme" value="${param.search_LIKE_infoTheme}">
									</div>
								</div>
								<div class="col-md-4">
									<label class="control-label col-sm-3">通知类型</label>
									<div class="col-sm-9">
										<select id="basic" name="search_EQ_infoType" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${infoTypeMap}" var="map">
												<option value="${map.key}" <c:if test="${param.search_EQ_infoType==map.key}">selected</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="box-footer">
							<div class="btn-wrap">
								<button type="reset" class="btn btn-default">重置</button>
							</div>
							<div class="btn-wrap">
								<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
							</div>
						</div>
					</div>

					<div class="box box-border">
						<div class="box-header with-border">
							<div class="fr">
								<div class="btn-wrap fl">
									 <shiro:hasPermission name="/admin/teachMessageInfo/list$create">
										<a href="create" class="btn btn-default btn-sm"> <i class="fa fa-fw fa-plus"></i> 新增</a>
									</shiro:hasPermission>
								</div>
							</div>
						</div>

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
													<th width="30%">接收身份</th>
													<th>发送人</th>
													<th>发送时间</th>
													<th>是否置顶</th>
													<th>已读/未读人数</th>
													<th>操作</th>
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
																	<td>${info.infoTheme }</td>
																	<td>${infoTypeMap[info.infoType]}</td>
																	<td>${info.userRole}</td>
																	<td>${info.putUserName}</td>
																	<td>${info.createdDt}</td>
																	<td>
																			${info.isStick==true?'':'否' } 
																			<c:if test="${info.isStick }">
																					是/有效期:${info.stickTime}
																			</c:if>
																	</td>
																	<td>${info.readTotal}/${info.putTotal}</td>
																	<td>
																		<div class="data-operion">
																			<a href="view/${info.messageId}" class="operion-item operion-view" data-toggle="tooltip" title="" data-original-title="查看详情"><i
																				class="fa fa-fw fa-view-more"></i>
																			</a>
																			<shiro:hasPermission name="/admin/teachMessageInfo/list$update">
																				<a href="update/${info.messageId}"class="operion-item operion-edit" title="编辑">
																					<i class="fa fa-fw fa-edit"></i>
																				</a> 
																			</shiro:hasPermission>
																			<%-- 	<a href="toDetail?id=${info.messageId}"
																		   class="operion-item operion-view" title="查看">
																			<i class="fa fa-fw fa-eye"></i></a> --%>
																			<%-- 
																		<a href="view/${info.messageId}" 
																			class="operion-item operion-view" title="查看">
																			<i class="fa fa-fw fa-eye"></i></a>
																		 --%>
																		 	<shiro:hasPermission name="/admin/teachMessageInfo/list$delete">
																		 		<a href="javascript:void(0);"	class="operion-item operion-del del-one" val="${info.messageId}"
																					title="删除" data-tempTitle="删除"><i class="fa fa-fw fa-trash-o"></i></a>
																			</shiro:hasPermission>
																		</div>
																	</td>
																</tr>
															</c:if>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<tr>
															<td align="center" colspan="9">暂无数据</td>
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
