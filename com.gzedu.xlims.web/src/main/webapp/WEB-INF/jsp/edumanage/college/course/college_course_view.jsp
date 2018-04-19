<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<jsp:include page="/eefileupload/upload.jsp"/>

</head>
<body class="inner-page-body">
<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">课程管理</a></li>
		<li class="active">新建课程</li>
	</ol>
</section>
<section class="content">
	<div class="box no-margin">
	<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/edumanage/course/${action}" method="post">
		<div class="box-body pad-t15">
			<table class="table vertical-middle no-border table-fixed">
				<tbody>
					<tr>
						
						<th width="10%" class="text-right">
							课程代码
						</th>
						<td>
							${entity.kch }
						</td>
						
						<th width="10%" class="text-right">
							课程层次
						</th>
						<td>
							${pyccMap[entity.pycc]}
						</td>
					</tr>
					<tr>
						
						<th class="text-right">
							课程名称
						</th>
						<td>
							${entity.kcmc }
						</td>
						
						<th class="text-right">
							教学方式
						</th>
						<td>
							${wsjxzkMap[entity.wsjxzk]}
						</td>
					</tr>
					<tr>
						
						<th class="text-right">
							课程性质
						</th>
						<td>
							${courseNatureMap[entity.courseNature]}
						</td>
						
						<th class="text-right">
							课程标签
						</th>
						<td>
							${entity.label}
						</td>
					</tr>
					<th class="text-right">
						适用行业
					</th>
					<td>
						${syhyMap[entity.syhy]}
					</td>
						
						<th class="text-right">
							适用专业
						</th>
						<td>
							${syzyMap[entity.syzy]}
						</td>
					</tr>
					<tr>
						
						<th class="text-right">
							课程学分
						</th>
						<td>
							${entity.credit }
						</td>
						
						<th class="text-right">
							课程学时
						</th>
						<td>
							${entity.hour }
						</td>
					</tr>
					<tr>
						
						<th class="text-right">
							课程状态
						</th>
						<td>
							<c:if test="${entity.isEnabled == 1 or empty entity.isEnabled }">已启用</c:if>
							<c:if test="${entity.isEnabled == 0 }">未启用</c:if>
						</td>
					</tr>
				</tbody>
			</table>

			<hr>

			<table class="table vertical-middle no-border table-fixed">
				<tbody>
					<tr>
						<th width="10%" class="text-right">
							课程考核
						</th>
						<td>
							<c:if test="${entity.assessment == 1 or empty entity.assessment }">双考核</c:if>
		                    <c:if test="${entity.assessment == 2 }">达到分数合格</c:if>
		                    <c:if test="${entity.assessment == 3 }">完成必修活动</c:if>
		                    <c:if test="${entity.assessment == 4 }">无考核</c:if>
						</td>

						<th width="10%" class="text-right">
							
						</th>
						<td>
							
						</td>
					</tr>
					<tr>
						<th class="text-right qualified">
							合格分
						</th>
						<td class="qualified">
							${entity.qualified} 分
						</td>

						<th class="text-right activity">
							必修活动
						</th>
						<td class="activity">
							${entity.activity} 个
						</td>
					</tr>
					<tr class="khsm">
						<th class="text-right" style="vertical-align:top !important;">
							考核说明
						</th>
						<td colspan="3">
							${entity.khsm }
						</td>
					</tr>
					<tr>
						<th class="text-right" style="vertical-align:top !important;">
							<!--  -->课程简介
						</th>
						<td colspan="3">
							${entity.kcjj }
						</td>
					</tr>
					<tr>
						<th class="text-right" style="vertical-align:top !important;">
							主教材
						</th>
						<td colspan="3">
							<c:if test="${not empty entity.gjtTextbookList1 }">
								<c:forEach  items="${entity.gjtTextbookList1}" var="item">
									${item.textbookName}
							   </c:forEach>
			      			</c:if>
						</td>
					</tr>
					<tr>
						<th class="text-right" style="vertical-align:top !important;">
							复习资料
						</th>
						<td colspan="3">
							<c:if test="${not empty entity.gjtTextbookList2 }">
								<c:forEach  items="${entity.gjtTextbookList2}" var="item">
								 ${item.textbookName}
							   </c:forEach>
			      			</c:if>
						</td>
					</tr>
					<tr>
						<th class="text-right">
							课程封面
						</th>
						<td colspan="3">
							<div class="inline-block vertical-middle">
								<ul class="img-list clearfix">
									<li>
										<img id="imgId" src="${entity.kcfm }" class="user-image" style="cursor: pointer;" >
									</li>
								</ul>
							</div>
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
		</form>
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