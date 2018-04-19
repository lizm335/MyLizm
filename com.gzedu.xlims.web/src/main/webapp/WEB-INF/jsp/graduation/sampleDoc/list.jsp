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
				示例文档管理
			</li>
		</ol>
	</section>

	<section class="content">
		<form class="form-horizontal" id="listForm" action="list.html">
		<div class="box">
			<div class="box-body">
				<div class="row pad-t15">
					<div class="col-md-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">文档名称</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_documentName" value="${param.search_LIKE_documentName}">
							</div>
						</div>
					</div>
					<div class="col-md-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">文档类型</label>
							<div class="col-sm-9">
								<select name="search_EQ_documentType" class="selectpicker show-tick form-control">
									<option value="">请选择</option>
									<c:forEach items="${documentTypes}" var="map">
										<option value="${map.code}"  <c:if test="${map.code==param['search_EQ_documentType']}">selected='selected'</c:if>>${map.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">专业</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${specialtyMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="box-footer text-right">
				<button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
        		<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
			</div>
		</div>

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box margin-bottom-none">
			<div class="box-header with-border">
				<div class="pull-right no-margin">
					<!-- <div class="btn-wrap fl">
						<button class="btn btn-block btn-success btn-outport">
							<i class="fa fa-fw fa-sign-out"></i> 批量导出
						</button>
					</div> -->
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
								<th>文档名称</th>
								<th>文档类型</th>
								<th>所属专业</th>
								<th>上传时间</th>
								<th>下载次数</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageInfo.content }" var="info">
								<tr>
									<td><input type="checkbox" value="${info.documentId }"	name="ids"></td>
									<td>${info.documentName}</td>
									<td>
										<c:forEach var="t" items="${documentTypes}">
											<c:if test="${t.code==info.documentType}">${t.name}</c:if>
										</c:forEach>
									</td>
									<td>${info.gjtSpecialty.zymc}</td>
									<td><fmt:formatDate type="both" value="${info.createdDt}" /></td>
									<td><fmt:formatNumber value="${info.downloadNum}" maxFractionDigits="0" /></td>
									<td>
										<div class="data-operion">
											<a href="${info.documentUrl}"
												class="operion-item operion-view" title="下载">
												<i class="fa fa-fw fa-download"></i></a>
											<a href="javascript:void(0);"
												class="operion-item operion-del del-one" val="${info.documentId}"
												title="删除" data-tempTitle="删除">
												<i class="fa fa-fw fa-trash-o"></i></a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					 <tags:pagination page="${pageInfo}" paginationSize="10" />
				</div>
			</div>
		</div>
		</form>
	</section>
	<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>