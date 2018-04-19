<%@page import="com.gzedu.xlims.common.constants.WebConstants"%>
<%@page import="com.gzedu.xlims.pojo.GjtUserAccount"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>导出考场数据</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>

<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">导出考场数据</h3>
	</div>
	<div class="box-body pos-rel overlay-wrapper">
		<div style="padding-top:80px;">
			<div class="text-center">
				<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>
				<span class="inlineblock">
					请选择需要导出笔试考试计划的考试批次
				</span>
			</div>
			<div class="form-group margin_t20">
	          	<label class="control-label sr-only">考试批次</label>
	          	<div class="center-block" style="width:55%;">
			        <select id="examBatchCode" name="search_EQ_examPonitNew" class="form-control select2">
						 <option value="">请选择</option>
						 <c:forEach items="${batchMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_examPonitNew.examBatchCode']}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>       	
					</select>
		        </div>
	        </div>
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
	<button type="button" class="btn btn-success min-width-90px" data-role="export">导出</button>
</div>

<script type="text/javascript">
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
		var flag = exportData();
		if(flag == true){
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
		}
		
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

$(".select2").select2()
.on('select2:open', function(e) {
	$(".select2-results__options").css('max-height', 100);
});

function exportData(){
	var examBatchID = $("select[name='search_EQ_examPonitNew']").val();
	if(examBatchID == "" || examBatchID ==null){
		alert("请选择考试计划");
		return false;
	}else{
		var form = $("<form>");
		form.attr("style","display:none");
		form.attr("target","");
		form.attr("method","post");
		form.attr("action","/exam/new/room/exportDownLoad");
		$("body").append(form);
		form.append("<input type='text' name='search_EQ_examBatchCode' value='"+examBatchID+"' /> ");
		form.submit();
	}
	return true;
}

</script>
</body>
</html>

