<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>管理系统</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>

<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">选择课程</h3>
	</div>
	<div class="box-body">
	<form id="listForm" action="${ctx}/edumanage/coachdata/chooseCourse" >
		<input type="hidden" name="gradeIds"  value="${param.gradeIds}"/>
		<div class="margin_b10 clearfix">
			<div class="form-inline text-right">
				<div class="form-group">
					<label class="text-no-bold">课程名称：</label>
					<input class="form-control" type="text" name="search_LIKE_course" value="${param.search_LIKE_course}" placeholder="请输入课程名称">
					<button class="btn btn-default margin_l5">搜索</button>
				</div>
			</div>
		</div>
		
		<div class="table-responsive">
			<div class="slim-Scroll" style="height:190px;overflow:hidden;">
				<table class="batch-teacher table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
					<thead>
						<tr>
							<th width="60">
								<i class="fa fa-square-o" data-role="sel-all"></i>
							</th>
							<th>学期</th>
							<th>课程名称</th>
							<th>选课人数</th>
							<th>需辅导人数</th>
							<th>任课老师</th>
							<th>状态</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${pageInfo.getContent() }" var="item">
		            	<tr>
		            		<td>
								<i class="fa fa-square-o" data-json='{
									"term":"${item.GRADE_NAME}",
									"courseName":"${item.KCMC}",
									"courseCode":"${item.KCH}",
									"selectCount":"${item.CHOOSE_COUNT}",
									"needTrainCount":"${item.CHOOSE_COUNT}",
									"termCourseId":"${item.TERMCOURSE_ID }"
								}'></i>
							</td>
		            		<td>
		            			${item.GRADE_NAME} 
		            		</td>
		            		<td>
		            			${item.KCMC} <br>
		            			<small class="gray9">（${item.KCH}）</small>
		            		</td>
		            		<td>
		            			${item.CHOOSE_COUNT}
		            		</td>
		            		<td>
		            			${item.CHOOSE_COUNT}
		            		</td>
		            		<td>
		            			<c:if test="${not empty(item.TEACHER_NAME)}">
		            				${item.TEACHER_NAME}
		            			</c:if>
		            			<c:if test="${empty(item.TEACHER_NAME)}">
		            				<span class="text-orange">未分配</span>
		            			</c:if>
		            		</td>
		            		<td>
		            			<c:choose>
		            				<c:when test="${item.STATUS eq 0 }">
		            					<span class="text-orange">未开课</span>
		            				</c:when>
		            				<c:when test="${item.STATUS eq 1 }">
		            					<span class="text-green">开课中</span><br />
		            					<small class="gray9">（${item.END_DAYS }天后结束）</small>
		            					
		            				</c:when>
		            				<c:when test="${item.STATUS eq 2 }">
		            					<span class="gray9">已结束</span>
		            				</c:when>
		            			</c:choose>
		            		</td>
		            	</tr>
		            </c:forEach>
					</tbody>
				</table>
			</div>
			<tags:pagination page="${pageInfo}" paginationSize="5" /> 
		</div>
		</form>
	</div>
</div>
<div class="text-right pop-btn-box pad clearfix">
	<div class="pull-left pad-t5">已选择 <b class="text-red" data-id="count">0</b> 门课程</div>
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>
<script type="text/template" id="temp">
	<tr>
		<td>{0}</td>
		<td>
			<div>{1}</div>
			<small class="gray9">（{2}）</small>
		</td>
		<td>{3}</td>
		<td>{4}</td>
		<td>
			<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove"><i class="fa fa-trash-o text-red"></i></a>
			<input type="hidden" class="termCourseId" name="termCourseIds" value="{5}" />
		</td>
	</tr>
</script>

<script type="text/javascript">

$(function(){
	$('.slim-Scroll').slimScroll({
	    height: $(frameElement).height()-106-44-80,
	    size: '5px'
	});

	//确定选择
	$("[data-role='save-data']").click(function(event) {
	  	event.preventDefault();

		var $container=parent.$('[data-id="course-box"]');

		var html=$('#temp').html();
		var arr=[];

		$(".batch-teacher tbody tr.on").each(function(index, el) {
			var tmp=html;
			var $i=$(this).find('.fa-square-o');
			var json=$i.data('json');
			tmp=tmp.format(
				json.term,
				json.courseName,
				json.courseCode,
				json.selectCount,
				json.needTrainCount,
				json.termCourseId
			);
			arr.push(tmp);
		});

		$container.append(arr.join(''));
	 	parent.$.closeDialog(frameElement.api);
	});

	// 选中
	$(".batch-teacher").on('click', 'tr', function(event) {
		event.preventDefault();
		if($(this).hasClass('on')){
			$(this).removeClass('on');

			if( $('[data-role="sel-all"]',this).length>0 ){
				$(".batch-teacher tbody tr").removeClass('on')
			}
		}
		else{
			$(this).addClass('on');

			if( $('[data-role="sel-all"]',this).length>0 ){
				$(".batch-teacher tbody tr").addClass('on')
			}
		}

		$('[data-id="count"]').text( $(".batch-teacher tbody tr.on").length );

	});

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

})
</script>

</body>
</html>
