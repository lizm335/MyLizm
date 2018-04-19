<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
  <title>班主任平台 - 通知公告</title>
  <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<h1>新增通知公告</h1>
		<ol class="breadcrumb">
			<li>
				<a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="${ctx}/home/class/message/publishList">通知公告</a>
			</li>
			<li class="active">新增通知公告</li>
		</ol>
	</section>
	<section class="content">
		<div class="box margin-bottom-none">
			<div class="box-body">
				<form id="theform" class="form-horizontal" role="form" action="${ctx }/home/class/message/${action}" method="post">
					<input id="action" type="hidden" name="action" value="${action }"> <input type="hidden" name="messageId" value="${info.messageId }">
					<div class="form-horizontal reset-form-horizontal margin_t10">
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>通知标题</label>
							<div class="col-md-8 col-sm-10  position-relative">
								<input type="text" name="infoTheme" value="${info.infoTheme }" class="form-control" placeholder="通知标题" datatype="*" nullmsg="请填写通知标题！"
									errormsg="请填写通知标题！">
								<div class="tooltip left" role="tooltip">
									<div class="tooltip-arrow"></div>
									<div class="tooltip-inner"></div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>公告类型</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<select class="form-control select2" name="infoType" placeholder="请选择通知类型！" datatype="*" nullmsg="请选择通知类型！"
									errormsg="请选择通知类型！"  data-size="8" data-live-search="true">
									<option value="">请选择</option>
									<option value="11" <c:if test="${info.infoType==11}">selected</c:if>>班级公告</option>
									<option value="12" <c:if test="${info.infoType==12}">selected</c:if>>考试通知</option>
									<option value="13" <c:if test="${info.infoType==13}">selected</c:if>>学习提醒</option>
								</select>
								<div class="tooltip left" role="tooltip">
									<div class="tooltip-arrow"></div>
									<div class="tooltip-inner"></div>
								</div>
							</div>
						</div>
						<c:if test="${action=='create' }">
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>接收对象</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<div class="row row-offset-3px">
										<div class="col-xs-10">
											<ul class="list-unstyled stu-list-box clearfix">
											</ul>
										</div>
										<div class="col-xs-2">
											<button class="btn btn-block btn-default add-person-btn">添加</button>
										</div>
									</div>
									<div class="tooltip top" role="tooltip">
										<div class="tooltip-arrow"></div>
										<div class="tooltip-inner"></div>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label no-pad-top">是否推送到微信</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<div class="radio form-control-static no-padding">
										<label class="margin_r15"> 
											<input type="checkbox"  name="isWeiXin"  value="1" class="minimal" checked="checked"> 
										</label> 
									</div>
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>通知内容</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<div class="textarea-box">
									<textarea id="editor1" name="infoContent" rows="10" cols="80">${info.infoContent}</textarea>
								</div>
								<div class="tooltip left" role="tooltip">
									<div class="tooltip-arrow"></div>
									<div class="tooltip-inner"></div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">上传附件</label>
							<div class="col-md-8 col-sm-10">
								<input type="text" id="fileName" name="fileName" value="${info.fileName}" readonly="readonly">
								 <input type="hidden" id="fileUrl" name="fileUrl" value="${info.fileUrl}">
								<input value="选择文件" type="button" onclick="javascript:uploadFile('fileName','fileUrl');">
								 <input value="删除附件" type="button" 	id="deleteFile" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label no-pad-top">通知置顶</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<div class="radio form-control-static no-padding">
									<label class="margin_r15">
										 <input type="radio" data-role="top" name="stick" data-value="0" value="0"  <c:if test="${empty isStick or isStick==false }">checked</c:if>> 不置顶
										 
									</label>
									 <label> 
									 	<input type="radio" data-role="top" name="stick" data-value="1" value="1"   <c:if test="${isStick}">checked</c:if>> 置顶
									</label>
								</div>
							</div>
						</div>
						<div class="form-group" style="display: none;" data-id="top-time">
								<div class="clearfix">
									<label class="col-md-2 col-sm-2 control-label">置顶时间</label>
									<div class="col-md-8 col-sm-10 position-relative">
										<select name="days" class="form-control"  nullmsg="请选择置顶时长！" errormsg="请选择置顶时长！"  data-id="top-time-form-control">
											<option value="">请选择置顶时间</option>
											<option value="7">置顶1个星期</option>
											<option value="15">置顶半个月</option>
											<option value="30">置顶1个月</option>
											<option value="90">置顶3个月</option>
											<option value="182">置顶半年</option>
											<option value="365">置顶1年</option>
											<option value="3650">一直置顶</option>
											<option value="0" data-value="custom">自定义置顶时间</option>
										</select>
									</div>
								</div>
								<div class="clearfix margin_t15" style="display: none;" data-id="custom-time">
									<label class="col-md-2 col-sm-2 control-label"></label>
									<div class="col-md-8 col-sm-10 position-relative">
										<input type="text" name ="times"class="form-control" placeholder="设置置顶结束时间" nullmsg="请设置置顶结束时间" errormsg="时间格式不对" data-role="datetime">
									</div>
								</div>
							</div>
						
						<div class="form-group margin_t20">
							<label class="col-md-2 col-sm-2 control-label"></label>
							<div class="col-md-8 col-sm-10">
								<button type="button" class="btn btn-success min-width-90px margin_r15 btn-sure-edit">${action=='create'?'发布':'保存'}</button>
								<button type="button" class="btn btn-default min-width-90px btn-cancel-edit">取消</button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
