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
    window.opener.document.getElementById("example").value='${picPath}';
    window.opener.document.getElementById("exampleName").value='${fileName}';
    window.opener.document.getElementById("exampleA").href='${picPath}';
    window.opener.document.getElementById("exampleA").innerHTML='${fileName}';
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
