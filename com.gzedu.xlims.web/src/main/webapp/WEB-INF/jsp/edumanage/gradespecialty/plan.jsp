<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业管理－课程列表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
</script>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">专业管理</a></li>
		<li class="active">设置基础教学计划</li>
	</ol>
</section>
<section class="content">
<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<div class="box">
      <div class="box-header with-border">
        <h3 class="box-title text-bold">专业信息</h3>
      </div>
      <div class="box-body">
      	<div class="table-responsive">
      		<input name="specialtyId" value="${gjtSpecialty.specialtyId }" type="hidden"/>
	      	<table class="table-gray-th">	      	
	      		<tr>
	      			<th width="15%">年级名字</th>
	      			<td width="35%">${gjtGrade.gradeName }</td>

	      			<th width="15%"></th>
	      			<td width="35%"></td>
	      		</tr>
	      		<tr>
	      			<th width="15%">专业编码</th>
	      			<td width="35%">${gjtSpecialty.zyh }</td>

	      			<th width="15%">专业名字</th>
	      			<td width="35%">${gjtSpecialty.zymc}</td>
	      		</tr>
	      		<tr>
	      			<th>专业性质</th>
	      			<td>
	      			<c:if test="${gjtSpecialty.specialtyCategory==1}">开放教育专业</c:if>
	      			<c:if test="${gjtSpecialty.specialtyCategory==2}">助力计划专业</c:if>
	      			</td>

	      			<th>培养层次</th>
	      			<td>
	      			 ${pyccMap[gjtSpecialty.pycc]} 
	      			</td>
	      		</tr>
	      		<tr>
	      			<th>所属院校</th>
	      			<td>${gjtSpecialty.gjtSchoolInfo.xxmc}</td>

	      			<th>总学分</th>
	      			<td>${gjtSpecialty.zxf}</td>
	      		</tr>
	      		<tr>
	      			<th>最低毕业学分</th>
	      			<td>${gjtSpecialty.zymc}</td>

	      			<th>必修学分</th>
	      			<td>${gjtSpecialty.bxxf}</td>
	      		</tr>
	      		<tr>
	      			<th>选修学分</th>
	      			<td>${gjtSpecialty.xxxf}</td>

	      			<th>所属行业</th>
	      			<td>${gjtSpecialty.syhy}</td>
	      		</tr>
	      	</table>
      	</div>
      </div>
    </div>
    
     <div class="box">
      <div class="box-header with-border">
        <h3 class="box-title text-bold">教学计划信息</h3>
        <div class="pull-right no-margin">
			<a href="#" role="button" class="btn btn-default btn-sm margin_r10" data-role="set"><i class="fa fa-fw fa-sign-in"></i> 批量导入教学计划</a>

			<a href="#" role="button" class="btn btn-default btn-sm" data-role="add-semester"><i class="fa fa-fw fa-plus"></i> 新增学期</a>
		</div>
      </div>
      
      <div class="box-body" data-role="box">
      	<div class="table-responsive tea-plan-box">
	      	<table class="plan-table tea-plan-tbl table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none" data-semester="第一学期">
	      		<thead>
	      			<tr>
	      				<th rowspan="2" width="50">序号</th>
	      				<th rowspan="2">学期</th>
	      				<th colspan="5">课程设置</th>
	      				<th colspan="2">考试设置</th>
	      				<th rowspan="2">学分</th>
	      				<th rowspan="2">学时</th>
	      				<th rowspan="2">形考比例</th>
	      				<th rowspan="2" width="80">操作</th>
	      			</tr>
	      			<tr>
	      				<th>课程模块</th>
	      				<th>课程名称</th>
	      				<th>是否必修</th>
	      				<th>替换课程</th>
	      				<th>主教材</th>
	      				<th>考试方式</th>
	      				<th>考试科目</th>
	      			</tr>
	      		</thead>
	      		<tbody>
	      		
	      		<c:forEach items="${gradeSpecialtyPlans}" varStatus="i" var="item">
	      			<tr>
 	      				<c:forEach items="${countMap}" var="countMapItem">
	      					<c:if test="${(item.termTypeCode!=gradeSpecialtyPlans[i.index-1].termTypeCode) && (item.termTypeCode==countMapItem.key)}">	      					
	      						<td class="order" rowspan="${countMapItem.value}" ></td>
			      				<td class="semester" rowspan="${countMapItem.value}">
			      					第${item.termTypeCode }学期
			      				</td>
	      					</c:if>
	      				</c:forEach>
	      				<td>公共基础课</td>
	      				<td>
	      					${item.gjtCourse.kcmc}
		      				<br>
	      					<span class="gray9">
	      						（${item.gjtCourse.kch }）
	      					</span>
	      				</td>
	      				<td>
	      				<c:if test="${item.courseCategory==0}">必修</c:if>
	      				<c:if test="${item.courseCategory==1}">选修</c:if>
	      				</td>
	      				<td>无</td>
	      				<td>无</td>
	      				<td><c:if test="${item.examType==0}">网考</c:if>
	      				<c:if test="${item.examType==1}">场考</c:if></td>
	      				<td>--</td>
	      				<td>${item.score }</td>
	      				<td>${item.hours }</td>
	      				<td>${item.studyRatio }:${item.examRatio }</td>
	      				<td>
	      					<a href="${ctx}/edumanage/gradespecialty/courseFormUpdate?id=${item.id}" class="operion-item" data-toggle="tooltip" title="编辑" data-role="add-course"><i class="fa fa-edit"></i></a>
	      					<a href="${ctx}/edumanage/gradespecialty/courseFormView?id=${item.id}" data-role="add-course" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
	      				</td>	      				
	      			</tr>	
	      			
      				<c:if test="${ !i.last && item.termTypeCode!=gradeSpecialtyPlans[i.index+1].termTypeCode || i.last}">
      				 	<tr>
			      			<td colspan="13" align="center">
			      			<a href="${ctx}/edumanage/gradespecialty/courseForm?gradeId=${item.gradeId}&termTypeCode=${item.termTypeCode }&specialtyId=${item.specialtyId }" role="button" class="btn btn-block text-green nobg margin_t5" data-role="add-course"><i class="fa fa-fw fa-plus"></i>添加课程</a>
			      			</td>
		      			 </tr> 		      				
      				</c:if>	
      				
	      		</c:forEach>   		
	      		</tbody>   	      		
	      	</table>	
	      	<input type="hidden" id = "count" value ="${countMap.size()}"/>	      	
      	</div>
      </div>
    </div> 
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
/* 
//新增学期
$('[data-role="add-semester"]').click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'add-semester',
	  width:460,
	  height:300,
	  zIndex:11000,
	  content: 'url:/edumanage/specialty/'+$(this).attr('href')
	});
});*/
var count = $("#count").val();
$('[data-role="add-semester"]').click(function(event) {	
	count++;
	var html = "<tbody>"+
		"<tr>"+
	"<td class=\"order\"></td>"+
	"<td class=\"semester\" rowspan=\"3\">"+
		
	"</td>"+
	"<td></td>"+
	"<td>"+
		
	"</td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td>"+
	"</td>"+
	"</tr>"+	
	"</tbody>"+
	"<tr>"+
	"<td colspan=\"13\" align=\"center\">"+
	"<a href=\"${ctx}/edumanage/gradespecialty/courseForm?gradeId=${gjtGrade.gradeId}&specialtyId=${gjtSpecialty.specialtyId }&termTypeCode="+count+"\" role=\"button\" class=\"btn btn-block text-green nobg margin_t5\" data-role=\"add-course\"><i class=\"fa fa-fw fa-plus\"></i>添加课程</a>"+
	"</td>"+
	"</tr>";
	
	$(".plan-table").append(html);
});
//新增课程
$("body").on('click', '[data-role="add-course"]', function(event) {
	event.preventDefault();
	$(".tea-plan-box").removeClass('on');
	$(this).parent().addClass('on');
	$.mydialog({
	  id:'add-course',
	  width:'90%',
	  height:580,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});

</script>
</body>
</html>