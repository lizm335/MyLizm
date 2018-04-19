<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<!DOCTYPE HTML>
<%
	String  actionerrors=request.getAttribute("actionerrors")!=null?request.getAttribute("actionerrors").toString():"";
	request.removeAttribute("actionerrors");
	java.util.HashMap resultMap = (java.util.HashMap)request.getAttribute("resultMap");
	/* String cssPath = AppUtil.getCssImageServerPath(request,resultMap); */
	String css = com.gzedu.xlims.common.AppConfig.getProperty("cssconfig");
	String style = request.getParameter("collegeCode") != null ? request.getParameter("collegeCode").toString() : "teacher";
	String p = "_temp";//css style

	String logoico = "/static/images/favicon.ico";
	String logoImg = css + "/xueli_oucnet_cn/portal/v1.0/images/logo.png";
	String backgroundStyle = null;
	String title = "班主任平台";
	String name = "国家开放大学（在线教育）";
	String hotline = "400 096 9300";
	String zhuban = "<p><span class='right20'>主办：国家开放大学</span></p>";
	String technicalSupport = "";
	String copyright = "";
	//System.out.println("joho login_index.jsp style= "+style);
	if ("4".equals(style)) {// 深圳市龙岗区社区学院
		logoico = "/static/images/favicon.ico";
		logoImg = css + "/xueli_oucnet_cn/lg_zz_oucnet_cn/portal/v1.0/images/logo.png";
		backgroundStyle = "style='background-image:url(" + css + "/xueli_oucnet_cn/lg_zz_oucnet_cn/portal/v1.0/style/images/logoArea_body.jpg)'";
		title = "班主任平台";
		name = "深圳市龙岗区社区学院";
		hotline = "";
		zhuban = "深圳市龙岗区人力资源局 <br>";
		technicalSupport = "技术支持：<img src='" + css + "/common/logo/eenet_logo_small.png' alt='' /><br>";
		copyright = "增值电信业务经营许可证编号粤B2-20042063 | 粤ICP备08131364号";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=title%></title>
<link rel="shortcut icon" href="${ctx }<%=logoico%>" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="${css}/common/common.css" />
<link rel="stylesheet" type="text/css" href="${css}/ouchgzee_com/portal/v2.1/style/style.css" />
<script type="text/javascript" src="${ctx }/static/common/jquery.js"></script>
<!--[if IE]>
<script type="text/javascript" src="${css}/common/js/resetHTML5_forIE.js"></script>
<![endif]-->
<!--[if lte IE 6]>
<script type="text/javascript" src="${css}/common/js/DD_belatedPNG_0.0.8a.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix('.bg_png');
</script>
<![endif]-->

	<style type="text/css">
		.wrap_head {
			font-family: Microsoft YaHei, '\5b8b\4f53', sans-serif !important;
		}
		.wrap_head img {
			display: inline-block;
			margin-top: -5px;
			float: left;
		}
		.wrap_head span {
			float: left;
			margin-top: 10px;
			font-size: 24px;
			color: #c9151e;
			font-weight: normal;
			border-left: 1px solid #c5c5c5;
			padding-left: 20px;
			margin-left: 20px;
			line-height: 30px;
		}
		.error {
			color: #ff0000;
			background: none;
			display: none;
		}
		.prompt {
			display: inline;
		}
	</style>
	<script type="text/javascript">
	
		if(window !=top){  
		    top.location.href=location.href;  
		}  
	
		function checkInput(){
			var b=$("#username").val();
			if(""==b){
				alert("请输入用户名!");
				$("#username").focus().select();
				return
			}
			var a=$("#password").val();
			if(""==a){
				alert("请输入密码!");
				$("#password").focus().select();
				return
			}
			var c=$("#checkNumber").val();
			if(""==c){
				alert("请输入验证码!");
				$("#checkNumber").focus().select();
				return
			}
			$("#main").submit()
		};

		function changeImg(){
			document.getElementById("change").src="${ctx}/vCode?w=80&h=30&"+Math.random();
		}

		function checkAutoLogin(){
			var arr = document.cookie.split(";");
			for(var i=0;i<arr.length;i++){
				var param=arr[i].split("=");
				if("GZEDUN"==trimStr(param[0])){
					document.getElementById("username").value=trimStr(param[1])
				} else if("GZEDUP"==trimStr(param[0])){
					document.getElementById("password").value=trimStr(param[1])
				} else if("GZEDUR"==trimStr(param[0])){
					document.getElementById("rempwd").checked=trimStr(param[1]);
				}
			}
		};

		function trimStr(str) {
			var re = /\s*(\S[^\0]*\S)\s*/;
			re.exec(str);
			return RegExp.$1;

		}

		// 考虑兼容性，在元素中需要加上着两个onkeypress="EnterPress(event)" onkeydown="EnterPress()"
		function  EnterPress(e) {
			var e = (e || window.event);
			if(e.keyCode == 13)
				checkInput();
		}

		function  actionerrors() {
			var  err='<%=actionerrors %>';
			if(err!=''){
				alert(err);
			}
		}

		$(document).ready(function(){
			checkAutoLogin();
			//alert error infomation.
			actionerrors();

			var msg =  $('#msg').val();
			if(msg != ""){
				$(".error").addClass("prompt");
				$(".error").text(msg);
				$("#password").val("");
				$("#password").focus().select();
			}else{
				$("#username").focus().select();
			}

			//文本框焦点
			$('.input_text').focus(function(event){
				$(".error").text("");
			});
		});
		//-->
	</script>
</head>

<body onkeypress="EnterPress(event);" onkeydown="EnterPress();">

	<div class="wrap">
		<div class="wrap_head">
			<img src="<%=logoImg%>">
			<a target="_blank" class="btn01" href="http://www.ouchn.edu.cn/"></a>
			<a target="_blank" class="btn02" href="http://www.oucnet.cn/"></a>
			<a target="_blank" class="btn03" href="http://xueli.oucnet.cn"></a>
		</div>
		<div class="wrap_body" <%=backgroundStyle%>>
			<form name="loginForm" action="${ctx}/login"  method="post" id="main">
				<input type="hidden" id="msg" name="msg" value="${msg}">
				<input type="hidden" name="userType" value="133" 　/>
				<div class="loginArea">
					<div class="loginArea_head">
						<h1 id="loginTxt">班主任请登录</h1>
					</div>
					<div class="loginArea_body">
						<table>
							<tbody>
							<tr>
								<td></td>
								<td>
									<span class="error">用户名/密码错误！</span>
								</td>
							</tr>
							<%--<tr>
								<td width="60" align="right">用户名：</td>
								<td id="username_div"><input type="text" class="loginArea_input"
										   id="username" name="username" value="${username}" style="height: 12px;"></td>
							</tr>
							<tr>
								<td width="60" align="right">密 码：</td>
								<td id="password_div"><input type="password" class="loginArea_input"
										   id="password" name="password" style="height: 12px;"></td>
							</tr>--%>
							<tr>
								<td width="60" align="right">用户名：</td>
								<td id="username_div"><input type="text" class="loginArea_input"
										   id="username" name="username" value="gkbanzhuren" style="height: 12px;"></td>
							</tr>
							<tr>
								<td width="60" align="right">密 码：</td>
								<td id="password_div"><input type="password" class="loginArea_input"
										   id="password" name="password" value="888888" style="height: 12px;"></td>
							</tr>
							<tr>
								<td width="60" align="right">验证码：</td>
								<td><input type="text" class="loginArea_input"
										   id="checkNumber" name="vCode" value="0000"
										   style="height: 12px;"></td>
							</tr>
							<tr>
								<td width="60" align="right"></td>
								<td><img id="change" src="${ctx}/vCode?w=80&h=30" />
									&nbsp;&nbsp;<a onClick="changeImg();" href="javascript:;">看不清？换一张</a>
								</td>
							</tr>
							<tr>
								<td></td>
								<td><a href="javascript:checkInput();" class="btn_style03">
												<span class="loginArea_body_line"
													  style="width: 70px; text-align: center;">登录</span>&nbsp;&nbsp;
								</a></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="loginArea_foot"></div>
				</div>
			</form>
		</div>

		<footer class="footer top10" style="width: 900px; margin: 0 auto;">
			<div class="foot_ctn">
				<div class="foot_tel">
					<%="".equals(hotline)?hotline:"<p class='bottom5'>教育服务热线</p><p class='tel_txt'>"+hotline+"</p>" %>
				</div>
				<div class="copyright">
					<%=zhuban %>
					<%=technicalSupport %>
					<%=copyright %>
				</div>
			</div>
		</footer>

	</div>

</body>
</html>
