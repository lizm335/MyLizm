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
		<li class="active">考情分析</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li>
					<a href="#" data-toggle="tab" onclick="changType('1')">考试预约明细</a>
				</li>
				<li class="active">
					<a href="#" data-toggle="tab" onclick="changType('2')">考试情况明细</a>
				</li>
			</ul>
			<div class="tab-content" id="scoreContent">
				<div class="tab-pane active" id="tab_top_1">
					<div class="box box-border">
					    <div class="box-body">
					    	<input type="hidden" name="EXAM_STATE" id="exam_state" value="${param.EXAM_STATE }">
					        <div class="row pad-t15">
							  <div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">考试计划</label>
									<div class="col-sm-9">
										<select name="EXAM_BATCH_CODE" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
											<c:forEach items="${batchMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==EXAM_BATCH_CODE}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							  </div>
							  
							  <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">考试科目</label>
					              <div class="col-sm-9">
					                <input type="text" class="form-control" name="EXAM_PLAN_NAME" value="${param.EXAM_PLAN_NAME }">
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
											<option value="" selected="">全部学期</option>
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
								<input type="hidden" name="ALL_COUNT" id="all_count" value="0">
								<ul class="list-unstyled">
									<li <c:if test="${param.EXAM_STATE eq '' }">class="actived"</c:if> onclick="onchangState('')">全部(<span id="exam_state_count">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '1' }">class="actived"</c:if> onclick="onchangState('1')">登记中(<span id="exam_state_count1">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '2' }">class="actived"</c:if> onclick="onchangState('2')">未考试(<span id="exam_state_count2">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '3' }">class="actived"</c:if> onclick="onchangState('3')">作弊(<span id="exam_state_count3">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '4' }">class="actived"</c:if> onclick="onchangState('4')">已达标，已通过(<span id="exam_state_count4">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '5' }">class="actived"</c:if> onclick="onchangState('5')">已达标，未通过(<span id="exam_state_count5">0</span>)</li>
									<li <c:if test="${param.EXAM_STATE eq '6' }">class="actived"</c:if> onclick="onchangState('6')">未达标，未通过(<span id="exam_state_count6">0</span>)</li>
								</ul>
								<div class="pull-right no-margin">
									<shiro:hasPermission name="/exam/new/record/getExamAppointmentList$export">
										<a href="javascript:;" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-download"></i>导出考试情况数据</a>
									</shiro:hasPermission>
								</div>
							</div>
							<div class="table-responsive">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
						              <tr>
						              	<th>头像</th>
						              	<th>个人信息</th>
						              	<th>报读信息</th>
						              	<th>考试科目</th>
						                <th>课程信息</th>
						                <th>学习进度</th>
						                <th>学习成绩</th>
						                <th>考试成绩</th>
						                <th>总成绩</th>
						               	<th>考试要求</th>
						                <th>考试成绩状态</th>
						              </tr>
						            </thead>
						            <tbody>
						            	<c:if test="${dateFlg eq 'N' }">
						            		<tr>
						            			<td colspan="11">
						            				<div class="text-center gray9 f12">
														<i class="fa fa-fw fa-exclamation-circle f18"></i>
														<span style="font-size: 20px;">还未到考试登记时间，暂无考试详情数据！</span>
													</div>
						            			</td>
						            		</tr>
						            	</c:if>
						            	<c:if test="${dateFlg eq 'Y' }">
						            		<c:forEach items="${pageInfo.content}" var="entity">
							            	<tr>
							            		<td class="text-center">
							            			<c:if test="${not empty entity.ZP }">
							            				<img src="${entity.ZP }" class="img-circle" width="50" height="50">
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
							            		<td width="220px">
													<div class="text-left">
														考试科目：${entity.EXAM_PLAN_NAME }<br>
														试卷号：${entity.EXAM_NO }<br>
														考试形式：${entity.KSFS_NAME }
													</div>
							            		</td>
							            		<td>
							            			${entity.KCMC }<br>
													<span class="gray9">（${entity.KCH }）</span>
							            		</td>
							            		<td>
							            			<c:choose>
							            				<c:when test="${empty entity.PROGRESS }">
							            					--
							            				</c:when>
							            				<c:otherwise>
							            					${entity.PROGRESS }
							            				</c:otherwise>
							            			</c:choose>
							            		</td>
							            		<td>
							            			<c:choose>
							            				<c:when test="${empty entity.XCX_SCORE }">
							            					--
							            				</c:when>
							            				<c:otherwise>
							            					${entity.XCX_SCORE }
							            				</c:otherwise>
							            			</c:choose>
							            		</td>
							            		<td>
							            			<c:choose>
							            				<c:when test="${empty entity.ZJX_SCORE }">
							            					--
							            				</c:when>
							            				<c:otherwise>
							            					${entity.ZJX_SCORE }
							            				</c:otherwise>
							            			</c:choose>
							            		</td>
							            		<td>
							            			<c:choose>
							            				<c:when test="${empty entity.ZCJ_SCORE }">
							            					--
							            				</c:when>
							            				<c:otherwise>
							            					${entity.ZCJ_SCORE }
							            				</c:otherwise>
							            			</c:choose>
							            		</td>
							            		<td>
							            			<c:choose>
							            				<c:when test="${empty entity.EXAM_PLAN_LIMIT || entity.EXAM_PLAN_LIMIT==0}">
							            					<span class="text-green">已达标</span>
							            				</c:when>
							            				<c:when test="${entity.EXAM_SCORE >= entity.EXAM_PLAN_LIMIT }">
							            					<span class="text-green">已达标</span>
							            				</c:when>
							            				<c:otherwise>
							            					<span class="text-red">未达标</span>
							            				</c:otherwise>
							            			</c:choose>
							            			<br>
							            			<span class="gray9">
							            			<c:choose>
							            				<c:when test="${empty entity.EXAM_PLAN_LIMIT || entity.EXAM_PLAN_LIMIT==0}">
							            					（无分数限制）
							            				</c:when>
							            				<c:otherwise>
							            					（大于等于${entity.EXAM_PLAN_LIMIT }分）
							            				</c:otherwise>
							            			</c:choose>
						            				</span>
							            		</td>
							            		<td>
							            			<c:choose>
							            				<c:when test="${empty entity.ZJX_SCORE}">
							            					--
							            				</c:when>
							            				<c:when test="${entity.ZJX_SCORE >= 60}">
							            					通过
							            				</c:when>
							            				<c:when test="${entity.ZJX_SCORE < 60 && entity.ZJX_SCORE >= 0}">
							            					不通过
							            				</c:when>
							            				<c:when test="${entity.ZJX_SCORE < 0 }">
							            					<dic:getExamScore typeCode="EXAM_SCORE" code="${entity.ZJX_SCORE }" />
							            				</c:when>
							            				<c:otherwise>
							            					--
							            				</c:otherwise>
							            			</c:choose>
							            		</td>
							            	</tr>
							            	</c:forEach>
						            	</c:if>
						            	
						            </tbody>
								</table>
								<c:if test="${dateFlg eq 'Y' }">
								<tags:pagination page="${pageInfo}" paginationSize="5" />
								</c:if>
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

