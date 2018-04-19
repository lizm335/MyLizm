<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>准考证信息</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {

})

</script>

</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试管理</a></li>
		<li class="active">准考证信息</li>
	</ol>
</section>
		
<section class="content">
	<form id="listForm" class="form-horizontal" action="listAdmissionTicket">
		<!-- 搜索栏 start --> 
		<div class="box">
			<div class="box-body"> 
				<div class="row reset-form-horizontal clearbox">
					<div class="col-md-4">
						<label class="control-label col-sm-3">姓名</label>
						<div class="col-sm-9">
							<input class="form-control" type="text" name="search_LIKE_xm"   value="${param['search_LIKE_xm']}">
						</div>
					</div>
					
					<div class="col-md-4">
						<label class="control-label col-sm-3">学号</label>
						<div class="col-sm-9">
							<input class="form-control" type="text" name="search_LIKE_xh"   value="${param['search_LIKE_xh']}">
						</div>
					</div>
				</div>
			</div>
			
			<div class="box-footer text-right">
	          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
	          <button type="button" class="btn min-width-90px btn-default" id="btn-reset">重置</button>
	        </div>
		</div>
		<!-- 搜索栏 end --> 
		 
		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		
		
		<div class="box box-border margin-bottom-none">
			<!-- 列表内容 start -->
			<div class="box-body">
				<div class="table-responsive">
					<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
						<thead>
							<tr> 
								<th>学号</th>
								<th>姓名</th>
								<th>卷号</th>
								<th>科目名称</th>
								<th>考试形式</th>
								<th>考试日期</th>
								<th>考试时间</th> 
								<th>考场及座位号</th>
								<th>考点</th>
								<th>考点地址</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageInfo.content}" var="entity">
								<c:if test="${not empty entity}">
									<tr>
										<td>
											${entity.XH}
										</td>
										<td>
											${entity.XM}
										</td>
										<td>
											${entity.EXAM_NO}
										</td>
										<td>
											${entity.COURSE_NAME}
										</td>
										<td>
											${entity.EXAM_TYPE}
										</td>
										<td>
											${entity.EXAM_DATE}
										</td>														
										<td>
											${entity.EXAM_TIME}
										</td>
										<td>
											${entity.EXAM_SEAT_NUMBER}
										</td>
										<td>
											${entity.EXAM_PONIT}
										</td>
										<td>
											${entity.EXAM_POINT_ADDRESS}
										</td> 
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
					<tags:pagination page="${pageInfo}" paginationSize="13" />
				</div>
			</div>
					
		</div>
	</form>
</section>
	
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>

</html>
