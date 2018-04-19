<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>管理系统 - 发布新活动</title>
<style type="text/css">
	[data-role="cate-menu"]>li.on>a{
		background-color: #e1e3e9;
	    color: #333;
	}
	[data-id="radio-edit"]{
		counter-reset: order;
	}
	[data-id="radio-item"]{
		margin-bottom:10px;
	}
	[data-id="radio-edit"] .inner{
		counter-increment: order;
		position: relative;
	}
	[data-id="radio-edit"] .inner .form-control{
		padding-left: 23px;
	}
	[data-id="radio-edit"] .inner:before{
		content: counter(order)'. ';
		position: absolute;
	    z-index: 10;
	    left: 5px;
	    top: 50%;
	    margin-top: -10px;
	    color: #999;
	}
	</style>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body overlay-wrapper">
	<section class="content-header clearfix">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="homepage.html"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="${ctx }/admin/messageInfo/list">通知公告</a>
			</li>
			<li class="active">新增通知公告</li>
		</ol>
	</section>
	<section class="content">
		<form id="theform" class="form-horizontal" role="form" action="${ctx }/admin/messageInfo/${action}" method="post">
			<input id="action" type="hidden" name="action" value="${action }">
			 <input type="hidden" name="messageId" value="${info.messageId }">
			 <input type="hidden" name="newMessageId" id="newMessageId" value="${newMessageId }">
			<div class="box margin-bottom-none">
				<div class="box no-margin school-set-box">
					<div class="box-body">
						<div class="form-horizontal reset-form-horizontal margin_t10">
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>标题</label>
								<div class="col-md-8 col-sm-10">
									<div class="position-relative">
										<input type="text" name="infoTheme" value="${info.infoTheme }" class="form-control" placeholder="标题" datatype="*" nullmsg="请填写标题"
											errormsg="请填写标题" />
									</div>
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>类型</label>
								<div class="col-md-8 col-sm-10">
									<div class="position-relative">
										<select class="form-control select2" name="infoType" datatype="*" nullmsg="请选择通知类型" errormsg="请选择类型" data-size="8" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${infoTypeMap}" var="map">
												<option value="${map.key}" <c:if test="${infoTypeId == map.key}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<c:if test="${action eq 'create' }">
							
							<div class="form-group">
								<label class="col-sm-2 control-label"><small class="text-red">*</small>分类</label>
								<div class="col-sm-5">
									<div class="overlay-wrapper">
										<div class="position-relative" data-id="dropdown">
											<input type="hidden" name="typeClassify" id="typeClassify">
											<div class="input-group">
												<input type="text" name="typeClassifyId" id="typeClassifyId" class="form-control bg-white" placeholder="请选择分类" datatype="*" nullmsg="请选择分类" errormsg="请选择分类" data-id="cate-input" disabled style="cursor: pointer;">
												<div class="input-group-btn">
													<div role="button" class="btn btn-default dropdown-toggle">
														<span class="caret"></span>
													</div>
												</div>
												<div class="input-group-addon border-none no-pad-right gray9 f12">请先选择类型</div>
											</div>
											<ul class="dropdown-menu flat dropdown-menu-right no-padding no-margin full-width" data-role="cate-menu">
										      	<li data-id="cate-form-control">
										      		<div class="input-group input-group-sm">
										      			<input class="form-control flat" data-id="add-input" placeholder="请输入分类名称">
										      			<div class="input-group-btn">
										      				<button type="button" class="btn btn-default flat" data-id="add-btn">添加分类</button>
										      			</div>
										      		</div>
										      	</li>
										    </ul>
										</div>
										<div class="overlay" id="overlay2" style="display:none;"><i class="fa fa-spinner fa-spin f16" style="margin-top: -7px;"></i></div>
									</div>
								</div>
							</div>
							
						<%-- 	<c:if test="${action eq 'create' }"> --%>
								<div class="form-group">
									<label class="col-sm-2 control-label"><small class="text-red">*</small>接收</label>
									<div class="col-sm-9">
										<div class="box-border">
											<ul class="nav nav-tabs bg-f2f2f2">
												<li class="active" style="margin: -1px;">
													<a class="flat gray" href="#tab_top_1" data-toggle="tab">学员</a>
												</li>
												<li style="margin: -1px;">
													<a class="flat gray" href="#tab_top_2" data-toggle="tab">辅导员</a>
												</li>
												<li style="margin: -1px;">
													<a class="flat gray" href="#tab_top_3" data-toggle="tab">教务</a>
												</li>
												<li style="margin: -1px;">
													<a class="flat gray" href="#tab_top_4" data-toggle="tab">教学</a>
												</li>
												<li style="margin: -1px;">
													<a class="flat gray" href="#tab_top_5" data-toggle="tab">学生事务</a>
												</li>
												<li style="margin: -1px;">
													<a class="flat gray" href="#tab_top_6" data-toggle="tab">院长</a>
												</li>
											</ul>
											<div class="tab-content">
												<div class="tab-pane active" id="tab_top_1">
													<div class="margin_t15">
														<input type="hidden" name="isAppoint" value="0" >
														<ul class="nav nav-tabs pad-l15 pad-r15">
															<li class="active">
																<a href="#tab_top_1_1" data-toggle="tab" id="condition_yes">按条件筛选</a>
															</li>
															<li>
																<a href="#tab_top_1_2" data-toggle="tab" id="condition_no">指定收件人</a>
															</li>
														</ul>
														<div class="tab-content">
															<div class="tab-pane active" id="tab_top_1_1">
																<div class="pad15">
																	<table class="table-panel border-bottom-none vertical-middle">
																		<colgroup>
																			<col width="100" />
																			<col />
																		</colgroup>
																		<tbody>
																			<tr>
																				<td>学期： </td>
																				<td>
																					<div class="item-sel-box clearfix">
																						<span class="btn btn-default active" data-id="sel-all">
																							<input type="checkbox" checked value="1" name="gradeIdAll"> 全部
																						</span>
																						<c:forEach items="${gradeMap }" var="map">
																							<span class="btn">
																								<input type="checkbox" name="gradeIds"  value="${map.key }_${map.value }" data-id="${map.key }"> ${map.value }
																							</span>
																						</c:forEach>
																					</div>
																				</td>
																			</tr>
																			<tr>
																				<td>层次：</td>
																				<td>
																					<div class="item-sel-box clearfix">
																						<span class="btn btn-default active" data-id="sel-all">
																							<input type="checkbox" checked value="1" name="pyccIdAll"> 全部
																						</span>
																						<c:forEach items="${pyccMap }" var="map">
																							<span class="btn">
																								<input type="checkbox"  name="pyccIds" value="${map.key }_${map.value }" data-id="${map.key }"> ${map.value }
																							</span>
																						</c:forEach>
																					</div>
																				</td>
																			</tr>
																			<tr>
																				<td>专业：</td>
																				<td>
																					<div class="item-sel-box clearfix" data-role="no-toggle">
																						<span class="btn btn-default active" data-id="sel-all">
																							<input type="checkbox" checked name="specialtyIdAll" value="1"> 全部
																						</span>
																						<!-- <span class="btn btn-default active">
																							<input type="checkbox" > 旅游(酒店管理方向) <i class="fa fa-remove pad-l5" data-toggle="tooltip" title="删除"
																								data-role="remove-sel"></i>
																						</span> -->

																						<button class="btn btn-default" type="button" data-role="add-major">
																							<i class="fa fa-fw fa-plus"></i>
																						</button>
																					</div>
																				</td>
																			</tr>

																			<tr>
																				<td>课程：</td>
																				<td>
																					<div class="item-sel-box clearfix" data-role="no-toggle">
																						<span class="btn btn-default active" data-id="sel-all">
																							<input type="checkbox" checked value="1" name="courseIdAll"> 全部
																						</span>
																						<button class="btn btn-default" type="button" data-role="add-course">
																							<i class="fa fa-fw fa-plus"></i>
																						</button>
																					</div>
																				</td>
																			</tr>
																			<tr>
																				<td>学生类型：</td>
																				<td>
																					<div class="item-sel-box clearfix">
																						<span class="btn btn-default active" data-id="sel-all">
																							<input type="checkbox" checked value="1" name="userTypeAll"> 全部
																						</span>
																						<c:forEach items="${userTypeMap }" var="map">
																							<span class="btn">
																								<input type="checkbox"  name="userTypes" value="${map.key }_${map.value }" data-id="${map.key }"> ${map.value }
																							</span>
																						</c:forEach>
																					</div>
																				</td>
																			</tr>
																			<tr>
																				<td>学籍状态：</td>
																				<td>
																					<div class="item-sel-box clearfix">
																						<span class="btn btn-default active" data-id="sel-all">
																							<input type="checkbox" checked value="1" name="xjztTypeAll"> 全部
																						</span>
																						<c:forEach items="${xjztMap }" var="map">
																							<span class="btn">
																								<input type="checkbox"  name="xjztTypes" value="${map.key }_${map.value }" data-id="${map.key }"> ${map.value }
																							</span>
																						</c:forEach>
																					</div>
																				</td>
																			</tr>
																			<tr>
																				<td colspan="2" class="">
																					<div class="text-center">
																						根据以上条件共筛选出 
																						<u class="text-blue" role="button" data-role="view-sel-stu1"> -- </u> 名学生作为收件人
																						<button type="button" class="btn min-width-90px btn-default margin_r5" data-role="seachCount">查询人数</button>
																					</div>
																				</td>
																			</tr>
																			<tr>
																				<td class="no-padding"></td>
																				<td class="no-padding"></td>
																			</tr>
																		</tbody>
																	</table>
																</div>
															</div>
															<div class="tab-pane" id="tab_top_1_2">
																<div class="pad15 border-bottom">
																	<button type="button" class="btn min-width-90px btn-default margin_r5" data-role="import">批量导入学员</button>
																	<button type="button" class="btn min-width-90px btn-default" data-role="sel-online">在线选择学员</button>
																</div>
																<div class="text-center pad15">
																	根据以上条件共筛选出 <u class="text-blue" role="button" data-role="view-sel-stu"> --</u> 名学生作为收件人
																	<button type="button" class="btn min-width-90px btn-default margin_r5" data-role="seachImportCount">查询人数</button>
																</div>
															</div>
														</div>
													</div>
												</div>
												<div class="tab-pane" id="tab_top_2">
													<div class="pad15">
														<div class="item-sel-box clearfix">
															<span class="btn btn-default" data-id="sel-all">
																<input type="checkbox"  > 全部
															</span>
															<c:forEach items="${shuchuMap['fcf94a20da1c44a1a31f94eafaf4b707']}" var="user">
																<span class="btn btn-default">
																	<input type="checkbox" name="userIds" value="${user.id}_2_班主任_${user.realName }"> ${user.realName }
																</span>
															</c:forEach>
														</div>
													</div>
												</div>
												<div class="tab-pane" id="tab_top_3">
													<div class="pad15">
														<table class="table-panel border-bottom-none vertical-middle">
															<colgroup>
																<col width="115" />
																<col />
															</colgroup>
															<tbody>
																<tr>
																	<td>教务管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['7c0d7c8a39f315d610377458894050ae']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_3_教务管理员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>考务管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['03799e0b9fa7d6fe56c548df0cc3b150']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_3_考务管理员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>教材管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['409891a89c2e4e0ca12381e207e3d9bb']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_3_教材管理员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>学籍管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['3f5f7ca336a64c42bc5d3a4c1986289e']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_3_学籍管理员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>毕业管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['97e31c2c70a442208443751fdeede0ff']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_3_毕业管理员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>

																<tr>
																	<td class="no-padding"></td>
																	<td class="no-padding"></td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
												<div class="tab-pane" id="tab_top_4">
													<div class="pad15">
														<table class="table-panel border-bottom-none vertical-middle">
															<colgroup>
																<col width="145" />
																<col />
															</colgroup>
															<tbody>
																<tr>
																	<td>教学管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['449c71cbac1082b468a5941fbba4e5d6']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_4_教学管理员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<!-- <tr>
																	<td>教育项目管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox"> 全部
																			</span>
																			<span class="btn btn-default">
																				<input type="checkbox"> 李老师
																			</span>
																		</div>
																	</td>
																</tr> -->
																<tr>
																	<td>辅导教师：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['be60d336bc1946a5a24f88d5ae594b17']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_4_辅导教师_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>督导教师：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['699f6f83acf54548bfae7794915a3cf3']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_4_督导教师_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>

																<tr>
																	<td class="no-padding"></td>
																	<td class="no-padding"></td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
												<div class="tab-pane" id="tab_top_5">
													<div class="pad15">
														<table class="table-panel border-bottom-none vertical-middle">
															<colgroup>
																<col width="145" />
																<col />
															</colgroup>
															<tbody>
																<tr>
																	<td>学支管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['9a6f05b3e24d456fb84435dd75e934c2']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_5_学支管理员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<!-- <tr>
																	<td>学院运营管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox"> 全部
																			</span>
																			<span class="btn btn-default">
																				<input type="checkbox"> 李老师
																			</span>
																		</div>
																	</td>
																</tr> -->
																<tr>
																	<td>平台运管员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['bf59965fdc774f21bae6394ed8fe8ceb']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_5_平台运管员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>招生管理员：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox" > 全部
																			</span>
																			<c:forEach items="${shuchuMap['K5f2034cdd64840e5946f72b5ab3a0ffb']}" var="user">
																				<span class="btn btn-default">
																					<input type="checkbox" name="userIds" value="${user.id}_5_招生管理员_${user.realName }"> ${user.realName }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<!-- <tr>
																	<td>招生点主任：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox"> 全部
																			</span>
																			<span class="btn btn-default">
																				<input type="checkbox"> 邓老师
																			</span>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>学习中心主任：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default" data-id="sel-all">
																				<input type="checkbox"> 全部
																			</span>
																			<span class="btn btn-default">
																				<input type="checkbox"> 王老师
																			</span>
																		</div>
																	</td>
																</tr> -->

																<tr>
																	<td class="no-padding"></td>
																	<td class="no-padding"></td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
												<div class="tab-pane" id="tab_top_6">
													<div class="pad15">
														<div class="item-sel-box clearfix">
															<span class="btn btn-default" data-id="sel-all">
																<input type="checkbox" > 全部
															</span>
															<c:forEach items="${shuchuMap['d4b27a66c0a87b010120da231915c223']}" var="user">
																<span class="btn btn-default">
																	<input type="checkbox" name="userIds" value="${user.id}_6_院长_${user.realName }"> ${user.realName }
																</span>
															</c:forEach>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
						</c:if> 
						
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top"><small class="text-red">*</small>内容</label>
							<div class="col-sm-9">
								<div class="radio form-control-static no-padding">
					                <label class="margin_r15">
					                  <input type="radio" name="r3" data-role="cnt-type" value="1" data-value="0" checked>
					                  图文
					                </label>
					                <label class="margin_r15">
					                  <input type="radio" name="r3" data-role="cnt-type" value="2" data-value="1">
					                  小视频
					                </label>
					                <label>
					                  <input type="radio" name="r3" data-role="cnt-type" value="3" data-value="2">
					                  H5页面
					                </label>
					            </div>
								<div class="box-border pad clearfix">
									<div data-role="tab-item">
										<div class="position-relative">
											<textarea id="editor4" name="infoContent1" rows="10" class="full-width position-absolute show" data-rel="tab-item" datatype="vNoEmpty" nullmsg="请填写内容" errormsg="请填写内容">${info.infoContent }</textarea>
										</div>
				            			<div class="table-block control-group margin_t10">
				            				<div class="table-cell-block">
				    		                    <input class="form-control" id="fileName" name="fileName" value="${info.fileName }" readonly="readonly">
				            					<input type="hidden" id="fileUrl"	name="fileUrl" value="${info.fileUrl }">
				            				</div>
				            				<div class="table-cell-block pad-l10 text-nowrap">
				            					<button type="button" class="btn  btn-default" onclick="javascript:uploadFile('fileName','fileUrl');" >添加附件</button>
				            					<button type="button" class="btn  btn-default" id="deleteFile"  >删除附件</button>
				            				</div>
				            			</div>
									</div>
									<div data-role="tab-item" style="display:none">
										<div class="radio no-padding">
											<label class="no-padding">视频添加方式：</label>
							                <label class="margin_r15">
							                  <input type="radio" data-role="video-url" name="r4" data-value="0" checked>
							                  视频地址
							                </label>
							                <label>
							                 <!--  <input type="radio" data-role="video-url" name="r4" data-value="1">
							                  上传视频
							                </label> -->
							            </div>
							            <div>
							            	<div data-role="video-tab">
							            		<div class="position-relative">
													<input type="text" name="infoContent2" value="${info.infoContent }" class="form-control" placeholder="请复制粘贴地址" data-rel="video-tab" datatype="vNoEmpty" nullmsg="请填写视频地址" errormsg="请填写视频地址">
												</div>
							            	</div>
							            	<!-- <div data-role="video-tab" style="display:none;">
							            		<div class="margin_t10">
							            			<div class="position-relative">
								            			<button type="button" class="btn btn-default">上传视频</button>
								            			<div class="tooltip top" role="tooltip" data-id="sm-video-tip" style="
								            				z-index: 2;
														    bottom: 100%;
														    margin-bottom: -5px;
								            			"><div class="tooltip-arrow"></div><div class="tooltip-inner">请上传上传视频</div></div>
								            		</div>
								            		<ul class="list-inline margin_t10 margin-bottom-none" data-id="sm-video">
								            			<li class="pos-rel">
									            			<video src="http://v.eecn.cn/gw_xuelijiaoyu/dd_xuelijiaoyu/2013/qiyebanzu/cha08_03.mp4?start=0" width="120" height="80" style="object-fit: fill;" data-role="video-obj" role="button">
															</video>
															<i class="fa fa-times-circle dot-remove" title="删除" onclick="$(this).parent().remove();" role="button"></i>
														</li>
								            		</ul>
							            		</div>
							            	</div> -->
							            </div>
									</div>
									<div data-role="tab-item" style="display:none">
										<div class="margin_b10">H5页面地址：</div>
										<div class="position-relative">
											<input type="text" name="infoContent3"  value="${info.infoContent }" class="form-control" placeholder="请复制粘贴地址" data-rel="tab-item" datatype="vNoEmpty" nullmsg="请填写H5页面地址" errormsg="请填写H5页面地址">
										</div>
									</div>
								</div>
							</div>
						</div>

						
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label no-pad-top">是否置顶</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<div class="radio form-control-static no-padding">
									<label class="margin_r15"> 
										<input type="radio" data-role="top" name="stick" data-value="0" value="0"<c:if test="${empty  isStick or isStick==false  }">checked</c:if>> 
										不置顶
									</label>
									 <label> 
										 <input type="radio" data-role="top" name="stick" data-value="1" value="1" <c:if test="${isStick}">checked</c:if>>
											置顶
									</label>
								</div>
							</div>
						</div>
						<div class="form-group" style="display: none;" data-id="top-time">
							<div class="clearfix">
								<label class="col-md-2 col-sm-2 control-label">置顶时间</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<select name="days" class="form-control" nullmsg="请选择置顶时长！" errormsg="请选择置顶时长！" data-id="top-time-form-control">
										<option value="">请选择置顶时间</option>
										<option value="7">置顶1个星期</option>
										<option value="15">置顶半个月</option>
										<option value="30">置顶1个月</option>
										<option value="90">置顶3个月</option>
										<option value="182">置顶半年</option>
										<option value="365">置顶1年</option>
										<option value="3650">一直置顶</option>
										<option value="0" data-value="custom">自定义置顶时间</option>
									</select>
								</div>
							</div>
							<div class="clearfix margin_t15" style="display: none;" data-id="custom-time">
								<label class="col-md-2 col-sm-2 control-label"></label>
								<div class="col-md-8 col-sm-10 position-relative">
									<input type="text" name="times" class="form-control" placeholder="设置置顶结束时间" nullmsg="请设置置顶结束时间" errormsg="时间格式不对" data-role="datetime">
								</div>
							</div>
						</div>
						
					<div class="form-group">
		            <label class="col-sm-2 control-label no-pad-top">重要程度</label>
		            <div class="col-md-8 col-sm-10 position-relative">
		              <div class="radio no-padding">
		                <label class="margin_r15">
		                  <input type="radio" name="degree" value="1" checked>
		                  重要
		                </label>
		                <label class="margin_r15">
		                  <input type="radio" name="degree" value="0">
		                  一般
		                </label>
		                <span class="gray9 f12">注：设置重要会在学习空间弹出并需要学员阅读后确认已读</span>
		              </div>
		            </div>
		        </div>
		        
		        <c:if test="${action eq 'create' }">
		        <div class="form-group">
		            <label class="col-sm-2 control-label no-pad-top">APP推送</label>
		            <div class="col-md-8 col-sm-10 position-relative">
		              <div class="radio no-padding">
		                <label class="margin_r15">
		                  <input type="radio" name="isApp" value="1" checked>
		                  推送到学习首页
		                </label>
		                <label>
		                  <input type="radio" name="isApp" value="0">
		                  不推送
		                </label>
		              </div>
		            </div>
		        </div>
		         <div class="form-group">
		            <label class="col-sm-2 control-label no-pad-top">公众号推送</label>
		            <div class="col-md-9 col-sm-10 position-relative">
		              <div class="radio no-padding">
		                <label class="margin_r15">
		                  <input type="radio" name="isWeiXin" value="1" data-role="public-push-type" checked>
		                  推送到公众号
		                </label>
		                <label>
		                  <input type="radio" name="isWeiXin" value="0" data-role="public-push-type">
		                  不推送
		                </label>
		              </div>
		              <div data-role="public-push">
			              <div class="box-border pad clearfix">
			              	<div class="pad-b5">请填写推送到公众号模板内容：</div>
			              	<div class="row">
			              		<div class="col-sm-9 border-right-dedede">
			              			<table width="100%">
			              				<colgroup>
			              					<col width="85"></col>
			              				</colgroup>
			              				<tbody>
			              					<tr>
			              						<td valign="top" class="pad-t5 pad-b5">
			              							<div class="pad-t5">
					              						活动内容：
					              					</div>
			              						</td>
			              						<td class="pad-t5 pad-b5">
			              							<div class="position-relative">
										                <textarea class="form-control" rows="3" name="weixinContent" placeholder="请填写活动内容" data-rel="public-push" datatype="vNoEmpty" nullmsg="请填写活动内容" errormsg="请填写活动内容"></textarea>
										            </div>
			              						</td>
			              					</tr>
			              					<tr>
			              						<td valign="top" class="pad-t5 pad-b5">
			              							<div class="pad-t5">
					              						活动名称：
					              					</div>
			              						</td>
			              						<td class="pad-t5 pad-b5">
			              							<div class="position-relative">
										                <input type="text" class="form-control" name="weixinTitle"data-rel="public-push" datatype="vNoEmpty" placeholder="请填写活动名称" nullmsg="请填写活动名称" errormsg="请填写活动名称">
										            </div>
			              						</td>
			              					</tr>
			              					<!-- <tr>
			              						<td valign="top" class="pad-t5 pad-b5">
			              							<div class="pad-t5">
					              						活动类型：
					              					</div>
			              						</td> 
			              						<td class="pad-t5 pad-b5">
			              							<div class="position-relative">
										                <input type="text" class="form-control" data-rel="public-push" datatype="vNoEmpty" placeholder="请填写活动类型" nullmsg="请填写活动类型" errormsg="请填写活动类型">
										            </div>
			              						</td>
			              					</tr>-->
			              					<tr>
			              						<td valign="top" class="pad-t5 pad-b5">
			              							<div class="pad-t5">
					              						活动时间：
					              					</div>
			              						</td>
			              						<td class="pad-t5 pad-b5">
			              							<div class="position-relative">
										                <input type="text" class="form-control" name="weixinTime"data-rel="public-push" datatype="vNoEmpty" placeholder="活动时间" nullmsg="请设置活动时间" errormsg="时间格式不对" data-role="datetime">
										            </div>
			              						</td>
			              					</tr>
			              					<tr>
			              						<td class="pad-t5 pad-b5">
			              							学员姓名：
			              						</td>
			              						<td class="pad-t5 pad-b5">
			              							<div class="gray9">读取接收人</div>
			              						</td>
			              					</tr>
			              					<tr>
			              						<td valign="top" class="pad-t5 pad-b5">
			              							<div class="pad-t5">
					              						活动备注：
					              					</div>
			              						</td>
			              						<td class="pad-t5 pad-b5">
			              							<div class="position-relative">
										                <textarea class="form-control" rows="3" name="weixinRemark" placeholder="请填写活动备注" data-rel="public-push" datatype="vNoEmpty" nullmsg="请填写活动备注" errormsg="请填写活动备注"></textarea>
										            </div>
			              						</td>
			              					</tr>
			              				</tbody>
			              			</table>
			              		</div>
			              		<div class="col-sm-3 f12">
			              			<div>内容示例：</div>

			              			<div class="margin_t10">
										您好！恭喜报名毕业典礼活动成功！<br>
										活动名称：2017年秋毕业典礼<br>
										活动类型：毕业活动<br>
										学员姓名：李丽丽<br>
										活动时间：2017年12月21日<br>
										点击查看活动详情。
									</div>
			              		</div>
			              	</div>
			              </div>
		              </div>
		            </div>
		        </div>
				
				
		        <div class="form-group">
					<label class="col-sm-2 control-label no-pad-top">开放功能</label>
					<div class="col-sm-9">
						<div class="checkbox form-control-static no-padding">
			                <label class="margin_r15">
			                  <input type="checkbox" name="isComment" data-role="open-fn-type" data-value="0" value="1">
			                  评论
			                </label>
			                <label class="margin_r15">
			                  <input type="checkbox" name="isLike" data-role="open-fn-type" data-value="1" value="1">
			                  点赞
			                </label>
			                <label>
			                  <input type="checkbox" name="isFeedback" data-role="open-fn-type" data-value="2" value="1">
			                  反馈
			                </label>
			            </div>
			            <div data-role="feedback-box" style="display:none;">
							<div class="box-border pad clearfix">
								<div>反馈内容：</div>
								<div class="box-border pad margin_t5">
									<div class="margin_b10">标题：</div>
									<div class="position-relative">
										<textarea class="form-control" name="feedbackContent" rows="3" placeholder="请对本活动反馈意见" data-rel="feedback-box" datatype="vNoEmpty" nullmsg="请对本活动反馈意见" errormsg="请对本活动反馈意见"></textarea>
									</div>

									<div class="margin_t10 margin_b10">
										选项：
										<small class="text-orange">(“反馈”选项至少有两个)</small>
									</div>
									<div class="box-border pad">
										<div class="row" data-id="radio-edit">
											<div class="col-sm-4" data-id="radio-item">
												<div class="inner position-relative">
													<div class="input-group">
														<input type="text" name="feedbackContents" class="form-control" placeholder="选项内容" data-rel="feedback-box" datatype="vNoEmpty" 
														nullmsg="请填写选项内容" errormsg="请填写选项内容" value="有帮助">
														<div class="input-group-btn">
															<button type="button" class="btn btn-default" data-role="remove-radio"><i class="fa fa-fw fa-remove"></i></button>
														</div>
													</div>
												</div>
											</div>
											<div class="col-sm-4" data-id="radio-item">
												<div class="inner position-relative">
													<div class="input-group">
														<input type="text" name="feedbackContents" class="form-control" placeholder="选项内容" data-rel="feedback-box" datatype="vNoEmpty" 
														nullmsg="请填写选项内容" errormsg="请填写选项内容" value="一般般">
														<div class="input-group-btn">
															<button type="button" class="btn btn-default" data-role="remove-radio"><i class="fa fa-fw fa-remove"></i></button>
														</div>
													</div>
												</div>
											</div>
											<div class="col-sm-4" data-id="radio-item">
												<div class="inner position-relative">
													<div class="input-group">
														<input type="text" name="feedbackContents" class="form-control" placeholder="选项内容" data-rel="feedback-box" datatype="vNoEmpty" nullmsg="请填写选项内容" errormsg="请填写选项内容" value="无帮助">
														<div class="input-group-btn">
															<button type="button" class="btn btn-default" data-role="remove-radio"><i class="fa fa-fw fa-remove"></i></button>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="margin_t10">
											<button type="button" data-role="add-radio" class="btn btn-block btn-default">+添加选项</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
		        </c:if>
		        
		        <div class="form-group">
					<label class="col-md-2 col-sm-2 control-label">封面</label>
					<div class="col-md-8 col-sm-10">
						<button type="button" class="btn btn-default min-width-90px btn-sm btn-add-img-addon"  onclick="javascript:uploadImage('headImgId','headImgUrl');" >添加封面</button>
						<span class="gray9 margin_l10">注：封面分辨率建议大小：800X400px</span>
			            <ul class="list-inline img-gallery img-gallery-md " style="margin-right:-5px;">
				            <li>
				            	<c:if test="${empty  info.attachment  }">
				                <img src="${css }/ouchgzee_com/platform/xlbzr_css/dist/img/images/no_photo.png" id="headImgId" data-role="lightbox" role="button">
				                </c:if>
				                <c:if test="${not empty  info.attachment  }">
				                  <img src="${info.attachment }" id="headImgId" data-role="lightbox" role="button">
				                </c:if>
				                <span class="glyphicon glyphicon-remove-sign" id="delete-img" data-toggle="tooltip" data-html="true" title="删除" ></span>
				                <!-- <a class="img-fancybox f12" data-role="img-fancybox" href="http://172.16.170.119:801/ouchgzee_com/platform/xlbzr_css/dist/img/images/no_photo.png">点击放大</a> -->
				            </li>
				        </ul>
				         <input type="hidden" value="${info.attachment }" name="attachment" id="headImgUrl" >
					</div>
				</div>
		        
						
						
						<!-- <div class="form-group">
							<label class="col-md-2 col-sm-2 control-label no-pad-top">是否推送到微信</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<div class="radio form-control-static no-padding">
									<label class="margin_r15"> <input type="checkbox" name="isWeiXin" value="1" class="minimal" checked="checked">
									</label>
								</div>
							</div>
						</div> -->
		<%-- 				</c:if> --%>

					<%-- 	<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>通知内容</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<div class="textarea-box">
									<textarea id="editor1" name="infoContent" rows="10" cols="80" class="full-width position-absolute show" datatype="*" nullmsg="请填写通知内容"
										errormsg="请填写通知内容">${info.infoContent }</textarea>
								</div>
							</div>
						</div> 
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">上传附件</label>
							<div class="col-md-8 col-sm-10">
								<input type="text" id="fileName" name="fileName" value="${info.fileName }" readonly="readonly"> <input type="hidden" id="fileUrl"
									name="fileUrl" value="${info.fileUrl }"> <input value="选择文件" type="button" onclick="javascript:uploadFile('fileName','fileUrl');" />
								<input value="删除附件" type="button" id="deleteFile" />
							</div>
						</div>
						--%>
						<%-- <div class="form-group">
							<label class="col-md-2 col-sm-2 control-label no-pad-top">通知置顶</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<div class="radio form-control-static no-padding">
									<label class="margin_r15"> <input type="radio" data-role="top" name="stick" data-value="0" value="0"
										<c:if test="${empty  isStick or isStick==false  }">checked</c:if>> 不置顶
									</label> <label> <input type="radio" data-role="top" name="stick" data-value="1" value="1" <c:if test="${isStick}">checked</c:if>>
										置顶
									</label>
								</div>
							</div>
						</div>
						<div class="form-group" style="display: none;" data-id="top-time">
							<div class="clearfix">
								<label class="col-md-2 col-sm-2 control-label">置顶时间</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<select name="days" class="form-control" nullmsg="请选择置顶时长！" errormsg="请选择置顶时长！" data-id="top-time-form-control">
										<option value="">请选择置顶时间</option>
										<option value="7">置顶1个星期</option>
										<option value="15">置顶半个月</option>
										<option value="30">置顶1个月</option>
										<option value="90">置顶3个月</option>
										<option value="182">置顶半年</option>
										<option value="365">置顶1年</option>
										<option value="3650">一直置顶</option>
										<option value="0" data-value="custom">自定义置顶时间</option>
									</select>
								</div>
							</div>
							<div class="clearfix margin_t15" style="display: none;" data-id="custom-time">
								<label class="col-md-2 col-sm-2 control-label"></label>
								<div class="col-md-8 col-sm-10 position-relative">
									<input type="text" name="times" class="form-control" placeholder="设置置顶结束时间" nullmsg="请设置置顶结束时间" errormsg="时间格式不对" data-role="datetime">
								</div>
							</div>
						</div> --%>

						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"></label>
							<div class="col-md-8 col-sm-10">
								<button type="submit" id="btnSubmit" class="btn btn-success min-width-90px margin_r15" data-role="submite">发布</button>
								<button type="button" class="btn btn-default min-width-90px" data-role="cancel" onclick="history.back()">取消</button>
							</div>
						</div>
					</div>
				</div>
				<!-- /.box-body -->
			</div>
			</div>

		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

	<jsp:include page="/eefileupload/upload.jsp" />
