<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

</head>
<body>

	<div class="box">
			<div class="box-body">
				<div id="dtable_wrapper"
					class="dataTables_wrapper form-inline dt-bootstrap no-footer">
					<div class="row">
						<div class="col-sm-6"></div>
						<div class="col-sm-6"></div>
					</div>

					<div class="row">
						<div class="col-sm-12">
							<table id="dtable"
								class="table table-bordered table-striped table-container">
								<thead>
									<tr>
										<th><input type="checkbox" class="select-all"
											id="selectAll"></th>
										<th>姓名</th>
										<th>账号</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pageInfo.getContent() }" var="item">
										<tr>
											<td><input type="checkbox" value="${item.employeeId}"
												name="ids" class="checkbox chk-item"></td>
											<td class="teacher-name">${item.xm }</td>
											<td>${item.gjtUserAccount.loginAccount}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<tags:pagination page="${pageInfo}" paginationSize="5" />
				</div>
			</div>
		</div>

</body>
</html>
