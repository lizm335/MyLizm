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
	//参考： http://bv.doc.javake.cn/examples/ 
	
	
    $('#inputForm').bootstrapValidator({
    		excluded: [':disabled'],//验证下拉必需加
            fields: {
            	gjtSchoolInfoId: {
            		validators: {
                        notEmpty: {
                        }
                    }
                },
                bjmc: {
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
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">操作管理</h3>
					</div>
					<form id="inputForm" class="form-horizontal" role="form"
						action="${ctx }/edumanage/courseclass/${action }" method="post">
						<input id="action" type="hidden" name="action" value="${action }">
						<input type="hidden" name="classId" value="${item.classId }">
						<div class="box-body">
							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>所属院校:</label>
								<div class="col-sm-3">
									<select  id="gjtSchoolInfoId" name="gjtSchoolInfoId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="">请选择</option>
										<c:forEach items="${schoolInfoMap}" var="map">
											<c:if test="${action=='create'}">
													<option value="${map.key}">${map.value}</option>
											</c:if>
											<c:if test="${action!='create'}">
												<option value="${map.key}" <c:if test="${map.key==item.gjtSchoolInfo.id}">selected='selected'</c:if>>${map.value}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>班级名称:</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" id="bjmc" name="bjmc"	value="${item.bjmc}">
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>班主任:</label>
								<div class="col-sm-3">
									<select id="gjtEmployeeId" name="gjtEmployeeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="">请选择</option>
									 	<c:forEach items="${headTeacherMap}" var="map">
											<c:if test="${action=='create'}">
													<option value="${map.key}">${map.value}</option>
											</c:if>
											<c:if test="${action!='create'}">
												<option value="${map.key}" <c:if test="${map.key==item.gjtBzr.employeeId}">selected='selected'</c:if>>${map.value}</option>
											</c:if>
										</c:forEach> 
									</select>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label">限制人数:</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" name="memo"	value="${item.memo}"> 
								</div>
							</div>
							<div class="box-footer">
								<div class="col-sm-offset-1 col-sm-11">
									<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
									<button type="reset" class="btn btn-primary"
										onclick="history.back()">返回</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>