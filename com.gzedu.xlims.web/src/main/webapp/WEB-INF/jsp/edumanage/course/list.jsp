<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li class="active">课程管理</li>
	</ol>
</section>
<section class="content">
<form id="listForm" class="form-horizontal">
	<input type="hidden" name="search_EQ_courseNature" value="${param['search_EQ_courseNature']}">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<c:if test="${isBtnFormalTab}">
				<li <c:if test="${param['search_EQ_courseNature'] == '1' || empty param['search_EQ_courseNature']}">class="active"</c:if>><a href="list?search_EQ_courseNature=1" target="_self">正式课程</a></li>
			</c:if>
			<c:if test="${isBtnExperienceTab}">
				<li <c:if test="${param['search_EQ_courseNature'] == '2' || (!isBtnFormalTab && empty param['search_EQ_courseNature'])}">class="active"</c:if>><a href="list?search_EQ_courseNature=2" target="_self">体验课程</a></li>
			</c:if>
			<c:if test="${isBtnReplaceTab}">
				<li <c:if test="${param['search_EQ_courseNature'] == '3' || (!isBtnFormalTab && !isBtnExperienceTab && empty param['search_EQ_courseNature'])}">class="active"</c:if>><a href="list?search_EQ_courseNature=3" target="_self">替换课程</a></li>
			</c:if>
		</ul> 
			
		<div class="tab-content">
			<div class="box box-border">
			    <div class="box-body">
			        <div class="row pad-t15">
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">课程代码</label>
			              <div class="col-sm-9">
			                <input class="form-control" type="text" name="search_LIKE_kch" value="${param.search_LIKE_kch}">
			              </div>
			            </div>
			          </div>
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">课程名称</label>
			              <div class="col-sm-9">
			                <input class="form-control" type="text" name="search_LIKE_kcmc" value="${param.search_LIKE_kcmc}">
			              </div>
			            </div>
			          </div>
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">教学方式</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_wsjxzk" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${wsjxzkMap}" var="s">
									<option value="${s.key}"  <c:if test="${s.key==param['search_EQ_wsjxzk']}">selected='selected'</c:if>>${s.value}</option>
								</c:forEach>
							</select>
			              </div>
			            </div>
			          </div>
			          <%-- <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">课程性质</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_courseNature" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected='selected'>请选择</option>
								<c:forEach items="${courseNatureMap}" var="s">
									<option value="${s.key}"  <c:if test="${s.key==param['search_EQ_courseNature']}">selected='selected'</c:if>>${s.value}</option>
								</c:forEach>
							</select>
			              </div>
			            </div>
			          </div> --%>
			          <c:choose>
					  		<c:when test="${param['search_EQ_courseNature'] != '3'}">
					          <%-- <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">适用行业</label>
					              <div class="col-sm-9">
									<select name="search_EQ_syhy" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${syhyMap}" var="s">
											<option value="${s.key}"  <c:if test="${s.key==param['search_EQ_syhy']}">selected='selected'</c:if>>${s.value}</option>
										</c:forEach>
									</select>
					              </div>
					            </div>
					          </div> --%>
					          <div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">适用专业</label>
					              <div class="col-sm-9">
									<select name="search_LIKE_syzy" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${syzyMap}" var="s">
											<option value="${s.key}"  <c:if test="${s.key==param['search_LIKE_syzy']}">selected='selected'</c:if>>${s.value}</option>
										</c:forEach>
									</select>
					              </div>
					            </div>
					          </div>
					  		</c:when>
					  		<c:otherwise>
					  			<div class="col-sm-4">
					            <div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">可替换课</label>
					              <div class="col-sm-9">
									<select name="search_LIKE_replaceCourseId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${replaceCourses}" var="course">
											<option value="${course.courseId}"  <c:if test="${course.courseId==param['search_LIKE_replaceCourseId']}">selected='selected'</c:if>>${course.kcmc}(${course.kch})</option>
										</c:forEach>
									</select>
					              </div>
					            </div>
					          </div>
					  		</c:otherwise>
					  </c:choose>
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">状态</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_isEnabled" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<option value="1" <c:if test="${param.search_EQ_isEnabled=='1'}">selected='selected'</c:if>>已全部启用</option>
								<option value="2" <c:if test="${param.search_EQ_isEnabled=='2'}">selected='selected'</c:if>>建设中</option>
								<option value="0" <c:if test="${param.search_EQ_isEnabled=='0'}">selected='selected'</c:if>>暂无资源</option>
								<option value="3" <c:if test="${param.search_EQ_isEnabled=='3'}">selected='selected'</c:if>>待验收</option>
								<option value="4" <c:if test="${param.search_EQ_isEnabled=='4'}">selected='selected'</c:if>>验收不通过</option>
								<option value="5" <c:if test="${param.search_EQ_isEnabled=='5'}">selected='selected'</c:if>>部分启用</option>
							</select>
			              </div>
			            </div>
			          </div>
		
			        </div>
			      
			    </div><!-- /.box-body -->
			    <div class="box-footer">
			      <div class="btn-wrap">
						<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
			    </div><!-- /.box-footer-->
			</div>
			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="box">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">课程列表</h3>			
					<div class="fr">
						<div class="btn-wrap fl">
							<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnCreateFormal) || (param['search_EQ_courseNature'] == '2' && isBtnCreateExperience) || (param['search_EQ_courseNature'] == '3' && isBtnCreateReplace)}">
								<a href="create?courseNature=${param['search_EQ_courseNature']}" class="btn btn-default btn-sm">	<i class="fa fa-fw fa-plus"></i> 新建课程</a>
							</c:if>
						</div>
					</div>
				</div>
				<div class="box-body">
					<div class="filter-tabs filter-tabs2 clearfix">
						<ul class="list-unstyled">
							<li lang=":input[name='search_EQ_isEnabled']" <c:if test="${empty param['search_EQ_isEnabled']}">class="actived"</c:if>>全部(${isEnabledNum + isNoResourcesNum + isConstructionNum})</li>
							<li value="1" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == 1 }">class="actived"</c:if>>已全部启用(${isEnabledNum})</li>
							<li value="5" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == 5 }">class="actived"</c:if>>部分启用(${isPortionNum})</li>
							<li value="3" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == 3 }">class="actived"</c:if>>待验收(${isWaitNum})</li>
							<li value="4" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == 4 }">class="actived"</c:if>>验收不通过(${isNotNum})</li>
							<li value="2" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == 2 }">class="actived"</c:if>>建设中(${isConstructionNum})</li>
							<li value="0" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == '0' }">class="actived"</c:if>>暂无资源(${isNoResourcesNum})</li>
						</ul>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
				              <tr>
				                <th>课程代码</th>
				                <th>课程名称</th>
				                <c:choose>
				                	 <c:when test="${param['search_EQ_courseNature'] != '3'}">
				                	 	<th>课程属性</th>
						                <!-- <th>适用行业</th> -->
						                <th>适用专业</th>
									  	<shiro:lacksPermission name="/studymanage//edumanage/course/list$schoolModel">
						                	<th>学分</th>
										</shiro:lacksPermission>
						                <th>学时</th>
						                <th width="15%">配置教材</th>
				                	 </c:when>
				                	 <c:otherwise>
				                	 	<th>课程类型</th>
				                	 	<th>教学方式</th>
						                <th>可替换课</th>
				                	 </c:otherwise>
				                </c:choose>
				                <!-- <th>所属院校</th> -->
				                <th>状态</th>
				                <th>操作</th>
				              </tr>
				            </thead>
				            <tbody>
				           		 <c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.content}" var="entity">
											<c:if test="${not empty entity}">
								            	<tr>
								            		<td>
								            			<input type="hidden" name="courseId" value="${entity.courseId}">
								            			${entity.kch}
								            		</td>
								            		<td>
								            			<span class="kcmc">${entity.kcmc}</span>
								            		</td>
								            		<c:choose>
									                	 <c:when test="${param['search_EQ_courseNature'] != '3'}">
										            		<td style="text-align: left;">
										            			<%-- 课程类型：${courseTypeMap[entity.courseType]}<br> --%>
				
																教学方式：${wsjxzkMap[entity.wsjxzk]}<br>
																
																<%-- 课程性质：${courseNatureMap[entity.courseNature]}<br> --%>
																
																课程层次：${pyccMap[entity.pycc]}<br>
																
																<%-- 学科：${subjectMap[entity.subject]}<br>
																
																学科门类：${categoryMap[entity.subject][entity.category]} --%>
										            			
										            			课程类型：
										            			<%-- <c:choose>
										            				<c:when test="${entity.courseCategory == 0}">普通课程</c:when>
										            				<c:when test="${entity.courseCategory == 1}">社会实践</c:when>
										            				<c:when test="${entity.courseCategory == 2}">毕业论文</c:when>
										            			</c:choose> --%>
										            			${courseCategoryMap[entity.courseCategory]} 
										            		</td>
										            		<%-- <td>
										            			${syhyMap[entity.syhy]}
										            		</td> --%>
										            		<td>
											                	<c:if test="${not empty entity.syzy}">
											                		<c:forEach items="${fn:split(entity.syzy, ',')}" var="syzy">
											                			${syzyMap[syzy]} <br>
											                		</c:forEach>
											                	</c:if>
										            		</td>
															<shiro:lacksPermission name="/studymanage//edumanage/course/list$schoolModel">
										            			<td>${entity.credit}</td>
															</shiro:lacksPermission>
										            		<td>${entity.hour}</td>
										            		<td>
										            			<c:if test="${empty(entity.gjtTextbookList) }">--</c:if>
										            			<c:forEach items="${entity.gjtTextbookList }" var="tb">
										            				${tb.textbookName }<br/>
										            			</c:forEach>
										            		</td>
									                	 </c:when>
									                	 <c:otherwise>
									                	 	<td>
									                	 		<%-- <c:choose>
										            				<c:when test="${entity.courseCategory == 0}">普通课程</c:when>
										            				<c:when test="${entity.courseCategory == 1}">社会实践</c:when>
										            				<c:when test="${entity.courseCategory == 2}">毕业论文</c:when>
										            			</c:choose> --%>
										            			${courseCategoryMap[entity.courseCategory]} 
									                	 	</td>
									                	 	<td>${wsjxzkMap[entity.wsjxzk]}</td>
											                <td>
											                	<c:if test="${not empty entity.replaceCourseId}">
											                		<c:forEach items="${fn:split(entity.replaceCourseId, ',')}" var="courseId">
											                			<c:forEach items="${replaceCourses}" var="replaceCourse">
											                				<c:if test="${replaceCourse.courseId eq courseId}">
											                					${replaceCourse.kcmc}(${replaceCourse.kch}) <br>
											                				</c:if>
											                			</c:forEach>
											                		</c:forEach>
											                	</c:if>
											                </td>
									                	 </c:otherwise>
									                </c:choose>
								            		<%-- <td>${entity.gjtOrg.orgName}</td> --%>
								            		<td >
								            		<c:if test="${entity.isEnabled==1}"><font class="text-green">已全部启用</font></c:if>
								            		<c:if test="${entity.isEnabled=='0'}"><font class="gray9">暂无资源</font></c:if>
								            		<c:if test="${entity.isEnabled==2}"><font class="text-orange">建设中</font></c:if>
								            		<c:if test="${entity.isEnabled==3}"><font class="text-orange">待验收</font></c:if>
								            		<c:if test="${entity.isEnabled==4}"><font class="text-red">验收不通过</font></c:if>
								            		<c:if test="${entity.isEnabled==5}">
								            			<font class="text-green">
								            				部分启用 <br/> ${entity.proportion}
								            			</font>
								            		</c:if>
								            		</td>	
								            		<td class="data-operion">
								            			<%-- <c:if test="${user.gjtOrg.id == entity.gjtOrg.id }"> --%>
								            				<%-- <c:if test="${entity.isEnabled==1}">
															<a href="share/${entity.courseId}"
																class="operion-item" title="分享"
																data-toggle="tooltip" title="共享课程" data-role='share'>
																<i class="fa fa-share-alt"></i></a> 
															</c:if> --%>
															
															<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnViewFormal) || (param['search_EQ_courseNature'] == '2' && isBtnViewExperience) || (param['search_EQ_courseNature'] == '3' && isBtnViewReplace)}">
																<a href="view/${entity.courseId}?courseNature=${param['search_EQ_courseNature']}" class="operion-item" title="查看">
																	<i class="fa fa-fw fa-view-more"></i></a>
															</c:if>
															
														<%-- 	<c:if test="${entity.isEnabled == 0 || entity.isEnabled == 2 || entity.isEnabled == 3}"> --%>
																<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnUpdateFormal) || (param['search_EQ_courseNature'] == '2' && isBtnUpdateExperience) || (param['search_EQ_courseNature'] == '3' && isBtnUpdateReplace)}">
																	<a href="update/${entity.courseId}?courseNature=${param['search_EQ_courseNature']}"	class="operion-item" title="编辑">
																		<i class="fa fa-edit"></i></a> 
															<%-- 	</c:if> --%>
															
																<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnDeleteFormal) || (param['search_EQ_courseNature'] == '2' && isBtnDeleteExperience) || (param['search_EQ_courseNature'] == '3' && isBtnDeleteReplace)}">
																	<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${entity.courseId}" title="删除" data-tempTitle="删除">
																		<i class="fa fa-fw fa-trash-o"></i></a>
																</c:if>
															</c:if>
															
															<c:if test="${(
															(empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnConstructionFormal) 
															|| (param['search_EQ_courseNature'] == '2' && isBtnConstructionExperience) 
															|| (param['search_EQ_courseNature'] == '3' && isBtnConstructionReplace)}">
																<c:if test="${entity.isEnabled == 0 }">
																		<!-- 如果是暂无资源可以更改为建设中 -->
																		<!-- 如果是部分启用则可以转换成建设中的状态 ，又说改定时器 or entity.isEnabled ==5}-->
																	<a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" 
																	 		title="设置为建设中" data-role='construction' data-id="2">
																		<i class="fa fa-exchange"></i>
																	</a>
																</c:if>
															</c:if>
															
															<c:if test="${(
															(empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnExperienceChangeWait) 
															|| (param['search_EQ_courseNature'] == '2' && isBtnExperienceChangeWait) 
															|| (param['search_EQ_courseNature'] == '3' && isBtnReplaceChangeWait)}">
																<c:if test="${entity.isEnabled == 4 or entity.isEnabled == 2}"><!-- 如果是建设中可以更改为待验收状态 ，如果验收不通过也是-->
																	<a href="javascript:void(0);" class="operion-item" data-toggle="tooltip"
																	  		title="是否将课程提交验收" data-role='construction'  data-id="3">
																		<i class="fa fa-check-square-o"></i>
																	</a>
																</c:if>
															</c:if>
															<!-- 如果是待验收或者验收不通过状态，显示验收按钮 -->
															<c:if test="${ entity.isEnabled == 3 and isBtnCheckCourse}"> 
																<a href="checkCourse?courseId=${entity.courseId}&action=check" class="operion-item" data-toggle="tooltip" title="验收课程" >
																	<i class="fa fa-shxxjl"></i>
																</a>
															</c:if>
															
															<c:if test="${isBtnCheckCourseView}"> 
															<a href="checkCourse?courseId=${entity.courseId}&action=view" class="operion-item" data-toggle="tooltip" title="课程验收详情" >
																<i class="glyphicon glyphicon-list-alt"></i>
															</a>
															</c:if>
															<%-- <c:if test="${entity.isEnabled == 2}">
																<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnEnableFormal) || (param['search_EQ_courseNature'] == '2' && isBtnEnableExperience) || (param['search_EQ_courseNature'] == '3' && isBtnEnableReplace)}">
																	<a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="设置为已启用" data-role='issue'>
																		<i class="fa fa-play"></i></a>
																</c:if>
																<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnNoResourcesFormal) || (param['search_EQ_courseNature'] == '2' && isBtnNoResourcesExperience) || (param['search_EQ_courseNature'] == '3' && isBtnNoResourcesReplace)}">
																	<a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="设置为暂无资源" data-role='stop'>
																		<i class="fa fa-warning"></i></a>
																</c:if>
															</c:if> --%>
															
															<%-- <c:if test="${entity.isEnabled == 1}">
																<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnConstructionFormal) || (param['search_EQ_courseNature'] == '2' && isBtnConstructionExperience) || (param['search_EQ_courseNature'] == '3' && isBtnConstructionReplace)}">
																	<a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="设置为建设中" data-role='construction'>
																		<i class="fa fa-wrench"></i></a>
																</c:if>
															</c:if> --%>
															
															<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnChangeCourseNature) || (param['search_EQ_courseNature'] == '2' && isBtnChangeCourseNature)}">
																<a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="转换课程性质" data-role='change'>
																	<i class="fa fa-exchange"></i></a> 
															</c:if>
															
															<c:if test="${entity.wsjxzk == 1}">
																<c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnPreviewFormal) || (param['search_EQ_courseNature'] == '2' && isBtnPreviewExperience) || (param['search_EQ_courseNature'] == '3' && isBtnPreviewReplace)}">
																	<a href="${oclassHost}/processControl/previewCourse.do?formMap.COURSE_ID=${entity.courseId}" target="_blank" class="operion-item" title="预览课程">
																		<i class="fa fa-fw fa-simulated-login"></i></a>
																</c:if>
															</c:if>
														<%-- </c:if> --%>
													
															<a href="#" class="operion-item" data-toggle="tooltip" data-container="body" title="" data-role="config" data-original-title="教材配置" data-id="${entity.courseId }"><i class="fa fa-gear"></i></a>
								            		</td>
								            	</tr>	
								       		</c:if>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td align="center" colspan="9">暂无数据</td>
										</tr>
									</c:otherwise>
								</c:choose>          	
				            </tbody>
						</table>
						<tags:pagination page="${pageInfo}" paginationSize="5" />				
					</div>
				</div>		
			</div>
		</div>
	</div>
	</form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
