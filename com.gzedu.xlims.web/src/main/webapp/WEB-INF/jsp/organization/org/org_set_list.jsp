<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<jsp:include page="/eefileupload/upload.jsp" />
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" id="go_for_back" onclick="window.history.back()">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">机构管理</a>
			</li>
			<li>
				<a href="#">院校管理</a>
			</li>
			<li class="active">院校设置</li>
		</ol>
	</section>
	<section class="content">
		<div class="box">
			<div class="box-header with-border">
				<h3 class="box-title">基础信息</h3>
			</div>
			<div class="box-body">
				<div class="form-horizontal">
					<div class="row pad-t15">
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap no-pad-right">院校编码：</label>
								<div class="col-sm-9 pad-l15">
									<p class="form-control-static">${entity.code}</p>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap no-pad-right">院校名称：</label>
								<div class="col-sm-9 pad-l15">
									<input class="form-control" value="${entity.gjtSchoolInfo.xxmc}" disabled="disabled">
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap no-pad-right">联系人：</label>
								<div class="col-sm-9 pad-l15">
									<input class="form-control" value="${entity.gjtSchoolInfo.linkMan}" disabled="disabled">
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap no-pad-right">联系电话：</label>
								<div class="col-sm-9 pad-l15">
									<input class="form-control" value="${entity.gjtSchoolInfo.linkTel}" disabled="disabled">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="box margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title pad-t5">版权设置</h3>
			</div>
			<div class="box-body">
				<div class="nav-tabs-custom margin-bottom-none no-shadow">
					<input type="hidden" id="platfromType" />
					<ul class="nav nav-tabs" data-role="tab">
						<li class="active" value="1">
							<a href="#tab_1" data-toggle="tab">管理平台</a>
						</li>
						<li value="2">
							<a href="#tab_2" data-toggle="tab">辅导教师平台</a>
						</li>
						<li value="3">
							<a href="#tab_3" data-toggle="tab">班主任平台</a>
						</li>
						<li value="4">
							<a href="#tab_4" data-toggle="tab">督导教师平台</a>
						</li>
						<li value="5">
							<a href="#tab_5" data-toggle="tab">个人中心</a>
						</li>
					</ul>
					<div class="tab-content">

						<div class="tab-pane active" id="tab_1">
							<form id="inputForm1" class="form-horizontal" role="form" action="" method="post">
								<input type="hidden" name="id" value="${entity.id}" /> <input type="hidden" name="platfromType" value="1" />
								<div class="table-block full-width copyright-tbl">
									<div class="table-row">
										<div class="table-cell-block">
											<strong>登陆院校LOGO</strong>
										</div>
										<div class="table-cell-block">
											<div class="row">
												<div class="col-md-6">
													<div class="upload-image-box full-width">
														<div class="upload-image-container">
															<div class="table-cell-block">
																<div class="pad5">
																	<img id="imgId1" src="${xlimsGzeduMap.loginHeadLogo}" /> <input id="imgPath1" type="hidden" value="${xlimsGzeduMap.loginHeadLogo}"
																		name="loginHeadLogo">
																</div>
															</div>
														</div>
														<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
															点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId1','imgPath1');" />
														</div>
													</div>
												</div>
												<div class="col-md-4">
													<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
												</div>
											</div>
										</div>
									</div>

									<c:choose>
										<c:when test="${xlimsGzeduMap.loginBackground==null||xlimsGzeduMap.loginBackground==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId3" src="" /> <input id="imgPath3" type="hidden" name="loginBackground">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId3','imgPath3');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId3" src="${xlimsGzeduMap.loginBackground}" /> <input id="imgPath3" type="hidden" name="loginBackground"
																				value="${xlimsGzeduMap.loginBackground}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId3','imgPath3');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>院校域名：</strong>
										</div>
										<div class="table-cell-block">
											<input name="schoolRealmName" value="${xlimsGzeduMap.schoolRealmName }" type="text" class="form-control"
											 placeholder="示例：http://study.oucnet.cn" value="" />
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>平台名称：</strong>
										</div>
										<div class="table-cell-block">
											<input name="platformName" value="${xlimsGzeduMap.platformName }" type="text" class="form-control"  placeholder="请输入平台名称！" />
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>登陆底部版权：</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor1" rows="10" cols="80">
												${xlimsGzeduMap.loginFooterCopyright}
											</textarea>
											<input name="loginFooterCopyright" id="editorHide1" type="hidden" />
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>平台底部版权：</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor11" rows="10" cols="80">
												${xlimsGzeduMap.homeFooterCopyright}
											</textarea>
											<input name="homeFooterCopyright" id="editorHide11" type="hidden" />
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="tab-pane" id="tab_2">
							<form id="inputForm2" action="${ctx}/organization/org/updateOrInsert" method="post">
								<input type="hidden" name="id" value="${entity.id}" /> <input type="hidden" name="platfromType" value="2" />
								<div class="cnt-box-header with-border clearfix">
									<h3 class="cnt-box-title">登录页面</h3>
								</div>
								<div class="table-block full-width copyright-tbl">
									<c:choose>
										<c:when test="${fudaoTeacherMap.loginHeadLogo==null||fudaoTeacherMap.loginHeadLogo==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId2" src="" /> <input id="imgPath2" type="hidden" value="" name="loginHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId2','imgPath2');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId2" src="${fudaoTeacherMap.loginHeadLogo}" /> <input id="imgPath2" type="hidden" name="loginHeadLogo"
																				value="${fudaoTeacherMap.loginHeadLogo}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId2','imgPath2');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>

									<c:choose>
										<c:when test="${fudaoTeacherMap.loginBackground==null||fudaoTeacherMap.loginBackground==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId33" src="" /> <input id="imgPath33" type="hidden" name="loginBackground">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId33','imgPath33');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId33" src="${fudaoTeacherMap.loginBackground}" />
																			 <input id="imgPath33" type="hidden" name="loginBackground" value="${fudaoTeacherMap.loginBackground}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId33','imgPath33');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${fudaoTeacherMap.qcodePic==null||fudaoTeacherMap.qcodePic==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>移动APP下载二维码：</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId15" src="" /> <input id="imgPath15" type="hidden" name="qcodePic">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId15','imgPath15');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>移动APP下载二维码：</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId15" src="${fudaoTeacherMap.qcodePic}" /> <input id="imgPath15" type="hidden" name="qcodePic"
																				value="${fudaoTeacherMap.qcodePic}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId15','imgPath15');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>院校域名：</strong>
										</div>
										<div class="table-cell-block">
											<input name="schoolRealmName" value="${fudaoTeacherMap.schoolRealmName }" type="text" class="form-control"  placeholder="示例：http://study.oucnet.cn" value="" />
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>登录标题</strong>
										</div>
										<div class="table-cell-block">
											<div class="row">
												<div class="col-md-6">
													<input class="form-control" name="loginTitle" value="${fudaoTeacherMap.loginTitle}" placeholder="请输入登录标题">
												</div>
											</div>
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>底部版权</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor2" class="loginFooter2" rows="10" cols="80">
												${fudaoTeacherMap.loginFooterCopyright}
											</textarea>
											<input id="editorHide2" name="loginFooterCopyright" type="hidden" />
										</div>
									</div>
								</div>
								<div class="cnt-box-header with-border clearfix">
									<h3 class="cnt-box-title">首页</h3>
								</div>
								<div class="table-block full-width copyright-tbl">
									<c:choose>
										<c:when test="${fudaoTeacherMap.homeHeadLogo==null||fudaoTeacherMap.homeHeadLogo==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId4" src="" /> <input id="imgPath4" type="hidden" value="" name="homeHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId4','imgPath4');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId4" src="${fudaoTeacherMap.homeHeadLogo}" /> <input id="imgPath4" type="hidden"
																				value="${fudaoTeacherMap.homeHeadLogo}" name="homeHeadLogo" v>
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId4','imgPath4');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>底部版权</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor3" rows="10" cols="80">
												${fudaoTeacherMap.homeFooterCopyright}
											</textarea>
											<input id="editorHide3" type="hidden" name="homeFooterCopyright" />
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="tab-pane" id="tab_3">
							<form id="inputForm3" class="form-horizontal" role="form" action="${ctx}/organization/org/updateOrInsert" method="post">
								<input type="hidden" name="id" value="${entity.id}" /> <input type="hidden" name="platfromType" value="3" />
								<div class="cnt-box-header with-border clearfix">
									<h3 class="cnt-box-title">登录页面</h3>
								</div>
								<div class="table-block full-width copyright-tbl">
									<c:choose>
										<c:when test="${bzrTeacherMap.loginHeadLogo==null||bzrTeacherMap.loginHeadLogo==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId5" src="" /> <input id="imgPath5" type="hidden" value="" name="loginHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId5','imgPath5');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId5" src="${bzrTeacherMap.loginHeadLogo}" /> <input id="imgPath5" type="hidden" name="loginHeadLogo"
																				value="${bzrTeacherMap.loginHeadLogo}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId5','imgPath5');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${bzrTeacherMap.loginBackground==null||bzrTeacherMap.loginBackground==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId6" src="" /> <input id="imgPath6" type="hidden" name="loginBackground">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId6','imgPath6');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId6" src="${bzrTeacherMap.loginBackground}" /> <input id="imgPath6" type="hidden" name="loginBackground"
																				value="${bzrTeacherMap.loginBackground}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId6','imgPath6');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<c:choose>
									<c:when test="${bzrTeacherMap.qcodePic==null||bzrTeacherMap.qcodePic==''}">
										<div class="table-row">
											<div class="table-cell-block">
												<strong>移动APP下载二维码：</strong>
											</div>
											<div class="table-cell-block">
												<div class="row">
													<div class="col-md-6">
														<div class="upload-image-box full-width">
															<div class="upload-image-container">
																<div class="table-cell-block">
																	<div class="pad5">
																		<img id="imgId16" src="" /> <input id="imgPath16" type="hidden" name="qcodePic">
																	</div>
																</div>
															</div>
															<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId16','imgPath16');" />
															</div>
														</div>
													</div>
													<div class="col-md-4">
														<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
													</div>
												</div>
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<div class="table-row">
											<div class="table-cell-block">
												<strong>移动APP下载二维码：</strong>
											</div>
											<div class="table-cell-block">
												<div class="row">
													<div class="col-md-6">
														<div class="upload-image-box full-width">
															<div class="upload-image-container">
																<div class="table-cell-block">
																	<div class="pad5">
																		<img id="imgId16" src="${bzrTeacherMap.qcodePic}" /> <input id="imgPath16" type="hidden" name="qcodePic"
																			value="${bzrTeacherMap.qcodePic}">
																	</div>
																</div>
															</div>
															<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId16','imgPath16');" />
															</div>
														</div>
													</div>
													<div class="col-md-4">
														<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
													</div>
												</div>
											</div>
										</div>
									</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>院校域名：</strong>
										</div>
										<div class="table-cell-block">
											<input name="schoolRealmName" value="${bzrTeacherMap.schoolRealmName }" type="text" class="form-control"  placeholder="示例：http://study.oucnet.cn" value="" />
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>登录标题</strong>
										</div>
										<div class="table-cell-block">
											<div class="row">
												<div class="col-md-6">
													<input class="form-control" value="${bzrTeacherMap.loginTitle}" name="loginTitle" placeholder="请输入登录标题">
												</div>
											</div>
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>底部版权</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor4" rows="10" cols="80">
											 	${bzrTeacherMap.loginFooterCopyright}
											</textarea>
											<input id="editorHide4" type="hidden" name="loginFooterCopyright" />
										</div>
									</div>
								</div>

								<div class="cnt-box-header with-border clearfix">
									<h3 class="cnt-box-title">首页</h3>
								</div>
								<div class="table-block full-width copyright-tbl">
									<c:choose>
										<c:when test="${bzrTeacherMap.homeHeadLogo==null||bzrTeacherMap.homeHeadLogo==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId7" src="" /> <input id="imgPath7" type="hidden" name="homeHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId7','imgPath7');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>

													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId7" src="${bzrTeacherMap.homeHeadLogo}" /> <input id="imgPath7" type="hidden" value="${bzrTeacherMap.homeHeadLogo}"
																				name="homeHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId7','imgPath7');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>底部版权</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor5" rows="10" cols="80">
								 				${bzrTeacherMap.homeFooterCopyright}
											</textarea>
											<input id="editorHide5" type="hidden" name="homeFooterCopyright" />
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="tab-pane" id="tab_4">
							<form id="inputForm4" class="form-horizontal" role="form" action="${ctx}/organization/org/updateOrInsert" method="post">
								<input type="hidden" name="id" value="${entity.id}" /> <input type="hidden" name="platfromType" value="4" />
								<div class="cnt-box-header with-border clearfix">
									<h3 class="cnt-box-title">登录页面</h3>
								</div>
								<div class="table-block full-width copyright-tbl">
									<c:choose>
										<c:when test="${dudaoTeacherMap.loginHeadLogo==null||dudaoTeacherMap.loginHeadLogo==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId8" src="" /> <input id="imgPath8" type="hidden" name="loginHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId8','imgPath8');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId8" src="${dudaoTeacherMap.loginHeadLogo}" /> <input id="imgPath8" type="hidden" name="loginHeadLogo"
																				value="${dudaoTeacherMap.loginHeadLogo}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId8','imgPath8');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${dudaoTeacherMap.loginBackground==null||dudaoTeacherMap.loginBackground==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId9" src="" /> <input id="imgPath9" type="hidden" name="loginBackground">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId9','imgPath9');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId9" src="${dudaoTeacherMap.loginBackground}" /> <input id="imgPath9" type="hidden" name="loginBackground"
																				value="${dudaoTeacherMap.loginBackground}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId9','imgPath9');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<c:choose>
									<c:when test="${dudaoTeacherMap.qcodePic==null||dudaoTeacherMap.qcodePic==''}">
										<div class="table-row">
											<div class="table-cell-block">
												<strong>移动APP下载二维码：</strong>
											</div>
											<div class="table-cell-block">
												<div class="row">
													<div class="col-md-6">
														<div class="upload-image-box full-width">
															<div class="upload-image-container">
																<div class="table-cell-block">
																	<div class="pad5">
																		<img id="imgId17" src="" /> <input id="imgPath17" type="hidden" name="qcodePic">
																	</div>
																</div>
															</div>
															<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId17','imgPath17');" />
															</div>
														</div>
													</div>
													<div class="col-md-4">
														<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
													</div>
												</div>
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<div class="table-row">
											<div class="table-cell-block">
												<strong>移动APP下载二维码：</strong>
											</div>
											<div class="table-cell-block">
												<div class="row">
													<div class="col-md-6">
														<div class="upload-image-box full-width">
															<div class="upload-image-container">
																<div class="table-cell-block">
																	<div class="pad5">
																		<img id="imgId17" src="${dudaoTeacherMap.qcodePic}" /> <input id="imgPath17" type="hidden" name="qcodePic"
																			value="${dudaoTeacherMap.qcodePic}">
																	</div>
																</div>
															</div>
															<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId17','imgPath17');" />
															</div>
														</div>
													</div>
													<div class="col-md-4">
														<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
													</div>
												</div>
											</div>
										</div>
									</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>院校域名：</strong>
										</div>
										<div class="table-cell-block">
											<input name="schoolRealmName" value="${dudaoTeacherMap.schoolRealmName }" type="text" class="form-control"  placeholder="示例：http://study.oucnet.cn" value="" />
										</div>
									</div>

									<div class="table-row">
										<div class="table-cell-block">
											<strong>登录标题</strong>
										</div>
										<div class="table-cell-block">
											<div class="row">
												<div class="col-md-6">
													<input class="form-control" value="${dudaoTeacherMap.loginTitle}" name="loginTitle" placeholder="请输入登录标题">
												</div>
											</div>
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>底部版权</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor6" rows="10" cols="80">
								${dudaoTeacherMap.loginFooterCopyright}
								</textarea>
											<input id="editorHide6" type="hidden" name="loginFooterCopyright" />
										</div>
									</div>
								</div>

								<div class="cnt-box-header with-border clearfix">
									<h3 class="cnt-box-title">首页</h3>
								</div>
								<div class="table-block full-width copyright-tbl">
									<c:choose>
										<c:when test="${dudaoTeacherMap.homeHeadLogo==null||dudaoTeacherMap.homeHeadLogo==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId10" src="" /> <input id="imgPath10" type="hidden" name="homeHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId10','imgPath10');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>

													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId10" src="${dudaoTeacherMap.homeHeadLogo}" /> <input id="imgPath10" type="hidden" name="homeHeadLogo"
																				value="${dudaoTeacherMap.homeHeadLogo}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId10','imgPath10');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>

													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>底部版权</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor7" rows="10" cols="80">
												${dudaoTeacherMap.homeFooterCopyright}
											</textarea>
											<input id="editorHide7" type="hidden" name="homeFooterCopyright" />
										</div>
									</div>
								</div>
							</form>
						</div>

						<div class="tab-pane" id="tab_5">
							<form id="inputForm5" class="form-horizontal" role="form" action="${ctx}/organization/org/updateOrInsert" method="post">
								<input type="hidden" name="id" value="${entity.id}" /> <input type="hidden" name="platfromType" value="5" />
								<div class="cnt-box-header with-border clearfix">
									<h3 class="cnt-box-title">登录页面</h3>
								</div>
								<div class="table-block full-width copyright-tbl">
									<c:choose>
										<c:when test="${studyCenterMap.loginHeadLogo==null||studyCenterMap.loginHeadLogo==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId11" src="" /> <input id="imgPath11" type="hidden" name="loginHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId11','imgPath11');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId11" src="${studyCenterMap.loginHeadLogo}" /> <input id="imgPath11" type="hidden" name="loginHeadLogo"
																				value="${studyCenterMap.loginHeadLogo}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId11','imgPath11');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${studyCenterMap.loginBackground==null||studyCenterMap.loginBackground==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId12" src="" /> <input id="imgPath12" type="hidden" name="loginBackground">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId12','imgPath12');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>登录背景</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId12" src="${studyCenterMap.loginBackground}" /> <input id="imgPath12" type="hidden" name="loginBackground"
																				value="${studyCenterMap.loginBackground}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId12','imgPath12');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${studyCenterMap.qcodePic==null||studyCenterMap.qcodePic==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>移动APP下载二维码：</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId14" src="" /> <input id="imgPath14" type="hidden" name="qcodePic">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId14','imgPath14');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>移动APP下载二维码：</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId14" src="${studyCenterMap.qcodePic}" /> <input id="imgPath14" type="hidden" name="qcodePic"
																				value="${studyCenterMap.qcodePic}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId14','imgPath14');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为900*280，图片大小不能超过2M</div>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>院校域名：</strong>
										</div>
										<div class="table-cell-block">
											<input name="schoolRealmName" value="${studyCenterMap.schoolRealmName }" type="text" class="form-control"  placeholder="示例：http://study.oucnet.cn" value="" />
										</div>
									</div>

									<div class="table-row">
										<div class="table-cell-block">
											<strong>登录标题</strong>
										</div>
										<div class="table-cell-block">
											<div class="row">
												<div class="col-md-6">
													<input class="form-control" value="${studyCenterMap.loginTitle}" name="loginTitle" placeholder="请输入登录标题">
												</div>
											</div>
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>底部版权</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor8" rows="10" cols="80">
								${studyCenterMap.loginFooterCopyright}
								</textarea>
											<input id="editorHide8" type="hidden" name="loginFooterCopyright" />
										</div>
									</div>
								</div>

								<div class="cnt-box-header with-border clearfix">
									<h3 class="cnt-box-title">首页</h3>
								</div>
								<div class="table-block full-width copyright-tbl">
									<c:choose>
										<c:when test="${studyCenterMap.homeHeadLogo==null||studyCenterMap.homeHeadLogo==''}">
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId13" src="" /> <input id="imgPath13" type="hidden" name="homeHeadLogo">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	点击上传 <input type="button" class="btn-file" onclick="uploadImage('imgId13','imgPath13');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>

													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="table-row">
												<div class="table-cell-block">
													<strong>头部LOGO</strong>
												</div>
												<div class="table-cell-block">
													<div class="row">
														<div class="col-md-6">
															<div class="upload-image-box full-width">
																<div class="upload-image-container">
																	<div class="table-cell-block">
																		<div class="pad5">
																			<img id="imgId13" src="${studyCenterMap.homeHeadLogo}" /> <input id="imgPath13" type="hidden" name="homeHeadLogo"
																				value="${studyCenterMap.homeHeadLogo}">
																		</div>
																	</div>
																</div>
																<div class="btn btn-md btn-block btn-primary upload-image-btn upload-reset-btn">
																	重新上传 <input type="button" class="btn-file" onclick="uploadImage('imgId13','imgPath13');" />
																</div>
															</div>
														</div>
														<div class="col-md-4">
															<div class="upfile-tips no-pad-left">支持.jpg/.gif/.png/.bmp格式图片， 尺寸最大为420*50，图片大小不能超过2M</div>
														</div>

													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
									<div class="table-row">
										<div class="table-cell-block">
											<strong>底部版权</strong>
										</div>
										<div class="table-cell-block">
											<textarea id="editor9" rows="10" cols="80">
								${studyCenterMap.homeFooterCopyright}
								</textarea>
											<input id="editorHide9" type="hidden" name="homeFooterCopyright" />
										</div>
									</div>
									<div class="table-row">
										<div class="table-cell-block"></div>
										<div class="table-cell-block">
											<div class="radio">
												<label>
													 <input class="minimal_invigi" name="schoolModel" type="radio" value="0"
													<c:if test="${studyCenterMap.schoolModel =='0' or empty studyCenterMap.schoolModel}">checked</c:if> />非院校
												</label>
												 <label class="left10"> <input class="minimal_invigi" name="schoolModel" type="radio" value="1"
													<c:if test="${studyCenterMap.schoolModel == '1' }">checked</c:if> />院校模式
												</label>
												 <label class="left10"> <input class="minimal_invigi" name="schoolModel" type="radio" value="2"
													<c:if test="${studyCenterMap.schoolModel == '2' }">checked</c:if> />院校模式(无考试)
												</label>
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			<div class="box-footer text-right">
				<button onclick="history.back();" class="btn btn-default min-width-90px btn-cancel-edit margin_r15">返回</button>
				<button type="button" id="update-one" class="btn btn-success min-width-90px btn-save-edit">确定</button>
			</div>
		</div>
	</section>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#platfromType').val("1");
			$("li").click(function() {
				var id = $(this).val();
				$('#platfromType').val(id);
			});
			$('#update-one').click(function() {
				var $this = $(this);
				//动态获取每个表单的ID
				var formId = "#inputForm" + $('#platfromType').val();
				//获取每个表单中textArea文本框中的值
				getTextAreaValue($('#platfromType').val());
				$.confirm({
					title : '提示',
					content : '确认保存版权设置？',
					confirmButton : '确认',
					icon : 'fa fa-warning',
					cancelButton : '取消',
					confirmButtonClass : 'btn-primary',
					closeIcon : true,
					closeIconClass : 'fa fa-close',
					confirm : function() {
						$.ajax({
							type : 'post',
							url : '${ctx}/organization/org/updateOrInsert',
							data : $(formId).serialize(),
							success : function(data) {
								var data = JSON.parse(data);
								//showMessage(data);
								alert(data.message);
								window.location.reload();
							}
						});
					}
				});
			});
		});
		//对textArea文本域赋值
		function getTextAreaValue(platfromType) {
			if (platfromType == '1') {
				var areaValue1 = CKEDITOR.instances.editor1.getData();
				$("#editorHide1").val(areaValue1);
				var areaValue11 = CKEDITOR.instances.editor11.getData();
				$("#editorHide11").val(areaValue11);
			} else if (platfromType == '2') {
				var areaValue2 = CKEDITOR.instances.editor2.getData();
				$("#editorHide2").val(areaValue2);
				var areaValue3 = CKEDITOR.instances.editor3.getData();
				$("#editorHide3").val(areaValue3);
			} else if (platfromType == '3') {
				var areaValue4 = CKEDITOR.instances.editor4.getData();
				$("#editorHide4").val(areaValue4);
				var areaValue5 = CKEDITOR.instances.editor5.getData();
				$("#editorHide5").val(areaValue5);
			} else if (platfromType == '4') {
				var areaValue6 = CKEDITOR.instances.editor6.getData();
				$("#editorHide6").val(areaValue6);
				var areaValue7 = CKEDITOR.instances.editor7.getData();
				$("#editorHide7").val(areaValue7);
			} else {
				var areaValue8 = CKEDITOR.instances.editor8.getData();
				$("#editorHide8").val(areaValue8);
				var areaValue9 = CKEDITOR.instances.editor9.getData();
				$("#editorHide9").val(areaValue9);
			}
		}

		//标签页与编辑器,管理平台
		if (!CKEDITOR.instances.editor1) {
			CKEDITOR.replace('editor1');
			CKEDITOR.replace('editor11');
		}
		$('[data-role="tab"] a').on(
				'shown.bs.tab',
				function(e) {
					e.preventDefault();
					var index = $(e.target).parent().index();

					switch (index) {
					/*   	case 0://管理平台没效果
					 if(!CKEDITOR.instances.editor1){
					 CKEDITOR.replace('editor1'); 
					 CKEDITOR.replace('editor11'); 
					 }
					 break; */
					case 1://辅导教师平台
						if (!CKEDITOR.instances.editor2
								&& !CKEDITOR.instances.editor3) {
							CKEDITOR.replace('editor2');
							CKEDITOR.replace('editor3');
						}

						break;
					case 2://班主任平台
						if (!CKEDITOR.instances.editor4
								&& !CKEDITOR.instances.editor5) {
							CKEDITOR.replace('editor4');
							CKEDITOR.replace('editor5');
						}

						break;
					case 3://辅导教师平台
						if (!CKEDITOR.instances.editor6
								&& !CKEDITOR.instances.editor7) {
							CKEDITOR.replace('editor6');
							CKEDITOR.replace('editor7');
						}

						break;
					case 4://个人中心
						if (!CKEDITOR.instances.editor8
								&& !CKEDITOR.instances.editor9) {
							CKEDITOR.replace('editor8');
							CKEDITOR.replace('editor9');
						}

						break;
					default:
						break;
					}
				})
	</script>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
