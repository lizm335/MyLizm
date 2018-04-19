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
			<c:if test="${empty(param.showCreate)}">
				<li><a href="#">教学管理</a></li>
				<li class="active">专业计划</li>
			</c:if>
			<c:if test="${not empty(param.showCreate)}">
				<li><a href="#">教务管理</a></li>
				<li class="active">新设专业</li>
			</c:if>
			
		</ol>
	</section>
	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html">
			<input type="hidden" name="showCreate" value="${param.showCreate}" />
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">学期</label>
							<div class="col-sm-9">
								<select  name="search_EQ_gjtGrade.gradeId"  class="selectpicker show-tick form-control"	data-size="6" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']||map.key==currentGradeId}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">专业规则号</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_gjtSpecialty.ruleCode" value="${param['search_LIKE_gjtSpecialty.ruleCode']}">
							</div>
						</div>
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">专业</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_gjtSpecialty.zymc" value="${param['search_LIKE_gjtSpecialty.zymc']}">
							</div>
						</div>
					</div>
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">专业层次</label>
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
							<label class="control-label col-sm-3">学科</label>
							<div class="col-sm-9">
								<select id="subject" name="search_EQ_gjtSpecialty.subject" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${subjectMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtSpecialty.subject']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">适用范围</label>
							<div class="col-sm-9">
								<select id="subject" name="search_EQ_gjtStudyCenters.id" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${studyCenterMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtStudyCenters.id']}">selected='selected'</c:if>>${map.value}</option>
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
						<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
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

		<div class="box margin-bottom-none">
			
			<div class="box-body">
				<div class="filter-tabs clearfix">
						<input type="hidden" value="${param['search_EQ_status'] }" name="search_EQ_status"/>
						<ul class="list-unstyled">
							<li <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${set+unset})</li>
							<li status="0" <c:if test="${param['search_EQ_status'] == '0' }">class="actived"</c:if>>待设置(${unset})</li>
							<li status="1" <c:if test="${param['search_EQ_status'] == '1' }">class="actived"</c:if>>已设置(${set})</li>
						</ul>
						<shiro:hasPermission name="/edumanage/teachPlan/list?showCreate=true$setSpecialty">
							<c:if test="${not empty(param.showCreate)}">
								<div class="pull-right no-margin">
									<a href="${ctx}/edumanage/gradespecialty/querySpecialty" role="button" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新设专业</a>
								</div>
							</c:if>
						</shiro:hasPermission>
						<shiro:hasPermission name="/edumanage/teachPlan/list">
							<div class="pull-right no-margin">
								<a href="#" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-download"></i>导出数据</a>
							</div>
						</shiro:hasPermission>
					</div>
						<div class="table-responsive">
							<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
									<tr>
										<th>学期</th>
										<th>专业规则号</th>
										<th>专业</th>
										<th width="25%">专业属性</th>
										<th>最低毕业学分</th>
										<th>课程总数</th>
										<th>专业性质</th>
										<th>状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pageInfo.getContent() }" var="item">
										<tr>
											<td>${item.gjtGrade.gradeName }</td>
											<td>${item.gjtSpecialty.ruleCode}</td>
											<td>${item.gjtSpecialty.gjtSpecialtyBase.specialtyName}</td>
											<td style="text-align: left">
												<b>专业层次：</b>${pyccMap[item.gjtSpecialty.pycc]}<br>
												<b>学科：</b>${subjectMap[item.gjtSpecialty.subject]}<br>
												<b>学科门类：</b>${subjectCategoryMap[item.gjtSpecialty.subject][item.gjtSpecialty.category]}<br>
												<b>适用范围：</b>
											 	<c:if test="${empty(item.gjtStudyCenters)}">通用</c:if>
												<c:forEach items="${item.gjtStudyCenters}" var="range">
													${range.scName}、
												</c:forEach>
											</td>
											<td>${item.gjtSpecialty.zdbyxf}</td>
											<td>${countPlanMap[item.id]}</td>
											<td>
												<c:choose>
													<c:when test="${item.gjtSpecialty.type == 1}">正式专业</c:when>
													<c:when test="${item.gjtSpecialty.type == 2}">体验专业</c:when>
													<c:otherwise>--</c:otherwise>
												</c:choose>
											</td>
											<td>
												<c:if test="${item.status==0}"><font color="#ff6600">待设置</font></c:if>
												<c:if test="${item.status==1}"><font color="#008000">已设置</font></c:if>
											</td>
											<td>
												<div class="data-operion">
													<shiro:hasPermission name="/edumanage/teachPlan/list$setTeachPlan">
														<a href="plan?id=${item.id}&gradeId=${ item.gjtGrade.gradeId}&specialtyId=${item.gjtSpecialty.specialtyId}" class="operion-item" title="设置教学计划" data-tempTitle="设置教学计划">
															<i class="fa fa-gear"></i>
														</a>
													</shiro:hasPermission>
													<shiro:hasPermission name="/edumanage/teachPlan/list$view">
														<c:if test="${item.status==1}">
															<a href="plan?action=view&id=${item.id}&gradeId=${ item.gjtGrade.gradeId}&specialtyId=${item.gjtSpecialty.specialtyId}" class="operion-item" title="查看详情" data-tempTitle="查看详情">
																<i class="fa fa-fw fa-view-more"></i>
															</a>
														</c:if>
													</shiro:hasPermission>
													<%-- <a href="javascript:void(0);" class="operion-item operion-del del-one" val="${item.id}" title="删除" data-tempTitle="删除">
														<i class="fa fa-fw fa-trash-o"></i>
													</a> --%>
												</div>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					<tags:pagination page="${pageInfo}" paginationSize="5" />
				</div>
			</div>
		</div>
	</form>
	</section>
</body>

<script type="text/javascript">
$(function() {
	$('.list-unstyled li').click(function(){
		$('[name="search_EQ_status"]').val($(this).attr('status'));
	    $('#listForm').submit();
	});
	
	$("a[data-role='export']").click(function(event) {
		var data = $("#listForm").serialize();
		window.location.href = '/edumanage/teachPlan/expCoursePlan?'+data;
	});
})
</script>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>