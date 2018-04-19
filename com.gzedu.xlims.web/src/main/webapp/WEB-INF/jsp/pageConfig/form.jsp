<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>文章管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<!-- ckeditor --> 
<script src="${ctx }/static/plugins/ckeditor/ckeditor.js"></script> 
<script type="text/javascript">
$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
	//参考： http://bv.doc.javake.cn/examples/ 
    $('#inputForm').bootstrapValidator({
    	excluded: [':disabled'],
    	 fields: {    		   
    		 	xxId: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                appKey: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                appName: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                posKey: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                posName: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                content: {
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
<!-- Main content -->
<section class="content-header">
	
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">主页</a></li>
		<li class="active">页面配置设置</li>
	</ol>
</section>
<section class="content">
          
  <div class="box box-default margin-bottom-none">
    <div class="box-header with-border"> 
      <h3 class="box-title">页面配置</h3>
    </div>
    <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/pageConfig/${action}" method="post">
    	<input id="action" type="hidden" name="action" value="${action}">
    	<input type="hidden" name="id" value="${gjtPageDef.id}">
      	<div class="box-body">
			<div class="form-group"> 
				<label class="col-sm-2 control-label"><small class="text-red">*</small>院校:</label>
				<div class="col-sm-6">
					<select name ="xxId" class="form-control selectpicker">
						<option value="">请选择</option>
						<c:forEach items="${xxmcMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==gjtPageDef.xxId}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group"> 
				<label class="col-sm-2 control-label"><small class="text-red">*</small>应用KEY:</label>
				<div class="col-sm-6">
					<input type="tel" name="appKey" class="form-control" value="${gjtPageDef.appKey}"/>
				</div>
			</div>
			<div class="form-group"> 
				<label class="col-sm-2 control-label"><small class="text-red">*</small>应用名称:</label>
				<div class="col-sm-6">
					<input type="tel" name="appName" class="form-control" value="${gjtPageDef.appName}"/>
				</div>
			</div>
			<div class="form-group"> 
				<label class="col-sm-2 control-label"><small class="text-red">*</small>位置KEY:</label>
				<div class="col-sm-6">
					<input type="tel" name="posKey" class="form-control" value="${gjtPageDef.posKey}"/>
				</div>
			</div>
			<div class="form-group"> 
				<label class="col-sm-2 control-label"><small class="text-red">*</small>位置名称:</label>
				<div class="col-sm-6">
					<input type="tel" name="posName" class="form-control" value="${gjtPageDef.posName}"/>
				</div>
			</div>								
			<div class="form-group"> 
				<label class="col-sm-2 control-label"><small class="text-red">*</small>内容:</label>
				<div class="col-sm-6">
					<textarea class="form-control" name="content" rows="10">${gjtPageDef.content}</textarea>																
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-6 col-sm-offset-2">
					<button id="btn-submit" type="submit" class="btn btn-primary min-width-90px margin_r10">确定</button>
					<button id="btn-back"  class="btn btn-default min-width-90px" onclick="history.back()">返回</button>
				</div>
			</div>
  		</div>
    </form>
  </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>