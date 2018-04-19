<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	var action = $('#action').val();
	if(action == 'view' || action == 'audit'){
		$(':input').attr("readonly","readonly");
		$(':radio').attr("disabled","disabled");
		$('select').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();

		$('#auditRecord').show();
		$('#auditRecord :input').removeAttr("readonly");
	} else {
		//参考： http://bv.doc.javake.cn/examples/
		$('#inputForm').bootstrapValidator({
			excluded: [':disabled'],//验证下拉必需加
			fields: {
				graPlanTitle: {
					validators: {
						notEmpty: {
							message: "请输入计划名称"
						}
					}
				},
				'gjtGrade.gradeId': {
					validators: {
						notEmpty: {
							message: "请选择学期"
						}
					}
				},
				graApplyBeginDt: {
					validators: {
						notEmpty: {
							message: '请输入毕业申请开始时间'
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '开始日期不能晚于结束日期',
							callback: function (value, validator, $field, options) {
								var graApplyEndDt = $('#inputForm').find("input[name='graApplyEndDt']").val();
								$('#inputForm').find("input[name='graApplyEndDt']").keypress();
								validator.updateStatus('graApplyEndDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) <= Date.parse(graApplyEndDt.replace(/-/g, "/"));
							}
						}
					}
				},
				graApplyEndDt: {
					validators: {
						notEmpty: {
							message: "请输入毕业申请结束时间"
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '结束日期不能早于开始日期',
							callback: function (value, validator, $field, options) {
								var graApplyBeginDt = $('#inputForm').find("input[name='graApplyBeginDt']").val();
								$('#inputForm').find("input[name='graApplyBeginDt']").keypress();
								validator.updateStatus('graApplyBeginDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) >= Date.parse(graApplyBeginDt.replace(/-/g, "/"));
							}
						}
					}
				},
				degreeApplyBeginDt: {
					validators: {
						notEmpty: {
							message: '请输入学位申请开始时间'
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '开始日期不能晚于结束日期',
							callback: function (value, validator, $field, options) {
								var degreeApplyEndDt = $('#inputForm').find("input[name='degreeApplyEndDt']").val();
								$('#inputForm').find("input[name='degreeApplyEndDt']").keypress();
								validator.updateStatus('degreeApplyEndDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) <= Date.parse(degreeApplyEndDt.replace(/-/g, "/"));
							}
						}
					}
				},
				degreeApplyEndDt: {
					validators: {
						notEmpty: {
							message: "请输入学位申请结束时间"
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '结束日期不能早于开始日期',
							callback: function (value, validator, $field, options) {
								var degreeApplyBeginDt = $('#inputForm').find("input[name='degreeApplyBeginDt']").val();
								$('#inputForm').find("input[name='degreeApplyBeginDt']").keypress();
								validator.updateStatus('degreeApplyBeginDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) >= Date.parse(degreeApplyBeginDt.replace(/-/g, "/"));
							}
						}
					}
				},
				graAuditBeginDt: {
					validators: {
						notEmpty: {
							message: '请输入毕业审核开始时间'
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '开始日期不能晚于结束日期',
							callback: function (value, validator, $field, options) {
								var graAuditEndDt = $('#inputForm').find("input[name='graAuditEndDt']").val();
								$('#inputForm').find("input[name='graAuditEndDt']").keypress();
								validator.updateStatus('graAuditEndDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) <= Date.parse(graAuditEndDt.replace(/-/g, "/"));
							}
						}
					}
				},
				graAuditEndDt: {
					validators: {
						notEmpty: {
							message: "请输入毕业审核结束时间"
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '结束日期不能早于开始日期',
							callback: function (value, validator, $field, options) {
								var graAuditBeginDt = $('#inputForm').find("input[name='graAuditBeginDt']").val();
								$('#inputForm').find("input[name='graAuditBeginDt']").keypress();
								validator.updateStatus('graAuditBeginDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) >= Date.parse(graAuditBeginDt.replace(/-/g, "/"));
							}
						}
					}
				},
				degreeAuditBeginDt: {
					validators: {
						notEmpty: {
							message: '请输入学位审核开始时间'
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '开始日期不能晚于结束日期',
							callback: function (value, validator, $field, options) {
								var degreeAuditEndDt = $('#inputForm').find("input[name='degreeAuditEndDt']").val();
								$('#inputForm').find("input[name='degreeAuditEndDt']").keypress();
								validator.updateStatus('degreeAuditEndDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) <= Date.parse(degreeAuditEndDt.replace(/-/g, "/"));
							}
						}
					}
				},
				degreeAuditEndDt: {
					validators: {
						notEmpty: {
							message: "请输入学位审核结束时间"
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '结束日期不能早于开始日期',
							callback: function (value, validator, $field, options) {
								var degreeAuditBeginDt = $('#inputForm').find("input[name='degreeAuditBeginDt']").val();
								$('#inputForm').find("input[name='degreeAuditBeginDt']").keypress();
								validator.updateStatus('degreeAuditBeginDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) >= Date.parse(degreeAuditBeginDt.replace(/-/g, "/"));
							}
						}
					}
				},
				graCertReceiveBeginDt: {
					validators: {
						notEmpty: {
							message: '请输入毕业证书领取开始时间'
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '开始日期不能晚于结束日期',
							callback: function (value, validator, $field, options) {
								var graCertReceiveEndDt = $('#inputForm').find("input[name='graCertReceiveEndDt']").val();
								$('#inputForm').find("input[name='graCertReceiveEndDt']").keypress();
								validator.updateStatus('graCertReceiveEndDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) <= Date.parse(graCertReceiveEndDt.replace(/-/g, "/"));
							}
						}
					}
				},
				graCertReceiveEndDt: {
					validators: {
						notEmpty: {
							message: "请输入毕业证书领取结束时间"
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '结束日期不能早于开始日期',
							callback: function (value, validator, $field, options) {
								var graCertReceiveBeginDt = $('#inputForm').find("input[name='graCertReceiveBeginDt']").val();
								$('#inputForm').find("input[name='graCertReceiveBeginDt']").keypress();
								validator.updateStatus('graCertReceiveBeginDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) >= Date.parse(graCertReceiveBeginDt.replace(/-/g, "/"));
							}
						}
					}
				},
				graArchivesReceiveBeginDt: {
					validators: {
						notEmpty: {
							message: '请输入毕业档案领取开始时间'
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '开始日期不能晚于结束日期',
							callback: function (value, validator, $field, options) {
								var graArchivesReceiveEndDt = $('#inputForm').find("input[name='graArchivesReceiveEndDt']").val();
								$('#inputForm').find("input[name='graArchivesReceiveEndDt']").keypress();
								validator.updateStatus('graArchivesReceiveEndDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) <= Date.parse(graArchivesReceiveEndDt.replace(/-/g, "/"));
							}
						}
					}
				},
				graArchivesReceiveEndDt: {
					validators: {
						notEmpty: {
							message: "请输入毕业档案领取结束时间"
						},
						date: {
							format: 'YYYY-MM-DD',
							message: '请输入正确的时间格式（YYYY-MM-DD）'
						},
						callback: {
							message: '结束日期不能早于开始日期',
							callback: function (value, validator, $field, options) {
								var graArchivesReceiveBeginDt = $('#inputForm').find("input[name='graArchivesReceiveBeginDt']").val();
								$('#inputForm').find("input[name='graArchivesReceiveBeginDt']").keypress();
								validator.updateStatus('graArchivesReceiveBeginDt', 'VALID');
								return Date.parse(value.replace(/-/g, "/")) >= Date.parse(graArchivesReceiveBeginDt.replace(/-/g, "/"));
							}
						}
					}
				}
			}
		});

		//时间控件改变文本框的值时重新触发校验
		$('.reservation').on('change', function (e) {
			var fieldName = $(this).attr('name');
			$('#inputForm')
					.data('bootstrapValidator')
					.updateStatus(fieldName, 'NOT_VALIDATED', null)
					.validateField(fieldName);
		});
	}
})
</script> 

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">毕业管理</a></li>
		<li><a href="${ctx}/graduation/plan/list">毕业计划</a></li>
		<li class="active">新增计划</li>
	</ol>
</section>

<section class="content">

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

	<div class="box no-margin">
              <div class="box-body pad20">
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/graduation/plan/${action}" method="post">
                <input id="action" type="hidden" name="action" value="${action}">
                <input type="hidden" name="id" value="${entity.id}">
                  <div class="cnt-box-header with-border"> 
                    <h3 class="cnt-box-title">毕业计划设置</h3>
                  </div>
                  <div class="form-horizontal reset-form-horizontal">
                 		<c:if test="${not empty entity.graPlanNo}">
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>计划编号:</label>
							<div class="col-sm-6">
								<p class="form-control-static">${entity.graPlanNo}</p>
							</div>
						</div>
						</c:if>
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>计划名称:</label>
							<div class="col-sm-6">
								<input type="text" name="graPlanTitle" class="form-control" value="${entity.graPlanTitle}" placeholder="计划名称"/>
							</div>
						</div>
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>学期:</label>
							<div class="col-sm-6">
								<select name="gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${termMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==entity.gjtGrade.gradeId}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
							</div>
						</div>
					</div>

					<div class="cnt-box-header with-border margin_b15">
					  <h3 class="cnt-box-title">毕业时间安排</h3>
					</div>
					<div class="form-horizontal reset-form-horizontal">
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>毕业登记时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" class="reservation form-control" name="graRegisterBeginDt" value="${entity.graRegisterBeginDt}"/>
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	至
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" class="reservation form-control" name="graRegisterEndDt" value="${entity.graRegisterEndDt}" />
				                    </div>
				                </div>
							</div>
						</div>
					
						<div class="form-group"> 
							<label class="col-sm-3 control-label"><small class="text-red">*</small>毕业申请时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
				                	<div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" class="reservation form-control" name="graApplyBeginDt" value="${entity.graApplyBeginDt}"/>
				                    </div>
				                	<div class="input-group-addon no-border">
					                    	至
					                </div>
					                <div class="input-group full-width">
				                      <div class="input-group-addon">
				                        <i class="fa fa-calendar"></i>
				                      </div>
				                      <input type="text" class="reservation form-control" name="graApplyEndDt" value="${entity.graApplyEndDt}" />
				                    </div>
				                </div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>学位申请时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="degreeApplyBeginDt" value="${entity.degreeApplyBeginDt}"/>
									</div>
									<div class="input-group-addon no-border">
										至
									</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="degreeApplyEndDt" value="${entity.degreeApplyEndDt}" />
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>毕业审核时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="graAuditBeginDt" value="${entity.graAuditBeginDt}"/>
									</div>
									<div class="input-group-addon no-border">
										至
									</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="graAuditEndDt" value="${entity.graAuditEndDt}" />
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>学位审核时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="degreeAuditBeginDt" value="${entity.degreeAuditBeginDt}"/>
									</div>
									<div class="input-group-addon no-border">
										至
									</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="degreeAuditEndDt" value="${entity.degreeAuditEndDt}" />
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">学位意向退回时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="degreeBackBeginDt" value="${entity.degreeBackBeginDt}"/>
									</div>
									<div class="input-group-addon no-border">
										至
									</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="degreeBackEndDt" value="${entity.degreeBackEndDt}" />
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>毕业证书领取时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="graCertReceiveBeginDt" value="${entity.graCertReceiveBeginDt}"/>
									</div>
									<div class="input-group-addon no-border">
										至
									</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="graCertReceiveEndDt" value="${entity.graCertReceiveEndDt}" />
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>毕业档案领取时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="graArchivesReceiveBeginDt" value="${entity.graArchivesReceiveBeginDt}"/>
									</div>
									<div class="input-group-addon no-border">
										至
									</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="graArchivesReceiveEndDt" value="${entity.graArchivesReceiveEndDt}" />
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">学位证书领取时间:</label>
							<div class="col-sm-6">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="degreeCertReceiveBeginDt" value="${entity.degreeCertReceiveBeginDt}"/>
									</div>
									<div class="input-group-addon no-border">
										至
									</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="reservation form-control" name="degreeCertReceiveEndDt" value="${entity.degreeCertReceiveEndDt}" />
									</div>
								</div>
							</div>
						</div>
					</div>

					<div id="auditRecord" style="display: none;">
						<div class="cnt-box-header with-border margin_b15">
							<h3 class="cnt-box-title">审核记录</h3>
						</div>

						<div class="form-horizontal reset-form-horizontal">
							<input type="hidden" name="auditState" value="" />
							<div class="form-group">
								<label class="col-sm-3 control-label"></label>
								<div class="col-sm-6">
									<div class="approval-list clearfix">
										<c:forEach var="record" items="${flowRecordList}" varStatus="s">
											<c:if test="${record.auditOperatorRole==1}">
												<dl class="approval-item">
													<dt class="clearfix">
														<b class="a-tit gray6">${s.index==0?'发布计划':'重新发布计划'}</b>
														<span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
														<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
													</dt>
												</dl>
											</c:if>
											<c:if test="${record.auditOperatorRole!=1}">
												<dl class="approval-item" <c:if test="${s.index==(fn:length(flowRecordList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
													<dt class="clearfix">
														<b class="a-tit gray6">${record.auditOperatorRole==5?'教务管理员审核':''}</b>
														<c:if test="${record.auditState==0}">
															<span class="fa fa-fw fa-dot-circle-o text-orange"></span>
															<label class="state-lb text-orange">待审核</label>
														</c:if>
														<c:if test="${record.auditState==1}">
															<span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
															<span class="fa fa-fw fa-check-circle text-green"></span>
															<label class="state-lb text-green">审核通过</label>
														</c:if>
														<c:if test="${record.auditState==2}">
															<span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
															<span class="fa fa-fw fa-times-circle text-red"></span>
															<label class="state-lb text-red">审核不通过</label>
														</c:if>
													</dt>
													<c:if test="${record.auditState==0}">
														<c:if test="${record.auditOperatorRole==5}">
															<shiro:hasPermission name="/graduation/plan/list$approval">
																<dd>
																	<div class="col-xs-12 no-padding position-relative">
																		<textarea name="auditContent" class="form-control" rows="3" placeholder="请输入审核备注，例如该计划确认无误" datatype="*1-200" nullmsg="请输入内容！" errormsg="字数不能超过200"></textarea>
																	</div>
																	<div>
																		<button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10 btn-audit" val="2" data-form-id="5">审核不通过</button>
																		<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10 btn-audit" val="1" data-form-id="5">审核通过</button>
																	</div>
																</dd>
															</shiro:hasPermission>
														</c:if>
													</c:if>
													<c:if test="${record.auditState!=0}">
														<dd>
															<div class="txt">
																<p>${record.auditContent}</p>
																<div class="gray9 text-right">审核人：${record.auditOperator}</div>
																<i class="arrow-top"></i>
															</div>
														</dd>
													</c:if>
												</dl>
											</c:if>
										</c:forEach>
									</div>
								</div>
							</div>

						</div>
						</div>

                  <div class="row">
                  	<div class="col-sm-6 col-sm-offset-3">
	                    <button id="btn-submit" type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
    	                <button id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
                  	</div>
                  </div>
                  </form>
                  
              </div>
     </div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/graduation/plan/graduation_plan_form.js"></script>
</body>
</html>