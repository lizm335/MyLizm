<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>日报详情</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
<link rel="stylesheet" href="${ctx}/static/plugins/fullcalendar/fullcalendar.min.css">
<link rel="stylesheet" href="${ctx}/static/plugins/fullcalendar/fullcalendar.print.css" media="print">
</head>
<body class="inner-page-body">
<section class="content-header">
  <h1>
  日报详情
  </h1>
  <ol class="breadcrumb">
    <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
    <li><a href="${ctx}/home/class/report/list">工作计划</a></li>
    <li class="active">日报详情</li>
  </ol>
</section>
<section class="content">
  <div class="box margin-bottom-none">
    <div class="box-header with-border">
      <h3 class="box-title text-bold f16">${fn:substring( info.createdDt ,0,10)}日报详情</h3>
      <div class="box-tools pull-right">
        <button class="btn btn-block btn-default btn-sm" onclick="window.history.back();">返回列表</button>
      </div>
    </div>
    <div class="pad30 box-body">
      <div class="gray6">
       ${info.summary}
      </div>
      <div class="gray9 margin_t20 pad-t15">
        我 ${fn:substring( info.createdDt ,0,16)}
      </div>
    </div>
	<c:if test="${not empty info}">
		  <c:if test="${not empty info.commententId}">
    <!-- 已点评状态 -->
    <div class="box-footer pad-l30 pad-r30">
      <ul class="report-list list-unstyled">
        <li>
          <div>
            <span class="label label-primary">点评</span>
            <span class="gray9 margin_l10">${info.commententName} ${fn:substring( info.updatedDt ,0,16)}</span>
          </div>
          <div class="pad-t15">${info.comments}</div>
        </li>
      </ul>
    </div>
</c:if>
</c:if>
    <!-- 未点评状态 -->
    <div class="box-footer pad-r20 pad-l20">
      <div class="text-right">
        <a class="btn btn-primary min-width-90px" href="${ctx}/home/class/report/toUpdateReport/${info.id}">编辑</a>
      </div>
    </div>

  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script src="${ctx}/static/plugins/fullcalendar/fullcalendar.min.js"></script>
<script src="${ctx}/static/plugins/fullcalendar/zh-cn.js"></script>
</body>
</html>
