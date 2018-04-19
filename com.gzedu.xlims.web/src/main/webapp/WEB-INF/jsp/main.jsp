<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="../jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>办学管理系统</title>
    
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.5 -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/bootstrap/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${css_nocdn}/ouchgzee_com/platform/xllms_css/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/skins/skin-blue.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/AdminLTE.min.css">
<link rel="stylesheet" href="${css}/ouchgzee_com/platform/xllms_css/dist/css/reset.css">
    
</head>
<body class="hold-transition fixed skin-blue sidebar-mini">
<div class="wrapper"> 
  
  <!-- Left side column. contains the logo and sidebar -->
  <header class="main-header"> 
  	<a href="${ctx}/admin/home/main" class="logo">
	    <span class="logo-mini"><i class="fa fa-fw fa-tv"></i></span>
	    <span class="logo-lg"><i class="fa fa-fw fa-tv"></i>${not empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT && not empty sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginTitle ? sessionScope.LOGIN_NAMESPACE_COPYRIGHT.loginTitle : '教学教务管理平台'}</span>
	</a>
    <nav class="navbar navbar-static-top" role="navigation"> <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"> <span class="sr-only">首页</span> </a>
		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">
			  <!-- Notifications: style can be found in dropdown.less --> 
			  <!-- Messages: style can be found in dropdown.less-->
			  <li class="first-level-wrap">
				<div class="dropdown operion-more-menu"> <a class="dropdown-toggle" data-toggle="dropdown" href="#"> 更多 <i class="fa fa-fw fa-caret-down"></i> </a>
				  <ul class="dropdown-menu">
					<!-- <li> <a role="menuitem" href="homepage.html">首页</a>
					  <ul class="menu-temp hide">
						<li class="header">首页</li>
						<li class="treeview"> <a href="homepage.html"> <i class="fa fa-home"></i> <span>首页</span></a>
						</li>
						<li class="treeview"> <a href="通知公告.html"> <i class="fa fa-volume-up"></i> <span>通知公告</span></a>
						</li>
						<li class="treeview"> <a href="规则设置.html"> <i class="fa fa-rule-set"></i> <span>规则设置</span></a>
						</li>
						<li class="treeview"> <a href="文章管理.html"> <i class="fa fa-article-manage"></i> <span>文章管理</span></a>
						</li>
					  </ul>
					</li> -->
					
					<c:forEach items="${modelInfoList}" var="model" varStatus="vs">
					 	<c:if test="${model.isShow}">
					 		<li> <a role="menuitem" href="homepage.html">${model.modelName }</a>
							<ul class="menu-temp hide">
								<li class="header">${model.modelName}</li>
								<c:forEach items="${model.childModelList}" var="child">
									<c:if test="${child.isShow }">
										<c:choose>
											<c:when test="${child.isLeaf}">
												<li class="treeview">
													  <a href="${ctx}${child.modelAddress}">
														<i class="${child.modelCode }"></i> <span>${child.modelName}</span> 
													  </a>
												</li>
											</c:when>
											<c:otherwise>
												<li class="treeview">
													  <a href="#">
														<i class="fa fa-dashboard"></i> <span>${child.modelName}</span> <i class="fa fa-angle-left pull-right"></i>
													  </a>
													  <ul class="treeview-menu">
													  	<c:forEach items="${child.childModelList}" var="child2">
													  		<c:if test="${child2.isShow }">
																<li><a href="${ctx}${child2.modelAddress}"><i class="${child.modelCode }"></i>${child2.modelName}</a></li>
													  		</c:if>
														</c:forEach>
													  </ul>
												</li>
											</c:otherwise>
										</c:choose>
									</c:if>
							 </c:forEach>
							</ul>
						</li> 
					 	</c:if>
	                </c:forEach>
					
				  </ul>
				</div>
			  </li>
			  
			  <li class="dropdown user user-menu">
			  	<c:set var="avatar" value="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png"></c:set>
			  	<c:if test="${not empty sessionScope.student_info.avatar}">
			  		<c:set var="avatar" value="${sessionScope.student_info.avatar}"></c:set>
			  	</c:if>
			  	<a href="#" class="dropdown-toggle" data-toggle="dropdown"> 
			  		<img src="${avatar }" class="user-image" alt="User Image">
			  		<span class="hidden-xs"> ${user.realName } </span> 
			  	</a>
				
				<ul class="dropdown-menu full-width" style="min-width:0;">
	              <li><a href="#" id="myInformation">我的资料</a></li>
	              <li><a href="${ctx }/logout">退出</a></li>
	            </ul>
			  </li>
			</ul>
		</div>
		<ul class="first-level-menu">
		</ul>
	</nav>
  </header>
  <aside class="main-sidebar">
    <section class="sidebar">

      <ul class="sidebar-menu"></ul>
    </section>
    <!-- /.sidebar --> 
  </aside>
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper pos-rel">
    <div class="overlay-wrapper loading" style="display: hidden;"><div class="overlay"><i class="fa fa-refresh fa-spin" style="top:200px !important"></i></div></div>
    <!-- <div class="page-tabs-box">
      <div class="page-tabs-content clearfix">
        <div class="dropdown page-group-control yahei"> <a class="dropdown-toggle" data-toggle="dropdown" href="#"> 关闭操作 <span class="caret"></span> </a>
          <ul class="dropdown-menu">
            <li role="presentation" class="close-all-tabs"><a role="menuitem" tabindex="-1" href="#">关闭全部选项卡</a></li>
            <li role="presentation" class="close-other-tabs"><a role="menuitem" tabindex="-1" href="#">关闭其他选项卡</a></li>
          </ul>
        </div>
        <div class="page-tabs-div clearfix">
          <div class="page-left-btn"><i class="fa fa-fw fa-angle-left"></i></div>
          <div class="page-right-btn"><i class="fa fa-fw fa-angle-right"></i></div>
          <div class="page-tabs">
            <ul class="clearfix">
              <li class="home-tab" data-id="0"><a href="#">首页</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div> -->
    <div class="page" data-id="0" style="min-height:300px;">
        <iframe id="iframe"  frameborder="0" scrolling="auto" allowtransparency="true"></iframe>
    </div>
  </div>
