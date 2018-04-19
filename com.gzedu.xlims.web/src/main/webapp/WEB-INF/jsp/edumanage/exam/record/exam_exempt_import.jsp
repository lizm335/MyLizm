<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>导入免修\统考成绩</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title"> 导入免修\统考成绩</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li data-role="step1" class="process-step1 actived cur"><i></i>1.下载模板</li>
			<li data-role="step2" class="process-step2"><i></i>2.验证身份</li>
			<li data-role="step3" class="process-step2"><i></i>3.导入数据</li>
			<li data-role="step4" class="process-step3">4.导入结果反馈</li>
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
							为了方便你的工作，我们已经准备好了《成绩登记表》的标准模板<br>
							你可以点击下面的下载按钮，下载标准模板。
						</div>
					</div>
					<div class="text-center pad-t30 margin_t10">
						<a href="downRecordResult?execleFileName=getExemptRecordImport" class="btn btn-primary btn-lg" style='width:220px;'>下载标准模板</a>
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

			<!-- 2 验证身份 -->
			<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-cell-block vertical-mid">
							<div class="text-center">
								<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>
								<div class="inline-block vertical-mid text-left">
									<c:if test="${empty sjhs }">
										为了确保数据安全，导出数据需要手机验证，你的手机号码没有填写，请到个人资料填写手机号码。
									</c:if>
									<c:if test="${not empty sjhs }">
										为了确保数据安全，请用你尾号为 <b class="text-light-blue">${sjhs }</b>的手机号获取验证码，进行身份验证。
									</c:if>
								</div>
							</div>
							<c:if test="${not empty sjhs }">
								<div class="text-center pad-t30 margin_t10 col-xs-8 col-xs-offset-2 pos-rel">
									<div class="input-group input-group-md">
					                    <input type="text" class="form-control tel-vercode">
					                    <span class="input-group-btn">
					                    	<button class="btn btn-primary" type="button" data-role="get-ver-code">获取验证码</button>
					                    </span>
			      					</div>
			      					<div class="ver-tips text-left" style="display:none;position:absolute;bottom:0px;left:0;margin:0 0 -24px 15px;">
			      						<span class="glyphicon glyphicon-remove-circle vertical-mid"></span>
			      						<span data-role="ver-tips-txt">
			      							验证码错误
			      						</span>
			      					</div>
								</div>
							</c:if>
						</div>
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<c:if test="${not empty sjhs }">
					<button type="button" class="btn btn-success min-width-90px margin_l15 disabled" data-control="btn" disabled>下一步</button>
					</c:if>
				</div>
			</div>

			<!-- 3 导入数据 -->
			<div class="step-item" style="display:none">
				<div class="box-body pos-rel overlay-wrapper process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid ">
								<p class="text-bold f16 text-center">
								请选择你要导入的《成绩登记表》
								</p>
								<form id="uploadForm" name="uploadForm" action="importExemptRecord" method="post" enctype="multipart/form-data">
								<div class="pad-t10 col-xs-8 col-xs-offset-2 no-float">
									<div class="input-group input-group-md">
					                    <input type="text" class="form-control upload-input">
					                    <span class="input-group-btn">
					                    	<input type="file" name="file" id="file" class='hide'>
					                    	<button class="btn btn-primary upload-btn" type="button">选择文件</button>
					                    </span>
                  					</div>
								</div>
								</form>
								<div class="alert custom-alert center-block margin_t30 margin-bottom-none f12" style="width:85%;">
									<ol class="pad-l20 margin-bottom-none">
					    				<li>为避免无法导入，请在导入成绩前仔细检查成绩登记表是否按照模板要求填写。</li>
					    				<li>导入时会严格核对学习成绩和网考成绩是否与系统中的一致，一旦存在差异，该条数据将无法导入。</li>
					    				<li>若遇到导入失败的情况，请下载导入失败表，根据提示调整后重新导入。</li>
					    			</ol>
								</div>
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
		</div>
	</div>
</div>
<script type="text/javascript">

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
	$("#uploadForm").submit();
});

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//点击进入‘下一步’
$(".next-btn").click(function(event) {
	$(".step-item").eq(0).hide().next().show();
	$('[data-role="step2"]').addClass('actived cur').siblings().removeClass('cur');
});

//点击验证和进入‘下一步’
$('[data-control="btn"]').click(function(event) {
	$(".step-item").eq(1).hide().next().show();
    $('[data-role="step3"]').addClass('actived cur').siblings().removeClass('cur');
});

var vercode = "";

//短信验证
$('[data-role="get-ver-code"]').click(function(event){
	event.preventDefault();
	
	$.post('<%=request.getContextPath()%>/exam/new/record/getSmsCode.do',{},function(re){
		if (re.result=="success") {
			vercode = re.code;
		}
	},"json");
	
	var that=$(this);
	if(!that.hasClass("disabled")){
		that.addClass("disabled").prop('disabled', true).html('重新获取<span class="count-down2">(60)</span>');
		var timer,i = 60;
		var fn = function () {
			if(i<=0){
				that.removeClass("disabled").prop('disabled', false).find(".count-down2").html("");
				clearInterval(timer);
			}
			else{
				that.find(".count-down2").html("("+i+")");
				i --;
			}
		};
		timer = setInterval(fn, 1000);
		fn();
	}
});

//验证码输入
$(".tel-vercode").keyup(function(event) {
	//验证码输入
	$(".tel-vercode").keyup(function(event) {
		var $formControl=$('[data-control="btn"]');
		var $verTips=$(".ver-tips");
		if($.trim(this.value)==""){
			$verTips.show().addClass('text-red').removeClass('text-green').find('[data-role="ver-tips-txt"]').text("请输入验证码");
			$verTips.find("glyphicon").addClass('glyphicon-remove-circle').removeClass('glyphicon-ok-circle')
		}
		else{	
			$verTips.show();
			if(vercode == ""){
				$verTips.addClass('text-red').removeClass('text-green').find('[data-role="ver-tips-txt"]').text("验证码错误");
				$verTips.find("glyphicon").addClass('glyphicon-remove-circle').removeClass('glyphicon-ok-circle');
				$formControl.prop('disabled', true).addClass('disabled');
			}
			else if(this.value==vercode){
				$verTips.addClass('text-green').removeClass('text-red').find('[data-role="ver-tips-txt"]').text("验证码正确");
				$verTips.find("glyphicon").addClass('glyphicon-ok-circle').removeClass('glyphicon-remove-circle');
				$formControl.prop('disabled', false).removeClass('disabled');
			}
			else{
				$verTips.addClass('text-red').removeClass('text-green').find('[data-role="ver-tips-txt"]').text("验证码错误");
				$verTips.find("glyphicon").addClass('glyphicon-remove-circle').removeClass('glyphicon-ok-circle');
				$formControl.prop('disabled', true).addClass('disabled');
			}
		}
	});
	
});

//点击'重新选择'
$(".re-select").click(function(event) {
	$(".upload-input").val('');
	$(".input-group-btn").children('input:file').val('');
	$(".import-sure-btn").prop("disabled",true).addClass('disabled');
	$(".step-item").eq(3).hide();
	$(".step-item").eq(2).show();
	$(".process-step3").removeClass('actived cur');
		$(".process-step2").addClass('actived cur');
});

</script>
</body>
</html>