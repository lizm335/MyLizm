
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>订购安排</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材管理</a></li>
		<li class="active">订购安排</li>
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
	              <label class="control-label col-sm-3 text-nowrap">学期</label>
	              <div class="col-sm-9">
	                	 <select name="search_EQ_gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
	                		<c:forEach items="${gradeMap}" var="map">
								<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gradeId']}">selected='selected'</c:if> >${map.value}</option>
							</c:forEach>
	                	</select>
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer">
	      <div class="pull-right"><button  class="btn min-width-90px btn-default btn-reset">重置</button></div>
	      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	    </div><!-- /.box-footer-->
	</div>
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">安排列表</h3>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered text-center vertical-mid table-font table-hover">
					<thead class="with-bg-gray">
		              <tr>
		                <th>学期</th>
		                <th>学期时间</th>
		                <th>教材订购时间</th>
		                <th width="11%">操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>${entity.gradeName}</td>
						            		<td>
						            			<c:if test="${not empty(entity.startDate)}">
						            				<fmt:formatDate value="${entity.startDate}" pattern="yyyy-MM-dd"/>
													~
													<fmt:formatDate value="${entity.endDate}" pattern="yyyy-MM-dd"/>
						            			</c:if>
						            			<c:if test="${empty(entity.startDate)}">
						            				未设置
						            			</c:if>
						            		</td>
						            		<td>
						            			<fmt:formatDate value="${entity.textbookStartDate}" pattern="yyyy-MM-dd"/>
												~
												<fmt:formatDate value="${entity.textbookEndDate}" pattern="yyyy-MM-dd"/>
						            		</td>
						            		
						            		<td>
						            			<a href="updateOrderPlan/${entity.gradeId}"  class="operion-item update-btn" data-toggle="tooltip" title="修改"><i class="fa fa-edit"></i></a>
						            		</td>
						            	</tr>
		            				</c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="4">暂无数据</td>
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
$('.update-btn').click(function(){
    event.preventDefault();
    $.mydialog({
		id : 'mydialog',
		width : 700,
		height : $(window).height() * 0.8,
		zIndex : 11000,
		content : 'url:' + $(this).attr('href')
    });
});

</script>
</body>
</html>
