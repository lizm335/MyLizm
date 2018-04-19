<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>考试成绩</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:">考试管理</a></li>
		<li class="active">考试成绩</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li class="active"><a href="#" data-toggle="tab" onclick="changType('1')">成绩查询</a></li>
				<shiro:hasPermission name="/exam/new/record/getExamRecordList$examRegister">
					<li><a href="#" data-toggle="tab" onclick="changType('2')">登记成绩</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content" id="scoreContent">
				<div class="tab-pane active" id="tab_top_1">
					<div class="box box-border">
					    <div class="box-body">
					    	<input type="hidden" name="EXAM_STATE" id="exam_state" value="${param.EXAM_STATE }">
					        <div class="row pad-t15">
							  <div class="col-sm-4">
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
					              <label class="control-label col-sm-3 text-nowrap">考试形式</label>
					              <div class="col-sm-9">
					                <select class="selectpicker show-tick form-control" name="EXAM_TYPE" id="exam_type" data-size="5" data-live-search="true">
					                  	<option value="" selected="selected">请选择</option>
										<c:forEach items="${examTypeMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.EXAM_TYPE}">selected='selected'</c:if>>
												${map.value}
											</option>
										</c:forEach>
					                </select>
					              </div>
					            </div>
					          </div>
					          
					          <div class="col-sm-4 col-xs-6">
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

							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">入学学期</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="NJ" id="nj" data-size="5" data-live-search="true">
											<option value="all" selected="">全部学期</option>
											<c:forEach items="${gradeMap}" var="map">
												<c:if test="${empty param.NJ}">
													<option value="${map.key}" <c:if test="${currentGradeId eq map.key }">selected="selected"</c:if>>${map.value}</option>
												</c:if>
												<c:if test="${not empty param.NJ}">
													<option value="${map.key}" <c:if test="${param.NJ eq map.key }">selected="selected"</c:if>>${map.value}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</div>
							  </div>
					          
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">入学年级</label>
					              <div class="col-sm-9">
					                <select class="selectpicker show-tick form-control" name="YEARID" id="yearid" data-size="5" data-live-search="true">
					                	<option value="" selected="selected">请选择</option>
					                	<c:forEach items="${yearMap}" var="map">
					                  		<option value="${map.key}" <c:if test="${param.YEARID eq map.key }">selected="selected"</c:if>>${map.value}</option>
					                  	</c:forEach>
					                </select>
					              </div>
					            </div>
					          </div>
	
							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">课程</label>
									<div class="col-sm-9">
										<select class="selectpicker show-tick form-control" name="COURSE_ID" id="COURSE_ID" data-size="10" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${courseMap}" var="map">
												<option value="${map.key}" <c:if test="${param.COURSE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
											</c:forEach>
										</select>
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
						<div class="box-body">
							<div class="filter-tabs clearfix">
								<input type="hidden" name="ALL_COUNT" id="all_count" value="${pageInfo.totalElements}">
								<ul class="list-unstyled">
									<li <c:if test="${param.EXAM_STATE eq '' }">class="actived"</c:if> onclick="onchangState('')">全部(<span id="exam_state_count">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '1' }">class="actived"</c:if> onclick="onchangState('1')">已通过(<span id="exam_state_count1">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '0' }">class="actived"</c:if> onclick="onchangState('0')">未通过(<span id="exam_state_count0">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '2' }">class="actived"</c:if> onclick="onchangState('2')">学习中(<span id="exam_state_count2">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '3' }">class="actived"</c:if> onclick="onchangState('3')">登记中(<span id="exam_state_count3">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '4' }">class="actived"</c:if> onclick="onchangState('4')">未学习(<span id="exam_state_count4">0</span>)</li>
								</ul>
								<shiro:hasPermission name="/exam/new/record/getExamRecordList$export">
									<div class="pull-right no-margin">
										<a href="javascript:;" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-download"></i>导出成绩</a>
									</div>
								</shiro:hasPermission>
							</div>
							<div class="table-responsive">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
									<tr>
										<th>头像</th>
										<th>个人信息</th>
										<th>报读信息</th>
										<th>课程</th>
										<th>考试方式</th>
										<th>考试单位</th>
										<shiro:lacksPermission name="/studymanage/getScoreList$schoolModel">
											<th>学分</th>
											<th>已获学分</th>
										</shiro:lacksPermission>
										<th>形考比例</th>
										<th>学习进度</th>
										<th>学习成绩</th>
										<th>考试成绩</th>
										<th>总成绩</th>
										<th>成绩状态</th>
										<th>操作</th>
									</tr>
									</thead>
									<tbody>
									<c:choose>
										<c:when test="${not empty pageInfo}">
											<c:forEach items="${pageInfo.content}" var="entity">
												<tr>
													<td class="text-center">
														<c:if test="${not empty entity.ZP }">
															<img src="${entity.ZP }" class="img-circle" width="50" height="50" onerror="this.src='${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png'">
														</c:if>
														<c:if test="${empty entity.ZP }">
															<img src="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" width="50" height="50">
														</c:if>

													</td>
													<td>
														<div class="text-left">
															姓名：${entity.XM } <br>
															学号：${entity.XH } <br>
															<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																手机：${entity.SJH }
															</shiro:hasPermission>
														</div>
													</td>
													<td width="220px">
														<div class="text-left">
															专业层次：${entity.PYCC_NAME }<br>
															入学年级：${entity.YEAR_NAME }<br>
															入学学期：${entity.GRADE_NAME }<br>
															报读专业：${entity.ZYMC }
														</div>
													</td>
													<td width="120px">
															${entity.KCMC }
													</td>
													<td>
														<c:choose>
															<c:when test="${entity.EXAM_STATE eq '0' || entity.EXAM_STATE eq '1' || entity.EXAM_STATE eq '3'}">
																<c:choose>
																	<c:when test="${not empty entity.EXAM_PLAN_KSFS_NAME }">${entity.EXAM_PLAN_KSFS_NAME }</c:when>
																	<c:when test="${not empty entity.KSFS_NAME }">${entity.KSFS_NAME }</c:when>
																	<c:otherwise>--</c:otherwise>
																</c:choose>
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when test="${not empty entity.KSFS_NAME }">${entity.KSFS_NAME }</c:when>
																	<c:otherwise>--</c:otherwise>
																</c:choose>
															</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${entity.EXAM_STATE eq '0' || entity.EXAM_STATE eq '1' || entity.EXAM_STATE eq '3'}">
																<c:choose>
																	<c:when test="${entity.KSDW eq '1'}">省</c:when>
																	<c:when test="${entity.KSDW eq '2'}">中央</c:when>
																	<c:when test="${entity.KSDW eq '3'}">分校</c:when>
																	<c:otherwise>
																		--
																	</c:otherwise>
																</c:choose>
															</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<shiro:lacksPermission name="/studymanage/getScoreList$schoolModel">
														<td>${entity.XF }</td>
														<td>
															<c:choose>
																<c:when test="${entity.EXAM_STATE eq '1'}">
																	${entity.GET_CREDITS }
																</c:when>
																<c:when test="${entity.EXAM_STATE eq '0'}">
																	0
																</c:when>
																<c:otherwise>--</c:otherwise>
															</c:choose>
														</td>
													</shiro:lacksPermission>
													<td>
														<c:choose>
															<c:when test="${entity.EXAM_STATE eq '0' || entity.EXAM_STATE eq '1' || entity.EXAM_STATE eq '3'}">
																${entity.KCXXBZ }%
															</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${empty entity.PROGRESS}">--<br><c:if test="${entity.STUDY_FLG eq '0' }"><span class="gray9">(未学习)</span></c:if></c:when>
															<c:when test="${not empty entity.PROGRESS}">
																${entity.PROGRESS}%<br>
																<c:if test="${not empty entity.STATE}">
																	<c:choose>
																		<c:when test="${entity.STATE eq '0'}"><span class="gray9">(${entity.STATE_NAME})</span></c:when>
																		<c:when test="${entity.STATE eq '1'}"><span class="text-red">(${entity.STATE_NAME})</span></c:when>
																		<c:when test="${entity.STATE eq '2'}"><span class="text-orange">(${entity.STATE_NAME})</span></c:when>
																		<c:when test="${entity.STATE eq '3'}"><span class="text-info">(${entity.STATE_NAME})</span></c:when>
																		<c:when test="${entity.STATE eq '4'}"><span class="text-green">(${entity.STATE_NAME})</span></c:when>
																		<c:otherwise></c:otherwise>
																	</c:choose>
																</c:if>
															</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${not empty entity.EXAM_SCORE}">${entity.EXAM_SCORE }</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${not empty entity.EXAM_SCORE1}">
																<dic:getExamScore typeCode="EXAM_SCORE" code="${entity.EXAM_SCORE1 }" />
															</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${entity.EXAM_STATE eq '0' || entity.EXAM_STATE eq '1'}">
																<dic:getExamScore typeCode="EXAM_SCORE" code="${entity.EXAM_SCORE2 }" />
															</c:when>
															<c:otherwise>--</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${entity.STUDY_FLG eq '0' }">
																<span class="gray9">未学习</span>
															</c:when>
															<c:when test="${entity.EXAM_STATE eq '0' }">
																<span class="text-red">未通过</span>
															</c:when>
															<c:when test="${entity.EXAM_STATE eq '1' }">
																<span class="text-green">已通过</span>
															</c:when>
															<c:when test="${entity.EXAM_STATE eq '2' }">
																<span class="text-orange">学习中</span>
															</c:when>
															<c:when test="${entity.EXAM_STATE eq '3' }">
																<span class="text-red">登记中</span>
															</c:when>
															<c:otherwise>
																<span class="text-red">未通过</span>
															</c:otherwise>
														</c:choose>
													</td>
													<td>
														<a href="${ctx}/exam/new/record/getExamRecordDetail?STUDENT_ID=${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
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

