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
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li><a href="#">论文安排</a></li>
		<li class="active">可申请学生明细</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="thesisPlanId" value="${param.thesisPlanId}">
	<input type="hidden" name="specialtyBaseId" value="${param.specialtyBaseId}">
	<div class="box">
	  <div class="box-body">
	      <div class="row pad-t15">
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">学期</label>
	            <div class="col-sm-9">
	              <select name="search_EQ_gradeId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${gradeMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gradeId']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	            </div>
	          </div>
	        </div>
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">专业规则</label>
	            <div class="col-sm-9">
	                <select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${specialtyMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_specialtyId']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	            </div>
	          </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学号</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_xh" value="${param.search_LIKE_xh}">
	              </div>
	            </div>
	        </div>
	      </div>
	      <div class="row pad-t15">
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">姓名</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm}">
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
		  	<c:if test="${isBtnExport}">
		    	<a href="javascript:void(0)" class="btn btn-default btn-sm btn-export"><i class="fa fa-fw fa-sign-out"></i> 导出可申请学生明细</a>
		    </c:if>
		  </div>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered vertical-mid text-center table-font">
					<thead class="with-bg-gray">
		              <tr>
		              	<th>论文计划</th>
		                <th>个人信息</th>
		                <th>报读信息</th>
		                <th>所属学习中心</th>
		                <th>状态</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>
						            			${thesisPlan.thesisPlanName}<br>
						            			<div class="gray9">（${thesisPlan.thesisPlanCode}）</div>
						            		</td>
						            		<td>
						            			<div class="text-left">
						            				姓名：${entity.studentName}<br>
						            				学号：${entity.studentCode}<br>
						            				<shiro:hasPermission name="/personal/index$privacyJurisdiction">
						            				手机：${entity.mobile}
						            				</shiro:hasPermission>
						            			</div>
						            		</td>
						            		<td>
						            			<div class="text-left">
						            				学期：${entity.gradeName}<br>
						            				层次：${entity.trainingLevel}<br>
						            				专业：${entity.specialtyName}（${entity.specialtyCode}）<br>
						            				专业规则号：${entity.ruleCode}
						            			</div>
						            		</td>
						            		<td>
						            			${entity.scName}
						            		</td>
						            		<td>
						            			${thesisStatusMap[entity.status]}
						            		</td>
						            	</tr>
		            				</c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="5">暂无数据</td>
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

$(".btn-export").click(function(event) {
	window.open(ctx + "/thesisArrange/exportStudentList?" + $("#listForm").serialize());
});

</script>


</body>
</html>
