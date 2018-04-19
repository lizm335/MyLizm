<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>课程班级管理-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript">
$().ready( function() {
		$('#allot').click(function(){
			 var $checkedIds = $("table td input[name='ids']:enabled:checked");
		 		if ($checkedIds.size() ==0) {
		 			$.alert({
		        	    title: '提示',
		        	    icon: 'fa fa-exclamation-circle',
		        	  	confirmButtonClass: 'btn-primary',
		        	    content: '请选择要分配的班级！',
		        	    confirmButton: '确认'
		        });
		 			return false;
				}else{
					window.location.href="editeAllot.html?"+$checkedIds.serialize();
				}
		});
});
</script>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li class="active">课程班级</li>
	</ol>
</section>
<section class="content">
<form class="form-horizontal" id="listForm" action="list.html">
	<input type="hidden" value="${judge }" name="judge"/><!-- 分页的时候需要保存参数 -->
	<input type="hidden" value="${param.noTeacher }" name="noTeacher"/>
	<div class="box">	
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学期</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_actualGrade.gradeId" class="selectpicker show-tick form-control" 
						data-size="5" data-live-search="true">
						<option value="-1" selected="selected">请选择</option>
						<c:forEach items="${termMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param['search_EQ_actualGrade.gradeId'] || map.key==gradeId}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">课程</label>
	              <div class="col-sm-9">
	                <select name="search_LIKE_gjtCourse.courseId" class="selectpicker show-tick form-control" 
						data-size="5" data-live-search="true">
						<option value="" selected="selected">请选择</option>
						<c:forEach items="${courseMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param['search_LIKE_gjtCourse.courseId']}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">班级名称</label>
	              <div class="col-sm-9">
	                <input name="search_LIKE_bjmc" value="${param['search_LIKE_bjmc']}" type="text" class="form-control">
	              </div>
	            </div>
	          </div>
	        </div>
			<div class="row pad-t15">
	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">督导教师</label>
	              <div class="col-sm-9">
	                <input type="text" name="search_LIKE_gjtDuDao.xm" value="${param['search_LIKE_gjtDuDao.xm']}" class="form-control">
	              </div>
	            </div>
	          </div>

	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">辅导教师</label>
	              <div class="col-sm-9">
	                <input type="text" name="search_LIKE_gjtBzr.xm" value="${param['search_LIKE_gjtBzr.xm']}" class="form-control">
	              </div>
	            </div>
	          </div>

	          <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">状态</label>
	              <div class="col-sm-9">
	                <select name="status" class="form-control" class="selectpicker show-tick form-control" 
						data-size="5" data-live-search="true">
	                  <option value="" selected="selected">请选择</option>
	                  <option value="1" <c:if test="${param['status'] == 1}">selected='selected'</c:if>>待开班</option>
	                  <option value="2" <c:if test="${param['status'] == 2}">selected='selected'</c:if>>开班中</option>
	                  <option value="3" <c:if test="${param['status'] == 3}">selected='selected'</c:if>>已结束</option>
	                </select>
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer text-right">
	    	<button type="submit" class="btn min-width-90px btn-primary margin_r10">搜索</button>
	      	<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
	    </div><!-- /.box-footer-->
	</div>
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">开课列表</h3>
			<div class="pull-right">
				<c:if test="${isBtnCreate }">
					<a href="${ctx}/edumanage/courseclass/create" role="button" class="btn btn-default btn-sm" data-role="create"><i class="fa fa-fw fa-gear"></i> 新增班级</a>
				</c:if>
				<c:if test="${isBtnSettingRule }">
					<a href="${ctx}/edumanage/rulesClass/autoCourseClass" role="button" class="btn btn-default btn-sm" data-role="class-manage"><i class="fa fa-fw fa-gear"></i> 设置自动分班规则</a>
				</c:if>	
			</div>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='status'], :input[name='noTeacher']" <c:if test="${empty param['status'] && empty param['noTeacher']}">class="actived"</c:if>>全部(${all})</li>
					<li value="1" role=":input[name='status']" lang=":input[name='noTeacher']" <c:if test="${param['status'] == 1 }">class="actived"</c:if>>待开班(${status1})</li>
					<li value="2" role=":input[name='status']" lang=":input[name='noTeacher']" <c:if test="${param['status'] == 2 }">class="actived"</c:if>>开班中(${status2})</li>
					<li value="3" role=":input[name='status']" lang=":input[name='noTeacher']" <c:if test="${param['status'] == 3 }">class="actived"</c:if>>已结束(${status3})</li>
					<li value="1" role=":input[name='noTeacher']" lang=":input[name='status']" <c:if test="${param['noTeacher'] == 1 }">class="actived"</c:if>>未设置督导教师(${nodd})</li>
					<li value="2" role=":input[name='noTeacher']" lang=":input[name='status']" <c:if test="${param['noTeacher'] == 2 }">class="actived"</c:if>>未设置辅导教师(${nobzr})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th>学期</th>
		                <th>课程名称</th>
		                <th>班级名称</th>
		                <th>班级人数</th>
		                <!-- <th>班级类型</th> -->
		                <th>督导教师</th>
		                <th>辅导教师</th>
		                <!-- <th>平均学习进度</th>
		                <th>平均学习成绩</th> -->
		                <th>状态</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            <c:choose>
					<c:when test="${not empty pageInfo.content}">
					<c:forEach items="${pageInfo.getContent() }" var="item">
		            	<tr>
		            		<td>
		            			${item.actualGrade.gradeName}
		            		</td>
		            		<td>
		            			${item.gjtCourse.kcmc} <br>
		            			<span class="gray9">（${item.gjtCourse.kch}）</span>
		            		</td>
		            		<td>
		            			${item.bjmc}
		            		</td>
		            		<td>
		            			${item.num}
		            		</td>
		            		<%-- <td>
		            			<c:if test="${item.classType=='course'}">课程班</c:if>
		            			<c:if test="${item.classType=='teach'}">学籍班</c:if>
		            		</td> --%>
		            		<td>
		            			${item.gjtDuDao.xm}
		            		</td>
		            		<td>
		            			${item.gjtBzr.xm}
		            		</td>
		            		<%-- <td>
		            			<c:if test="${not empty item.progressAvg}">${item.progressAvg}%</c:if>
		            		</td>
		            		<td>${item.scoreAvg}</td> --%>
		            		<td>
            					<c:choose>
		            				<c:when test="${item.status == 1}">
		            					<span class="text-orange">待开班</span>
				            			<br>
				            			<small class="gray9">（${item.day}天后开班）</small>
		            				</c:when>
		            				<c:when test="${item.status == 2}">
		            					<span class="text-green">开班中</span>
				            			<br>
				            			<small class="gray9">（${item.day}天后结束）</small>
		            				</c:when>
		            				<c:when test="${item.status == 3}">
		            					<span class="gray9">已结束</span>
		            				</c:when>
		            			</c:choose>
		            		</td>
		            		<td>
		            			<c:if test="${isBtnAllocationDD}">
		            			<a href="teacherChoiceTree/4/${item.classId }" class="operion-item" data-toggle="tooltip" title="配置督导教师" data-role="configure-teacher"><i class="fa fa-rule-set"></i></a>
		            			</c:if>
		            			<c:if test="${isBtnAllocationFD}">
		            			<a href="teacherChoiceTree/2/${item.classId }" class="operion-item" data-toggle="tooltip" title="配置辅导教师" data-role="configure-teacher"><i class="fa fa-article-manage"></i></a>
		            			</c:if>
		            			<c:if test="${isBtnView}">
		            			<a href="classMemberManageList/1/${item.classId }" class="operion-item" data-toggle="tooltip" title="班级人员管理"><i class="fa fa-group-person"></i></a>
		            			</c:if>
		            		</td>
		            	</tr>
	            	</c:forEach> 
					</c:when>
					<c:otherwise>
						<tr>
							<td align="center" colspan="8">暂无数据</td>
						</tr>
					</c:otherwise>
					</c:choose>
		            </tbody>
				</table>
				<tags:pagination page="${pageInfo}" paginationSize="5" /> 
			</div>
		</div>
	</div>
	</form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

