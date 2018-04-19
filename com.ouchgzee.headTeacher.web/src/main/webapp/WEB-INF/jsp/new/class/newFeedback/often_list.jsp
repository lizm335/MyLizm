<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<h1>答疑管理</h1>
		<ol class="breadcrumb">
			<li>
				<a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li class="active">答疑管理</li>
		</ol>
	</section>
	<section class="content">
		<div class="nav-tabs-custom no-shadow margin-bottom-none">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li>
					<a href="${ctx}/home/class/newFeedback/list?type=N">待解答疑问(${noSolve})</a>
				</li>
				<li>
					<a href="${ctx}/home/class/newFeedback/list?type=Y">已解答疑问(${yesSolve})</a>
				</li>
				<li>
					<a href="${ctx}/home/class/newFeedback/list?type=F">转出去的答疑(${forwardCount})</a>
				</li>
				<li class="active">
					<a href="${ctx}/home/class/newFeedback/list?type=E">常见问题(${oftenCount})</a>
				</li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="tab_1">
					<div class="box box-border no-shadow margin-bottom-none">
						<div class="box-body pad-t5">
							<form id="listForm" action="${ctx }/home/class/newFeedback/list">
								<input type="hidden" name="type" value="${type}">
								<div class="clearfix faq-list-bar">
									<div class="search-group-box">
										<div class="form-inline">
											<div class="form-group">
												<select class="form-control margin_r5 select2" name="search_EQ_oftenType">
													<option value="">全部</option>
													<c:forEach items="${oftenTypeMap }" var="map">
														<option value="${map.key }" <c:if test="${param.search_EQ_oftenType eq map.key }">selected</c:if>>${map.value }</option>
													</c:forEach>
												</select>
											</div>
											<div class="form-group">
												<input type="text" name="search_LIKE_subjectTitle" 
												value="${param.search_LIKE_subjectTitle}" class="form-control"
												placeholder="问题标题" >
											</div>
											<div class="form-group">
												<input type="text" name="search_LIKE_isCommendType" 
												value="${param.search_LIKE_isCommendType}" class="form-control"
												placeholder="常见问题标签" >
											</div>
											<div class="form-group">
												<button type="submit" class="btn btn-default margin_r5">搜索</button>
												<button type="button" class="btn btn-default" data-role="add-faq">发布新的常见问题</button>
											</div>
										</div>
									</div>
								</div>
							</form>
							<ul class="list-unstyled news-list clearfix">
								<c:choose>
									<c:when test="${not empty pageInfo && pageInfo.numberOfElements > 0}">
										<c:forEach items="${pageInfo.content}" var="info">
											<c:if test="${not empty info}">
												<li class="news-item question-item pad-l15 pad-r15">
													<div class="the-topic-ask">
														<h4 class="text-yellow text-bold f16 no-margin pad-t5">
															<span class="label label-warning">提问</span>
															${info.title}
														</h4>
														<div class="txt">${info.content}</div>

														<c:if test="${not empty info.imgUrls }">
															<ul class="list-inline img-gallery">
																<c:forEach items="${info.imgUrls }" var="picture">
																	<li>
																		<img src="${picture }" data-role="lightbox" role="button">
																	</li>
																</c:forEach>
															</ul>
														</c:if>
													</div>
													<c:if test="${not empty info.replyList }">
														<c:forEach items="${info.replyList }" var="item" begin="0" end="0">
															<div class="the-topic-ans margin_t20">
																<span class="label label-primary">回答</span>
																<div class="txt">${item.content }</div>
																<c:if test="${not empty item.imgUrls }">
																	<ul class="list-inline img-gallery">
																		<c:forEach items="${item.imgUrls }" var="teacherPicture">
																			<li>
																				<img src="${teacherPicture }" data-role="lightbox" role="button">
																			</li>
																		</c:forEach>
																	</ul>
																</c:if>
																<div class="margin_t15">
																	<c:if test="${info.isComm eq 'Y' && not empty info.isState}">
																		<button type="button" class="btn min-width-90px btn-default" data-role="cancel-faq" data-id='${info.id }'>取消设置常见问题</button>
																	</c:if>
																	<c:if test="${empty info.isState}">
																		<button type="button" class="btn min-width-90px btn-default" data-role="update-faq" data-id='${info.id }'>修改</button>
																		<button type="button" class="btn min-width-90px btn-default" data-role="delete-faq" data-id='${info.id }'>删除</button>
																	</c:if>
																</div>
																<ul class="list-unstyled margin_t10 f12 gray9">
																	<li>
																		<span class="margin_r15">类型：${oftenTypeMap[info.oftenType] } </span>
																		来自：${info.questionSourcePath }
																	</li>
																	<li>
																		<span class="margin_r15">标签：
																			<c:forEach items="${info.isCommendType } " var="item">
																				<font color="red">${item }</font>   
																			</c:forEach>
																		</span>
																	</li>
																</ul>
															</div>
														</c:forEach>
													</c:if>
												</li>
											</c:if>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<li class="news-item question-item" style="text-align: center;">暂无数据</li>
									</c:otherwise>
								</c:choose>
							</ul>
							<div style="padding: 10px 0 10px 0;">
								<tags:pagination page="${pageInfo}" paginationSize="5" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
	
	// 发布新的常见问题
	$('[data-role="add-faq"]').click(function(event) {
	  event.preventDefault();
	  $.mydialog({
	    id:'add-faq',
	    width:700,
	    height:($(window).height()<600?$(window).height()*0.95:600),
	    zIndex:11000,
	    content: 'url: ${ctx}/home/class/newFeedback/createOften.html'
	  });
	});

	// 修改常见问题
	$('[data-role="update-faq"]').click(function(event) {
		var $id=$(this).data('id');
	  event.preventDefault();
	  $.mydialog({
	    id:'update-faq',
	    width:700,
	    height:($(window).height()<600?$(window).height()*0.95:600),
	    zIndex:11000,
	    content: 'url: ${ctx}/home/class/newFeedback/updateOften?id='+$id
	  });
	});
	
	//取消设置常见问题
	$('[data-role="cancel-faq"]').click(function(event) {
	  var $id=$(this).data('id');
	  $.alertDialog({
	    id:'cancel-faq',
	        width:400,
	        height:280,
	        zIndex:11000,
	        ok:function(){//“确定”按钮的回调方法
	          //这里 this 指向弹窗对象
	          $.post('${ctx}/home/class/newFeedback/setOften.html',{id:$id,state:'N'},function(data){
					if(data.successful){
						alert(data.message);
						window.location.reload();
					}
	          },'json'); 
	          $.closeDialog(this);
	        },
	        content:[ '<div class="text-center">',
	          '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
	              '<span class="f16 inline-block vertical-mid text-left">',
	                '是否确认取消设置该常见问题？', '</span>', '</div>'].join('')
	  })
	});
	
	
	//删除常见问题
	$('[data-role="delete-faq"]').click(function(event) {
	  var $id=$(this).data('id');
	  $.alertDialog({
	    id:'cancel-faq',
	        width:400,
	        height:280,
	        zIndex:11000,
	        ok:function(){//“确定”按钮的回调方法
	          //这里 this 指向弹窗对象
	          $.get('${ctx}/home/class/newFeedback/delete.html',{id:$id},function(data){
					if(data.successful){
						alert(data.message);
						window.location.reload();
					}
	          },'json'); 
	          $.closeDialog(this);
	        },
	        content:[ '<div class="text-center">',
	          '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
	              '<span class="f16 inline-block vertical-mid text-left">',
	                '是否删除数据？', '</span>', '</div>'].join('')
	  })
	});
	
		//简略文字的控制
		function summaryControl() {
			$('.question-item .txt').each(function(index, ele) {
				ele = $(ele);
				if (ele.height() > 40 && ele.children(".dotdot").length <= 0) {
					ele.height(40).append($("<span>", {
						"class" : "dotdot",
						html : "...<em>[ + ]</em>"
					}));
				}
			});

		};

		$(".nav-tabs").on('shown.bs.tab', 'a', function(event) {
			event.preventDefault();
			summaryControl();
		});

		$(window).on('resize load', function(event) {
			summaryControl();
		});

		$(".question-item").on("click", '.dotdot', function() {
			if ($(this).parent().height() == 40) {
				$(this).html("<em>[ - ]</em>").parent().css("height", "auto");
			} else {
				$(this).html("...<em>[ + ]</em>").parent().height(40)
			}
		});
		
		//下拉样式
		$('.select2').select2();
	</script>

</body>
</html>
