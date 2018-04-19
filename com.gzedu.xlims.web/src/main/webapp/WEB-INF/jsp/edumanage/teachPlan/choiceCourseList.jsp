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
<form id="listForm" class="form-horizontal">
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">选择课程</h3>
	</div>
	<div class="box-body">
		<div class="table-responsive">
			<div class="slim-Scroll" style="height:500px;overflow:hidden;">
				<table class="batch-teacher table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
					<thead>
						<tr>
							<th>选择</th>
							<th>建议学期</th>
							<th>课程模块</th>
							<th>课程代码</th>
							<th>课程名称</th>
							<th>课程性质</th>
							<th>课程类型</th>
							<th>考试单位</th>
							<th>学分</th>
							<th>学时</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${not empty list}">
							<c:forEach items="${list}" var="item">
								<c:if test="${not empty item}">
									<c:set var="course" value="${item.gjtCourse}" />
									<tr courseid="${course.courseId}">
										<td>
											<i class="fa fa-circle-o"></i>
											<input type="hidden" class="courseTypeId" value="${item.courseTypeId}"  />
											<input type="hidden" class="ksdw" value="${item.examUnit}"  />
											<input type="hidden" class="kcsx" value="${item.courseCategory}"  />
										</td>
										<td class="term">
											第${item.termTypeCode}学期
										</td>
										
										<td class="kclbm">${courseTypeMap[item.courseTypeId]}</td>
					            		<td class="kch">
											${course.kch}
										</td>
										<td class="kcmc">
											<span>${course.kcmc}</span><br />
											<c:if test="${course.isEnabled==1}">
												<span class="text-green">（已启用）</span>
											</c:if>
											<c:if test="${course.isEnabled==2}">
												<span class="text-orange">（建设中）</span>
											</c:if>
											<c:if test="${course.isEnabled==0}">
												<span class="text-red">（暂无资源）</span>
											</c:if>
										</td>
										<td class="kcsxText">	
											<c:if test="${item.courseCategory=='0'}">必修</c:if>
											<c:if test="${item.courseCategory=='1'}">选修</c:if>
											<c:if test="${item.courseCategory=='2'}">补修</c:if>
										</td>
										<td class="courseType">
											<c:if test="${item.coursetype=='0'}">统设</c:if>
											<c:if test="${item.coursetype=='1'}">非统设</c:if>
										</td>
										<td class="ksdwText">
											<c:if test="${item.examUnit=='1'}">省</c:if>
											<c:if test="${item.examUnit=='2'}">中央</c:if>
											<c:if test="${item.examUnit=='3'}">分校</c:if>
										</td>
										<td class="xf"><span class="credit">${item.score}</span></td>
										<td class="xs">${item.hours}</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td align="center" colspan="9">
									  没有搜索到相关课程！
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>			
		</div>
	</div>
</div>
</form>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>

<script type="text/javascript">

$(function(){

	$('.slim-Scroll').slimScroll({
	    height: 600,
	    size: '5px'
	});

	
	
	//确定选择
	$("[data-role='save-data']").click(function(event) {
	  event.preventDefault();
	  if(self!==parent){
	      if(parent.choiceCourseCallback){
			  var courses = [];
			  $('.batch-teacher tr.on').each(function(){
				  courses.push({
				      courseId:$(this).attr('courseid'),
				      kclbm:$.trim($(this).find('.kclbm').text()),
				      courseTypeId:$(this).find('.courseTypeId').val(),
				      kch:$.trim($(this).find('.kch').text()),
				      kcmc:$(this).find('.kcmc').html(),
				      kcsxText:$.trim($(this).find('.kcsxText').text()),
				      kcsx:$(this).find('.kcsx').val(),
					  courseType:$.trim($(this).find('.courseType').text()),
					  ksdw:$(this).find('.ksdw').val(),
					  ksdwText:$.trim($(this).find('.ksdwText').text()),
					  xf:$.trim($(this).find('.xf').text()),
					  xs:$.trim($(this).find('.xs').text())
				  });
			  });
			  parent.choiceCourseCallback(courses);
	      }
		}
	});

	// 选中
	$(".batch-teacher").on('click', 'tr', function(event) {
		event.preventDefault();
		if($(this).hasClass('on')){
			$(this).removeClass('on');
		}else{
			$(this).addClass('on');
		}
	});

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

})
</script>
</body>
</html>
