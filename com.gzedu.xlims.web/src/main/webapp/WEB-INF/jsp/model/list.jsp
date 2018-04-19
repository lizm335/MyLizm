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
		
		<!-- Main content -->
		<section class="content-header">
			
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">操作管理</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box">
					<div class="box-body">
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">操作名称</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_operateName" value="${param.search_LIKE_operateName}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">创建时间</label>
								<div class="input-daterange input-group">
								    <input type="text" class="input-sm form-control" name="search_GTE_createdDt" value="${param.search_GTE_createdDt}"/>
								    <span class="input-group-addon">to</span>
								    <input type="text" class="input-sm form-control" name="search_LTE_createdDt" value="${param.search_LTE_createdDt}" />
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer">
<!-- 						<div class="search-more-in">
							高级搜索<i class="fa fa-fw fa-caret-down"></i>
						</div> -->
						<div class="btn-wrap">
							<button type="reset" class="btn btn-default">重置</button>
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
												<th>操作名称</th>
												<th>操作编码</th>
												<th>描述</th>
												<th>创建时间</th>
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
																	data-id="${entity.operateId}" data-name="check-id"
																	value="${entity.operateId}"></td>
																<td>${entity.operateName}</td>
																<td>${entity.operateCode}</td>
																<td>${entity.operateDec}</td>
																<td>${entity.createdDt}</td>
																<td>
																	<div class="data-operion">
																		<a href="update/${entity.operateId}"
																			class="operion-item operion-edit" title="编辑">
																			<i class="fa fa-fw fa-edit"></i></a> 
																		<a href="view/${entity.operateId}" 
																			class="operion-item operion-view" title="查看">
																			<i class="fa fa-fw fa-eye"></i></a>
																		<a href="javascript:void(0);"
																			class="operion-item operion-del del-one" val="${entity.operateId}"
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
														<td align="center" colspan="5">暂无数据</td>
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
