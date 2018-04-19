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
<form id="theform" action="${ctx}/thesisApply/confirmPropose" method="post">
    <input type="hidden" name="applyId" value="${param.applyId}">
	<div class="box no-border no-shadow">
		<div class="box-header with-border">
	      <h3 class="box-title">开题报告确认定稿</h3>
	    </div>
	    <div class="box-body scroll-box">
			<textarea name="content" class="form-control" placeholder="请填写开题报告定稿意见" rows="10" datatype="*" nullmsg='<i class="fa fa-exclamation-circle f16 vertical-middle"></i> 请填写定稿意见' errormsg="请填写定稿意见"></textarea>
			<div class="Validform_checktip"></div>
	    </div>
	</div>
	<div class="pop-btn-box pad text-right">
		<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel">取消</button>
		<button type="submit" class="btn btn-success min-width-90px" data-role="sure">确认定稿</button>
	</div>
</form>

<script>
$('.scroll-box').height($(window).height()-126);

//表单验证
;(function(){
	$.Tipmsg.r='';
	var postIngIframe;
	var postForm=$("#theform").Validform({
	  //showAllError:true,
	  ajaxPost:true,
	  tiptype:3,
	  beforeSubmit:function(curform){
	    postIngIframe=$.formOperTipsDialog({
	    	text:'数据提交中...',
	    	iconClass:'fa-refresh fa-spin'
	    })
	  },
	  callback:function(data){
		  if (data.successful) {
			  parent.$.closeDialog(frameElement.api);
			  parent.location.reload();
		  } else {
			  $.closeDialog(postIngIframe);
			  alert(data.message);
		  }
	  }
	});

})();

/*取消*/
$('[data-role="cancel"]').click(function(event) {
  	parent.$.closeDialog(frameElement.api);
});

</script>

</body>
</html>


					

