<%@page import="com.gzedu.xlims.pojo.status.GraduationProgressCodeEnum"%>
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
<body>

<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">指导详情</h3>
	</div>
	<div class="box-body slim-Scroll">
		<div class="panel panel-default">
	      <div class="panel-heading">
	        <h3 class="panel-title">学生信息</h3>
	      </div>
	      <div class="panel-body">
	        <div class="table-responsive margin-bottom-none">
                <table class="table-gray-th table-font">
                  <tbody>
	                  <tr>
	                    <th width="15%" class="text-right">
	                      	姓名
	                    </th>
	                    <td width="35%">
	                      	${studentInfo.xm}
	                    </td>
	                    <th width="15%" class="text-right">
	                      	学号
	                    </th>
	                    <td width="35%">
	                      	${studentInfo.xh}
	                    </td>
	                  </tr>
	                  
	                  <tr>
	                    <th class="text-right">
	                      	身份证
	                    </th>
	                    <td>
	                      	
	                      	<shiro:hasPermission name="/personal/index$privacyJurisdiction">
								${studentInfo.sfzh}
							</shiro:hasPermission>
							<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
								<c:if test="${not empty studentInfo.sfzh }">
								${fn:substring(studentInfo.sfzh,0, 4)}******${fn:substring(studentInfo.sfzh,14, (studentInfo.sfzh).length())}
								</c:if>
							</shiro:lacksPermission>
	                      	
	                    </td>
	                    <th class="text-right">
	                     	 手机
	                    </th>
	                    <td>
	                      	<shiro:hasPermission name="/personal/index$privacyJurisdiction">
								${studentInfo.sjh}
							</shiro:hasPermission>
							<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
								<c:if test="${not empty studentInfo.sjh }">
								${fn:substring(studentInfo.sjh,0, 3)}******${fn:substring(studentInfo.sjh,8, (studentInfo.sjh).length())}
								</c:if>
							</shiro:lacksPermission>
	                    </td>
	                  </tr>
	                  <tr>
	                    <th class="text-right">
	                      	年级
	                    </th>
	                    <td>
	                      	${studentInfo.gjtGrade.gradeName}
	                    </td>
	                    <th class="text-right">
	                    	  层次
	                    </th>
	                    <td>
	                      	${trainingLevelMap[studentInfo.pycc]} 
	                    </td>
	                  </tr>
	                  <tr>
	                    <th class="text-right">
	                      	专业
	                    </th>
	                    <td colspan="3">
	                      	${studentInfo.gjtSpecialty.zymc}
	                    </td>
	                  </tr>
                	</tbody>
                </table>
              </div>
	      </div>
	    </div>
	    <div class="panel panel-default margin-bottom-none">
	      <div class="panel-heading">
	        <h3 class="panel-title">指导记录</h3>
	      </div>
	      <div class="panel-body">
	      	<div class="approval-list approval-list-2 full-width clearfix no-margin">
	            <dl class="approval-item">
	              <dt class="clearfix">
	                <div class="a-tit">
	                  <b>申请论文写作 </b>
	                </div>
	                <span class="time state-lb"><fmt:formatDate value="${apply.createdDt}" pattern="yyyy-MM-dd HH:mm"/></span>
	              </dt>
	            </dl>
	            <c:forEach items="${progList}" var="prog" varStatus="status">
	            	<dl class="approval-item">
		              <dt class="clearfix">
		                <div class="a-tit">
		                  <b>${thesisProgressCodeMap[prog.progressCode]}</b>
		                </div>
		                <span class="time state-lb"><fmt:formatDate value="${prog.createdDt}" pattern="yyyy-MM-dd HH:mm"/></span>
		              </dt>
		              <dd>
		              	<c:if test="${prog.progressCode eq GraduationProgressCodeEnum.THESIS_STAY_OPEN.code}">
                    		<div class="txt media">
			                	<div class="media-left">
			                		<img src="${apply.guideTeacher1.zp}" class="img-circle" alt="User Image" width="45" height="45">
			                		<div class="text-center f12">${apply.guideTeacher1.xm}</div>
			                	</div>
			                </div>
                    	</c:if>
                    	<c:forEach items="${progRecord[prog.progressId]}" var="record">
	                    	<div class="txt media">
			                	<div class="media-left">
			                		<c:choose>
	                					<c:when test="${record.isStudent == 1}">
					                		<img src="${record.gjtStudentInfo.avatar}" class="img-circle" alt="User Image" width="45" height="45">
					                		<div class="text-center f12">${record.gjtStudentInfo.xm}</div>
	                					</c:when>
	                					<c:otherwise>
		                          			<img src="${record.teacher.zp}" class="img-circle" alt="User Image" width="45" height="45">
			                				<div class="text-center f12">${record.teacher.xm}</div>
	                					</c:otherwise>
	                				</c:choose>
			                	</div>
			                	<div class="media-body pad-l15 f12">
		    						<div><fmt:formatDate value="${record.createdDt}" pattern="yyyy-MM-dd HH:mm"/></div>
		    						<div class="margin_t5">${record.content}</div>
		    						<div class="margin_t5">
		    							<a href="${record.attachment}">${record.attachmentName}</a>
		    						</div>
			                  </div>
			                </div>
                    	</c:forEach>
		              </dd>
		            </dl>
	            </c:forEach>
	          </div>
	      </div>
	    </div>
	</div>
</div>

<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
</div>

<script type="text/javascript">
$('.slim-Scroll').slimScroll({
    height: $(window).height()-106,
    size: '5px'
});
//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});
</script>
		
</body>
</html>