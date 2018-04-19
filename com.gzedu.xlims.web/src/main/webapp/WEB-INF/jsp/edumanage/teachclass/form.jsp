<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任管理系统-操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("readonly","readonly");
		$(':radio').attr("disabled","disabled");
		$('select').attr("disabled","disabled");
		$('#btn-submit').remove();  
	}


    $('#gjtSchoolInfoId').change(function(){
        var id = $('#gjtSchoolInfoId').val();
        $.post("${ctx}/edumanage/teachclass/queryGradeBySchool?gjtSchoolInfoId="+id,
            function(data,status){
                $('#gradeId').empty();
                $("#gradeId").append("<option value=''>请选择</option>");
                $.each(data, function (i) {
                    $("#gradeId").append("<option value='"+data[i].key+"'>"+data[i].value+"</option>");
                });
                $('#gradeId').selectpicker('refresh');
            }
            ,"json");
    });

	//参考： http://bv.doc.javake.cn/examples/
    $('#inputForm').bootstrapValidator({
    		excluded: [':disabled'],//验证下拉必需加
            fields: {
            	gjtSchoolInfoId:{
            		validators: {
                        notEmpty: {
                        }
                    }
                },
                gradeId:{
                    validators: {
                        notEmpty: {
                        }
                    }
                },
                bjmc: {
                	validators: {
                        notEmpty: "required",
                        callback: {  
                            message: '名称已经存在！',
                            callback: function(value, validator) {  
                            	var validator=true;
                            	$.ajax({  
                                    type : "post",  
                                    url : "checkLogin.html",  
                                    dataType:'json',
                                    data : {bjmc:$('#bjmc').val()},  
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
                gjtEmployeeId: {
                    validators: {
                        notEmpty: "required"
                     }
                }
            }
        });
  
	/* $('#gjtSchoolInfoId').change(function(){
			var tongji=$("#gjtEmployeeId").siblings(".bootstrap-select").find("ul.dropdown-menu");
			tongji.html('');
			tongji.html('<li data-original-index="0"><a tabindex="0" class="" style="" data-tokens="null">'+
					'<span class="text">请选择</span><span class="glyphicon glyphicon-ok check-mark"></span></a></li>');
			$('#gjtEmployeeId').html('');
			$('#gjtEmployeeId').html('<option value="" selected="selected">请选择</option>');
			var allhtml='';
			
			$.get('queryHeard.html',{schoolId:$(this).val()},function(data){
				for(var i=0;i<data.length;i++){
					var html ='<option value="'+data[i].ID+'">'+data[i].NAME+'</option>';
					allhtml+=html;
					tongji.append('<li data-original-index="'+(i+1)+'"><a tabindex="0" class="" style="" data-tokens="null">'+
							'<span class="text">'+data[i].NAME+'</span><span class="glyphicon glyphicon-ok check-mark"></span></a></li>');
				}
				$('#gjtEmployeeId').html(allhtml);
			},'json');
	}); */
	
})
</script>

</head>
<body class="inner-page-body">
	<section class="content-header clearfix">
		<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.go(-1)">返回</button>
		<ol class="breadcrumb">
			<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">教务管理</a></li>
			<li><a href="list.html">教务班级</a></li>
			<li class="active">新增班级</li>
		</ol>
	</section>
		

	<section class="content">
		<div class="box no-margin">
			<div class="box-body pad20">
		        <div class="form-horizontal reset-form-horizontal">
					<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/edumanage/teachclass/${action }" method="post">
						<input id="action" type="hidden" name="action" value="${action }">
						<input type="hidden" name="classId" value="${item.classId }">
							<div class="form-group">
								<label class="col-sm-3 control-label"><!-- <small class="text-red">*</small> -->所属院校:</label>
								<div class="col-sm-6">
									<!-- 突然说默认选择管理员的院校就好了 暂时注释咯 -->
									<%-- <select  id="gjtSchoolInfoId" name="gjtSchoolInfoId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="">请选择</option>
										<c:forEach items="${schoolInfoMap}" var="map">
											<c:if test="${action=='create'}">
												<option value="${map.key}">${map.value}</option>
											</c:if>
											<c:if test="${action!='create'}">
												<option value="${map.key}" <c:if test="${map.key==item.gjtSchoolInfo.id}">selected='selected'</c:if>>${map.value}</option>
											</c:if>
										</c:forEach>
									</select> --%>
									
									<input type="text" class="form-control" disabled="disabled"	value="${schoolInfoMap['name']}">
									<input type="hidden" class="form-control" name="gjtSchoolInfoId"	value="${schoolInfoMap['id']}">
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-3 control-label"><!-- <small class="text-red">*</small> -->所属机构:</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" disabled="disabled"	value="${orgName}">
								</div>
							</div>
							
							
							<div class="form-group">
								<label class="col-sm-3 control-label"><small class="text-red">*</small>学期:</label>
								<div class="col-sm-6">
									<select  id="gradeId" name="gradeId" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
										<c:forEach items="${gradeMap}" var="map">
											<c:if test="${action=='create'}">
												<option value="${map.key}" <c:if test="${map.key==currentGradeId}">selected='selected'</c:if>>${map.value}</option>
											</c:if>
											<c:if test="${action=='update'}">
												<option value="${map.key}" <c:if test="${map.key==item.gjtGrade.gradeId}">selected='selected'</c:if>>${map.value}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><small class="text-red">*</small>班级名称:</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="bjmc" name="bjmc"	value="${item.bjmc}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">人数上限:</label>
								<div class="col-sm-6">
									<input type="number" class="form-control"  name="limitNum" maxlength="5" value="${item.limitNum}">
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-6 col-sm-offset-3">
									<button class="btn btn-success min-width-90px margin_r15" type="submit">保存</button>
									<button class="btn btn-default min-width-90px" type="reset" onclick="history.back()" >取消</button>
								</div>
							</div>
					</form>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>