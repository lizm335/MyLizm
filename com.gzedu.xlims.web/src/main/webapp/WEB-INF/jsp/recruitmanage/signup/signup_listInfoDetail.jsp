<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">招生计划</a></li>
		<li class="active">报读信息详情</li>
	</ol>
</section>
<section class="content">
	<div class="box margin-bottom-none">
		<div class="box-body">
			<div class="panel panel-default margin_t10">
				<div class="panel-heading">
					<h3 class="panel-title text-bold">个人信息</h3>
				</div>
				<div class="panel-body">
					<div class="table-responsive margin-bottom-none">
						<table class="table-gray-th">
							<tr>
								<th width="15%">姓名：</th>
								<td width="35%">${info.XM }</td>

								<th width="15%">学号：</th>
								<td width="35%">${info.XH }</td>
							</tr>
							<tr>
								<th>身份证：</th>
								<td>
								<shiro:hasPermission name="/personal/index$privacyJurisdiction">
									${info.SFZH}
								</shiro:hasPermission>
								<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
									<c:if test="${not empty info.SFZH }">
									${fn:substring(info.SFZH,0, 4)}******${fn:substring(info.SFZH,14, (info.SFZH).length())}
									</c:if>
								</shiro:lacksPermission>
								
								</td>
								
								<th>手机：</th>
								<td>
									<shiro:hasPermission name="/personal/index$privacyJurisdiction">
										${info.SJH}
									</shiro:hasPermission>
									<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
										<c:if test="${not empty info.SJH }">
											${fn:substring(info.SJH,0, 3)}******${fn:substring(info.SJH,8, (info.SJH).length())}
										</c:if>
									</shiro:lacksPermission>
								</td>
							</tr>
							<tr>
								<th>邮箱：</th>
								<td>
								<shiro:hasPermission name="/personal/index$privacyJurisdiction">
										${info.DZXX}
									</shiro:hasPermission>
									<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
									<c:if test="${not empty info.DZXX }">
										${fn:substring(info.DZXX,0, 2)}***${fn:substring(info.DZXX,6, (info.DZXX).length())}
									</c:if>
									</shiro:lacksPermission>
								</td>
								
								<th>单位名称：</th>
								<td>${info.SC_CO }</td>
							</tr>
							<tr>
								<th>联系地址：</th>
								<td colspan="3">${info.SC_CO_ADDR }</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="panel panel-default margin_t10">
				<div class="panel-heading">
					<h3 class="panel-title text-bold">报读明细</h3>
				</div>
				<div class="panel-body">
					<div class="table-responsive margin-bottom-none">
						<table class="table-gray-th">
							<tr>
								<th width="15%">院校：</th>
								<td width="35%">${info.XXMC }</td>

								<th width="15%">学期：</th>
								<td width="35%">${info.GRADE_NAME }</td>
							</tr>
							<tr>
								<th>层次：</th>
								<td>${info.PYCC_NAME }</td>
								
								<th>专业：</th>
								<td>${info.ZYMC }</td>
							</tr>
							<tr>
								<th>学习中心：</th>
								<td>${info.SC_NAME }</td>
								
								<th>入学时间：</th>
								<td>${info.CREATED_DT }</td>
							</tr>
							<tr>
								<th>缴费状态：</th>
								<td>
									<c:if test="${(info.CHARGE eq '已全额缴费') || (info.CHARGE eq '已部分缴费')}">
										<span class="text-green">${info.CHARGE }<br></span>
									</c:if>
									<c:if test="${info.CHARGE eq '待缴费'}">
										<span class="text-orange">${info.CHARGE }<br></span>
									</c:if>
									<c:if test="${info.CHARGE eq '已欠费'}">
										<span class="text-red">${info.CHARGE }<br></span>
									</c:if>
									<c:if test="${info.CHARGE eq '--'}">
										${info.CHARGE }
									</c:if>
									<c:if test="${info.CHARGE_FLG eq '1'}">
										<c:if test="${not empty info.CHARGE_DESCRIB }">
											<small class="gray9">（${info.CHARGE_DESCRIB}）</small>
										</c:if>
									</c:if>
								</td>
								
								<th>资料状态：</th>
								<td>
									<c:if test="${info.AUDIT_STATE eq '审核通过'}">
										<span class="text-green">${info.AUDIT_STATE }</span>
									</c:if>
									<c:if test="${info.AUDIT_STATE eq '审核不通过'}">
										<span class="text-red">${info.AUDIT_STATE }</span>
									</c:if>
									<c:if test="${(info.AUDIT_STATE eq '审核中') || (info.AUDIT_STATE eq '待审核') || (info.AUDIT_STATE eq '未审核')}">
										<span class="text-orange">${info.AUDIT_STATE }</span>
									</c:if>
                                    <c:if test="${info.AUDIT_STATE eq '--'}">
                                        ${info.AUDIT_STATE }
                                    </c:if>
								</td>
							</tr>
							<tr>
								<th>学籍状态：</th>
								<td colspan="3">${info.XJZT }</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
//取消
$("button[data-role='back-close']").click(function(event) {
	parent.$.closeDialog(frameElement.api);
});
</script>
</body>
</html>
