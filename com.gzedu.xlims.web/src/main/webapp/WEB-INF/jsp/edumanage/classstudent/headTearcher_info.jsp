<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs_new.jsp"%>
</head>
<body class="inner-page-body">

	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.go(-1)">返回</button>
		<ol class="breadcrumb oh">
			<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">教务管理</a></li>
			<li><a href="${ctx }/edumanage/teachclass/list">教务班级</a></li>
			<li class="active">班主任</li>
		</ol>
	</section>

	<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li <c:if test="${action=='student'}">class="active"</c:if>>
				<a href="${ctx }/edumanage/classstudent/list?action=student&classId=${classId}">班级学员</a></li>
				<li <c:if test="${action=='headTeacher'}">class="active"</c:if>>
				<a href="${ctx }/edumanage/classstudent/queryHeadTeacher.html?action=headTeacher&classId=${classId}">班主任</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="tab_top_1">

					<div class="box box-border margin-bottom-none">
						<div class="box-header with-border">
							<h3 class="box-title pad-t5">班主任信息</h3>
								<shiro:hasPermission name="/edumanage/teachclass/list$allocation">
								<div class="pull-right">
									<a href="${ctx }/edumanage/teachclass/setHeadTeacher.html?classId=${classId}&classType=teach" role="button"
										class="btn btn-default btn-sm" data-role="configure-teacher">分配班主任</a>
								</div>
								</shiro:hasPermission>
						</div>
						<div class="box-body">
							<div class="table-responsive">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
										<tr>
											<th>账号</th>
											<th>姓名</th>
											<th>班级</th>
											<th>登录次数</th>
											<!-- <th>登录时长<br>（小时）
											</th> -->
											<th>操作</th>
										</tr>
									</thead>
										<c:choose>
											<c:when test="${not empty gjtEmployeeInfo &&gjtEmployeeInfo!=null}">
												<tr>
													
													<td>${gjtEmployeeInfo.gjtUserAccount.loginAccount }</td>
													<td>${gjtEmployeeInfo.xm}</td>
													<td>${classInfo.bjmc }</td>
													<td>${gjtEmployeeInfo.gjtUserAccount.loginCount }</td>
													<!-- <td>数据不知道哪里来</td> -->
													<td>
														<shiro:hasPermission name="/edumanage/teachclass/list$headTearchSimulatio">
														 <a href="${ctx }/usermanage/headteacher/simulation?id=${gjtEmployeeInfo.employeeId}&cid=${classId}"
														  target="_blank" class="operion-item" data-toggle="tooltip" title="模拟登录">
														 <i class="fa fa-fw fa-simulated-login"></i></a>
														</shiro:hasPermission>						 
													 </td>
												</tr>	
											</c:when>
											<c:otherwise>
												<tr>
													<td align="center" colspan="10">暂无数据</td>
												</tr>
											</c:otherwise>
										</c:choose>
							
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- 底部 -->
		<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
    
    $('[data-role="configure-teacher"]').click(function(event) {
    	event.preventDefault();
      	$.mydialog({
    	  id:'configure-teacher',
    	  width:760,
    	  height:500,
    	  zIndex:11000,
    	  content: 'url:'+$(this).attr('href')
    	});
    });

    
    

    // filter tabs
    $(".filter-tabs li").click(function(event) {
        if($(this).hasClass('actived')){
            $(this).removeClass('actived');
        }
        else{
            $(this).addClass('actived');
        }
    });

</script>
</body>
</html>

