<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body no-padding">
	<section class="content">
		<div class="nav-tabs-custom margin-bottom-none">
			<ul class="nav nav-tabs">
				<li class="active">
					<a href="${ctx}/admin/home/myWorkbench" target="_self">我的工作台</a>
				</li>
				<li>
					<a href="${ctx}/admin/home/statistical" target="_self">统计分析</a>
				</li>
			</ul>
			<div class="tab-content">
				<div class="row">
					<div class="col-md-6 col-sm-12">
						<div class="box box-border no-shadow">
							<div class="box-header with-border">
								<h3 class="box-title">代办事项（${todoList.size()}）</h3>

							</div>
							<div class="box-body" style="height: 214px;">
								<c:choose>
									<c:when test="${not empty todoList}">
										<div class="carousel slide" id="carousel-generic">
											<div class="carousel-inner">
												<ul class="list-unstyled agency-list">
													<c:forEach items="${todoList}" var="item">
														<li>
															<small class="label pull-left bg-red">${item.title}</small>
															<span>${item.content}</span>
															<a class="agency-link" href="${ctx}/${item.link}"><small>进入处理</small></a>
														</li>
													</c:forEach>
												</ul>
											</div>
											<div data-role="control">
												<a href="#carousel-generic" data-slide="prev"> <i class="fa fa-fw fa-angle-left gray6"></i>
												</a>
												<ul class="carousel-indicators no-margin" data-role="indicators"></ul>
												<a href="#carousel-generic" data-slide="next"> <i class="fa fa-fw fa-angle-right gray6"></i>
												</a>
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<table width="100%" height="100%">
											<tbody>
												<tr>
													<td>
														<div class="text-center gray9 f12">
															<i class="fa fa-fw fa-exclamation-circle f18"></i>
															<span>你当前没有待办事项，工作做得很棒！</span>
														</div>
													</td>
												</tr>
											</tbody>
										</table>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
					<div class="col-md-6 col-sm-12">
						<div class="box box-border no-shadow">
							<div class="box-header with-border">
								<h3 class="box-title">通知公告</h3>
								<div class="pull-right">
									<a href="${ctx}/admin/messageInfo/list">更多</a>
								</div>
							</div>
							<div class="box-body" style="height: 214px;">
								<c:choose>
									<c:when test="${not empty messageList}">
										<ul class="list-unstyled note-list">
											<c:forEach items="${messageList}" var="item">
												<li>
													<a href="${ctx}/admin/messageInfo/detail?id=${item[0]}"> <c:choose>
															<c:when test="${item[1] == 1}">
																<span class="pull-left">
																	<small class="label label-default text-no-bold">发送</small>
																</span>
															</c:when>
															<c:otherwise>
																<span class="pull-left">
																	<small class="label label-warning text-no-bold">接收</small>
																</span>
															</c:otherwise>
														</c:choose> <span class="pull-left">【${infoTypeMap[item[2]]}】</span> <time class="pull-right">
															<fmt:formatDate value="${item[4]}" type="date" pattern="yyyy-MM-dd" />
														</time> <span class="note-tit">${item[3]}
															<c:if test="${item[5] == '0'}">
					                        	&nbsp;&nbsp;<small class="label bg-red">new</small>
															</c:if>
														</span>
													</a>
												</li>
											</c:forEach>
										</ul>
									</c:when>
									<c:otherwise>
										<table width="100%" height="100%">
											<tbody>
												<tr>
													<td>
														<div class="text-center gray9 f12">
															<i class="fa fa-fw fa-exclamation-circle f18"></i>
															<span>
																你暂无通知，<a href="${ctx}/admin/messageInfo/create"><u>去发布一条新的通知</u></a>！
															</span>
														</div>
													</td>
												</tr>
											</tbody>
										</table>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<shiro:hasPermission name="/home/workOrder/list">
					<div class="col-md-6 col-sm-12">
						<div class="box box-border no-shadow">
							<div class="box-header with-border">
								<h3 class="box-title">我的工单</h3>
								<div class="pull-right">
				                  	<a href="${ctx }/home/workOrder/list">更多&gt;&gt;</a>
			                  </div>
							</div>
							<div class="box-body" style="height: 214px; text-align: center;">
								<c:choose>
									<c:when test="${not empty workOrderList}">
										<table class="table f12 vertical-middle text-center margin-bottom-none">
											<thead class="with-bg-gray">
												<tr>
													<th width="12%">级别</th>
													<th>标题</th>
													<th width="18%">要求完成时间</th>
													<th width="18%">创建人</th>
													<th width="13%">状态</th>
												</tr>
											</thead>
											<tbody class="my-task">
												<c:forEach items="${workOrderList }" var="info">
													<tr>
														<td><c:if test="${info.gjtWorkOrder.priority eq '1' }">
																<small class="label label-warning text-no-bold">优先</small>
															</c:if> <c:if test="${info.gjtWorkOrder.priority eq '0' }">
																<small class="label label-default text-no-bold">一般</small>
															</c:if> <c:if test="${info.gjtWorkOrder.priority eq '2' }">
																<small class="label label-danger text-no-bold">紧急</small>
															</c:if></td>
														<td>
															<div class="text-left">
																${info.gjtWorkOrder.title }
																<div class="gray9">${workTypeMap[info.gjtWorkOrder.workOrderType] }</div>
															</div>
														</td>
														<td>${info.gjtWorkOrder.demandDate }</td>
														<td>${info.gjtWorkOrder.createUser.realName}
															<div class="gray9">（${info.gjtWorkOrder.createUser.priRoleInfo.roleName}）</div>
														</td>
														<td><c:if test="${info.gjtWorkOrder.isState eq '0' }">
																<span class="text-orange">待完成</span>
															</c:if> <c:if test="${info.gjtWorkOrder.isState eq '1' }">
																<span class="text-green">已完成</span>
															</c:if> <c:if test="${info.gjtWorkOrder.isState eq '2' }">
																<span class="gray9">已关闭</span>
															</c:if></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</c:when>
									<c:otherwise>
										<table width="100%" height="100%">
											<tbody>
												<tr>
													<td>
														<div class="text-center gray9 f12">
															<i class="fa fa-fw fa-exclamation-circle f18"></i>
															<span>暂无指派给你的工单</span>
															<a href="${ctx }/home/workOrder/create"><u>去发布一个工单</u></a>!
														</div>
													</td>
												</tr>
											</tbody>
										</table>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
					</shiro:hasPermission>
					<shiro:hasPermission name="/myPlan/planList">
					<div class="col-md-6 col-sm-12">
						<div class="box box-border no-shadow">
							<div class="box-header with-border">
								<h3 class="box-title">我的计划</h3>
								<div class="pull-right">
									<a href="${ctx}/myPlan/planList">更多&gt;&gt;</a>
								</div>
							</div>
							<div class="box-body" style="height: 214px; text-align: center;" id="my-plan">
								<%--<img width="260px" height="200px" src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/waiting-for-open.png">--%>
							</div>
						</div>
					</div>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">

	//我的计划模块
	(function getPlan() {
		$.ajax({
			type:"post",
			dataType:"html",
			url:"${ctx}/myPlan/getPlan",
			success:function (data) {
				$("#my-plan").append(data);
            }
		});
    })();


