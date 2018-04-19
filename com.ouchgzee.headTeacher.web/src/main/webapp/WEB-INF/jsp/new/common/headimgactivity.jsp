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
    <title>上传班级活动图片</title>
</head>
<script type="application/javascript">
    <%--var rtpath='${rtpath}';
    var rtid='${rtid}';
    var fileName='${fileName}';--%>
    var imgUrl = window.opener.document.getElementById("imgs").value;  
    try{
	    if(imgUrl==""){
	    	imgUrl='${picPath}';
	    }else{
	    	imgUrl=imgUrl+","+'${picPath}';
	    }
    } catch (e) {};
    try{
    	
    	window.opener.document.getElementById("imgs").value=imgUrl;
    	window.opener.document.getElementById("imgSrc").src='${picPath}';
    	//window.opener.document.getElementsByClassName("imgClass").src='${picPath}';
    	//getClass('input','imgClass').src='${picPath}';
    	window.opener.document.getElementById("imgSrc").removeAttribute("id");
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
