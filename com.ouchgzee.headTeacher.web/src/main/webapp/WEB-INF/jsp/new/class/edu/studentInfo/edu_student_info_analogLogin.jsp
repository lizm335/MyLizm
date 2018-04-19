<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员管理</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
<form id="form1" name="form1" action="${pageMap.synUrl}" method="post">
    <input type="hidden" name="id_card" id="id_card" value="${pageMap.id_card}"/>
    <input type="hidden" name="phone" id="phone" value="${pageMap.phone}"/>
    <input type="hidden" name="account" id="account" value="${pageMap.account}"/>
    <input type="hidden" name="organ" id="organ" value="${pageMap.organ}"/>
    <input type="hidden" name="code" id="code" value="${pageMap.code}"/>
</form>
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<script type="text/javascript">
    $(function () {
        $("#form1").submit();
    });
</script>

</body>
</html>
