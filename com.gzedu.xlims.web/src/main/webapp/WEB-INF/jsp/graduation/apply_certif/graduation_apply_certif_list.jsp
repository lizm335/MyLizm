<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<c:set var="ctx">${pageContext.request.contextPath}</c:set>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

	<script type="text/javascript">
		$(function() {
            $("[data-role='import']").click(function(event) {
                $.mydialog({
                    id:'import',
                    width:600,
                    height:415,
                    zIndex:11000,
                    content: 'url:toImport'
                });
            });

		});
		function choiceXJ(flag){
			//$('#listForm .btn-reset').trigger('click');
			$("#auditStateType").val('');
			$("#statusType").val('');
			$("#auditStateType").val('');
			$("#auditState").val(flag);
			$("#listForm").submit();
		}
	</script>
</head>
<body class="inner-page-body overlay-wrapper">
		
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">毕业管理</a></li>
			<li class="active">毕业申请</li>
		</ol>
	</section>

	<section class="content">
		<form id="listForm" class="form-horizontal">
			<input id="auditStateType" type="hidden" name="search_EQ_auditStateType" value="${param.search_EQ_auditStateType}">
			<input id="statusType" type="hidden" name="search_EQ_statusType" value="${param.search_EQ_statusType}">
			<input id="auditState" type="hidden" name="search_EQ_auditState" value="${param.search_EQ_auditState}">
			<div class="box box-border">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">毕业计划</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtGraduationPlan.id" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${graduationPlanMap}" var="plan">
											<option value="${plan.key}"  <c:if test="${plan.key==param['search_EQ_gjtGraduationPlan.id']}">selected='selected'</c:if>>${plan.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_gjtStudentInfo.xh" value="${param['search_EQ_gjtStudentInfo.xh']}">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">层次</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${pyccMap}" var="trainingLevel">
											<option value="${trainingLevel.key}"  <c:if test="${trainingLevel.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if>>${trainingLevel.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学期</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${gradeMap}" var="grade">
											<option value="${grade.key}"  <c:if test="${grade.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']}">selected='selected'</c:if>>${grade.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">专业</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected='selected'>请选择</option>
										<c:forEach items="${specialtyMap}" var="specialty">
											<option value="${specialty.key}"  <c:if test="${specialty.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${specialty.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</div>
					<%--<c:forEach var="item" items="${param}">
						${item.key} > ${item.value} <br>
					</c:forEach>--%>

					<div id="more-search" class="row collapse <c:if test="${not empty param['search_EQ_graduationCondition'] || not empty param['search_EQ_gjtGraduationRegister.expressSignState'] || not empty param['search_EQ_graduationState'] || not empty param.search_LIKE_scCo }">in</c:if>">
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">毕业满足条件</label>
								<div class="col-sm-9">
									<select name="search_EQ_graduationCondition" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<option value="0"  <c:if test="${'0' eq param['search_EQ_graduationCondition']}">selected='selected'</c:if>>否</option>
										<option value="1"  <c:if test="${'1' eq param['search_EQ_graduationCondition']}">selected='selected'</c:if>>是</option>
									</select>
								</div>
							</div>
						</div>
						<%--<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">毕业生登记表</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtGraduationRegister.expressSignState" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<option value="0"  <c:if test="${'0' eq param['search_EQ_gjtGraduationRegister.expressSignState']}">selected='selected'</c:if>>未寄送</option>
										<option value="1"  <c:if test="${'1' eq param['search_EQ_gjtGraduationRegister.expressSignState']}">selected='selected'</c:if>>寄送中</option>
										<option value="2"  <c:if test="${'2' eq param['search_EQ_gjtGraduationRegister.expressSignState']}">selected='selected'</c:if>>已签收</option>
									</select>
								</div>
							</div>
						</div>--%>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">毕业状态</label>
								<div class="col-sm-9">
									<select name="search_EQ_graduationState" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<option value="0"  <c:if test="${'0' eq param['search_EQ_graduationState']}">selected='selected'</c:if>>未毕业</option>
										<option value="1"  <c:if test="${'1' eq param['search_EQ_graduationState']}">selected='selected'</c:if>>延迟毕业</option>
										<option value="2"  <c:if test="${'2' eq param['search_EQ_graduationState']}">selected='selected'</c:if>>已毕业</option>
									</select>
								</div>
							</div>
						</div>
						<%--<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">身份证号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_gjtStudentInfo.sfzh" value="${param['search_EQ_gjtStudentInfo.sfzh'] }">
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学习中心</label>
								<div class="col-sm-9">
									<select name="search_EQ_gjtStudentInfo.gjtStudyCenter.id" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${studyCenterMap}" var="map">
											<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtStudyCenter.id']}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-sm-4 col-xs-6">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">电子注册号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_scCo" value="${param.search_LIKE_scCo }">
								</div>
							</div>
						</div>
					</div>--%>

				</div><!-- /.box-body -->
				<div class="box-footer text-right">
					<button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
					<button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
					<div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">高级搜索<i class="fa fa-fw fa-angle-up <c:if test="${not empty param.search_EQ_sfzh || not empty param.search_EQ_userType || not empty param.search_LIKE_scCo }">fa-angle-down</c:if>"></i> </div>
				</div><!-- /.box-footer-->
			</div>
				
			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
			<div class="box box-border margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">信息列表</h3>
					<div class="pull-right no-margin">
						<shiro:hasPermission name="/graduation/applyCertif/list$importXinhuashePhoto">
						<a class="btn btn-default btn-sm margin_l10" onclick="javascript:uploadImages.call(this,'headImgId','headImgUrl','png,jpg','100',addPost,'1000','true','imgPaths');" data-role="importPhoto"><i class="fa fa-fw fa-sign-in"></i> 批量导入新华社照片</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="/graduation/applyCertif/list$downloadGraduationRegister">
						<%-- <a  href="${ctx}/graduation/applyCertif/batchDownloadRegister" class="btn btn-default btn-sm" data-role="outZip">
							<i class="fa fa-fw fa-download"></i> 批量下载毕业生登记表
						</a> --%>
						<a  href="#" class="btn btn-default btn-sm" data-role="outZip" data-toggle="modal" data-target="#exportModal2">
							<i class="fa fa-fw fa-download"></i> 批量下载毕业生登记表
						</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="/graduation/applyCertif/list$exportGraduationApply">
						<a href="${ctx}/graduation/applyCertif/exportGraduationApply" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出毕业申请记录</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="/graduation/applyCertif/list$importGraduationAudit">
						<a class="btn btn-default btn-sm margin_l10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 导入审核记录</a>
						</shiro:hasPermission>
					</div>
				</div>

				<div class="box-body">
					<div class="filter-tabs clearfix">
						<ul class="list-unstyled">
							<li <c:if test="${empty param.search_EQ_auditState && empty param.search_EQ_auditState}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${not empty countAuditStateMap[''] ? countAuditStateMap[''] : 0})</li>
							<li <c:if test="${param.search_EQ_auditState == '0'}">class="actived"</c:if> value="0" onclick="choiceXJ('0')">未审核(${not empty countAuditStateMap['0'] ? countAuditStateMap['0'] : 0})</li>
							<li <c:if test="${param.search_EQ_auditState == '1'}">class="actived"</c:if> value="1" onclick="choiceXJ('1')">省校审核通过(${not empty countAuditStateMap['1'] ? countAuditStateMap['1'] : 0})</li>
							<li <c:if test="${param.search_EQ_auditState == '6'}">class="actived"</c:if> value="6" onclick="choiceXJ('6')">中央待审核${not empty countAuditStateMap['6'] ? countAuditStateMap['6'] : 0})</li>
							<li <c:if test="${param.search_EQ_auditState == '11'}">class="actived"</c:if> value="11" onclick="choiceXJ('11')">中央审核通过${not empty countAuditStateMap['11'] ? countAuditStateMap['11'] : 0})</li>
							<li <c:if test="${param.search_EQ_auditState == '12'}">class="actived"</c:if> value="12" onclick="choiceXJ('12')">中央审核不通过${not empty countAuditStateMap['12'] ? countAuditStateMap['12'] : 0})</li>
						</ul>
					</div>
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
							<tr>
								<th>毕业计划</th>
								<th>新华社照片</th>
								<th>个人信息</th>
								<th>报读信息</th>
								<th>是否满足<br>毕业申请条件</th>
								<th>毕业生登记表</th>
								<th>电子注册号</th>
								<%--<th>是否<br>申请学位</th>--%>
								<th>审核状态</th>
								<th>毕业状态</th>
								<th>操作</th>
							</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.content}">
										<c:forEach items="${pageInfo.content}" var="info">
											<tr>
												<td>
													${info.gjtGraduationPlan.graPlanTitle}
												</td>
												<td class="text-center">
													<img src="${not empty info.photoUrl ? info.photoUrl : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-thumbnail" width="80" height="100" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>
													<c:if test="${empty info.photoUrl}">
														<p class="gray9">未上传</p>
													</c:if>
												</td>
												<td>
													<ul class="list-unstyled text-left">
														<li>姓名：${info.gjtStudentInfo.xm}</li>
														<li>学号：${info.gjtStudentInfo.xh}</li>
														<li>手机：
														
														<shiro:hasPermission name="/personal/index$privacyJurisdiction">
															${info.gjtStudentInfo.sjh}
														</shiro:hasPermission>
														<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
															<c:if test="${not empty info.gjtStudentInfo.sjh }">
															${fn:substring(info.gjtStudentInfo.sjh,0, 3)}******${fn:substring(info.gjtStudentInfo.sjh,8, (info.gjtStudentInfo.sjh).length())}
															</c:if>
														</shiro:lacksPermission>
														
														</li>
														<li>身份证：
															<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																${info.gjtStudentInfo.sfzh}
															</shiro:hasPermission>
															<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
																<c:if test="${not empty info.gjtStudentInfo.sfzh }">
																${fn:substring(info.gjtStudentInfo.sfzh,0, 4)}******${fn:substring(info.gjtStudentInfo.sfzh,14, (info.gjtStudentInfo.sfzh).length())}
																</c:if>
															</shiro:lacksPermission>
														</li>
													</ul>
												</td>
												<td>
													<ul class="list-unstyled text-left">
														<li>层次：<dic:getLabel typeCode="TrainingLevel" code="${info.gjtStudentInfo.pycc }" /></li>
														<li>学期：${info.gjtStudentInfo.gjtGrade.gradeName}</li>
														<li>专业：${info.gjtStudentInfo.gjtSpecialty.zymc}</li>
														<li>学习中心：${info.gjtStudentInfo.gjtStudyCenter.scName}</li>
													</ul>
												</td>
												<td>
													${info.graduationCondition==1?'<span class="text-green">已满足</span>':'<span class="text-orange">未满足</span>'}
												</td>
												<td>
													<c:choose>
														<c:when test="${empty info.gjtGraduationRegister || info.gjtGraduationRegister.expressSignState==0}">未寄送</c:when>
														<c:when test="${info.gjtGraduationRegister.expressSignState==1}"><span class="text-orange">寄送中</span></c:when>
														<c:otherwise><span class="text-green">已签收</span></c:otherwise>
													</c:choose>
												</td>
												<td>
													${empty info.eleRegistrationNumber ? '--' : info.eleRegistrationNumber  }
												</td>
												<%--<td>
													${info.applyDegree==1?'<span class="text-green">是</span>':'<span class="text-orange">否</span>'}
												</td>--%>
												<td class="text-orange">
													<c:choose>
													<c:when test="${info.auditState==0}">未审核</c:when>
													<c:when test="${info.auditState==1}"><span class="text-green">省校审核通过</span></c:when>
													<c:when test="${info.auditState==2}"><span class="text-red">省校审核不通过</span></c:when>
													<c:when test="${info.auditState==6}"><span class="text-orange">中央待审核</span></c:when>
													<c:when test="${info.auditState==11}"><span class="text-green">中央审核通过</span></c:when>
													<c:when test="${info.auditState==12}"><span class="text-red">中央审核不通过</span></c:when>
													</c:choose>
												</td>
												<td class="text-orange">
													<c:choose>
													<c:when test="${info.graduationState==0}"><span class="text-red">未毕业</span></c:when>
													<c:when test="${info.graduationState==1}"><span class="text-orange">延迟毕业</span></c:when>
													<c:otherwise><span class="text-green">已毕业</span></c:otherwise>
													</c:choose>
												</td>
												<td class="text-center">
													<shiro:hasPermission name="/graduation/applyCertif/list$view">
														<a href="view/${info.applyId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-view-more"></i></a>
													</shiro:hasPermission>
													<shiro:hasPermission name="/graduation/applyCertif/list$downloadGraduationRegister">
														<a href="download?studentId=${info.gjtStudentInfo.studentId}" class="operion-item" data-toggle="tooltip" title="下载毕业生登记表"><i class="fa fa-download"></i></a>
													</shiro:hasPermission>
												</td>
											</tr>
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
		</form>
	</section>
 
 <div id="exportModal2" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2">
	    <div class="modal-dialog">
	        <form id="uploadForm2" name="uploadForm" action="${ctx}/graduation/applyCertif/batchDownloadRegister" method="post" target="temp_target" enctype="multipart/form-data">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">导出毕业登记表数据</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div>
	                		<p>
	                		请选择需要导出毕业登记表数据的毕业计划
		                    </p>    
							<div class="col-sm-9">
								<select id="gradeId" name="search_gradeId" class="selectpicker show-tick form-control"
									data-size="5" data-live-search="true">
									<option value="" >请选择</option>
									<c:forEach items="${graduationMap}" var="map">
										<option value="${map.key}">${map.value}</option>
									</c:forEach>
								</select>
							</div>
							
						</div>
	                </div>
	                <br>
	                <br>
	                <br>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">取消</button>
	                    <button type="button" class="btn btn-primary" id="point_export">导出</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	<!-- 加载的时候转圈圈 -->
