<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body no-padding">
<section class="content">
    <div class="nav-tabs-custom margin-bottom-none">
      <ul class="nav nav-tabs">
        <li>
          <a href="${ctx}/admin/home/myWorkbench" target="_self">我的工作台</a>
        </li>
          <li>
              <a href="${ctx}/admin/home/statistical" target="_self">统计分析</a>
          </li>
          <li>
              <a href="${ctx}/admin/home/studentSynthesizeStatistical" target="_self">学生综合信息查询</a>
          </li>
          <li class="active">
              <a href="${ctx}/admin/home/businessSynthesizeStatistical" target="_self">业务综合信息查询</a>
          </li>
      </ul>
      <div class="text-center">
          <img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/waiting-for-open.png">
      </div>
    </div>
    
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
</html>
