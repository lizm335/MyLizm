<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>管理系统 -选择学员</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<style type="text/css">
html, body {
	height: 100%;
	overflow: hidden;
}
</style>

</head>
<body>
	<form id="listForm" action="${ctx}/admin/messageInfo/searchStudentPage" method="post">
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">已选择学员</h3>
			</div>
			<div>
				<div class="border-bottom pad-t15 clearfix" data-id="form">
					<div class="form-horizontal">
						<%-- <div class="col-xs-4">
							<div class="form-group">
								<label class="control-label col-xs-3 text-nowrap">层次</label>
								<div class="col-xs-9">
									<select class="form-control select2" name="search_EQ_pycc" data-placeholder="全部层次" style="width: 100%;">
										<option value="">全部</option>
										<c:forEach items="${pyccMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label col-xs-3 text-nowrap">学期</label>
								<div class="col-xs-9">
									<select class="form-control select2" name="search_EQ_gradeId" data-placeholder="全部学期" style="width: 100%;">
										<option value="">全部</option>
										<c:forEach items="${gradeMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_gradeId}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label col-xs-3 text-nowrap">专业</label>
								<div class="col-xs-9">
									<select class="form-control select2" name="search_EQ_specialtyId" data-placeholder="全部专业" style="width: 100%;">
										<option value="">全部</option>
										<c:forEach items="${specialtyMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_specialtyId}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label col-xs-3 text-nowrap">学籍</label>
								<div class="col-xs-9">
									<select class="form-control select2" name="search_EQ_xjzt" style="width: 100%;">
										<option value=''>全部</option>
										<c:forEach items="${xjztMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_xjzt}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div> 
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label col-xs-3 text-nowrap">学习中心</label>
								<div class="col-xs-9">
									<select class="form-control select2" name="search_EQ_orgId" data-placeholder="全部学习中心" style="width: 100%;">
										<option value="">全部</option>
										<c:forEach items="${orgMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_orgId}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div> 
						--%>
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label col-xs-3 text-nowrap">姓名</label>
								<div class="col-xs-9">
									<input class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm }">
								</div>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label col-xs-3 text-nowrap">学号</label>
								<div class="col-xs-9">
									<input class="form-control" name="search_EQ_xh" value="${param.search_EQ_xh }">
								</div>
							</div>
						</div>
						<div class="col-xs-4">
							<div class="form-group">
								<label class="control-label col-xs-3 text-nowrap"></label>
								<div class="col-xs-9">
									<button type="submit" id="btnSubmit" class="btn btn-primary min-width-90px">搜索</button>
								</div>
							</div>
						</div>
						<!-- <div class="col-xs-8 text-right">
							<button type="submit" class="btn btn-primary min-width-90px">搜索</button>
						</div> -->
					</div>
				</div>
				<div class="box-body pad-l15 pad-r15">
					<!-- <div class="pad-t5 pad-b10 text-right" data-id="batch-box">
							<button type="button" class="btn btn-sm btn-default min-width-90px" data-role="batch-del">批量删除</button>
					</div> -->
					<div class="scroll-box" data-id="box">
						<table class="table-gray-th text-center table-font">
							<thead>
								<tr>
									<th width="80">头像</th>
									<th width="60">个人信息</th>
									<th width="38%">报读信息</th>
									<th width="100">所属学习中心</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.content }" var="student">
											<tr>
												<td><c:if test="${empty student.AVATAR }">
														<img src="${css }/ouchgzee_com/platform/xlbzr_css/dist/img/images/no-img.png" class="img-circle" width="55" height="55">
													</c:if> <c:if test="${not empty student.AVATAR }">
														<img src="${student.AVATAR }" class="img-circle" width="55" height="55">
													</c:if></td>
												<td>
													<ul class="list-unstyled text-left">
														<li>姓名：${student.XM }</li>
														<li>学号：${student.XH }</li>
														<li>手机：${student.SJH }</li>
														<li>学籍：${xjztMap[student.XJZT] }</li>
													</ul>
												</td>
												<td>
													<ul class="list-unstyled text-left">
														<li>层次：${pyccMap[student.PYCC] }</li>
														<li>学期：${student.GRADE_NAME }</li>
														<li>专业：${student.ZYMC }</li>
													</ul>
												</td>
												<td>${student.ORG_NAME }</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="4">请重新筛选条件查询！</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<c:if test="${not empty pageInfo.content }">
							<div style="padding:0 10px 10px;" class="text-center">
								<div class="inline-block">
									<tags:pagination page="${pageInfo}" paginationSize="10" />
								</div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		<div class="text-right pop-btn-box pad clearfix">
			<button type="button" class="btn btn-primary min-width-90px" data-role="close-pop">确定</button>
		</div>
		<input type="hidden" name="gradeIds"><input type="hidden" name="gradeIdAll"> 
		<input type="hidden" name="pyccIds"><input type="hidden" name="pyccIdAll"> 
		<input type="hidden" name="specialtyIds"><input type="hidden" name="specialtyIdAll"> 
		<input	type="hidden" name="courseIds"><input type="hidden" name="courseIdAll"> 
		<input type="hidden" name="userTypes"><input type="hidden" name="userTypeAll"> 
		<input type="hidden" name="xjztTypes"><input type="hidden" name="xjztTypeAll">
	</form>
	<script type="text/javascript">
	//关闭 弹窗
	$("[data-role='close-pop']").click(function(event) {
		parent.$.closeDialog(frameElement.api)
	});
	$('.select2').select2();
	//设置内容主体高度
	$('.scroll-box').height($(frameElement).height() - 106-$('[data-id="form"]').outerHeight());
	
	
	//这么写是获取父页面的值，然后用post提交，get参数又限制
	$('[name="gradeIds"]').val(frameElement.data.gradeIds);
	$('[name="gradeIdAll"]').val(frameElement.data.gradeIdAll);
	$('[name="pyccIds"]').val(frameElement.data.pyccIds);
	$('[name="specialtyIds"]').val(frameElement.data.specialtyIds);
	$('[name="specialtyIdAll"]').val(frameElement.data.specialtyIdAll);
	$('[name="courseIds"]').val(frameElement.data.courseIds);
	$('[name="courseIdAll"]').val(frameElement.data.courseIdAll);
	$('[name="userTypes"]').val(frameElement.data.userTypes);
	$('[name="userTypeAll"]').val(frameElement.data.userTypeAll);
	$('[name="xjztTypes"]').val(frameElement.data.xjztTypes);
	$('[name="xjztTypeAll"]').val(frameElement.data.xjztTypeAll);
	
	//判断是否要转发
	var temp="${isTranse}";
	if(temp=='Y'){
		$('#btnSubmit').click();
	}
	
	
</script>
</body>
</html>
