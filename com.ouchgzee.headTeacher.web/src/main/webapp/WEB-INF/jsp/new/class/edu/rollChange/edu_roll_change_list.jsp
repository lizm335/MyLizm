<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学籍异动</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
    <section class="content-header">
        <div class="pull-left">
            您所在位置：
        </div>
        <ol class="breadcrumb">
            <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
            <li><a href="#">教务管理</a></li>
            <li class="active">学员管理</li>
        </ol>
    </section>

    <section class="content">
        <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
        <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

        <div class="nav-tabs-custom no-margin">
            <ul class="nav nav-tabs nav-tabs-lg">
                <li><a href="${ctx}/home/class/studentInfo/list">学籍信息</a></li>
                <li class="active"><a href="#tab_top_2">学籍异动</a></li>
                <!--<li><a href="#tab_top_3" >统计分析</a></li>-->
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_top_2">
                    <iframe id="enterIFrame" src="${ctx}/openingSoon" frameborder="0" scrolling="no" allowtransparency="true" style="height: 420px;"></iframe>
                </div>
            </div>
        </div>
    </section>

    <%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

    <%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
</html>
