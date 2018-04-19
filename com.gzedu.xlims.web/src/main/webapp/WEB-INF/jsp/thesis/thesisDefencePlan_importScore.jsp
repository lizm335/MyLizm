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
		<h3 class="box-title">批量导入答辩成绩</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li class="process-step1 actived cur"><i></i>1.下载模板</li>
			<li class="process-step2"><i></i>2.导入数据</li>
			<li class="process-step3">3.导入结果反馈</li>
		</ol>

		<div class="step-box">
			<!-- 1 下载模板 -->
			<div class="step-item">
				<div class="box-body process-cnt-box">
					<div class="media left10 pad-t15 margin_r15">
						<div class="media-left media-middle">
							<i class="fa fa-fw fa-exclamation-circle" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>
						</div>
						<div class="media-body">
							为了方便你的工作，我们已经准备好了《答辩成绩导入表》的标准模板<br>
							你可以点击下面的下载按钮，下载标准模板。
						</div>
					</div>
					<div class="text-center pad-t30 margin_t10">
						<a class="btn btn-primary btn-lg" style='width:220px;' href="${ctx}/excel/download?name=答辩成绩导入表.xls">下载标准模板</a>
					</div>
					<div class="text-center text-red f12 pad-t30 margin_t10">
						注：为了能够准确的导入数据，请务必按照标准模板的要求进行填写
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 next-btn">下一步</button>
				</div>
			</div>

			<!-- 2 导入数据 -->
			<div class="step-item" style="display:none">
				<div class="box-body pos-rel overlay-wrapper process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid text-center">
								<p class="text-bold f16">
								请选择你要导入的《答辩成绩导入表》，并选择需要导入到的论文计划
								</p>
								<form id="uploadForm" action="${ctx}/thesisDefencePlan/importScore" enctype="multipart/form-data" method="post">
									<div class="pad-t30 col-xs-8 col-xs-offset-2">
										<select name="thesisPlanId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
											<c:forEach items="${thesisPlanMap}" var="map">
												<option value="${map.key}">${map.value}</option>
											</c:forEach>
										</select>
										<br>
										<br>
										<div class="input-group input-group-md">
						                    <input type="text" class="form-control upload-input">
						                    <input type="file" id="fileInput" class="hide" name="file" />
						                    <span class="input-group-btn">
						                    	<button class="btn btn-primary upload-btn" type="button">选择文件</button>
						                    </span>
	                  					</div>
									</div>
								</form>
							</div>
						</div>
					</div>
					<div class="overlay uploading-overlay">
						<div class="uploading-txt text-center">
							<i class="fa fa-refresh fa-spin vertical-mid"></i>
							<span class="inlineblock left10">数据正在导入中，请稍后...</span>
						</div>
	                </div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 import-sure-btn disabled" disabled>导入</button>
				</div>
			</div>

			<!-- 3 导入结果反馈-成功 -->
			<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="" style="width:305px;margin:0 auto;">
									<p class="text-bold f16 margin_b20">
									你本次共导入<span id="totalCount"></span>条信息，导入结果如下:
									</p>
									<p class="margin_b20">
										<span class="glyphicon glyphicon-ok-circle text-green vertical-mid"  style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-green margin_r15">导入成功：<span id="successCount"></span>条</span>
										&nbsp;
		              					<a id="successFileName" href="javascript:void(0)" class="btn btn-default btn-sm left10"><span>下载导入成功记录</span></a>
									</p>
									<p class="">
										<span class="glyphicon glyphicon-remove-circle text-red vertical-mid" style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-red margin_r15">导入失败：<span id="failedCount"></span>条</span>
										&nbsp;
										<a id="failedFileName" href="javascript:void(0)" class="btn btn-primary btn-sm left10"><span>下载导入失败记录</span></a>
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

			<!-- 3 导入结果反馈-失败 -->
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
    
    //选择导入文件
    $(".upload-input").click(function(){
    	$("#uploadForm").find("input:file").click();
    });
    $(".upload-btn").click(function(){
    	$("#uploadForm").find("input:file").click();
    });
    $("#uploadForm").find("input:file").change(function(){
    	if(this.value!=""){
	        $(".upload-input").val(this.value);
	        $(".import-sure-btn").prop("disabled",false).removeClass('disabled');
	    }
    });

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
					$("#successFileName").attr("href", ctx+"/excelImport/downloadResult?path=thesisDefencePlan&name="+data.successFileName);
					$("#failedFileName").attr("href", ctx+"/excelImport/downloadResult?path=thesisDefencePlan&name="+data.failedFileName);
					$(".step-item").eq(1).hide().next().show();
				} else {
					$("#message").html(data.message);
					$(".step-item").eq(1).hide();
		    		$(".step-item").eq(3).show();
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

    //点击进入‘下一步’
    $(".next-btn").click(function(event) {
    	$(".step-item").eq(0).hide().next().show();
    	$(".process-step2").addClass('actived cur').siblings().removeClass('cur');
    });

    //点击'重新选择'
    $(".re-select").click(function(event) {
    	$(".upload-input").val('');
    	$(".input-group-btn").children('input:file').val('');
    	$(".import-sure-btn").prop("disabled",true).addClass('disabled');
    	$(".step-item").eq(3).hide();
    	$(".step-item").eq(1).show();
    	$(".process-step3").removeClass('actived cur');
  		$(".process-step2").addClass('actived cur');
    });
});
</script>
</body>
</html>