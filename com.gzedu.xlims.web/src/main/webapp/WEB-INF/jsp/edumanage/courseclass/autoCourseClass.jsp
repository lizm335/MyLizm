<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>设置自动分班规则</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form action="${ctx}/edumanage/rulesClass/addRule" method="post" id="inputForm">
<input type="hidden" name="xxid" value="${info.xxid}" />
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">设置自动分班规则</h3>
	</div>
	<div class="box-body">
		<table class="full-width" height="200">
			<tr>
				<td valign="middle">
					<table class="per-tbl" width="80%" align="center">
						<tr>
							<th width="80" class="text-no-bold">命名规则：</th>
							<td>
								课程名+学年度+序号 <br>
								<div class="gray9">
									例如：  职业英语（专）2016上学期01班
								</div>
							</td>
						</tr>
						<tr>
							<th class="text-no-bold">
								<div class="pad-t5">
								人数上限：
								</div>
							</th>
							<td>
								<div class="input-group">
									<input class="form-control" type="text" name="num" value="${info.num}"
										onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')">
									<span class="input-group-addon">人</span>
								</div>
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
//确认
$("button[data-role='sure']").click(function(event) {
	var postIngIframe=$.mydialog({
	  id:'dialog-1',
	  width:150,
	  height:50,
	  backdrop:false,
	  fade:false,
	  showCloseIco:false,
	  zIndex:11000,
	  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
	});
	
	$.post($("#inputForm").attr("action"), $("#inputForm").serialize(),function(data){
   		if(data.successful){
   			parent.$.closeDialog(frameElement.api);
   		}else{
   			alert(data.message);
   		}
   },"json"); 
	
});

$('.slim-Scroll').slimScroll({
    height: 375,
    size: '5px'
});
</script>
</body>
</html>



