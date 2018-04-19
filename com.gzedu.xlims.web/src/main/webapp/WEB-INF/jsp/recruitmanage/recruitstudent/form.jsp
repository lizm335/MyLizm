<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>招生管理</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<jsp:include page="/eefileupload/upload.jsp"/>
<script type="text/javascript">
$(function(){
	if($('#action').val() == 'view'){
		$(':input').attr("readonly","readonly");
		$('select').attr("disabled","disabled");
		$('#add-button').attr("disabled","disabled");
		$('#add-all-button').attr("disabled","disabled");
		$('#sdate').attr("disabled","disabled");
		$('#edate').attr("disabled","disabled");
		$('#add-all-button').attr("disabled","disabled");
		$('#add-one-button').attr("disabled","disabled");	
		$("#exampleA").html($("#fileName").val());
		$("#exampleA").attr("href", $("#fileUrl").val());
		$('#bt-submit').remove(); 
	}
	//隐藏删除学习中心按钮
	if($('#action').val() == 'view'||$('#action').val() == 'update'){
		$('.operion-item').remove();
	}
	if($('#action').val() == 'update'){
		$("#exampleA").html($("#fileName").val());
		$("#exampleA").attr("href", $("#fileUrl").val());
	}
	
	//检查各中心的招生指标总和是否大于招生总指标
	$('#bt-submit').click(function() {
	    $('#inputForm').bootstrapValidator({
	    	excluded: [':disabled'],//验证下拉必需加
	    	fields: {
	    		enrollBatchName: {
	        		validators: {
	                    notEmpty:  {message:'招生批次不能为空'}
	                 }
	            },
	            enrollTotalNum: {
	                validators: {
	                    notEmpty: {message:'招生总指标不能为空'},
	                    regexp: {  
	    					regexp: /^[1-9]\d*$/,  
	    					message: '必须是非零的正整数'  
	    				} 
	                },
	            },
	            enrollDownNum: {
	                validators: {
	                	notEmpty: {message:'开班要求不能为空'},
	                	regexp: {  
	    					regexp: /^[1-9]\d*$/,  
	    					message: '必须是非零的正整数'  
	    				} 
	                }
	            },
	            'gjtSpecialty.specialtyId': {
	                validators: {
	                	notEmpty: {message:'招生专业不能为空'}
	                }
	            },
	            pycc: {
	                validators: {
	                	notEmpty: {message:'培养层次不能为空'}
	                }
	            },
	            studyYearId: {
	                validators: {
	                	notEmpty: {message:'招生年度不能为空'}
	                }
	            }	        
	        }
	    
	    });
		var enrollTotalNum=$('#enroll_total_num').val();
		var kpiNum=document.getElementsByName("kpiNum");
		var totalKpiNum=0;
		for (var i = 0, j = kpiNum.length; i < j; i++){
			totalKpiNum+=parseInt(kpiNum[i].value);
		}
		if(totalKpiNum>enrollTotalNum){
			alert("各中心的指标人数已超出招生总指标!");
			return false;
		}else{
			//document.forms['inputForm'].submit();	
			return true;
		}	
	});
});

function uploadCallback() {
	$("#exampleA").attr("href", $("#fileUrl").val());
	$("#exampleA").html($("#fileName").val());
}
function changeNum(){
	var enroll_total_num=$('#enroll_total_num').val();
	$('#totalNum').text(enroll_total_num);
}
function changeStudyTotalNum(){
	var kpiNum=document.getElementsByName("kpiNum");
	var totalKpiNum=0;
	for (var i = 0, j = kpiNum.length; i < j; i++){
		totalKpiNum+=parseInt(kpiNum[i].value);
	}
	$('#studyTotalNum').text(totalKpiNum);
}
</script>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">招生计划</a></li>
		<li class="active">制定招生计划</li>
	</ol>
