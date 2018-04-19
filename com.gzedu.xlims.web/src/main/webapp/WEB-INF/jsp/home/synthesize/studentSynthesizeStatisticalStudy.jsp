<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body no-padding">

<section class="content">
    <div class="nav-tabs-custom margin-bottom-none">
      <ul class="nav nav-tabs">
        <li>
          <a href="${ctx}/admin/home/myWorkbench" target="_self">我的工作台</a>
        </li>
          <li>
              <a href="${ctx}/admin/home/statistical" target="_self">统计分析</a>
          </li>
          <li class="active">
              <a href="${ctx}/admin/home/studentSynthesizeStatistical" target="_self">学生综合信息查询</a>
          </li>
          <li>
              <a href="${ctx}/admin/home/businessSynthesizeStatistical" target="_self">业务综合信息查询</a>
          </li>
      </ul>
    </div>
    
    <form id="listForm" class="form-horizontal">
		<div class="nav-tabs-custom no-margin">
			<div class="tab-content">
				<div class="tab-pane active" id="tab_top_2">
					<div class="box box-border">
					    <div class="box-body">
					      <form class="form-horizontal">
					        <div class="row pad-t15">

					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">姓名</label>
					              <div class="col-sm-9">
					                <input type="text" class="form-control" name="XM" value="${param.XM }">
									  <input type="hidden" name="STUDY_STATUS" value="${param.STUDY_STATUS}" id="study_status">
					              </div>
					            </div>
					          </div>
	
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">学号</label>
					              <div class="col-sm-9">
					                <input type="text" class="form-control" name="XH" value="${param.XH }">
					              </div>
					            </div>
					          </div>

							  <div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学习中心</label>
									<div class="col-sm-9">
										<select name="XXZX_ID" class="selectpicker show-tick form-control" 	data-size="10" data-live-search="true" id="XXZX_ID">
											<option value="">请选择</option>
											<c:forEach items="${studyCenterMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==param.XXZX_ID}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							  </div>

							  <div class="col-sm-4">
								  <div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">资料审批</label>
										<div class="col-sm-9">
											<select class="selectpicker show-tick form-control" name="auditState" id="auditState" data-size="10" data-live-search="true">
												<option value="" selected="">- 请选择 -</option>
												<c:forEach items="${auditStateMap}" var="map">
													<option value="${map.key}" <c:if test="${param.auditState eq map.key }">selected="selected"</c:if>>${map.value}</option>
												</c:forEach>
											</select>
										</div>
								  </div>
							  </div>

								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">学员类型</label>
										<div class="col-sm-9">
											<c:if test="${sessionScope.current_user.loginAccount=='admin'}">
												<dic:selectBox name="EQ_userType" typeCode="USER_TYPE" code="${param.EQ_userType }" otherAttrs='class="selectpicker show-tick form-control" data-size="10" data-live-search="true"' />
											</c:if>
											<c:if test="${sessionScope.current_user.loginAccount!='admin'}">
												<dic:selectBox name="EQ_userType" typeCode="USER_TYPE" code="${param.EQ_userType }" excludes="21,31" otherAttrs='class="selectpicker show-tick form-control" data-size="10" data-live-search="true"' />
											</c:if>
										</div>
									</div>
								</div>

								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">学籍状态</label>
										<div class="col-sm-9">
											<dic:selectBox name="EQ_xjzt" typeCode="StudentNumberStatus" code="${param.EQ_xjzt }" excludes="5" otherAttrs='class="selectpicker show-tick form-control" data-size="10" data-live-search="true"' />
										</div>
									</div>
								</div>
	
								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">入学学期</label>
										<div class="col-sm-9">
											<select class="selectpicker show-tick form-control" name="GRADE_ID" id="GRADE_ID" data-size="10" data-live-search="true">
												<option value="all" selected="">全部学期</option>
												<c:forEach items="${gradeMap}" var="map">
													<c:if test="${empty param.GRADE_ID}">
														<option value="${map.key}" <c:if test="${currentGradeId eq map.key }">selected="selected"</c:if>>${map.value}</option>
													</c:if>
													<c:if test="${not empty param.GRADE_ID}">
														<option value="${map.key}" <c:if test="${param.GRADE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
													</c:if>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
	
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">报读专业</label>
					              <div class="col-sm-9">
					                <select class="selectpicker show-tick form-control" name="SPECIALTY_ID" id="specialty_id" data-size="10" data-live-search="true">
					                	<option value="" selected="selected">请选择</option>
					                	<c:forEach items="${specialtyMap}" var="map">
					                  		<option value="${map.key}" <c:if test="${param.SPECIALTY_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
					                  	</c:forEach>
					                </select>
					              </div>
					            </div>
					          </div>

							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业层次</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="PYCC" id="pycc" data-size="10" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}" <c:if test="${param.PYCC eq map.key}">selected='selected'</c:if>>
														${map.value}
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
							  </div>
					        </div>
					      </form>
					    </div><!-- /.box-body -->
					    <div class="box-footer text-right">
				          <button type="submit" class="btn min-width-90px btn-primary margin_r15" id="submit_buttion">搜索</button>
				          <button type="reset" class="btn min-width-90px btn-default">重置</button>
				        </div><!-- /.box-footer-->
					</div>
					<div class="box box-border margin-bottom-none">
						<ul class="nav nav-tabs nav-tabs-lg">
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatistical?${searchParams }');">全部</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalFee?${searchParams }');">缴费</a></li>
							<li class="active"><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalStudy?${searchParams }');">学习</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalClockingIn?${searchParams }');">考勤</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalExam?${searchParams }');">考试</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalThesis?${searchParams }');">论文</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalPractice?${searchParams }');">实践</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalGraduation?${searchParams }');">毕业</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalLink?${searchParams }');">链接</a></li>
						</ul>
						
						<div class="box-header with-border">
							<div class="filter-tabs clearfix">
								<ul class="list-unstyled">
									<li <c:if test="${empty param.STUDY_STATUS}">class="actived"</c:if> id="data_btn_all">全部(<span id="data_btn_all_num">0</span>)</li>
									<li <c:if test="${param.STUDY_STATUS eq '0'}">class="actived"</c:if> id="data_btn_0">学情正常(<span id="data_btn_0_num">0</span>)</li>
									<li <c:if test="${param.STUDY_STATUS eq '1'}">class="actived"</c:if> id="data_btn_1">异常，有落后课程(<span id="data_btn_1_num">0</span>)</li>
								</ul>
							<shiro:hasPermission name="/admin/home/myWorkbench$exportStudy">
								<div class="pull-right no-margin">
									<a href="${ctx}/excelExport/validateSmsCode/${pageInfo.totalElements}?formAction=/admin/home/downLoadStudentCourseListExportXls" class="btn btn-default btn-sm" data-role="export">
											<i class="fa fa-fw fa-download"></i> 导出学员</a>
								</div>
							</shiro:hasPermission>
							</div>
						</div>
						<div class="box-body">
							<div class="table-responsive">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
						              <tr>
						              	<th>头像</th>
						              	<th>个人信息</th>
						              	<th <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">width="250px"</shiro:lacksPermission>>报读信息</th>
									    <th>全部<br/>课程</th>
						                <th>学习中</th>
						                <th>正常的</th>
						                <th>落后的</th>
						                <th>已通过</th>
						                <th>未通过</th>
						                <th>登记中</th>
						                <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
											<th>总学分</th>
											<th>已获学分</th>
											<th>未获学分</th>
									    </shiro:lacksPermission>
										<th>操作</th>
						              </tr>
						            </thead>
						            <tbody>
									<c:choose>
										<c:when test="${not empty pageInfo.content}">
											<c:forEach items="${pageInfo.content}" var="entity">
											<tr>
												<td class="text-center" width="90">
													<c:choose>
														<c:when test="${not empty entity.ZP}">
															<img src="${entity.ZP}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/images/headImg04.png'">
														</c:when>
														<c:otherwise>
															<img src="${ctx }/static/images/headImg04.png" class="img-circle" width="50" height="50">
														</c:otherwise>
													</c:choose>
													<a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
														<i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
														<span class="gray9">交流</span>
													</a>
												</td>
												<td>
													<div class="text-left">
														姓名：${entity.XM }<br>
														学号：${entity.XH }<br>
														<shiro:hasPermission name="/personal/index$privacyJurisdiction">
														手机：${entity.SJH }<br>
														</shiro:hasPermission>
														学员类型：<dic:getLabel typeCode="USER_TYPE" code="${entity.USER_TYPE }"/><br>
														学籍状态：<dic:getLabel typeCode="StudentNumberStatus" code="${entity.XJZT }"/>
													</div>
												</td>
												<td class="text-left">
												<shiro:hasPermission name="/studymanage/getCourseStudyList$schoolModel">
													<div class="text-left">
														报读专业：${entity.ZYMC }<br>
														专业层次：${entity.PYCC_NAME }<br>
														入学年级：${entity.YEAR_NAME }<br>
														入学学期：${entity.START_GRADE }
													</div>
												</shiro:hasPermission>
												<shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
													<div class="text-left">
														专业层次：${entity.PYCC_NAME }<br>
														入学年级：${entity.YEAR_NAME }<br>
														入学学期：${entity.START_GRADE }<br>
														报读专业：${entity.ZYMC }<br>
														教务班级：${entity.BJMC }<br>
														学习中心：${entity.SC_NAME}
													</div>
												</shiro:lacksPermission>
												</td>
												<td>${entity.REC_COUNT }</td>
												<td>${entity.LEARNING_REC_COUNT }</td>
												<td>${entity.NORMAL_REC_COUNT }</td>
												<td>
												<c:choose>
													<c:when test="${entity.BEHIND_REC_COUNT==0 }">
													${entity.BEHIND_REC_COUNT }
													</c:when>
													<c:otherwise>
													<span class="text-orange">
													${entity.BEHIND_REC_COUNT }
													<br/>(异常)
													</span>
													</c:otherwise>
												</c:choose>
												</td>
												<td>${entity.PASS_REC_COUNT }</td>
												<td>${entity.UNPASS_REC_COUNT }</td>
												<td>${entity.SUM_REGISTER_COUNT }</td>
												<shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
													<td>
														${entity.ZXF }
													</td>
													<td>
														${entity.SUM_GET_CREDITS }
													</td>
													<td>
														${entity.ZXF - entity.SUM_GET_CREDITS }
													</td>
												</shiro:lacksPermission>
												<td>
													<a href="getStudentCourseDetails/${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
												</td>
											</tr>
											</c:forEach>
										</c:when>
									</c:choose>
						            </tbody>
								</table>
								<tags:pagination page="${pageInfo}" paginationSize="5" />
							</div>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
