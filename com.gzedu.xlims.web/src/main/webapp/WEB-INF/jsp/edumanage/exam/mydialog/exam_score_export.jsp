<%@page import="com.gzedu.xlims.common.constants.WebConstants"%>
<%@page import="com.gzedu.xlims.pojo.GjtUserAccount"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>导出成绩</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">

var vercode = "******";
$(function() {
	$(".box-body").height($(window).height()-126);
	//关闭 弹窗
	$("button[data-role='close-pop']").click(function(event) {
		parent.$.closeDialog(frameElement.api)
	});
	
	//导出
	$('[data-role="export"]').click(function(event) {
		var $overlay=$(".overlay");
		var $that=$(this);
		$overlay.show();
		$that.addClass("disabled").prop('disabled', true);
		setTimeout(function(){
			$overlay.hide();
			/*失败状态
			$.mydialog({
			  id:'dialog-1',
			  width:150,
			  height:50,
			  backdrop:false,
			  fade:true,
			  transparent:true,
			  showCloseIco:false,
			  content: '<div class="alert alert-danger alert-dismissable no-margin pad15"><h4 class="no-margin text-no-bold"><i class="icon fa fa-ban"></i> <span class="f16">导出失败！</span></h4></div>'
			});
			*/
			/*成功状态*/
			$.mydialog({
			  id:'dialog-1',
			  width:350,
			  height:50,
			  backdrop:false,
			  fade:true,
			  transparent:true,
			  showCloseIco:false,
			  content: '<div class="alert alert-success alert-dismissable no-margin pad15"><h4 class="no-margin text-no-bold"><i class="icon fa fa-check"></i> <span class="f16">导出成功，请耐心等待下载结果返回...</span></h4></div>'
			});
			
			/*服务器繁忙状态
			$.mydialog({
			  id:'dialog-1',
			  width:265,
			  height:50,
			  backdrop:false,
			  fade:true,
			  transparent:true,
			  showCloseIco:false,
			  content: '<div class="alert alert-warning alert-dismissable no-margin pad15"><h4 class="no-margin text-no-bold"><i class="icon fa fa-warning"></i> <span class="f16">服务器繁忙，请稍后再试！</span></h4></div>'
			});
			*/
			setTimeout(function(){
				$.closeDialog($("[data-id='dialog-1']"));
				$that.removeClass("disabled").prop('disabled', false);
			},5000);
	
		},2000);
	});
	//短信验证
	$('[data-role="get-ver-code"]').click(function(event){
		event.preventDefault();
		$.get("/edumanage/exam/score/getSmsCode",{},function(re){
			debugger;
			vercode = re.code;
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
		//var vercode="123";//测试数据，实际开发时动态生成
		var $formControl=$('[data-role="export"]');
		var $verTips=$(".ver-tips");
		if($.trim(this.value)==""){
			$verTips.show().addClass('text-red').removeClass('text-green').find('[data-role="ver-tips-txt"]').text("请输入验证码");
			$verTips.find("glyphicon").addClass('glyphicon-remove-circle').removeClass('glyphicon-ok-circle')
		}
		else{	
			$verTips.show();
			if(this.value==vercode){
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
</script>

</head>

<body>
<form id="exportRoomSeat" action="exportRoomSeat" method="post">
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">导出成绩</h3>
	</div>
	<div class="box-body pos-rel overlay-wrapper">
		<table style="margin:20px auto 0;width:80%;">
			<tr>
				<td>
					<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>
				</td>
				<td>
					<div>
						为了确保数据安全，请用你尾号为 <b class="text-light-blue">
						<c:if test="${not empty user.sjh }">
							${fn:substring(user.sjh,fn:length(user.sjh)-4,fn:length(user.sjh)) }
						</c:if>
						</b>的手机号获取验证码，进行身份验证。
					</div>
				</td>
			</tr>
		</table>
		
		<div class="text-center pad-t10 margin_t20 col-xs-8 col-xs-offset-2 no-float pos-rel">
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
		<div class="alert custom-alert center-block margin_t30 margin-bottom-none f12" style="width:85%;">
        	<ol class="pad-l20 margin-bottom-none">
				<li>导出考场签到表前，请确认已经完成本考试批次所有预约考试的排考工作考成绩，学生只能进入课程复习和查看网考情况，成绩将不会发生任何变化。</li>
				<li>成绩再次导入后，只会对未通过的学员重新开放学习、考试和成绩获取。</li>
			</ol>
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
	<button type="button" class="btn btn-success min-width-90px disabled" data-role="export" disabled>导出</button>
</div>

</form>
</body>


</html>
