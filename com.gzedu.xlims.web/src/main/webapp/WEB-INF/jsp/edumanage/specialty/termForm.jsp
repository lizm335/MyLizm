<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/edumanage/specialty/${action}" method="post">
<input name="specialtyId" value="${specialtyId}" hidden="hidden"/>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">新增学期</h3>
	</div>
	<div class="box-body" style="height:194px;">
		<table class="table-block full-width full-height">
			<tr>
				<td valign="middle">
					<table class="per-tbl" style="margin:0 auto;width:75%;">
						<tr>
							<th class="text-no-bold text-right">
								<div class="pad-t5">
									<small class="text-red">*</small>
									学期名称：
								</div>
							</th>
							<td>
								<!-- <input name="" type="text" class="form-control" placeholder="学期名称" data-role="semester-name"> -->
								<select name="termTypeCode" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="1">第一学期</option>
									<option value="2">第二学期</option>
									<option value="3">第三学期</option>
									<option value="4">第四学期</option>
								</select>
								<!-- 
								<div class="error-tips text-red hide-block">
									<i class="fa fa-fw fa-exclamation-circle"></i>
									学期名称不能为空
								</div> -->
							</td>
						</tr>
						<tr>
							<th class="text-no-bold text-right">
								<div class="pad-t5">
									<small class="text-red">*</small>
									课程数：
								</div>
							</th>
							<td>
								<input type="text" class="form-control" placeholder="课程数" data-role="course-num">
								<div class="error-tips text-red hide-block">
									<i class="fa fa-fw fa-exclamation-circle"></i>
									课程数不能为空
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="submit" class="btn btn-success min-width-90px" data-role="sure">确认</button>
</div>
</form>

<!--添加学期 模块-->
<script type="text/template" id="tmp">
<div class="table-responsive tea-plan-box">
  	<table class="tea-plan-tbl table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none" data-semester="{name}">
  		<thead>
  			<tr>
  				<th rowspan="2" width="50">序号</th>
  				<th rowspan="2">学期</th>
  				<th colspan="5">课程设置</th>
  				<th colspan="2">考试设置</th>
  				<th rowspan="2">学分</th>
  				<th rowspan="2">学时</th>
  				<th rowspan="2">形考比例</th>
  				<th rowspan="2" width="80">操作</th>
  			</tr>
  			<tr>
  				<th>课程模块</th>
  				<th>课程名称</th>
  				<th>是否必修</th>
  				<th>替换课程</th>
  				<th>主教材</th>
  				<th>考试方式</th>
  				<th>考试科目</th>
  			</tr>
  		</thead>
  		<tbody></tbody>
  	</table>
  	<a href="新增课程.html" role="button" class="btn btn-block text-green nobg margin_t5" data-role="add-course"><i class="fa fa-fw fa-plus"></i>添加课程</a>
</script>
<!-- jQuery 2.1.4 --> 
<!--[if gte IE 9]>
	<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
	<![endif]--> 

<!--[if !IE]><!--> 
<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script> 
<!--<![endif]--> 

<!--[if lt IE 9]>
		<script src="plugins/jQuery/jquery-1.12.4.min.js"></script>
	<![endif]--> 
<script src="bootstrap/js/bootstrap.min.js"></script> 
<!-- custom-model --> 
<script src="plugins/custom-model/custom-model.js"></script>
<!-- SlimScroll 1.3.0 --> 
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script> 
<!-- AdminLTE App --> 
<script src="dist/js/app.js"></script> 
<!-- common js --> 
<script src="dist/js/common.js"></script> 
<script type="text/javascript">

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//输入框
$('[data-role="semester-name"],[data-role="course-num"]').keyup(function(event) {
	if($.trim($(this).val())!=""){
		$(this).removeClass('Validform_error').siblings('.error-tips').hide();
	}
	else{
		$(this).addClass('Validform_error').siblings('.error-tips').show();
	}
});
//确认
$("button[data-role='sure']").click(function(event) {
	var $semesterName=$('[data-role="semester-name"]');
	var $courseNum=$('[data-role="course-num"]');
	var i=0;

	if( $.trim($semesterName.val())=="" ){
		i++;
		$semesterName.addClass('Validform_error').siblings('.error-tips').show();
	}
	if( $.trim($courseNum.val())=="" ){
		i++;
		$courseNum.addClass('Validform_error').siblings('.error-tips').show();
	}
	if(i>0) return;

	var html=$("#tmp").html();
	html=html.replace(/\{name\}/g,$semesterName.val());

	parent.$('[data-role="box"]').append(html);
	parent.$.closeDialog(frameElement.api);
});



</script>
</body>
</html>