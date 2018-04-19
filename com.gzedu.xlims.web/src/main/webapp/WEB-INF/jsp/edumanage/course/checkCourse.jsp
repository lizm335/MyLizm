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
<body class="inner-page-body">

	<section class="content-header clearfix">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="homepage.html"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">教学管理</a>
			</li>
			<li>
				<a href="#">课程管理</a>
			</li>
			<li class="active">验收课程</li>
		</ol>
	</section>
	<section class="content">
		<div class="box no-margin">
			<div class="box-body">
				<table class="table-gray-th text-center margin_b15">
					<tbody>
						<tr>
							<th width="14%">课程名称：</th>
							<td width="14%">${info.kcmc }<input type="hidden" value="${info.courseId}" id="courseId"> <input type="hidden"
								value="${totalSection}" id="totalSection">
							</td>

							<th width="14%">原始课程状态：</th>
							 <c:if test="${originalSatus==1}">
								<td class="text-green">已全部发布 </td>
							</c:if>
							 <c:if test="${originalSatus==2}">
							 	<td class="text-orange">已部分发布</td>
							 </c:if>
							<th width="14%">验收人：</th>
							<td width="14%">${info.checkUser }</td>

							<td rowspan="2"><a href="${oclassHost}/processControl/previewCourse.do?formMap.COURSE_ID=${info.courseId}" target="_blank"
								class="btn btn-warning" title="检查课程内容">检查课程内容 </a></td>
						</tr>

						<tr>
							<th>课程代码：</th>
							<td>${info.kch }</td>

							<th>课程上线状态：</th>
							<td><c:if test="${info.isEnabled==1}">
									<font class="text-green">已全部启用 </font>
								</c:if> <c:if test="${info.isEnabled==5}">
									<font class="text-green">部分启用(${totalIsEn }/${totalSection })</font>
								</c:if> <c:if test="${info.isEnabled==0}">
									<font class="gray9">暂无资源</font>
								</c:if> <c:if test="${info.isEnabled==2}">
									<font class="text-orange">建设中</font>
								</c:if> <c:if test="${info.isEnabled==4}">
									<font class="text-red">验收不通过</font>
								</c:if> <c:if test="${info.isEnabled==3}">
									<font class="text-orange">待验收</font>
								</c:if></td>

							<th>验收时间：</th>
							<td><fmt:formatDate value="${info.checkDt }" type="both" /></td>
						</tr>
					</tbody>
				</table>

				<div class="box-border">
					<ul class="nav nav-tabs bg-f2f2f2 f16">
						<c:forEach items="${courseStageList }" var="list" varStatus="var">
							<li <c:if test="${var.first }"> class="active" </c:if> style="margin: -1px;">
								<a href="#tab_top_${var.index+1 }" data-toggle="tab" class="flat gray">${list.PERIOD_NAME }</a>
							</li>
						</c:forEach>
						<!-- 		<li class="active" style="margin:-1px;"><a href="#tab_top_1" data-toggle="tab" class="flat gray">① 基础学习（20分）</a></li>
					<li style="margin:-1px;"><a href="#tab_top_2" data-toggle="tab" class="flat gray">② 重难点学习（80分）</a></li>
					<li style="margin:-1px;"><a href="#tab_top_3" data-toggle="tab" class="flat gray">③ 拓展学习（选修）</a></li> -->
					</ul>
					<div class="tab-content">
						<c:forEach items="${courseStageList }" var="list" varStatus="var">
							<div class="tab-pane <c:if test="${var.first }">active</c:if>" id="tab_top_${var.index+1 }">
								<table class="table text-center vertical-middle margin-bottom-none">
									<c:forEach items="${list.courseStageArea }" var="sunList">
										<thead>
											<tr>
												<th width="20" class="no-pad-right text-left"><i class="fa fa-fw fa-bars"></i></th>
												<th>
													<div class="text-left">${sunList.AREA_NAME }</div>
												</th>
												<th>制作时间</th>
												<th>制作状态</th>
												<th>验收状态</th>
												<c:if test="${action ne 'view' }">
													<th width="20%">操作</th>
												</c:if>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${sunList.courseSection }" var="grandsonList">
												<tr>
													<td class="no-pad-right"></td>
													<td>
														<div class="text-left">${grandsonList.TASK_NAME }</div>
													</td>
													<td>${grandsonList.CREATED_DT }</td>
													<td>
														<c:choose>
															<c:when test="${grandsonList.DO_FINISH eq 'Y' }">
																<font class="text-green"> 已发布 </font>
															</c:when>
															<c:otherwise>
																<font class="text-red"> 未发布</font>
															</c:otherwise>
														</c:choose>
													</td>
													<td>
														<c:choose>
															<c:when test="${grandsonList.DO_FINISH eq 'Y' }">
																<c:if test="${grandsonList.IS_CHECK eq '1' }">
																	<font class="text-green"> 已验收</font>
																</c:if>
																<c:if test="${grandsonList.IS_CHECK eq '0' }">
																	<font class="text-red"> 待验收</font>
																</c:if>
																<c:if test="${grandsonList.IS_CHECK eq '2' }">
																	<font class="text-red"> 验收不通过</font>
																</c:if>
															</c:when>
														<c:otherwise>
															--
														</c:otherwise>
													</c:choose>
													</td>
													<c:if test="${action ne 'view' }">
														<td><c:choose>
																<c:when test="${grandsonList.DO_FINISH eq 'Y' and grandsonList.IS_CHECK ne '1'  }">
																	<div class="radio-group">
																		<label class="text-no-bold margin_r10 margin-bottom-none"> 
																		<input type="radio" data-id="${grandsonList.TASK_ID }"
																			name="r${grandsonList.TASK_ID }" data-value="1" checked> 验收通过
																		</label> 
																		<label class="text-no-bold margin_r10 margin-bottom-none"> 
																			<input type="radio" name="r${grandsonList.TASK_ID }"data-value="0"
																			data-id="${grandsonList.TASK_ID }" > 验收不通过
																		</label>
																	</div>
																</c:when>
																<c:otherwise>
																	--
																</c:otherwise>
															</c:choose></td>
													</c:if>
												</tr>
											</c:forEach>
										</tbody>
									</c:forEach>
								</table>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
			<c:if test="${isCheckBtn and action ne 'view'}">
				<div class="box-footer text-right">
					<button type="button" class="btn btn-primary" style="width: 120px;" data-role="sure">确认验收</button>
				</div>
			</c:if>
			<c:if test="${action eq 'view'}">
				<div class="panel panel-default flat">
					<div class="panel-heading">
						<h3 class="panel-title">验收记录</h3>
					</div>
					<div class="panel-body">
						<div class="approval-list clearfix margin_b20">
							<!-- 状态2 -->
							<c:forEach items="${checkList }" var="checkItem" varStatus="var">
								<c:choose>
									<c:when test="${var.first }">
										<dl class="approval-item">
											<dt class="clearfix">
												<b class="a-tit gray6">提交验收</b>
												<span class="gray9 text-no-bold f12">
													课程制作老师 ： ${checkItem.createdBy } 
													<fmt:formatDate value="${checkItem.createdDt}" type="both" />
												</span>
												<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
											</dt>
										</dl>
									</c:when>
									<c:when test="${empty checkItem.auditUser and !var.first}">
										<dl class="approval-item">
											<dt class="clearfix">
												<b class="a-tit gray6">重新提交验收</b>
												<span class="gray9 text-no-bold f12">
														课程制作老师 : ${checkItem.createdBy } 
													<fmt:formatDate value="${checkItem.createdDt}" type="both" />
												</span>
												<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
											</dt>
										</dl>
									</c:when>
									<c:otherwise>
										<dl class="approval-item">
											<dt class="clearfix">
												<b class="a-tit gray6">${checkItem.auditUser.priRoleInfo.roleName } 验收</b>
												<span class="gray9 text-no-bold f12">
												<fmt:formatDate value="${checkItem.createdDt}" type="both" />
												</span>
												<c:if test="${checkItem.auditState eq '1'}">
													<span class="fa fa-fw fa-check-circle text-green"></span>
													<label class="state-lb text-green">审核通过</label>
												</c:if>
												<c:if test="${checkItem.auditState eq '2'}">
													<span class="fa fa-fw fa-times-circle text-red"></span>
													<label class="state-lb text-red"> 验收不通过 </label>
												</c:if>
											</dt>
											<dd>
												<div class="txt">
													<p>${checkItem.auditContent }</p>
													<div class="gray9 text-right">审批人：${checkItem.auditUser.realName}</div>
													<i class="arrow-top"></i>
												</div>
											</dd>
										</dl>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</div>
					</div>
				</div>
			</c:if>
		</div>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<!--验收不通过 弹窗-->
