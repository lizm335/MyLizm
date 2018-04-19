<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>班主任平台</title>

<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>

</head>
<body>
	<div id="logisticsDialog" class="box no-border">
	  <div class="box-header with-border">
		  <h3 class="box-title">物流状态</h3>
	  </div>
	  <div class="pop-content" style="padding: 15px;">
		  <div class="padding15 f14 text-bold f_474747" style="background:#f5f5f5; padding: 15px;">
		    <p>货运单号：<span class="express-num">${expressNumber}</span></p>
		    <p>快递名称：<span class="express-name">${expressCompany}</span></p>
		  </div>
		  <div class="logistics-box approval-list approval-list-2 clearfix" style="overflow-y: scroll; overflow-x: hidden; max-height: 460px; width:470px;">
		  	<c:choose>
		  		<c:when test="${not empty errorMsg}">
		  			${errorMsg}
		  		</c:when>
		  		<c:otherwise>
		  			<c:forEach items="${list}" var="obj">
		  				<dl class="approval-item">
		  					<dt class="clearfix">
		  						<div class="a-tit"><b>${obj.context}</b></div>
		  						<span class="time state-lb">${obj.time}</span>
		  					</dt>
		  				</dl>
		  			</c:forEach>
		  		</c:otherwise>
		  	</c:choose>
		  </div>
	  </div>
	</div>
</body>
</html>


					