<div class="overlay" style="display:none;  position: fixed;" id="overlay">
   <i class="fa fa-refresh fa-spin"></i>
 </div>
 	<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
    <jsp:include page="/eefileupload/upload.jsp" />
	<script type="text/javascript">
	
		var addPost=function(){
			var imgPaths=$("[name='imgPaths']");
			$("#overlay").show();
			$.post('${ctx}/graduation/applyCertif/addCardUrl',imgPaths.serializeArray(),function(data){
				$("#overlay").hide();
				alert(data.message);
			},'json');
		}
		
		
		//高级搜索
		$("#more-search").on('shown.bs.collapse', function(event) {
			event.preventDefault();
			$('[data-target="#more-search"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
		}).on('hidden.bs.collapse', function(event) {
			event.preventDefault();
			$('[data-target="#more-search"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
		});

		function exportAuditState() {
			var form = $("<form>");
			form.attr("style","display:none");
			form.attr("target","");
			form.attr("method","post");
			form.attr("action","exportAuditState");
			$("body").append(form);
			var fields = $('#listForm').serializeArray();
			$.each( fields, function(i, field){
				form.append('<input type="hidden" name="'+field.name+'" value="' + field.value + '" />');
			});
			form.submit();
		}
		
		$('#point_export').click(function(){
			var gradeId = $('#gradeId').val();
			if(!gradeId){
				alert('请先选择毕业计划');
				return;
			}
			$('#uploadForm2').submit();
		});
	</script>
</body>
</html>