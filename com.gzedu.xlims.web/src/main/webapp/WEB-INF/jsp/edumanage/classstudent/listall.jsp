<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>查看全部班级学员</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">班级学员列表</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<form class="form-horizontal" id="listForm" action="listall.html">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">年级</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">班主任</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtClassInfo.gjtBzr.employeeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${headTeacherMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtClassInfo.gjtBzr.employeeId']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">层次</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtClassInfo.pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtClassInfo.pycc']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
					</div>
					<div class="row reset-form-horizontal clearbox">
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">班级名称</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtClassInfo.classId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${classInfoMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtClassInfo.classId']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_gjtStudentInfo.xh" value="${param['search_EQ_gjtStudentInfo.xh']}">
								</div>
						</div>
						
						<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
								</div>
						</div>
					</div>
					
					<c:choose>
							<c:when test="${not empty param['search_LIKE_gjtStudentInfo.scCo'] ||not empty param['search_EQ_gjtOrg.id']||not empty  param['search_EQ_gjtStudyCenter.id']}">
									<div class="form-search-more" >
							</c:when>
							<c:otherwise>
									<div class="form-search-more" style="display: none;">
							</c:otherwise>
						</c:choose>
						<div class="row reset-form-horizontal clearbox">
								<div class="col-md-4">
									<label class="control-label col-sm-3">所在单位</label>
									<div class="col-sm-9">
										<input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.scCo" value="${param['search_LIKE_gjtStudentInfo.scCo']}">
									</div>
								</div>
								<div class="col-md-4">
									<label class="control-label col-sm-3">招生机构</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtOrg.id" class="selectpicker show-tick form-control" 
											data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${orgMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtOrg.id']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="col-md-4">
									<label class="control-label col-sm-3">学习中心</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudyCenter.id" class="selectpicker show-tick form-control" 	data-size="10" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${studyCenterMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtStudyCenter.id']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
						</div>
					</div>
					
				</div>
				<div class="box-footer">
				<div class="search-more-in">
						高级搜索<i class="fa fa-fw fa-caret-down"></i>
					</div> 
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
									<th><input type="checkbox" class="select-all"
										id="selectAll"></th>
									<th>班级名称</th>
									<th>班主任</th>
									<th>学号</th>
									<th>姓名</th>
									<th>层次</th>
									<th>专业</th>
									<th>所在单位</th>
									<th>招生机构</th>
									<th>学习中心</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageInfo.getContent() }" var="item">
									<tr>
										<td><input type="checkbox" value="${item.classStudentId }"	name="ids" class="checkbox"></td>
										<td>${item.gjtClassInfo.bjmc }</td>
										<td>${item.gjtClassInfo.gjtBzr.xm }</td>
										<td>${item.gjtStudentInfo.xh }</td>
										<td>${item.gjtStudentInfo.xm }</td>
										<td> ${pyccMap[item.gjtStudentInfo.pycc]}</td>
										<td>${item.gjtStudentInfo.gjtSpecialty.zymc }</td>
										<td>${item.gjtStudentInfo.scCo }</td>
										<td>${item.gjtOrg.orgName }</td>
										<td>${item.gjtStudyCenter.scName }</td>
									</tr>
								</c:forEach> 
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