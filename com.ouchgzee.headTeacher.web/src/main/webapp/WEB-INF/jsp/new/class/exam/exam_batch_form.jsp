<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任平台</title>

<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<div class="pull-left">
    	您所在位置：
  	</div>
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试组织</a></li>
		<li><a href="javascript:;">考试计划</a></li>
		<li class="active">明细</li>
	</ol>
</section>

<section class="content">
          <div class="row">
            <div class="col-md-12">
              <div class="box box-default" style="padding-bottom: 20px;">
                <div class="box-header with-border"> 
                  <h3 class="box-title">考试计划</h3>
                </div>
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx}/edumanage/exam/${action}" method="post">
                  <input type="hidden" id="entity_examBatchId" value="${entity.examBatchId}">
                  <input id="action" type="hidden" name="action" value="${action}">
                
                  <div class="box-body">
                  		<div class="cnt-box-header with-border margin_t20 margin_b20">
							<h3 class="cnt-box-title f14 text-bold margin_b10">计划基础信息</h3>	
						</div>
						<div class="form-group"> 
							<label class="col-sm-2 control-label text-no-bold"><small class="text-red">*</small>计划编号:</label>
							<div class="col-sm-3">
								<input type="text" name="examBatchCode" id="entity_examBatchCode" class="form-control" value="${entity.examBatchCode}" readOnly="true"/>
							</div>
							
							<label class="col-sm-2 control-label text-no-bold"><small class="text-red">*</small>计划名称:</label>
							<div class="col-sm-3">
								<input type="text" name="name" id="entity_name" <c:if test="${action=='update'}">disabled="disabled"</c:if> 
									class="form-control" value="${entity.name}" />
							</div>
						</div>
						
						<div class="form-group"> 
							<label class="col-sm-2 control-label text-no-bold"><small class="text-red">*</small>学期:</label>
							<div class="col-sm-3">
								<select name="gradeId" id="entity_gradeId" <c:if test="${action=='update'}">disabled="disabled"</c:if> 
									class="selectpicker show-tick form-control" data-size="5" data-live-search="true" >
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}"<c:if test="${map.key==entity.gradeId}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
							</div>
							
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">开考科目设置时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_planSt" name="planSt" 
										placeholder="开考科目设置开始时间" data-role="date-start"
										value="<fmt:formatDate value='${entity.planSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control bg-white" id="entity_planEnd" name="planEnd" 
										placeholder="开考科目设置结束时间" data-role="date-end"
										value="<fmt:formatDate value='${entity.planEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
						</div>
						
						<div class="form-group margin-bottom-none">
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">考试预约时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_bookSt" name="bookSt"
										placeholder="考试预约开始时间" nullmsg="请填写考试预约开始时间" data-role="date-start"
										value="<fmt:formatDate value='${entity.bookSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>">
									
									<input type="text" class="form-control bg-white" id="entity_bookEnd" name="bookEnd" 
										placeholder="考试预约结束时间" nullmsg="请填写考试预约结束时间" data-role="date-end"
										value="<fmt:formatDate value='${entity.bookEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>">
								</div>
							</div>
							
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">排考时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_arrangeSt" name="arrangeSt" 
										placeholder="排考开始时间" data-role="date-start"
										value="<fmt:formatDate value='${entity.arrangeSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
										
									<input type="text" class="form-control bg-white" id="entity_arrangeEnd" name="arrangeEnd" 
										placeholder="排考结束时间" data-role="date-end" 
										value="<fmt:formatDate value='${entity.arrangeEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
						</div>
						
						<div class="form-group margin-bottom-none">
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold"></label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_booksSt" name="booksSt" 
										placeholder="考试预约开始时间" data-role="date-start"
										value="<fmt:formatDate value='${entity.booksSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>">
									
									<input type="text" class="form-control bg-white" id="entity_booksEnd" name="booksEnd" 
										placeholder="考试预约结束时间" data-role="date-end"
										value="<fmt:formatDate value='${entity.booksSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>">
								</div>
							</div>
						</div>
						
						<div class="cnt-box-header with-border margin_t20 margin_b20">
							<h3 class="cnt-box-title f14 text-bold margin_b10">考试时间</h3>	
						</div>
						<div class="form-group margin-bottom-none">
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">网考、大作业考试时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_onlineSt" name="onlineSt" 
										placeholder="网考考试开始时间" data-role="date-start" 	
										value="<fmt:formatDate value='${entity.onlineSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control bg-white" id="entity_onlineEnd" name="onlineEnd" 
										placeholder="网考考试结束时间" data-role="date-end" 	
										value="<fmt:formatDate value='${entity.onlineEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
							
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">省网考考试时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_provinceOnlineSt" name="provinceOnlineSt" 
										placeholder="省网考考试开始时间" data-role="date-start"	
										value="<fmt:formatDate value='${entity.provinceOnlineSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control bg-white" id="entity_provinceOnlineEnd" name="provinceOnlineEnd" 
										placeholder="省网考考试结束时间" data-role="date-end" 	
										value="<fmt:formatDate value='${entity.provinceOnlineEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
						</div>
						
						<div class="form-group margin-bottom-none">
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">笔考考试时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_paperSt" name="paperSt" 
										placeholder="笔考考试开始时间" data-role="date-start"	
										value="<fmt:formatDate value='${entity.paperSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control bg-white" id="entity_paperEnd" name="paperEnd" 
										placeholder="笔考考试结束时间" data-role="date-end"	
										value="<fmt:formatDate value='${entity.paperEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
							
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">机考考试时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_machineSt" name="machineSt" 
										placeholder="机考考试开始时间" data-role="date-start"	
										value="<fmt:formatDate value='${entity.machineSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control bg-white" id="entity_machineEnd" name="machineEnd" 
										placeholder="机考考试结束时间" data-role="date-end"	
										value="<fmt:formatDate value='${entity.machineEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
						</div>
						
						<div class="form-group margin-bottom-none">
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">本科统考预约时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_bktkBookSt" name="bktkBookSt" 
										placeholder="本科统考预约开始时间" data-role="date-start" 	
										value="<fmt:formatDate value='${entity.bktkBookSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control bg-white" id="entity_bktkBookEnd" name="bktkBookEnd" 
										placeholder="本科统考预约结束时间" data-role="date-end"	
										value="<fmt:formatDate value='${entity.bktkBookEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
							
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">学位英语报考时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<div class="input-group" data-role="daterangetime-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									 </div>
									<input type="text" class="form-control bg-white" id="entity_xwyyBookSt" name="xwyyBookSt" 
										placeholder="学位英语报考开始时间" data-role="date-start" 	
										value="<fmt:formatDate value='${entity.xwyyBookSt}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
									<input type="text" class="form-control bg-white" id="entity_xwyyBookEnd" name="xwyyBookEnd" 
										placeholder="学位英语报考结束时间" data-role="date-end"	
										value="<fmt:formatDate value='${entity.xwyyBookEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
								</div>
							</div>
						</div>
						
						<div class="form-group margin-bottom-none" data-role="daterangetime-group">
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">形考任务登记截止时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<input type="text" class="form-control  bg-white" id="entity_shapeEnd" name="shapeEnd" 
									placeholder="形考任务登记截止时间" data-role="date-start"
									value="<fmt:formatDate value='${entity.shapeEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
							</div>
							
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">论文截止时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<input type="text" class="form-control  bg-white" id="entity_thesisEnd" name="thesisEnd" 
									placeholder="论文截止时间" data-role="date-start"	
									value="<fmt:formatDate value='${entity.thesisEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
							</div>
						</div>
						
						<div class="form-group margin-bottom-none" data-role="daterangetime-group">
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">报告截止时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<input type="text" class="form-control bg-white" id="entity_reportEnd" name="reportEnd" 
									placeholder="报告截止时间" data-role="date-start"	
									value="<fmt:formatDate value='${entity.reportEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
							</div>
							
							<label class="col-md-2 col-sm-2 control-label margin_b15 text-no-bold">学习成绩登记截止时间</label>
							<div class="col-md-3 col-sm-10 margin_b15">
								<input type="text" class="form-control bg-white" id="entity_recordEnd" name="recordEnd" 
									placeholder="学习成绩登记截止时间" data-role="date-start"
									value="<fmt:formatDate value='${entity.recordEnd}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" />
							</div>
						</div>
						
                  </div>

				<c:if test="${action != 'view'}">
                  <div class="box-footer">
                  	<div class="col-sm-7 col-sm-offset-2">
                  		<c:if test="${action=='create'}">
                  			<button id="btn-create" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit">创建</button>
                  		</c:if>
	                    <c:if test="${action=='update'}">
                  			<button id="btn-update" type="button" class="btn btn-success min-width-90px margin_r15 btn-save-edit">更新</button>
                  		</c:if>
    	                <button type="reset" id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">返回</button>
                  	</div>
                  </div>
                </c:if>
                  
                  </form>
                  
              	<c:if test="${action == 'view'}">
              		<div class="box-body">
						<div class="cnt-box-header with-border margin_t20 margin_b20">
						  <h3 class="cnt-box-title f14 text-bold margin_b10">审核记录</h3>	
						</div>
			
						<div class="approval-list clearfix">
							<c:forEach items="${entity.gjtExamBatchApprovals}" var="approval" varStatus="status">
								<dl class="approval-item">
									<dt class="clearfix">
										<b class="a-tit gray6">
											<c:choose>
												<c:when test="${approval.auditState == 1}">
													发布计划
												</c:when>
												<c:when test="${approval.auditState == 3}">
													重新发布计划
												</c:when>
												<c:otherwise>
													${approval.createdUser.priRoleInfo.roleName}审核
												</c:otherwise>
											</c:choose>
										</b>
										<c:choose>
							          		<c:when test="${approval.auditOperatorRole == 1}">
							          			<span class="gray9 text-no-bold f12 margin_l10">
							          				${approval.createdUser.priRoleInfo.roleName}：${approval.createdUser.realName}  <fmt:formatDate value='${approval.createdDt}' type='date' pattern='yyyy-MM-dd HH:mm'/> 发布
							          			</span>
							          			<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
							          		</c:when>
							          		<c:otherwise>
							          			<span class="gray9 text-no-bold f12 margin_l10">
							          				<fmt:formatDate value='${approval.createdDt}' type='date' pattern='yyyy-MM-dd HH:mm'/>
							          				<c:choose>
							          					<c:when test="${entity.planStatus == 3 && status.last}">
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
									<c:if test="${approval.auditOperatorRole == 2}">
										<dd>
						                    <div class="txt">
						                        <p>${approval.auditContent}</p>
						                        <div class="gray9 text-right">审批人：${approval.createdUser.realName}</div>
						                        <i class="arrow-top"></i>
						                    </div>
						                </dd>
									</c:if>
								</dl>
							</c:forEach>
							
							<c:if test="${entity.planStatus == 1 && param.isApproval}">
								<dl class="approval-item white-border">
				                  <dt class="clearfix">
				                    <b class="a-tit gray6">审批人审核</b>
				                    <span class="fa fa-fw fa-dot-circle-o text-yellow"></span>
				                    <label class="state-lb pending-state">待审核</label>
				                  </dt>
				                  <dd>
				                    <form id="approvalForm" class="theform" action="${ctx}/exam/new/batch/approval" method="post">
						        	  <input type="hidden" id="examBatchId" name="examBatchId" value="${entity.examBatchId}">
						        	  <input type="hidden" id="auditState" name="auditState" value="">
				                      <div class="col-xs-12 no-padding position-relative">
				                        <textarea id="auditContent" name="auditContent" class="form-control" rows="3" placeholder="请输入审批评语或重交、不通过的原因和指引" datatype="*" nullmsg="请输入内容！" errormsg="请输入内容！"></textarea>
				                      </div>
				                      <div>
				                        <button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10" data-role="btn-pass">通过</button>
				                        <button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10" data-role="btn-not-pass">不通过</button>
				                      </div>
				                    </form>
				                  </dd>
				              </dl>
							</c:if>
			            </div>
		            </div>
				</c:if>
                  
              </div>
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
		$('[data-role="cancel"]').removeAttr("disabled");
	}
	
});
</script>

</body>
</html>