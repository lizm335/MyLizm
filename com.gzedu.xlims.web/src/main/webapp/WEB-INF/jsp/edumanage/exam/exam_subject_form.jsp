<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
var ctx = "${ctx}";
$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
})
</script> 

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试管理</a></li>
		<li class="active">新增网考科目</li>
	</ol>
</section>

<section class="content">
          <div class="box no-margin">
			<div class="box-body pad-t15">
				<div class="form-horizontal reset-form-horizontal">
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx}/exam/new/subject/${action}" method="post">
                <input id="action" type="hidden" name="action" value="${action }">
                <input type="hidden" id="entity_subjectId" value="${entity.subjectId}">
                <input type="hidden" id="entity_type" value="${entity.type}">
                
                  <div class="box-body">
						<div class="form-group"> 
							<label class="col-sm-2 control-label">科目编号:</label>
							<div class="col-sm-7">
								<p class="form-control-static">
								<input type="hidden" name="entity_subjectCode" id="entity_subjectCode" value="${entity.subjectCode}"/>
									${entity.subjectCode}
								</p> 
							</div> 
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>课程选择:</label>
							<div class="col-sm-3">
								<select name="entity_courseId" id="entity_courseId" <c:if test="${action=='update'}">disabled="disabled"</c:if> 
									class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
									<option value="0">请选择</option>
									<c:forEach items="${courseList}" var="course">
											<option value="${course.courseId}" <c:if test="${course.courseId==entity.courseId}">selected='selected'</c:if>>${course.kcmc}（${course.kch}）</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<%-- <div class="form-group">
							<label class="col-sm-2 control-label"><small class="text-red">*</small>课程选择:</label>
							<div class="col-sm-3">
								<input id="entity_courseId" name="entity_courseId" type="hidden" value="${entity.courseId}"/>
								<select name="entity_kch" id="entity_kch" <c:if test="${action=='update'}">disabled="disabled"</c:if> 
									class="selectpicker show-tick form-control" data-size="10" data-live-search="true" >
									<option value="0">请选择</option>
									<c:forEach items="${plansList}" var="plan">
											<option value="${plan.COURSE_CODE}" <c:if test="${plan.COURSE_CODE==entity.kch}">selected='selected'</c:if> cid="${plan.COURSE_ID}">
												${plan.KCMC}  （${plan.COURSE_CODE}）
											</option>
									</c:forEach>
								</select>
							</div>
						</div> --%>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>科目名称:</label>
							<div class="col-sm-3">
								<input type="text" name="entity_name" id="entity_name" class="form-control" value="${entity.name}"/>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>试卷号:</label>
							<div class="col-sm-3">
								<input type="text" name="entity_examNo" id="entity_exam_no" class="form-control" value="${entity.examNo}" <c:if test="${action=='update'}">disabled="disabled"</c:if>/>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>标签说明:</label>
							<div class="col-sm-3">
								<input type="text" name="kcmc" id="entity_memo"  class="form-control" value="${entity.memo}"/>
							</div>
						</div>
                  </div>

                  <div class="row">
					<div class="col-sm-7 col-sm-offset-2">
                  		<c:if test="${action=='create'}">
                  			<button id="btn-create" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit">创建</button>
                  		</c:if>
	                    <c:if test="${action=='update'}">
                  			<button id="btn-update" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit">更新</button>
                  		</c:if>
    	                <button type="reset" id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">返回</button>
                  	</div>
                  </div>
                  </form>
                  
              </div> 
            </div>
          </div>  
</section>
</body>
<script src="${ctx}/static/js/exam/exam_subject_form.js"></script>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>