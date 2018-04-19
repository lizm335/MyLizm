<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="hold-transition fixed skin-blue sidebar-mini">
<div class="wrapper">
	<!-- Main Header -->
	<%@ include file="/WEB-INF/jsp/new/layouts/header.jsp"%>

	<!-- Left side column. contains the logo and sidebar -->
	<%@ include file="/WEB-INF/jsp/new/layouts/sidebar.jsp"%>

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<div class="overlay-wrapper loading" style="display: block;">
			<div class="overlay"><i class="fa fa-refresh fa-spin" style="top:200px !important"></i></div>
		</div>
		<%--<%@ include file="/WEB-INF/jsp/new/layouts/tabs.jsp"%>--%>

		<!-- Main content -->
		<div class="page" data-id="0" style="min-height:300px;">
			<iframe src="${ctx }/home/class/index" frameborder="0" scrolling="auto" allowtransparency="true"></iframe>
		</div>
	</div>
</div>
<!-- ./wrapper -->
<!-- 浮动工具 -->
<dl class="note-tool">
	<dt class="note-tool-title" title="快捷工具"><i class="fa fa-fw fa-sign-out"></i>快<br>捷<br>工<br>具</dt>
	<dd class="note-tool-body">
		<ul class="list-unstyled">
			<li class="urge-remind">
				<a href="#">
					<i class="fa fa-fw fa-sms"></i>
					<span>督促提醒</span>
				</a>
			</li>
			<li class="service-record">
				<a href="#">
					<i class="fa fa-fw fa-record"></i>
					<span>服务记录</span>
				</a>
			</li>
			<li class="ee-online">
				<a href="${ctx}/home/forwardWebEE" target="_blank">
					<i class="fa fa-fw fa-ee-online" style="font-size:40px;"></i>
					<span>EE在线</span>
				</a>
			</li>
			<li>
				<a href="#">
					<i class="fa fa-fw fa-mobile-phone" style="font-size:40px;"></i>
					<span>下载APP</span>
				</a>
				<div class="popover left qr-code-menu" style="left:auto;right:80px;top:auto;bottom:-15px;max-width: none;width:340px;">
					<div class="arrow" style="top:65%;"></div>
					<div class="popover-content">
						<div class="media">
							<div class="media-left pad-r20">
								<img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/qrcode.png" alt="Image">
							</div>
							<div class="media-body">
								<h4 class="media-heading f16 text-bold text-center pad-t5 gray6">
									扫码下载移动APP
								</h4>
								<div>
									<a href="#" target="_blank" role="button" class="androild-down btn-block qr-down-btn"><i class="fa fa-fw fa-android" style="float: left;"></i> Android下载</a>
									<a href="#" target="_blank" role="button" class="app-down btn-block qr-down-btn"><i class="fa fa-fw fa-apple" style="float: left;"></i> APP Sotre下载</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</li>
		</ul>
	</dd>
</dl>
<!-- ./浮动工具 -->
<!-- IE8及以下的浏览器弹出提示窗口 -->
<div id="dialog-message" title="温馨提示" style="display:none">
	<p style="padding:20px 10px;margin:0;"> 本管理系统支持IE9以上版本浏览器，为了获取更好的体验，建议
		使用Firefox、Chrome、Opera等浏览器。 </p>
</div>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<!-- 选项卡滚动 -->
<script src="${ctx}/static/plugins/tabsSroll/sunny_scroll.js"></script>
<script src="${ctx}/static/dist/js/index.js"></script>

<!--[if lt IE 9]>
<script type="text/javascript">
	$(function() {
		var dialog=$( "#dialog-message" ).dialog({
			modal: true,
			width: 600,
			create: function( event, ui ) {
				//$("html").css("overflow","hidden")
			},
			buttons: {
				'我知道了': function() {
					$( this ).dialog( "close" );
				}
			}
		}).dialog( "instance" );
		dialog.uiDialog.css("z-index",10001);
		dialog.overlay.css("z-index",10000);
	});
