<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>短信通知</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

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
			<li class="active">短信通知</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal" action="list">
			<div class="nav-tabs-custom no-margin">
				<div class="tab-content">
					<div class="box box-border">
						<div class="box-body">
							<div class="row pad-t15">
								<div class="col-md-4">
									<label class="control-label col-sm-3">内容</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_content" value="${param.search_LIKE_content}">
										<input type="hidden" name="search_EQ_auditStatus" value="${param.search_EQ_auditStatus}">
									</div>
								</div>
								<div class="col-md-4">
									<label class="control-label col-sm-3">类型</label>
									<div class="col-sm-9">
										<select id="basic" name="search_EQ_type" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${typeMap}" var="map">
												<option value="${map.key}" <c:if test="${param.search_EQ_type==map.key}">selected</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="col-xs-6 col-sm-4">
						            <div class="form-group">
						              <label class="control-label col-sm-3 text-nowrap">发送时间</label>
						              <div class="col-sm-9">
						                <div class="input-group input-daterange" data-role="date-group">
						                  <input type="text" name="search_GTE_sendTime" class="form-control" data-role="date-start">
						                  <span class="input-group-addon nobg">－</span>
						                  <input type="text" name="search_LT_sendTime" class="form-control" data-role="date-end">
						              	</div>
						              </div>
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
									<shiro:hasPermission name="/admin/messageInfo/list$create">
										<a href="create" class="btn btn-default btn-sm"> <i class="fa fa-fw fa-plus"></i> 新增
										</a>
									</shiro:hasPermission>
								</div>
							</div>
						</div>

						<div class="box-body">
							<div class="filter-tabs filter-tabs2 clearfix">
								<ul class="list-unstyled">
									<li lang=":input[name='search_EQ_auditStatus']" <c:if test="${empty param['search_EQ_auditStatus']}">class="actived"</c:if>>全部(${waitAuditCount + noAuditCount+yesAuditCount})</li>
									<li value="0" role=":input[name='search_EQ_auditStatus']" <c:if test="${param['search_EQ_auditStatus'] == '0' }">class="actived"</c:if>>待审核(${waitAuditCount})</li>
									<li value="2" role=":input[name='search_EQ_auditStatus']" <c:if test="${param['search_EQ_auditStatus'] == '2' }">class="actived"</c:if>>审核不通过(${noAuditCount})</li>
									<li value="1" role=":input[name='search_EQ_auditStatus']" <c:if test="${param['search_EQ_auditStatus'] == '1' }">class="actived"</c:if>>已发送(${yesAuditCount})</li>
								</ul>
							</div>
							<div class="table-responsive">
								<table class="table table-bordered table-striped  text-center  table-cell-ver-mid ">
									<thead>
										<tr>
											<th>类型</th>
											<th>签名</th>
											<th>内容</th>
											<th>发送人</th>
											<th>发送时间</th>
											<th>发送条数</th>
											<th>发送成功/发送失败</th>
											<th>费用</th>
											<th>状态</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty pageInfo.content}">
												<c:forEach items="${pageInfo.content}" var="info">
													<c:if test="${not empty info}">
														<td>
															${typeMap[info.type]}
														</td>
														<td>
															${signatureMap[info.signature]}
														</td>
														<td style="text-align: left;">
															${info.content}
														</td>
														<td>${info.createUsername}
																<div class="gray9">（${info.createRoleName}）</div>
														</td>
														<td>
															<c:if test="${not empty info.sendTime}">
																<fmt:formatDate value="${info.sendTime}" type="both"/>
															</c:if>
														</td>
														<td>
															${info.sendCount}
														</td>
														<td>
															<c:if test="${info.auditStatus eq '1'}">
																${info.sendSuccessCount}/${info.sendFailedCount}
															</c:if>
														</td>
														<td>
															￥0.00
														</td>
														<td>
															<c:if test="${info.auditStatus eq '0'}">
																<span class="text-orange">待审核</span>
															</c:if>
															
															<c:if test="${info.auditStatus eq '1'}">
																<span class="text-green">已发送</span>
															</c:if>
															
															<c:if test="${info.auditStatus eq '2'}">
															<span class="text-red">审核不通过</span>
															</c:if>
														</td>
														<td>
															
															<div class="data-operion">
																<c:if test="${info.auditStatus ne '1' }">
																	<a href="audit/${info.id}"  data-id="${info.id}" class="operion-item operion-view" 
																	data-toggle="tooltip" title="" data-original-title="审核"><i class="fa fa-shxxjl"></i></a>
																</c:if>
																<a href="view/${info.id}" class="operion-item operion-view" data-toggle="tooltip" title="" data-original-title="查看详情"><i
																	class="fa fa-fw fa-view-more"></i> </a>
																<c:if test="${info.auditStatus ne '1' }">
																	<a href="update/${info.id}" class="operion-item" val="${info.id}" title="修改" data-tempTitle="修改">
																	<i class="fa fa-edit"></i></a>
																</c:if>
															</div>
														</td>
														</tr>
													</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td align="center" colspan="10">暂无数据</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
								<tags:pagination page="${pageInfo}" paginationSize="5" />
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
