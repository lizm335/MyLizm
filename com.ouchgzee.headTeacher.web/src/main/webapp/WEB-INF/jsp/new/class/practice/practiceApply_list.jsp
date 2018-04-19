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
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学教务服务</a></li>
		<li><a href="#">社会实践组织</a></li>
		<li class="active">学员实践</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<li><a href="${ctx}/home/class/practicePlan/list" target="_self">实践计划</a></li>
			<li class="active"><a href="${ctx}/home/class/practiceApply/list" target="_self">学员实践</a></li>
		</ul>
		<div class="tab-content">
			<form id="listForm" class="form-horizontal">
			<div class="box">
			  <div class="box-body">
			      <div class="row pad-t15">
			        <div class="col-sm-4 col-xs-6">
			          <div class="form-group">
			            <label class="control-label col-sm-3 text-nowrap">实践计划</label>
			            <div class="col-sm-9">
			               <select name="search_EQ_practicePlanId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${practicePlanMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==practicePlanId}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
			            </div>
			          </div>
			        </div>
			        <div class="col-sm-4 col-xs-6">
			          <div class="form-group">
			            <label class="control-label col-sm-3 text-nowrap">学期</label>
			            <div class="col-sm-9">
			                <select name="search_EQ_gjtStudentInfo.nj" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${gradeMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.nj']}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
			            </div>
			          </div>
			        </div>
			        <div class="col-xs-6 col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">层次</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_gjtStudentInfo.gjtSpecialty.pycc" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${pyccMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.pycc']}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
			              </div>
			            </div>
			        </div>
			      </div>
			      <div class="row pad-t15">
			        <div class="col-xs-6 col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">专业</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
			              </div>
			            </div>
			        </div>
			        <div class="col-xs-6 col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">姓名</label>
			              <div class="col-sm-9">
			                <input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
			              </div>
			            </div>
			        </div>
			        <div class="col-xs-6 col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">学号</label>
			              <div class="col-sm-9">
			                <input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xh" value="${param['search_LIKE_gjtStudentInfo.xh']}">
			              </div>
			            </div>
			        </div>
			      </div>
			      <div class="row pad-t15">
			        <div class="col-xs-6 col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">状态</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_status" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${practiceStatusMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param['search_EQ_status']}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
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
		
			<div class="box margin-bottom-none">
				<div class="box-header with-border">
				  <div class="pull-right no-margin">
				    <a href="javascript:void(0)" class="btn btn-default btn-sm btn-export"><i class="fa fa-fw fa-plus"></i> 导出社会实践记录明细</a>
				  </div>
				</div>
				<div class="box-body">
					<div>
						<table class="table table-bordered vertical-mid text-center table-font">
							<thead class="with-bg-gray">
				              <tr>
				              	<th>实践计划</th>
				                <th>个人信息</th>
				                <th>报读信息</th>
				                <th>指导老师</th>
				                <th>实践成绩</th>
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
								            		<td>
								            			${entity.gjtPracticePlan.practicePlanName}<br>
								            			<div class="gray9">（${entity.gjtPracticePlan.practicePlanCode}）</div>
								            		</td>
								            		<td>
								            			<div class="text-left">
								            				姓名：${entity.gjtStudentInfo.xm} <br>
								            				学号：${entity.gjtStudentInfo.xh} <br>
								            				手机：${entity.gjtStudentInfo.sjh}
								            			</div>
								            		</td>
								            		<td>
								            			<div class="text-left">
								            				层次：${pyccMap[entity.gjtStudentInfo.gjtSpecialty.pycc]} <br>
								            				学期：${entity.gjtStudentInfo.gjtGrade.gradeName} <br>
								            				专业：${entity.gjtStudentInfo.gjtSpecialty.zymc}
								            			</div>
								            		</td>
								            		<td>
								            			<c:choose>
								            				<c:when test="${not empty entity.guideTeacher1}">
								            					${entity.guideTeacher1.xm}
								            				</c:when>
								            				<c:otherwise>--</c:otherwise>
								            			</c:choose>
								            		</td>
								            		<td>
								            			<c:choose>
								            				<c:when test="${not empty entity.reviewScore}">
								            					${entity.reviewScore}
								            				</c:when>
								            				<c:otherwise>--</c:otherwise>
								            			</c:choose>
								            		</td>
								            		<td>
								            			${practiceStatusMap[entity.status]}
								            		</td>
								            		<td>
								            			<a href="view?practicePlanId=${entity.practicePlanId}&studentId=${entity.studentId}" class="operion-item" data-toggle="tooltip" title="查看详情" data-role="set"><i class="fa fa-fw fa-view-more"></i></a>
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
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
 
<script type="text/javascript">

$(function() {
	$(".btn-export").click(function(event) {
		window.open("export?" + $("#listForm").serialize());
	});
});

</script>
</body>
</html>
