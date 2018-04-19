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
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材管理</a></li>
		<li class="active">发放编排</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="search_EQ_status" value="">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学期</label>
	              <div class="col-sm-9">
	                <select name="search_gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
						<option value="" selected="selected">请选择</option>
						<c:forEach items="${termMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param.search_gradeId}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>
					</select>
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
	
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">发放编排</h3>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered table-striped text-center vertical-mid table-font">
					<thead>
		              <tr>
		                <th width="20%">学期</th>
		                <th width="15%">课程总数</th>
		                <th width="15%">教材总数</th>
		                <th width="15%">编排状态</th>
		                <th width="15%">启用状态</th>
		                <th >操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>${entity.GRADE_NAME}</td>
						            		<td>
						            			${entity.COURSE_COUNT}
						            		</td>
						            		<td>${entity.TEXTBOOK_COUNT}</td>
						            		<td>
						            			<c:if test="${entity.TEXTBOOK_COUNT==0}">
						            				<span class="gray9">未编排</span>
						            			</c:if>
						            			<c:if test="${entity.TEXTBOOK_COUNT>1}">
						            				<span class="text-green">已编排</span>
						            			</c:if>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.STATUS == 0}">
						            					<span class="gray9">未启用</span>
						            				</c:when>
						            				<c:when test="${entity.STATUS == 1}">
						            					<span class="text-green">已启用</span>
						            				</c:when>
						            			</c:choose>
						            		</td>
						            		<td>
						            			
						            			
						            			<c:if test="${entity.TEXTBOOK_COUNT>0}">
						            				<c:if test="${entity.STATUS == 0}">
						            					<a href="/textbookArrange/changeStatus?gradeId=${entity.GRADE_ID}&status=1" class="operion-item operion-view"  data-toggle="tooltip" title="启用"><i class="fa fa-fw fa-play"></i></a>
						            				</c:if>
						            				<c:if test="${entity.STATUS == 1}">
						            					<a href="/textbookArrange/changeStatus?gradeId=${entity.GRADE_ID}&status=0" class="operion-item operion-view"  data-toggle="tooltip" title="停用"><i class="fa fa-fw fa-pause"></i></a>
						            				</c:if>
						            			</c:if>
						            			
						            			<c:if test="${entity.TEXTBOOK_COUNT==0||entity.STATUS == 0}">
						            				<a href="/textbookArrange/findTextbookList?gradeId=${entity.GRADE_ID }" class="operion-item operion-view" data-toggle="tooltip" title="设置"><i class="fa fa-gear"></i></a>
						            			</c:if>
						            			<c:if test="${entity.TEXTBOOK_COUNT>0}">
						            				<a href="/textbookArrange/findArrangeTextBook?search_gradeId=${entity.GRADE_ID }" class="operion-item operion-view" data-toggle="tooltip" title="查看"><i class="fa fa-fw fa-view-more"></i></a>
						            			</c:if>
						            		</td>
						            	</tr>
						            </c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="8">暂无数据</td>
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

$(".setting").click(function(event) {
	 window.location.href = "${ctx}/textbookArrange/setting";
});



$(".btn-import").click(function(event) {
	var actionName = ctx+"/textbookArrange/import";
	var downloadFileUrl = ctx+"/excelImport/downloadModel?name=复习资料发放安排导入表.xls";
	var content1 = "为了方便你的工作，我们已经准备好了《复习资料发放安排导入表》的标准<br>模板，你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《复习资料发放安排导入表》";
	excelImport(actionName, "file", "textbook", downloadFileUrl, null, "批量导入复习资料发放安排", null, null, null, content1, content2);
});

</script>
</body>
</html>
