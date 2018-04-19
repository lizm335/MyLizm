<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>导出选课记录</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">导出选课记录</h3>
	</div>
	<div class="box-body pos-rel overlay-wrapper">
		<div class="text-center" style="margin-top:60px;">
			<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>
			<div class="inline-block vertical-mid text-left">
				<c:if test="${empty sjhs }">
					<div class="text-bold f16">为了确保数据安全，导出数据需要手机验证，你的手机号码没有填写，请到个人资料填写手机号码。</div>
				</c:if>
				<c:if test="${not empty sjhs }">
					<div class="text-bold f16">按搜索框中设置的查询条件，你总共可以导出 <b class="text-light-blue">${resultMap.ALL_COUNT }</b> 条记录</div>
					<div class="gray9">
					为了确保数据安全，请用你尾号为${sjhs }的手机号获取验证码，进行身份验证。
					</div>
				</c:if>
			</div>
		</div>
		<c:if test="${not empty sjhs }">
			<div class="text-center pad-t30 margin_t10 col-xs-8 col-xs-offset-2 pos-rel">
				<div class="input-group input-group-md">
	                <input type="text" class="form-control tel-vercode" placeholder="请输入手机验证码">
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
	<c:if test="${not empty sjhs }">
		<button type="button" class="btn btn-success min-width-90px disabled" data-role="export" disabled>导出</button>
	</c:if>
</div>
<form action="expRecordRecResult" id="expRecordRecResult" method="post" style="display: none;">
	<input type="hidden" name="XM" value="${resultMap.XM }">
	<input type="hidden" name="XH" value="${resultMap.XH }">
	<input type="hidden" name="SFZH" value="${resultMap.SFZH }">
	<input type="hidden" name="PYCC" value="${resultMap.PYCC }">
	<input type="hidden" name="GRADE_ID" value="${resultMap.GRADE_ID }">
	<input type="hidden" name="SPECIALTY_ID" value="${resultMap.SPECIALTY_ID }">
	<input type="hidden" name="XXZX_ID" value="${resultMap.XXZX_ID }">
	<input type="hidden" name="BJMC" value="${resultMap.BJMC }">
	<input type="hidden" name="TEACH_NAME" value="${resultMap.TEACH_NAME }">
	<input type="hidden" name="OPEN_GRADE_ID" value="${resultMap.OPEN_GRADE_ID }">
	<input type="hidden" name="KCMC" value="${resultMap.KCMC }">
	<input type="hidden" name="REBUILD_STATE" value="${resultMap.REBUILD_STATE }">
</form>
<script type="text/javascript">

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

var vercode = "";

//短信验证
$('[data-role="get-ver-code"]').click(function(event){
	event.preventDefault();
	
	$.ajax({
        type:"POST",
        dataType:"json",
        data:{},
        async:false,
        url:"${ctx}/studymanage/getMessageCode",
        success:function (data) {
            if(data.successful==false||data.successful=='false'){
                alert("发送验证码失败，请稍后重试！");
                return;
            } else {
            	vercode = data.obj;
            }
        }
    });
	
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
		var $formControl=$('[data-role="export"]');
		var $verTips=$(".ver-tips");
		if($.trim(this.value)==""){
			$verTips.show().addClass('text-red').removeClass('text-green').find('[data-role="ver-tips-txt"]').text("请输入验证码");
			$verTips.find("glyphicon").addClass('glyphicon-remove-circle').removeClass('glyphicon-ok-circle')
		}
		else{	
			$verTips.show();
			if(vercode == "******"){
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

//确定验证
$('[data-role="export"]').click(function(event) {
	$("#exam_batch_code").val($("#examBatchCode").val());
	var $overlay=$(".overlay");
	var $that=$(this);
	$overlay.show();
	$that.addClass("disabled").prop('disabled', true);
	$("#expRecordRecResult").submit();
	setTimeout(function(){
		$overlay.hide();
		setTimeout(function(){
			$.closeDialog($("[data-id='dialog-1']"));
			$that.removeClass("disabled").prop('disabled', false);
		},2000);
	},10000);
});
</script>
</body>
</html>