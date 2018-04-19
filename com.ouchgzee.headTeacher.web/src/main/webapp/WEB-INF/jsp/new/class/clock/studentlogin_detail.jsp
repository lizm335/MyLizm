<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>学期分析</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">学习管理</a></li>
		<li><a href="javascript:;">考勤分析</a></li>
		<li class="active">学员考勤明细</li>
	</ol>
</section>
<section class="content">
	<div class="box">
		<div class="box-body">
			<div class="media pad">
				<div class="media-left" style="padding-right:25px;">
					<c:set var="studentMap" value="${resultMap.studentMap}" scope="request"></c:set>
					<c:choose>
						<c:when test="${not empty studentMap.ZP}">
							<img id ="headImgId" src="${studentMap.ZP}" class="img-circle" alt="User Image" style="width: 128px; height: 128px;" onerror="this.src='${ctx }/static/images/headImg04.png'">
						</c:when>
						<c:otherwise>
							<img id ="headImgId" src="${ctx }/static/images/headImg04.png" class="img-circle" alt="User Image" style="width: 128px; height: 128px;">
						</c:otherwise>
					</c:choose>
				</div>
				<div class="media-body">
					<h3 class="margin_t10">
						${studentMap.XM}
					</h3>
					<div class="row">
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>学号:</b> <span>${studentMap.XH}</span>
						</div>
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>手机:</b>
							<span>${studentMap.SJH}</span>
						</div>
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>层次:</b> <span>${studentMap.PYCC_NAME}</span>
						</div>
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>年级:</b> <span>${studentMap.YEAR_NAME}</span>
						</div>
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>学期:</b> <span>${studentMap.GRADE_NAME}</span>
						</div>
						<div class="col-xs-6 col-sm-4 pad-b5">
							<b>专业:</b> <span>${studentMap.ZYMC}</span>
						</div>
					</div>
				</div>
			</div>

		</div>
		<div class="box-footer">
			<div class="row stu-info-status">
				<div class="col-sm-2 col-xs-6">
					<div class="f24 text-center">${studentMap.LOGIN_TIMES}</div>
					<div class="text-center gray6">学习次数</div>
				</div>
				<div class="col-sm-2 col-xs-6">
					<div class="f24 text-center">${studentMap.LOGIN_TIME_COUNT}</div>
					<div class="text-center gray6">学习时长（小时）</div>
				</div>
				<div class="col-sm-2 col-xs-6">
					<div class="f24 text-center">${studentMap.PC_ONLINE_PERCENT}%</div>
					<div class="text-center gray6">PC应用占比</div>
				</div>
				<div class="col-sm-2 col-xs-6">
					<div class="f24 text-center">${studentMap.APP_ONLINE_PERCENT}%</div>
					<div class="text-center gray6">移动应用占比</div>
				</div>
				<div class="col-sm-2 col-xs-6">
					<div class="f24 text-center">
						<c:if test="${studentMap.IS_ONLINE eq 'Y'}"><div class="text-green">在线</div></c:if>
						<c:if test="${studentMap.IS_ONLINE eq 'N'}"><div class="gray9">离线</div></c:if>
					</div>
					<div class="text-center gray6">当前在线状态</div>
				</div>
				<div class="col-sm-2 col-xs-6">
					<div class="f24 text-center">${studentMap.LAST_LOGIN_TIME}</div>
					<div class="text-center gray6">最近登录时间</div>
				</div>
			</div>
		</div>
	</div>
	<div class="box box-border margin-bottom-none">
		<c:set var="resultList" value="${resultMap.resultList}" scope="request"></c:set>
		<div class="box-header with-border">
			<div class="box-header with-border">
				<h3 class="box-title pad-t5">课程考勤明细</h3>
				<div class="pull-right no-margin">
					<a href="javascript:void(0);" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出学习记录明细</a>
				</div>
			</div>
			<div class="box-body">
				<div class="table-responsive">
					<table id="Merge_rows" class="table table-bordered table-striped vertical-mid text-center table-font margin-bottom-none">
						<thead>
						<tr>
							<th>学期</th>
							<th>课程模块</th>
							<th>课程名称</th>
							<shiro:hasAnyRoles name="班主任,班主任（有考试院校模式）">
								<th>学习成绩</th>
							</shiro:hasAnyRoles>
							<th>学习进度</th>
							<th>学习次数</th>
							<th>学习时长</th>
							<th>主要应用终端</th>
							<th>最近学习日期</th>
							<th>当前学习状态</th>
							<th>操作</th>
						</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${not empty resultList}">
								<c:forEach items="${resultList}" var="item">
									<tr>
										<td>${item.GRADE_NAME}</td>
										<td>${item.KCLB_NAME}</td>
										<td>${item.KCMC}</td>
										<shiro:hasAnyRoles name="班主任,班主任（有考试院校模式）">
											<td><c:if test="${empty item.EXAM_SCORE}">--</c:if><c:if test="${not empty item.EXAM_SCORE}">${item.EXAM_SCORE}</c:if></td>
										</shiro:hasAnyRoles>
										<td><c:if test="${empty item.SCHEDULE}">--</c:if><c:if test="${not empty item.SCHEDULE}">${item.SCHEDULE}%</c:if></td>
										<td>${item.LOGIN_COUNT}</td>
										<td>${item.LOGIN_TIME}</td>
										<td>
											<c:choose>
												<c:when test="${item.LOGIN_COUNT eq 0}">--</c:when>
												<c:when test="${item.PC_ONLINE_PERCENT gt item.APP_ONLINE_PERCENT}">PC<br>(${item.PC_ONLINE_PERCENT}%)</c:when>
												<c:when test="${item.PC_ONLINE_PERCENT lt item.APP_ONLINE_COUNT}">APP<br>(${item.APP_ONLINE_PERCENT}%)</c:when>
												<c:otherwise>--</c:otherwise>
											</c:choose>
										</td>
										<td>${item.LAST_DATE}</td>
										<td>
											<c:if test="${item.IS_ONLINE eq 'N' and item.LOGIN_COUNT > 0}"><div class="gray9">离线<br>(${item.LEFT_DAY}天未学习)</div></c:if>
											<c:if test="${item.IS_ONLINE eq 'N' and item.LOGIN_COUNT eq 0}"><div class="gray9">离线<br>(从未学习)</div></c:if>
											<c:if test="${item.IS_ONLINE eq 'Y'}"><div class="text-green">在线</div></c:if>
										</td>
										<td>
											<c:if test="${not empty item.TERMCOURSE_ID}">
												<a href="${ctx}/home/class/learn/courseCondition/${item.TERMCOURSE_ID}/${item.STUDENT_ID}/${item.COURSE_ID}" class="operion-item" data-toggle="tooltip" title="查看课程学情明细"><i class="fa fa-fw fa-view-more"></i></a>
											</c:if>
											<c:if test="${empty item.TERMCOURSE_ID}">--</c:if>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script src="${ctx}/static/plugins/custom-model/custom-model.js"></script>
