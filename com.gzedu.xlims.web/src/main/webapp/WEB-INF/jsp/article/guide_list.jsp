<%@page import="com.gzedu.xlims.pojo.status.CaochDateTypeEnum"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

	<section class="content-header clearfix">
		<ol class="breadcrumb oh">
			<li>
				<a href="#">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">教学管理</a>
			</li>
			<li class="active">学习指引</li>
		</ol>
	</section>
	<section class="content">
		<form id="listForm" class="form-horizontal">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li>
						<a href="${ctx}/edumanage/course/queryFirstCoursesList">开学第一堂课</a>
					</li>
					<li class="active">
						<a href="#">学习指引</a>
					</li>
				</ul>
				<div class="box box-border no-shadow">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">标题</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_title" class="form-control" value="${param.search_LIKE_title }">
									</div>
								</div>
							</div>

						</div>
					</div>
					<!-- /.box-body -->
					<div class="box-footer">
						<div class="pull-right">
							<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="pull-right margin_r15">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
					<!-- /.box-footer-->
				</div>
				<div class="box box-border no-shadow margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">学习指引列表</h3>
						<div class="pull-right no-margin">
							<shiro:hasPermission name="/edumanage/course/queryFirstCoursesList$delete">
								<button type="button" class="btn btn-default btn-sm margin_r10 btn-del del-checked">
									<i class="fa fa-fw fa-trash-o"></i> 删除
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="/edumanage/course/queryFirstCoursesList$create">
								<a role="button" href="toGuideForm" class="btn btn-default btn-sm">
									<i class="fa fa-fw fa-plus"></i> 新增
								</a>
							</shiro:hasPermission>
						</div>
					</div>
					<div class="box-body">
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
									<tr>
										<th><input type="checkbox" class="select-all"></th>
										<th>标题</th>
										<!-- <th>专业</th>
										<th>学期</th>
										<th>层次</th> -->
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${infos.content}" var="info">
										<c:if test="${not empty info}">
											<tr>
												<td><input class="checkbox" type="checkbox" name="ids" data-id="${info.id}" data-name="check-id" value="${info.id}"></td>
												<td>${info.title}</td>
												<%-- <td>${info.specialtyName}</td>
												<td>${info.gradeName}</td>
												<td>${info.pyccName}</td> --%>
												<td>
													<div class="data-operion">
														<shiro:hasPermission name="/edumanage/course/queryFirstCoursesList$update">
														<a href="toGuideForm?id=${info.id }" class="operion-item operion-edit" title="编辑">
															<i class="fa fa-fw fa-edit"></i>
														</a>
														</shiro:hasPermission>
														<shiro:hasPermission name="/edumanage/course/queryFirstCoursesList$view">
														<a href="detail/${info.id}" class="operion-item operion-view" title="查看">
															<i class="fa fa-fw fa-eye"></i>
														</a>
														</shiro:hasPermission>
														<shiro:hasPermission name="/edumanage/course/queryFirstCoursesList$delete">
															<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${info.id}" title="删除" data-tempTitle="删除">
																<i class="fa fa-fw fa-trash-o"></i>
															</a>
														</shiro:hasPermission>
													</div>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
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
