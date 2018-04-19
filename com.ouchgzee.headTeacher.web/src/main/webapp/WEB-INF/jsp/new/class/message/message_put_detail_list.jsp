<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>我发布的通知</title>
  <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
<script type="text/javascript">
</script>

</head>
<body class="inner-page-body">
	<form id="listForm" >
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">消息接收列表</h3>
			</div>
			<div class="scroll-box">
				<div class="box-body">
					<div class="row">
						<div class="col-xs-2 no-pad-right">
							<select name="isRead" class="form-control">
								<option value="">全部状态</option>
								<option value="0" <c:if test="${param.isRead=='0'}">selected</c:if>>未读</option>
								<option value="1" <c:if test="${param.isRead=='1'}">selected</c:if>>已读</option>
							</select>
						</div>
						<div class="col-xs-4">
							<div class="media">
								<div class="media-body pad-r5">
									<input type="text" class="form-control" name="userName" value="${param.userName }" placeholder="搜索姓名/学号/帐号"> <input type="hidden"
										name="id" value="${messageId }">
								</div>
								<div class="media-right">
									<button class="btn btn-block btn-primary">搜索</button>
								</div>
							</div>
						</div>
					</div>
					<table class="table-gray-th text-center margin_t15 margin_b15">
						<thead>
							<tr>
								<th>帐号</th>
								<th>姓名</th>
								<th>状态</th>
								<th>时间</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty pageInfo.content}">
									<c:forEach items="${pageInfo.content}" var="info">
										<tr>
											<td>${info.LOGIN_ACCOUNT}</td>
											<td>${info.REAL_NAME}</td>
											<td>${info.IS_ENABLED=='1'?'已读':'未读'}</td>
											<td>
												<c:if test="${not empty  info.UPDATED_DT }">
													<fmt:formatDate value="${info.UPDATED_DT}" pattern="yyyy-MM-dd HH:mm:ss"/>
												</c:if>
										</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td align="center" colspan="4">暂无数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
				<tags:pagination page="${pageInfo}" paginationSize="5" />
			</div>
		</div>
		<div class="text-right pop-btn-box pad">
			<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
		</div>
	</form>
	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
	<script type="text/javascript">
	/*不需要置顶按钮*/
	$('.jump-top').remove();
	
	//关闭 弹窗
	$("[data-role='close-pop']").click(function(event) {
		parent.$.closeDialog(frameElement.api)
	});

	//设置内容主体高度
	$('.scroll-box').height($(frameElement).height()-106);

	</script>
</body>
</html>
