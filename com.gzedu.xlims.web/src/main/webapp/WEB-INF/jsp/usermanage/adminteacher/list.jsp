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
<title>管理员管理系统-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">管理员管理</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<form class="form-horizontal" id="listForm" action="list.html">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
					<div class="col-md-4">
								<label class="control-label col-sm-3">帐号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_loginAccount" 
									value="${param['search_LIKE_loginAccount']}">
								</div>
						</div>
						<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_realName" value="${param.search_LIKE_realName}">
								</div>
						</div>
						<div class="col-md-4">
							<label class="control-label col-sm-3">所属机构</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtOrg.id" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<c:forEach items="${orgMap}" var="org">
										<option value="${org.key}"  <c:if test="${org.key==param['search_EQ_gjtOrg.id']}">selected='selected'</c:if>>${org.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer">
<!-- 					<div class="search-more-in">
						高级搜索<i class="fa fa-fw fa-caret-down"></i>
					</div> -->
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
				<!-- <div class="btn-wrap fl">
					<button class="btn btn-block btn-success btn-outport">
						<i class="fa fa-fw fa-sign-out"></i> 批量导出
					</button>
				</div> -->
				<div class="btn-wrap fl">
					<a href="create?orgId=${param['search_EQ_gjtOrg.id']}" class="btn btn-default btn-sm">
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
									<th>院校名称</th>
									<th>帐号</th>
									<th>姓名</th>
									<th>角色</th>
									<th>登陆IP</th>
									<th>最后登陆时间</th>
									<th>登陆次数</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							<c:choose>
								<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content }" var="item">
									<tr>
										<td><input type="checkbox" value="${item.id }"	name="ids" class="checkbox"></td>
										<td>${item.gjtOrg.orgName}</td>
										<td>${item.loginAccount}</td>
										<td>${item.realName}</td>
										<td>
											${item.priRoleInfo.roleName }
										</td>
										<td>${item.lastLoginIp}</td>
										<td>${item.lastLoginTime}</td>
										<td>${item.loginCount}</td>
										<td>${item.createdDt}</td>
										<td>
											<div class="data-operion">
												<a href="update/${item.id}"
													class="operion-item operion-edit" title="编辑">
													<i class="fa fa-fw fa-edit"></i></a> 
												<a href="view/${item.id}" 
													class="operion-item operion-view" title="查看">
													<i class="fa fa-fw fa-eye"></i></a>
												<a href="javascript:void(0);"
													class="operion-item operion-del del-one" val="${item.id}"
													title="删除" data-tempTitle="删除">
													<i class="fa fa-fw fa-trash-o"></i></a>
											</div>
										</td>
									</tr>
								</c:forEach> 
								</c:when>
								<c:otherwise>
									<tr>
										<td align="center" colspan="9">暂无数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
					</div>
				</div>
				<c:if test="${not empty pageInfo.content}">
					<tags:pagination page="${pageInfo}" paginationSize="5" />
				</c:if>
			</div>
		</div>
	</div>
	</section>
	</form>
</body>
</html>