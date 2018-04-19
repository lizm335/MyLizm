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
				<li>
					<a href="${ctx}/graduation/certificate/graduationList">毕业证书发放记录</a>
				</li>
				<li class="active">
					<a href="#">学位证书发放记录</a>
				</li>
			</ul>
			<div class="box box-border no-shadow">
				<div class="box-body">
					<form class="form-horizontal">
						<div class="row pad-t15">
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">姓名</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学号</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">发放机构</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">发放人</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">发放方式</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">签收人</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>

						</div>
						<div id="more-search" class="row collapse <c:if test="${not empty param.search_EQ_sfzh || not empty param.search_EQ_userType || not empty param.search_LIKE_scCo }">in</c:if>">
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学习中心</label>
									<div class="col-sm-9">
										<select name="search_EQ_studyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${studyCenterMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==param.search_EQ_studyId}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">层次</label>
									<div class="col-sm-9">
										<select name="search_EQ_studyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学期</label>
									<div class="col-sm-9">
										<select name="search_EQ_studyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业</label>
									<div class="col-sm-9">
										<input type="text" class="form-control" name="search_LIKE_scCo" value="${param.search_LIKE_scCo }">
									</div>
								</div>
							</div>
							<div class="col-sm-4 col-xs-6">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">身份证号</label>
									<div class="col-sm-9">
										<input type="text" class="form-control" name="search_EQ_sfzh" value="${param.search_EQ_sfzh }">
									</div>
								</div>
							</div>
						</div>
					</form>
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
							<li class="actived">全部（0）</li>
							<li>已发放（0）</li>
							<li>未发放（0）</li>
							<li>发放中（0）</li>
						</ul>
						<div class="pull-right no-margin">
							<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-export">
								<i class="fa fa-fw fa-download"></i> 导出学位证书发放记录
							</a>
							<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-import margin_l10">
								<i class="fa fa-fw fa-upload"></i> 导入学位证书发放记录
							</a>
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
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="8">暂无数据</td>
								</tr>
							</tbody>
						</table>
						<tags:pagination page="${pageInfo}" paginationSize="5" />
					</div>
				</div>
			</div>

		</div>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>
