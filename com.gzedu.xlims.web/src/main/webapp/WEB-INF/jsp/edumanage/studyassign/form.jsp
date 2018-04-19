<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学年度制作任务</title>

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
            	sort: {
            		validators: {
                        notEmpty: {
                        }
                    }
                },
                assignmentName: {
                    validators: {
                        notEmpty: "required"
                     }
                },
                assignmentEnact: {
                    validators: {
                        notEmpty: "required"
                    }
                }
            }
        });
	
    $('.datepickers').datepicker({
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
					<form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/edumanage/studyassign/${action }" method="post">
						<input id="action" type="hidden" name="action" value="${action }">
						<input type="hidden" name="assignmentId" value="${item.assignmentId }">
						<input type="hidden" name="id" value="${id}">
						<div class="box-body">
							<div class="form-horizontal reset-form-horizontal margin_t10 add-major">
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">序号</label>
										<div class="col-md-3 col-sm-10 margin_b15">
											<input type="text" class="form-control" name="sort"  value="${item.sort}" placeholder="序号"/>
										</div>
								</div>
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">任务名称</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<input type="text" class="form-control" name="assignmentName"  value="${item.assignmentName}" placeholder="任务名称" />
									</div>
								</div>
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">任务制定人</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<input type="text" class="form-control" name="assignmentEnact"  value="${item.assignmentEnact}" placeholder="任务制定人" />
									</div>
								</div>
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">任务执行人</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<input type="text" class="form-control" name="assignmentDo"  value="${item.assignmentDo}" placeholder="任务执行人" />
									</div>
								</div>
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">对应功能</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<input type="text" class="form-control" name="parallelism"  value="${item.parallelism}" placeholder="对应功能" />
									</div>
								</div>
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">备注</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<input type="text" class="form-control" name="memo"  value="${item.memo}" placeholder="备注" />
									</div>
								</div>
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">开始时间</label>
									
									<div class="col-md-3 col-sm-10 margin_b15">
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											 </div>
										<input type="text" class="form-control datepickers" name="startDate"  value="${item.startDate}" placeholder="开始时间" />
										</div>
									</div>
								</div>
								<div class="form-group margin-bottom-none">
									<label class="col-md-2 col-sm-2 control-label margin_b15">结束时间</label>
									<div class="col-md-3 col-sm-10 margin_b15">
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											 </div>
										<input type="text" class="form-control datepickers" name="endDate"  value="${item.endDate}" placeholder="结束时间" />
										</div>
									</div>
								</div>
								<div class="form-group margin-bottom-none">
									<label class="col-xs-2 control-label margin_b15 sr-only"></label>
									<div class="col-xs-5 margin_b15">
										<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
										<a href="${ctx }/edumanage/studyassign/list/${id}" class="btn btn-primary" >返回</a>
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