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
					<c:if test="${is_personage ne '1' }">
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/view/${messageId}">详情</a>
					</c:if>
					<c:if test="${is_personage eq '1' }">
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/detail?id=${messageId}">详情</a>
					</c:if>
				</li>
					<c:if test="${is_personage ne '1' }">
				<li>
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/queryPutListById?id=${messageId}">统计</a>
				</li>
					</c:if>
				<li class="active">
					<a class="flat gray no-margin" href="#tab_top_3" data-toggle="tab">评论</a>
				</li>
				<c:if test="${is_personage ne '1' }">
				<li>
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/likeList?id=${messageId}">点赞</a>
				</li>
				</c:if>
				<c:if test="${is_personage ne '1' }">
				<li>
					<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/feedbackList?id=${messageId}">反馈</a>
				</li>
				</c:if>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="tab_top_1">
					<div class="pad20">
						<div class="clearfix pad-b10 border-bottom">
							<div class="col-sm-8 pad-t5 no-pad-left no-pad-right">共 ${size } 条评论</div>
							<div class="col-sm-4 text-right no-padding">
								<!-- <div class="form-inline">
										<div class="form-group">
											<input type="text" class="form-control input-sm" placeholder="请输入搜索关键字">
										</div>
										<button type="button" class="btn btn-sm btn-default">搜索</button>
									</div> -->
							</div>
						</div>
						<div>
							<!-- <div style="padding: 50px 20px;" class="text-center">
									<i class="fa fa-pencil-square-o" style="font-size: 80px; color: #bdbdbd;"></i>
									<div class="f18">没有发表想法...</div>
								</div> -->

							<ul class="list-unstyled news-list clearfix">
								<c:forEach items="${list }" var="item">
									<li class="news-item question-item">
										<div class="media">
											<div class="media-left">
												<c:if test="${empty item.avatar }">
													<img src="${css }/ouchgzee_com/platform/xlbzr_css/dist/img/images/no-img.png" class="media-object img-circle" alt="User Image">
												</c:if>
												<c:if test="${not empty item.avatar }">
													<img src="${item.avatar }" class="media-object img-circle" alt="User Image">
												</c:if>
											</div>
											<div class="media-body">
												<div class="the-topic-ask">
													<h4 class="media-heading f16">${item.userName }</h4>
													<div class="q-info">
														${item.createdDt }
														${platformMap[item.platform] }
													</div>
													<div class="txt">${item.content }</div>
													<ul class="list-inline img-gallery">
														<li>
															<c:if test="${not empty item.imgUrls }">
																<c:forEach items="${fn:split(item.imgUrls,',') }" var="img">
																	<img data-role="lightbox" src="${img }" role="button" >
																</c:forEach>
															</c:if>
														</li>
													</ul>
													<div class="margin_t10 text-right">
														<a href="#" data-id="${item.id }" data-role="addAsk">回复(${item.detailCount })</a> 
															| 
														<c:if test="${item.isLike eq '0' }">
															<a href="#" data-role="isLike" data-json='{"id":"${item.id}","likecount":"${item.likecount }"}'>
															点赞
															(${item.likecount })
															</a>
													  </c:if>
														<c:if test="${item.isLike eq '1' }">
															已赞
															(${item.likecount })
														  </c:if>	
															<c:if test="${item.userId eq userId}">
																| <a href="#"  data-role="remove-pl" data-json='{"id":"${item.id}","type":"parent"}'>删除</a>
															</c:if>
													</div>
												</div>
												<div class="feedback-box margin_t15">
													<c:forEach items="${item.gjtMessageCommentDetails }" var="comm">
														<div class="box-border feedback-item">
															<div class="clearfix">
																<span class="gray9 pull-right">
																	${comm.createdDt }
																	${platformMap[comm.platform] }
																</span>
																<span class="f16">${comm.userName }</span>
															</div>
															<div class="txt">${comm.content }</div>
															<div class="margin_t10 text-right">
																<c:if test="${comm.isLike eq '0' }">
																	<a href="#" data-role="isLike" data-json='{"id":"${comm.id}","likecount":"${comm.likecount }"}'>
																	点赞
																	(${comm.likecount })
																	</a>
															  </c:if>
																<c:if test="${comm.isLike eq '1' }">
																	已赞
																	(${comm.likecount })
																  </c:if>	
																	<c:if test="${comm.userId eq userId}">
																		| <a href="#" data-role="remove-pl" data-json='{"id":"${comm.id}","type":"son"}'>删除</a>
																	</c:if>
															</div>
														</div>
													</c:forEach>
												</div>
											</div>
										</div>
									</li>
								</c:forEach>
							</ul>
							<form action="${ctx }/admin/messageInfo/addComment" id="addForm" method="post">
								<input type="hidden" value="${messageId}" name="messageId">
								<div class="fb-box margin_t20">
									<div class="form-group">
										<label class="form-control-label f16 text-no-bold">发表讨论</label>
										<textarea class="form-control" rows="5" placeholder="发表一下你的想法..." id="edit" name="content"></textarea>
									</div>
									<div class="media" style="overflow: visible">
										<div class="media-body" style="overflow: visible">
											<div class="upload-img-component">
												<div class="margin_b20">
													<button type="button" class="btn btn-default min-width-90px btn-sm btn-add-img-addon">上传图片</button>
													<span class="f12 gray9 pad-t5 margin_l10"> * 支持jpg/png/gif类型图片，每张不超过5mb大小，支持选择多张 </span>
												</div>
												<ul class="list-inline img-gallery upload-box">
													<%-- <li>
														<img src="http://172.16.170.119:801/ouchgzee_com/platform/xlbzr_css/dist/img/images/no_photo.png">
														<span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" data-html="true" title="" onclick="$(this).parent().remove()"
															data-original-title="<div style='width:40px;'>删除</div>"></span>
														<a class="img-fancybox f12" data-role="img-fancybox"
															href="http://172.16.170.119:801/ouchgzee_com/platform/xlbzr_css/dist/img/images/no_photo.png">点击放大</a>
													</li>
													<li>
														<img src="http://172.16.170.119:801/ouchgzee_com/platform/xlbzr_css/dist/img/images/no_photo.png">
														<span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" data-html="true" title="" onclick="$(this).parent().remove()"
															data-original-title="<div style='width:40px;'>删除</div>"></span>
														<a class="img-fancybox f12" data-role="img-fancybox"
															href="http://172.16.170.119:801/ouchgzee_com/platform/xlbzr_css/dist/img/images/no_photo.png">点击放大</a>
													</li> --%>
												</ul>
											</div>
										</div>
										<div class="media-right">
											<button type="button" class="btn btn-primary btn-lg" id="btnSubmit">发表</button>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- 底部 -->
	<jsp:include page="/eefileupload/upload.jsp" />
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
	//图片上传
	$('body').on('click','.btn-add-img-addon',function() {
		var $container=$(this).parent().next('.upload-box');
		uploadImageNew( $container.get(0) ,'imgUrl');
	});
	/*删除图片*/
	$("body").on('click', '.remove-addon', function(event) {
		event.preventDefault();
		$(this).parent().remove();
	});
	
	$('#btnSubmit').click(function(){
		var edit=$('#edit').val();
		if($.trim(edit)==''){
			alert('请填写内容!');
			return false;
		}
		
		 $('#addForm').submit(); 
		
	});
	
	$('[data-role="isLike"]').click(function(){
		var _this=$(this);
		var obj=_this.data('json');
		$.post('${ctx}/admin/messageInfo/addLike',{id:obj.id},function(data){
			if(data.successful){
				var like =parseInt(obj.likecount)+1;
				_this.replaceWith('<span >已赞('+like+')</span>');
			}else{
				alert(data.message);
			}
		},'json')
	});
	
	$('[data-role="remove-pl"]').click(function() {
		var $obj=$(this).data('json');
		$.post('${ctx}/admin/messageInfo/deleteAsk',{id:$obj.id,type:$obj.type},function(data){
           		if(data.successful){
           			location.reload();
           		}else{
           			alert(data.message);
           		}
           },'json');	
	});
	
	
$('[data-role="addAsk"]').click(function() {
		var $id=$(this).data('id');
	  	$.alertDialog({
		    id:'addAsk',
		    width:450,
		    height:290,
		    zIndex:11000,
		    title:'回复',
		    ok:function(){//“确定”按钮的回调方法
		      //这里 this 指向弹窗对象
		      var _this=this;
		      var $content=$(_this).find('.cause').val();
		      if($.trim($content)==''){
		    	  alert('回复内容不能为空');
		    	  return false;
		      }
		      var dialog1=$.formOperTipsDialog({
		        text:'数据提交中...',
		        iconClass:'fa-refresh fa-spin'
		      });
		      
		      $.post('${ctx}/admin/messageInfo/addAsk',
		    		  { id:$id,content:$content},
		    		  function(data){
	             		if(data.successful){
	             			location.reload();
	             		}else{
	             			alert(data.message);
	             		}
	             },'json');
		    },
		    content:'<div><p>请输入回复内容：</p>'+
		    '<textarea class="form-control cause" placeholder="回复内容不能为空" rows="5">'+
		    '</textarea><div>'
		})
	});
</script>
</body>
</html>

