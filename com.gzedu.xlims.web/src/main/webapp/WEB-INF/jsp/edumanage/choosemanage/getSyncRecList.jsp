<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>同步选课</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">同步学员选课</h3>
	</div>
	<div class="box-body pos-rel overlay-wrapper">
		<div class="text-center" style="margin-top:60px;">
			<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>
			<div class="inline-block vertical-mid text-left">
				<div class="text-bold f16">本次操作共有 ${resultMap.NO_SYNC_COUNT } 条数据信息需要同步！<br/>是否同步选课信息到国开学习网？</div>
			</div>
			<br/><br/>
			<table class="table table-bordered table-striped vertical-mid text-center table-font">
				<thead>
					<tr>
						<th>开课学期</th>
						<th>开课总数</th>
						<th>选课总数</th>
						<th>应选课</th>
						<th>已选课</th>
						<th>未选课</th>
						<th>选课失败</th>
						<th>新增选课</th>
						<th>重修选课</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${resultMap.GRADE_NAME }</td>
						<td>${resultMap.OPEN_COURSE_COUNT }</td>
						<td>${resultMap.REC_COUNT }</td>
						<td>${resultMap.REC_COUNT }</td>
						<td>${resultMap.YES_SYNC_COUNT }</td>
						<td>${resultMap.NO_SYNC_COUNT }</td>
						<td>${resultMap.REC_COUNT - resultMap.YES_SYNC_COUNT-resultMap.NO_SYNC_COUNT }</td>
						<td>${resultMap.REC_COUNT }</td>
						<td>0</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="overlay" style="display:none;">
			<div class="uploading-txt text-center">
				<i class="fa fa-refresh fa-spin vertical-mid"></i>
				<span class="inlineblock left10 f16" id="tipsInfo">选课信息同步中，请稍后...</span>
			</div>
        </div>
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sync">确定同步</button>
</div>
<form id="uploadForm" name="uploadForm" action="getSyncRecResult" method="post">

</form>
<script type="text/javascript">

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

$("button[data-role='sync']").click(function(event) {
	var $overlay=$(".overlay");
	var $that=$(this);
	$overlay.show();
	$that.hide();
	$("#uploadForm").submit();
});

function getSyncNum() {
	$.ajax({
        type:"POST",
        dataType:"json",
        data:{},
        async:false,
        url:"${ctx}/edumanage/choosemanage/getSyncNum",
        success:function (data) {
        	if (data.num!=null && data.num!="") {
        		console.log("=="+data.num);
        		$("#tipsInfo").html(data.num+"，选课信息同步中，请稍后...");
        	} else {
        		$("#tipsInfo").html("选课信息同步中，请稍后...");
        	}
        }
    });
}

window.setInterval(getSyncNum,3000);
</script>
</body>
</html>