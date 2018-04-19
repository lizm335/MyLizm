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
<title>学年度制定任务管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript">
$(function() {
	
	$('.updateRen').click(function(){
		 var $this = $(this);
		 var id=$(this).attr('val');
          $.confirm({
              title: '提示',
              content: '确认把任务更改为完成？',
              confirmButton: '确认',
              icon: 'fa fa-warning',
              cancelButton: '取消',  
              confirmButtonClass: 'btn-primary',
              closeIcon: true,
              closeIconClass: 'fa fa-close',
              confirm: function () { 
              	 $.post("${ctx }/edumanage/studyassign/updateStutas.html",{id:id},function(data){
              		if(data.successful){
              			showSueccess(data);
              			window.location.reload();
              		}else{
              			$.alert({
                   	    title: '失败',
                   	    icon: 'fa fa-exclamation-circle',
                   	    confirmButtonClass: 'btn-primary',
                   	    content: '网络异常！',
                   	    confirmButton: '确认',
                   	    confirm:function(){
                   	    	showFail(data);
                   	    }
                   	});
              		}
              },"json"); 
              } 
          });
      });
	
})
</script>

</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">学年度制定任务管理</li>
		</ol>
	</section>


		<section class="content" data-id="0">

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
	<div class="box">
		<div class="box-header with-border">
			<div class="fr">
				<div class="btn-wrap fl">
					<a href="${ctx}/edumanage/studyyear/list" class="btn btn-default"    title="返回学年度管理">返回
						<i class="fa fa-fw fa-mail-reply "></i> 
					</a>
				</div> 
				<div class="btn-wrap fl">
					<a href="${ctx}/edumanage/studyassign/create/${id}" class="btn btn-default btn-sm">
							<i class="fa fa-fw fa-plus"></i> 添加任务</a>
				</div>
				
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
									<th><input type="checkbox" class="select-all" 	id="selectAll"></th>
									<th>序号</th>
									<th>任务名称</th>
									<th>任务制定人</th>
									<th>任务执行人</th>
									<th>完成状态</th>
									<th>对应功能</th>
									<th>备注</th>
									<th>开始时间</th>
									<th>结束时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${list}" var="item">
									<tr>
										<td><input type="checkbox" value="${item.assignmentId}"	name="ids" class="checkbox"></td>
										<td>${item.sort}</td>
										<td>${item.assignmentName}</td>
										<td>${item.assignmentEnact}</td>
										<td>${item.assignmentDo}</td>
										<td>${item.status==0?'未完成':'已完成'}</td>
										<td>${item.parallelism}</td>
										<td>${item.memo}</td>
										<td>${item.startDate}</td>
										<td>${item.endDate}</td>
										<td>
											<div class="data-operion">
												<a href="${ctx }/edumanage/studyassign/update/${item.assignmentId}"
													class="operion-item operion-edit" title="编辑">
													<i class="fa fa-fw fa-edit"></i>编辑</a> 
									
											
												<a href="javascript:;" 
													class="operion-item operion-edit updateRen" title="激活" val="${item.assignmentId}">
													<i class="fa fa-fw fa-edit"></i>激活</a> 
												</div>
										</td>
									</tr>
								</c:forEach> 
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	</section>
</body>
</html>