<script type="text/javascript">
	var newMessageId=$('#newMessageId').val();
$(function() {
		$(".select2").select2({language: "zh-CN"});
		//iCheck for checkbox and radio inputs
		/* $('input.minimal').iCheck({
			checkboxClass : 'icheckbox_minimal-blue',
			radioClass : 'iradio_minimal-blue'
		}); */
		//初始化全部下拉
		/* $("div[data-id='more-items']").slideDown("fast"); */
		/* .on("ifChecked",function(e){
			var $e=$(e.target);
			$e.attr('checked',true);
			if($e.is(":radio")){
				if($e.val()=="student"){
					$("div[data-id='more-items']").slideDown("fast");
				}
				else{
					$("div[data-id='more-items']").slideUp("fast");
				}
			}
		}).on("ifUnchecked",function(e){
			$(e.target).attr('checked',false);
		}); */

		//编辑器
		CKEDITOR.replace('editor4', {
			on : {
				change : function(evt) {
					evt.sender.element.$.value = evt.editor.getData();
				},
				instanceReady : ckeditorPaste
			}
		});
		
		//表单提交验证
		 var $theform=$("#theform");
		    var htmlTemp='<div class="tooltip top" role="tooltip">'
		          +'<div class="tooltip-arrow"></div>'
		          +'<div class="tooltip-inner"></div>'
		          +'</div>';
		    $theform.find(".position-relative").each(function(index, el) {
		        $(this).append(htmlTemp);
		    });
		    $.Tipmsg.r='';
		    var postIngIframe;
		    var postForm=$theform.Validform({
		      showAllError:true,
		      ajaxPost:true,
		      ignoreHidden:true,//是否忽略验证不可以见的表单元素
		      datatype:{
		        	"vNoEmpty":function(gets,obj,curform,regxp){
		        		var $rel=obj.closest('[data-role="'+obj.data('rel')+'"]');

		        		if(regxp['*'].test(gets) && $rel.is(':visible')){return true;}

		        		return false;
		        	}
		        },
		      tiptype:function(msg,o,cssctl){
		        if(!o.obj.is("form")){
		            var msgBox=o.obj.closest('.position-relative').find('.tooltip');
		            msgBox.css({
		            	'z-index':2,
			            bottom:"100%",
			            'margin-bottom':-5
			        }).children('.tooltip-inner').text(msg);
		            switch(o.type){
		              case 3:
		                msgBox.addClass('in');
		                break;
		              default:
		                msgBox.removeClass('in');
		                break;
		            }
		        }
		      },
		      beforeSubmit:function(curform){
		        postIngIframe=$.formOperTipsDialog({
		          text:'数据提交中...',
		          iconClass:'fa-refresh fa-spin'
		        });
		      },
		      callback:function(data){
				
				setTimeout(function(){
					postIngIframe.find('[data-role="tips-text"]').html(data.message);
					postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
					
					setTimeout(function(){
					$.closeDialog(postIngIframe);
						if(data.successful==true){
							window.location.href='${ctx}/admin/messageInfo/putList';
						}
					},1000); 
				},2000); 
			}
	    });
		    
		//删除上传附件	
		$('#deleteFile').click(function() {
			$('#fileName').val('');
			$('#fileUrl').val('');
		});

		//置顶选项
		$('[data-role="top"]').click(function(event) {
			if ($(this).prop('checked')&& $(this).attr('data-value') == 1) {
				$('[data-id="top-time"]').show();
				$('[data-id="top-time-form-control"]').attr('datatype','*');
			} else {
				$('[data-id="top-time"]').hide();
				$('[data-id="top-time-form-control"]').removeAttr('datatype').removeClass('Validform_error');
			}
		});

		//自定义置顶时间
		$('[data-id="top-time-form-control"]').on('change',function(event) {
			if ($(this).children().eq(this.selectedIndex).attr('data-value') == 'custom') {
				$('[data-id="custom-time"]').show();
				$('[data-role="datetime"]').attr('datatype', '*');
			} else {
				$('[data-id="custom-time"]').hide();
				$('[data-role="datetime"]').removeAttr('datatype').removeClass('Validform_error');
			}
		});
		/*日期控件*/
		$('[data-role="datetime"]').datepicker({
			language : 'zh-CN',
			format : 'yyyy-mm-dd',
			startDate:increaseOnedate(new Date())
		});
		
		
		//查询导入和在线选择人数
		$('[data-role="seachImportCount"]').click(function(){
			 $.get('${ctx}/admin/messageInfo/getImportStudentCount',{messageId:newMessageId},function(data){
					$('[data-role="view-sel-stu"]').text(data.obj);
				},'json'); 
		});
		
		//条件查询人数
		$('[data-role="seachCount"]').click(function(){
			var gradeIds="";
			var gradeIdAll=$('[name="gradeIdAll"]:checked').val();
			$('[name="gradeIds"]:checked').each(function(i){
				gradeIds+=$(this).data('id')+",";
			}); 
			
			var pyccIds="";
			var pyccIdAll=$('[name="pyccIdAll"]:checked').val();
			$('[name="pyccIds"]:checked').each(function(i){
				pyccIds+=$(this).data('id')+",";
			}); 
			
			var specialtyIds="";
			var specialtyIdAll=$('[name="specialtyIdAll"]:checked').val();
			$('[name="specialtyIds"]:checked').each(function(i){
				specialtyIds+=$(this).data('id')+",";
			}); 
			
			var courseIds="";
			var courseIdAll=$('[name="courseIdAll"]:checked').val();
			$('[name="courseIds"]:checked').each(function(i){
				courseIds+=$(this).data('id')+",";
			}); 
			
			var userTypes="";
			var userTypeAll=$('[name="userTypeAll"]:checked').val();
			$('[name="userTypes"]:checked').each(function(i){
				userTypes+=$(this).data('id')+",";
			}); 
			
			var xjztTypes="";
			var xjztTypeAll=$('[name="xjztTypeAll"]:checked').val();
			$('[name="xjztTypes"]:checked').each(function(i){
				xjztTypes+=$(this).data('id')+",";
			}); 
			
			$('#overlay').show();
			 $.post('${ctx}/admin/messageInfo/getStudentCount',{
					gradeIds:gradeIds,
					gradeIdAll:gradeIdAll,
					pyccIds:pyccIds,
					pyccIdAll:pyccIdAll,
					specialtyIds:specialtyIds,
					specialtyIdAll:specialtyIdAll,
					courseIds:courseIds,
					courseIdAll:courseIdAll,
					userTypes:userTypes,
					userTypeAll:userTypeAll,
					xjztTypes:xjztTypes,
					xjztTypeAll:xjztTypeAll			
				},function(data){
					$('#overlay').hide();
					$('[data-role="view-sel-stu1"]').text(data.obj);
				},'json'); 
				
			});
	});
	
	

	//选项操作
	$(document).on('click','.item-sel-box .btn:not([data-role])',function(e){
		//如果点击的是删除操作，那么就不执行下面操作
		if(e.target.nodeName=='I'){
			return;
		} 
		
		if( $(this).hasClass('active') ){
			$(this).removeClass('active').children(':checkbox').prop('checked',false);
		}
		else{
			$(this).addClass('active').children(':checkbox').prop('checked',true);
		}
		var $selItems=$(this).siblings('span.btn');
		//如果点击的是“全选”
		if( $(this).attr('data-id') ){
			if($(this).hasClass('active')){
				$selItems.addClass('active').children(':checkbox').prop('checked',true);
			}else{
				$selItems.removeClass('active').children(':checkbox').prop('checked',false);
			}
		}else{//点击单一选项
			/* if($(this).hasClass('active')){ */
				$(this).siblings('[data-id="sel-all"]').removeClass('active').children(':checkbox').prop('checked',false);
			/* } */
		}
	}).on('click', '[data-role="remove-sel"]', function(event) {//删除 专业 / 课程 
		var $p=$(this).parent();
		var $brothers=$p.siblings('span.btn.active');
		$p.remove();
	}).on('click', '[data-role="add-major"]', function(event) {//添加专业
		$.mydialog({
		    id:'export',
		    width:850,
		    height:550,
		    zIndex:11000,
		    content: 'url:findSpecialty'
		});
	}).on('click', '[data-role="add-course"]', function(event) {//添加课程
		$.mydialog({
		    id:'export',
		    width:850,
		    height:550,
		    zIndex:11000,
		    content: 'url:findCourse'
		});
	}).on('click', '[data-role="import"]', function(event) {//批量导入学员
	  event.preventDefault();
	  $.mydialog({
	    id:'import',
	    width:600,
	    height:415,
	    zIndex:11000,
	    content: 'url:${ctx}/admin/messageInfo/importStudent?newMessageId='+newMessageId
	  });
	}).on('click', '[data-role="sel-online"]', function(event) {//在线选择学员
	  $.mydialog({
	    id:'sel-online-student',
	    width:800,
	    height:600,
	    zIndex:11000,
	    content: 'url:${ctx}/admin/messageInfo/searchOlineStudent?newMessageId='+newMessageId
	  });
	}).on('click', '[data-role="view-sel-stu1"]', function(event) {//查看根据条件选择的学员
		var gradeIds="";
		var gradeIdAll=$('[name="gradeIdAll"]:checked').val();
		$('[name="gradeIds"]:checked').each(function(i){
			gradeIds+=$(this).data('id')+",";
		}); 
		
		var pyccIds="";
		var pyccIdAll=$('[name="pyccIdAll"]:checked').val();
		$('[name="pyccIds"]:checked').each(function(i){
			pyccIds+=$(this).data('id')+",";
		}); 
		
		var specialtyIds="";
		var specialtyIdAll=$('[name="specialtyIdAll"]:checked').val();
		$('[name="specialtyIds"]:checked').each(function(i){
			specialtyIds+=$(this).data('id')+",";
		}); 
		
		var courseIds="";
		var courseIdAll=$('[name="courseIdAll"]:checked').val();
		$('[name="courseIds"]:checked').each(function(i){
			courseIds+=$(this).data('id')+",";
		}); 
		
		var userTypes="";
		var userTypeAll=$('[name="userTypeAll"]:checked').val();
		$('[name="userTypes"]:checked').each(function(i){
			userTypes+=$(this).data('id')+",";
		}); 
		
		var xjztTypes="";
		var xjztTypeAll=$('[name="xjztTypeAll"]:checked').val();
		$('[name="xjztTypes"]:checked').each(function(i){
			xjztTypes+=$(this).data('id')+",";
		}); 
		
	  $.mydialog({
	    id:'view-student1',
	    width:800,
	    height:750,
	    zIndex:11000,
	    urlData:{
	    	gradeIds:gradeIds,
			gradeIdAll:gradeIdAll,
			pyccIds:pyccIds,
			pyccIdAll:pyccIdAll,
			specialtyIds:specialtyIds,
			specialtyIdAll:specialtyIdAll,
			courseIds:courseIds,
			courseIdAll:courseIdAll,
			userTypes:userTypes,
			userTypeAll:userTypeAll,
			xjztTypes:xjztTypes,
			xjztTypeAll:xjztTypeAll	
	    },  //frameElement.data.gradeIds
	    content: 'url: ${ctx}/admin/messageInfo/searchStudent?isTranse=Y'
	  });
	}).on('click', '[data-role="view-sel-stu"]', function(event) {//查看已导入的学员
	  $.mydialog({
	    id:'view-student',
	    width:800,
	    height:600,
	    zIndex:11000,
	    content: 'url:${ctx}/admin/messageInfo/searchImportStudent?newMessageId='+newMessageId
	  });
	}).on('click', '[data-role="review"]', function(event) {//预览通知
		$.mydialog({
		    id:'review',
		    width:$(window).width(),
		    height:$(window).height(),
		    zIndex:11000,
		    content: 'url:预览通知-弹窗.html'
		  });
	}).on('mouseenter', '[data-id="dropdown"]', function(event) {//分类下拉菜单-----start
		if($('select[name="infoType"]').val()!=''){
			$(this).addClass('open');
		}
	}).on('mouseleave', '[data-id="dropdown"]', function(event) {
		$(this).removeClass('open');
	}).on('click', '[data-role="cate-item"]', function(event) {//选择分类
		var $target=$(event.target);
		if($target.data('role')!='edit' && $target.data('role')!='txt-input' && $target.data('role')!='remove-item'){
			var $li=$(this).closest('li');
			var $txt=$li.find('[data-role="txt"]');
			if($txt.length>0){
				var $list=$('[data-role="cate-item"]');
				var index=$list.index($li);

				$('[data-id="cate-input"]').val( $.trim($txt.text()) )
				.attr('rel-item',index).removeClass('Validform_error')
				.closest('.position-relative').children('.tooltip').removeClass('in');
				
				$('#typeClassify').val($li.data('id'));
				
				$list.removeClass('on');
				$li.addClass('on');
				
				$('[data-id="dropdown"]').removeClass('open');
			}
		}
	})
	.on('click', '[data-role="edit"]', function(event) {
		//编辑操作
		if($(this).hasClass('fa-edit')){
			$(this).removeClass('fa-edit').addClass('fa-check-square').attr('title', '确定');

			var $txt=$(this).closest('li').find('[data-role="txt"]');
			$txt.parent().html('<input data-role="txt-input" class="form-control input-xs" value="'+$.trim($txt.text())+'" data-id="'+$(this).data('id')+'">');
		}
		//确认编辑
		else{
			var $txtInput=$(this).closest('li').find('[data-role="txt-input"]');
			editCate.call( $txtInput );
			var $id=$(this).data('id');
			var $txt=$(this).closest('li').find('[data-role="txt"]').text();
			
			$.post('updateClassify',{id:$id,text:$txt},function(data){
				if(!data.successful){
					alert(data.message);
				}
			},'json'); 
		}
	})
	//enter键确定修改分类
	.on('keypress', '[data-role="txt-input"]', function(event) {
		if(event.keyCode==13){
			editCate.call( $(this) );
			event.preventDefault();
			
			var $id=$(this).data('id');
			var $txt=$(this).val();
			
			$.post('updateClassify',{id:$id,text:$txt},function(data){
				if(!data.successful){
					alert(data.message);
				}
			},'json'); 
		}
	})
	//删除分类
	.on('click', '[data-role="remove-item"]', function(event) {
		var _this=$(this);
		var $list=$('[data-role="cate-item"]'),
		$li=_this.closest('li'),
		index=$list.index($li),
		$cateInput=$('[data-id="cate-input"]');

		if(index==$cateInput.attr('rel-item')){
			$cateInput.val('');
		}
		
		var $id=_this.data('id');
		$.post('deleteClassify',{id:$id},function(data){
			if(data.successful){
				_this.closest('li').remove();
			}else{
				alert(data.message);
			}
		},'json'); 
		
	})
	//添加分类
	.on('click', '[data-id="add-btn"]', function(event) {
		var $txtInput=$('[data-id="add-input"]');
		if( $.trim($txtInput.val())!='' ){
			addCate.call($txtInput);
		}
	})
	//enter键增加分类
	.on('keypress', '[data-id="add-input"]', function(event){
		if(event.keyCode==13){
			if( $.trim($(this).val())!='' ){
				addCate.call($(this));
			}
			event.preventDefault();
		}
	})
	
	
	
	$('select[name="infoType"]').change(function(){
		var infoTypeId=$(this).val();
		var _this=$('[data-role="cate-menu"]');
		
		$('#typeClassify').val('');
		$('#typeClassifyId').val('');
		
		$('[data-role="cate-menu"]').empty();
		
		if(!infoTypeId) return;//如果没有选择项目，以下逻辑不执行
		
		$("#overlay2").show();
		
		$.get('queryTypeClassifyByInfoType',{infoType:infoTypeId},function(data){
			var list=data.obj;
			//_this.children().filter(function(){ return !$(this).data('id')=='cate-form-control'}).remove();
			_this.empty();			
			var arrTpl=[];
			 for(var o in list){  
				 arrTpl.push(
							'<li data-role="cate-item" data-id="'+list[o].id+'">'+
								'<a href="javascript:;" class="pad10 border-bottom" data-id="dropdown-item">'+
									'<div class="pull-right pad-l10 f16">'+
										'<i class="fa fa-fw fa-edit" data-role="edit" title="编辑" data-id="'+list[o].id+'"></i>'+
										'<i class="fa fa-fw fa-remove" data-role="remove-item" title="删除" data-id="'+list[o].id+'"></i>'+
									'</div>'+
									'<div class="oh f12"><span data-role="txt">'+list[o].name+'</span></div>'+
								'</a>'+
							'</li>');
		    }
			if(arrTpl.length>0){
			 	_this.prepend(arrTpl.join(''));
			}
			
			_this.append('\
					<li data-id="cate-form-control">\
		      		<div class="input-group input-group-sm">\
		      			<input class="form-control flat" data-id="add-input" placeholder="请输入分类名称">\
		      			<div class="input-group-btn">\
		      				<button type="button" class="btn btn-default flat" data-id="add-btn">添加分类</button>\
		      			</div>\
		      		</div>\
		      	</li>	\
			');
			
			$('[data-id="dropdown"]').addClass('open');
			$("#overlay2").hide();
		},'json'); 
		
	});

	//添加分类
	function addCate(){
		if($('[data-role="cate-item"]').length>=6){
			this.blur().val('');
		    $.resultDialog(
				{
					type:0,
					msg:'最多可以添加6个分类',
					timer:2500,
					width:250,
					height:50,
					zIndex:1000
				}
			);
		}
		else{
			var _this=this;
			var sd=$.trim(_this.val());
			var infoType=$('select[name="infoType"]').val();
			if(infoType==''){
				alert('请先选择类型！');
			}else{
				//插入到数据库
			 	$.post('addClassify',{text:$.trim(_this.val()),infoType:infoType},function(data){
					if(data.successful){
						_this.closest('li').before(
								'<li data-role="cate-item" data-id="'+data.obj+'">'+
									'<a href="javascript:;" class="pad10 border-bottom" data-id="dropdown-item">'+
										'<div class="pull-right pad-l10 f16">'+
											'<i class="fa fa-fw fa-edit" data-role="edit" title="编辑" data-id="'+data.obj+'"></i>'+
											'<i class="fa fa-fw fa-remove" data-role="remove-item" title="删除" data-id="'+data.obj+'"></i>'+
										'</div>'+
										'<div class="oh f12"><span data-role="txt">'+$.trim(_this.val())+'</span></div>'+
									'</a>'+
								'</li>');
						_this.val('');
					}
			 	},'json'); 
			}
		}
	}

	//编辑分类
	function editCate(){
		var v=$.trim(this.val());
		if(v!=''){
			var $list=$('[data-role="cate-item"]'),
				$li=this.closest('li'),
				index=$list.index($li),
				$cateInput=$('[data-id="cate-input"]');

			var $txtBtn=$li.find('[data-role="edit"]');
			$txtBtn.removeClass('fa-check-square').addClass('fa-edit').attr('title', '编辑');
			this.parent().html('<span data-role="txt">'+v+'</span>');

			if(index==$cateInput.attr('rel-item')){
				$cateInput.val( v );
			}
		}
	}
	//分类下拉菜单-----end


	$(document)
	//内容 类型选择
	.on('click', '[data-role="cnt-type"]', function(event) {
		var index=$(this).data('value');
		$('[data-role="tab-item"]:eq('+index+')').show().siblings().hide();
	})
	//视频预览
	.on('click', '[data-role="video-obj"]', function(event) {
		var w=650, h=470;
		$.alertDialog({
	        id:'video',
	        title:'视频预览',
	        width:w,
	        height:h,
	        zIndex:11000,
	        cancel:false,
	        ok:false,
	        content:'\
	        	<div>\
	        		<video src="http://v.eecn.cn/gw_xuelijiaoyu/dd_xuelijiaoyu/2013/qiyebanzu/cha08_03.mp4?start=0" width="'+(w-20)+'" height="'+(h-20-51)+'" style="object-fit: fill;" controls></video>\
	        	</div>\
	        '
	    });
	})
	//小视频 类型选择
	.on('click', '[data-role="video-url"]', function(event) {
		var index=$(this).data('value');
		$('[data-role="video-tab"]:eq('+index+')').show().siblings().hide();
	})
	//开放功能-是否选中“反馈”
	.on('click', '[data-role="open-fn-type"][data-value="2"]', function(event) {
		if($(this).prop('checked')){
			$('[data-role="feedback-box"]').show();
		}
		else{
			$('[data-role="feedback-box"]').hide();
		}
	})
	//删除“反馈”选项
	.on('click', '[data-role="remove-radio"]', function(event) {
		if($('[data-id="radio-edit"]').children().length>2){
			$(this).closest('[data-id="radio-item"]').remove();
		}
	})
	//增加“反馈”选项
	.on('click', '[data-role="add-radio"]', function(event) {
		var html='\
			<div class="col-sm-4" data-id="radio-item">\
				<div class="inner position-relative">\
					<div class="input-group">\
						<input type="text"  name="feedbackContents" class="form-control" placeholder="选项内容" data-rel="feedback-box" datatype="vNoEmpty" nullmsg="请填写选项内容" errormsg="请填写选项内容">\
						<div class="input-group-btn">\
							<button type="button" class="btn btn-default" data-role="remove-radio"><i class="fa fa-fw fa-remove"></i></button>\
						</div>\
					</div>\
				</div>\
			</div>\
		';
		$('[data-id="radio-edit"]').append(html);
	})
	//公众号推送 类型
	.on('click', '[data-role="public-push-type"]', function(event) {
		var v=$(this).val();
		if(v==1){
			$('[data-role="public-push"]').show();
		}
		else{
			$('[data-role="public-push"]').hide();
		}
	});
	
	//删除封面
	$('#delete-img').click(function(){
		$('#headImgId').attr('src','${css }/ouchgzee_com/platform/xlbzr_css/dist/img/images/no_photo.png');
		$('#headImgUrl').val('');
	});
	
	
	$('#condition_yes').click(function(){
		$('[name="isAppoint"]').val('0');
	});
	
	$('#condition_no').click(function(){
		$('[name="isAppoint"]').val('1');
	});
	
</script>

<!-- 加载的时候转圈圈 -->
<div class="overlay" style="display:none;  position: fixed;" id="overlay">
   <i class="fa fa-refresh fa-spin"></i>
 </div>
</body>
</html>