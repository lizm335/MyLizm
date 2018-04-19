<%@page import="com.gzedu.xlims.pojo.system.StudyYear"%>
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
	<section class="content-header clearfix">
		<ol class="breadcrumb oh">
			<li>
				<a href="homepage.html">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li class="active">授课管理</li>
		</ol>
	</section>
	<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li <c:if test="${param.search_EQ_certificateType!=1}">class="active"</c:if>>
					<a href="?search_EQ_certificateType=0">毕业证书发放记录</a>
				</li>
				<li <c:if test="${param.search_EQ_certificateType==1}">class="active"</c:if>>
					<a href="?search_EQ_certificateType=1">学位证书发放记录</a>
				</li>
			</ul>
			<form class="form-horizontal">
				<div class="box box-border no-shadow">
					<div class="box-body">
						<c:set var="certificateType" value="${empty(param.search_EQ_certificateType)?0:param.search_EQ_certificateType}"></c:set>
						<input type="hidden" name="isOpen" value="${empty(param.isOpen)?'up':'down'}" />
						<input type="hidden" name="search_EQ_certificateType" value="${certificateType}" />
						<div class="row pad-t15">
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">姓名</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学号</label>
									<div class="col-sm-9">
										<input type="text" name="search_EQ_gjtStudentInfo.xh" value="${param['search_EQ_gjtStudentInfo.xh']}" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">发放机构</label>
									<div class="col-sm-9">
										<select name="search_EQ_sendOrg.id" class="selectpicker show-tick form-control">
											<option value="">请选择</option>
											<c:forEach items="${orgMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param['search_EQ_sendOrg.id']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">发放人</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_sendPerson" value="${param.search_LIKE_sendPerson}" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">发放方式</label>
									<div class="col-sm-9">
										<select name="search_EQ_sendType" class="selectpicker show-tick form-control">
											<option value="">请选择</option>
											<option <c:if test="${param.search_EQ_sendType=='0'}">selected='selected'</c:if> value="0">现场认领</option>
											<option <c:if test="${param.search_EQ_sendType=='1'}">selected='selected'</c:if> value="1">快递发放</option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">签收人</label>
									<div class="col-sm-9">
										<input type="text" class="form-control" name="search_LIKE_signPerson" value="${param.search_LIKE_signPerson}">
									</div>
								</div>
							</div>

						</div>
						<div id="more-search" class="row collapse <c:if test="${param.isOpen=='down'}">in</c:if>">
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学习中心</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.gjtStudyCenter.id" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${studyCenterMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtStudyCenter.id']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">层次</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.pycc" class="selectpicker show-tick form-control">
											<option value="">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学期</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="6" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${gradeMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']||map.key==currentGradeId}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyBaseId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${specialtyBaseMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyBaseId']}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">身份证号</label>
									<div class="col-sm-9">
										<input type="text" class="form-control" name="search_EQ_gjtStudentInfo.sfzh" value="${param['search_EQ_gjtStudentInfo.sfzh']}">
									</div>
								</div>
							</div>
						</div>

					</div>
					<!-- /.box-body -->
					<div class="box-footer text-right">
						<button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
						<button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
						<div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">
							高级搜索<i class="fa fa-fw fa-angle-up "></i>
						</div>
					</div>
					<!-- /.box-footer-->
				</div>

				<div class="box box-border no-shadow margin-bottom-none">
					<div class="box-body">
						<div class="filter-tabs clearfix">
							<ul class="list-unstyled">
								<li class="actived">全部（${sended+unsend+sending}）</li>
								<li>已发放（${sended}）</li>
								<li>未发放（${unsend}）</li>
								<li>发放中（${sending}）</li>
							</ul>
							<c:set var="certificate" value="${certificateType==1?'学位证书发放记录':'毕业证书发放记录' }"></c:set>
							<div class="pull-right no-margin">
								<a href="${ctx}/graduation/certificate/export?search_EQ_certificateType=${certificateType}" class="btn btn-sm btn-default btn-add">
									<i class="fa fa-fw fa-download"></i> 导出${certificate}
								</a>
								<button type="button" class="btn btn-default btn-sm left10 btn-import"><i class="fa fa-fw fa-sign-in"></i>导入${certificate}</button>
							</div>
						</div>
						<div class="table-responsive">
							<table class="table table-bordered vertical-mid text-center table-font">
								<thead class="with-bg-gray">
									<tr>
										<th>个人信息</th>
										<th>报读信息</th>
										<th>发放方式</th>
										<th>发放人信息</th>
										<th>发放时间</th>
										<th>签收人</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pageInfo.getContent()}" var="item">
										<tr>
											<td>
												<ul class="list-unstyled text-left">
													<li>姓名：${item.gjtStudentInfo.xm}</li>
													<li>学号：${item.gjtStudentInfo.xh}</li>
													<li>手机：
													
													<shiro:hasPermission name="/personal/index$privacyJurisdiction">
														${item.gjtStudentInfo.sjh}
													</shiro:hasPermission>
													<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
														<c:if test="${not empty item.gjtStudentInfo.sjh }">
														${fn:substring(item.gjtStudentInfo.sjh,0, 3)}******${fn:substring(item.gjtStudentInfo.sjh,8, (item.gjtStudentInfo.sjh).length())}
														</c:if>
													</shiro:lacksPermission>
													</li>
													
													<li>身份证：
													<shiro:hasPermission name="/personal/index$privacyJurisdiction">
														${item.gjtStudentInfo.sfzh}
													</shiro:hasPermission>
													<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
														<c:if test="${not empty item.gjtStudentInfo.sfzh }">
														${fn:substring(item.gjtStudentInfo.sfzh,0, 4)}******${fn:substring(item.gjtStudentInfo.sfzh,14, (item.gjtStudentInfo.sfzh).length())}
														</c:if>
													</shiro:lacksPermission>
													
													</li>
												</ul>
											</td>
											<td>
												<ul class="list-unstyled text-left">
													<li>层次：${pyccMap[item.gjtStudentInfo.pycc]}</li>
													<li>学期：${item.gjtStudentInfo.gjtGrade.gradeName}</li>
													<li>专业：${item.gjtStudentInfo.gjtSpecialty.zymc}</li>
													<li>学习中心：${item.gjtStudentInfo.gjtStudyCenter.scName}</li>
												</ul>
											</td>
											<td><c:choose>
													<c:when test="${item.sendType==0}">现场认领</c:when>
													<c:when test="${item.sendType==1}">
													快递发放
												</c:when>
												</c:choose></td>
											<td>
												<ul class="list-unstyled text-left">
													<li>姓名：${item.sendPerson}</li>
													<li>
														机构：${item.sendOrg.orgName}
													</li>

												</ul>
											</td>
											<td>${item.sendDt}</td>
											<td>${item.signPerson}
												<c:if test="${item.isAgent==1}">
													<small class="gray9">（代领）</small>
												</c:if></td>
											<td><c:choose>
													<c:when test="${item.status==0}">
														<span class="text-orange">未发放</span>
													</c:when>
													<c:when test="${item.status==1}">
														<span class="text-orange">发放中</span>
													</c:when>
													<c:when test="${item.status==2}">
														<span class="text-green">已发放</span>
													</c:when>
												</c:choose></td>
										</tr>
									</c:forEach>
									<c:if test="${empty(pageInfo.getContent())}">
										<tr>
											<td colspan="8">暂无数据</td>
										</tr>
									</c:if>

								</tbody>
							</table>
							<tags:pagination page="${pageInfo}" paginationSize="5" />
						</div>
					</div>
				</div>
			</form>
		</div>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
	//高级搜索
	$("#more-search").on('shown.bs.collapse', function(event) {
	    event.preventDefault();
	    $('[data-target="#more-search"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
	    $('[name="isOpen"]').val('down');
	}).on('hidden.bs.collapse', function(event) {
	    event.preventDefault();
	    $('[data-target="#more-search"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
	    $('[name="isOpen"]').val('up');
	});

	$(".btn-import").click(function(event) {
		var actionName = ctx+"/graduation/certificate/import?certificateType=${certificateType}";
		var downloadFileUrl = ctx+"/excelImport/downloadModel?name=证书发放记录导入模板.xls";
		var content1 = "为了方便你的工作，我们已经准备好了《${certificate}导入模板》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
		var content2 = "请选择你要导入的《${certificate}导入模板》";
		excelImport(actionName, "file", "certificateRecord", downloadFileUrl, null, "批量导入${certificate}", null, null, null, content1, content2);
	});
    </script>
</body>
</html>
