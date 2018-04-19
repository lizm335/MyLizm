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
var ctx = "${ctx}";
$(function() {
	
	$('#examBatchCode').change(function(){
		var examBatchCode = $('#examBatchCode').val();
		if(examBatchCode!=''){
			$.get("${ctx}/exam/new/batch/queryExamPoint",{examBatchCode:examBatchCode},function(data,status){
				$('#examPointId').empty();
				$("#examPointId").append("<option value=''>请选择</option>");
				$.each(data, function (i) {
					$("#examPointId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
	        	});
			  },"json");
		}else{
			$('#examPointId').empty();
			$("#examPointId").append("<option value=''>请选择</option>");
			$("#examPointId").val("").trigger("change");
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
		<li><a href="#">考场管理</a></li>
		<li class="active">编辑</li>
	</ol>
</section>
<section class="content">
          
              <div class="box box-default">
                <div class="box-header with-border"> 
                  <h3 class="box-title">考场</h3>
                </div>
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx}/edumanage/exam/room/${action}" method="post">
                <%-- <input id="action" type="hidden" name="action" value="${action }"> --%>
                <input type="hidden" id="entity_examRoomId" name="entity_examRoomId" value="${entity.examRoomId}">
                
                  <div class="box-body">
                  
                  		<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>考试计划:</label>
							<div class="col-sm-6">
								<c:if test="${action eq 'update' }">
									<select disabled class="form-control select2">
                                    	<c:forEach items="${batchMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param['search_EQ_examPonitNew.examBatchCode']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
                                	</select>
								</c:if>
								<c:if test="${action ne 'update'}">
									<select id="examBatchCode" name="search_EQ_examPonitNew.examBatchCode" class="form-control select2">
							        	<option value="">请选择</option>
										<c:forEach items="${batchMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param['search_EQ_examPonitNew.examBatchCode']}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</c:if>
							</div>
						</div>
							
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>所属考点:</label>
							<div class="col-sm-6">
								<c:if test="${action eq 'update' }">
									<select disabled class="form-control select2">
                                    	<c:forEach items="${pointMap}" var="examPoint">
											<option value="${examPoint.key}" <c:if test="${examPoint.key==entity.examPointId}">selected='selected'</c:if>>${examPoint.value}</option>
										</c:forEach>
                                	</select>
								</c:if>
								<c:if test="${action ne 'update' }">
									<select id="examPointId" name="search_EQ_examPointId" class="form-control select2">
						        		<option value="">请选择</option>
									</select>
								</c:if>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>考场名称:</label>
							<div class="col-sm-6">
								<input type="text" name="entity_name" id="entity_name" class="form-control" value="${entity.name}" />
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>座位数:</label>
							<div class="col-sm-6">
								<input type="text" name="entity_seats" id="entity_seats" class="form-control" value="${entity.seats}"/>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>考场顺序号:</label>
							<div class="col-sm-6">
								<input type="text" name="entity_orderNo" id="entity_orderNo" class="form-control" value="${entity.orderNo}"/>
							</div>
						</div>
						<div class="row">
		                  	<div class="col-sm-6 col-sm-offset-2">
		                  		<c:if test="${action=='create'}">
		                  			<button id="btn-create" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit">创建</button>
		                  		</c:if>
			                    <c:if test="${action=='update'}">
		                  			<button id="btn-update" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit">更新</button>
		                  		</c:if>
		    	                <button type="reset" id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit"onclick="history.back()">返回</button>
		                  	</div>
		                </div>
                  </div>

                  
                  </form>
                  
              </div>
            
</section>
</body>
<script src="${ctx}/static/js/exam/exam_room_form.js"></script>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>