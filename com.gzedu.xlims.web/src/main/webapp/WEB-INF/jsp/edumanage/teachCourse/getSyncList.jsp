<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>同步任教信息</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">同步任教信息</h3>
	</div>
	<div class="box-body pos-rel overlay-wrapper">
		<div class="text-center" style="margin-top:60px;">
			<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>
			<div class="inline-block vertical-mid text-left">
				<div class="text-bold f16">本次操作共有 ${resultMap.ALL_COUNT } 条数据信息需要同步！<br/>是否将信息同步到国开学习网？</div>
			</div>
			<br/><br/>
		</div>
		<div class="overlay" style="display:none;">
			<div class="uploading-txt text-center">
				<i class="fa fa-refresh fa-spin vertical-mid"></i>
				<span class="inlineblock left10">任教信息同步中，请稍后...</span>
			</div>
        </div>
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sync">确定同步</button>
</div>
<form id="uploadForm" name="uploadForm" action="/edumanage/teachCourse/getSyncResult" method="post">

</form>
<script type="text/javascript">

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

$("button[data-role='sync']").click(function(event) {
	var $overlay=$(".overlay");
	var $that=$(this);
	$overlay.show();
	$that.hide();
	$("#uploadForm").submit();
});
</script>
</body>
</html>