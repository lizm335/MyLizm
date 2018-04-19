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
<form action="${ctx }/edumanage/rulesClass/update" method="post" id="inputForm">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">设置自动分班规则</h3>
	</div>
	<div class="slimScrollDiv" style="position: relative; overflow: hidden; width: auto; height: 375px;"><div class="box-body slim-Scroll" style="overflow: hidden; width: auto; height: 375px;">
		<div class="input-group input-group-sm">
			<p class="input-group-addon">年级：</p>
			<select name="gradeId" class="form-control selectpicker">
				<option value="">请选择</option>
				<c:forEach items="${gradeMap}" var="map">
					<option value="${map.key}"
						<c:if test="${map.key==gjtArticle.gradeId}">selected='selected'</c:if>>${map.value}
					</option>
				</c:forEach>
			</select>	
		</div>
		<h4 class="f14">分班规则：</h4>
		<div class="panel panel-default flat margin_b10">
			<div class="panel-heading pad-t5 pad-b5">
				<div class="checkbox no-margin">
			    	<label>
			        	<input type="checkbox" name="rules" value="1">
			        	按学习中心分班（只支持带有招生+学支服务的学习中心）
			    	</label>
				</div>
			</div>
			<div class="panel-body pad-t10">
				<div>命名规则：年级+专业+层次+学习中心+序号</div>
				<div class="gray9">
					<small>例如：2016春管理学高起专麓湖学习中心01班
					</small>
				</div>
				<div class="input-group input-group-sm margin_t10">
					<p class="input-group-addon">人数上限：</p>
					<input class="form-control">	
				</div>
			</div>
		</div>
		<div class="panel panel-default flat margin-bottom-none">
			<div class="panel-heading pad-t5 pad-b5">
				<div class="checkbox no-margin">
			    	<label>
			        	<input type="checkbox" name="rules" value="2"  checked="checked">
			        	不按学习中心分班
			    	</label>
				</div>
			</div>
			<div class="panel-body pad-t10">
				<div>命名规则：年级+专业+层次+序号</div>
				<div class="gray9">
					<small>例如：2016春管理学高起专01班
					</small>
				</div>
				<div class="input-group input-group-sm margin_t10">
					<p class="input-group-addon">人数上限：</p>
					<input class="form-control">	
				</div>
			</div>
		</div>
	</div><div class="slimScrollBar" style="background: rgb(0, 0, 0); width: 5px; position: absolute; top: 0px; opacity: 0.4; display: none; border-radius: 7px; z-index: 99; right: 1px; height: 375px;"></div><div class="slimScrollRail" style="width: 5px; height: 100%; position: absolute; top: 0px; display: none; border-radius: 7px; background: rgb(51, 51, 51); opacity: 0.2; z-index: 90; right: 1px;"></div></div>
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
	setTimeout(function(){//此句模拟交互，程序时请去掉
      postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>')
      /*关闭弹窗*/
      setTimeout(function(){
        parent.$.closeDialog(frameElement.api)
      },2000)
    },2000);//此句模拟交互，程序时请去掉
});

$('.slim-Scroll').slimScroll({
    height: 375,
    size: '5px'
});
</script>
</body>
</html>



