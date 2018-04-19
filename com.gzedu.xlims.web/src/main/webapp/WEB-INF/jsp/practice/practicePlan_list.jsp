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
		<li><a href="#">实践管理</a></li>
		<li class="active">实践计划</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="box">
	  <div class="box-body">
	      <div class="row pad-t15">
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">计划编号</label>
	            <div class="col-sm-9">
	              <input type="text" class="form-control" name="search_LIKE_practicePlanCode" value="${param.search_LIKE_practicePlanCode}" placeholder="计划编号">
	            </div>
	          </div>
	        </div>
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">计划名称</label>
	            <div class="col-sm-9">
	              <input type="text" class="form-control" name="search_LIKE_practicePlanName" value="${param.search_LIKE_practicePlanName}" placeholder="计划名称">
	            </div>
	          </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学期</label>
	              <div class="col-sm-9">
					<select name="search_EQ_gradeId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${termMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gradeId']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">状态</label>
	              <div class="col-sm-9">
					<select name="status" id="status" class="selectpicker form-control">
						<option value="0">全部</option>
						<option value="1" <c:if test="${param.status == 1}">selected="selected"</c:if> >未开始</option>
						<option value="2" <c:if test="${param.status == 2}">selected="selected"</c:if> >进行中</option>
						<option value="3" <c:if test="${param.status == 3}">selected="selected"</c:if> >已结束</option>
						<option value="4" <c:if test="${param.status == 4}">selected="selected"</c:if> >待审核</option>
						<option value="5" <c:if test="${param.status == 5}">selected="selected"</c:if> >审核不通过</option>
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

	<div class="box margin-bottom-none">
		<div class="box-header with-border">
		  <h3 class="box-title pad-t5">计划列表</h3>
		  <div class="pull-right no-margin">
		  	<c:if test="${isBtnCreate }">
		    	<a href="create" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新增社会实践计划</a>
		    </c:if>
		  </div>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='status']" <c:if test="${empty param['status']}">class="actived"</c:if>>全部（${preAudit + auditNotPass + notStart + starting + end}）</li>
					<li value="1" role=":input[name='status']" <c:if test="${param['status'] == 1 }">class="actived"</c:if>>未开始（${notStart}）</li>
					<li value="2" role=":input[name='status']" <c:if test="${param['status'] == 2 }">class="actived"</c:if>>进行中（${starting}）</li>
					<li value="3" role=":input[name='status']" <c:if test="${param['status'] == 3 }">class="actived"</c:if>>已结束（${end}）</li>
					<li value="4" role=":input[name='status']" <c:if test="${param['status'] == 4 }">class="actived"</c:if>>待审核（${preAudit}）</li>
					<li value="5" role=":input[name='status']" <c:if test="${param['status'] == 5 }">class="actived"</c:if>>审核不通过（${auditNotPass}）</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered vertical-mid text-center table-font">
					<thead class="with-bg-gray">
		              <tr>
		              	<th>计划编号</th>
		                <th>计划名称</th>
		                <th>学期</th>
		                <th colspan="2">社会实践时间安排</th>
		                <th>状态</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td rowspan="4">${entity.practicePlanCode}</td>
						            		<td rowspan="4">
						            			${entity.practicePlanName}
						            		</td>
						            		<td rowspan="4">
						            			${termMap[entity.gradeId]}
						            		</td>
						            		<td>
						            			社会实践申请时间
						            		</td>
						            		<td>${entity.applyBeginDt} ~ ${entity.applyEndDt}</td>
						            		<td rowspan="4">
												<c:if test="${entity.status2 == 1}">
													<span class="text-orange">未开始</span>
												</c:if>
												<c:if test="${entity.status2 == 2}">
													<span class="text-green">进行中</span>
												</c:if>
												<c:if test="${entity.status2 == 3}">
													<span class="gray9">已结束</span>
												</c:if>
												<c:if test="${entity.status2 == 4}">
													<span class="text-orange">待审核</span>
												</c:if>
												<c:if test="${entity.status2 == 5}">
													<span class="text-red">审核不通过</span>
												</c:if>
											</td>
						            		<td rowspan="4">
						            			<c:if test="${canApproval && entity.status == 1}">
						            				<a href="view/${entity.practicePlanId}?isApproval=true" class="operion-item" data-toggle="tooltip" title="审核"><i class="fa fa-shxxjl"></i></a>
						            			</c:if>
						            			<c:if test="${entity.status2 != 3 && isBtnUpdate}">
						            				<a href="update/${entity.practicePlanId}" class="operion-item" data-toggle="tooltip" title="编辑"><i class="fa fa-edit"></i></a>
						            			</c:if>
						            			<c:if test="${isBtnView}">
						            				<a href="view/${entity.practicePlanId}" class="operion-item" data-toggle="tooltip" title="查看明细"><i class="fa fa-view-more"></i></a>
						            			</c:if>
						            			<c:if test="${entity.status == 2 && isBtnDelete}">
						            				<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${entity.practicePlanId}" data-toggle="tooltip" title="删除"><i class="fa fa-trash-o text-red"></i></a>
						            			</c:if>
						            		</td>
						            	</tr>
						            	<tr>
						            		<td>
						            			初稿提交截止时间
						            		</td>
						            		<td>${entity.submitPracticeEndDt}</td>
						            	</tr>
						            	<tr>
						            		<td>
						            			实践定稿截止时间
						            		</td>
						            		<td>${entity.confirmPracticeEndDt}</td>
						            	</tr>
						            	<tr>
						            		<td>
						            			实践写作评分时间
						            		</td>
						            		<td>${entity.reviewDt}</td>
						            	</tr>
		            				</c:if>
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
			</div>
			<tags:pagination page="${pageInfo}" paginationSize="5" />
		</div>
	</div>
	</form>
</section>
		
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
</html>
