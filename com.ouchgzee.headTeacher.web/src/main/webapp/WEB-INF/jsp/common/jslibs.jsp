<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- <%@ page import="com.gzedu.xlims.common.AppUtil"%> --%>
<c:set var="ctx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>

<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<link rel="shortcut icon" href="${ctx }/static/images/favicon.ico" type="image/x-icon" />
<!-- Bootstrap 3.3.5 -->
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctx}/static/dist/css/bootstrapValidator.css">
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap-select.min.css"	>
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap-table.css">
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/jquery-confirm.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${ctx}/static/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="${ctx}/static/ionicons/2.0.1/css/ionicons.min.css">
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
<link rel="stylesheet" href="${ctx}/static/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
<!-- Date Picker -->
<link rel="stylesheet" href="${ctx}/static/plugins/datepicker/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet" href="${ctx}/static/plugins/daterangepicker/daterangepicker-bs3.css">
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet" href="${ctx}/static/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
<!-- DataTables -->
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/static/plugins/datatables/dataTables.bootstrap.css">
<!-- jquery-ui -->
<link rel="stylesheet" href="${ctx}/static/plugins/jQueryUI/css/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/static/dist/css/reset.css">
<!--[if !IE]><!-->
<script type="text/javascript" src="${ctx}/static/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<script type="text/javascript" src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/static/dist/js/bootstrapValidator.js"></script>
<script type="text/javascript"src="${ctx}/static/dist/js/language/zh_CN.js"></script>
<!-- jQuery 2.1.4 -->
<script src="${ctx}/static/bootstrap/js/bootstrap-select.js"></script>
<script src="${ctx}/static/bootstrap/js/bootstrap-table.js"></script>
<script src="${ctx}/static/bootstrap/js/bootstrap-table-zh-CN.js"></script>
<script src="${ctx}/static/bootstrap/js/defaults-zh_CN.js"></script>
<script src="${ctx}/static/plugins/jQueryUI/jquery-ui.min.js"></script>

<!-- iCheck 1.0.1 -->
<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
<!-- jQuery Form -->
<script src="${ctx}/static/plugins/jQueryForm/jquery.form.min.js"></script>
<!-- datepicker -->
<script src="${ctx}/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${ctx}/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<!-- date-range-picker -->
<script src="${ctx}/static/plugins/daterangepicker/moment.min.js"></script>
<script src="${ctx}/static/plugins/daterangepicker/daterangepicker.js"></script>
<script src="${ctx}/static/js/daterangepicker/daterangepicker.js"></script>
<script src="${ctx}/static/plugins/tabsSroll/sunny_scroll.js"></script>
<!-- jquery-confirm -->
<script src="${ctx}/static/bootstrap/js/jquery-confirm.js"></script>
<!-- bootstrap-confirmation -->
<script src="${ctx}/static/plugins/bootstrap-confirmation/bootstrap-confirmation.js"></script>
<!-- SlimScroll 1.3.0 -->
<script src="${ctx}/static/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- ChartJS 1.0.1 -->
<script src="${ctx}/static/plugins/chartjs/Chart.min.js"></script>
<!-- AdminLTE App -->
<script src="${ctx}/static/dist/js/app.min.js"></script>
<!-- common js -->
<script src="${ctx}/static/dist/js/common.js"></script>
<script src="${ctx}/static/js/common.js"></script>
<!-- page-->
<script src="${ctx}/static/js/page/list.js"></script>

<!-- echarts 3 -->
<script src="${ctx}/static/plugins/echarts/echarts.min.js"></script>



<script type="application/javascript">
	var ctx = '${ctx }';
</script>

