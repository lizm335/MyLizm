<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>导出成绩</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body>
<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">导入审核记录</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li data-role="step1" class="process-step1 actived cur"><i></i>1.下载模板</li>
			<%--<li data-role="step2" class="process-step2"><i></i>2.验证身份</li>--%>
			<li data-role="step3" class="process-step2"><i></i>2.导入数据</li>
			<li data-role="step4" class="process-step3">3.导入结果反馈</li>
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
							为了方便你的工作，我们已经准备好了《学位申请审核记录》的标准模板<br>
							你可以点击下面的下载按钮，下载标准模板。
						</div>
					</div>
					<div class="text-center pad-t30 margin_t10">
						<a href="${ctx}/excelImport/downloadModel?name=学位申请审核记录导入模板.xls" class="btn btn-primary btn-lg" style='width:220px;'>下载标准模板</a>
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
			<%--<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-cell-block vertical-mid">
							<div class="text-center">
								<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>
								<div class="inline-block vertical-mid text-left">
									
									<c:if test="${not empty mobileNumber}">
										为了确保数据安全，请用你尾号为 <b class="text-light-blue">${mobileNumber}</b> 的手机号获取验证码，<br>进行身份验证。 
				                    </c:if>
				                    <c:if test="${empty mobileNumber}">
				                       	为了确保数据安全，请先绑定手机号,然后再进行数据导出操作。
				                    </c:if>
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
			</div>--%>

			<!-- 3 导入数据 -->
			<div class="step-item" style="display:none">
				<div class="box-body pos-rel overlay-wrapper process-cnt-box">
					<%--<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid text-center">
								<p class="text-bold f16">
									请选择你要导入的《学位申请审核记录》
								</p>
								<div class="pad-t30 col-xs-8 col-xs-offset-2">
									<div class="input-group input-group-md">
					                    <input type="text" class="form-control upload-input">
					                    <form id="uploadForm" action="${ctx}/graduation/degreemanager/importApplyFlowRecord" enctype="multipart/form-data" method="post">
					                    	<input type="file" id="fileInput" class="hide" name="file" />
					                    </form>
					                    <span class="input-group-btn">
					                    	<button class="btn btn-primary upload-btn" type="button">选择文件</button>
					                    </span>
                  					</div>
								</div>
							</div>
						</div>
					</div>--%>
					<form id="uploadForm" action="${ctx}/graduation/degreeCerif/importApplyFlowRecord" enctype="multipart/form-data" method="post">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid text-center">
								<p class="text-bold f16">
									请选择你要导入的《学位申请审核记录》，为保障导入修改能够顺利完成，导入前请确保表格模板正确，内容已按要求填写。
								</p>
								<div class="pad-t30 col-xs-8 col-xs-offset-2">
									<select name="planId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<c:forEach items="${graduationPlanMap}" var="plan">
											<option value="${plan.key}" >${plan.value}</option>
										</c:forEach>
									</select>
									<br>
									<br>
									<div class="input-group input-group-md">
										<input type="text" class="form-control upload-input">
										<span class="input-group-btn">
										<input type="file" id="fileInput" class="hide" name="file" />
										<button class="btn btn-primary upload-btn" type="button">选择文件</button>
									</span>
									</div>
								</div>
							</div>
						</div>
					</div>
					</form>
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

			<!-- 4 导入结果反馈-失败 -->
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

//选择导入文件
//选择导入文件
    $(".upload-input").click(function(){
    	$("#uploadForm").children("input:file").click();
    });
    $(".upload-btn").click(function(){
//    	$("#uploadForm").children("input:file").click();
    	$("#fileInput").click();
    });
//    $("#uploadForm").children("input:file").change(function(){
    $("#fileInput").change(function(){
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
				//如果存在回调函数，则执行回调函数
			    if($.trim('${param.callbackFun}') && typeof(parent['${param.callbackFun}'])=='function'){
					var callbackFun='${param.callbackFun}';
					var res = parent[callbackFun](data);
					if(res===false){//回调函数返回false，则关闭弹窗
					    parent.$.closeDialog(frameElement.api);
						return false;
					}
				}
				if (data.successful) {
					$("#totalCount").html(data.totalCount);
					$("#successCount").html(data.successCount);
					$("#failedCount").html(data.failedCount);
					$("#successFileName").attr("href", ctx+"/excelImport/downloadResult?path=degreeApplyAuditRecord&name="+data.successFileName);
					$("#failedFileName").attr("href", ctx+"/excelImport/downloadResult?path=degreeApplyAuditRecord&name="+data.failedFileName);
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
	parent.$.closeDialog(frameElement.api)
});

//点击进入‘下一步’
$(".next-btn").click(function(event) {
    $(".step-item").eq(0).hide().next().show();
    $('[data-role="step3"]').addClass('actived cur').siblings().removeClass('cur');
});
/*$(".next-btn").click(function(event) {
	$(".step-item").eq(0).hide().next().show();
	$('[data-role="step2"]').addClass('actived cur').siblings().removeClass('cur');
});*/

//点击验证和进入‘下一步’
/*$('[data-control="btn"]').click(function(event) {
	$(".step-item").eq(1).hide().next().show();
    $('[data-role="step3"]').addClass('actived cur').siblings().removeClass('cur');
});*/

//短信验证
/*$('[data-role="get-ver-code"]').click(function(event){
    event.preventDefault();
	var hasPhone = '${mobileNumber}';
    if(hasPhone==''){
        alert("您不存在手机号，或者手机号有误，不能获取手机号！");
        return;
    }
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
});*/

//验证码输入
/*$(".tel-vercode").keyup(function(event) {
	var $formControl=$('[data-control="btn"]');
	var $verTips=$(".ver-tips");
	if($.trim(this.value)==""){
		$verTips.show().addClass('text-red').removeClass('text-green').find('[data-role="ver-tips-txt"]').text("请输入验证码");
		$verTips.find("glyphicon").addClass('glyphicon-remove-circle').removeClass('glyphicon-ok-circle')
		$formControl.prop('disabled', true).addClass('disabled');
	}
	else{	
	    if(this.value.length!=6)return;//6位数才查询
            $verTips.show();
            $.ajax({
                type:"POST",
                dataType:"json",
                data:{"userCode":this.value},
                url:"${ctx}/studymanage/getCheckCode",
                success:function (data) {
                    if(data.successful==true){
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
	}
});*/

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