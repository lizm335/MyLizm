<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>管理系统</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
		$(function() {

		})
		function choiceXJ(flag){
			//$('#listForm .btn-reset').trigger('click');
			$("#auditState").val('');
			$("#auditState").val(flag);
			$("#listForm").submit();
		}
	</script>
</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li class="active">开课管理</li>
	</ol>
</section>
<section class="content">
<form id="listForm" class="form-horizontal">
	<input id="auditState" type="hidden" name="search_EQ_auditState" value="${param.search_EQ_auditState}">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学期</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_gradeId" class="selectpicker show-tick form-control" 
						data-size="5" data-live-search="true">
						<option value="-1" selected="selected">请选择</option>
						<c:forEach items="${termMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gradeId'] || map.key==gradeId}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">课程名称</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_courseId" class="selectpicker show-tick form-control" 
						data-size="5" data-live-search="true">
						<option value="" selected="selected">请选择</option>
						<c:forEach items="${courseMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param['search_EQ_courseId']}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">课程代码</label>
	              <div class="col-sm-9">
	                <input type="text" name="search_EQ_courseCode" class="form-control" value="${param.search_EQ_courseCode }">
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
			<h3 class="box-title pad-t5">信息列表</h3>
			<div class="pull-right no-margin">
				<shiro:hasPermission name="/edumanage/openclassCollege/list$create">
					<a href="${ctx}/edumanage/openclassCollege/toOpenCourse" class="btn btn-default btn-sm margin_l10"><i class="fa fa-fw fa-sign-in"></i> 开设课程</a>
				</shiro:hasPermission>
			</div>
		</div>
		<div class="box-body">
			<div class="filter-tabs clearfix">
				<ul class="list-unstyled">
					<li <c:if test="${empty param.search_EQ_auditState}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${not empty countAuditStateMap[''] ? countAuditStateMap[''] : 0})</li>
					<li <c:if test="${param.search_EQ_auditState == '0'}">class="actived"</c:if> value="0" onclick="choiceXJ('0')">待复制(${not empty countAuditStateMap['0'] ? countAuditStateMap['0'] : 0})</li>
					<li <c:if test="${param.search_EQ_auditState == '1'}">class="actived"</c:if> value="1" onclick="choiceXJ('1')">已复制(${not empty countAuditStateMap['1'] ? countAuditStateMap['1'] : 0})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th>学期</th>
		                <th>课程名称</th>
		                <th>选课人数</th>
		                <th>课程班数</th>
		                <th>资源状态</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
							<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.getContent() }" var="item">
					            	<tr>
					            		<td>
					            			${item.termName}
					            		</td>
					            		<td>
					            			${item.courseName} <br>
					            			<span class="gray9">（${item.courseCode}）</span>
					            		</td>
					            		<td>
					            			${item.studentCount}
					            		</td>
					            		<td>
					            			${item.classCount}
					            		</td>
					            		<td class="text-orange">
					            			待复制
					            		</td>
					            		<td>
					            			<a href="${ctx}/edumanage/courseclass/list?search_EQ_actualGrade.gradeId=${item.termId}&search_LIKE_gjtCourse.courseId=${item.courseId}" class="operion-item" data-toggle="tooltip" title="课程班管理"><i class="fa fa-kcbgl"></i></a>
					            			<a href="${ctx}/edumanage/openclassCollege/process?gradeId=${item.termId}&courseId=${item.courseId}" target="_blank" class="operion-item" data-toggle="tooltip" title="开课流程"><i class="fa fa-ksyy"></i></a>
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
			</div>
			<tags:pagination page="${pageInfo}" paginationSize="5" /> 
		</div>
	</div>
</form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
</html>
