<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业管理－课程列表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
    
</script>
<style type="text/css">
.table {
	margin-bottom: 0px
}
</style>
</head>
<body class="inner-page-body">

	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">教务管理</a>
			</li>
			<li>
				<a href="#">开设专业</a>
			</li>
			<li class="active">设置专业教学计划</li>
		</ol>
	</section>
	<section class="content">
		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>>
			<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>>
			<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="nav-tabs-custom">
			<c:if test="${param.action=='confirm'}">
				<ul class="nav nav-tabs nav-tabs-lg">
			      <li data-role="show"><a href="javascript:void(0)">1.设置专业规则</a></li>
			      <li data-role="show" class="active"><a href="javascript:void(0)">2.发布专业</a></li>
			    </ul>
		    </c:if>
			<div class="box-body">
				<h3 class="cnt-box-title f16 margin_b10">专业信息</h3>
				<div class="table-responsive">
					<input name="kkzy" value="${gjtSpecialty.specialtyId }" type="hidden" />
					<table class="table-gray-th text-center">
						<tr>
							<th>学期</th>
							<td colspan="5" style="text-align: left;padding-left:30px">${gjtGrade.gradeName}</td>
						</tr>
						<tr>
							<th width="13%">专业规则号</th>
							<td width="20%">${gjtSpecialty.ruleCode }</td>

							<th width="13%">专业名称</th>
							<td width="20%">${gjtSpecialty.zymc}</td>

							<th width="13%">学生类型</th>
							<td width="20%">${studentTypeMap[gjtSpecialty.xslx]}</td>
						</tr>
						<tr>
							<th>专业层次</th>
							<td>${pyccMap[gjtSpecialty.pycc]}</td>

							<th>学科</th>
							<td>${subjectMap[gjtSpecialty.subject]}</td>
							
							<th>学科门类</th>
							<td>${categoryMap[gjtSpecialty.category]}</td>
						</tr>
						<tr>
							<th>学制</th>
							<td>${gjtSpecialty.xz}</td>
							<th>专业类型</th>
							<td>
								<c:if test="${gjtSpecialty.type=='1' }">正式专业</c:if>
								<c:if test="${gjtSpecialty.type=='2' }">体验专业</c:if>
							</td>
							<th>适用行业</th>
							<td>${syhyMap[gjtSpecialty.syhy]}</td>
						</tr>
					</table>
				</div>
				
				<h3 class="cnt-box-title f16 margin_b10 margin_t20">学分信息</h3>
				<div class="table-responsive">
						<table class="table-gray-th text-center">
						<tr>
							<th width="13%">最低毕业学分</th>
							<td width="20%">${gjtSpecialty.zdbyxf}</td>
							<th width="13%">中央电大考试学分</th>
							<td width="20%">${gjtSpecialty.zyddksxf}</td>
							<th width="13%">必修学分</th>
							<td width="20%">${gjtSpecialty.bxxf}</td>
						</tr>
						</table>
				</div>

				<h3 class="cnt-box-title f16 margin_b10 margin_t20">课程模块学分</h3>
				<div class="table-responsive">
					<table class="table-gray-th text-center">
						<tr>
							<th>课程模块</th>
							<th>模块最低学分</th>
							<th>模块最低毕业学分</th>
							<th width="20%">模块中央电大考试最低学分</th>
							<th>已设置课程数</th>
							<th>已设置学分</th>
						</tr>
						<c:forEach items="${list}" var="obj">
							<tr class="modelRow">
								<td>
									${courseTypeMap[obj.id]}
								</td>
								<td>
									<fmt:formatNumber value="${obj.totalScore}" pattern="#" type="number"/>
								</td>
								<td>
									<fmt:formatNumber value="${obj.score}" pattern="#" type="number"/>
								</td>
								<td><fmt:formatNumber value="${obj.crtvuScore}" pattern="#" type="number"/></td>
								<td class="totalCourse_${obj.code}">0</td>
								<td class="totalCredit_${obj.code}">0</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<h3 class="cnt-box-title f16 margin_b10 margin_t20">教学计划</h3>
				<div data-role="sort-tbl-box">
					<div class="table-responsive">
						<table class="table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none">
							<thead class="with-bg-gray">
								<tr>
									<th width="7%" class="border-bottom-none">学期</th>
									<th class="no-padding border-none">
										<table class="table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none border-none border-none">
											<thead class="with-bg-gray">
												<tr>
													<th class="border-bottom-none border-left-none" width="7%">课程模块</th>
													<th class="border-bottom-none" width="8%">课程代码</th>
													<th class="border-bottom-none" width="5%">试卷号</th>
													<th class="border-bottom-none" width="7%">课程名称</th>
													<th class="border-bottom-none" width="6%">替换课程代码</th>
													<th class="border-bottom-none" width="5%">试卷号(替)</th>
													<th class="border-bottom-none" width="10%">替换课程</th>
													<th class="border-bottom-none" width="8%">发放教材</th>
													<th class="border-bottom-none" width="5%">学习方式</th>
													<th class="border-bottom-none" width="7%">课程性质</th>
													<th class="border-bottom-none" width="7%">课程类型</th>
													<th class="border-bottom-none" width="7%">考试单位</th>
													<th class="border-bottom-none" width="5%">学分</th>
													<th class="border-bottom-none" width="5%">学时</th>
												</tr>
											</thead>
										</table>
									</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach  begin="1" end="${kkxq}" var="xq">
									<tr>
										<td>第${xq}学期</td>
										<td class="no-padding border-none">
											<table class="table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none border-none">
												<tbody class="sort-order">
													<c:forEach items="${gjtTeachPlans}" varStatus="i" var="item">
														<c:if test="${item.kkxq==xq}">
															<tr class="planrow" dataId="${item.teachPlanId}">
																<td width="7%" class="border-left-none">
																	${courseTypeMap2[item.kclbm] }
																	<input type="hidden" class="model" value="${item.kclbm}" />
																</td>
																<td width="8%" class="border-left-none">
																	<c:if test="${not empty(item.gjtCourse) }">${item.gjtCourse.kch }</c:if>
																</td>
																
																<td width="5%">
																	${item.examNo}
																</td>
																
																<td width="7%">
																	<c:if test="${not empty(item.gjtCourse) }">
																		${item.gjtCourse.kcmc}<br />
																		<c:if test="${item.gjtCourse.isEnabled==1}">
																			<span class="text-green">（已启用）</span>
																		</c:if>
																		<c:if test="${item.gjtCourse.isEnabled==2}">
																			<span class="text-orange">（建设中）</span>
																		</c:if>
																		<c:if test="${item.gjtCourse.isEnabled==0}">
																			<span class="text-red">（暂无资源）</span>
																		</c:if>
																	</c:if>
																</td>
																<td width="6%">
																	<c:if test="${not empty(item.gjtReplaceCourse) }">
																		${item.gjtReplaceCourse.kch }
																	</c:if>
																</td>
																<td width="5%">
																	${item.replaceExamNo}
																</td>
																<td width="10%">
																	<c:if test="${not empty(item.gjtReplaceCourse) }">
																		${item.gjtReplaceCourse.kcmc } <br />
																			<c:if test="${item.gjtReplaceCourse.isEnabled==1}">
																				<span class="text-green">（已启用）</span>
																			</c:if>
																			<c:if test="${item.gjtReplaceCourse.isEnabled==2}">
																				<span class="text-orange">（建设中）</span>
																			</c:if>
																			<c:if test="${item.gjtReplaceCourse.isEnabled==0}">
																				<span class="text-red">（暂无资源）</span>
																			</c:if>
																	</c:if>
																</td>
																
																<td width="8%">
																	<c:if test="${empty(item.gjtTextbookList) }">
																		无
																	</c:if>
																	<c:forEach items="${item.gjtTextbookList}" var="book">
																		${book.textbookName} <br>
																		<span class="gray9">(${book.textbookCode})</span><br>
																	</c:forEach>
																</td>
																	<%--<td width="7%">
																 <c:if test="${item.grantData==0}">是</c:if>
																	<c:if test="${item.grantData==1}">否</c:if> 
																</td>--%>
																
																<td width="5%">
																	<c:if test="${item.learningStyle==0}">国开在线</c:if>
																	<c:if test="${item.learningStyle==1}">国开学习网</c:if>
																</td>
																<td width="7%">
																	<c:if test="${item.kcsx=='0'}">必修</c:if> 
																	<c:if test="${item.kcsx=='1'}">选修</c:if> 
																	<c:if test="${item.kcsx=='2'}">补修</c:if>
																</td>

																<td width="7%">
																	${item.courseType=="0"?"统设":"非统设"}
																</td>
																<td width="7%">
																	<c:if test="${item.ksdw=='1'}">省</c:if>
																	<c:if test="${item.ksdw=='2'}">中央</c:if>
																	<c:if test="${item.ksdw =='3' }">分校</c:if>
																</td>
																<td width="5%" class="credit">${item.xf }</td>
																<td width="5%">${item.xs }</td>
															</tr>
														</c:if>
													</c:forEach>
												</tbody>
											</table>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<c:if test="${param.action=='confirm'}">
					<div class="box-footer text-right margin_t30">
						<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="back-off">取消</button>
						<button type="button" class="btn btn-primary min-width-90px" data-role="sure">发布专业</button>
					</div>
				</c:if>
			</div>
		</div>

	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		$('[data-role="sure"]').click(function(){
			var postIngIframe = $.mydialog({
				id : 'dialog-1',
				width : 150,
				height : 50,
				backdrop : false,
				fade : false,
				showCloseIco : false,
				zIndex : 11000,
				content : '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
		    });
			$.get('${ctx}/edumanage/teachPlan/releaseSpecialty',{
				id:'${param.id}'
			},function(data){
				if(data.successful){
					location.href='${ctx}/edumanage/teachPlan/list';
				}else{
					alert(data.message);
					$.closeDialog(postIngIframe);
				}
			},'json');
		});

		//计算学分
		function calcCredit(){
			var creditObj= {};
			$('.planrow').each(function(){
				var model=$.trim($(this).find('.model').val());
				var totalCredit= 'totalCredit_'+ model;
				var totalCourse= 'totalCourse_'+ model;
				var credit=$(this).find('.credit').html();
				credit= $.trim(credit) == ''? 0: Number(credit);
				if(creditObj[totalCredit]){
				    creditObj[totalCredit] += credit;
				    creditObj[totalCourse] += 1;
				}else{
				    creditObj[totalCredit] = credit;
				    creditObj[totalCourse] = 1;
				}
			});
			for(var key in creditObj){
				$('.'+key).html(creditObj[key]);
			}
		}
		calcCredit();
    </script>
</body>
</html>