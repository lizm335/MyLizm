<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>绑定手机</title>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
</head>
<body>
<form id="theform" action="<%=request.getContextPath()%>/personal/bindMobile.do"> 
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">更换绑定手机</h3>
	</div>
	<div class="box-body">
        <table class="table no-border">
        	<c:if test="${not empty mobile }">
        	<tr>
        		<th width="120" class="text-right">原绑定手机</th>
        		<td>${mobile }</td>
        		<td width="160"></td>
        	</tr>
        	<tr>
        		<th class="text-right">
        			<div class="pad-t5">
        				<span class="text-red">*</span>
        				原手机验证码
        			</div>
        		</th>
        		<td>
        			<div class="position-relative">
		                <input type="text" class="form-control" name="firstValidateCode" placeholder="验证码" datatype="*" nullmsg="请填写验证码！" errormsg="请填写验证码！">
        			</div>
        		</td>
        		<td>
        			<input type="hidden" value="${mobile }"  id="phone-num-0">
        			<button type="button" class="btn btn-success btn-block" data-role="get-ver-code" data-rel="phone-num-0">获取验证码</button>
        		</td>
        	</tr>
        	</c:if>
        	<tr>
        		<th class="text-right">
        			<div class="pad-t5">
        				<span class="text-red">*</span>
        				绑定新手机
        			</div>
        		</th>
        		<td>
        			<div class="position-relative">
        				<input type="text" class="form-control" name="mobile" placeholder="新手机号码" datatype="m" nullmsg="请填写新手机号码！" errormsg="手机号码不对！" id="phone-num-1">
        			</div>
        		</td>
        	</tr>
        	<tr>
        		<th class="text-right">
        			<div class="pad-t5">
        				<span class="text-red">*</span>
        				新手机验证码
        			</div>
        		</th>
        		<td>
        			<div class="position-relative">
		                <input type="text" class="form-control" name="newValidateCode" placeholder="验证码" datatype="*" nullmsg="请填写验证码！" errormsg="请填写验证码！">
        			</div>
        		</td>
        		<td>
        			<button type="button" class="btn btn-success btn-block" data-role="get-ver-code" data-rel="phone-num-1">获取验证码</button>
        		</td>
        	</tr>
        </table>
	</div>
	<div class="box-footer text-right pop-footer">
		<button type="button" class="btn btn-success min-width-90px" data-role="sure">确定</button>
		<button type="button" class="btn btn-default margin_l15 min-width-90px" data-role="cancel">取消</button>
	</div>
</div>
</form>
<script type="text/javascript">

$(".box-body").height($(frameElement).height()-126);
//关闭 弹窗
$("button[data-role='cancel']").click(function(event) {
	parent.$.closeDialog(frameElement.api);
});

/*确认发送*/

var $theform=$("#theform");

var htmlTemp='<div class="tooltip top" role="tooltip">'
      +'<div class="tooltip-arrow"></div>'
      +'<div class="tooltip-inner"></div>'
      +'</div>';
$theform.find(":input[datatype]").each(function(index, el) {
	$(this).after(htmlTemp);
});

$.Tipmsg.r='';
var postIngIframe;
var postForm=$theform.Validform({
  showAllError:true,
  tiptype:function(msg,o,cssctl){
    //msg：提示信息;
    //o:{obj:*,type:*,curform:*},
    //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
    //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
    //curform为当前form对象;
    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
    if(!o.obj.is("form")){
	    var msgBox=o.obj.closest('.position-relative').find('.tooltip');

	    msgBox.children('.tooltip-inner').text(msg);
	    if(msgBox.hasClass('left')){
	      msgBox.css({
	        width:130,
	        left:-120,
	        top:5
	      })
	    }
	    else{
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
    if(curform.find(".Validform_error").length>0){
      return false;
    }
    postIngIframe=$.formOperTipsDialog({
      text:'数据提交中...',
      iconClass:'fa-refresh fa-spin'
    });

    return true;
  },
  callback:function(data){
    console.info(data);
    setTimeout(function(){//此句模拟交互，程序时请去掉
        postIngIframe.find('[data-role="tips-text"]').html(data.message);
    	if(data.result == 'success') {
	        postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
	        
    	} else {
    		postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-close ');
    	}

		/*关闭弹窗*/
		setTimeout(function(){$.closeDialog(postIngIframe);},2000);
	},2000);//此句模拟交互，程序时请去掉
	if(data.result == 'success') {
		$("[data-dismiss='modal']").focus();//("click");
	}
  }
});

$('[data-role="sure"]').click(function(event) {
  /*插入业务逻辑*/
  
  postForm.ajaxPost();
});
	

//短信验证
$('[data-role="get-ver-code"]').click(function(event){
  event.preventDefault();
  var that=$(this);
  var checkLegal=true;

  //验证手机 正则表达式
  if($(this).data('rel')){

    var reg=/^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/;
    var $relInput=$( '#'+$(this).attr('data-rel') );
    var relPhoneNum=$relInput.val();

    if(relPhoneNum==''){
      $relInput.addClass('Validform_error')
      .next('.tooltip')
        .addClass('in')
        .css({
          bottom: '100%',
          'margin-bottom': -5
        })
        .children('.tooltip-inner').text( $relInput.attr('nullmsg') );

      checkLegal=false;
    }
    else if(!reg.test(relPhoneNum)){
      $relInput.addClass('Validform_error')
      .next('.tooltip')
        .addClass('in')
        .css({
          bottom: '100%',
          'margin-bottom': -5
        })
        .children('.tooltip-inner').text( $relInput.attr('errormsg') );

      checkLegal=false;
    }

  }

  if(checkLegal){
    if(!that.hasClass("disabled")){
      // 请求发送验证码
      
      var sendCode;
      $.ajax({
      	url:'<%=request.getContextPath()%>/personal/sendSmsCode.do',
      	data:{"mobile":relPhoneNum},
      	dataType:"json",
      	async:false,
      	success:function(data) {
   			sendCode = data.result;
      	},
      });
      if(sendCode != 'success') {
    	  alert("验证码发送失败!");
    	  return;
      }
      that.addClass("disabled").prop('disabled', true).html('已发送验证码<span class="count-down2">(60s)</span>');

      var timer,i = 60;
      var fn = function () {
        if(i<=0){
          that.removeClass("disabled").prop('disabled', false).find(".count-down2").html("");
          clearInterval(timer);
        }
        else{
          that.find(".count-down2").html("("+i+"s)");
          i --;
        }
      };
      timer = setInterval(fn, 1000);
      fn();
    }
  }
});

</script>
</body>
</html>

