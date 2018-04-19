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
		<h3 class="box-title">自动生成开考科目</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li class="process-step1 actived cur"><i></i>1.选择</li>
			<li class="process-step2"><i></i>2.初始化</li>
			<li class="process-step3">3.结果反馈</li>
		</ol>

		<div class="step-box">
			<!-- 1 下载模板 -->
			<div class="step-item">
				<div class="box-body process-cnt-box">
                    <div class="text-center pad-t30 margin_t10">
                        <a class="btn btn-primary btn-lg" style='width:300px;' href="javascript:void(0)">根据教学计划初始化开考科目</a>
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
						<div class="media left10 pad-t15 margin_r15">
							<div class="media-left media-middle">
								<i class="fa fa-fw fa-exclamation-circle" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>
							</div>
							<div class="media-body">
								确认自动生成？
							</div>
						</div>
						<div class="text-left text-red f12 pad-t30 margin_t10" style="margin-left: 80px;">
							说明：<br/>
							1. 系统会根据已设置试卷号和制定专业和课程自动生成中央课程开考科目；<br/>
							2. 试卷号、课程号自动生成通用专业开考科目。
						</div>
					</div>
					<div class="overlay uploading-overlay">
						<div class="uploading-txt text-center">
							<i class="fa fa-refresh fa-spin vertical-mid"></i>
							<span class="inlineblock left10">数据正在生成中，请稍后...</span>
						</div>
	                </div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 import-sure-btn">确认</button>
				</div>
			</div>

			<!-- 3 导入结果反馈-成功 -->
			<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="text-center">
									<span class="glyphicon glyphicon-ok-circle text-green vertical-mid margin_r10" style="font-size:40px;"></span>
									<span>
										生成成功
									</span>
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
					<button type="button" class="btn btn-success min-width-90px margin_l15 re-select" data-role="close-pop">重新选择</button>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

$(function(){

  	//确定导入
    $(".import-sure-btn").click(function(event) {
    	var $overlay=$(".uploading-overlay");
    	var $that=$(this);
    	$overlay.show();
    	$that.hide();

        $.ajax({
            type: "POST",
            url: ctx + "/exam/new/plan/autoExamPlan",
            data: {},
            dataType: "json",
			success: function(data) {
				if (data.successful) {
					$(".step-item").eq(1).hide().next().show();
				} else {
					$("#message").html(data.message);
					$(".step-item").eq(1).hide();
		    		$(".step-item").eq(3).show();
				}
				
				$(".process-step3").addClass('actived cur').siblings().removeClass('cur');
				$overlay.hide();
				$that.show();
			},
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("#message").html("服务器异常！");
                $(".step-item").eq(1).hide();
                $(".step-item").eq(3).show();
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