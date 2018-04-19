
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材管理</a></li>
		<li class="active">教材计划</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="search_EQ_status" value="">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">计划编号</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_planCode" value="${param.search_LIKE_planCode}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">计划名称</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_planName" value="${param.search_LIKE_planName}">
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer">
	      <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button></div>
	      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	    </div><!-- /.box-footer-->
	</div>
	
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">计划列表</h3>
			<div class="pull-right no-margin">
				<c:if test="${isBtnCreate }">
				<a href="create" class="btn btn-default btn-sm left10 btn-add"><i class="fa fa-fw fa-plus"></i> 发布教材计划</a>
				</c:if>
			</div>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${preAudit + auditNotPass + auditPass})</li>
					<li value="1" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 1 }">class="actived"</c:if>>待审核(${preAudit})</li>
					<li value="2" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 2 }">class="actived"</c:if>>审核不通过(${auditNotPass})</li>
					<li value="3" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 3 }">class="actived"</c:if>>已发布(${auditPass})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered text-center vertical-mid table-font table-hover">
					<thead class="with-bg-gray">
		              <tr>
		                <th>学期</th>
		                <th>计划编号</th>
		                <th>计划名称</th>
		                <th>收货地址确认时间</th>
		                <th>教材发放编排时间</th>
		                <th>教材订购时间</th>
		                <th>教材发放时间</th>
		                <th>教材反馈处理时间</th>
		                <th>状态</th>
		                <th width="11%">操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>${entity.gjtGrade.gradeName}</td>
						            		<td>${entity.planCode}</td>
						            		<td>${entity.planName}</td>
						            		<td>
						            			<fmt:formatDate value="${entity.oaddressConfirmSdate}" pattern="yyyy-MM-dd"/> <br>
												~<br>
												<fmt:formatDate value="${entity.oaddressConfirmEdate}" pattern="yyyy-MM-dd"/>
						            		</td>
						            		<td>
						            			<fmt:formatDate value="${entity.arrangeSdate}" pattern="yyyy-MM-dd"/> <br>
												~<br>
												<fmt:formatDate value="${entity.arrangeEdate}" pattern="yyyy-MM-dd"/>
						            		</td>
						            		<td>
						            			<fmt:formatDate value="${entity.orderSdate}" pattern="yyyy-MM-dd"/> <br>
												~<br>
												<fmt:formatDate value="${entity.orderEdate}" pattern="yyyy-MM-dd"/>
						            		</td>
						            		<td>
						            			<fmt:formatDate value="${entity.odistributeSdate}" pattern="yyyy-MM-dd"/> <br>
												~<br>
												<fmt:formatDate value="${entity.odistributeEdate}" pattern="yyyy-MM-dd"/>
						            		</td>
						            		<td>
						            			<fmt:formatDate value="${entity.ofeedbackSdate}" pattern="yyyy-MM-dd"/> <br>
												~<br>
												<fmt:formatDate value="${entity.ofeedbackEdate}" pattern="yyyy-MM-dd"/>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.status == 1}">
						            					<span class="text-orange">待审核</span>
						            				</c:when>
						            				<c:when test="${entity.status == 2}">
						            					<span class="text-red">审核不通过</span>
						            				</c:when>
						            				<c:when test="${entity.status == 3}">
						            					<span class="text-green">已发布</span>
						            				</c:when>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:if test="${canApproval && entity.status == 1}">
						            				<a href="view/${entity.planId}?isApproval=true" class="operion-item" data-toggle="tooltip" title="审核"><i class="fa fa-shxxjl"></i></a>
						            			</c:if>
						            			<c:if test="${entity.status != 3 && isBtnUpdate}">
						            				<a href="update/${entity.planId}" class="operion-item" data-toggle="tooltip" title="修改"><i class="fa fa-edit"></i></a>
						            			</c:if>
						            			<c:if test="${isBtnView}">
						            				<a href="view/${entity.planId}" class="operion-item" data-toggle="tooltip" title="查看明细"><i class="fa fa-view-more"></i></a>
						            			</c:if>
						            			<c:if test="${entity.status == 2 && isBtnDelete}">
						            				<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${entity.planId}" data-toggle="tooltip" title="删除"><i class="fa fa-trash-o text-red"></i></a>
						            			</c:if>
						            		</td>
						            	</tr>
		            				</c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="11">暂无数据</td>
								</tr>
							</c:otherwise>
						</c:choose>
		            </tbody>
				</table>
			</div>
			<tags:pagination page="${pageInfo}" paginationSize="5" />
		</div>
	</div>
	</form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

//filter tabs
/* $(".filter-tabs li").click(function(event) {
	var $li = $(this);
	$(".filter-tabs li").each(function(index, el) {
		if (el == $li.context && index == 0) {
			window.location.href = "list";
		} else if (el == $li.context && index == 1) {
			window.location.href = "list?search_EQ_status=1";
		} else if (el == $li.context && index == 2) {
			window.location.href = "list?search_EQ_status=2";
		} else if (el == $li.context && index == 3) {
			window.location.href = "list?search_EQ_status=3";
		}
	});
}); */

</script>
</body>
</html>
