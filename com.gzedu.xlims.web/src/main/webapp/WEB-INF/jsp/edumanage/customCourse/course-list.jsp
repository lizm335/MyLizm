<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>定制课程</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li class="active">定制课程</li>
	</ol>
</section>
<section class="content">
<form class="form-horizontal" id="listForm" action="list">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">开课学期</label>
	              <div class="col-sm-9">
	                <select name="gradeId"  class="selectpicker show-tick form-control"	data-size="6" data-live-search="true">
						<option value="" selected="selected">请选择</option>
						<c:forEach items="${gradeMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['gradeId']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学院课程</label>
	              <div class="col-sm-9">
	                <input type="text" name="courseName" value="${param['courseName'] }" class="form-control">
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">替换课程</label>
	              <div class="col-sm-9">
	                <input type="text" name="replaceCourseName" value="${param['replaceCourseName'] }" class="form-control">
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap control-label-2lines">授课计划<br>状态</label>
	              <div class="col-sm-9">
	                <select name="planStatus" class="form-control selectpicker show-tick form-control" data-size="5" data-live-search="true">
	                  <option value="" >请选择</option>
	                  <option value="0" <c:if test="${not empty param['planStatus'] and param['planStatus']==0 }">selected="selected"</c:if> >未上传</option>
	                  <option value="1" <c:if test="${param['planStatus']==1 }">selected="selected"</c:if>>待审核</option>
	                  <option value="2" <c:if test="${param['planStatus']==2 }">selected="selected"</c:if>>审核通过</option>
	                  <option value="3" <c:if test="${param['planStatus']==3 }">selected="selected"</c:if>>审核不通过</option>
	                </select>
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap control-label-2lines">授课凭证<br>状态</label>
	              <div class="col-sm-9">
	                <select name="certificateStatus" class="form-control selectpicker show-tick form-control" data-size="5" data-live-search="true">
	                  <option value="" >请选择</option>
	                  <option value="0" <c:if test="${not empty param['certificateStatus'] and param['certificateStatus']==0 }">selected="selected"</c:if> >未上传</option>
	                  <option value="1" <c:if test="${param['certificateStatus']==1 }">selected="selected"</c:if> >上传中</option>
	                  <option value="2" <c:if test="${param['certificateStatus']==2 }">selected="selected"</c:if> >已上传</option>
	                </select>
	              </div>
	            </div>
	          </div>

	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">成绩状态</label>
	              <div class="col-sm-9">
	                <select name="scoreStatus" class="form-control selectpicker show-tick form-control" data-size="5" data-live-search="true">
	                  <option value="" >请选择</option>
	                  <option value="0" <c:if test="${not empty param['scoreStatus'] and param['scoreStatus']==0 }">selected="selected"</c:if> >未上传</option>
	                  <option value="1" <c:if test="${param['scoreStatus']==1 }">selected="selected"</c:if> >待审核</option>
	                  <option value="2" <c:if test="${param['scoreStatus']==2 }">selected="selected"</c:if> >审核通过</option>
	                  <option value="3" <c:if test="${param['scoreStatus']==3 }">selected="selected"</c:if> >审核不通过</option>
	                  <option value="6" <c:if test="${param['scoreStatus']==6 }">selected="selected"</c:if> >上传中</option>
	                </select>
	              </div>
	            </div>
	          </div>
	        </div>
		    <div class="box-footer">
		      <div class="pull-right">
		      	<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
		      	</div>
		      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
		    </div>
	    </div>
	</div>
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">课程列表</h3>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th width="">学期</th>
		                <th width="">替换课程代码</th>
		                <th width="">替换课程名称</th>
		                <th width="">所属专业</th>
		                <th width="">学院课程</th>
		                <th width="">选课人数</th>
		                <th width="">上传授课计划截止时间</th>
		                <th>授课计划状态</th>
		                <th width="">授课凭证状态</th>
		                <th>成绩状态</th>
		                <th width="10%">操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:if test="${not empty pageInfo }">
		            	<c:forEach items="${pageInfo.content }" var="info">
		            	<tr id="${info.ORG_ID }-${info.TEACH_PLAN_ID }">
		            		<td class="gradeName">${info.GRADE_NAME }</td>
		            		<td class="replaceCourseCode">${info.REPLACE_COURSE_CODE }</td>
		            		<td class="replaceCourseName">${info.REPLACE_COURSE_NAME }</td>
		            		<td class="specialty">${info.SPECIALTY_NAME }<br><small class="gray9">(${info.RULE_CODE })</small></td>
		            		<td>${info.COURSE_NAME }<br><small class="gray9">(${info.COURSE_CODE })</small></td>
		            		<td class="chooseCount">${info.CHOOSE_COUNT }</td>
		            		<td><fmt:formatDate value="${info.UPCOURSE_END_DATE }" pattern="yyyy-MM-dd"/></td>
		            		<td>
		            			<c:if test="${info.PLAN_STATUS == 0}"><span class="gray9">未上传</span></c:if>
		            			<c:if test="${info.PLAN_STATUS == 1}"><span class="text-orange">待审核</span></c:if>
		            			<c:if test="${info.PLAN_STATUS == 2}"><span class="text-green">审核通过</span></c:if>
		            			<c:if test="${info.PLAN_STATUS == 3}"><span class="text-red">审核不通过</span></c:if>
		            		</td>
		            		<td>
		            			<c:if test="${info.CERTIFICATE_STATUS == 0}">
		            				<span class="gray9">未上传</span><small class="gray9"></small></c:if>
		            			<c:if test="${info.CERTIFICATE_STATUS == 1}">
		            				<span class="text-orange">上传中</span> <small class="gray9"></small></c:if>
		            			<c:if test="${info.CERTIFICATE_STATUS == 2}">
		            				<span class="text-green">已上传</span> <small class="gray9"></small></c:if>
		            			<br><small class="gray9 uploadCount">(${info.UPLOAD_COUNT })</small>
		            		</td>
		            		<td>
		            			<c:if test="${info.SCORE_STATUS == 0}"><span class="gray9">未上传</span></c:if>
		            			<c:if test="${info.SCORE_STATUS == 1}"><span class="text-orange">待审核</span></c:if>
		            			<c:if test="${info.SCORE_STATUS == 2}"><span class="text-green">审核通过</span></c:if>
		            			<c:if test="${info.SCORE_STATUS == 3}"><span class="text-red">审核不通过</span></c:if>
		            			<c:if test="${info.SCORE_STATUS == 6}"><span class="text-red">上传中</span></c:if>
		            		</td>
		            		<td data-orgId="${info.ORG_ID }" data-teachPlanId="${info.TEACH_PLAN_ID }" data-customCourseId="${info.CUSTOM_COURSE_ID }"
		            				data-chooseCount="${info.CHOOSE_COUNT }" data-uploadCount="${info.UPLOAD_COUNT }" >
		            			<c:choose>
		            				<c:when test="${info.PLAN_STATUS == 2 or info.PLAN_STATUS == 1}">
		            					<a href="#" class="operion-item plan" data-toggle="tooltip" data-container="body" title="查看授课计划"><i class="fa fa-view-more"></i></a>
		            					<c:if test="${info.PLAN_STATUS == 2}">
		            					<c:choose>
		            						<c:when test="${info.CERTIFICATE_STATUS == 2}">
		            							<a href="#" class="operion-item certificate" data-toggle="tooltip" data-container="body" title="查看授课凭证"><i class="fa fa-search-plus"></i></a>
		            							<c:choose>
		            								<c:when test="${info.SCORE_STATUS ==2 or info.SCORE_STATUS ==1}">
		            									<a href="#" class="operion-item score" data-toggle="tooltip" data-container="body" title="查看成绩"><i class="glyphicon glyphicon-search f16"></i></a>
		            								</c:when>
		            								<c:otherwise>
		            									<a href="#" class="operion-item score" data-toggle="tooltip" data-container="body" title="上传成绩"><i class="glyphicon glyphicon-open f16"></i></a>
		            								</c:otherwise>
		            							</c:choose>
		            						</c:when>
		            						<c:otherwise>
		            							<a href="#" class="operion-item certificate" data-toggle="tooltip" data-container="body" title="上传授课凭证"><i class="fa fa-cloud-upload"></i></a>
		            						</c:otherwise>
		            					</c:choose>
		            					</c:if>
		            				</c:when>
		            				<c:otherwise>
		            					<a href="#" class="operion-item plan" data-toggle="tooltip" data-container="body" title="上传授课计划"><i class="fa fa-upload"></i></a>
		            				</c:otherwise>
		            			</c:choose>
		            		</td>
		            	</tr>
		            	</c:forEach>
		            	</c:if>
		            </tbody>
				</table>
				<c:if test="${not empty pageInfo }">
					<tags:pagination page="${pageInfo}" paginationSize="5" />
				</c:if>
			</div>
		</div>
	</div>