// 导出
$('[data-role="export"]').click(function(event) {
	event.preventDefault();
	var self=this;
	$.mydialog({
		id:'export',
		width:600,
		height:415,
		zIndex:11000,
		content: 'url:'+$(this).attr('href')
	});
});

function formatSeconds(value) {
	var theTime = parseInt(value);// 秒
	var theTime1 = 0;// 分
	var theTime2 = 0;// 小时
	if(theTime > 60) {
		theTime1 = parseInt(theTime/60);
		theTime = parseInt(theTime%60);
		if(theTime1 > 60) {
			theTime2 = parseInt(theTime1/60);
			theTime1 = parseInt(theTime1%60);
		}
	}
	var result = ""+parseInt(theTime)+"秒";
	if(theTime1 > 0) {
		result = ""+parseInt(theTime1)+"分"+result;
	}
	if(theTime2 > 0) {
		result = ""+parseInt(theTime2)+"小时"+result;
	}
	return result;
}

$(".login_time").each(function(){
	var login_time = $(this).html();
	login_time = formatSeconds(login_time);
	$(this).html(login_time);
})

//按钮切换
$("#data_btn_all,#data_btn_1").click(function () {
    var this_id =$(this).attr("id");
    switch (this_id){
        case "data_btn_all":
            $("#study_status").val("");
            break;
        case "data_btn_0":
            $("#study_status").val("0");
            break;
        case "data_btn_1":
            $("#study_status").val("1");
            break;
    }
    $("#submit_buttion").click();
});


function getRecordCount(type, code) {
    $.ajax({
        type: "POST",
        dataType: "json",
        url: ctx + '/admin/home/getStudentCourseCount?STUDY_STATUS_TEMP='+code,
        data: $('#listForm').serialize(),
        success: function (result) {
            $("#"+type).html(result.STUDY_STATUS_COUNT);
        },
        error: function(data) {}
    });
}

$(function() {
    $("#XXZX_ID").selectpicker();
    $("#pycc").selectpicker();
    $("#GRADE_ID").selectpicker();
    $("#specialty_id").selectpicker();
    $("#auditState").selectpicker();
    getRecordCount('data_btn_all_num', '');
    getRecordCount('data_btn_0_num', '0');
    getRecordCount('data_btn_1_num', '1');
});
</script>
</body>
</html>
