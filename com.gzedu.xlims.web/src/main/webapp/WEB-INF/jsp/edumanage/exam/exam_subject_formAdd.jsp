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
					<label class="col-sm-2 control-label">科目编号</label>
					<div class="col-sm-7">
						<p class="form-control-static">
							<input type="hidden" name="entity_subjectCode" id="entity_subjectCode" value="${entity.subjectCode}"/>
							${entity.subjectCode}
						</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>科目名称</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="entity_name" id="entity_name" value="${entity.name}" placeholder="科目名称"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>课程</label>
					<div class="col-sm-6">
						<div class="pull-right margin_l10">
							<button type="button" class="btn btn-default" data-role="select-pop" data-target="s1">选择课程</button>
						</div>
						<div class="select2-container select2-container--default show oh">
							<div class="select2-selection--multiple">
								<ul class="select2-selection__rendered select-container-ul" data-id="s1">
									
								</ul>			
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>试卷号:</label>
					<div class="col-sm-3">
						<input type="text" maxlength="32" name="entity_examNo" id="entity_exam_no" class="form-control" value="${entity.examNo}" <c:if test="${action=='update'}">disabled="disabled"</c:if> onblur="getIsExistNo()"/>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-7 col-sm-offset-2">
						<button id="btn-create" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit" onclick="createSubject()">保存</button>
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



function createSubject(){
	var entity_name = $("#entity_name").val();
	var entity_exam_no = $("#entity_exam_no").val();
	
	if(entity_name == null){
		alert('请填写考试科目名称');
		return false;
	}
	
	if(entity_exam_no == null){
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
		url: ctx+'/exam/new/subject/saveSubject',
		dataType: 'json',
		data:params,
		type:"post",
		success: function(obj) {
			  if(obj.successful == true) {
				 // alert('新增成功');
				  postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>');
				  setTimeout(function(){
					  window.location.href = ctx + '/exam/new/subject/list';
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
