<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<!-- iCheck 1.0.1 -->
	<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
	<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
	<script type="text/javascript">
	$(function() {
		
		if($('#action').val() == 'view'){
			$(':input').attr("disabled","disabled");
			$('#btn-back').removeAttr("disabled");
			$('#btn-back2').removeAttr("disabled");
			$('#btn-submit').remove();
		}
		
		$(".select2").select2({language: "zh-CN"});

		var old_code =$('#code').val();//原来的
		
		// 加载省市区联动查询
		$('#province').select_district($('#city'), $("#district"), $("#district").val());

		//参考： http://bv.doc.javake.cn/examples/
		$('#inputForm').bootstrapValidator({
			excluded: [':disabled'],//验证下拉必需加
				fields: {
					'parentGjtOrg.id': {
						validators: {
							notEmpty: {message:'上级单位不能为空'}
						}
					},
					orgName: {
						validators: {
							notEmpty: {message:'学习中心名称不能为空'}
						}
					},
					/*'gjtStudyCenter.linkTel': {
						validators: {
							notEmpty: {message:'联系电话不能为空'},
							regexp:{
								regexp : /(^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$)|(^1(3|4|5|7|8)\d{9}$)/,
								message:"电话号码格式不正确！"
							}
						}
					},
					 'gjtStudyCenter.linkman': {
						validators: {
							notEmpty: {message:'联系人不能为空'}
						}
					},
					'gjtStudyCenter.officeAddr': {
						validators: {
							notEmpty: {message:'详细地址不能为空'}
						}
					},*/
					'gjtStudyCenter.district': {
						validators: {
							notEmpty: {message:'区域不能为空'}
						}
					}, 
					serviceAreas:{
						validators: {
							notEmpty: {message:'服务项至少选择一项！'}
						}
					},
					code: {
						validators: {
							notEmpty: {message:'学习中心代码不能为空'},
							callback: {
								message: '学习中心代码已经存在',
								callback: function(value, validator) {
									var validator=true;
									$.ajax({
										type : "post",
										url : "${ctx}/organization/gjtStudyCenter/checkCode",
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
					}
				}
			});
	})
	</script>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button onclick="window.href='list.do'" id="btn-back2"  class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>	
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">机构管理</a></li>
		<li><a href="#">学习中心</a></li>
		<c:if test="${action=='create'}">
		<li><a href="#">新增学习中心</a></li>
		</c:if>
		<c:if test="${action=='update'}">
		<li><a href="#">编辑学习中心</a></li>
		</c:if>
	</ol>
</section>
<section class="content">
	<div class="box margin-bottom-none">
	<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/organization/gjtStudyCenter/${action}" method="post">
        <input id="action" type="hidden" name="action" value="${action}">
        <input type="hidden" name="id" value="${entity.id}">
		<div class="box-body">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">上级单位</label>
					<div class="col-sm-8">
						<select name="parentGjtOrg.id" class="select2 form-control"  >
							<option value="" >请选择</option>
							<c:forEach items="${listAll }" var="item">
								<option value="${item.ID }" <c:if test="${entity.parentGjtOrg.id eq item.ID }">selected='selected'</c:if>>${item.NAME }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>学习中心代码</label>
					<div class="col-sm-8">
						<c:if test="${action=='create'}">
						<input class="form-control" type="text" id="code" name="code" value="${entity.code}" placeholder="所属分部或者分校代码+001~999">
						</c:if>
						<c:if test="${action=='update'}">
							<input class="form-control" type="text" id="code"  value="${entity.code}" placeholder="所属分部或者分校代码+001~999"
								disabled="disabled">
								<input  type="hidden"  value="${entity.code}" name="code">
						</c:if>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>学习中心名称</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="orgName" value="${entity.orgName}" placeholder="请输入学习中心名称">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">学习中心合同编号</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="gjtStudyCenter.compactNo" value="${entity.gjtStudyCenter.compactNo}" placeholder="请填写合同编号！">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>学习中心服务项</label>
					<div class="col-sm-8 form-control-static" >
						<c:forEach items="${studyServiceInfoMap }" var="map">
							<input type="checkbox" value="${map.key }" name="serviceAreas" <c:if test="${serviceAreas[map.key] }">checked='checked'</c:if>>
							${map.value } &nbsp;&nbsp;
						</c:forEach>
					</div>
				</div>
				
				<div class="form-group">				 
				<label class="col-sm-2 control-label"><small class="text-red">*</small>所在区域</label>
				<div class="col-sm-8">
					<div class="row">
						<div class="col-sm-4">
							<select id="province" class="select2 form-control" data-size="8" data-live-search="true">
								<option value="" selected="selected"></option>
							</select>
						</div>
						<div class="col-sm-4">
							<select id="city"  class="select2 form-control" data-size="8" data-live-search="true">
								<option value="" selected="selected"></option>
							</select>
						</div>
						<div class="col-sm-4">
							<select id="district" name="gjtStudyCenter.district" class="select2 form-control" data-size="8"  data-live-search="true">
								<option value="${entity.gjtStudyCenter.district}" selected="selected"></option>
							</select>
						</div>
					</div>
				</div>
			 </div>
			<div class="form-group"> 
			<label class="col-sm-2 control-label">详细地址</label>
			<div class="col-sm-8">
				<input type="text" name="gjtStudyCenter.officeAddr" class="form-control" value="${entity.gjtStudyCenter.officeAddr}" placeholder="请输入学习中心详细地址"/>
			</div>
		   </div>
									
			<div class="form-group">
				<label class="col-sm-2 control-label"><!-- <small class="text-red">*</small> -->联系电话</label>
				<div class="col-sm-8">
					<input class="form-control" type="text" name="gjtStudyCenter.linkTel" value="${entity.gjtStudyCenter.linkTel}" placeholder="请输入联系电话">
				</div>
			</div>	
			<div class="form-group">
				<label class="col-sm-2 control-label"><!-- <small class="text-red">*</small> -->联系人</label>
				<div class="col-sm-8">
					<input class="form-control" type="text" name="gjtStudyCenter.linkman" value="${entity.gjtStudyCenter.linkman}" placeholder="请输入联系人" maxlength="8">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">简介</label>
				<div class="col-sm-8">
					<textarea id="editor4" name="gjtStudyCenter.memo" rows="10" cols="80">
					${entity.gjtStudyCenter.memo}
					</textarea>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-8 col-sm-offset-2">
					<button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
					<button id="btn-back" onclick="history.back()" class="btn btn-default min-width-90px btn-cancel-edit">取消</button>
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

	CKEDITOR.replace('gjtStudyCenter.memo');
</script>
</body>

</html>
