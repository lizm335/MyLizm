<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-开设课程</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
		$(function() {
			var action = $('#action').val();
			if(action == 'view'){
				$(':input').attr("readonly","readonly");
				$(':radio').attr("disabled","disabled");
				$('select').attr("disabled","disabled");
				$('#btn-submit').remove();
			} else {
				//参考： http://bv.doc.javake.cn/examples/
				$('#inputForm').bootstrapValidator({
					excluded: [':disabled'],//验证下拉必需加
					fields: {
						"termId": {
							validators: {
								notEmpty: {
									message: "请选择学期"
								}
							}
						},
						officialEndDtStr: {
							validators: {
								callback: {
									message: '请填写正确的正式学籍注册时间',
									callback: function(value, validator) {
										var flag = true;
										var officialBeginDtStr = validator.getFieldElements('officialBeginDtStr').val();
										if(officialBeginDtStr != '' && value == '') {
											flag = false;
											return flag;
										}
										value = value.replace(/-/g,"/");//替换字符，变成标准格式
										officialBeginDtStr = officialBeginDtStr.replace(/-/g,"/");//替换字符，变成标准格式
										var d2 = new Date(Date.parse(value));
										var d1 = new Date(Date.parse(officialBeginDtStr));
										if(d1>d2){
											flag = false;
											return flag;
										}
										return flag;
									}
								}
							}
						}
					}
				});
			}
		})
	</script>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off" onclick="history.back();">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教务管理</a></li>
		<li><a href="${ctx}/edumanage/openclassCollege/list">开课管理</a></li>
		<li class="active">开设课程</li>
	</ol>
</section>

<section class="content">

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

	<div class="row">
		<div class="col-md-12">
			<div class="box box-primary">
				<div class="box-header with-border">
					<h3 class="box-title">开设课程</h3>
				</div>
				<form id="inputForm" class="form-horizontal" role="form" action="${ctx}/edumanage/openclassCollege/doOpenCourse" method="post">
					<input id="action" type="hidden" name="action" value="${action }"/>
					<input type="hidden" name="courseIds" value=""/>

					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-2 control-label"><small class="text-red">*</small>所属学期</label>
							<div class="col-sm-4">
								<select id="termId" name="termId"
										class="selectpicker show-tick form-control" data-size="8" <c:if test="${action!='create'}">disabled="disabled"</c:if>>
									<c:forEach items="${termMap}" var="map">
										<c:if test="${action=='create'}">
											<option value="${map.key}" 	<c:if test="${map.key==defaultGradeId}">selected='selected'</c:if>>${map.value}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">开设课程</label>
							<div class="col-sm-10">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
										<tr>
											<th><input type="checkbox" class="select-all"></th>
											<th>课程代码</th>
											<th>课程名称</th>
											<th>课程属性</th>
											<th>状态</th>
										</tr>
									</thead>
									<tbody id="courseTbody">
										<tr>
											<td>
												<input type="checkbox" name="ids"
													   data-id="" data-name="check-id"
													   value="">
											</td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>

					<div class="box-footer">
						<div class="col-sm-offset-2 col-sm-10">
							<button id="btn-submit" type="submit" class="btn btn-primary">开设课程</button>
							<button type="reset" class="btn btn-default" onclick="history.back()">返回</button>
						</div>
					</div>
				</form>

			</div>
		</div>
	</div>
</section>

<jsp:include page="/eefileupload/upload.jsp" />

<script type="text/javascript" src="${ctx}/static/js/edumanage/openclass/open_course_form.js"></script>
</body>
</html>