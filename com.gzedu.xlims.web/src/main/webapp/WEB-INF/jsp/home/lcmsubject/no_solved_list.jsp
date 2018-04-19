<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>管理系统 - 答疑服务</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
			<li class="active">答疑管理</li>
		</ol>
	</section>
	
	<section class="content">
		
		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>>
			<button data-dismiss='alert' class='close'>×</button>
			<i class='icon fa fa-check'></i>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>>
			<button data-dismiss='alert' class='close'>×</button>
			<i class='icon fa fa-warning'></i>${feedback.message}</div>
			
			<div class="box" style="border-top: none">
				<div class="nav-tabs-custom reset-nav-tabs-custom" id="notice-tabs" style="margin-bottom: 0px;">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="${ctx}/lcmsubject/list.html?type=N" data-toggle="tab" aria-expanded="true">未解决<b class="text-red" id="noSolve" >(${noSolve})</b></a>
						</li>
						<li>
							<a href="${ctx}/lcmsubject/list.html?type=Y">已解决(<span class="text-light-blue" id="yesSolve">${yesSolve}</span>)
							</a>
						</li>
						<li>
							<a href="${ctx}/lcmsubject/list.html?type=E">常见问题(<span class="text-light-blue">${oftenCount}</span>)
							</a>
						</li>
					</ul>
					<div class="box-body">
						<div class="form-horizontal">
							<div class="row pad-t15">
								<div class="col-sm-5">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">问题标题</label>
										<div class="col-sm-9">
											<input type="text" id="search_LIKE_subjectTitle" value="${param.search_LIKE_subjectTitle}" class="form-control">
										</div>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label class="control-label col-sm-2 text-nowrap">提问时间</label>
										<div class="col-sm-9">
											<div class="input-group input-daterange">
												<input type="text" id="createDtBegin" value="${param.createDtBegin}" class="form-control " data-role="date-start">
												<span class="input-group-addon nobg">－</span>
												<input type="text" id="createDtEnd" value="${param.createDtEnd}" class="form-control  " data-role="date-end">
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div><!-- /.box-body -->
				</div><!-- nav-tabs-custom -->

				<div class="box-footer">
					<div class="pull-right">
						<button type="reset" class="btn min-width-90px btn-default">重置</button>
					</div>
					<div class="pull-right margin_r15">
						<button type="button" class="btn min-width-90px btn-primary" id="btnSearch">搜索</button>
					</div>
				</div><!-- /.box-footer-->
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
																		onerror="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png'"
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
																	                  		"url":"${info.url }"}'
												                  	/>
																</c:when>
																<c:otherwise>
																	<img src="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png"
																		 class="media-object img-circle" alt="User Image" data-json='{
																	                  		"imgurl":"${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png",
																	                  		"name":"${info.studentXm}",
																	                  		"class":"${info.className}",
																	                  		"stu-no":"${info.xh}",
																	                  		"level":"${info.pycc}",
																	                  		"tel":"${info.tel}",
																	                  		"grade":"${info.grade}",
																	                  		"semester":"${info.semester}",
																	                  		"professional":"${info.specialtyName}",
																	                  		"url":"${info.url }"}'
												                  >
																</c:otherwise>
															</c:choose>
														</div>
														<div class="media-body">
															<div class="the-topic-ask">
																<h4 class="media-heading text-yellow text-bold f16">
																	<span class="label label-warning margin_r10">问</span>${info.title}
																</h4>
																<div class="q-info">${info.studentXm} &nbsp; &nbsp; ${info.createDt} &nbsp; &nbsp; 提问</div>
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
	
															<div class="clearfix q-btn-box">
																<shiro:hasPermission name="/lcmsubject/list.html?type=N$answer">
																	<button type="button" class="ans-q-control btn min-width-90px btn-primary margin_r10">解答</button>
																</shiro:hasPermission>
																<shiro:hasPermission name="/lcmsubject/list.html?type=N$beGivenToOther">
																<c:if test="${info.askCount==0  }">
																	<button type="button" class="transfer-to-other-btn btn min-width-90px btn-default"  
																		data-role="transfer-to-other" data-json='{"id":"${info.id }",
																														                                "chushiId":"${info.chushiId }",
																														                                "studentId":"${info.studentId }"
																															                            }'>
																	<i class="fa fa-fw fa-share-alt"></i>转给其他人回答</button>
																</c:if>
																</shiro:hasPermission>
															</div>
															<div class="q-form-panel ">
																<form class="theform" role="form" action="askAgain" method="post">
																	<input type="hidden" name="pid" value="${info.id }">
																	<div class="form-group position-relative">
																		<label class="sr-only">回复内容</label>
																		<textarea name="content" class="form-control" rows="5" placeholder="请输入回复内容" datatype="*" nullmsg="请输入回复内容" errormsg="请输入回复内容"></textarea>
																		<div class="tooltip top" role="tooltip">
																			<div class="tooltip-arrow"></div>
																			<div class="tooltip-inner"></div>
																		</div>
																	</div>
																	<c:if test="${info.askCount<1  }">
																	<div class="margin_b20">
												                        <button type="button" role="button" class="btn min-width-90px btn-default btn-add-img-addon">上传图片</button>
												                        <span class="f12 gray9 pad-t5 margin_l10">
												                          * 支持jpg/png/gif类型图片，每张不超过5mb大小，支持选择多张
												                        </span>
												                      </div>
																	<ul class="list-inline img-gallery upload-box">
	
																	</ul>
																	</c:if>
																	<div class="clearfix q-btn-box">
																		<button type="button" class="btn-sure btn min-width-90px btn-success margin_r10">回复</button>
																		<button type="button" class="cancel-q-btn btn min-width-90px btn-default">取消</button>
																		<span class="checkbox inline-block margin_l10">
																			<c:if test="${info.isComm eq 'N' }">
																				<label> <input type="checkbox" name="is_comm" value="Y"> 设为常见问题
																				</label>
																			</c:if>
																		</span>
																	</div>
																</form>
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
							<form class="form-horizontal" id="listForm">
								<div style="padding: 10px 0 10px 0;">
									<input type="hidden" name="type" value="${param.type}" >
									<input type="hidden" name="search_LIKE_subjectTitle" id="subjectTitle" value="${param.search_LIKE_subjectTitle}" >
									<input type="hidden" name="createDtBegin" id="createDtBegin1" value="${param.createDtBegin}" >
									<input type="hidden" name="createDtEnd" id="createDtEnd1" value="${param.createDtEnd}">
									<tags:pagination page="${pageInfo}" paginationSize="10" />
								</div>
							</form>
						</div><!-- /.tab-pane -->
					</div><!-- /.tab-content -->
				</div><!-- nav-tabs-custom -->
			</div>
		
	</section>
	<jsp:include page="/eefileupload/upload.jsp" /> 
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	
	
