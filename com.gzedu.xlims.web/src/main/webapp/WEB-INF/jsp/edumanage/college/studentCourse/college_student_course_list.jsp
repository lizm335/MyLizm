<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-学员选课</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
		$(function() {

			$("[data-role='import']").click(function(event) {
				$.mydialog({
					id:'import',
					width:600,
					height:415,
					zIndex:11000,
					content: 'url:toImport'
				});
			});

			$(".search").click(function(event) {
				event.preventDefault;
				$("#schoolId").val("");
				$("#listForm").submit();
			});

		});
	</script>
</head>
<body class="inner-page-body">
	<section class="content-header clearfix">
		<ol class="breadcrumb oh">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">教学管理</a></li>
			<li class="active">学员选课</li>
		</ol>
	</section>

	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html" method="post">
			<input id="xjzt" type="hidden" name="search_EQ_xjzt" value="${param.search_EQ_xjzt}">
			<input id="schoolId" type="hidden" name="search_schoolId">
			<div class="box">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_xh" value="${param.search_EQ_xh }">
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">姓名</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm }">
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">专业名称</label>
								<div class="col-sm-9">
									<select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${specialtyMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_specialtyId}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>

						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">专业层次</label>
								<div class="col-sm-9">
									<select  name="search_EQ_pycc"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${pyccMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param['search_EQ_pycc']}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">入学年级</label>
								<div class="col-sm-9">
									<select  name="search_EQ_yearId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${yearMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param['search_EQ_yearId'] }">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">入学学期</label>
								<div class="col-sm-9">
									<select  name="search_EQ_viewStudentInfo.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${gradeMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==((not empty param['search_EQ_viewStudentInfo.gradeId']) ? param['search_EQ_viewStudentInfo.gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</div>

					<div id="more-search" class="row collapse <c:if test="${not empty param.search_EQ_kchkcmc }">in</c:if>">
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">课程</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_kchkcmc" value="${param.search_EQ_kchkcmc }" placeholder="搜索课程名称或代码">
								</div>
							</div>
						</div>
					</div>

				</div><!-- /.box-body -->
				<div class="box-footer text-right">
					<button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
					<button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
					<div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">高级搜索<i class="fa fa-fw fa-angle-up <c:if test="${not empty param.search_EQ_kchkcmc }">fa-angle-down</c:if>"></i> </div>
				</div><!-- /.box-footer-->
			</div>

			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">选课列表</h3>
					<div class="pull-right no-margin">
						<shiro:hasPermission name="/edumanage/studentCourseCollege/list$import">
						<a class="btn btn-default btn-sm margin_l10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 导入选课</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="box-body">
					<div class="table-responsive" style="overflow: hidden">
						<table class="table table-bordered table-striped vertical-mid table-font">
							<thead>
							<tr>
								<!-- <th><input type="checkbox" class="select-all" id="selectAll"></th> -->
								<th width="70" class="text-center">头像</th>
								<th width="20%" class="text-center">个人信息</th>
								<th width="20%" class="text-center">报读信息</th>
								<th width="20%" class="text-center">学期</th>
								<th class="text-center">课程</th>
								<th width="200" class="text-center">选课时间</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${pageInfo.content }" var="student">
								<tr>
										<%-- <td><input type="checkbox" value="${student.studentId }" name="ids" class="checkbox"></td> --%>
									<td class="text-center">
										<img src="${not empty student.avatar ? student.avatar : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>
										<c:if test="${empty student.avatar}">
											<p class="gray9">未上传</p>
										</c:if>
									</td>
									<td>
										姓名：${student.xm}<br/>
										学号：${student.xh}<br/>
										<shiro:hasPermission name="/personal/index$privacyJurisdiction">
										手机号：${student.sjh}
										</shiro:hasPermission>
									</td>
									<td>
										报读专业：${student.specialtyName}<br/>
										专业层次：<dic:getLabel typeCode="TrainingLevel" code="${student.pycc }" /><br/>
										入学年级：${student.yearName}
									</td>
									<td class="text-center">
										${student.gradeName}
									</td>
									<td class="text-center">
										${student.kcmc} <br>
										（${student.kch}）
									</td>
									<td class="text-center">
										<fmt:formatDate value="${student.takeCourseCreatedDt}" type="both" />
									</td>
								</tr>
							</c:forEach>
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
		//高级搜索
		$("#more-search").on('shown.bs.collapse', function(event) {
			event.preventDefault();
			$('[data-target="#more-search"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
		}).on('hidden.bs.collapse', function(event) {
			event.preventDefault();
			$('[data-target="#more-search"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
		});
	</script>
</body>
</html>
