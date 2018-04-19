<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>批量调班</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form action="${ctx }/edumanage/classstudent/toMoveClass" method="post" id="inputForm">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">批量调班</h3>
	</div>
	<div class="box-body pos-rel overlay-wrapper">
		<table class="full-width" height="200">
			<tr>
				<td valign="middle">
					<table class="per-tbl" width="80%" align="center">
						<tr>
							<th width="80" class="text-no-bold">原始班级：</th>
							<td>
								${gjtClassInfo.bjmc}
								<input type="hidden" id="sendClass" value="${gjtClassInfo.classId}"></input>
							</td>
						</tr>
						<tr>
							<th width="80" class="text-no-bold">调整人数：</th>
							<td>
								<span id="num">${num}人</span>
							</td>
						</tr>
						<tr>
							<th class="text-no-bold">
								<div class="pad-t5">
								接收班级：
								</div>
							</th>
							<td>
								<%-- <select id="receiveClass"
											class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>		
									<c:forEach items="${gjtClassInfoMap}" var="gjtClassInfoItem">
										<option value="${gjtClassInfoItem.key}" >${gjtClassInfoItem.value}</option>
									</c:forEach>
								</select> --%>
								<input type="text" val="${ctx}/edumanage/classstudent/queryTeachClass/${gjtClassInfo.gradeId}/${gjtClassInfo.classId}" class="form-control" data-role="select-class" onclick="changeClass()"/>
								<input type="hidden" id="newClassId"></input>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>


<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">确认</button>
</div>
</form>
<script type="text/javascript">
//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});
//确认
$("button[data-role='sure']").click(function(event) {	
	if ($("#receiveClass option:selected").val() == "") {
		alert("请选择班级");
		return false;
	}
	 var postIngIframe = $.mydialog({
			id : 'dialog-1',
			width : 150,
			height : 50,
			backdrop : false,
			fade : false,
			showCloseIco : false,
			zIndex : 11000,
			content : '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
	 });
	var studentIds= '${studentIds}'.split(',');
	$.post("${ctx }/edumanage/classstudent/toMoveTeachClass",{
		sendClass:$("#sendClass").val(),
		receiveClass:$("#newClassId").val(),
		studentIds:studentIds
		},function(data,status){
			parent.$.closeDialog(frameElement.api);
			  var data = JSON.parse(data); 
			  alert(data.message);		
			  window.parent.location.reload();
		}
	);
});
$("[data-role='select-class']").click(function(event) {	
	$.mydialog({
		id : 'select-class',
		width : 880,
		height : 500,
		zIndex : 11000,
		content : 'url:' + $(this).attr('val')
	    });	
});
</script>
</body>
</html>



