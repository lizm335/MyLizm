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
<title>学年度课程</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">学年度课程管理</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<form class="form-horizontal" id="listForm" action="list.html">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">学年度</label>
							<div class="col-sm-9">
								<select name="search_EQ_studyYearCode" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${studyYearMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==param['search_EQ_studyYearCode']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
							</div>
						</div>
						<div class="col-md-4">
								<label class="control-label col-sm-3">课程名称</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_courseName" value="${param.search_LIKE_courseName}">
								</div>
						</div>
						<div class="col-md-4">
								<label class="control-label col-sm-3">辅导教师</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_coachTeach" value="${param.search_LIKE_coachTeach}">
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
									<th>学年度</th>
									<th>课程名称</th>
									<th>辅导教师</th>
									<th>班级数</th>
									<th>所属院校</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageInfo.getContent() }" var="item">
									<tr>
										<td>${studyYearMap[item.yearCode]}</td>
										<td>${item.COURSENAME}</td>
										<td>${item.COACHTEACH}</td>
										<td>${item.CLASSNUM}</td> 
										<td>${item.SCHOOLNAME}</td> 
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