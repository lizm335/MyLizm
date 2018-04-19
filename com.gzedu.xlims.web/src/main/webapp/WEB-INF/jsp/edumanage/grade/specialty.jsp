<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript">
$(function() {
	$('#allot').click(function(){
		 var $checkedIds = $("table td input[name='ids']:enabled:checked");
		 $checkedIds.serialize();
	 		if ($checkedIds.size() ==0) {
	 			$.alert({
	        	    title: '提示',
	        	    icon: 'fa fa-exclamation-circle',
	        	  	confirmButtonClass: 'btn-primary',
	        	    content: '至少选择一门专业！',
	        	    confirmButton: '确认'
	        });
	 			return false;
			}
	 		$.confirm({
                title: '提示',
                content: '确认开设专业?',
                confirmButton: '确认',
                icon: 'fa fa-warning',
                cancelButton: '取消',  
                confirmButtonClass: 'btn-primary',
                closeIcon: true,
                closeIconClass: 'fa fa-close',
                confirm: function () {
                	$.get("${ctx}/edumanage/gradespecialty/addSpecialty.html?gradeId="+$('#gradeId').val(),$checkedIds.serialize(),function(data){
                		if(data.successful){ 
                			showSueccess(data);
                			setTimeout(function(){
                				window.location.reload();
                			},3100);
                  		}else{
                  			showFail(data);
                  		}
                	},"json");  
                }
            });
	 		
	});
	
	$('.deleteItem').click(function(){
		var specialtyId=$(this).attr('val');
		 $.confirm({
             title: '提示',
             content: '确认删除？',
             confirmButton: '确认',
             icon: 'fa fa-warning',
             cancelButton: '取消',  
             confirmButtonClass: 'btn-primary',
             closeIcon: true,
             closeIconClass: 'fa fa-close',
             confirm: function () { 
             	 $.post('${ctx}/edumanage/gradespecialty/deleteSpecialty',{gradeId:$('#gradeId').val(),specialtyId:specialtyId},function(data){
             		if(data.successful){ 
            			showSueccess(data);
            			setTimeout(function(){
            				window.location.reload();
            			},3100);
              		}else{
              			showFail(data);
              		}
             },"json"); 
             } 
         })
	});
})

function tabClick(id){
	$('#type').val(id);
	$('#listForm').submit();
}

function back(){
	window.location.href = "${ctx}/edumanage/gradespecialty/list.html";
}
</script>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="back()" >返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教务管理</a></li>
		<li><a href="#">开设专业</a></li>
		<li class="active">新设专业</li>
	</ol>
</section>

<section class="content">
	<form class="form-horizontal" id="listForm" action="querySpecialty.html">
		<div class="box">
			<div class="box-body">
				<div class="row reset-form-horizontal clearbox">
					<div class="col-md-4">
							<label class="control-label col-sm-3">专业编码</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_zyh" 	value="${param.search_LIKE_zyh}">
							</div>
					</div>
					<div class="col-md-4">
							<label class="control-label col-sm-3">专业名称</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_zymc" value="${param.search_LIKE_zymc}">
							</div>
					</div>
					<div class="col-md-4">
						<label class="control-label col-sm-3">专业性质</label>
						<div class="col-sm-9">
							<select name="search_EQ_specialtyCategory" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<option value="1"  <c:if test="${param.search_EQ_specialtyCategory==1}">selected='selected'</c:if>>开放教育专业</option>
								<option value="2" <c:if test="${param.search_EQ_specialtyCategory==2}">selected='selected'</c:if>>助力计划专业</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row reset-form-horizontal clearbox">
					<div class="col-md-4">
						<label class="control-label col-sm-3">年级名称</label>
						<div class="col-sm-9">
							<select id="gradeId" name="gradeId"
								class="selectpicker show-tick form-control" 
								data-size="5" data-live-search="true">
								<!-- <option value="" selected="selected">请选择</option> -->
								<c:forEach items="${gradeMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==gradeId}">selected='selected'</c:if> >
										${map.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-md-4">
						<label class="control-label col-sm-3">培养层次</label>
						<div class="col-sm-9">
							<select name="search_EQ_pycc" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${pyccMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>					
				</div>
			</div>
			<div class="box-footer">
				<div class="btn-wrap">
					<button type="button" class="btn min-width-90px btn-default">重置</button>
				</div>
				<div class="btn-wrap">
					<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
				</div>
			</div>
		</div>
		


<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			
<div class="box margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title pad-t5">专业列表</h3>
		<div class="pull-right no-margin">
			<c:if test="${type==1 }">
				<button type="button" class="btn btn-primary min-width-90px" id="allot" data-role="sure">确认开设</button>
			</c:if>
		</div>
	</div>
	<div class="box-body">
		<div class="filter-tabs clearfix">
			<input type="hidden" name="type" value="${type}" id="type">
			<ul class="list-unstyled">
				<li <c:if test="${type==0}">class="actived"</c:if> onclick="tabClick(0)">
				已选</li>
				<li <c:if test="${type==1}">class="actived"</c:if> onclick="tabClick(1)">
				未选</li>
			</ul> 
		</div>
	</div>
	
	<div class="table-responsive">
	<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
			<thead>
				<tr>
					<th><input type="checkbox" class="select-all" id="selectAll"></th>
					<th>规则号</th>
					<th>专业名称</th>
					<th>专业性质</th>
					<th>培养层次</th>
					<th>适用行业</th>
					<th>总学分</th>
					<th>必修学分</th>
					<th>选修学分</th>
					<c:if test="${type==0 }"><th>操作</th></c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pageInfo.getContent() }" var="item">
					<tr>
						<td>
							<input type="checkbox" value="${item.specialtyId }"	name="ids">
						</td>
						<td>${item.zyh}</td>
						<td>${item.zymc}</td>
						<td>
						 	<c:if test="${item.specialtyCategory==1}">开放教育专业</c:if>
							<c:if test="${item.specialtyCategory==2}">助力计划专业</c:if>
						</td>
						<td> 
									${pyccMap[item.pycc]}
						 </td>
						 <td>${item.syhy}</td>
						<td>${item.zxf}</td>
						<td>${item.bxxf}</td>
						<td>${item.xxxf}</td>
						<c:if test="${type==0}">
							<td>
							<a href="javascript:void(0);" val="${item.specialtyId }" class="operion-item operion-del deleteItem" title="删除" data-tempTitle="删除"> <i class="fa fa-fw fa-trash-o"></i></a>
							</td>
						</c:if>
					</tr>
				</c:forEach> 
			</tbody>
		</table>
	</div>
 <tags:pagination page="${pageInfo}" paginationSize="5" /> 
</div>
</section>
</form>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>