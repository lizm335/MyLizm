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
            	operateCode: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                operateName: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                operateDec: {
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
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/system/option/${action}" method="post">
                <input id="action" type="hidden" name="action" value="${action }">
                <input type="hidden" name="operateId" value="${entity.operateId }">
                  <div class="box-body">
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>操作编码:</label>
								<div class="col-sm-3">
									<input type="text" name="operateCode" class="form-control" value="${entity.operateCode }"/>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>操作名称:</label>
								<div class="col-sm-3">
									<input type="text" name="operateName" class="form-control" value="${entity.operateName }"/>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label">描述:</label>
								<div class="col-sm-3">
									<input type="text" name="operateDec" class="form-control" value="${entity.operateDec }"/>
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