<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="../jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.5 -->
<link rel="stylesheet"
	href="${ctx}/static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${ctx}/static/dist/css/bootstrapValidator.css">
<link href="${ctx}/static/bootstrap/css/bootstrap-select.min.css"
	rel="stylesheet">
<link href="${ctx}/static/bootstrap/css/bootstrap-table.css"
	rel="stylesheet">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="${ctx}/static/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet"
	href="${ctx}/static/ionicons/2.0.1/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${ctx}/static/dist/css/AdminLTE.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet"
	href="${ctx}/static/dist/css/skins/skin-blue.min.css">
<!-- iCheck -->
<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/flat/blue.css">
<!-- Morris chart -->
<link rel="stylesheet" href="${ctx}/static/plugins/morris/morris.css">
<!-- jvectormap -->
<link rel="stylesheet"
	href="${ctx}/static/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
<!-- Date Picker -->
<link rel="stylesheet"
	href="${ctx}/static/plugins/datepicker/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet"
	href="${ctx}/static/plugins/daterangepicker/daterangepicker-bs3.css">
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet"
	href="${ctx}/static/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
<!-- DataTables -->
<link rel="stylesheet"
	href="${ctx}/static/plugins/datatables/dataTables.bootstrap.css">
<!-- jquery-ui -->
<link rel="stylesheet"
	href="${ctx}/static/plugins/jQueryUI/css/jquery-ui.css">

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
<script type="text/javascript"
	src="${ctx}/static/plugins/jQuery/jquery-1.10.2.min.js"></script>
<!--<![endif]-->
<script src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${ctx}/static/dist/js/bootstrapValidator.js"></script>
<script type="text/javascript"
	src="${ctx}/static/dist/js/language/zh_CN.js"></script>
