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
	$(function() {
		$('select').attr("disabled","disabled");
		$('input[type="radio"]').attr("disabled","disabled");
		$('#province').select_district($('#city'), $("#district"), $("#district").val());
		$(".select2").select2({language: "zh-CN"});
	})
	</script>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" id="go_for_back" onclick="window.history.back()">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">分部管理</a></li>
		<li class="active">分部详情</li>
	</ol>
</section>
<section class="content" data-id="0">
	<div class="box margin-bottom-none">
		<div class="box-body">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">上级单位：</label>
					<div class="col-sm-8 ">
							<p class=" form-control-static">${entity.parentGjtOrg.orgName }</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">分部代码：</label>
					<div class="col-sm-8 ">
						<p class=" form-control-static">${entity.code}</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">分部名称：</label>
					<div class="col-sm-8 ">
						<p class=" form-control-static">${entity.orgName}</p>
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-2 control-label">联系电话：</label>
					<div class="col-sm-8 ">
						<p class=" form-control-static">${entity.gjtSchoolInfo.linkTel}</p>
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-2 control-label">联系人：</label>
					<div class="col-sm-8">
						<p class=" form-control-static">${entity.gjtSchoolInfo.linkMan}</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">电子邮箱：</label>
					<div class="col-sm-8 ">
						<p class=" form-control-static">${entity.gjtSchoolInfo.linkMail}</p>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">所在区域：</label>
					<div class="col-sm-8">
						<div class="row">
							<div class="col-sm-4">
								<select id="province" class=" show-tick form-control" >
									<option value="" selected="selected"></option>
								</select>
							</div>
							<div class="col-sm-4">
								<select id="city" class=" show-tick form-control" >
									<option value="" selected="selected"></option>
								</select>
							</div>
							<div class="col-sm-4">
								<select id="district" name="district" class=" show-tick form-control" >
									<option value="${entity.gjtSchoolInfo.xxqhm}" selected="selected"></option>
								</select>
							</div>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">详细地址:</label>
					<div class="col-sm-8">
						<p class=" form-control-static">${entity.gjtSchoolInfo.xxdz}</p>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">办学模式:</label>
					<div class="col-sm-8">
						<input type="radio"  name="schoolModel" value="1" class="flat-red" <c:if test="${empty entity.schoolModel || entity.schoolModel==1}">checked="checked"</c:if>> 学历办学
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="schoolModel" value="2" class="flat-red" <c:if test="${entity.schoolModel==2}">checked="checked"</c:if>> 中职院校
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="schoolModel" value="3" class="flat-red" <c:if test="${entity.schoolModel==3}">checked="checked"</c:if>> 院校模式（有考试）
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="schoolModel" value="4" class="flat-red" <c:if test="${entity.schoolModel==4}">checked="checked"</c:if>> 院校模式（无考试）
					</div>
				</div> 

				<div class="form-group">
					<label class="col-sm-2 control-label">简介</label>
					<div class="col-sm-8">
						<pre>${entity.gjtSchoolInfo.memo}	</pre>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-8 col-sm-offset-2">
						<a href="${ctx }/organization/branchSchool/list" class="btn btn-default min-width-90px btn-cancel-edit" >关闭</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</html>
