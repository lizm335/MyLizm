<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>学期分析</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:">学习管理</a></li>
		<li class="active">考勤分析</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal" action="${ctx}/studymanage/getCourseLoginList">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<shiro:hasPermission name="/studymanage/getCourseLoginList$attendanceTab3">
					<li class="active"><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getCourseLoginList');">课程考勤</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/studymanage/getCourseLoginList$attendanceTab1">
					<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getCourseClassLoginList');">课程班考勤</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/studymanage/getCourseLoginList$attendanceTab2">
					<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getStudentLoginList');">学员考勤</a></li>
				</shiro:hasPermission>
			</ul>
			
			<div class="tab-content">
				
				<div class="box box-border">
				    <div class="box-body">
				        <div class="row pad-t15">
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">开课学期</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="GRADE_ID" id="GRADE_ID" data-size="5" data-live-search="true">
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
				              <label class="control-label col-sm-3 text-nowrap">课程</label>
				              <div class="col-sm-9">
				                <%--<input type="text" class="form-control" name="KCMC" value="${param.KCMC }">--%>
								  <input type="hidden" name="TIME_FLG" value="" id="time_flg">
								  <select class="selectpicker show-tick form-control" name="COURSE_ID" id="COURSE_ID" data-size="5" data-live-search="true">
									  <option value="" selected="selected">请选择</option>
									  <c:forEach items="${courseMap}" var="map">
										  <option value="${map.key}" <c:if test="${param.COURSE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
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
				          
				        </div>
				    </div><!-- /.box-body -->
				    <div class="box-footer text-right">
			          <button type="submit" class="btn min-width-90px btn-primary margin_r15" id="submit_button">搜索</button>
			          <button type="reset" class="btn min-width-90px btn-default">重置</button>
			        </div><!-- /.box-footer-->
				</div>
				<div class="box box-border margin-bottom-none">
					<div class="box-body">
						<div class="filter-tabs clearfix">
							<ul class="list-unstyled">

								<li <c:if test="${empty param.TIME_FLG}">class="actived"</c:if> id="data_btn_all">全部(<span id="data_btn_all_num">0</span>)</li>
								<li <c:if test="${ param.TIME_FLG eq '1'}">class="actived"</c:if> id="data_btn_ing">开课中(<span id="data_btn_ing_num">0</span>)</li>
								<li <c:if test="${ param.TIME_FLG eq '2'}">class="actived"</c:if> id="data_btn_wait">待开课(<span id="data_btn_wait_num">0</span>)</li>
								<li <c:if test="${ param.TIME_FLG eq '3'}">class="actived"</c:if> id="data_btn_end">已结束(<span id="data_btn_end_num">0</span>)</li>
							</ul>
							<shiro:hasPermission name="/studymanage/getCourseLoginList$export">
								<div class="pull-right no-margin">
									<a href="${ctx}/studymanage/courseLoginListExport/${pageInfo0}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出课程考勤统计表</a>
								</div>
							</shiro:hasPermission>
						</div>
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
					              <tr>
					              	<th>开课信息</th>
					                <th>
										<span class="inline-block vertical-middle">选课人数</span>
										<span class="inline-block vertical-middle" style="width:12px;line-height:1;">
											<input type="hidden" name="CHOOSE_COUNT_SORT" value="${param.CHOOSE_COUNT_SORT}">
											<c:choose>
												<c:when test="${param.CHOOSE_COUNT_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
											</c:choose>
					                		<c:choose>
												<c:when test="${param.CHOOSE_COUNT_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
											</c:choose>
					                	</span>
									</th>
					                <th>
										<span class="inline-block vertical-middle">学习总次数/<br>平均学习次数</span>
										<span class="inline-block vertical-middle" style="width:12px;line-height:1;">
											<input type="hidden" name="STUDY_COUNT_SORT" value="${param.STUDY_COUNT_SORT}">
											<c:choose>
												<c:when test="${param.STUDY_COUNT_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
											</c:choose>
					                		<c:choose>
												<c:when test="${param.STUDY_COUNT_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
											</c:choose>
					                	</span>
									</th>
					                <th>
										<span class="inline-block vertical-middle">学习总时长/<br>平均学习时长</span>
										<span class="inline-block vertical-middle" style="width:12px;line-height:1;">
											<input type="hidden" name="STUDY_TIMES_SORT" value="${param.STUDY_TIMES_SORT}">
											<c:choose>
												<c:when test="${param.STUDY_TIMES_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
											</c:choose>
					                		<c:choose>
												<c:when test="${param.STUDY_TIMES_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
											</c:choose>
					                	</span>
									</th>
					                <th>
										<span class="inline-block vertical-middle">7天以上<br>未学习人数</span>
										<span class="inline-block vertical-middle" style="width:12px;line-height:1;">
											<input type="hidden" name="DAY7_LOGIN_SORT" value="${param.DAY7_LOGIN_SORT}">
											<c:choose>
												<c:when test="${param.DAY7_LOGIN_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
											</c:choose>
					                		<c:choose>
												<c:when test="${param.DAY7_LOGIN_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
											</c:choose>
					                	</span>
									</th>
					                <th>
										<span class="inline-block vertical-middle">3天以上<br>未学习人数</span>
										<span class="inline-block vertical-middle" style="width:12px;line-height:1;">
											<input type="hidden" name="DAY3_7_LOGIN_SORT" value="${param.DAY3_7_LOGIN_SORT}">
											<c:choose>
												<c:when test="${param.DAY3_7_LOGIN_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
											</c:choose>
					                		<c:choose>
												<c:when test="${param.DAY3_7_LOGIN_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
											</c:choose>
					                	</span>
									</th>
					                <th>
										<span class="inline-block vertical-middle">3天以内<br>未学习人数</span>
										<span class="inline-block vertical-middle" style="width:12px;line-height:1;">
											<input type="hidden" name="DAY3_LOGIN_SORT" value="${param.DAY3_LOGIN_SORT}">
											<c:choose>
												<c:when test="${param.DAY3_LOGIN_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
											</c:choose>
					                		<c:choose>
												<c:when test="${param.DAY3_LOGIN_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
											</c:choose>
					                	</span>
									</th>
					                <th>
										<span class="inline-block vertical-middle">从未学习人数</span>
										<span class="inline-block vertical-middle" style="width:12px;line-height:1;">
											<input type="hidden" name="UNSTUDY_COUNT_SORT" value="${param.UNSTUDY_COUNT_SORT}">
											<c:choose>
												<c:when test="${param.UNSTUDY_COUNT_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
											</c:choose>
					                		<c:choose>
												<c:when test="${param.UNSTUDY_COUNT_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
											</c:choose>
					                	</span>
									</th>
					                <th>
										<span class="inline-block vertical-middle">当前在学人数</span>
										<span class="inline-block vertical-middle" style="width:12px;line-height:1;">
											<input type="hidden" name="STUDYING_COUNT_SORT" value="${param.STUDYING_COUNT_SORT}">
											<c:choose>
												<c:when test="${param.STUDYING_COUNT_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
											</c:choose>
					                		<c:choose>
												<c:when test="${param.STUDYING_COUNT_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
												<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
											</c:choose>
					                	</span>
									</th>
					                <th>操作</th>
					              </tr>
					            </thead>
					            <tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.content}" var="item">
											<tr>
												<td>
													<div class="text-left">
														开课学期：${item.GRADE_NAME}<br>
														课程名称：${item.KCMC}<br>
														课程代码：${item.KCH}<br>
														开课状态：<c:choose>
																	<c:when test="${item.TIME_FLG eq '1'.charAt(0) }">
																		<span class="text-green">开课中</span>
																	</c:when>
																	<c:when test="${item.TIME_FLG eq '2'.charAt(0) }">
																		<span class="text-orange">待开课</span>
																	</c:when>
																	<c:when test="${item.TIME_FLG eq '3'.charAt(0) }">
																		<span class="gray9">已结束</span>
																	</c:when>
																	<c:otherwise>
																		<span class="gray9">已结束</span>
																	</c:otherwise>
																</c:choose>
													</div>
												</td>
												<td>
													<c:choose>
														<c:when test="${not empty item.REC_COUNT}">${item.REC_COUNT}</c:when>
														<c:otherwise>--</c:otherwise>
													</c:choose>
												</td>
												<td>${item.SUM_LOGIN_COUNT}/${item.AVG_LOGIN_COUNT}</td>
												<td>${item.SUM_LOGIN_TIME}/${item.AVG_LOGIN_TIME}</td>
												<td>${item.DAY7_LOGIN}</td>
												<td>${item.DAY3_7_LOGIN}</td>
												<td>${item.DAY3_LOGIN}</td>
												<td>${item.NO_DAY_LOGIN}</td>
												<td>${item.ONLINE_STUDENT_COUNT}</td>
												<td>
													<a href="${ctx}/studymanage/${item.COURSE_ID}/${item.GRADE_ID}/getCourseClockingDetail" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise><tr><td colspan="12">暂无数据！</td></tr></c:otherwise>
								</c:choose>
					            </tbody>
							</table>
							<tags:pagination page="${pageInfo}" paginationSize="5" />
						</div>
					</div>
				</div>
				
			</div>
		</div>
	</form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
    //排序
    $(".glyphicon-triangle-top").click(function () {
        var old_arg = $(this).prevAll("input").val();
        if(old_arg=='ASC'){
            $(this).prevAll("input").val("");
        }else {
            $(this).prevAll("input").val("ASC")
        }
        $("#submit_button").click();
    });
    $(".glyphicon-triangle-bottom").click(function () {
        var old_arg = $(this).prevAll("input").val();
        if(old_arg=='DESC'){
            $(this).prevAll("input").val("");
        }else {
            $(this).prevAll("input").val("DESC");
        }
        $("#submit_button").click();
    });

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

    // filter tabs
    $(".filter-tabs li").click(function(event) {
        if($(this).hasClass('actived')){
            $(this).removeClass('actived');
        }
        else{
            $(this).addClass('actived');
        }
    });
    //按钮切换
	$("#data_btn_all,#data_btn_ing,#data_btn_wait,#data_btn_end").click(function () {
		var this_id =$(this).attr("id");
		switch (this_id){
			case "data_btn_all":
			    $("#time_flg").val("");
			    break;
			case "data_btn_ing":
                $("#time_flg").val("1");
                break;
			case "data_btn_wait":
                $("#time_flg").val("2");
                break;
			case "data_btn_end":
                $("#time_flg").val("3");
                break;
		}
        $("#submit_button").click();
    });


    function getRecordCount(type, code) {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: 'getCourseLoginCount?TIME_FLG_TEMP='+code,
            data: $('#listForm').serialize(),
            success: function (result) {
                $("#"+type).html(result.LOGIN_STATE_COUNT);
            },
            error: function(data) {}
        });
    }

    $(function() {
        $("#COURSE_ID").selectpicker();
        $("#GRADE_ID").selectpicker();
        getRecordCount('data_btn_all_num', '');
        getRecordCount('data_btn_ing_num', '1');
        getRecordCount('data_btn_wait_num', '2');
        getRecordCount('data_btn_end_num', '3');
    });
</script>
</body>
</html>