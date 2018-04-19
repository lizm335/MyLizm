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
	    <input type="hidden" name="STATUS" value="${param.STATUS}" id="study_status">
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
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalStudy?${searchParams }');">学习</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalClockingIn?${searchParams }');">考勤</a></li>
							<li class="active"><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalExam?${searchParams }');">考试</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalThesis?${searchParams }');">论文</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalPractice?${searchParams }');">实践</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalGraduation?${searchParams }');">毕业</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalLink?${searchParams }');">链接</a></li>
						</ul>
						
						<div class="box-header with-border">
							<div class="filter-tabs clearfix">
								<ul class="list-unstyled">
									<li <c:if test="${empty param.STATUS}">class="actived"</c:if> id="data_btn_all">全部(<span id="data_btn_all_num">0</span>)</li>
									<li <c:if test="${ param.STATUS eq '1'}">class="actived"</c:if> id="data_btn_7">考试正常(<span id="data_btn_7_num">0</span>)</li>
									<li <c:if test="${ param.STATUS eq '2'}">class="actived"</c:if> id="data_btn_7_3">异常，预约范围内，需督促(<span id="data_btn_7_num">0</span>)</li>
									<li <c:if test="${ param.STATUS eq '3'}">class="actived"</c:if> id="data_btn_3_0">异常，预约已过期，漏报考(<span id="data_btn_3_0_num">0</span>)</li>
									<li <c:if test="${ param.STATUS eq '4'}">class="actived"</c:if> id="data_btn_4">异常，已约满，需下次再约(<span id="data_btn_4_num">0</span>)</li>
								</ul>
							<shiro:hasPermission name="/admin/home/myWorkbench$exportClockingIn">
								<div class="pull-right no-margin">
									<a href="${ctx}/excelExport/validateSmsCode/${pageInfo.totalElements}?formAction=/admin/home/downLoadExcelExportByExamAppointment" class="btn btn-default btn-sm" data-role="export">
											<i class="fa fa-fw fa-download"></i> 导出学员</a>
								</div>
							</shiro:hasPermission>
							</div>
						</div>
						
						<div class="box-body">
						  <div class="panel panel-default">
						  	<div class="panel-heading">
						    	<div class="row reset-form-horizontal clearbox" style="padding: 0px">
									<div class="col-md-4">
										<label class="control-label col-sm-3">考试计划</label>
										<div class="col-sm-9">
											<select id="examBatchCode" name="examBatchCode" class="selectpicker show-tick form-control" data-size="10" data-live-search="true">
												<option value="">请选择</option>
												<c:forEach items="${batchMap}" var="map">
													<option value="${map.key}"<c:if test="${map.key == examBatchCode || map.key == param.examBatchCode }">selected='selected'</c:if> >${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
						  </div>
						  <div class="panel-body">
							<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
										<thead>
											<tr>
												<th>计划编号</th>
												<th>计划名称</th>
												<th>学期</th>
												<th>开考科目设置时间</th>
												<th>考试预约时间</th>
												<th>排考时间</th>
												<th>考试时间</th>
												<th>学习成绩登记截止时间</th>
												<th>状态</th>
											</tr>
										</thead>
										<tbody>
													<tr>
														<td>${examBatchNew.examBatchCode}</td>
														<td>${examBatchNew.name}</td>
														<td>${gradeMap[examBatchNew.gradeId]}</td>
														<td>
															<fmt:formatDate value="${examBatchNew.planSt}" type="date" pattern="yyyy-MM-dd"/><br>
															至<br>
															<fmt:formatDate value="${examBatchNew.planEnd}" type="date" pattern="yyyy-MM-dd"/>
														</td>
														<td>
															<table class="table table-bordered" style="margin: 0px;">
															<tr>
															<td>
															<fmt:formatDate value="${examBatchNew.bookSt}" type="date" pattern="yyyy-MM-dd"/><br>
															至<br>
															<fmt:formatDate value="${examBatchNew.bookEnd}" type="date" pattern="yyyy-MM-dd"/>
															</td>
															</tr>
															<c:if test="${not empty examBatchNew.booksSt && not empty examBatchNew.booksEnd}">
															<tr>
																<td>
																<fmt:formatDate value="${examBatchNew.booksSt}" type="date" pattern="yyyy-MM-dd"/><br>
																至<br>
																<fmt:formatDate value="${examBatchNew.booksEnd}" type="date" pattern="yyyy-MM-dd"/>
																</td>
															</tr>
															</c:if>
															</table>
														</td>
														<td>
															<fmt:formatDate value="${examBatchNew.arrangeSt}" type="date" pattern="yyyy-MM-dd"/><br>
															至<br>
															<fmt:formatDate value="${examBatchNew.arrangeEnd}" type="date" pattern="yyyy-MM-dd"/>
														</td>
														<td style="padding: 0px">
															<table class="table table-bordered" style="margin: 0px;">
															<tr>
															<td style="text-align: right;">
															网考、大作业考试时间：
															</td>
															<td style="text-align: left;">
															<fmt:formatDate value="${examBatchNew.onlineSt}" type="date" pattern="yyyy-MM-dd"/>
															至
															<fmt:formatDate value="${examBatchNew.onlineEnd}" type="date" pattern="yyyy-MM-dd"/>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															省网考考试时间：
															</td>
															<td style="text-align: left;">
															<fmt:formatDate value="${examBatchNew.provinceOnlineSt}" type="date" pattern="yyyy-MM-dd"/>
															至
															<fmt:formatDate value="${examBatchNew.provinceOnlineEnd}" type="date" pattern="yyyy-MM-dd"/>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															笔考考试时间：
															</td>
															<td style="text-align: left;">
															<fmt:formatDate value="${examBatchNew.paperSt}" type="date" pattern="yyyy-MM-dd"/>
															至
															<fmt:formatDate value="${examBatchNew.paperEnd}" type="date" pattern="yyyy-MM-dd"/>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															机考考试时间：
															</td>
															<td style="text-align: left;">
															<c:choose>
																<c:when test="${not empty examBatchNew.machineSt && not empty examBatchNew.machineEnd }">
																	<fmt:formatDate value="${examBatchNew.machineSt}" type="date" pattern="yyyy-MM-dd"/>
																	至
																	<fmt:formatDate value="${examBatchNew.machineEnd}" type="date" pattern="yyyy-MM-dd"/>
																</c:when>
																<c:otherwise>
																	<span class="text-orange">待定</span>
																</c:otherwise>
															</c:choose>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															学位英语报考时间：
															</td>
															<td style="text-align: left;">
															<c:choose>
																<c:when test="${not empty examBatchNew.xwyyBookSt && not empty examBatchNew.xwyyBookEnd }">
																	<fmt:formatDate value="${examBatchNew.xwyyBookSt}" type="date" pattern="yyyy-MM-dd"/>
																	至
																	<fmt:formatDate value="${examBatchNew.xwyyBookEnd}" type="date" pattern="yyyy-MM-dd"/>
																</c:when>
																<c:otherwise>
																	<span class="text-orange">待定</span>
																</c:otherwise>
															</c:choose>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															本科统考预约时间：
															</td>
															<td style="text-align: left;">
															<c:choose>
																<c:when test="${not empty examBatchNew.bktkBookSt && not empty examBatchNew.bktkBookEnd }">
																	<fmt:formatDate value="${examBatchNew.bktkBookSt}" type="date" pattern="yyyy-MM-dd"/>
																	至
																	<fmt:formatDate value="${examBatchNew.bktkBookEnd}" type="date" pattern="yyyy-MM-dd"/>
																</c:when>
																<c:otherwise>
																	<span class="text-orange">待定</span>
																</c:otherwise>
															</c:choose>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															学位英语考试时间：
															</td>
															<td style="text-align: left;">
															<c:choose>
																<c:when test="${not empty examBatchNew.xwyyExamSt && not empty examBatchNew.xwyyExamEnd }">
																	<fmt:formatDate value="${examBatchNew.xwyyExamSt}" type="date" pattern="yyyy-MM-dd"/>
																	至
																	<fmt:formatDate value="${examBatchNew.xwyyExamEnd}" type="date" pattern="yyyy-MM-dd"/>
																</c:when>
																<c:otherwise>
																	<span class="text-orange">待定</span>
																</c:otherwise>
															</c:choose>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															本科统考考试时间：
															</td>
															<td style="text-align: left;">
															<c:choose>
																<c:when test="${not empty examBatchNew.bktkExamSt && not empty examBatchNew.bktkExamEnd }">
																	<fmt:formatDate value="${examBatchNew.bktkExamSt}" type="date" pattern="yyyy-MM-dd"/>
																	至
																	<fmt:formatDate value="${examBatchNew.bktkExamEnd}" type="date" pattern="yyyy-MM-dd"/>
																</c:when>
																<c:otherwise>
																	<span class="text-orange">待定</span>
																</c:otherwise>
															</c:choose>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															形考任务登记截止时间：
															</td>
															<td style="text-align: left;">
															<fmt:formatDate value="${examBatchNew.shapeEnd}" type="date" pattern="yyyy-MM-dd"/>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															论文截止时间：
															</td>
															<td style="text-align: left;">
															<fmt:formatDate value="${examBatchNew.thesisEnd}" type="date" pattern="yyyy-MM-dd"/>
															</td>
															</tr>
															<tr>
															<td style="text-align: right;">
															报告截止时间：
															</td>
															<td style="text-align: left;">
															<fmt:formatDate value="${examBatchNew.reportEnd}" type="date" pattern="yyyy-MM-dd"/>
															</td>
															</tr>
															</table>
														</td>
														<td>
															<fmt:formatDate value="${examBatchNew.recordEnd}" type="date" pattern="yyyy-MM-dd"/>
														</td>
														<td>
									            			<c:choose>
									            				<c:when test="${examBatchNew.planStatus == 1}">
									            					<span class="text-orange">待审核</span>
									            				</c:when>
									            				<c:when test="${examBatchNew.planStatus == 2}">
									            					<span class="text-red">审核不通过</span>
									            				</c:when>
									            				<c:when test="${examBatchNew.planStatus == 3}">
									            					<span class="text-green">已发布</span>
									            				</c:when>
									            				<c:when test="${examBatchNew.planStatus == 4}">
									            					<span class="gray9">已过期</span>
									            				</c:when>
									            			</c:choose>
														</td>
													</tr>
										</tbody>
									</table>
							  </div>
							</div>
							
							<div class="table-responsive">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
						              <tr>
						              	<th>头像</th>
						              	<th>个人信息</th>
						              	<th width="26%">报读信息</th>
						                <th>应考科目</th>
						                <th>已考科目</th>
						                <th>未考科目</th>
						                <th>本次已预约</th>
						                <th>本次未预约</th>
						                <th>操作</th>
						              </tr>
						            </thead>
						            <tbody>
						            	<c:choose>
											<c:when test="${not empty pageInfo.content}">
												<c:forEach items="${pageInfo.content}" var="entity">
													<tr>
														<td class="text-center" width="100">
															<c:choose>
																<c:when test="${not empty entity.AVATAR}">
																	<img src="${entity.AVATAR}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/images/headImg04.png'">
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
																姓名：${entity.XM } <br>
																学号：${entity.XH } <br>
																<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																手机：${entity.SJH }<br>
																</shiro:hasPermission>
																学员类型：<dic:getLabel typeCode="USER_TYPE" code="${entity.USER_TYPE }"/><br>
																学籍状态：<dic:getLabel typeCode="StudentNumberStatus" code="${entity.XJZT }"/>
															</div>
														</td>
														<td>
															<div class="text-left">
																入学年级：${entity.YEAR_NAME} <br>
																入学学期：${entity.GRADE_NAME }<br>
																专业层次：<dic:getLabel typeCode="TrainingLevel" code="${entity.PYCC }"/><br>
																专业名称：${entity.ZYMC } <c:if test="${not empty entity.RULE_CODE}">(${entity.RULE_CODE})</c:if> <br>
																学习中心：${entity.XXZX_NAME}
															</div>
														</td>
														<td>
															${entity.SHOULD_EXAM_COUNT }
														</td>
														<td>
															${entity.BEEN_EXAM_COUNT }
															<div class="gray9">
																(
																已通过：<span class="text-green">${entity.BEEN_THROUGH_COUNT }</span>
																未通过：<span class="text-orange">${entity.NOT_THROUGH_COUNT }</span>
																)
															</div>
														</td>
														<td>
															${entity.SHOULD_EXAM_COUNT-entity.BEEN_EXAM_COUNT }
														</td>
														<td>
															${entity.MAKE_COUNT }
															<c:if test="${entity.MAKE_COUNT>=8 }">
																<div class="gray9">(已约满8科)</div>
															</c:if>
														</td>
														<td>
															${entity.SHOULD_EXAM_COUNT-entity.MAKE_COUNT }
															<c:choose>
																<c:when test="${entity.status==1}">
																	<div class="text-green">(正常)</div>
																</c:when>
																<c:when test="${entity.status==2}">
																	<div class="text-orange">(异常，预约范围内，需督促)</div>
																</c:when>
																<c:when test="${entity.status==3}">
																	<div class="text-orange">(异常，预约已过期，漏报考)</div>
																</c:when>
																<c:when test="${entity.status==4}">
																	<div class="text-orange">(异常，已约满，需下次再约)</div>
																</c:when>
															</c:choose>
														</td>
														<td>
															<a href="${ctx}/admin/home/studentSynthesizeStatistical/detail/${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
														</td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise><tr><td colspan="9">暂无数据！</td></tr></c:otherwise>
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

//按钮切换
$("#data_btn_all,#data_btn_4,#data_btn_7,#data_btn_7_3,#data_btn_3_0").click(function () {
    var this_id =$(this).attr("id");
    switch (this_id){
        case "data_btn_all":
            $("#study_status").val("");
            break;
        case "data_btn_7":
            $("#study_status").val("1");
            break;
        case "data_btn_7_3":
            $("#study_status").val("2");
            break;
        case "data_btn_3_0":
            $("#study_status").val("3");
            break;
        case "data_btn_4":
            $("#study_status").val("4");
            break;
    }
    $("#submit_buttion").click();
});

function getRecordCount(type, code) {
    $.ajax({
        type: "POST",
        dataType: "json",
        url: ctx + '/admin/home/getStudentExamCount?STATUS_TEMP='+code,
        data: $('#listForm').serialize(),
        success: function (result) {
            $("#"+type).html(result.STATUS_COUNT);
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
    getRecordCount('data_btn_7_num', '1');
    getRecordCount('data_btn_7_3_num', '2');
    getRecordCount('data_btn_3_0_num', '3');
    getRecordCount('data_btn_4_num', '4');
});

//选择考试计划
$('#examBatchCode').change(function () {
 $("#submit_buttion").click();
});
</script>
</body>
</html>
