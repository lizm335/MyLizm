<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<script type="text/javascript">
		var planId = '${textbookPlan.planId}';
		if (!planId) {
			alert("当前没有可设置的教材发放安排！");
			history.back();
		}
	</script>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.back()">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材管理</a></li>
		<li><a href="#">发放编排</a></li>
		<li class="active">设置教材发放安排</li>
	</ol>
</section>

<section class="content">
	<div class="row">
		<div class="col-md-12">
			<div class="box box-primary">
				<div class="box-header with-border">
					<h3 class="box-title">设置教材发放安排</h3>
				</div>
				<form id="theform" class="theform" role="form" action="${ctx}/textbookArrange/save" method="post">
					<input type="hidden" name="textbookIds" value=""/>

					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-2 control-label" style="width: 10%"><small class="text-red">*</small>教材计划</label>
							<div class="col-sm-4">
								<select id="textbookPlanId" name="textbookPlanId" class="selectpicker show-tick form-control" data-size="8">
									<option value="${textbookPlan.planId}">${textbookPlan.planName}</option>
								</select>
							</div>
						</div>
						<br><br>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top" style="width: 10%"><small class="text-red">*</small>发放教材</label>
							<div class="col-sm-10">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
										<tr>
											<th><input type="checkbox" class="select-all"></th>
											<th>学期</th>
											<th>教材计划</th>
											<th>书号</th>
											<th>教材名称</th>
											<th>教材类型</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="textbook" items="${textbookList}">
											<tr>
												<td>
													<input type="checkbox" name="ids" data-id="" data-name="check-id" value="${textbook.textbookId}">
												</td>
												<td>${textbookPlan.gjtGrade.gradeName}</td>
												<td>
													${textbookPlan.planName} <br>
													<span class="gray9">${textbookPlan.planCode}</span>
												</td>
												<td>${textbook.textbookCode}</td>
												<td>${textbook.textbookName}</td>
												<td>
													<c:choose>
														<c:when test="${textbook.textbookType == 1}">主教材</c:when>
														<c:otherwise>复习资料</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="box-footer text-right">
						<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="cancel" onclick="history.back()">取消</button>
						<button type="submit" class="btn btn-primary min-width-90px" data-role="sure">确定</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

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
		    	//msg：提示信息;
			    //o:{obj:*,type:*,curform:*},
			    //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
			    //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
			    //curform为当前form对象;
			    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
	
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
	            alert('请选择发放教材');
	            return false;
	        }
	        var ids = '';
	        $(':input[name="ids"]:checked').each(function(i, v) {
	        	ids = ids + v.value + ',';
	        });
	        $(':input[name="textbookIds"]').val(ids.substring(0, ids.length - 1));  
			
		    window.postIngIframe=$.formOperTipsDialog({
				text:'数据提交中...',
				iconClass:'fa-refresh fa-spin'
			});
		  },
		  callback:function(data){
			  if (data.successful) {
				  window.location.href = "${ctx}/textbookArrange/list";
			  } else {
				  alert(data.message);
			  }
		  }
		});
	
	})();
</script>

</body>
</html>