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

<jsp:include page="/eefileupload/upload.jsp" />

<form id="theform" action="${ctx}/thesisApply/submitRecord" method="post">
  <input type="hidden" name="applyId" value="${entity.applyId}">
  <div class="box no-border no-shadow">
  	<div class="box-header with-border">
        <h3 class="box-title">
        	<c:if test="${param.type eq '1' }">
        	<c:choose>
        		<c:when test="${entity.status == 2}">
        			开题报告发回修改
        		</c:when>
        		<c:when test="${entity.status == 4}">
        			论文初稿发回修改
        		</c:when>
        	</c:choose>
        	</c:if>
        	<c:if test="${param.type eq '2' }">
        		回复
        	</c:if>
        </h3>
      </div>
      <div class="box-body scroll-box">
        <div>
          <textarea name="content" class="form-control" placeholder="请填写修改意见" rows="8" datatype="*" nullmsg='<i class="fa fa-exclamation-circle f16 vertical-middle"></i> 请填写修改意见' errormsg="请填写修改意见"></textarea>
          <span class="Validform_checktip no-margin"></span>
        </div>
        <c:if test="${param.type eq '1' }">
        <div class="margin_t10">
          <button type="button" class="btn btn-default min-width-90px" onclick="javascript:uploadFile('attachmentName','attachment','doc|docx|wps', null, uploadCallback)">上传附件</button>
        </div>
        <div class="pad-l10 pad-r10">
          <div class="row">
            <div class="alert col-sm-3 col-xs-6 pad-l5 pad-r5 no-pad-top no-pad-bottom margin_t10 margin-bottom-none fade in">
              <div class="box no-border no-margin">
              	  <input type="hidden" id="attachment" name="attachment" class="form-control" value=""/>
				  <input type="hidden" id="attachmentName" name="attachmentName" class="form-control" value=""/>
                  <div id="attachmentDiv" class="text-center oh text-overflow text-nowrap" data-toggle="tooltip" title="" data-html="true"></div>
              </div>
            </div>
          </div>
        </div>
        </c:if>
      </div>
  </div>
  <div class="pop-btn-box pad text-right">
  	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel">取消</button>
  	<button type="submit" class="btn btn-success min-width-90px" data-role="sure">发回修改</button>
  </div>
</form>

<script>

function uploadCallback() {
	$("#attachmentDiv").attr("title", $("#attachment").val());
	$("#attachmentDiv").html($("#attachmentName").val());
	$("#attachmentDiv").parent().addClass("bg-light-blue pad");
}

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


					

