<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退出</title>
</head>
<body>
<%
// String role=null==request.getSession().getAttribute("syxRole")?"":request.getSession().getAttribute("syxRole").toString();
response.setHeader("Cache-Control","no-cache"); //Forces caches to obtain a new copy of the page from the origin server
response.setHeader("Cache-Control","no-store"); //Directs caches not to store the page under any circumstance
response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
response.setHeader("Pragma","no-cache"); //HTTP 1.0 backward compatibility
//session.removeAttribute(com.firstnet.gftravel.front.Constants.MEMBER_ID);
session.invalidate();

String href="/login.do?style="+request.getParameter("style");
    
%>
<script language="javascript">
 function delCookies(){
      var aCookie = document.cookie.split("; ");
      date = new Date();
      date.setTime(date.getTime()-1000*60*60*24);
      document.cookie ="schoolCode=; expires=" + date.toGMTString()+"; domain=.eecn.cn; path=/;";
      return null;
 }
delCookies();
</script>		
<script language="javascript">
window.top.location.href="<%=request.getContextPath()%><%=href%>";
</script>
</body>
</html>
