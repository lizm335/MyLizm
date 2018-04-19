
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
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学教务服务</a></li>
		<li><a href="#">教材组织</a></li>
		<li class="active">教材计划</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<li class="active"><a href="${ctx}/home/class/textbookPlan/list" target="_self">教材计划</a></li>
			<li><a href="${ctx}/home/class/textbookDistribute/list" target="_self">发放教材</a></li>
			<li><a href="${ctx}/home/class/textbookFeedback/list" target="_self">学员反馈</a></li>
		</ul>
		<div class="tab-content">
			<form id="listForm" class="form-horizontal">
				<div class="box box-border no-shadow">
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
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">学期</label>
				              <div class="col-sm-9">
								<select name="search_EQ_gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
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
				      <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button></div>
				      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
				    </div><!-- /.box-footer-->
				</div>
			</form>
			
			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			
			<div class="box box-border no-shadow margin-bottom-none">
				<div class="box-body">
					<div class="table-responsive">
						<table class="table table-bordered text-center vertical-mid table-font table-hover">
							<thead class="with-bg-gray">
				              <tr>
				                <th>学期</th>
				                <th>计划编号</th>
				                <th>计划名称</th>
				                <th>教材发放编排时间</th>
				                <th>教材订购时间</th>
				                <th>收货地址确认时间</th>
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
								            			<fmt:formatDate value="${entity.oaddressConfirmSdate}" pattern="yyyy-MM-dd"/> <br>
														~<br>
														<fmt:formatDate value="${entity.oaddressConfirmEdate}" pattern="yyyy-MM-dd"/>
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
							            				<a href="view/${entity.planId}" class="operion-item" data-toggle="tooltip" title="查看明细"><i class="fa fa-view-more"></i></a>
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
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

</body>
</html>
