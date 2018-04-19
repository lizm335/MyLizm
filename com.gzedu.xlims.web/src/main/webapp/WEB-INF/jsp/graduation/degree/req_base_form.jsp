<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-毕业管理</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

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
					baseType: {
						validators: {
							notEmpty: {
								message: "请填写类型"
							},
							integer: {
								message: '请填写整数'
							}
						}
					},
					baseName: {
						validators: {
							notEmpty: {
								message: "请填写名称"
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
		<li><a href="#">学位基础条件管理</a></li>
		<li class="active">新增学位基础条件</li>
	</ol>
</section>

	<section class="content">
		<div class="box no-margin">
			<form id="inputForm" class="form-horizontal" role="form" action="${ctx}/graduation/degreeReqBase/${action }" method="post">
				<input id="action" type="hidden" name="action" value="${action }"/>
				<input type="hidden" name="baseId" value="${info.baseId }"/>

				<div class="box-body pad20">
					<div class="form-horizontal reset-form-horizontal">
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>类型:</label>
							<div class="col-sm-6">
								<select name="baseType" <c:if test="${action=='update'}">disabled="disabled"</c:if> 
									class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
									<c:forEach items="${baseTypeMap}" var="m">
											<option value="${m.key}" <c:if test="${m.key==info.baseType}">selected='selected'</c:if>>${m.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
	
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>名称:</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="baseName" value="${info.baseName}">
							</div>
						</div>
	
						<div class="form-group">
							<label class="col-sm-3 control-label">描述:</label>
							<div class="col-sm-6">
								<textarea class="form-control" name="baseDesc" rows="3">${info.baseDesc}</textarea>
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

	<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>