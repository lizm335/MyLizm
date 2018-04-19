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
            fields: {
            	xxdm: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                xxmc: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                'gjtSchoolInfo.linkTel': {
                    validators: {
                        notEmpty: "required",
                        phone : "required"
                    }
                },
                'gjtSchoolInfo.linkMan': {
                    validators: {
                        notEmpty: "required",
                    }
                },
                /* 'gjtSchoolInfo.linkMail': {
                    validators: {
                        notEmpty: "required",
                        emailAddress: "required"
                    }
                }, */
                'gjtSchoolInfo.appid': {
                    validators: {
                        notEmpty: "required",
                    }
                }
            }
        });
})
</script> 

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li class="active">新增通知</li>
	</ol>
</section>

<section class="content">
    <div class="box no-margin">
    	<div class="box-body pad20">
	      <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/graduation/adviserMsg/${action}" method="post">
		      <input id="action" type="hidden" name="action" value="${action}">
		      <input type="hidden" name="adviserType" value="${param.adviserType}">
		      <div class="form-horizontal reset-form-horizontal">
					<div class="form-group"> 
						<label class="col-sm-3 control-label"><small class="text-red">*</small>指导老师:</label>
						<div class="col-sm-6">
							<select id="gjtGraduationAdvisers" name="advisers" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" multiple="multiple">
								<c:forEach items="${advisers}" var="map">
									<option value="${map.employeeId}">${map.xm}</option>
								</c:forEach>
							</select>
						</div>
					</div>
		       </div>
		
		       <div class="row">
		           	<div class="col-sm-6 col-sm-offset-3">
		              <button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
		              <button id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
		        	</div>
		       </div>
	       </form>
        </div>
    </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>