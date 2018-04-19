<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-毕业管理</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
		$(function() {
			var action = $('#action').val();
			if(action == 'view' || action == 'audit'){
				$(':input').attr("readonly","readonly");
				$(':radio').attr("disabled","disabled");
				$('select').attr("disabled","disabled");
				$('#btn-submit').remove();

				$('#auditRecord').show();
				$('#auditRecord :input').removeAttr("readonly");
			} else {
				//参考： http://bv.doc.javake.cn/examples/
				$('#inputForm').bootstrapValidator({
					excluded: [':disabled'],//验证下拉必需加
					fields: {
						"gjtGrade.gradeId": {
							validators: {
								notEmpty: {
									message: "请选择学期"
								}
							}
						},
						rollPlanNo: {
							validators: {
								notEmpty: {
									message: "请填写计划编号"
								}
							}
						},
						rollPlanTitle: {
							validators: {
								notEmpty: {
									message: "请填写计划名称"
								}
							}
						},
						officialBeginDtStr: {
							validators: {
								notEmpty: {
									message: "请填写正式学籍注册时间"
								}
							}
						},						
						officialEndDtStr: {
							validators: {
								callback: {
									message: '请填写正确的正式学籍注册时间',
									callback: function(value, validator) {
										var flag = true;
										var officialBeginDtStr = validator.getFieldElements('officialBeginDtStr').val();
										if(officialBeginDtStr != '' && value == '') {
											flag = false;
											return flag;
										}
										if(officialBeginDtStr == '' && value != '') {
											flag = false;
											return flag;
										}
										value = value.replace(/-/g,"/");//替换字符，变成标准格式
										officialBeginDtStr = officialBeginDtStr.replace(/-/g,"/");//替换字符，变成标准格式
										var d2 = new Date(Date.parse(value));
										var d1 = new Date(Date.parse(officialBeginDtStr));
										if(d1>d2){
											flag = false;
											return flag;
										}
										return flag;
									}
								}
							}
						},
						officialEndDt2Str: {
							validators: {
								callback: {
									message: '请填写正确的正式学籍注册时间',
									callback: function(value, validator) {
										var flag = true;
										var officialBeginDt2Str = validator.getFieldElements('officialBeginDt2Str').val();
										if(officialBeginDt2Str != '' && value == '') {
											flag = false;
											return flag;
										}
										if(officialBeginDt2Str == '' && value != '') {
											flag = false;
											return flag;
										}
										value = value.replace(/-/g,"/");//替换字符，变成标准格式
										officialBeginDt2Str = officialBeginDt2Str.replace(/-/g,"/");//替换字符，变成标准格式
										var d2 = new Date(Date.parse(value));
										var d1 = new Date(Date.parse(officialBeginDt2Str));
										if(d1>d2){
											flag = false;
											return flag;
										}
										var officialEndDtStr = validator.getFieldElements('officialEndDtStr').val();
										officialEndDtStr = officialEndDtStr.replace(/-/g,"/");//替换字符，变成标准格式
										var d3 = new Date(Date.parse(officialEndDtStr));
										if(d3>d1) {
											flag = false;
											return flag;
										}
										return flag;
									}
								}
							}
						},
						/*followBeginDtStr: {
							validators: {
								notEmpty: {
									message: "请填写跟读学籍注册时间"
								}
							}
						},*/
						rollTransBeginDt: {
							validators: {
								notEmpty: {
									message: "请填写学籍异动办理时间"
								}
							}
						},
						rollTransEndDt: {
							validators: {
								callback: {
									message: '请填写正确的学籍异动办理时间',
									callback: function(value, validator) {
										var flag = true;
										var rollTransBeginDt = validator.getFieldElements('rollTransBeginDt').val();
										if(rollTransBeginDt != '' && value == '') {
											flag = false;
											return flag;
										}
										if(rollTransBeginDt == '' && value != '') {
											flag = false;
											return flag;
										}
										value = value.replace(/-/g,"/");//替换字符，变成标准格式
										rollTransBeginDt = rollTransBeginDt.replace(/-/g,"/");//替换字符，变成标准格式
										var d2 = new Date(Date.parse(value));
										var d1 = new Date(Date.parse(rollTransBeginDt));
										if(d1>d2){
											flag = false;
											return flag;
										}
										return flag;
									}
								}
							}
						},
						rollTransEndDt2: {
							validators: {
								callback: {
									message: '请填写正确的学籍异动办理时间',
									callback: function(value, validator) {
										var flag = true;
										var rollTransBeginDt2 = validator.getFieldElements('rollTransBeginDt2').val();
										if(rollTransBeginDt2 != '' && value == '') {
											flag = false;
											return flag;
										}
										if(rollTransBeginDt2 == '' && value != '') {
											flag = false;
											return flag;
										}
										value = value.replace(/-/g,"/");//替换字符，变成标准格式
										rollTransBeginDt2 = rollTransBeginDt2.replace(/-/g,"/");//替换字符，变成标准格式
										var d2 = new Date(Date.parse(value));
										var d1 = new Date(Date.parse(rollTransBeginDt2));
										if(d1>d2){
											flag = false;
											return flag;
										}
										return flag;
									}
								}
							}
						},
						followEndDtStr: {
							validators: {
								callback: {
									message: '请填写正确的跟读学籍注册时间',
									callback: function(value, validator) {
										var flag = true;
										var followBeginDtStr = validator.getFieldElements('followBeginDtStr').val();
										if(followBeginDtStr != '' && value == '') {
											flag = false;
											return flag;
										}
										if(followBeginDtStr == '' && value != '') {
											flag = false;
											return flag;
										}
										value = value.replace(/-/g,"/");//替换字符，变成标准格式
										followBeginDtStr = followBeginDtStr.replace(/-/g,"/");//替换字符，变成标准格式
										var d2 = new Date(Date.parse(value));
										var d1 = new Date(Date.parse(followBeginDtStr));
										if(d1>d2){
											flag = false;
											return flag;
										}
										var officialEndDtStr = validator.getFieldElements('officialEndDtStr').val();
										officialEndDtStr = officialEndDtStr.replace(/-/g,"/");//替换字符，变成标准格式
										var d3 = new Date(Date.parse(officialEndDtStr));
										if(d3>d1) {
											flag = false;
											return flag;
										}
										var officialEndDt2Str = validator.getFieldElements('officialEndDt2Str').val();
										if(officialEndDt2Str != '') {
											officialEndDt2Str = officialEndDt2Str.replace(/-/g,"/");//替换字符，变成标准格式
											var d4 = new Date(Date.parse(officialEndDt2Str));
											if(d4>d1) {
												flag = false;
												return flag;
											}
										}
										return flag;
									}
								}
							}
						}
					}
				});

				//时间控件改变文本框的值时重新触发校验
				$('.single-datetime2').on('change', function (e) {
					var fieldName = $(this).attr('name');
					$('#inputForm')
							.data('bootstrapValidator')
							.updateStatus(fieldName, 'NOT_VALIDATED', null)
							.validateField(fieldName);
				});
			}
		})
	</script>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off" onclick="history.back();">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学籍管理</a></li>
		<li><a href="${ctx}/edumanage/rollPlan/list">学籍计划</a></li>
		<li class="active">发布学籍计划</li>
	</ol>
