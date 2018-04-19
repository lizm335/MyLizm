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
		<li class="active">发放教材</li>
	</ol>
</section>
<section class="content">
		<div class="nav-tabs-custom no-margin">
			<ul class="nav nav-tabs nav-tabs-lg">
				<li><a href="${ctx}/home/class/textbookPlan/list" target="_self">教材计划</a></li>
				<li class="active"><a href="${ctx}/home/class/textbookDistribute/list" target="_self">发放教材</a></li>
				<li><a href="${ctx}/home/class/textbookFeedback/list" target="_self">学员反馈</a></li>
			</ul>
			<div class="tab-content">
			<div class="tab-pane active" id="tab_top_1">
				<form id="listForm" class="form-horizontal">
				<div class="box box-border">
				    <div class="box-body">
				        <div class="row pad-t15">
				          
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">计划</label>
				              <div class="col-sm-9">
								<select name="search_EQ_gjtTextbookPlan.planId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${textbookPlanMap}" var="map">
										<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtTextbookPlan.planId']}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
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
				              <label class="control-label col-sm-3 text-nowrap">学期</label>
				              <div class="col-sm-9">
								<select name="search_EQ_gjtStudentInfo.nj" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.nj']}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
				              </div>
				            </div>
				          </div>

				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">层次</label>
				              <div class="col-sm-9">
								<select name="search_EQ_gjtStudentInfo.pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${trainingLevelMap}" var="map">
										<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">专业</label>
				              <div class="col-sm-9">
								<select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${specialtyMap}" var="map">
										<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId']}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">当前状态</label>
				              <div class="col-sm-9">
								<select name="search_EQ_status"  class="selectpicker show-tick form-control" data-size="5" data-live-search="true" data-style="btn-default no-bg flat">
									<option value="" selected="selected">请选择</option>
									<option value="34" <c:if test="${param['search_EQ_status'] == 34}">selected='selected'</c:if> >未就绪</option>
									<option value="5" <c:if test="${param['search_EQ_status'] == 5}">selected='selected'</c:if> >待配送</option>
									<option value="6" <c:if test="${param['search_EQ_status'] == 6}">selected='selected'</c:if> >配送中</option>
									<option value="7" <c:if test="${param['search_EQ_status'] == 7}">selected='selected'</c:if> >已签收</option>
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
	
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
				<div class="box box-border margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">
							<c:choose>
								<c:when test="${param['search_EQ_gjtTextbook.textbookType'] == 2}">复习资料发放列表</c:when>
								<c:otherwise>教材发放列表</c:otherwise>
							</c:choose>
						</h3>
						<div class="pull-right no-margin">
							<!-- <button type="button" class="btn btn-default btn-sm btn-export1 margin-r-5"><i class="fa fa-fw fa-sign-out"></i> 导出待发教材订单,按照年级，而且班主任没这样的权限</button> -->
							<button type="button" class="btn btn-default btn-sm btn-export2"><i class="fa fa-fw fa-sign-out"></i> 导出待签收教材订单</button>
						</div>
					</div>
					<div class="box-body">
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid table-font text-center">
								<thead>
					              <tr>
					                <th width="8%">学期</th>
					                <th>教材计划</th>
					                <th width="15%">学员信息</th>
					                <th width="17%">报读信息</th>
					                <th width="7%">书号</th>
					                <th width="8%">教材名称</th>
					                <th width="8%">教材类型</th>
					                <th width="10%">发放课程</th>
					                <th width="8%">状态</th>
					                <th width="5%">操作</th>
					              </tr>
					            </thead>
					            <tbody>
					            	<c:choose>
					            		<c:when test="${not empty pageInfo.content}">
											<c:forEach items="${pageInfo.content}" var="entity">
												<c:if test="${not empty entity}">
									            	<tr>
									            		<td>
									            			${entity.gjtGrade.gradeName}
									            		</td>
									            		<td>
									            			${entity.gjtTextbookPlan.planName}
									            			<div class="gray9">（${entity.gjtTextbookPlan.planCode}）</div>
									            		</td>
									            		<td>
							                              	<div class="text-left">
								                              	姓名：${entity.gjtStudentInfo.xm}<br>
								                              	学号：${entity.gjtStudentInfo.xh}
							                              	</div>
									            		</td>
									            		<td>
							                              	<div class="text-left">
								                              	学期：${entity.gjtStudentInfo.gjtGrade.gradeName}<br>
								                              	层次：${trainingLevelMap[entity.gjtStudentInfo.pycc]}<br>
								                              	专业：${entity.gjtStudentInfo.gjtSpecialty.zymc}
							                              	</div>
									            		</td>
									            		<td>
									            			${entity.gjtTextbook.textbookCode}
									            		</td>
									            		<td>
									            			${entity.gjtTextbook.textbookName}
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.gjtTextbook.textbookType == 1}">主教材</c:when>
									            				<c:otherwise>复习资料</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			${entity.gjtCourse.kcmc}
									            			<div class="gray9">（${entity.gjtCourse.kch}）</div>
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.status == 3 || entity.status == 4}">
									            					<span class="text-muted">未就绪</span>
									            				</c:when>
									            				<c:when test="${entity.status == 5}">
									            					<span class="text-yellow">待配送</span>
									            				</c:when>
									            				<c:when test="${entity.status == 6}">
									            					<span class="text-light-blue">配送中</span>
									            				</c:when>
									            				<c:when test="${entity.status == 7}">
									            					<span class="text-green">已签收</span>
									            				</c:when>
									            				<c:otherwise>--</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			<a href="view/${entity.studentId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
									            		</td>
									            	</tr>
									            </c:if>
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
						</div>
						<tags:pagination page="${pageInfo}" paginationSize="5" />
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

$(function(){
/* 	$(".btn-export1").click(function(event) {
		window.location.href=ctx+'/home/class/textbookDistribute/exportWaitSign';
	});
 */	
	$(".btn-export2").click(function(event) {
		window.location.href=ctx+'/home/class/textbookDistribute/exportNotSign';
	});
})
</script>
</body>
</html>