<!-- jQuery 2.1.4 -->
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
<!-- <style type="text/css">
html,body {
	width: 100%;
	min-height: 100%;
	overflow: hidden; 
}
</style> -->
</head>
<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">
		<!-- Main Header -->
		<%@ include file="/WEB-INF/jsp/layouts/header.jsp"%>

		<!-- Left side column. contains the logo and sidebar -->
		<%@ include file="/WEB-INF/jsp/layouts/sidebar.jsp"%>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<%@ include file="/WEB-INF/jsp/layouts/tabs.jsp"%>
			<!-- Main content -->
			
			<iframe name="0_1" id="main" frameborder="0"  scrolling="auto" src="${ctx }/admin/home/index"
				height="100%" width="100%" style="min-height: 874px;" data-id="0_1"> </iframe>
			
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
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
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
	$("#main").load(function(){
		var mainheight = $(this).contents().find("body").height()+100;
		$(this).height(mainheight);
		}); 
		$(function() {
			//初使化一级菜单
			$(".first-level-menu > li[data-id]").each(
					function(i, ele) {
						var $that = $(this);
						$(this).find(".menu-temp a").each(
								function(i, n) {
									$(this).attr(
											"data-id",
											$that.attr("data-id") + "_"
													+ (i + 1))
								});
					}).on("click", function() {
				$(".sidebar-menu").html($(this).find(".menu-temp").html());
				//$.AdminLTE.tree('.sidebar');
			}).eq(0).trigger("click");
			//Date range picker
			$('.reservation')
					.daterangepicker(
							{
								"singleDatePicker" : true,
								"locale" : {
									"format" : "MM/DD/YYYY HH:mm",
									"separator" : " - ",
									"daysOfWeek" : [ "一", "二", "三", "四", "五",
											"六", "日" ],
									"monthNames" : [ "一月", "二月", "三月", "四月",
											"五月", "六月", "七月", "八月", "九月", "十月",
											"十一月", "十二月" ],
									"firstDay" : 0
								}
							},
							function(start, end, label) {
								console.log("New date range selected: "
										+ start.format('YYYY-MM-DD HH:mm')
										+ " to "
										+ end.format('YYYY-MM-DD HH:mm')
										+ " (predefined range: " + label + ")");
							});

			//Initialize Select2 Elements
			$(".select2").select2();

			//省市区联动查询
			$(".select-box").citySelect();

			//选项卡滚动
			$(".page-tabs").sunny_scroll({
				scrollTime : 200
			});

			$(".page-left-btn").click(function() {
				$(".page-tabs .tabs_btn_left").click();
			});
			$(".page-right-btn").click(function() {
				$(".page-tabs .tabs_btn_right").click();
			});
			$(".page-tabs .fa-close").click(function() {
				var dataId = $(this).parent().parent().attr("data-id");
				//移除iframe  移除标签
				$(".content-wrapper iframe[name='"+dataId+"']").remove();
				$(".page-tabs").find("li[data-id='"+dataId+"']").remove();
				
				//关闭的TAB是当前窗体,则选中最后一个TAB标签
				if($(this).parent().parent().attr("class") == 'cur'){
					//选中最后的TAB
					var lastIframe = $(".content-wrapper").find("iframe:last").show();
					var lastTab = $(".page-tabs ul").find("li:last").addClass("cur");
					
					var iframeName = $(".content-wrapper").find("iframe:last").attr("name");
					$(".content-wrapper iframe[name='"+iframeName+"']").show();
					
					//左边导航 siblings() 同胞元素移除样式  
					 $(".sidebar-menu a[data-id='"+iframeName+"']").parent().addClass("active");
					 $(".sidebar-menu a[data-id='"+iframeName+"']").parent().siblings().removeClass("active");
				}
				$(window).resize();
			});

			//高级搜索
			$(".search-more-in").click(
					function() {
						var $that = $(this);
						var $child = $that.children(".fa-fw");
						if ($child.hasClass("fa-caret-down")) {
							$child.addClass("fa-caret-up").removeClass(
									"fa-caret-down");
						} else if ($child.hasClass("fa-caret-up")) {
							$child.addClass("fa-caret-down").removeClass(
									"fa-caret-up");
						}
						$(this).closest(".box").find(".form-search-more")
								.slideToggle("fast")
					});

			//更多选项
			$(".more-tabs").click(
					function() {
						var $that = $(this);
						var $moreTabs = $that.closest(".box").find(
								".more-filter-tabs");
						if ($moreTabs.length > 0) {
							var $child = $that.children(".fa-fw");
							var $childLable = $that
									.children(".more-tabs-label");

							if ($child.hasClass("fa-caret-down")) {
								$child.addClass("fa-caret-up").removeClass(
										"fa-caret-down");
								$childLable.text("精简选项");
							} else if ($child.hasClass("fa-caret-up")) {
								$child.addClass("fa-caret-down").removeClass(
										"fa-caret-up");
								$childLable.text("更多选项");
							}
							$moreTabs.slideToggle("fast");

						}
					});

			$(".tabs-box > li").click(function() {
				$(this).toggleClass("cur");
			});

			//页卡
			$(".page-tabs ul")
					.on(
							"click",
							"li",
							function() {
								var dataId = $(this).attr("data-id");
								var $tab = $(".page-tabs li[data-id='" + dataId
										+ "']");
								var $sidebarTab = $(".sidebar-menu a[data-id='"
										+ dataId + "']");
								var $treeview = $sidebarTab
										.closest(".treeview");
								if (!$tab.hasClass("cur")) {
									$(".content[data-id='" + dataId + "']")
											.show().siblings("section").hide();
									
									 //切换TAB效果
									var existsTab = $(".page-tabs").find("li[data-id='"+dataId+"']");
									 if(existsTab.length>0){
										 //左边导航 siblings() 同胞元素移除样式
										 $(".sidebar-menu a[data-id='"+dataId+"']").parent().addClass("active");
										 $(".sidebar-menu a[data-id='"+dataId+"']").parent().siblings().removeClass("active");
										//TAB移除样式,IFRAME隐藏
										 $(".page-tabs li").removeClass("cur");
										 $(".content-wrapper iframe").hide();
										 //添加样式并展示对应的iframe
										 existsTab.addClass("cur");
										 $(".content-wrapper iframe[name='"+dataId+"']").show();
									 }
								}
								event.preventDefault();
							}).on(
							"click",
							".fa-close",
							function(event) {
								var $parent = $(this).closest("li");
								var dataId = $parent.attr("data-id");
								var $tab = $(".page-tabs li[data-id='" + dataId
										+ "']");

								if ($parent.hasClass("cur")) {
									var $relContent = $(".content[data-id='"
											+ dataId + "']");
									$relContent.hide();
									if ($parent.prev().attr('data-id') != "0") {
										$parent.prev().addClass("cur");
										$relContent.prev().show().end()
												.remove();
									} else {
										if ($parent.next().length > 0) {
											$parent.next().addClass("cur");
											$relContent.next().show().end()
													.remove();
										} else {
											$relContent.prev().show().end()
													.remove();
										}
									}
								} else {
									$(".content[data-id='" + dataId + "']")
											.remove();
								}
								$parent.remove();
								$(window).resize();
								event.preventDefault();
							});

			$(".close-all-tabs").click(function() {
				$(".page-tabs li").filter(function(index) {
					return !$(this).hasClass("home-tab");
				}).remove();
				$(".content-wrapper > .content[data-id!='0']").remove();
				$(".content[data-id='0']").show();
			});

			$(".close-other-tabs").click(
					function() {
						var index = $(".page-tabs li.cur").attr("data-id");
						$(".page-tabs li").filter(
								function(index) {
									return !($(this).hasClass("home-tab") || $(
											this).hasClass("cur"));
								}).remove();
						$(
								".content-wrapper > .content[data-id!='0'][data-id!='"
										+ index + "']").remove();
					});

			 $(document).on("click",".sidebar-menu a[href!='#']",function(event){
				 
				 
				 //获得当前a标签的data-id
				 var dataId=$(this).attr("data-id");
				 $(this).parent().addClass("active");
				 $(this).parent().siblings().removeClass("active");
				 
				 //TAB洗白白，于是iframe全部隐藏起来（感觉将要发生什么）
				 $(".page-tabs li").removeClass("cur");
				 $(".content-wrapper iframe").hide();
				 
				 //寻找TAB是否已存在
				 var existsTab = $(".page-tabs").find("li[data-id='"+dataId+"']");
				 
				 //已存在就切换成当前TAB页下的iframe
				 if(existsTab.length>0){
					 existsTab.addClass("cur");
					 $(".content-wrapper iframe[name='"+dataId+"']").show();
					 
				 }else{//不存在就追加TAB,IFRAME
					//克隆一个TAB隐藏（猥琐的小伙子），并赋予data-id,展示文本，添加样式
					 var tabLi = $(".page-tabs ul").find("li:last").clone(true);
					 tabLi.show();
					 tabLi.attr("data-id",dataId);
					 tabLi.find("span").text($(this).text());
					 tabLi.addClass("cur");
					 $(".page-tabs ul").append(tabLi)
					 
					 //克隆一个iframe（美艳的女子）,赋予name,因为A标签跳转指向的是对应的NAME
					 var tabIframe = $(".content-wrapper").find("iframe:last").clone(true);
					 tabIframe.show();
					 tabIframe.attr("name",dataId);
					 $(".content-wrapper").append(tabIframe);
					 
				 }
				 //当前A标签指向clone出来的iframe
				 $(this).attr("target",dataId);
				 return true; 
			}); 

			$(window).resize(
					function() {
						setTimeout(function() {
							$(".content-wrapper").css(
									'min-height',
									$(window).height()
											- $(".main-header").height());
						}, 300);
					});
		})

		function iFrameHeight() {
			var ifm = document.getElementById("iframe");
			var subWeb = document.frames ? document.frames["iframe"].document
					: ifm.contentDocument;
			if (ifm != null && subWeb != null) {
				ifm.height = subWeb.body.scrollHeight;
				ifm.width = subWeb.body.scrollWidth;
			}
		}
	</script>
</body>
</html>