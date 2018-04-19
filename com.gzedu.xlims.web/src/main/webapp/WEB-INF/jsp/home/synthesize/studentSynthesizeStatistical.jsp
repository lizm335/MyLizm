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
							<li class="active"><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatistical?${searchParams }');">全部</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalFee?${searchParams }');">缴费</a></li>
							<li><a href="javascript:window.parent.loadPage('${ctx}/admin/home/studentSynthesizeStatisticalStudy?${searchParams }');">学习</a></li>
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
									<li <c:if test="${ param.STUDY_STATUS eq '0'}">class="actived"</c:if> id="data_btn_1">正常(<span id="data_btn_1_num">0</span>)</li>
									<li <c:if test="${ param.STUDY_STATUS eq '1'}">class="actived"</c:if> id="data_btn_7">异常(<span id="data_btn_7_num">0</span>)</li>
									<li <c:if test="${ param.STUDY_STATUS eq '2'}">class="actived"</c:if> id="data_btn_7_3">在线(<span id="data_btn_7_3_num">0</span>，app <span id="data_btn_7_3_num">0</span>，pc <span id="data_btn_7_3_num">0</span>)</li>
									<li <c:if test="${ param.STUDY_STATUS eq '3'}">class="actived"</c:if> id="data_btn_3_0">离线(<span id="data_btn_3_0_num">0</span>，3天 <span id="data_btn_7_3_num">0</span>，7天 <span id="data_btn_7_3_num">0</span>)</li>
								</ul>
							<shiro:hasPermission name="/admin/home/myWorkbench$exportClockingIn">
								<div class="pull-right no-margin">
									<a href="${ctx}/excelExport/validateSmsCode/${pageInfo.totalElements}?formAction=/admin/home/downLoadStudentLoginListExportXls" class="btn btn-default btn-sm" data-role="export">
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
						              	<th width="26%">报读信息</th>
						                <th>教学教务状态</th>
						                <th>链接状态</th>
						                <th>操作</th>
						              </tr>
						            </thead>
						            <tbody>
													<tr>
														<td class="text-center" width="100">
															<img src="${ctx }/static/images/headImg04.png" class="img-circle" width="50" height="50">
															<a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
																<i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
																<span class="gray9">交流</span>
															</a>
												 		</td>
														<td>
															<div class="text-left">
																姓名：张小可 <br>
																学号：441502145 <br>
																<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																手机：18955477819<br>
																</shiro:hasPermission>
																资料：审核通过 <br>
																学员类型：正式生<br>
																学籍状态：在籍
															</div>
														</td>
														<td>
															<div class="text-left">
																专业层次：2016秋 <br>
																当前学期：第二学期 <br>
																专业层次：高起专 <br>
																专业名称：计算机网络技术（网络管理）<br>
																学习中心：麓湖学习中心
															</div>
														</td>
														<td>
															<div class="text-left">
															缴费状态：<span class="text-orange">异常</span><br/>
															  教材状态：<span class="text-green">正常</span><br/>
															  考试状态：<span class="text-green">正常</span><br/>
															  论文状态：<span class="text-green">正常</span><br/>
															  社会实践：<span class="text-green">正常</span><br/>
															  毕业状态：<span class="text-green">正常</span><br/>
															  学情状态：<span class="text-green">正常</span><br/>
															  考勤状态：<span class="text-green">正常</span><span class="text-gray">（在线、APP登录）</span>
														  	</div>
														</td>
														<td>
															<div class="text-left">
															安装APP：<span class="text-green">已安装</span><span class="text-gray">（当前在线）</span><br/>
															  使用PC：<span class="text-green">已使用</span><span class="text-gray">（3天内登录过）</span><br/>
															  关注微信公众号：<span class="text-green">已关注</span><br/>
														  	</div>
														</td>
														<td>
															<a href="${ctx}/admin/home/studentSynthesizeStatistical/detail/${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
														</td>
													</tr>
													<tr>
														<td class="text-center" width="100">
															<img src="${ctx }/static/images/headImg04.png" class="img-circle" width="50" height="50">
															<a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
																<i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
																<span class="gray9">交流</span>
															</a>
												 		</td>
														<td>
															<div class="text-left">
																姓名：张小可 <br>
																学号：441502145 <br>
																<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																手机：18955477819<br>
																</shiro:hasPermission>
																资料：审核通过 <br>
																学员类型：正式生<br>
																学籍状态：在籍
															</div>
														</td>
														<td>
															<div class="text-left">
																专业层次：2016秋 <br>
																当前学期：第二学期 <br>
																专业层次：高起专 <br>
																专业名称：计算机网络技术（网络管理）<br>
																学习中心：麓湖学习中心
															</div>
														</td>
														<td>
															<div class="text-left">
															缴费状态：<span class="text-orange">异常</span><br/>
															  教材状态：<span class="text-green">正常</span><br/>
															  考试状态：<span class="text-green">正常</span><br/>
															  论文状态：<span class="text-green">正常</span><br/>
															  社会实践：<span class="text-green">正常</span><br/>
															  毕业状态：<span class="text-green">正常</span><br/>
															  学情状态：<span class="text-green">正常</span><br/>
															  考勤状态：<span class="text-orange">异常</span><span class="text-gray">（离线、30天未登录）</span>
														  	</div>
														</td>
														<td>
															<div class="text-left">
															安装APP：<span class="text-orange">未安装</span><br/>
															  使用PC：<span class="text-orange">未使用</span><br/>
															  关注微信公众号：<span class="text-orange">未关注</span><br/>
														  	</div>
														</td>
														<td>
															<a href="${ctx}/admin/home/studentSynthesizeStatistical/detail/${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
														</td>
													</tr>
													<tr>
														<td class="text-center" width="100">
															<img src="${ctx }/static/images/headImg04.png" class="img-circle" width="50" height="50">
															<a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
																<i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
																<span class="gray9">交流</span>
															</a>
												 		</td>
														<td>
															<div class="text-left">
																姓名：张小可 <br>
																学号：441502145 <br>
																<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																手机：18955477819<br>
																</shiro:hasPermission>
																资料：审核通过 <br>
																学员类型：正式生<br>
																学籍状态：在籍
															</div>
														</td>
														<td>
															<div class="text-left">
																专业层次：2016秋 <br>
																当前学期：第二学期 <br>
																专业层次：高起专 <br>
																专业名称：计算机网络技术（网络管理）<br>
																学习中心：麓湖学习中心
															</div>
														</td>
														<td>
															<div class="text-left">
															缴费状态：<span class="text-green">正常</span><br/>
															  教材状态：<span class="text-green">正常</span><br/>
															  考试状态：<span class="text-green">正常</span><br/>
															  论文状态：--<br/>
															  社会实践：--<br/>
															  毕业状态：--<br/>
															  学情状态：<span class="text-green">正常</span><br/>
															  考勤状态：<span class="text-green">正常</span><span class="text-gray">（在线、APP登录）</span>
														  	</div>
														</td>
														<td>
															<div class="text-left">
															安装APP：<span class="text-orange">未安装</span><br/>
															  使用PC：<span class="text-orange">未使用</span><br/>
															  关注微信公众号：<span class="text-orange">未关注</span><br/>
														  	</div>
														</td>
														<td>
															<a href="${ctx}/admin/home/studentSynthesizeStatistical/detail/${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
														</td>
													</tr>
						            </tbody>
								</table>
								<%-- <tags:pagination page="${pageInfo}" paginationSize="5" /> --%>
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
$("#data_btn_all,#data_btn_1,#data_btn_7,#data_btn_7_3,#data_btn_3_0").click(function () {
    var this_id =$(this).attr("id");
    switch (this_id){
        case "data_btn_all":
            $("#study_status").val("");
            break;
        case "data_btn_1":
            $("#study_status").val("0");
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
    }
    $("#submit_buttion").click();
});


function getRecordCount(type, code) {
    $.ajax({
        type: "POST",
        dataType: "json",
        url: ctx + '/admin/home/getStudentLoginCount?STUDY_STATUS_TEMP='+code,
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
    getRecordCount('data_btn_1_num', '0');
    getRecordCount('data_btn_7_num', '1');
    getRecordCount('data_btn_7_3_num', '2');
    getRecordCount('data_btn_3_0_num', '3');
});
</script>
</body>
</html>