</section>
<section class="content">
	<div class="box margin-bottom-none">
		<form id="inputForm" class="form-horizontal" name="inputForm" role="form" action="${ctx }/recruitmanage/recruitstudent/${action}" method="post">
		<input id="action" type="hidden" name="action" value="${action}">
		<input type="hidden" name="enrollBatchId" id="enrollBatchId" value="${item.enrollBatchId }">
		<div class="box-body">
			<h3 class="cnt-box-title f16 text-bold margin_b10">招生信息</h3>
			<div class="table-responsive ">
				<table class="table-gray-th">
					<tr>
						<th width="15%">招生批次：</th>
						<td width="35%" class="form-group margin-bottom-none">
							<input class="form-control" type="text" id="enroll_batch_name" name="enrollBatchName" value="${item.enrollBatchName}" <c:if test="${action=='update'}">disabled="disabled"</c:if>>
						</td>

						<th width="15%">招生总指标：</th>
						<td width="35%" class="form-group margin-bottom-none">
							<input class="form-control" type="number" id="enroll_total_num" name="enrollTotalNum" value="${item.enrollTotalNum}" onchange="changeNum();">
						</td>
					</tr>
					<tr>
						<th>招生周期：</th>
						<td class="">
							<div class="input-group input-daterange" >
			                  <input type="text" class="form-control single-datetime"  id="sdate" name="sdate" value="<fmt:formatDate value="${item.enrollSdate}" pattern="yyyy-MM-dd"/>" placeholder="招生开始时间" >
			                  <span class="input-group-addon nobg">－</span>
			                  <input type="text" class="form-control single-datetime"  id="edate" name="edate" value="<fmt:formatDate value="${item.enrollEdate}" pattern="yyyy-MM-dd"/>"  placeholder="招生结束时间">
			              	</div>
						</td>
						
						<th>招生年度：</th>
						<td class="form-group  margin-bottom-none">
							<select class="form-control" name="studyYearId"  <c:if test="${action=='update'}">disabled="disabled"</c:if>>
								<option value="">--请选择--</option>
								<c:forEach items="${studyYearMap}" var="map">
									<c:if test="${action=='create'}">
										<option value="${map.key}">${map.value}</option>
									</c:if>
									<c:if test="${action!='create'}">
										<option value="${map.key}" <c:if test="${map.key==item.studyYearId}">selected='selected'</c:if>>${map.value}</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>培养层次：</th>
						<td class="form-group margin-bottom-none">
							<select class="form-control" name="pycc" id="pycc" <c:if test="${action=='update'}">disabled="disabled"</c:if>>
								<option value="">--请选择--</option>
								<c:forEach items="${pyccMap}" var="map">
									<c:if test="${action=='create'}">
										<option value="${map.key}">${map.value}</option>
									</c:if>
									<c:if test="${action!='create'}">
										<option value="${map.key}" <c:if test="${map.key==item.pycc}">selected='selected'</c:if>>${map.value}</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
						
						<th>招生专业：</th>
						<td class="form-group margin-bottom-none">
							<select class="form-control" name="gjtSpecialty.specialtyId" id="specialty_id" <c:if test="${action=='update'}">disabled="disabled"</c:if>>
								<option value="">--请选择--</option>
								<c:forEach items="${specialtyMap}" var="map">
									<c:if test="${action=='create'}">
										<option value="${map.key}">${map.value}</option>
									</c:if>
									<c:if test="${action!='create'}">
										<option value="${map.key}" <c:if test="${map.key==item.gjtSpecialty.specialtyId}">selected='selected'</c:if>>${map.value}</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>开班要求：</th>
						<td class="form-group margin-bottom-none" <c:if test="${action=='update'}">disabled="disabled"</c:if>>
							<div class="input-group ">
								<div class="input-group-addon no-border no-pad-left ">
									最低不少于
								</div>
								<input type="text" class="form-control" id="enroll_down_num" name="enrollDownNum" value='<fmt:formatNumber value="${item.enrollDownNum}" pattern="#" type="number"/>' />
								<span class="input-group-addon">人</span>
							</div>
						</td>
						
						<th>招生简章：</th>
						<td>
						<c:if test="${item.url!=null}">
						<c:choose>
						  <c:when test="${action!='view'}">
						    <span class="upload-file-lbl vertical-mid margin_r10">
								<div class="in">
								 <a id="exampleA" href="#" target="_blank" ></a>
								</div>								
								<i class="fa fa-close" title="删除" data-toggle="tooltip" data-role="remove"></i>								
							</span>
						</c:when>
						<c:otherwise>
						    <span class="upload-file-lbl vertical-mid margin_r10">
								<div class="in">
								 <a id="exampleA" href="#" target="_blank" ></a>
								</div>															
							</span>
						</c:otherwise>
						</c:choose>
						</c:if>
						<c:if test="${item.url==null&&action!='view'}">
						    <span class="upload-file-lbl vertical-mid margin_r10">
								<div class="in">
								 <a id="exampleA" href="#" target="_blank" ></a>
								</div>								
								<i class="fa fa-close" title="删除" data-toggle="tooltip" data-role="remove"></i>								
							</span>
						</c:if>	  							
						    <input type="hidden" id="fileUrl" name="url" class="form-control" value="${item.url}"/>
							<input type="hidden" id="fileName" name="fileName" class="form-control" value="${item.fileName}"/> 
							 <!--<button type="button" class="btn btn-default btn-sm">上传文件</button> -->							
							<button class="btn btn-default" id="add-button" name="headPortrait" type="button" onclick="uploadFile('fileName','fileUrl',null, null, uploadCallback);">上传</button>
							<%-- <input type="hidden" id="fileUrl"  class="form-control" value="${item.url}" name="url">
							<input type="hidden" id="fileName" name="fileName" class="form-control" value="${item.fileName}"/>
							<input type="button" class="btn btn-default" value="上传" onclick="uploadFile ('fileName','fileUrl',uploadCallback);" /> --%>
						</td>
					</tr>
				</table>
			</div>
			
			
			<div class="clearfix margin_b10 margin_t20" >
				<div class="pull-right" >
					<a href="#" id="add-one-button" role="button" <c:if test="${action!='view'}">data-role="add-center"</c:if> class="btn btn-default btn-sm margin_r10">
						<i class="fa fa-plus"></i>
						添加学习中心
					</a>
					<a href="#" id="add-all-button" role="button" class="btn btn-default btn-sm">
						<i class="fa fa-plus"></i>
						添加下属所有学习中心
					</a>
				</div>
				<h3 class="cnt-box-title f16 text-bold">学习中心指标任务</h3>				
			</div>
			<c:choose>
			  <c:when test="${action=='create'}">
			    <div class="text-orange margin_b10">总招生指标： <span id="totalNum">0</span>，各学习中心分指标总和：<span id="studyTotalNum">0</span></div>
			  </c:when>
			  <c:otherwise>
			  	<div class="text-orange margin_b10">总招生指标： <span id="totalNum">${item.enrollTotalNum}</span>，各学习中心分指标总和：<span id="studyTotalNum">${kpiNums}</span></div>			  	                              
			  </c:otherwise>
			</c:choose>
			<div class="table-responsive margin-bottom-none">
				<table class="table table-bordered table-striped table-cell-ver-mid text-center margin-bottom-none" data-role="study-center-table">
					<thead>
						<tr>
							<th>学习中心</th>
							<th>招生指标人数</th>
							<th>操作</th>
						</tr>
					</thead>
				 
				<tbody>
				 <c:if test="${action=='view'||action=='update'}">	
				 <c:forEach items="${item.gjtStudyEnrollNums}" var="enrollNum">				
				   <tr>
					<td>
					  <div class="form-inline" >
						<select class="form-control select2" name="orgId"  <c:if test="${action=='update'}">readonly="readonly"</c:if>>
					     <c:forEach items="${schoolInfoMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==enrollNum.xxzxId}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
					</div>
					</td>
					<td>
						<div class="form-inline">
							<input type="text" class="form-control text-center" name="kpiNum" value="${enrollNum.enrollKpiNum}" onchange="changeStudyTotalNum();"/>
						</div>
					</td>
					<td>
						<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove-addon" id="study_delete" ><i class="fa fa-trash-o text-red"></i></a>
					</td>
				  </tr>
				  </c:forEach>
				   </c:if>				  
				  </tbody>				   
				</table>
			</div>			
		</div>
		 <div class="box-footer text-right">
			<button type="button" onclick="history.back();" class="btn btn-default min-width-90px margin_r10" data-role="cancel">返回</button>
			<button id="bt-submit" type="submit" class="btn btn-primary min-width-90px" data-role="sure">保存</button>
		</div>
	</form>
	</div>	
