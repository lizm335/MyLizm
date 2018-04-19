<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>辅导教师管理系统-操作管理</title>

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
            	gjtSchoolInfoId: {
            		validators: {
                        notEmpty: {
                        }
                    }
                },
                gradeId: {
                    validators: {
                        notEmpty: "required"
                     }
                },
                termName: {
                    validators: {
                        notEmpty: "required"
                    }
                },
                startDates: {
                	validators: {
	                       notEmpty: "required"
                	}
                },
                endDates: {
                	validators: {
	                       notEmpty: "required"
                	}
                }
            }
        });
	
    $('.datepicker').datepicker({
	      autoclose: true,
	      format : 'yyyy-mm-dd', //控件中from和to 显示的日期格式
	      language: 'zh-CN',
	      todayHighlight: true
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
						action="${ctx }/edumanage/termInfo/${action }" method="post">
						<input id="action" type="hidden" name="action" value="${action }">
						<input type="hidden" name="termId" value="${item.termId }">
						<div class="box-body">
							<div class="form-group">
								<label class="col-sm-1 control-label"><small class="text-red">*</small>所属院校:</label>
								<div class="col-sm-3">
									<select name="gjtSchoolInfoId"
										class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
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
								<label class="col-sm-1 control-label"><small class="text-red">*</small>年级:</label>
								<div class="col-sm-3">
									<select name="gradeId"
										class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="">请选择</option>
										<c:forEach items="${gradeMap}" var="map">
											<c:if test="${action=='create'}">
													<option value="${map.key}">${map.value}</option>
											</c:if>
											<c:if test="${action!='create'}">
												<option value="${map.key}" <c:if test="${map.key==item.gjtGrade.gradeId}">selected='selected'</c:if>>${map.value}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">学期代码:</label>
								<div class="col-sm-3">
										<input type="text" class="form-control" name="termCode"  value="${item.termCode}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label"><small class="text-red">*</small>学期名称:</label>
								<div class="col-sm-3">
										<input type="text" class="form-control" name="termName"  value="${item.termName}">
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-1 control-label"><small class="text-red">*</small>开始时间:</label>
								<div class="col-sm-3">
									<input type="text" class="form-control pull-right datepicker" name="startDates" 
						                  value="<fmt:formatDate value="${item.startDate}" pattern="yyyy-MM-dd"/>" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-1 control-label"><small class="text-red">*</small>结束时间:</label>
								<div class="col-sm-3">
									<input type="text" class="form-control pull-right datepicker" name="endDates" 
						                  value="<fmt:formatDate value="${item.endDate}" pattern="yyyy-MM-dd"/>" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-1 control-label">学期描述:</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" name="termDesc"
										value="${item.termDesc}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">备注:</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" name="memo"	value="${item.memo}">
								</div>
							</div>
						</div>
						<div class="box-footer">
							<div class="col-sm-offset-1 col-sm-11">
								<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
								<button type="reset" class="btn btn-primary"
									onclick="history.back()">返回</button>
							</div>
						</div>
					</form>

				</div>
			</div>
		</div>
	</section>
</body>
</html>