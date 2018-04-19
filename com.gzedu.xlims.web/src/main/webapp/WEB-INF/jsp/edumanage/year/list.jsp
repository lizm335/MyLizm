<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>年级管理系统-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb oh">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">教务管理</a></li>
			<li class="active">年级管理</li>
		</ol>
	</section>

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

	<section class="content">
		<form class="form-horizontal" id="listForm" action="list.html">
			<div class="box">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">年级编号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_code" value="${param.search_LIKE_code}">
								</div>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">年级名称</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_name" value="${param.search_LIKE_name}">
								</div>
							</div>
						</div>
					</div>
				</div><!-- /.box-body -->
				<div class="box-footer">
					<div class="pull-right"><button type="button" class="btn min-width-90px btn-default  btn-reset">重置</button></div>
					<div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
				</div><!-- /.box-footer-->
			</div>

			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">年级列表</h3>
					<div class="pull-right no-margin">
						<a href="create" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新建学期</a>
					</div>
				</div>
				<div class="box-body">
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
							<tr>
								<th>年级编号</th>
								<th>年级名称</th>
								<th>入学年份</th>
								<th>已开设学期</th>
								<th>操作</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${pageInfo.getContent() }" var="item">
							<tr>
								<td>${item.code}</td>
								<td>${item.name}</td>
								<td>
									${item.startYear}
								</td>
								<td>
									
									${fn:length(item.gjtGrades)}
								</td>
								<td>
									<%-- <shiro:hasPermission name="/edumanage/year/list$update">
										<a href="update/${item.gradeId}" class="operion-item" data-toggle="tooltip" title="编辑"><i class="fa fa-edit"></i></a>
									</shiro:hasPermission> --%>
									<shiro:hasPermission name="/edumanage/year/list$delete">
										<a href="#" class="operion-item operion-view" data-gradeid="${item.gradeId}" data-toggle="tooltip" title="" data-role="sure-btn-1" data-original-title="删除"><i class="fa fa-trash-o text-red"></i></a>
									</shiro:hasPermission>
								</td>
							</tr>
							</c:forEach>
							</tbody>
						</table>
	
						<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
	$(".filter-tabs li").click(function(event) {
		if($(this).hasClass('actived')){
			$(this).removeClass('actived');
		}
		else{
			$(this).addClass('actived');
		}
	});

//删除
$("body").confirmation({
  selector: "[data-role='sure-btn-1']",
  html:true,
  placement:'top',
  content:'<div class="margin_b10"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>确定要删除该年级？</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:220,
  onShow:function(event,element){
    
  },
  onConfirm:function(event,element){
      $.post('${ctx}/edumanage/year/update',{
	  	gradeId:$(element).data('gradeid'),
		isEnabled:0
	  },function(){
			location.reload();
	  },'json');
  },
  onCancel:function(event, element){
    
  }
});
</script>
</body>
</html>