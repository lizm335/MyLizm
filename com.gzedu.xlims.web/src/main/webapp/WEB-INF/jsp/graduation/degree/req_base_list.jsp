<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-毕业管理</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">毕业管理</a></li>
			<li class="active">
				学位基础条件管理
			</li>
		</ol>
	</section>

	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html">

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box">
			<div class="box-header with-border">
				<div class="pull-right no-margin">
					<a href="create" class="btn btn-default btn-sm margin_r10">
							<i class="fa fa-fw fa-plus"></i> 新增</a>
					<a href="javascript:void(0);" class="btn btn-default btn-sm del-checked">
						<i class="fa fa-fw fa-trash-o"></i> 删除
					</a>
				</div>
			</div>
			<div class="box-body">
				<div id="dtable_wrapper" class="table-responsive">
					<table id="dtable" class="table table-bordered table-hover table-font vertical-mid text-center">
						<thead>
							<tr>
								<th><input type="checkbox" class="select-all" id="selectAll"></th>
								<th>类型</th>
								<th>名称</th>
								<th>描述</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${infos }" var="info">
								<tr>
									<td><input type="checkbox" value="${info.baseId }"	name="ids"></td>
									<td>${baseTypeMap[info.baseType]}</td>
									<td>${info.baseName}</td>
									<td>${info.baseDesc}</td>
									<td>
										<div class="data-operion">
											<a href="update/${info.baseId}"
											   class="operion-item operion-edit" title="编辑">
												<i class="fa fa-fw fa-edit"></i></a>
											<a href="javascript:void(0);"
												class="operion-item operion-del del-one" val="${info.baseId}"
												title="删除" data-tempTitle="删除">
												<i class="fa fa-fw fa-trash-o"></i></a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

				</div>
			</div>
		</div>
		</form>
	</section>
	
	<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>