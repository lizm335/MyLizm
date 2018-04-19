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
		var oldScCode = $("#scCode").val();
		if($('#action').val() == 'view'){
			$(':input').attr("disabled","disabled");
			$('#btn-back').removeAttr("disabled");
			$('#btn-back2').removeAttr("disabled");
			$('#btn-submit').remove();
		}

		// 加载省市区联动查询
		$('#province').select_district($('#city'), $("#district"), $("#district").val());

		//参考： http://bv.doc.javake.cn/examples/
		$('#inputForm').bootstrapValidator({
			excluded: [':disabled'],//验证下拉必需加
				fields: {
					scName: {
						validators: {
							notEmpty: {message:'学习中心名称不能为空'}
						}
					},
					linkTel: {
						validators: {
							notEmpty: {message:'联系电话不能为空'},
							regexp:{
								regexp : /(^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$)|(^1(3|4|5|7|8)\d{9}$)/,
								message:"电话号码格式不正确！"
							}
						}
					},
					linkMan: {
						validators: {
							notEmpty: {message:'联系人不能为空'}
						}
					},
					officeAddr: {
						validators: {
							notEmpty: {message:'详细地址不能为空'}
						}
					},
					district: {
						validators: {
							notEmpty: {message:'区域不能为空'}
						}
					},
					orgType:{
						validators: {
							notEmpty: {message:'类型不能为空'}
						}
					},
					serviceAreas:{
						validators: {
							notEmpty: {message:'服务项至少选择一项！'}
						}
					},
					suoShuXxzxId:{
						validators: {
							callback: {
								message: '招生点必需填写所属学习中心！',
								callback: function(value, validator) {
									var validator=true;
									var $orgtype=$('#orgType').val();
									if($orgtype=='6'){
										var $suoShuXxzxId=$('#suoShuXxzxId').val();
										if($suoShuXxzxId==''){
											validator=false;
										}
									}
								  return validator;
								}
							 }
						 }
					},
					scCode: {
						validators: {
							notEmpty: {message:'学习中心代码不能为空'},
							callback: {
								message: '学习中心代码已经存在',
								callback: function(value, validator) {
									var validator=true;
									if(oldScCode != $("#scCode").val() && $("#scCode").val()!==" "){
										$.ajax({
											type : "post",
											url : "${ctx}/organization/studyCenter/checkScCode.html",
											dataType:'json',
											data : {scCode:$('#scCode').val()},
											async : false,
											success : function(data){
												if(data.successful){
													validator=false;
												}
											}
										});
									}
								  return validator;
								}
							 }
						 }
					},
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
		<c:if test="${action=='view'}">
		<li><a href="#">学习中心详情</a></li>
		</c:if>
		<c:if test="${action=='update'}">
		<li><a href="#">编辑学习中心</a></li>
		</c:if>
	</ol>
</section>
<section class="content">
	<div class="box margin-bottom-none">
	<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/organization/studyCenter/${action}" method="post">
        <input id="action" type="hidden" name="action" value="${action}">
        <input type="hidden" name="id" value="${entity.id}">
		<div class="box-body">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">所属院校</label>
					<div class="col-sm-8">
						<p class="form-control-static">${gjtSchoolInfo.orgName} ${flag }</p>
						<input type="hidden" value="${gjtSchoolInfo.id}" name="suoShuXxId">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>学习中心类型:</label>
					<div class="col-sm-8">
						<c:if test="${action eq 'update'}"> <input type="hidden" name="orgType" value="${entity.gjtOrg.orgType}"></c:if>
						<select class="selectpicker show-tick form-control" name="orgType"  id="orgType"<c:if test="${action eq 'update'}"> disabled="disabled"</c:if>>
							<option value="">请选择</option>
							<option value="3" <c:if test="${entity.gjtOrg.orgType eq '3'}">selected="selected"</c:if>>学习中心</option>
							<option value="6" <c:if test="${entity.gjtOrg.orgType eq '6'}">selected="selected"</c:if>>招生点</option>
						</select>
					</div>
				</div>
				
				
				<div class="form-group  isShow" <c:if test="${entity.gjtOrg.orgType eq '3'}">style="display: none;"</c:if>>
					<label class="col-sm-2 control-label"><small class="text-red">*</small>所属学习中心：</label>
					<div class="col-sm-8">
						<c:if test="${action eq 'create'}"> 
						<select name="suoShuXxzxId" class="selectpicker show-tick form-control" data-size="7" data-live-search="true" id="suoShuXxzxId">
							<option value="">请选择</option>
							<c:forEach items="${studyList}" var="org">
								<option value="${org.id}"  <c:if test="${org.id==entity.gjtOrg.parentGjtOrg.id}">selected='selected'</c:if>>${org.orgName}</option>
							</c:forEach>
						</select>
						</c:if>
						<c:if test="${action eq 'update'}"> <!-- 暂时不能修改，因为会影响分班 -->
						<p class="form-control-static">${entity.gjtOrg.parentGjtOrg.orgName} </p>
						<input type="hidden" value="${gjtSchoolInfo.id}" name="suoShuXxzxId">
						</c:if>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>学习中心代码</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" id="scCode" name="scCode" value="${entity.scCode}" placeholder="所属院校代码+001~999">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">合同编号</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="compactNo" value="${entity.compactNo}" placeholder="请填写合同编号！">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>
					<font id="orgName">
						<c:if test="${entity.gjtOrg.orgType eq '3'}">
							学习中心名称:
						</c:if>
						<c:if test="${entity.gjtOrg.orgType eq '6'}">
							招生点名称：
						</c:if>
						</font>
						</label>
					<div class="col-sm-8">
						<input class="form-control" type="text" name="scName" value="${entity.scName}" placeholder="请输入学习中心名称">
					</div>
				</div>
<%-- 				<div class="form-group">
					<label class="col-sm-2 control-label">中心类型</label>
					<div class="col-sm-8">
						<select class="selectpicker show-tick form-control" name="centerType">
							<option value="">请选择</option>
							<option value="1" <c:if test="${entity.centerType eq '1'}">selected="selected"</c:if>>学习中心</option>
							<option value="2" <c:if test="${entity.centerType eq '2'}">selected="selected"</c:if>>招生服务站</option>
						</select>
					</div>
				</div> --%>

				<%-- <div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>服务范围</label>
					<div class="col-sm-8">
						<select class="selectpicker show-tick form-control" name="serviceArea">
							<option value="">请选择</option>
							<option value="1" <c:if test="${entity.serviceArea eq '1'}">selected="selected"</c:if>>招生</option>
							<option value="2" <c:if test="${entity.serviceArea eq '2'}">selected="selected"</c:if>>招生+学支</option>
						</select>
					</div>
				</div> --%>
				
				
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>提供的服务项:</label>
					<div class="col-sm-8">
						
						<input type="checkbox" value="1" name="serviceAreas" <c:if test="${serviceAreas['1'] }">checked='checked'</c:if>>招生服务
						<input type="checkbox" value="2" name="serviceAreas" <c:if test="${serviceAreas['2'] }">checked='checked'</c:if> >学习支持服务
						<input type="checkbox" value="3" name="serviceAreas"<c:if test="${serviceAreas['3'] }">checked='checked'</c:if> >课程定制服务
						<input type="checkbox" value="4" name="serviceAreas" <c:if test="${serviceAreas['4'] }">checked='checked'</c:if>>课程教学辅导服务
						<input type="checkbox" value="5" name="serviceAreas" <c:if test="${serviceAreas['5'] }">checked='checked'</c:if>>教材服务
					</div>
				</div>
				
				<div class="form-group">				 
				<label class="col-sm-2 control-label"><small class="text-red">*</small>中心地址</label>
				<div class="col-sm-8">
					<div class="row">
						<div class="col-sm-4">
							<select id="province" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
								<option value="" selected="selected"></option>
							</select>
						</div>
						<div class="col-sm-4">
							<select id="city"  class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
								<option value="" selected="selected"></option>
							</select>
						</div>
						<div class="col-sm-4">
							<select id="district" name="district" class="selectpicker show-tick form-control" data-size="8"  data-live-search="true">
								<option value="${entity.district}" selected="selected"></option>
							</select>
						</div>
					</div>
				</div>
			 </div>
			<div class="form-group"> 
			<label class="col-sm-2 control-label"></label>
			<div class="col-sm-8">
				<input type="text" name="officeAddr" class="form-control" value="${entity.officeAddr}" placeholder="请输入学习中心详细地址"/>
			</div>
		   </div>
									
			<div class="form-group">
				<label class="col-sm-2 control-label"><small class="text-red">*</small>联系电话</label>
				<div class="col-sm-8">
					<input class="form-control" type="text" name="linkTel" value="${entity.linkTel}" placeholder="请输入联系电话">
				</div>
			</div>	
			<div class="form-group">
				<label class="col-sm-2 control-label"><small class="text-red">*</small>联系人</label>
				<div class="col-sm-8">
					<input class="form-control" type="text" name="linkman" value="${entity.linkman}" placeholder="请输入联系人" maxlength="8">
				</div>
			</div>
			<c:if test="${action=='view'}">
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>办学模式:</label>
					<div class="col-sm-8">
						<input type="radio"  name="gjtOrg.schoolModel" value="1" class="flat-red" <c:if test="${empty entity.gjtOrg.schoolModel || entity.gjtOrg.schoolModel==1}">checked="checked"</c:if>> 学历办学
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="gjtOrg.schoolModel" value="2" class="flat-red" <c:if test="${entity.gjtOrg.schoolModel==2}">checked="checked"</c:if>> 中职院校
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="gjtOrg.schoolModel" value="3" class="flat-red" <c:if test="${entity.gjtOrg.schoolModel==3}">checked="checked"</c:if>> 院校模式
					</div>
				</div>
			</c:if>
			<div class="form-group">
				<label class="col-sm-2 control-label">描述</label>
				<div class="col-sm-8">
					<textarea id="editor4" name="memo" rows="10" cols="80">
					${entity.memo}
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
		
		
		$('#orgType').change(function(){
			var orgType=$(this).val();
			if(orgType=='3'){
				$('.isShow').hide();
				$('#orgName').html('学习中心名称：');
			}
			if(orgType=='6'){
				$('.isShow').show();
				$('#orgName').html('招生点名称：');
			}
		});
	});

	CKEDITOR.replace('memo');
</script>
</body>

</html>
