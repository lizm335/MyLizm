<%@page import="com.gzedu.xlims.pojo.status.CaochDateTypeEnum"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

	<section class="content-header clearfix">
		<ol class="breadcrumb oh">
			<li>
				<a href="#">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">教学管理</a>
			</li>
			<li class="active">教学指引</li>
		</ol>
	</section>
	<section class="content">
		<form id="listForm" class="form-horizontal">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li class="active">
						<a href="#">开学第一堂课</a>
					</li>
					<li>
						<a href="${ctx}/article/guideList">学习指引</a>
					</li>
				</ul>
				<div class="box box-border no-shadow">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">标题</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_title" class="form-control" value="${param.search_LIKE_title }">
									</div>
								</div>
							</div>

							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">适用专业</label>
									<div class="col-sm-9">
										<select name="search_EQ_specialtyBaseId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${specialtyBaseMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param.search_EQ_specialtyBaseId}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>

						</div>
					</div>
					<!-- /.box-body -->
					<div class="box-footer">
						<div class="pull-right">
							<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="pull-right margin_r15">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
					<!-- /.box-footer-->
				</div>
				<div class="box box-border no-shadow margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">视频列表</h3>
						<div class="pull-right no-margin">
							<shiro:hasPermission name="/edumanage/course/queryFirstCoursesList$create">
								<a href="${ctx}/edumanage/course/createFirstCourse" class="btn btn-default btn-sm">
									<i class="fa fa-fw fa-plus"></i>新增
								</a>
							</shiro:hasPermission>
						</div>
					</div>
					<div class="box-body">
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
									<tr>
										<th>标题</th>
										<th>内容说明</th>
										<th>视频</th>
										<th>适用专业</th>
										<th>已观看/未观看</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${not empty pageInfo.content}">
											<c:forEach items="${pageInfo.getContent() }" var="item">
												<tr>
													<td>${item.title }</td>
													<td>${item.content }</td>
													<td>
														<c:forEach items="${item.gjtSpecialtyVideoList}" var="v">
															<c:if test="${not empty(v.coverUrl)}">
																<img alt="视频图片" src="${v.coverUrl}" />
															</c:if>
															<c:if test="${empty(v.coverUrl)}">
																<img alt="视频图片" src="${ctx}/static/dist/img/images/video-img.jpg" width="80" />
															</c:if>
															<br/>
															${v.title}<br/>
														</c:forEach>
													</td>
													<td>
														<div class="text-left">
															<c:forEach items="${item.gjtSpecialtyBaseList}" var="s">
									            				${s.specialtyName}
									            				<small class="gray9">(${s.specialtyCode})</small>
																<br />
															</c:forEach>
															<c:if test="${empty(item.gjtSpecialtyBaseList)}">
																公共
															</c:if>
														</div>
													</td>
													<td>${item.viewNum}/${item.allViewNum-item.viewNum}</td>
													<td>
														<shiro:hasPermission name="/edumanage/course/queryFirstCoursesList$update">
															<a href="${ctx}/edumanage/course/createFirstCourse?firstCourseId=${item.id}" class="operion-item operion-edit" title="修改">
																<i class="fa fa-fw fa-edit"></i>
															</a>
														</shiro:hasPermission>
													</td>
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
						<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

	<script type="text/javascript">
	$('[data-toggle="tab"]').click(function(event) {
	    event.preventDefault();
	    window.location.href = $(this).attr("href");
	});
	//删除辅导资料
	$("html").confirmation({
	    selector : '[data-role="remove"]',
	    html : true,
	    placement : 'top',
	    content : '<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定要删除该资料？</div>',
	    title : false,
	    btnOkClass : 'btn btn-xs btn-primary',
	    btnOkLabel : '确认',
	    btnOkIcon : '',
	    btnCancelClass : 'btn btn-xs btn-default margin_l10',
	    btnCancelLabel : '取消',
	    btnCancelIcon : '',
	    popContentWidth : 190,
	    onShow : function(event, element) {

	    },
	    onConfirm : function(event, element) {
		var postIngIframe = $.formOperTipsDialog({
		    text : '数据提交中...',
		    iconClass : 'fa-refresh fa-spin'
		});
		var dataId = $(element).data('id');
		$.get('${ctx}/edumanage/coachdata/deleteData', {
		    dataId : dataId
		}, function(data) {
		    if (data.successful) {
			location.reload();
		    } else {
			alert(data.message);
			$.closeDialog(postIngIframe);
		    }
		}, 'json')
	    },
	    onCancel : function(event, element) {

	    }
	});

	//附件预览
	$('[data-role="addon-preview"]').click(function(event) {
	    event.preventDefault();
	    var _this = this;
	    var $pop = $.alertDialog({
		id : 'addon-preview',
		title : '在线预览',
		width : $(window).width(),
		height : $(window).height(),
		zIndex : 11000,
		content : '',
		cancelLabel : '关闭',
		cancelCss : 'btn btn-default min-width-90px margin_r15',
		okLabel : '下载文档',
		ok : function() {
		    var json = $(_this).data('json');
		    location.href = '${ctx}/edumanage/coachdata/downloadData?dataId=' + json.dataId + '&redUrl=' + json.dataPath;
		},
		okCss : 'btn btn-primary min-widtufh-90px'
	    });

	    //载入附件内容
	    $('.box-body', $pop).addClass('overlay-wrapper position-relative').html([ '<iframe src="' + $(_this).attr('href') + '" id="Iframe-addon-preview" name="Iframe-addon-preview" frameborder="0" scrolling="auto" style="width:100%;height:100%;position:absolute;left:0;top:0;"></iframe>', '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>' ].join(''));

	    $('#Iframe-addon-preview').on('load', function() {
		$('.overlay', $pop).hide();
	    });
	});
    </script>

</body>
</html>