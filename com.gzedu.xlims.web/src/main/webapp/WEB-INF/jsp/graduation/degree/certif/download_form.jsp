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
		<h3 class="box-title">批量下载学位申请材料</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<%--<li data-role="step2" class="process-step1 actived cur"><i></i>1.验证身份</li>--%>
			<%--<li data-role="step3" class="process-step3 actived cur"><i></i>2.下载数据</li>--%>
		</ol>

		<div class="step-box">
			<!-- 1 验证身份 -->
			<%--<div class="step-item">--%>
				<%--<div class="box-body process-cnt-box">--%>
					<%--<div class="table-block full-width" style="height:100%;">--%>
						<%--<div class="table-cell-block vertical-mid">--%>
							<%--<div class="text-center">--%>
								<%--<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>--%>
								<%--<div class="inline-block vertical-mid text-left">--%>
									<%--<c:if test="${not empty mobileNumber}">--%>
										<%--为了确保数据安全，请用你尾号为 <b class="text-light-blue">${mobileNumber}</b> 的手机号获取验证码，<br>进行身份验证。 --%>
				                    <%--</c:if>--%>
				                    <%--<c:if test="${empty mobileNumber}">--%>
				                       	<%--为了确保数据安全，请先绑定手机号,然后再进行数据导出操作。--%>
				                    <%--</c:if>--%>
								<%--</div>--%>
							<%--</div>--%>
							<%--<div class="text-center pad-t30 margin_t10 col-xs-8 col-xs-offset-2 pos-rel">--%>
								<%--<div class="input-group input-group-md">--%>
				                    <%--<input type="text" class="form-control tel-vercode">--%>
				                    <%--<span class="input-group-btn">--%>
				                    	<%--<button class="btn btn-primary" type="button" data-role="get-ver-code">获取验证码</button>--%>
				                    <%--</span>--%>
		      					<%--</div>--%>
		      					<%--<div class="ver-tips text-left" style="display:none;position:absolute;bottom:0px;left:0;margin:0 0 -24px 15px;">--%>
		      						<%--<span class="glyphicon glyphicon-remove-circle vertical-mid"></span>--%>
		      						<%--<span data-role="ver-tips-txt">--%>
		      							<%--验证码错误--%>
		      						<%--</span>--%>
		      					<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="box-footer text-right process-btn-box">--%>
					<%--<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>--%>
					<%--<button type="button" class="btn btn-success min-width-90px margin_l15 disabled" data-control="btn" disabled>确认，下一步</button>--%>
				<%--</div>--%>
			<%--</div>--%>

			<!-- 2 导入数据 -->
			<div class="step-item">
				<div class="box-body pos-rel overlay-wrapper process-cnt-box">
					<div class="table-block full-width" style="height:100%;">
						<div class="table-row">
							<div class="table-cell-block vertical-mid ">
								<div class="text-center">
									<i class="fa fa-exclamation-circle vertical-mid margin_r10" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>
									<span class="inlineblock">
										请选择你要下载登记表的数据范围
									</span>
								</div>
								<div class="form-group margin_t20">
						          	<div class="center-block" style="width:50%;">
								        <select class="form-control" id="state">
								          <option value="0">待审核</option>
								          <option value="1">审核通过</option>
								          <option value="2">审核不通过</option>
								          <option value="3">已发放学位证书</option>
								        </select>
							        </div>
						        </div>
							</div>
						</div>
					</div>
					<div class="overlay uploading-overlay">
						<div class="uploading-txt text-center">
							<i class="fa fa-refresh fa-spin vertical-mid"></i>
							<span class="inlineblock left10">数据正在下载中，请稍后...</span>
						</div>
	                	
	                </div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
					<button type="button" class="btn btn-success min-width-90px margin_l15 import-sure-btn">确认下载</button>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(".import-sure-btn").click(function(event) {
	var $overlay=$(".uploading-overlay");
	var $pstate=parent.$('[name="search_EQ_auditState"]');
	var restate=$pstate.val();
	$pstate.val($('#state').val());
	var url='${ctx}/graduation/degreeCerif/download?'+parent.$('#listForm').serialize();
	$pstate.val(restate);
	location.href=url;
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
		  width:150,
		  height:50,
		  backdrop:false,
		  fade:true,
		  transparent:true,
		  showCloseIco:false,
		  content: '<div class="alert alert-success alert-dismissable no-margin pad15"><h4 class="no-margin text-no-bold"><i class="icon fa fa-check"></i> <span class="f16">导出成功！</span></h4></div>'
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
			parent.$.closeDialog(frameElement.api);
		},2000);

	},2000);
});

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//下一步
$('[data-control="btn"]').click(function(event) {
	$(".step-item").eq(0).hide().next().show();
    $('[data-role="step3"]').addClass('actived cur').siblings().removeClass('cur');
});

/*
//短信验证
$('[data-role="get-ver-code"]').click(function(event){
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
});

//验证码输入
$(".tel-vercode").keyup(function(event) {

	var $formControl=$('[data-control="btn"]');
	var $verTips=$(".ver-tips");
	if($.trim(this.value)==""){
		$verTips.show().addClass('text-red').removeClass('text-green').find('[data-role="ver-tips-txt"]').text("请输入验证码");
		$verTips.find("glyphicon").addClass('glyphicon-remove-circle').removeClass('glyphicon-ok-circle')
		$formControl.prop('disabled', true).addClass('disabled');
	}
	else{
	    //$formControl.prop('disabled', false).removeClass('disabled');
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

</script>
</body>
</html>