function choice(flag){
	$("#changetype").val(flag);
	$("#listForm").submit();
}

//删除
/* $("body").confirmation({
  selector: "[data-role='remove-item']",
  html:true,
  placement:'top',
  content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>确定要删除该课程？</div><div class="f12 gray9 margin_b10 text-center">课程：经济数学基础12</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:200,
  onShow:function(event,element){
    
  },
  onConfirm:function(event,element){

  },
  onCancel:function(event, element){
    
  }
}); */

//共享课程
$("[data-role='share']").click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'import',
	  width:600,
	  height:415,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});

//启用
$("html").confirmation({
	selector: "[data-role='issue']",
	html:true,
	placement:'top',
	content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>是否将课程设置为已启用状态？</div><div class="f12 gray9 margin_b10 text-center">课程：{0}<div class="margin_t5"></div></div>',
	title:false,
	btnOkClass    : 'btn btn-xs btn-primary',
	btnOkLabel    : '确认',
	btnOkIcon     : '',
	btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	btnCancelLabel  : '取消',
	btnCancelIcon   : '',
	popContentWidth:250,
	onShow:function(event,element){
	  var $el=$(element);
	  if($el.is('[data-role="issue"]')){
	  	this.content=this.content.format($el.closest('tr').find('.kcmc').text());
	  }
	},
	onConfirm:function(event,element){
		  var id = $(element).closest('tr').find('input[name="courseId"]').val()
		  window.location.href = ctx + "/edumanage/course/enable?id=" + id;
	},
	onCancel:function(event, element){
	  
	}
});

