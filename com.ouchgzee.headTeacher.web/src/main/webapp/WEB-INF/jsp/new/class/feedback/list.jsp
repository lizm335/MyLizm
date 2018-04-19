<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 答疑管理</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
	<style type="text/css">
		.teacher {width: 100%;margin-bottom: 10px;}
	</style>
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		答疑管理
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">答疑管理</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

	<form class="form-horizontal">
	<div class="box" style="border-top:none">
		<div class="nav-tabs-custom reset-nav-tabs-custom" id="notice-tabs" style="margin-bottom: 0px;">
			<ul class="nav nav-tabs">
				<li class="active"><a href="${ctx}/home/class/feedback/list" data-toggle="tab">未解决<b class="text-red">(${umsCount})</b></a></li>
				<li><a href="${ctx}/home/class/feedback/solvedList">已解决(<span class="text-light-blue">${sCount}</span>)</a></li>
			</ul>
			<div class="box-body">
					<div class="row pad-t15">
						<div class="col-sm-5">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">问题标题</label>
								<div class="col-sm-9">
									<input type="text" name="search_title" value="${param.search_title}" class="form-control">
								</div>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label class="control-label col-sm-2 text-nowrap">提问时间</label>
								<div class="col-sm-9">
									<div class="input-group">
										<input type="text" name="search_createdDtBegin" value="${param.search_createdDtBegin}" class="form-control pull-right reservation">
										<div class="input-group-addon no-border">
											至
										</div>
										<input type="text" name="search_createdDtEnd" value="${param.search_createdDtEnd}" class="form-control pull-right reservation">
									</div>
								</div>
							</div>
						</div>
					</div>
			</div><!-- /.box-body -->
		</div><!-- nav-tabs-custom -->

		<div class="box-footer">
			<div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
			<div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
		</div><!-- /.box-footer-->
	</div>
	</form>
	<div class="box no-border margin-bottom-none">
		<div class="nav-tabs-custom reset-nav-tabs-custom">
			<div class="tab-content">
				<div class="tab-pane active" id="tab_notice_1">
					<ul class="list-unstyled news-list">
					<c:choose>
						<c:when test="${not empty infos && infos.numberOfElements > 0}">
							<c:forEach items="${infos.content}" var="info">
								<c:if test="${not empty info}">
								<li class="news-item question-item">
									<div class="media">
										<div class="media-left">
											<c:choose>
												<c:when test="${not empty info.avatar}">
													<img src="${info.avatar}" class="media-object img-circle" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'">
												</c:when>
												<c:otherwise>
													<img src="${ctx }/static/images/headImg04.png" class="media-object img-circle" alt="User Image">
												</c:otherwise>
											</c:choose>
										</div>
										<div class="media-body">
											<div class="the-topic-ask">
												<h4 class="media-heading text-yellow text-bold f16">
													<span class="label label-warning margin_r10">问</span>${info.title}
												</h4>
												<div class="q-info">
													${info.studentXm} <fmt:formatDate value="${info.createdDt}" type="both" /> 提问
												</div>
												<div class="txt">
													${info.content}
												</div>
												<c:if test="${not empty info.pictures }">
												<ul class="list-inline img-gallery">
													<c:forEach items="${info.pictures }" var="item">
														<li>
															<img src="${item }" >
														</li>
													</c:forEach>
												</ul>
												</c:if>
											</div>
											
											
											
										<c:if test="${not empty info.gjtFeedbackList }">
											<c:forEach items="${info.gjtFeedbackList }" var="item">
												<c:choose>
												<c:when test="${item.type eq 'reply'}">
													  <div class="the-topic-ans margin_t20">
									                    <div>
									                      <span class="label label-primary margin_r10">答</span>
									                      <span>${item.answerEmployeeXm }</span>
									                      <span class="gray9">
									                        ${item.employeeType }&nbsp; &nbsp; <fmt:formatDate value="${item.createdDt}" type="both" /> 
									                      </span>
									                    </div>
									                    <div class="txt">
									                      	${item.content }
									                    </div>
									                   <c:if test="${not empty item.teacherPictures }">
															<ul class="list-inline img-gallery">
																<c:forEach items="${item.teacherPictures }" var="teacherPicture">
																	<li>
																		<img src="${teacherPicture }" >
																	</li>
																</c:forEach>
															</ul>
														</c:if>
								                	 </div>	
												</c:when>
												<c:otherwise>
													<div class="panel panel-default margin_t15">
														<div class="panel-body">
															<div class="text-red">追问：</div>
															<div class="margin_t5">
															  ${item.content }
															</div>
															<div class="gray9 margin_t10">
															  <small>${item.studentXm } &nbsp; &nbsp;<fmt:formatDate value="${item.createdDt}" type="both" /> </small>
															</div>
														</div>
													</div>
												</c:otherwise>
											</c:choose>
											</c:forEach>
						             	 </c:if>
											
											<div class="clearfix q-btn-box">
												<button type="button" class="ans-q-control btn min-width-90px btn-primary margin_r10">解答</button>
												<c:choose>
													<c:when test="${empty info.shareEmployeeId}">
														<button type="button" class="shareTeacher transfer-to-other-btn btn min-width-90px btn-default" val="${info.id}"><i class="fa fa-fw fa-share-alt"></i> 转发辅导老师</button>
													</c:when>
													<c:otherwise>
														<span class="operion-view"><i class="fa fa-fw fa-share-alt"></i> 已转发给${info.shareEmployeeXm}老师</span>
													</c:otherwise>
												</c:choose>
											</div>
											<div class="q-form-panel addImages">
												<form class="theform" role="form" action="${ctx }/home/class/feedback/answer" method="post">
													<input type="hidden" name="id" value="${info.id }">
													<div class="form-group position-relative">
														<label class="sr-only">回复内容</label>
														<textarea name="answerContent" class="form-control" rows="5" placeholder="请输入回复内容" datatype="*" nullmsg="请输入回复内容" errormsg="请输入回复内容"></textarea>
														<div class="tooltip top" role="tooltip">
															<div class="tooltip-arrow"></div>
															<div class="tooltip-inner"></div>
														</div>
													</div>
													<div class="margin_b20">
														<button type="button" class="btn btn-default min-width-90px btn-sm btn-add-img-addon">添加图片附件</button>
													</div>
													<ul class="list-inline img-gallery upload-box">
														<%-- <li>
															<img  src="${ctx}/static/dist/img/images/no_photo.png"  id="imgSrc">
															 <input type="hidden" id="imgs" name="imgUrl" value=""/>
															<span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" data-html="true" title="<div style='width:40px;'>删除</div>"></span>
														</li> --%>
														
													</ul>
													<div class="clearfix q-btn-box">
														<button type="button" class="btn-sure btn min-width-90px btn-success margin_r10">回复</button>
														<button type="button" class="cancel-q-btn btn min-width-90px btn-default">取消</button>
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

					<div style="padding: 10px 0 10px 0;">
						<tags:pagination page="${infos}" paginationSize="10" />
					</div>
				</div><!-- /.tab-pane -->
			</div><!-- /.tab-content -->
		</div><!-- nav-tabs-custom -->
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<div id="shareModal" class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title">转发辅导老师</h4>
			</div>
			<div class="modal-body" style="height: 500px; overflow: auto;">
				<c:choose>
					<c:when test="${fn:length(teacherList) > 0}">
						<c:forEach var="info" items="${teacherList}">
							<a href="javascript:void(0);" val="${info.employeeId}" class="teacher btn btn-default btn-flat">${info.xm} - ${info.bjmc}</a>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<p>该学校没有辅导老师</p>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default pull-left" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary">确认</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
 <jsp:include page="/eefileupload/upload.jsp"/> 
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>


