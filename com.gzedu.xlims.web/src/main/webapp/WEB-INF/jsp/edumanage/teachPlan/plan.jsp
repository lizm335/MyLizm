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
.termScoreCacl{text-align: left;color: #666;text-align: left !important;}
.termScoreCacl span,#planCalc span{color:orange;margin:0 1px}
#planCalc{font-weight: normal;font-size: 14px;color: #666;}
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
		<div class="nav-tabs-custom">
			<c:if test="${status==0}">
				<ul class="nav nav-tabs nav-tabs-lg">
			      <li data-role="show" class="active"><a href="javascript:void(0)">1.设置教学计划</a></li>
			      <li data-role="show"><a href="javascript:void(0)">2.发布教学计划</a></li>
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
							<td width="20%"><fmt:formatNumber value="${gjtSpecialty.zyddksxf}" pattern="#.##" type="number"/></td>
							<th width="13%">必修学分</th>
							<td width="20%"><fmt:formatNumber value="${gjtSpecialty.bxxf}" pattern="#.##" type="number"/></td>
						</tr>
						</table>
						<input id="zyddksxf" type="hidden" value="${gjtSpecialty.zyddksxf}" />
						<input id="bxxf" type="hidden" value="${gjtSpecialty.bxxf}" />
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
							<tr class="modelRow" code="${obj.code}">
								<td class="modelName">
									${courseTypeMap[obj.id]}
								</td>
								<td>
									<fmt:formatNumber value="${obj.totalScore}" pattern="#.##" type="number"/>
								</td>
								<td>
									<fmt:formatNumber value="${obj.score}" pattern="#.##" type="number"/>
									<input type="hidden" class="score" value="${obj.score}"/>
								</td>
								<td>
									<fmt:formatNumber value="${obj.crtvuScore}" pattern="#.##" type="number"/>
									<input type="hidden" class="crtvuScore" value="${obj.crtvuScore}"/>
								</td>
								<td class="totalCourse_${obj.code}">0</td>
								<td class="totalCredit_${obj.code}">0</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="clearfix margin_b10 margin_t20">
					<div class="pull-right">
						<a href="#" role="button" class="btn btn-default btn-sm margin_r10" data-role="add-semester"><i class="fa fa-fw fa-plus"></i> 新增学期</a>
					</div>
					<h3 class="cnt-box-title f16 text-bold ">
					教学计划
					<span id="planCalc">
						(本教学计划已设置 <span class="tnum">0</span>学期 共<span class="cnum">0</span>门课程,
						必修课程<span class="bxnum">0</span>门,
						选修课程<span class="xxnum">0</span>门,
						总学分<span class="zxf">0</span>分,
						必修学分<span  class="bxxf">0</span>分,
						选修学分<span  class="xxxf">0</span>分,
						中央电大考试学分<span  class="zyxf">0</span>分)
					</span>
					</h3>
				</div>
				
				<div data-role="sort-tbl-box">
					<div class="table-responsive">
						<table class="table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none">
							<thead class="with-bg-gray">
								<tr>
									<th width="7%" class="border-bottom-none">学期</th>
									<th class="no-padding border-none">
										<table class="table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none border-none border-none">
											<thead class="with-bg-gray">
												<tr id="heardTr">
													<th class="border-bottom-none border-left-none" width="7%">课程模块</th>
													<th class="border-bottom-none" width="7%">课程代码</th>
													<th class="border-bottom-none" width="7%">试卷号</th>
													<th class="border-bottom-none" >课程名称</th>
													<th class="border-bottom-none" width="7%">替换课程代码</th>
													<th class="border-bottom-none" width="7%">试卷号(替)</th>
													<th class="border-bottom-none" width="7%">替换课程</th>
													<th class="border-bottom-none" width="7%">发放教材</th>
													<%--<th class="border-bottom-none" width="7%">是否发放复习资料</th>--%>
													<th class="border-bottom-none" width="10%">学习方式</th>
													<th class="border-bottom-none" width="5%">课程性质</th>
													<th class="border-bottom-none" width="5%">课程类型</th>
													<th class="border-bottom-none" width="5%">考试单位</th>
													<th class="border-bottom-none" width="5%">学分</th>
													<th class="border-bottom-none" width="5%">学时</th>
													<th class="border-bottom-none border-right-none" width="8%">操作</th>
												</tr>
											</thead>
										</table>
									</th>
								</tr>
							</thead>
							<tbody id="planBody">
								<c:forEach  begin="1" end="${kkxq}" var="xq">
									<c:set var="editable" value="${status==0 || status==1 && xq > currentTerm}" />
									<!--  大于当前学期才可以编辑 -- status:${status} index:${xq} currentTerm:${currentTerm} editable:${editable} -->
									<tr class="termRow">
										<td>第${xq}学期</td>
										<td class="no-padding border-none termtd" kkxq="${xq}">
											<table class="table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none border-none">
												
												<tbody <c:if test="${editable }">class="sort-order"</c:if>>
													<c:forEach items="${gjtTeachPlans}" varStatus="i" var="item">
														<c:if test="${item.kkxq==xq}">
															<tr class="planrow" dataid="${item.teachPlanId}">
																<td class="border-left-none">
																	${courseTypeMap2[item.kclbm] }
																	<input type="hidden" class="model" value="${item.kclbm}" />
																	<input type="hidden" class="editable" value="${editable}" />
																</td>
																<td><c:if test="${not empty(item.gjtCourse) }">${item.gjtCourse.kch }</c:if></td>
																
																<td>
																	<%--<select &lt;%&ndash; <c:if test="${!editable}">disabled="disabled"</c:if> &ndash;%&gt; name="grantData"   class="form-control selectpicker">
																		<option <c:if test="${item.grantData==0}">selected="selected"</c:if> value="0">是</option>
																		<option <c:if test="${item.grantData==1}">selected="selected"</c:if>  value="1">否</option>
																	</select>--%>
																	<input type="text" name="examNo" class="form-control" placeholder="试卷号" value="${item.examNo}"/>
																</td>
																
																<td>
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
																		<input type="hidden" value="${item.gjtCourse.courseId }" class="courseId">
																	</c:if>
																</td>
																
																<td class="replaceKch">
																	<c:if test="${not empty(item.gjtReplaceCourse) }">
																		${item.gjtReplaceCourse.kch }
																	</c:if>
																</td>
																<td>
																	<c:if test="${not empty item.gjtReplaceCourse }">
																		<input type="text" name="replaceExamNo" class="form-control" placeholder="试卷号(替)" value="${item.replaceExamNo}"/>
																	</c:if>
																	<c:if test="${empty item.gjtReplaceCourse }">
																		<input type="hidden" name="replaceExamNo"/>
																	</c:if>
																	
																</td>
																<td>
																		<c:if test="${editable}">
																			<a href="${ctx}/edumanage/teachPlan/choiceReplaceCourseList?gradeSpecialtyId=${param.id}" notinid="true" data-role="select-pop">
																		</c:if>
																		<c:if test="${not empty(item.gjtReplaceCourse) }">
																			${item.gjtReplaceCourse.kcmc } <br>
																			<c:if test="${item.gjtReplaceCourse.isEnabled==1}">
																				<span class="text-green">（已启用）</span>
																			</c:if>
																			<c:if test="${item.gjtReplaceCourse.isEnabled==2}">
																				<span class="text-orange">（建设中）</span>
																			</c:if>
																			<c:if test="${item.gjtReplaceCourse.isEnabled==0}">
																				<span class="text-red">（暂无资源）</span>
																			</c:if>
																			<input type="hidden" class="replaceCourseId" value="${item.gjtReplaceCourse.courseId}" />
																		</c:if>
																		<c:if test="${empty(item.gjtReplaceCourse) && editable}">
																			+选择课程
																		</c:if>
																		<c:if test="${editable}">
																			</a>
																		</c:if>
																</td>
																
																<td>
																	<c:if test="${editable}"></c:if>
																		<a href="${ctx}/edumanage/teachPlan/choiceTextbookList?planId=${item.teachPlanId}"  data-role="select-pop">
																	<c:if test="${not empty(item.gjtTextbookList)}">
																	<c:forEach items="${item.gjtTextbookList}" var="book">
																		${book.textbookName} <br>
																		<span class="gray9">(${book.textbookCode})</span><br>
																		<input type="hidden" value="${book.textbookId}" class="textbookId" />
																	</c:forEach>
																	</c:if>
																	<c:if test="${empty(item.gjtTextbookList) }"><!-- && editable -->
																		+选择教材
																	</c:if>
																	<c:if test="${editable}"></c:if>
																		</a>
																	
																</td>
																
																<td>
																	<select <%-- <c:if test="${!editable}">disabled="disabled"</c:if> --%> name="learningStyle"   class="form-control selectpicker">
																		<option <c:if test="${item.learningStyle==0}">selected="selected"</c:if> value="0">国开在线</option>
																		<option <c:if test="${item.learningStyle==1}">selected="selected"</c:if>  value="1">国开学习网</option>
																	</select>
																</td>
																<td>
																	<c:if test="${item.kcsx=='0'}">必修</c:if> 
																	<c:if test="${item.kcsx=='1'}">选修</c:if> 
																	<c:if test="${item.kcsx=='2'}">补修</c:if>
																	<input type="hidden" class="kcsx" value="${item.kcsx}" />
																</td>
																<td>
																	${item.courseType=="0"?"统设":"非统设"}
																</td>
																<td>
																	<c:if test="${item.ksdw =='1' }">省</c:if>
																	<c:if test="${item.ksdw =='2' }">中央</c:if>
																	<c:if test="${item.ksdw =='3' }">分校</c:if>
																	<input type="hidden" class="ksdw" value="${item.ksdw}" />
																</td>

																<td class="credit">${item.xf}</td>
																<td>${item.xs }</td>
																<td class="border-right-none">
																	<c:if test="${editable}">
																		<a href="#" class="operion-item drop-holder" data-toggle="tooltip" title="拖动课程到其他学期">
																			<i class="fa fa-arrows"></i>
																		</a> 
																		<c:if test="${item.ksdw==3 || status==0 }">
																			<a href="javascript:void(0);" class="operion-item operion-del" data-role="remove"  title="删除" data-tempTitle="删除">
																				<i class="fa fa-fw fa-trash-o"></i>
																			</a>
																		</c:if>
																	</c:if>
																</td>
															</tr>
														</c:if>
													</c:forEach>
												</tbody>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="2" class="termScoreCacl">
											本学期已设置<span class="cnum">0</span>门课程，必修课程<span class="bxnum">0</span>门,选修课程<span class="xxnum">0</span>门,总学分<span class="zxf">0</span>分,必修学分<span  class="bxxf">0</span>分,选修学分<span  class="xxxf">0</span>分,中央电大考试学分<span  class="zyxf">0</span>分
										</td>
									</tr>
									<tr>
						      			<td colspan="2" align="center">
							      			<c:if test="${editable }">
							      				<a href="choiceCourseList?id=${param.id}&specialtyId=${gjtSpecialty.specialtyId}&gradeId=${gjtGrade.gradeId}&term=${xq}" role="button" class="btn btn-block text-green nobg margin_t5" data-role="add-course"><i class="fa fa-fw fa-plus"></i>添加课程</a>
							      			</c:if>
						      			</td>
					      			</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="box-footer text-right margin_t30">
					<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="back-off">取消</button>
					<button type="button" class="btn btn-primary min-width-90px" data-role="sure">
						<c:if test="${status==0}">
							保存，进入下一步
						</c:if>
						<c:if test="${status==1}">
							保存
						</c:if>
					
					</button>
				</div>
			</div>
	    </div>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		//是否同步后台
		var synchro = ${status==0};
		var isChangeCourse=false;
		//新增课程
		$("body").on('click', '[data-role="view-more"]', function(event) {
		    event.preventDefault();
		    $.mydialog({
			id : 'view-more',
			width : 900,
			height : $(window).height() * 0.9,
			zIndex : 11000,
			content : 'url:' + $(this).attr('href')
		    });
		});

		function update(planId, kkxq) {
		    $.post('${ctx}/edumanage/teachPlan/updatePlan', {
			planId : planId,
			kkxq : kkxq
		    }, function(data) {
			if (data.successful) {
			} else {
			    alert(data.message);
			    location.reload();
			}
		    }, 'json');

		}

		// 排序
		function sortableFn() {
		    $(".sort-order").sortable({
			containment : '[data-role="sort-tbl-box"]',
			placeholder : "sort-highlight",
			connectWith : ".sort-order",
			handle : ".drop-holder",
			forcePlaceholderSize : true,
			zIndex : 999999,
			revert : 100,
			update : function(event, ui) {
			    var $sender = ui.sender;
			    var $item = ui.item;
			    console.log(ui);
			    // $('[data-toggle="tooltip"]',$item).tooltip('hide')
			    if ($sender) {// 只有移动到另一学期才执行
					resetTable();
					if(synchro)
						update($item.attr('dataid'), $item.closest('td').attr('kkxq'));
			    }
			}
		    }).disableSelection();
		}

		// 删除计划
		$(document).on('click', '[data-role="remove"]', function(event) {
		    var $this = $(this);
		    var id = $(this).closest('tr').attr('dataid');
		    var len = $(this).closest('tr').siblings().length;
		    $this.closest('tr').remove();
		    isChangeCourse=true;
		    resetTable();
		    if(!synchro) return;
		    $.post('${ctx}/edumanage/teachPlan/removePlan', {
				planId : id
		    }, function(data) {
				if (data.successful) {
	
				} else {
				    alert(data.message);
				    location.reload();
				}
		    }, "json");
		});

		var $select_pop;
		var mydialog;
		// 新增课程
		$("body").on('click', '[data-role="add-course"]', function(event) {
		    event.preventDefault();
		    $select_pop=$(this);
		    var url='url:' + $(this).attr('href');
		   	if(!synchro){
				var notInId =[];
				$('.planrow').each(function() {
					notInId.push($(this).find('.courseId').val());
				});
				if(notInId.length>0){
				    url+='&notInId='+notInId.join(',');
				}
			}
		    $(".tea-plan-box").removeClass('on');
		    $(this).parent().addClass('on');
		    mydialog=$.mydialog({
				id : 'add-course',
				width : '90%',
				height : 700,
				zIndex : 11000,
				content : url
		    });
		});

		// 选择替换课程 或 选择教材
		$('body').on('click','[data-role="select-pop"]',function(event){
		    event.preventDefault();
		    var url = $(this).attr("href");
		    if ($(this).attr('notinid')&& !synchro) {
				var notInIds = [];
				$('.planrow').each(function() {
				    if ($(this).find('.replaceCourseId').val())
					notInIds.push($(this).find('.replaceCourseId').val());
				});
				if (notInIds.length > 0) {
				    url += '&notInCoursIds=' + notInIds.join(',')
				}
		    }
		    $select_pop = $(this);
		    mydialog = $.mydialog({
			id : 'select-pop',
			width : '90%',
			height : 700,
			zIndex : 11000,
			content : 'url:' + url
		    });
		});
		// 选择课程后回调
		function choiceCourseCallback(courses){
		    $.closeDialog(mydialog);
		    isChangeCourse=true;
		    if(courses.length==0) return;
		    var courseIds=[];
		    var html=[];
		    var $termtd=$select_pop.closest('tr').prev().prev().find('.termtd');
		    $.each(courses,function(index,obj){
				html.push($('#planTemp').html().format(obj));
				courseIds.push(obj.courseId);
			});
		    $termtd.find('tbody').append(html.join(''));
		    $('[name="grantData"]').selectpicker('refresh');
		    $('[name="learningStyle"]').selectpicker('refresh');
		    if(!synchro){
				$termtd.find('.planrow').each(function(){
					if($.trim($(this).find('.ksdw').val())!=3){
						$(this).find('[data-role="remove"]').remove();
					}
				});
			}
		    resetTable();
		    if(synchro){
				$.post('${ctx}/edumanage/teachPlan/addCourse',{
				    courseIds: courseIds,
				    id:'${param.id}',
				    term: $termtd.attr('kkxq')
				},function(data){
					if(data.successful){
					    $termtd.find('.planrow').each(function(){
							if($.trim($(this).attr('dataid'))==''){
								var courseId=$(this).find('.courseId').val();
							    $(this).attr('dataid',data.obj[courseId]);
							}
						});
					}else{
						alert(data.message);
					}
				},'json');
			}
		}

		// 选择替换课程后回调
		function choiceReplaceCourseCallback(courseId, name, code, isEnable) {
		    $.closeDialog(mydialog);
		    isChangeCourse=true;
		    if (courseId) {
				var enableText;
				if (isEnable == '1') {
				    enableText = '<span class="text-green">（已启用）</span>';
				} else if(isEnable == '0'){
				    enableText = '<span class="text-orange">（建设中）</span>';
				}else if(isEnable == '2'){
				    enableText = '<span class="text-red">（暂无资源）</span>';
				}
				var html = [ name, '<br />', enableText, '<input type="hidden" class="replaceCourseId" value="' + courseId + '" />' ];
				$select_pop.html(html.join(''));
				$select_pop.closest('tr').find('.replaceKch').text(code);
		    } else {
				$select_pop.html('+选择课程');
				$select_pop.closest('tr').find('.replaceKch').empty();
		    }

		    if(synchro){
				$.get('${ctx}/edumanage/teachPlan/updateReplaceCourse', {
					planId : $select_pop.closest('tr').attr('dataid'),
					courseId : courseId
				  }, function(data) {
					if (data.successful) {
					   // location.reload();
					} else {
					    alert(data.message);
					    location.reload();
					}
			  }, 'json');
			}
		}

		// 选择教材后回调
		function choiceTextbookCallback(textbooks) {
		    $.closeDialog(mydialog);
		    var textbookIds = [];
		    if (textbooks.length > 0) {
			var html = [];
			$.each(textbooks, function(index, tb) {
			    html.push(tb.name);
			    html.push('<br>');
			    html.push('<span class="gray9">(' + tb.code + ')</span>');
			    html.push('<input type="hidden" value="' + tb.id + '" class="textbookId" />');
			    textbookIds.push(tb.id);
			});
				$select_pop.html(html.join(''));
		    } else {
				$select_pop.html('+选择教材');
		    }

		    if (synchro) {
				$.post('${ctx}/edumanage/teachPlan/updateTextbook', {
				    planId : $select_pop.closest('tr').attr('dataid'),
				    textbookIds : textbookIds
				}, function(data) {
				    if (data.successful) {
					
				    } else {
						alert(data.message);
						location.reload();
				    }
				}, 'json');
		    }
		}

		$('body').on('click','[data-role="exchange"]',function(){
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
		    var planId=$(this).closest('tr').attr('dataid');
		    $tr = $(this).closest('tr');
		    $.get('${ctx}/edumanage/teachPlan/exchangeCourse',{
				planId:planId
			 },function(data){
				if(data.successful){
					location.reload();
				}else{
				    $.closeDialog(postIngIframe);
				    alert(data.message);
				}
			 },'json');
		});
		
		function validForm(){
		    var flag = true;
		    $('.termRow').not(':last').each(function() {
				if ($(this).find('[data-role="empty-tr"]').length > 0) {
				    return flag = false;
				}
		    });
		    if (!flag) {
				$.alertDialog({
			            width:300,
			            height:200,
			            content:'部分学期还未添加课程',
			            cancel:false,
			            okLabel:'关闭',
			            okCss:'btn btn-normal btn-gray min-width-100'
					});
				return false;
		    }
		    var requiredCredit=0;
		    var examScore=0;
		    $('.planrow').each(function() {
				var credit=$.trim($(this).find('.credit').text());
				credit= credit=='' ? 0 : parseFloat(credit);
				if($(this).find('.kcsx').val()==0){
				    requiredCredit+=credit;
				}
				if($(this).find('.ksdw').val()==2){
				    examScore+=credit;
				}
			});
			if(parseFloat($('#bxxf').val())!=requiredCredit){
				$.alertDialog({
			            width:300,
			            height:200,
			            content:'设置必修课程的总学分必须等于专业的必修学分',
			            cancel:false,
			            okLabel:'关闭',
			            okCss:'btn btn-normal btn-gray min-width-100'
					});
				console.log('必修学分已设置'+requiredCredit);
				return false;
			}
			if(parseFloat($('#zyddksxf').val())>examScore){
			    $.alertDialog({
			            width:300,
			            height:200,
			            content:'考试单位为中央的课程总学分应不少于中央电大考试学分',
			            cancel:false,
			            okLabel:'关闭',
			            okCss:'btn btn-normal btn-gray min-width-100'
					});
			    console.log('中央电大考试学分已设置'+examScore);
				return false;
			}
			$('.modelRow').each(function(){
				var code =$(this).attr('code');
				var score=$(this).find('.score').val();
				var crtvuScore=$(this).find('.crtvuScore').val();
				var modelName=$(this).find('.modelName').text();
				var totalCredit=0;
				var crtvuCredit=0;
				$('.planrow').each(function(index,el){
					if($(el).find('.model').val()==code){
					    var credit=$.trim($(this).find('.credit').text());
						credit= credit=='' ? 0 : parseFloat(credit);
						totalCredit += credit;
						if($(el).find('.ksdw').val()==2){
						    crtvuCredit+=credit;
						}
					}
				});
				if(totalCredit<parseFloat(score)){
					var txt='课程模块【' + modelName + '】<br/>下的课程总学分不能少于模块最低毕业学分';
					$.alertDialog({
			            width:300,
			            height:200,
			            content:txt,
			            cancel:false,
			            okLabel:'关闭',
			            okCss:'btn btn-normal btn-gray min-width-100'
					});
					return flag=false;
				}
				if(crtvuCredit<parseFloat(crtvuScore)){
				    var txt='课程模块【' + modelName + '】<br/>考试单位为中央的课程总学分不能少于模块中央电大考试最低学分';
				    $.alertDialog({
				            width:300,
				            height:200,
				            content:txt,
				            cancel:false,
				            okLabel:'关闭',
				            okCss:'btn btn-normal btn-gray min-width-100'
						});
					return flag=false;
				}
			});
			return flag;
		}
		
		$('[data-role="sure"]').click(function() {
		    if(!validForm()){
				return;
			}
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
		    if(synchro){
			location.href = '${ctx}/edumanage/teachPlan/plan?id=${param.id}&specialtyId=${gjtSpecialty.specialtyId}&gradeId=${gjtGrade.gradeId}&action=confirm';
				/* $.get('${ctx}/edumanage/teachPlan/validaTeachPlan', {
					id : '${param.id}',
					specialtyId : '${gjtSpecialty.specialtyId}'
			    }, function(data) {
					if (data.successful) {
					    location.href = '${ctx}/edumanage/teachPlan/plan?id=${param.id}&specialtyId=${gjtSpecialty.specialtyId}&gradeId=${gjtGrade.gradeId}&action=confirm';
					} else {
					    $.closeDialog(postIngIframe);
					    alert(data.message);
					}
			    }, 'json'); */
			}else{
				var plans=[];
				$('.planrow').each(function(){
					var plan={};
					plan.planId=$(this).attr('dataid');
					plan.term=Number($(this).closest('.termtd').attr('kkxq'));
					plan.courseId=$(this).find('.courseId').val();
					plan.replaceCourseId=$(this).find('.replaceCourseId').val();
					plan.update=$(this).find('.editable').val()==='false'?false:true;
					var textbookIds=[];
					$(this).find('.textbookId').each(function(){
					    textbookIds.push($(this).val());
					});
					plan.textbookIds=textbookIds;
                    // plan.grantData=Number($(this).find('[name="grantData"]').val());
                    plan.examNo=$(this).find('[name="examNo"]').val();
                    plan.replaceExamNo=$(this).find('[name="replaceExamNo"]').val();
					plan.learningStyle=Number($(this).find('[name="learningStyle"]').val());
					plans.push(JSON.stringify(plan));
				});
				$.post('${ctx}/edumanage/teachPlan/updateTeachPlan', {
					id : '${param.id}',
					teachPlans : plans
			    }, function(data) {
					if (data.successful) {
					    postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>');
					    initPlanStuRec();
					    setTimeout(function(){
						 location.href='${ctx}/edumanage/teachPlan/list';
					    },500);
					} else {
					    $.closeDialog(postIngIframe);
					    alert(data.message);
					}
			    }, 'json');
			}
		    
		});

		function initPlanStuRec(){
			if(isChangeCourse){
			    $.get('${ctx}/api/openclass/initPlanStuRec',{GRADE_SPECIALTY_ID:'${param.id}'});
			}
		}

		// 计算学分
		function calcCredit() {
		    var creditObj = {};
		    var bxnum=0;//必修课程
		    var xxnum=0;//选修课程
		    var zxf=0;//总学分
		    var bxxf=0;//必修学分
		    var xxxf=0;//选修学分
		    var zyxf=0;//中央电大考试学分
		    
		    $('.planrow').each(function() {
				var model = $.trim($(this).find('.model').val());
				var totalCredit = 'totalCredit_' + model;
				var totalCourse = 'totalCourse_' + model;
				var credit = $(this).find('.credit').text();
				credit = $.trim(credit) == '' ? 0 : parseFloat(credit);
				if (creditObj[totalCredit]) {
				    creditObj[totalCredit] += credit;
				    creditObj[totalCourse] += 1;
				} else {
				    creditObj[totalCredit] = credit;
				    creditObj[totalCourse] = 1;
				}
			
				if($(this).find('.kcsx').val()==0){//必修
				    bxnum+=1;
				    bxxf+=credit;
				}else if($(this).find('.kcsx').val()==1){//选修
				    xxnum+=1;
				    xxxf+=credit;
				}
				zxf+=credit;
				if($(this).find('.ksdw').val()==2){//中央
				    zyxf+=credit;
				}
		    });
		    for ( var key in creditObj) {
				$('.' + key).html(creditObj[key]);
		    }
		    //教学计划学分统计
			$('#planCalc .tnum').text($('.termRow').length);
			$('#planCalc .cnum').text($('.planrow').length);
			$('#planCalc .bxnum').text(bxnum);
			$('#planCalc .xxnum').text(xxnum);
			$('#planCalc .zxf').text(zxf);
			$('#planCalc .bxxf').text(bxxf);
			$('#planCalc .xxxf').text(xxxf);
			$('#planCalc .zyxf').text(zyxf);

			//每个学期学分统计
			$('.termtd').each(function(){
				var bxnum=0;//必修课程
				var xxnum=0;//选修课程
				var zxf=0;//总学分
				var bxxf=0;//必修学分
				var xxxf=0;//选修学分
				var zyxf=0;//中央电大考试学分
				$(this).find('.planrow').each(function(){
				    var credit = $(this).find('.credit').text();
					credit = $.trim(credit) == '' ? 0 : parseFloat(credit);
				    if($(this).find('.kcsx').val()==0){//必修
					    bxnum+=1;
					    bxxf+=credit;
					}else if($(this).find('.kcsx').val()==1){//选修
					    xxnum+=1;
					    xxxf+=credit;
					}
					zxf+=credit;
					if($(this).find('.ksdw').val()==2){//中央
					    zyxf+=credit;
					}
				});
				var $termScoreCacl=$(this).closest('tr').next();
				$termScoreCacl.find('.cnum').text($(this).find('.planrow').length);
				$termScoreCacl.find('.bxnum').text(bxnum);
				$termScoreCacl.find('.xxnum').text(xxnum);
				$termScoreCacl.find('.zxf').text(zxf);
				$termScoreCacl.find('.bxxf').text(bxxf);
				$termScoreCacl.find('.xxxf').text(xxxf);
				$termScoreCacl.find('.zyxf').text(zyxf);
			});

			
			
		}

		function sortFun(){
		    $('.planrow').each(function(index,obj){
				console.log(obj);
			});
		}
		
		$('[data-role="add-semester"]').click(function(event) {
		    window.scrollTo(0, document.body.scrollHeight);
		    if ($('.sort-order:last').children('[data-role="empty-tr"]').length > 0) {
			return;
		    }
		    var last = $('.termtd:last').attr('kkxq');
		    last = $.trim(last) == '' ? 0 : Number(last);
		    var obj = {
			'term' : (last + 1)
		    };
		    var html = $('#termTemp').html().format(obj);
		    $('#planBody').append(html);
		    resetTable();
		});

		function resetTable(){
		    $(".termtd tbody").each(function() {
				if ($(this).children().length == 0) {
				    $(this).append('<tr data-role="empty-tr"><td>&nbsp;<br>&nbsp;</td></tr>');
				}else if($(this).children('.planrow').length>0){
				    $(this).find('[data-role="empty-tr"]').remove();
					$(this).find('tr:first td').each(function(index,el){
						$(el).attr('width',$('#heardTr th').eq(index).attr('width'))
					});
				}
		    });
		    sortableFn();
		    calcCredit();
		}

	

		$('body').on('change', '[name="grantData"]', function() {
			if(!synchro) return;
		    $.get('${ctx}/edumanage/teachPlan/updateGrantData', {
				planId : $(this).closest('tr').attr('dataid'),
				grantData : $(this).val()
		    }, function(data) {
				if (data.successful) {
				    // location.reload();
				} else {
				    alert(data.message);
				    location.reload();
				}
		    }, 'json');
		});

		$('body').on('change', '[name="learningStyle"]', function() {
			if(!synchro) return;
		    $.get('${ctx}/edumanage/teachPlan/updateLearningStyle', {
				planId : $(this).closest('tr').attr('dataid'),
				learningStyle : $(this).val()
		    }, function(data) {
				if (data.successful) {
				    // location.reload();
				} else {
				    alert(data.message);
				    location.reload();
				}
		    }, 'json');
		});

		$('body').on('change', '[name="courseCategory"]', function() {
			if(!synchro) return;
		    $.get('${ctx}/edumanage/teachPlan/updateCourseCategory', {
				planId : $(this).closest('tr').attr('dataid'),
				category : $(this).val()
		    }, function(data) {
				if (data.successful) {
				    // location.reload();
				} else {
				    alert(data.message);
				    location.reload();
				}
		    }, 'json');
		});
		resetTable();
    </script>

	<script type="template" id="termTemp">
		<tr class="termRow">
			<td>第{term}学期</td>
			<td class="no-padding border-none termtd" kkxq="{term}">
				<table class="table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none border-none">
					<tbody class="sort-order ui-sortable">
						<tr data-role="empty-tr">
							<td>&nbsp;<br>&nbsp;</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="termScoreCacl">
				本学期已设置<span class="cnum">0</span>门课程，必修课程<span class="bxnum">0</span>门,选修课程<span class="xxnum">0</span>门,总学分<span class="zxf">0</span>分,必修学分<span  class="bxxf">0</span>分,中央电大考试学分<span  class="zyxf">0</span>分
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
			<a href="choiceCourseList?id=${param.id}&specialtyId=${gjtSpecialty.specialtyId}&gradeId=${gjtGrade.gradeId}&term={term}" role="button" class="btn btn-block text-green nobg margin_t5" data-role="add-course"><i class="fa fa-fw fa-plus"></i>添加课程</a>
			</td>
		</tr>
	</script>
	<script type="template" id="planTemp">
		<tr class="planrow" dataid="">
			<td class="border-left-none">
				{kclbm}
				<input type="hidden" class="model" value="{courseTypeId}" />
			</td>
			<td>{kch}</td>
			<td>
				{kcmc}
				<input type="hidden" value="{courseId}" class="courseId">
			</td>
			<td class="replaceKch">
			</td>
			<td>
				<a href="${ctx}/edumanage/teachPlan/choiceReplaceCourseList?gradeSpecialtyId=${param.id}" notinid="true" data-role="select-pop">
					+选择课程
				</a>
			</td>
			<td>
				<a href="${ctx}/edumanage/teachPlan/choiceTextbookList" data-role="select-pop">
					+选择教材
				</a>
			</td>
			<td>
				<select name="grantData" class="form-control selectpicker">
					<option value="0">是</option>
					<option value="1">否</option>
				</select>
			</td>
			<td>
				<select  name="learningStyle"   class="form-control selectpicker">
					<option value="0">国开在线</option>
					<option value="1">国开学习网</option>
				</select>
			</td>
			<td>
				{kcsxText}
				<input type="hidden" class="kcsx" value="{kcsx}" />
			</td>
			<td>{courseType}</td>
			<td>
				{ksdwText}
				<input type="hidden" class="ksdw" value="{ksdw}" />
			</td>
			<td class="credit">{xf}</td>
			<td>{xs}</td>
			<td class="border-right-none">
				<a href="#" class="operion-item drop-holder" data-toggle="tooltip" title="拖动课程到其他学期">
					<i class="fa fa-arrows"></i>
				</a>
				<a href="javascript:void(0);" class="operion-item operion-del" data-role="remove"  title="删除" data-tempTitle="删除">
					<i class="fa fa-fw fa-trash-o"></i>
				</a>
			</td>
		</tr>
	</script>
</body>
</html>