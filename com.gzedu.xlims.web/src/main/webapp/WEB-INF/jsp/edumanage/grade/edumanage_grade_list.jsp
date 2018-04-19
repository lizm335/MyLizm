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
<title>学期管理系统-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb oh">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">教务管理</a></li>
			<li class="active">学期管理</li>
		</ol>
	</section>

	<section class="content">
		<form class="form-horizontal" id="listForm" action="list.html">
			<div class="box">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">编号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_gradeCode" value="${param.search_LIKE_gradeCode}">
								</div>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学期</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_gradeName" value="${param.search_LIKE_gradeName}">
								</div>
							</div>
						</div>
					</div>
				</div><!-- /.box-body -->
				<div class="box-footer">
					<div class="pull-right"><button type="button" class="btn min-width-90px btn-default  btn-reset">重置</button></div>
					<div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
				</div><!-- /.box-footer-->
			</div>

			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">学期列表</h3>
					 <div class="pull-right no-margin">
						<a href="create" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新建学期</a>
					</div>
				</div>
				<div class="box-body">
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
							<tr>
								<th>编号</th>
								<th>年级</th>
								<th>学期</th>
								<th colspan="2">学期计划</th>
								<th>已开设专业</th>
								<th>操作</th>
							</tr>
							</thead>
							<tbody>
							<c:set var="rowspan" value="${permitted?2:7 }"></c:set>
							<c:forEach items="${pageInfo.getContent() }" var="item">
							<tr>
								<td rowspan="${rowspan}">${item.gradeCode}</td>
								<td rowspan="${rowspan}">
									<c:if test="${not empty(item.gjtYear) }">
										${item.gjtYear.name}
									</c:if>
								</td>
								<td rowspan="${rowspan}">${item.gradeName}</td>
								<td>
				                  	<div class="text-right">
				                  		学期时间
				                  	</div>
				                  </td>
								<td>
									<div class="text-left">
										<c:if test="${not empty(item.startDate)}">
											${item.startDate}~${item.endDate}
										</c:if>
									</div>
								</td>
								<td rowspan="${rowspan}">
									本科： ${item.getSpecialtyCount(1)}<br />
									专科： ${item.getSpecialtyCount(0)}
								</td>
								<td rowspan="${rowspan}">
				                    <div class="data-operion">
				                    	<shiro:hasPermission name="/edumanage/grade/list$setSpecialty">
											<a href="${ctx}/edumanage/gradespecialty/querySpecialty?gradeId=${item.gradeId}" class="operion-item operion-view" data-toggle="tooltip" title="开设专业"><i class="fa fa-fw fa fa-kszj"></i></a>
										</shiro:hasPermission>
										<shiro:hasPermission name="/edumanage/grade/list$update">
											<a href="update/${item.gradeId}" class="operion-item operion-view" data-toggle="tooltip" title="编辑"><i class="fa fa-edit"></i></a>
										</shiro:hasPermission>
				                    </div>
								</td>
							</tr>
							<tr> 
	                  <td>
	                  	<div class="text-right">
	                  		课程教学周期
	                  	</div>
	                  </td>
	                  <td>
	                  	<div class="text-left">
	                  		<c:if test="${not empty(item.courseStartDate)}">
	                  		${item.courseStartDate}~${item.courseEndDate}
	                  		</c:if>
	                  	</div>
	                  </td>
	                </tr>
	                <c:if test="${!permitted }">
	                <tr> 
	                  <td>
	                  	<div class="text-right">
	                  		上传授课计划截止时间
	                  	</div>
	                  </td>
	                  <td>
	                  	<div class="text-left">
	                  		${item.upCourseEndDate}
	                  	</div>
	                  </td>
	                </tr>
	                <tr> 
	                  <td>
	                  	<div class="text-right">
	                  		上传成绩截止时间
	                  	</div>
	                  </td>
	                  <td>
	                  	<div class="text-left">
	                  		${item.upAchievementDate}
	                  	</div>
	                  </td>
	                </tr>
	                <tr> 
	                  <td>
	                  	<div class="text-right">
	                  		老生缴费日期
	                  	</div>
	                  </td>
	                  <td>
	                  	<div class="text-left">
	                  		<c:if test="${not empty(item.payStartDate)}">
	                  		${item.payStartDate}~${item.payEndDate }
	                  		</c:if>
	                  	</div>
	                  </td>
	                </tr>
	                <tr> 
	                  <td>
	                  	<div class="text-right">
	                  		老生开学日期
	                  	</div>
	                  </td>
	                  <td>
	                  	<div class="text-left">
	                  		${item.oldStudentEnterDate}
	                  	</div>
	                  </td>
	                </tr>
	                <tr> 
	                  <td>
	                  	<div class="text-right">
	                  		新生开学日期
	                  	</div>
	                  </td>
	                  <td>
	                  	<div class="text-left">
	                  		${item.newStudentEnterDate}
	                  	</div>
	                  </td>
	                </tr>
	                </c:if>
							</c:forEach>
							</tbody>
						</table>
	
						<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>
			</div>
		</form>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript" src="${ctx}/static/js/edumanage/grade/edumanage_grade_list.js"></script>
</body>
</html>