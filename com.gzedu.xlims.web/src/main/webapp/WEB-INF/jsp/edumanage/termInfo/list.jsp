<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学期管理-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">学期管理</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<form class="form-horizontal" id="listForm" action="list.html">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">年级</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtGrade.gradeId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-4">
							<label class="control-label col-sm-3">学期</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_termName" 	value="${param.search_LIKE_termName}">
							</div>
						</div>
						<div class="col-md-4">
									<label class="control-label col-sm-3">院校</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtSchoolInfo.id" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${schoolInfoMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtSchoolInfo.id']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
					</div>
				</div>
				<div class="box-footer">
					<div class="btn-wrap">
						<button type="button" class="btn btn-default">重置</button>
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
						<a href="javascript:void(0);" class="btn btn-block btn-danger btn-del del-checked">
							<i class="fa fa-fw fa-trash-o"></i> 删除
						</a>
				</div>
				<div class="btn-wrap fl">
					<a href="create" class="btn btn-default btn-sm">
							<i class="fa fa-fw fa-plus"></i> 新增</a>
				</div>
				
			</div>
		</div>
		<div class="box-body">
			<div id="dtable_wrapper"
				class="dataTables_wrapper form-inline dt-bootstrap no-footer">
				<div class="row">
					<div class="col-sm-6"></div>
					<div class="col-sm-6"></div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<table id="dtable"
							class="table table-bordered table-striped table-container">
							<thead>
								<tr>
									<th><input type="checkbox" class="select-all" 	id="selectAll"></th>
									<th>年级</th>
									<th>学期名称</th>
									<th>学期开始时间</th>
									<th>学期结束时间</th>
									<th>学期描述</th>
									<th>院校</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							<c:choose>
								<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.getContent() }" var="item">
									<tr>
										<td><input type="checkbox" value="${item.termId }"	name="ids" class="checkbox"></td>
										<td>${item.gjtGrade.gradeName}</td>
										<td>${item.termName}</td>
										<td>${item.startDate}</td>
										<td>${item.endDate}</td>
										<td>${item.termDesc}</td>
										<td>${item.gjtSchoolInfo.xxmc}</td>
										<td>
											<div class="data-operion">
												<a href="update/${item.termId}"
													class="operion-item operion-edit" title="编辑">
													<i class="fa fa-fw fa-edit"></i></a> 
												<a href="view/${item.termId}" 
													class="operion-item operion-view" title="查看">
													<i class="fa fa-fw fa-eye"></i></a>
												<a href="javascript:void(0);"
													class="operion-item operion-del del-one" val="${item.termId}"
													title="删除" data-tempTitle="删除">
													<i class="fa fa-fw fa-trash-o"></i></a>
											</div>
										</td>
									</tr>
								</c:forEach> 
								</c:when>
								<c:otherwise>
									<tr>
										<td align="center" colspan="8">暂无数据</td>
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