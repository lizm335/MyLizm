<%@ page contentType="text/html; charset=UTF-8"%>

<!-- Main Header -->
<header class="main-header">
	<a href="${ctx}/home/manyClass" class="logo">
		<!-- mini logo for sidebar mini 50x50 pixels -->
		<span class="logo-mini"><i class="fa fa-fw fa-tv"></i></span>
		<!-- logo for regular state and mobile devices -->
		<span class="logo-lg"><i class="fa fa-fw fa-tv"></i>班主任平台</span>
	</a>
	<nav class="navbar navbar-static-top" role="navigation">  <!-- <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"> <span class="sr-only">首页</span> </a> -->
		<div class="pull-left">
			<span class="topbar-course-title">${currentClassInfo.bjmc}</span> <button class="btn btn-default btn-xs" data-role="change-class">切换班级</button>
		</div>
		<div class="navbar-custom-menu" style ="float:right !important">
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

				<li class="pull-right dropdown user user-menu">  
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						<c:choose>
							<c:when test="${not empty info.zp }">
								<img src="${info.zp}" class="user-image" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'" />
							</c:when>
							<c:otherwise>
								<img src="${ctx }/static/images/headImg04.png" class="user-image" alt="User Image"/>
							</c:otherwise>
						</c:choose>
						<span class="hidden-xs"> ${info.xm} </span></a>
					<ul class="dropdown-menu">
						<li role="presentation"><a role="menuitem" tabindex="-1" href="${ctx}/home/personal/updateInfo" data-id='personal-info' data-page-role="single-page" title="个人资料管理">编辑个人资料</a></li>
						<li role="presentation"><a href="${ctx}/home/class/report/list" data-page-role="single-page">工作日报周报</a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1" href="${ctx}/logout">退出平台</a></li>
					</ul>
				</li>
				<!-- Control Sidebar Toggle Button -->
				<!-- <li><a href="#" data-toggle="control-sidebar"> <i
                    class="fa fa-gears"></i>
                </a></li> -->
			</ul>
		</div>
		<ul class="first-level-menu">
		</ul>
	</nav>
</header>