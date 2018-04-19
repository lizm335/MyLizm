<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>院校管理员管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

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
            	'gjtOrg.id': {
            		validators: {
            			notEmpty: "required"
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
                realName: {
                    validators: {
                        notEmpty: "required"
                    }
                },
                email: {
                    validators: {
                    	emailAddress: {  
        					message: '邮箱格式不正确'  
        				}  
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: '新密码不能为空'
                        },
                        stringLength: {
                            min: 6,
                            max: 20,
                            message: '新密码长度为6-20个字符'
                        },
                        identical: {
                            field: 'password2',
                            message: "需与确认密码保持一致"
                        }
                    }
                },
                password2: {
                	validators: {
                        notEmpty: {
                            message: '确认密码不能为空'
                        },
                        stringLength: {
                            min: 6,
                            max: 20,
                            message: '新密码长度为6-20个字符'
                        },
                        identical: {
                            field: 'password',
                            message: "需与新密码保持一致"
                        }
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
						<h3 class="box-title">院校管理员管理</h3>
					</div>
					<form id="inputForm" class="form-horizontal" role="form"
						action="${ctx }/organization/userManager/${action }" method="post">
						<input id="action" type="hidden" name="action" value="${action }">
						<input type="hidden" name="orgId" value="${orgId }">
						<input type="hidden" name="id" value="${item.id }">
						<div class="box-body">
							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>所属机构:</label>
								<div class="col-sm-3">
									<select name="gjtOrg.id"
										class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<c:forEach items="${orgMap}" var="org">
											<c:if test="${action=='create'}">
													<option value="${org.key}">${org.value}</option>
											</c:if>
											<c:if test="${action!='create'}">
												<option value="${org.key}" <c:if test="${org.key==item.gjtOrg.id}">selected='selected'</c:if>>${org.value}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">角色:</label>
								<div class="col-sm-3">
									<select name="priRoleInfo.roleId" class="selectpicker show-tick form-control" data-size="6" data-live-search="true">
										<c:forEach items="${orgMagagerRoleMap}" var="role">
											<option value="${role.key}"  <c:if test="${role.key==item.priRoleInfo.roleId}">selected='selected'</c:if>>${role.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>帐号:</label>
								<div class="col-sm-3">
									<c:if test="${action=='create'}">
										<input type="text" class="form-control" name="loginAccount" id="loginAccount">
									</c:if>
									<c:if test="${action!='create'}">
										<input type="text" class="form-control" name="loginAccount" id="loginAccount" value="${item.loginAccount}">
									</c:if>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>姓名:</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" name="realName"value="${item.realName}">
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">密码:</label>
								<div class="col-sm-3">
									<input type="password" class="form-control" name="password"
										value="${item.password2}">
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">再次确认密码:</label>
								<div class="col-sm-3">
									<input type="password" class="form-control" name="password2"
										value="${item.password2}">
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">邮箱:</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" name="email"
										value="${item.email}">
								</div>
							</div>
						</div>		
						<div class="box-footer">
							<div class="col-sm-offset-1 col-sm-11">
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
</body>
</html>