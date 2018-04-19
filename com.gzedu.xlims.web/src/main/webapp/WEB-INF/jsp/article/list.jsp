<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>文章管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">

</script>

</head>
<body class="inner-page-body">
		
		<!-- Main content -->
		<section class="content-header">
			
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li class="active">文章管理</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box">
				  <div class="box-body">
				  	<div class="input-group">
				  		<div class="input-group-addon no-border text-bold">文章标题</div>
				  		<input type="text" class="form-control" name="search_LIKE_title" value="${param['search_LIKE_title']}">
				  		<div class="input-group-btn">
				  			<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
				  		</div>
				  	</div>
				  </div>
				</div>
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="box margin-bottom-none">			
					<div class="box-header with-border">
					  <h3 class="box-title pad-t5">文章列表</h3>
					  <div class="pull-right no-margin">
					  <shiro:hasPermission name="/article/list$delete">
					  	<button type="button" class="btn btn-default btn-sm margin_r10 btn-del del-checked"><i class="fa fa-fw fa-trash-o"></i> 删除</button>
					  </shiro:hasPermission>
					  <shiro:hasPermission name="/article/list$create">
					    <a role="button" href="toCreateArticle" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新增文章</a>
					 </shiro:hasPermission>
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
									<table class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
										<thead>
											<tr>
												<th><input type="checkbox" class="select-all"></th>
												<th>标题</th>
												<th>专业</th>
												<th>学期</th>
												<th>层次</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach items="${infos.content}" var="info">
											<c:if test="${not empty info}">
												<tr>
													<td><input class="checkbox" type="checkbox" name="ids"
														data-id="${info.id}" data-name="check-id"
														value="${info.id}"></td>
													<td>${info.title}</td>
													<td>
														${info.specialtyName}
													</td>
													<td>
														${info.gradeName}
													</td>
													<td>
														${info.pyccName}
													</td>
													<td>
														<div class="data-operion">
															<shiro:hasPermission name="/article/list$update">
															 <a href="update/${info.id}"
																class="operion-item operion-edit" title="编辑">
																<i class="fa fa-fw fa-edit"></i></a> 
															</shiro:hasPermission>
															<a href="detail/${info.id}" 
																class="operion-item operion-view" title="查看">
																<i class="fa fa-fw fa-eye"></i></a>
															<shiro:hasPermission name="/article/list$delete">
															<a href="javascript:void(0);"
																class="operion-item operion-del del-one" val="${info.id}"
																title="删除" data-tempTitle="删除">
																<i class="fa fa-fw fa-trash-o"></i></a>
															</shiro:hasPermission>
														</div>
													</td> 
												</tr>
											</c:if>
										</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<tags:pagination page="${infos}" paginationSize="5" />
						</div>
					</div>
				</div>
			</form>
		</section>		
</body>
</html>