window.onload=function(){
  top.$(".overlay-wrapper.loading").hide();
}

//代办事项 轮播效果
;(function(){
  var $carouselBox=$('#carousel-generic');
  var $liItems=$('.agency-list li',$carouselBox),
      totalNum=$liItems.length;
  var $indicators=$('[data-role="indicators"]',$carouselBox);
  if(totalNum <=0 ) {
	  $('[data-role="control"]',$carouselBox).hide();
	  return;
  }

  var pageSize=6,//分页大小
      pageCount=totalNum%pageSize==0?(totalNum/pageSize):(Math.floor(totalNum/pageSize)+1);//总页数
  //临时存放需要处理的结点容器
  var $tempContainer=$('<div/>');

  for (var i = 0; i < pageCount; i++) {
      var $liTemp=$('<div class="item '+(i==0?'active':'')+'"><ul class="list-unstyled agency-list"></ul></div>');

      $indicators.append( 
        $('<li/>',{
          'class':'fa fa-fw fa-circle-o '+(i==0?'active':''),
          'role':'button',
          'data-target':'#carousel-generic',
          'data-slide-to':i
        }) 
      );

      $tempContainer.append($liTemp);
      for (var j = 0; j < pageSize; j++) {
          var k=j+i*pageSize;
          if(k==$liItems.length) break;
          $liTemp.children('ul').append($liItems.eq(k).clone());
      };
  };

  $('.carousel-inner',$carouselBox).html($tempContainer.html());

  //小于2页时不轮播
  if(pageCount < 2){
    $('[data-role="control"]',$carouselBox).hide();
  } else {
	//启动轮播效果
	$carouselBox.carousel();
  }
  
})();

</script>


</body>
</html>
