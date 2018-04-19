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
	<button class="btn btn-default btn-sm pull-right min-width-60px" id="go_for_back" onclick="window.history.back()">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">招生点管理</a></li>
		<li class="active">招生点详情</li>
	</ol>
</section>
<section class="content" data-id="0">
	<div class="box margin-bottom-none">
		<div class="box-body">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">上级单位：</label>
					<div class="col-sm-8 ">
							<p class=" form-control-static">${entity.parentGjtOrg.orgName }</p>
							<input type="hidden"  id="orgId" value="${entity.id}">
							<input type="hidden"  id="orgCode" value="${entity.code}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">招生点代码：</label>
					<div class="col-sm-8 ">
						<p class=" form-control-static">${entity.code}</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">招生点名称：</label>
					<div class="col-sm-8 ">
						<p class=" form-control-static">${entity.orgName}</p>
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-2 control-label">招生点合同编号：</label>
					<div class="col-sm-8 ">
						<p class=" form-control-static">${entity.gjtStudyCenter.compactNo}</p>
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-2 control-label">联系电话：</label>
					<div class="col-sm-8 ">
						<p class=" form-control-static">${entity.gjtStudyCenter.linkTel}</p>
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-2 control-label">联系人：</label>
					<div class="col-sm-8">
						<p class=" form-control-static">${entity.gjtStudyCenter.linkman}</p>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>招生点服务项</label>
					<div class="col-sm-8 form-control-static" >
						<c:forEach items="${studyServiceInfoMap }" var="map">
							<input type="checkbox" value="${map.key }" name="serviceAreas" <c:if test="${serviceAreas[map.key] }">checked='checked'</c:if>>
							${map.value } &nbsp;&nbsp;
						</c:forEach>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">所在区域：</label>
					<div class="col-sm-8">
						<div class="row">
							<div class="col-sm-4">
								<select id="province" class=" show-tick form-control" >
									<option value="" selected="selected"></option>
								</select>
							</div>
							<div class="col-sm-4">
								<select id="city" class=" show-tick form-control" >
									<option value="" selected="selected"></option>
								</select>
							</div>
							<div class="col-sm-4">
								<select id="district" name="district" class=" show-tick form-control" >
									<option value="${entity.gjtStudyCenter.district}" selected="selected"></option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">详细地址:</label>
					<div class="col-sm-8">
						<p class=" form-control-static">${entity.gjtStudyCenter.officeAddr}</p>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">简介</label>
					<div class="col-sm-8">
						<pre>${entity.gjtStudyCenter.memo}	</pre>
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
							<c:forEach items="${list }" var="item" varStatus="var">
								<c:if test="${item.auditStep eq '1'  and var.first }">
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6">招生办：${item.createdBy}</b>
											<span class="gray9 text-no-bold f12">
												<fmt:formatDate value="${item.createdDt }" type="both" />
												发布
											</span>
											<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
											<label class="state-lb">新建招生点</label>
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
								
								
								<c:if test="${item.auditStep eq '1'  and var.first ne true }">
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6"> ${item.auditOperatorRole} 重新提交</b>
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

							<%-- <c:forEach items="${list }" var="item" varStatus="var">
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
									<c:if test="${item.auditStep eq '2' and isYuanZhang  and item.auditState eq '1'}">
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
							</c:forEach> --%>
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
			$('#province').select_district($('#city'), $("#district"),$("#district").val());

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
				var $orgCode=$('#orgCode').val();
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
						 $.post("${ctx}/organization/enrollStudent/audit",{orgCode:$orgCode,id:$id,audit:$status,content:$content},function(data){
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
</html>
