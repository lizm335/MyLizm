<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body>
	<form id="theform" action="${ctx}/home/class/newFeedback/setOften" method="post">
		<input type="hidden" name="id" value="${subjectId }">
		<input type="hidden" name="state" value="${state }">
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">提示</h3>
			</div>
			<div class="scroll-box">
			  	<div class="text-center f18 margin_b10">请选择常见问题分类</div>
			  	<div style="width: 90%;">
			   		<div class="position-relative margin_l15" >
							<select name="oftenType" class="form-control select2" datatype="*" errormsg="请选择分类" datatype="normalType">
								<option value="">请选择</option>
								<c:forEach items="${oftenTypeMap }" var="map">
									<option value="${map.key }">${map.value }</option>
								</c:forEach>
							</select>
						</div>
					<div class="position-relative margin_l15 margin_t15" >
							<input type="text" name="isCommendType" placeholder="标签用于检索，多个请用英文分号“;”隔开" class="form-control ">
					</div>
			   </div>
		  </div>
		</div>
		<div class="text-right pop-btn-box pad">
			<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
			<button type="submit" class="btn btn-success min-width-90px" data-role="sure">确定</button>
		</div>
	</form>


<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
	
	$('.select2').select2();
	
	//关闭 弹窗
	$("button[data-role='close-pop']").click(function(event) {
		parent.$.closeDialog(frameElement.api)
	});

	//设置内容主体高度
	$('.scroll-box').height($(frameElement).height()-106);


	//确认
    var $theform=$("#theform");
    var htmlTemp='<div class="tooltip top" role="tooltip">'
          +'<div class="tooltip-arrow"></div>'
          +'<div class="tooltip-inner"></div>'
          +'</div>';
    $theform.find(".position-relative").each(function(index, el) {
        $(this).append(htmlTemp);
    });

    $.Tipmsg.r='';
    var postIngIframe;
    var postForm=$theform.Validform({
      ajaxPost:true,
      tiptype:function(msg,o,cssctl){
        if(!o.obj.is("form")){
            var msgBox=o.obj.closest('.position-relative').find('.tooltip');
            msgBox.children('.tooltip-inner').text(msg);
            if(msgBox.hasClass('left')){
              msgBox.css({
                width:130,
                left:-120,
                top:5
              })
            } else{
              msgBox.css({
                bottom:"100%",
                'margin-bottom':-5
              })
            }
            switch(o.type){
              case 3:
                msgBox.addClass('in');
                break;
              default:
                msgBox.removeClass('in');
                break;
            }
        }
      },
      beforeSubmit:function(curform){
        postIngIframe=$.formOperTipsDialog({
          text:'数据提交中...',
          iconClass:'fa-refresh fa-spin'
        });      
      }, callback:function(data){
            postIngIframe.find('[data-role="tips-text"]').html(data.message);
            postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
            /*关闭弹窗*/
            setTimeout(function(){
              parent.location.href=parent.location.href;
            },1500)
      }
    });

	</script>
</body>
</html>