</div>
<!-- ./wrapper --> 


<!-- IE8及以下的浏览器弹出提示窗口 -->
<div id="dialog-message" title="温馨提示" style="display:none">
  <p style="padding:20px 10px;margin:0;"> 本管理系统支持IE9以上版本浏览器，为了获取更好的体验，建议
    使用Firefox、Chrome、Opera等浏览器。 </p>
</div>
<!-- jQuery 2.1.4 --> 
<!--[if gte IE 9]>
	<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
	<![endif]--> 

<!--[if !IE]><!--> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQuery/jQuery-2.1.4.min.js"></script> 
<!--<![endif]--> 

<!--[if lt IE 9]>
		<script src="plugins/jQuery/jquery-1.9.0.min.js"></script>
	<![endif]--> 

<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/jQueryUI/jquery-ui.min.js"></script> 

<!--[if lt IE 9]>
	<script>
		$(function() {
		var dialog=$( "#dialog-message" ).dialog({
		  modal: true,
		  width: 600,
		  create: function( event, ui ) {
			//$("html").css("overflow","hidden")
		  },
		  buttons: {
			'我知道了': function() {
			  $( this ).dialog( "close" );
			}
		  }
		}).dialog( "instance" );
		dialog.uiDialog.css("z-index",10001);
		dialog.overlay.css("z-index",10000);
		});
	</script>
	<![endif]--> 

<script src="${css}/ouchgzee_com/platform/xllms_css/bootstrap/js/bootstrap.min.js"></script> 
<!-- SlimScroll 1.3.0 --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/slimScroll/jquery.slimscroll.min.js"></script> 
 
