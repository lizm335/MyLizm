<%--
  Created by IntelliJ IDEA.
  User: hyf
  Date: 2016/5/19
  Time: 21:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传回调</title>
</head>
<script type="application/javascript">
    <%--var rtpath='${rtpath}';
    var rtid='${rtid}';
    var fileName='${fileName}';--%>
    window.opener.document.getElementById("headImgUrl").value='${picPath}';
    try{
    	window.opener.document.getElementById("headImgId").src='${picPath}';
    } catch (e) {};
    
    try{
        window.opener.document.getElementById("bzrImage").src='${picPath}';
    } catch (e) {};

    try{
        var list = window.opener.document.getElementsByClassName('headSmallImg');
        for(var i=0;i<list.length;i++) {
            list[i].src='${picPath}';
        }
    } catch (e) {};
</script>
<body>
    <table align="center">
        <tr>
            <td><h2><a href="javascript:window.close();">关闭</a></h2></td>
        </tr>
    </table>

    <script type="application/javascript">
        // 自动关闭当前窗口
        window.close();
    </script>
</body>
</html>
