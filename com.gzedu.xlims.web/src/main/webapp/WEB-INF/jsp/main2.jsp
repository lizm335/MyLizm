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
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap.min.css">
<link href="${ctx}/static/bootstrap/css/bootstrap-select.min.css"  rel="stylesheet">
<link href="${ctx}/static/bootstrap/css/bootstrap-table.css"  rel="stylesheet">
<!-- Font Awesome -->
<link rel="stylesheet" href="${ctx}/static/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="${ctx}/static/ionicons/2.0.1/css/ionicons.min.css">

<!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="${ctx}/static/dist/css/skins/skin-blue.min.css">
<!-- iCheck -->
<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/flat/blue.css">
<!-- Morris chart -->
<link rel="stylesheet" href="${ctx}/static/plugins/morris/morris.css">
<!-- jvectormap -->
<link rel="stylesheet" href="${ctx}/static/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
<!-- Date Picker -->
<link rel="stylesheet" href="${ctx}/static/plugins/datepicker/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet" href="${ctx}/static/plugins/daterangepicker/daterangepicker-bs3.css">
 <!-- Select2 -->
<link rel="stylesheet" href="${ctx}/static/plugins/select2/select2.min.css">
<!-- iCheck for checkboxes and radio inputs -->
<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet" href="${ctx}/static/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
<!-- DataTables -->
<link rel="stylesheet" href="${ctx}/static/plugins/datatables/dataTables.bootstrap.css">
<!-- Theme style -->
<link rel="stylesheet" href="${ctx}/static/dist/css/AdminLTE.min.css">
<!-- jquery-ui -->
<link rel="stylesheet" href="${ctx}/static/plugins/jQueryUI/css/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/static/dist/css/reset.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body class="hold-transition fixed skin-blue sidebar-mini">
<div class="wrapper"> 
  
  <!-- Left side column. contains the logo and sidebar -->
  <header class="main-header"> <a href="${ctx}/index" class="logo">
    <div class="logo-wrap"><img src="${ctx}/static/dist/img/images/logo.png"></div>
    <span class="logo-mini"><b>${user.loginAccount }</b></span> <span class="logo-lg"><b>广州远程教育管理平台</b></span> </a>
    <nav class="navbar navbar-static-top" role="navigation"> <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"> <span class="sr-only">首页</span> </a>
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
           <li class="first-level-wrap">
            <ul class="first-level-menu">
            	<c:forEach items="${modelInfoList}" var="model" varStatus="vs">
            		<c:if test="${model.isShow}">
            			<li data-id="${vs.index+1}">${model.modelName }</li>
            		</c:if>
            	</c:forEach>
            </ul>
            
            <div class="dropdown operion-more-menu"> 
            	<!-- <a class="dropdown-toggle" data-toggle="dropdown" href="#"> 更多 <i class="fa fa-fw fa-caret-down"></i> </a> -->
            	<ul class="dropdown-menu">
            	<c:forEach items="${modelInfoList}" var="model" varStatus="vs">
					 	<c:if test="${model.isShow}">
					 		<li data-id="${vs.index+1 }"> 
							${model.modelName }
							<ul class="menu-temp hide">
								<li class="header">${model.modelName}</li>
								<c:forEach items="${model.childModelList}" var="child">
									<c:if test="${child.isShow }">
										<c:choose>
											<c:when test="${child.isLeaf}">
												<li class="treeview">
													  <a href="${ctx}${child.modelAddress}">
														<i class="fa fa-circle-o"></i> <span>${child.modelName}</span> 
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
																<li><a href="${ctx}${child2.modelAddress}"><i class="fa fa-circle-o"></i>${child2.modelName}</a></li>
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
          
          <li class="dropdown messages-menu"> <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-envelope-o"></i> <span class="label label-success">4</span> </a>
            <ul class="dropdown-menu">
              <li class="header">You have 4 messages</li>
              <li> 
                <!-- inner menu: contains the actual data -->
                <ul class="menu">
                  <li><!-- start message --> 
                    <a href="#">
                    <div class="pull-left"> <img src="${ctx}/static/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image"> </div>
                    <h4> Support Team <small><i class="fa fa-clock-o"></i> 5 mins</small> </h4>
                    <p>Why not buy a new awesome theme?</p>
                    </a> </li>
                  <!-- end message -->
                  <li> <a href="#">
                    <div class="pull-left"> <img src="${ctx}/static/dist/img/user3-128x128.jpg" class="img-circle" alt="User Image"> </div>
                    <h4> AdminLTE Design Team <small><i class="fa fa-clock-o"></i> 2 hours</small> </h4>
                    <p>Why not buy a new awesome theme?</p>
                    </a> </li>
                  <li> <a href="#">
                    <div class="pull-left"> <img src="${ctx}/static/dist/img/user4-128x128.jpg"  alt="User Image"> </div>
                    <h4> Developers <small><i class="fa fa-clock-o"></i> Today</small> </h4>
                    <p>Why not buy a new awesome theme?</p>
                    </a> </li>
                  <li> <a href="#">
                    <div class="pull-left"> <img src="${ctx}/static/dist/img/user3-128x128.jpg" class="img-circle" alt="User Image"> </div>
                    <h4> Sales Department <small><i class="fa fa-clock-o"></i> Yesterday</small> </h4>
                    <p>Why not buy a new awesome theme?</p>
                    </a> </li>
                  <li> <a href="#">
                    <div class="pull-left"> <img src="${ctx}/static/dist/img/user4-128x128.jpg" class="img-circle" alt="User Image"> </div>
                    <h4> Reviewers <small><i class="fa fa-clock-o"></i> 2 days</small> </h4>
                    <p>Why not buy a new awesome theme?</p>
                    </a> </li>
                </ul>
              </li>
              <li class="footer"><a href="#">See All Messages</a></li>
            </ul>
          </li>
          <li class="dropdown notifications-menu"> <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-bell-o"></i> <span class="label label-warning">10</span> </a>
            <ul class="dropdown-menu">
              <li class="header">You have 10 notifications</li>
              <li> 
                <!-- inner menu: contains the actual data -->
                <ul class="menu">
                  <li> <a href="#"> <i class="fa fa-users text-aqua"></i> 5 new members joined today </a> </li>
                  <li> <a href="#"> <i class="fa fa-warning text-yellow"></i> Very long description here that may not fit into the page and may cause design problems </a> </li>
                  <li> <a href="#"> <i class="fa fa-users text-red"></i> 5 new members joined </a> </li>
                  <li> <a href="#"> <i class="fa fa-shopping-cart text-green"></i> 25 sales made </a> </li>
                  <li> <a href="#"> <i class="fa fa-user text-red"></i> You changed your username </a> </li>
                </ul>
              </li>
              <li class="footer"><a href="#">View all</a></li>
            </ul>
          </li>
          <!-- Tasks: style can be found in dropdown.less -->
          <li class="dropdown tasks-menu"> <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-flag-o"></i> <span class="label label-danger">9</span> </a>
            <ul class="dropdown-menu">
              <li class="header">You have 9 tasks</li>
              <li> 
                <!-- inner menu: contains the actual data -->
                <ul class="menu">
                  <li><!-- Task item --> 
                    <a href="#">
                    <h3> Design some buttons <small class="pull-right">20%</small> </h3>
                    <div class="progress xs">
                      <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100"> <span class="sr-only">20% Complete</span> </div>
                    </div>
                    </a> </li>
                  <!-- end task item -->
                  <li><!-- Task item --> 
                    <a href="#">
                    <h3> Create a nice theme <small class="pull-right">40%</small> </h3>
                    <div class="progress xs">
                      <div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100"> <span class="sr-only">40% Complete</span> </div>
                    </div>
                    </a> </li>
                  <!-- end task item -->
                  <li><!-- Task item --> 
                    <a href="#">
                    <h3> Some task I need to do <small class="pull-right">60%</small> </h3>
                    <div class="progress xs">
                      <div class="progress-bar progress-bar-red" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100"> <span class="sr-only">60% Complete</span> </div>
                    </div>
                    </a> </li>
                  <!-- end task item -->
                  <li><!-- Task item --> 
                    <a href="#">
                    <h3> Make beautiful transitions <small class="pull-right">80%</small> </h3>
                    <div class="progress xs">
                      <div class="progress-bar progress-bar-yellow" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100"> <span class="sr-only">80% Complete</span> </div>
                    </div>
                    </a> </li>
                  <!-- end task item -->
                </ul>
              </li>
              <li class="footer"> <a href="#">View all tasks</a> </li>
            </ul>
          </li>
          <li class="dropdown user user-menu"><a href="#"
						class="dropdown-toggle" data-toggle="dropdown"> <img
							src="${ctx}/static/dist/img/user2-160x160.jpg"
							class="user-image" alt="User Image"> <span class="hidden-xs"> ${user.loginAccount } </span> </a>
            <ul class="dropdown-menu">
              <!-- User image -->
              <li class="user-header"><img
								src="${ctx}/static/dist/img/user2-160x160.jpg"
								class="img-circle" alt="User Image">
                <p> XXXX
                  - Web Developer <small>Member since Nov. 2012</small> </p>
              </li>
              <!-- Menu Footer-->
              <li class="user-footer">
                <div class="pull-left"> <a href="#" class="btn btn-default btn-flat">我的资料</a> </div>
                <div class="pull-right"> <a href="${ctx }/logout" class="btn btn-default btn-flat">退出</a> </div>
              </li>
            </ul>
          </li>
          <!-- Control Sidebar Toggle Button --> 
          <!-- <li><a href="#" data-toggle="control-sidebar"> <i
							class="fa fa-gears"></i>
					</a></li> -->
        </ul>
      </div>
    </nav>
  </header>
  <aside class="main-sidebar">
    <section class="sidebar">
      <div class="user-panel">
        <div class="pull-left image"> <img src="${ctx}/static/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image"> </div>
        <div class="pull-left info">
          <p>${user.loginAccount }</p>
          <a href="#"><i class="fa fa-circle text-success"></i> 在线</a> </div>
      </div>
      <!-- search form (Optional) -->
      <form action="#" method="get" class="sidebar-form">
        <div class="input-group">
          <input type="text" name="q" class="form-control" placeholder="搜索...">
          <span class="input-group-btn">
          <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i></button>
          </span> </div>
      </form>
      <!-- /.search form -->
      <ul class="sidebar-menu"></ul>
    </section>
    <!-- /.sidebar --> 
  </aside>
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <div class="overlay-wrapper loading" style="display:none;">
    	<div class="overlay">
    		<i class="fa fa-refresh fa-spin" style="top:200px !important"></i>
    	</div>
    </div>
    
	<div id="msgBoxDIV" class="alert alert-success fade in tips-box" 
	style="position: absolute; top: 100px; text-align: center; display: none;">   
		<div class="no-margin f16"><i class="icon fa fa-check"></i> 保存成功！</div>
	</div> 
    
    <div class="page-tabs-box">
      <div class="page-tabs-content clearbox">
        <div class="dropdown page-group-control yahei"> <a class="dropdown-toggle" data-toggle="dropdown" href="#"> 关闭操作 <span class="caret"></span> </a>
          <ul class="dropdown-menu">
            <li role="presentation" class="close-all-tabs"><a role="menuitem" tabindex="-1" href="#">关闭全部选项卡</a></li>
            <li role="presentation" class="close-other-tabs"><a role="menuitem" tabindex="-1" href="#">关闭其他选项卡</a></li>
          </ul>
        </div>
        <div class="page-tabs-div clearbox">
          <div class="page-left-btn"><i class="fa fa-fw fa-angle-left"></i></div>
          <div class="page-right-btn"><i class="fa fa-fw fa-angle-right"></i></div>
          <div class="page-tabs">
            <ul class="clearbox">
              <li class="home-tab" data-id="0"><a href="#">首页</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="page" data-id="0">
        <iframe src="${ctx }/admin/home/index" frameborder="0" scrolling="auto" allowtransparency="true"></iframe>
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
<script src="${ctx}/static/plugins/jQuery/jQuery-2.1.4.min.js"></script> 
<!--<![endif]--> 

