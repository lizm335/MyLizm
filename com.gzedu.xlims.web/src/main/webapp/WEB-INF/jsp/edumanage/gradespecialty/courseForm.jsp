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
  		//excluded: [':disabled'],//验证下拉必需加
          fields: {
        	  courseId: {
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
<form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/edumanage/gradespecialty/${action}" method="post">
<input type="hidden" name="gradeId" value="${entity.gradeId}" id="gradeId"/>
<input type="hidden" name="specialtyId" value="${entity.specialtyId}" id="specialtyId"/>
<input type="hidden" name="termTypeCode" value="${entity.termTypeCode}" id="termTypeCode"/>
<input type="hidden" name="id" value="${entity.id}"/>
<input id="action" type="hidden" value="${action }">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">年级专业课程编辑</h3>
	</div>
	<div class="box-body">	
		<div class="slim-Scroll">
			<h3 class="cnt-box-title f16 margin_b10">基础信息设置</h3>
			<div class="table-responsive margin-bottom-none">
				<table class="table-gray-th">
					<tr>
						<th width="15%" class="text-right">课程模块：</th>
						<td width="35%">
							<select class="form-control" name="courseTypeId">
			                  <option value="0" <c:if test="${entity.courseTypeId=='0'}"> selected="selected"</c:if>>公共基础课</option>
			                  <option value="1" <c:if test="${entity.courseTypeId=='1'}"> selected="selected"</c:if>>专业课</option>
			                  <option value="2" <c:if test="${entity.courseTypeId=='2'}"> selected="selected"</c:if>>公共拓展课</option>
			                </select>
						</td>

						<th width="15%" class="text-right"><small class="text-red">*</small>课程名称：</th>
						<td width="35%">
							<div class="pull-right margin_l10">
								<a role="button" class="btn btn-default" <c:if test="${action!='viewPlan' }">href="${ctx}/edumanage/specialty/choiceCourseList?action=course" data-role="select-pop"</c:if> data-target="s1">选择课程</a>
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
							       			<input type="hidden" name="courseId" value="${entity.gjtCourse.courseId}"/>	
							      			</li>
						      			</c:if>
									</ul>			
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th class="text-right">
						课程性质：</th>
						<td>
							<select class="form-control" name="courseCategory">
			                  <option value="0" <c:if test="${entity.courseCategory=='0'}"> selected="selected"</c:if>>必修</option>
			                  <option value="1" <c:if test="${entity.courseCategory=='1'}"> selected="selected"</c:if>>选修</option>
			                  <option value="2" <c:if test="${entity.courseCategory=='2'}"> selected="selected"</c:if>>补修</option>
			                </select>
						</td>

						<th class="text-right">
						替换课程：</th>
						<td>
							<div class="pull-right margin_l10">
								<a role="button" class="btn btn-default" <c:if test="${action!='viewPlan' }">href="choiceCourseList?action=replaceCourse" data-role="select-pop"</c:if> data-target="s2">选择课程</a>
							</div>
							<div class="select2-container select2-container--default show oh">
								<div class="select2-selection--multiple">
									<%-- <ul class="select2-selection__rendered select-container-ul" data-id="s2">
										<c:if test="${not empty entity.gjtCourse }">
											<li class="select2-selection__choice">										
											<c:if test="${action!='viewPlan' }">
											<span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>
											</c:if>
											<span class="select2-name" title="${entity.gjtReplaceCourse.kcmc}" data-toggle="tooltip" 
											data-container="body" data-order="${entity.gjtReplaceCourse.kch}">${entity.gjtReplaceCourse.kcmc}</span>
							       			<input type="hidden" name = "replaceCourseId" value="${entity.gjtReplaceCourse.courseId}"/>	
							      			</li>
						      			</c:if>
									</ul>	 --%>		
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th class="text-right">
						主教材：</th>
						<td>
							<div class="pull-right margin_l10">
								<a href="${ctx}/edumanage/specialty/choiceTextbookList/1" role="button" class="btn btn-default" data-role="select-pop" data-target="s3">选择教材</a>
							</div>
							<div class="select2-container select2-container--default show oh">
								<div class="select2-selection--multiple">
									<ul class="select2-selection__rendered select-container-ul" data-id="s3">
										<c:if test="${not empty entity.gjtTextbookList1 }">
											<c:forEach  items="${entity.gjtTextbookList1}" var="item">
											 <li class="select2-selection__choice">
											 <c:if test="${action!='viewPlan' }">
										      <span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>
										     </c:if>
										      <span class="select2-name" title="${item.textbookName}" data-toggle="tooltip" data-container="body"  data-order="${item.textbookCode}">${item.textbookName}</span>
										      <input type="hidden" name = "textbookIds" value="${item.textbookId}"/>			      
										      </li>
										   </c:forEach>
						      			</c:if>
									</ul>			
								</div>
							</div>
						</td>
						
						<th class="text-right">复习资料：</th>
						<td>
							<div class="pull-right margin_l10">
								<a href="${ctx}/edumanage/specialty/choiceTextbookList/2" role="button" class="btn btn-default" data-role="select-pop" data-target="s4">选择教材</a>
							</div>
							<div class="select2-container select2-container--default show oh">
								<div class="select2-selection--multiple">
									<ul class="select2-selection__rendered select-container-ul" data-id="s4">
										<c:if test="${not empty entity.gjtTextbookList2 }">
											<c:forEach  items="${entity.gjtTextbookList2}" var="item">
											 <li class="select2-selection__choice">
											 <c:if test="${action!='viewPlan' }">
										      <span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>
										      </c:if>
										      <span class="select2-name" title="${item.textbookName}" data-toggle="tooltip" data-container="body"  data-order="${item.textbookCode}">${item.textbookName}</span>
										      <input type="hidden" name = "textbookIds" value="${item.textbookId}"/>			      
										      </li>
										   </c:forEach>
						      			</c:if>
									</ul>			
								</div>
							</div>
						</td>
					</tr>
				</table>
			</div>

			<h3 class="cnt-box-title f16 margin_b10 margin_t20">考试信息</h3>
			<div class="table-responsive margin-bottom-none">
				<table class="table-gray-th">
					<tr>
						<th width="15%" class="text-right">考试方式：</th>
						<td width="35%">
							<select  data-role="choiceExamList" class="form-control" name="examType" id="examType">
			                  <c:forEach items="${ksfsMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==entity.examType}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
			                </select>
						</td>

						<th width="15%" class="text-right">考试科目：</th>
						<td width="35%">
							<div class="pull-right margin_l10">
								<a id="choiceExamListId" href="#" role="button" class="btn btn-default" data-role="select-pop" data-target="s5">选择考试科目</a>
							</div>
							<div class="select2-container select2-container--default show oh">
								<div class="select2-selection--multiple">
									<ul class="select2-selection__rendered select-container-ul" data-id="s5">
										<c:if test="${not empty entity.gjtExamSubjectNew}">	
										<li class="select2-selection__choice">
										<c:if test="${action!='viewPlan' }">										
									      <span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>
									    </c:if>
									      <span class="select2-name" title="${entity.gjtExamSubjectNew.name}" data-toggle="tooltip" data-container="body"  data-order="${entity.gjtExamSubjectNew.subjectCode}">${entity.gjtExamSubjectNew.name}</span>
									      <input type="hidden" name = "subjectId" value="${entity.gjtExamSubjectNew.subjectId}"/>
									      </li>
									    </c:if>
									</ul>						
								</div>
							</div>
							
						</td>
					</tr>
				</table>
			</div>

			<h3 class="cnt-box-title f16 margin_b10 margin_t20">考核信息</h3>
			
			<div class="table-responsive margin-bottom-none">
				<table class="table-gray-th">
					<tr>
						<th width="15%" class="text-right"><small class="text-red">*</small>学时：</th>
						<td width="35%">
							<input class="form-control" type="number" placeholder="学时" name="hours" value="${entity.hours}">
						</td>

						<th width="15%" class="text-right"><small class="text-red">*</small>学分：</th>
						<td width="35%">
							<input class="form-control" type="number" placeholder="学分" name="score" value="${entity.score}">
						</td>
					</tr>
					<tr>
						<th class="text-right">形成性考核比例：</th>
						<td>
							<input id="studyRatio" class="form-control" type="number" placeholder="1-100之间的值" name="studyRatio" value="${entity.studyRatio}">
						</td>

						<th class="text-right">终结性考核比例：</th>
						<td>
							<input id="examRatio" class="form-control" type="number" placeholder="1-100之间的值" name="examRatio" value="${entity.examRatio}">
						</td>
					</tr>
					<tr>
						<th class="text-right">考核说明：</th>
						<td colspan="3">
							<textarea class="form-control" rows="5" placeholder="考核说明" name="instructions">${entity.instructions }</textarea>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">确认</button>
</div>
</form>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
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
	  width:800,
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
	 var url = "${ctx}/edumanage/gradespecialty/${action}";
	 $.ajax({
		    type: 'post',
		    url:url,
		 data: $("#inputForm").serialize(),
		 success:function(data){		  
		  parent.$.closeDialog(frameElement.api);
		  var data = JSON.parse(data); 
		  console.log(data);
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
		var url = "${ctx}/edumanage/specialty/choiceExamList?examType="+$('#examType').val();
		$("#choiceExamListId").attr("href",url);
	});
$("[data-role='choiceExamList']").click();
</script>
</body>
</html>