//暂无资源
$("body").confirmation({
	selector: "[data-role='stop']",
	html:true,
	placement:'top',
	content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>是否将课程设置为暂无资源状态？</div><div class="f12 gray9 margin_b10 text-center">课程：{0}<div class="margin_t5"></div></div>',
	title:false,
	btnOkClass    : 'btn btn-xs btn-primary',
	btnOkLabel    : '确认',
	btnOkIcon     : '',
	btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	btnCancelLabel  : '取消',
	btnCancelIcon   : '',
	popContentWidth:250,
	onShow:function(event,element){
	  var $el=$(element);
	  if($el.is('[data-role="stop"]')){
	  	this.content=this.content.format($el.closest('tr').find('.kcmc').text());
	  }
	},
	onConfirm:function(event,element){
		  var id = $(element).closest('tr').find('input[name="courseId"]').val()
		  window.location.href = ctx + "/edumanage/course/disable?id=" + id;
	},
	onCancel:function(event, element){

	}
});

//建设中
$(".data-operion").confirmation({
	selector: "[data-role='construction']",
	html:true,
	placement:'top',
	content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>{1}？</div><div class="f12 gray9 margin_b10 text-center">课程：{0}<div class="margin_t5"></div></div>',
	title:false,
	btnOkClass    : 'btn btn-xs btn-primary',
	btnOkLabel    : '确认',
	btnOkIcon     : '',
	btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	btnCancelLabel  : '取消',
	btnCancelIcon   : '',
	popContentWidth:250,
	onShow:function(event,element){
	  var $el=$(element);
	  if($el.is('[data-role="construction"]')){
		  var status=$el.data("id");
		  var text;
		  if(status=='3'){
			  text='是否将课程提交验收';
		  }else if(status=='2'){
			  text='是否将课程设置为建设中';
		  }
	  	this.content=this.content.format($el.closest('tr').find('.kcmc').text(),text);
	  }
	},
	onConfirm:function(event,element){
		  var id = $(element).closest('tr').find('input[name="courseId"]').val()
		  var status=$(element).data("id");
		  window.location.href = ctx + "/edumanage/course/construction?id=" + id+"&status="+status;
	},
	onCancel:function(event, element){

	}
});

