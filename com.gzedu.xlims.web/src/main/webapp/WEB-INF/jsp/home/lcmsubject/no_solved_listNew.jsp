<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>管理系统 - 答疑服务-未解答列表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
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
				<li class="active">
					<a href="${ctx}/lcmsubject/list?type=N">待解答疑问(${noSolve})</a>
				</li>
				<li>
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
							<form id="listForm" action="${ctx }/lcmsubject/list">
								<input type="hidden" name="type" value="${type}">
								<div class="clearfix faq-list-bar">
									<div class="search-group-box">
										<div class="form-inline">
											<div class="form-group">
												<select class="form-control margin_r5 select2" name="isTimeout">
													<option>全部</option>
													<option value="2" <c:if test="${isTimeout eq '2' }">selected</c:if>>超过2小时未解答</option>
													<option value="24" <c:if test="${isTimeout eq '24' }">selected</c:if>>超过24小时未解答</option>
												</select>
											</div>
											<div class="form-group">
												<input type="text" name="search_LIKE_subjectTitle" placeholder="问题标题"
												value="${param.search_LIKE_subjectTitle}" class="form-control">
											</div>
											<div class="form-group">
												<button type="submit" class="btn btn-default margin_r5">搜索</button>
												<button type="button" class="btn btn-default" data-role="ask-for-student">替学员提问题</button>
											</div>
										</div>
									</div>
									<div class="filter-group-box">
										<div class="btn-group ">
											<input type="hidden" value="${isTranspond }" name="isTranspond"> <a data-id="" data-role=":input[name='isTranspond']"
												class="btn btn-default <c:if test="${empty isTranspond }">active</c:if>"> 全部待解答(${xiangWoTiWenCount+zhuanfaMeCount })</a> <a data-id="N"
												data-role=":input[name='isTranspond']" class="btn btn-default <c:if test="${isTranspond  eq 'N'}">active</c:if>">
												向我提问的(${xiangWoTiWenCount })</a> <a data-id="Y" data-role=":input[name='isTranspond']"
												class="btn btn-default <c:if test="${isTranspond  eq 'Y'}">active</c:if>">转给我的(${zhuanfaMeCount })</a>
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
																	                  		"url":"${info.url }",
																	                  		"orgName":"${info.orgName }",
																	                  		"eeUrl":"${info.eeUrl }",
																	                  		"singUrl":"${info.singUrl }" }' />
																</c:when>
																<c:otherwise>
																	<img src="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="media-object img-circle" alt="User Image"
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
																	<c:if test="${info.isTranspond eq '1'}">
																		<li>计时：距离转发已过${info.timeOutDate }</li>
																	</c:if>
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
																								<img src="${teacherPicture }"  data-role="lightbox" role="button">
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

															<div class="clearfix q-btn-box">
																<button type="button" class="ans-q-control btn min-width-90px btn-primary margin_r10">解答</button>
																<c:if test="${info.askCount==0 }">
																	<c:if test="${ info.isTranspond eq '0' }">
																		<button type="button" class="transfer-to-other-btn btn min-width-90px btn-default" data-role="transfer-to-other"
																			data-json='{"id":"${info.id }",
																						                                "chushiId":"${info.chushiId }",
																						                                "studentId":"${info.studentId }"
																							                            }'>
																			<i class="fa fa-fw fa-share-alt"></i>转给其他人回答
																		</button>
																	</c:if>
																	<c:if test="${ info.isTranspond eq '1' }">
																		<button type="button" class="transfer-to-other-btn btn min-width-90px btn-default" data-role="transfer-to-orient"
																			data-json='{"id":"${info.id }",
																		                                "chushiId":"${info.chushiId }",
																		                                "studentName":"${info.studentXm }",
																		                                "teacherName":"${info.initialUserName }"
																		                                
																			                }'>
																			回答不了，转回原处[${info.initialUserName } ]</button>
																	</c:if>
																</c:if>


															</div>

															<div class="q-form-panel addImages">
																<form class="theform" role="form" action="${ctx }/lcmsubject/askAgain" method="post">
																	<input type="hidden" name="pid" value="${info.id }">
																	<div class="form-group position-relative">
																		<label class="sr-only">回复内容</label>
																		<textarea name="content" class="form-control" rows="5" placeholder="请输入回复内容" datatype="*" nullmsg="请输入回复内容" errormsg="请输入回复内容"></textarea>
																		<div class="tooltip top" role="tooltip">
																			<div class="tooltip-arrow"></div>
																			<div class="tooltip-inner"></div>
																		</div>
																	</div>

																	<div class="margin_b20">
																		<button type="button" class="btn btn-default min-width-90px btn-sm btn-add-img-addon">上传图片</button>
																		<span class="f12 gray9 pad-t5 margin_l10"> * 支持jpg/png/gif类型图片，每张不超过5mb大小，支持选择多张 </span>
																	</div>
																	<ul class="list-inline img-gallery upload-box">

																	</ul>

																	<div class="clearfix q-btn-box">
																		<button type="button" class="btn-sure btn min-width-90px btn-success margin_r10">回复</button>
																		<button type="button" class="cancel-q-btn btn min-width-90px btn-default">取消</button>
																		<span class="checkbox inline-block margin_l10 " data-id="set-normal">
																			<c:if test="${info.isComm eq 'N' }">
																				<label> 
																					<input type="checkbox" name="is_comm" value="Y"> 设为常见问题
																				</label>
																			</c:if>
																		</span>
																		<span class="margin_l10" data-id="rel-normal" style="display: none;">
																			<div class="inline-block position-relative">
																				<select class="form-control select2" name="oftenType" datatype="normalType" nullmsg="请选择分类" errormsg="请选择分类">
																					<option value="">请选择</option>
																					<c:forEach items="${oftenTypeMap }" var="map">
																						<option value="${map.key }">${map.value }</option>
																					</c:forEach>
																				</select>
																			</div>
																			<div class="inline-block position-relative">
																				<input type="text" name="isCommendType" placeholder="常见问题标签" class="form-control "> 
																			</div>
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
										<li>
											<div class="text-center gray9" style="padding: 100px 0;">
												<i class="fa fa-fw fa-exclamation-circle vertical-middle" style="font-size: 40px;"></i>
												<span class="f18 inline-block vertical-middle">暂无学员疑问！</span>
											</div>
										</li>
									</c:otherwise>
								</c:choose>
							</ul>
							<div class="page-container margin_t15 margin_b10 clearfix">
								<tags:pagination page="${pageInfo}" paginationSize="10" />
							</div>
						</div>
					</div>
				</div>
			</div>
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
		var dialog1;
		var $theform=$(".theform");
		var htmlTemp='<div class="tooltip top" role="tooltip">'
		        +'<div class="tooltip-arrow"></div>'
		        +'<div class="tooltip-inner"></div>'
		        +'</div>';
		$theform.find(".position-relative").each(function(index, el) {
		    if( $(this).children('.tooltip').length<=0 ){
		      $(this).append(htmlTemp);
		    }
		});
		
		$.Tipmsg.r='';
		postForm=$theform.Validform({
		  showAllError:true,
		  datatype:{
		    'normalType':function(gets,obj,curform){
		        var $wrap=obj.closest('[data-id="rel-normal"]');
		        if($wrap.is(':hidden')){
		          return true;
		        }
		        if( $.trim(gets)=='' ){
		          return false;
		        }
		        return true;
		    }
		  },
		  tiptype:function(msg,o,cssctl){
		    var msgBox=o.obj.closest('.position-relative').children('.tooltip');
		
		    msgBox.children('.tooltip-inner').text(msg);
		    msgBox.css({
		      top:-23
		    });
		
		    switch(o.type){
		      case 3:
		        msgBox.addClass('in');
		        break;
		      default:
		        msgBox.removeClass('in');
		        break;
		    }
		  },
		  beforeSubmit:function(curform){
		    
		    if(curform.find(".Validform_error").length>0){
		      return false;
		    }
		    
		    dialog1=$.formOperTipsDialog({
		      text:'数据提交中...',
		      iconClass:'fa-refresh fa-spin'
		    });
		  },
		  callback:function(data){
		        if (data.successful) {
		        	dialog1.find('[data-role="tips-text"]').html('数据提交成功...');
		            dialog1.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
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
			    content: 'url:'+ctx+'/lcmsubject/findRole?id='+$id+'&chushiId='+$chushiId+'&studentId='+$studentId
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
	
	//下拉样式
	$('.select2').select2();
	
	//tab
	$(".btn-group a").click(function(event) {
		$(""+$(this).data("role")+"").val($(this).data("id"));
		$("#listForm").submit();
	});
	
//回答不了,转回原处
$('body').on('click','[data-role="transfer-to-orient"]',function(event) {
	var $obj=$(this).data('json');
  	$.alertDialog({
	    id:'transfer-to-orient',
	    width:450,
	    height:290,
	    zIndex:11000,
	    ok:function(){//“确定”按钮的回调方法
	      //这里 this 指向弹窗对象
	      var _this=this;

	      var dialog1=$.formOperTipsDialog({
	        text:'数据提交中...',
	        iconClass:'fa-refresh fa-spin'
	      });
	      var $content=$(_this).find('.cause').val();
	      $.post(ctx+'/lcmsubject/updateInitial',
	    		  {id:$obj.id,
	    	  		chushiId:$obj.chushiId,
	    	  		studentName:$obj.studentName,
	    	  		teacherName:$obj.teacherName,
	    	  		content:$content},
	    		  function(data){
             		dialog1.find('[data-role="tips-text"]').html(data.message);
                	dialog1.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
                   /*关闭弹窗*/
                   setTimeout(function(){
                   		location.reload();
                   },1000)
             },'json');
	    },
	    content:'<div><p>请输入回答不了该问题的原因：</p>'+
	    '<textarea class="form-control cause" placeholder="请输入回答不了的原因，限50字！" rows="5">'+
	    '</textarea><div>'
	})
})//设为常见问题
.on('click', '[data-id="set-normal"] :checkbox', function(event) {
	  var $normalSel=$(this).closest('[data-id="set-normal"]').next('[data-id="rel-normal"]');
	  if( $(this).prop('checked') ){
	    $normalSel.show();
	  }
	  else{
	    $normalSel.hide();
	  }
})
//替学员提问题
.on('click', '[data-role="ask-for-student"]', function(event) {
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
