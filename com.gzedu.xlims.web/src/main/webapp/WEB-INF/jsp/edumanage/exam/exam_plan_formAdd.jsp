<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript">
var ctx = "${ctx}";
$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
})
</script> 

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li class="active">新增网考科目</li>
	</ol>
</section>
<section class="content">
<form id="inputForm" class="form-horizontal" role="form"  method="post">
	<input type="hidden" id="entity_type" value="${entity.type}" name="entity_type"/>
	<div class="box no-margin">
		<div class="box-body pad-t15">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">考试计划</label>
					<div class="col-sm-3">
						<select name="EXAM_BATCH_ID" id="exam_batch_id" class="selectpicker show-tick form-control" 
							data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
							<c:forEach items="${planMap}" var="map">
								<option value="${map.key}"<c:if test="${map.key==param.EXAM_BATCH_ID}">selected='selected'</c:if> >${map.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>开考科目名称</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" name="EXAM_PLAN_NAME" id="exam_plan_name" value="" placeholder="开考科目名称"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>开考科目编号</label>
					<div class="col-sm-3">
						<p class="form-control-static">
							<input type="hidden" class="form-control" name="EXAM_PLAN_CODE" id="exam_plan_code" value="${entity.examPlanCode}"/>
								${entity.examPlanCode}
						</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>课程</label>
					<div class="col-sm-5">
						<div class="pull-right margin_l10">
							<button type="button" class="btn btn-default" data-role="select-pop" data-target="s1">选择课程</button>
						</div>
						<div class="select2-container select2-container--default show oh">
							<div class="select2-selection--multiple">
								<ul class="select2-selection__rendered select-container-ul" data-id="s1" >
									
								</ul>			
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>形考比例:</label>
					<div class="col-sm-3">
						<input type="text" maxlength="32" name="XK_PERCENT" id="xk_percent" class="form-control" placeholder="形考比例"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>试卷号:</label>
					<div class="col-sm-3">
						<input type="text" maxlength="32" name="EXAM_NO" id="exam_no" placeholder="试卷号" class="form-control" value="" <c:if test="${action=='update'}">disabled="disabled"</c:if> onblur="getIsExistNo()"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>考试预约方式:</label>
					<div class="radio">
						<label>
							<input class="minimal_invigi" name="EXAM_PLAN_ORDER" type="radio" value="1" checked>个人预约
						</label>
						<label class="left10">
							<input class="minimal_invigi" name="EXAM_PLAN_ORDER" type="radio" value="2">集体预约
						</label>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label margin_b15">考试预约时间：</label>
					<div class="col-sm-6">
						<div class="input-group" data-role="date-group">
							<div class="input-group full-width">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" name="BOOK_ST" id="book_st" class="form-control" data-role="date-start">
							</div>
							<div class="input-group-addon no-border">至</div>
							<div class="input-group full-width">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" name="BOOK_END" id="book_end" class="form-control" data-role="date-end">
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label margin_b15">考试时间：</label>
					<div class="col-sm-6">
						<div class="input-group" data-role="date-group">
							<div class="input-group full-width">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" name="EXAM_ST" id="exam_st" class="form-control" data-role="date-start">
							</div>
							<div class="input-group-addon no-border">至</div>
							<div class="input-group full-width">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" name="EXAM_END" id="exam_end" class="form-control" data-role="date-end">
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-7 col-sm-offset-2">
						<button id="btn-create" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit" onclick="createExamPlan()">确定</button>
						<button class="btn btn-default min-width-90px btn-cancel-edit" data-role="back-off">取消</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
/*日期控件*/
$('[data-role="date-group"]').each(function(i,e){
	var startDate=$('[data-role="date-start"]',e);
	var endDate=$('[data-role="date-end"]',e);
	//开始时间			
	startDate.datepicker({
	  language:'zh-CN',
	  format:'yyyy-mm-dd'
	}).on('changeDate', function(e) {
		var add=increaseOnedate(e.target.value);
		endDate.datepicker('setStartDate',add);
	});
	//结束时间
	endDate.datepicker({
	  language:'zh-CN',
	  format:'yyyy-mm-dd'
	}).on('changeDate', function(e) {
		var d=decreaseOnedate(e.target.value);
		startDate.datepicker('setEndDate',d);
	}).on('focus',function(){
		if(this.value==""&&startDate.val()==""){
			startDate.focus();
			endDate.datepicker('hide');
		}
	});
});



//选择课程
$("[data-role='select-pop']").click(function(event) {
	$(".select-container-ul").removeClass('on');
	$('[data-id="'+$(this).data('target')+'"]').addClass('on');

	$.mydialog({
	  id:'select-course',
	  width:850,
	  height:600,
	  zIndex:11000,
	  content: 'url:'+ctx+'/exam/new/subject/getGjtCourseList?COURSE_IDS='+'${entity.subjectCode}'
	});
});

//删除课程
$(".select-container-ul").on('click', '.select2-selection__choice__remove', function(event) {
	event.preventDefault();
	$("#"+$(this).attr("aria-describedby")).remove();
	$(this).parent().remove();
});

function getIsExistNo(){
	var entity_exam_no = $("#entity_exam_no").val();
	var entity_type = $("#entity_type").val();
	if(entity_exam_no!=null){
		var params = {
				entity_examNo:entity_exam_no,
				entity_type:entity_type
		};
		$.ajax({
			type:"post",
			url:ctx+'/exam/new/subject/getIsExistExamNo',
			dataType:"json",
			data:params,
			success:function(data){
				if(data.TEMP!="0"){
					alert("该试卷号对应的考试科目已存在, 请勿重复创建!");
					return;
				}
			}
		});
	}
}



function createExamPlan(){
	var exam_plan_name = $("#exam_plan_name").val();
	var xk_percent = $("#xk_percent").val();
	var exam_no = $("#exam_no").val();
	
	if(exam_plan_name == null){
		alert('请填写开考科目名称');
		return false;
	}
	
	if(xk_percent == null){
		alert('请填写形考比例');
		return false;
	}
	
	if(exam_no == null){
		alert('请填写试卷号');
		return false;
	}
	
	/*插入业务逻辑*/
	var postIngIframe=$.mydialog({
		id:'dialog-1',
		width:150,
		height:50,
		backdrop:false,
		fade:true,
		showCloseIco:false,
		zIndex:11000,
		content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
	});
	
	var params = $("#inputForm").serialize();
	$.ajax({
		url: ctx+'/exam/new/plan/createExamPlan',
		dataType: 'json',
		data:params,
		type:"post",
		success: function(obj) {
			  if(obj.successful == true) {
				 // alert('新增成功');
				  postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>');
				  setTimeout(function(){
					  window.location.href = ctx + '/exam/new/plan/list';
					},2000)
			  } else {
				  alert(obj.message);
			  }
		  }
		
	});
	
}



function dataValid(data) {
	if(!data.subjectCode || !data.type) return false;	
	if(!data.courseId) {
		alert('请选择课程');
		return false;
	}
	if(!data.name) {
		alert('请填写考试科目名称');
		return false;
	}
	if(!data.examNo) {
		alert('请填写试卷号');
		return false;
	}
	return true;
}
</script>
</body>
</html>
