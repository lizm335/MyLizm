<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>教职人员新增</title>
   <%@ include file="/WEB-INF/jsp/common/jslibs_new.jsp"%>
   
</head>

<body class="inner-page-body">

<section class="content-header clearfix">
    <button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.go(-1)">返回</button>
    <ol class="breadcrumb">
        <li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">教务管理</a></li>
        <li><a href="#">教职人员</a></li>
        <li class="active">
            <c:if test="${type eq 'supervisorTeacher'}">新增督导教师</c:if>
            <c:if test="${type eq 'coachTeacher'}">新增辅导教师</c:if>
            <c:if test="${type eq 'paperTeacher'}">论文教师</c:if>
<%--             <c:if test="${type eq 'guideTeacher'}">新增论文指导教师</c:if>
            <c:if test="${type eq 'replyTeacher'}">新增论文答辩教师</c:if>
            <c:if test="${type eq 'practiceTeacher'}">新增社会实践教师</c:if> --%>
        </li>
    </ol>
</section>
<section class="content">
    <div class="box no-margin">
        <div class="box-body pad20">
            <form action="${isAdd?'saveTeacher':'update' }" method="post" id="inputForm" class="form-horizontal">
            	<input type="hidden" value="${gjtEmployeeInfo.employeeId }" name="employeeId"/>
                <div class="form-horizontal reset-form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">头像</label>
                        <div class="col-sm-6">
                            <div class="upload-image-box inlineblock">
                                <div class="upload-image-container">
                                	<input id="headImgUrl" type="hidden"  name ="zp" value="${gjtEmployeeInfo.zp}"/>
                                    <div class="table-cell-block">
                                        <c:if test="${ empty gjtEmployeeInfo.zp}">
                                            <img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/user-placehoder.png" 
                                            class="upload-image-placeholder" alt="User Image" id="headImgId" style="width: 119px;height: 119px;">
                                        </c:if>
                                        <c:if test="${not empty gjtEmployeeInfo.zp}">
                                            <img src="${gjtEmployeeInfo.zp }" class="upload-image-placeholder" alt="User Image" style="width: 119px;height: 119px;">
                                        </c:if>
                                    </div>
                                </div>
                                <div class="btn btn-flat btn-sm btn-block btn-default upload-image-btn">
                                	上传头像
                                    <input class="btn-file" value="" type="button" onclick="javascript:uploadImage('headImgId','headImgUrl');">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <small class="text-red">*</small>
                            账号
                        </label>
                        <div class="col-sm-6">
                            <input type="hidden" name="employeeType" value="${type}">
                            <input type="text" class="form-control form-control-static" id="zgh" placeholder="账号" name="zgh" value="${gjtEmployeeInfo.zgh}" 
                            <c:if test="${!isAdd }">disabled="disabled"</c:if>>
                        </div>
                    </div>
                      <c:if test="${type eq 'paperTeacher'}">
	                     <div class="form-group">
	                        <label class="col-sm-3 control-label">
	                            <small class="text-red">*</small>
	                            教师类别：
	                        </label>
	                        <div class="col-sm-6 form-control-static">
	                            <input type="checkbox"   name="employeeTypes" value="11"  <c:if test="${checkedMap['11']}">checked="checked"</c:if>> 论文指导教师  &nbsp;&nbsp;&nbsp;
	                            <input type="checkbox"   name="employeeTypes" value="12"  <c:if test="${checkedMap['12']}">checked="checked"</c:if> > 论文答辩教师  &nbsp;&nbsp;&nbsp;
	                            <input type="checkbox"    name="employeeTypes" value="13"  <c:if test="${checkedMap['13']}">checked="checked"</c:if>> 社会实践教师 &nbsp;&nbsp;&nbsp;
	                        </div>
	                    </div>
                    </c:if>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><small class="text-red">*</small> 姓名</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="gjtEmployeeInfoXm" placeholder="姓名" name="xm" value="${gjtEmployeeInfo.xm}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><small class="text-red">*</small> 性别</label>
                        <div class="col-sm-6 form-control-static" >
                            <label>
                                <input type="radio" class="minimal" checked name="xbm" value="1">
                                男
                            </label>
                            <label class="left10">
                                <input type="radio" class="minimal" name="xbm" value="2">
                                女
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">手机</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="gjtEmployeeInfoSjh" placeholder="手机" name="sjh" value="${gjtEmployeeInfo.sjh}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">办公电话</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="gjtEmployeeInfoLxdh" placeholder="办公电话" name="lxdh" value="${gjtEmployeeInfo.lxdh}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">QQ</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="gjtEmployeeInfoQq" placeholder="QQ" name="qq" value="${gjtEmployeeInfo.qq}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><small class="text-red">*</small>邮箱</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="gjtEmployeeInfoDzxx" placeholder="邮箱" name="dzxx" value="${gjtEmployeeInfo.dzxx}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">工作时间</label>
                        <div class="col-sm-6">
                            <div class="input-group">
                                <div class="input-group full-width bootstrap-timepicker">
                                       <input type="text" class="form-control timepicker" id="gjtEmployeeInfoWorkTime1" name="workTime" 
                                    	<c:if test="${empty gjtEmployeeInfo.workTime}">value="09:00-17:00"</c:if>  
                                    		<c:if test="${not empty gjtEmployeeInfo.workTime}">value="${gjtEmployeeInfo.workTime }"</c:if>>
                                    <span class="input-group-addon">
                                      <i class="fa fa-clock-o"></i>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">备注</label>
                        <div class="col-sm-6">
                            <textarea class="form-control" rows="5" id="gjtEmployeeInfoIndividualitySign" placeholder="备注" name="individualitySign">${gjtEmployeeInfo.individualitySign}</textarea>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6 col-sm-offset-3">
                        <button class="btn btn-success min-width-90px margin_r15" data-role="save" type="submit">保存</button>
                        <button class="btn btn-default min-width-90px" data-role="cancel" onclick="history.back()">取消</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
 <jsp:include page="/eefileupload/upload.jsp"/> 
<script type="text/javascript">
    $('input[type="radio"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on("ifChecked",function(e){
        $(e.target).attr('checked',true);
    }).on("ifUnchecked",function(e){
        $(e.target).attr('checked',false);
    });

//字段校验
$(function(){
    //参考： http://bv.doc.javake.cn/examples/
    $('#inputForm').bootstrapValidator({
        fields: {
            xm:{
                validators: {
                    notEmpty: {
                        message: '姓名不能为空'
                    }
                }
            },
            zgh: {
                validators: {
                    notEmpty: {message:"帐号不能为空"},
                    stringLength: {
                        min: 3,
                        max: 18,
                        message: '帐号长度必须在3到18位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '帐号只能包含大写、小写、数字和下划线'
                    },
                    callback: {
                        message: '帐号已经存在！',
                        callback: function(value, validator) {
                            var validator=true;
                            $.ajax({
                                type : "post",
                                url : "checkLogin.html",
                                dataType:'json',
                                data : {zgh:$('#zgh').val()},
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
            dzxx:{
              validators:{
            	  notEmpty: {
                      message: '邮箱地址不能为空'
                  },
                  emailAddress: {
                      message: '邮箱地址格式有误'
                  }
              }
            },
            sjh: {
                validators: {
                    phone:{
                        message:"手机号格式不对"
                    }
                }
            },
            qq:{
            	validators: {
            		numeric:{
                        message:"QQ格式不对"
                    }
                }
            }
        }
    });
});
</script>
</body>
</html>
