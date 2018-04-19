<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body>
	<form id="listForm" action="${ctx}/home/class/newFeedback/findAskStudent" method="get">
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">选择提问学员</h3>
			</div>
			<div class="scroll-box">
				<div class="box-body">
					<div class="row">
						<div class="col-xs-5">
							<select  name="search_EQ_gjtGrade.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
								<option value="" selected="selected">全部学期</option>
								<c:forEach items="${gradeMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-xs-5 no-padding">
							<select name="search_EQ_gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected="selected">全部专业</option>
								<c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-xs-2">
							<button class="btn btn-block btn-primary" type="submit">搜索</button>
						</div>
					</div>
					<div class="row margin_t10">
						<div class="col-xs-5">
							<select name ="search_EQ_pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
								<option value="" selected="selected">全部层次</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
							</select>
						</div>
						<div class="col-xs-5 no-padding">
							<input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm }" placeholder="搜索姓名">
						</div>
					</div>
					<div class="row margin_t10">
						<div class="col-xs-5">
							<input type="text" class="form-control" name="search_EQ_xh" value="${param.search_EQ_xh }" placeholder="搜索学号">
						</div>
					</div>
					<table class="table-gray-th text-center margin_t15 f12">
						<thead>
							<tr>
								<th width="60">选择</th>
								<th width="80">姓名</th>
								<th width="160">学号</th>
								<th width="60">层次</th>
								<th width="90">学期</th>
								<th>专业</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
							<c:when test="${not empty page.content}">
								<c:forEach items="${page.content }" var="student">
							<tr>
								<td><input type="radio" name="r1" data-id="sel-item"
									data-json='{
								"studentId":"${student.studentId }",	
                                "studentName":"${student.xm }",
                                "studentNo":"${student.xh }",
                                "level":"${pyccMap[student.pycc]}",
                                "gradeName":"${student.gjtGrade.gradeName}",
                                "specialtyName":"${student.gjtSpecialty.zymc}"
                            }'>
								</td>
								<td>${student.xm }</td>
								<td><span style="word-break: break-all;"> ${student.xh } </span></td>
								<td>${pyccMap[student.pycc]}</td>
								<td>${student.gjtGrade.gradeName}</td>
								<td>${student.gjtSpecialty.zymc}</td>
							</tr>
						</c:forEach>
							</c:when>
								<c:otherwise>
									<tr>
										<td align="center" colspan="6">暂无数据</td>
									</tr>
								</c:otherwise>
						</c:choose>
					</table>
				</div>
				<div style="padding: 10px 0 10px 0;">
					<tags:pagination page="${page}" paginationSize="10" />
				</div>
			</div>
		</div>
		<div class="text-right pop-btn-box pad clearfix">
			<!--<button type="button" class="btn btn-primary min-width-90px pull-left" data-role="transfer-orient">转回原处（黄小米班主任）</button>-->

			<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
			<button type="button" class="btn btn-primary min-width-90px" data-role="sure">确定</button>
		</div>
	</form>
	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
//关闭 弹窗
$("[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//设置内容主体高度
$('.scroll-box').height($(frameElement).height()-106);

//确定
$('[data-role="sure"]').click(function(event) {
    var $ck=$('[data-id="sel-item"]:checked');
    if($ck.length>0){
        var $container=parent.$('[data-id="stu-box"]');
        var $container2=parent.$('[data-id="object-box"]');
        var obj=$ck.data('json');
        var template='<tr> <td> {1} <input type ="hidden" name="studentId" value="{0}" id="studentId"> </td><td>'
        			+'<span style="word-break: break-all;"> {2} </span> </td>'+
        			' <td>{3}</td> <td>{4}</td> <td>{5}</td>'+
        			' <td> <a href="#" data-role="remove">删除</a> </td></tr>';
        $container.html(
            template.format(
            	obj.studentId,
                obj.studentName,
                obj.studentNo,
                obj.level,
                obj.gradeName,
                obj.specialtyName
            )
        );
        $container2.empty();
        parent.$.closeDialog(frameElement.api);
        
    }
    else{
        parent.$.closeDialog(frameElement.api);
    }    
    
});
</script>


</body>
</html>