<!--[if lt IE 9]>
		<script src="plugins/jQuery/jquery-1.9.0.min.js"></script>
	<![endif]--> 

<script src="${ctx}/static/plugins/jQueryUI/jquery-ui.min.js"></script> 

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

<script src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script> 
<script src="${ctx}/static/bootstrap/js/bootstrap-select.js"></script> 
<script src="${ctx}/static/bootstrap/js/bootstrap-table.js"></script> 
<script src="${ctx}/static/bootstrap/js/bootstrap-table-zh-CN.js"></script> 
<script src="${ctx}/static/bootstrap/js/defaults-zh_CN.js"></script> 

<!-- date-range-picker --> 
<script src="${ctx}/static/ajax/libs/moment.js/2.10.2/moment.min.js"></script> 
<script src="${ctx}/static/plugins/daterangepicker/daterangepicker.js"></script> 
<!-- Select2 --> 
<script src="${ctx}/static/plugins/select2/select2.full.min.js"></script> 
<!-- 选项卡滚动 --> 
<script src="${ctx}/static/plugins/tabsSroll/sunny_scroll.js"></script> 
<!-- 省市区联动查询 --> 
<script src="${ctx}/static/plugins/cityselect/jsonData.js"></script> 
<script src="${ctx}/static/plugins/cityselect/citySelect.js"></script> 
<!-- DataTables --> 
<script src="${ctx}/static/plugins/datatables/jquery.dataTables.js"></script> 
<script src="${ctx}/static/plugins/datatables/dataTables.bootstrap.js"></script> 
<!-- iCheck 1.0.1 -->
<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
<!-- SlimScroll 1.3.0 --> 
<script src="${ctx}/static/plugins/slimScroll/jquery.slimscroll.min.js"></script> 
<!-- Morris.js charts -->
<script src="${ctx}/static/plugins/morris/raphael-min.js"></script>
<script src="${ctx}/static/plugins/morris/morris.min.js"></script>
<!-- ckeditor --> 
<script src="${ctx}/static/plugins/ckeditor/ckeditor.js"></script> 
<!-- AdminLTE App --> 
<script src="${ctx}/static/dist/js/app.min.js"></script>  
<!-- common js --> 
<script src="${ctx}/static/dist/js/common.js"></script>
<script src="${ctx}/static/dist/js/index.js"></script>
<script type="text/javascript">

</script>
</body>
</html>