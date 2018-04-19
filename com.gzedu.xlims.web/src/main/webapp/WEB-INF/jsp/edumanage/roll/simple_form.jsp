<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
	
	$('#gradeId').change(function(){
		var id = $('#gradeId').val();
		$.get("${ctx}/select/querySpecialtyByGradeId?id="+id,
			function(data,status){
				$('#majorId').empty();
				$("#majorId").append("<option value=''>请选择</option>");
				$.each(data, function (i) {
					$("#majorId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
	        	});
				$('#majorId').selectpicker('refresh'); 
			  }
		,"json");
	});
	
	//参考： http://bv.doc.javake.cn/examples/ 
    $('#inputForm').bootstrapValidator({
    		excluded : [ ':disabled' ],//验证下拉必需加
            fields: {
            	textbookCode: {
                    validators: {
                        notEmpty: {
                        	message: "请填写书号"
                        }
                    }
                },
                textbookName: {
                    validators: {
                        notEmpty: {
                        	message: "请填写教材名称"
                        }
                    }
                },
                price: {
                    validators: {
                        notEmpty: {
                        	message: "请填写定价"
                        },
                        regexp: {
                        	regexp: /^[1-9][0-9]*(\.[0-9]{1,2})?$/,
                            message: "请填写有效数字"
                        }
                    }
                },
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
		<li><a href="#">学员管理</a></li>
		<li class="active">新增学员</li>
	</ol>
</section>
<section class="content">
	<div class="box no-margin">
		<div class="box-body pad-t15">
		<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/api/signupdata/add" method="post">
			<input id="action" type="hidden" name="action" value="${action}">
            <input type="hidden" name="textbookId" value="${entity.textbookId}">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>姓名</label>
					<div class="col-sm-7">
						<input type="text" name="name" class="form-control" placeholder="姓名" value="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>身份证</label>
					<div class="col-sm-7">
						<input type="text" name="studentNo" class="form-control" placeholder="身份证" value="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>年级</label>
					<div class="col-sm-7">
						<select id="gradeId" name="gradeId"
							class="selectpicker show-tick form-control" 
							data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
							<c:forEach items="${gradeMap}" var="map">
								<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']}">selected='selected'</c:if> >
									${map.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>专业</label>
					<div class="col-sm-7">
						<select id="majorId" name="majorId"
							class="selectpicker show-tick form-control" 
							data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-7 col-sm-offset-2">
						<button type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
						<button class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
					</div>
				</div>
			</div>
		</form>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
</html>
