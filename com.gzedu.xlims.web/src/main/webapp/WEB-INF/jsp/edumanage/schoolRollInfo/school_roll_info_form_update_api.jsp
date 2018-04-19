<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-学籍资料</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<!-- iCheck 1.0.1 -->
	<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
	<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
	<!-- 省市县级联 -->
	<script src="${ctx}/static/plugins/jquery.cxselect/jquery.cxselect.min.js"></script>
	<!-- 学籍资料相关样式 -->
	<link rel="stylesheet" href="${ctx}/static/dist/css/signup-info.css">
</head>
<body class="inner-page-body" style="padding-top: 0px;">
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
							<div class="col-xs-6 col-sm-4 pad-b5">
								<b>层次:</b> <span><dic:getLabel typeCode="TrainingLevel" code="${item.pycc }" /></span>
							</div>
							<div class="col-xs-6 col-sm-4 pad-b5">
								<b>专业:</b> <span>${item.gjtSpecialty.zymc}</span>
							</div>
							<div class="col-xs-6 col-sm-4 pad-b5">
								<b>学习中心:</b> <span>${item.gjtStudyCenter.scName}</span>
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
						${item.perfectStatus==1?'<div class="text-green f24 text-center">已完善</div>':'<div class="text-red f24 text-center">未完善</div>' }
						<div class="text-center gray6">资料状态</div>
					</div>
					<div class="col-xs-4">
						<c:choose>
							<c:when test="${item.gjtSignup.auditState=='0'}"><div class="text-red f24 text-center">审核不通过</div></c:when>
							<c:when test="${item.gjtSignup.auditState=='1'}"><div class="text-green f24 text-center">审核通过</div></c:when>
							<c:otherwise><div class="text-orange f24 text-center">待审核</div></c:otherwise>
						</c:choose>
						<div class="text-center gray6">资料审批</div>
					</div>
				</div>
			</div>
		</div>

		<div class="box no-border margin-bottom-none">
			<form id="inputForm" class="theform" role="form" action="${ctx }/api/roll/update" method="post">
				<input type="hidden" name="action" value="${action}" />
				<input type="hidden" name="studentId" value="${item.studentId}" />
				<input type="hidden" name="nativeplace" value="${item.nativeplace}" />
				<input type="hidden" name="hkszd" value="${item.hkszd}" />

				<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li class="active"><a href="#tab_notice_1" data-toggle="tab">报名资料<small class="label bg-red reset-label margin_l5">未完善</small></a></li>
					<li><a href="#tab_notice_2" data-toggle="tab">证件资料<small class="label bg-red reset-label margin_l5">未完善</small></a></li>
				<div class="pull-right no-margin"></div>
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
								<div class="panel-body content-group">
									<div class="cnt-box-body no-padding">
										<table class="table-gray-th">
											<tr>
												<th>
													姓名
												</th>
												<td>
													<input type="text" class="form-control" name="xm" value="${item.xm}">
												</td>
												<th>
													学号
												</th>
												<td>
													<input type="text" class="form-control" name="xh" value="${item.xh}">
												</td>
											</tr>
											<tr>
												<th>
													性别
												</th>
												<td>
													<dic:selectBox name="xbm" typeCode="Sex" code="${item.xbm}" otherAttrs='class="selectpicker show-tick form-control" data-size="5"' />
												</td>
												<th>
													证件类型
												</th>
												<td>
													<select name="certificatetype"  class="selectpicker show-tick form-control" data-size="5">
														<c:if test="${not empty item.certificatetype}">
															<option value="${item.certificatetype}">${item.certificatetype}</option>
														</c:if>
														<c:if test="${item.certificatetype != '身份证'}">
															<option value="身份证">身份证</option>
														</c:if>
													</select>
												</td>
											</tr>
											<tr>
												<th>
													证件号
												</th>
												<td>
													<input type="text" class="form-control" name="sfzh" value="${item.sfzh}" maxlength="18">
												</td>
												<th>
													籍贯
												</th>
												<td required>
													<div id="nativeplace" class="row pcSeclet">
													  <div class="col-md-6">
														<select class="form-control province" data-value="${fn:split(item.nativeplace, ' ')[0]}"></select>
													  </div>
													  <div class="col-md-6">
														<select class="col-md-4 form-control city" data-value="${fn:split(item.nativeplace, ' ')[1]}"></select>
													  </div>
													</div>
												</td>
											</tr>
											<tr>
												<th>
													民族
												</th>
												<td required>
													<dic:selectBox name="mzm" typeCode="NationalityCode" code="${item.mzm}" otherAttrs='class="selectpicker show-tick form-control" data-size="10"' />
												</td>
												<th>
													政治面貌
												</th>
												<td required>
													<select name="politicsstatus"  class="selectpicker show-tick form-control" data-size="5">
														<option value="" selected="selected">请选择</option>
														<c:forEach items="${dicPoliticsstatuList}" var="d">
															<option value="${d.code}" <c:if test="${d.code==item.politicsstatus}">selected='selected'</c:if>>${d.name}</option>
														</c:forEach>
													</select>
												</td>
											</tr>
											<tr>
												<th>
													婚姻状态
												</th>
												<td required>
													<dic:selectBox name="hyzkm" typeCode="MaritalStatusCode" code="${item.hyzkm}" otherAttrs='class="selectpicker show-tick form-control" data-size="5"' />
												</td>
												<th>
													户口性质
												</th>
												<td required>
													<dic:selectBox name="hkxzm" typeCode="AccountsNatureCode" code="${item.hkxzm}" otherAttrs='class="selectpicker show-tick form-control" data-size="5"' />
												</td>
											</tr>
											<tr>
												<th>
													出生日期
												</th>
												<td required>
													<input type="text" class="form-control reservation2" name="csrq" value="${item.csrq}">
												</td>
												<th>
													在职状况
												</th>
												<td required>
													<dic:selectBox name="isonjob" typeCode="OccupationStatus" code="${item.isonjob}" otherAttrs='class="selectpicker show-tick form-control" data-size="5"' />
												</td>
											</tr>
											<tr>
												<th>
													户籍所在地
												</th>
												<td colspan="3" required>
													<div id="hkszd" class="row pcSeclet">
														<div class="col-md-2">
															<select class="form-control province" data-value="${fn:split(item.hkszd, ' ')[0]}"></select>
														</div>
														<div class="col-md-2">
															<select class="col-md-4 form-control city" data-value="${fn:split(item.hkszd, ' ')[1]}"></select>
														</div>
													</div>
												</td>
											</tr>
										</table>
									</div>
								</div>
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
								<div class="panel-body content-group overlay-wrapper position-relative">
									<div class="cnt-box-body no-padding">
										<div class="table-responsive margin-bottom-none">
											<table class="table-gray-th">
												<tr>
													<th>
														手机号码
													</th>
													<td>
														<input type="text" class="form-control" name="sjh" value="${item.sjh}" maxlength="11">
													</td>
													<th>
														固定电话
													</th>
													<td>
														<input type="text" class="form-control" name="lxdh" value="${item.lxdh}">
													</td>
												</tr>
												<tr>
													<th>
														邮箱
													</th>
													<td required>
														<input type="text" class="form-control" name="dzxx" value="${item.dzxx}">
													</td>
													<th>
														通信地址
													</th>
													<td required>
														<input type="text" class="form-control" name="txdz" value="${item.txdz}">
													</td>
												</tr>
												<tr>
													<th>
														邮编
													</th>
													<td required>
														<input type="text" class="form-control" name="yzbm" value="${item.yzbm}" maxlength="6">
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
													<td required>
														<input type="text" class="form-control" name="scCo" value="${item.scCo}">
													</td>
												</tr>
												<tr>
													<th>
														单位地址
													</th>
													<td required>
														<input type="text" class="form-control" name="scCoAddr" value="${item.scCoAddr}">
													</td>
													<th>岗位职务</th>
													<td required>
														<input type="text" class="form-control" name="jobPost" value="${item.gjtSignup.jobPost}">
													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
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
								<div class="panel-body content-group">
									<div class="cnt-box-body no-padding">
										<table class="table-gray-th">
											<tr>
												<th>
													学期
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
												<td>
													${item.gjtStudyCenter.scName}
												</td>
												<th>学籍状态</th>
												<td>
													<c:choose>
														<c:when test="${item.xjzt=='2'}">
															<span class="text-green"><dic:getLabel typeCode="StudentNumberStatus" code="${item.xjzt }" /><c:if test="${not empty item.rollRegisterDt}">（${item.rollRegisterDt}）</c:if></span>
														</c:when>
														<c:when test="${item.xjzt=='8'}">
															<span class="text-green"><dic:getLabel typeCode="StudentNumberStatus" code="${item.xjzt }" /></span>
														</c:when>
														<c:otherwise>
															<span class="text-red"><dic:getLabel typeCode="StudentNumberStatus" code="${item.xjzt }" /></span>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</table>
									</div>
								</div>
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
								<div class="panel-body content-group overlay-wrapper position-relative">
									<div class="cnt-box-body no-padding">
										<div class="table-responsive margin-bottom-none">
											<table class="table-gray-th">
												<tr>
													<th>
														原学历层次
													</th>
													<td required>
														<select name="exedulevel"  class="form-control">
															<option value="" selected="selected">请选择</option>
															<c:forEach items="${dicExedulevelList}" var="d">
																<option value="${d.code}" <c:if test="${d.code==item.exedulevel}">selected='selected'</c:if>>${d.name}</option>
															</c:forEach>
														</select>
													</td>
													<th>
														原毕业学校
													</th>
													<td required>
														<input type="text" class="form-control" name="exschool" value="${item.exschool}">
													</td>
												</tr>
												<%-- 电大续读生，以下选项不需要填写 --%>
												<c:if test="${item.userType!='42' }">
												<c:choose>
												<c:when test="${isUndergraduateCourse }">
													<tr>
														<th>
															毕业时间
														</th>
														<td required>
															<input type="text" class="form-control reservation" name="exgraduatedtime" value="${item.exgraduatedtime}">
														</td>
														<th>
															原学科
														</th>
														<td required>
															<select name="exsubject" class="form-control">
																<option value="" selected="selected">请选择</option>
																<c:forEach items="${dicExsubjectList}" var="d">
																	<option value="${d.code}" <c:if test="${d.code==item.exsubject}">selected='selected'</c:if>>${d.name}</option>
																</c:forEach>
															</select>
														</td>
													</tr>
													<tr>
														<th>
															原学科门类
														</th>
														<td required>
															<select name="exsubjectkind"  class="form-control">
																<option value="" selected="selected">请选择</option>
																<c:forEach items="${dicExsubjectList}" var="c">
																	<c:if test="${c.code==item.exsubject}">
																	<c:forEach items="${c.dicExsubjectkindList}" var="d">
																		<option value="${d.code}" <c:if test="${d.code==item.exsubjectkind}">selected='selected'</c:if>>${d.name}</option>
																	</c:forEach>
																	</c:if>
																</c:forEach>
															</select>
														</td>
														<th>
															原学历学习类型
														</th>
														<td required>
															<select name="exedubaktype"  class="selectpicker show-tick form-control" data-size="5">
																<option value="" selected="selected">请选择</option>
																<c:forEach items="${dicExedubaktypeList}" var="d">
																	<option value="${d.code}" <c:if test="${d.code==item.exedubaktype}">selected='selected'</c:if>>${d.name}</option>
																</c:forEach>
															</select>
														</td>
													</tr>
													<tr>
														<th>
															原学历所学专业
														</th>
														<td required>
															<input type="text" class="form-control" name="exedumajor" value="${item.exedumajor}">
														</td>
														<th>
															原学历毕业证书编号
														</th>
														<td required>
															<input type="text" class="form-control" name="excertificatenum" value="${item.excertificatenum}">
														</td>
													</tr>
													<tr>
														<th>
															原学历证明材料
														</th>
														<td required>
															<select name="excertificateprove"  class="selectpicker show-tick form-control" data-size="5">
																<option value="" selected="selected">请选择</option>
																<c:forEach items="${dicExcertificateproveList}" var="d">
																	<option value="${d.code}" <c:if test="${d.code==item.excertificateprove}">selected='selected'</c:if>>${d.name}</option>
																</c:forEach>
															</select>
														</td>
														<th>
															原学历证明材料编号
														</th>
														<td required>
															<input type="text" class="form-control" name="excertificateprovenum" value="${item.excertificateprovenum}">
														</td>
													</tr>
													<tr>
														<th>
															原学历姓名
														</th>
														<td required>
															<input type="text" class="form-control" name="exeduname" value="${item.exeduname}">
														</td>
														<th>
															原学历证件类型
														</th>
														<td>
															<select name="excertifiType"  class="selectpicker show-tick form-control" data-size="5">
																<c:forEach items="${dicExeduCertificateList}" var="d">
																	<option value="${d.code}">${d.name}</option>
																</c:forEach>
															</select>
														</td>
													</tr>
													<tr>
														<th>
															原学历证件号码
														</th>
														<td required>
															<input type="text" class="form-control" name="exedunum" value="${item.exedunum}">
														</td>
														<th>
															是否电大毕业
														</th>
														<td required>
															<select name="isgraduatebytv"  class="form-control">
																<option value="" selected="selected">请选择</option>
																<c:forEach items="${dicIsgraduatebytvList}" var="d">
																	<option value="${d.code}" <c:if test="${d.code==item.isgraduatebytv}">selected='selected'</c:if>>${d.name}</option>
																</c:forEach>
															</select>
														</td>
													</tr>
												</c:when>
												<c:otherwise>
													<tr>
														<th>
															毕业时间
														</th>
														<td required>
															<input type="text" class="form-control reservation" name="exgraduatedtime" value="${item.exgraduatedtime}">
														</td>
														<th>
															是否电大毕业
														</th>
														<td required>
															<select name="isgraduatebytv"  class="form-control">
																<option value="" selected="selected">请选择</option>
																<c:forEach items="${dicIsgraduatebytvList}" var="d">
																	<option value="${d.code}" <c:if test="${d.code==item.isgraduatebytv}">selected='selected'</c:if>>${d.name}</option>
																</c:forEach>
															</select>
														</td>
													</tr>
												</c:otherwise>
												</c:choose>
												</c:if>
											</table>
										</div>
									</div>
								</div>
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
									<div id="qrcodeDiv" class="margin-bottom-none no-shadow flat margin_t10 text-center">
										<div class="box-body">
											<div id="qrcode"></div>
											<div class="gray9">手机扫一扫，签名确认</div>
										</div>
									</div>
									<div id="autographDiv" class="margin-bottom-none no-shadow flat margin_t10 text-center" style="display: none;">
										<div class="box-body">
											<img id="autographImg" src="${signupCopyData['sign'] }" style="max-width: 750px;" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'" />
											<input type="hidden" id="autograph" name="sign" value="${signupCopyData['sign']}" />
											<div class="margin_t10">
												<button type="button" class="btn btn-warning min-width-90px re-sign">重新签名</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="margin_b20 text-center">
							<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10 btn-next">下一步</button>
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
											<div style="width:160px;">
												<div class="cert-box has-upload cert-box-3">
													<a href="javascript:void(0);" class="info-img-box">
														<img class="info-img" key="zp" src="${signupCopyData['zp'] }" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
														<div class="upload-btn" data-role="upload-img" data-object='{
											                	"title":"电子版免冠一寸蓝底证件照",
											                	"sampleImg":[]
											                }'><i></i><span>点击上传</span>
									                	</div>
													</a>
													<a href="${not empty signupCopyData['zp'] ? signupCopyData['zp'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
													<input type="hidden" class="img-val" name="zp" value="${signupCopyData['zp'] }">
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
								<div class="signupType">
									<input type="radio"  name="signupSfzType" value="0" class="flat-red" <c:if test="${item.gjtSignup.signupSfzType==0}">checked="checked"</c:if>> 中国居民身份证
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio"  name="signupSfzType" value="1" class="flat-red" <c:if test="${item.gjtSignup.signupSfzType==1}">checked="checked"</c:if>> 其他身份证件（中国居民临时身份证、港澳台、外籍人士身份证）
								</div>
								<div class="row margin_t15 signupSfzType" style="display: none;">
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">身份证正面</h4>
											<div class="cert-box has-upload cert-box-4">
												<a href="javascript:void(0);" class="info-img-box">
													<img class="info-img" key="sfzz" src="${signupCopyData['sfz-z']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
													<div class="upload-btn" data-role="upload-img" data-object='{
										                	"title":"身份证正面",
										                	"sampleImg":[]
										                }'><i></i><span>点击上传</span>
									                </div>
												</a>
												<a href="${not empty signupCopyData['sfz-z'] ? signupCopyData['sfz-z'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
												<input type="hidden" class="img-val" name="sfzz" value="${signupCopyData['sfz-z'] }" />
											</div>
										</div>
									</div>
									<div class="col-sm-6 margin_b15">
										<div class="cert-wrap">
											<h4 class="cert-title text-center f16">身份证反面</h4>
											<div class="cert-box has-upload cert-box-5">
												<a href="javascript:void(0);" class="info-img-box">
													<img class="info-img" key="sfzf" src="${signupCopyData['sfz-f']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
													<div class="upload-btn" data-role="upload-img" data-object='{
										                	"title":"身份证反面",
										                	"sampleImg":[]
										                }'><i></i><span>点击上传</span>
									                </div>
												</a>
												<a href="${not empty signupCopyData['sfz-f'] ? signupCopyData['sfz-f'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
												<input type="hidden" class="img-val" name="sfzf" value="${signupCopyData['sfz-f'] }" />
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<c:choose>
							<%-- 21 31 测试环境使用 --%>
							<c:when test="${item.userType=='11'||item.userType=='12'||item.userType=='21'||item.userType=='31' }">
								<c:if test="${isUndergraduateCourse }">
									<div class="panel panel-default">
										<div class="panel-heading">
											<h3 class="panel-title text-bold">
												<span class="margin-r-5">毕业信息</span>
												<c:choose>
													<c:when test="${((empty item.gjtSignup.signupByzType||item.gjtSignup.signupByzType==0) && not empty signupCopyData['byz-z'] && not empty signupCopyData['xlz']) || (item.gjtSignup.signupByzType==1 && not empty signupCopyData['dzzch'] && not empty signupCopyData['xsz']) }">
														<small class="label bg-green reset-label fr">已完善</small>
													</c:when>
													<c:otherwise>
														<small class="label bg-yellow reset-label fr">待完善</small>
													</c:otherwise>
												</c:choose>
											</h3>
										</div>
										<div class="panel-body">
											<div class="signupType">
												<input type="radio"  name="signupByzType" value="0" class="flat-red" <c:if test="${item.gjtSignup.signupByzType==0}">checked="checked"</c:if>> 毕业证原件+《中国高等教育学历认证报告》或《教 育部学历证书电子注册备案表》原件
												&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio"  name="signupByzType" value="1" class="flat-red" <c:if test="${item.gjtSignup.signupByzType==1}">checked="checked"</c:if>> 毕业电子注册号证明+学生证原件
											</div>
											<div class="row margin_t15 signupByzType" style="display: none;">
												<div class="col-sm-6 margin_b15">
													<div class="cert-wrap">
														<h4 class="cert-title text-center f16">毕业证原件</h4>
														<div class="cert-box has-upload cert-box-6">
															<a href="javascript:void(0);" class="info-img-box">
																<img class="info-img" key="byzz" src="${signupCopyData['byz-z']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
																<div class="upload-btn" data-role="upload-img" data-object='{
												                	"title":"毕业证原件",
												                	"sampleImg":[]
												                }'><i></i><span>点击上传</span>
												                </div>
															</a>
															<a href="${not empty signupCopyData['byz-z'] ? signupCopyData['byz-z'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
															<input type="hidden" class="img-val" name="byzz" value="${signupCopyData['byz-z'] }" />
														</div>
													</div>
												</div>
												<div class="col-sm-6 margin_b15">
													<div class="cert-wrap">
														<div style="width:160px;">
															<h4 class="cert-title text-center f16">《中国高等教育学历认证报告》或《教育部学历证书电子注册备案表》</h4>
															<div class="cert-box has-upload cert-box-8">
																<a href="javascript:void(0);" class="info-img-box">
																	<img class="info-img" key="xlz" src="${signupCopyData['xlz']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
																	<div class="upload-btn" data-role="upload-img" data-object='{
													                	"title":"《中国高等教育学历认证报告》或《教育部学历证书电子注册备案表》",
													                	"sampleImg":[]
													                }'><i></i><span>点击上传</span>
													                </div>
																</a>
																<a href="${not empty signupCopyData['xlz'] ? signupCopyData['xlz'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
																<input type="hidden" class="img-val" name="xlz" value="${signupCopyData['xlz'] }" />
															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="row margin_t15 signupByzType" style="display: none;">
												<div class="col-sm-6 margin_b15">
													<div class="cert-wrap">
														<div style="width:160px;">
															<h4 class="cert-title text-center f16">毕业电子注册号证明</h4>
															<div class="cert-box has-upload cert-box-6">
																<a href="javascript:void(0);" class="info-img-box">
																	<img class="info-img" key="dzzch" src="${signupCopyData['dzzch']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
																	<div class="upload-btn" data-role="upload-img" data-object='{
													                	"title":"毕业电子注册号证明",
													                	"sampleImg":[]
													                }'><i></i><span>点击上传</span>
													                </div>
																</a>
																<a href="${not empty signupCopyData['dzzch'] ? signupCopyData['dzzch'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
																<input type="hidden" class="img-val" name="dzzch" value="${signupCopyData['dzzch'] }" />
															</div>
														</div>
													</div>
												</div>
												<div class="col-sm-6 margin_b15">
													<div class="cert-wrap">
														<h4 class="cert-title text-center f16">国家开放大学或广州电大学生证原件</h4>
														<div class="cert-box has-upload cert-box-6">
															<a href="javascript:void(0);" class="info-img-box">
																<img class="info-img" key="xsz" src="${signupCopyData['xsz']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
																<div class="upload-btn" data-role="upload-img" data-object='{
												                	"title":"国家开放大学或广州电大学生证原件",
												                	"sampleImg":[]
												                }'><i></i><span>点击上传</span>
												                </div>
															</a>
															<a href="${not empty signupCopyData['xsz'] ? signupCopyData['xsz'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
															<input type="hidden" class="img-val" name="xsz" value="${signupCopyData['xsz'] }" />
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
													<c:when test="${((empty item.gjtSignup.zgxlRadioType||item.gjtSignup.zgxlRadioType==0) && not empty signupCopyData['byz-z']) || (item.gjtSignup.zgxlRadioType==1 && not empty signupCopyData['xlzm'] && not empty signupCopyData['cns']) }">
														<small class="label bg-green reset-label fr">已完善</small>
													</c:when>
													<c:otherwise>
														<small class="label bg-yellow reset-label fr">待完善</small>
													</c:otherwise>
												</c:choose>
											</h3>
										</div>
										<div class="panel-body">
											<div style="padding-bottom: 10px !important; color: #ff7200;"> * 高中或职高、中专、技校等同等学历的毕业证扫描件（一份）或【学历证明（广州市公司盖章）原件（一份）＋专科承诺书（一份）】</div>
											<div class="signupType">
												<input type="radio"  name="zgxlRadioType" value="0" class="flat-red" <c:if test="${item.gjtSignup.zgxlRadioType==0}">checked="checked"</c:if>> 高中或职高、中专、技校等同等学历的毕业证扫描件
												&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio"  name="zgxlRadioType" value="1" class="flat-red" <c:if test="${item.gjtSignup.zgxlRadioType==1}">checked="checked"</c:if>> 学历证明（广州市公司盖章）原件＋专科承诺书
											</div>
											<div class="row margin_t15 zgxlRadioType" style="display: none;">
												<div class="col-sm-6 margin_b15">
													<div class="cert-wrap">
														<h4 class="cert-title text-center f16">高中或职高、中专、技校等同等学历的毕业证扫描件</h4>
														<div class="cert-box has-upload cert-box-6">
															<a href="javascript:void(0);" class="info-img-box">
																<img class="info-img" key="byzz" src="${signupCopyData['byz-z']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
																<div class="upload-btn" data-role="upload-img" data-object='{
												                	"title":"高中或职高、中专、技校等同等学历的毕业证扫描件",
												                	"sampleImg":[]
												                }'><i></i><span>点击上传</span>
												                </div>
															</a>
															<a href="${not empty signupCopyData['byz-z'] ? signupCopyData['byz-z'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
															<input type="hidden" class="img-val" name="byzz" value="${signupCopyData['byz-z'] }" />
														</div>
													</div>
												</div>
											</div>
											<div class="row margin_t15 zgxlRadioType" style="display: none;">
												<div class="col-sm-6 margin_b15">
													<div class="cert-wrap">
														<div style="width: 160px;">
															<h4 class="cert-title text-center f16">学历证明（广州市公司盖章）原件</h4>
															<div class="cert-box has-upload cert-box-8">
																<a href="javascript:void(0);" class="info-img-box">
																	<img class="info-img" key="xlzm" src="${signupCopyData['xlzm']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
																	<div class="upload-btn" data-role="upload-img" data-object='{
													                	"title":"学历证明（广州市公司盖章）原件",
													                	"sampleImg":[]
													                }'><i></i><span>点击上传</span>
													                </div>
																</a>
																<a href="${not empty signupCopyData['xlzm'] ? signupCopyData['xlzm'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
																<input type="hidden" class="img-val" name="xlzm" value="${signupCopyData['xlzm'] }" />
															</div>
														</div>
													</div>
												</div>
												<div class="col-sm-6 margin_b15">
													<div class="cert-wrap">
														<div style="width: 160px;">
															<h4 class="cert-title text-center f16">专科承诺书</h4>
															<div class="cert-box has-upload cert-box-8">
																<a href="javascript:void(0);" class="info-img-box">
																	<img class="info-img" key="cns" src="${signupCopyData['cns']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
																	<div class="upload-btn" data-role="upload-img" data-object='{
													                	"title":"专科承诺书",
													                	"sampleImg":[]
													                }'><i></i><span>点击上传</span>
													                </div>
																</a>
																<a href="${not empty signupCopyData['cns'] ? signupCopyData['cns'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
																<input type="hidden" class="img-val" name="cns" value="${signupCopyData['cns'] }" />
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</c:if>
							</c:when>
							<c:when test="${item.userType=='42' }">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">国家开放大学或广州电大学生证原件</span>
											<c:choose>
												<c:when test="${not empty signupCopyData['xsz'] }">
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
													<h4 class="cert-title text-center f16">学生证</h4>
													<div class="cert-box has-upload cert-box-6">
														<a href="javascript:void(0);" class="info-img-box">
															<img class="info-img" key="xsz" src="${signupCopyData['xsz']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
															<div class="upload-btn" data-role="upload-img" data-object='{
											                	"title":"学生证",
											                	"sampleImg":[]
											                }'><i></i><span>点击上传</span>
											                </div>
														</a>
														<a href="${not empty signupCopyData['xsz'] ? signupCopyData['xsz'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
														<input type="hidden" class="img-val" name="xsz" value="${signupCopyData['xsz'] }" />
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">成绩单</span>
											<c:choose>
												<c:when test="${not empty signupCopyData['cjd'] }">
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
													<h4 class="cert-title text-center f16">成绩单</h4>
													<div class="cert-box has-upload cert-box-6">
														<a href="javascript:void(0);" class="info-img-box">
															<img class="info-img" key="cjd" src="${signupCopyData['cjd']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
															<div class="upload-btn" data-role="upload-img" data-object='{
											                	"title":"成绩单",
											                	"sampleImg":[]
											                }'><i></i><span>点击上传</span>
											                </div>
														</a>
														<a href="${not empty signupCopyData['cjd'] ? signupCopyData['cjd'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
														<input type="hidden" class="img-val" name="cjd" value="${signupCopyData['cjd'] }" />
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:when>
							<c:when test="${item.userType=='51' }">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">成绩单</span>
											<c:choose>
												<c:when test="${not empty signupCopyData['cjd'] }">
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
													<h4 class="cert-title text-center f16">成绩单</h4>
													<div class="cert-box has-upload cert-box-6">
														<a href="javascript:void(0);" class="info-img-box">
															<img class="info-img" key="cjd" src="${signupCopyData['cjd']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
															<div class="upload-btn" data-role="upload-img" data-object='{
											                	"title":"成绩单",
											                	"sampleImg":[]
											                }'><i></i><span>点击上传</span>
											                </div>
														</a>
														<a href="${not empty signupCopyData['cjd'] ? signupCopyData['cjd'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
														<input type="hidden" class="img-val" name="cjd" value="${signupCopyData['cjd'] }" />
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">录取名册或入学通知书</span>
											<c:choose>
												<c:when test="${not empty signupCopyData['lqmc'] }">
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
													<h4 class="cert-title text-center f16">录取名册或入学通知书</h4>
													<div class="cert-box has-upload cert-box-6">
														<a href="javascript:void(0);" class="info-img-box">
															<img class="info-img" key="lqmc" src="${signupCopyData['lqmc']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
															<div class="upload-btn" data-role="upload-img" data-object='{
											                	"title":"录取名册或入学通知书",
											                	"sampleImg":[]
											                }'><i></i><span>点击上传</span>
											                </div>
														</a>
														<a href="${not empty signupCopyData['lqmc'] ? signupCopyData['lqmc'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
														<input type="hidden" class="img-val" name="lqmc" value="${signupCopyData['lqmc'] }" />
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">应届毕业生证明</span>
											<c:choose>
												<c:when test="${not empty signupCopyData['yjbyszm'] }">
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
													<div style="width:160px;">
														<h4 class="cert-title text-center f16">普通高等学校应届毕业生证明</h4>
														<div class="cert-box has-upload cert-box-6">
															<a href="javascript:void(0);" class="info-img-box">
																<img class="info-img" key="yjbyszm" src="${signupCopyData['yjbyszm']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
																<div class="upload-btn" data-role="upload-img" data-object='{
												                	"title":"普通高等学校应届毕业生证明",
												                	"sampleImg":[]
												                }'><i></i><span>点击上传</span>
												                </div>
															</a>
															<a href="${not empty signupCopyData['yjbyszm'] ? signupCopyData['yjbyszm'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
															<input type="hidden" class="img-val" name="yjbyszm" value="${signupCopyData['yjbyszm'] }" />
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
											<span class="margin-r-5">承诺书</span>
											<c:choose>
												<c:when test="${not empty signupCopyData['ykcns'] }">
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
													<div style="width:160px;">
														<h4 class="cert-title text-center f16">国家开放大学（广州）开放教育预科生信息表（承诺书）</h4>
														<div class="cert-box has-upload cert-box-6">
															<a href="javascript:void(0);" class="info-img-box">
																<img class="info-img" key="ykcns" src="${signupCopyData['ykcns']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
																<div class="upload-btn" data-role="upload-img" data-object='{
												                	"title":"国家开放大学（广州）开放教育预科生信息表（承诺书）",
												                	"sampleImg":[]
												                }'><i></i><span>点击上传</span>
												                </div>
															</a>
															<a href="${not empty signupCopyData['ykcns'] ? signupCopyData['ykcns'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
															<input type="hidden" class="img-val" name="ykcns" value="${signupCopyData['ykcns'] }" />
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:when>
						</c:choose>
						<c:if test="${isOffsite==1}">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title text-bold">
										<span class="margin-r-5">区域证明</span>
										<c:choose>
											<c:when test="${((empty item.gjtSignup.signupJzzType||item.gjtSignup.signupJzzType==0) && not empty signupCopyData['jzz'] && not empty signupCopyData['jzzf']) || (item.gjtSignup.signupJzzType==1 && not empty signupCopyData['ygzm']) }">
												<small class="label bg-green reset-label fr">已完善</small>
											</c:when>
											<c:otherwise>
												<small class="label bg-yellow reset-label fr">待完善</small>
											</c:otherwise>
										</c:choose>
									</h3>
								</div>
								<div class="panel-body">
									<div style="padding-bottom: 10px !important; color: #ff7200;"> * 身份证前两位非44开头非广东省学员，请提供区域证明（在读年级证明）</div>
									<div style="padding-bottom: 10px !important; color: #ff7200;"> 区域证明：广州市居住证、社保卡、医保卡、劳动合同、租房协议、个人所得税完税证明、户口本、房产证、社保缴纳证明（以上任一扫描件，并注意：证明真实有效，公章所在区域与本人居住区域务必一致。）</div>
									<div style="padding-bottom: 10px !important; color: #ff7200;"> 在读年级证明：向在读学校申请开具在读年级证明，盖有学校公章后，扫描原件上传。</div>
									<div class="signupType">
										<input type="radio"  name="signupJzzType" value="0" class="flat-red" <c:if test="${item.gjtSignup.signupJzzType==0}">checked="checked"</c:if>> 区域证明
										&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="radio"  name="signupJzzType" value="1" class="flat-red" <c:if test="${item.gjtSignup.signupJzzType==1}">checked="checked"</c:if>> 在读年级证明
									</div>
									<div class="row margin_t15 signupJzzType" style="display: none;">
										<div class="col-sm-6 margin_b15">
											<div class="cert-wrap">
												<h4 class="cert-title text-center f16">区域证明正面</h4>
												<div class="cert-box has-upload cert-box-6">
													<a href="javascript:void(0);" class="info-img-box">
														<img class="info-img" key="jzz" src="${signupCopyData['jzz']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
														<div class="upload-btn" data-role="upload-img" data-object='{
										                	"title":"区域证明正面",
										                	"sampleImg":[]
										                }'><i></i><span>点击上传</span>
										                </div>
													</a>
													<a href="${not empty signupCopyData['jzz'] ? signupCopyData['jzz'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
													<input type="hidden" class="img-val" name="jzz" value="${signupCopyData['jzz'] }" />
												</div>
											</div>
										</div>
										<div class="col-sm-6 margin_b15">
											<div class="cert-wrap">
												<h4 class="cert-title text-center f16">区域证明反面</h4>
												<div class="cert-box has-upload cert-box-6">
													<a href="javascript:void(0);" class="info-img-box">
														<img class="info-img" key="jzzf" src="${signupCopyData['jzzf']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
														<div class="upload-btn" data-role="upload-img" data-object='{
										                	"title":"区域证明反面",
										                	"sampleImg":[]
										                }'><i></i><span>点击上传</span>
										                </div>
													</a>
													<a href="${not empty signupCopyData['jzzf'] ? signupCopyData['jzzf'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
													<input type="hidden" class="img-val" name="jzzf" value="${signupCopyData['jzzf'] }" />
												</div>
											</div>
										</div>
									</div>
									<div class="row margin_t15 signupJzzType" style="display: none;">
										<div class="col-sm-6 margin_b15">
											<div class="cert-wrap">
												<h4 class="cert-title text-center f16">在读年级证明</h4>
												<div class="cert-box has-upload cert-box-6">
													<a href="javascript:void(0);" class="info-img-box">
														<img class="info-img" key="ygzm" src="${signupCopyData['ygzm']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
														<div class="upload-btn" data-role="upload-img" data-object='{
										                	"title":"在读年级证明",
										                	"sampleImg":[]
										                }'><i></i><span>点击上传</span>
										                </div>
													</a>
													<a href="${not empty signupCopyData['ygzm'] ? signupCopyData['ygzm'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
													<input type="hidden" class="img-val" name="ygzm" value="${signupCopyData['ygzm'] }" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</c:if>
						<div class="margin_b20 text-center">
							<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10 btn-save" val="1" data-form-id="5">${item.gjtSignup.charge=='2'?'预&nbsp;&nbsp;&nbsp;&nbsp;存':'提交资料'}</button>
						</div>
					</div>
				</div>
			</div>
			</form>
		</div>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

	<script type="text/javascript" src="${ctx}/static/jquery-qrcode/jquery.qrcode.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/edumanage/schoolRollInfo/school_roll_info_form.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/edumanage/schoolRollInfo/school_roll_info_form_update.js"></script>
	<script type="text/javascript">
		(function($) {
			//Flat red color scheme for iCheck
			$('input[type="radio"].flat-red').iCheck({
				checkboxClass: 'icheckbox_flat-green',
				radioClass: 'iradio_flat-green'
			}).on("ifChecked",function(e){
				$(e.target).attr('checked',true);
				loadSignupType(e.target);
			}).on("ifUnchecked",function(e){
				$(e.target).attr('checked',false);
			});

			$('input[type="radio"].flat-red:checked').each(function(i, element) {
				loadSignupType(element);
			});

			function loadSignupType(ele) {
				var name = $(ele).attr("name");
				var value = $(ele).val();
				var index = 0;
				$(':input[name="'+name+'"]').each(function(i, element) {
					if(value == element.value) {
						index = i;
					}
				});
				$('.' + name).hide();
				if($('.' + name).length == 1) {
					$('.' + name).show();
				} else {
					$('.' + name + ':eq('+index+')').show();
				}
			}

			$('.reservation2').daterangepicker({
				singleDatePicker: true,
				maxDate : moment(), //最大时间
				showDropdowns : true,
				format : 'YYYYMMDD',
				locale : {
					daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
					monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
					firstDay : 1
				}
			});

			$('.pcSeclet').cxSelect({
			  url: '${ctx}/static/plugins/jquery.cxselect/cityData.min.json',               // 如果服务器不支持 .json 类型文件，请将文件改为 .js 文件
			  selects: ['province', 'city', 'area'],  // 数组，请注意顺序
			  emptyStyle: 'none'
			});

			var dicExsubjectListJson = JSON.parse('${dicExsubjectListJson}');
			$(':input[name="exsubject"]').change(function () {
				$(':input[name="exsubjectkind"]')[0].options.length = 1;
				var thisValue = this.value;
				var dicExsubjectkindList = null;
				$.each(dicExsubjectListJson, function(i, item){
					if(thisValue == item.code) {
						dicExsubjectkindList = item.dicExsubjectkindList;
						return false; // 退出循环
					}
				});
				$.each(dicExsubjectkindList, function(i, item){
					$(':input[name="exsubjectkind"]')[0].options.add(new Option(item.name, item.code));
				});
			});

			// 签名控件 - 调用公共签名插件
			var autograph = $("#autograph").val();
			if (autograph != '') {
				$('#qrcodeDiv').hide();
				$('#autographDiv').show();
			} else {
				$('#qrcode').my_qrcode(function(obj) {
					$('#autograph').val(obj);
					$('#autographImg').attr('src', obj);
					$('#qrcodeDiv').hide();
					$('#autographDiv').show();
				});
			}

			$('.re-sign').click(function(){
				$('#autograph').val('');
				$('#qrcode').empty();
				$('#qrcodeDiv').show();
				$('#autographDiv').hide();

				$('#qrcode').my_qrcode(function(obj) {
					$('#autograph').val(obj);
					$('#autographImg').attr('src', obj);
					$('#qrcodeDiv').hide();
					$('#autographDiv').show();
				});
			});
			// ./end 签名控件

		})(jQuery);

		<c:if test="${not empty feedback && feedback.successful}">
		setTimeout(function() {
			/*自动关闭当前窗口*/
			window.close();
		}, 3000);
		</c:if>
	</script>
</body>
</html>
