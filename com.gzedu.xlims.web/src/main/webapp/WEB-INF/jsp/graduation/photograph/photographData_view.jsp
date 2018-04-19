<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" id="go_for_back" onclick="window.history.back()">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li class="active">拍摄资料</li>
		</ol>
	</section>
	<section class="content" data-id="0">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li <c:if test="${type eq '1' }">class="active"</c:if>>
					<a href="${ctx}/graduation/photograph/list?type=1" target="_self">学院分点</a>
				</li>
				<li <c:if test="${type eq '2' }">class="active"</c:if>>
					<a href="${ctx}/graduation/photograph/list?type=2" target="_self">新华社分点</a>
				</li>
				<li  <c:if test="${type eq '3'}">class="active"</c:if>>
					<a href="${ctx}/graduation/photograph/photographDataView?type=3" target="_self">拍摄资料</a>
				</li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" >
					<form id="theform" action="photographDataCreate" method="post">
						<input type="hidden" value="${info.id }" name="id">
						<div class="box box-border no-margin">
							<div class="box-header with-border">
								<h3 class="box-title text-no-bold">拍摄现场填写资料</h3>
							</div>
							<div class="box-body pad-t15">
								<div class="form-horizontal reset-form-horizontal">
									<div class="form-group">
										<label class="col-sm-3 control-label">院校代码</label>
										<div class="col-sm-6 position-relative">
											<input type="text" class="form-control" placeholder="院校代码" datatype="*" 
											nullmsg="请填写院校代码" errormsg="请填写院校代码" value="${info.schoolCode }" name="schoolCode">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label">（院）系代码</label>
										<div class="col-sm-6 position-relative">
											<input type="text" class="form-control" placeholder="（院）系代码" datatype="*" nullmsg="请填写（院）系代码" 
											errormsg="请填写（院）系代码" value="${info.schoolSetCode }" name="schoolSetCode">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label">所在校别</label>
										<div class="col-sm-6 position-relative">
											<input type="text" class="form-control" placeholder="所在校别" datatype="*" nullmsg="请填写所在校别"
											 errormsg="请填写所在校别" value="${info.inSchool }" name="inSchool">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label">收件人姓名</label>
										<div class="col-sm-6 position-relative">
											<input type="text" class="form-control" placeholder="收件人姓名" datatype="*" nullmsg="请填写收件人姓名" 
											errormsg="请填写收件人姓名" value="${info.recipients }" name="recipients">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label"><small class="text-red">*</small>收件人地址</label>
										<div class="col-sm-6 position-relative">
											<textarea type="text" class="form-control" rows="5" placeholder="收件人地址" datatype="*" 
											nullmsg="请填写收件人地址" errormsg="请填写收件人地址" name="recipientsAdd">${info.recipientsAdd }</textarea>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label">联系电话</label>
										<div class="col-sm-6 position-relative">
											<input type="text" class="form-control" placeholder="联系电话" datatype="*" nullmsg="请填写联系电话" 
											errormsg="请填写联系电话" value="${info.telePhone }" name="telePhone">
										</div>
									</div>
								</div>
							</div>
							<div class="box-footer text-center">
								<button type="submit" class="btn btn-success min-width-90px">保存</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
	//确认
	;(function(){
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
	      showAllError:true,
	      ignoreHidden:true,//是否忽略验证不可以见的表单元素
	      ajaxPost:true,
	      tiptype:function(msg,o,cssctl){
	        if(!o.obj.is("form")){
	            var msgBox=o.obj.closest('.position-relative').find('.tooltip');
	            msgBox.css({
	            	'z-index':2,
		            bottom:"100%",
		            'margin-bottom':-5
		        }).children('.tooltip-inner').text(msg);

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
	      },
	      callback:function(data){
	        	postIngIframe.find('[data-role="tips-text"]').html(data.message);
				postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
				setTimeout(function(){
					$.closeDialog(postIngIframe);
				},2000); 
	      }
	    });

	})();
	
	</script>
</html>
