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
<title>规则设置</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li class="active">规则设置</li>
		</ol>
	</section>

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
	<section class="content">
		<div class="box margin-bottom-none">
			<div class="box-header with-border">
			  <h3 class="box-title pad-t5">规则设置列表</h3>
			  <div class="pull-right no-margin">
			  	<button type="button" class="btn btn-default btn-sm margin_r10 del-checked"><i class="fa fa-fw fa-trash-o"></i> 删除</button>
			    <a href="create" role="button" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新增规则</a>
			  </div>
			</div>
		
		<div class="box-body">
			<div class="table-responsive">
	            <table class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
							<thead>
								<tr>
									<th><input type="checkbox" class="select-all" 	id="selectAll"></th>
									<th>所属院校</th>
									<th>年级</th>
									<th>层次</th>
									<th>专业</th>
									<th>学习中心</th>
									<th>班级类型</th>
									<th>上限人数</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageInfo.getContent() }" var="item">
									<tr>
										<td><input type="checkbox" value="${item.id}"	name="ids"></td>
										<td>${item.gjtSchoolInfo.xxmc}</td>
										<td>${item.gjtGrade.gradeName}</td>
										<td>${pyccMap[item.pycc]}</td>
										<td>${item.gjtSpecialty.zymc}	</td>
										<td>${item.gjtStudyCenter.scName}	</td>
										<td>   <c:if test="${item.classtype=='teach' }">教务班级</c:if>
												<c:if test="${item.classtype=='course' }">课程班级</c:if>
										</td>
										<td>${item.peopleNo}	</td>
										<td>
											<div class="data-operion">
												<a href="update/${item.id}" 
													class="operion-item operion-view" data-toggle="tooltip" title="编辑">
													<i class="fa fa-fw fa-edit"></i></a>
													
												<a href="view/${item.id}" 
													class="operion-item operion-view" data-toggle="tooltip" title="查看">
													<i class="fa fa-fw fa-view-more"></i></a>
													
												<a href="javascript:void(0);"
													class="operion-item operion-del del-one" val="${item.id}"
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
				 <tags:pagination page="${pageInfo}" paginationSize="5" /> 
			</div>
	</section>
	<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>