function getExamDetailCount() {
    $.ajax({
      type: "POST",
      dataType: "json",
      url: 'getExamDetailCount',
      data: $('#listForm').serialize(),
      success: function (result) {
    	  $("#all_count").val(result.EXAM_STATE_COUNT);
    	  $("#exam_state_count").html(result.EXAM_STATE_COUNT);
    	  $("#exam_state_count1").html(result.EXAM_STATE_COUNT1);
    	  $("#exam_state_count2").html(result.EXAM_STATE_COUNT2);
    	  $("#exam_state_count3").html(result.EXAM_STATE_COUNT3);
    	  $("#exam_state_count4").html(result.EXAM_STATE_COUNT4);
    	  $("#exam_state_count5").html(result.EXAM_STATE_COUNT5);
    	  $("#exam_state_count6").html(result.EXAM_STATE_COUNT6);
      },
      error: function(data) {}
    });
}

$(function() {
	$("#exam_type").selectpicker();
	$("#exam_state").selectpicker();
	$("#specialty_id").selectpicker();
	$("#pycc").selectpicker();
	
	
	//导出
	$('[data-role="export"]').click(function(event) {
	    event.preventDefault();
	    var data = $("#listForm").serialize()+"&TYPE=EXPORTEXAMDETAIL";
	    var self=this;
	    $.mydialog({
	        id:'export',
	        width:600,
	        height:415,
	        zIndex:11000,
	        content: 'url:${ctx}/exam/new/record/getExpRecord?'+data
	    });
	});
	if ("${dateFlg}"=="Y") {
		getExamDetailCount();
	}
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

function changType(type) {
	if (type=="1") {
		window.location.href = "getExamAppointmentList";
	} else {
		window.location.href = "getExamDetailList";
	}
}

function onchangState(type) {
	$("#exam_state").val(type);
	$("#listForm").submit();
}
</script>
</body>
</html>