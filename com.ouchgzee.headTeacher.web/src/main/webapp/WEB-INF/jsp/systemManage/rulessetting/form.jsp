<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学年度操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("readonly","readonly");
		$('select').attr("disabled","disabled");
		$('#btn-submit').remove();  
	}
	//参考： http://bv.doc.javake.cn/examples/ 
	
	
    $('#inputForm').bootstrapValidator({
    		excluded: [':disabled'],//验证下拉必需加
            fields: {
            	'gjtSchoolInfo.id': {
            		validators: {
                        notEmpty: {
                        }
                    }
                },
                classtype: {
                    validators: {
                        notEmpty: "required"
                     }
                },
                peopleNo: {
                    validators: {
                        notEmpty: "required"
                    }
                }
            }
        });
})
</script>

</head>
<body class="inner-page-body">
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">操作管理</h3>
					</div>
					<form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/system/rulessetting/${action }" method="post">
						<input id="action" type="hidden" name="action" value="${action }">
						<input type="hidden" name="id" value="${item.id }">
						<div class="box-body">
							<div class="form-horizontal reset-form-horizontal margin_t10 add-major">
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15"><small class="text-red">*</small>所属院校</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<select name="gjtSchoolInfo.id" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${schoolInfoMap}" var="map">
												<c:if test="${action=='create'}">
														<option value="${map.key}">${map.value}</option>
												</c:if>
												<c:if test="${action!='create'}">
													<option value="${map.key}" <c:if test="${map.key==item.gjtSchoolInfo.id}">selected='selected'</c:if>>${map.value}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15"><small class="text-red">*</small>班级类型</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<select name ="classtype"class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<option value="teach" <c:if test="${item.classtype=='teach'}">selected='selected'</c:if>>教学班级</option>
											<option value="course" <c:if test="${item.classtype=='course'}">selected='selected'</c:if>>课程班级</option>
										</select>
									</div>
								</div>
								
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">年级</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<select name="gjtGrade.gradeId"	class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${gradeMap}" var="map">
												<c:if test="${action=='create'}">
													<option value="${map.key}">${map.value}</option>
												</c:if>
												<c:if test="${action!='create'}">
													<option value="${map.key}"  <c:if test="${map.key==item.gjtGrade.gradeId}">selected='selected'</c:if> >	${map.value}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">培养层次</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<select name ="pycc"class="selectpicker show-tick form-control" 
											data-size="5" data-live-search="true">
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}"  <c:if test="${map.key==item.pycc}">selected='selected'</c:if>>${map.value}	</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">专业名称</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<select name="gjtSpecialty.specialtyId"  class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" >
											<option value="">请选择专业</option>
											<c:forEach items="${specialtyMap}" var="map">
												<c:if test="${action=='create'}">
													<option value="${map.key}">${map.value}</option>
												</c:if>
												<c:if test="${action!='create'}">
													<option value="${map.key}"  <c:if test="${map.key==item.gjtSpecialty.specialtyId}">selected='selected'</c:if> >${map.value}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">学习中心</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<select name="gjtStudyCenter.id" class="selectpicker show-tick form-control"  data-size="5" data-live-search="true"  >
											<option value="">请选择学习中心</option>
											<c:forEach items="${studyCenterMap}" var="map">
												<c:if test="${action=='create'}">
													<option value="${map.key}">${map.value}</option>
												</c:if>
												<c:if test="${action!='create'}">
													<option value="${map.key}"  <c:if test="${map.key==item.gjtStudyCenter.id}">selected='selected'</c:if> >${map.value}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group margin-bottom-none">
								<label class="col-md-2 col-sm-2 control-label margin_b15">班级上限人数</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<input type="number" class="form-control " name="peopleNo" placeholder="班级上限人数" 	value="${item.peopleNo}" />
									</div>
								</div>
								
								<div class="form-group margin-bottom-none">
									<label class="col-xs-2 control-label margin_b15 sr-only"></label>
									<div class="col-xs-5 margin_b15">
										<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
										<a href="${ctx }/system/rulessetting/list.html" class="btn btn-primary" >返回</a>
									</div>
								</div>
							</div>	
						</div>
					</form>

				</div>
			</div>
		</div>
	</section>
</body>
</html>