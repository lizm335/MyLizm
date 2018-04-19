<%@page import="java.util.Date"%>
<%@page import="javax.xml.crypto.Data"%>
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
	<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.back()">返回</button>
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试管理</a></li>
		<li><a href="javascript:;">开考科目</a></li>
		<li class="active">编辑</li>
	</ol>
</section>

<section class="content">
          
              <div class="box box-default">
                <div class="box-header with-border"> 
                  <h3 class="box-title">开考科目</h3>
                </div>
                <form id="inputForm" class="form-horizontal" role="form"  method="post">
                <input type="hidden" id="entity_examPlanId" value="${entity.examPlanId}">
                <input type="hidden" name="subjectType" value="${subjectType}" id="subjectType">
                
                  <div class="box-body">
						<c:if test="${action=='create'}">
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考试计划：</label>
								<div class="col-sm-8">
									<select name="examBatchCode" id="entity_examBatchCode" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
										<c:forEach items="${batchMap}" var="map">
											<option value="${map.key}">${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</c:if>	
							
						<c:if test="${action!='create'}"> 
							<div class="form-group"> 
								<label class="col-sm-2 control-label"><small class="text-red">*</small>考试计划:</label>
								<div class="col-sm-8">
									${entity.examBatchNew.name}
									<input type="hidden" name="examBatchCode" id="entity_examBatchCode" class="form-control" 
									value="${entity.examBatchCode}" />
								</div>
							</div>
						</c:if>

						<div class="form-group">
						  <label class="col-sm-2 control-label"><small class="text-red">*</small>开考科目名称：</label>
						  <div class="col-sm-8">
							<input type="text" class="form-control" name="examPlanName" value="${entity.examPlanName}" id="entity_examPlanName">
						  </div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>对应课程：</label>
							<div class="col-sm-8">
								<div class="pull-right margin_l10">
									<button type="button" class="btn btn-default" data-role="select-pop">选择课程</button>
								</div>
								<div class="select2-container select2-container--default show oh">
									<div class="select2-selection--multiple">
										<ul class="select2-selection__rendered select-container-ul">
											<c:if test="${not empty entity.gjtCourseList }">
												<c:forEach  items="${entity.gjtCourseList}" var="item">
												 <li class="select2-selection__choice">
												 <c:if test="${action!='view' }">
											      <span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>
											      </c:if>
											      <span class="select2-name" title="${item.kcmc}" data-toggle="tooltip" data-container="body"  data-order="${item.kch}">${item.kcmc}</span>
											      <input type="hidden" name="courseIds" value="${item.courseId}"/>			      
											      </li>
											   </c:forEach>
							      			</c:if>
										</ul>			
									</div>
								</div>
							</div>
						</div>				

						<div class="form-group">
						  <label class="col-sm-2 control-label"><small class="text-red">*</small>试卷号：</label>
						  <div class="col-sm-8">
							<input type="text" class="form-control" name="examNo" value="${entity.examNo}" id="entity_examNo">
						  </div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>考试形式：</label>
							<div class="col-sm-8">
								<select name="type" id="entity_type" class="selectpicker show-tick form-control" 
									data-size="5" data-live-search="true">
									<option value="" >请选择</option>
									<c:forEach items="${examTypeMap}" var="map">
										<option value="${map.key}"<c:if test="${map.key==entity.type}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

					    <div id="document">
						  <div class="form-group">
							  <label class="col-sm-2 control-label"><small class="text-red">*</small>文档名称:</label>
							  <div class="col-sm-8">
								  <input id="documentFileName" type="text" class="form-control" name="documentFileName" value="${entity.documentFileName}" readonly />
								  <input id="documentFilePath" type="hidden" name ="documentFilePath" value=""/>
							  </div>
						  </div>

						  <div class="form-group">
							  <label class="col-sm-2 control-label"><small class="text-red">*</small>文档文件:</label>
							  <div class="col-sm-8">
								  <button id="uploadBtn" type="button" class="btn btn-default" onclick="uploadFile('documentFileName','documentFilePath','doc|docx',null,uploadCallback)"><i class="fa fa-fw fa-upload"></i> 上传文件</button>
							  </div>
						  </div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>考试方式：</label>
							<div class="col-sm-8">
								<select name="examStyle" id="entity_examStyle" class="selectpicker show-tick form-control" 
									data-size="5" data-live-search="true">
									<option value="" >请选择</option>
									<c:forEach items="${examStyleMap}" var="map">
										<option value="${map.key}"<c:if test="${map.key==entity.examStyle}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label"><small class="text-red">*</small>指定专业：</label>
							<div class="col-sm-8">
								<select name="specialtyIds" id="entity_specialtyIds" class="selectpicker show-tick form-control" multiple
									data-size="5" data-live-search="true">
									<c:forEach items="${specialtyMap}" var="map">
										<c:set var="flag" value="${false}"></c:set>
										<c:forEach items="${entity.gjtSpecialtyList}" var="specialty">
											<c:if test="${map.key==specialty.gjtSpecialtyBase.specialtyBaseId}">
												<c:set var="flag" value="${true}"></c:set>
											</c:if>
										</c:forEach>
										<option value="${map.key}"<c:if test="${flag}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
								<span class="gray9">注：默认不指定则为通用科目，相关专业都可以预约这个科目；如果指定专业则只有指定的专业可以预约这个科目</span>
							</div>
						</div>

						<div class="form-group">
						  <label class="col-sm-2 control-label">形考比例：</label>
						  <div class="col-sm-8">
							  <div class="input-group">
								  <input type="text" class="form-control" name="xkPercent" value="${entity.xkPercent}" id="entity_xkPercent" 
								  	onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')">
								  <span class="input-group-addon">%</span>
							  </div>
						  </div>
						</div>

						<div class="form-group margin-bottom-none">
							<label class="col-sm-2 control-label margin_b15"><small class="text-red">*</small>考试时间：</label>
							<div class="col-sm-8 margin_b15">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										 </div>
										<input type="text" class="form-control  single-datetime" id="entity_examSt" name="examSt"
										 placeholder="考试开始时间" value="<fmt:formatDate value='${entity.examSt}' type='date' pattern='yyyy-MM-dd HH:mm'/>" />
	
									</div>
									<div class="input-group-addon no-border">
										至
									 </div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										 </div>
										<input type="text" class="form-control  single-datetime" id="entity_examEnd" name="examEnd"
										 placeholder="考试结束时间" value="<fmt:formatDate value='${entity.examEnd}' type='date' pattern='yyyy-MM-dd HH:mm'/>" />
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
						  <label class="col-sm-2 control-label"><small class="text-red">*</small>考试预约方式：</label>
						  <div class="col-sm-3">
							<div class="radio">
		                        <label>
		                          <input type="radio" name="examPlanOrder" class="minimal" value="1" <c:if test="${entity.examPlanOrder == 1 or empty entity.examPlanOrder }">checked</c:if> />个人预约
		                        </label>
		                        <label class="margin_l15">
		                          <input type="radio" name="examPlanOrder" class="minimal" value="2"  <c:if test="${entity.examPlanOrder == 2 }">checked</c:if>/>集体预约
		                        </label>
		                    </div>
						  </div>
						</div>
						
						<div class="form-group">
						  <label class="col-sm-2 control-label"><small class="text-red">*</small>考试预约最低分数限制：</label>
						  <div class="col-sm-3">
							  <div class="input-group">
								  <input type="text" class="form-control" name="examPlanLimit" value="${not empty entity.examPlanLimit ? entity.examPlanLimit : 0}" id="entity_examPlanLimit"
										 onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')">
								  <span class="input-group-addon">分</span>
							  </div>
						  </div>
						</div>						
                  </div>

                  <div class="box-footer">
                  	<div class="row">
	                  	<div class="col-sm-7 col-sm-offset-2">
	                  		<c:if test="${action=='create'}">
	                 			<button id="btn-create" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit">创建</button>
	                 		</c:if>
	                  		<c:if test="${action=='update'}">
	                 			<button id="btn-update" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit">更新</button>
	                 		</c:if>
	    	                <button type="reset" id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
	                  	</div>
                  	</div>
                  </div>
                  </form>
                  
              </div>
           
</section>

<jsp:include page="/eefileupload/upload.jsp" />
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
<%
Date time = new Date();
%>
<%-- <!-- Daterange date-range-picker -->
<script src="${ctx}/static/plugins/daterangepicker/moment.min.js"></script>
<script src="${ctx}/static/plugins/daterangepicker/daterangepicker.js"></script> --%>
<script src="${ctx}/static/js/exam/exam_plan_form.js?time=<%=time%>"></script>
</html>