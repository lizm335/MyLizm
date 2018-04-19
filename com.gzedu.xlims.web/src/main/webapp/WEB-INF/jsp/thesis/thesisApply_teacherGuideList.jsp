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

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li class="active">指导记录</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="box">
	  <div class="box-body">
	      <div class="row pad-t15">
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">论文计划</label>
	            <div class="col-sm-9">
	              <select name="search_EQ_thesisPlanId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${thesisPlanMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==thesisPlanId}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	            </div>
	          </div>
	        </div>
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">学期</label>
	            <div class="col-sm-9">
	                <select name="search_EQ_gradeId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${gradeMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gradeId']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	            </div>
	          </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">层次</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_pycc" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${pyccMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_pycc']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">专业</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${specialtyMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_specialtyId']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">指导老师</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_teacherId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${guideTeachers}" var="map">
							<option value="${map.employeeId}" <c:if test="${map.employeeId==param['search_EQ_teacherId']}">selected='selected'</c:if>>${map.xm}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	        </div>
	      </div>
	  </div><!-- /.box-body -->
	  <div class="box-footer">
	    <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
	    <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	  </div><!-- /.box-footer-->
	</div>

	<div class="box margin-bottom-none">
		<div class="box-header with-border">
		  <h3 class="box-title pad-t5">指导记录列表</h3>
		  <div class="pull-right no-margin">
		  	<c:if test="${isBtnExport}">
		    	<a href="javascript:void(0)" class="btn btn-default btn-sm btn-export"><i class="fa fa-fw fa-sign-out"></i> 导出指导记录统计表</a>
		    </c:if>
		  </div>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered vertical-mid text-center table-font">
					<thead class="with-bg-gray">
		              <tr>
		              	<th>论文计划</th>
		                <th>指导老师</th>
		                <th>指导专业</th>
		                <th>指导学员总数</th>
		                <th>开题报告待定稿</th>
		                <th>初稿待定稿</th>
		                <th>定稿待评分</th>
		                <th>定稿初评已通过</th>
		                <th>定稿初评未通过</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>
						            			${entity.thesisPlanName}<br>
						            			<div class="gray9">（${entity.thesisPlanCode}）</div>
						            		</td>
						            		<td>${entity.teacherName}</td>
						            		<td>
						            			<div class="text-left">
						            				学期：${entity.gradeName}<br>
						            				层次：${entity.trainingLevel}<br>
						            				专业：${entity.specialtyName}
						            			</div>
						            		</td>
						            		<td>${entity.all}</td>
						            		<td>
						            			${entity.submitPropose}
						            		</td>
						            		<td>
						            			${entity.submitThesis}
						            		</td>
						            		<td>
						            			${entity.teacherConfirm}
						            		</td>
						            		<td>
						            			${entity.collegeConfirm}
						            		</td>
						            		<td>
						            			${entity.reviewFailed}
						            		</td>
						            		<td>
						            			<c:if test="${isBtnView}">
						            				<a href="${ctx}/thesisApply/viewTeacherGuide?thesisPlanId=${entity.thesisPlanId}&gradeSpecialtyId=${entity.gradeSpecialtyId}&teacherId=${entity.teacherId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
						            			</c:if>
						            		</td>
						            	</tr>
		            				</c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="10">暂无数据</td>
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
	$(".btn-export").click(function(event) {
		window.open(ctx + "/thesisApply/exportTeacherGuide?" + $("#listForm").serialize());
	});
});

</script>
</body>
</html>
