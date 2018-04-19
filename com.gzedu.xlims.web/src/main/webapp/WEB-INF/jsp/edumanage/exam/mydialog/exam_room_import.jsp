<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>导入考场信息</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>

<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">批量导入考场</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li class="process-step1 actived cur"><i></i>1.下载模板</li>
			<li class="process-step2"><i></i>2.导入数据</li>
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
							为了方便你的工作，我们已经准备好了《考点导入表》的标准模板你可以点击下面的下载按钮，下载标准模板
						</div>
					</div>
					
					<div class="text-center margin_t20">
						<a href="downloadXLS" class="btn btn-primary btn-lg" style="width:220px;">下载标准模板</a>
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
				<div class="box-body pos-rel overlay-wrapper process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="media pad-t10 center-block" style="width:65%;">
									<div class="media-left media-middle">
										<i class="fa fa-exclamation-circle" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>
									</div>
									<div class="media-body">
										请选择你要导入的《考点导入表》，并选择需要导入到的考试批次
									</div>
								</div>
								<div class="form-group margin_t20 center-block" style="width:55%;">
							        <select id="examBatchCode" name="search_EQ_examPonitNew" class="form-control select2">
										 <option value="">请选择</option>
										 <c:forEach items="${batchMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param['search_EQ_examPonitNew.examBatchCode']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>       	
									</select>
						        </div>
								<div class="pad-t15 center-block" style="width:55%;">
									<div class="input-group input-group-md">
										<form action="importExamRoomXls" class="form-horizontal" name="search_EQ_batchForm" id="search_EQ_batchForm" method="post" enctype="multipart/form-data">
											
										</form>
					                    <input type="text" class="form-control upload-input">
					                    <span class="input-group-btn">
					                    	<input type="file" class='hide'>
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
							<span class="inlineblock left10">考试时间设置中，请稍后...</span>
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
								<div class="" style="width:315px;margin:0 auto;">
									<p class="text-bold f16 margin_b20">
									你本次共导入1380条信息，导入结果如下：
									</p>
									<p class="margin_b20">
										<span class="glyphicon glyphicon-ok-circle text-green vertical-mid"  style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-green margin_r15">设置成功：1000条</span>

										<button class="btn btn-default btn-sm left10">下载导入成功记录</button>
									</p>
									<p class="">
										<span class="glyphicon glyphicon-remove-circle text-red vertical-mid" style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-red margin_r15">设置失败：380条</span>
										&nbsp;
										<button class="btn btn-primary btn-sm left10">下载导入失败记录</button>
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

    //确定导入
    $(".import-sure-btn").click(function(event) {
    	var $overlay=$(".uploading-overlay");
    	var $that=$(this);
    	$overlay.show();
    	$that.hide();
    	setTimeout(function(){//实际开发时请删除
    		if(Math.random()>=0.5){//实际开发时请删除
	    		$(".step-item")
	    		.eq(1).hide()
	    		.next().show();//导入成功时显示
	    	}//实际开发时请删除
	    	else{//实际开发时请删除

	    		//导入失败时显示
	    		$(".step-item").eq(1).hide();
	    		$(".step-item").eq(3).show();

	    	}//实际开发时请删除
    		$overlay.hide();
    		$that.show();

    		$(".process-step3").addClass('actived cur').siblings().removeClass('cur');
    	},2000);
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
    	$(".import-sure-btn").prop("disabled",true).addClass('disabled');
    	$(".step-item").eq(3).hide();
    	$(".step-item").eq(1).show();
    	$(".process-step3").removeClass('actived cur');
  		$(".process-step2").addClass('actived cur');
    });
})
</script>
</body>
</html>