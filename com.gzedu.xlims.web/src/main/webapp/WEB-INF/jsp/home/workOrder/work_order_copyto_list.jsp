<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>工单管理列表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li class="active">我的工单</li>
		</ol>
	</section>
	<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li>
					<a href="${ctx}/home/workOrder/list" target="_self">我收到的工单</a>
				</li>
				<li>
					<a href="${ctx}/home/workOrder/putList" target="_self">我发布的工单</a>
				</li>
				<li class="active">
					<a href="${ctx}/home/workOrder/copyTolist" target="_self">抄送给我的工单</a>
				</li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<form id="listForm" class="form-horizontal">
						<div class="box box-border">
							<div class="box-body">
								<div class="form-horizontal">
									<div class="row pad-t15">
										<div class="col-sm-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">标题</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_LIKE_gjtWorkOrder.title" value="${param.search_LIKE_title}"> <input type="hidden"
														class="form-control" name="search_EQ_isState" value="${param['search_LIKE_gjtWorkOrder.title']}">
												</div>
											</div>
										</div>
										<div class="col-sm-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">类型</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick" name="search_EQ_gjtWorkOrder.workOrderType" data-size="6" data-live-search="true">
														<option value="">全部</option>
														<c:forEach items="${workTypeMap}" var="map">
															<option value="${map.key}" <c:if test="${param['search_EQ_gjtWorkOrder.workOrderType'] eq map.key}">selected</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
										<div class="col-xs-6 col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">指派日期</label>
												<div class="col-sm-9">
													<div class="input-group input-daterange" data-role="date-group"> 
														<input type="text" class="form-control" data-role="date-start" name="createDtBegin" value="${createDtBegin }">
														<span class="input-group-addon nobg">－</span>
														<input type="text" class="form-control" data-role="date-end" name="createDtEnd" value="${createDtEnd }">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<!-- /.box-body -->
							<div class="box-footer">
								<div class="pull-right">
									<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
								</div>
								<div class="pull-right margin_r15">
									<button type="submit" class="btn min-width-90px btn-primary" id='btn'>搜索</button>
								</div>
							</div>
							<!-- /.box-footer-->
						</div>

						<div class="box box-border margin-bottom-none">
							<div class="box-header with-border">
								<h3 class="box-title pad-t5">工单列表</h3>
								<div class="pull-right no-margin">
								</div>
							</div>
							<div class="box-body">
								<div class="filter-tabs filter-tabs2 clearfix">
									<ul class="list-unstyled">
										<li lang=":input[name='search_EQ_gjtWorkOrder.isState']" <c:if test="${empty param['search_EQ_gjtWorkOrder.isState']}">class="actived"</c:if>>全部(${finishNum +  waitNum + closeNum})</li>
										<li value="1" role=":input[name='search_EQ_gjtWorkOrder.isState']" <c:if test="${param['search_EQ_gjtWorkOrder.isState'] == '1' }">class="actived"</c:if>>完成(${finishNum})</li>
										<li value="0" role=":input[name='search_EQ_gjtWorkOrder.isState']" <c:if test="${param['search_EQ_gjtWorkOrder.isState'] == '0' }">class="actived"</c:if>>待完成(${waitNum})</li>
										<li value="2" role=":input[name='search_EQ_gjtWorkOrder.isState']" <c:if test="${param['search_EQ_gjtWorkOrder.isState'] == '2' }">class="actived"</c:if>>已关闭(${closeNum})</li>
									</ul>
								</div>
								<div class="table-responsive">
									<table class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
										<thead>
											<tr>
												<th>级别</th>
												<th>标题</th>
												<th width="10%">要求完成时间</th>
												<th width="10%">指派时间</th>
												<th width="15%">指派给</th>
												<th width="15%">状态</th>
												<th width="10%">操作</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="info">
														<tr>
															<td>
																<c:if test="${info.gjtWorkOrder.priority eq '1' }">
																	<small class="label label-warning text-no-bold">优先</small>
																</c:if>
																<c:if test="${info.gjtWorkOrder.priority eq '0' }">
																	<small class="label label-default text-no-bold">一般</small>
																</c:if>
																<c:if test="${info.gjtWorkOrder.priority eq '2' }">
																	<small class="label label-danger text-no-bold">紧急</small>
																</c:if>
															</td>
															<td>
																<div class="text-left">
																	${info.gjtWorkOrder.title }
																	<div class="gray9">${workTypeMap[info.gjtWorkOrder.workOrderType] }</div>
																</div>
															</td>
															<td>${info.gjtWorkOrder.demandDate }</td>
															<td><fmt:formatDate value="${info.gjtWorkOrder.createdDt }" type="both"/> </td>
															<td>${info.gjtWorkOrder.createUser.realName}
																<div class="gray9">（${info.gjtWorkOrder.createUser.priRoleInfo.roleName}）</div>
															</td>
															<td>
																<c:if test="${info.gjtWorkOrder.isState eq '0' }">
																	<span class="text-orange">待完成</span>
																</c:if>
																<c:if test="${info.gjtWorkOrder.isState eq '1' }">
																	<span class="text-green">已完成</span>
																</c:if>
																<c:if test="${info.gjtWorkOrder.isState eq '2' }">
																	<span class="gray9">已关闭</span>
																</c:if>
															</td>
															<td>
																<shiro:hasPermission name="/home/workOrder/list$view">
					                    							<a href="view?id=${info.gjtWorkOrder.id }&type=2" class="operion-item operion-view" data-toggle="tooltip" title="查看详情">
																		<i class="fa fa-fw fa-view-more"></i>
																	</a>
																</shiro:hasPermission>
															</td>
														</tr>
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
									<tags:pagination page="${pageInfo}" paginationSize="5" />
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>

</html>
