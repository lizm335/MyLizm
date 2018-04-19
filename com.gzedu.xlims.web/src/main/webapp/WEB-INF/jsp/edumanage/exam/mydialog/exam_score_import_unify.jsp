<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>导入登记成绩</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
var vercode = "******";
$(function() {
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
	    		.eq(2).hide()
	    		.next().show();//导入成功时显示
	    	}//实际开发时请删除
	    	else{//实际开发时请删除
	
	    		//导入失败时显示
	    		$(".step-item").eq(2).hide();
	    		$(".step-item").eq(3).show();
	
	    	}//实际开发时请删除
			$overlay.hide();
			$that.show();
	
			$('[data-role="step4"]').addClass('actived cur').siblings().removeClass('cur');
		},2000);
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
		var $formControl=$('[data-control="btn"]');
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
});
</script>

</head>

<body>
<form id="exportRoomSeat" action="exportRoomSeat" method="post">
<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">导入登记成绩</h3>
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
						<button class="btn btn-primary btn-lg" style='width:220px;'>下载标准模板</button>
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
									为了确保数据安全，请用你尾号为 <b class="text-light-blue">
									<c:if test="${not empty user.sjh }">
										${fn:substring(user.sjh,fn:length(user.sjh)-4,fn:length(user.sjh)) }
									</c:if>
									</b> 的手机号获取验证码，<br>进行身份验证。
								</div>
							</div>
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
						</div>
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 disabled" data-control="btn" disabled>下一步</button>
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
								<div class="pad-t10 col-xs-8 col-xs-offset-2 no-float">
									<div class="input-group input-group-md">
					                    <input type="text" class="form-control upload-input">
					                    <span class="input-group-btn">
					                    	<input type="file" class='hide'>
					                    	<button class="btn btn-primary upload-btn" type="button">选择文件</button>
					                    </span>
                  					</div>
								</div>
								<div class="alert custom-alert center-block margin_t30 margin-bottom-none f12" style="width:85%;">
									<ol class="pad-l20 margin-bottom-none">
					    				<li>导出考场签到表前，请确认已经完成本考试批次所有预约考试的排考工作</li>
					    				<li>只能导出完成排考的考场签到表。考场中若有未排考的预约数据，则不能被导出</li>
					    				<li>导出规则：一个考场、一个考试科目、一份表。采用打包下载方式，导出过程可 能需要几分钟，请耐心等待。</li>
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

			<!-- 4 导入结果反馈-成功 -->
			<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid">
								<div class="" style="width:305px;margin:0 auto;">
									<p class="text-bold f16 margin_b20">
									你本次共导入1380条信息，导入结果如下:
									</p>
									<p class="margin_b20">
										<span class="glyphicon glyphicon-ok-circle text-green vertical-mid"  style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-green margin_r15">导入成功：1000条</span>

										<button class="btn btn-default btn-sm left10">下载导入成功记录</button>
									</p>
									<p class="">
										<span class="glyphicon glyphicon-remove-circle text-red vertical-mid" style="font-size:22px;margin-top:-4px;"></span>
										<span class="text-red margin_r15">导入失败：380条</span>
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

			<!-- 4 导入结果反馈-失败 -->
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

</form>
</body>


</html>
