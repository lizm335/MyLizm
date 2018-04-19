<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-新增选课</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.back()">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">选课管理</a></li>
		<li class="active">新增选课</li>
	</ol>
</section>

<section class="content">
	<div class="row">
		<div class="col-md-12">
			<div class="box box-primary">
				<div class="box-header with-border">
					<h3 class="box-title">新增选课</h3>
				</div>
				<form id="theform" class="theform" role="form" action="${ctx}/edumanage/choosemanage/addRecResult" method="post">
					<div class="box-body" id="openclass">
						<div class="form-group">
							<label class="col-sm-2 control-label" style="width: 10%"><small class="text-red">*</small>开课学期</label>
							<div class="col-sm-4">
								<select id="termId" name="ACTUAL_GRADE_ID" class="selectpicker show-tick form-control" data-size="8" >
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}" >${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<br><br>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top" style="width: 10%">新增选课</label>
							<div class="col-sm-10" id="chooseHtml">
								
							</div>
						</div>
					</div>
					<div class="box-footer text-right">
						<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="cancel" onclick="history.back()">返回</button>
						<button type="submit" class="btn btn-primary min-width-90px" data-role="sure">确认选课</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>  
<script type="text/javascript">
	
	//表单验证
	;(function(){
		var $theform=$(".theform");
	
		var htmlTemp='<div class="tooltip top" role="tooltip">'
		      +'<div class="tooltip-arrow"></div>'
		      +'<div class="tooltip-inner"></div>'
		      +'</div>';
		$theform.find(":input[datatype]").each(function(index, el) {
			$(this).after(htmlTemp);
		});
	
		$.Tipmsg.r='';
		var postForm=$theform.Validform({
		  showAllError:false,
		  ajaxPost:true,
		  tiptype:function(msg,o,cssctl){
		    if(!o.obj.is("form")){
		    	
			    var msgBox=o.obj.closest('.position-relative').find('.tooltip');
	
			    msgBox.css({
			        bottom:"100%",
			        'margin-bottom':-5
			    })
			    msgBox.children('.tooltip-inner').text(msg);
	
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
			  
			if($(':input[name="ids"]:checked').length==0) {
	            alert('请选择新增的选课！');
	            return false;
	        }
	        var ids = '';
	        $(':input[name="ids"]:checked').each(function(i, v) {
	        	ids = ids + v.value + ',';
	        });
	        $(':input[name="courseIds"]').val(ids.substring(0, ids.length - 1));  
			
		    window.postIngIframe=$.formOperTipsDialog({
				text:'数据提交中...',
				iconClass:'fa-refresh fa-spin'
			});
		  },
		  callback:function(data){
			  history.back();
		  }
		});
	
	})();
	
	$(function() {

	    // 选择学期
	    $("#termId").on('change', function(){
	        var termId = $(this).val();
	        if($.trim(termId) == '') {
	            $("form#inputForm :input[name='termId']").focus();
	            return;
	        }
	        $.get(ctx+'/edumanage/choosemanage/getNoChooseInfo?ACTUAL_GRADE_ID='+termId, function (data) {
	        	$("#chooseHtml").html(data);
	        });
	    }).trigger('change');
	})
</script>

</body>
</html>