<script type="text/template" id="temp1">
	<table style="margin:0 auto;">
		<tr>
			<td valign="top"><i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:46px;"></i></td>
			<td>本课程待验收 {0} 个章节，已验收通过 {1} 个章节，验收不通过 {2} 个章节，请填写验收评语，验收不通过的原因！</td>
		</tr>
	</table>
	<hr class="margin_t10 margin_b10" />
	<div class="margin_b10">验收评语：</div>
	<textarea class="form-control" placeholder="请输入验收评语、不通过的原因和指引" rows="6" data-id="comments"></textarea>
	<div class="ver-tips text-left text-red hide-block">
		<span class="glyphicon glyphicon-remove-circle vertical-mid"></span>
		<span data-role="ver-tips-txt">请填写评语</span>
	</div>
</script>

<!--验收通过 弹窗-->
<script type="text/template" id="temp2">
	<table style="margin:0 auto;">
		<tr>
			<td valign="top"><i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:46px;"></i></td>
			<td>本课程待验收 {0} 个章节，已验收通过 {1} 个章节，请确认课程验收通过！</td>
		</tr>
	</table>
	<hr class="margin_t10 margin_b10" />
	<div class="margin_b10">验收评语：</div>
	<textarea class="form-control" placeholder="请输入验收评语" rows="6" data-id="comments"></textarea>
	<div class="ver-tips text-left text-red hide-block">
		<span class="glyphicon glyphicon-remove-circle vertical-mid"></span>
		<span data-role="ver-tips-txt">请填写评语</span>
	</div>
