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
<title>工单管理列表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li class="active">  ${type=='1'?'学院分点':'新华社分点' }</li>
		</ol>
	</section>
	<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li <c:if test="${type eq '1' }">class="active"</c:if>>
					<a href="${ctx}/graduation/photograph/list?type=1" target="_self">学院分点</a>
				</li>
				<li <c:if test="${type eq '2' }">class="active"</c:if>>
					<a href="${ctx}/graduation/photograph/list?type=2" target="_self">新华社分点</a>
				</li>
				<li  <c:if test="${type eq '3'}">class="active"</c:if>>
					<a href="${ctx}/graduation/photograph/photographDataView?type=3" target="_self">拍摄资料</a>
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
												<label class="control-label col-sm-3 text-nowrap">拍摄地点</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_LIKE_photographAddress" value="${param.search_LIKE_photographAddress}">
												</div>
											</div>
										</div>
										<div class="col-sm-4 col-xs-6">
											<div class="form-group">
												<label class="control-label col-sm-3 text-nowrap">拍摄点名称</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="search_LIKE_photographName" value="${param.search_LIKE_photographName}">
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
									<button type="submit" class="btn min-width-90px btn-primary" id="search_submit_button">搜索</button>
								</div>
							</div>
							<!-- /.box-footer-->
						</div>

						<div class="box box-border margin-bottom-none">
							<div class="box-header with-border">
								<h3 class="box-title pad-t5">学院分点</h3>
								 <%-- <shiro:hasPermission name="/organization/branchSchool/list$create"> --%>
								  <div class="pull-right no-margin">
									<a role="button" href="create?type=${type }" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新增分点</a>
								  </div>
								<%--  </shiro:hasPermission> --%>
							</div>
							<div class="box-body">
								<div class="table-responsive">
									<table class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
										<thead>
											<tr>
												<th>所在区域</th>
												<th>拍摄点名称</th>
												<c:if test="${type eq '1' }">
												<th >拍摄时间</th>
												</c:if>
												<th >拍摄地点</th>
												<th >联系电话</th>
												<th width="15%">状态</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="info">
														<tr>
															<td>${info.remark }</td>
															<td>${info.photographName }</td>
															<c:if test="${type eq '1' }">
															<td>
																<fmt:formatDate value="${info.photographSrartDate }" type="both"/>  ~ 
																<fmt:formatDate value="${info.photographEndDate }" type="both"/>
															 </td>
															 </c:if>
															<td>${info.photographAddress }</td>
															<td>${info.telePhone }</td>
															<td>
																<c:if test="${info.isEnabled eq '0'}">
																	<span class="text-red">已停用</span>
																</c:if>
																<c:if test="${info.isEnabled eq '1'}">
																	<span class="text-green">已启用</span>
																</c:if>
															</td>
															<td>
																	<shiro:hasPermission name="/graduation/photograph/list$edit">
																		<a href="update/${info.id}" class="operion-item operion-view" data-toggle="tooltip" title="编辑"><i class="fa fa-fw fa-edit"></i></a>
																	</shiro:hasPermission>
																  	<shiro:hasPermission name="/graduation/photograph/list$delete">
																		<a href="delete/${info.id}" val="${info.id}" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="sure-btn-1"><i class="fa fa-fw fa-trash-o text-red"></i></a>
																  	</shiro:hasPermission>
																  	<shiro:hasPermission name="/graduation/photograph/list$startOrstop">
																	  	<c:if test="${info.isEnabled eq '1'}">
																			<a href="javascript:;" onclick="changeStatus('${ctx}/graduation/photograph/changeStatus/${info.id}/0')" 
																				class="operion-item operion-view" data-toggle="tooltip" title="停用">
																				<i class="fa fa-fw fa-pause"></i>
																			</a>
																		</c:if>
																		<c:if test="${info.isEnabled eq '0'}">
																			<a href="javascript:;" onclick="changeStatus('${ctx}/graduation/photograph/changeStatus/${info.id}/1')"
																				 class="operion-item operion-view" data-toggle="tooltip" title="启用">
																				<i class="fa fa-fw fa-play"></i>
																			</a>
																		</c:if>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="/graduation/photograph/list$view">
																		<a href="view/${entity.id}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
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
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

var url = "";
$("body").confirmation({
  selector: "[data-role='sure-btn-1']",
  html:true,
  placement:'top',
  content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定要删除？</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:190,
  onShow:function(event,element){
    url = $(event.target).attr("href");
  },
  onConfirm:function(event,element){
      $.ajax({
          type: "POST",
          dataType: "json",
          url: url,
          success: function (data) {
			  alert(data.message);
              if(data.successful==true){
              	$("#search_submit_button").click();
			  }
          },
          complete: function (request,status) {

          }
      });
  },
  onCancel:function(event, element){

  }
});

function changeStatus(url) {
	$.ajax({
	   	type:"post",
	   	dataType:"json",
		url:url,
		success:function (data) {
	   	    if(data.successful==false){
	   	    	alert('操作失败！')
			}else{
				window.location.href=window.location.href;
			}
      }
	});
}
	
</script>
</body>
</html>
