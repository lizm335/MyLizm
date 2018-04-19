<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>年级专业管理系统-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">年级专业管理</li>
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
								<select name="search_EQ_gjtGrade.gradeId"
									class="selectpicker show-tick form-control" 
									data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']}">selected='selected'</c:if> >
											${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">层次</label>
							<div class="col-sm-9">
								<select name ="search_EQ_gjtSpecialty.pycc"class="selectpicker show-tick form-control" 
									data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}" 
											<c:if test="${map.key==param['search_EQ_gjtSpecialty.pycc']}">selected='selected'</c:if>>${map.value}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">专业名称</label>
							<div class="col-sm-9">
								<select  name="search_EQ_gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" 
									data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${specialtyMap}" var="map">
										<option value="${map.key}" 
											 <c:if test="${map.key==param['search_EQ_search_EQ_gjtSpecialty.specialtyId']}">	selected='selected'
											 </c:if> > ${map.value}
										</option>
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
		

		<div class="alert alert-success"
			<c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>>
			<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger"
			<c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>>
			<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box">
			<div class="box-header with-border">
				<div class="fr">
					<div class="btn-wrap fl">
						<a href="javascript:void(0);"
							class="btn btn-block btn-danger btn-del del-checked"> <i
							class="fa fa-fw fa-trash-o"></i> 删除
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
										<th>年级</th>
										<th>专业编码</th>
										<th>专业名称</th>
										<th>专业性质</th>
										<th>层次</th>
										<th>总学分</th>
										<th>必修学分</th>
										<th>选修学分</th>
										<th>所属院校</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pageInfo.getContent() }" var="item">
										<tr>
											<td><input type="checkbox" value="${item.gjtSpecialty.specialtyId }"
												name="ids" class="checkbox"></td>
											<td>${item.gjtGrade.gradeName }</td>
											<td>${item.gjtSpecialty.zyh}</td>
											<td>${item.gjtSpecialty.zymc}</td>
											<td>${item.gjtSpecialty.specialtyCategory}
											</td>
											<td> 
													${pyccMap[item.gjtSpecialty.pycc]}
											 </td>
											<td>${item.gjtSpecialty.zxf}</td>
											<td>${item.gjtSpecialty.bxxf}</td>
											<td>${item.gjtSpecialty.xxxf}</td>
											<td>${item.gjtSchoolInfo.xxmc}</td>
											<td>
												<div class="data-operion">
													<%-- <a href="update/${item.gjtSpecialty.specialtyId}"	class="operion-item operion-edit" title="编辑"> <i
														class="fa fa-fw fa-edit"></i></a> 
														<a href="view/${item.gjtSpecialty.specialtyId}" class="operion-item operion-view" title="查看"> 
														<i	class="fa fa-fw fa-eye"></i></a>  --%>
														<a href="javascript:void(0);" class="operion-item operion-del del-one"
															val="${item.gjtSpecialty.specialtyId}" title="删除" data-tempTitle="删除">
														<i class="fa fa-fw fa-trash-o"></i>
													</a>
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
			</div>
		</div>
	</section>
	</form>
</body>
</html>