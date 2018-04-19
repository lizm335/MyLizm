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
<form id="theform" action="${ctx}/thesisApply/confirmThesis" method="post">
    <input type="hidden" name="applyId" value="${param.applyId}">
	<div class="box no-border no-shadow">
		<div class="box-header with-border">
	      <h3 class="box-title">论文初稿确认定稿</h3>
	    </div>
	    <div class="box-body scroll-box">
	    	<%-- <div>
				<textarea name="content" class="form-control" placeholder="请填写论文初稿定稿意见" rows="10" datatype="*" nullmsg='<i class="fa fa-exclamation-circle f16 vertical-middle"></i> 请填写定稿意见' errormsg="请填写定稿意见"></textarea>
				<span class="Validform_checktip no-margin"></span>
			</div>
			 <div class="margin_t10">
				
				<input name="reviewScore" type="text" class="form-control inline-block vertical-middle margin_r10" placeholder="请填写论文初稿定稿得分" style="width:35%" datatype="n" nullmsg='<i class="fa fa-exclamation-circle f16 vertical-middle"></i> 请填写定稿得分' errormsg='<i class="fa fa-exclamation-circle f16 vertical-middle"></i> 请填写数字'>

				<small class="inline-block gray9 vertical-middle">说明：论文初稿确认定稿，得分不能少于60分</small>
				
				<div class="Validform_checktip no-margin" style="height:auto"></div>
			</div>  --%>
			<%-- <div id="autographDiv" class="box box-border margin-bottom-none no-shadow flat margin_t10 text-center">
				<div class="box-body">
					<c:choose>
						<c:when test="${not empty signPhoto}">
							<img id="autographImg" src="${signPhoto}">
						</c:when>
						<c:otherwise>
							<span class="text-red">暂无签名，请到个人信息设置处签名。</span>
						</c:otherwise>
					</c:choose>
				</div>
			</div> --%>
			<div class="margin_t10">	是否确认定稿？</div>
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
		/* var validator = true; 
		
		var reviewScore = $(':input[name="reviewScore"]').val();
		if (parseFloat(reviewScore) < 60) {
			alert("得分不能少于60分");
			validator = false; 
		} 
		
		if (!validator) {
			return false;
		} */
		  
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


					

