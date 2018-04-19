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
			<li class="active">个人中心菜单管理</li>
		</ol>
	</section>
	<section class="content" data-id="0">
		<form id="listForm" class="form-horizontal">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">菜单名称</label>
							<div class="col-sm-9">
								<select id="basic" class="selectpicker show-tick form-control"
									multiple data-size="5" data-live-search="true">
									<c:forEach items="${list}" var="menu">
										<option value="${menu.id}">
											<c:choose>
												<c:when test="${menu.menu_level eq 0}">
												&nbsp;&nbsp;
											</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${menu.menu_level eq 1}">
														&nbsp;&nbsp;&nbsp;&nbsp;
													</c:when>
														<c:otherwise>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</c:otherwise>
													</c:choose>

												</c:otherwise>
											</c:choose> ${menu.name}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
			</div>
		<div class="box-footer">
<!-- 			<div class="search-more-in">
				高级搜索<i class="fa fa-fw fa-caret-down"></i>
			</div> -->
			<div class="btn-wrap">
				<button type="button" class="btn btn-default">重置</button>
			</div>
			<div class="btn-wrap">
				<button type="button" class="btn min-width-90px btn-primary">搜索</button>
			</div>
		</div>
		</div>
		

		<div class="box">
			<div class="box-header with-border">
				<div class="fr">
					<div class="btn-wrap fl">
						<a href="${pageContext.request.contextPath}/module/menu/add"><button
								class="btn btn-default btn-sm">
								<i class="fa fa-fw fa-plus"></i> 新增
							</button></a>
					</div>
					<div class="btn-wrap fl">
						<a href="javascript:void(0);" onclick="deleteAll()"><button
								class="btn btn-block btn-danger btn-del">
								<i class="fa fa-fw fa-trash-o"></i> 删除
							</button></a>
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
										<th>菜单名称</th>
										<th>菜单地址</th>
										<th>是否显示</th>
										<th>管理</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${not empty list}">
											<c:forEach items="${infos.content}" var="menu">
												<c:if test="${not empty menu}">
													<tr>
														<td><input type="checkbox" name="id"
															data-id="${menu.id}" data-name="check-id"
															value="${menu.id}"></td>
														<td><span
															style="margin-left:${menu.menu_level * 15}px;">
																${menu.name} </span></td>
														<td>${menu.url}</td>
														<td>${menu.visible == 1?"显示":"不显示"}</td>
														<td>
															<div class="data-operion">
																<a href="edit?id=${menu.id}"
																	class="operion-item operion-edit" title="编辑"><i
																	class="fa fa-fw fa-edit"></i></a> <a
																	href="view?id=${menu.id}"
																	class="operion-item operion-view" title="查看"><i
																	class="fa fa-fw fa-eye"></i></a> <a href="#modal-template"
																	val="${menu.id}" class="operion-item operion-del"
																	data-toggle="modal" title="删除" data-tempTitle="删除"><i
																	class="fa fa-fw fa-trash-o"></i></a>
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
					<tags:pagination page="${infos}" paginationSize="5" />
				</div>
			</div>
		</div>
	</section>
	</form>
</body>
</html>