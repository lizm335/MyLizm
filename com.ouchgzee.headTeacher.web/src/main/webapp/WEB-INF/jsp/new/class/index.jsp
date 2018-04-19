<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 班级首页</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header clearfix">
	<div class="pull-left">
		您所在位置：
	</div>
	<ol class="breadcrumb oh">
		<li class="active"><i class="fa fa-home"></i> 班级首页</li>
	</ol>
</section>

<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<li class="active"><a href="#tab_top_index_1" data-toggle="tab">我的工作台</a></li>
			<li><a href="#tab_top_index_2" data-toggle="tab">实时动态</a></li>
			<!--<li><a href="#tab_top_3" >统计分析</a></li>-->
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="tab_top_index_1">

				<div class="row">
					<div class="col-sm-6">
						<!-- 代办事项 -->
						<div class="box box-border no-shadow">
							<div class="box-header with-border">
								<h3 class="box-title"><i class="fa fa-fw fa-list-ul margin_r10"></i>代办事项</h3>
							</div>
							<div class="box-body" style="min-height:216px;">
								<div class="row">
									<div class="col-xs-3 agency-item">
										<a href="${ctx}/home/class/studentInfo/list?search_EQ_isEnteringSchool=0" data-page-role="single-page" data-id="3" data-module-title="学员状态">
											<i>${notLearningCount}</i>
											<span>未确认入学</span>
										</a>
									</div>
									<div class="col-xs-3 agency-item">
										<a href="${ctx}/home/class/serviceManager/list" data-page-role="single-page" data-id="11" data-module-title="服务记录">
											<i>${unServiceCount}</i>
											<span>待跟进服务</span>
										</a>
									</div>
									<div class="col-xs-3 agency-item">
										<a href="${ctx}/home/class/feedback/list" data-page-role="single-page" data-id="6" data-module-title="答疑管理">
											<i>${unFeedbackCount}</i>
											<span>待回复答疑</span>
										</a>
									</div>
									<div class="col-xs-3 agency-item">
										<a href="${ctx}/home/class/exam/appointment/list?search_EQ_appointmentStatus=0" data-page-role="single-page" data-id="7" data-module-title="考试管理">
											<i>${waitExamCount}</i>
											<span>待考试预约</span>
										</a>
									</div>
									<div class="col-xs-3 agency-item">
										<a href="${ctx}/home/class/activity/list" data-page-role="single-page" data-id="10" data-module-title="班级活动">
											<i>${auditActivityNum}</i>
											<span>待审核活动报名</span>
										</a>
									</div>
									<div class="col-xs-3 agency-item">
										<a href="${ctx}/home/class/exam/appointment/list?search_EQ_pointStatus=0" data-page-role="single-page" data-id="7" data-module-title="考试管理">
											<i>${waitExamPointCount}</i>
											<span>待预约考点</span>
										</a>
									</div>
									<%--<div class="col-xs-3 agency-item">
                                        <a href="${ctx}/home/class/studentState/list" data-page-role="single-page" data-id="3" data-module-title="学员状态">
                                            <i>${graduateCount}</i>
                                            <span>可申请毕业</span>
                                        </a>
                                    </div>--%>
									<div class="col-xs-3 agency-item">
										<a href="${ctx}/home/class/graduation/apply/list?search_EQ_isConfirm=0">
											<i>${notConfirmGraduation}</i>
											<span>待确认论文终稿</span>
										</a>
									</div>
									<div class="col-xs-3 agency-item">
										<a href="${ctx}/home/class/studentInfo/list.html?search_EQ_perfectStatus=0">
											<i>${notPerfectCount}</i>
											<span>未完善资料</span>
										</a>
									</div>
								</div>
							</div>
						</div>
						<!-- /代办事项 -->
					</div>
					<div class="col-sm-6">
						<!-- 通知公告 -->
						<div class="box box-border no-shadow">
							<div class="box-header with-border">
								<h3 class="box-title"><i class="fa fa-fw fa-envelope-o margin_r10"></i>通知公告</h3>
								<div class="pull-right">
									<a href="${ctx}/home/class/message/list" data-page-role="single-page" data-id="1" data-module-title="通知公告">更多</a>
								</div>
							</div>
							<div class="box-body" style="min-height:216px;">
								<c:if test="${fn:length(messageList)>0}">
									<ul class="list-unstyled note-list">
										<c:forEach items="${messageList}" var="info">
											<li>
												<a href="${ctx}/home/class/message/view/${info.gjtMessageInfo.messageId}?source=1" data-page-role="single-page" data-id="1" data-module-title="通知公告">
													<span class="pull-left">【<c:choose><c:when test="${info.gjtMessageInfo.infoType=='1'}">系统</c:when><c:when test="${info.gjtMessageInfo.infoType=='2'}">教务</c:when><c:otherwise>其他</c:otherwise></c:choose>】</span>
													<time class="pull-right"><fmt:formatDate value="${info.gjtMessageInfo.createdDt}" type="date" /></time>
													<div class="note-tit">
														<div class="table-block">
															<div class="table-cell-block">
																<div class="note-txt">
																		${info.gjtMessageInfo.infoTheme}
																</div>
															</div>
															<c:if test="${info.isEnabled=='0'}">
																<div class="table-cell-block"><small class="label label-danger news-label">New</small></div>
															</c:if>
														</div>
													</div>
												</a>
											</li>
										</c:forEach>
									</ul>
								</c:if>
								<c:if test="${fn:length(messageList)==0}">
									<table width="100%" height="196">
										<tr>
											<td valign="middle">
												<div class="text-center gray9 f12">
													<i class="fa fa-fw fa-exclamation-circle f18"></i>
													<span>你暂无通知</span>
													<a href="${ctx}/home/class/message/create"><u>去发布一条新的通知</u></a>!
												</div>
											</td>
										</tr>
									</table>
									
								</c:if>
							</div>
						</div>
						<!-- /通知公告 -->
					</div>
				</div>

				<!--班级考勤-->
				<div class="nav-tabs-custom no-shadow box-border flat">
					<div class="pull-right pad-r10 pad-t15">
						<a href="${ctx}/home/class/clock/list">更多</a>
					</div>
					<ul class="nav nav-tabs" data-role="tab-list">
						<li class="active"><a href="#tab_top_1" data-toggle="tab">班级考勤统计</a></li>
						<li><a href="#tab_top_2" data-toggle="tab">课程考勤统计</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="tab_top_1">
							<div class="form-inline">
								<select id="teachTermId" class="form-control margin_r10">
									<c:forEach var="item" items="${termList}" varStatus="s">
										<option value="${item.gradeId}" <c:if test="${s.index==(fn:length(termList)-1)}">selected</c:if>>第${s.index==0?'一':s.index==1?'二':s.index==2?'三':s.index==3?'四':s.index==4?'五':''}学期<c:if test="${s.index==(fn:length(termList)-1)}">(当前学期)</c:if></option>
									</c:forEach>
								</select>
								<span class="gray9 margin_l10">距离本学期结束还有 ${remainingDays} 天</span>
							</div>
							<div class="row">
								<div class="col-sm-8">
									<div id="chart8" style="height:280px;"></div>
								</div>
								<div class="col-sm-4">
									<table class="table border-bottom tbl-info">
										<tbody>
										<tr>
											<td>班级人数</td>
											<td><span id="studentNum"></span>人</td>
										</tr>
										<tr>
											<td>学习总次数</td>
											<td><span id="studyNum"></span>次</td>
										</tr>
										<tr>
											<td>平均学习次数</td>
											<td><span id="avgStudyNum"></span>次</td>
										</tr>
										<tr>
											<td>学习总时长</td>
											<td><span id="studyHourNum"></span>小时</td>
										</tr>
										<tr>
											<td>平均学习时长</td>
											<td><span id="avgStudyHourNum"></span>小时</td>
										</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="tab_top_2">
							<div class="form-inline">
								<select id="courseTermId" class="form-control margin_r10">
									<c:forEach var="item" items="${termList}" varStatus="s">
										<option value="${item.gradeId}" <c:if test="${s.index==(fn:length(termList)-1)}">selected</c:if>>第${s.index==0?'一':s.index==1?'二':s.index==2?'三':s.index==3?'四':s.index==4?'五':''}学期<c:if test="${s.index==(fn:length(termList)-1)}">(当前学期)</c:if></option>
									</c:forEach>
								</select>
								<select id="courseId" class="form-control">

								</select>
								<span class="gray9 margin_l10">距离本学期结束还有 ${remainingDays} 天</span>
							</div>
							<div class="row">
								<div class="col-sm-8">
									<div id="chart9" style="height:280px;"></div>
								</div>
								<div class="col-sm-4">
									<table class="table border-bottom tbl-info">
										<tbody>
										<tr>
											<td>选课人数</td>
											<td><span id="course_studentNum"></span>人</td>
										</tr>
										<tr>
											<td>学习总次数</td>
											<td><span id="course_studyNum"></span>次</td>
										</tr>
										<tr>
											<td>平均学习次数</td>
											<td><span id="course_avgStudyNum"></span>次</td>
										</tr>
										<tr>
											<td>学习总时长</td>
											<td><span id="course_studyHourNum"></span>小时</td>
										</tr>
										<tr>
											<td>平均学习时长</td>
											<td><span id="course_avgStudyHourNum"></span>小时</td>
										</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>

					<!-- Loading (remove the following to stop the loading)-->
					<div class="overlay clockSituation" style="display: none;">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
					<!-- end loading -->
				</div>

				<!--课程学情统计-->
				<div class="box box-border no-shadow">
					<div class="box-header with-border">
						<h3 class="box-title"><i class="glyphicon glyphicon-stats margin_r10"></i>课程学情统计</h3>
						<div class="pull-right">
							<a href="${ctx}/home/class/learn/list">更多</a>
						</div>
					</div>
					<div class="box-body">
						<div class="form-inline">
							<select id="learnTermId" class="form-control margin_r10">
								<c:forEach var="item" items="${termList}" varStatus="s">
									<option value="${item.gradeId}" <c:if test="${s.index==(fn:length(termList)-1)}">selected</c:if>>第${s.index==0?'一':s.index==1?'二':s.index==2?'三':s.index==3?'四':s.index==4?'五':''}学期<c:if test="${s.index==(fn:length(termList)-1)}">(当前学期)</c:if></option>
								</c:forEach>
							</select>
							<select id="learnCourseId" class="form-control">
								<option value="">全部课程</option>
							</select>
							<span class="gray9 margin_l10">距离本学期结束还有 ${remainingDays} 天</span>
						</div>

						<div class="row count-info-list margin_b20 pad-t30">
							<div class="col-md-2_5 col-sm-4 col-xs-6 count-info-item">
								<div class="square">
									<span id="learn_studentNum"></span>
									<i>人</i>
								</div>
								<p>选课人数</p>
							</div>
							<div class="col-md-2_5 col-sm-4 col-xs-6 count-info-item">
								<div class="square">
									<span id="learn_avgSchedule"></span>
									<i>%</i>
								</div>
								<p>平均学习进度</p>
							</div>
							<div class="col-md-2_5 col-sm-4 col-xs-6 count-info-item">
								<div class="square">
									<span id="learn_avgStudyScore"></span>
									<i>分</i>
								</div>
								<p>平均学习成绩</p>
							</div>
							<div class="col-md-2_5 col-sm-4 col-xs-6 count-info-item">
								<div class="square">
									<span id="learn_avgStudyNum"></span>
									<i>次</i>
								</div>
								<p>平均学习次数</p>
							</div>
							<div class="col-md-2_5 col-sm-4 col-xs-6 count-info-item">
								<div class="square">
									<span id="learn_avgStudyHourNum"></span>
									<i>小时</i>
								</div>
								<p>平均学习时长</p>
							</div>
						</div>
					</div>

					<!-- Loading (remove the following to stop the loading)-->
					<div class="overlay learnSituation" style="display: none;">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
					<!-- end loading -->
				</div>

				<!--学习排行TOP10-->
				<div class="box box-border no-shadow margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title"><i class="glyphicon glyphicon-stats margin_r10"></i>学习排行TOP10</h3>
						<div class="pull-right">
							<%--<a href="${ctx}/home/class/clock/list">更多</a>--%>
						</div>
					</div>
					<div class="box-body">
						<div class="pie-menu fr" style="position:static;width:auto;">
							<div class="btn-group" role="group" data-role="tab">
								<a role="button" class="btn btn-default min-width-90px actived" data-toggle="tab">学习进度</a>
								<a role="button" class="btn btn-default min-width-90px">学习次数</a>
								<a role="button" class="btn btn-default min-width-90px">学习时长</a>
							</div>
						</div>
						<div class="rank-tab-content">
							<div class="form-inline">
								<select id="rankTermId" class="form-control margin_r10">
									<c:forEach var="item" items="${termList}" varStatus="s">
										<option value="${item.gradeId}" <c:if test="${s.index==(fn:length(termList)-1)}">selected</c:if>>第${s.index==0?'一':s.index==1?'二':s.index==2?'三':s.index==3?'四':s.index==4?'五':''}学期<c:if test="${s.index==(fn:length(termList)-1)}">(当前学期)</c:if></option>
									</c:forEach>
								</select>
								<select id="rankCourseId" class="form-control">
									<option value="">全部课程</option>
								</select>
								<span class="gray9 margin_l10">距离本学期结束还有 ${remainingDays} 天</span>
							</div>
							<div data-role="tab-pane" style="height: 225px;">
								<div class="row rank-list margin_t20">
									<div class="col-sm-6">
										<table id="topTenSchedule1" class="table border-bottom">
											<tbody>

											</tbody>
										</table>
									</div>

									<div class="col-sm-6">
										<table id="topTenSchedule2" class="table border-bottom">
											<tbody>

											</tbody>
										</table>
									</div>
								</div>
							</div>
							<div data-role="tab-pane" style="display:none;height: 225px;">
								<div class="row rank-list margin_t20">
									<div class="col-sm-6">
										<table id="topTenStudyNum1" class="table border-bottom">
											<tbody>

											</tbody>
										</table>
									</div>

									<div class="col-sm-6">
										<table id="topTenStudyNum2" class="table border-bottom">
											<tbody>

											</tbody>
										</table>
									</div>
								</div>
							</div>
							<div data-role="tab-pane" style="display:none;height: 225px;">
								<div class="row rank-list margin_t20">
									<div class="col-sm-6">
										<table id="topTenStudyHour1" class="table border-bottom">
											<tbody>

											</tbody>
										</table>
									</div>

									<div class="col-sm-6">
										<table id="topTenStudyHour2" class="table border-bottom">
											<tbody>

											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Loading (remove the following to stop the loading)-->
					<div class="overlay studyRanking" style="display: none;">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
					<!-- end loading -->
				</div>

			</div>
			<div class="tab-pane" id="tab_top_index_2">
				<iframe id="enterIFrame" src="${ctx}/openingSoon" frameborder="0" scrolling="no" allowtransparency="true" style="height: 420px;"></iframe>
			</div>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<!-- echarts 3 -->
