<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
<title>签名结果</title>
<link rel="stylesheet" type="text/css" href="${css}/ouchgzee_com/person_center/v3.0.1/mobile-signature/css/base.css">
<style type="text/css">
.text-center {
    text-align: center;
}
</style>
</head>

<body>
   	<div class="page-box">
		<!--header-->
		<header class="header">
			<div class="header_in">
				<h3 class="page_tit">签名结果</h3>
			</div>
		</header>
      	
		<!--main-->
		<div class="wrap-page">
			
			<div class="content">
				
				<div class="text-center padding_t25">
	              <p>
	                <img src="${css}/ouchgzee_com/person_center/v2.1/images/tips-${feedback.successful?'success':'fail' }.png" class="vertical-middle">
	                <span class="font22 margin_l10 gray6 txt_l vertical-middle inline-block">${feedback.message }</span>
	              </p>
	            </div>
				
			</div>
			
		</div>
		
	</div>
</body>
</html>
