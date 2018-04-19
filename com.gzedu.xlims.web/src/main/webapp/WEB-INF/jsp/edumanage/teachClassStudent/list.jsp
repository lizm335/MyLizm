<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>查看班级学员</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb oh">
			<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">教务管理</a></li>
			<li><a href="#">教务班级学员</a></li>
		</ol>
	</section>

	
		
		<section class="content">
			<form class="form-horizontal" id="listForm" action="list.html">
			<input type="hidden" name="action" value="${action}"/> 
			<div class="nav-tabs-custom no-margin">
				<div class="tab-content">
					<div class="tab-pane active" id="tab_top_1">
							<div class="box box-border">
								<div class="box-body">
									<div class="row reset-form-horizontal clearbox">
										<div class="col-md-4">
											<label class="control-label col-sm-3">学号</label>
											<div class="col-sm-9">
												<input type="text" class="form-control" name="search_XH"
													value="${param['search_XH']}"> 
											</div>
										</div>
										<div class="col-md-4">
											<label class="control-label col-sm-3">姓名</label>
											<div class="col-sm-9">
												<input type="text" class="form-control"
													name="search_XM"
													value="${param['search_XM']}">
											</div>
										</div>
										<div class="col-md-4">
											<label class="control-label col-sm-3">所在单位</label>
											<div class="col-sm-9">
												<input type="text" class="form-control"
													name="search_scCo"
													value="${param['search_scCo']}">
											</div>
										</div>
									</div>
									<div class="row reset-form-horizontal clearbox">
										<div class="col-md-4">
											<label class="control-label col-sm-3">层次</label>
											<div class="col-sm-9">
												<select name="search_PYCC"
													class="selectpicker show-tick form-control" data-size="5"
													data-live-search="true">
													<option value="">请选择</option>
													<c:forEach items="${pyccMap}" var="map">
														<option value="${map.key}"
															<c:if test="${map.key==param['search_PYCC']}">selected='selected'</c:if>>${map.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="col-md-4">
											<label class="control-label col-sm-3">专业</label>
											<div class="col-sm-9">
												<select
													id="specialtyId"
													name="search_MAJOR"
													class="selectpicker show-tick form-control" data-size="5"
													data-live-search="true">
													<option value="">请选择</option>
													<c:forEach items="${specialtyMap}" var="map">
														<option value="${map.key}"
															<c:if test="${map.key==param['search_MAJOR']}">selected='selected'</c:if>>${map.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
											<div class="col-md-4">
											<label class="control-label col-sm-3">学习中心</label>
											<div class="col-sm-9">
												<select
													id="orgId"
													name="search_ID"
													class="selectpicker show-tick form-control" data-size="10"
													data-live-search="true">
													<option value="">请选择</option>
													<c:forEach items="${studyCenterMap}" var="map">
														<option value="${map.key}"
															<c:if test="${map.key==param['search_ID']}">selected='selected'</c:if>>${map.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
										
									</div>
									
								</div>
								<div class="box-footer text-right">
									<button type="submit"
										class="btn min-width-90px btn-primary margin_r15">搜索</button>
									<button type="button"
										class="btn min-width-90px btn-default btn-reset">重置</button>
								</div>
							</div>
							<div class="box box-border margin-bottom-none">
								<div class="box-header with-border">
									<h3 class="box-title pad-t5">学员列表</h3>
										 <div class="pull-right no-margin">
									<!-- 
									<shiro:hasPermission name="/edumanage/teachclass/list$batchChangeClass">
									<div class="pull-right">
										<a href="${ctx }/edumanage/classstudent/moveTeachClass/${classId}" role="button" class="btn btn-default btn-sm"data-role="batch">批量调班</a>
									</div>
									</shiro:hasPermission>
									 -->
									 
									 <shiro:hasPermission name="/edumanage/teachClassStudent/list$export"> 
				                   		<a class="btn btn-default btn-sm margin_r10"  data-role="export" target= "_blank" >
				                   	 	 <i class="fa fa-fw fa-sign-in"></i> 导出教务班级学员列表</a>
				                   	 </shiro:hasPermission> 
				                   	 
									 <shiro:hasPermission name="/edumanage/teachClassStudent/list$importStuClassInfo">
									  <a class="btn btn-default btn-sm margin_l10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 批量导入调班信息</a>
									 </shiro:hasPermission>
									 </div>
								</div>
								<div class="box-body">
									<div class="table-responsive">
										<table id="list_table"
											class="table table-bordered table-striped vertical-mid text-center table-font">
											<thead>
												<tr>
													<!-- <th><input type="checkbox" class="select-all"	id="selectAll"></th> -->
													<th>个人信息</th>
													<th>报读信息</th>
													<th>班级</th>
													<th>班主任</th>
													<th>所在单位</th>
													<th>学习中心</th>
													<!-- <th>操作</th> -->
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${pageInfo.getContent() }" var="item">
													<tr>
														<%-- <td><input type="checkbox" class="studentId"	value="${item.STUDENT_ID }"></td> --%>
														<td>
															<div class="text-left">
																姓名：${item.XM } <br>
																学号：${item.XH }<br>
																<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																手机：${item.SJH }<br>
																身份证：${item.SFZH }
																</shiro:hasPermission>
															</div>
														</td>
														<td style="text-align:left">
															层次：${pyccMap[item.PYCC]}<br>
															学期：${item.GRADE_NAME}<br>
															专业：${specialtyMap[item.MAJOR]}
														</td>
														<td>${item.BJMC}</td>
														<td>${item.TEACH_NAME}</td>
														<td>${item.SC_CO}</td>
														<td>${item.ORG_NAME}</td>
														<!-- 操作功能暂时取消 -->
														<!--  
														<td>
															<shiro:hasPermission name="/edumanage/teachclass/list$changeClass">
															<a href="${ctx }/edumanage/classstudent/moveClassByOne/${classId}/${item.gjtStudentInfo.studentId }" 	
																class="operion-item" title="调班" data-role="class-change">
																<i class="fa fa-fw fa-xjyd"></i>
															</a>
															</shiro:hasPermission>
															<shiro:hasPermission name="/edumanage/teachclass/list$simulation">
															 <a href="${ctx}/edumanage/roll/simulation?id=${item.gjtStudentInfo.studentId }" target="${item.gjtStudentInfo.studentId }"
																class="operion-item" data-toggle="tooltip" title="模拟登录">
																<i	class="fa fa-fw fa-simulated-login"></i>
															</a>
															</shiro:hasPermission>
															<shiro:hasPermission name="/edumanage/teachclass/list$analogLogin">
																<a href="${ctx}/edumanage/classstudent/analogLogin?studentId=${item.gjtStudentInfo.studentId}" class="operion-item operion-login" data-toggle="tooltip" title="模拟登录个人中心" target="_blank" data-role="data-analogLogin"><i class="fa fa-fw fa-simulated-login"></i></a>
															</shiro:hasPermission>
															</td>
															-->
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
									<tags:pagination page="${pageInfo}" paginationSize="5" />
								</div>
							</div>

					</div>
				</div>
			</div>
			</form>
		</section>
	
	
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
<script type="text/javascript">

//导出
$('[data-role="export"]').click(function(event) {
	var specialtyId=$('#specialtyId').val();
	var orgId=$('#orgId').val();
	var url="${ctx}/edumanage/teachClassStudent/export?specialtyId="+specialtyId+'&orgId='+orgId
	$(this).attr('href',url);
});

//批量调班
$("[data-role='batch']").click(function(event) {
	event.preventDefault();
	var ids =[];
	$('.studentId:checked').each(function(){
		ids.push($(this).val());
	});
	if(ids.length==0){
		alert('至少要选择一个学生');
		return;
	}
	$.mydialog({
	  id:'batch',
	  width:600,
	  height:326,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')+"?studentIds="+ids.join(',')
	});
});
//单个调班
//调班
$("[data-role='class-change']").click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'batch',
	  width:540,
	  height:366,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});
$(function(){
	 $("[data-role='import']").click(function(event) {
         $.mydialog({
             id:'import',
             width:600,
             height:415,
             zIndex:11000,
             content: 'url:toImport'
         });
     });	
});
</script>
</html>