<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 服务记录</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body>

<form id="theform" class="form-horizontal" role="form" action="${ctx }/home/class/serviceManager/doAddRecord" method="post">
	<input type="hidden" name="id" value="${id}" />
	<div class="slim-Scroll" style="height:450px;">
		<div class="table-block full-width position-relative add-remind-tbl">
			<div class="table-row">
				<div class="table-cell-block add-remind-lbl">
					<div class="pad-t5"><span class="text-red">*</span> 服务时间 </div>
				</div>
				<div class="table-cell-block position-relative">
					<div class="input-group input-custom-daterange">
						<input type="text" name="serviceStartTime" class="form-control single-datetime2" datatype="/^(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})$/i" nullmsg="请填写服务开始时间" errormsg="日期格式不对">
						<span class="input-group-addon">－</span>
						<input type="text" name="serviceEndTime" class="form-control single-datetime2" datatype="/^(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})$/i" nullmsg="请填写服务结束时间" errormsg="日期格式不对">
					</div>
					<div class="tooltip left" role="tooltip">
						<div class="tooltip-arrow"></div>
						<div class="tooltip-inner"></div>
					</div>
				</div>
			</div>
			<div class="table-row">
				<div class="table-cell-block add-remind-lbl">
					<div class="pad-t5"><span class="text-red">*</span> 服务描述</div>
				</div>
				<div class="table-cell-block position-relative">
					<div class="textarea-box">
						<textarea id="editor1" name="content" class="form-control" rows="5" placeholder="服务描述 ..." datatype="*" nullmsg="请填写服务描述！" errormsg="请填写服务描述！"></textarea>
					</div>
					<div class="tooltip left" role="tooltip">
						<div class="tooltip-arrow"></div>
						<div class="tooltip-inner"></div>
					</div>
				</div>
			</div>
			<div class="table-row">
				<div class="table-cell-block add-remind-lbl">
					<div><span class="text-red">*</span> 服务方式</div>
				</div>
				<div class="table-cell-block position-relative">
					<div class="chkbox">
						<dic:radioBox typeCode="ServiceMethod" name="way" defaultCode="0" otherAttrs='class="minimal"' />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="dialog-btn-box">
		<button type="button" class="btn btn-default min-width-90px btn-cancel-edit margin_r15">取消</button>
		<button type="button" class="btn btn-primary min-width-90px  btn-sure-edit">保存</button>
	</div>
</form>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<!-- ckeditor -->
<script src="${ctx}/static/plugins/ckeditor/ckeditor.js"></script>
<script type="text/javascript">
	/*编辑器*/
	CKEDITOR.replace('editor1',{  toolbar: [
		{ name: 'document', items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates' ] },
		{ name: 'insert', items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar'] },
		{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','CreateDiv',
			'-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
		{ name: 'tools', items : [ 'Maximize', 'ShowBlocks' ] },
		{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },

		{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
		{ name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
		{ name: 'colors', items : [ 'TextColor','BGColor' ] }
	]
	});
	//iCheck for checkbox and radio inputs
	$('input.minimal').iCheck({
		checkboxClass: 'icheckbox_minimal-blue',
		radioClass: 'iradio_minimal-blue'
	}).on("ifChecked",function(e){
		var $e=$(e.target);
		$e.attr('checked',true);
	}).on("ifUnchecked",function(e){
		var $e=$(e.target);
		$e.attr('checked',false);
	});
	$('.slim-Scroll').slimScroll({
		height: '450px',
		size: '5px',
		railVisible :true
	});

	/*取消*/
	$(".btn-cancel-edit").click(function(event) {
		top.$("#record-pop-iframe").closest('.jconfirm-box').find(".closeIcon").click();//关闭窗口
	});

	
	var $id=$('#id').val();
	
	/*确认发送*/
	var dialog1;
	var $theform=$("#theform");
	$.Tipmsg.r='';
	var postForm=$theform.Validform({
		showAllError:true,
		tiptype:function(msg,o,cssctl){
			var msgBox=o.obj.closest('.table-cell-block').children('.tooltip');

			msgBox.children('.tooltip-inner').text(msg);
			if(msgBox.hasClass('left')){
				msgBox.css({
					width:125,
					left:-115,
					top:15
				})
			}
			else{
				msgBox.css({
					top:-23
				})
			}

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
			if($theform.find(".Validform_error").length>0){
				return false;
			}
			dialog1 = $.dialog({
				title: false,
				opacity: 0,
				contentHeight:20,
				closeIcon:false,
				content: '<div class="text-center">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
				columnClass:'col-xs-4 col-xs-offset-4'
			});
		},
		callback:function(data){
			dialog1.close();
			if(data.successful){
				$.dialog({
					title: false,
					opacity: 0,
					contentHeight: 20,
					closeIcon: false,
					content: '<div class="text-center">数据提交成功...<i class="icon fa fa-check-circle"></i></div>',
					columnClass: 'col-xs-4 col-xs-offset-4'
				});
				setTimeout(function () {
					
					parent.location.href=parent.location.href;
				}, 2000);
			}else{
				$.alert({
					title: '失败',
					icon: 'fa fa-exclamation-circle',
					confirmButtonClass: 'btn-primary',
					content: '添加失败！',
					confirmButton: '确认',
					confirm:function(){
						showFail(data);
					}
				});
			}
		}
	});

	function checkTextarea(){
		var $editor=$("#editor1");
		var $textareaBox=$(".textarea-box");
		var msgBox=$editor.closest('.table-cell-block').children('.tooltip');
		msgBox.css({
			width:110,
			left:-100,
			top:15
		})

		if(CKEDITOR.instances.editor1.getData()!==""){
			$editor.val(CKEDITOR.instances.editor1.getData());
			$textareaBox.removeClass('Validform_error');
			msgBox.removeClass('in');
		}
		else{
			$textareaBox.addClass('Validform_error');
			msgBox.children('.tooltip-inner').text('请填写服务描述！');
			msgBox.addClass('in');
		}
	}

	$(".btn-sure-edit").click(function(event) {
		/*插入业务逻辑*/
		checkTextarea();
		if($(".Validform_error").length>0){
			$('.slim-Scroll').slimScroll({
				scrollTo:
				$(".Validform_error:first").position().top
			});
		}
		postForm.ajaxPost();
	});
</script>
</body>
</html>
