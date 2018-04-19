<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>专业操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<jsp:include page="/eefileupload/upload.jsp"/>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">专业管理</a></li>
		<li class="active">设置专业基础信息</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom">
		<ul class="nav nav-tabs nav-tabs-lg">
		  
	      <li data-role="show" class="active"><a href="javascript:void(0)">
	      	<c:if test="${action=='create' }">新建专业</c:if>
	      	<c:if test="${action=='update' }">修改专业</c:if>
	      	</a>
	      </li>
	    </ul>
	    <div class="tab-content no-padding">
	    	<div class="tab-pane active" id="tab_top_1">
	    		<form id="inputForm"  class="theform" action="${ctx }/edumanage/specialty/base/${action }" method="post">
	    			<input id="action" type="hidden" name="action" value="${action }">
					<input type="hidden" name="specialtyBaseId" value="${item.specialtyBaseId }">
		    		<div class="pad">
			    		<table class="table-gray-th">
							<tr>
								<th width="15%" class="text-right"><small class="text-red">*</small> 专业代码：</th>
								<td width="35%">
									<div id="ruleCodeDiv" class="position-relative" data-role="valid">
										<input type="text" class="form-control" name="specialtyCode" value="${item.specialtyCode}" 
											placeholder="专业代码" datatype="*" nullmsg="请填写专业代码！" maxlength="8" 
											<c:if test="${action=='update' }">readonly="readonly"</c:if> />
									</div>
								</td>
								
								<th class="text-right">
								<small class="text-red">*</small> 专业层次：</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select name="specialtyLayer" class="selectpicker show-tick form-control" data-size="5" 
										<c:if test="${action=='update' }">disabled="disabled"</c:if>
										data-live-search="true" datatype="*" nullmsg="请选择培养层次！" errormsg="请选择培养层次！">
											<option value="">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==item.specialtyLayer}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
					                </div>
								</td>
							</tr>
							<tr>
								<th width="15%" class="text-right">
								<small class="text-red">*</small> 专业名称：</th>
								<td width="35%">
									<div class="position-relative" data-role="valid">
										<input type="text" class="form-control" name="specialtyName"  value="${item.specialtyName}"
											 placeholder="专业名称" datatype="*" nullmsg="请填写专业名称！" />
									</div>
								</td>
								<th class="text-right">
								<small class="text-red">*</small> 责任教师：</th>
								<td>
									<div class="position-relative" data-role="valid">
										<input type="text" class="form-control" name="teacher"  value="${item.teacher}"
											 placeholder="责任教师" datatype="*" nullmsg="请填写责任教师！" />
					                </div>
								</td>
							</tr>
							<tr>
								<th class="text-right">专业封面：</th>
								<td>
									<input type="button" value="添加封面" onclick="uploadImage('imgId-specialtyImgUrl','imgPath-specialtyImgUrl');" />
									<div class="inline-block vertical-middle">
										<ul class="img-list clearfix">
											<li>
												<img id="imgId-specialtyImgUrl" src="${item.specialtyImgUrl }" class="user-image" style="cursor: pointer;">
												<input id="imgPath-specialtyImgUrl" type="hidden" value="${item.specialtyImgUrl }" name="specialtyImgUrl">
											</li>
										</ul>
									</div>
								</td>
								
								
								<th class="text-right">责任教师照片：</th>
								<td>
									<input type="button" value="添加封面" onclick="uploadImage('imgId-teacherImgUrl','imgPath-teacherImgUrl');" />
									<div class="inline-block vertical-middle">
										<ul class="img-list clearfix">
											<li>
												<img id="imgId-teacherImgUrl" src="${item.teacherImgUrl }" class="user-image" style="cursor: pointer;">
												<input id="imgPath-teacherImgUrl" type="hidden" value="${item.teacherImgUrl }" name="teacherImgUrl">
											</li>
										</ul>
									</div>
								</td>
							</tr>
							<tr>
								<th class="text-right">专业简介：</th>
								<td colspan="3">
									<textarea rows="5" cols="120" name="specialtyDetails">${item.specialtyDetails }</textarea>
								</td>
							</tr>
							<tr>
								<th class="text-right">责任教师简介：</th>
								<td colspan="3">
									<textarea rows="5" cols="120" name="teacherDetails">${item.teacherDetails }</textarea>
								</td>
							</tr>
						</table>
					</div>
					<div class="box-footer text-right">
						<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="cancel" onclick="history.back()">取消</button>
						<button id="btn-submit" type="submit" class="btn btn-primary min-width-90px" data-role="sure">保存</button>
					</div>
				</form>
	    	</div>
	    </div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$("#imgId-specialtyImgUrl").click(function() {
	var src = $(this).attr("src");
	if (src != "") {
		window.open(src);
	}
});
$("#imgId-teacherImgUrl").click(function() {
	var src = $(this).attr("src");
	if (src != "") {
		window.open(src);
	}
});

//表单验证
;(function(){
	//var oldZyh = $("#zyh").val();
	var oldRuleCode = $("#ruleCode").val();
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
         var validator=true;     
         
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
	    if (data.successful) {
	    	window.location.href = ctx + "/edumanage/specialty/base/list";
	    } else {
	    	alert(data.message);
	    	$.closeDialog(postIngIframe);
	    }
		
	  }
	});

})();
</script>
</body>
</html>
