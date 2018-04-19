<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<header class="main-header">
	<!-- Header Navbar -->
	<a href="${ctx}/index" class="logo">
			<div class="logo-wrap"><img src="${ctx }/static/dist/img/images/logo.png"></div>
			<span class="logo-mini"><b>BOSS</b></span>
			<span class="logo-lg"><b>广州远程教育管理平台</b></span> 
		</a> 
		<nav class="navbar navbar-static-top" role="navigation"> 
			<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
				<span class="sr-only">首页</span>
			</a>
			<div class="navbar-custom-menu">
				<ul class="nav navbar-nav">
					<!-- Notifications: style can be found in dropdown.less -->
					<!-- Messages: style can be found in dropdown.less-->
				  <li class="first-level-wrap">
						<ul class="first-level-menu">
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
				  </li>
				  <li class="dropdown messages-menu">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					  <i class="fa fa-envelope-o"></i>
					  <span class="label label-success">4</span>
					</a>
					<ul class="dropdown-menu">
					  <li class="header">You have 4 messages</li>
					  <li>
						<!-- inner menu: contains the actual data -->
						<ul class="menu">
						  <li><!-- start message -->
							<a href="#">
							  <div class="pull-left">
								<img src="${ctx }/static/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
							  </div>
							  <h4>
								Support Team
								<small><i class="fa fa-clock-o"></i> 5 mins</small>
							  </h4>
							  <p>Why not buy a new awesome theme?</p>
							</a>
						  </li><!-- end message -->
						  <li>
							<a href="#">
							  <div class="pull-left">
								<img src="${ctx }/static/dist/img/user3-128x128.jpg" class="img-circle" alt="User Image">
							  </div>
							  <h4>
								AdminLTE Design Team
								<small><i class="fa fa-clock-o"></i> 2 hours</small>
							  </h4>
							  <p>Why not buy a new awesome theme?</p>
							</a>
						  </li>
						  <li>
							<a href="#">
							  <div class="pull-left">
								<img src="${ctx }/static/dist/img/user4-128x128.jpg" class="img-circle" alt="User Image">
							  </div>
							  <h4>
								Developers
								<small><i class="fa fa-clock-o"></i> Today</small>
							  </h4>
							  <p>Why not buy a new awesome theme?</p>
							</a>
						  </li>
						  <li>
							<a href="#">
							  <div class="pull-left">
								<img src="${ctx }/static/dist/img/user3-128x128.jpg" class="img-circle" alt="User Image">
							  </div>
							  <h4>
								Sales Department
								<small><i class="fa fa-clock-o"></i> Yesterday</small>
							  </h4>
							  <p>Why not buy a new awesome theme?</p>
							</a>
						  </li>
						  <li>
							<a href="#">
							  <div class="pull-left">
								<img src="${ctx }/static/dist/img/user4-128x128.jpg" class="img-circle" alt="User Image">
							  </div>
							  <h4>
								Reviewers
								<small><i class="fa fa-clock-o"></i> 2 days</small>
							  </h4>
							  <p>Why not buy a new awesome theme?</p>
							</a>
						  </li>
						</ul>
					  </li>
					  <li class="footer"><a href="#">See All Messages</a></li>
					</ul>
				  </li>
				  <li class="dropdown notifications-menu">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					  <i class="fa fa-bell-o"></i>
					  <span class="label label-warning">10</span>
					</a>
					<ul class="dropdown-menu">
					  <li class="header">You have 10 notifications</li>
					  <li>
						<!-- inner menu: contains the actual data -->
						<ul class="menu">
						  <li>
							<a href="#">
							  <i class="fa fa-users text-aqua"></i> 5 new members joined today
							</a>
						  </li>
						  <li>
							<a href="#">
							  <i class="fa fa-warning text-yellow"></i> Very long description here that may not fit into the page and may cause design problems
							</a>
						  </li>
						  <li>
							<a href="#">
							  <i class="fa fa-users text-red"></i> 5 new members joined
							</a>
						  </li>
						  <li>
							<a href="#">
							  <i class="fa fa-shopping-cart text-green"></i> 25 sales made
							</a>
						  </li>
						  <li>
							<a href="#">
							  <i class="fa fa-user text-red"></i> You changed your username
							</a>
						  </li>
						</ul>
					  </li>
					  <li class="footer"><a href="#">View all</a></li>
					</ul>
				  </li>
				  <!-- Tasks: style can be found in dropdown.less -->
				  <li class="dropdown tasks-menu">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					  <i class="fa fa-flag-o"></i>
					  <span class="label label-danger">9</span>
					</a>
					<ul class="dropdown-menu">
					  <li class="header">You have 9 tasks</li>
					  <li>
						<!-- inner menu: contains the actual data -->
						<ul class="menu">
						  <li><!-- Task item -->
							<a href="#">
							  <h3>
								Design some buttons
								<small class="pull-right">20%</small>
							  </h3>
							  <div class="progress xs">
								<div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
								  <span class="sr-only">20% Complete</span>
								</div>
							  </div>
							</a>
						  </li><!-- end task item -->
						  <li><!-- Task item -->
							<a href="#">
							  <h3>
								Create a nice theme
								<small class="pull-right">40%</small>
							  </h3>
							  <div class="progress xs">
								<div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
								  <span class="sr-only">40% Complete</span>
								</div>
							  </div>
							</a>
						  </li><!-- end task item -->
						  <li><!-- Task item -->
							<a href="#">
							  <h3>
								Some task I need to do
								<small class="pull-right">60%</small>
							  </h3>
							  <div class="progress xs">
								<div class="progress-bar progress-bar-red" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
								  <span class="sr-only">60% Complete</span>
								</div>
							  </div>
							</a>
						  </li><!-- end task item -->
						  <li><!-- Task item -->
							<a href="#">
							  <h3>
								Make beautiful transitions
								<small class="pull-right">80%</small>
							  </h3>
							  <div class="progress xs">
								<div class="progress-bar progress-bar-yellow" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
								  <span class="sr-only">80% Complete</span>
								</div>
							  </div>
							</a>
						  </li><!-- end task item -->
						</ul>
					  </li>
					  <li class="footer">
						<a href="#">View all tasks</a>
					  </li>
					</ul>
				  </li>
				  
					<li class="dropdown user user-menu"><a href="#"
						class="dropdown-toggle" data-toggle="dropdown"> <img
							src="${ctx }/static/dist/img/user2-160x160.jpg"
							class="user-image" alt="User Image"> <span class="hidden-xs"> ${user.loginAccount} </span>
					</a>
						<ul class="dropdown-menu">
							<!-- User image -->
							<li class="user-header"><img
								src="${ctx }/static/dist/img/user2-160x160.jpg"
								class="img-circle" alt="User Image">
								<p>
									XXXX
									- Web Developer <small>Member since Nov. 2012</small>
								</p></li>
							<!-- Menu Footer-->
							<li class="user-footer">
								<div class="pull-left">
									<a href="#" class="btn btn-default btn-flat">我的资料</a>
								</div>
								<div class="pull-right">
									<a href="${ctx }/logout" class="btn btn-default btn-flat">退出</a>
								</div>
							</li>
						</ul></li>
					<!-- Control Sidebar Toggle Button -->
					<!-- <li><a href="#" data-toggle="control-sidebar"> <i
							class="fa fa-gears"></i>
					</a></li> -->
				</ul>
			</div>
		</nav>
</header>