var courseNature = '${param["search_EQ_courseNature"]}';
if (!courseNature) {
	courseNature = 1;
}
var str = "是否将课程转为体验课程？";
if (courseNature == 2) {
	str = "是否将课程转为正式课程？";
}
//转换课程性质
/* $(".data-operion").confirmation({
	selector: "[data-role='change']",
	html:true,
	placement:'top',
	content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>'+str+'</div><div class="f12 gray9 margin_b10 text-center">课程：{0}<div class="margin_t5"></div></div>',
	title:false,
	btnOkClass    : 'btn btn-xs btn-primary',
	btnOkLabel    : '确认',
	btnOkIcon     : '',
	btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	btnCancelLabel  : '取消',
	btnCancelIcon   : '',
	popContentWidth:250,
	onShow:function(event,element){
	  var $el=$(element);
	  if($el.is('[data-role="change"]')){
	  	this.content=this.content.format($el.closest('tr').find('.kcmc').text());
	  }
	},
	onConfirm:function(event,element){
		  var id = $(element).closest('tr').find('input[name="courseId"]').val()
		  window.location.href = ctx + "/edumanage/course/changeCourseNature?id=" + id;
	},
	onCancel:function(event, element){

	}
}); */

//教材配置
$("body").on('click', '[data-role="config"]', function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'config',
	  width:$(window).width(),
	  height:$(window).height(),
	  zIndex:11000,
	  content: 'url:${ctx}/edumanage/course/choiceTextbookList?courseId='+$(this).data('id')
	});
});

</script>
</body>
</html>
