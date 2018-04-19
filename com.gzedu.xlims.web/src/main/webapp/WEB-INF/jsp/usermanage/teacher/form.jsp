<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>督导管理系统-操作管理</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<!-- iCheck 1.0.1 -->
	<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
	<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>

	<script type="text/javascript">
	$(function() {
		if($('#action').val() == 'view'){
			$(':input').attr("readonly","readonly");
			$(':radio').attr("disabled","disabled");
			$('select').attr("disabled","disabled");
			$('#btn-submit').remove();
		}
		//参考： http://bv.doc.javake.cn/examples/


		$('#inputForm').bootstrapValidator({
				excluded: [':disabled'],//验证下拉必需加
				fields: {
					xxId: {
						validators: {
							notEmpty: {
							}
						}
					},
					xxzxId: {
						validators: {
							notEmpty: {
							}
						}
					},
					loginAccount: {
						validators: {
							notEmpty: "required",
							callback: {
								message: '帐号已经存在',
								callback: function(value, validator) {
									var validator=true;
									$.ajax({
										type : "post",
										url : "checkLogin.html",
										dataType:'json',
										data : {loginAccount:$('#loginAccount').val()},
										async : false,
										success : function(data){
											if(data.successful){
												validator=false;
											}
										}
									});
								  return validator;
								}
							 }
						 }
					},
					xm: {
						validators: {
							notEmpty: "required"
						}
					},
					xbm: {
						validators: {
							   notEmpty: "required"
						}
					}
				}
			});
	})
	</script>
