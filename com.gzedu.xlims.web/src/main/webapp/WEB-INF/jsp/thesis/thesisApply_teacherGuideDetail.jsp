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
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li><a href="#">指导记录</a></li>
		<li class="active">指导记录详情</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="thesisPlanId" value="${param.thesisPlanId}">
	<input type="hidden" name="gradeSpecialtyId" value="${param.gradeSpecialtyId}">
	<input type="hidden" name="teacherId" value="${param.teacherId}">
	<input type="hidden" name="search_EQ_status" value="">
	<div class="box">
    <div class="box-body">
      <div class="media pad">
        <div class="media-left" style="padding-right:25px;">
          <c:choose>
			<c:when test="${not empty teacherGuide.teacherZp}">
				<img src="${teacherGuide.teacherZp}" class="img-circle" style="width:112px;height:112px;" alt="User Image">
			</c:when>
			<c:otherwise>
		        <img src="${ctx}/static/dist/img/images/user-placehoder.png" class="img-circle" style="width:112px;height:112px;" alt="User Image">
			</c:otherwise>
		  </c:choose>
        </div>
        <div class="media-body">
          <h3 class="margin_t10">
            ${teacherGuide.teacherName}
            <small class="f14">毕业论文指导老师</small>
          </h3>
          <div class="row">
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>指导学期:</b> <span>${teacherGuide.gradeName}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>指导层次:</b>
              <span>${teacherGuide.trainingLevel}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>指导专业:</b> <span>${teacherGuide.specialtyName}（${teacherGuide.ruleCode}）</span>
            </div>
            
          </div>
        </div>
      </div>
    </div>
    <div class="box-footer">
      <div class="row stu-info-status">
        <div class="col-xs-1">
          <div class="f24 text-center">${teacherGuide.all}</div>
          <div class="text-center gray6">指导学员总数</div>
        </div>
        <div class="col-xs-2">
          <div class="f24 text-center">${teacherGuide.submitPropose}</div>
          <div class="text-center gray6">开题报告待定稿</div>
        </div>
        <div class="col-xs-1">
          <div class="f24 text-center">${teacherGuide.submitThesis}</div>
          <div class="text-center gray6">初稿待定稿</div>
        </div>
        <div class="col-xs-1">
          <div class="f24 text-center">${teacherGuide.teacherConfirm}</div>
          <div class="text-center gray6">定稿待评分</div>
        </div>
        <div class="col-xs-2">
          <div class="f24 text-center">${teacherGuide.collegeConfirm}</div>
          <div class="text-center gray6">定稿初评已通过</div>
        </div>
        <div class="col-xs-2">
          <div class="f24 text-center">${teacherGuide.reviewFailed}</div>
          <div class="text-center gray6">定稿初评未通过</div>
        </div>
        <div class="col-xs-3">
          <div class="pad-t10">
            <a href="#" role="button" class="btn btn-default btn-block btn-sm">
              <i class="fa fa-fw fa-simulated-login f18 vertical-middle"></i>
              	模拟登录
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="box margin-bottom-none">
    <div class="box-header with-border">
      <h3 class="box-title pad-t5">学员论文记录详情</h3>
    </div>
    <div class="box-body">
      <div class="filter-tabs filter-tabs2 clearfix">
		<ul class="list-unstyled">
			<li lang=":input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部（${all}）</li>
			<li value="2" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 2 }">class="actived"</c:if>>开题报告待定稿（${submitPropose}）</li>
			<li value="4" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 4 }">class="actived"</c:if>>初稿待定稿（${submitThesis}）</li>
			<li value="5" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 5 }">class="actived"</c:if>>定稿待评分（${teacherConfirm}）</li>
			<li value="7" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 7 }">class="actived"</c:if>>定稿初评已通过（${reviewFailed}）</li>
			<li value="6" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 6 }">class="actived"</c:if>>定稿初评未通过（${collegeConfirm}）</li>
		</ul>
	  </div>
      <div>
        <table class="table table-bordered vertical-mid text-center table-font">
          <thead class="with-bg-gray">
            <tr>
              <th>论文计划</th>
              <th>个人信息</th>
              <th>报读信息</th>
              <th>指导老师</th>
              <th>初评成绩</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
           	<c:choose>
           		<c:when test="${not empty pageInfo.content}">
					<c:forEach items="${pageInfo.content}" var="entity">
						<c:if test="${not empty entity}">
				            <tr>
				              <td>
		            			${entity.gjtThesisPlan.thesisPlanName}<br>
		            			<div class="gray9">（${entity.gjtThesisPlan.thesisPlanCode}）</div>
				              </td>
				              <td>
				                <div class="text-left">
				                  	姓名：${entity.gjtStudentInfo.xm} <c:if test="${entity.applyDegree == 1}"><i class="fa fa-graduate-1 text-orange f14"></i></c:if> <br>
				                  	学号：${entity.gjtStudentInfo.xh} <br>
				                  	<shiro:hasPermission name="/personal/index$privacyJurisdiction">
				                 	手机：${entity.gjtStudentInfo.sjh}
				                 	</shiro:hasPermission>
				                </div>
				              </td>
				              <td>
				                <div class="text-left">
							                  学期：${entity.gjtStudentInfo.gjtGrade.gradeName}<br>
							                  层次：${pyccMap[entity.gjtStudentInfo.gjtSpecialty.pycc]}<br>
							                  专业：${entity.gjtStudentInfo.gjtSpecialty.zymc}
				                </div>
				              </td>
				              <td>
				               	${entity.guideTeacher1.xm}
				              </td>
				              <td>
		            			<c:choose>
		            				<c:when test="${not empty entity.reviewScore}">
		            					${entity.reviewScore}
		            				</c:when>
		            				<c:otherwise>--</c:otherwise>
		            			</c:choose>
				              </td>
				              <td>
				               	${thesisStatusMap[entity.status]}
				              </td>
				              <td>
				                <a href="${ctx}/thesisApply/view?thesisPlanId=${entity.thesisPlanId}&studentId=${entity.studentId}" class="operion-item" data-toggle="tooltip" title="查看详情" data-role="set"><i class="fa fa-fw fa-view-more"></i></a>
				              </td>
				            </tr>
           				</c:if>
			        </c:forEach>
			    </c:when>
				<c:otherwise>
					<tr>
						<td align="center" colspan="7">暂无数据</td>
					</tr>
				</c:otherwise>
			</c:choose>
          </tbody>
        </table>
      </div>
	  <tags:pagination page="${pageInfo}" paginationSize="5" />
    </div>
  </div>
  </form>
</section>
		
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

</script>
</body>
</html>