<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业管理-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">专业管理</li>
		</ol>
	</section>


	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html">
			<input type="hidden" name="search_EQ_type" value="${search_EQ_type}">
			<input type="hidden" name="search_EQ_status"
				value="${search_EQ_status}">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">专业代码</label>
							<div class="col-sm-9">
								<input type="text" class="form-control"
									name="search_LIKE_specialtyCode"
									value="${param.search_LIKE_specialtyCode}">
							</div>
						</div>
						<div class="col-md-4">
							<label class="control-label col-sm-3">专业名称</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" name="search_LIKE_specialtyName"
									value="${param.search_LIKE_specialtyName}">
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer">
					<div class="btn-wrap">
						<button type="button"
							class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
			</div>


			<div class="alert alert-success"
				<c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>>
				<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger"
				<c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>>
				<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

			<div class="box">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">专业列表</h3>
					<div class="fr">
						<div class="btn-wrap fl">
							<c:if test="${isBtnCreate }">
								<a href="create" class="btn btn-default btn-sm"> <i
									class="fa fa-fw fa-plus"></i> 新增专业
								</a>
							</c:if>
						</div>
					</div>
				</div>
				<div class="box-body">
					<div class="filter-tabs filter-tabs2 clearfix">
						<ul class="list-unstyled">
							<%-- <li
								lang=":input[name='search_EQ_type'], :input[name='search_EQ_status']"
								<c:if test="${empty param['search_EQ_type'] && empty param['search_EQ_status']}">class="actived"</c:if>>全部(${all})
							</li> --%>
						</ul>
					</div>
					<div class="table-responsive">
						<table id="dtable"
							class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
								<tr>
									<th>专业代码</th>
									<th>专业名称</th>
									<th>层次</th>
									<th>责任教师</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.getContent() }" var="item">
											<tr>
												<td>${item.specialtyCode }</td>
												<td class="specialtyName">${item.specialtyName }</td>
												<td>
													<c:choose>
														<c:when test="${item.specialtyLayer == 0}">专科</c:when>
														<c:when test="${item.specialtyLayer == 2}">本科</c:when>
														<c:when test="${item.specialtyLayer == 4}">中专</c:when>
														<c:when test="${item.specialtyLayer == 6}">高起专_助力计划</c:when>
														<c:when test="${item.specialtyLayer == 8}">专升本_助力计划</c:when>
														<c:otherwise>--</c:otherwise>
													</c:choose>	
												</td>
												<td>${item.teacher }</td>
												<td>
													<c:choose>
														<c:when test="${item.status == 0}"><span class="text-red">停用</span></c:when>
														<c:when test="${item.status == 1}"><span class="text-green">启用</span></c:when>
														<c:otherwise>--</c:otherwise>
													</c:choose>
												</td>
												<td>
													<input type="hidden" name="specialtyBaseId" value="${item.specialtyBaseId}">
													<c:if test="${isBtnView}">
														<a href="view/${item.specialtyBaseId}" 
																class="operion-item" title="查看详情">
																<i class="fa fa-fw fa-view-more"></i></a>
													</c:if>
													<c:if test="${isBtnStop && item.status == 1}">
														<a href="#" class="operion-item" data-toggle="tooltip"
																title="停用" data-role='stop'><i
																class="fa fa-minus-circle"></i></a>
													</c:if>
													<c:if test="${isBtnEnable && item.status == 0}">
														<a href="#" class="operion-item" data-toggle="tooltip"
																title="启用" data-role='enable'><i
																class="fa fa-play"></i></a>
													</c:if>
													<c:if test="${isBtnDelete && item.status == 0}">
														<a href="javascript:void(0);"
															class="operion-item operion-del del-one" val="${item.specialtyBaseId}"
															title="删除" data-tempTitle="删除">
															<i class="fa fa-fw fa-trash-o"></i></a>
													</c:if>
													<c:if test="${isBtnUpdate}">
														<a href="update/${item.specialtyBaseId}"
															class="operion-item operion-edit" title="编辑">
															<i class="fa fa-fw fa-edit"></i></a> 
													</c:if>
													<c:if test="${isBtnCreateRule}">
														<a href="${ctx}/edumanage/specialty/list?search_EQ_specialtyBaseId=${item.specialtyBaseId}"
															class="operion-item" title="添加专业规则">
															<i class="fa fa-fw  fa-plus-square"></i></a> 
													</c:if>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td align="center" colspan="10">暂无数据</td>
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
	</section>


	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>

<script type="text/javascript">

//启用
$("html").confirmation({
	selector: "[data-role='enable']",
	html:true,
	placement:'top',
	content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>是否将专业设置为启用状态？</div><div class="f12 gray9 margin_b10 text-center">专业：{0}<div class="margin_t5"></div></div>',
	title:false,
	btnOkClass    : 'btn btn-xs btn-primary',
	btnOkLabel    : '确认',
	btnOkIcon     : '',
	btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	btnCancelLabel  : '取消',
	btnCancelIcon   : '',
	popContentWidth:250,
	onShow:function(event,element){
	  var $el=$(element);
	  if($el.is('[data-role="enable"]')){
	  	this.content=this.content.format($el.closest('tr').find('.specialtyName').text());
	  }
	},
	onConfirm:function(event,element){
		  var id = $(element).closest('tr').find('input[name="specialtyBaseId"]').val()
		  window.location.href = ctx + "/edumanage/specialty/base/changeStatus?id=" + id + "&b=true";
	},
	onCancel:function(event, element){
	  
	}
});

//停用
$("body").confirmation({
	selector: "[data-role='stop']",
	html:true,
	placement:'top',
	content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>是否将专业设置为停用状态？</div><div class="f12 gray9 margin_b10 text-center">专业：{0}<div class="margin_t5"></div></div>',
	title:false,
	btnOkClass    : 'btn btn-xs btn-primary',
	btnOkLabel    : '确认',
	btnOkIcon     : '',
	btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	btnCancelLabel  : '取消',
	btnCancelIcon   : '',
	popContentWidth:250,
	onShow:function(event,element){
	  var $el=$(element);
	  if($el.is('[data-role="stop"]')){
	  	this.content=this.content.format($el.closest('tr').find('.specialtyName').text());
	  }
	},
	onConfirm:function(event,element){
		  var id = $(element).closest('tr').find('input[name="specialtyBaseId"]').val()
		  window.location.href = ctx + "/edumanage/specialty/base/changeStatus?id=" + id + "&b=false";
	},
	onCancel:function(event, element){

	}
});

</script>

</html>