</head>
<body class="inner-page-body">
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">操作管理</h3>
					</div>
					<form id="inputForm" class="form-horizontal" role="form" action="${ctx}/usermanage/teacher/${action }" method="post">
						<input type="hidden" name="employeeType" value="${employeeType}"/>
						<input id="action" type="hidden" name="action" value="${action }">
						<input type="hidden" name="employeeId" value="${item.employeeId }">
						<input id="headImgUrl" type="hidden" name ="zp" value=""/>
						<div class="row">
							<div class="col-sm-8">
								<div class="box-body">
									<c:if test="${employeeType==10}">
										<div class="form-group">
											<label class="col-sm-3 control-label"><small class="text-red">*</small>教师类别:</label>
											<div class="col-sm-5">
												<c:set var="p12" />
												<c:forEach var="p" items="${item.gjtEmployeePositionList}">
													<c:if test="${p.id.type==11}"><c:set var="p11" value="true" /></c:if>
													<c:if test="${p.id.type==12}"><c:set var="p12" value="true" /></c:if>
													<c:if test="${p.id.type==13}"><c:set var="p13" value="true" /></c:if>
												</c:forEach>
												<!-- checkbox -->
												<div class="">
													<label>
														<input name="position" value="11" type="checkbox" class="flat-red" <c:if test="${empty item.employeeId || p11}">checked</c:if>>
														论文指导教师
													</label>
													<label>
														<input name="position" value="12" type="checkbox" class="flat-red" <c:if test="${p12}">checked</c:if>>
														论文答辩教师
													</label>
													<label>
														<input name="position" value="13" type="checkbox" class="flat-red" <c:if test="${p13}">checked</c:if>>
														社会实践教师
													</label>
												</div>
											</div>
										</div>
									</c:if>
									<div class="form-group">
										<label class="col-sm-3 control-label"><small class="text-red">*</small>所属院校:</label>
										<div class="col-sm-5">
											<select name="xxId"
													class="selectpicker show-tick form-control" data-size="5" data-live-search="true" <c:if test="${action!='create'}">disabled="disabled"</c:if>>
												<option value="">请选择</option>
												<c:forEach items="${schoolInfoMap}" var="map">
													<c:if test="${action=='create'}">
														<option value="${map.key}">${map.value}</option>
													</c:if>
													<c:if test="${action!='create'}">
														<option value="${map.key}" <c:if test="${map.key==item.xxId}">selected='selected'</c:if>>${map.value}</option>
													</c:if>
												</c:forEach>
											</select>

										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label"><small class="text-red">*</small>学习中心:</label>
										<div class="col-sm-5">
											<select name="xxzxId"
													class="selectpicker show-tick form-control" data-size="5"	data-live-search="true" <c:if test="${action!='create'}">disabled="disabled"</c:if>>
												<option value="">请选择</option>
												<c:forEach items="${studyCenterMap}" var="map">
													<c:if test="${action=='create'}">
														<option value="${map.key}">${map.value}</option>
													</c:if>
													<c:if test="${action!='create'}">
														<option value="${map.key}" 	<c:if test="${map.key==item.xxzxId}">selected='selected'</c:if>>${map.value}</option>
													</c:if>
												</c:forEach>
											</select>
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label"><small class="text-red">*</small>帐号:</label>
										<div class="col-sm-5">
											<c:if test="${action=='create'}">
												<input type="text" class="form-control" name="loginAccount" id="loginAccount">
											</c:if>
											<c:if test="${action!='create'}">
												<input type="text" readonly="readonly" class="form-control" name="loginAccount" id="loginAccount" value="${item.gjtUserAccount.loginAccount}">
											</c:if>
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label"><small class="text-red">*</small>姓名:</label>
										<div class="col-sm-5">
											<input type="text" class="form-control" name="xm"
												   value="${item.xm}">
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label"><small class="text-red">*</small>性别:</label>
										<div class="col-sm-5">
											<input type="radio"  name="xbm" value="1" class="flat-red" <c:if test="${item.xbm==1}">checked="checked"</c:if>> 男
											&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="radio"  name="xbm" value="2" class="flat-red" <c:if test="${item.xbm==2}">checked="checked"</c:if>> 女
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label">手机号:</label>
										<div class="col-sm-5">
											<input type="text" class="form-control" name="sjh"
												   value="${item.sjh}">
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label">办公电话:</label>
										<div class="col-sm-5">
											<input type="text" class="form-control" name="lxdh"
												   value="${item.lxdh}">
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label">QQ:</label>
										<div class="col-sm-5">
											<input type="text" class="form-control" name="qq"
												   value="${item.qq}">
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label">邮箱:</label>
										<div class="col-sm-5">
											<input type="text" class="form-control" name="dzxx"
												   value="${item.dzxx}">
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label">办公时间:</label>
										<div class="col-sm-5">
											<input type="text" class="form-control" name="workTime"
												   value="${item.workTime}">
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label">办公地点:</label>
										<div class="col-sm-5">
											<input type="text" class="form-control" name="workAddr"
												   value="${item.workAddr}">
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-3 control-label">备注:</label>
										<div class="col-sm-7">
											<textarea class="form-control" name="individualitySign" rows="3">${item.individualitySign}</textarea>
										</div>
									</div>
								</div>
							</div>
							<div class="col-sm-4">
								<div class="box-body">
									<div class="form-group">
										<label class="col-sm-2 control-label"></label>
										<%--<div class="col-sm-6">
											<c:choose>
												<c:when test="${not empty item.zp}">
													<img id ="headImgId" src="${item.zp}" class="img-circle" alt="User Image" width="200" height="200" onerror="this.src='${ctx }/static/images/headImg04.png'">
												</c:when>
												<c:otherwise>
													<img id ="headImgId" src="${ctx }/static/images/headImg04.png" class="img-circle" alt="User Image" width="200" height="200">
												</c:otherwise>
											</c:choose>
											<ul class="pager">
												<li><a href="javascript:void(0);" onclick="">上传头像</a></li>
											</ul>
										</div>--%>
									</div>
								</div>
							</div>
						</div>

						<div class="box-footer">
							<div class="col-sm-offset-2 col-sm-10">
								<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
								<button type="reset" class="btn btn-primary"
									onclick="history.back()">返回</button>
							</div>
						</div>
					</form>

				</div>
			</div>
		</div>
	</section>

	<script type="text/javascript">
		$(function () {
			//Flat red color scheme for iCheck
			$('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
				checkboxClass: 'icheckbox_flat-green',
				radioClass: 'iradio_flat-green'
			});
		});
	</script>
</body>
</html>