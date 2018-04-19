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
		<li><a href="#">专业规则</a></li>
		<li class="active">设置专业基础信息</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom">
		<ul class="nav nav-tabs nav-tabs-lg">
	      <li data-role="show" class="active"><a href="javascript:void(0)">1.设置专业基础信息</a></li>
	      <li data-role="show"><a href="javascript:void(0)">2.设置专业规则</a></li>
	      <li data-role="show"><a href="javascript:void(0)">3.发布专业</a></li>
	    </ul>
	    <div class="tab-content no-padding">
	    	<div class="tab-pane active" id="tab_top_1">
	    		<form id="inputForm"  class="theform" action="${ctx }/edumanage/specialty/${action }" method="post">
	    			<input id="action" type="hidden" name="action" value="${action }">
					<input type="hidden" name="specialtyId" value="${item.specialtyId }">
		    		<div class="pad">
			    		<table class="table-gray-th">
							<tr>
								<th width="15%" class="text-right"><small class="text-red">*</small> 专业规则号：</th>
								<td width="35%">
									<div id="ruleCodeDiv" class="position-relative" data-role="valid">
										<input type="text" class="form-control" id="ruleCode" name="ruleCode"  value="${item.ruleCode}" placeholder="专业规则号" datatype="*" nullmsg="请填写专业规则号！"
											<c:if test="${action=='update' }">readonly="readonly"</c:if> />
									</div>
								</td>

								<%-- <th width="15%" class="text-right"><small class="text-red">*</small> 专业代码：</th>
								<td width="35%">
									<div id="zyhDiv" class="position-relative" data-role="valid">
										<input type="text" class="form-control" id="zyh" name="zyh"  value="${item.zyh}" placeholder="专业代码" datatype="*" nullmsg="请填写专业代码！"/>
									</div>
								</td> --%>
								
								<th width="15%" class="text-right">
								<small class="text-red">*</small> 所属专业：</th>
								<td width="35%">
									<div class="position-relative" data-role="valid">
										<select id="specialtyBaseId" name="specialtyBaseId" class="selectpicker show-tick form-control" 	
											<c:if test="${action=='update' }">disabled="disabled"</c:if>
										 	data-size="5" data-live-search="true" datatype="*" nullmsg="请填写专业名称！">
											<option value="">请选择</option>
											<c:forEach items="${specialtyBaseMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==item.specialtyBaseId || map.key==param.specialtyBaseId}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</td>
							</tr>
							<tr>

								<%-- <th class="text-right">
								<small class="text-red">*</small> 专业性质：</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select name="specialtyCategory" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择专业类型！" errormsg="请选择专业类型！">
											<option value="">请选择</option>
											<c:forEach items="${zyxzMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==item.specialtyCategory}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
					                </div>
								</td> --%>

								<th class="text-right">
								<small class="text-red">*</small> 学生类型：</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select name="xslx" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择学生类型！" errormsg="请选择学生类型！">
											<option value="">请选择</option>
											<c:forEach items="${studentTypeMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==item.xslx}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
					                </div>
								</td>
								
								<th class="text-right">
								<small class="text-red">*</small> 专业层次：</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select id="pycc" name="pycc" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true" datatype="*" nullmsg="请选择培养层次！" errormsg="请选择培养层次！" disabled="disabled">
											<option value="">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==item.pycc}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
					                </div>
								</td>
							</tr>
							<tr>
						
								<th class="text-right">
									<small class="text-red">*</small>学科：
								</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select id="subject" name="subject" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择学科！" errormsg="请选择学科！">
											<option value="">请选择</option>
											<c:forEach items="${subjectMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==item.subject}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</td>
						
								<th class="text-right">
									<small class="text-red">*</small>学科门类：
								</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select id="category" name="category" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" datatype="*" nullmsg="请选择门类！" errormsg="请选择门类！">
											<option value="">请选择</option>
											<c:forEach items="${categoryMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==item.category}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</td>
							</tr>
							<tr>
								
								<th class="text-right">
									<small class="text-red">*</small>学制：
								</th>
								<td>
									<div class="position-relative" data-role="valid">
										<div class="input-group">
											<input type="text" class="form-control" name="xz"  value="${item.xz }" datatype="/^[1-9]+\.?[0-9]{0,2}$/" nullmsg="请填写学制！" errormsg="请填写两位小数的数字！">
											<div class="input-group-addon">年</div>
										</div>
									</div>
								</td>
								
								<th class="text-right">专业类型：</th>
								<td>
									<div class="full-width">
										<label class="text-no-bold">
										  <input type="radio" name="type" class="minimal" value="1" <c:if test="${2!=item.type}">checked</c:if>>
										  正式专业
										</label>
										<label class="text-no-bold left10">
										  <input type="radio" name="type" class="minimal" value="2" <c:if test="${2==item.type}">checked</c:if>>
										  体验专业
										</label>
									</div>
								</td>
							</tr>
							<tr>
								
								<%-- <th class="text-right">适用行业：</th>
								<td>
									<select name="syhy" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
										<option value="">请选择</option>
										<c:forEach items="${syhyMap}" var="map">
											<option value="${map.key}"  <c:if test="${map.key==item.syhy}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>                
					                </select>
								</td> --%>
								
								<th class="text-right">专业封面：</th>
								<td>
									<input type="button" value="添加封面" onclick="uploadImage('imgId','imgPath');" />
									<div class="inline-block vertical-middle">
										<ul class="img-list clearfix">
											<li>
												<img id="imgId" src="${item.zyfm }" class="user-image" style="cursor: pointer;">
												<input id="imgPath" type="hidden" value="${item.zyfm }" name="zyfm">
												<!-- <button type="button" class="btn flat btn-default btn-sm btn-block">删除</button> -->
											</li>
										</ul>
									</div>
								</td>
								
								<th></th>
								<td></td>
							</tr>
						</table>
					</div>
					<div class="box-footer text-right">
						<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="cancel" onclick="history.back()">取消</button>
						<button id="btn-submit" type="submit" class="btn btn-primary min-width-90px" data-role="sure">保存，进入下一步</button>
					</div>
				</form>
	    	</div>
	    </div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$("#imgId").click(function() {
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
         
         if(oldRuleCode != $("#ruleCode").val() && $("#ruleCode").val()!==" "){
             $.ajax({  
                 type : "post",  
                 url : "${ctx}/edumanage/specialty/checkRuleCode",  
                 dataType:'json',
                 data : {ruleCode:$('#ruleCode').val()},  
                 async : false,  
                 success : function(data){  
                 	if(data.successful){
							validator=false;
						}  
                 }
         	});
         }
    	 var $ruleCodeDiv=$("#ruleCodeDiv");
         if (!validator) {
        	 $ruleCodeDiv.find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').text('专业规则号已存在');
        	 $("#ruleCode").focus();
        	 return false;
         } else {
        	 $ruleCodeDiv.find('.tooltip').removeClass('in');
         }
         
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
	    	window.location.href = ctx + "/edumanage/specialty/plan/" + data.message;
	    } else {
	    	alert(data.message);
	    	$.closeDialog(postIngIframe);
	    }
		
	  }
	});
	
	var pyccMap = '${pyccMap}';
	$("#specialtyBaseId").change(function(){
		var $pycc = $("#pycc");
		$pycc.empty();
		$pycc.append('<option value="">请选择</option>');
		var value = $(this).children('option:selected').val();
		if (value != "") {
			$.get(ctx+'/edumanage/specialty/base/getLayerById', {specialtyBaseId:value}, function (pycc) {
				$.getJSON(ctx+'/edumanage/specialty/getPyccMap', {}, function (data) {
					$.each(data, function(i,item){
		            	if (item.key == pycc) {
		            		$pycc.append('<option value="'+item.key+'" selected="selected">'+item.value+'</option>');
		            	} else {
		            		$pycc.append('<option value="'+item.key+'">'+item.value+'</option>');
		            	}
		            });
					$pycc.selectpicker('refresh');
		        });
	        });
		}
	});
	$("#specialtyBaseId").change();

	$("#subject").change(function(){
		var $category = $("#category");
		$category.empty();
		$category.append('<option value="">请选择</option>');
		var value = $(this).children('option:selected').val();
		if (value != "") {
			$.getJSON(ctx+'/edumanage/course/changeSubject', {subject:value}, function (data) {
				$.each(data.obj, function(i,item){
	            	if (item.code == '${entity.category}') {
	            		$category.append('<option value="'+item.code+'" selected="selected">'+item.name+'</option>');
	            	} else {
	            		$category.append('<option value="'+item.code+'">'+item.name+'</option>');
	            	}
	            });
				$category.selectpicker('refresh');
	        });
		}
	});

})();

</script>
</body>
</html>
