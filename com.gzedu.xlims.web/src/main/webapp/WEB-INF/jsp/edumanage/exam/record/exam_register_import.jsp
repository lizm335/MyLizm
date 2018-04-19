<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>导入登记成绩</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">登记成绩</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li data-role="step1" class="process-step1 actived cur"><i></i>1.验证身份</li>
			<li data-role="step2" class="process-step2"><i></i>2.导入登记成绩</li>
			<li data-role="step3" class="process-step3"><i></i>3.导入结果反馈</li>
		</ol>

		<div class="step-box">
			<!-- 1 验证身份 -->
			<div class="step-item">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-cell-block vertical-mid">
							<div class="text-center">
								<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>
								<div class="inline-block vertical-mid text-left">
									<c:if test="${empty sjhs}">
										为了确保数据安全，导出数据需要手机验证，你的手机号码没有填写，请到个人资料填写手机号码。
									</c:if>
									<c:if test="${not empty sjhs}">
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
						<button type="button" class="btn btn-success min-width-90px disabled" data-role="export" disabled>确认验证</button>
					</c:if>
				</div>
			</div>
			
			<!-- 2 导入数据 -->
			<div class="step-item" style="display:none">
				<div class="box-body pos-rel overlay-wrapper process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid text-center">
								<div class="pad-t10 col-xs-10 col-xs-offset-1">

									<div class="alert custom-alert margin_b15">
							        	<div class="media no-padding">
							        		<div class="media-left">
							        			<i class="fa fa-exclamation-circle pad-t5" style="font-size:48px;margin-top:-5px;"></i>
							        		</div>
							        		<div class="media-body f12 media-middle text-left">
							        			导入的形成性成绩、学院网考成绩必须与导出后锁定的成绩一致，不一致不允许导入。导入前，请下载成绩导入模板，按照模板中的要求填写相关数据，确认无误后，请选择文件进行导入。
							        		</div>
							        	</div>
									</div>
									<p class="f16 margin_b15">
										<a href="downRecordResult?execleFileName=getRegisterRecordImport">下载成绩导入模板 <i class="fa fa-fw fa-download"></i></a>
									</p>
									<form id="uploadForm" name="uploadForm" action="importRegisterRecord" method="post" enctype="multipart/form-data">
										<div class="margin_b10 text-left">
											<select class="form-control select2 full-width" name="EXAM_BATCH_CODE">
												<c:forEach items="${batchMap}" var="map">
													<option value="${map.key}">${map.value}（${map.key}）</option>
												</c:forEach>
											</select>
										</div>
										<div class="input-group input-group-md">
						                    <input type="text" class="form-control upload-input">
						                    <span class="input-group-btn">
						                    	<input type="file" name="file" id="file" class='hide'>
						                    	<button class="btn btn-primary upload-btn" type="button">选择文件</button>
						                    </span>
	                  					</div>
                  					</form>
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

//确定验证
$('[data-role="export"]').click(function(event) {
	$(".step-item").eq(0).hide().next().show();
	$('[data-role="step2"]').addClass('actived cur').siblings().removeClass('cur');
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
$('[data-role="exportExam"]').click(function(event) {
	$("#exam_batch_code").val($("#examBatchCode").val());
	var $overlay=$(".overlay");
	var $that=$(this);
	$overlay.show();
	$that.addClass("disabled").prop('disabled', true);
	setTimeout(function(){
		$overlay.hide();
		setTimeout(function(){
			$.closeDialog($("[data-id='dialog-1']"));
			$that.removeClass("disabled").prop('disabled', false);
		},3000);
	},2000);
	$("#expExamRecord").submit();
});
</script>
</body>
</html>