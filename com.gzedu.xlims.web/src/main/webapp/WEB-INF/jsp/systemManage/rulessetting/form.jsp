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
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">规则设置</a></li>
		<li class="active">编辑规则设置</li>
	</ol>
</section>

<section class="content">
	<div class="box margin-bottom-none">
		<div class="box-body">
		<form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/system/rulessetting/${action }" method="post">
		<input id="action" type="hidden" name="action" value="${action }">
		<input type="hidden" name="id" value="${item.id }">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-3 control-label"><small class="text-red">*</small>所属院校</label>
					<div class="col-sm-6">
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
				<div class="form-group">
					<label class="col-sm-3 control-label"><small class="text-red">*</small>班级类型</label>
					<div class="col-sm-6">
						<select name ="classtype"class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
							<option value="teach" <c:if test="${item.classtype=='teach'}">selected='selected'</c:if>>教务班级</option>
							<option value="course" <c:if test="${item.classtype=='course'}">selected='selected'</c:if>>课程班级</option>
						</select>
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-3 control-label">年级</label>
					<div class="col-sm-6">
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
				<div class="form-group">
					<label class="col-sm-3 control-label">层次</label>
					<div class="col-sm-6">
						<select name ="pycc"class="selectpicker show-tick form-control" 
							data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
							<c:forEach items="${pyccMap}" var="map">
								<option value="${map.key}"  <c:if test="${map.key==item.pycc}">selected='selected'</c:if>>${map.value}	</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">专业</label>
					<div class="col-sm-6">
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
				<div class="form-group">
					<label class="col-sm-3 control-label">学习中心</label>
					<div class="col-sm-6">
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
				<div class="form-group">
					<label class="col-sm-3 control-label">班级上限人数</label>
					<div class="col-sm-6">
						<div class="input-group">
							<input name="peopleNo" class="form-control" placeholder="人数" value="${item.peopleNo}" >
							<span class="input-group-addon">人</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3">
						<button type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
						<a href="${ctx }/system/rulessetting/list.html" class="btn btn-default min-width-90px btn-cancel-edit" >返回</a>
					</div>
				</div>
			</div>
			</form>
		</div>
	</div>
</section>
<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>