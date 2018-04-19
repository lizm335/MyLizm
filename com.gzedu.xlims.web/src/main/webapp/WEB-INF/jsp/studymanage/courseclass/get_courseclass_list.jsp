<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>学情分析</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:">学习管理</a></li>
		<li class="active">学情分析</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<shiro:hasPermission name="/studymanage/getCourseStudyList$conditionTab1">
					<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getCourseStudyList');" >课程学情</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/studymanage/getCourseStudyList$conditionTab2">
					<li class="active"><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getCourseClassList');" >课程班学情</a></li>
				</shiro:hasPermission>
				<%--<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getTeachClassList');" >教务班学情</a></li>--%><%--暂时隐藏吧。。。--%>
				<shiro:hasPermission name="/studymanage/getCourseStudyList$conditionTab3">
					<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getStudentCourseList');" >学员学情</a></li>
				</shiro:hasPermission>
				<%--<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getStudentMajorList');" >专业学情</a></li>--%><%--暂时隐藏吧。。。--%>
			</ul>
			
			<div class="tab-content">
				<div class="tab-pane active" id="tab_top_2">
					<div class="box box-border">
					    <div class="box-body">
					        <div class="row pad-t15">
					          
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">开课学期</label>
					              <div class="col-sm-9">
					                <%--<input type="text" class="form-control" name="GRADE_NAME" value="${param.GRADE_NAME }">--%>
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
	
<%--
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">课程</label>
					              <div class="col-sm-9">
					                <input type="text" class="form-control" name="KCMC" value="${param.KCMC }">
					              </div>
					            </div>
					          </div>
--%>
							  <div class="col-sm-4">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">课程</label>
										<div class="col-sm-9">
											<select class="selectpicker show-tick form-control" name="COURSE_ID" id="COURSE_ID" data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${courseMap}" var="map">
													<option value="${map.key}" <c:if test="${param.COURSE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
							  </div>
<%--
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">课程班</label>
					              <div class="col-sm-9">
					                <input type="text" class="form-control" name="BJMC" value="${param.BJMC }">
					              </div>
					            </div>
					          </div>
