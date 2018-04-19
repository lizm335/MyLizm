<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>报读信息</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
function choice(flag){
	$("#changeCharge").val("");
	$("#changetype").val(flag);
	$("#listForm").submit();
}
function choiceCharge(flag){
	$("#changetype").val("");
	$("#changeCharge").val(flag);
	$("#listForm").submit();
}
</script>

</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">招生管理</a></li>
			<li class="active">报读信息</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<form class="form-horizontal" id="listForm" action="list.html">
			<input id="changetype" type="hidden" name="search_EQ_auditStatus" value="${param.search_EQ_auditStatus}">
			<input id="changeCharge" type="hidden" name="search_EQ_charge" value="${param.search_EQ_charge}">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.xm" value="${param.search_LIKE_gjtStudentInfo.xm}">
								</div>
							</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_gjtStudentInfo.xh" value="${param.search_EQ_gjtStudentInfo.xh}">
								</div>
							</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">身份证</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_gjtStudentInfo.sfzh" value="${param.search_EQ_gjtStudentInfo.sfzh}">
								</div>
							</div>
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
									<select name="search_EQ_signupSpecialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${specialtyMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_signupSpecialtyId}">selected='selected'</c:if>>${map.value}</option>
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
		  <li <c:if test="${param.search_EQ_auditStatus == '0'|| param.search_EQ_auditStatus == null|| param.search_EQ_auditStatus==''}">class="active"</c:if>><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_auditStatus == '0'|| param.search_EQ_auditStatus == null|| param.search_EQ_auditStatus==''}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_auditStatus != '0'&& param.search_EQ_auditStatus != null}">aria-expanded="false"</c:if> onclick="choice('0')">未审核</a></li>
		  <li <c:if test="${param.search_EQ_auditStatus == '1'}">class="active"</c:if>><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_auditStatus == '1'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_auditStatus != '1'}">aria-expanded="false"</c:if> onclick="choice('1')">通过</a></li>
		  <li <c:if test="${param.search_EQ_auditStatus == '2'}">class="active"</c:if>><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_auditStatus == '2'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_auditStatus != '2'}">aria-expanded="false"</c:if> onclick="choice('2')">不通过</a></li>
		  <li <c:if test="${param.search_EQ_auditStatus == '3'}">class="active"</c:if>><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_auditStatus == '3'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_auditStatus != '3'}">aria-expanded="false"</c:if> onclick="choice('3')">取消资格通过</a></li>
		  <li <c:if test="${param.search_EQ_charge == '0'}">class="active"</c:if> ><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_charge == '0'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_charge != '0'}">aria-expanded="false"</c:if> onclick="choiceCharge('0')">未缴费</a></li>
		  <li <c:if test="${param.search_EQ_charge == '1'}">class="active"</c:if> ><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_charge == '1'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_charge != '1'}">aria-expanded="false"</c:if> onclick="choiceCharge('1')">已缴费</a></li>
		
		</ul>
			<div class="fr">
				<a href="${ctx}/signup/exportInfo?sortType=${sortType}&${searchParams}" target="_blank" class="btn btn-success btn-outport">
						<i class="fa fa-fw fa-sign-out"></i> 批量下载学生报读资料</a>
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
									<th>报名时间</th>
									<th>学生来源</th>
									<th>资料审批</th>
									<th>缴费状态</th>
									<th>学习中心</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${infos.getContent() }" var="item">
									<tr>
										<td>
												姓名：${item.gjtStudentInfo.xm}<br/>												
												性别：${item.gjtStudentInfo.xbm==1?'男':'女'}<br/>
												学号：${item.gjtStudentInfo.xh}<br/>
												身份证：${item.gjtStudentInfo.sfzh}
												
										</td>
										<td>
											年级：${item.gjtStudentInfo.gjtSignup.gjtEnrollBatch.gjtGrade.gradeName}<br/>
												层次：${pyccMap[item.gjtStudentInfo.pycc]} <br/>
												专业：${item.zymc}<br/>
										</td>
										<td>
											${item.createdDt}
										</td>
										<td> 
											${item.auditSource}
										</td>
										<td>
											<c:if test="${item.auditStatus==0}">未审核</c:if>	
											<c:if test="${item.auditStatus==1}">通过</c:if>	
											<c:if test="${item.auditStatus==2}">不通过</c:if>	
											<c:if test="${item.auditStatus==3}">取消资格通过</c:if>										
										</td>
										<td> 
											${item.charge==1?'已缴费':'未缴费'}
										 </td>
										<td> 
													${item.xxzxmc}
										 </td>
										
									</tr>
								</c:forEach> 
							</tbody>
						</table>
					</div>
				</div>
				 <tags:pagination page="${infos}" paginationSize="5" /> 
			</div>
		</div>
	</div>
	</form>
	</section>
</body>
</html>