<script type="text/template" id="temp">
<li>
	<div class="upload-image-box position-relative">
	  <div class="upload-image-container">
		<div class="table-cell-block">
		  <img src="${ctx}/static/dist/img/images/no_photo.png" id="imgSrc-#id#">
		  <input type="hidden" id="imgs-#id#" name="imgUrl" value=""/>
		</div>
	  </div>
	  <div class="btn btn-flat btn-sm btn-block btn-default upload-image-btn">
		点击添加
		<input type="button" class="btn-file"  nullmsg="请添加活动图！" errormsg="请添加活动图！"
			onclick="javascript:uploadImage('imgSrc-#id#','imgs-#id#',null, null, uploadCallback);">
	  </div>
	  <div class="tooltip top" role="tooltip">
		<div class="tooltip-arrow"></div>
		<div class="tooltip-inner"></div>
	  </div>
	  <div class="remove-addon">
		<span data-toggle="tooltip" title="删除">
		  <i class="fa fa-fw fa-close"></i>
		</span>
	  </div>
	</div>
</li>
</script>
<script type="text/javascript">
function uploadCallback(){
	
}

var $addDiv=50;
/*添加多图*/
//$(".btn-add-img-addon").click(function(event) {
//	$addDiv++;
//var html=$("#temp").html();
//	html=html.replace(/#id#/g,$addDiv);
 // $(".upload-box").append(html);

/*   onclick="javascript:uploadImage('imgSrc','imgs',null, null, uploadCallback) */

//});

