<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<html>
<head>
    <title>上传回调</title>
</head>
<body>
    <table align="center">
        <tr>
            <td><h2><a href="javascript:window.close();">关闭</a></h2></td>
        </tr>
    </table>
</body>
<!-- jQuery 2.1.4 -->
<!--[if gte IE 9]>
<script src="${ctx}/static/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<![endif]-->

<!--[if !IE]><!-->
<script src="${ctx}/static/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<!--<![endif]-->

<!--[if lt IE 9]>
<script src="${ctx}/static/plugins/jQuery/jquery-1.12.4.min.js"></script>
<![endif]-->

<script type="application/javascript">
    (function($) {

        var list = window.opener.document.getElementsByClassName('cert-box-7');
        $(list[0]).find('.cert-img-box').html('<div class="table-cell-block vertical-middle text-center"><img src="${picPath}" alt="User Image"><\/div>');
        $(list[0]).addClass('has-upload');
        $(list[0]).find('.cert-upload-btn').addClass('cert-re-upload');
        $(list[0]).find('.cert-upload-btn').text('重新上传');

    })(jQuery);
    // 自动关闭当前窗口
    window.close();
</script>
</html>
