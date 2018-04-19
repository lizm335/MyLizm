<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 督促提醒</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		添加提醒
	</h1>
	<button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="${ctx}/home/class/remind/eeList">督促提醒</a></li>
		<li class="active">添加提醒</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

	<div class="box margin-bottom-none">
		<div class="box-body">
			<form id="theform" class="form-horizontal" role="form" action="${ctx }/home/class/remind/create" method="post">
				<div class="table-block full-width position-relative add-remind-tbl">
					<div class="table-row">
		              <div class="table-cell-block add-remind-lbl">
		                <div class="pad-t5"><span class="text-red">*</span> <b>接收对象</b></div>
		              </div>
		              <div class="table-cell-block position-relative">
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
					<div class="table-row">
						<div class="table-cell-block add-remind-lbl">
							<div class="pad-t5"><span class="text-red">*</span> 提醒内容</div>
						</div>
						<div class="table-cell-block position-relative">
							<div class="textarea-box">
								<textarea id="editor1" name="infoContent" class="form-control" rows="5" placeholder="提醒内容 ..."  datatype="*" nullmsg="请填写内容！" errormsg="请填写内容！"></textarea>
							</div>
							<div class="tooltip left" role="tooltip">
								<div class="tooltip-arrow"></div>
								<div class="tooltip-inner"></div>
							</div>
						</div>
					</div>
					<div class="table-row">
						<div class="table-cell-block add-remind-lbl">
							<div><span class="text-red">*</span> <b>接收方式</b></div>
						</div>
						<div class="table-cell-block position-relative">
							<div class="chkbox">
								<label>
									<input type="checkbox" name="sendType" class="minimal" value="1"  datatype="*" nullmsg="请选择接收方式！" checked>
									<span class="text-no-bold">ee提醒</span>
								</label>
								<label class="left10">
									<input type="checkbox" name="sendType" class="minimal" value="2">
									<span class="text-no-bold">手机短信</span>
								</label>
							</div>
							<div class="tooltip left" role="tooltip">
								<div class="tooltip-arrow"></div>
								<div class="tooltip-inner"></div>
							</div>
						</div>
					</div>
					<div class="table-row">
						<div class="table-cell-block add-remind-lbl">

						</div>
						<div class="table-cell-block position-relative">
							<div class="msg-tips no-margin" style="display:none">
								温馨提示：<br>
								手机短信一般用于严重落后学员或者需要特别辅导的学员提醒!短信平台有限制，如果内容过多，人多了会发送不出去；建议内容过多的时候，分批发送！<br>
								<!-- 你共可发送短信 <span class="text-red">0</span> 条，已发送 <span class="text-red">0</span> 条，还剩 <span class="text-red">0</span> 条可发，如果需要额外短信配额，请联系 <a href="#" class="text-light-blue">督导教师</a> -->
							</div>
						</div>
					</div>
					<div class="table-row">
						<div class="table-cell-block add-remind-lbl">

						</div>
						<div class="table-cell-block position-relative">
							<button type="button" class="btn btn-success min-width-90px  btn-sure-edit">发送</button>
							<button type="button" class="btn btn-default min-width-90px btn-cancel-edit margin_l15">取消</button>
						</div>
					</div>
				</div>
			</form>
		</div><!-- /.box-body -->
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
	//iCheck for checkbox and radio inputs
	$('input.minimal').iCheck({
		checkboxClass: 'icheckbox_minimal-blue',
		radioClass: 'iradio_minimal-blue'
	}).on("ifChecked",function(e){
		var $e=$(e.target);
		$e.attr('checked',true);
		if($e.is(":checkbox")){
			if($e.val()=='2'){
				$(".msg-tips").show();
			}
		}
	}).on("ifUnchecked",function(e){
		var $e=$(e.target);
		$e.attr('checked',false);
		if($e.val()=='2'){
			$(".msg-tips").hide();
		}
	});

	//Initialize Select2 Elements
	$(".select2").select2();

	/*督促提醒*/
	$(".add-person-btn").click(function(event) {
	  var path=encodeURI('addObject.html');
	  $.dialog({
	      title: false,
	      contentHeight:511,
	      iframeId:'add-person-iframe',
	      content:'url:'+path,
	      contentLoaded:function(){
	        $('#add-person-iframe').closest('.content-pane').css('margin-bottom', '0');
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
	  self.history.back();
	});

	/*确认发送*/
	var $theform=$("#theform");
	$.Tipmsg.r='';
	var postIngIframe;
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
				if(o.obj.is(":checkbox")){
					msgBox.css({
						top:0
					})
				}
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
			postIngIframe=$.dialog({
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
	/* 	else if($container.children(".has-remid-stu").length>0){
			$container.addClass('Validform_error');
			msgBox.children('.tooltip-inner')
					.text('请注意部分学员缺少手机号码信息，不能接收手机短信提醒！');
			msgBox.addClass('in');
		} */
		else{
			msgBox.removeClass('in');
			$container.removeClass('Validform_error');
		}
	}
	$(".btn-sure-edit").click(function(event) {
		//插入业务逻辑
		checkAddPer();
		/*
		 ajaxPost(flag,sync,url)【返回值：Validform】
		 以ajax方式提交表单。flag为true时，跳过验证直接提交，sync为true时将以同步的方式进行ajax提交。
		 */
		postForm.submitForm();
	});
</script>
</body>
</html>