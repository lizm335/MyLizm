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
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">角色管理</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box">
					<div class="box-body">
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">角色名称</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_roleName" value="${param.search_LIKE_roleName}">
								</div>
							</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">角色编号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_roleCode" value="${param.search_EQ_roleCode}">
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
							<c:if test="${isBtnCreate }">
								<a href="create" class="btn btn-default btn-sm">
										<i class="fa fa-fw fa-plus"></i> 新增</a>
							</c:if>
							</div>
							<div class="btn-wrap fl">
								<c:if test="${isBtnDelete }">
								<a href="javascript:void(0);" class="btn btn-block btn-danger btn-del del-checked">
										<i class="fa fa-fw fa-trash-o"></i> 删除
								</a>
								</c:if>
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
												<th>角色名称</th>
												<th>角色编号</th>
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
																	data-id="${entity.roleId}" data-name="check-id"
																	value="${entity.roleId}"></td>
																<td>${entity.roleName}</td>
																<td>${entity.roleCode}</td>
																<td>
																	<div class="data-operion">
																	<c:if test="${isBtnUpdate }">
																		<a href="update/${entity.roleId}"
																			class="operion-item operion-edit" title="编辑">
																			<i class="fa fa-fw fa-edit"></i></a>
																			
																		<a href="roleManage/${entity.roleId}" 
																			class="operion-item operion-view" title="角色权限控制设置">
																			<i class="fa fa-exchange"></i></a>	
																	</c:if>
																		<a href="view/${entity.roleId}" 
																			class="operion-item operion-view" title="查看">
																			<i class="fa fa-fw fa-eye"></i></a>
																	<%-- <c:if test="${isBtnDelete }">		
																		<a href="javascript:void(0);"
																			class="operion-item operion-del del-one" val="${entity.roleId}"
																			title="删除" data-tempTitle="删除">
																			<i class="fa fa-fw fa-trash-o"></i></a>
																	</c:if> --%>
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
