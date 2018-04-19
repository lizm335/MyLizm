<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body>

<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">提示</h3>
	</div>
	<div class="box-body" style="height:194px;">
		<div class="pad-l20 pad-r20 pad-t10">
			<p>请填写审核不通过原因：</p>
			<div>
				<textarea class="form-control" placeholder="审核不通过原因" data-role="semester-name" rows="8"></textarea>
				<div class="error-tips text-red hide-block">
					<i class="fa fa-fw fa-exclamation-circle"></i>
					请填写原因
				</div>
			</div>
		</div>
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">审核不通过</button>
</div>

<script type="text/javascript">

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//输入框
$('[data-role="semester-name"]').keyup(function(event) {
	if($.trim($(this).val())!=""){
		$(this).removeClass('Validform_error').siblings('.error-tips').hide();
	}
	else{
		$(this).addClass('Validform_error').siblings('.error-tips').show();
	}
});

var id = '${param.orderId}';
//确认
$("button[data-role='sure']").click(function(event) {
	var $semesterName=$('[data-role="semester-name"]');
	var i=0;

	if( $.trim($semesterName.val())=="" ){
		i++;
		$semesterName.addClass('Validform_error').siblings('.error-tips').show();
	}

	if(i>0) return;

	var postIngIframe=$.formOperTipsDialog({
		text:'数据提交中...',
		iconClass:'fa-refresh fa-spin'
	});

	$.post("approval", {orderId:id, status:2, reason:$semesterName.val()}, function(data){
		 if(data.successful) {
			 parent.location.reload();
		 } else {
			 alert(data.message);
		 }
	},"json");
	
});



</script>
</body>
</html>
