<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/static/dist/css/bootstrapValidator.css">
	<link href="${ctx}/static/bootstrap/css/bootstrap-select.min.css"  rel="stylesheet">
    <link href="${ctx}/static/bootstrap/css/bootstrap-table.css"  rel="stylesheet">
    <!-- Font Awesome -->
	<link rel="stylesheet" href="${ctx}/static/font-awesome/4.4.0/css/font-awesome.min.css">
	<!-- Ionicons -->
	<link rel="stylesheet" href="${ctx}/static/ionicons/2.0.1/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${ctx}/static/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="${ctx}/static/dist/css/skins/skin-blue.min.css">
    <!-- iCheck -->
    <link rel="stylesheet" href="${ctx}/static/plugins/iCheck/flat/blue.css">
    <!-- Morris chart -->
    <link rel="stylesheet" href="${ctx}/static/plugins/morris/morris.css">
    <!-- jvectormap -->
    <link rel="stylesheet" href="${ctx}/static/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
    <!-- Date Picker -->
    <link rel="stylesheet" href="${ctx}/static/plugins/datepicker/datepicker3.css">
    <!-- Daterange picker -->
    <link rel="stylesheet" href="${ctx}/static/plugins/daterangepicker/daterangepicker-bs3.css">
    <!-- bootstrap wysihtml5 - text editor -->
    <link rel="stylesheet" href="${ctx}/static/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
	<!-- DataTables -->
    <link rel="stylesheet" href="${ctx}/static/plugins/datatables/dataTables.bootstrap.css">
	<!-- jquery-ui -->
	<link rel="stylesheet" href="${ctx}/static/plugins/jQueryUI/css/jquery-ui.css">
	
	<link rel="stylesheet" href="${ctx}/static/dist/css/reset.css">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!--[if gte IE 9]>
	<script src="${ctx}/static/plugins/jQuery/jQuery-2.1.4.min.js"></script>
	<![endif]-->

	<!--[if !IE]><!-->
	<script type="text/javascript" src="${ctx}/static/plugins/jQuery/jquery-1.10.2.min.js"></script>
	<!--<![endif]-->
	<script src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/dist/js/bootstrapValidator.js"></script>
	<script type="text/javascript" src="${ctx}/static/dist/js/language/zh_CN.js"></script>
	<!-- jQuery 2.1.4 -->
	<!--[if lt IE 9]>
		<script src="${ctx}/static/plugins/jQuery/jquery-1.9.0.min.js"></script>
		  <script>
		  $(function() {
			var dialog=$( "#dialog-message" ).dialog({
			  modal: true,
			  width: 600,
			  create: function( event, ui ) {
				$("html").css("overflow","hidden")
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
	
    <script src="${ctx}/static/bootstrap/js/bootstrap-select.js"></script>
    <script src="${ctx}/static/bootstrap/js/bootstrap-table.js"></script>
    <script src="${ctx}/static/bootstrap/js/bootstrap-table-zh-CN.js"></script>
    <script src="${ctx}/static/bootstrap/js/defaults-zh_CN.js"></script>
    <script src="${ctx}/static/plugins/jQueryUI/jquery-ui.min.js"></script>
    
	<!-- date-range-picker --> 
	<script src="${ctx}/static/ajax/libs/moment.js/2.10.2/moment.min.js"></script> 
	<script src="${ctx}/static/plugins/daterangepicker/daterangepicker.js"></script> 
	<!-- Select2 --> 
	<script src="${ctx}/static/plugins/select2/select2.full.min.js"></script> 
	<!-- 选项卡滚动 --> 
	<script src="${ctx}/static/plugins/tabsSroll/sunny_scroll.js"></script>
	<!-- 省市区联动查询 --> 
	<script src="${ctx}/static/plugins/cityselect/jsonData.js"></script> 
	<script src="${ctx}/static/plugins/cityselect/citySelect.js"></script>
	<!-- DataTables -->
    <script src="${ctx}/static/plugins/datatables/jquery.dataTables.js"></script>
    <script src="${ctx}/static/plugins/datatables/dataTables.bootstrap.js"></script>

    <!-- AdminLTE App -->
    <script src="${ctx}/static/dist/js/app.min.js"></script>

    <!-- SlimScroll 1.3.0 -->
    <script src="${ctx}/static/plugins/slimScroll/jquery.slimscroll.min.js"></script>
    <!-- ChartJS 1.0.1 -->
    <script src="${ctx}/static/plugins/chartjs/Chart.min.js"></script>
	
</head>
<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">
		<!-- Main Header -->
		<%@ include file="/WEB-INF/layouts/header.jsp"%>

		<!-- Left side column. contains the logo and sidebar -->
		<%@ include file="/WEB-INF/layouts/sidebar.jsp"%>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<%@ include file="/WEB-INF/layouts/tabs.jsp"%>
			<!-- Main content -->
				<sitemesh:write property='body'></sitemesh:write> 
		</div>
	</div>
	<!-- <div id="dialog-message" title="温馨提示" style="display: none">
		<p style="padding: 20px 10px; margin: 0;">
			本管理系统支持IE9以上版本浏览器，为了获取更好的体验，建议 使用Firefox、Chrome、Opera等浏览器。</p>
	</div> -->
	<div class="modal fade" id="modal-template" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
					<h4 class="modal-title">提示</h4>
				</div>
				<div class="modal-body">
					<p>确认删除</p>
				</div>
				<div class="modal-footer">
					<button class="btn btn-primary" data-role="ok">确认</button>
					<button class="btn btn-default" data-role="cancel">取消</button>
				</div>
			</div>
		</div>
	</div>
    
	<script type="text/javascript">
		$(function(){
			//初使化一级菜单
			$(".first-level-menu > li[data-id]").each(function(i,ele){
				var $that=$(this);
				$(this).find(".menu-temp a").each(function(i,n){
					$(this).attr("data-id",$that.attr("data-id")+"_"+(i+1))
				});
			}).on("click",function(){
				$(".sidebar-menu").html($(this).find(".menu-temp").html());
				//$.AdminLTE.tree('.sidebar');
			}).eq(0).trigger("click");
			//Date range picker
			$('.reservation').daterangepicker({
				"singleDatePicker": true,
				"locale": {
					"format": "MM/DD/YYYY HH:mm",
					"separator": " - ",
					"daysOfWeek": [
						"一",
						"二",
						"三",
						"四",
						"五",
						"六",
						"日"
					],
					"monthNames": [
						"一月",
						"二月",
						"三月",
						"四月",
						"五月",
						"六月",
						"七月",
						"八月",
						"九月",
						"十月",
						"十一月",
						"十二月"
					],
					"firstDay": 0
				}
				}, function(start, end, label) {
				console.log("New date range selected: " + start.format('YYYY-MM-DD HH:mm') + " to " + end.format('YYYY-MM-DD HH:mm') + " (predefined range: " + label + ")");
			});


			//Initialize Select2 Elements
			$(".select2").select2();
			
			//省市区联动查询
			$(".select-box").citySelect();
			
			//选项卡滚动
			$(".page-tabs").sunny_scroll({scrollTime:200});
			
			$(".page-left-btn").click(function(){
				$(".page-tabs .tabs_btn_left").click();
			});
			$(".page-right-btn").click(function(){
				$(".page-tabs .tabs_btn_right").click();
			});
			$(".page-tabs .fa-close").click(function(){
				$(this).parent().remove();
				$(window).resize();
			});
			
			//高级搜索
			$(".search-more-in").click(function(){
			  var $that=$(this);
			  var $child=$that.children(".fa-fw");
			  if($child.hasClass("fa-caret-down")){
				$child.addClass("fa-caret-up").removeClass("fa-caret-down");
			  }
			  else if($child.hasClass("fa-caret-up")){
				$child.addClass("fa-caret-down").removeClass("fa-caret-up");
			  }
			  $(this).closest(".box").find(".form-search-more").slideToggle("fast")
			});
			
			//更多选项
			$(".more-tabs").click(function(){
			  var $that=$(this);
			  var $moreTabs=$that.closest(".box").find(".more-filter-tabs");
			  if($moreTabs.length>0){
				var $child=$that.children(".fa-fw");
				var $childLable=$that.children(".more-tabs-label");

				if($child.hasClass("fa-caret-down")){
				  $child.addClass("fa-caret-up").removeClass("fa-caret-down");
				  $childLable.text("精简选项");
				}
				else if($child.hasClass("fa-caret-up")){
				  $child.addClass("fa-caret-down").removeClass("fa-caret-up");
				  $childLable.text("更多选项");
				}
				$moreTabs.slideToggle("fast");
				
			  }
			});
			
			$(".tabs-box > li").click(function(){
				$(this).toggleClass("cur");
			});
			
			//页卡
			$(".page-tabs ul").on("click","li",function(){
			  var dataId=$(this).attr("data-id");
			  var $tab=$(".page-tabs li[data-id='"+dataId+"']");
			  var $sidebarTab=$(".sidebar-menu a[data-id='"+dataId+"']");
			  var $treeview=$sidebarTab.closest(".treeview");
			  if(!$tab.hasClass("cur")){
				  $(".content[data-id='"+dataId+"']").show().siblings("section").hide();
				  if(dataId!="0"){
					$tab.addClass("cur");
				  }
				  $tab.siblings().removeClass("cur");
			  }

			  event.preventDefault();
		  }).on("click",".fa-close",function(event){
			  var $parent=$(this).closest("li");
			  var dataId=$parent.attr("data-id");
			  var $tab=$(".page-tabs li[data-id='"+dataId+"']");

			  if($parent.hasClass("cur")){
				var $relContent=$(".content[data-id='"+dataId+"']");
				$relContent.hide();
				if($parent.prev().attr('data-id')!="0"){
					$parent.prev().addClass("cur");
					$relContent.prev().show().end().remove();
				}
				else{
					if($parent.next().length>0){
						$parent.next().addClass("cur");
						$relContent.next().show().end().remove();
					}
					else{
						$relContent.prev().show().end().remove();
					}
				}
			  }
			  else{
				$(".content[data-id='"+dataId+"']").remove();
			  }
			  $parent.remove();
			  $(window).resize();
			  event.preventDefault();
		  });
		  
		  $(".close-all-tabs").click(function(){
				$(".page-tabs li").filter(function(index){
					return !$(this).hasClass("home-tab");
				}).remove();
				$(".content-wrapper > .content[data-id!='0']").remove();
				$(".content[data-id='0']").show();
		  });
		  
		  $(".close-other-tabs").click(function(){
				var index=$(".page-tabs li.cur").attr("data-id");
				$(".page-tabs li").filter(function(index){
					return !($(this).hasClass("home-tab")||$(this).hasClass("cur"));
				}).remove();
				$(".content-wrapper > .content[data-id!='0'][data-id!='"+index+"']").remove();
		  });
		  
		  
		  
		  $(document).on("click",".sidebar-menu a[href!='#']",function(event){
				event.preventDefault();
				
				var domThis=this;
				var domId=$(domThis).attr("data-id");
				var $contentWrapper=$(".content-wrapper");
				var $container=$(".content[data-id='"+domId+"']");
				var $loading=$contentWrapper.css("position","relative").children(".loading");
				if($loading.length<=0){
					$loading=$("<div class='loading'></div>");
					$contentWrapper.prepend($loading);
				}
				
				if($container.length<=0){
					$loading.show();
					$container=$('<section class="content"></section>').attr("data-id",domId);
					$.get(domThis.href).done(function(data){
						$contentWrapper.children(".content").hide();
						$(".page-tabs li").removeClass("cur");
						$(".page-tabs ul").append('<li class="cur" data-id="'+domId+'"><a href="#"><span>'+$(domThis).text()+'</span><i class="fa fa-fw fa-close" title="关闭"></i></a></li>')
						$loading.hide();
						$contentWrapper.css("position","static").append($container.html(data));
					}).fail(function(data){
						$loading.hide();
						alert("url '"+domThis.href+"' "+data.statusText);
					});
				}
				else{
					var $t=$(".page-tabs li[data-id='"+domId+"']");

					$t.addClass("cur").siblings().removeClass("cur");
					$contentWrapper.children(".content").hide();
					$container.show();
				}
				$(window).resize();
		  });
		  
		  $(window).resize(function(){
				setTimeout(function(){
					$(".content-wrapper").css('min-height', $(window).height()-$(".main-header").height());
				},300);
		  });
		})
	</script>
</body>
</html>