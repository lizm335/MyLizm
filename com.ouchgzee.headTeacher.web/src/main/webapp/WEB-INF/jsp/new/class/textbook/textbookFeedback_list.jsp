<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>班主任平台</title>

<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<div class="pull-left">
    	您所在位置：
  	</div>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学教务服务</a></li>
		<li><a href="#">教材组织</a></li>
		<li class="active">学员反馈</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li><a href="${ctx}/home/class/textbookPlan/list" target="_self">教材计划</a></li>
				<li><a href="${ctx}/home/class/textbookDistribute/list" target="_self">发放教材</a></li>
				<li class="active"><a href="${ctx}/home/class/textbookFeedback/list" target="_self">学员反馈</a></li>
			</ul>
			<div class="tab-content">
				<div class="box box-border">
				    <div class="box-body">
				        <div class="row pad-t15">
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
				              <label class="control-label col-sm-3 text-nowrap">反馈类型</label>
				              <div class="col-sm-9">
				                <select name="search_EQ_feedbackType" class="form-control">
				                  	<option value="">全部</option>
									<option value="1" <c:if test="${param['search_EQ_feedbackType'] == 1}">selected="selected"</c:if> >退换教材</option>
									<option value="2" <c:if test="${param['search_EQ_feedbackType'] == 2}">selected="selected"</c:if> >补发教材</option>
									<option value="3" <c:if test="${param['search_EQ_feedbackType'] == 3}">selected="selected"</c:if> >其它</option>
								</select>
				              </div>
				            </div>
				          </div>
			
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">处理状态</label>
				              <div class="col-sm-9">
				                <select name="search_EQ_status" class="form-control">
				                  	<option value="">全部</option>
									<option value="1" <c:if test="${param['search_EQ_status'] == 1}">selected="selected"</c:if> >待处理</option>
									<option value="2" <c:if test="${param['search_EQ_status'] == 2}">selected="selected"</c:if> >已处理</option>
								</select>
				              </div>
				            </div>
				          </div>
				        </div>
				    </div><!-- /.box-body -->
				    <div class="box-footer">
				      <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
				      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
				    </div><!-- /.box-footer-->
				</div>
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box box-border margin-bottom-none">
					<div class="box-body">
						<div class="filter-tabs clearfix">
							<ul class="list-unstyled">
								<li <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${pending + processed})</li>
								<li <c:if test="${param['search_EQ_status'] == 1}">class="actived"</c:if>>待处理(${pending})</li>
								<li <c:if test="${param['search_EQ_status'] == 2}">class="actived"</c:if>>已处理(${processed})</li>
							</ul>
						</div>
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center">
								<thead>
					              <tr>
					                <th>姓名</th>
					                <th>学号</th>
					                <th>反馈类型</th>
					                <th>教材名称</th>
					                <th>原因</th>
					                <th>反馈时间</th>
					                <th>处理状态</th>
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
									            			${entity.gjtStudentInfo.xm}
									            		</td>
									            		<td>
									            			${entity.gjtStudentInfo.xh}
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.feedbackType == 1}">退换教材</c:when>
									            				<c:when test="${entity.feedbackType == 2}">补发教材</c:when>
									            				<c:when test="${entity.feedbackType == 3}">其它</c:when>
									            				<c:otherwise>--</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			<c:if test="${not empty entity.gjtTextbookFeedbackDetails}">
									            				<c:forEach items="${entity.gjtTextbookFeedbackDetails}" var="detail" varStatus="status">
									            					<c:choose>
													          			<c:when test="${status.last}">${detail.gjtTextbook.textbookName}</c:when>
													          			<c:otherwise>${detail.gjtTextbook.textbookName}, </c:otherwise>
													          		</c:choose>
									            				</c:forEach>
									            			</c:if>
									            		</td>
									            		<td>
									            			${entity.reason}
									            		</td>
									            		<td>
									            			<fmt:formatDate value="${entity.createdDt}" pattern="yyyy-MM-dd HH:mm"/>
									            		</td>
									            		<td>
									            			
									            			<c:choose>
									            				<c:when test="${entity.status == 1}">
									            					<span class="text-yellow">待处理</span>
									            				</c:when>
									            				<c:when test="${entity.status == 2}">
									            					<span class="text-green">已处理</span>
									            				</c:when>
									            				<c:otherwise>--</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			<a href="view/${entity.feedbackId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
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
			</div>
		</div>
	</form>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">

$(function(){
	// filter tabs
	$(".filter-tabs li").click(function(event) {
		var $li = $(this);
		$(".filter-tabs li").each(function(index, el) {
			if (el == $li.context && index == 0) {
				window.location.href = "list";
			} else if (el == $li.context && index == 1) {
				window.location.href = "list?search_EQ_status=1";
			} else if (el == $li.context && index == 2) {
				window.location.href = "list?search_EQ_status=2";
			}
		});
	});
})
</script>
</body>
</html>
