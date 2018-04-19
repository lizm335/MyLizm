<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="../../../jsp/common/taglibs.jsp" %>
<!DOCTYPE HTML>
<%
	String  actionerrors=request.getAttribute("actionerrors")!=null?request.getAttribute("actionerrors").toString():"";
	request.removeAttribute("actionerrors");
	java.util.HashMap resultMap = (java.util.HashMap)request.getAttribute("resultMap");
	/* String cssPath = AppUtil.getCssImageServerPath(request,resultMap); */
	 String style = session.getAttribute("style") != null ? session
			.getAttribute("style").toString() : "admin";
	String p = "_temp";//css style
	String logosrc = "/static/images/login/ee_logo.png";
	String title = "综合管理平台";
	String name = "综合管理平台";
	String copyright = "2013  广州远程教育中心版权所有";
	//System.out.println("joho login_index.jsp style= "+style);
	if ("1".equals(style)) {//gzdd
		logosrc = "/static/images/login/logo_ougz.png";
		title = "终身学习体系教务管理系统";
		name = "终身学习体系教务管理系统";
		copyright = "2013  广州市广播电视大学";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%=title%></title>
<link rel="stylesheet" type="text/css" href="${css}/admin/style/login_default/base.css" />
<script type="text/javascript" src="${ctx }/static/common/jquery.js"></script>
<script type="text/javascript" src="${ctx }/static/js/login/login.js"></script>
<script type="text/javascript" src="${ctx }/static/common/placeholder.js"></script>
<!--[if lte IE 6]>
<script type="text/javascript" src="${css}/common/js/DD_belatedPNG_0.0.8a.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix('.bg_png, .login_contianer, .ico_lock, .ico_user, .login_header .left, .login_header .right, .login_header .mid, .login_header .mid1, .login_content .left, .login_content .right, .login_bottom .left, .login_bottom .right, .login_bottom .mid');
</script>
<![endif]-->

<script src="${ctx }/static/common/check105.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	var msg =  $('#msg').val();
	if(msg != ""){
		$(".error").addClass("prompt");
		$(".error").text(msg);
		$("#password").val("");
		$("#password").focus();
		$("#password_div").addClass("input_box_focus");
	}else{
		$("#username").focus();
		$("#username_div").addClass("input_box_focus");
	}
	
	
	
	 document.onclick = function(){ 
	  //$(".input_box").removeClass("input_box_focus"); 
	 }; 

	//模拟下拉层
	$(".triangle").click(function(){
		$(".selectoptions").show();
	});
	$(".selectitem a").click(function(){
		$(this).blur();
		var value = $(this).attr("rel");
		var txt = $(this).text();
		$(".selectvalue").text(txt).css("color","#474747");
		$(".selectoptions").hide();
		return false;
	});

	$(".selectvalue").click(function(){
		$(this).parent().parent().parent().click().addClass("input_box_focus");
		$(".selectoptions").show();	
	});

	$(document).click(function(event){
		if( $(event.target).attr("class") != "selectbox"){
			$(".selectoptions").hide();
		}
	});
	
	 $("#choose a").click(function(){  
			 $(this).addClass("cur").siblings().removeClass("cur");  
		 }); 
        hoverFn($("#choose a"),"hover_css",$("#mainBg img"),"url");    
        function hoverFn(obj,hc,content,url){
        obj.click(function(){
                var h_css=hc;
                $(this).addClass(h_css).siblings().removeClass(h_css);
                var imgUrl=$(this).attr(url);
                content.attr("src",imgUrl);             
            });
        }
	//alert error infomation.
	actionerrors();

});

//文本框焦点
$('.input_text').focus(function(event){
	$(".input_box_focus").removeClass("input_box_focus"); 
	$(this).parent().parent().parent().addClass("input_box_focus");
	$(".error").text("");
});

//文本框提示文字
function showtext() {
  if($("#password").val()=="") {
  $("#pwd_warpper").html("<input type='text' value='请输入密码' class='input_text common_input2' id='password' onfocus='showpassword();'/>");
   }
}

function showpassword() {
  $("#pwd_warpper").html("<input type='password' value='' id='password' class='input_text common_input2' onblur='showtext();' style='color:#474747'/>");
  $("#pwd_warpper").parent().parent().click().addClass("input_box_focus");
  $(".selectoptions").hide();
  setTimeout(function(){$("#password").focus();},20);
}

$(function(){
	var usernameDefStr = $("#code").val();
	$("#code").focus(function(){
	    if($(this).val()==usernameDefStr)
	        $(this).val("").css("color","#474747");
	});
	$("#code").blur(function(){
	    if($(this).val()=="")
	        $(this).val(usernameDefStr).css("color","#cbcbcb");
	});
});



function closehide(){
  $(".selectoptions").hide();
}

function userclick(){
    closehide();
	$("#user_warpper").parent().parent().click().addClass("input_box_focus");
}

function vercodeclick(){
    closehide();
	$("#vercode").parent().parent().click().addClass("input_box_focus");
}
</script>
<style type="text/css">
.login_header .mid1{font-size:30px;}
.login_header .logo{margin:30px 10px 0 0;padding:10px 20px 0 0;*padding:10px 20px 0 0;height:60px;vertical-align:middle;}
.login_header .text{margin:30px 0 0 0;height:60px;line-height:60px;vertical-align:middle;}
</style>
</head>

<body>


