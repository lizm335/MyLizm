<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统</title>
	<!-- Tell the browser to be responsive to screen width -->
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<!-- iCheck 1.0.1 -->
	<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
	<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
	<script type="text/javascript">
	$(function() {

		var old_code =$('#code').val();//原来的
		// 加载省市区联动查询
		$('#province').select_district($('#city'), $("#district"), $("#district").val());

		//参考： http://bv.doc.javake.cn/examples/
		$('#inputForm').bootstrapValidator({
				fields: {
					code: {
						validators: {
							notEmpty: "required",
							callback: {
								message: '院校代码已经存在！',
								callback: function(value, validator) {
									var validator=true;
									$.ajax({
										type : "post",
										url : "${ctx}/organization/headquarters/checkCode",
										dataType:'json',
										data : {code:$('#code').val(),oldCode:old_code},
										async : false,
										success : function(data){
											if(!data.successful){
												validator=false;
											}
										}
									});
									return validator;
								}
							}
						}
					},
					orgName: {
						validators: {
							notEmpty: "required",
						}
					}
					/* ,
					'gjtSchoolInfo.linkTel': {
						validators: {
							notEmpty: "required",
							regexp:{
								regexp : /(^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$)|(^1(3|4|5|7|8)\d{9}$)/,
								message:"电话号码格式不正确！"
							}
						},
					},
					'gjtSchoolInfo.linkMan': {
						validators: {
							notEmpty: "required",
						}
					},
					'gjtSchoolInfo.linkMail': {
						validators: {
							notEmpty: "required",
							emailAddress: "required"
						}
					} */
				}
			});
	})
	</script>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" id="go_for_back" onclick="window.history.back()">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">总部管理</a></li>
		<c:if test="${action=='create'}">
		<li class="active">新增总部</li>
		</c:if>
		<c:if test="${action=='update'}">
		<li class="active">编辑总部</li>
		</c:if>
	</ol>
</section>
<section class="content" data-id="0">
	<div class="box margin-bottom-none">
		<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/organization/headquarters/${action}" method="post">
        <input id="action" type="hidden" name="action" value="${action}">
        <input type="hidden" name="id" value="${entity.id}">
		<div class="box-body">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>总部代码</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="code" value="${entity.code}"placeholder="请输入总部代码" id="code">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>总部名称</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="orgName" value="${entity.orgName}" placeholder="请输入总部名称">
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><!-- <small class="text-red">*</small> -->联系电话</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="gjtSchoolInfo.linkTel" value="${entity.gjtSchoolInfo.linkTel}" placeholder="请输入联系电话">
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><!-- <small class="text-red">*</small> -->联系人</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="gjtSchoolInfo.linkMan" value="${entity.gjtSchoolInfo.linkMan}" placeholder="请输入联系人">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><!-- <small class="text-red">*</small> -->电子邮箱</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="gjtSchoolInfo.linkMail"value="${entity.gjtSchoolInfo.linkMail}" placeholder="请输入电子邮箱">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>总部地址</label>
					<div class="col-sm-8">
						<div class="row">
							<div class="col-sm-4">
								<select id="province" class="selectpicker show-tick form-control"   data-size="8" data-live-search="true">
									<option value="" selected="selected"></option>
								</select>
							</div>
							<div class="col-sm-4">
								<select id="city"  class="selectpicker show-tick form-control"   data-size="8" data-live-search="true">
									<option value="" selected="selected"></option>
								</select>
							</div>
							<div class="col-sm-4">
								<select id="district" name="gjtSchoolInfo.xxqhm" class="selectpicker show-tick form-control" data-size="8"  data-live-search="true">
									<option value="${entity.gjtSchoolInfo.xxqhm}" selected="selected"></option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"></label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="gjtSchoolInfo.xxdz" value="${entity.gjtSchoolInfo.xxdz}" placeholder="请输入总部详细地址">
					</div>
				</div>

				<%-- <div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>办学模式:</label>
					<div class="col-sm-8">
						<input type="radio"  name="schoolModel" value="1" class="flat-red" <c:if test="${empty entity.schoolModel || entity.schoolModel==1}">checked="checked"</c:if>> 学历办学
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="schoolModel" value="2" class="flat-red" <c:if test="${entity.schoolModel==2}">checked="checked"</c:if>> 中职院校
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="schoolModel" value="3" class="flat-red" <c:if test="${entity.schoolModel==3}">checked="checked"</c:if>> 院校模式（有考试）
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="schoolModel" value="4" class="flat-red" <c:if test="${entity.schoolModel==4}">checked="checked"</c:if>> 院校模式（无考试）
					</div>
				</div> --%>

				<div class="form-group">
					<label class="col-sm-2 control-label">简介</label>
					<div class="col-sm-8">
						<textarea id="editor4" name="gjtSchoolInfo.memo" rows="10" cols="80">
						${entity.gjtSchoolInfo.memo}
						</textarea>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-8 col-sm-offset-2">
						<button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
						<button type="button" id="btn-back"  onclick="window.history.back()" class="btn btn-default min-width-90px btn-cancel-edit">取消</button>
					</div>
				</div>
			</div>
		</div>
	 </form>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
	$(function () {
		//Flat red color scheme for iCheck
		$('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
			checkboxClass: 'icheckbox_flat-green',
			radioClass: 'iradio_flat-green'
		});
	});

	CKEDITOR.replace('gjtSchoolInfo.memo');
</script>
</body>

</html>
