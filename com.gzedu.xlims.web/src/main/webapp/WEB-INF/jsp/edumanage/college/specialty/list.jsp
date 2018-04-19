<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<ol class="breadcrumb oh">
		<li>教学管理</li>
		<li class="active">专业规则</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">专业规则号</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_ruleCode" value="${param.search_LIKE_ruleCode}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">专业名称</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_name" value="${param.search_LIKE_name}">
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer">
	      <div class="pull-right"><button type="button" class="btn min-width-90px btn-default btn-reset">重置</button></div>
	      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	    </div><!-- /.box-footer-->
	</div>
	
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<div class="pull-right no-margin">
				<shiro:hasPermission name="/edumanage/specialtyCollege/list$import">
					<button type="button" class="btn btn-default btn-sm left10 btn-import"><i class="fa fa-fw fa-sign-in"></i> 导入专业规则</button>
				</shiro:hasPermission>
			</div>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered table-striped text-center vertical-mid table-font">
					<thead>
		              <tr>
		                <th>专业规则号</th>
		                <th>专业名称</th>
		                <th>专业层次</th>
		                <th>课程总数</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
					            	<tr>
					            		<td>
					            			${entity.ruleCode}
					            		</td>
					            		<td>
					            			${entity.name}
					            		</td>
					            		<td>
					            			${pyccMap[entity.specialtyLevel]}
					            		</td>
					            		<td>
					            			${empty(countMap[entity.specialtyId])?0:countMap[entity.specialtyId]}
					            		</td>
					            		<td>
					            			<a href="view/${entity.specialtyId}" class="operion-item" data-toggle="tooltip" title="详情"><i class="fa fa-view-more"></i></a>
					            		</td>
					            	</tr>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="5">暂无数据</td>
								</tr>
							</c:otherwise>
						</c:choose>
		            </tbody>
				</table>
			</div>
			<tags:pagination page="${pageInfo}" paginationSize="5" />
		</div>
	</div>
	</form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
$(".btn-import").click(function(event) {
	var actionName = ctx+"/edumanage/specialtyCollege/import";
	var downloadFileUrl = ctx+"/excelImport/downloadModel?name=专业规则导入模板.xls";
	var content1 = "为了方便你的工作，我们已经准备好了《专业规则导入模板》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《专业规则导入模板》";
	excelImport(actionName, "file", "secialtyPlan", downloadFileUrl, null, "批量导入专业规则", null, null, null, content1, content2);
});
</script>
</body>
</html>