</form>
</section>
</body>
<!-- 底部 -->
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
$(".box-body").find(".plan").unbind("click").bind("click",function() {
	var custom = buildCustom($(this).parent());
	location.href="toPlan/"+custom.orgId+"/"+custom.teachPlanId;
});
$(".box-body").find(".certificate").unbind("click").bind("click",function() {
	var custom = buildCustom($(this).parent());
	location.href="toCertificate/"+custom.customCourseId;
});
$(".box-body").find(".score").unbind("click").bind("click",function() {
	var custom = buildCustom($(this).parent());
	location.href="toScore/"+custom.customCourseId;
});
function buildCustom(t) {
	var custom = {};
	custom["customCourseId"] = t.attr("data-customCourseId");
	custom["orgId"] = t.attr("data-orgId");
	custom["teachPlanId"] = t.attr("data-teachPlanId");
	var tr = $("#"+custom["orgId"]+"-"+custom["teachPlanId"]);
	
	/* custom["chooseCount"] = t.attr("data-chooseCount");
	custom["uploadCount"] = t.attr("data-uploadCount"); */
	custom["gradeName"] = tr.find(".gradeName").html();
	custom["replaceCourseCode"] = tr.find(".replaceCourseCode").html();
	custom["replaceCourseName"] = tr.find(".replaceCourseName").html();
	custom["course"] = tr.find(".course").html();
	custom["uploadCount"] = tr.find(".uploadCount").html();
	custom["uploadCount"] = tr.find(".uploadCount").html();
	custom["chooseCount"] = tr.find(".chooseCount").html();
	custom["uploadCount"] = tr.find(".uploadCount").html();
	var spcialty = tr.find(".specialty").html();
	if(spcialty != undefined || spcialty != '') {
		spcialty = spcialty.replace(/<[^>]+>/g,"");;
		custom["specialty"] = spcialty;
	}
	if(custom.customCourseId == undefined || custom.customCourseId == '') {
		sessionStorage.setItem("custom",JSON.stringify(custom));
	} else {
		sessionStorage.setItem(custom.customCourseId,JSON.stringify(custom));
	}
	console.info(custom);
	return custom;
}
</script>
</html>
