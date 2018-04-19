<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-学籍资料</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<style type="text/css">
		.timeline > li > .timeline-item {
			background: #ECF0F5 none repeat scroll 0 0;
		}
	</style>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb oh">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">招生管理</a></li>
			<li><a href="#">报读信息</a></li>
			<li class="active">报读信息明细</li>
		</ol>
	</section>

	<section class="content">

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box">
			<div class="box-body">
				<div class="media pad">
					<div class="media-left" style="padding-right:25px;">
						<img id ="headImgId" src="${not empty item.avatar ? item.avatar : ctx.concat('/static/images/headImg04.png')}" class="img-circle" style="width:112px;height:112px;" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'">
					</div>
					<div class="media-body">
						<h3 class="margin_t10">
							${item.xm}
							<small class="f14">(<dic:getLabel typeCode="Sex" code="${item.xbm }" />)</small>
						</h3>
						<div class="row">
							<div class="col-xs-6 col-sm-4 pad-b5">
								<b>学号:</b> <span>${item.xh}</span>
							</div>
							<div class="col-xs-6 col-sm-4 pad-b5">
								<b>手机:</b>
								<span>
									<shiro:hasPermission name="/personal/index$privacyJurisdiction">
										${item.sjh}
									</shiro:hasPermission>
									<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
										<c:if test="${not empty item.sjh }">
										${fn:substring(item.sjh,0, 3)}******${fn:substring(item.sjh,8, (item.sjh).length())}
										</c:if>
									</shiro:lacksPermission>
								
								</span>
							</div>
							<div class="col-xs-6 col-sm-4 pad-b5">
								<b>邮箱:</b> <span>${item.dzxx}</span>
							</div>

						</div>
					</div>
				</div>

			</div>
			<div class="box-footer">
				<div class="row stu-info-status">
					<div class="col-xs-4">
						<c:if test="${item.isEnteringSchool==1}">
							<div class="text-green f24 text-center">已确认</div>
						</c:if>
						<c:if test="${item.isEnteringSchool!=1}">
							<div class="text-red f24 text-center">未确认</div>
						</c:if>
						<div class="text-center gray6">入学确认</div>
					</div>
					<div class="col-xs-4">
						<c:choose>
							<c:when test="${item.gjtSignup.auditState=='0'}"><div class="text-red f24 text-center">审核不通过</div></c:when>
							<c:when test="${item.gjtSignup.auditState=='1'}"><div class="text-green f24 text-center">审核通过</div></c:when>
							<c:when test="${item.gjtSignup.auditState=='2'}"><div class="text-orange f24 text-center">重提交</div></c:when>
							<c:when test="${item.gjtSignup.auditState=='3'}"><div class="text-orange f24 text-center">待审核</div></c:when>
							<c:when test="${item.gjtSignup.auditState=='4'}"><div class="text-yellow f24 text-center">待提交</div></c:when>
						</c:choose>
						<div class="text-center gray6">资料提交</div>
					</div>
					<div class="col-xs-4">
						${student.perfectStatus==1?'<div class="text-green f24 text-center">已完善</div>':'<div class="text-red f24 text-center">未完善</div>' }
						<div class="text-center gray6">资料状态</div>
					</div>
				</div>
			</div>
		</div>

		<div class="box no-border margin-bottom-none">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li class="active"><a href="#tab_notice_1" data-toggle="tab">报名资料<small class="label bg-red reset-label margin_l5">未完善</small></a></li>
					<li><a href="#tab_notice_2" data-toggle="tab">证件资料<small class="label bg-red reset-label margin_l5">未完善</small></a></li>
					<li><a href="#tab_notice_3" data-toggle="tab">资料审批
						<c:if test="${item.gjtSignup.auditState==0}"><small class="label bg-red reset-label margin_l5">审核不通过</small></c:if>
						<c:if test="${item.gjtSignup.auditState==1}"><small class="label bg-green reset-label margin_l5">审核通过</small></c:if>
						<c:if test="${item.gjtSignup.auditState!=0 && item.gjtSignup.auditState!=1}"><small class="label bg-orange reset-label margin_l5">待审核</small></c:if>
					</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab_notice_1">
						<div class="clearfix">
							<%--<button class="btn btn-default fr margin_l10"><i class="fa fa-fw fa-download"></i> 下载资料</button>
							<button class="btn btn-default fr"><i class="fa fa-fw fa-print"></i> 打印资料</button>--%>
						</div>
						<div class="panel panel-default margin_t10">
							<div class="panel-heading">
								<h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-1" role="button">
									<small class="label bg-yellow reset-label fr">待完善</small>
									<i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
									<span class="margin-r-5">基础信息</span>
								</h3>
							</div>
							<div id="info-box-1" class="collapse in">
								<form class="theform">
									<div class="panel-body content-group">
										<div class="cnt-box-body no-padding">
											<table class="table-gray-th">
												<tr>
													<th>
														姓名
													</th>
													<td>
														${item.xm}
													</td>
													<th>
														学号
													</th>
													<td>
														${item.xh}
													</td>
												</tr>
												<tr>
													<th>
														性别
													</th>
													<td>
														<dic:getLabel typeCode="Sex" code="${item.xbm }" />
													</td>
													<th>
														证件类型
													</th>
													<td>
														${item.certificatetype}
													</td>
												</tr>
												<tr>
													<th>
														证件号
													</th>
													<td>
														${item.sfzh}
													</td>
													<th>
														籍贯
													</th>
													<td>
														${item.nativeplace}
													</td>
												</tr>
												<tr>
													<th>
														民族
													</th>
													<td>
														<dic:getLabel typeCode="NationalityCode" code="${item.mzm}"/>
													</td>
													<th>
														政治面貌
													</th>
													<td>
														${item.politicsstatus}
													</td>
												</tr>
												<tr>
													<th>
														婚姻状态
													</th>
													<td>
														<dic:getLabel typeCode="MaritalStatusCode" code="${item.hyzkm}" />
													</td>
													<th>
														户口性质
													</th>
													<td>
														<dic:getLabel typeCode="AccountsNatureCode" code="${item.hkxzm}"/>
													</td>
												</tr>
												<tr>
													<th>
														出生日期
													</th>
													<td>
														${item.csrq}
													</td>
													<th>
														在职状况
													</th>
													<td>
														<dic:getLabel typeCode="OccupationStatus" code="${item.isonjob}"/>
													</td>
												</tr>
												<tr>
													<th>
														户籍所在地
													</th>
													<td colspan="3">
														${item.hkszd}
													</td>
												</tr>
											</table>
										</div>
									</div>
								</form>
							</div>
						</div>
						<div class="panel panel-default margin_t10">
							<div class="panel-heading">
								<h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button">
									<small class="label bg-yellow reset-label fr">待完善</small>
									<i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
									<span class="margin-r-5">通讯信息</span>
								</h3>
							</div>
							<div id="info-box-2" class="collapse in">
								<form class="theform">
									<div class="panel-body content-group overlay-wrapper position-relative">
										<div class="cnt-box-body no-padding">
											<div class="table-responsive margin-bottom-none">
												<table class="table-gray-th">
													<tr>
														<th>
															手机号码
														</th>
														<td>
															<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																${item.sjh}
															</shiro:hasPermission>
															<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
																<c:if test="${not empty item.sjh }">
																${fn:substring(item.sjh,0, 3)}******${fn:substring(item.sjh,8, (item.sjh).length())}
																</c:if>
															</shiro:lacksPermission>
														</td>
														<th>
															固定电话
														</th>
														<td>
															${item.lxdh}
														</td>
													</tr>
													<tr>
														<th>
															邮箱
														</th>
														<td>
															${item.dzxx}
														</td>
														<th>
															通信地址
														</th>
														<td>
															${item.txdz}
														</td>
													</tr>
													<tr>
														<th>
															邮编
														</th>
														<td>
															${item.yzbm}
														</td>
														<%--<th>
															第二联系人
														</th>
														<td>
															${item.scName}
														</td>
													</tr>
													<tr>
														<th>
															第二联系人电话
														</th>
														<td>
															${item.scPhone}
														</td>--%>
														<th>
															所在单位
														</th>
														<td>
															${item.scCo}
														</td>
													</tr>
													<tr>
														<th>
															单位地址
														</th>
														<td>
															${item.scCoAddr}
														</td>
														<th>岗位职务</th>
														<td>
															${item.gjtSignup.jobPost}
														</td>
													</tr>
												</table>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
						<div class="panel panel-default margin_t10">
							<div class="panel-heading">
								<h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button">
									<small class="label bg-green reset-label fr">已完善</small>
									<i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
									<span class="margin-r-5">学籍信息</span>
								</h3>
							</div>
							<div id="info-box-3" class="collapse in">
								<form class="theform">
									<div class="panel-body content-group">
										<div class="cnt-box-body no-padding">
											<table class="table-gray-th">
												<tr>
													<th>
														年级
													</th>
													<td>
														${item.gjtGrade.gradeName}
													</td>
													<th>
														专业
													</th>
													<td>
														${item.gjtSpecialty.zymc}
													</td>
												</tr>
												<tr>
													<th>
														层次
													</th>
													<td>
														<dic:getLabel typeCode="TrainingLevel" code="${item.pycc }" />
													</td>
													<th>
														学习方式
													</th>
													<td>
														2.5年<%--${item.academic}--%>
													</td>
												</tr>
												<tr>
													<th>
														报读院校
													</th>
													<td>
														${item.gjtSchoolInfo.xxmc}
													</td>
													<th>
														班级
													</th>
													<td>
														${item.userclass}
													</td>
												</tr>
												<tr>
													<th>
														学习中心
													</th>
													<td colspan="3">
														${item.gjtStudyCenter.scName}
													</td>
												</tr>
											</table>
										</div>
									</div>
								</form>
							</div>
						</div>
						<div class="panel panel-default margin_t10">
							<div class="panel-heading">
								<h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button">
									<small class="label bg-yellow reset-label fr">待完善</small>
									<i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
									<span class="margin-r-5">原最高学历</span>
								</h3>
							</div>
							<div id="info-box-4" class="collapse in">
								<form class="theform">
									<div class="panel-body content-group overlay-wrapper position-relative">
										<div class="cnt-box-body no-padding">
											<div class="table-responsive margin-bottom-none">
												<table class="table-gray-th">
													<tr>
														<th>
															原学历层次
														</th>
														<td>
															${item.exedulevel}
														</td>
														<th>
															原毕业学校
														</th>
														<td>
															${item.exschool}
														</td>
													</tr>
													<c:choose>
													<c:when test="${isUndergraduateCourse }">
														<tr>
															<th>
																毕业时间
															</th>
															<td>
																	${item.exgraduatedtime}
															</td>
															<th>
																原学科
															</th>
															<td>
																	${item.exsubject}
															</td>
														</tr>
														<tr>
															<th>
																原学科门类
															</th>
															<td>
																${item.exsubjectkind}
															</td>
															<th>
																原学历学习类型
															</th>
															<td>
																${item.exedubaktype}
															</td>
														</tr>
														<tr>
															<th>
																原学历所学专业
															</th>
															<td>
																${item.exedumajor}
															</td>
															<th>
																原学历毕业证书编号
															</th>
															<td>
																${item.excertificatenum}
															</td>
														</tr>
														<tr>
															<th>
																原学历证明材料
															</th>
															<td>
																${item.excertificateprove}
															</td>
															<th>
																原学历证明材料编号
															</th>
															<td>
																${item.excertificateprovenum}
															</td>
														</tr>
														<tr>
															<th>
																原学历姓名
															</th>
															<td>
																${item.exeduname}
															</td>
															<th>
																原学历证件类型
															</th>
															<td>
																身份证
															</td>
														</tr>
														<tr>
															<th>
																原学历证件号码
															</th>
															<td>
																${item.exedunum}
															</td>
															<th>
																是否电大毕业
															</th>
															<td>
																${item.isgraduatebytv}
															</td>
														</tr>
													</c:when>
													<c:otherwise>
														<tr>
															<th>
																毕业时间
															</th>
															<td>
																	${item.exgraduatedtime}
															</td>
															<th>
																是否电大毕业
															</th>
															<td>
																${item.isgraduatebytv}
															</td>
														</tr>
													</c:otherwise>
													</c:choose>
												</table>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
						<div class="panel panel-default margin_t10">
							<div class="panel-heading">
								<h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-5" role="button">
									<c:choose>
										<c:when test="${not empty signupCopyData['sign'] }">
											<small class="label bg-green reset-label fr">已完善</small>
										</c:when>
										<c:otherwise>
											<small class="label bg-yellow reset-label fr">待完善</small>
										</c:otherwise>
									</c:choose>
									<i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
									<span class="margin-r-5">签名</span>
								</h3>
							</div>
							<div id="info-box-5" class="collapse in">
								<div class="panel-body text-center">
									<img src="${signupCopyData['sign'] }" style="max-width: 750px;" />
								</div>
							</div>
						</div>
					</div>
					<div class="tab-pane" id="tab_notice_2">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title text-bold">
									<span class="margin-r-5">电子版免冠一寸蓝底证件照</span>
									<c:choose>
										<c:when test="${not empty signupCopyData['zp'] }">
											<small class="label bg-green reset-label fr">已完善</small>
										</c:when>
										<c:otherwise>
											<small class="label bg-yellow reset-label fr">待完善</small>
										</c:otherwise>
									</c:choose>
								</h3>
							</div>
							<div class="panel-body">
								<div class="row margin_t15">
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">电子版免冠一寸蓝底证件照</h4>
											<div class="cert-box has-upload cert-box-3">
												<div class="cert-img-box table-block">
													<div class="table-cell-block vertical-mid text-center">
														<img src="${signupCopyData['zp'] }" alt="User Image">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title text-bold">
									<span class="margin-r-5">身份证原件正反面扫描件</span>
									<c:choose>
										<c:when test="${not empty signupCopyData['sfz-z'] && not empty signupCopyData['sfz-f'] }">
											<small class="label bg-green reset-label fr">已完善</small>
										</c:when>
										<c:otherwise>
											<small class="label bg-yellow reset-label fr">待完善</small>
										</c:otherwise>
									</c:choose>
								</h3>
							</div>
							<div class="panel-body">
								<div class="row margin_t15">
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">身份证正面</h4>
											<div class="cert-box has-upload cert-box-4">
												<div class="cert-img-box table-block">
													<div class="table-cell-block vertical-mid text-center">
														<img src="${signupCopyData['sfz-z']}" alt="User Image">
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">身份证反面</h4>
											<div class="cert-box has-upload cert-box-5">
												<div class="cert-img-box table-block">
													<div class="table-cell-block vertical-mid text-center">
														<img src="${signupCopyData['sfz-f']}" alt="User Image">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<c:if test="${isUndergraduateCourse }">

						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title text-bold">
									<span class="margin-r-5">毕业信息</span>
									<c:choose>
										<c:when test="${not empty signupCopyData['byz-z'] }">
											<small class="label bg-green reset-label fr">已完善</small>
										</c:when>
										<c:otherwise>
											<small class="label bg-yellow reset-label fr">待完善</small>
										</c:otherwise>
									</c:choose>
								</h3>
							</div>
							<div class="panel-body">
								<div class="row margin_t15">
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">毕业证原件</h4>
											<div class="cert-box has-upload cert-box-6">
												<div class="cert-img-box table-block">
													<div class="table-cell-block vertical-mid text-center">
														<img src="${signupCopyData['byz-z']}" alt="User Image">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title text-bold">
									<span class="margin-r-5">《中国高等教育学历认证报告》或《教育部学历证书电子注册备案表》</span>
									<c:choose>
										<c:when test="${not empty signupCopyData['xlz'] }">
											<small class="label bg-green reset-label fr">已完善</small>
										</c:when>
										<c:otherwise>
											<small class="label bg-yellow reset-label fr">待完善</small>
										</c:otherwise>
									</c:choose>
								</h3>
							</div>
							<div class="panel-body">
								<div class="row margin_t15">
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<div class="cert-box has-upload cert-box-8">
												<div class="cert-img-box table-block">
													<div class="table-cell-block vertical-mid text-center">
														<img src="${signupCopyData['xlz']}" alt="User Image">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						</c:if>
						<c:if test="${!isUndergraduateCourse }">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title text-bold">
									<span class="margin-r-5">最高学历证明</span>
									<c:choose>
										<c:when test="${not empty signupCopyData['byz-z'] || (not empty signupCopyData['xlzm'] && not empty signupCopyData['cns']) }">
											<small class="label bg-green reset-label fr">已完善</small>
										</c:when>
										<c:otherwise>
											<small class="label bg-yellow reset-label fr">待完善</small>
										</c:otherwise>
									</c:choose>
								</h3>
							</div>
							<div class="panel-body">
								<div style="padding-left: 10px !important; padding-top: 10px !important; color: #ff7200;"> * 高中或职高、中专、技校等同等学历的毕业证扫描件（一份）或【学历证明（广州市公司盖章）原件（一份）＋专科承诺书（一份）】</div>
								<div class="row margin_t15">
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">高中或职高、中专、技校等同等学历的毕业证扫描件</h4>
											<div class="cert-box has-upload cert-box-6">
												<div class="cert-img-box table-block">
													<div class="table-cell-block vertical-mid text-center">
														<img src="${signupCopyData['byz-z']}" alt="User Image">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="row margin_t15">
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">学历证明（广州市公司盖章）原件</h4>
											<div class="cert-box has-upload cert-box-8">
												<div class="cert-img-box table-block">
													<div class="table-cell-block vertical-mid text-center">
														<img src="${signupCopyData['xlzm']}" alt="User Image">
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">专科承诺书</h4>
											<div class="cert-box has-upload cert-box-8">
												<div class="cert-img-box table-block">
													<div class="table-cell-block vertical-mid text-center">
														<img src="${signupCopyData['cns']}" alt="User Image">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						</c:if>
					</div>
					<div class="tab-pane" id="tab_notice_3">
						<form id="inputForm" class="theform" role="form" action="${ctx }/edumanage/schoolRollInfo/audit.html" method="post">
						<input type="hidden" name="action" value="${action}" />
						<input type="hidden" name="studentId" value="${item.studentId}" />
						<input type="hidden" name="auditState" value="" />
						<div class="approval-list clearfix">
							<c:forEach var="record" items="${flowRecordList}" varStatus="s">
								<c:if test="${record.auditOperatorRole==1}">
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6">${s.index==0?'学员提交资料':'学员重交资料'}</b>
											<span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
											<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
										</dt>
									</dl>
								</c:if>
								<c:if test="${record.auditOperatorRole!=1}">
									<dl class="approval-item" <c:if test="${s.index==(fn:length(flowRecordList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
										<dt class="clearfix">
											<b class="a-tit gray6">${record.auditOperatorRole==2?'班主任初审':record.auditOperatorRole==3?'招生办复审':record.auditOperatorRole==4?'学籍科终审':''}</b>
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
											<c:if test="${record.auditOperatorRole==4}">
											<dd>
												<div class="col-xs-12 no-padding position-relative">
													<textarea name="auditContent" class="form-control" rows="3" placeholder="请输入资料审核备注，例如该学员的资料确认无误" datatype="*1-200" nullmsg="请输入内容！" errormsg="字数不能超过200"></textarea>
												</div>
												<div>
													<button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10 btn-audit" val="2" data-form-id="5">审核不通过</button>
													<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10 btn-audit" val="1" data-form-id="5">审核通过</button>
												</div>
											</dd>
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
						</form>
					</div>
				</div>
			</div>
		</div>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

	<script type="text/javascript" src="${ctx}/static/js/edumanage/schoolRollInfo/school_roll_info_form.js"></script>
	<script type="text/javascript">
		(function($) {
			var action = $('form#inputForm :input[name="action"]').val();
			if(action == 'audit') {
				$('ul.nav-tabs li:last a').trigger('click');
			}
		})(jQuery);
	</script>
</body>
</html>
