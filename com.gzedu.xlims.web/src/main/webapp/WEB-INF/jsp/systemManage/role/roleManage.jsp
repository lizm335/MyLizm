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
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" id="btn-back" onclick="history.back()">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">系统管理</a>
			</li>
			<li class="active">角色控制管理</li>
		</ol>
	</section>
	<section class="content">
		<form id="inputForm" role="form" action="${ctx }/system/role/updateRoleManage" method="post">
			<div class="box no-margin">
				<div class="box-body pad20">
					<div class="row">
						<input  type="hidden" name="id" value="${entity.roleId }">
						<table class="table table-bordered table-striped table-container">
							<thead>
								<th>角色名称</th>
								<th>管理权限</th>
								<th>工单下发权限</th>
							</thead>
							<tbody>
								<c:forEach items="${roleList }" var="item">
									<tr>
										<td>${item.roleName }</td>
										<td><input type="checkbox" class="minimal" value="${item.roleId }" name="roleIds" <c:if test="${item.isCheck}">checked="checked"</c:if>></td>
										<td><input type="checkbox" class="minimal" value="${item.roleId }" name="workRoleIds"
											<c:if test="${item.isWorkCheck}">checked="checked"</c:if>></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="box-footer no-pad-left no-pad-right form-group">
						<div class="col-sm-offset-10 col-sm-6">
							<button type="submit" class="btn btn-primary margin_r10" data-role="save">确定</button>
							<button type="button" class="btn btn-default" onclick="history.back()">取消</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>