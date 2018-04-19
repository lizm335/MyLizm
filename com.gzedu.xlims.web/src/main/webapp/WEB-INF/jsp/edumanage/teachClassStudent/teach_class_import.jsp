<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>导入批量调班信息</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">导入批量调班信息</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li class="process-step1 actived cur"><i></i>1.下载批量调班信息表</li>
			<li class="process-step2"><i></i>2.导入创建批量调班信息</li>
			<li class="process-step3">3.导入结果</li>
		</ol>

		<div class="step-box">
			<!-- 1 下载模板 -->
			<div class="step-item">
				<div class="box-body process-cnt-box">
					<div class="media pad-t30 left10 margin_t10 margin_r15">
						<div class="media-left media-middle">
							<i class="fa fa-fw fa-exclamation-circle" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>
						</div>
						<div class="media-body">
							导入调班信息前，建议先下载《批量调班信息表》根据表设置好相关参数再导入。
						</div>
					</div>
					<div class="text-center pad-t30 margin_t10">
						<a href="javascript:void(0);" class="btn btn-primary btn-lg download_file" data-file="template">下载批量调班信息表</a>
					</div>
					<div class="text-center text-red f12 pad-t30 margin_t10">
						注：为了保障导入修改能够顺利完成，请严格按照导出的表格要求进行设置
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 next-btn">下一步</button>
				</div>
			</div>

			<!-- 2 导入数据 -->
			<div class="step-item" style="display:none">
				<form id="uploadForm" name="uploadForm" action="importStuClassInfo" method="post" enctype="multipart/form-data">
				<div class="box-body pos-rel overlay-wrapper process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="media pad-t10 left10 margin_r15">
									<div class="media-left media-middle">
										<i class="fa fa-fw fa-exclamation-circle" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>
									</div>
									<div class="media-body">
										请选择你要导入的《批量调班信息表》，为保障导入修改能够顺利完成，导入前请确保表格模板正确，内容已按要求填写。
									</div>
								</div>
								<div class="pad-t30 col-xs-8 col-xs-offset-2">
									<div class="input-group input-group-md">
										<input type="text" class="form-control upload-input">
					                    <span class="input-group-btn">
					                    	<input type="file" name="file" id="file" class='hide'>
					                    	<button class="btn btn-primary upload-btn" type="button">选择文件</button>
					                    </span>
                  					</div>
								</div>
							</div>
						</div>
					</div>
					<div class="overlay uploading-overlay">
						<div class="uploading-txt text-center">
							<i class="fa fa-refresh fa-spin vertical-mid"></i>
							<span class="inlineblock left10">导入中，请稍后...</span>
						</div>
	                </div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 import-sure-btn disabled" disabled>导入</button>
				</div>
				</form>
			</div>

			<!-- 3 导入结果反馈-成功 -->
			<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="exception" style="width:305px;margin:0 auto;">
									<p class="text-bold f16 margin_b20 all_num"></p>
									<p class="margin_b20">
										<span class="glyphicon glyphicon-ok-circle text-green vertical-mid"  style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-green margin_r15 success_num"></span>&nbsp;
										<button class="btn btn-default btn-sm left10 download_file success_file">下载导入成功记录</button>
									</p>
									<p class="">
										<span class="glyphicon glyphicon-remove-circle text-red vertical-mid" style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-red margin_r15 error_num"></span>&nbsp;
										<button class="btn btn-primary btn-sm left10 download_file error_file">下载导入失败记录</button>
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
									<span>
										你导入的表格式不正确，请核查后再导入!
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
    	$(".upload-btn").siblings("input:file").click()
    });
    $(".upload-btn").click(function(){
        $(this).siblings("input").click();
    }).siblings("input:file").change(function(){
    	if(this.value!=""){
	        $(".upload-input").val(this.value);
	        $(".import-sure-btn").prop("disabled",false).removeClass('disabled');
	    }
    });

	//下载文件
	$(".download_file").click(function(event) {
		window.location.href="download?file="+$(this).attr("data-file");
	});

	//	异步导入文件
	$(".import-sure-btn").click(function(event) {
		var fileName = $(".upload-input").val();
		$(".uploading-overlay").show();
		$(this).hide();
		var formData = new FormData($('#uploadForm')[0]);
		$.ajax({
			url: 'importStuClassInfo.do',
			type: 'POST',
			dataType:'json',
			beforeSend: function(re){},
			success: function(re){
				console.log(re);
				if("success" == re.msg ){
					$(".all_num").text("本次共修改了"+re.all_num+"个记录，设置结果如下:");
					$(".success_num").text("成功："+re.success_num+"条");
					$(".error_num").text("失败："+re.error_num +"条");
					$(".success_file").attr("data-file",re.success_file);
					$(".error_file").attr("data-file",re.error_file);
					$(".step-item").eq(1).hide().next().show();
				}else{
					$(".exception").text(re.exception);
					$(".step-item").eq(1).hide().next().next().show();
				}

				$(".uploading-overlay").hide();
				$(".process-step3").addClass('actived cur').siblings().removeClass('cur');
			},
			error: function(re){
				console.log(re,'error');
				$(".uploading-overlay").hide();
				$(".process-step3").addClass('actived cur').siblings().removeClass('cur');
			},
			data: formData,
			cache: false,
			contentType: false,
			processData: false
		});

	});

	//关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$("button[data-dismiss='modal']").click();
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
    	$(".import-sure-btn").prop("disabled",true).addClass('disabled').show();
    	$(".step-item").eq(3).hide();
    	$(".step-item").eq(1).show();
    	$(".process-step3").removeClass('actived cur');
  		$(".process-step2").addClass('actived cur');
    });
})
</script>
</body>
</html>