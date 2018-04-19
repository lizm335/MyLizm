<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
		 
		<!-- Main content -->
		 <section class="content-header">
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">用户管理</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal" method="post">
				<div class="box">
					<div class="box-body">
						
						<div class="row pad-t15">
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">帐号</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_loginAccount" value="${param.search_LIKE_loginAccount}">
									</div>
								</div>
							</div>
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">姓名</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_realName" value="${param.search_LIKE_realName}">
									</div>
								</div>
							</div>
							<div class="col-md-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">角色</label>
									<div class="col-sm-9">
										<select name="search_EQ_priRoleInfo.roleId" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
											<option value="">全部</option>
											<c:forEach items="${roles}" var="role">
												<option value="${role.key}" <c:if test="${role.key == param['search_EQ_priRoleInfo.roleId']}">selected = 'selected'</c:if>>${role.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
							  <div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">所属机构</label>
								<div class="col-sm-9">
								  <select name="search_EQ_orgId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<option value="">全部</option>
									<c:forEach items="${orgMap}" var="org">
										<option value="${org.key}"  <c:if test="${org.key==param['search_EQ_orgId']}">selected='selected'</c:if>>${org.value}</option>
									</c:forEach>
								 </select>
								</div>
							  </div>
							</div>
						</div>
					</div>
					<div class="box-footer text-right">
						<button type="submit" class="btn min-width-90px btn-primary margin_r10">搜索</button>
	    				<button type="reset" class="btn min-width-90px btn-default">重置</button>
					</div>
				</div>
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box margin-bottom-none">
					<div class="box-header with-border">
							<div class="pull-right no-margin">
								<c:if test="${isBtnCreate }">
									<a href="create" class="btn btn-default btn-sm">
										<i class="fa fa-fw fa-plus"></i> 新增用户</a>
								</c:if>
							</div>
					</div>

					<div class="box-body">
						<div class="table-responsive">
							<table class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
								<thead>
									<tr>
										<th>帐号</th>
										<th>姓名</th>
										<th>所属机构</th>
										<th>角色</th>
										<th>联系电话</th>
										<th>状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${not empty pageInfo.content}">
											<c:forEach items="${pageInfo.content}" var="entity">
												<c:if test="${not empty entity}">
													<tr>
														<td>${entity.loginAccount}</td>
														<td>${entity.realName}</td>
														<td>${entity.orgName}</td>
														<td>${entity.roleName}</td>
														<td>${entity.sjh}</td>
														<c:choose>
															<c:when test="${entity.isEnabled == 1}">
																<td class="text-green">启用</td>
															</c:when>
															<c:otherwise>
																<td class="text-red">停用</td>
															</c:otherwise>
														</c:choose>
														<td>
															<div class="data-operion">
																<c:if test="${isBtnUpdate }">
																	<a href="update/${entity.id}"
																		class="operion-item operion-edit" title="编辑">
																		<i class="fa fa-fw fa-edit"></i></a>
																</c:if>
																
																<c:if test="${isBtnReset }">	
																	<a href="#" class="operion-item operion-view" val="${entity.id}" data-toggle="tooltip" title="重置密码" data-role="reset-psw">
																	<i class="fa fa-fw fa-psw-reset"></i>
																	</a>
																</c:if>
																
																<c:if test="${isBtnDelete }">
																	<c:if test="${entity.isEnabled != 1}">
																	<a href="javascript:void(0);"
																		class="operion-item operion-del del-one" val="${entity.id}"
																		title="删除" data-tempTitle="删除">
																		<i class="fa fa-fw fa-trash-o text-red"></i></a>
																	</c:if>
																</c:if>
															</div>
														</td> 
													</tr>
												</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td align="center" colspan="7">暂无数据</td>
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
		

<script type="text/javascript">
$(function() {
	
	$("[data-role='reset-psw']").click(function(){
		 var $this = $(this);
		 var id=$(this).attr('val');
          $.confirm({
              title: '提示',
              content: '是否将密码重置为888888？',
              confirmButton: '确认',
              icon: 'fa fa-warning',
              cancelButton: '取消',  
              confirmButtonClass: 'btn-primary',
              closeIcon: true,
              closeIconClass: 'fa fa-close',
              confirm: function () { 
              	 $.post("resetPassword",{id:id},function(data){
              		alert(data.message);
              },"json"); 
              } 
          });
      });
	
})
</script>

</body>
</html>
