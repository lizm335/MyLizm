<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li><a href="#">排考安排</a></li>
		<li class="active">明细</li>
	</ol>
</section>
<section class="content">
	<div class="box">
      <div class="box-header with-border">
        <h3 class="cnt-box-title text-bold">排考明细</h3>
      </div>
      <div class="box-body">
      	<div class="table-responsive">
	      	<table class="table-gray-th">	      	
	      		<tr>
	      			<th width="20%" class="text-right">姓名：</th>
	      			<td width="30%">${entity.student.xm}</td>
	      			
	      			<th width="20%" class="text-right">学号：</th>
	      			<td width="30%">${entity.student.xh}</td>
	      		</tr>
	      		<tr>
	      			<th class="text-right">考试计划：</th>
	      			<td>
	      				${entity.examPlanNew.examBatchNew.name}<br>
						<span class="gray9">${entity.examPlanNew.examBatchNew.examBatchCode}</span>
	      			</td>
	      			
	      			<th class="text-right">考试科目：</th>
	      			<td>
	      				<c:forEach items="${entity.examPlanNew.gjtCourseList}" var="c">
							${c.kcmc}<span class="gray9">（${c.kch}）</span><br>
						</c:forEach>
	      			</td>
	      		</tr>
	      		<tr>
	      			<th class="text-right">考试类型：</th>
	      			<td>${examTypeMap[entity.examPlanNew.type]}</td>
	      			
	      			<th class="text-right">试卷号：</th>
	      			<td>
	      				${entity.examPlanNew.examNo}
	      			</td>
	      		</tr>
	      		<tr>
	      			<th class="text-right">考试日期：</th>
	      			<td>
	      				<fmt:formatDate value="${entity.examPlanNew.examSt}" type="date" pattern="yyyy-MM-dd"/><br>
	      			</td>
	      			
	      			<th class="text-right">考试时间：</th>
	      			<td>
	      				<fmt:formatDate value="${entity.examPlanNew.examSt}" type="date" pattern="HH:mm"/>
	      				-
	      				<fmt:formatDate value="${entity.examPlanNew.examEnd}" type="date" pattern="HH:mm"/>
	      			</td>
	      		</tr>
	      		<tr>
	      			<th class="text-right">考点：</th>
	      			<td>${entity.examPointName}</td>
	      			
	      			<th class="text-right">考场：</th>
	      			<td>
	      				${entity.gjtExamStudentRoomNew.gjtExamRoomNew.name}
	      			</td>
	      		</tr>
	      		<tr>
	      			<th class="text-right">座位号：</th>
	      			<td>${entity.gjtExamStudentRoomNew.seatNo}</td>
	      			
	      			<th class="text-right">考试地址：</th>
	      			<td>
	      				${entity.address}
	      			</td>
	      		</tr>
	      	</table>
      	</div>
      </div>
    </div>
    
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
</html>