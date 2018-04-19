<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>新增班级</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form action="${ctx}/edumanage/courseclass/create" method="post" id="inputForm">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">新增班级</h3>
	</div>
	<div class="box-body">
		<table class="full-width" height="200">
			<tr>
				<td valign="middle">
					<table class="per-tbl" width="80%" align="center">
						<tr>
							<th class="text-no-bold"><small class="text-red">*</small> 学期：</th>
							<td>
								<div class="position-relative" data-role="valid">
									<select name="actualGradeId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择学期！" errormsg="请选择学期！">
										<option value="">请选择</option>
										<c:forEach items="${termMap}" var="map">
											<option value="${map.key}" >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<th class="text-no-bold"><small class="text-red">*</small> 课程名称：</th>
							<td>
								<div class="position-relative" data-role="valid">
									<select name="courseId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择课程！" errormsg="请选择课程！">
										<option value="">请选择</option>
										<c:forEach items="${courseMap}" var="map">
											<option value="${map.key}" >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<th class="text-no-bold">
								<small class="text-red">*</small>班级名称：
							</th>
							<td>
								<div class="position-relative" data-role="valid">
									<input class="form-control" type="text" name="bjmc" datatype="*" nullmsg="请填写班级名称！" />
								</div>
							</td>
						</tr>
						<tr>
							<th class="text-no-bold">
								<small class="text-red">*</small>人数上限：
							</th>
							<td>
								<div class="position-relative" data-role="valid">
									<input class="form-control" type="text" name="limitNum" datatype="n" nullmsg="请填写 人数上限！" errormsg="请填写数字！" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>

<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button id="btn-submit" type="submit" class="btn btn-success min-width-90px" data-role="sure">确认</button>
</div>
	</form>
<script type="text/javascript">
//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api);
});
//确认
/* $("button[data-role='sure']").click(function(event) {
	var postIngIframe=$.mydialog({
	  id:'dialog-1',
	  width:150,
	  height:50,
	  backdrop:false,
	  fade:false,
	  showCloseIco:false,
	  zIndex:11000,
	  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
	});
	
	$.post($("#inputForm").attr("action"), $("#inputForm").serialize(),function(data){
   		if(data.successful){
   			parent.$.closeDialog(frameElement.api);
   		}else{
   			alert(data.message);
   		}
   },"json"); 
	
}); */

//表单验证
;(function(){
	var $theform=$("#inputForm");

	var htmlTemp='<div class="tooltip top" role="tooltip" style="white-space: nowrap;">'
        +'<div class="tooltip-arrow"></div>'
        +'<div class="tooltip-inner"></div>'
        +'</div>';
	$theform.find('[data-role="valid"]').each(function(index, el) {
		$(this).append(htmlTemp);
	});

	$.Tipmsg.r='';
	var postIngIframe;
	var postForm=$theform.Validform({
	  //showAllError:true,
	  ajaxPost:true,
	  tiptype:function(msg,o,cssctl){
	    //msg：提示信息;
	    //o:{obj:*,type:*,curform:*},
	    //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
	    //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
	    //curform为当前form对象;
	    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
	    if(!o.obj.is("form")){
		    var msgBox=o.obj.siblings('.tooltip');
		    if(msgBox.length<=0){
		    	var $t=$(htmlTemp);
		    	o.obj.after($t);
		    	msgBox=$t;
		    }

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
		        bottom:'100%'
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
       
	    postIngIframe=$.mydialog({
		  id:'dialog-1',
		  width:150,
		  height:50,
		  backdrop:false,
		  fade:true,
		  showCloseIco:false,
		  zIndex:11000,
		  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
		});
	  },
	  callback:function(data){
	    //这里执行回调操作;
	    //如果callback里明确return alse，则表单不会提交，如果return true或没有return，则会提交表单。

	    //if("成功提交") 就执行下面语句
		if(data.successful){
   			parent.$.closeDialog(frameElement.api);
   			parent.location.reload();
   		}else{
   			alert(data.message);
   		}
	  }
	});

})();

$('.slim-Scroll').slimScroll({
    height: 430,
    size: '5px'
});
</script>
</body>
</html>



