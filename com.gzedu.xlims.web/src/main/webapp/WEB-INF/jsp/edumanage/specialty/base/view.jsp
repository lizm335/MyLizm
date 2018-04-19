<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>专业操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<jsp:include page="/eefileupload/upload.jsp"/>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">专业管理</a></li>
		<li class="active">专业基础信息</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom">
		<ul class="nav nav-tabs nav-tabs-lg">
	      <li data-role="show" class="active"><a href="javascript:void(0)">专业基础信息</a></li>
	    </ul>
	    <div class="tab-content no-padding">
	    	<div class="tab-pane active" id="tab_top_1">
	    		<form id="inputForm"  class="theform" action="${ctx }/edumanage/specialty/base/${action }" method="post">
		    		<div class="pad">
			    		<table class="table-gray-th">
							<tr>
								<th width="15%" class="text-right"><small class="text-red">*</small> 专业代码：</th>
								<td width="35%">
									<div id="ruleCodeDiv" class="position-relative" data-role="valid">
										<input type="text" class="form-control" name="specialtyCode" value="${item.specialtyCode}" 
											 readonly="readonly" />
									</div>
								</td>
								
								<th class="text-right">
								<small class="text-red">*</small> 专业层次：</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select name="specialtyLayer" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true" datatype="*" nullmsg="请选择培养层次！" errormsg="请选择培养层次！">
											<option value="">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==item.specialtyLayer}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
					                </div>
								</td>
							</tr>
							<tr>
								<th width="15%" class="text-right">
								<small class="text-red">*</small> 专业名称：</th>
								<td width="35%">
									<div class="position-relative" data-role="valid">
										<input type="text" class="form-control" name="specialtyName"  value="${item.specialtyName}"
											  readonly="readonly"  />
									</div>
								</td>
								<th class="text-right">
								<small class="text-red">*</small> 责任教师：</th>
								<td>
									<div class="position-relative" data-role="valid">
										<input type="text" class="form-control" name="teacher"  value="${item.teacher}"
											 readonly="readonly" />
					                </div>
								</td>
							</tr>
							<tr>
								<th class="text-right">专业封面：</th>
								<td>
									<div class="inline-block vertical-middle">
										<ul class="img-list clearfix">
											<li>
												<img id="imgId-specialtyImgUrl" src="${item.specialtyImgUrl }" class="user-image" style="cursor: pointer;">
											</li>
										</ul>
									</div>
								</td>
								
								
								<th class="text-right">责任教师照片：</th>
								<td>
									<div class="inline-block vertical-middle">
										<ul class="img-list clearfix">
											<li>
												<img id="imgId-teacherImgUrl" src="${item.teacherImgUrl }" class="user-image" style="cursor: pointer;">
											</li>
										</ul>
									</div>
								</td>
							</tr>
							<tr>
								<th class="text-right">专业简介：</th>
								<td colspan="3">
									<textarea rows="5" cols="120" readonly="readonly" name="specialtyDetails">${item.specialtyDetails }</textarea>
								</td>
							</tr>
							<tr>
								<th class="text-right">责任教师简介：</th>
								<td colspan="3">
									<textarea rows="5" cols="120" readonly="readonly" name="teacherDetails">${item.teacherDetails }</textarea>
								</td>
							</tr>
						</table>
					</div>
				</form>
	    	</div>
	    </div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$("#imgId-specialtyImgUrl").click(function() {
	var src = $(this).attr("src");
	if (src != "") {
		window.open(src);
	}
});
$("#imgId-teacherImgUrl").click(function() {
	var src = $(this).attr("src");
	if (src != "") {
		window.open(src);
	}
});
</script>
</body>
</html>
