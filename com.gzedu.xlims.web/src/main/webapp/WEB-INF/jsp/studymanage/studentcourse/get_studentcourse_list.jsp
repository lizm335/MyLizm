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
					<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getCourseClassList');" >课程班学情</a></li>
				</shiro:hasPermission>
				<%--<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getTeachClassList');" >教务班学情</a></li>--%><%--暂时隐藏吧。。。--%>
				<shiro:hasPermission name="/studymanage/getCourseStudyList$conditionTab3">
					<li class="active"><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getStudentCourseList');" >学员学情</a></li>
				</shiro:hasPermission>
				<%--<li><a href="javascript:window.parent.loadPage('${ctx}/studymanage/getStudentMajorList');" >专业学情</a></li>--%><%--暂时隐藏吧。。。--%>
			</ul>
			
			<div class="tab-content">
				<div class="tab-pane active" id="tab_top_2">
					<div class="box box-border">
					    <div class="box-body">
					        <div class="row pad-t15">

							  <div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学习中心</label>
									<div class="col-sm-9">
										<select name="XXZX_ID" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
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
					              <label class="control-label col-sm-3 text-nowrap">姓名</label>
					              <div class="col-sm-9">
					                <input type="text" class="form-control" name="XM" value="${param.XM }">
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

							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业层次</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="PYCC" id="pycc" data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param.PYCC}">selected='selected'</c:if>>
														${map.value}
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
							  </div>

							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">入学学期</label>
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
									<label class="control-label col-sm-3 text-nowrap">报读专业</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="SPECIALTY_ID" id="specialty_id" data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${specialtyMap}" var="map">
												<option value="${map.key}" <c:if test="${param.SPECIALTY_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							  </div>
								<%--
								  <div class="col-sm-4">
									<div class="form-group">
									  <label class="control-label col-sm-3 text-nowrap">完成课程比例</label>
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
										  <input type="text" name="PASS_BL" class="form-control" value="${param.PASS_BL }">
										</div>
									  </div>
									</div>
								  </div>
								  --%>
								<%--
	                            <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
	                              <div class="col-sm-4">
	                                <div class="form-group">
	                                  <label class="control-label col-sm-3 text-nowrap">已获学分比例</label>
	                                  <div class="col-sm-9">
	                                    <div class="input-group">
	                                      <div class="input-group-btn">
	                                        <select class="form-control input-group-select bg-white" name="PASS_FLG">
	                                          <option value="EQ" <c:if test="${empty param.XF_FLG || param.XF_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
	                                          <option value="GT" <c:if test="${param.XF_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
	                                          <option value="GTE" <c:if test="${param.XF_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
	                                          <option value="LT" <c:if test="${param.XF_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
	                                          <option value="LTE" <c:if test="${param.XF_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
	                                        </select>
	                                      </div>
	                                      <input type="text" name="XF_BL" class="form-control" value="${param.XF_BL }">
	                                    </div>
	                                  </div>
	                                </div>
	                              </div>
	                            </shiro:lacksPermission>
	                            --%>
							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">教务班级</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="CLASS_ID" id="CLASS_ID" data-size="5" data-live-search="true">
											<option value="" selected="">全部班级</option>
											<c:forEach items="${classMap}" var="map">
												<option value="${map.key}" <c:if test="${param.CLASS_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							  </div>

							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">当前在线状态</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="IS_ONLINE" id="IS_ONLINE" data-size="5" data-live-search="true">
											<option value="">请选择</option>
											<option value="Y" <c:if test="${param.IS_ONLINE eq 'Y' }">selected="selected"</c:if>>在线</option>
											<option value="N" <c:if test="${param.IS_ONLINE eq 'N' }">selected="selected"</c:if>>离线</option>
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
				          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
				          <button type="button" class="btn min-width-90px btn-default">重置</button>
				        </div><!-- /.box-footer-->
					</div>
					<div class="box box-border margin-bottom-none">
						<div class="box-header with-border">
							<h3 class="box-title pad-t5">学员学情列表</h3>
							<shiro:hasPermission name="/studymanage/getCourseStudyList$export">
								<div class="pull-right no-margin">
									<a href="${ctx}/studymanage/studCourseConditionDetailExport/${pageInfo.getTotalElements()}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出学员学情明细表</a>
								</div>
								<div class="pull-right no-margin">
									<a href="${ctx}/studymanage/studentCourseListExport/${totalNum}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出学员学情统计表</a>
								</div>
							</shiro:hasPermission>
						</div>
						<div class="box-body">
							<div class="table-responsive">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
						              <tr>
						              	<th>头像</th>
						              	<th>个人信息</th>
						              	<th <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">width="250px"</shiro:lacksPermission>>报读信息</th>
									    <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
											<th>已获学分/<br/>总学分</th>
									    </shiro:lacksPermission>
										<th>课程<br/>总数</th>
						                <th>已通过<br/>课程数</th>
						                <th>未通过<br/>课程数</th>
						                <th>学习中<br/>课程数</th>
						                <th>未学习<br/>课程数</th>
						                <th>学习<br/>总次数</th>
						                <th>学习<br/>总时长</th>
						                <th>当前<br/>在线状态</th>
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
												<shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
													<td>
														<div>
															<span class="f18">${entity.SUM_GET_CREDITS }/</span>${entity.ZXF }<br>

															<c:if test="${entity.XJZT eq '8' }">
																<span class="text-green">已毕业</span>
															</c:if>
															<c:if test="${entity.XJZT ne '8' }">
																<c:choose>
																	<c:when test="${entity.SUM_GET_CREDITS>0 && entity.ZDBYXF>0 && entity.SUM_GET_CREDITS >=entity.ZDBYXF}">
																		<span class="text-green">已满足毕业要求</span>
																	</c:when>
																	<c:otherwise>
																		<span class="text-orange">未满足毕业要求</span>
																	</c:otherwise>
																</c:choose>
															</c:if>
														</div>
														<%--
															<div class="gray9">
																（已获得${entity.XF_BL }%）
															</div>
														--%>
													</td>
												</shiro:lacksPermission>
												<td>${entity.REC_COUNT }</td>
												<td>${entity.PASS_REC_COUNT }</td>
												<td>${entity.UNPASS_REC_COUNT }</td>
												<td>${entity.LEARNING_REC_COUNT }</td>
												<td>${entity.UNLEARN_REC_COUNT }</td>
												<td>${entity.LOGIN_TIMES }</td>
												<td>${entity.LOGIN_TIME_COUNT }</td>
												<td>
													<c:choose>
														<c:when test="${empty entity.LOGIN_TIMES }">
															<span class="gray9">离线<br>(从未学习)</span>
														</c:when>
														<c:when test="${entity.IS_ONLINE eq 'Y'}">
															<span class="text-green">在线<br>(${entity.DEVICE}在线)</span>
														</c:when>
														<c:when test="${entity.IS_ONLINE eq 'N'}">
															<span class="gray9">离线<br>
																<c:choose>
																	<c:when test="${empty entity.LOGIN_TIMES or entity.LOGIN_TIMES eq '0'}">(从未学习)</c:when>
																	<c:when test="${not empty entity.LOGIN_TIMES and entity.LOGIN_TIMES ne '0' and not empty entity.LAST_LOGIN_TIME}">(${entity.LAST_LOGIN_TIME}天未学习)</c:when>
																	<c:otherwise>(从未学习)</c:otherwise>
																</c:choose>
															</span>
														</c:when>
													</c:choose>
												</td>
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