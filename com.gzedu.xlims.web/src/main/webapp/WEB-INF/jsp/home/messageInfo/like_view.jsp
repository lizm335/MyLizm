<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="${ctx }/admin/messageInfo/list"">通知公告</a>
			</li>
			<li class="active">通知详情</li>
		</ol>
	</section>

	<section class="content">
		<div class="bg-white">
			<ul class="nav nav-tabs bg-f2f2f2" data-role="top-nav">
				<li>
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/view/${messageId}" style="border-left-color: transparent;">详情</a>
				</li>
				<li>
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/queryPutListById?id=${messageId}">统计</a>
				</li>
				<li>
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/commentList?id=${messageId}">评论</a>
				</li>
				<li class="active">
					<a class="flat gray no-margin" href="#tab_top_4" data-toggle="tab">点赞</a>
				</li>
				<li>
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/feedbackList?id=${messageId}">反馈</a>
				</li>
			</ul>
			<div class="tab-pane active">
				<div class="pad20">
					<div class="clearfix pad-b10 border-bottom">
						<div class="col-sm-8 pad-t5 no-pad-left no-pad-right">共 ${listSize }人点赞</div>
						<div class="col-sm-4 text-right no-padding">
							<div class="form-inline">
								<!-- <div class="form-group">
	    							<input type="text" class="form-control input-sm" placeholder="请输入搜索关键字">
	    						</div> 
	    						<button type="button" class="btn btn-sm btn-default">搜索</button>-->
							</div>
						</div>
					</div>
					<div class="row">
						<c:forEach items="${list }" var="item">
						<div class="col-xs-6 col-sm-3 col-lg-2 pad15">
							<div class="media">
								<div class="media-left">
									<img src="${css }/ouchgzee_com/platform/xlbzr_css/dist/img/images/no-img.png" 
										class="media-object img-circle" width="50" height="50" alt="User Image" >
								</div>
								<div class="media-body media-middle">
									<div class="f16">${item.gjtUserAccount.realName }</div>
									<div class="gray9 f12"><fmt:formatDate value="${item.createdDt }" type="both"/> </div>
								</div>
							</div>
						</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- 底部 -->
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!--<script type="text/template" id="temp">
	<div class="popover bottom" style="max-width: 450px;display:block;">
		<div class="arrow" style="left: 50px"></div>
		<div class="popover-content">
			<div class="media">
				<div class="media-left">
					<a href="{12}"target="_blank" class="btn btn-default btn-flat">
						<img src="{0}" class="img-circle" width="60" height="60">
					</a>
				</div>
				<div class="media-body">
					<dl class="margin-bottom-none">
						<dt class="text-no-bold f16">{1}</dt>
						<dd class="gray9 f12 ">{10}</dd>
						<dd class="gray9 f12 margin_b10">{2}</dd>
						<dd>
							<table width="100%" class="f12">
								<tbody>
									<tr>
										<td width="40%" class="text-nowrap">学号：{3}</td>
										<td class="text-nowrap">层次：{4}</td>
									</tr>
									<tr>
										<td class="text-nowrap">手机：{5}</td>
										<td class="text-nowrap">年级：{6}</td>
									</tr>
									<tr>
										<td class="text-nowrap">学期：{7}</td>
										<td class="text-nowrap">专业：{8}</td>
									</tr>
								</tbody>
							</table>
						</dd>
					</dl>
				</div>
			</div>
		</div>
		
		<div class="btn-group btn-group-justified" role="group">
			<a href="{11}"target="_blank" class="btn btn-default btn-flat">
       			 ee交流
			</a>
			<a href="{9}" target="_blank" class="btn btn-default btn-flat">
				模拟登录学习空间
			</a>
		</div>
	</div>
</script>
	<script type="text/javascript">
	
	$('body').on('mouseenter', 'img.media-object', function(event) {
		event.preventDefault();
		var self=this;
		var htmlTemp=$("#temp").html();
	  	var json=$(this).data('json');
	  	htmlTemp=htmlTemp.format(
	  		json['imgurl'],
	  		json['name'],
	  		json['class'],
	  		json['stu-no'],
	  		json['level'],
	  		json['tel'],
	  		json['grade'],
	  		json['semester'],
	  		json['professional'],
	  		json['url'],
	  		json['orgName'],
	  		json['eeUrl'],
	  		json['singUrl']
	  	);
	  	if($('[data-role="popover"]').length<=0){
		  	$('body').append(
		  		$('<div/>',{'data-role':'popover'}).css({
			  		top: $(self).offset().top+60,
				  	left: $(self).offset().left-20,
				  	position:'absolute',
				  	width:450
			  	}).html(htmlTemp)
		  	);
	  	}
	  	else{
	  		$('[data-role="popover"]').css({
		  		top: $(self).offset().top+60,
			  	left: $(self).offset().left-20,
			  	position:'absolute'
		  	}).html(htmlTemp).show();
	  	}
	}).on('mouseleave', 'img.media-object', function(event) {
		event.preventDefault();
		$('[data-role="popover"]').hide();
	})
	.on('mouseenter', '[data-role="popover"]', function(event) {
		event.preventDefault();
		$(this).show();
	}).on('mouseleave', '[data-role="popover"]', function(event) {
		event.preventDefault();
		$(this).hide();
	});
	</script>  -->
</body>
</html>

