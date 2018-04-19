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
		<li><a href="#">任课管理</a></li>
		<li class="active">课程管理</li>
	</ol>
</section>
<section class="content">
<form id="listForm" class="form-horizontal">
	<input id="copy_flg" type="hidden" name="search_EQ_status" value="${param['searhc_search_EQ_status']}">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学期</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_gradeId" id="gradeId" class="selectpicker show-tick form-control" 
						data-size="5" data-live-search="true">
						<option value="" selected="selected">请选择</option>
						<c:forEach items="${termMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gradeId'] || map.key==gradeId}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">课程</label>
	              <div class="col-sm-9">
	                <input type="text" name="search_LIKE_course" class="form-control" placeholder="输入代码或名称查询" value="${param.search_LIKE_course }">
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">任课老师</label>
	              <div class="col-sm-9">
	                <input type="text" name="search_LIKE_teacherName" class="form-control"  value="${param.search_LIKE_teacherName }">
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
			<h3 class="box-title pad-t5">信息列表</h3>
			<div class="pull-right no-margin">
				<shiro:hasPermission name="/edumanage/openclass/list$create">
					<a href="${ctx}/edumanage/openclass/expTermCourse?EQ_gradeId=${gradeId}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-download"></i>导出数据</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="/edumanage/openclass/list$create">
					<a href="${ctx}/edumanage/openclass/toOpenCourse" class="btn btn-default btn-sm margin_l10"><i class="fa fa-fw fa-sign-in"></i> 开设课程</a>
				</shiro:hasPermission>
			</div>
		</div>
		<div class="box-body">
			<div class="filter-tabs clearfix">
				<ul class="list-unstyled">
					<li <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${COPY_FLG_ALL})</li>
					<li <c:if test="${param['search_EQ_status'] == '1'}">class="actived"</c:if> value="1" onclick="choiceXJ('1')">开课中(${COPY_FLG1})</li>
					<li <c:if test="${param['search_EQ_status'] == '2'}">class="actived"</c:if> value="2" onclick="choiceXJ('2')">已结束(${COPY_FLG2})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th>学期</th>
		                <th>课程名称</th>
		                <th>选课人数</th>
		                <th>平均学习进度</th>
		                <th>平均学习成绩</th>
		                <th>任课教师</th>
		                <th>状态</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
							<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.getContent() }" var="item">
					            	<tr>
					            		<td>
					            			${item.GRADE_NAME} 
					            		</td>
					            		<td>
					            			${item.KCMC} <br>
					            			<small class="gray9">（${item.KCH}）</small>
					            		</td>
					            		<td>
					            			${item.CHOOSE_COUNT}
					            		</td>
					            		<td>
					            			<c:if test="${empty(item.PROGRESS_AVG)}">--</c:if>
					            			<fmt:formatNumber value="${item.PROGRESS_AVG}" pattern="#.##" type="number"/>
					            		</td>
					            		<td>
					            			<c:if test="${empty(item.EXAM_SCORE_AVG)}">--</c:if>
					            			<fmt:formatNumber value="${item.EXAM_SCORE_AVG}" pattern="#.##" type="number"/>
					            		</td>
					            		<td>
					            			<c:if test="${not empty(item.TEACHER_NAME)}">
					            				${item.TEACHER_NAME}
					            			</c:if>
					            			<c:if test="${empty(item.TEACHER_NAME)}">
					            				<span class="text-orange">未分配</span>
					            			</c:if>
					            		</td>
					            		<td>
					            			<c:choose>
					            				<c:when test="${item.STATUS eq 0 }">
					            					<span class="text-orange">未开课</span>
					            				</c:when>
					            				<c:when test="${item.STATUS eq 1 }">
					            					<span class="text-green">开课中</span><br />
					            					<small class="gray9">（${item.END_DAYS }天后结束）</small>
					            					
					            				</c:when>
					            				<c:when test="${item.STATUS eq 2 }">
					            					<span class="gray9">已结束</span>
					            				</c:when>
					            			</c:choose>
					            		</td>
					            		<td>
					            			<a href="${ctx}/edumanage/courseclass/termCourseStudentList?termCourseId=${item.TERMCOURSE_ID}" class="operion-item" data-toggle="tooltip" title="" data-original-title="查看选课学员"><i class="fa fa-view-more"></i></a>
					            		</td>
					            	</tr>
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
				<tags:pagination page="${pageInfo}" paginationSize="5" /> 
			</div>
		</div>
	</div>
</form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
	function choiceXJ(flag){
		//$('#listForm .btn-reset').trigger('click');
		$("#copy_flg").val(flag);
		$("#listForm").submit();
	}
</script>

</body>
</html>
