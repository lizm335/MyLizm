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

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">学员反馈</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	           <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">订单号</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_EQ_gjtTextbookDistribute.orderCode" value="${param['search_EQ_gjtTextbookDistribute.orderCode']}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">姓名</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学号</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.xh" value="${param['search_LIKE_gjtStudentInfo.xh']}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">评价等级</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_commentType" class="selectpicker form-control">
	                  	<option value="">全部</option>
						<option value="0" <c:if test="${param['search_EQ_commentType'] == '0'}">selected="selected"</c:if> >好评</option>
						<option value="1" <c:if test="${param['search_EQ_commentType'] == 1}">selected="selected"</c:if> >中评</option>
						<option value="2" <c:if test="${param['search_EQ_commentType'] == 2}">selected="selected"</c:if> >差评</option>
					</select>
	              </div>
	            </div>
	          </div>

	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer">
	      <div class="pull-right"><button type="button" class="btn min-width-90px btn-default">重置</button></div>
	      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	    </div><!-- /.box-footer-->
	</div>
	
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">反馈列表</h3>
			<div class="pull-right no-margin">
				<c:if test="${isBtnCreate }">
				<a href="create" class="btn btn-default btn-sm left10 btn-add"><i class="fa fa-fw fa-plus"></i> 新增反馈</a>
				</c:if>
			</div>
		</div>
		<div class="box-body">
			<div class="filter-tabs  clearfix">
				<ul class="list-unstyled">
					<li data-type="" <c:if test="${empty param['search_EQ_commentType']}">class="actived"</c:if>>全部(${goodCount + middleCount+badCount})</li>
					<li data-type="0" <c:if test="${param['search_EQ_commentType'] == '0'}">class="actived"</c:if>>好评(${goodCount})</li>
					<li data-type="1" <c:if test="${param['search_EQ_commentType'] == 1}">class="actived"</c:if>>中评(${middleCount})</li>
					<li data-type="2" <c:if test="${param['search_EQ_commentType'] == 2}">class="actived"</c:if>>差评(${badCount})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th>订单号</th>
		                <th>商品</th>
		                <th>合计</th>
		                <th>评价内容</th>
		                <th>评价人</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>
						            			${entity.gjtTextbookDistribute.orderCode}
						            			
						            		</td>
						            		<td>
						            			<c:if test="${not empty entity.gjtTextbookDistribute.gjtTextbookDistributeDetails}">
						            				<c:forEach items="${entity.gjtTextbookDistribute.gjtTextbookDistributeDetails}" var="detail" varStatus="status">
						            					${detail.gjtTextbook.textbookName}<br/>
						            				</c:forEach>
						            			</c:if>
						            		</td>
						            		<td>
						            			${entity.commentContent}
						            		</td>
						            		<td>
						            			姓名：${entity.gjtStudentInfo.xm}<br />
						            			学号：${entity.gjtStudentInfo.xh}
						            		</td>
						            		<td>
						            			<fmt:formatDate value="${entity.createdDt}" pattern="yyyy-MM-dd HH:mm"/>
						            		</td>
						            		<td>
						            			<c:if test="${isBtnView }">
						            			<a href="view/${entity.feedbackId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
						            			</c:if>
						            		</td>
						            	</tr>
						            </c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="8">暂无数据</td>
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

$(function(){
	$('.list-unstyled li').click(function(){
		$('[name="search_EQ_commentType"]').val($.trim($(this).data('type')));
		$('#listForm').submit();
	});
})
</script>
</body>
</html>
