<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>成绩详情</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试管理</a></li>
		<li><a href="javascript:;">考试成绩</a></li>
		<li class="active">成绩详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
    <div class="box-body">
      <div class="media pad">
        <div class="media-left" style="padding-right:25px;">
          <c:if test="${not empty resultMap.ZP }">
          	<img src="${resultMap.ZP }" class="img-circle" style="width:112px;height:112px;" alt="User Image">
          </c:if>
          <c:if test="${empty resultMap.ZP }">
          	<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/user-placehoder.png" class="img-circle" style="width:112px;height:112px;" alt="User Image">
          </c:if>
        </div>
        <div class="media-body">
          <h3 class="margin_t10">${resultMap.XM }
            <small class="f14">
            	<c:if test="${resultMap.XBM eq '2'}">(女)</c:if>
            	<c:if test="${resultMap.XBM ne '2'}">(男)</c:if>
            </small>
          </h3>
          <table class="full-width per-tbl">
      		<tr>
      			<th width="35">学号:</th>
      			<td>${resultMap.XH }</td>
      			<th width="80">身份证:</th>
      			<td>
      				<shiro:hasPermission name="/personal/index$privacyJurisdiction">
						${resultMap.SFZH}
					</shiro:hasPermission>
					<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
						<c:if test="${not empty resultMap.SFZH }">
						${fn:substring(resultMap.SFZH,0, 4)}******${fn:substring(resultMap.SFZH,14, (resultMap.SFZH).length())}
						</c:if>
					</shiro:lacksPermission>
      			</td>
      			<th width="80">手机:</th>
      			<td>
      				<shiro:hasPermission name="/personal/index$privacyJurisdiction">
						${resultMap.SJH}
					</shiro:hasPermission>
					<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
						<c:if test="${not empty resultMap.SJH }">
						${fn:substring(resultMap.SJH,0, 3)}******${fn:substring(resultMap.SJH,8, (resultMap.SJH).length())}
						</c:if>
					</shiro:lacksPermission>
      			</td>
      		</tr>
      		<tr>
      			<th>邮箱:</th>
      			<td>
      				<shiro:hasPermission name="/personal/index$privacyJurisdiction">
						${resultMap.DZXX}
					</shiro:hasPermission>
					<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
						<c:if test="${not empty resultMap.DZXX }">
						${fn:substring(resultMap.DZXX, 0, 3)}******${fn:substring(resultMap.DZXX, 6, (resultMap.DZXX).length())}
						</c:if>
					</shiro:lacksPermission>
      			</td>
      			<th>单位名称:</th>
      			<td>${resultMap.SC_CO }</td>
      			<th>联系地址:</th>
      			<td>
      				${resultMap.TXDZ }
      			</td>
      		</tr>
      	  </table>
        </div>
      </div>
    </div>
    <div class="box-footer">
    	<div class="table-responsive no-border">
	      <table class="vertical-middle text-center" width="100%">
	      	<tbody>
	      		<tr>
			        <shiro:lacksPermission name="/studymanage/getScoreList$schoolModel">
			            <td class="pad" style="border-right: 1px solid #e5e5e5;">
			              <div class="f24">${resultMap.ZXF }</div>
			              <div class="gray6">总学分</div>
			            </td>
			            <td class="pad" style="border-right: 1px solid #e5e5e5;">
			              <div class="f24">${resultMap.ZDBYXF }</div>
			              <div class="gray6">最低毕业学分</div>
			            </td>
			            <td class="pad" style="border-right: 1px solid #e5e5e5;">
			              <div class="f24">${resultMap.GET_POINT }</div>
			              <div class="gray6">已获得学分</div>
			            </td>
			        </shiro:lacksPermission>
			        <td class="pad" style="border-right: 1px solid #e5e5e5;">
			          <div class="f24">${resultMap.ALL_COURSE_COUNT }</div>
			          <div class="gray6">课程总数</div>
			        </td>
			        <td class="pad" style="border-right: 1px solid #e5e5e5;">
			          <div class="f24">${resultMap.PASS_COURSE_COUNT }</div>
			          <div class="gray6">已通过<br>课程数</div>
			        </td>
			        <td class="pad" style="border-right: 1px solid #e5e5e5;">
			          <div class="f24">${resultMap.NOT_PASS_COURSE_COUNT }</div>
			          <div class="gray6">未通过<br>课程数</div>
			        </td>
			        <td class="pad" style="border-right: 1px solid #e5e5e5;">
			          <div class="f24">${resultMap.REGISTER_COURSE_COUNT }</div>
			          <div class="gray6">登记中<br>课程数</div>
			        </td>
			        <td class="pad" style="border-right: 1px solid #e5e5e5;">
			          <div class="f24">${resultMap.STUDY_COURSE_COUNT }</div>
			          <div class="gray6">学习中<br>课程数</div>
			        </td>
			        <td class="pad">
			          <div class="f24">${resultMap.NOT_STUDY_COURSE_COUNT }</div>
			          <div class="gray6">未学习<br>课程数</div>
			        </td>
		        </tr>
	        </tbody>
	      </table>
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
                      <th width="15%"> 院校</th>
                      <td width="35%">${resultMap.XXMC }</td>
                      <th width="15%">学期</th>
                      <td width="35%">${resultMap.GRADE_NAME }</td>
                    </tr>
                    <tr>
                      <th>层次</th>
                      <td>${resultMap.PYCC_NAME }</td>
                      <th>专业</th>
                      <td>${resultMap.ZYMC }</td>
                    </tr>
                    <tr>
                      <th> 学习中心</th>
                      <td>${resultMap.SC_NAME }</td>
                      <th>入学时间</th>
                      <td>${resultMap.CREATED_DT }</td>
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
                      <shiro:lacksPermission name="/studymanage/getScoreList$schoolModel">
                          <th>学分</th>
                          <th>已获学分</th>
                      </shiro:lacksPermission>
                      <th>形考比例</th>
                      <th>学习成绩</th>
                      <th>考试成绩</th>
                      <th>总成绩</th>
                      <th>状态</th>
                      <th>考试次数</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                  	<c:forEach items="${resultMap.SCORELIST }" var="scorelist">
                    <tr>
                      <td class="kkxq_${scorelist.KKXQ}">
                      	第${scorelist.KKXQ }学期
                      	<input type="hidden" class="kkxq" value="${scorelist.KKXQ}"  />
                      </td>
                      <td>${scorelist.KCLBM_NAME }</td>
                      <td>${scorelist.KCMC }</td>
                      <td>
                      	<c:choose>
                      		<c:when test="${scorelist.EXAM_STATE eq '0' || scorelist.EXAM_STATE eq '1'}">
                      			<c:choose>
                      				<c:when test="${not empty scorelist.EXAM_PLAN_KSFS_NAME }">
                      					${scorelist.EXAM_PLAN_KSFS_NAME }
                      				</c:when>
                      				<c:when test="${not empty scorelist.KSFS_NAME }">
                      					${scorelist.KSFS_NAME }
                      				</c:when>
                      				<c:otherwise>
                      					--
                      				</c:otherwise>
                      			</c:choose>
	           				</c:when>
	           				<c:otherwise>
	           					--
	           				</c:otherwise>
	           			</c:choose>
                      </td>
	                  <shiro:lacksPermission name="/studymanage/getScoreList$schoolModel">
	                      <td>${scorelist.XF }</td>
	                      <td>
	                      	<c:choose>
	                      		<c:when test="${scorelist.EXAM_STATE eq '0' || scorelist.EXAM_STATE eq '1'}">
		           					${scorelist.GET_CREDITS }
		           				</c:when>
		           				<c:otherwise>
		           					--
		           				</c:otherwise>
	           				</c:choose>
	                      </td>
	                  </shiro:lacksPermission>
                      <td>
                      	<c:choose>
                      		<c:when test="${scorelist.EXAM_STATE eq '0' || scorelist.EXAM_STATE eq '1'}">
	           					${scorelist.KCXXBZ }%
	           				</c:when>
	           				<c:otherwise>
	           					--
	           				</c:otherwise>
           				</c:choose>
                      </td>
                      <td>${scorelist.EXAM_SCORE }</td>
                      <td>
                      	<dic:getExamScore typeCode="EXAM_SCORE" code="${scorelist.EXAM_SCORE1 }" />
                      </td>
                      <td>
                      	<c:choose>
                      		<c:when test="${scorelist.EXAM_STATE eq '0' || scorelist.EXAM_STATE eq '1'}">
                      			<dic:getExamScore typeCode="EXAM_SCORE" code="${scorelist.EXAM_SCORE2 }" />
	           				</c:when>
	           				<c:otherwise>
	           					--
	           				</c:otherwise>
           				</c:choose>
                      </td>
                      <td>
                      	<c:choose>
                      		<c:when test="${scorelist.STUDY_FLG eq '0' }">
	           					<span class="gray9">未学习</span>
	           				</c:when>
                      		<c:when test="${scorelist.EXAM_STATE eq '0' }">
	           					<span class="text-red">未通过</span>
	           				</c:when>
	           				<c:when test="${scorelist.EXAM_STATE eq '1' }">
	           					<span class="text-green">已通过</span>
	           				</c:when>
	           				<c:when test="${scorelist.EXAM_STATE eq '2' }">
	           					<span class="text-red">学习中</span>
	           				</c:when>
	           				<c:when test="${scorelist.EXAM_STATE eq '3' }">
	           					<span class="text-red">登记中</span>
	           				</c:when>
	           				<c:otherwise>
	           					<span>--</span>
	           				</c:otherwise>
	           		  	</c:choose>
                      </td>
                      <td>${scorelist.EXAM_NUM}</td>
                      <td>
                      	<c:if test="${scorelist.EXAM_STATE eq '2' }">
           					--
           				</c:if>
           				<c:if test="${scorelist.EXAM_STATE ne '2'  and scorelist.EXAM_NUM>0  }">
                      	    <a href="${ctx}/exam/new/record/getHistoryView/${scorelist.TEACH_PLAN_ID}/${scorelist.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="" data-role="view-more" data-original-title="查看历史成绩"><i class="fa fa-fw fa-view-more"></i></a>
           				</c:if>
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
        <shiro:lacksPermission name="/studymanage/getScoreList$schoolModel">
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
                                  <th>通过课程数</th>
                                  <th>未通过课程数</th>
                                  <th>状态</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach items="${resultMap.MODULELIST }" var="modulelist">
                                  <tr>
                                      <td>${modulelist.NAME }</td>
                                      <td>${modulelist.XF_COUNT }</td>
                                      <td>${modulelist.LIMIT_SCORE }</td>
                                      <td>${modulelist.GET_POINT }</td>
                                      <td>${modulelist.COUNT_COURSE }</td>
                                      <td>${modulelist.PASS_COURSE }</td>
                                      <td>${modulelist.UN_PASS_COURSE }</td>
                                      <td>
                                          <c:choose>
                                              <c:when test="${modulelist.GET_POINT>=modulelist.LIMIT_SCORE  and modulelist.COUNT_COURSE eq modulelist.PASS_COURSE and modulelist.PASS_COURSE>0}">
                                                  <span class="text-green">已达标</span>
                                              </c:when>
                                              <c:otherwise>
                                                  <span class="text-red">未达标</span>
                                              </c:otherwise>
                                          </c:choose>
                                      </td>
                                  </tr>
                              </c:forEach>
                              </tbody>
                          </table>
                          <div class="text-orange">注：为了能够顺利毕业，以上各课程模块所得学分都必须满足最低毕业学分要求</div>
                      </div>
                  </div>
              </div>
            </div>
          </div>
        </shiro:lacksPermission>
    </div>
  </div>
</section>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
$(".kkxq").each(function(){
	var kkxq = $(this).val();
	if ($(".kkxq_"+kkxq).length>1) {
		$(".kkxq_"+kkxq).each(function(i){
			if (i==0) {
				$(this).attr("rowspan", $(".kkxq_"+kkxq).length);
			} else {
				$(this).remove();
			}
		})
	}
});

// 查看历史成绩
$('[data-role="view-more"]').click(function(event) {
    event.preventDefault();
    var self=this;
    $.mydialog({
        id:'view-more',
        width:600,
        height:415,
        zIndex:11000,
        content: 'url:'+$(this).attr('href')
    });
});

</script>
</html>