<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>课程成绩</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">必修课程平均成绩详情</h3>
	</div>
	<div class="box-body">
		<div class="slim-Scroll">
			<h3 class="cnt-box-title f16 margin_b10">专业规则要求</h3>
            <table class="table-gray-th table-font text-center vertical-middle">
              <tbody>
                <th>最低毕业学分</th>
                <td>${specialty.zdbyxf}</td>

                <th>已通过学分</th>
                <td>
                    ${totalCredits}
                    <c:choose>
                    	<c:when test="${totalCredits>=specialty.zdbyxf }">
                    		<div class="text-green">（已达标）</div>
                    	</c:when>
                    	<c:otherwise>
                    		<div class="text-red">（未达标）</div>
                    	</c:otherwise>
                    </c:choose>
                </td>

                <th>中央电大考试学分</th>
                <td>${specialty.zyddksxf}</td>
                <th>已通过中央电大考试学分</th>
                <td>
                    ${centerCredits}
                    <c:choose>
                    <c:when test="${centerCredits>=specialty.zyddksxf }">
                    		<div class="text-green">（已达标）</div>
                    	</c:when>
                    	<c:otherwise>
                    		<div class="text-red">（未达标）</div>
                    	</c:otherwise>
                    </c:choose>
                </td>
              </tbody>
            </table>

            <h3 class="cnt-box-title f16 margin_b10 margin_t20">成绩明细</h3>
            <table class="table table-bordered vertical-mid text-center margin-bottom-none table-font">
              <thead class="with-bg-gray">
                <tr>
                  <th>课程模块</th>
                  <th>模块<br>最低学分</th>
                  <th>模块<br>中央最低学分</th>
                  <th>模块<br>已通过学分</th>
                  <th>课程代码</th>
                  <th>课程名称</th>
                  <th>课程性质</th>
                  <th>课程类型</th>
                  <th>学分</th>
                  <th>开设学期</th>
                  <th>考试单位</th>
                  <th>成绩</th>
                  <th>是否达标<br>毕业条件</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${modelList}" var="item">
              		<c:forEach items="${item.achieveList}" var="a" varStatus="i">
              			<tr>
	              			<c:if test="${i.index==0}">
	              				 <td rowspan="${fn:length(item.achieveList)}">${item.modelName}</td>
	              				 <td rowspan="${fn:length(item.achieveList)}">${item.totalscore}</td>
	              				 <td rowspan="${fn:length(item.achieveList)}">${item.crtvuScore}</td>
	              				 <td rowspan="${fn:length(item.achieveList)}">${item.getCredits}</td>
	              			</c:if>
	              			<td>${a.courseCode}</td>
	              			<td>${a.courseName}</td>
	              			<td>
	              				<c:if test="${a.courseNature=='0'}">必修</c:if> 
								<c:if test="${a.courseNature=='1'}">选修</c:if> 
								<c:if test="${a.courseNature=='2'}">补修</c:if>
	              			</td>
	              			<td>${a.courseType=="0"?"统设":"非统设"}</td>
	              			<td>${a.courseScore}</td>
	              			<td>${a.term}</td>
	              			<td>${a.examUnit}</td>
	              			<td>${a.examScore}</td>
	              			<c:if test="${i.index==0}">
	              				<c:choose>
	              					<c:when test="${item.getCredits>=item.totalscore}">
	              						<td rowspan="${fn:length(item.achieveList)}">
	              							<span class="text-green">已达标</span>
	              						</td>
	              					</c:when>
	              					<c:otherwise>
	              						<td rowspan="${fn:length(item.achieveList)}">
	              							<span class="text-red">未达标</span>
	              						</td>
	              					</c:otherwise>
	              				</c:choose>
	              			</c:if>
              			</tr>
              		</c:forEach>
              	</c:forEach>
              </tbody>
            </table>
		</div>
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
</div>
<script type="text/javascript">
$('.slim-Scroll').slimScroll({
    height: $(window).height()-126,
    size: '5px'
});

//关闭窗口
$('[data-role="close-pop"]').click(function(event) {
	parent.$.closeDialog(frameElement.api);
});
</script>
</body>
</html>