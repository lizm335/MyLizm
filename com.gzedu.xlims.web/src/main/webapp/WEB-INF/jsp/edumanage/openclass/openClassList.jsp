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
		<li><a href="#">教学管理</a></li>
		<li class="active">开课管理</li>
	</ol>
</section>
<section class="content">
<form id="listForm" class="form-horizontal">
	<input id="copy_flg" type="hidden" name="search_COPY_FLG" value="${param['search_COPY_FLG']}">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">开课学期</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_gradeId" id="gradeId" class="selectpicker show-tick form-control" 
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
					<li <c:if test="${empty param['search_COPY_FLG']}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${COPY_FLG_ALL})</li>
					<li <c:if test="${param['search_COPY_FLG'] == '1'}">class="actived"</c:if> value="1" onclick="choiceXJ('1')">待复制(${COPY_FLG1})</li>
					<li <c:if test="${param['search_COPY_FLG'] == '2'}">class="actived"</c:if> value="2" onclick="choiceXJ('2')">已复制(${COPY_FLG2})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th>学期</th>
		                <th>课程代码</th>
		                <th>课程名称</th>
		                <th>任课教师</th>
		                <th>选课人数</th>
		                <th>课程班数</th>
		                <th>资源状态</th>
		                <th>复制状态</th>
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
					            			${item.KCH} <br>
					            		</td>
					            		<td>
					            			${item.KCMC} <br>
					            		</td>
					            		<td>
					            			${item.CLASS_TEACHER}
					            			<c:if test="${empty(item.CLASS_TEACHER)}">
					            				<span class="text-orange">未配置</span>
					            			</c:if>
					            		</td>
					            		<td>
					            			${item.CHOOSE_COUNT}
					            		</td>
					            		<td>
					            			${item.CLASS_COUNT}
					            		</td>
					            		<td>
					            			<c:if test="${item.IS_OPEN eq '0' }">
					            				<span class="text-orange">暂无资源</span>
					            			</c:if>
					            			
					            			<c:if test="${item.IS_OPEN eq '1' }">
					            				<span class="text-green">已启用</span>
					            			</c:if>
					            			
					            			<%-- <c:choose>
					            				<c:when test="${item.IS_ENABLED eq '0' }">
					            					<span class="text-orange">暂无资源</span>
					            				</c:when>
					            				<c:when test="${item.IS_ENABLED eq '1' }">
					            					<span class="text-green">已启用</span>
					            				</c:when>
					            				<c:when test="${item.IS_ENABLED eq '2' }">
					            					<span class="text-orange">建设中</span>
					            				</c:when>
					            				<c:when test="${item.IS_ENABLED eq '5' }">
					            					<span class="text-green">部分启用</span>
					            				</c:when>
					            				<c:otherwise>
					            					<span class="text-orange">暂无资源</span>
					            				</c:otherwise>
					            			</c:choose> --%>
					            		</td>
					            		<td>
					            			<c:choose>
					            				<c:when test="${item.COPY_FLG eq '2' }">
					            					<span class="text-green">已复制</span>
					            				</c:when>
					            				<c:otherwise>
					            					<span class="text-orange">待复制</span><br/>
					            					<c:if test="${item.TASK_COUNT > 0 }">
					            						<span class="gray9">(${item.TERM_TASK_COUNT }/${item.TASK_COUNT })</span>
					            					</c:if>
					            				</c:otherwise>
					            			</c:choose>
					            		</td>
					            		<td>
					            			<shiro:hasPermission name="/edumanage/openclass/list$setClassTeacher">
					            				<a href="${ctx}/edumanage/openclass/teacherChoiceTree?termCourseId=${item.TERMCOURSE_ID}" class="operion-item" data-toggle="tooltip" title="配置任课教师" data-role="configure-teacher"><i class="fa fa-rule-set"></i></a>
					            			</shiro:hasPermission>
					            			<shiro:hasPermission name="/edumanage/courseclass/getClassList">
					            				<a href="${ctx}/edumanage/courseclass/getClassList?GRADE_ID=${item.TERM_ID}&COURSE_ID=${item.COURSE_ID}" class="operion-item" data-toggle="tooltip" title="课程班管理"><i class="fa fa-kcbgl"></i></a>
					            			</shiro:hasPermission>
					            			<shiro:hasPermission name="/edumanage/openclass/list$openClass">
					            				<a href="${ctx}/edumanage/openclass/copycourse?termcourse_id=${item.TERMCOURSE_ID}" target="_blank" class="operion-item" data-toggle="tooltip" title="开课流程" onclick="initTermcourseStuRec('${item.TERMCOURSE_ID}')"><i class="fa fa-ksyy"></i></a>
					            			</shiro:hasPermission>
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

<script type="text/javascript">
	$(function() {
		var gradeId = $("#gradeId").val();
		$.post("${ctx}/edumanage/openclass/updTermCopyFlg?TERM_ID="+gradeId);
	})
	function choiceXJ(flag){
		//$('#listForm .btn-reset').trigger('click');
		$("#copy_flg").val(flag);
		$("#listForm").submit();
	}
	
	function initTermcourseStuRec(termcourse_id) {
		$.post("${ctx}/api/openclass/initTermcourseStuRec?TERMCOURSE_ID="+termcourse_id);
	}

	//配置老师
	$('[data-role="configure-teacher"]').click(function(event) {
		event.preventDefault();
	  	$.mydialog({
		  id:'configure-teacher',
		  width:760,
		  height:500,
		  zIndex:11000,
		  content: 'url:'+$(this).attr('href')
		});
	});
</script>

</body>
</html>