//$("[data-id='6_4']", parent.document).parent().addClass("active").siblings('.treeview').removeClass('active');

//filter tabs
/* $(".filter-tabs2 li").click(function(event) {
	var $li = $(this);
	$(".filter-tabs li").each(function(index, el) {
		if (el == $li.context && index == 0) {
			window.location.href = "list";
		} else if (el == $li.context && index == 1) {
			window.location.href = "list?status=1";
		} else if (el == $li.context && index == 2) {
			window.location.href = "list?status=2";
		} else if (el == $li.context && index == 3) {
			window.location.href = "list?status=3";
		} else if (el == $li.context && index == 4) {
			window.location.href = "list?noTeacher=1";
		} else if (el == $li.context && index == 5) {
			window.location.href = "list?noTeacher=2";
		}
	});
}); */

//高级搜索
$("[id^='more-search']").on('shown.bs.collapse', function(event) {
	event.preventDefault();
	var index=$("[id^='more-search']").index(this);
	$('[data-target="#more-search-'+index+'"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
}).on('hidden.bs.collapse', function(event) {
	event.preventDefault();
	var index=$("[id^='more-search']").index(this);
	$('[data-target="#more-search-'+index+'"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
});

//配置老师
$('[data-role="configure-teacher"]').click(function(event) {
	event.preventDefault();
  	$.mydialog({
	  id:'configure-teacher',
	  width:760,
	  height:500,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});


$('[data-role="class-manage"]').click(function(event) {
	event.preventDefault();
  	$.mydialog({
	  id:'class-manage',
	  width:500,
	  height:326,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});


$('[data-role="create"]').click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'create',
	  width:700,
	  height:450,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});
</script>
</body>
</html>