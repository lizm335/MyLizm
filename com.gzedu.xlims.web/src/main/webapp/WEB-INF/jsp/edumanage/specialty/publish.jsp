<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业规则</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">专业规则</a></li>
		<li class="active">专业详情</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom">
		<ul class="nav nav-tabs nav-tabs-lg">
	      <li data-role="show"><a href="javascript:void(0)">1.设置专业基础信息</a></li>
	      <li data-role="show"><a href="javascript:void(0)">2.设置专业规则</a></li>
	      <li data-role="show" class="active"><a href="javascript:void(0)">3.发布专业</a></li>
	    </ul>
	    <div class="tab-content no-padding">
	    	<div class="tab-pane active" id="tab_top_2">
				<div class="box">
			      <div class="box-header with-border">
			        <h3 class="cnt-box-title text-bold">专业信息</h3>
			      </div>
			      <div class="box-body">
			      	<div class="table-responsive">
				      	<table class="table-gray-th">	      	
				      		<tr>
				      			<th width="13%" class="text-right">专业规则号：</th>
				      			<td width="20%">${entity.ruleCode}</td>
				      			
				      			<%-- <th width="15%" class="text-right">专业代码：</th>
				      			<td width="35%">${entity.zyh}</td> --%>
				      			
				      			<th width="14%" class="text-right">所属专业：</th>
				      			<td width="20%">${entity.gjtSpecialtyBase.specialtyName}（${entity.gjtSpecialtyBase.specialtyCode}）</td>
				      			
				      			<th width="14%" class="text-right">学生类型：</th>
				      			<td width="20%">${studentTypeMap[entity.xslx]}</td>
				      		</tr>
				      		<%-- <tr>
				      			
				      			<th class="text-right">专业性质：</th>
				      			<td>${zyxzMap[entity.specialtyCategory]}</td>
				      		</tr> --%>
				      		<tr>
				      			<th class="text-right">专业层次：</th>
				      			<td>
				      			 ${pyccMap[entity.pycc]} 
				      			</td>
				      			
				      			<th class="text-right">学科：</th>
				      			<td>${subjectMap[entity.subject]}</td>
				      			
				      			<th class="text-right">学科门类：</th>
				      			<td>${categoryMap[entity.category]}</td>
				      			
				      		</tr>
				      		<tr>
				      			<th class="text-right">学制：</th>
				      			<td>${entity.xz}</td>
				      			
				      			<th class="text-right">专业类型：</th>
				      			<td>
				      				<c:choose>
				      					<c:when test="${2 == entity.type}">体验专业</c:when>
				      					<c:otherwise>正式专业</c:otherwise>
				      				</c:choose>
				      			</td>
				      			
				      			<%-- <th class="text-right">适用行业：</th>
				      			<td>${syhyMap[entity.syhy]}</td> --%>
				      			
				      			<th class="text-right">专业封面：</th>
				      			<td colspan="5">
				      				<div class="inline-block vertical-middle">
										<ul class="img-list clearfix">
											<li>
												<img id="imgId" src="${entity.zyfm }" class="user-image" style="cursor: pointer;">
											</li>
										</ul>
									</div>
				      			</td>
				      		</tr>
				      		<%-- <tr>
				      			<th class="text-right">专业封面：</th>
				      			<td colspan="5">
				      				<div class="inline-block vertical-middle">
										<ul class="img-list clearfix">
											<li>
												<img id="imgId" src="${entity.zyfm }" class="user-image" style="cursor: pointer;">
											</li>
										</ul>
									</div>
				      			</td>
				      		</tr> --%>
				      	</table>
			      	</div>
			      </div>
			    </div>
			    
			    <div class="box">
			      <div class="box-header with-border">
			        <h3 class="cnt-box-title text-bold">学分设置</h3>
			      </div>
			      <div class="box-body">
			      	<div class="table-responsive">
			      		<table class="table-gray-th" data-role="set-model">
							<tr>
								<%-- <th width="15%" class="text-right">总学分：</th>
								<td width="35%">${entity.zxf}</td> --%>
				
								<th width="13%" class="text-right">最低毕业学分：</th>
								<td width="20%"><fmt:formatNumber value="${entity.zdbyxf}" pattern="#.##" /></td>
				
								<th width="14%" class="text-right">中央电大考试学分：</th>
								<td width="20%"><fmt:formatNumber value="${entity.zyddksxf}" pattern="#.##" /></td>
								
								<th width="13%" class="text-right">必修学分：</th>
								<td width="20%"><fmt:formatNumber value="${entity.bxxf}" pattern="#.##" /></td>
				
								<%-- <th class="text-right">选修学分：</th>
								<td>${entity.xxxf}</td> --%>
							</tr>
						</table>
			      	</div>
			      </div>
			    </div>
			    
			    <div class="box">
			      <div class="box-header with-border">
			        <h3 class="cnt-box-title text-bold">课程模块学分</h3>
			      </div>
			      <div class="box-body">
			      	<div class="table-responsive">
			      		<table class="table table-bordered table-striped vertical-mid text-center margin-bottom-none" data-role="set-model">
							<thead>
								<tr>
									<th width="23%">课程模块</th>
									<th width="23%">模块最低学分</th>
									<th width="23%">模块最低毕业学分</th>
									<th width="23%">模块中央电大考试最低学分</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${list }" var="obj">
								<tr>
									<td>
										${courseTypeMap2[obj.id]}
									</td>
									<td>
										<fmt:formatNumber value="${obj.totalScore}" pattern="#.##" type="number"/>
									</td>
									<td>
										<fmt:formatNumber value="${obj.score}" pattern="#.##" type="number"/>
									</td>
									<td>
										<fmt:formatNumber value="${obj.crtvuScore}" pattern="#.##" type="number"/>
									</td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
			      	</div>
			      </div>
			    </div>
			    
			     <div class="box">
			      <div class="box-header with-border">
			        <h3 class="cnt-box-title text-bold">专业规则</h3>
			        <div class="pull-right no-margin">
					</div>
			      </div>
			      <div class="box-body" data-role="box">
			      	<div class="table-responsive tea-plan-box">
				      	<table id="planTable" class="plan-table tea-plan-tbl table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none" data-semester="第一学期">
				      		<thead>
				      			<tr>
				      				<th>序号</th>
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
				      		
				      		<tbody id="plan1">
				      		<%-- <c:forEach items="${gjtSpecialtyPlan}" var="item"> --%>
				      		<c:forEach items="${gjtSpecialtyPlan}" varStatus="i" var="item">
				      			<tr> 		      				
				      				<c:forEach items="${countMap}" var="countMapItem">
				      					<c:if test="${(item.termTypeCode!=gjtSpecialtyPlan[i.index-1].termTypeCode) && (item.termTypeCode==countMapItem.key)}">	      					
				      						<td class="order" rowspan="${countMapItem.value}" ></td>
						      				<td class="semester" rowspan="${countMapItem.value}">
						      					第${item.termTypeCode }学期
						      				</td>
				      					</c:if>
				      				</c:forEach>		
				      				<td>
										${courseTypeMap[item.courseTypeId]}
									</td>
									<td>${item.gjtCourse.kch }</td>
				      				<td>
										${item.gjtCourse.kcmc }
										<br>
										<c:choose>
											<c:when test="${item.gjtCourse.isEnabled == 1 }">
												<span class="text-green">
						      						(已启用)
						      					</span>
											</c:when>
											<c:otherwise>
												<span class="text-orange">
						      						(未启用)
						      					</span>
											</c:otherwise>
										</c:choose>
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
						                	<c:when test="${item.examUnit=='2'}">中央</c:when>
						                	<c:when test="${item.examUnit=='3'}">分校</c:when>
						                </c:choose>
				      				</td>
				      				<td>
				      					<fmt:formatNumber value="${item.score}" pattern="#.##" type="number"/>
				      				</td>
				      				<td>
				      					<fmt:formatNumber value="${item.hours}" pattern="#" type="number"/>
				      				</td>
				      			</tr>	
				      		</c:forEach>	
				      		</tbody>   		
				      	</table>	
			      	</div>
			      </div>
				  <div class="box-footer text-right">
				  	  <form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/edumanage/specialty/publish" method="post">
				          <input type="hidden" name="id" value="${entity.specialtyId}">
						  <button type="button" class="btn btn-default min-width-90px margin_r10" data-role="cancel" onclick="history.back()">上一步</button>
						  <c:if test="${isBtnPublish }">
						  <button id="btn-submit" type="submit" class="btn btn-primary min-width-90px" data-role="sure">确认发布</button>
						  </c:if>
				  	  </form>
				  </div>
			    </div> 
			</div>
		</div>
    </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">

$("#imgId").click(function() {
	var src = $(this).attr("src");
	if (src != "") {
		window.open(src);
	}
});

</script>
</body>
</html>