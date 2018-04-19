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
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<div class="pull-left">
    	您所在位置：
  	</div>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学教务服务</a></li>
		<li><a href="#">教材组织</a></li>
		<li><a href="#">教材计划</a></li>
		<li class="active">明细</li>
	</ol>
</section>
<section class="content">
	<div class="box margin-bottom-none">
		<div class="box-body">
				<div class="cnt-box-header with-border margin_b20">
				  <h3 class="cnt-box-title">教材计划详情：</h3>
				</div>
		        <div class="form-horizontal reset-form-horizontal">
		        	<c:choose>
		        		<c:when test="${action=='create'}">
							<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>所属学期</label>
								<div id="gradeDiv" class="col-sm-6 position-relative">
									<select id="gradeId" name="gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" datatype="*" nullmsg="请选择所属学期！" errormsg="请选择所属学期！">
										<option value="" selected="selected">请选择学期</option>
										<c:forEach items="${termMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==entity.gradeId}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>计划编号</label>
								<div id="planCodeDiv" class="col-sm-6 position-relative">
									<input id="planCode" name="planCode" value="${entity.planCode}" type="text" class="form-control" placeholder="建议输入年+月份作为编号" datatype="*" nullmsg="请填写计划编号！" errormsg="请填写计划编号！">
								</div>
							</div>
		        		</c:when>
		        		<c:otherwise>
		        			<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>所属学期</label>
								<div class="col-sm-6 position-relative">
									<input type="text" class="form-control" value="${entity.gjtGrade.gradeName}" readOnly="true" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>计划编号</label>
								<div class="col-sm-6 position-relative">
									<input value="${entity.planCode}" type="text" class="form-control"  readOnly="true">
								</div>
							</div>
		        		</c:otherwise>
		        	</c:choose>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>计划名称</label>
						<div class="col-sm-6 position-relative">
							<input name="planName" value="${entity.planName}" type="text" class="form-control" placeholder="计划名称" datatype="*" nullmsg="请填写计划名称！" errormsg="请填写计划名称！">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>教材发放编排时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom input-group" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="arrangeSdate" value="<fmt:formatDate value='${entity.arrangeSdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间"
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" data-date-orientation="top" readonly>
			            		</div>
			            		<p class="input-group-addon no-border">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="arrangeEdate" value="<fmt:formatDate value='${entity.arrangeEdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" data-date-orientation="top" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>教材订购时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom input-group" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="orderSdate" value="<fmt:formatDate value='${entity.orderSdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
			            		</div>
			            		<p class="input-group-addon no-border">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="orderEdate" value="<fmt:formatDate value='${entity.orderEdate}' type='date' pattern='yyyy-MM-dd'/>"  
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>收货地址确认时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom input-group" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="oaddressConfirmSdate" value="<fmt:formatDate value='${entity.oaddressConfirmSdate}' type='date' pattern='yyyy-MM-dd'/>"
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
			            		</div>
			            		<p class="input-group-addon no-border">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="oaddressConfirmEdate" value="<fmt:formatDate value='${entity.oaddressConfirmEdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>教材发放时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom input-group" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="odistributeSdate" value="<fmt:formatDate value='${entity.odistributeSdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" 
			            				errormsg="日期格式不对" data-role="date-start" readonly>
			            		</div>
			            		<p class="input-group-addon no-border">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="odistributeEdate" value="<fmt:formatDate value='${entity.odistributeEdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" 
			            				errormsg="日期格式不对" data-role="date-end" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>教材反馈处理时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom input-group" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="ofeedbackSdate" value="<fmt:formatDate value='${entity.ofeedbackSdate}' type='date' pattern='yyyy-MM-dd'/>"
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" 
			            				errormsg="日期格式不对" data-role="date-start" readonly>
			            		</div>
			            		<p class="input-group-addon no-border">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="ofeedbackEdate" value="<fmt:formatDate value='${entity.ofeedbackEdate}' type='date' pattern='yyyy-MM-dd'/>"
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" 
			            				errormsg="日期格式不对" data-role="date-end" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
				</div>
			
			<c:if test="${action == 'view'}">
				<div class="cnt-box-header with-border margin_t20 margin_b20">
				  <h3 class="cnt-box-title">审核记录：</h3>
				</div>
	
				<div class="approval-list clearfix">
					<c:forEach items="${entity.gjtTextbookPlanApprovals}" var="approval" varStatus="status">
						<dl class="approval-item">
							<dt class="clearfix">
								<b class="a-tit gray6">
									<c:choose>
										<c:when test="${approval.operaType == 1}">
											发布计划
										</c:when>
										<c:when test="${approval.operaType == 3}">
											重新发布计划
										</c:when>
										<c:otherwise>
											${approval.createdUser.priRoleInfo.roleName}审核
										</c:otherwise>
									</c:choose>
								</b>
								<c:choose>
					          		<c:when test="${approval.operaRole == 1}">
					          			<span class="gray9 text-no-bold f12 margin_l10">
					          				${approval.createdUser.priRoleInfo.roleName}：${approval.createdUser.realName}  <fmt:formatDate value='${approval.createdDt}' type='date' pattern='yyyy-MM-dd HH:mm'/> 发布
					          			</span>
					          			<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
					          		</c:when>
					          		<c:otherwise>
					          			<span class="gray9 text-no-bold f12 margin_l10">
					          				<fmt:formatDate value='${approval.createdDt}' type='date' pattern='yyyy-MM-dd HH:mm'/>
					          				<c:choose>
					          					<c:when test="${entity.status == 3 && status.last}">
					          						<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
	                    							<label class="state-lb text-green">审核通过</label>
					          					</c:when>
					          					<c:otherwise>
					          						<span class="fa fa-fw fa-times-circle text-red"></span>
	  							                    <label class="state-lb text-red">审核不通过</label>
					          					</c:otherwise>
					          				</c:choose>
					          			</span>
					          		</c:otherwise>
					          	</c:choose>
							</dt>
							<c:if test="${approval.operaRole == 2}">
								<dd>
				                    <div class="txt">
				                        <p>${approval.description}</p>
				                        <div class="gray9 text-right">审批人：${approval.createdUser.realName}</div>
				                        <i class="arrow-top"></i>
				                    </div>
				                </dd>
							</c:if>
						</dl>
					</c:forEach>
	            </div>
			</c:if>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">
	$(function() {
		if($('#action').val() == 'view'){
			$(':input').attr("disabled","disabled");
			$('[data-role="back-off"]').removeAttr("disabled");
		}
		
	});
	
</script>
</body>
</html>