function getRecordCount(type, code) {
    $.ajax({
      type: "POST",
      dataType: "json",
      url: 'getRecordCount?EXAM_STATE_TEMP='+code,
      data: $('#listForm').serialize(),
      success: function (result) {
    	  $("#"+type).html(result.EXAM_STATE_COUNT);
    	  if (type=="exam_state_count") {
    		  $("#all_count").val(result.EXAM_STATE_COUNT);
    	  }
      },
      error: function(data) {}
    });
}

$(function() {
	$("#exam_type").selectpicker();
	$("#exam_state").selectpicker();
	$("#specialty_id").selectpicker();
	$("#pycc").selectpicker();
	getRecordCount('exam_state_count', '');
	getRecordCount('exam_state_count0', '0');
	getRecordCount('exam_state_count1', '1');
	getRecordCount('exam_state_count2', '2');
	getRecordCount('exam_state_count3', '3');
	getRecordCount('exam_state_count4', '4');
});

//Initialize Select2 Elements
$(".select2").select2();
// filter tabs
$(".filter-tabs li").click(function(event) {
	if($(this).hasClass('actived')){
		$(this).removeClass('actived');
	}
	else{
		$(this).addClass('actived');
	}
});

//高级搜索
/*$("[id^='more-search']").on('shown.bs.collapse', function(event) {
	event.preventDefault();
	var index=$("[id^='more-search']").index(this);
	$('[data-target="#more-search-'+index+'"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
}).on('hidden.bs.collapse', function(event) {
	event.preventDefault();
	var index=$("[id^='more-search']").index(this);
	$('[data-target="#more-search-'+index+'"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
});*/

// 导出
$('[data-role="export"]').click(function(event) {
    event.preventDefault();
    var data = $("#listForm").serialize()+"&TYPE=RECORD";
    var self=this;
    $.mydialog({
        id:'export',
        width:600,
        height:415,
        zIndex:11000,
        content: 'url:${ctx}/exam/new/record/getExpRecord?'+data
    });
});

function changType(type) {
	if (type=="1") {
		window.location.href = "getExamRecordList";
	} else {
		window.location.href = "getExamRegisterList";
	}
}

function onchangState(type) {
	$("#exam_state").val(type);
	$("#listForm").submit();
}
</script>
</body>
</html>