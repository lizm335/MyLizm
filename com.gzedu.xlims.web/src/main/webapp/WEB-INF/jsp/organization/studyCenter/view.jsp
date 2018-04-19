<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button onclick="window.href='list.do'" id="btn-back2" class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">机构管理</a>
			</li>
			<li>
				<a href="#">学习中心</a>
			</li>
			<li>
				<a href="#">学习中心详情</a>
			</li>
		</ol>
	</section>
	
	<section class="content">
		<div class="box margin-bottom-none">
			<div class="box-body">
				<div class="form-horizontal reset-form-horizontal">
					<input type="hidden"  id="orgId" value="${entity.id}">
					<div class="form-group">
						<label class="col-sm-2 control-label">所属院校：</label>
						<div class="col-sm-8">
							<p class="form-control-static">${schoolName}</p>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">学习中心类型：</label>
						<div class="col-sm-8">
							<p class="form-control-static">
								<c:if test="${entity.gjtOrg.orgType eq '3'}">学习中心</c:if>
								<c:if test="${entity.gjtOrg.orgType eq '6'}">招生点</c:if>
							</p>
							</select>
						</div>
					</div>

					<c:if test="${entity.gjtOrg.orgType eq '6'}">
						<div class="form-group ">
							<label class="col-sm-2 control-label"><small class="text-red">*</small>所属学习中心：</label>
							<div class="col-sm-8">
								<p class="form-control-static">${entity.gjtOrg.parentGjtOrg.orgName}</p>
							</div>
						</div>
					</c:if>

					<div class="form-group">
						<label class="col-sm-2 control-label"> <c:if test="${entity.gjtOrg.orgType eq '3'}">
							学习中心名称:
						</c:if> <c:if test="${entity.gjtOrg.orgType eq '6'}">
							招生点名称：
						</c:if>
						</label>
						<div class="col-sm-8">
							<p class="form-control-static">${entity.scName}</p>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">学习中心代码：</label>
						<div class="col-sm-8">
							<p class="form-control-static">${entity.scCode}</p>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">合同编号：</label>
						<div class="col-sm-8">
							<p class="form-control-static">${entity.compactNo}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">提供的服务项：</label>
						<div class="col-sm-8">
							<p class="form-control-static">
								<input type="checkbox" value="1" name="serviceAreas" <c:if test="${serviceAreas['1'] }">checked='checked'</c:if>>招生服务
								<input type="checkbox" value="2" name="serviceAreas" <c:if test="${serviceAreas['2'] }">checked='checked'</c:if>>学习支持服务 
								<input type="checkbox" value="3" name="serviceAreas" <c:if test="${serviceAreas['3'] }">checked='checked'</c:if>>课程定制服务 
								<input type="checkbox" value="4" name="serviceAreas" <c:if test="${serviceAreas['4'] }">checked='checked'</c:if>>课程教学辅导服务
								<input type="checkbox" value="5" name="serviceAreas" <c:if test="${serviceAreas['5'] }">checked='checked'</c:if>>教材服务
							</p>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">所在区域：</label>
						<div class="col-sm-8">
							<div class="row">
								<div class="col-sm-4">
									<select id="province" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
										<option value="" selected="selected"></option>
									</select>
								</div>
								<div class="col-sm-4">
									<select id="city" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
										<option value="" selected="selected"></option>
									</select>
								</div>
								<div class="col-sm-4">
									<select id="district" name="district" class="selectpicker show-tick form-control" data-size="8" data-live-search="true">
										<option value="${entity.district}" selected="selected"></option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">详细地址：</label>
						<div class="col-sm-8">
							<p class="form-control-static">${entity.officeAddr}</p>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">联系电话：</label>
						<div class="col-sm-8">
							<p class="form-control-static">${entity.linkTel}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">联系人：</label>
						<div class="col-sm-8">${entity.linkman}</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><small class="text-red">*</small>办学模式：</label>
						<div class="col-sm-8">
							<p class="form-control-static">
								<input type="radio" name="gjtOrg.schoolModel" value="1" class="flat-red"
									<c:if test="${empty entity.gjtOrg.schoolModel || entity.gjtOrg.schoolModel==1}">checked="checked"</c:if>> 学历办学
								&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio" name="gjtOrg.schoolModel" value="2" class="flat-red"
									<c:if test="${entity.gjtOrg.schoolModel==2}">checked="checked"</c:if>> 中职院校 &nbsp;&nbsp;&nbsp;&nbsp; <input type="radio"
									name="gjtOrg.schoolModel" value="3" class="flat-red" <c:if test="${entity.gjtOrg.schoolModel==3}">checked="checked"</c:if>> 院校模式
							</p>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">描述：</label>
						<div class="col-sm-8">
							<%-- <pre id="memo"></pre>
							<input type="hidden" value="${entity.memo}" id="memoToo"> --%>
							<p class="form-control-static">${entity.memo}</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="box no-border margin-bottom-none">
			
			<div class="box margin-bottom-none">
				<div class="nav-tabs-custom no-margin">
					<div class="tab-pane ">
						<div class="approval-list clearfix">
							<!-- 状态2 -->
							<c:forEach items="${list }" var="item">
								<c:if test="${item.auditStep eq '1' }">
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6">招生办：${item.createdBy}</b>
											<span class="gray9 text-no-bold f12">
												<fmt:formatDate value="${item.createdDt }" type="both" />
												发布
											</span>
											<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
											<label class="state-lb">新建学习中心</label>
										</dt>
									</dl>
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6"> ${item.auditOperatorRole} 初审</b>
											<span class="gray9 text-no-bold f12">
												<fmt:formatDate value="${item.auditDt }" type="both" />
											</span>
											<span class="fa fa-fw fa-check-circle text-green"></span>
											<label class="state-lb text-green">审核通过</label>
										</dt>
										<dd>
											<div class="txt">
												<p>${item.auditContent }</p>
												<div class="gray9 text-right">审核人：${item.auditOperator}</div>
												<i class="arrow-top"></i>
											</div>
										</dd>
									</dl>
								</c:if>

								<c:if test="${item.auditStep eq '2' }">
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6">${item.auditOperatorRole} 复审</b>
											<span class="gray9 text-no-bold f12">
												<fmt:formatDate value="${item.auditDt }" type="both" />
											</span>
											<c:if test="${item.auditState eq '1'}">
												<span class="fa fa-fw fa-check-circle text-green"></span>
												<label class="state-lb text-green">审核通过</label>
											</c:if>
											<c:if test="${item.auditState eq '2'}">
												<span class="fa fa-fw fa-times-circle text-red"></span>
												<label class="state-lb text-red">审核不通过</label>
											</c:if>
										</dt>
										<dd>
											<div class="txt">
												<p>${item.auditContent }</p>
												<div class="gray9 text-right">审核人：${item.auditOperator}</div>
												<i class="arrow-top"></i>
											</div>
										</dd>
									</dl>
								</c:if>

								<c:if test="${item.auditStep eq '3' }">
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6">${item.auditOperatorRole} 终审</b>
											<span class="gray9 text-no-bold f12">
												<fmt:formatDate value="${item.auditDt }" type="both" />
											</span>
											<c:if test="${item.auditState eq '1'}">
												<span class="fa fa-fw fa-check-circle text-green"></span>
												<label class="state-lb text-green">审核通过</label>
											</c:if>
											<c:if test="${item.auditState eq '2'}">
												<span class="fa fa-fw fa-times-circle text-red"></span>
												<label class="state-lb text-red">审核不通过</label>
											</c:if>
										</dt>
										<dd>
											<div class="txt">
												<p>${item.auditContent }</p>
												<div class="gray9 text-right">审核人：${item.auditOperator}</div>
												<i class="arrow-top"></i>
											</div>
										</dd>
									</dl>
								</c:if>
							</c:forEach>

							<c:forEach items="${list }" var="item" varStatus="var">
								<c:if test="${var.last }">
									<c:if test="${item.auditStep eq '1' and isXueZhi }">
										<dl class="approval-item white-border">
											<dt class="clearfix">
												<b class="a-tit gray6">学支部复审</b>
												<span class="fa fa-fw fa-dot-circle-o text-yellow"></span>
												<label class="state-lb pending-state">待审核</label>
											</dt>
											<dd>
												<form class="theform">
													<div class="col-xs-12 no-padding position-relative">
														<textarea class="form-control" id="content" rows="3" placeholder="如没有意见，则填写：同意！" >
														</textarea>
													</div>
													<div>
														<button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10 btn-save-edit" data-role="btn-pass" data-id="2">审核不通过</button>
														<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10" data-role="btn-pass" data-id="1">审核通过</button>
													</div>
												</form>
											</dd>
										</dl>
									</c:if>
									<c:if test="${item.auditStep eq '2' and isYuanZhang }">
										<dl class="approval-item white-border">
											<dt class="clearfix">
												<b class="a-tit gray6">院长终审
												</b>
												<span class="fa fa-fw fa-dot-circle-o text-yellow"></span>
												<label class="state-lb pending-state">待审核</label>
											</dt>
											<dd>
												<form class="theform">
													<div class="col-xs-12 no-padding position-relative">
														<textarea class="form-control" id="content" rows="3" placeholder="如没有意见，则填写：同意！" >
														</textarea>
													</div>
													<div>
														<button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10 btn-save-edit" data-role="btn-pass" data-id="2">审核不通过</button>
														<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10" data-role="btn-pass" data-id="1">审核通过</button>
													</div>
												</form>
											</dd>
										</dl>
									</c:if>
								</c:if>
							</c:forEach>
							<p>&nbsp;</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>


	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		$(function() {
			$('.box-body :input').attr("disabled", "disabled");
			// 加载省市区联动查询
			$('#province').select_district($('#city'), $("#district"),
					$("#district").val());

			//Flat red color scheme for iCheck
			$('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
						checkboxClass : 'icheckbox_flat-green',
						radioClass : 'iradio_flat-green'
					});
			/* $('#memo').html($('#memoToo').val()); */

			//审核通过
			$('[data-role="btn-pass"]').click(function(event) {
				var $status = $(this).data('id');
				var $id=$('#orgId').val();
				var $content=$('#content').val();
				
				$.confirm({
					title : '提示',
					content : '确认操作？',
					confirmButton : '确认',
					icon : 'fa fa-warning',
					cancelButton : '取消',
					confirmButtonClass : 'btn-primary',
					closeIcon : true,
					closeIconClass : 'fa fa-close',
					confirm : function() {
						var dialog1 = $.mydialog({
							id : 'dialog-1',
							width : 150,
							height : 50,
							backdrop : false,
							fade : true,
							showCloseIco : false,
							zIndex : 11000,
							content : '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
						});
						 $.post("${ctx}/organization/studyCenter/audit",{id:$id,audit:$status,content:$content},function(data){
							 dialog1.find(".text-center.pad-t15").html(data.message+'<i class="icon fa fa-check-circle"></i>');
							 setTimeout(function() {
								 window.location.href=window.location.href;
								}, 1500);
						},"json");  
					}
				});
			});
		});
	</script>
</body>

</html>
