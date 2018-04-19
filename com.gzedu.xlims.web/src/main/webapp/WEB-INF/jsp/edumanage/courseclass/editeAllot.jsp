<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任管理系统-操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	$('#inputForm').bootstrapValidator({
  		excluded: [':disabled'],//验证下拉必需加
          fields: {
        	  inspectorId: {
          		validators: {
                      notEmpty: "required"
                  }
              },
              counselorId: {
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
					<form id="inputForm" class="form-horizontal" role="form"
						action="${ctx }/edumanage/courseclass/allotHeadTeacherUpdate" method="post">
						<div class="box-body">
							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>辅导教师:</label>
								<div class="col-sm-3">
									<select  id="counselorId" name="counselorId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="">请选择</option>
										<c:forEach items="${counselorMap}" var="map">
											<option value="${map.key}">${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>督导教师:</label>
								<div class="col-sm-3">
									<select  id="inspectorId" name="inspectorId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="">请选择</option>
										<c:forEach items="${inspectorMap}" var="map">
											<option value="${map.key}">${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>班级:</label>
								<div class="col-sm-3">
									<table class="table table-striped">
										<tr>
											<th><input type="checkbox" checked="checked" class="select-all"></th>
											<th>班级名称</th>
										<tr>
										<c:forEach items="${classInfoMap}" var="item" >
										<tr>
											<td><input  type="checkbox" value="${item.id}"name="ids"  checked="checked" class="checkbox"/></td>
											<td>${item.name}</td>
										</tr>
										</c:forEach>
									</table>
								</div>
							</div>
							
							<div class="box-footer">
								<div class="col-sm-offset-1 col-sm-11">
									<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
									<button type="reset" class="btn btn-primary"
										onclick="history.back()">返回</button>
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