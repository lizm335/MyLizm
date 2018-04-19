<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.5 -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/bootstrapValidator.css">
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/bootstrap/css/bootstrap-select.min.css"	>
<%-- <link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/bootstrap/css/bootstrap-table.css">
 --%>
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/bootstrap/css/jquery-confirm.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${css_nocdn}/ouchgzee_com/platform/xllms_css/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- zTree -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/zTree/css/zTreeStyle/zTreeStyle.css">
<!-- Ionicons -->
<%-- <link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/ionicons/2.0.1/css/ionicons.min.css"> --%>
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/select2/select2.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/AdminLTE.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet"
	href="${css}/ouchgzee_com/platform/xllms_css/dist/css/skins/skin-blue.min.css">
<!-- iCheck -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/iCheck/flat/blue.css">
<%-- 
<!-- Morris chart -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/morris/morris.css">
<!-- jvectormap -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/jvectormap/jquery-jvectormap-1.2.2.css"> --%>
<!-- Date Picker -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/datepicker/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/daterangepicker/daterangepicker-bs3.css">
<%-- <!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
<!-- DataTables -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/datatables/dataTables.bootstrap.css"> --%>
<!-- jquery-ui -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/plugins/jQueryUI/css/jquery-ui.css">
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/reset.css">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="${ctx}/static/js/html5shiv.min.js"></script>
<script src="${ctx}/static/js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQueryForm/jquery.form.min.js"></script>
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/dist/js/bootstrapValidator.js"></script>
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/dist/js/language/zh_CN.js"></script>
<!-- custom-model -->
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/plugins/custom-model/custom-model.js"></script>
<!-- jQuery 2.1.4 -->
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQueryUI/jquery-ui.min.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/bootstrap-select.js"></script>
<%-- <script src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/bootstrap-table.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/bootstrap-table-zh-CN.js"></script> --%>
<script src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/defaults-zh_CN.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/jquery-confirm.js"></script>
<!-- datepicker -->
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>

<!-- Daterange date-range-picker -->
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/daterangepicker/moment.min.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/daterangepicker/daterangepicker.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/ckeditor/ckeditor.js"></script>

<!-- bootstrap-confirmation -->
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/bootstrap-confirmation/bootstrap-confirmation.js"></script>

<!-- zTree --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/zTree/js/jquery.ztree.core.min.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/zTree/js/jquery.ztree.excheck.min.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/zTree/js/jquery.ztree.exhide.min.js"></script>

<script src="${ctx}/static/js/page/list.js?v=1"></script>

<script type="application/javascript">
	var ctx = '${ctx }';
	var css = '${css }';
</script>
<!-- common js -->
<script src="${css}/ouchgzee_com/platform/xllms_css/dist/js/common.js?t=123465"></script>
<script src="${ctx}/static/js/common.js?v=1"></script>

<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/select2/select2.full.min.js"></script>
<!-- JQuery-Validform -->
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/JQuery-Validform/Validform_v5.3.2_min.js"></script>

<script src="${ctx}/static/js/LodopFuncs.js"></script>