--%>
							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">课程班级</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="CLASS_ID" id="CLASS_ID" data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${classMap}" var="map">
												<option value="${map.key}" <c:if test="${param.CLASS_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							  </div>

					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">学习成绩</label>
					              <div class="col-sm-9">
					                <div class="input-group">
				                      <div class="input-group-btn">
				                        <select class="form-control input-group-select bg-white" name="SCORE_FLG">
				                          <option value="EQ" <c:if test="${empty param.SCORE_FLG || param.SCORE_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
				                          <option value="GT" <c:if test="${param.SCORE_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
				                          <option value="GTE" <c:if test="${param.SCORE_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
				                          <option value="LT" <c:if test="${param.SCORE_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
				                          <option value="LTE" <c:if test="${param.SCORE_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
				                        </select>
				                      </div>
				                      <input type="text" name="AVG_STUDY_SCORE" class="form-control" value="${param.AVG_STUDY_SCORE }">
				                    </div>
					              </div>
					            </div>
					          </div>
					          
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">学习进度</label>
					              <div class="col-sm-9">
					                <div class="input-group">
				                      <div class="input-group-btn">
				                        <select class="form-control input-group-select bg-white" name="SCHEDULE_FLG">
				                          <option value="EQ" <c:if test="${empty param.SCHEDULE_FLG || param.SCHEDULE_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
				                          <option value="GT" <c:if test="${param.SCHEDULE_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
				                          <option value="GTE" <c:if test="${param.SCHEDULE_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
				                          <option value="LT" <c:if test="${param.SCHEDULE_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
				                          <option value="LTE" <c:if test="${param.SCHEDULE_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
				                        </select>
				                      </div>
				                      <input type="text" name="AVG_SCHEDULE" class="form-control" value="${param.AVG_SCHEDULE }">
				                    </div>
					              </div>
					            </div>
					          </div>
	
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">通过率</label>
					              <div class="col-sm-9">
					                <div class="input-group">
				                      <div class="input-group-btn">
				                        <select class="form-control input-group-select bg-white" name="PASS_FLG">
				                          <option value="EQ" <c:if test="${empty param.PASS_FLG || param.PASS_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
				                          <option value="GT" <c:if test="${param.PASS_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
				                          <option value="GTE" <c:if test="${param.PASS_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
				                          <option value="LT" <c:if test="${param.PASS_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
				                          <option value="LTE" <c:if test="${param.PASS_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
				                        </select>
				                      </div>
				                      <input type="text" name="AVG_PASS_COUNT" class="form-control" value="${param.AVG_PASS_COUNT }">
				                    </div>
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
						<div class="box-header with-border">
							<h3 class="box-title pad-t5">课程班学情列表</h3>
							<shiro:hasPermission name="/studymanage/getCourseStudyList$export">
								<div class="pull-right no-margin">
									<a href="${ctx}/studymanage/courseClassConditionListExport/${totalNum}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出课程班学情统计表</a>
								</div>
							</shiro:hasPermission>
						</div>
						<div class="box-body">
							<div class="table-responsive">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
						              <tr>
										  <th>开课信息</th>
										  <th>
											  <span class="inline-block vertical-middle">班级人数</span>
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
											  <span class="inline-block vertical-middle">平均<br>学习进度</span>
											  <span class="inline-block vertical-middle" style="width:12px;line-height:1;">
												<input type="hidden" name="STUDY_PROGRESS_SORT" value="${param.STUDY_PROGRESS_SORT}">
												<c:choose>
													<c:when test="${param.STUDY_PROGRESS_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
													<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${param.STUDY_PROGRESS_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
													<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
												</c:choose>
											  </span>
										  </th>
										  <th>
											  <span class="inline-block vertical-middle">平均<br>学习次数</span>
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
											  <span class="inline-block vertical-middle">平均<br>学习时长(小时)</span>
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
										  <th>成绩</th>
										  <th>
											  <span class="inline-block vertical-middle">已通过人数</span>
											  <span class="inline-block vertical-middle" style="width:12px;line-height:1;">
												<input type="hidden" name="PASS_COUNT_SORT" value="${param.PASS_COUNT_SORT}">
												<c:choose>
													<c:when test="${param.PASS_COUNT_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
													<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${param.PASS_COUNT_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
													<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
												</c:choose>
											  </span>
										  </th>
										  <th>
											  <span class="inline-block vertical-middle">未通过人数</span>
											  <span class="inline-block vertical-middle" style="width:12px;line-height:1;">
												<input type="hidden" name="UNPASS_COUNT_SORT" value="${param.UNPASS_COUNT_SORT}">
												<c:choose>
													<c:when test="${param.UNPASS_COUNT_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
													<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${param.UNPASS_COUNT_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
													<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
												</c:choose>
											  </span>
										  </th>
										  <th>
											  <span class="inline-block vertical-middle">学习中人数</span>
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
										  <th>
											  <span class="inline-block vertical-middle">登记中人数</span>
											  <span class="inline-block vertical-middle" style="width:12px;line-height:1;">
												<input type="hidden" name="REGISTER_COUNT_SORT" value="${param.REGISTER_COUNT_SORT}">
												<c:choose>
													<c:when test="${param.REGISTER_COUNT_SORT eq 'ASC'}"><i role="button" class="glyphicon glyphicon-triangle-top text-blue"></i></c:when>
													<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-top gray9"></i></c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${param.REGISTER_COUNT_SORT eq 'DESC'}"><i role="button" class="glyphicon glyphicon-triangle-bottom text-blue"></i></c:when>
													<c:otherwise><i role="button" class="glyphicon glyphicon-triangle-bottom gray9"></i></c:otherwise>
												</c:choose>
											  </span>
										  </th>
										  <th>
											  <span class="inline-block vertical-middle">未学习人数</span>
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
						                  <th>操作</th>
						              </tr>
						            </thead>
						            <tbody>
						            	<c:forEach items="${pageInfo.content}" var="entity">
						            	<tr>
						            		<td>
												<div class="text-left">
													开课学期：${entity.GRADE_NAME }<br>
													课程名称：${entity.KCMC }<br>
													课程代码：${entity.KCH }<br>
													课程班级：${entity.BJMC}<br>
													开课状态：<c:choose>
													<c:when test="${entity.TIME_FLG eq '1'.charAt(0) }">
														<span class="text-green">开课中</span>
													</c:when>
													<c:when test="${entity.TIME_FLG eq '2'.charAt(0) }">
														<span class="text-orange">待开课</span>
													</c:when>
													<c:when test="${entity.TIME_FLG eq '3'.charAt(0) }">
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
													<c:when test="${not empty entity.REC_COUNT }">${entity.REC_COUNT }</c:when>
													<c:otherwise>--</c:otherwise>
												</c:choose>
											</td>
						            		<td>
												<c:choose>
													<c:when test="${not empty entity.AVG_SCHEDULE }">${entity.AVG_SCHEDULE }%</c:when>
													<c:otherwise>--</c:otherwise>
												</c:choose>
											</td>
						            		<td>
												<c:choose>
													<c:when test="${not empty entity.AVG_LOGIN_COUNT }">${entity.AVG_LOGIN_COUNT }</c:when>
													<c:otherwise>--</c:otherwise>
												</c:choose>
											</td>
						            		<td>
												<c:choose>
													<c:when test="${not empty entity.AVG_LOGIN_TIME }">${entity.AVG_LOGIN_TIME }</c:when>
													<c:otherwise>--</c:otherwise>
												</c:choose>
											</td>
											<td>
												<div class="text-left">
													平均学习成绩：${entity.AVG_STUDY_SCORE }<br>
													平均考试成绩：${entity.AVG_EXAM_SCORE }<br>
													平均总成绩：${entity.AVG_TOTAL_SCORE }
												</div>
											</td>
											<td>${entity.SUM_PASS_COUNT }<br><span class="gray9">(${entity.AVG_PASS_COUNT }%)</span></td>
											<td>${entity.SUM_UNPASS_COUNT}<br><span class="gray9">(${entity.AVG_UNPASS_COUNT}%)</span></td>
											<td>${entity.SUM_STUDY_IMG }<br><span class="gray9">(${entity.STUDY_IMG_PERCENT }%)</span></td>
											<td>${entity.SUM_REGISTER_COUNT }<br><span class="gray9">(${entity.AVG_REGISTER_COUNT }%)</span></td>
											<td>${entity.SUM_NEVER_STUDY }<br><span class="gray9">(${entity.NEVER_STUDY_PERCENT }%)</span></td>
						            		<td>
						            			<a href="${ctx}/studymanage/getCourseClassDetail/${entity.CLASS_ID}/${entity.COURSE_ID}/${entity.GRADE_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
						            		</td>
						            	</tr>
						            	</c:forEach>
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
</script>
</body>
</html>