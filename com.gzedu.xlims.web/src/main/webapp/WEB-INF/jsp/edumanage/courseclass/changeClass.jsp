<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>新增班级</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form action="${ctx}/edumanage/courseclass/updClassStudent" method="post" id="inputForm">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">批量调班</h3>
	</div>
	<div class="box-body pos-rel overlay-wrapper">
		<table class="full-width" height="200">
			<input type="type" name="CLASS_ID" value="${resultMap.CLASS_ID }"/>
			<tr>
				<td valign="middle">
					<table class="per-tbl" width="80%" align="center">
						<tr>
							<th width="80" class="text-no-bold">原始班级：</th>
							<td>
								${resultMap.BJMC }
							</td>
						</tr>
						<tr>
							<th width="80" class="text-no-bold">调整人数：</th>
							<td>
								${resultMap.STUDENT_COUNT }
							</td>
						</tr>
						<tr>
							<th class="text-no-bold">
								<div class="pad-t5">
								接收班级：
								</div>
							</th>
							<td>
								<select class="form-control" name="NEW_CLASS_ID" id="new_class_id">
									<option value="">请选择</option>
                                    <c:forEach items="${classMap}" var="map">
                                        <option value="${map.key}">${map.value}</option>
                                    </c:forEach>
								</select>
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
	parent.$.closeDialog(frameElement.api);
});

$("button[data-role='sure']").click(function(event) {
	var id = $("#new_class_id").val();
	if(id == ""){
		alert("请选择接收班级");
		return false;
	}
	$.ajax({
        type: "POST",
        dataType: "json",
        url: "/edumanage/courseclass/updClassStudent",
        data: $('#inputForm').serialize(),
        success: function (result) {
        	if(result > 0){
        		$.alert("调班成功",function(){
        			parent.$.closeDialog(frameElement.api);
        		});
        	}else{
        		$.alert("调班失败",function(){
        			parent.$.closeDialog(frameElement.api);
        		});
        	}
        },
        error : function() {
        }
    });
});

</script>
</body>
</html>