<script src="${ctx}/static/plugins/echarts/echarts.min.js"></script>

<script type="text/javascript">
	window.onload=function(){
		top.$(".overlay-wrapper.loading").hide();
	}
	$(function(){
		$(window).bind("load resize",function(){
			$(".note-tit > .table-block").each(function(i,e){
				if ($(this).width()>$(this).parent().width()) {
					$(this).width("100%");
				};
			});
		});

		//班级考勤统计
		var chart8;
		$('#teachTermId').change(function() {
			$('.overlay.clockSituation').show();
			$.ajax({
				type: "GET",
				url: "${ctx}/home/class/clockSituation",
				data: {termId: $(this).val()},
				dataType: "json",
				async: true,	// 是否异步
				success: function(data) {
					var clockingInSituation, studySituation;
					if (data.successful) {
						clockingInSituation = data.obj.clockingInSituation;
						studySituation = data.obj.studySituation;
					}
					$('.overlay.clockSituation').hide();

					$('#studentNum').text(clockingInSituation.STUDENTNUM);
					$('#studyNum').text(studySituation.STUDYNUM);
					$('#avgStudyNum').text(clockingInSituation.STUDENTNUM != 0 ? parseInt(studySituation.STUDYNUM/clockingInSituation.STUDENTNUM) : 0);
					$('#studyHourNum').text(studySituation.STUDYHOURNUM);
					$('#avgStudyHourNum').text(clockingInSituation.STUDENTNUM != 0 ? parseInt(studySituation.STUDYHOURNUM/clockingInSituation.STUDENTNUM) : 0);

					chart8=(function(){
						var obj={};
						var renderData=[
							{
								name:'7天以上未学习人数',
								value:clockingInSituation.MORETHANSEVENDAYNOTLOGINNUM,
								itemStyle:{
									normal:{
										color:'#f56954'
									}
								}
							},
							{
								name:'3天以上未学习人数',
								value:clockingInSituation.MORETHANTHREEDAYNOTLOGINNUM,
								itemStyle:{
									normal:{
										color:'#00a65a'
									}
								}
							},
							{
								name:'3天以内未学习人数',
								value:clockingInSituation.WITHINTHREEDAYNOTLOGINNUM,
								itemStyle:{
									normal:{
										color:'#00c0ef'
									}
								}
							},
							{
								name:'从未学习人数',
								value:clockingInSituation.NOTLOGINNUM,
								itemStyle:{
									normal:{
										color:'#ffa412'
									}
								}
							},
							{
								name:'当前在学人数',
								value:clockingInSituation.ONLINELOGINNUM,
								itemStyle:{
									normal:{
										color:'#3c8dbc'
									}
								}
							}
						];
						obj.option={
							tooltip : {
								trigger: 'axis',
								axisPointer : {
									type : 'shadow'
								},
								formatter:'{b}：{c}'
							},
							grid: {
								left:10,
								right:10,
								bottom: 10,
								top:20,
								containLabel: true
							},
							xAxis : [
								{
									type : 'category',
									data : $.map(renderData,function(n){ return n.name }),
									axisTick: {
										alignWithLabel: true
									},
									axisLine:{
										lineStyle:{
											color:'#e5e5e5',
											width:1
										}
									},
									axisLabel:{
										textStyle:{
											fontSize:14,
											color:'#333'
										},
										formatter:function (value, index) {
											if( index<3 ){
												if( value.length>4 ){
													return value.substring(0,4)+'\n'+value.substring(5,value.length);
												}
												else{
													return value;
												}
											}
											else{
												return value;
											}
										}
									}

								}
							],
							yAxis : [
								{
									type : 'value',
									axisLine:{
										lineStyle:{
											color:'#e5e5e5',
											width:1
										}
									},
									axisLabel:{
										textStyle:{
											fontSize:14,
											color:'#333'
										},
										formatter:'{value}'
									}
								}
							],
							series : [
								{
									name:'网考记录统计',
									type:'bar',
									barWidth: '50%',
									label:{
										normal:{
											position:'top',
											show:true,
											formatter: '{c}'
										}
									},
									data:renderData
								}
							]
						}
						obj.instance=echarts.init(document.getElementById('chart8'));
						obj.instance.setOption(obj.option);
						return obj;
					})();
				}
			});
		});

		$('#courseTermId').change(function() {
			$('#courseId')[0].options.length = 0;
			$.getJSON(ctx+'/home/class/getCourses', {termId:$(this).val()}, function (data) {
				$.each(data.obj, function(i,item){
					$('#courseId')[0].options.add(new Option(item.kcmc, item.courseId));
				});
				$('#courseId').trigger('change');
			});
		});

		var chart9;
		$('#courseId').change(function() {
			$('.overlay.clockSituation').show();
			$.ajax({
				type: "GET",
				url: "${ctx}/home/class/clockSituationByCourseId",
				data: {courseId: $(this).val()},
				dataType: "json",
				async: true,	// 是否异步
				success: function(data) {
					var clockingInSituation, studySituation;
					if (data.successful) {
						clockingInSituation = data.obj.clockingInSituation;
						studySituation = data.obj.studySituation;
					}
					$('.overlay.clockSituation').hide();

					$('#course_studentNum').text(clockingInSituation.STUDENTNUM);
					$('#course_studyNum').text(studySituation.STUDYNUM);
					$('#course_avgStudyNum').text(clockingInSituation.STUDENTNUM != 0 ? parseInt(studySituation.STUDYNUM / clockingInSituation.STUDENTNUM) : 0);
					$('#course_studyHourNum').text(studySituation.STUDYHOURNUM);
					$('#course_avgStudyHourNum').text(clockingInSituation.STUDENTNUM != 0 ? parseInt(studySituation.STUDYHOURNUM / clockingInSituation.STUDENTNUM) : 0);

					chart9=(function() {
						var obj={};
						var renderData=[
							{
								name:'7天以上未学习人数',
								value:clockingInSituation.MORETHANSEVENDAYNOTLOGINNUM,
								itemStyle:{
									normal:{
										color:'#f56954'
									}
								}
							},
							{
								name:'3天以上未学习人数',
								value:clockingInSituation.MORETHANTHREEDAYNOTLOGINNUM,
								itemStyle:{
									normal:{
										color:'#00a65a'
									}
								}
							},
							{
								name:'3天以内未学习人数',
								value:clockingInSituation.WITHINTHREEDAYNOTLOGINNUM,
								itemStyle:{
									normal:{
										color:'#00c0ef'
									}
								}
							},
							{
								name:'从未学习人数',
								value:clockingInSituation.NOTLOGINNUM,
								itemStyle:{
									normal:{
										color:'#ffa412'
									}
								}
							},
							{
								name:'当前在学人数',
								value:clockingInSituation.ONLINELOGINNUM,
								itemStyle:{
									normal:{
										color:'#3c8dbc'
									}
								}
							}
						];
						obj.option={
							tooltip : {
								trigger: 'axis',
								axisPointer : {
									type : 'shadow'
								},
								formatter:'{b}：{c}'
							},
							grid: {
								left:10,
								right:10,
								bottom: 10,
								top:20,
								containLabel: true
							},
							xAxis : [
								{
									type : 'category',
									data : $.map(renderData,function(n){ return n.name }),
									axisTick: {
										alignWithLabel: true
									},
									axisLine:{
										lineStyle:{
											color:'#e5e5e5',
											width:1
										}
									},
									axisLabel:{
										textStyle:{
											fontSize:14,
											color:'#333'
										},
										formatter:function (value, index) {
											if( index<3 ){
												if( value.length>4 ){
													return value.substring(0,4)+'\n'+value.substring(5,value.length);
												}
												else{
													return value;
												}
											}
											else{
												return value;
											}
										}
									}

								}
							],
							yAxis : [
								{
									type : 'value',
									axisLine:{
										lineStyle:{
											color:'#e5e5e5',
											width:1
										}
									},
									axisLabel:{
										textStyle:{
											fontSize:14,
											color:'#333'
										},
										formatter:'{value}'
									}
								}
							],
							series : [
								{
									name:'网考记录统计',
									type:'bar',
									barWidth: '50%',
									label:{
										normal:{
											position:'top',
											show:true,
											formatter: '{c}'
										}
									},
									data:renderData
								}
							]
						}
						obj.instance=echarts.init(document.getElementById('chart9'));
						obj.instance.setOption(obj.option);
						return obj;
					})();
				}
			});
		});

		$('[data-role="tab-list"] a').click(function(event) {
			event.preventDefault()
			var index=$(this).parent().index();
			$(this).tab('show');
			//班级考勤统计
			if(index==0){
				if($('#chart8').children().eq(0).width()==0){
					chart8.instance.resize();
				}
			}
			//课程考勤统计
			else{
				if($('#chart9').children().eq(0).width()==0){
					chart9.instance.resize();
				}
			}
		});


		$(window).bind("resize",function(){
			if(!chart8==undefined){
		 		chart8.instance.resize(); 
			}
			if(!chart9==undefined){
				chart9.instance.resize();
			}
		});

		$('#learnTermId').change(function() {
			$('#learnCourseId')[0].options.length = 1;
			$.getJSON(ctx+'/home/class/getCourses', {termId:$(this).val()}, function (data) {
				$.each(data.obj, function(i,item){
					$('#learnCourseId')[0].options.add(new Option(item.kcmc, item.courseId));
				});
				$('#learnCourseId').trigger('change');
			});
		});

		// 课程学情统计 课程切换
		$('#learnCourseId').change(function() {
			$('.overlay.learnSituation').show();
			$.ajax({
				type: "GET",
				url: "${ctx}/home/class/learnSituationByCourseId",
				data: {termId: $('#learnTermId').val(), courseId: $(this).val()},
				dataType: "json",
				async: true,	// 是否异步
				success: function(data) {
					var learnSituation;
					if (data.successful) {
						learnSituation = data.obj.learnSituation;
					}
					$('.overlay.learnSituation').hide();

					$('#learn_studentNum').text(learnSituation.STUDENTNUM);
					$('#learn_avgSchedule').text(learnSituation.STUDENTNUM != 0 ? parseInt(learnSituation.TOTALSCHEDULE / learnSituation.STUDENTNUM) : 0);
					$('#learn_avgStudyScore').text(learnSituation.STUDENTNUM != 0 ? parseInt(learnSituation.TOTALSTUDYSCORE / learnSituation.STUDENTNUM) : 0);
					$('#learn_avgStudyNum').text(learnSituation.STUDENTNUM != 0 ? parseInt(learnSituation.STUDYNUM / learnSituation.STUDENTNUM) : 0);
					$('#learn_avgStudyHourNum').text(learnSituation.STUDENTNUM != 0 ? parseInt(learnSituation.STUDYHOURNUM / learnSituation.STUDENTNUM) : 0);
				}
			});
		});

		$('#rankTermId').change(function() {
			$('#rankCourseId')[0].options.length = 1;
			$.getJSON(ctx+'/home/class/getCourses', {termId:$(this).val()}, function (data) {
				$.each(data.obj, function(i,item){
					$('#rankCourseId')[0].options.add(new Option(item.kcmc, item.courseId));
				});
				$('#rankCourseId').trigger('change');
			});
		});

		// 学习排行TOP10 课程切换
		$('#rankCourseId').change(function() {
			$('.overlay.studyRanking').show();
			$.ajax({
				type: "GET",
				url: "${ctx}/home/class/studyRankingByCourseId",
				data: {termId: $('#rankTermId').val(), courseId: $(this).val()},
				dataType: "json",
				async: true,	// 是否异步
				success: function(data) {
					if (data.successful) {
						$('#topTenSchedule1 tbody').empty();
						$('#topTenSchedule2 tbody').empty();
						$('#topTenStudyNum1 tbody').empty();
						$('#topTenStudyNum2 tbody').empty();
						$('#topTenStudyHour1 tbody').empty();
						$('#topTenStudyHour2 tbody').empty();
						// 左边为Top5，右边为Top6-10
						for(var i = 0; i < data.obj.topTenSchedule.length && i < 5; i++) {
							var info = data.obj.topTenSchedule[i];
							$('#topTenSchedule1 tbody').append('<tr><td class="td1">'+info.XM+'</td><td class="td2"><span class="gray9 margin_r10">平均学习进度</span><span>'+info.AVG_SCHEDULE+'%</span></td></tr>');
						}
						for(var i = 5; i < data.obj.topTenSchedule.length && i < 10; i++) {
							var info = data.obj.topTenSchedule[i];
							$('#topTenSchedule2 tbody').append('<tr><td class="td1">'+info.XM+'</td><td class="td2"><span class="gray9 margin_r10">平均学习进度</span><span>'+info.AVG_SCHEDULE+'%</span></td></tr>');
						}
						for(var i = 0; i < data.obj.topTenStudyNum.length && i < 5; i++) {
							var info = data.obj.topTenStudyNum[i];
							$('#topTenStudyNum1 tbody').append('<tr><td class="td1">'+info.XM+'</td><td class="td2"><span class="gray9 margin_r10">平均学习次数</span><span>'+info.AVG_STUDYNUM+'次</span></td></tr>');
						}
						for(var i = 5; i < data.obj.topTenStudyNum.length && i < 10; i++) {
							var info = data.obj.topTenStudyNum[i];
							$('#topTenStudyNum2 tbody').append('<tr><td class="td1">'+info.XM+'</td><td class="td2"><span class="gray9 margin_r10">平均学习次数</span><span>'+info.AVG_STUDYNUM+'次</span></td></tr>');
						}
						for(var i = 0; i < data.obj.topTenStudyHour.length && i < 5; i++) {
							var info = data.obj.topTenStudyHour[i];
							$('#topTenStudyHour1 tbody').append('<tr><td class="td1">'+info.XM+'</td><td class="td2"><span class="gray9 margin_r10">平均学习时长</span><span>'+info.AVG_STUDYHOUR+'小时</span></td></tr>');
						}
						for(var i = 5; i < data.obj.topTenStudyHour.length && i < 10; i++) {
							var info = data.obj.topTenStudyHour[i];
							$('#topTenStudyHour2 tbody').append('<tr><td class="td1">'+info.XM+'</td><td class="td2"><span class="gray9 margin_r10">平均学习时长</span><span>'+info.AVG_STUDYHOUR+'小时</span></td></tr>');
						}
					}
					$('.overlay.studyRanking').hide();
				}
			});
		});

		// 学习排行
		$('[data-role="tab"] a').click(function(event) {
			event.preventDefault();
			var index=$(this).index();
			$(this).addClass('actived').siblings('a').removeClass('actived');
			$('[data-role="tab-pane"]').hide().eq(index).show();
		});

		// init
		$('#teachTermId').trigger('change');
		$('#courseTermId').trigger('change');
		$('#learnTermId').trigger('change');
		$('#rankTermId').trigger('change');

	})
</script>
</body>
</html>
