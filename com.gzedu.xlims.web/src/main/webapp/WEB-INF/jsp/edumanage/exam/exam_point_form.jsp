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
            	code: {
                    validators: {
                        notEmpty: "required",
                        callback: {  
                            message: '编码已经存在',  
                            callback: function(value, validator) {  
                            	var validator=true;
                            	if(oldCode != $("#code").val() && $("#code").val().length > 2){
                            		$.get("${ctx}/exam/new/point/existsCode?code="+$('#code').val(),
                      		      		  function(data,status){
		                            			if(data.successful){
													validator=false;
												}
                      		      		  }
                      			    );
                            	}
                              return validator;
                            }  
                         }
                     }
                },
               examBatchCode: {
                	validators: {
                		 notEmpty: "required"
                     }
                },
                examType: {
                	validators: {
                		 notEmpty: "required"
                     }
                },
                name: {
                	validators: {
                		 notEmpty: "required"
                     }
                },
                province: {
                	validators: {
                		 notEmpty: "required"
                     }
                },
               city: {
                	validators: {
                		 notEmpty: "required"
                     }
                },
                district: {
                	validators: {
                		 notEmpty: "required"
                     }
                },
                address: {
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

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li><a href="#">考点管理</a></li>
		<li class="active">编辑考点</li>
	</ol>
</section>

<section class="content">
	<div class="box no-margin">
		
			
                <div class="box-header with-border"> 
                  <h3 class="box-title">考点管理</h3>
                </div>
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/exam/new/point/${action}" method="post">
                	<input id="action" type="hidden" name="action" value="${action}">
					<input type="hidden" name="examPointId" value="${entity.examPointId }">
					<input type="hidden" name="examBatchId" value="${entity.examBatchId }">
                  <div class="box-body">
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考试形式:</label>
								<div class="col-sm-6">
									<select name="examType" class="selectpicker show-tick form-control"
										data-size="5" data-live-search="true" <c:if test="${action=='update' }">disabled="disabled"</c:if>>
										<option value="" selected="selected">请选择</option>
										<option value="8" <c:if test="${entity.examType=='8'}">selected='selected'</c:if>>笔试</option>
										<option value="11" <c:if test="${entity.examType=='11'}">selected='selected'</c:if>>机试</option>
									</select>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考点编号:</label>
								<div class="col-sm-6">
										<input id="oldCode" type="hidden"  value="${entity.code }"/>
										<input id="code" type="text" name="code" class="form-control" value="${entity.code }" readonly="readonly"/>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考点名称:</label>
								<div class="col-sm-6">
									<input type="text" name="name" class="form-control" value="${entity.name }"/>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>所在区域:</label>
								<div class="col-sm-6" >
									<div class="row">
										<div class="col-sm-4">
											<select id="province" name="province" class="selectpicker show-tick form-control"  data-live-search="true" data-size="8" >

											</select>
										</div>
										<div class="col-sm-4">
											<select id="city" name="city" class="selectpicker show-tick form-control"  data-live-search="true" data-size="8" >

											</select>
										</div>
										<div class="col-sm-4">
											<select id="district" name="district" class="selectpicker show-tick form-control"  data-live-search="true" data-size="8" >
												<option value="${entity.areaId}" selected="selected">请选择</option>
											</select>
										</div>
									</div>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>详细地址:</label>
								<div class="col-sm-6 position-relative">
									<textarea rows="5" class="form-control" placeholder="详细地址" name="address">${entity.address }</textarea>
								</div>
							</div>
						
							<div class="form-group"> 
								<label class="col-sm-2 control-label">适用范围：</label>
								<div class="col-sm-6">
									<select name="studyCenterIds" id="entity_studyCenterIds" class="selectpicker show-tick form-control" multiple
										data-size="10" data-live-search="true">
										<c:forEach items="${studyCenterMap}" var="map">
											<c:set var="flag" value="${false}"></c:set>
											<c:forEach items="${entity.gjtStudyCenters}" var="s">
												<c:if test="${map.key==s.id}">
													<c:set var="flag" value="${true}"></c:set>
												</c:if>
											</c:forEach>
											<option value="${map.key}"<c:if test="${flag}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
									<span class="gray9">注：默认不指定则为通用考点，相关学习中心都可以预约这个考点；如果指定学习中心则只有指定的学习中心可以预约这个考点</span>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label">联系人:</label>
								<div class="col-sm-6 position-relative">
									<input type="text" name="linkMan" class="form-control" value="${entity.linkMan }"/>
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label">联系电话:</label>
								<div class="col-sm-6 position-relative">
									<input type="text" name="linkTel" class="form-control" value="${entity.linkTel }"/>
								</div>
							</div>
						<div class="row">
							<div class="col-sm-7 col-sm-offset-2">
								<button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
								<button id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
							</div>
						 </div>
                  </div>

                  
                  
				
                  </form>
                  
              
            
          </div>  
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="application/javascript">
    (function($){

        $("#province").select_district($("#city"), $("#district"), $("#district").val());

    })(jQuery);
</script>
</body>
</html>