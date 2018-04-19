<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-毕业管理</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<!-- iCheck 1.0.1 -->
	<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
	<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>

	<script type="text/javascript">
	$(function() {
		if($('#action').val() == 'view'){
			$(':input').attr("readonly","readonly");
			$(':radio').attr("disabled","disabled");
			$('select').attr("disabled","disabled");
			$('#btn-submit').remove();
		}

		//参考： http://bv.doc.javake.cn/examples/
		$('#inputForm').bootstrapValidator({
				excluded: [':disabled'],//验证下拉必需加
				fields: {
					documentType: {
						validators: {
							notEmpty: {
								message: "请选择文档类型"
							}
						}
					},
					specialtyId: {
						validators: {
							notEmpty: {
								message: "请选择专业"
							}
						}
					},
					documentName: {
						validators: {
							notEmpty: {
								message: "请填写文档名称"
							}
						}
					}
				}
			});
	})
	</script>
</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">毕业管理</a></li>
		<li><a href="#">示例文档管理</a></li>
		<li class="active">新增示例文档</li>
	</ol>
</section>

	<section class="content">
		<div class="box no-margin">
			<form id="inputForm" class="form-horizontal" role="form" action="${ctx}/graduation/sampleDoc/${action }" method="post">
				<input id="action" type="hidden" name="action" value="${action }"/>
				<input type="hidden" name="documentId" value="${info.documentId }"/>
				<input id="documentFileName" type="hidden" value=""/>
				<input id="documentFilePath" type="hidden" name ="documentUrl" value=""/>

				<div class="box-body pad20">
					<div class="form-horizontal reset-form-horizontal">
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>文档类型:</label>
							<div class="col-sm-6">
								<select name="documentType" class="selectpicker show-tick form-control">
									<c:forEach items="${documentTypes}" var="map">
										<option value="${map.code}"  <c:if test="${map.code==info.documentType}">selected='selected'</c:if>>${map.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
	
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>所属专业:</label>
							<div class="col-sm-6">
								<select name="gjtSpecialty.specialtyId"
										class="selectpicker show-tick form-control" data-size="5" data-live-search="true" <c:if test="${action!='create'}">disabled="disabled"</c:if>>
									<option value="">请选择</option>
									<c:forEach items="${specialtyMap}" var="map">
										<c:if test="${action=='create'}">
											<option value="${map.key}">${map.value}</option>
										</c:if>
										<c:if test="${action!='create'}">
											<option value="${map.key}" 	<c:if test="${map.key==info.gjtSpecialty.specialtyId}">selected='selected'</c:if>>${map.value}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
						</div>
	
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>文档名称:</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="documentName" value="${info.documentName}">
							</div>
						</div>
	
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>文档文件:</label>
							<div class="col-sm-6">
								<button id="uploadBtn" type="button" class="btn btn-default" onclick="uploadFile('documentFileName','documentFilePath','doc|docx',null,uploadCallback)"><i class="fa fa-fw fa-upload"></i> 上传文件</button>
							</div>
						</div>
					</div>
	
					<div class="row">
						<div class="col-sm-6 col-sm-offset-3">
							<button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
							<button type="reset" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
						</div>
					</div>
				</div>
			</form>

		</div>
	</section>

	<jsp:include page="/eefileupload/upload.jsp" />
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		$(function () {

		});
		function uploadCallback() {
			var fileName = $("#documentFileName").val();
			$("#uploadBtn").parent().html('<div class="alert alert-success"><h4><i class="icon fa fa-check"></i> '+fileName+'</h4></div>');
		}
	</script>
</body>
</html>