</script>
<script type="text/javascript">
//确认验收
$('[data-role="sure"]').click(function(event) {
	var $radioGroup=$('.radio-group'),
		allLen=$radioGroup.length;
	var $unpass,unpassLen=0;
	var $taskId=[];
	if(allLen>0){
		$unpass=$radioGroup.filter(function(index) {
			return $(this).find('[data-value="0"]:checked').length>0;
		});
		unpassLen=$unpass.length;
		
		$radioGroup.each(function(index){
			//$taskId[index]=$(this).find('[data-value="1"]:checked').data("id");
			$taskId.push($(this).find('[data-value="1"]:checked').data("id"));
		});
		$radioGroup.each(function(index){
			$taskId.push($(this).find('[data-value="0"]:checked').data("id"));
		});
	}
	var $pop=$.alertDialog({
	    id:'confirm',
	    width:480,
	    height:380,
	    zIndex:11000,
	    cancel:false,
	    okLabel: (unpassLen>0?'验收不通过':'验收通过'),
	    okCss:'btn min-width-90px '+(unpassLen>0?'btn-warning':'btn-primary'),
	    ok:function(){//“确定”按钮的回调方法
	    	var _this=this;
			if( $.trim($('[data-id="comments"]',_this).val())==''){
				$('.ver-tips',_this).show();
			}else{
				$('.ver-tips',_this).hide();
				var $btn = $('[data-role="confirm"]',_this);
				var $auditContent=$('[data-id="comments"]',_this).val();
				var courseId=$('#courseId').val();
				var totalSection=$('#totalSection').val();
				var status=unpassLen>0?'2':'1';
				
				$.post('${ctx}/edumanage/course/submitCheck',{taskIds:$taskId,courseId:courseId,
					totalSection:totalSection,auditContent:$auditContent,status:status},function(data){
					if(!data.successful){
						alert(data.message);
					}else{
						window.location.href="${ctx}/edumanage/course/list"; 
					}
				},'json')
			}
	    },
	    content:''
  	});

  	$('.box-body',$pop).html(
  		$(unpassLen>0?'#temp1':'#temp2').html().format(
  			allLen,
  			allLen-unpassLen,
  			unpassLen
  		)
  	);
});
</script>

</body>
</html>
