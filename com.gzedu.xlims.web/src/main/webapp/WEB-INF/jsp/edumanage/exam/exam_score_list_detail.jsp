<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>成绩详情</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	
	
	
	
});


</script>

</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li><a href="#">考试成绩</a></li>
		<li class="active">成绩详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
    <div class="box-body">
      <div class="media pad">
        <div class="media-left" style="padding-right:25px;">
          <img src="${map['AVATAR'] }" class="img-circle" style="width:112px;height:112px;" alt="User Image">
        </div>
        <div class="media-body">
          <h3 class="margin_t10">
            ${map['XM'] }
            <small class="f14">( ${map['XBM']==0?'男':'女'})</small>
          </h3>
          <table class="full-width per-tbl">
      		<tr>
      			<th width="35">学号:</th>
      			<td> ${map['XH'] }</td>

      			<th width="80">身份证:</th>
      			<td>
      				<shiro:hasPermission name="/personal/index$privacyJurisdiction">
						${map['CARDNO']}
					</shiro:hasPermission>
					<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
						<c:if test="${not empty map['CARDNO'] }">
						${fn:substring(map['CARDNO'],0, 4)}******${fn:substring(map['CARDNO'],14, (map['CARDNO']).length())}
						</c:if>
					</shiro:lacksPermission>
      			</td>

      			<th width="80">手机:</th>
      			<td>
      			<shiro:hasPermission name="/personal/index$privacyJurisdiction">
					${map['MOBILEPHONE']}
				</shiro:hasPermission>
				<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
					<c:if test="${not empty map['MOBILEPHONE'] }">
					${fn:substring(map['MOBILEPHONE'],0, 3)}******${fn:substring(map['MOBILEPHONE'],8, (map['MOBILEPHONE']).length())}
					</c:if>
				</shiro:lacksPermission>
      			</td>
      		</tr>
      		<tr>
      			<th>邮箱:</th>
      			<td>${map['EMAIL'] }</td>

      			<th>单位名称:</th>
      			<td>${map['SCCO'] }</td>

      			<th>联系地址:</th>
      			<td>
      				${map['ADDRESS'] }
      			</td>
      		</tr>
      	  </table>
        </div>
      </div>
    </div>
    <div class="box-footer">
      <div class="row stu-info-status text-center">
        <div class="col-sm-2_5">
          <div class="f24">${empty studentCredit.zxf ?'0': studentCredit.zxf}</div>
          <div class="gray6">总学分</div>
        </div>
        <div class="col-sm-2_5">
          <div class="f24">${pyccMap[map['PYCC']] eq '高起专' ? '76' : '71'}</div>
          <div class="gray6">最低毕业学分</div>
        </div>
        <div class="col-sm-2_5">
          <div class="f24">${empty studentCredit.yxxf ?'0': studentCredit.yxxf}</div>
          <div class="gray6">已获得学分</div>
        </div>
        <div class="col-sm-2_5">
          <div class="f24">${totalCoursePass}/${totalCourse }</div>
          <div class="gray6">已通过/全部课程</div>
        </div>
        <div class="col-sm-2_5">
          <div class="f24">3/20</div>
          <div class="gray6">已通过/全部考试</div>
        </div>
      </div>
    </div>
  </div>

  <div class="box margin-bottom-none">
    <div class="box-body">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">报读明细</span>
          </h3>
        </div>
        <div id="info-box-2" class="collapse in">
          <form class="theform">
            <div class="panel-body content-group overlay-wrapper position-relative">
              <div class="cnt-box-body">
                <div class="table-responsive margin-bottom-none">
                  <table class="table-gray-th">
                    <tr>
                      <th width="15%">
                        院校
                      </th>
                      <td width="35%">
                       ${map['XXMC'] }
                      </td>
                      <th width="15%">
                        年级
                      </th>
                      <td width="35%">
                          ${map['GRADENAME'] }
                      </td>
                    </tr>
                    <tr>
                      <th>
                        层次
                      </th>
                      <td>
                       	 ${pyccMap[map['PYCC']]}
                      </td>
                      <th>
                        专业
                      </th>
                      <td>
                        ${map['ZYMC'] }
                      </td>
                    </tr>
                    <tr>
                      <th>
                        学习中心
                      </th>
                      <td>
                        ${map['ORGNAME'] }
                      </td>
                      <th>
                        入学时间
                      </th>
                      <td>
                       ${map['RXNY'] }
                      </td>
                    </tr>
                  </table>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">成绩详情</span>
          </h3>
        </div>
        <div id="info-box-3" class="collapse in">
          <div class="panel-body content-group">
            <div class="pad-l10 pad-r10">
              <div class="table-responsive">
                <table class="table table-bordered table-striped vertical-mid text-center table-font">
                  <thead>
                    <tr>
                      <th>学期</th>
                      <th>课程模块</th>
                      <th>课程名称</th>
                      <th>考试类型</th>
                      <th>学分</th>
                      <th>已获学分</th>
                      <th>成绩比例</th>
                      <th>学习成绩</th>
                      <th>考试成绩</th>
                      <th>总成绩</th>
                      <th>状态</th>
                    </tr>
                  </thead>
                  <tbody>
                  	<c:forEach items="${listResult }" var="list" varStatus="status" >
             			<tr>
             				<td rowspan="${fn:length(list)}">第${status.index+1 }学期</td>
             				<c:forEach items="${list }" var="item">
             					<td>${item.COURSE_TYPE_ID }</td>
             					<td>${item.KCMC }</td>
             					<td>${examTypeMap[item.EXAM_TYPE] }  </td>
             					<td>${item.SCORE }</td>
             					<td>计算出来</td>
             					<td>
             						学习成绩：${item.STUDY_RATIO }% <br>
                        			考试成绩：${item.EXAM_RATIO }%
             					</td>
             					<td>${item.COURSE_SCHEDULE }</td>
             					<td>${item.EXAM_SCORE }</td>
             					<td>${item.TOTAL_POINTS }</td> 
             					<c:if test="${item.SCORE_STATE=='0' or item.TOTAL_POINTS>=60 }"><td class="text-green">通过</td></c:if>
          						<c:if test="${item.SCORE_STATE=='1' or item.TOTAL_POINTS<60 }"><td class="text-red">未通过</td></c:if>
             				</c:forEach>
             			</tr>
                  	</c:forEach>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">学分详情</span>
          </h3>
        </div>
        <div id="info-box-4" class="collapse in">
          <div class="panel-body content-group">
            <div class="pad-l10 pad-r10">
              <div class="table-responsive">
                <table class="table table-bordered table-striped vertical-mid text-center margin_b10 table-font">
                  <thead>
                    <tr>
                      <th>课程模块</th>
                      <th>总学分</th>
                      <th>最低毕业学分</th>
                      <th>已获学分</th>
                      <th>课程总数</th>
                      <th>已修课程数</th>
                      <th>状态</th>
                    </tr>
                  </thead>
                   	<c:forEach var="resultsand" items="${studentCredit.studentand}">
                     <tr>
                      	<td>${resultsand.KCLBM }</td>
                      	<td>${resultsand.XF }</td>
                      	<td>${resultsand.ZDF }</td>
                      	<td>${resultsand.YXXF }</td>
                         <td>${resultsand.KCSL }</td>
                         <td>${resultsand.pass}</td>
                         <c:choose>
                          		<c:when test="${resultsand.zt=='0'}">
                          			<td class="text-red">未达标</td>
                          		</c:when>
                          		<c:when test="${resultsand.zt=='1'}">
                          			<td class="text-green">已通过</td>
                          		</c:when>
                         </c:choose>
                      	<%-- <td><c:out value="${resultsand.zt }" /></td> --%>
                     </tr>
                      <tr>
                      <td>专业拓展课</td>
                      <td>12</td>
                      <td>10</td>
                      <td>2</td>
                      <td>4</td>
                      <td>0</td>
                      <td class="text-red">未达标</td>
                    </tr>
                  </c:forEach>
                </table>
                <div class="text-orange">注：为了能够顺利毕业，以上各课程模块所得学分都必须满足最低毕业学分要求</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>


</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
