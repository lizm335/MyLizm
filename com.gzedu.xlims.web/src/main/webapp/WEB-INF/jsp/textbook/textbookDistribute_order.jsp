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

<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">导出待发教材订单</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li class="process-step1 actived cur"><i></i>1.导出待发教材订单</li>
			<li class="process-step3"><i></i>2.提交教材发放申请</li>
		</ol>

		<div class="step-box">
			<div class="step-item">
				<div class="box-body process-cnt-box" style="height:215px;">
					<div class="center-block margin_t30" style="width:80%">
						<!-- <div class="radio text-center">
	                        <label class="pad-r15">
	                          <input type="radio" name="r1" value="1" checked>
	                        	  旧生待发教材导出
	                        </label>
	                        <label class="margin_l15">
	                          <input type="radio" name="r1" value="2">
	                         	 新生待发教材导出
	                        </label>
	                    </div> -->
	                    	请选择需要导出待发教材订单的教材计划：
	                    <select id="planId" name="planId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<c:forEach items="${textbookPlanMap}" var="map">
								<option value="${map.key}">${map.value}</option>
							</c:forEach>
						</select>
						<div class="media pad-t10 margin_t20">
							<div class="media-left">
								<i class="fa fa-exclamation-circle text-yellow margin_r10" style="font-size:54px;"></i>
							</div>
							<div id="message" class="media-body media-middle">
								当前共有<b class="text-light-blue">${currentDistributeCount}</b>本待发教材
								<c:if test="${currentDistributeCount > 0 && currentDistributeCount > enoughCurrentDistributeCount}">
								，由于部分库存不足，当前只能发放<b class="text-light-blue">${enoughCurrentDistributeCount}</b>
								本待发教材
								</c:if>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="export-only">直接导出</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 re-select" data-role="export-commit">导出并提交教材发放申请</button>
				</div>
			</div>

			<div class="step-item" style="display:none">
				<div class="box-body pos-rel overlay-wrapper process-cnt-box" style="height:215px;">
					<div class="form-horizontal margin_t15">
						<form id="exportCurrentDistributeForm" class="form-horizontal" action="${ctx}/textbookDistribute/exportCurrentDistributeAndCommit">
						  <div class="form-group">
						    <label class="col-xs-3 control-label text-right">操作类型:</label>
						    <div class="col-xs-8">
						     	 教材发放
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="inputPassword" class="col-xs-3 control-label text-right">操作明细:</label>
						    <div class="col-xs-8">
						      <textarea id="description" name="description" class="form-control" rows="5" placeholder="请简要填写教材发放说明！"></textarea>
						    </div>
						  </div>
						</form>
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px re-select" data-role="commit">提交教材发放申请</button>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

$(function(){
	
	var currentDistributeCount = '${currentDistributeCount}';
	var textbookType = '${param.textbookType}';
	var planId = '${planId}';

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });
    
    $("#planId").change(function(event) {
    	planId = $(this).children('option:selected').val();
    	$.post("queryCurrentDistributeInfoJson", {textbookType:textbookType, planId:planId}, function(data){
    		currentDistributeCount = data.currentDistributeCount;
    		var msg = "当前共有<b class='text-light-blue'>" + currentDistributeCount + "</b>本待发教材";
    		if (currentDistributeCount > 0 && currentDistributeCount > data.enoughCurrentDistributeCount) {
    			msg += "，由于部分库存不足，当前只能发放<b class='text-light-blue'>" + data.enoughCurrentDistributeCount + "</b>本待发教材";
    		}
    		$("#message").html(msg);
	   	},"json");
    });

    $("button[data-role='export-commit']").click(function(event) {
    	if (currentDistributeCount == 0) {
    		return false;
    	}
    	$(".step-item").eq(0).hide().next().show();
    	$(".process-step2").addClass('actived cur').siblings().removeClass('cur');
    });

    $("button[data-role='export-only']").click(function(event) {
    	if (currentDistributeCount == 0) {
    		return false;
    	}
    	window.location.href = ctx + '/textbookDistribute/exportCurrentDistributeList?textbookType='+textbookType+'&planId='+planId;
    });

    $("button[data-role='commit']").click(function(event) {
    	if (currentDistributeCount == 0) {
    		return false;
    	}
    	
    	var description = $("#description").val();
		if (description == "") {
			alert("请输入操作明细！"); 
			return false;
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
		
		window.location.href = ctx + '/textbookDistribute/exportCurrentDistributeList?textbookType='+textbookType+'&planId='+planId;
		
		$.ajax({  
            type : "post",  
            url : "${ctx}/textbookDistribute/exportCurrentDistributeAndCommit",  
            dataType:'json',
            data : {textbookType:textbookType, planId:planId, description:description},  
            async : true,  
            success : function(data){  
                if(data.successful){
                	parent.$.closeDialog(postIngIframe);
                	parent.$.closeDialog(frameElement.api);
                } else {
                	alert(data.message);
                }  
            }
        });
		
    });
	
})
</script>
</body>
</html>
