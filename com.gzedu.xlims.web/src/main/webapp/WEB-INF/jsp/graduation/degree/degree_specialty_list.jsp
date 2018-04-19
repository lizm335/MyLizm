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
			<li>毕业管理</li>
			<li class="active">学位设置</li>
		</ol>
	</section>
	<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li>
					<a href="${ctx}/graduation/degreemanager/degreeCollegeList">学位院校设置</a>
				</li>
				<li class="active">
					<a href="#">学位专业设置</a>
				</li>
			</ul>
			<div class="box box-border no-shadow">
				<div class="box-body">
					<form class="form-horizontal">
						<div class="row pad-t15">
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学院名称</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业代码</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-xs-6 col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业名称</label>
									<div class="col-sm-9">
										<input type="text" class="form-control">
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<!-- /.box-body -->
				<div class="box-footer">
					<div class="pull-right">
						<button type="button" class="btn min-width-90px btn-default">重置</button>
					</div>
					<div class="pull-right margin_r15">
						<button type="button" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
				<!-- /.box-footer-->
			</div>

			<div class="box box-border no-shadow margin-bottom-none">
				<div class="box-body">
					<div class="filter-tabs clearfix">
						<ul class="list-unstyled">
							<li class="actived">全部（0）</li>
							<li>待设置（0）</li>
							<li>已启用（0）</li>
							<li>已停用（0）</li>
						</ul>
						<div class="pull-right no-margin">
							<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-export">
								<i class="fa fa-fw fa-plus"></i>  新增专业
							</a>
						</div>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered vertical-mid text-center table-font">
							<thead class="with-bg-gray">
								<tr>
									<th>院校名称</th>
									<th>专业名称</th>
									<th>是否配置学位申请条件</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="5">暂无数据</td>
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
