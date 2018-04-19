<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统-学籍信息</title>
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">

<form id="form1" name="form1" action="${pageMap.synUrl}" method="post">
    <input type="hidden" name="id_card" id="id_card" value="${pageMap.id_card}"/>
    <input type="hidden" name="phone" id="phone" value="${pageMap.phone}"/>
    <input type="hidden" name="account" id="account" value="${pageMap.account}"/>
    <input type="hidden" name="organ" id="organ" value="${pageMap.organ}"/>
    <input type="hidden" name="code" id="code" value="${pageMap.code}"/>
</form>
<script type="text/javascript">
    $(function () {
        $("#form1").submit();
    });
</script>

</body>
</html>
