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
<title>学籍异动-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script>
function choiceYD(flag){
	$("#changetype").val(flag);
	$("#listForm").submit();
}
</script>
</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">教务管理</a></li>
			<li class="active">学籍异动</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<form class="form-horizontal" id="listForm" action="list.html">
			<input id="changetype" type="hidden" name="search_EQ_changetype" value="${param.search_EQ_changetype}">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
					<div class="col-md-4">
							<label class="control-label col-sm-3">姓名</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
							</div>
					</div>
					<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_gjtStudentInfo.xh" 	value="${param['search_EQ_gjtStudentInfo.xh']}">
								</div>
						</div>
						<div class="col-md-4">
							<label class="control-label col-sm-3">身份证</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.sfzh" value="${param['search_LIKE_gjtStudentInfo.sfzh']}">
							</div>
					</div>					
					<!-- <div class="col-md-4">
							<label class="control-label col-sm-3">异动类型：</label>
							<div class="col-sm-9">
								<select name="search_EQ_changetype" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<option value="101"<c:if test="${param.search_EQ_changetype==101 }">selected='selected'</c:if>>转专业</option>
									<option value="102"<c:if test="${param.search_EQ_changetype==102 }">selected='selected'</c:if>>转学习中心</option>
									<option value="103"<c:if test="${param.search_EQ_changetype==103 }">selected='selected'</c:if>>转年级</option>
								</select>
							</div>
						</div> -->	
					</div>
					
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
								<label class="control-label col-sm-3">年级</label>
								<div class="col-sm-9">
									<select  name="search_EQ_gjtStudentInfo.gjtSignup.gjtEnrollBatch.gjtGrade.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${gradeMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_gjtStudentInfo.gjtSignup.gjtEnrollBatch.gjtGrade}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="col-md-4">
									<label class="control-label col-sm-3">层次</label>
									<div class="col-sm-9">
										<select name ="search_EQ_gjtStudentInfo.pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_gjtStudentInfo.pycc}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">专业名称</label>
								<div class="col-sm-9">
									<select name="search_EQ_nowGjtSpecialty.specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${specialtyMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_nowGjtSpecialty}">selected='selected'</c:if>>${map.value}</option>
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
		<ul class="nav nav-tabs">		  	 
		  <li <c:if test="${param.search_EQ_changetype == '101'|| param.search_EQ_changetype == null|| param.search_EQ_changetype == ''}">class="active"</c:if> value="3"><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_changetype == '101'|| param.search_EQ_changetype == null|| param.search_EQ_changetype == ''}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_changetype != '101'&& param.search_EQ_changetype != null}">aria-expanded="false"</c:if> onclick="choiceYD('101')">转专业</a></li>
		  <li <c:if test="${param.search_EQ_changetype == '102'}">class="active"</c:if> value="5"><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_changetype == '102'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_changetype != '102'}">aria-expanded="false"</c:if> onclick="choiceYD('102')">转学习中心</a></li>
		  <li <c:if test="${param.search_EQ_changetype == '103'}">class="active"</c:if> value="4"><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_changetype == '103'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_changetype != '103'}">aria-expanded="false"</c:if> onclick="choiceYD('103')">转年级</a></li>
		 <!--  <li <c:if test="${param.search_EQ_changetype == '3'}">class="active"</c:if> value="7"><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_changetype == '3'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_changetype != '3'}">aria-expanded="false"</c:if> onclick="choiceYD('3')">退学</a></li>
		  <li <c:if test="${param.search_EQ_changetype == '4'}">class="active"</c:if> value="7"><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_changetype == '4'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_changetype != '4'}">aria-expanded="false"</c:if> onclick="choiceYD('4')">转专业</a></li>
		  <li <c:if test="${param.search_EQ_changetype == '5'}">class="active"</c:if> value="7"><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_changetype == '5'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_changetype != '5'}">aria-expanded="false"</c:if> onclick="choiceYD('5')">开除学籍</a></li>
		  <li <c:if test="${param.search_EQ_changetype == '6'}">class="active"</c:if> value="7"><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_changetype == '6'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_changetype != '6'}">aria-expanded="false"</c:if> onclick="choiceYD('6')">更改层次</a></li>
		 -->
		</ul>
			<div class="fr">
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
									<th>个人信息</th>
									<th>报读信息</th>
									<th>异动类型</th>
									<th>申请详情</th>
									<th>异动原因</th>
									<th>审批状态</th>
									<th>申请时间</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageInfo.getContent() }" var="item">
									<tr>
										<td>
												姓名：${item.gjtStudentInfo.xm}<br/>												
												性别：${item.gjtStudentInfo.xbm==1?'男':'女'}<br/>
												学号：${item.gjtStudentInfo.xh}<br/>
												<shiro:hasPermission name="/personal/index$privacyJurisdiction">
												身份证：${item.gjtStudentInfo.sfzh}
												</shiro:hasPermission>
										</td>
										<td>年级：${item.gjtStudentInfo.gjtSignup.gjtEnrollBatch.gjtGrade.gradeName}<br/>
												层次：${pyccMap[item.gjtStudentInfo.pycc]} <br/>
												专业：${item.nowGjtSpecialty.zymc}<br/>
										</td>												
										<td>
											${changeTypeMap[item.changetype]}
										</td>
										<td> <c:if test="${item.changetype==101 }">
												“${item.oldGjtSpecialty.zymc}”转“${item.nowGjtSpecialty.zymc}”
												</c:if>  
												<c:if test="${item.changetype!=101 }">
												由“${item.bName}”转“${item.aName}”
												</c:if>
												</td>
										<td>
												<c:if test="${fn:length(item.remark)>16}">
														<a style="color: black " title="${item.remark}">${fn:substring(item.remark,0,16)}...</a>
													</c:if>    
													<c:if test="${fn:length(item.remark)<17}">
														${item.remark}
													</c:if>
										</td>
										<td> 
													待审批
										 </td>
										<td> 
													${item.createdDt}
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