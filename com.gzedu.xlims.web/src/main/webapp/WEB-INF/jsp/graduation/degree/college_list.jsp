<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统-毕业管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li>
				<a href="#">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">毕业管理</a>
			</li>
			<li class="active">学位院校管理</li>
		</ol>
	</section>

	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html">

			<div class="nav-tabs-custom reset-nav-tabs-custom margin-bottom-none" id="notice-tabs">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li class="active">
						<a href="${ctx}/graduation/degreeCollege/list" data-toggle="tab">学位院校设置</a>
					</li>
					<li>
						<a href="${ctx}/graduation/degreeCollege/specialtyList">学位专业设置</a>
					</li>
				</ul>
				<div class="tab-content">
					<div class="box box-border">
						<div class="box-body">
							<div class="row pad-t15">
								<div class="col-md-4 col-xs-6">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap"">院校名称</label>
										<div class="col-sm-9">
											<input type="text" class="form-control" name="search_LIKE_collegeName" value="${param.search_LIKE_collegeName}"> <input type="hidden" name="search_EQ_status" value="${param.search_EQ_status}">
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- /.box-body -->
						<div class="box-footer text-right">
							<button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
							<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
					</div>
					<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>>
						<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
					<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>>
						<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

					<div class="box box-border no-shadow margin-bottom-none">
						<div class="box-body">
							<div class="filter-tabs clearfix">
								<ul class="list-unstyled">
									<li <c:if test="${empty(param.search_EQ_status)}">class="actived"</c:if>>全部（${enable+disable}）</li>
									<li <c:if test="${param.search_EQ_status=='0'}">class="actived"</c:if> data-status="0">已启用（${enable}）</li>
									<li <c:if test="${param.search_EQ_status=='1'}">class="actived"</c:if> data-status="1">已停用（${disable}）</li>
								</ul>
								<div class="pull-right no-margin">
									<shiro:hasPermission name="/graduation/degreeCollege/list$create">
										<a href="${ctx}/graduation/degreeCollege/create" class="btn btn-default btn-sm">
											<i class="fa fa-fw fa-plus"></i> 新增
										</a>
									</shiro:hasPermission>
								</div>
							</div>
							<div id="dtable_wrapper" class="table-responsive">
								<table id="dtable" class="table table-bordered table-hover table-font vertical-mid text-center">
									<thead>
										<tr>
											<th>院校名称</th>
											<th>专业数</th>
											<th>状态</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${empty(pageInfo.content)}">
											<tr><td colspan="4">暂无数据</td></tr>
										</c:if>
										<c:forEach items="${pageInfo.content}" var="info">
											<tr>
												<td>${info.collegeName}</td>
												<td>${fn:length(info.gjtSpecialtyDegreeColleges)}</td>
												<td><c:choose>
														<c:when test="${info.status==0}">
															<span class="text-green">已启用</span>
														</c:when>
														<c:when test="${info.status==1}">
															<span class="text-orange">已停用</span>
														</c:when>
													</c:choose></td>
												<td>
													<div class="data-operion">
													<shiro:hasPermission name="/graduation/degreeCollege/list$view">
														<a href="view/${info.collegeId}" class="operion-item operion-view" title="查看">
															<i class="fa fa-fw fa-eye"></i>
														</a>
													</shiro:hasPermission>
													<shiro:hasPermission name="/graduation/degreeCollege/list$update">
														<a href="update/${info.collegeId}" class="operion-item operion-edit" title="编辑">
															<i class="fa fa-fw fa-edit"></i>
														</a>
													</shiro:hasPermission>
													<shiro:hasPermission name="/graduation/degreeCollege/list$startOrstop">
														<a href="${ctx}/graduation/degreeCollege/changeStatus?collegeId=${info.collegeId}&status=${info.status==0?1:0}" class="operion-item operion-view changeStatus" title="${info.status==0?'停用':'启用'}">
															<i class="fa fa-fw ${info.status==0?'fa-pause':'fa-play'}"></i>
														</a>
													</shiro:hasPermission>
													<shiro:hasPermission name="/graduation/degreeCollege/list$delete">
														<a href="#" class="operion-item operion-view" data-collegeid="${info.collegeId}" data-toggle="tooltip" title="删除" data-role="sure-btn-1" data-original-title="删除">
															<i class="fa fa-trash-o text-red"></i>
														</a>
													</shiro:hasPermission>
													<shiro:hasPermission name="/graduation/degreeCollege/list$create">
														<a href="${ctx}/graduation/degreeCollege/addSpecialty?collegeId=${info.collegeId}" class="operion-item operion-view" title="新增学位专业" data-collegeid="${info.collegeId}" data-toggle="tooltip" title=""  data-original-title="新增专业">
															<i class="fa fa-fw fa-plus"></i>
														</a>
													</shiro:hasPermission>
													</div>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>

								<tags:pagination page="${pageInfo}" paginationSize="10" />

							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- nav-tabs-custom -->
		</form>
	</section>

	<!-- 底部 -->
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
	$(function() {
	    $('.list-unstyled li').click(function() {
		$('[name="search_EQ_status"]').val($(this).data('status'));
		$('#listForm').submit();
	    });

	    $('.changeStatus').click(function() {
			var postIngIframe = $.mydialog({
			    id : 'dialog-1',
			    width : 150,
			    height : 50,
			    backdrop : false,
			    fade : false,
			    showCloseIco : false,
			    zIndex : 11000,
			    content : '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
			});
			$.get($(this).attr('href'), {}, function(data) {
			    if (data.successful) {
					location.reload();
			    } else {
					$.closeDialog(postIngIframe);
					alert(data.message);
			    }
			}, 'json');
			return false;
	    });

	    //删除
	    $("body").confirmation({
		selector : "[data-role='sure-btn-1']",
		html : true,
		placement : 'top',
		content : '<div class="margin_b10"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>确定要删除该院校？</div>',
		title : false,
		btnOkClass : 'btn btn-xs btn-primary',
		btnOkLabel : '确认',
		btnOkIcon : '',
		btnCancelClass : 'btn btn-xs btn-default margin_l10',
		btnCancelLabel : '取消',
		btnCancelIcon : '',
		popContentWidth : 220,
		onShow : function(event, element) {

		},
		onConfirm : function(event, element) {
		    var postIngIframe = $.mydialog({
			id : 'dialog-1',
			width : 150,
			height : 50,
			backdrop : false,
			fade : false,
			showCloseIco : false,
			zIndex : 11000,
			content : '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
		    });
		    $.get('${ctx}/graduation/degreeCollege/delete', {
			collegeId : $(element).data('collegeid')
		    }, function(data) {
			if (data.successful) {
			    location.reload();
			} else {
			    $.closeDialog(postIngIframe);
			    alert(data.message);
			}
		    }, 'json');
		},
		onCancel : function(event, element) {

		}
	    });

	})
    </script>
</body>
</html>