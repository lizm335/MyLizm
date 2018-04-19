<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>清除缓存</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form action="${ctx }/cleanCached/cleanAll" method="post">
	<input id="cleanAll" type="button" value="清除全部">
</form>
<form action="${ctx }/cleanCached/cleanByKey" method="post">
	根据key：<input id="key" name="key" type="text" value=""> <br>
	<input id="cleanByKey" type="button" value="清除">
</form>

<script type="text/javascript">
$(function() {
	
	$("#cleanAll").click(function() {
		$.ajax({  
            type : "post",  
            url : "${ctx}/cleanCached/cleanAll",  
            dataType:'json',
            async : false,  
            success : function(data){  
                if(data.successful){
                    alert(data.message);
                }
            }
        });
	});
	
	$("#cleanByKey").click(function() {
		var key = $("#key").val();
		if (key == "") {
			alert("请输入key！");
			return false;
		}
		$.ajax({  
            type : "post",  
            url : "${ctx}/cleanCached/cleanByKey",  
            dataType:'json',
            data : {key:key},  
            async : false,  
            success : function(data){  
                if(data.successful){
                    alert(data.message);
                }
            }
        });
	});
	
})
</script> 

</body>
</html>