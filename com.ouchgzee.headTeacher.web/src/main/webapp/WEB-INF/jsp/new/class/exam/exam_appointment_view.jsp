<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任平台</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
    <div class="pull-left">
        	您所在位置：
    </div>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">考试组织</a></li>
        <li><a href="#">考试预约</a></li>
        <li class="active">详情</li>
    </ol>
</section>
<section class="content">

    <div class="box">
        <div class="box-body">
            <div class="row stu-info-status">
                <div class="col-xs-3">
		            <div class="media pad">
		                <div class="media-left" style="padding-right:25px;">
		                    <c:choose>
		                        <c:when test="${not empty studentInfo.avatar}">
		                            <img id ="headImgId" src="${studentInfo.avatar}" class="img-circle" alt="" style="width: 128px; height: 128px;">
		                        </c:when>
		                        <c:otherwise>
		                            <img id ="headImgId" src="${ctx}/static/images/headImg04.png" class="img-circle" alt="" style="width: 128px; height: 128px;">
		                        </c:otherwise>
		                    </c:choose>
		                </div>
		                <div class="media-body">
		                    <b>姓名:</b> <span>${studentInfo.xm}（<dic:getLabel typeCode="Sex" code="${studentInfo.xbm}"/>）</span><br> <br>
		                    <b>学号:</b> <span>${studentInfo.xh}</span> <br> <br>
		                    <b>手机:</b> <span>${studentInfo.sjh}</span> <br> <br>
		                    <b>邮箱:</b> <span>${studentInfo.dzxx}</span>
		                </div>
		            </div>
                </div>
                <div class="col-xs-3">
                    <b>层次:</b> <span><dic:getLabel typeCode="TrainingLevel" code="${studentInfo.pycc}"/></span> <br> <br>
                    <b>年级:</b> <span>${studentInfo.gjtGrade.gjtYear.name}</span> <br> <br>
                    <b>学期:</b> <span>${studentInfo.gjtGrade.gradeName}</span> <br> <br>
                    <b>专业:</b> <span>${studentInfo.gjtSpecialty.zymc}</span>
                </div>
                <div class="col-xs-3">
                	<c:choose>
                		<c:when test="${isCurrent}">
                			<div class="f24 text-center">${hasAppointment}/${canAppointment}</div>
                    		<div class="text-center gray6">已预约/可预约科目</div>
                		</c:when>
                		<c:otherwise>
                			<div class="f24 text-center">${canAppointment}</div>
                    		<div class="text-center gray6">已预约科目</div>
                		</c:otherwise>
                	</c:choose>
                </div>
                <div class="col-xs-3">
                	<c:choose>
                		<c:when test="${isCurrent}">
                			<c:choose>
                				<c:when test="${not empty point}">
                					<div class="f24 text-center">已预约</div>
                				</c:when>
                				<c:otherwise>
                					<div class="f24 text-center">未预约</div>
                				</c:otherwise>
                			</c:choose>
                    		<div class="text-center gray6">考点预约</div>
                		</c:when>
                		<c:otherwise>
                			
                		</c:otherwise>
                	</c:choose>
                </div>
            </div>
        </div>
    </div>

    <div class="box no-border">
    	<c:choose>
      		<c:when test="${isCurrent}">
      			<table class="table table-bordered table-striped table-cell-ver-mid text-center dataTable">
                    <thead>
	                    <tr>
	                        <th>考点名称</th>
	                        <th>区域</th>
	                        <th>地址</th>
	                        <th>考点状态</th>
	                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty point}">
                            <tr>
                                <td>${point.pointName}</td>
                                <td>
                                    ${point.areaName}
                                </td>
                                <td>
                                    ${point.address}
                                </td>
                                <td>已预约</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td>--</td>
                                <td>
                                   --
                                </td>
                                <td>
                                   --
                                </td>
                                <td><span class="text-orange">待预约</span></td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                </div>
                 <div class="box no-border">
      			<table class="table table-bordered table-striped table-cell-ver-mid text-center dataTable">
                    <thead>
	                    <tr>
	                        <th>学期</th>
	                        <th>考试科目</th>
	                        <th>考试方式</th>
	                        <th>考试时间</th>
	                        <th>预约时间</th>
	                        <th>是否补考</th>
	                        <th>缴费状态</th>
	                        <th>预约状态</th>
	                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty appointmentMap}">
                            <c:forEach items="${appointmentMap}" var="map" varStatus="s1">
                            	<c:forEach items="${map.value}" var="appointment" varStatus="s2">
	                            	<c:choose>
	                            		<c:when test="${s2.first}">
	                            			<tr>
	                            				<td rowspan="${fn:length(map.value)}">第${map.key}学期</td>
	                            				<td>${appointment.courseName}</td>
	                            				<td>${appointment.type}</td>
	                            				<td>
	                            					<fmt:formatDate value="${appointment.examSt}" type="date" pattern="yyyy-MM-dd"/><br>
	                            					至<br>
	                            					<fmt:formatDate value="${appointment.examEnd}" type="date" pattern="yyyy-MM-dd"/>
	                            				</td>
	                            				<td>
	                            					<fmt:formatDate value="${appointment.bookSt}" type="date" pattern="yyyy-MM-dd"/><br>
	                            					至<br>
	                            					<fmt:formatDate value="${appointment.bookEnd}" type="date" pattern="yyyy-MM-dd"/>
	                            				</td>
	                            				<td>
	                            					<c:choose>
	                            						<c:when test="${appointment.payState == 0 || appointment.payState == 1}">是</c:when>
	                            						<c:otherwise>否</c:otherwise>
	                            					</c:choose>
	                            				</td>
	                            				<td>
	                            					<c:choose>
	                            						<c:when test="${appointment.payState == 0}"><span class="text-orange">待缴费</span></c:when>
	                            						<c:when test="${appointment.payState == 1}">已缴费</c:when>
	                            						<c:otherwise>无需缴费</c:otherwise>
	                            					</c:choose>
	                            				</td>
	                            				<td>
	                            					<c:choose>
	                            						<c:when test="${not empty appointment.courseId2}">已预约</c:when>
	                            						<c:otherwise><span class="text-orange">待预约</span></c:otherwise>
	                            					</c:choose>
	                            				</td>
	                            			</tr>
	                            		</c:when>
	                            		<c:otherwise>
	                            			<tr>
	                            				<td>${appointment.courseName}</td>
	                            				<td>${appointment.type}</td>
	                            				<td>
	                            					<fmt:formatDate value="${appointment.examSt}" type="date" pattern="yyyy-MM-dd"/><br>
	                            					至<br>
	                            					<fmt:formatDate value="${appointment.examEnd}" type="date" pattern="yyyy-MM-dd"/>
	                            				</td>
	                            				<td>
	                            					<fmt:formatDate value="${appointment.bookSt}" type="date" pattern="yyyy-MM-dd"/><br>
	                            					至<br>
	                            					<fmt:formatDate value="${appointment.bookEnd}" type="date" pattern="yyyy-MM-dd"/>
	                            				</td>
	                            				<td>
	                            					<c:choose>
	                            						<c:when test="${appointment.payState == 0 || appointment.payState == 1}">是</c:when>
	                            						<c:otherwise>否</c:otherwise>
	                            					</c:choose>
	                            				</td>
	                            				<td>
	                            					<c:choose>
	                            						<c:when test="${appointment.payState == 0}"><span class="text-orange">待缴费</span></c:when>
	                            						<c:when test="${appointment.payState == 1}">已缴费</c:when>
	                            						<c:otherwise>无需缴费</c:otherwise>
	                            					</c:choose>
	                            				</td>
	                            				<td>
	                            					<c:choose>
	                            						<c:when test="${not empty appointment.courseId2}">已预约</c:when>
	                            						<c:otherwise><span class="text-orange">待预约</span></c:otherwise>
	                            					</c:choose>
	                            				</td>
	                            			</tr>
	                            		</c:otherwise>
	                            	</c:choose>
                            	</c:forEach>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td align="center" colspan="15">暂无数据</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
      		</c:when>
      		<c:otherwise>
      			<table class="table table-bordered table-striped table-cell-ver-mid text-center dataTable">
                    <thead>
	                    <tr>
	                        <th>学期</th>
	                        <th>考试科目</th>
	                        <th>考试方式</th>
	                        <th>考试时间</th>
	                        <th>考试成绩</th>
	                        <th>考试状态</th>
	                        <th>操作</th>
	                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty appointmentMap}">
                            <c:forEach items="${appointmentMap}" var="map" varStatus="s1">
                            	<c:forEach items="${map.value}" var="appointment" varStatus="s2">
	                            	<c:choose>
	                            		<c:when test="${s2.first}">
	                            			<tr>
	                            				<td rowspan="${fn:length(map.value)}">第${map.key}学期</td>
	                            				<td>${appointment.courseName}</td>
	                            				<td>${appointment.type}</td>
	                            				<td>
	                            					<fmt:formatDate value="${appointment.examSt}" type="date" pattern="yyyy-MM-dd"/><br>
	                            					至<br>
	                            					<fmt:formatDate value="${appointment.examEnd}" type="date" pattern="yyyy-MM-dd"/>
	                            				</td>
	                            				<td>
	                            					${appointment.examScore}
	                            				</td>
	                            				<td>
	                            					已结束
	                            				</td>
	                            				<td>
	                            				</td>
	                            			</tr>
	                            		</c:when>
	                            		<c:otherwise>
	                            			<tr>
	                            				<td>${appointment.courseName}</td>
	                            				<td>${appointment.type}</td>
	                            				<td>
	                            					<fmt:formatDate value="${appointment.examSt}" type="date" pattern="yyyy-MM-dd"/><br>
	                            					至<br>
	                            					<fmt:formatDate value="${appointment.examEnd}" type="date" pattern="yyyy-MM-dd"/>
	                            				</td>
	                            				<td>
	                            					${appointment.examScore}
	                            				</td>
	                            				<td>
	                            					已结束
	                            				</td>
	                            				<td>
	                            				</td>
	                            			</tr>
	                            		</c:otherwise>
	                            	</c:choose>
                            	</c:forEach>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td align="center" colspan="15">暂无数据</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
      			
      		</c:otherwise>
      	</c:choose>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

</body>
</html>
