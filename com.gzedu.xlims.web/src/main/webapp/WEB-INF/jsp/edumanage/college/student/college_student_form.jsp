<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-学员管理</title>

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
						sjh: {
							validators: {
								notEmpty: {
									message: "请填写手机号"
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
		<li><a href="${ctx}/edumanage/studentCollege/list">学员管理</a></li>
		<li class="active">编辑信息</li>
	</ol>
</section>

<section class="content">

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

	<div class="row">
		<div class="col-md-12">
			<div class="box box-primary">
				<div class="box-header with-border">
					<h3 class="box-title">编辑信息</h3>
				</div>
				<form id="inputForm" class="form-horizontal" role="form" action="${ctx}/edumanage/studentCollege/${action }" method="post">
					<input id="action" type="hidden" name="action" value="${action }"/>
					<input type="hidden" name="studentId" value="${info.studentId }"/>
					<input id="zpUrl" type="hidden" name="zp" value="" />

					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">姓名:</label>
							<div class="col-sm-5">
								${info.xm}
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">身份证号:</label>
							<div class="col-sm-5">
								
								<shiro:hasPermission name="/personal/index$privacyJurisdiction">
										${info.sfzh}
									</shiro:hasPermission>
									<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
										<c:if test="${not empty info.sfzh }">
										${fn:substring(info.sfzh,0, 4)}******${fn:substring(info.sfzh,14, (info.sfzh).length())}
										</c:if>
									</shiro:lacksPermission>
								
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">学号:</label>
							<div class="col-sm-5">
								${info.xh}
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">报读专业:</label>
							<div class="col-sm-5">
								${info.gjtSpecialtyCollege.name}
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">专业层次:</label>
							<div class="col-sm-5">
								<dic:getLabel typeCode="TrainingLevel" code="${info.pycc }" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">入学年级:</label>
							<div class="col-sm-5">
								${info.gjtGrade.gjtYear.name}
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">入学学期:</label>
							<div class="col-sm-5">
								${info.gjtGrade.gradeName}
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">教务班级:</label>
							<div class="col-sm-5">
								${info.userclass}
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top">所属院校:</label>
							<div class="col-sm-5">
								${info.gjtSchoolInfo.xxmc}
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label"><small class="text-red">*</small>照片:</label>
							<div class="col-sm-2">
								<div class="cert-wrap">
									<button id="uploadBtn" type="button" class="btn btn-info" onclick="javascript:uploadImage('zpId','zpUrl',null,null,uploadCallback);"
											style="margin-bottom: 10px;"><i class="fa fa-fw fa-upload"></i> 上传照片</button>
									<div class="cert-box has-upload cert-box-3" style="width:160px;">
										<a href="javascript:void(0);" class="info-img-box">
											<img id="zpId" class="info-img" src="${signupCopyData['zp'] }" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
										</a>
										<c:if test="${not empty signupCopyData['zp'] }">
											<a href="#" data-role="lightbox" data-large-img="${signupCopyData['zp'] }" class="light-box">点击放大</a>
										</c:if>
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label"><small class="text-red">*</small>手机号:</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="sjh" value="${info.sjh}">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label">邮箱:</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" name="dzxx" value="${info.dzxx}">
							</div>
						</div>
					</div>

					<div class="box-footer">
						<div class="col-sm-offset-2 col-sm-10">
							<button id="btn-submit" type="submit" class="btn btn-primary">保存</button>
							<button type="reset" class="btn btn-default" onclick="history.back()">返回</button>
						</div>
					</div>
				</form>

			</div>
		</div>
	</div>
</section>

<jsp:include page="/eefileupload/upload.jsp" />

<script type="text/javascript">
	$(function() {

		init();

	});


	function init() {
		/*********'图片放大'**********/
		//加载脚本
		$.getScript(css + '/ouchgzee_com/platform/xllms_css/plugins/custom-model/custom-model.js?t=123465', function () {
			$('[data-role="lightbox"]').click(function (event) {
				event.preventDefault();
				var self = this;
				var imgURL = $(self).attr('data-large-img');
				var h = 600;

				var t = [
					'<table width="100%" height="' + h + '">',
					'<tr>',
					'<td class="text-center" valign="middle">',
					'<span class="inline-block position-relative bg-white overlay-wrapper">',
					'<div class="overlay"><i class="fa fa-refresh fa-spin" style="color:#fff;"></i></div>',
					'</span>',
					'</td>',
					'</tr>',
					'</table>'
				].join('');

				var pop = $.mydialog({
					id: 'fancybox',
					width: 600,
					height: h,
					zIndex: 11000,
					content: t,
					//backdrop:false,
					//fade:false,
					showCloseIco: false,
					modalCss: 'my-modal',
					onLoaded: function () {
						var $img = $('<img src="' + imgURL + '">');
						$img.on('load', function (event) {
							pop.find('span.overlay-wrapper').html('<img src="' + imgURL + '" style="max-width:100%;max-height:' + h + 'px;"><i data-dismiss="modal" class="fa fa-fw fa-times-circle"></i>');
						});
					}
				});

			});
		});
	}

	function uploadCallback() {
		var zpUrl = $("#zpUrl").val();
		var $ligntbox = $('#uploadBtn + div a[data-role="lightbox"]');
		if($ligntbox[0] == undefined) {
			$('#uploadBtn + div').append('<a href="#" data-role="lightbox" data-large-img="'+zpUrl+'" class="light-box">点击放大</a>');
			init();
		} else {
			$ligntbox.attr('data-large-img', zpUrl);
		}
	}
</script>
</body>
</html>