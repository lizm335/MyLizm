<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>导入考试科目</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">批量设置考试计划</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li class="process-step1 actived"><i></i>1.未设置考试时间的考试科目</li>
			<li class="process-step2 actived"><i></i>2.设置考试安排</li>
			<li class="process-step3 actived cur">3.导入结果</li>
		</ol>
		<div class="step-box">

			<c:if test="${empty resultMap.errorMessage }">
			<!-- 3 导入结果反馈-成功 -->
			<div class="step-item">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="" style="width:305px;margin:0 auto;">
									<p class="text-bold f16 margin_b20">
									你本次共导入设置了${resultMap.allNum }个考试时间的考试科目，导入结果如下:
									</p>
									<p class="margin_b20">
										<span class="glyphicon glyphicon-ok-circle text-green vertical-mid"  style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-green margin_r15">导入成功：${resultMap.rightNum }条</span>

										<a href="downPlanResult?execleFileName=${resultMap.RIGHT_DOWNPATH }"  class="btn btn-default btn-sm left10">下载导入成功记录</a>
									</p>
									<p class="">
										<span class="glyphicon glyphicon-remove-circle text-red vertical-mid" style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-red margin_r15">导入失败：${resultMap.errorNum }条</span>
										&nbsp;
										<a href="downPlanResult?execleFileName=${resultMap.ERROR_DOWNPATH }" class="btn btn-primary btn-sm left10">下载导入失败记录</a>
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
			</c:if>
			<c:if test="${not empty resultMap.errorMessage }">
			<!-- 3 导入结果反馈-失败 -->
			<div class="step-item">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="text-center">
									<span class="glyphicon glyphicon-ban-circle text-red vertical-mid margin_r10" style="font-size:40px;"></span>
									<span>
										${resultMap.errorMessage }
									</span>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 re-select" data-role="close-pop">确定</button>
				</div>
			</div>
			</c:if>
		</div>
	</div>
</div>
<script type="text/javascript">

$(function(){
    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$("button[data-dismiss='modal']").click();
    });
})
</script>
</body>
</html>