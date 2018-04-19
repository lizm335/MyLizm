<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>管理系统 - 答疑服务-已解答列表</title>
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
		<div class="nav-tabs-custom no-shadow margin-bottom-none">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li>
					<a href="${ctx}/lcmsubject/list?type=N">待解答疑问(${noSolve})</a>
				</li>
				<li class="active">
					<a href="${ctx}/lcmsubject/list?type=Y">已解答疑问(${yesSolve})</a>
				</li>
				<li>
					<a href="${ctx}/lcmsubject/list?type=F">转出去的答疑(${forwardCount})</a>
				</li>
				<li>
					<a href="${ctx}/lcmsubject/list?type=E">常见问题(${oftenCount})</a>
				</li>
			</ul>

			<div class="tab-content">
				<div class="tab-pane active" id="tab_1">
					<div class="box box-border no-shadow margin-bottom-none">
						<div class="box-body pad-t5">
							<form  id="listForm" action="${ctx}/lcmsubject/list">
								<input type="hidden" name="type" value="${type}">
								<div class="clearfix faq-list-bar">
									<div class="search-group-box">
										<div class="form-inline">
											<div class="form-group">
												<input type="text" name="search_LIKE_subjectTitle" placeholder="问题标题"
												value="${param.search_LIKE_subjectTitle}" class="form-control margin_r5">
											</div>
											<div class="form-group">
												<button type="submit" class="btn btn-default margin_r5">搜索</button>
												<button type="button" class="btn btn-default" data-role="ask-for-student">替学员提问题</button>
											</div>
										</div>
									</div>
									<div class="filter-group-box">
										<div class="btn-group ">
											<input type="hidden" value="${isSolve }" name="isSolve"> <a data-id="" data-role=":input[name='isSolve']"
												class="btn btn-default <c:if test="${empty isSolve }">active</c:if>"> 全部已解答(${noAffirmSolve+yesAffirmSolve}) </a> <a data-id="0"
												data-role=":input[name='isSolve']" class="btn btn-default <c:if test="${isSolve  eq '0'}">active</c:if>"> 未确认解决的(${noAffirmSolve }) </a> <a
												data-id="1" data-role=":input[name='isSolve']" class="btn btn-default <c:if test="${isSolve  eq '1'}">active</c:if>">已确认解决
												(${yesAffirmSolve }) </a>
										</div>
									</div>
								</div>
							</form>
							<ul class="list-unstyled news-list clearfix">
								<c:choose>
									<c:when test="${not empty pageInfo && pageInfo.numberOfElements > 0}">
										<c:forEach items="${pageInfo.content}" var="info">
											<c:if test="${not empty info}">
												<li class="news-item question-item">
													<div class="media">
														<div class="media-left">
															<c:choose>
																<c:when test="${not empty info.avatar}">
																	<img src="${info.avatar}" class="media-object img-circle" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'"
																		data-json='{
																	                  		"imgurl":"${info.avatar}",
																	                  		"name":"${info.studentXm}",
																	                  		"class":"${info.className}",
																	                  		"stu-no":"${info.xh}",
																	                  		"level":"${info.pycc}",
																	                  		"tel":"${info.tel}",
																	                  		"grade":"${info.grade}",
																	                  		"semester":"${info.semester}",
																	                  		"professional":"${info.specialtyName}",
																	                  		"url":"${info.url }",
																	                  		 "orgName":"${info.orgName }",
																	                  		 "eeUrl":"${info.eeUrl }",
																	                  		"singUrl":"${info.singUrl }"  }' />
																</c:when>
																<c:otherwise>
																	<img src="${ctx }/static/images/headImg04.png" class="media-object img-circle" alt="User Image"
																		data-json='{
																	                  		"imgurl":"${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png",
																	                  		"name":"${info.studentXm}",
																	                  		"class":"${info.className}",
																	                  		"stu-no":"${info.xh}",
																	                  		"level":"${info.pycc}",
																	                  		"tel":"${info.tel}",
																	                  		"grade":"${info.grade}",
																	                  		"semester":"${info.semester}",
																	                  		"professional":"${info.specialtyName}",
																	                  		"url":"${info.url }",
																	                  		 "orgName":"${info.orgName }",
																	                  		 "eeUrl":"${info.eeUrl }",
																	                  		"singUrl":"${info.singUrl }"  }' />
																</c:otherwise>
															</c:choose>
														</div>
														<div class="media-body">
															<div class="the-topic-ask">
																<h4 class="media-heading text-yellow text-bold f16">
																	<c:if test="${info.isTranspond eq '0'}">
																		<span class="label label-warning margin_r10">提问</span>
																	</c:if>
																	<c:if test="${info.isTranspond eq '1'}">
																		<span class="label label-info margin_r10">转问</span>
																	</c:if>
																	${info.title}
																	<c:if test="${info.isComm eq 'Y' }">
																		<small class="label label-danger margin_l10 f12 flat">常见问题</small>
																	</c:if>
																	<c:if test="${info.isSolve eq '1' }">
																		<small class="label bg-green margin_l10 f12 flat">已确认解决</small>
																	</c:if>
																	<c:if test="${info.isSolve eq '0' }">
																		<small class="label bg-yellow margin_l10 f12 flat">未确认解决</small>
																	</c:if>
																</h4>
																<c:if test="${info.isTranspond eq '0'}">
																	<div class="q-info">${info.studentXm}&nbsp;&nbsp;${info.createDt}&nbsp;&nbsp;
																		向
																		<span class="text-blue">${info.forwardUserName } </span>
																		提问
																	</div>
																</c:if>
																<c:if test="${info.isTranspond eq '1'}">
																	<div class="q-info">${info.studentXm}&nbsp;&nbsp;${info.createDt}&nbsp;&nbsp;
																		向
																		<span class="text-blue"> ${info.initialUserName } </span>
																		&nbsp;提问 ，转给 &nbsp;
																		<span class="text-blue">${info.forwardUserName }</span>
																		&nbsp;回答
																	</div>
																</c:if>
																<div class="txt">${info.content}</div>
																<c:if test="${not empty info.imgUrls }">
																	<ul class="list-inline img-gallery">
																		<c:forEach items="${info.imgUrls }" var="item">
																			<li>
																				<img src="${item }"   data-role="lightbox" role="button">
																			</li>
																		</c:forEach>
																	</ul>
																</c:if>
																<ul class="list-unstyled margin_t10 f12 gray9">
																	<li>来自：${info.questionSourcePath }</li>
																</ul>
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
																								<img src="${teacherPicture }"   data-role="lightbox" role="button">
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
																					<c:if test="${not empty item.imgUrls }">
																						<ul class="list-inline img-gallery">
																							<c:forEach items="${item.imgUrls }" var="picture">
																								<li>
																									<img src="${picture }"   data-role="lightbox" role="button">
																								</li>
																							</c:forEach>
																						</ul>
																					</c:if>
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
																<c:if test="${info.isComm eq 'N' }">
																	<button type="button" class="btn min-width-90px btn-default" data-role="add-faq" data-id='${info.id }'>设为常见问题</button>
																</c:if>
																<c:if test="${info.isComm eq 'Y' }">
																	<button type="button" class="btn min-width-90px btn-default" data-role="cancel-faq" data-id='${info.id }'>取消设置常见问题</button>
																</c:if>
															</div>
														</div>
													</div>
												</li>
											</c:if>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<li>
											<div class="text-center gray9" style="padding: 100px 0;">
												<i class="fa fa-fw fa-exclamation-circle vertical-middle" style="font-size: 40px;"></i>
												<span class="f18 inline-block vertical-middle">暂无学员疑问！</span>
											</div>
										</li>
									</c:otherwise>
								</c:choose>
							</ul>
							<div style="padding: 10px 0 10px 0;">
								<tags:pagination page="${pageInfo}" paginationSize="10" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!--弹窗模板-->
<script type="text/template" id="temp">
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
		//提示框
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
		  $.alertDialog({
		    id:'cancel-faq',
		        width:400,
		        height:280,
		        zIndex:11000,
		        ok:function(){//“确定”按钮的回调方法
		          //这里 this 指向弹窗对象
		          $.post('setOften.html',{id:$id,state:'N'},function(data){
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
			  $.mydialog({
			        id:'cancel-faq',
			        width:400,
			        height:350,
			        zIndex:11000,
			        content: 'url:'+ctx+'/lcmsubject/settingOften?subjectId='+$id
		      });
		});
		
		//tab
		$(".btn-group a").click(function(event) {
			$(""+$(this).data("role")+"").val($(this).data("id"));
			$("#listForm").submit();
		});
		
		//替学员提问题
		$('[data-role="ask-for-student"]').click(function(event) {
		  $.mydialog({
		    id:'add-faq',
		    width:850,
		    height:($(window).height()<600?$(window).height()*0.95:600),
		    zIndex:11000,
		    content: 'url:'+ctx+'/lcmsubject/add'
		  });
		});

	</script>
</body>
</html>
