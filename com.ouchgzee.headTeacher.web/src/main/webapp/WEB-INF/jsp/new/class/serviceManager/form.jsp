<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 服务记录</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		新增服务记录
	</h1>
	<button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="${ctx}/home/class/serviceManager/list">服务记录</a></li>
		<li class="active">新增服务记录</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

	<div class="box margin-bottom-none">
		<div class="box-body">
			<form id="theform" class="form-horizontal" role="form" action="${ctx }/home/class/serviceManager/doCreateService" method="post">
				<div class="table-block full-width position-relative add-remind-tbl">
					<div class="table-row">
						<div class="table-cell-block media-middle add-remind-lbl">
							<span class="text-red">*</span> 服务班级
						</div>
						<div class="table-cell-block position-relative">
							<select id="class_list" class="form-control" datatype="*" nullmsg="请选择年级！" errormsg="请选择年级！"></select>
							<div class="tooltip left" role="tooltip">
								<div class="tooltip-arrow"></div>
								<div class="tooltip-inner"></div>
							</div>
						</div>
					</div>
					<div class="table-row">
						<div class="table-cell-block media-middle add-remind-lbl">
							<span class="text-red">*</span> 主题
						</div>
						<div class="table-cell-block position-relative">
							<input type="text" name="title" class="form-control" datatype="*" nullmsg="请填写主题！" errormsg="请填写主题！" placeholder="主题">
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
							<div class="pad-t5"><span class="text-red">*</span> 服务对象</div>
						</div>
						<div class="table-cell-block position-relative">
							<%--<select name="getUser" class="form-control select2" multiple data-size="10" data-live-search="true" data-placeholder="服务对象"
									datatype="*" nullmsg="请选择服务对象！" errormsg="请选择服务对象！">
								<c:forEach items="${studentMap}" var="s">
									<option value="${s.key}">${s.value }</option>
								</c:forEach>
							</select>--%>
							<input type="hidden" name="getUser" id="getUser" >
							<div class="row row-offset-3px">
								<div class="col-xs-10">
									<ul class="list-unstyled stu-list-box clearfix">
									</ul>
								</div>
								<div class="col-xs-2">
									<button type="button" class="btn btn-block btn-default add-person-btn">添加</button>
								</div>
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
					<div class="table-row">
						<div class="table-cell-block add-remind-lbl">
							<div class="pad-t5"><span class="text-red">*</span> 服务时间</div>
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
							<div><span class="text-red">*</span> 服务状态</div>
						</div>
						<div class="table-cell-block position-relative">
							<div class="chkbox">
								<label>
									<input type="radio" name="status" class="minimal" value="0" checked="checked">
									<span class="text-no-bold">未结束</span>
								</label>
								<label class="left10">
									<input type="radio" name="status" class="minimal" value="1">
									<span class="text-no-bold">已结束</span>
								</label>
							</div>
						</div>
					</div>
				</div>
				<div class="dialog-btn-box">
					<button type="button" class="btn btn-default min-width-90px btn-cancel-edit margin_r15">取消</button>
					<button type="button" class="btn btn-primary min-width-90px  btn-sure-edit">保存</button>
				</div>
			</form>
		</div><!-- /.box-body -->
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

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



	var singleDateTimePickerOpt = {
		singleDatePicker: true,
		maxDate : moment(), //最大时间
		showDropdowns : true,
		timePicker : true, //是否显示小时和分钟
		timePickerIncrement : 5, //时间的增量，单位为分钟
		timePicker12Hour : false, //是否使用12小时制来显示时间
		format : 'YYYY-MM-DD HH:mm', //控件中from和to 显示的日期格式
		locale : {
			applyLabel : '确定',
			cancelLabel : '取消',
			daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
			monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
			firstDay : 1
		},
		drops: 'up' // 弹出的位置 (string: 'down' or 'up') Whether the picker appears below (default) or above the HTML element it's attached to
	};
	$('.single-datetime').daterangepicker(singleDateTimePickerOpt);

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

	//Initialize Select2 Elements
	$(".select2").select2();

	/*新增服务记录-添加学员*/


	var servicePerson;
	$(".add-person-btn").click(function(event) {
		var path="${ctx }/home/class/serviceManager/chooseStu?classid="+$("select  option:selected").val();
		servicePerson=$.dialog({
			title:  false,//'<b class="f18">添加学员</b>',
			contentHeight:480,
			iframeId:'service-person-iframe',
			content:'url:'+path,
			contentLoaded:function(){

			},
			onOpen:function(){

			},
			backgroundDismiss: true,
			animation: 'top',
			columnClass: 'col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 reset-jconfirm',
			closeAnimation: 'bottom'
		});
		return false;
	});
	/*删除学员*/
	$(".stu-list-box").on("click",".fa-remove",function(){
		$(this).parent().remove();
		checkAddPer();
	});
	/*取消*/
	$(".btn-cancel-edit").click(function(event) {

	});

	/*确认发送*/
	var dialog1,dialog2;
	var $theform=$("#theform");
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
			var msgBox=o.obj.closest('.table-cell-block').children('.tooltip');

			msgBox.children('.tooltip-inner').text(msg);
			if(msgBox.hasClass('left')){
				msgBox.css({
					width:110,
					left:-100,
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
			var stus = new Array();
			$("ul").children('li').each(function () {// 遍历并加载学员到隐藏字段
				var temp = $(this).attr("data-val");
				if(temp != "" &&  temp != undefined){
					stus.push(temp.split("_")[0]);
				}
			});
			$("#getUser").val(stus);
			if($theform.find(".Validform_error").length>0){
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
		}
	});

	function checkAddPer(){
		var $container=$(".stu-list-box");
		var $liCollectors=$container.children('li');
		var msgBox=$container.closest('.table-cell-block').children('.tooltip');

		msgBox.css({top:-23})
				.children('.tooltip-inner').css({'max-width':'none'});

		if($liCollectors.length<=0){
			$container.addClass('Validform_error');
			msgBox.children('.tooltip-inner').text('请添加接收学员');
			msgBox.addClass('in');
		}
		else if($container.children(".has-remid-stu").length>0){
			$container.addClass('Validform_error');
			msgBox.children('.tooltip-inner')
					.text('请注意部分学员缺少手机号码信息，不能接收手机短信提醒！');
			msgBox.addClass('in');
		}
		else{
			msgBox.removeClass('in');
			$container.removeClass('Validform_error');
		}
	}
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
		checkAddPer();
		checkTextarea();
		postForm.submitForm();
	});

	// 获取班级列表
	$.post("${ctx}/headTeacher/webservice/classList",{},function (re) {
		var arr = new Array();
		$.each(re,function (index,c) {
			var select_sign = "" ;
			if("${classId}" == c.CLASSID ) {
				select_sign = "selected";
			}
			arr.push("<option value='"+c.CLASSID+"' "+select_sign+">"+c.CLASSNAME+"</option>");
		});
		$("#class_list").html(arr.join(""));
	},"json");


</script>
</body>
</html>
