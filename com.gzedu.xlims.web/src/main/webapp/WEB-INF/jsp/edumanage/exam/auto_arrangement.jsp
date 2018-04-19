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
<body>
<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">自动排考</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li class="process-step2 actived cur"><i></i>1.选择计划及考点</li>
			<li class="process-step3">2.排考结果反馈</li>
		</ol>

		<div class="step-box">
			<!-- 1.选择计划及考点 -->
			<div class="step-item">
				<div class="box-body pos-rel overlay-wrapper process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block text-center">
								<p class="text-bold f16">
								请选择你要自动排考的考试计划，并选择考点，<br>如不选择考点，则对所有考点自动排考。
								</p>
								<form id="uploadForm" action="${ctx}/exam/new/student/room/plan" method="post">
									<input type="hidden" name="examType" value="${param.examType}">
									<div class="col-xs-8 col-xs-offset-2">
										<div class="row">
										<div class="col-sm-3">考试计划：</div>
										<div class="col-sm-9">
										<select id="examBatchCode" name="examBatchCode" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
											<c:forEach items="${batchMap}" var="map">
												<option value="${map.key}">${map.value}</option>
											</c:forEach>
										</select>
										</div>
										</div>
										<br>
										<div class="row">
										<div class="col-sm-3">考点：</div>
										<div class="col-sm-9">
										<select id="examPoint" name="examPointId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="-1">请选择考点</option>
										</select>
										</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
					<div class="overlay uploading-overlay">
						<div class="uploading-txt text-center">
							<i class="fa fa-refresh fa-spin vertical-mid"></i>
							<span class="inlineblock left10">排考中，请稍后...</span>
						</div>
	                </div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 import-sure-btn">开始排考</button>
				</div>
			</div>

			<!-- 2.排考结果反馈-成功 -->
			<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="" style="width:305px;margin:0 auto;">
									<p class="text-bold f16 margin_b20">
									已完成<span id="totalCount"></span>条预约考试记录的排考，排考结果如下:
									</p>
									<p class="margin_b20">
										<span class="glyphicon glyphicon-ok-circle text-green vertical-mid"  style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-green margin_r15">排考成功：<span id="successCount"></span>条</span>
										&nbsp;
		              					<a id="successFileName" href="javascript:void(0)" class="btn btn-default btn-sm left10"><span>下载排考成功记录</span></a>
									</p>
									<p class="">
										<span class="glyphicon glyphicon-remove-circle text-red vertical-mid" style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-red margin_r15">排考失败：<span id="failedCount"></span>条</span>
										&nbsp;
										<a id="failedFileName" href="javascript:void(0)" class="btn btn-primary btn-sm left10"><span>下载排考失败记录</span></a>
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="close-pop">完成</button>
				</div>
			</div>

			<!-- 2.排考结果反馈-失败 -->
			<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="text-center">
									<span class="glyphicon glyphicon-ban-circle text-red vertical-mid margin_r10" style="font-size:40px;"></span>
									<span id="message">
										
									</span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 re-select">重新选择</button>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

$(function(){
	
	$('#examBatchCode').change(function(){
		var examBatchCode = $('#examBatchCode').val();
		if(examBatchCode!=''){
			$.get("${ctx}/exam/new/batch/queryExamPoint",{examBatchCode:examBatchCode},function(data,status){
				$('#examPoint').empty();
				$("#examPoint").append("<option value='-1'>请选择考点</option>");
				$.each(data, function (i) {
					$("#examPoint").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
	        	});
				$('#examPoint').selectpicker('refresh'); 
			  },"json");
		}
	});
	$('#examBatchCode').change();

  	//确定导入
    $(".import-sure-btn").click(function(event) {
    	var $overlay=$(".uploading-overlay");
    	var $that=$(this);
    	$overlay.show();
    	$that.hide();
    	
    	$("#uploadForm").ajaxSubmit({
    		dataType: "json",
			success: function(data) {
				if (data.successful) {
					$("#totalCount").html(data.totalCount);
					$("#successCount").html(data.successCount);
					$("#failedCount").html(data.failedCount);
					$("#successFileName").attr("href", ctx+"/excelImport/downloadResult?path=autoArrangement&name="+data.successFileName);
					$("#failedFileName").attr("href", ctx+"/excelImport/downloadResult?path=autoArrangement&name="+data.failedFileName);
					$(".step-item").eq(0).hide().next().show();
				} else {
					$("#message").html(data.message);
					$(".step-item").eq(0).hide();
		    		$(".step-item").eq(2).show();
				}
				
				$(".process-step3").addClass('actived cur').siblings().removeClass('cur');
				$overlay.hide();
				$that.show();
			}
		});
    });

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

    //点击'重新选择'
    $(".re-select").click(function(event) {
    	$(".step-item").eq(2).hide();
    	$(".step-item").eq(0).show();
    	$(".process-step3").removeClass('actived cur');
  		$(".process-step2").addClass('actived cur');
    });
});
</script>
</body>
</html>