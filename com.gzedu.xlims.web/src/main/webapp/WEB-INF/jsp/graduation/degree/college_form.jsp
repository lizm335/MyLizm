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
			$('#btn-submit,#uploadId').remove();
		}

		//参考： http://bv.doc.javake.cn/examples/
		$('#inputForm').bootstrapValidator({
				excluded: [':disabled'],//验证下拉必需加
				fields: {
					collegeName: {
						validators: {
							notEmpty: {
								message: "请填写院校名称"
							}
						}
					},
					cover: {
						validators: {
							notEmpty: {
								message: "请添加封面"
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
		<li><a href="#">学位院校管理</a></li>
		<li class="active">新增学位院校</li>
	</ol>
</section>

	<section class="content">
		<div class="box no-margin">
			<form id="inputForm" class="form-horizontal" role="form" action="${ctx}/graduation/degreeCollege/${action }" method="post">
				<input id="action" type="hidden" name="action" value="${action }"/>
				<input type="hidden" name="collegeId" value="${info.collegeId }"/>

				<div class="box-body pad20">
					<div class="form-horizontal reset-form-horizontal">
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>院校名称:</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="collegeName" value="${info.collegeName}">
							</div>
						</div>
	
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>封面:</label>
							<div class="col-sm-6">
								<div class="position-relative" data-role="valid" data-id="valid-img">
								<input type="button" id="uploadId" value="添加封面" onclick="uploadImage('imgId','imgPath',null,5,uploadCallback);" />
								<div class="inline-block vertical-middle">
									<ul class="img-list clearfix">
										<li>
											<img id="imgId" src="${info.cover }" class="user-image" style="cursor: pointer;" >
											<input id="imgPath" type="hidden" value="${info.cover }" name="cover">
											<!-- <button type="button" class="btn flat btn-default btn-sm btn-block">删除</button> -->
										</li>
									</ul>
								</div>
							</div>
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
    <jsp:include page="/eefileupload/upload.jsp"/>
	<script type="text/javascript">
		var uploadCallback=function(){
		    $('#inputForm').bootstrapValidator('updateStatus', 'cover', 'NOT_VALIDATED').bootstrapValidator('validateField', 'cover');
		}
		var postIngIframe;
		$(function(){
		    $("#inputForm").ajaxForm({
				dataType:'json',
				beforeSubmit:function(){
				    var flag = $('#inputForm').data('bootstrapValidator').isValid();
				    if(!flag){
						return false;
					}
				   postIngIframe = $.mydialog({
						id : 'dialog-1',
						width : 150,
						height : 50,
						backdrop : false,
						fade : false,
						showCloseIco : false,
						zIndex : 11000,
						content : '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
				    });
				},
				success:function(data){
				    if(data.successful){
						location.href='${ctx}/graduation/degreeCollege/list';
					}else{
					    $.closeDialog(postIngIframe);
					    alert(data.message);
					}
				}
			});
		});
		 
	</script>

</body>
</html>