<jsp:include page="/eefileupload/upload.jsp"/>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">

	$(".select2").select2();
	
	/*督促提醒*/
	$(".add-person-btn").click(function(event) {
		var path = encodeURI('addObject.html');
		$.dialog({
			title : false,
			contentHeight : 511,
			iframeId : 'add-person-iframe',
			content : 'url:' + path,
			contentLoaded : function() {
				$('#add-person-iframe').closest('.content-pane').css('margin-bottom', '0');
			},
			onOpen : function() {

			},
			backgroundDismiss : true,
			animation : 'top',
			columnClass : 'col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 reset-jconfirm',
			closeAnimation : 'bottom'
		});
		return false;
	});

	/*删除学员*/
	$(".stu-list-box").on("click", ".fa-remove", function() {
		$(this).parent().remove();
		checkAddPer();
	});

	function checkAddPer() {
		var $container = $(".stu-list-box");
		var $liCollectors = $container.children('li');
		var msgBox = $container.closest('.position-relative').children('.tooltip');
		msgBox.css({
			top : -23
		}).children('.tooltip-inner').css({'max-width' : 'none'});

		if ($liCollectors.length <= 0) {
			$container.addClass('Validform_error');
			msgBox.children('.tooltip-inner').text('请添加接收学员');
			msgBox.addClass('in');
		} else {
			msgBox.removeClass('in');
			$container.removeClass('Validform_error');
		}
	}

	CKEDITOR.replace('editor1');

	//iCheck for checkbox and radio inputs
	$('input.minimal').iCheck({
		checkboxClass : 'icheckbox_minimal-blue',
		radioClass : 'iradio_minimal-blue'
	}).on("ifChecked", function(e) {
		var $e = $(e.target);
		$e.attr('checked', true);
	}).on("ifUnchecked", function(e) {
		$(e.target).attr('checked', false);
	});

	/*确认发送*/
	var $theform = $("#theform");
	$.Tipmsg.r = '';
	var postIngIframe;
	var postForm = $theform.Validform({
		showAllError : true,
		tiptype : function(msg, o, cssctl) {
			//msg：提示信息;
			//o:{obj:*,type:*,curform:*},
			//obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
			//type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
			//curform为当前form对象;
			//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
			var msgBox = o.obj.closest('.position-relative').children('.tooltip');
			msgBox.children('.tooltip-inner').text(msg);
			if (msgBox.hasClass('left')) {
				msgBox.css({
					width : 130,
					left : -120,
					top : 5
				})
			} else {
				msgBox.css({
					top : -23
				})
			}
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
			if ($theform.find(".Validform_error").length > 0) {
				return false;
			}
			postIngIframe = $.dialog({
				title : false,
				contentHeight : 20,
				closeIcon : false,
				content : '<div class="text-center">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
				columnClass : 'col-xs-4 col-xs-offset-4'
			});
		},
		callback : function(data) {
			var rIframe = $.dialog({
				title : false,
				contentHeight : 20,
				closeIcon : false,
				content : '<div class="text-center">'
						+ data.message
						+ '<i class="icon fa fa-check-circle"></i></div>',
				columnClass : 'col-xs-4 col-xs-offset-4',
			});
			setTimeout(
				function() {
					if (data.successful == true) {
						postIngIframe.close();
						rIframe.close();
						window.location.href = '${ctx}/home/class/message/publishList';
					} else {
						postIngIframe.close();
						rIframe.close();
					}
			}, 1500);
		}
	});

	function checkTextarea() {
		var $editor = $("#editor1");
		var $textareaBox = $(".textarea-box");
		var msgBox = $editor.closest('.position-relative').children('.tooltip');
		msgBox.css({
			width : 130,
			left : -120,
			top : 5
		})
		if (CKEDITOR.instances.editor1.getData() !== "") {
			$editor.val(CKEDITOR.instances.editor1.getData());
			$textareaBox.removeClass('Validform_error');
			msgBox.removeClass('in');
		} else {
			$textareaBox.addClass('Validform_error');
			msgBox.children('.tooltip-inner').text('请填写通知内容！');
			msgBox.addClass('in');
		}
	}
	$(".btn-sure-edit").click(function(event) {
		/*插入业务逻辑*/
		checkTextarea();
		checkAddPer();
		postForm.ajaxPost();
	});
	/*取消*/
	$(".btn-cancel-edit").click(function(event) {
		self.history.back();
	});
	/*预览文章*/
	$(".btn-preview").click(
			function(event) {
				checkTextarea();
				if (postForm.check()
						&& !$(".textarea-box").hasClass('Validform_error')) {
					postForm.abort();
					window.location.href = '文章详情-预览.html';
					return false;
				}
			});

	//删除上传附件	
	$('#deleteFile').click(function() {
		$('#fileName').val('');
		$('#fileUrl').val('');
	});

	//置顶选项
	$('[data-role="top"]').click(
		function(event) {
			if ($(this).prop('checked')&& $(this).attr('data-value') == 1) {
				$('[data-id="top-time"]').show();
				$('[data-id="top-time-form-control"]').attr('datatype', '*');
			} else {
				$('[data-id="top-time"]').hide();
				$('[data-id="top-time-form-control"]').removeAttr('datatype').removeClass('Validform_error');
			}
		});

	//自定义置顶时间
	$('[data-id="top-time-form-control"]').on('change',function(event) {
		if ($(this).children().eq(this.selectedIndex).attr('data-value') == 'custom') {
			$('[data-id="custom-time"]').show();
			$('[data-role="datetime"]').attr('datatype', '*');
		} else {
			$('[data-id="custom-time"]').hide();
			$('[data-role="datetime"]').removeAttr('datatype').removeClass('Validform_error');
		}
	});

	/*日期控件*/
	$('[data-role="datetime"]').datepicker({
		language : 'zh-CN',
		format : 'yyyy-mm-dd',
		startDate : increaseOnedate(new Date())
	});
</script>
</body>
</html>
