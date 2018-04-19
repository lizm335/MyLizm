<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>排考安排</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	
	
	
	//导出
	$('[data-role="export"]').click(function(event) {
		
		var examBatchCode = $('#examBatchCode').val();
		
		if(examBatchCode == ""){
			alert("请选择批次");
		}else{
			var $overlay=$(".overlay");
			var $that=$(this);
			$overlay.show();
			$(this).addClass("disabled").prop('disabled', true);
			
			setTimeout(function(){
				$overlay.hide();
				$('[data-role="export"]').removeClass("disabled").prop('disabled', false);
			},5000);
			$('#exportRoomSeat').submit();
		}
	}); 
	
	$('#examBatchCode').change(function(){
		var examBatchCode = $('#examBatchCode').val();
		if(examBatchCode!=''){
			$.get("${ctx}/exam/new/batch/queryExamPoint",{examBatchCode:examBatchCode},function(data,status){
				$('#examPointId').empty();
				$("#examPointId").append("<option value=''>请选择</option>");
				$.each(data, function (i) {
					$("#examPointId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
	        	});
			  },"json");
		}else{
			$('#examPointId').empty();
			$("#examPointId").append("<option value=''>请选择</option>");
			$("#examPointId").val("").trigger("change");
		}
	}); 
	
	
	$('#examPointId').change(function(){
			isArrangeOver();
	});
	
	$('#examType').change(function(){
			isArrangeOver();
	});
	
	function isArrangeOver(){
		var examBatchCode = $('#examBatchCode').val();
		var examPointId = $('#examPointId').val();
		var examType = $('#examType').val();
		if($('#examPointId').val() != '' && $('#examBatchCode').val()!=''){
			$.get("isArrangeOver",{examBatchCode:examBatchCode,examPointId:examPointId,examType:examType},function(data,status){
				if(data.successful){
					if(!data.obj){
						$('[data-role="export"]').addClass("disabled",true);
						alert(data.message);
					}else{
						$('[data-role="export"]').removeClass("disabled").prop('disabled', false);
					}
				}
			  },"json");
		}else{
			$('[data-role="export"]').addClass("disabled",true);
		}
	}

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });
})

</script>

</head>

<body>
<form id="exportRoomSeat" action="exportRoomSeat" method="post">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">导出考场签到表</h3>
	</div>
	
	<div class="box-body pos-rel overlay-wrapper">
		<div class="pad-t10">
			<div class="form-group center-block" style="width:75%;">
	          	<h4>请选择要导出考场签到表的考试批次</h4>
	          	<div>
			        <select id="examBatchCode" name="examBatchCode" class="form-control select2">
			        	<option value="">请选择</option>
						<c:forEach items="${batchMap}" var="map">
							<option value="${map.key}">${map.value}</option>
						</c:forEach>
					</select>
		        </div>
	        </div>
			<div class="form-group center-block" style="width:75%;">
	          	<h4>请选择要导出考场签到表的考试考点</h4>
	          	<div>
			        <select id="examPointId" name="examPointId" class="form-control select2">
			        	<option value="">请选择</option>
					</select>
		        </div>
	        </div>
			<div class="form-group center-block" style="width:75%;">
	          	<h4>请选择要导出考场签到表的考试类型</h4>
	          	<div>
			        <select id="examType" name="examType" class="form-control select2">
			        	<option value="8">笔试</option>
			        	<option value="11">机考</option>
					</select>
		        </div>
	        </div>
	        <div class="custom-alert center-block margin_t30 margin-bottom-none f12" style="width:75%; padding-top: 20px; padding-bottom: 20px;">
	        	<ol class="pad-l20 margin-bottom-none">
    				<li>导出考场签到表前，请确认已经完成本考试批次所有预约考试的排考工作</li>
    				<li>只能导出完成排考的考场签到表。考场中若有未排考的预约数据，则不能被导出</li>
    				<li>导出过程可 能需要几分钟，请耐心等待。</li>
    			</ol>
			</div>
		</div>
		<div class="overlay" style="display:none;">
			<div class="uploading-txt text-center">
				<i class="fa fa-refresh fa-spin vertical-mid"></i>
				<span class="inlineblock left10">数据正在导出中，请稍后...</span>
			</div>
        </div>
	</div>
	
</div>


<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="export" disabled="disabled">导出</button>
</div>

</form>
</body>


</html>
