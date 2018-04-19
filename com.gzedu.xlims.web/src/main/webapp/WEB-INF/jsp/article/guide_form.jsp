<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>文章管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<!-- ckeditor -->
	<script src="${ctx }/static/plugins/ckeditor/ckeditor.js"></script>
	<!-- iCheck 1.0.1 -->
	<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
	<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var editor=CKEDITOR.replace('editor4');
			//参考： http://bv.doc.javake.cn/examples/
		 var $theform = $("#inputForm");
	    var htmlTemp = '<div class="tooltip top" role="tooltip">' + '<div class="tooltip-arrow"></div>' + '<div class="tooltip-inner"></div>' + '</div>';
	    $theform.find(":input[datatype]").each(function(index, el) {
		$(this).closest('.position-relative').append(htmlTemp);
	    });
		var postIngIframe;
	    $.Tipmsg.r = '';
	    var postForm = $theform.Validform({
			ignoreHidden : true,
			showAllError : false,
			ajaxPost:true,
			tiptype : function(msg, o, cssctl) {
			    if (!o.obj.is("form")) {
				var msgBox = o.obj.closest('.position-relative').find('.tooltip');
	
				msgBox.css({
				    bottom : "100%",
				    'margin-bottom' : -5
				})
				msgBox.children('.tooltip-inner').text(msg);
	
				switch (o.type) {
				case 3:
				    msgBox.addClass('in');
				    break;
				default:
				    msgBox.removeClass('in');
				    break;
				}
			    }
			},
			beforeSubmit : function(curform) {
			    if ($.trim(editor.document.getBody().getText())=='') {
					$('#contentTip').addClass('in');
					return false;
			    }
			    $('[name="content"]').val(editor.getData());
			    postIngIframe = $.formOperTipsDialog({
					text : '数据提交中...',
					iconClass : 'fa-refresh fa-spin'
			    });

			    return true;
			},
			callback:function(data){
			    if(data.successful){
				    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
					postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-check-circle');
					location.href='${ctx}/article/guideList';
				}else{
					alert(data.message);
				    $.closeDialog(postIngIframe);
				}
			}
	    });

		$('.datepickers').datepicker({
			autoclose : true,
			format : 'yyyy-mm-dd', //控件中from和to 显示的日期格式
			language : 'zh-CN',
			todayHighlight : true
		});

		})
	</script>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px"
			data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="${ctx}/article/list">文章管理</a></li>
			<li class="active">新增文章</li>
		</ol>
	</section>
	<section class="content">
		<div class="col-md-12">
			<div class="box margin-bottom-none">
				<form id="inputForm" class="form-horizontal" role="form"
					action="${ctx }/article/saveGuide" method="post">
					<input id="action" type="hidden" name="action" value="${action}">
					<input type="hidden" name="id" value="${gjtArticle.id}">
					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-2 control-label"><small
								class="text-red">*</small>标题</label>
							<div class="col-sm-8 position-relative">
								<input type="text" name="title" class="form-control"
									value="${gjtArticle.title}" placeholder="请输入标题"  datatype="*" nullmsg="请输入标题" errormsg="请输入标题" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">有效日期</label>
							<div class="col-sm-8">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" id="effectiveStimeStr"
											class="form-control datepickers" name="effectiveStime"
											placeholder="有效开始时间"
											value="<fmt:formatDate value="${gjtArticle.effectiveStime}" pattern="yyyy-MM-dd"/>" />
									</div>
									<div class="input-group-addon no-border">至</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" id="effectiveEtimeStr"
											class="form-control datepickers" name="effectiveEtime"
											placeholder="有效结束时间"
											value="<fmt:formatDate value="${gjtArticle.effectiveEtime}" pattern="yyyy-MM-dd"/>" />
									</div>

								</div>
							</div>
						</div>
						

						<div class="form-group">
							<label class="col-sm-2 control-label"><small
								class="text-red">*</small>内容</label>
							<div class="col-sm-8  position-relative">
								<div class="tooltip top" id="contentTip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner">请填写内容</div></div>
								<textarea id="editor4" name="content"  rows="10" cols="80">${gjtArticle.content}</textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">上传附件</label>
							<div class="col-sm-8">
								<input  type="text" id="fileName" name="fileName" value="${gjtArticle.fileName}" readonly="readonly">
								<input  type="hidden" id="fileUrl"  name="fileUrl"> 
								<input value="选择文件" type="button"
									onclick="javascript:uploadFile('fileName','fileUrl','xls|xlsx|doc|docx|ppt|pptx|pdf|rar|zip');">
							</div>
						</div>
					</div>
					<div class="form-group margin-bottom-none">
						<label class="col-xs-2 control-label margin_b15 sr-only"></label>
						<div class="col-xs-5 margin_b15">
							<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
							<button id="btn-back" class="btn btn-primary" onclick="history.back()">返回</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</section>
	
	<jsp:include page="/eefileupload/upload.jsp"/>
</body>
</html>