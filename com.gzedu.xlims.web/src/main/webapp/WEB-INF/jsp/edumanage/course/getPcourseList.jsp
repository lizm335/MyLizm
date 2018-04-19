<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li class="active">课程管理</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li <c:if test="${empty param.APP_ID || param.APP_ID eq 'APP014'}">class="active"</c:if>><a href="#" data-toggle="tab" onclick="changType('APP014')">学历课程</a></li>
				<li <c:if test="${param.APP_ID eq 'APP012'}">class="active"</c:if>><a href="#" data-toggle="tab" onclick="changType('APP012')">职业课程</a></li>
				<li <c:if test="${param.APP_ID eq 'APP999'}">class="active"</c:if>><a href="#" data-toggle="tab" onclick="changType('APP999')">企业培训</a></li>
				<li <c:if test="${param.APP_ID eq 'APP008'}">class="active"</c:if>><a href="#" data-toggle="tab" onclick="changType('APP008')">高教社课程</a></li>
			</ul>
			<div class="tab-content" id="scoreContent">
				<div class="tab-pane active" id="tab_top_1">
					<div class="box box-border">
					    <div class="box-body">
					    	<input type="hidden" name="APP_ID" id="app_id" value="${param.APP_ID }">
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">课程名称</label>
					              <div class="col-sm-9">
					                <input type="text" class="form-control" name="COURSE_NAME" value="${param.COURSE_NAME }">
					              </div>
					            </div>
					          </div>
	
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">课程编号</label>
					              <div class="col-sm-9">
					                <input type="text" class="form-control" name="COURSE_CODE" value="${param.COURSE_CODE }">
					              </div>
					            </div>
					          </div>
					          
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">课程状态</label>
					              <div class="col-sm-9">
					                <select class="selectpicker show-tick form-control" name="COURSE_STATE" id="course_state" data-size="5" data-live-search="true">
					                  	<option value="">请选择</option>
					                  	<option value="1">有内容</option>
					                  	<option value="2">无内容</option>
					                </select>
					              </div>
					            </div>
					          </div>
					        </div>
					    </div><!-- /.box-body -->
					    <div class="box-footer text-right">
				          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
				          <button type="button" class="btn min-width-90px btn-default">重置</button>
				        </div><!-- /.box-footer-->
					</div>
					<div class="box box-border margin-bottom-none">
						<div class="box-body">
							<div class="table-responsive">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
						              <tr>
						              	<th>课程编号</th>
						              	<th>课程名称</th>
						              	<th>所属专业</th>
						                <th>学历层次</th>
						                <th>标签</th>
						                <th>发布时间</th>
						                <th>课程状态</th>
						                <th>操作</th>
						              </tr>
						            </thead>
						            <tbody>
						            	<c:forEach items="${pageInfo.content}" var="entity">
						            	<tr>
						            		<td>
						            			${entity.COURSE_CODE }
						            		</td>
						            		<td>
						            			${entity.COURSE_NAME }
						            		</td>
						            		<td>
						            			${entity.PROFESSION }
						            		</td>
						            		<td>
						            			${entity.EDUCATION_LEVEL }
						            		</td>
						            		<td>
						            			${entity.LABEL }
						            		</td>
						            		<td>
						            			${entity.PUBLISH_DT }
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.TASK_COUNT > 0 }">
						            					<span class="text-green">有内容</span>
						            				</c:when>
						            				<c:otherwise>
						            					<span class="text-red">无内容</span>
						            				</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<a href="${oclassHost }/processControl/previewCourse.do?formMap.COURSE_ID=${entity.COURSE_ID }" target="_blank" class="operion-item" title="预览课程">
													<i class="fa fa-fw fa-simulated-login"></i>
												</a>
						            		</td>
						            	</tr>
						            	</c:forEach>
						            </tbody>
								</table>
								<tags:pagination page="${pageInfo}" paginationSize="5" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">

$(function() {
	
});

//Initialize Select2 Elements
$(".select2").select2();
// filter tabs
$(".nav-tabs li").click(function(event) {
	if($(this).hasClass('actived')){
		$(this).removeClass('actived');
	}
	else{
		$(this).addClass('actived');
	}
});

function changType(type) {
	$("#app_id").val(type);
	window.location.href = "getPcourseList?APP_ID="+type;
}
</script>
</body>
</html>