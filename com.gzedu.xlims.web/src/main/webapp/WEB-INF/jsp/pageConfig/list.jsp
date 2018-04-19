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
				<li><a href="#">主页</a></li>
				<li class="active">页面配置列表</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box">
					<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">应用名称</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_appName" value="${param['search_LIKE_appName']}">
								</div>
							</div>
						</div>
						<div class="box-footer">
							<div class="btn-wrap">
								<button type="reset" class="btn min-width-90px btn-default">重置</button>
							</div>
							<div class="btn-wrap">
								<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
							</div>
						</div>
					</div>
				
				
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box margin-bottom-none">
					<div class="box-header with-border">
						<div class="fr">
							<div class="btn-wrap fl">
								<a href="toCreatePageConfig" class="btn btn-default btn-sm">
										<i class="fa fa-fw fa-plus"></i> 新增</a>
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
												<th>学校</th>
												<th>应用key</th>
												<th>应用名称</th>
												<th>位置key</th>
												<th>位置名称</th>
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
													<td>${info.orgName }</td>
													<td>
														${info.appKey}
													</td>
													<td>
														${info.appName}
													</td>
													<td>
														${info.posKey}
													</td>
													<td>
														${info.posName}
													</td>
													<td>
														<div class="data-operion">
															 <a href="update/${info.id}"
																class="operion-item operion-edit" title="编辑">
																<i class="fa fa-fw fa-edit"></i></a> 
															<a href="view/${info.id}" 
																class="operion-item operion-view" title="查看">
																<i class="fa fa-fw fa-eye"></i></a>
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
		<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>	
</body>
</html>