</section>

<section class="content">

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

	<div class="row">
		<div class="col-md-12">
			<div class="box box-primary">
				<div class="box-header with-border">
					<h3 class="box-title">发布学籍计划</h3>
				</div>
				<form id="inputForm" class="form-horizontal" role="form" action="${ctx}/edumanage/rollPlan/${action }" method="post">
					<input id="action" type="hidden" name="action" value="${action }"/>
					<input type="hidden" name="id" value="${info.id }"/>
					<input type="hidden" name="auditState" value="" />

					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-2 control-label text-nowrap"><small class="text-red">*</small>所属学期:</label>
							<div class="col-sm-8">
								<select name="gjtGrade.gradeId"
										class="selectpicker show-tick form-control" data-size="5" data-live-search="true" <c:if test="${action!='create'}">disabled="disabled"</c:if>>
									<option value="">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<c:if test="${action=='create'}">
											<option value="${map.key}" 	<c:if test="${map.key==defaultGradeId}">selected='selected'</c:if>>${map.value}</option>
										</c:if>
										<c:if test="${action!='create'}">
											<option value="${map.key}" 	<c:if test="${map.key==info.gjtGrade.gradeId}">selected='selected'</c:if>>${map.value}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label text-nowrap"><small class="text-red">*</small>计划编号:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" name="rollPlanNo" value="${info.rollPlanNo}">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label"><small class="text-red">*</small>计划名称:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" name="rollPlanTitle" value="${info.rollPlanTitle}">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label text-nowrap"><small class="text-red">*</small>正式学籍注册时间（1）:</label>
							<div class="col-sm-8">
								<div class="input-group input-custom-daterange">
									<input type="text" name="officialBeginDtStr" class="form-control single-datetime2" value='<fmt:formatDate value="${info.officialBeginDt}" pattern="yyyy-MM-dd HH:mm" />'>
									<span class="input-group-addon">－</span>
									<input type="text" name="officialEndDtStr" class="form-control single-datetime2" value='<fmt:formatDate value="${info.officialEndDt}" pattern="yyyy-MM-dd HH:mm" />'>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label text-nowrap">正式学籍注册时间（2）:</label>
							<div class="col-sm-8">
								<div class="input-group input-custom-daterange">
									<input type="text" name="officialBeginDt2Str" class="form-control single-datetime2" value='<fmt:formatDate value="${info.officialBeginDt2}" pattern="yyyy-MM-dd HH:mm" />'>
									<span class="input-group-addon">－</span>
									<input type="text" name="officialEndDt2Str" class="form-control single-datetime2" value='<fmt:formatDate value="${info.officialEndDt2}" pattern="yyyy-MM-dd HH:mm" />'>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label text-nowrap">跟读学籍注册时间:</label>
							<div class="col-sm-8">
								<div class="input-group input-custom-daterange">
									<input type="text" name="followBeginDtStr" class="form-control single-datetime2" value='<fmt:formatDate value="${info.followBeginDt}" pattern="yyyy-MM-dd HH:mm" />'>
									<span class="input-group-addon">－</span>
									<input type="text" name="followEndDtStr" class="form-control single-datetime2" value='<fmt:formatDate value="${info.followEndDt}" pattern="yyyy-MM-dd HH:mm" />'>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label text-nowrap"><small class="text-red">*</small>学籍异动办理时间（1）:</label>
							<div class="col-sm-8">
								<div class="input-group input-custom-daterange">
									<input type="text" name="rollTransBeginDt" class="form-control single-datetime2" value='<fmt:formatDate value="${info.rollTransBeginDt}" pattern="yyyy-MM-dd HH:mm" />'>
									<span class="input-group-addon">－</span>
									<input type="text" name="rollTransEndDt" class="form-control single-datetime2" value='<fmt:formatDate value="${info.rollTransEndDt}" pattern="yyyy-MM-dd HH:mm" />'>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label text-nowrap">学籍异动办理时间（2）:</label>
							<div class="col-sm-8">
								<div class="input-group input-custom-daterange">
									<input type="text" name="rollTransBeginDt2" class="form-control single-datetime2" value='<fmt:formatDate value="${info.rollTransBeginDt2}" pattern="yyyy-MM-dd HH:mm" />'>
									<span class="input-group-addon">－</span>
									<input type="text" name="rollTransEndDt2" class="form-control single-datetime2" value='<fmt:formatDate value="${info.rollTransEndDt2}" pattern="yyyy-MM-dd HH:mm" />'>
								</div>
							</div>
						</div>
						
						<div id="auditRecord" class="form-group" style="display: none;">
							<label class="col-sm-2 control-label text-nowrap">审核记录:</label>
							<div class="col-sm-8">
								<div class="approval-list clearfix full-width" style="padding-left:60px;">
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6">发布计划</b>
											<span class="gray9 text-no-bold f12">${info.publishOperator} <fmt:formatDate value="${info.publishDt}" type="both"/> 发布</span>
											<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
										</dt>
									</dl>
									<dl class="approval-item" style="border-left: 4px solid rgb(255,255,255);">
										<dt class="clearfix">
											<b class="a-tit gray6">分校管理员审核</b>
											<c:if test="${info.auditState==0}">
												<span class="fa fa-fw fa-dot-circle-o text-orange"></span>
												<label class="state-lb text-orange">待审核</label>
											</c:if>
											<c:if test="${info.auditState==1}">
												<span class="gray9 text-no-bold f12"><fmt:formatDate value="${info.auditDt}" type="both"/></span>
												<span class="fa fa-fw fa-check-circle text-green"></span>
												<label class="state-lb text-green">审核通过</label>
											</c:if>
											<c:if test="${info.auditState==2}">
												<span class="gray9 text-no-bold f12"><fmt:formatDate value="${info.auditDt}" type="both"/></span>
												<span class="fa fa-fw fa-times-circle text-red"></span>
												<label class="state-lb text-red">审核不通过</label>
											</c:if>
										</dt>
										<c:if test="${info.auditState==0}">
											<shiro:hasPermission name="/edumanage/rollPlan/list$approval">
											<dd>
												<div class="col-xs-12 no-padding position-relative">
													<textarea name="auditContent" class="form-control" rows="3" placeholder="请输入资料审核备注，例如该学员的资料确认无误" datatype="*1-200" nullmsg="请输入内容！" errormsg="字数不能超过200"></textarea>
												</div>
												<div>
													<button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10 btn-audit" val="2" data-form-id="5">审核不通过</button>
													<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10 btn-audit" val="1" data-form-id="5">审核通过</button>
												</div>
											</dd>
											</shiro:hasPermission>
										</c:if>
										<c:if test="${info.auditState!=0}">
											<dd>
												<div class="txt">
													<p>${info.auditContent}</p>
													<div class="gray9 text-right">审核人：${info.auditOperator}</div>
													<i class="arrow-top"></i>
												</div>
											</dd>
										</c:if>
									</dl>
								</div>
							</div>
						</div>

					</div>

					<div class="box-footer">
						<div class="row">
						<div class="col-sm-offset-2 col-sm-10">
							<button id="btn-submit" type="submit" class="btn btn-primary margin_r10 min-width-90px">确认发布</button>
							<button type="reset" class="btn btn-default min-width-90px" onclick="history.back()">返回</button>
						</div>
						</div>
					</div>
				</form>

			</div>
		</div>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
	var singleDateTimePickerOpt2 = {
		singleDatePicker: true,
		showDropdowns : true,
		timePicker : true, //是否显示小时和分钟
		timePickerIncrement : 1, //时间的增量，单位为分钟
		timePicker12Hour : false, //是否使用12小时制来显示时间
		format : 'YYYY-MM-DD HH:mm', //控件中from和to 显示的日期格式
		locale : {
			applyLabel : '确定',
			cancelLabel : '取消',
			daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
			monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
			firstDay : 1
		}
	};
	$('.single-datetime2').daterangepicker(singleDateTimePickerOpt2);
</script>
<script type="text/javascript" src="${ctx}/static/js/edumanage/rollPlan/roll_plan_form.js"></script>
</body>
</html>