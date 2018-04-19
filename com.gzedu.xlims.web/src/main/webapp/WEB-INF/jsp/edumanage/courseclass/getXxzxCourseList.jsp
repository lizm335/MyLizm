<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>设置教学辅导机构</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
	<div class="box no-border">
		<div class="box-header with-border">
			<h3 class="box-title">添加辅导课程</h3>
		</div>
		<div class="box-body cn-container">
			
			<table class="table table-bordered table-striped margin-bottom-none text-center f12">
			  <thead>
			    <tr>
			      <th width="6%"><input type="checkbox" data-role="select-all"></th>
			      <th>开课学期</th>
			      <th>教学周期</th>
			      <th>课程代码</th>
			      <th>课程名称</th>
			      <th>选课人数</th>
			    </tr>
			  </thead>
			  <tbody>
			  	<c:forEach items="${info }" var="course">
			  		<c:if test="${course.TERM_ORG_COUNT <=0 }">
					  	<tr>
					      <td>
					      	<c:if test="${course.TIME_FLG eq '1' }">
					      	<input type="checkbox" class="chk-item" data-id='${course.TERMCOURSE_ID }' data-json='{
					      	"term":"${course.GRADE_NAME }",
					      	"courseCode":"${course.KCH }",
					      	"courseName":"${course.KCMC }",
					      	"pcount":"${course.REC_COUNT }"
					      	}'>
					      	</c:if>
					      </td>
					      <td>${course.GRADE_NAME }</td>
					      <td>${course.COURSE_START_DATE }~${course.COURSE_END_DATE }</td>
					      <td>${course.KCH }</td>
					      <td>${course.KCMC }</td>
					      <td>${course.REC_COUNT }</td>
					    </tr>
				    </c:if>
			  	</c:forEach>
			  </tbody>
			</table>
		</div>
	</div>
	
	<div class="text-right pop-btn-box pad">
		<span class="pull-left pad-t5">
			已选择 <b class="text-light-blue select-total">0</b> 门课程
		</span>
		<input type="hidden" id="xxzx_id" value="${XXZX_ID }" />
		<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
		<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确认选择</button>
	</div>
	<!--html模板-->
	<script type="text/template" id="temp">
	<tr>
		<td>{0}</td>
		<td>{1}</td>
		<td>{2}</td>
		<td>{3}</td>
		<td>
			<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove"><i class="fa fa-trash-o text-red"></i></a>
		</td>
	</tr>
	</script>
	<script type="text/javascript">
		//确定选择
		$("[data-role='save-data']").click(function(event) {
			/*var $container=parent.$('[data-id="select-container"]');
			var temp=$('#temp').html(), arr=[];
			$(".chk-item:checked").each(function(index, el) {
				var json=$(this).data('json');
				var html=temp;
				html=html.format(
					json.term,
					json.courseCode,
					json.courseName,
					json.pcount
				);
				arr.push(html);
			});
			if(arr.length>0){
				$container.append(arr.join(''));
			}*/
	
			addTermcourseOrg();
		});
	
		//全部选择
		$("[data-role='select-all']").change(function(event) {
		  var $ckCollector=$(".chk-item");
		  var $txt=$(".select-total");
		  if($(this).prop('checked')){
		    $ckCollector.prop('checked', true);
		    $txt.text($ckCollector.length);
		  }
		  else{
		    $ckCollector.prop('checked', false);
		    $txt.text(0);
		  }
		});
	
		$(".chk-item").click(function(event) {
		  var $txt=$(".select-total");
		  if($(this).prop('checked')){
		    $txt.text(parseInt($txt.text())+1);
		  }
		  else{
		    $txt.text(parseInt($txt.text())-1);
		  }
		});
	
		//关闭 弹窗
		$("button[data-role='close-pop']").click(function(event) {
			parent.$.closeDialog(frameElement.api);
		});
	
		$(".cn-container").slimScroll({
			height:$(frameElement).height()-106
		});
		
		function addTermcourseOrg() {
			var xxzx_id = $("#xxzx_id").val();
			var arr=[];
			$(".chk-item:checked").each(function(index, el) {
				var termcourse_id=$(this).attr('data-id');
				arr.push(termcourse_id);
			});
			$.ajax({
		      type: "POST",
		      dataType: "json",
		      url: 'addTermcourseOrg',
		      data: 'XXZX_ID='+xxzx_id+"&TERMCOURSE_ID="+arr,
		      success: function (result) {
		    	  if (result!=null && result!="") {
		    		  parent.getXxzxCourse(xxzx_id);
		    		  parent.$.closeDialog(frameElement.api);
		    	  }
		      },
		      error: function(data) {}
		    });
		}
	</script>
</body>
</html>