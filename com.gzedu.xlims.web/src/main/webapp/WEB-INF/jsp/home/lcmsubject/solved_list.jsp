<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li>
				<a href="${ctx}/home/class/index"> <i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li class="active">答疑管理</li>
		</ol>
	</section>
	<section class="content">
		<form class="form-horizontal" id="listForm">
			<div class="box" style="border-top: none">
				<div class="nav-tabs-custom reset-nav-tabs-custom" id="notice-tabs" style="margin-bottom: 0px;">
					<ul class="nav nav-tabs">
						<li>
							<a href="${ctx}/lcmsubject/list.html?type=N"> 未解决<span class="text-light-blue">(${noSolve})</span>
							</a>
						</li>
						<li class="active">
							<a href="${ctx}/lcmsubject/list.html?type=Y" data-toggle="tab" aria-expanded="true"> 已解决 <b class="text-red">(${yesSolve})</b>
							</a>
						</li>
						<li>
							<a href="${ctx}/lcmsubject/list.html?type=E"> 常见问题( <span class="text-light-blue">${oftenCount}</span> )
							</a>
						</li>
					</ul>
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-sm-5">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">问题标题</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_subjectTitle" value="${param.search_LIKE_subjectTitle}" class="form-control">
										<input type="hidden" name="type" value="${param.type}" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="control-label col-sm-2 text-nowrap">提问时间</label>
									<div class="col-sm-9">
										<div class="input-group input-daterange">
											<input type="text" name="createDtBegin" value="${param.createDtBegin}" class="form-control " data-role="date-start">
											<span class="input-group-addon nobg">－</span>
											<input type="text" name="createDtEnd" value="${param.createDtEnd}" class="form-control  " data-role="date-end">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- /.box-body -->
				</div>
				<!-- nav-tabs-custom -->

				<div class="box-footer">
					<div class="pull-right">
						<button type="reset" class="btn min-width-90px btn-default">重置</button>
					</div>
					<div class="pull-right margin_r15">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
				<!-- /.box-footer-->
			</div>
		
			<div class="box no-border margin-bottom-none">
				<div class="nav-tabs-custom reset-nav-tabs-custom">
					<div class="tab-content">
						<div class="tab-pane active" id="tab_notice_1">
							<ul class="list-unstyled news-list">
								<c:choose>
									<c:when test="${not empty pageInfo && pageInfo.numberOfElements > 0}">
										<c:forEach items="${pageInfo.content}" var="info">
											<c:if test="${not empty info}">
												<li class="news-item question-item">
													<div class="media">
														<div class="media-left">
															<c:choose>
																<c:when test="${not empty info.avatar}">
																	<img src="${info.avatar}" class="media-object img-circle" alt="User Image"
																		onerror="this.src='${ctx }/static/images/headImg04.png'">
																</c:when>
																<c:otherwise>
																	<img src="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="media-object img-circle" alt="User Image">
																</c:otherwise>
															</c:choose>
														</div>
														<div class="media-body">
															<div class="the-topic-ask">
																<h4 class="media-heading text-yellow text-bold f16">
																	<span class="label label-warning margin_r10">问</span>${info.title}
																	<c:if test="${info.isComm eq 'Y' }">
																		<small class="label label-danger margin_l10 f12 flat">常见问题</small>
																	</c:if>
																</h4>
																<div class="q-info">${info.studentXm}&nbsp;&nbsp;${info.createDt}&nbsp;&nbsp;提问</div>
																<div class="txt">${info.content}</div>
																<c:if test="${not empty info.imgUrls }">
																	<ul class="list-inline img-gallery">
																		<c:forEach items="${info.imgUrls }" var="item">
																			<li>
																				<a href="${item }"  data-role="img-fancybox"><img src="${item }" class="light-box"></a>
																			</li>
																		</c:forEach>
																	</ul>
																</c:if>
															</div>
	
															<c:if test="${not empty info.replyList }">
																<c:forEach items="${info.replyList }" var="item" varStatus="var">
																	<c:choose>
																		<c:when test="${var.first}">
																			<div class="the-topic-ans margin_t20">
																				<div>
																					<span class="label label-primary margin_r10">答</span>
																					<span>${item.teacherName }</span>
																					<span class="gray9"> ${item.teacherType }&nbsp; &nbsp; ${item.createDt} </span>
																				</div>
																				<div class="txt">${item.content }</div>
																				<c:if test="${not empty item.imgUrls }">
																					<ul class="list-inline img-gallery">
																						<c:forEach items="${item.imgUrls }" var="teacherPicture">
																							<li>
																								<a href="${teacherPicture }"  data-role="img-fancybox"><img src="${teacherPicture }" class="light-box"></a>
																							</li>
																						</c:forEach>
																					</ul>
																				</c:if>
																			</div>
																		</c:when>
																		<c:otherwise>
																			<div class="panel panel-default margin_t15">
																				<div class="panel-body">
																					<c:if test="${item.type eq 'ask' }">
																						<div class="text-red">追问：</div>
																					</c:if>
																					<c:if test="${item.type eq 'reply' }">
																						<div class="text-green">回答：</div>
																					</c:if>
																					<div class="margin_t5">${item.content }</div>
																					<div class="gray9 margin_t10">
																						<small>${item.teacherName } ${item.studentXm } &nbsp; &nbsp;${item.createDt} </small>
																					</div>
																				</div>
																			</div>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</c:if>
															<div class="margin_t15">
																<shiro:hasPermission name="/lcmsubject/list.html?type=N$settingsOften">
																<c:if test="${info.isComm eq 'N' }">
																	<button type="button" class="btn min-width-90px btn-default" data-role="add-faq" data-id='${info.id }'>设为常见问题</button>
																</c:if>
																</shiro:hasPermission>
																
																<shiro:hasPermission name="/lcmsubject/list.html?type=N$cancelSettings">
																<c:if test="${info.isComm eq 'Y' }">
																	<button type="button" class="btn min-width-90px btn-default" data-role="cancel-faq" data-id='${info.id }'>取消设置常见问题</button>
																	<input type="hidden" value="${info.id }">
																</c:if>
																</shiro:hasPermission>
																
															</div>
														</div>
													</div>
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
								<tags:pagination page="${pageInfo}" paginationSize="10" />
							</div>
						</div><!-- /.tab-pane -->
					</div><!-- /.tab-content -->
				</div><!-- nav-tabs-custom -->
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		/*确认发送*/
		var dialog1, dialog2;
		var $theform = $(".theform");
		$.Tipmsg.r = '';
		var postForm = $theform.Validform({
					showAllError : true,
					tiptype : function(msg, o, cssctl) {
						var msgBox = o.obj.closest('.position-relative').children('.tooltip');
						msgBox.children('.tooltip-inner').text(msg);
						msgBox.css({
							top : -23
						});
						switch (o.type) {
						case 3:
							msgBox.addClass('in');
							break;
						default:
							msgBox.removeClass('in');
							break;
						}
					},
					beforeSubmit : function(curform) {
						if (curform.find(".Validform_error").length > 0) {
							return false;
						}
						dialog1 = $
								.dialog({
									title : false,
									opacity : 0,
									contentHeight : 20,
									closeIcon : false,
									content : '<div class="text-center">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
									columnClass : 'col-xs-4 col-xs-offset-4'
								});
					},
					callback : function(data) {
						dialog1.close();
						if (data.successful) {
							showSueccess(data);
							$('.news-item').eq(index).hide('fast');
						} else {
							$.alert({
								title : '失败',
								icon : 'fa fa-exclamation-circle',
								confirmButtonClass : 'btn-primary',
								content : '回复失败！',
								confirmButton : '确认',
								confirm : function() {
									showFail(data);
								}
							});
						}
					}
				});

		$(".theform").click(function(event) {
			$(".theform").removeClass('on')
			if (!$(this).hasClass('on')) {
				$(this).addClass('on')
			}
		});
		var index; // 标记提交的是哪个表单
		$(".btn-sure").click(function(event) {
			/*插入业务逻辑*/
			index = $(".btn-sure").index(this);
			postForm.eq(index).ajaxPost();
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
		
		//取消设置常见问题
		$('[data-role="cancel-faq"]').click(function(event) {
		  var $id=$(this).data('id');
		  console.log($id);
		  $.alertDialog({
		    id:'cancel-faq',
		        width:400,
		        height:280,
		        zIndex:11000,
		        ok:function(){//“确定”按钮的回调方法
		          //这里 this 指向弹窗对象
		          $.get('setOften.html',{id:$id,state:'N'},function(data){
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

		
		//设置常见问题
		$('[data-role="add-faq"]').click(function(event) {
		  var $id=$(this).data('id');
		  console.log($id);
		  $.alertDialog({
		    id:'cancel-faq',
		        width:400,
		        height:280,
		        zIndex:11000,
		        ok:function(){//“确定”按钮的回调方法
		          //这里 this 指向弹窗对象
		          $.get('setOften.html',{id:$id,state:'Y'},function(data){
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
		                '是否设置该常见问题？', '</span>', '</div>'].join('')
		  })
		});
		
		
		
	</script>
</body>
</html>
