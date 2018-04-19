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

<form id="theform" action="${ctx }/thesisArrange/updateDefenceTeacher" method="post">
<input type="hidden" name="arrangeId" value="${entity.arrangeId}">
<div class="box no-border no-shadow">
	<div class="box-header with-border">
      <h3 class="box-title">设置毕业论文答辩老师</h3>
    </div>
    <div class="box-body scroll-box">
		<table class="table-gray-th">
			<tr>
				<th width="20%" class="text-center">专业代码</th>
				<td>
					${entity.gjtSpecialtyBase.specialtyCode}
				</td>
			</tr>
			<tr>
				<th class="text-center">专业名称</th>
				<td>
					${entity.gjtSpecialtyBase.specialtyName}
				</td>
			</tr>
			<tr>
				<th class="text-center">层次</th>
				<c:set var="pycc">${entity.gjtSpecialtyBase.specialtyLayer}</c:set>
				<td>${pyccMap[pycc]}</td>
			</tr>
			<tr>
				<th class="text-center">论文答辩人数</th>
				<td>
					${entity.defenceNum}人
				</td>
			</tr>
			<tr>
				<th class="text-center"><small class="text-red">*</small>论文答辩老师</th>
				<td class="no-padding">
					<div style="margin:-1px;">

					
						<table class="table margin-bottom-none vertical-middle">
							<tr>
								<td width="100" class="text-center">主答辩老师
									<div class="margin_t5">
										<button class="btn btn-default bg-white btn-sm" type="button" data-role="add-person-2" data-rel="2">
											<i class="fa fa-fw fa-plus"></i>
											添加老师
										</button>
									</div>
								</td>
								<td>
									<div class="clearfix" data-box="2">
										<c:forEach items="${entity.gjtThesisAdvisers2}" var="adviser">
											<div class="alert col-sm-2 col-xs-4 pad-l5 pad-r5 no-pad-top no-pad-bottom margin_b10 fade in">
												<div class="box no-border bg-light-blue no-margin pad">
								                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
								                    <div class="text-center oh text-overflow text-nowrap">${adviser.teacher.xm}</div>
								                    <input type="hidden" name="advisers2" value="${adviser.teacherId}">
								                </div>
											</div>
										</c:forEach>
									</div>
								</td>
							</tr>
							<tr>
								<td class="text-center">辅答辩老师
									<div class="margin_t5">
										<button class="btn btn-default bg-white btn-sm" type="button" data-role="add-person-2" data-rel="3">
											<i class="fa fa-fw fa-plus"></i>
											添加老师
										</button>
									</div>
								</td>
								<td>
									<div class="clearfix" data-box="3">
										<c:forEach items="${entity.gjtThesisAdvisers3}" var="adviser">
											<div class="alert col-sm-2 col-xs-4 pad-l5 pad-r5 no-pad-top no-pad-bottom margin_b10 fade in">
												<div class="box no-border bg-light-blue no-margin pad">
								                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
								                    <div class="text-center oh text-overflow text-nowrap">${adviser.teacher.xm}</div>
								                    <input type="hidden" name="advisers3" value="${adviser.teacherId}">
								                </div>
											</div>
										</c:forEach>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>

    </div>
</div>
<div class="pop-btn-box pad text-right">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel">取消</button>
	<button type="submit" class="btn btn-success min-width-90px" data-role="sure">确认分配</button>
</div>
</form>

<script>
$('.scroll-box').height($(window).height()-126);

var defenceNum = '${entity.defenceNum}';

;(function(){
	
	if('${action}' == 'view'){
		$(':input').attr("disabled","disabled");
		$('[data-role="cancel"]').removeAttr("disabled");
		$('[data-role="sure"]').remove();  
		$('.close').remove();  
	}
	
	var $theform=$("#theform");

	var htmlTemp='<div class="tooltip top" role="tooltip" style="white-space: nowrap;">'
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
		        bottom:28
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
		  if (defenceNum != 0) {
			  if($('input[name="advisers2"]').length == 0) {
		            alert('请添加主答辩老师');
		            return false;
		      }
			  if($('input[name="advisers3"]').length == 0) {
		            alert('请添加辅答辩老师');
		            return false;
		      }
		  }
		  
		  $('[data-role="sure"]').attr("disabled","disabled");
	  },
	  callback:function(data){
		  if (data.successful) {
			  parent.$.closeDialog(frameElement.api);
			  parent.location.href = "${ctx}/thesisArrange/list";
		  } else {
			  alert(data.message);
			  $('[data-role="sure"]').removeAttr("disabled");
		  }
	  }
	});

})();

//添加论文答辩老师
$('body')
.on('click','[data-role="add-person-2"]',function(event) {
	var advisers = '';
	$('input[name="advisers2"]').each(function(index, el) {
		advisers = advisers + el.value + ',';
	});
	$('input[name="advisers3"]').each(function(index, el) {
		advisers = advisers + el.value + ',';
	});
	if (advisers != '') {
		advisers = advisers.substring(0, advisers.length - 1);
	}
	
	var _this=this;
  	$.mydialog({
	  id:'add-person',
	  width:760,
	  height:490,
	  zIndex:11000,
	  urlData:{
	  	container:$(_this).attr('data-rel')
	  },
	  content: 'url:${ctx}/thesisArrange/choiceDefenceTeacher?teacherIds='+advisers
	});
});

/*取消*/
$('[data-role="cancel"]').click(function(event) {
  	parent.$.closeDialog(frameElement.api);
});

</script>

</body>
</html>


					

