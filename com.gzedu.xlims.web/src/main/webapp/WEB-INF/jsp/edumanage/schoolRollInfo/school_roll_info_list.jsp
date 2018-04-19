<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-学籍资料</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
	$(function() {

	})
	function choiceXJ(flag){
		//$('#listForm .btn-reset').trigger('click');
		$("#signupAuditState").val('');
		$("#perfectStatus").val('');
		$("#signupAuditState").val(flag);
		$("#listForm").submit();
	}
	function choicePs(flag){
		//$('#listForm .btn-reset').trigger('click');
		$("#signupAuditState").val('');
		$("#perfectStatus").val('');
		$("#perfectStatus").val(flag);
		$("#listForm").submit();
	}
	</script>
</head>
<body class="inner-page-body">
	<section class="content-header clearfix">
		<ol class="breadcrumb oh">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">学籍管理</a></li>
			<li class="active">学籍资料</li>
		</ol>
	</section>

	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html" method="post">
		<input id="signupAuditState" type="hidden" name="search_EQ_signupAuditState" value="${param.search_EQ_signupAuditState}">
		<input id="perfectStatus" type="hidden" name="search_EQ_perfectStatus" value="${param.search_EQ_perfectStatus}">
		<div class="box">
			<div class="box-body">
				<div class="row pad-t15">
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">学号</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_EQ_xh" value="${param.search_EQ_xh }">
							</div>
						</div>
					</div>
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">姓名</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm }">
							</div>
						</div>
					</div>
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">学习中心</label>
							<div class="col-sm-9">
								<select name="search_EQ_studyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${studyCenterMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_studyId}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>

					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">专业名称</label>
							<div class="col-sm-9">
								<select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${specialtyMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param.search_EQ_specialtyId}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">学期</label>
							<div class="col-sm-9">
								<select  name="search_EQ_viewStudentInfo.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==((not empty param['search_EQ_viewStudentInfo.gradeId']) ? param['search_EQ_viewStudentInfo.gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">层次</label>
							<div class="col-sm-9">
								<select  name="search_EQ_pycc"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param['search_EQ_pycc']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</div>

				<div id="more-search" class="row collapse <c:if test="${not empty param.search_EQ_sfzh || not empty param.search_EQ_userType || not empty param.search_LIKE_scCo }">in</c:if>">
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">身份证号</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_EQ_sfzh" value="${param.search_EQ_sfzh }">
							</div>
						</div>
					</div>
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">学员类型</label>
							<div class="col-sm-9">
								<c:if test="${sessionScope.current_user.loginAccount=='admin'}">
								<dic:selectBox name="search_EQ_userType" typeCode="USER_TYPE" code="${param.search_EQ_userType }" otherAttrs='class="selectpicker show-tick form-control" data-size="5" data-live-search="true"' />
								</c:if>
								<c:if test="${sessionScope.current_user.loginAccount!='admin'}">
								<dic:selectBox name="search_EQ_userType" typeCode="USER_TYPE" code="${param.search_EQ_userType }" excludes="21,31" otherAttrs='class="selectpicker show-tick form-control" data-size="10" data-live-search="true"' />
								</c:if>
							</div>
						</div>
					</div>
					<div class="col-sm-4 col-xs-6">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">工作单位</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_scCo" value="${param.search_LIKE_scCo }">
							</div>
						</div>
					</div>
				</div>

			</div><!-- /.box-body -->
			<div class="box-footer text-right">
				<button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
				<button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
				<div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">高级搜索<i class="fa fa-fw fa-angle-up <c:if test="${not empty param.search_EQ_sfzh || not empty param.search_EQ_userType || not empty param.search_LIKE_scCo }">fa-angle-down</c:if>"></i> </div>
			</div><!-- /.box-footer-->
		</div>

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">资料列表</h3>
					<div class="pull-right no-margin">
						<shiro:hasPermission name="/edumanage/schoolRollInfo/list$exportAuditState">
						<a href="javascript:exportAuditState();" class="btn btn-default btn-sm"><i class="fa fa-fw fa-download"></i> 导出学籍审核环节数据</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="box-body">
					<div class="filter-tabs clearfix">
						<ul class="list-unstyled">
							<li <c:if test="${empty param.search_EQ_signupAuditState && empty param.search_EQ_perfectStatus}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${not empty countAuditStateMap[''] ? countAuditStateMap[''] : 0})</li>
							<li <c:if test="${param.search_EQ_perfectStatus == '0'}">class="actived"</c:if> value="0" onclick="choicePs('0')">未完善资料(${not empty countPerfectStatusMap['0'] ? countPerfectStatusMap['0'] : 0})</li>
							<li <c:if test="${param.search_EQ_perfectStatus == '1'}">class="actived"</c:if> value="1" onclick="choicePs('1')">已完善资料(${not empty countPerfectStatusMap['1'] ? countPerfectStatusMap['1'] : 0})</li>
							<li <c:if test="${param.search_EQ_signupAuditState == '3'}">class="actived"</c:if> value="3" onclick="choiceXJ('3')">待审核(${not empty countAuditStateMap['3'] ? countAuditStateMap['3'] : 0})</li>
							<li <c:if test="${param.search_EQ_signupAuditState == '4'}">class="actived"</c:if> value="4" onclick="choiceXJ('4')">未开始审核(${not empty countAuditStateMap['4'] ? countAuditStateMap['4'] : 0})</li>
							<li <c:if test="${param.search_EQ_signupAuditState == '2'}">class="actived"</c:if> value="2" onclick="choiceXJ('2')">审核中(${not empty countAuditStateMap['2'] ? countAuditStateMap['2'] : 0})</li>
							<li <c:if test="${param.search_EQ_signupAuditState == '0'}">class="actived"</c:if> value="0" onclick="choiceXJ('0')">审核不通过(${not empty countAuditStateMap['0'] ? countAuditStateMap['0'] : 0})</li>
							<li <c:if test="${param.search_EQ_signupAuditState == '1'}">class="actived"</c:if> value="1" onclick="choiceXJ('1')">审核通过(${not empty countAuditStateMap['1'] ? countAuditStateMap['1'] : 0})</li>
							<li <c:if test="${param.search_EQ_signupAuditState == '5'}">class="actived"</c:if> value="5" onclick="choiceXJ('5')">已退费，无需审核(${not empty countAuditStateMap['5'] ? countAuditStateMap['5'] : 0})</li>
						</ul>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid table-font">
							<thead>
								<tr>
									<!-- <th><input type="checkbox" class="select-all" id="selectAll"></th> -->
									<th width="70" class="text-center">照片</th>
									<th class="text-center">个人信息</th>
									<th class="text-center">报读信息</th>
									<th class="text-center">学习中心</th>
									<th class="text-center">学员类型</th>
									<th class="text-center">资料状态</th>
									<th class="text-center">资料审批</th>
									<th class="text-center">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageInfo.content }" var="student">
									<tr>
										<%-- <td><input type="checkbox" value="${student.studentId }" name="ids" class="checkbox"></td> --%>
										<td class="text-center">
											<img src="${not empty student.avatar ? student.avatar : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>
											<c:if test="${empty student.avatar}">
												<p class="gray9">未上传</p>
											</c:if>
											<%-- <a target="${student.studentId}" href="${ctx}/edumanage/classstudent/openEEchatToStudent?studentId=${student.studentId}">EEchat</a> --%>
										</td>
										<td>
											姓名：${student.xm}<br/>
											性别：<dic:getLabel typeCode="Sex" code="${student.xbm }" /><br/>
											学号：${student.xh}<br/>
											<shiro:hasPermission name="/personal/index$privacyJurisdiction">
											手机号：${student.sjh}<br/>
											身份证：${student.sfzh}
											</shiro:hasPermission>
										</td>
										<td>
											层次：<dic:getLabel typeCode="TrainingLevel" code="${student.pycc }" /><br/>
											学期：${student.gradeName}<br/>
											专业：${student.specialtyName}
										</td>
										<td class="text-center">
												${student.xxzxName} <br>

										</td>
										<td class="text-center">
											<dic:getLabel typeCode="USER_TYPE" code="${student.userType}" />
										</td>
										<td class="text-center">
											${student.perfectStatus==1?'<span class="text-green">已完善</span>':'<span class="text-red">未完善</span>' }
										</td>
										<td class="text-center">
											<c:choose>
												<c:when test="${student.xjzt=='5'}"><span class="text-orange">已退费，无需审核</span></c:when>
												<c:when test="${student.signupAuditState=='1'}"><span class="text-green">审核通过</span></c:when>
												<c:when test="${student.signupAuditState=='0'}"><span class="text-red">审核不通过<c:if test="${not empty student.flowAuditOperatorRole}"><br/>(${student.flowAuditOperatorRole-1}/3)</span></c:if></span></c:when>
												<c:otherwise>
													<c:if test="${empty student.flowAuditOperatorRole}"><span class="text-orange">未审核</span></c:if>
													<c:if test="${not empty student.flowAuditOperatorRole}">
														<c:if test="${student.flowAuditOperatorRole==4}">
															<span class="text-orange">待审核</span>
														</c:if>
														<c:if test="${student.flowAuditOperatorRole!=4}">
															<span class="text-orange">审核中<br/>(${student.flowAuditOperatorRole-1}/3)</span>
														</c:if>
													</c:if>
												</c:otherwise>
											</c:choose>
										</td>
										<td class="text-center">
											<c:if test="${student.flowAuditOperatorRole==4&&student.flowAuditState==0&&student.xjzt!='5'}">
												<shiro:hasPermission name="/edumanage/schoolRollInfo/list$approval">
												<a href="view/${student.studentId}?action=audit" class="operion-item" data-toggle="tooltip" title="审核学籍资料"><i class="fa fa-fw fa-shxxjl"></i></a>
												</shiro:hasPermission>
											</c:if>
											<shiro:hasPermission name="/edumanage/schoolRollInfo/list$view">
											<a href="view/${student.studentId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
											</shiro:hasPermission>
											<%--<a href="print.html?id=${student.studentId}" class="operion-item" data-toggle="tooltip" title="" data-original-title="打印报名登记表"><i class="fa fa-fw fa-print"></i></a>
											<a href="download.html?id=${student.studentId}" class="operion-item" data-toggle="tooltip" title="" data-original-title="下载报读资料"><i class="fa fa-fw fa-download"></i></a>--%>
											<shiro:hasPermission name="/edumanage/schoolRollInfo/list$sync">
												<c:if test="${student.yunyingSync!='Y'}">
													<a href="javascript:void(0);" val="${student.studentId}" class="operion-item syncYunYingCenter" data-toggle="tooltip" title="账号同步至运营平台"><i class="fa fa-fw fa-share-alt"></i></a>
												</c:if>
												<c:if test="${student.eesync!='Y'}">
													<a href="javascript:void(0);" val="${student.studentId}" class="operion-item syncEENo" data-toggle="tooltip" title="同步EE号"><i class="fa fa-fw fa-share-alt"></i>EE</a>
												</c:if>
											</shiro:hasPermission>
											<shiro:hasPermission name="/personal/index$admin">
												<a href="update/${student.studentId}" class="operion-item" data-toggle="tooltip" title="修改" target="_blank"><i class="fa fa-fw fa-edit"></i></a>
											</shiro:hasPermission>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>

				<!-- Loading (remove the following to stop the loading)-->
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"> 同步中...</i>
				</div>
				<!-- end loading -->
			</div>
		</form>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		//高级搜索
		$("#more-search").on('shown.bs.collapse', function(event) {
			event.preventDefault();
			$('[data-target="#more-search"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
		}).on('hidden.bs.collapse', function(event) {
			event.preventDefault();
			$('[data-target="#more-search"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
		});

		function exportAuditState() {
			var form = $("<form>");
			form.attr("style","display:none");
			form.attr("target","");
			form.attr("method","post");
			form.attr("action","exportAuditState");
			$("body").append(form);
			var fields = $('#listForm').serializeArray();
			$.each( fields, function(i, field){
				form.append('<input type="hidden" name="'+field.name+'" value="' + field.value + '" />');
			});
			form.submit();
		}

		$('.syncYunYingCenter').click(function () {
			$('.overlay').show();
			var $this = $(this);
			var studentId = $this.attr('val');
			$.post(ctx+'/edumanage/schoolRollInfo/syncYunYingCenter', {studentId: studentId}, function (data) {
				$('.overlay').hide();
				if(data.successful) {
					showSueccess(data);
					$this.remove();
				} else {
					showFail(data);
					alert(data.message);
				}
			}, 'json');
		})

		$('.syncEENo').click(function () {
			$('.overlay').show();
			var $this = $(this);
			var studentId = $this.attr('val');
			$.post(ctx+'/edumanage/schoolRollInfo/syncEENo', {studentId: studentId}, function (data) {
				$('.overlay').hide();
				if(data.successful) {
					showSueccess(data);
					$this.remove();
				} else {
					showFail(data);
					alert(data.message);
				}
			}, 'json');
		})
	</script>
</body>
</html>
