<%@page import="com.gzedu.xlims.pojo.status.EmployeeTypeEnum"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
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
<button class="btn btn-default btn-sm pull-right min-width-60px"
			data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 教学管理</a></li>
        <li><a href="#">专业规则</a></li>
        <li class="active">专业详情</li>
    </ol>
</section>
<section class="content">
    <div class="nav-tabs-custom no-margin">
        <div class="tab-content">
            <div class="tab-pane active" id="tab_top_1">
                    <div class="box box-border margin-bottom-none">
                        <div class="box-header with-border">
                           <h3 class="box-title pad-t5">${specialty.name }（${specialty.ruleCode }）</h3>
                        </div>
	                    <div class="box-body">
	                        <div class="table-responsive">
	                            <table class="table table-bordered table-striped vertical-mid text-center table-font">
	                                <thead>
						      			<tr>
						      				<th>建议学期</th>
						      				<th>课程模块</th>
						      				<th>课程代码</th>
						      				<th>课程名称</th>
						      				<th>课程性质</th>
						      				<th>课程类型</th>
						      				<th>考试单位</th>
						      				<th>学时</th>
						      				<th>试卷号</th>
						      			</tr>
						      		</thead>
	                                <tbody>
	                                <c:forEach items="${list}" varStatus="i" var="item">
						      			<tr> 		      				
						      				<c:if test="${i.index==0 || list[i.index-1].termTypeCode!=item.termTypeCode}">
												<td rowspan="${countMap[item.termTypeCode]}">第${item.termTypeCode}学期</td>
											</c:if>
						      				<td>
												${courseTypeMap[item.courseTypeId]}${item.courseTypeId }
											</td>
											<td>${item.gjtCourse.kch }</td>
						      				<td>
												${item.gjtCourse.kcmc }
						      				</td>
						      				<td>
								                <c:choose>
								                	<c:when test="${item.courseCategory=='0'}">必修</c:when>
									                <c:when test="${item.courseCategory=='1'}">选修</c:when>
									                <c:when test="${item.courseCategory=='2'}">补修</c:when>
								                </c:choose>
						      				</td>
						      				<td>
								                <c:choose>
								                	<c:when test="${item.coursetype=='0'}">统设</c:when>
								                	<c:otherwise>非统设</c:otherwise>
								                </c:choose>
						      				</td>
						      				<td>
								                <c:choose>
								                	<c:when test="${item.examUnit=='1'}">省</c:when>
								                	<c:otherwise>中央</c:otherwise>
								                </c:choose>
						      				</td>
						      				<td>
						      					<fmt:formatNumber value="${item.hours}" pattern="#" type="number"/>
						      				</td>
						      				<td>
						      					${item.examPaperNum}
						      				</td>
						      			</tr>	
						      		</c:forEach>	
	                                </tbody>
	                            </table>
	                        </div>
	                    </div>
                    </div>
            </div>
        </div>
    </div>
</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">


</script>
</body>
</html>

