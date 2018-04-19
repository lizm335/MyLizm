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
	<div class="box no-border no-shadow margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title">设置教学辅导机构</h3>
		</div>
		<div class="slim-Scroll">
			<div class="box-body">
				<div class="box-border pad">
					<div class="form-inline text-center">
						<div class="form-group">
							<label class="control-label text-no-bold">教学辅导机构：</label>
							<select class="form-control" id="xxzx_id" style="width:300px;" onchange="getChooseCourse(this)">
								<option value="">--请选择--</option>
								<c:forEach items="${info }" var="xxzxInfo">
									<option value="${xxzxInfo.XXZX_ID }">${xxzxInfo.SC_NAME }</option>
								</c:forEach>
							</select>
						</div>
					</div>
			    </div>
			</div>
			<div class="box-body">
				<div class="margin_b10 clearfix">
					<button type="button" class="btn btn-default btn-sm pull-right" data-role="add-course"><i class="fa fa-fw fa-plus"></i> 添加辅导课程</button>
	
					<h3 class="cnt-box-title f16">设置需辅导的课程</h3>
				</div>
				
				<div class="table-responsive">
					<table class="batch-teacher table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
						<thead>
							<tr>
								<th>开课学期</th>
								<th>教学周期</th>
								<th>课程代码</th>
								<th>课程名称</th>
								<th>选课人数</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody data-id="select-container" id="select_container">
							
						</tbody>
					</table>
				</div>
				<div class="alert text-orange margin_t10 pad margin-bottom-none f12" style="background:#FFFFCC;border:#FFCC33 1px solid;">
					<div class="clearfix">
						<span class="pull-left">注：</span>
						<div class="oh">
							<ol class="margin-bottom-none pad-l20">
								<li>教学辅导机构只能设置你本身所在的机构或下级拥有教学辅导权限的机构</li>
								<li>更改教学辅导机构确认设置后，学生将从原有班级中撤出，重新分配到新的教学辅导机构的课程班级中</li>
								<li>调整教学辅导机构不会影响学生学习记录，但会影响原课程班中辅导老师对其的辅导记录，请慎重操作！</li>
							</ol>
						</div>
					</div>
		        </div>
			</div>
		</div>
	</div>
	<div class="text-right pop-btn-box pad">
		<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
		<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确认设置</button>
	</div>
	<script type="text/javascript">
		$('.slim-Scroll').slimScroll({
		    height:$(frameElement).height()-106,
		    size: '5px'
		});
	
		//确定选择
		$("[data-role='save-data']").click(function(event) {
		  parent.$.closeDialog(frameElement.api);
		});
	
		//关闭 弹窗
		$("button[data-role='close-pop']").click(function(event) {
			parent.$.closeDialog(frameElement.api);
		});
	
		//添加辅导课程
		$('[data-role="add-course"]').click(function(event) {
			var xxzx_id = $("#xxzx_id").val();
			if(xxzx_id!=null && xxzx_id!="") {
				$.mydialog({
				  id:'add-course',
				  width:690,
				  height:410,
				  zIndex:1000,
				  content: 'url:${ctx}/edumanage/courseclass/getXxzxCourseList?XXZX_ID='+xxzx_id
				});
			} else {
				alert("请先选择教学辅导机构！");
			}
		});
		
		function getChooseCourse(obj) {
			var xxzx_id = $(obj).val();
			if(xxzx_id!=null && xxzx_id!="") {
				getXxzxCourse(xxzx_id);
			} else {
				$("#select_container").html("");
			}
		}
		
		function getXxzxCourse(xxzx_id) {
			$.ajax({
		      type: "POST",
		      dataType: "json",
		      url: 'getXxzxChooseList',
		      data: 'XXZX_ID='+xxzx_id,
		      success: function (result) {
		    	  if (result!=null && result!="") {
		    		  var tempHtml = "";
		    		  for (var i=0; i<result.length; i++) {
		    			  var course = result[i];
		    			  var term_org_count = course.TERM_ORG_COUNT;
		    			  if (term_org_count>0) {
		    				  tempHtml += "<tr id=\""+course.TERMCOURSE_ID+"\">";
			    			  tempHtml += "<td>"+course.GRADE_NAME+"</td>";
			    			  tempHtml += "<td>"+course.COURSE_START_DATE+"~"+course.COURSE_END_DATE+"</td>";
			    			  tempHtml += "<td>"+course.KCH+"</td>";
			    			  tempHtml += "<td>"+course.KCMC+"</td>";
			    			  tempHtml += "<td>"+course.REC_COUNT+"</td>";
			    			  tempHtml += "<td>";
			    			  tempHtml += "<a href=\"#\" class=\"operion-item operion-view\" data-toggle=\"tooltip\" title=\"删除\" data-role=\"remove\" onclick=\"delTermcourseOrg('"+xxzx_id+"','"+course.TERMCOURSE_ID+"')\"><i class=\"fa fa-trash-o text-red\"></i></a>";
			    			  tempHtml += "</td>";
			    			  tempHtml += "</tr>";
		    			  }
		    		  }
		    		  $("#select_container").html(tempHtml);
		    	  } else {
		    		  $("#select_container").html("");
		    	  }
		      },
		      error: function(data) {}
		    });
		}
		
		function delTermcourseOrg(xxzx_id, termcourse_id) {
			$.ajax({
		      type: "POST",
		      dataType: "json",
		      url: 'delTermcourseOrg',
		      data: 'XXZX_ID='+xxzx_id+"&TERMCOURSE_ID="+termcourse_id,
		      success: function (result) {
		    	  if (result!=null && result!="") {
		    		 $("#"+termcourse_id).remove();
		    	  }
		      },
		      error: function(data) {}
		    });
		}
	</script>
</body>
</html>