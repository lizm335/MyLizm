<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="fixed skin-blue">
	<div class="wrapper">
		<header class="main-header">
			<a href="${ctx}/home/manyClass" class="logo"> <span class="logo-mini">
					<i class="fa fa-fw fa-tv"></i>
				</span> <span class="logo-lg">
					<i class="fa fa-fw fa-tv"></i>班主任平台
				</span>
			</a>
			<nav class="navbar navbar-static-top" role="navigation">
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<li class="first-level-wrap">
							<div class="dropdown operion-more-menu">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#"> 更多 <i class="fa fa-fw fa-caret-down"></i>
								</a>

								<ul class="dropdown-menu">
									<li>
										<a role="menuitem" href="homepage.html">班级首页</a>
										<ul class="menu-temp hide">
											<li class="header">班级首页</li>
											<li class="treeview">
												<a href="homepage.html"> <i class="fa fa-home"></i> <span>班级首页</span></a>
											</li>
											<li class="treeview">
												<a href="即将开放页面.html"> <i class="fa fa-home"></i> <span>即将开放页面</span></a>
											</li>
										</ul>
									</li>
									<li>
										<a role="menuitem" href="#">教学教务服务</a>
										<ul class="menu-temp hide">
											<li class="header">教学教务服务</li>
											<li class="treeview">
												<a href="学员资料.html"> <i class="fa fa-file-text-o f16"></i> <span>学员管理</span></a>
											</li>
											<li class="treeview">
												<a href="学员学籍.html"> <i class="fa fa-credit-card f16"></i> <span>学籍信息</span></a>
											</li>
											<li class="treeview">
												<a href="教材管理.html"> <i class="fa fa-paste f16"></i> <span>教材管理</span></a>
											</li>
										</ul>
									</li>
									<li>
										<a role="menuitem" href="#">学习管理</a>
										<ul class="menu-temp hide">
											<li class="header">学习管理</li>
											<li class="treeview">
												<a href="学员考勤.html"> <i class="fa fa-calendar-check-o f16"></i> <span>学员考勤</span></a>
											</li>
											<li class="treeview">
												<a href="学员学情.html"> <i class="fa fa-signal f16"></i> <span>学员学情</span></a>
											</li>
											<li class="treeview">
												<a href="成绩查询.html"> <i class="fa fa-newspaper-o f14"></i> <span>成绩与学分</span></a>
											</li>
											<li class="treeview">
												<a href="考试管理.html"> <i class="fa fa-dating"></i> <span>考试预约</span></a>
											</li>
											<li class="treeview">
												<a href="毕业管理.html"> <i class="fa fa-photo f16"></i> <span>论文管理</span></a>
											</li>
										</ul>
									</li>
									<li>
										<a role="menuitem" href="#">班级服务</a>
										<ul class="menu-temp hide">
											<li class="header">班级服务</li>
											<li class="treeview">
												<a href="答疑管理.html"> <i class="fa fa-question-circle"></i> <span>答疑管理</span></a>
											</li>
											<li class="treeview">
												<a href="通知公告.html"> <i class="fa fa-envelope-o f16"></i> <span>通知公告</span></a>
											</li>
											<li class="treeview">
												<a href="督促提醒.html"> <i class="fa fa-invoices"></i> <span>督促提醒</span></a>
											</li>
											<li class="treeview">
												<a href="服务记录.html"> <i class="fa fa-service"></i> <span>服务记录</span></a>
											</li>
											<li class="treeview">
												<a href="班级活动.html"> <i class="fa fa-activity"></i> <span>班级活动</span></a>
											</li>
										</ul>
									</li>
								</ul>
							</div>
						</li>
						<li class="pull-right dropdown user user-menu">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown"> <c:choose>
									<c:when test="${not empty info.zp }">
										<img src="${info.zp}" class="user-image" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'" />
									</c:when>
									<c:otherwise>
										<img src="${ctx }/static/images/headImg04.png" class="user-image" alt="User Image" />
									</c:otherwise>
								</c:choose> <span class="hidden-xs"> ${info.xm} </span></a>
							<ul class="dropdown-menu">
								<%-- <li role="presentation">
									<a role="menuitem" tabindex="-1" href="${ctx}/home/personal/updateInfo" data-id='personal-info' data-page-role="single-page" title="个人资料管理">编辑个人资料</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/home/class/report/list" data-page-role="single-page">工作日报周报</a>
								</li> --%>
								<li role="presentation">
									<a role="menuitem" tabindex="-1" href="${ctx}/logout">退出平台</a>
								</li>
							</ul>
						</li>
					</ul>
					</li>
					</ul>
				</div>
				<ul class="first-level-menu">
					<li onclick="location.href='${ctx}/home/manyClass'">班级列表</li>
					<li class="cur">答疑辅导</li>
				</ul>
			</nav>
		</header>
		<div class="content-wrapper no-margin">
			<div class="scroll-box">
				<div class="pad">
					<div class="nav-tabs-custom no-shadow margin-bottom-none">
						<ul class="nav nav-tabs nav-tabs-lg">
							<li>
								<a href="${ctx}/home/manyFeedSubject?type=N">待解答疑问(${noSolve})</a>
							</li>
							<li>
								<a href="${ctx}/home/manyFeedSubject?type=Y">已解答疑问(${yesSolve})</a>
							</li>
							<li>
								<a href="${ctx}/home/manyFeedSubject?type=F">转出去的答疑(${forwardCount})</a>
							</li>
							<li class="active">
								<a href="${ctx}/home/manyFeedSubject?type=E">常见问题(${oftenCount})</a>
							</li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_1">
								<div class="box box-border no-shadow margin-bottom-none">
									<div class="box-body pad-t5">
										<form id="listForm" action="${ctx }/home/manyFeedSubject">
											<input type="hidden" name="type" value="${type}">
											<div class="clearfix faq-list-bar">
												<div class="search-group-box">
													<div class="form-inline">
														<div class="form-group">
															<select  class="form-control margin_r5 select2" name="search_EQ_oftenType" >
																<option value="" >全部</option>
																<c:forEach items="${oftenTypeMap }" var="map">
																	<option value="${map.key }" <c:if test="${param.search_EQ_oftenType eq map.key }">selected</c:if>>${map.value }</option>
																</c:forEach>
															</select>
														</div>
														<div class="form-group">
															<input type="text" name="search_LIKE_subjectTitle" 
															value="${param.search_LIKE_subjectTitle}" class="form-control"
															placeholder="问题标题" >
														</div>
														<div class="form-group">
															<input type="text" name="search_LIKE_isCommendType" 
															value="${param.search_LIKE_isCommendType}" class="form-control"
															placeholder="常见问题标签" >
														</div>
														<div class="form-group">
															<button type="submit" class="btn btn-default margin_r5">搜索</button>
															<button type="button" class="btn btn-default" data-role="add-faq">发布新的常见问题</button>
														</div>
													</div>
												</div>
											</div>
										</form>
										<ul class="list-unstyled news-list clearfix">
											<c:choose>
												<c:when test="${not empty pageInfo && pageInfo.numberOfElements > 0}">
													<c:forEach items="${pageInfo.content}" var="info">
														<c:if test="${not empty info}">
															<li class="news-item question-item pad-l15 pad-r15">
																<div class="the-topic-ask">
																	<h4 class="text-yellow text-bold f16 no-margin pad-t5">
																		<span class="label label-warning">提问</span>
																		${info.title}
																	</h4>
																	<div class="txt">${info.content}</div>

																	<c:if test="${not empty info.imgUrls }">
																		<ul class="list-inline img-gallery">
																			<c:forEach items="${info.imgUrls }" var="picture">
																				<li>
																					<img src="${picture }"   data-role="lightbox" role="button">
																				</li>
																			</c:forEach>
																		</ul>
																	</c:if>
																</div>
																<c:if test="${not empty info.replyList }">
																	<c:forEach items="${info.replyList }" var="item" begin="0" end="0">
																		<div class="the-topic-ans margin_t20">
																			<span class="label label-primary">回答</span>
																			<div class="txt">${item.content }</div>
																			<c:if test="${not empty item.imgUrls }">
																				<ul class="list-inline img-gallery">
																					<c:forEach items="${item.imgUrls }" var="teacherPicture">
																						<li>
																							<img src="${teacherPicture }"   data-role="lightbox" role="button">
																						</li>
																					</c:forEach>
																				</ul>
																			</c:if>
																			<div class="margin_t15">
																				<c:if test="${info.isComm eq 'Y' && not empty info.isState}">
																					<button type="button" class="btn min-width-90px btn-default" data-role="cancel-faq" data-id='${info.id }'>取消设置常见问题</button>
																				</c:if>
																				<c:if test="${empty info.isState}">
																					<button type="button" class="btn min-width-90px btn-default" data-role="update-faq" data-id='${info.id }'>修改</button>
																					<button type="button" class="btn min-width-90px btn-default" data-role="delete-faq" data-id='${info.id }'>删除</button>
																				</c:if>
																			</div>
																			<ul class="list-unstyled margin_t10 f12 gray9">
													                            <li>
													                            	<span class="margin_r15">类型：${oftenTypeMap[info.oftenType] } 
													                            	</span>    来自：${info.questionSourcePath }
													                            </li>
													                            <li>
																					<span class="margin_r15">标签：
																						<c:forEach items="${info.isCommendType } " var="item">
																							<font color="red">${item }</font>   
																						</c:forEach>
																					</span>
																				</li>
													                          </ul>
																		</div>
																	</c:forEach>
																</c:if>
															</li>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<li class="news-item question-item" style="text-align: center;">暂无数据</li>
												</c:otherwise>
											</c:choose>
										</ul>
										<div style="padding: 10px 0 10px 0;">
											<tags:pagination page="${pageInfo}" paginationSize="5" />
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
<script type="text/javascript">
	
	// 发布新的常见问题
	$('[data-role="add-faq"]').click(function(event) {
	  event.preventDefault();
	  $.mydialog({
	    id:'add-faq',
	    width:700,
	    height:($(window).height()<600?$(window).height()*0.95:600),
	    zIndex:11000,
	    content: 'url: ${ctx}/home/class/newFeedback/createOften.html'
	  });
	});
	
	// 修改常见问题
	$('[data-role="update-faq"]').click(function(event) {
		var $id=$(this).data('id');
	  event.preventDefault();
	  $.mydialog({
	    id:'update-faq',
	    width:700,
	    height:($(window).height()<600?$(window).height()*0.95:600),
	    zIndex:11000,
	    content: 'url: ${ctx}/home/class/newFeedback/updateOften?id='+$id
	  });
	});

	
	//取消设置常见问题
	$('[data-role="cancel-faq"]').click(function(event) {
	  var $id=$(this).data('id');
	  $.alertDialog({
	    id:'cancel-faq',
	        width:400,
	        height:280,
	        zIndex:11000,
	        ok:function(){//“确定”按钮的回调方法
	          //这里 this 指向弹窗对象
	          $.post('${ctx}/home/class/newFeedback/setOften.html',{id:$id,state:'N'},function(data){
					if(data.successful){
						alert(data.message);
						window.location.reload();
					}
	          },'json'); 
	          $.closeDialog(this);
	        },
	        content:[ '<div class="text-center">',
	          '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
	              '<span class="f16 inline-block vertical-mid text-left">',
	                '是否确认取消设置该常见问题？', '</span>', '</div>'].join('')
	  })
	});
	
	
	//删除常见问题
	$('[data-role="delete-faq"]').click(function(event) {
	  var $id=$(this).data('id');
	  $.alertDialog({
	    id:'cancel-faq',
	        width:400,
	        height:280,
	        zIndex:11000,
	        ok:function(){//“确定”按钮的回调方法
	          //这里 this 指向弹窗对象
	          $.get('${ctx}/home/class/newFeedback/delete.html',{id:$id},function(data){
					if(data.successful){
						alert(data.message);
						window.location.reload();
					}
	          },'json'); 
	          $.closeDialog(this);
	        },
	        content:[ '<div class="text-center">',
	          '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
	              '<span class="f16 inline-block vertical-mid text-left">',
	                '是否删除数据？', '</span>', '</div>'].join('')
	  })
	});
	
		//简略文字的控制
		function summaryControl() {
			$('.question-item .txt').each(function(index, ele) {
				ele = $(ele);
				if (ele.height() > 40 && ele.children(".dotdot").length <= 0) {
					ele.height(40).append($("<span>", {
						"class" : "dotdot",
						html : "...<em>[ + ]</em>"
					}));
				}
			});

		};

		$(".nav-tabs").on('shown.bs.tab', 'a', function(event) {
			event.preventDefault();
			summaryControl();
		});

		$(window).on('resize load', function(event) {
			summaryControl();
		});

		$(".question-item").on("click", '.dotdot', function() {
			if ($(this).parent().height() == 40) {
				$(this).html("<em>[ - ]</em>").parent().css("height", "auto");
			} else {
				$(this).html("...<em>[ + ]</em>").parent().height(40)
			}
		});
		
		//下拉样式
		$('.select2').select2();
	</script>
</html>
