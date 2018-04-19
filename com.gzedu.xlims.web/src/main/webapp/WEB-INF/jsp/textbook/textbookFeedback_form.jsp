<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
})
</script> 

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学员反馈</a></li>
		<li class="active">新增反馈</li>
	</ol>
</section>
<section class="content">
	<div class="box no-margin">
		<div class="box-body pad-t15">
		<form id="inputForm" class="form-horizontal" role="form" action="${ctx}/textbookFeedback/${action}" method="post">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">姓名</label>
					<div class="col-sm-7">
						<input type="text"  name="gjtStudentInfo.xm" class="form-control" placeholder="姓名" value="${entity.gjtStudentInfo.xm}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>学号</label>
					<div class="col-sm-7">
						<input type="text" id="studentCode" name="gjtStudentInfo.xh" class="form-control" placeholder="学号" value="${entity.gjtStudentInfo.xh}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-pad-top"><small class="text-red">*</small>反馈类型</label>
					<div class="col-sm-7">
						<div class="full-width">
							<label class="text-no-bold">
							  <input type="radio" name="feedbackType" class="minimal" value="1" checked>
							  退换教材
							</label>
							<label class="text-no-bold left10">
							  <input type="radio" name="feedbackType" class="minimal" value="2">
							  补发教材
							</label>
							<label class="text-no-bold left10">
							  <input type="radio" name="feedbackType" class="minimal" value="3">
							  其它
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>教材名称</label>
					<div class="col-sm-7">
						<div class="pull-right margin_l10">
							<button type="button" class="btn btn-default" data-role="select-pop">选择教材</button>
						</div>
						<div class="select2-container select2-container--default show oh">
							<div class="select2-selection--multiple">
								<ul class="select2-selection__rendered select-container-ul">
								</ul>			
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>反馈原因</label>
					<div class="col-sm-7">
						<textarea id="reason" name="reason" class="form-control" rows="5" placeholder="请说明申请售后服务的具体原因，如物流损坏、漏发等。"></textarea>
						<p class="text-red margin_t5 f12">注：申请后1-2个工作日内工作人员会与你联系，请保持手机畅通</p>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-7 col-sm-offset-2">
						<button type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit" data-role="btn-save-edit">保存</button>
						<button class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
					</div>
				</div>
			</div>
		</form>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$(function(){
	//选择教材
	$("[data-role='select-pop']").click(function(event) {
		var studentCode = $("#studentCode").val();
	    if (studentCode == "") {
		   alert("请先填写学号！");
		   return false;
	    }
		   
		var $dialog=$.mydialog({
		  id:'import',
		  width:'90%',
		  height:540,
		  zIndex:11000,
		  content: 'url:selectTextbook?studentCode='+studentCode
		});
	});

	//删除教材
	$(".select-container-ul").on('click', '.select2-selection__choice__remove', function(event) {
		event.preventDefault();
		$("#"+$(this).attr("aria-describedby")).remove();
		$(this).parent().remove();
	});
	
	$("[data-role='btn-save-edit']").click(function(event) {
		var studentCode = $("#studentCode").val();
	    if (studentCode == "") {
		   alert("请先填写学号！");
		   return false;
	    }
		   
	    var textbookIds = $(".select-container-ul").find("input[name='textbookIds']");
		if (textbookIds.length == 0) {
			alert("请选择教材！");
			return false;
		}
		var reason = $("#reason").val();
		if (reason == "") {
			alert("请填写原因！");
			return false;
		}
		
		$("#inputForm").submit();
	});
	
})
</script>
</body>
</html>
