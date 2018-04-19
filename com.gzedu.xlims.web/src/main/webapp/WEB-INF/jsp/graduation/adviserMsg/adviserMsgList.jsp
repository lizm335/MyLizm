<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
})
</script>

</head>
<body class="inner-page-body">
		
	<!-- Main content -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">论文管理</a></li>
			<li class="active">通知管理</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal">
			<input type="hidden" name="adviserType" value="${param.adviserType}">
			<div class="box">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">账号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_teacher.gjtUserAccount.loginAccount" value="${param['search_LIKE_teacher.gjtUserAccount.loginAccount']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_teacher.xm" value="${param['search_LIKE_teacher.xm']}">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer">
					<div class="btn-wrap">
						<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
			</div>
			
			
			
			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			
			<div class="nav-tabs-custom">
				<div class="pull-right margin_r10 margin_t10">
					<a href="create?adviserType=${param.adviserType}" class="btn btn-default min-width-90px">
						<i class="fa fa-fw fa-plus"></i> 新增</a>
				</div>
				<ul class="nav nav-tabs nav-tabs-lg">
			      <li <c:if test="${param.adviserType == 1}">class="active"</c:if>><a href="list?adviserType=1" target="_self">论文指导老师</a></li>
			      <li <c:if test="${param.adviserType == 2}">class="active"</c:if>><a href="list?adviserType=2" target="_self">社会实践老师</a></li>
			    </ul>
			    
			    <div class="tab-content">
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
								<tr>
								    <th>账号</th>
								    <th>姓名</th>
								    <th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.content}" var="entity">
											<c:if test="${not empty entity}">
												<tr>
													<td>
														${entity.teacher.gjtUserAccount.loginAccount}
													</td>
													<td>
														${entity.teacher.xm}
													</td>
													<td>
														<div class="data-operion">
															<a href="javascript:void(0);"
																class="operion-item operion-del del-one" val="${entity.id}"
																title="删除" data-tempTitle="删除">
																<i class="fa fa-fw fa-trash-o"></i></a>
														</div>
													</td> 
												</tr>
											</c:if>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td align="center" colspan="3">暂无数据</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>
