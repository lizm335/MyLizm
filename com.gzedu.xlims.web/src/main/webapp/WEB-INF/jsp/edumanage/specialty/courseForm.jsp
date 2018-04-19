<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript">
$(function() {
	if($('#action').val() == 'viewPlan'){
		$(':input').attr("readonly","readonly");
		$('select').attr("disabled","disabled");
		$('[data-role="sure"]').remove();  
	}    
	$("#studyRatio").change(function(){
		  var value = $(this).val();
		  if(value>100){
			  $(this).val(100);
		  }else if(value<0 || value == 0){ 
			  $(this).val(0);
		  }
		  $("#examRatio").val(100 - $(this).val());
	  });
	  
	  $("#examRatio").change(function(){
		  var value = $(this).val();
		  if(value>100){
			  $(this).val(100);
		  }else if(value<0 || value == 0){
			  $(this).val(0);
		  }
		  $('#studyRatio').val(100 - $(this).val());
	  });
	  $('#inputForm').bootstrapValidator({
		  //excluded: [':disabled'],验证下拉必需加
	        fields: {	        	
	        	score: {
	                validators: {
	                    notEmpty: "required"
	                }
	            } 
	        }
	    }); 
})
</script>
</head>
<body>
<form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/edumanage/specialty/${action}" method="post">
<input type="hidden" name="specialtyId" value="${param.specialtyId}" id="specialtyId"/>
<input type="hidden" name="termTypeCode" value="${param.termTypeCode}" id="termTypeCode"/>
<input type="hidden" name="id" value="${param.id}"/>
<input id="action" type="hidden" value="${action }">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">新增课程</h3>
	</div>
	<div class="box-body pad-t15">	
		<div class="form-horizontal reset-form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label"><small class="text-red">*</small>课程模块：</label>
				<div class="col-sm-7">
					<select name="courseTypeId"
						class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
						<c:forEach items="${courseTypeMap}" var="map">
								<option value="${map.key}" <c:if test="${map.key==entity.courseTypeId}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label"><small class="text-red">*</small>课程性质：</label>
				<div class="col-sm-7">
					<select class="form-control" name="courseCategory">
	                  <option value="0" <c:if test="${entity.courseCategory=='0'}"> selected="selected"</c:if>>选修</option>
	                  <option value="1" <c:if test="${entity.courseCategory=='1'}"> selected="selected"</c:if>>必修</option>
	                  <option value="2" <c:if test="${entity.courseCategory=='2'}"> selected="selected"</c:if>>补修</option>
	                </select>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label"><small class="text-red">*</small>课程名称：</label>
				<div class="col-sm-7">
					<div class="pull-right margin_l10">
						<a role="button" class="btn btn-default" <c:if test="${action!='viewPlan' }">href="choiceCourseList?action=course" data-role="select-pop"</c:if> data-target="s1">选择课程</a>
					</div>
					<div class="select2-container select2-container--default show oh">
						<div class="select2-selection--multiple">
							<ul class="select2-selection__rendered select-container-ul" data-id="s1">
								<c:if test="${not empty entity.gjtCourse }">
									<li class="select2-selection__choice">										
									<c:if test="${action!='viewPlan' }">
									<span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>
									</c:if>
									<span class="select2-name" title="${entity.gjtCourse.kcmc}" data-toggle="tooltip" 
									data-container="body" data-order="${entity.gjtCourse.kch}">${entity.gjtCourse.kcmc}</span>
					       			<input type="hidden" name = "courseId" value="${entity.gjtCourse.courseId}"/>	
					      			</li>
				      			</c:if>
							</ul>			
						</div>
					</div>
				</div>
			</div>
		</div>
		
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">确认</button>
</div>
</form>
<script type="text/template" id="temp">
	<tr>
		<td class="order"></td>
		{semester}
		<td>{courseModel}</td>
		<td>
			{courseNameOrder}
		</td>
		<td>{courseBiXiu}</td>
		<td>{courseReplace}</td>
		<td>
			{jqNameOrder}
		</td>
		<td>{textType}</td>
		<td>
			{kmNameOrder}
		</td>
		<td>{credits}</td>
		<td>{learningTime}</td>
		<td>{proportion}</td>
		<td>
			<a href="#" class="operion-item" data-toggle="tooltip" title="编辑" data-role="add-course"><i class="fa fa-edit"></i></a>
			<a href="#" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
		</td>
	</tr>