<script type="text/javascript">
    $(function autoRowSpan() {
        var lastValue = " ";
        var value = "";
        var pos = 1;
        var row= 1;
        var col = 0;
        if(Merge_rows.rows.length==2)return;
        for ( var i = row; i < Merge_rows.rows.length; i++) {
            value = Merge_rows.rows[i].cells[col].innerText;
            if (lastValue == value) {
                Merge_rows.rows[i].deleteCell(col);
                Merge_rows.rows[i - pos].cells[col].rowSpan = Merge_rows.rows[i - pos].cells[col].rowSpan + 1;
                pos++;
            } else {
                lastValue = value;
                pos = 1;
            }
        }
    });

	// filter tabs
	$(".filter-tabs li").click(function(event) {
		var $loading=$(".overlay-wrapper").find(".overlay");
		$(this).addClass('actived').siblings().removeClass('actived');

		$loading.show();
		setTimeout(function(){
			$loading.hide();
		},2000);

	});

	//时间轴
	$(".timeline").on('click', '.time-label', function(event) {
		event.preventDefault();
		var $li=$(this).nextUntil('.time-label').filter(":not(.last)");
		$li.slideToggle(300);
	});


	function smsCallback() {
		window.open('${ctx}/home/class/clock/downLoadAttendanceDetailXls?sortType=${sortType}&${searchParams}');
	}
	// 导出
	$('[data-role="export"]').click(function(event) {
		event.preventDefault();
		var self=this;
		$.mydialog({
			id:'export',
			width:600,
			height:415,
			zIndex:11000,
			content: 'url:'+'${ctx }/home/common/toSmsValidateCode?totalNum=${fn:length(resultList)}'
		});
	});
</script>
</body>
</html>