<!-- AdminLTE App --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/dist/js/app.min.js"></script>  
<!-- common js --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/dist/js/common.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/dist/js/index.js"></script>
<script type="text/javascript" src="${css}/ouchgzee_com/platform/xllms_css/plugins/custom-model/custom-model.js"></script>
<script type="text/javascript">

$("[data-id='1_1']").parent().addClass("active").siblings('.treeview').removeClass('active');
$(".sidebar-menu [data-id='1_1']").click();
$("#myInformation").bind("click",function() {
	$('.operion-more-menu > ul > li a').each(function(){
		var t = $(this);
		if($.isFunction(t.html)){
			console.info($.isFunction(t.html));
			if(t.html() == "个人设置") {
				t.trigger('click');
			}
		}
	});
});

function loginTimeout(){
	var h=280;
	var $loginPop=$.mydialog({
		id:'login-timeout',
		width:380,
		height:h,
		backdrop:false,
		fade:true,
		showCloseIco:false,
		zIndex:11000,
		content: $('#login-timeout-temp').html()
	});
	
	$('.box',$loginPop).height(h);
	$('.box-body',$loginPop).height(h-126);

	//确定
	$('[data-role="sure"]',$loginPop).click(function(event) {
		$('.ver-tips').hide();
		var flag =true;
		if($.trim($('#username').val())==''){
			$('#username-tip').show();
			flag=false;
		}
		if($.trim($('#password').val())==''){
			$('#password-tip').show();
			flag=false;
		}
		if(!flag){
			return;
		}
		$.post('${ctx}/ajax/login',{
		    username:$('#username').val(),
		    password:$('#password').val()
		},function(data){
			if(data.successful){
			    $.closeDialog($loginPop);
			}else{
			    $('#error-tip').show();
				$('#error-tip .error').text(data.message);
			}
		},'json');
		//$.closeDialog($loginPop);
	});
}

</script>

<script type="text/template" id="login-timeout-temp">
	<form id="login-timeout-form"> 
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">
					登录
					<small class="gray9">（你已登录超时，请重新登陆）</small>
				</h3>
			</div>
			<div class="box-body scroll-box">
		        <table class="table no-border">
		        	<tr>
		        		<th width="100" class="text-right">
		        			<div class="pad-t5">
		        				用户名
		        			</div>
		        		</th>
		        		<td>
		        			<div class="position-relative">
				                <input type="text" id="username" class="form-control" placeholder="请填写用户名">
		        			</div>
		        			<div class="ver-tips text-left text-red f12" style="display:none" id="username-tip">
	      						<span class="glyphicon glyphicon-remove-circle vertical-mid"></span>
	      						<span class="inline-block vertical-mid">请填写用户名</span>
	      					</div>
		        		</td>
		        		<td width="50"></td>
		        	</tr>
		        	<tr>
		        		<th class="text-right">
		        			<div class="pad-t5">
		        				密码
		        			</div>
		        		</th>
		        		<td>
		        			<div class="position-relative">
				                <input type="password" id="password" class="form-control" placeholder="请填写密码">
		        			</div>
		        			<div class="ver-tips text-left text-red f12 " style="display:none" id="password-tip" >
	      						<span class="glyphicon glyphicon-remove-circle vertical-mid"></span>
	      						<span class="inline-block vertical-mid">请填写密码</span>
	      					</div>
							<div class="ver-tips text-left text-red f12 " style="display:none" id="error-tip" >
	      						<span class="glyphicon glyphicon-remove-circle vertical-mid"></span>
	      						<span class="inline-block vertical-mid error"></span>
	      					</div>
		        		</td>
		        		<td></td>
		        	</tr>
		        </table>
			</div>
			<div class="box-footer text-right" style="position:fixed;bottom:0;left:0;width:100%;">
				<button type="button" class="btn btn-success min-width-90px" data-role="sure">确定</button>
			</div>
		</div>
	</form>
</script>

</body>
</html>