<!--弹窗模板-->
<script type="text/template" id="temp">
	<div class="popover bottom" style="max-width: 450px;display:block;">
		<div class="arrow" style="left: 50px"></div>
		<div class="popover-content">
			<div class="media">
				<div class="media-left">
					<img src="{0}" class="img-circle" width="60" height="60">
				</div>
				<div class="media-body">
					<dl class="margin-bottom-none">
						<dt class="text-no-bold f16">{1}</dt>
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
		<div class="panel-footer no-padding">
			<a href="{9}" target="_blank" class="btn btn-default btn-block btn-flat no-border">
				模拟登录学习空间
			</button>
		</div>
	</div>
</script>
	<script type="text/javascript">
		//因为form里面嵌套form有问题
		$('#btnSearch').click(function(){
			$('#subjectTitle').val($('#search_LIKE_subjectTitle').val());
			$('#createDtBegin1').val($('#createDtBegin').val());
			$('#createDtEnd1').val($('#createDtEnd').val());
			$('#page').val(1);
			
			$('#listForm').submit();
		});
	
	
		//新的图片上传
		$('body').on('click','.btn-add-img-addon',function() {
			var $container=$(this).parent().next('.upload-box');
			uploadImageNew( $container.get(0) ,'imgUrl');
		});

		/*日期控件*/
		$('[data-role="date-group"]').each(function(i, e) {
			var startDate = $('[data-role="date-start"]', e);
			var endDate = $('[data-role="date-end"]', e);
			//开始时间      
			startDate.datepicker({
				language : 'zh-CN',
				format : 'yyyy-mm-dd'
			}).on('changeDate', function(e) {
				var add = increaseOnedate(e.target.value);
				endDate.datepicker('setStartDate', add);
			});
			//结束时间
			endDate.datepicker({
				language : 'zh-CN',
				format : 'yyyy-mm-dd'
			}).on('changeDate', function(e) {
				var d = decreaseOnedate(e.target.value);
				startDate.datepicker('setEndDate', d);
			}).on('focus', function() {
				if (this.value == "" && startDate.val() == "") {
					startDate.focus();
					endDate.datepicker('hide');
				}
			});
		});

		/*删除图片*/
		$("body").on('click', '.remove-addon', function(event) {
			event.preventDefault();
			$(this).parent().remove();
		});

		/*点击解答*/
		$(".news-list").on('click', '.ans-q-control', function(event) {
		  event.preventDefault();
		  $(this).parent().hide().siblings('.q-form-panel').show();
		})
		/*取消解答*/
		.on('click', '.cancel-q-btn', function(event) {
		  event.preventDefault();
		  $(this).closest('.q-form-panel').hide().siblings('.q-btn-box').show();
		  var index=$(".cancel-q-btn").index(this);
		  postForm.eq(index).resetForm();
		  $(".theform").eq(index).find(".tooltip").removeClass('in');
		});


		/*确认发送*/
		var dialog1, dialog2;
		var $theform = $(".theform");
		$.Tipmsg.r = '';
		var postForm = $theform.Validform({
		    showAllError: true,
		    tiptype: function(msg, o, cssctl) {
		        var msgBox = o.obj.closest('.position-relative').children('.tooltip');
		        msgBox.children('.tooltip-inner').text(msg);
		        msgBox.css({
		            top: -23
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
		    beforeSubmit: function(curform) {
		        if (curform.find(".Validform_error").length > 0) {
		            return false;
		        }
		        dialog1 = $.dialog({
		            title: false,
		            opacity: 0,
		            contentHeight: 20,
		            closeIcon: false,
		            content: '<div class="text-center">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
		            columnClass: 'col-xs-4 col-xs-offset-4'
		        });
		    },
		    callback: function(data) {
		        dialog1.close();
		        if (data.successful) {
		            showSueccess(data);
		            $('.news-item').eq(index).hide('fast');
		            
		            setTimeout(function(){
		            	window.location.href=window.location.href;
		              },2000)
		        } else {
		            $.alert({
		                title: '失败',
		                icon: 'fa fa-exclamation-circle',
		                confirmButtonClass: 'btn-primary',
		                content: '回复失败！',
		                confirmButton: '确认',
		                confirm: function() {
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

		
		// 转给其他人回答
		$('[data-role="transfer-to-other"]').click(function(event) {
		var $obj=$(this).data('json');
		var $id=$obj.id;
		var $chushiId=$obj.chushiId;
		var $studentId=$obj.studentId;
		  $.mydialog({
		    id:'transfer',
		    width:900,
		    height:700,
		    zIndex:11000,
		    content: 'url:findRole.html?id='+$id+'&chushiId='+$chushiId+'&studentId='+$studentId
		  });
		});

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
	  		json['url']
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


	</script>
</body>
</html>
