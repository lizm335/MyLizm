<%@page import="com.gzedu.xlims.pojo.system.StudyYear"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<c:set var="ctx">${pageContext.request.contextPath}</c:set>

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
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">毕业管理</a></li>
			<li class="active">毕业计划</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal">
			<input type="hidden" name="search_EQ_auditState" value="${param.search_EQ_auditState}">
			<div class="box box-border">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xh" value="${param['search_LIKE_gjtStudentInfo.xh']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">是否申请学位</label>
								<div class="col-sm-9">
									<select name="search_EQ_applyDegree" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
											<option value="1"  <c:if test="${param.search_EQ_applyDegree eq '1' }">selected='selected'</c:if>>申请</option>
											<option value="0"  <c:if test="${param.search_EQ_applyDegree eq '0' }">selected='selected'</c:if>>不申请</option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">层次</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${pyccMap}" var="trainingLevel">
											<option value="${trainingLevel.key}"  <c:if test="${trainingLevel.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if>>${trainingLevel.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学期</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.nj" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${gradeMap}" var="grade">
											<option value="${grade.key}"  <c:if test="${grade.key==param['search_EQ_gjtStudentInfo.nj']}">selected='selected'</c:if>>${grade.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">专业</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.major" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${specialtyMap}" var="specialty">
											<option value="${specialty.key}"  <c:if test="${specialty.key==param['search_EQ_gjtStudentInfo.major']}">selected='selected'</c:if>>${specialty.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</div>

					<div id="more-search" class="row collapse <c:if test="${not empty param[search_EQ_gjtStudentInfo_sfzh] || not empty param[search_EQ_gjtStudentInfo.xxzxId] || not empty param.search_EQ_eleRegistrationNumber }">in</c:if>">
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">身份证号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_gjtStudentInfo_sfzh" value="${param[search_EQ_gjtStudentInfo_sfzh] }">
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学习中心</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.xxzxId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${studyCenterMap}" var="map">
											<option value="${map.key}"  <c:if test="${map.key==param[search_EQ_gjtStudentInfo.xxzxId]}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">电子注册号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_eleRegistrationNumber" value="${param.search_EQ_eleRegistrationNumber }">
								</div>
							</div>
						</div>
					</div>

				</div><!-- /.box-body -->
				<div class="box-footer text-right">
					<button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
					<button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
					<div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">高级搜索<i class="fa fa-fw fa-angle-up <c:if test="${not empty param.search_EQ_sfzh || not empty param.search_EQ_userType || not empty param.search_LIKE_scCo }">fa-angle-down</c:if>"></i> </div>
				</div><!-- /.box-footer-->
			</div>
				
			<div class="box box-border margin-bottom-none">
				<div class="box-body">
					<div class="filter-tabs filter-tabs2 clearfix">
						<ul class="list-unstyled">
						<li lang=":input[name='search_EQ_auditState']" <c:if test="${empty param['search_EQ_auditState']}">class="actived"</c:if>>全部(${allCount})</li>
						<li value="11" role=":input[name='search_EQ_auditState']" <c:if test="${param['search_EQ_auditState'] == '11' }">class="actived"</c:if>>已满足，已毕业(${yesGraduationCount})</li>
						<li value="6" role=":input[name='search_EQ_auditState']" <c:if test="${param['search_EQ_auditState'] == '6' }">class="actived"</c:if>>已满足，已申请毕业(${yesApplyGraduationCount})</li>
						<li value="1" role=":input[name='search_EQ_auditState']" <c:if test="${param['search_EQ_auditState'] == '1' }">class="actived"</c:if>>已满足，未申请毕业(${noApplyGraduationCount})</li>
						<li value="2" role=":input[name='search_EQ_auditState']" <c:if test="${param['search_EQ_auditState'] == '2' }">class="actived"</c:if>>未满足(${dissatisfyCount})</li>
						</ul>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
								<tr>
									<th>个人信息</th>
									<th>报读信息</th>
									<th>是否满足毕业申请条件</th>
									<th>是否申请学位</th>
									<th>电子注册号</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.content}" var="info">
											<tr>
											<td>
						            			<ul class="list-unstyled text-left">
						            				<li>姓名：${info.gjtStudentInfo.xm }</li>
						            				<li>学号：${info.gjtStudentInfo.xh }</li>
						            				<li>手机：${info.gjtStudentInfo.sjh }</li>
						            				<li>身份证：${info.gjtStudentInfo.sfzh }</li>
						            			</ul>
						            		</td>
						            		<td>
						            			<ul class="list-unstyled text-left">
						            				<li>层次：${pyccMap[info.gjtStudentInfo.pycc]}</li>
						            				<li>学期：${gradeMap[info.gjtStudentInfo.nj]}</li>
						            				<li>专业：${specialtyMap[info.gjtStudentInfo.major]}</li>
						            				<li>学习中心：${studyCenterMap[info.gjtStudentInfo.xxzxId]}</li>
						            			</ul>
						            		</td>
						            		<td>
						            			<c:if test="${info.auditState eq '11' }">
						            				<div class="text-green">已满足</div>
		            								<div class="gray9">（已毕业）</div>
		            							</c:if>
		            							
		            							<c:if test="${info.auditState eq '1' }">
		            								<div class="text-orange">已满足</div>
		            								<div class="text-orange">（未申请毕业）</div>
		            							</c:if>
		            							
		            							<c:if test="${info.auditState eq '6' }">
		            								<div class="text-orange">已满足</div>
		            								<div class="text-orange">（已申请毕业）</div>
		            							</c:if>
		            							
		            							<c:if test="${info.auditState eq '2' or info.auditState eq '12'}">
		            								<div class="text-red">不满足</div>
		            							</c:if>
		            							
						            		</td>
						            		
						            		<td>
						            			<c:if test="${info.applyDegree eq '0' }">
						            				<div class="text-orange">未申请学位</div>
						            			</c:if>
						            			<c:if test="${info.applyDegree eq '1' }">
						            				<c:choose>
							            				<c:when test="${not empty info.eleRegistrationNumber }">
							            					<div class="text-green">已获得学位</div>
							            				</c:when>
							            				<c:otherwise>
							            					<div class="text-green">已申请学位</div>
							            				</c:otherwise>
							            			</c:choose>
						            			</c:if>
						            		</td>
						            		<td>
						            			${info.eleRegistrationNumber  }
						            		</td>
						            		<td>
						            			<a href="view/${info.applyId}" class="operion-item" data-toggle="tooltip" title="查看详情" data-container="body"><i class="fa fa-view-more"></i></a>
						            		</td>
						            		</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td align="center" colspan="6">暂无数据</td>
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
 
 	<!-- 底部 -->
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

		function exportAuditState() {
			var form = $("<form>");
			form.attr("style","display:none");
			form.attr("target","");
			form.attr("method","post");
			form.attr("action","exportAuditState");
			$("body").append(form);
			var fields = $('#listForm').serializeArray();
			$.each( fields, function(i, field){
				form.append('<input type="hidden" name="'+field.name+'" value="' + field.value + '" />');
			});
			form.submit();
		}
	</script>
</body>
</html>
