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
		<li><a href="#">社会实践组织</a></li>
		<li class="active">实践计划</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<li class="active"><a href="${ctx}/home/class/practicePlan/list" target="_self">实践计划</a></li>
			<li><a href="${ctx}/home/class/practiceApply/list" target="_self">学员实践</a></li>
		</ul>
		<div class="tab-content">
			<form id="listForm" class="form-horizontal">
			<div class="box box-border no-shadow">
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
			      </div>
			  </div><!-- /.box-body -->
			  <div class="box-footer">
			    <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button></div>
			    <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
			  </div><!-- /.box-footer-->
			</div>
		
			<div class="box box-border no-shadow margin-bottom-none">
				<div class="box-body">
					<div class="table-responsive">
						<table class="table table-bordered text-center vertical-mid table-font table-hover">
							<thead class="with-bg-gray">
				              <tr>
				              	<th>计划编号</th>
				                <th>计划名称</th>
				                <th>学期</th>
				                <th colspan="2">社会实践时间安排</th>
				                <th>状态</th>
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
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

</body>
</html>
