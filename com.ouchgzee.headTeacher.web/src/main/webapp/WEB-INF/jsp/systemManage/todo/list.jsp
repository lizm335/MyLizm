<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>待办事项</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">代办事项</li>
	</ol>
</section>
<section class="content">
	<div class="box no-margin">
		<div class="box-body pad20">
			<ul class="list-unstyled agency-list agency-list-width-pad10">
				<c:if test="${queryStudyYear<1 }">
				<li>
					<small class="label pull-left bg-red">学年度</small>
					<span>请为院校建立学年度</span>
					<a class="agency-link" href="${ctx }/edumanage/studyyear/list"><small>进入处理</small></a>
				</li>
				</c:if>
				
				<c:if test="${queryHeadTeach<1 }">
				<li>
					<small class="label pull-left bg-red">班主任</small>
					<span>请为院校添加班主任</span>
					<a class="agency-link" href="${ctx }/usermanage/headteacher/list"><small>进入处理</small></a>
				</li>
				</c:if>
				<c:if test="${queryCourseTeach<1 }">
				<li>
					<small class="label pull-left bg-red">辅导教师</small>
					<span>请为院校添加辅导教师</span>
					<a class="agency-link" href="${ctx }/usermanage/counselor/list"><small>进入处理</small></a>
				</li>
				</c:if>
				<c:if test="${queryDuDaoTeach<1 }">
				<li>
					<small class="label pull-left bg-red">督导教师</small>
					<span>请为院校添加督导教师</span>
					<a class="agency-link" href="${ctx }/usermanage/inspector/list"><small>进入处理</small></a>
				</li>
				</c:if>
				<c:if test="${queryStudyYearRenWu<1 }">
				<li>
					<small class="label pull-left bg-red">学年度任务计划</small>
					<span>请为学年度添加任务计划</span>
					<a class="agency-link" href="${ctx }/edumanage/studyyear/list"><small>进入处理</small></a>
				</li>
				</c:if>
				<c:if test="${queryGrade<1}">
				<li>
					<small class="label pull-left bg-red">年级</small>
					<span>请为院校添加年级</span>
					<a class="agency-link" href="${ctx }/edumanage/grade/list"><small>进入处理</small></a>
				</li>
				</c:if>
				
				<c:if test="${queryTeachClass>0 }">
				<li>
					<small class="label pull-left bg-red">教学班级</small>
					<span>请为教学班级分配班主任！</span>
					<a class="agency-link" href="${ctx }/edumanage/teachclass/list.html?judge=1"><small>进入处理</small></a>
				</li>
				</c:if>
				<c:if test="${queryCourseClass>0 }">
				<li>
					<small class="label pull-left bg-red">课程班级</small>
					<span>请为课程班级分配辅导教师！</span>
					<a class="agency-link" href="${ctx }/edumanage/courseclass/list.html?judge=1"><small>进入处理</small></a>
				</li>
				</c:if>
			</ul>
		</div>
	</div>
</section>
</body>
</html>
