<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-教职工列表</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<h1>
			<c:if test="${employeeType==10}">论文教师管理</c:if>
		</h1>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">
				<c:if test="${employeeType==10}">论文教师管理</c:if>
			</li>
		</ol>
	</section>


	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html">
		<input type="hidden" name="employeeType" value="${employeeType}"/>
		<div class="box">
			<div class="box-body">
				<div class="row reset-form-horizontal clearbox">
				<div class="col-md-4">
							<label class="control-label col-sm-3">帐号</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_EQ_gjtUserAccount.loginAccount"
								value="${param['search_EQ_gjtUserAccount.loginAccount']}">
							</div>
					</div>
					<div class="col-md-4">
							<label class="control-label col-sm-3">姓名</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm}">
							</div>
					</div>
					<div class="col-md-4">
						<label class="control-label col-sm-3">学习中心</label>
						<div class="col-sm-9">
							<select name="search_EQ_xxzxId" class="selectpicker show-tick form-control" 	data-size="10" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${studyCenterMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_xxzxId']}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="row reset-form-horizontal clearbox">
					<div class="col-md-4">
							<label class="control-label col-sm-3">院校</label>
							<div class="col-sm-9">
								<select name="search_EQ_xxId" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${schoolInfoMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_xxId']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
				</div>
			</div>
			<div class="box-footer">
<!-- 					<div class="search-more-in">
					高级搜索<i class="fa fa-fw fa-caret-down"></i>
				</div> -->
				<div class="btn-wrap">
					<button type="button" class="btn btn-default">重置</button>
				</div>
				<div class="btn-wrap">
					<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
				</div>
			</div>
		</div>
	

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box">
			<div class="box-header with-border">
				<div class="fr">
	<!-- 				<div class="btn-wrap fl">
						<button class="btn btn-block btn-success btn-outport">
							<i class="fa fa-fw fa-sign-out"></i> 批量导出
						</button>
					</div> -->
					<div class="btn-wrap fl">
						<a href="create?employeeType=${employeeType}" class="btn btn-default btn-sm">
								<i class="fa fa-fw fa-plus"></i> 新增</a>
					</div>
					<div class="btn-wrap fl">
						<a href="javascript:void(0);" class="btn btn-block btn-danger btn-del del-checked">
							<i class="fa fa-fw fa-trash-o"></i> 删除
						</a>
					</div>
				</div>
			</div>
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
										<th><input type="checkbox" class="select-all" id="selectAll"></th>
										<th>帐号</th>
										<th>姓名</th>
										<th>性别</th>
										<th>教师类别</th>
										<th>办公电话</th>
										<th>手机号码</th>
										<th>学习中心</th>
										<th>所属院校</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pageInfo.getContent() }" var="item">
										<tr>
											<td><input type="checkbox" value="${item.employeeId }"	name="ids" class="checkbox"></td>
											<td>${item.gjtUserAccount.loginAccount}</td>
											<td>${item.xm}</td>
											<td><dic:getLabel typeCode="Sex" code="${item.xbm }" /></td>
											<td>
												<c:forEach var="p" items="${item.gjtEmployeePositionList}">
													<c:if test="${p.id.type==11}">论文指导教师，</c:if>
													<c:if test="${p.id.type==12}">论文答辩教师，</c:if>
													<c:if test="${p.id.type==13}">社会实践教师，</c:if>
												</c:forEach>
											</td>
											<td>${item.lxdh}</td>
											<td>${item.sjh}</td>
											<td>${studyCenterMap[item.xxzxId]}</td>
											<td>${schoolInfoMap[item.xxId]}</td>
											<td>
												<div class="data-operion">
													<a href="update/${item.employeeId}?employeeType=${employeeType}"
														class="operion-item operion-edit" title="编辑">
														<i class="fa fa-fw fa-edit"></i></a>
													<a href="view/${item.employeeId}?employeeType=${employeeType}"
														class="operion-item operion-view" title="查看">
														<i class="fa fa-fw fa-eye"></i></a>
													<a href="javascript:void(0);"
														class="operion-item operion-del del-one" val="${item.employeeId}"
														title="删除" data-tempTitle="删除">
														<i class="fa fa-fw fa-trash-o"></i></a>
												</div>
											</td>
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
		</form>
	</section>
</body>
</html>