</section>
<jsp:include page="/eefileupload/upload.jsp" />
<script type="text/javascript">
$(function(){
//删除附件
$('[data-role="remove"]').click(function(event) {
	//$(this).parent().remove();
	$.ajax({
		type:"GET",
		url:'${ctx }/recruitmanage/recruitstudent/deleteFile',
		data:{enrollBatchId:$('#enrollBatchId').val()},
		dataType:"JSON",
		async: true,
		success: function(datas){
		if(datas.successful){
			alert(datas.message);      		
      		window.location.reload();
      	   //showMessage(datas.message);
      	   //$this.parent().parent().parent().remove();
      	}else{
      		//showMessage(datas.message);
      		alert(datas.message);
      		window.location.reload();
      	}
	  }
	});	
	
});
//删除学习中心
$('[data-role="study-center-table"]').on('click', '[data-role="remove-addon"]', function(event) {
	event.preventDefault();
	$(this).closest('tr').remove();
});
//添加学习中心
$('[data-role="add-center"]').click(function(event) {
	$.ajax({
		type:"GET",
		url:'${ctx }/recruitmanage/recruitstudent/queryStudyCenter',
		dataType:"JSON",
		async: true,
		success: function(datas){
			event.preventDefault();			  	
			var select='<select class="form-control select2" name="orgId" id="org_ids">'
		  			   +'<option value="">--请选择--</option>'		  			  
		  	for(var item in datas.schoolInfoMap) {
		  		select = select + '<option value="'+item+'">'+datas.schoolInfoMap[item]+'</option>';
		  	}
		  	select = select + '</select>';	            		          
			var html=[
				'<tr>',
					'<td>',
						'<div class="form-inline">',
							select,
						'</div>',
					'</td>',
					'<td>',
						'<div class="form-inline">',
							'<input type="text" class="form-control text-center" id="kpi_num" name="kpiNum" onchange="changeStudyTotalNum();"/>',
						'</div>',
					'</td>',
					'<td>',
						'<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove-addon"><i class="fa fa-trash-o text-red"></i></a>',
					'</td>',
				'</tr>'
			];
			var $tr=$(html.join(''));
			$('[data-role="study-center-table"] tbody').append($tr);
			var $sel=$(".select2",$tr).select2({placeholder: "请选择学习中心",language: "zh-CN"});
	  }
	});	
 });
});
</script>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/recruitmanage/recruitstudent/form.js"></script>
</html>
