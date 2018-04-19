<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	var oldLoginAccount = $("#loginAccount").val();
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
	//参考： http://bv.doc.javake.cn/examples/ 
    $('#inputForm').bootstrapValidator({
    		excluded: [':disabled'],//验证下拉必需加
            fields: {
            	loginAccount: {
                    validators: {
                        notEmpty: {
                        	message: "帐号不能为空"
                        },
                        callback: {  
                            message: '帐号已经存在',  
                            callback: function(value, validator) {
                            	var validator=true;
                            	if(oldLoginAccount != $("#loginAccount").val() && $("#loginAccount").val()!==" "){
	                            	$.ajax({  
	                                    type : "post",  
	                                    url : "checkloginAccount",  
	                                    dataType:'json',
	                                    data : {loginAccount:$('#loginAccount').val()},  
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
                realName: {
                    validators: {
                        notEmpty: {
                        	message: "姓名不能为空"
                        },
                    }
                },
                email: {
                    validators: {
                        notEmpty: {
                        	message: "邮箱不能为空"
                        },
                    }
                },
                password2: {
                    validators: {
                       	notEmpty: {
                           	message: "密码不能为空"
                        },
                    }
                }
            }
        }); 
})
</script> 

</head>
<body class="inner-page-body">
<!-- Main content -->
 <section class="content-header">
 	<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.back()">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">系统管理</a></li>
		<li><a href="#">用户管理</a></li>
		<li class="active">新增用户</li>
	</ol>
</section>
<section class="content">
<div class="box box-default">
    <div class="box-header with-border"> 
      	<h3 class="box-title">新增用户</h3>
    </div>
    <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/system/user/${action}" method="post">
        <input id="action" type="hidden" name="action" value="${action }">
        <input type="hidden" name="id" value="${entity.id }">
			<div class="box-body">
				<div class="form-group"> 
					<label class="col-sm-2 control-label"><small class="text-red">*</small>帐号:</label>
					<div class="col-sm-8">
						<input type="text" id="loginAccount" name="loginAccount" class="form-control" value="${entity.loginAccount }"/>
					</div>
				</div>
				
				<div class="form-group"> 
					<label class="col-sm-2 control-label"><small class="text-red">*</small>姓名:</label>
					<div class="col-sm-8">
						<input type="text" name="realName" class="form-control" value="${entity.realName }"/>
					</div>
				</div>
				
				<div class="form-group"> 
					<label class="col-sm-2 control-label"><small class="text-red">*</small>院校:</label>
					<div class="col-sm-8">
						 <select name="gjtOrg.id" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<c:forEach items="${orgs}" var="org">
								<option value="${org.key}" <c:if test="${entity.gjtOrg!=null && org.key == entity.gjtOrg.id}">selected = 'selected'</c:if>>${org.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="form-group"> 
					<label class="col-sm-2 control-label"><small class="text-red">*</small>角色:</label>
					<div class="col-sm-8">
						 <select name="priRoleInfo.roleId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<c:forEach items="${roles}" var="role">
									<option value="${role.key}" <c:if test="${entity.priRoleInfo!=null && role.key == entity.priRoleInfo.roleId}">selected = 'selected'</c:if>>${role.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="form-group"> 
					<label class="col-sm-2 control-label">手机:</label>
					<div class="col-sm-8">
						<input type="text" name="sjh" class="form-control" value="${entity.sjh }"/>
					</div>
				</div>
				
				<div class="form-group"> 
					<label class="col-sm-2 control-label"><small class="text-red">*</small>邮箱:</label>
					<div class="col-sm-8">
						<input type="text" name="email" class="form-control" value="${entity.email }"/>
					</div>
				</div>
				
				<div class="form-group"> 
					<label class="col-sm-2 control-label"><small class="text-red">*</small>密码:</label>
					<div class="col-sm-8">
						<input type="text" name="password2" class="form-control" value="${entity.password2 }"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label no-pad-top"><small class="text-red">*</small>状态</label>
					<div class="col-sm-8">
						<div class="full-width">
							<label class="text-no-bold">
							  <input type="radio" name="isEnabled" class="minimal" value="true" <c:if test="${entity.isEnabled}">checked</c:if>>
							  启用
							</label>
							<label class="text-no-bold left10">
							  <input type="radio" name="isEnabled" class="minimal" value="false" <c:if test="${!entity.isEnabled}">checked</c:if>>
							  停用
							</label>
						</div>
					</div>
				</div>
			</div>

			<div class="box-footer text-center">
			    <button id="btn-submit" type="submit" class="btn btn-primary margin_r10 min-width-90px">确定</button>
			    <button id="btn-back" class="btn btn-default min-width-90px" onclick="history.back()">返回</button>
			</div>
    </form>
                  
</div>
 
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>