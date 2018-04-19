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
                        notEmpty: "required",
                        callback: {  
                            message: '帐号已经存在',  
                            callback: function(value, validator) {
                            	var validator=true;
                            	$.get("checkloginAccount/"+$('#loginAccount').val(),function(data){
                            		console.log(data);
                            	})
                            	return validator;
                            }  
                         }
                     }
                },
                password2: {
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
                  <h3 class="box-title">用户管理</h3>
                </div>
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/system/user/${action}" method="post">
                <input id="action" type="hidden" name="action" value="${action }">
                <input type="hidden" name="id" value="${entity.id }">
                  <div class="box-body">
							<div class="form-group"> 
								<label class="col-sm-2 control-label">登录帐号:</label>
								<div class="col-sm-3">
									<input type="text" name="loginAccount" class="form-control" value="${entity.loginAccount }"/>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label">登录密码:</label>
								<div class="col-sm-3">
									<input type="text" name="password2" class="form-control" value="${entity.password2 }"/>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label">真实姓名:</label>
								<div class="col-sm-3">
									<input type="text" name="realName" class="form-control" value="${entity.realName }"/>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label">是否启用:</label>
								<div class="col-sm-3">
									 <input type="checkbox" <c:if test="${entity.isEnabled}">checked='checked'</c:if> name="isEnabled"/>
								</div>
							</div>
							
							<%-- <div class="form-group"> 
								<label class="col-sm-2 control-label">用户类型:</label>
								<div class="col-sm-3">
									 <input type="radio" <c:if test="${entity.userType==0}">checked="checked"</c:if> name="userType" value="0">管理员
									 <input type="radio" <c:if test="${entity.userType==1}">checked="checked"</c:if> name="userType" value="1">学生
									 <input type="radio" <c:if test="${entity.userType==2}">checked="checked"</c:if> name="userType" value="2">教师
									 <input type="radio" <c:if test="${entity.userType==3}">checked="checked"</c:if> name="userType" value="3">职工
								</div>
							</div> --%>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label">角色:</label>
								<div class="col-sm-3">
									 <select name="priRoleInfo.roleId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<c:forEach items="${roles}" var="role">
												<option value="${role.roleId}" <c:if test="${entity.priRoleInfo!=null && role.roleId == entity.priRoleInfo.roleId}">selected = 'selected'</c:if>>${role.roleName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						
                  </div>

                  <div class="box-footer">
                  	<div class="col-sm-offset-1 col-sm-11">
	                    <button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
    	                <button id="btn-back" class="btn btn-primary" onclick="history.back()">返回</button>
                  	</div>
                  </div>
                  </form>
                  
              </div>
            </div>
          </div>  
</section>
</body>
</html>