</script>
<script type="text/javascript">
$('.slim-Scroll').slimScroll({
    height: $(window).height()-126,
    size: '5px'
});

//选择课程
$("[data-role='select-pop']").click(function(event) {
	event.preventDefault();

	var $target=$('[data-id="'+$(this).data('target')+'"]');
	$(".select-container-ul").removeClass('on');
	$target.addClass('on')

	$.mydialog({
	  id:'select-course',
	  width:960,
	  height:550,
	  zIndex:11000,
	  content: 'url:'+$(this).attr("href")
	});
});

//删除课程
$(".select-container-ul").on('click', '.select2-selection__choice__remove', function(event) {
	event.preventDefault();
	$("#"+$(this).attr("aria-describedby")).remove();
	$(this).parent().remove();
});

function createHtml(){
	var $container=parent.$(".tea-plan-box.on");
	var $sTxt=$container.find(".semester");
	var tmp=$("#temp").html();

	if($sTxt.length<=0){
		tmp=tmp.replace(/\{semester\}/g,[
				'<td class="semester" rowspan="1">',
					$container.find("table").data("semester"),
				'</td>'
			].join(''));
	}
	else{
		tmp=tmp.replace(/\{semester\}/g,'');
		$sTxt.attr("rowspan",parseInt($sTxt.attr("rowspan"))+1);
	}
	tmp=tmp.replace(/\{courseModel\}/g,$('[name="courseModel"]').val());
	tmp=tmp.replace(/\{courseBiXiu\}/g,$('[name="courseType"]').val());
	tmp=tmp.replace(/\{textType\}/g,$('[name="testType"]').val());
	tmp=tmp.replace(/\{learningTime\}/g,$('[name="learningTime"]').val());
	tmp=tmp.replace(/\{credits\}/g,$('[name="credits"]').val());
	tmp=tmp.replace(/\{proportion\}/g,$('[name="proportion1"]').val()+":"+$('[name="proportion2"]').val());

	tmp=tmp.replace(/\{courseNameOrder\}/g,crreateList( $('[data-id="s1"]') ));

	tmp=tmp.replace(/\{courseReplace\}/g,crreateList( $('[data-id="s2"]') ));

	tmp=tmp.replace(/\{jqNameOrder\}/g,crreateList( $('[data-id="s3"]') ));

	tmp=tmp.replace(/\{kmNameOrder\}/g,crreateList( $('[data-id="s5"]') ));

	return tmp;
}
function crreateList($ul){
	var tmp='{name}<br><span class="gray9">({order})</span>';
	var result="";
	var $list=$ul.find("li");

	if($list.length>0){
		$list.each(function(index, el) {
			var $item=$(this).find(".select2-name");
			var t=tmp;
			t=t.replace(/\{name\}/g,$item.text());
			t=t.replace(/\{order\}/g,$item.data("order"));

			result+=t;

			if(index!=$list.length-1){
				result+="<br>";
			}
		});
	}
	else{
		result='无';
	}

	return result;
}

//确认发布
 $('[data-role="sure"]').click(function(event) {	
	 var $li = $(".select-container-ul").children();
	 if ($li.length == 0) {
		alert("请选择课程！");
		return false;
	 }
	 
	 var url = "${ctx}/edumanage/specialty/${action}";
	 $.ajax({
		    type: 'post',
		    url:url,
		 data: $("#inputForm").serialize(),
		 success:function(data){		  
		  parent.$.closeDialog(frameElement.api);
		  var data = JSON.parse(data); 
		  alert(data.message);		
		  window.parent.location.reload();
		 }
	  });
	
	/*  parent.$.closeDialog(frameElement.api);
	 //$('#inputForm').submit();	 
	 window.parent.href.reload(); */
}); 
 
//关闭 弹窗
 $("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api);
});

$("[data-role='choiceExamList']").click(function(event) {
	//event.preventDefault();
	var url = "${ctx}/edumanage/specialty/choiceExamList?examType="+$('#examType').val();
	$("#choiceExamListId").attr("href",url);
	/* $.mydialog({
	  id:'select-course',
	  width:800,
	  height:550,
	  zIndex:11000,
	  content: 'url:'+url
	}); */
});
$("[data-role='choiceExamList']").click();
</script>
</body>
</html>