$(".addImages").each(function(e) {
	var $pDiv=$(this);
	$pDiv.find('.btn-add-img-addon').click(function(){
		$addDiv++;
		var html=$("#temp").html();
		html=html.replace(/#id#/g,$addDiv);
		$pDiv.find(".upload-box").append(html);
	})
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
	var dialog1,dialog2;
	var $theform=$(".theform");
	$.Tipmsg.r='';
	var postForm=$theform.Validform({
		showAllError:true,
		tiptype:function(msg,o,cssctl){
			//msg：提示信息;
			//o:{obj:*,type:*,curform:*},
			//obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
			//type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态,
			//curform为当前form对象;
			//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
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
			dialog1=$.dialog({
				title: false,
				opacity: 0,
				contentHeight:20,
				closeIcon:false,
				content: '<div class="text-center">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
				columnClass:'col-xs-4 col-xs-offset-4'
			});
		},
		callback:function(data){
			//返回数据data是json对象，{"info":"demo info","status":"y"}
			//info: 输出提示信息;
			//status: 返回提交数据的状态,是否提交成功。如可以用"y"表示提交成功，"n"表示提交失败，在ajax_post.php文件返回数据里自定字符，主要用在callback函数里根据该值执行相应的回调操作;
			//你也可以在ajax_post.php文件返回更多信息在这里获取，进行相应操作；
			//ajax遇到服务端错误时也会执行回调，这时的data是{ status:**, statusText:**, readyState:**, responseText:** }；

			//这里执行回调操作;
			//注意：如果不是ajax方式提交表单，传入callback，这时data参数是当前表单对象，回调函数会在表单验证全部通过后执行，然后判断是否提交表单，如果callback里明确return false，则表单不会提交，如果return true或没有return，则会提交表单。
			dialog1.close();
			if(data.successful){
				showSueccess(data);

				$('.news-item').eq(index).hide('fast');
			}else{
				$.alert({
					title: '失败',
					icon: 'fa fa-exclamation-circle',
					confirmButtonClass: 'btn-primary',
					content: '回复失败！',
					confirmButton: '确认',
					confirm:function(){
						showFail(data);
					}
				});
			}
		}
	});

	$(".theform").click(function(event) {
		$(".theform").removeClass('on')
		if(!$(this).hasClass('on')){
			$(this).addClass('on')
		}
	});
	var index; // 标记提交的是哪个表单
	$(".btn-sure").click(function(event) {
		/*插入业务逻辑*/
		index=$(".btn-sure").index(this);
		postForm.eq(index).ajaxPost();
	});


	//简略文字的控制
	function summaryControl(){
		$('.question-item .txt').each(function(index, ele) {
			ele=$(ele);
			if(ele.height()>40&&ele.children(".dotdot").length<=0){
				ele.height(40).append( $("<span>",{
					"class":"dotdot",
					html:"...<em>[ + ]</em>"
				}) );
			}
		});

	};

	$(".nav-tabs").on('shown.bs.tab', 'a', function(event) {
		event.preventDefault();
		summaryControl();
	});

	$(window).on('resize load',function(event) {
		summaryControl();
	});

	$(".question-item").on("click",'.dotdot',function(){
		if($(this).parent().height()==40){
			$(this).html("<em>[ - ]</em>").parent().css("height","auto");
		}
		else{
			$(this).html("...<em>[ + ]</em>").parent().height(40)
		}
	});

	var id; // 标记转发给辅导老师的问题
	$('.shareTeacher').click(function(){
		id = $(this).attr('val');
		$('#shareModal .teacher.btn-info').removeClass('btn-info');
		$('#shareModal').modal('show');
	});

	$('#shareModal .teacher').click(function () {
		$('#shareModal .teacher.btn-info').removeClass('btn-info');
		$(this).addClass('btn-info');
	});

	$('#shareModal .btn-primary').click(function(){
		var shareEmployeeId = $('#shareModal .teacher.btn-info').attr('val');
		if(id != undefined && shareEmployeeId != undefined) {
			$.post("shareTeacher",{id:id,shareEmployeeId:shareEmployeeId},function(data){
				if(data.successful){
					showSueccess(data);

					$('#shareModal').modal('hide');
					$('.shareTeacher[val="'+id+'"]').after('<span class="operion-view"><i class="fa fa-fw fa-share-alt"></i> 已转发给' + $('#shareModal .teacher.btn-info').text() + '老师</span>');
					$('.shareTeacher[val="'+id+'"]').remove();
				}else{
					$.alert({
						title: '失败',
						icon: 'fa fa-exclamation-circle',
						confirmButtonClass: 'btn-primary',
						content: '转发失败！',
						confirmButton: '确认',
						confirm:function(){
							showFail(data);
						}
					});
				}
			},"json");
		}
	});
</script>
</body>
</html>
