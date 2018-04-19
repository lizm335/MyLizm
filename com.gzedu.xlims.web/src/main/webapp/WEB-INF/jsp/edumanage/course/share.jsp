<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	if($('#action').val() == 'succesful'){
		$(".step-item").eq(0).hide().next().show();
		$(".process-step3").addClass('actived cur').siblings().removeClass('cur');
		autoClose(); 
	}
})
</script> 

</head>
<body>
<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">共享课程</h3>
	</div>
	<div class="pop-content">
		<ol class="list-unstyled clearfix process-list">
			<li class="process-step1 actived cur"><i></i>1.选择需要分享的院校</li>
			<li class="process-step3">2.分享结果</li>
		</ol>
 		<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/edumanage/course/${action}" method="post">
                <input id="action" type="hidden" name="action" value="${action }">
                <input type="hidden" name="courseId" value="${entity.courseId }">
		<div class="step-box">
			<!-- 1 选择需要分享的院校 -->
			<div class="step-item">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width full-height">
						<div class="table-cell-block vertical-mid">
							<table class="per-tbl" style="margin:0 auto;width:75%;">
								<tr>
									<th class="text-no-bold">共享课程：</th>
									<td>${entity.kcmc}（${entity.kch}）</td>
								</tr>
								<tr>
									<th class="text-no-bold">培养层次：</th>
									<td>${pyccMap[entity.pycc]}</td>
								</tr>
								<tr>
									<th class="text-no-bold">所属院校：</th>
									<td>
										${entity.gjtOrg.orgName}
										<input type="hidden" name="orgId" value="${entity.gjtOrg.id}">
									</td>
								</tr>
								<tr>
									<th class="text-no-bold">
										<div class="pad-t5">
											接收院校：
										</div>
									</th>
									<td>
										<select name="orgCode"
											class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<c:forEach items="${orgAllNotBSMap}" var="orgAllNotBS">
													<option value="${orgAllNotBS.key}" >${orgAllNotBS.value}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
					<button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_l15 next-btn">确认共享</button>
				</div>
			</div>

			<!-- 2 分享结果 -->
			<div class="step-item" style="display:none">
				<div class="box-body process-cnt-box">
					<div class="table-block full-width full-height">
						<div class="table-cell-block vertical-mid">
							<div class="center-block" style="width:70%;">
								<p class="margin_b20 text-center">
									<span class="fa fa-check-circle text-green vertical-mid"  style="font-size:60px;"></span>
								</p>
								<div class="f16">
									<c:choose>
									<c:when test="${result}">
										恭喜你，${entity.gjtOrg.orgName}的《${entity.kcmc}》共享成功
									</c:when>
									<c:otherwise>
										很遗憾，${entity.gjtOrg.orgName}的《${entity.kcmc}》共享失败
									</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer text-right process-btn-box">
					<button type="button" class="btn btn-default min-width-90px" data-role="timer"><span>5</span>秒后自动关闭</button>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>
<!-- jQuery 2.1.4 --> 
<!--[if gte IE 9]>
	<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
	<![endif]--> 

<!--[if !IE]><!--> 
<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script> 
<!--<![endif]--> 

<!--[if lt IE 9]>
		<script src="plugins/jQuery/jquery-1.12.4.min.js"></script>
	<![endif]--> 

<script src="bootstrap/js/bootstrap.min.js"></script> 
<!-- SlimScroll 1.3.0 --> 
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>  
<!-- AdminLTE App --> 
<script src="dist/js/app.js"></script> 
<!-- common js --> 
<script src="dist/js/common.js"></script> 
<script type="text/javascript">

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$("button[data-dismiss='modal']").click();
});

//自动关闭 弹窗
function autoClose() {
	var $timerContainer=$("button[data-role='timer']").find('span');
	var timer,i = 5;
	function fn() {
		if(i<=0){
			clearInterval(timer);
			parent.$("button[data-dismiss='modal']").click();
		}
		else{
			$timerContainer.text(i);
			i --;
		}
	}
	timer = setInterval(fn, 1000);
	fn();
};

//点击进入‘下一步’
$(".next-btn").click(function(event) {
	$(".step-item").eq(0).hide().next().show();
	$(".process-step3").addClass('actived cur').siblings().removeClass('cur');
	autoClose();
});

</script>
</body>
</html>