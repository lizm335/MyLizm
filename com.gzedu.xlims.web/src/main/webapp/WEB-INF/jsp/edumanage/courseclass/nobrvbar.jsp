<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>未分班学员</title>

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
	        	    content: '至少选择一名学员！',
	        	    confirmButton: '确认'
	        });
	 			return false;
			}
	 		$.confirm({
                title: '提示',
                content: '确认将选中的学员添加进-'+$('#className').val()+'？',
                confirmButton: '确认',
                icon: 'fa fa-warning',
                cancelButton: '取消',  
                confirmButtonClass: 'btn-primary',
                closeIcon: true,
                closeIconClass: 'fa fa-close',
                confirm: function () {
                	$.get("brvbar.html?classId="+$('#classId').val(),$checkedIds.serialize(),function(data){
                		if(data.successful){ 
                			showSueccess(data);
                			window.location.href="${ctx}/edumanage/courseclass/list.html"; 
                  		}else{
                  			showFail(data);
                  		}
                	},"json");  
                	showSueccess(true);
                }
            });
	 		
	});
	
	 //TAB标签
	$('.nav-tabs li').click(function(){
		$('#isExists').val($(this).val());//状态切换
		$('#page').val(1);//初始化页数
   	   	$("#listForm").submit(); 
   });
})
</script>

</head>


<body class="inner-page-body">
<form class="form-horizontal" id="listForm" action="queryNoBrvbar.html">
<input id="studyYearCode" type="hidden" name="studyYearCode" value="${studyYearCode}">
<input id="courseId" type="hidden" name="courseId" value="${courseId}">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">学员列表</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">年级</label>
							<div class="col-sm-9">
								<select name="search_EQ_gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_gradeId}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_xh" value="${param.search_EQ_xh}">
									<input type="hidden" name="classId" value="${classId}" id="classId">
									<input type="hidden" name="className" value="${className}" id="className">
								</div>
						</div>
					</div>
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm}">
								</div>
						</div>
						<div class="col-md-4">
									<label class="control-label col-sm-3">专业</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtSpecialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${specialtyMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_gjtSpecialtyId}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
						</div>
					</div>
					
				</div>
				<div class="box-footer">
				<div class="search-more-in">
						高级搜索<i class="fa fa-fw fa-caret-down"></i>
					</div> 
					<div class="btn-wrap">
						<button type="button" class="btn btn-default">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
			</div>
	

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		

	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs">
		  <input id="isExists" type="hidden" name="isExists" value="${isExists}">
		  
		  <li <c:if test="${isExists == 0}">class="active"</c:if> value="0"><a href="#" data-toggle="tab" aria-expanded="true">未分班学员</a></li>
		  <li <c:if test="${isExists == 1}">class="active"</c:if> value="1"><a href="#" data-toggle="tab" aria-expanded="false">已分班学员</a></li>
		</ul>
	
		<div class="box-header with-border">
			<div class="fr">
<!-- 				<div class="btn-wrap fl">
					<button class="btn btn-block btn-success btn-outport">
						<i class="fa fa-fw fa-sign-out"></i> 批量导出
					</button>
				</div>
				<div class="btn-wrap fl">
					<button class="btn btn-block btn-success btn-import">
						<i class="fa fa-fw fa-sign-in"></i> 批量导入
					</button>
				</div> -->
				<div class="btn-wrap fl">
					<button type="reset" class="btn" onclick="history.back()">返回
						<i class="fa fa-fw fa-mail-reply "></i> 
					</button>
				</div> 
				<c:if test="${isExists == 0}">
				<div class="btn-wrap fl">
					<a href="javascript:;" class="btn btn-block btn-success btn-add"  id="allot"  title="添加学员为选中班级学生">
							<i class="fa fa-fw fa-plus" ></i> 添加学员</a>
				</div>
				</c:if>
			</div>
		</div>
		
		
		<div class="box-body">
		
			<div id="dtable_wrapper"
				class="dataTables_wrapper form-inline dt-bootstrap no-footer">
				<div class="row">
					<div class="col-sm-6"></div>
					<div class="col-sm-6"></div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<table id="dtable"
							class="table table-bordered table-striped table-container">
							<thead>
								<tr>
									<th><input type="checkbox" class="select-all"
										id="selectAll"></th>
									<th>年级</th>
									<th>学号</th>
									<th>姓名</th>
									<th>层次</th>
									<th>专业</th>
									<th>学习中心</th>
									<th>所在单位</th>
									<th>所属院校</th>
								</tr>
							</thead>
							<tbody>
							<c:choose>
								<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.getContent() }" var="item">
									<tr>
										<td><input type="checkbox" value="${item.studentId }"	name="ids" class="checkbox"></td>
										<td>${item.gjtGrade.gradeName}</td>
										<td>${item.xh }</td>
										<td>${item.xm }</td>
										<td> ${pyccMap[item.pycc]}</td>
										<td>${item.gjtSpecialty.zymc }</td>
										<td>${item.gjtStudyCenter.scName }</td>
										<td>${item.scCo }</td>
										<td>${item.gjtSchoolInfo.xxmc }</td>
									</tr>
								</c:forEach> 
								</c:when>
								<c:otherwise>
									<tr>
										<td align="center" colspan="9">暂无数据</td>
									</tr>
								</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>
				 <tags:pagination page="${pageInfo}" paginationSize="5" /> 
			</div>
		</div>
	</div>
	</section>
</form>
</body>
</html>