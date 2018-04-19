<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 个人资料</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body >

<form id="theform" class="form-horizontal" role="form" action="${ctx }/home/personal/updateMobilePhone"  method="post">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">更换绑定手机</h3>
	</div>
	<div class="box-body pad-l20 pad-r20">
		<div class="slim-Scroll">
			<table class="table no-border margin-bottom-none">
	        	<tr>
	        		<th width="120" class="text-right">原绑定手机</th>
	        		<td>${info.sjh }
	        			 <input type="hidden" name="oldSjh"id="oldSjh" class="form-control"  value="${info.sjh }"  >
	        		</td>
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
			                <input type="text"  name="oldVerifyCode" id="oldVerifyCode"  class="form-control" placeholder="验证码" datatype="*" nullmsg="请填写验证码！" errormsg="请填写验证码！">
	        			</div>
	        		</td>
	        		<td>
	        			<button type="button" class="btn btn-success btn-block" data-role="get-ver-code">获取验证码</button>
	        		</td>
	        	</tr>
	        	<tr>
	        		<th class="text-right">
	        			<div class="pad-t5">
	        				<span class="text-red">*</span>
	        				绑定新手机
	        			</div>
	        		</th>
	        		<td>
	        			<div class="position-relative">
	        				<input type="text" name="newSjh" id="newSjh" class="form-control" placeholder="新手机号码" datatype="m" nullmsg="请填写新手机号码！" errormsg="手机号码不对！">
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
			                <input type="text" name="newVerifyCode" id="newVerifyCode"  class="form-control" placeholder="验证码" datatype="*" nullmsg="请填写验证码！" errormsg="请填写验证码！">
	        			</div>
	        		</td>
	        		<td>
	        			<button type="button" class="btn btn-success btn-block" data-role="get-ver-code-new">获取验证码</button>
	        		</td>
	        	</tr>
	        </table>
		</div>
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="submit" class="btn btn-success min-width-90px" data-role="sure">确定</button>

	<button type="button" class="btn btn-default margin_l15 min-width-90px" data-role="close-pop">取消</button>
</div>
</form>


<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="application/javascript">
	$('.slim-Scroll').slimScroll({
	    height: $(window).height()-106,
	    size: '5px'
	});

	//关闭窗口
	$('[data-role="close-pop"]').click(function(event) {
		parent.$.closeDialog(frameElement.api);
	});

	(function(){
		var $theform=$("#theform");
		var postIngIframe;
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
			  },
			  callback:function(data){
			    postIngIframe.find('[data-role="tips-text"]').html(data.message);
			    postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
				  /*关闭弹窗*/
		        setTimeout(function(){
		    	  parent.location.href=parent.location.href;
		       },2000);
			 }
		});
	})();

	//短信验证
	$('[data-role="get-ver-code"]').click(function(event){
		event.preventDefault();
		var that=$(this);
		var oldSjh=$('#oldSjh').val();
    	if(oldSjh==''){
    		alert('手机号码异常，请联系学支管理员！');
    		return ;
    	}else{
    		$.post('${ctx}/home/common/sendMobilePhone.html}',{mobilePhone:oldSjh,type:1},function(data){
    			if(data.successful==false){
    				alert(data.message);
    			}else{
	    			if(!that.hasClass("disabled")){
	    				  that.addClass("disabled").prop('disabled', true).html('已发送验证码<span class="count-down2">(120s)</span>');
	    				  var timer,i = 120;
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
    		},'json');
    	}
	});
    
    //校验验证码是否正确
    $('#oldVerifyCode').change(function(){
    	var code=$('#oldVerifyCode').val();
    	if(code.length==6){
    		$.post('${ctx}/home/common/doSmsValidateCode.html}',{code:code},function(data){
    			if(data.successful==false){
    				alert(data.message);
    			}
    		},'json');
    	}
    });
    
    
    
    //发送新手机验证码
	$('[data-role="get-ver-code-new"]').click(function(event){
		event.preventDefault();
		var that=$(this);
		var code=$('#oldVerifyCode').val();
		var newSjh=$('#newSjh').val();
		 if(code==''){
    		alert('旧手机验证码不能为空！');
    		return ;
    	}else if(newSjh==''){
    		alert('新手机号码不能为空！');
    		return ;
    	}else{
	   		$.post('${ctx}/home/common/sendMobilePhone.html}',{mobilePhone:newSjh},function(data){
	   			if(data.successful==false){
    				alert(data.message);
    			}else{
					if(!that.hasClass("disabled")){
					  that.addClass("disabled").prop('disabled', true).html('已发送验证码<span class="count-down2">(120s)</span>');
					  var timer,i = 120;
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
	   		},'json');
    	}
	});

    
    //校验新手机验证码是否正确
    $('#newVerifyCode').change(function(){
    	var code=$('#newVerifyCode').val();
    	if(code.length==6){
    		$.post('${ctx}/home/common/doSmsNewValidateCode.html}',{code:code},function(data){
    			if(data.successful==false){
    				alert(data.message);
    			}
    		},'json');
    	}
    });
    
    
</script>
</body>
</html>
