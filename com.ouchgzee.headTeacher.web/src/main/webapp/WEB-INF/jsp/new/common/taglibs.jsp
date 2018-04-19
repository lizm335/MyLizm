<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dic" uri="http://www.eenet.com/jsp/jstl/dictionary" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<c:set var="image"><%=com.gzedu.xlims.common.AppConfig.getProperty("imageconfig")%></c:set>
<c:set var="css"><%=com.gzedu.xlims.common.AppConfig.getProperty("cssconfig")%></c:set>
<c:set var="css_nocdn"><%=com.gzedu.xlims.common.AppConfig.getProperty("cssconfig_nocdn")%></c:set>