</script>
<![endif]-->

<script type="text/javascript">

	$("[data-id='1_1']").parent().addClass("active").siblings('.treeview').removeClass('active');

	//-- 工具 --//
	$(".note-tool-title").click(function(){
		$(".note-tool").toggleClass("expand");
	});
	$(document).click(function(event){
		if($(event.target).closest(".note-tool").length<=0){
			if($(".note-tool").hasClass("expand")){
				$(".note-tool").removeClass("expand");
			}
		}
		event.stopPropagation();
	});
	/*
	 $("[data-role='change-class']").click(function(event) {
	 var dialogInstance1 = new BootstrapDialog({
	 title:'<b>添加督促提醒</b>',
	 type:'type-default',
	 size:'size-wide',
	 message: function(dialog) {
	 var $message = $('<div class="model-cn-box" style="height:535px;"><div class="overlay-wrapper"><div class="overlay"><i class="fa fa-refresh fa-spin"></i></div></div></div>');
	 return $message;
	 }
	 });
	 dialogInstance1.open();
	 var path="test.html";
	 var $container=dialogInstance1.getModalBody();

	 setTimeout(function(){
	 $.get(path).done(function(data){
	 var $iframe=$("<iframe>");
	 $iframe.prop({
	 src:path,
	 scrolling:"auto",
	 frameBorder:"0"
	 });
	 $container
	 .find(".model-cn-box")
	 .append($iframe.hide());
	 $iframe.load(function(){
	 $container
	 .find(".overlay-wrapper").remove();
	 $iframe.show();
	 });
	 }).fail(function(data){
	 $container
	 .find(".model-cn-box")
	 .empty()
	 .append("url '"+path+"' "+data.statusText);
	 });
	 },500)
	 });*/

	/*切换班级*/
	var changeClassIframe;
	$("[data-role='change-class']").click(function(event) {
		var path=encodeURI('${ctx }/home/class/list');
		changeClassIframe=$.dialog({
			title: false,
			contentHeight:525,
			iframeId:'change-class-iframe',//如果引用的是html单页面，请务必使用此属性
			/*
			 content: function(){
			 var self = this;
			 return $.get(path).done(function(data){
			 $iframe.prop({
			 id:'change-class-iframe',
			 name:'change-class-iframe',
			 src:path,
			 scrolling:"auto",
			 frameBorder:"0"
			 });
			 self.setContent($iframe);
			 });

			 },
			 */
			content:'url:'+path,
			contentLoaded:function(){

			},
			onOpen:function(){

			},
			backgroundDismiss: true,
			animation: 'top',
			columnClass: 'col-md-10 col-md-offset-2 reset-jconfirm',
			closeAnimation: 'bottom'
		});
	});

	/*督促提醒*/
	var urgeRemind;
	$(".urge-remind").click(function(event) {
		var path=encodeURI('${ctx }/home/class/remind/toCreatePopup');
		urgeRemind=$.dialog({
			title: '<b class="f18">添加督促提醒</b>',
			contentHeight:410,
			iframeId:'add-remind-iframe',
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

	/*服务记录*/
	var serviceRecord;
	$(".service-record").click(function(event) {
		var path=encodeURI('${ctx }/home/class/serviceManager/toCreateServicePopup');
		serviceRecord=$.dialog({
			title: '<b class="f18">添加服务记录</b>',
			contentHeight:410,
			iframeId:'service-record-iframe',
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

	$('.first-level-menu li').click(function(){
	    $(this).addClass("cur").siblings().removeClass("cur");
	    var index=$('.first-level-menu li').index($(this));
	    var $ul = $('#tree_container ul').eq(index);
	    $li=$ul.find('li').clone();
	    $('.sidebar-menu').empty().append($li);
	    $('.sidebar-menu a :first').click();
	});
	
</script>
</body>
</html>
