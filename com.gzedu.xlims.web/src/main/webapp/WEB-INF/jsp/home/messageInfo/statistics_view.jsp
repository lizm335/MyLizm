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

	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="${ctx }/admin/messageInfo/list"">通知公告</a>
			</li>
			<li class="active">通知详情</li>
		</ol>
	</section>

		<section class="content">
				<form id="listForm" class="form-horizontal">
			<div class="bg-white">
				<ul class="nav nav-tabs bg-f2f2f2" data-role="top-nav">
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/view/${messageId}" style="border-left-color: transparent;">详情</a>
					</li>
					<li class="active">
						<a class="flat gray no-margin" href="#tab_top_2" data-toggle="tab">统计</a>
					</li>
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/commentList?id=${messageId}" >评论</a>
					</li>
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/likeList?id=${messageId}" >点赞</a>
					</li>
					<li>
						<a class="flat gray no-margin" href="${ctx }/admin/messageInfo/feedbackList?id=${messageId}" >反馈</a>
					</li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab_top_1">
						<div class="pad20">
							<div class="box box-border">
								<div class="box-body">
									<div class="row pad-t15">
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">姓名</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_LIKE_userName" value="${param.search_LIKE_userName }">
													<input type="hidden" class="form-control" name="id" id="messageId" value="${messageId }">
													<input type="hidden" class="form-control" name="search_EQ_isRead" value="${param.search_EQ_isRead }">
												</div>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">账号/学号</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_EQ_loginAccount" value="${param.search_EQ_loginAccount }">
												</div>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">角色</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick " name="search_EQ_roleId" data-size="8" data-live-search="true">
														<option value="">全部角色</option>
														<c:forEach items="${roleList }" var="role">
															<option value="${role.roleId }" <c:if test="${param.search_EQ_roleId eq role.roleId }">selected</c:if>>${role.roleName }</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">层次</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick" name="search_EQ_pycc">
														<option value="">全部层次</option>
														<c:forEach items="${pyccMap }" var="map">
															<option value="${map.key }" <c:if test="${param.search_EQ_pycc eq map.key }">selected</c:if>>${map.value }</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">学期</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick" name="search_EQ_gradeId" data-size="8" data-live-search="true">
														<option value="">全部学期</option>
														<c:forEach items="${gradeMap }" var="map">
															<option value="${map.key }" <c:if test="${param.search_EQ_gradeId eq map.key }">selected</c:if>>${map.value }</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">专业</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick" name="search_EQ_specialtyId" data-size="8" data-live-search="true">
														<option value="">全部专业</option>
														<c:forEach items="${specialtyMap }" var="map">
															<option value="${map.key }" <c:if test="${param.search_EQ_specialtyId eq map.key }">selected</c:if>>${map.value }</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- /.box-body -->
								<div class="box-footer">
									<div class="pull-right">
										<button type="button" class="btn min-width-90px btn-default">重置</button>
									</div>
									<div class="pull-right margin_r15">
										<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
									</div>
								</div>
								<!-- /.box-footer-->
							</div>

							<div class="box box-border margin-bottom-none">
								<div class="box-header with-border">
									<h3 class="box-title pad-t5">信息列表</h3>
									<div class="pull-right">
										<button type="button" class="btn btn-sm btn-default margin_r5" data-role="set1">一键设置未读学员强制查看</button>
										<button type="button" class="btn btn-sm btn-default" data-role="set2">一键短信提醒未读通知人</button>
									</div>
								</div>
								<input type="hidden" value="${noRead }" id="noRead">
								<div class="box-body">
									<div class="filter-tabs clearfix filter-tabs2">
										<ul class="list-unstyled">
											<li lang=":input[name='search_EQ_isRead']" <c:if test="${empty param.search_EQ_isRead}">class="actived"</c:if>> 全部(${noRead+yesReadPc+yesReadAPP+yesReadComm })</li>
											<li value="0" role=":input[name='search_EQ_isRead']"  <c:if test="${param.search_EQ_isRead == '0' }">class="actived"</c:if>>未读(${noRead })</li>
											<li value="1" role=":input[name='search_EQ_isRead']" <c:if test="${param.search_EQ_isRead == '1' }">class="actived"</c:if>>已读(PC)(${yesReadPc})</li>
											<li value="2" role=":input[name='search_EQ_isRead']"  <c:if test="${param.search_EQ_isRead == '2' }">class="actived"</c:if>>已读(APP)(${yesReadAPP})</li>
											<li value="3" role=":input[name='search_EQ_isRead']"  <c:if test="${param.search_EQ_isRead == '3' }">class="actived"</c:if>>已读(公众号)(${yesReadComm})</li>
										</ul>
									</div>
									<div class="table-responsive">
										<table class="table table-bordered table-striped vertical-mid text-center table-font">
											<thead>
												<tr>
													<th>个人信息</th>
													<th>报读信息</th>
													<th>状态</th>
													<th>已读时间</th>
													<th>操作</th>
												</tr>
											</thead>
											<tbody>
												<c:choose>
													<c:when test="${not empty pageInfo.content}">
														<c:forEach items="${pageInfo.content}" var="info">
															<tr>
																<td>
																	<ul class="list-unstyled text-left">
																		<li>姓名：${info.REAL_NAME}</li>
																		<li>角色：${info.ROLE_NAME}</li>
																		<li>帐号：${info.LOGIN_ACCOUNT}</li>
																	</ul>
																</td>
																<td>
																	<ul class="list-unstyled text-left">
																		<li>层次：${info.PYCC}</li>
																		<li>学期：${info.GRADE_NAME}</li>
																		<li>专业：${info.ZYMC}</li>
																	</ul>
																</td>
																<td>
																	<c:if test="${info.IS_ENABLED=='1' }">
																		<div class="text-green">
																			已读
																			<c:if test="${info.PLATFORM eq '0'}">（PC）</c:if>
																			<c:if test="${info.PLATFORM eq '1'}">（APP）</c:if>
																			<c:if test="${info.PLATFORM eq '2'}">（公众号）</c:if>
																		</div>
																	</c:if> 
																	<c:if test="${info.IS_ENABLED=='0' }">
																		<div class="text-red">未读</div>
																	</c:if>
																</td>
																<td>
																	${info.UPDATED_DT}
																</td>
																<td>
																	<c:choose>
																		<c:when test="${info.IS_ENABLED=='1' }">
											            					--
											            				</c:when>
																		<c:when test="${info.IS_ENABLED=='0' and info.IS_CONSTRAINT=='1' }">
																			<u class="gray9" role="button" data-role="cancel-force" data-json='{"id":"${info.ID}","userName":"${info.REAL_NAME}"}'>取消设置强制查看</u>
																		</c:when>
																		<c:otherwise>
																			<u class="text-blue" role="button" data-role="force-view" data-json='{"id":"${info.ID}","userName":"${info.REAL_NAME}"}'>设置强制查看</u>
																		</c:otherwise>
																	</c:choose>
																	</td>
															</tr>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<tr>
															<td align="center" colspan="5">暂无数据</td>
														</tr>
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
										<div class="page-container clearfix margin_r10 left10">
											<tags:pagination page="${pageInfo}" paginationSize="5" />
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		</section>
	<!-- 底部 -->
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
var noRead=$('#noRead').val();
var messageId=$('#messageId').val();
//一键设置未读学员强制查看
$(document)
.on('click','[data-role="set1"]',function(event) {
  	$.alertDialog({
	    id:'set1',
	    width:400,
	    height:300,
	    zIndex:11000,
	    ok:function(){//“确定”按钮的回调方法
	    	var postIngIframe=$.formOperTipsDialog({
				text:'数据提交中...',
				iconClass:'fa-refresh fa-spin'
			});
	    	var _this=this;
	    	$.post('${ctx}/admin/messageInfo/settingsConstraintAll',{messageId:messageId,type:'settings'},function(data){
	    		if(data.successful){
	    			setTimeout(function(){
	    				$.alertDialog({
	    				    id:'set2',
	    				    width:400,
	    				    height:300,
	    				    zIndex:11000,
	    				    cancel:false,
	    				    content:' 	<div class="margin_b10 text-center">'+
	    				    		'<i class="fa fa-check-circle text-green vertical-mid margin_r10" style="font-size:50px;"></i>'+
	    				    		'<span class="inline-block vertical-middle f16">已一键设置未读学员强制查看！</span>'+
	    				    		'<div class="gray9">'+noRead+' 学员将强制查看通知</div></div>',
   				    		ok:function(){
   				    			window.location.reload();
   				    		}
	    				});
	    				$.closeDialog(postIngIframe);
	    			},1500);
	    	      	$.closeDialog(_this);
	    		}
	    	},'json');
	    
			
	    },
	    content:'<div class="margin_b10 text-center">'+
	    		'<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:50px;"></i>'+
	    		'<span class="inline-block vertical-middle f16">确认一键设置未读学员强制查看？</span>'+
	    		'<div class="gray9">'+noRead+' 学员未读通知</div></div>'
	});
})
//一键短信提醒未读通知人
.on('click','[data-role="set2"]',function(event) {
  	$.alertDialog({
	    id:'set3',
	    width:400,
	    height:300,
	    zIndex:11000,
	    ok:function(){//“确定”按钮的回调方法
	    	var postIngIframe=$.formOperTipsDialog({
				text:'数据提交中...',
				iconClass:'fa-refresh fa-spin'
			});
	    	var _this=this;
			$.post('${ctx}/admin/messageInfo/settingsConstraintAll',{messageId:messageId,type:'message'},function(data){
	    		if(data.successful){
	    			setTimeout(function(){
	    				$.alertDialog({
	    				    id:'set4',
	    				    width:400,
	    				    height:300,
	    				    zIndex:11000,
	    				    cancel:false,
	    				    content:'<div class="margin_b10 text-center">'+
	    				    		'<i class="fa fa-check-circle text-green vertical-mid margin_r10" style="font-size:50px;"></i>'+
	    				    		'<span class="inline-block vertical-middle f16">已一键短信提醒未读通知人！</span>'+
	    				    		'<div class="gray9">'+noRead+'人将接收短信提醒</div></div> '
	    				});

	    				$.closeDialog(postIngIframe);
	    			},1500);
	    	      	$.closeDialog(_this);
	    		}
	    	},'json');
	    },
	    content:'<div class="margin_b10 text-center">'+
	    		'<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:50px;"></i>'+
	    		'<span class="inline-block vertical-middle f16">确认一键短信提醒未读通知人？</span>'+
	    		'<div class="gray9">'+noRead+' 人未读通知</div></div> '
	});
})
//查看已读/未读
.on('click', '[data-role="view-sel-stu"]', function(event) {
	$('[data-role="top-nav"] li:eq(1) a').tab('show');
	$(window).scrollTop(0);
})
//设置强制查看
.on('click', '[data-role="force-view"]', function(event) {
	var obj=$(this).data('json');
	$.alertDialog({
	    id:'force-view',
	    width:400,
	    height:300,
	    zIndex:11000,
	    ok:function(){
	    	var _this=this;
			$.post('${ctx}/admin/messageInfo/settingsSingleConstraint',{id:obj.id,isConstraint:'1'},function(){
				$.closeDialog(_this);
				window.location.reload();
	    	},'json');
	    	
	    },
	    content:'<div class="margin_b10 text-center">'+
	    		'<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:50px;"></i>'+
	    		'<span class="inline-block vertical-middle f16">确定设置该学员强制查看？</span>'+
	    		'<div class="gray9">学员：'+obj.userName+'</div></div>'
	});
})
//取消设置强制查看
.on('click', '[data-role="cancel-force"]', function(event) {
	var obj=$(this).data('json');
	$.alertDialog({
	    id:'cancel-force',
	    width:400,
	    height:300,
	    zIndex:11000,
	    ok:function(){
	    	var _this=this;
			$.post('${ctx}/admin/messageInfo/settingsSingleConstraint',{id:obj.id,isConstraint:'0'},function(){
				$.closeDialog(_this);
				window.location.reload();
	    	},'json');
	    },
	    content:'<div class="margin_b10 text-center">'+
	    		'<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:50px;"></i>'+
	    		'<span class="inline-block vertical-middle f16">确定取消设置该学员强制查看？</span>'+
	    		'<div class="gray9">学员：'+obj.userName+'</div></div> '
	});
})
</script>
</body>
</html>

