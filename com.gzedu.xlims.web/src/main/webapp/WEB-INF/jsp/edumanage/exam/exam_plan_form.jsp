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
})
</script>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试计划</a></li>
		<li class="active">编辑</li>
	</ol>
</section>

<section class="content">
          <div class="row">
            <div class="col-md-12">
              <div class="box box-primary">
                <div class="box-header with-border"> 
                  <h3 class="box-title">考试计划</h3>
                </div>
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx}/edumanage/exam/${action}" method="post">
                <input type="hidden" id="entity_examPlanId" value="${entity.examPlanId}">
                <input type="hidden" id="entity_type" value="${entity.type}">
                
                  <div class="box-body">
						
						<c:if test="${action=='create'}">
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考试批次编码:</label>
								<div class="col-sm-3">
									<select name="examBatchCode" id="entity_examBatchCode" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
										<c:forEach items="${batchMap}" var="map">
											<option value="${map.key}">${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考试科目编码</label>
								<div class="col-sm-3">
									<select name="subjectCode" id="entity_subjectCode" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
										<c:forEach items="${subjectList}" var="subject">
											<option value="${subject.subjectCode}">${subject.name}（试卷号：${subject.examNo}）</option>
										</c:forEach>
									</select>
								</div>
							</div>					
						
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>所属学年度:</label>
								<div class="col-sm-3">
									<select name="studyYearId" id="entity_studyYearId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
										<c:forEach items="${yearMap}" var="map">
											<option value="${map.key}">${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							
						</c:if>	
							
						<c:if test="${action!='create'}"> 
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考试批次:</label>
								<div class="col-sm-3">
									<input type="text" name="examBatchNew_name" id="entity_examBatchNew_name" class="form-control" 
									value="${entity.examBatchNew.name}" readOnly="true" />
								</div>
							</div>
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考试科目:</label>
								<div class="col-sm-3">
									<input type="text" name="examSubjectNew_name" id="entity_examSubjectNew_name" class="form-control" 
									value="${entity.examSubjectNew.name}" readOnly="true" />
								</div>
							</div>
							
							
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>所属学年度:</label>
								<div class="col-sm-3">
									<input type="text" name="studyYearInfo_name" id="entity_studyYearInfo_name" class="form-control" 
									value="${entity.studyYearInfo.studyYearName}" readOnly="true" />
								</div>
							</div>
						</c:if>	
						
						
						<div class="form-group margin-bottom-none">
							<label class="col-md-2 col-sm-2 control-label margin_b15">预约考试时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control  single-datetime" id="entity_bookSt" name="bookSt" 
									placeholder="预约考试开始时间" 	value="<fmt:formatDate value='${entity.bookSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control  single-datetime" id="entity_bookEnd" name="bookEnd" 
									placeholder="预约考试结束时间" 	value="<fmt:formatDate value='${entity.bookEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
						</div>
						
						<div class="form-group margin-bottom-none">
							<label class="col-md-2 col-sm-2 control-label margin_b15">考试时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control  single-datetime" id="entity_examSt" name="examSt"
									 placeholder="考试开始时间" value="<fmt:formatDate value='${entity.examSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control  single-datetime" id="entity_examEnd" name="examEnd"
									 placeholder="考试结束时间" value="<fmt:formatDate value='${entity.examEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
						</div>
						
						
                  </div>

                  <div class="box-footer">
                  	<div class="col-sm-7 col-sm-offset-2">
                  		<c:if test="${action=='create'}">
                 			<button id="btn-create" type="button" class="btn btn-primary">创建</button>
                 		</c:if>
                  		<c:if test="${action=='update'}">
                 			<button id="btn-update" type="button" class="btn btn-primary">更新</button>
                 		</c:if>
    	                <button type="reset" id="btn-back" class="btn btn-primary" onclick="history.back()">返回</button>
                  	</div>
                  </div>
                  </form>
                  
              </div>
            </div>
          </div>  
</section>
</body>
<!-- Daterange date-range-picker -->
<script src="${ctx}/static/plugins/daterangepicker/moment.min.js"></script>
<script src="${ctx}/static/plugins/daterangepicker/daterangepicker.js"></script>
<script src="${ctx}/static/js/exam/exam_plan_form.js?v=1"></script>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>