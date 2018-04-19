<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>工单管理列表</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li class="active">我的工单</li>
		</ol>
	</section>
	<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li class="active">
					<a href="${ctx}/home/class/workOrder/list" target="_self">我收到的工单</a>
				</li>
				<li>
					<a href="${ctx}/home/class/workOrder/putList" target="_self">我发布的工单</a>
				</li>
				<li>
					<a href="${ctx}/home/class/workOrder/copyTolist" target="_self">抄送给我的工单</a>
				</li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<form id="listForm" class="form-horizontal" action="list">
						<div class="box box-border">
							<div class="box-body">
								<div class="form-horizontal">
									<div class="row pad-t15">
										<div class="col-sm-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">标题</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_LIKE_gjtWorkOrder.title" value="${param.search_LIKE_gjtWorkOrder.title}">
													 <input type="hidden"	class="form-control" name="search_EQ_gjtWorkOrder.isState" value="${param.search_EQ_gjtWorkOrder.isState}">
												</div>
											</div>
										</div>
										<div class="col-sm-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">类型</label>
												<div class="col-sm-9">
													<select class="form-control selectpicker show-tick" name="search_EQ_gjtWorkOrder.workOrderType" data-size="6" data-live-search="true">
														<option value="">全部</option>
														<c:forEach items="${workTypeMap}" var="map">
															<option value="${map.key}" <c:if test="${param.search_EQ_gjtWorkOrder.workOrderType eq map.key}">selected</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
										<div class="col-xs-6 col-sm-4">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">指派日期</label>
												<div class="col-sm-9">
													<div class="input-group input-daterange" data-role="date-group">
														<input type="text" class="form-control" data-role="date-start" name="createDtBegin" value="${createDtBegin }">
														<span class="input-group-addon nobg">－</span>
														<input type="text" class="form-control" data-role="date-end" name="createDtEnd" value="${createDtEnd }">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<!-- /.box-body -->
							<div class="box-footer">
								<div class="pull-right">
									<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
								</div>
								<div class="pull-right margin_r15">
									<button type="submit" class="btn min-width-90px btn-primary" id='btn'>搜索</button>
								</div>
							</div>
							<!-- /.box-footer-->
						</div>

						<div class="box box-border margin-bottom-none">
							<div class="box-header with-border">
								<h3 class="box-title pad-t5">工单列表</h3>
								<div class="pull-right no-margin">
								</div>
							</div>
							<div class="box-body">
								<div class="filter-tabs filter-tabs2 clearfix">
									<ul class="list-unstyled">
										<li lang=":input[name='search_EQ_gjtWorkOrder.isState']" <c:if test="${empty param['search_EQ_gjtWorkOrder.isState']}">class="actived"</c:if>>全部(${finishNum +  waitNum + closeNum})</li>
										<li value="1" role=":input[name='search_EQ_gjtWorkOrder.isState']" <c:if test="${param['search_EQ_gjtWorkOrder.isState'] == '1' }">class="actived"</c:if>>完成(${finishNum})</li>
										<li value="0" role=":input[name='search_EQ_gjtWorkOrder.isState']" <c:if test="${param['search_EQ_gjtWorkOrder.isState'] == '0' }">class="actived"</c:if>>待完成(${waitNum})</li>
										<li value="2" role=":input[name='search_EQ_gjtWorkOrder.isState']" <c:if test="${param['search_EQ_gjtWorkOrder.isState'] == '2' }">class="actived"</c:if>>已关闭(${closeNum})</li>
									</ul>
								</div>
								<div class="table-responsive">
									<table class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
										<thead>
											<tr>
												<th>级别</th>
												<th>标题</th>
												<th width="10%">要求完成时间</th>
												<th width="10%">指派时间</th>
												<th width="15%">指派人</th>
												<th width="15%">状态</th>
												<th width="10%">操作</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="info">
														<tr>
															<td>
																<input type="hidden" value="${info.gjtWorkOrder.id}">
																<c:if test="${info.gjtWorkOrder.priority eq '1' }">
																	<small class="label label-warning text-no-bold">优先</small>
																</c:if>
																<c:if test="${info.gjtWorkOrder.priority eq '0' }">
																	<small class="label label-default text-no-bold">一般</small>
																</c:if>
																<c:if test="${info.gjtWorkOrder.priority eq '2' }">
																	<small class="label label-danger text-no-bold">紧急</small>
																</c:if>
															</td>
															<td>
																<div class="text-left">
																	${info.gjtWorkOrder.title }
																	<div class="gray9">${workTypeMap[info.gjtWorkOrder.workOrderType] }</div>
																</div>
															</td>
															<td>${info.gjtWorkOrder.demandDate }</td>
															<td><fmt:formatDate value="${info.gjtWorkOrder.createdDt }" type="both"/> </td>
															<td>${info.gjtWorkOrder.createUser.realName}
																<div class="gray9">（${info.gjtWorkOrder.createUser.priRoleInfo.roleName}）</div>
															</td>
															<td>
																<c:if test="${info.gjtWorkOrder.isState eq '0' }">
																	<span class="text-orange">待完成</span>
																</c:if>
																<c:if test="${info.gjtWorkOrder.isState eq '1' }">
																	<span class="text-green">已完成</span>
																</c:if>
																<c:if test="${info.gjtWorkOrder.isState eq '2' }">
																	<span class="gray9">已关闭</span>
																</c:if>
															</td>
															<td>
																<shiro:hasPermission name="/home/class/workOrder/list$mySure">
																	<c:if test="${info.gjtWorkOrder.isState eq '0' }">
																		<a href="#" class="operion-item" data-id="${info.gjtWorkOrder.id}" data-toggle="tooltip" title="我已完成" data-role="sure">
																			<i class="fa fa-fw fa-check-circle"></i>
																		</a>
																	</c:if>
																</shiro:hasPermission>
																<shiro:hasPermission name="/home/class/workOrder/list$view">
																	<a href="view?id=${info.gjtWorkOrder.id }&type=1" class="operion-item operion-view" data-toggle="tooltip" title="查看详情">
																		<i class="fa fa-fw fa-view-more"></i>
																	</a>
																</shiro:hasPermission>
															</td>
														</tr>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr>
														<td align="center" colspan="7">暂无数据</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
									<tags:pagination page="${pageInfo}" paginationSize="5" />
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">

	/*日期控件*/
	$('[data-role="date-group"]').each(function(i,e){
		var startDate=$('[data-role="date-start"]',e);
		var endDate=$('[data-role="date-end"]',e);
		//开始时间			
		startDate.datepicker({
		  language:'zh-CN',
		  format:'yyyy-mm-dd'
		}).on('changeDate', function(e) {
			var add=increaseOnedate(e.target.value);
			endDate.datepicker('setStartDate',add);
		});
		//结束时间
		endDate.datepicker({
		  language:'zh-CN',
		  format:'yyyy-mm-dd'
		}).on('changeDate', function(e) {
			var d=decreaseOnedate(e.target.value);
			startDate.datepicker('setEndDate',d);
		}).on('focus',function(){
			if(this.value==""&&startDate.val()==""){
				startDate.focus();
				endDate.datepicker('hide');
			}
		});
	});

	//确认工单
	$("body").confirmation({
		selector: "[data-role='sure']",
		html:true,
		placement:'top',
		content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定已完成该工作项？</div>',
		title:false,
		btnOkClass    : 'btn btn-xs btn-primary',
		btnOkLabel    : '确认',
		btnOkIcon     : '',
		btnCancelClass  : 'btn btn-xs btn-default margin_l10',
		btnCancelLabel  : '取消',
		btnCancelIcon   : '',
		popContentWidth:190,
		onShow:function(event,element){
		
		},
		onConfirm:function(event,element){
			var $el=$(element);
			var id=$el.data('id');
			$.get('updateState',{id:id,state:1},function(data){
				if(data.successful==false){
					alert(data.message);
				}else{
					window.location.href=window.location.href;
				}
			},'json'); 
		},
		onCancel:function(event, element){
	    
		}
	});
</script>
</body>
</html>