<style type="text/css">
#choose{height:25px;background:#333;text-indent:10px;color:#fff;}
#choose a{color:#fff;margin-right:10px;line-heieght:25px;}
#choose a:hover{color:#3C3;}
#choose a.cur{color:#F90;}
.mess{
				width: 120px;
				height: 60px;
				background-color: #F7F7F7;
				display: block;
				margin: 30px 0 0 250px;
				vertical-align: middle;
				border-radius:5px;
				font-size: 18px;
				text-decoration:none;
				line-height:60px;
				color: #666666;
				text-align: center;
				text-shadow: 0 1px 0 #fff;
}
</style>

<!--选择皮肤-->

<form name="loginForm" action="${ctx}/login"  method="post" onsubmit="return Validator.Validate(this, 2)">
<input type="hidden" id="namespace" name="namespace" value="${namespace}">
<input type="hidden" id="msg" name="msg" value="${msg}">

<div class="body_container" id="bodyContainer">
	<div class="loading" id="loading"></div>
    <div class="login_main hide" id="loginMain">
    	<div class="main_contianer">
        <div class="login_contianer">
           
            <div class="login_header">
                <div class="left"></div>
                <div class="right"></div>
                <div class="mid1">
                	<c:if test="${empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginHeadLogo }">
                		<span class="logo" style="width:408px; overflow:hidden;height:80px; margin-top: 20px;">
                			<img src="${css}/xueli_oucnet_cn/portal/v1.0/images/logo.png" class="bg_png" />
                		</span>
                	</c:if>
                	<c:if test="${not empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginHeadLogo }">
                		<span class="logo" style="width:408px; overflow:hidden;height:80px; margin-top: 20px;">
                			<img src="${sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginHeadLogo }" class="bg_png" />
                		</span>
                	</c:if>
					<span class="text" style="margin: 26px 0px 0 15px; font-size: 26px;">${not empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT && not empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginTitle ? sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginTitle : '教学教务管理平台'}</span>
                </div>
            </div>
            
            <div class="login_content">
                <div class="left"></div>
                <div class="right"></div>
                <div class="mid">
                    <div class="content_inner">
                        <div class="row_wrapper">
                            <!--用户名(长)-->
                            <div class="input_box float_left" id="username_div">
                                <span>
                                    <i class="icon ico_user margin_r5"></i><ins id="user_warpper"><input name="username" type="text" class="input_text common_input2"  id="username" value="" placeholder="请输入用户名"/></ins>
                                </span>
                            </div>
                           
                      </div>
                    
                     <div class="row_wrapper" >
                        <!--密码-->
                        <div class="input_box" style="*float:left;" id="password_div">
                            <span>
                                  <i class="icon ico_lock margin_r5"></i><ins id="pwd_warpper"><input name="password" type="password" class="input_text common_input2" id="password" value="" datatype="Require;" msg="用户密码不能为空"  placeholder="请输入密码"/></ins>
                            </span>
                        </div>
                     </div>
                    
                    <div class="row_wrapper">
                    			<input name="formMap.userType" type="hidden" value="-1" />
			                    <input name="formMap.screen"  id="formMap.screen" type="hidden" />
                      <!--验证码-->
                      <%-- <div class="input_box float_left" style="*float:left;">
                          <span>
                              <div id="vercode"><input type="text" class="input_text common_input1" placeholder="请输入短信验证码" onfocus="vercodeclick()" id="smscode" /></div>
                          </span>
                      </div>
                      <div><a href="#" class="mess" onclick="javascript:sendSMS();return false;">点击发送短信</a></div> --%>
                      <!--验证码-->
                    </div>
                    
                    <p class="row_wrapper">
                    <!-- <input type="button"  class="login_in bg_png"     /> -->
                    <a href="#" class="login_in bg_png" onclick="javascript:login();return false;"></a>
                     
                    </p>
                    <p class="error" hidden="hidden">帐号或密码错误！</p>
                    </div>
                    
                 </div>
            </div>
            <div class="login_bottom">
                <div class="left"></div>
                <div class="right"></div>
                <div class="mid"></div>
            </div>
        </div>
        <div>
        <c:if test="${empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginFooterCopyright }">
        	<p class="clearfix" style="width:120px; margin:10px auto 0; color:#666;"><span class="fl" style="margin-right:5px;"><img class="bg_png fl" src="${css}/common/logo/eenet_logo_small.png" /><span class="fl ">技术支持</span></span></p>
        	<p class="copyright">增值电信业务经营许可证编号粤B2-20042063 | 粤ICP备08131364号</p>
		</c:if>
		<c:if test="${not empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginFooterCopyright }">
			${sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginFooterCopyright }
		</c:if>
		</div>
    </div>
    </div>
 
    
    <div class="bg_main hide" id="mainBg">
        <img src="${ctx}/static/images/login/bg_main01.jpg" orgwidth="1920" orgheight="1024" />
    </div>
    
</div>
</form>
</body>
</html>
<script type="text/javascript">

	if(window !=top){  
	    top.location.href=location.href;  
	}  

	function sendSMS(){
		console.log("ok");
	}
    function login()
    {
       var username= $('#username').val();
       var password =$('#password').val();
       if(username.length <= 0){
    	   $("#username").focus().select();
    	   return;
       } else if(password.length <=0){
    	   $("#password").focus().select();
    	   return;
       }
       $(".input_box_focus").removeClass("input_box_focus"); 
       document.loginForm["formMap.screen"].value=screen.width+"x"+screen.height;  
       document.loginForm.submit();     
    } 
    
    function  reset()
    {
      document.loginForm.reset();
    }
    
    document.onkeydown=function(){
    	if(window.event.keyCode==13){
    		login();
    	}
    };
    
    function  actionerrors()
    {
      var  err='<%=actionerrors %>';
      if(err!=''){
          alert(err);
       }
    }
</script>