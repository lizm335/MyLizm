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
$(function() {
})
</script>

</head>
<body class="inner-page-body">
		
		<!-- Main content -->
		<section class="content-header">
			
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">学期列表</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box">
					<div class="box-body">
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">名称</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_name" value="${param.search_LIKE_name}">
								</div>
							</div>
							
						</div>
					</div>
					<div class="box-footer">
						<div class="btn-wrap">
							<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="btn-wrap">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
				</div>
				
				
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box">
					<div class="box-header with-border">
						<div class="fr">
							<div class="btn-wrap fl">
								<a href="create" class="btn btn-default btn-sm">
										<i class="fa fa-fw fa-plus"></i> 新增</a>
							</div>
							<div class="btn-wrap fl">
								<a href="javascript:void(0);" class="btn btn-block btn-danger btn-del del-checked">
										<i class="fa fa-fw fa-trash-o"></i> 删除
								</a>
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
												<th>名称</th>
												<th>序号</th>
												<th>管理</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td><input class="checkbox" type="checkbox" name="ids"
																	data-id="${entity.id}" data-name="check-id"
																	value="${entity.id}"></td>
																<td>${entity.name}</td>
																<td>${entity.ordNo}</td>
																<td>
																	<div class="data-operion">
																		<a href="update/${entity.id}"
																			class="operion-item operion-edit" title="编辑">
																			<i class="fa fa-fw fa-edit"></i></a> 
																		<a href="view/${entity.id}" 
																			class="operion-item operion-view" title="查看">
																			<i class="fa fa-fw fa-eye"></i></a>
																		<a href="javascript:void(0);"
																			class="operion-item operion-del del-one" val="${entity.id}"
																			title="删除" data-tempTitle="删除">
																			<i class="fa fa-fw fa-trash-o"></i></a>
																	</div>
																</td> 
															</tr>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr>
														<td align="center" colspan="4">暂无数据</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</div>
							<tags:pagination page="${pageInfo}" paginationSize="5" />
						</div>
					</div>
				</div>
		</section>
		</form>
</body>
</html>
