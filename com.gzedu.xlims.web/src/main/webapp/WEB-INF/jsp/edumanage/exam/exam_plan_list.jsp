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
var ctx = "${ctx}";
$(function() {
})
</script>

</head>
<body class="inner-page-body">
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li class="active">考试安排</li>
	</ol>
</section>

		<section class="content">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
				<!-- 
				  <li <c:if test="${EXAM_TYPE==1}">class="active"</c:if>><a href="list?EXAM_TYPE=1" target="_self" >网考</a></li>
				  <li <c:if test="${EXAM_TYPE==2}">class="active"</c:if>><a href="list?EXAM_TYPE=2" target="_self" >笔考</a></li>
				  <li <c:if test="${EXAM_TYPE==3}">class="active"</c:if>><a href="list?EXAM_TYPE=3" target="_self" >机考</a></li>
				  <li <c:if test="${EXAM_TYPE==4}">class="active"</c:if>><a href="list?EXAM_TYPE=4" target="_self" >形考</a></li>
				  <li <c:if test="${EXAM_TYPE==5}">class="active"</c:if>><a href="list?EXAM_TYPE=5" target="_self" >网考（省）</a></li>
				  -->
				  <c:forEach var="et" items="${examTypeMap}">
						<li <c:if test="${exam_type==et.key}">class="active"</c:if>>
							<a href="list?search_EQ_type=${et.key}" target="_self" >${et.value}</a>
						</li>
					</c:forEach>
				</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="tab_top_1">
					<form id="listForm" class="form-horizontal">
						<!-- 搜索栏 start -->
						<input type="hidden" name="EXAM_TYPE" id="search_EQ_type" value="${EXAM_TYPE}"  />
						<div class="box box-border">
							<div class="box-body">
								<div class="row reset-form-horizontal clearbox">
									
									<div class="col-md-4">
										<label class="control-label col-sm-3">学年度</label>
										<div class="col-sm-9">
											<select name="search_EQ_studyYearId" id="search_EQ_studyYearId" class="selectpicker show-tick form-control" 
												data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${yearMap}" var="map">
													<option value="${map.key}"<c:if test="${map.key==param['search_EQ_studyYearId']}">selected='selected'</c:if> >${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									 
									<!-- 
									<div class="col-md-4">
										<label class="control-label col-sm-3">考试计划</label>
										<div class="col-sm-9">
											<select name="STUDY_YEAR_ID" id="search_EQ_studyYearId" class="selectpicker show-tick form-control" 
												data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${planMap}" var="map">
													<option value="${map.key}"<c:if test="${map.key==param.EXAM_BATCH_ID}">selected='selected'</c:if> >${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									-->
									
									<div class="col-md-4">
										<label class="control-label col-sm-3">考试期</label>
										<div class="col-sm-9">
											<select name="search_EQ_examBatchCode" id="search_EQ_examBatchCode" class="selectpicker show-tick form-control" 
												data-size="5" data-live-search="true">
												<option value="" >请选择</option>
												<c:forEach items="${batchMap}" var="map">
													<option value="${map.key}"<c:if test="${map.key==examBatchCode}">selected='selected'</c:if> >${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									
									<div class="col-md-4">
										<label class="control-label col-sm-3">考试课程</label>
										<div class="col-sm-9">
											<select name="search_EQ_courseId" id="search_EQ_courseId" class="selectpicker show-tick form-control"
												data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${courseList}" var="c">
													<option value="${c.courseId}"<c:if test="${c.courseId==param['search_EQ_courseId']}">selected='selected'</c:if> >${c.kcmc}（课程号：${c.kch}）</option>
												</c:forEach>
											</select>
										</div>
									</div>
									
									<!--  
									<div class="col-md-4">
										<label class="control-label col-sm-3">考试科目</label>
										<div class="col-sm-9">
											<input class="form-control" type="text" name="EXAM_PLAN_NAME" id="search_LIKE_examSubjectNew.examNo" value="${param.EXAM_PLAN_NAME}">
										</div>
									</div>
									
									<div class="col-md-4">
										<label class="control-label col-sm-3">试卷号</label>
										<div class="col-sm-9">
											<input class="form-control" type="text" name="EXAM_NO" id="search_LIKE_examSubjectNew.examNo" value="${param.EXAM_NO}">
										</div>
									</div>
									
									<div class="col-md-4">
										<label class="control-label col-sm-3">考试批次</label>
										<div class="col-sm-9">
											<select name="EXAM_BATCH_CODE" id="search_EQ_examBatchCode" class="selectpicker show-tick form-control" 
												data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${batchMap}" var="map">
													<option value="${map.key}"<c:if test="${map.key==param.EXAM_BATCH_CODE}">selected='selected'</c:if> >${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									-->
									<!--  
									<div class="col-md-4">
										<label class="control-label col-sm-3">考试科目</label>
										<div class="col-sm-9">
											<select name="SUBJECT_CODE" id="search_EQ_subjectCode" class="selectpicker show-tick form-control" 
												data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${subjectList}" var="subject">
													<option value="${subject.subjectCode}"<c:if test="${subject.subjectCode==param.SUBJECT_CODE}">selected='selected'</c:if> >${subject.name}（试卷号：${subject.examNo}）</option>
												</c:forEach>
											</select>
										</div>
									</div>
									-->
								</div>	
								
								<div class="row reset-form-horizontal clearbox">
									<div class="col-md-4">
										<label class="control-label col-sm-3">状态</label>
										<div class="col-sm-9">
											<!-- 
											<select name="PSTATUS" id="pstatus" class="selectpicker show-tick form-control" 
												data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<option value="1"<c:if test="${param.PSTATUS==1}">selected='selected'</c:if> >未设置</option>
												<option value="2"<c:if test="${param.PSTATUS==2}">selected='selected'</c:if> >未开始</option>
												<option value="3"<c:if test="${param.PSTATUS==3}">selected='selected'</c:if> >预约中</option>
												<option value="4"<c:if test="${param.PSTATUS==4}">selected='selected'</c:if> >待考试</option>
												<option value="5"<c:if test="${param.PSTATUS==5}">selected='selected'</c:if> >考试中</option>
												<option value="6"<c:if test="${param.PSTATUS==6}">selected='selected'</c:if> >已结束</option>
											</select>
											-->
											<select name="search_EQ_status" id="pstatus" class="selectpicker show-tick form-control"
												data-size="5" data-live-search="true">
												<option value="0" selected="selected">请选择</option>
												<option value="1"<c:if test="${param['search_EQ_status']==1}">selected='selected'</c:if> >未设置</option>
												<option value="2"<c:if test="${param['search_EQ_status']==2}">selected='selected'</c:if> >未开始</option>
												<option value="3"<c:if test="${param['search_EQ_status']==3}">selected='selected'</c:if> >预约中</option>
												<option value="4"<c:if test="${param['search_EQ_status']==4}">selected='selected'</c:if> >待考试</option>
												<option value="5"<c:if test="${param['search_EQ_status']==5}">selected='selected'</c:if> >考试中</option>
												<option value="6"<c:if test="${param['search_EQ_status']==6}">selected='selected'</c:if> >已结束</option>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="box-footer">
								<div class="pull-right">
									<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
								</div>
								<div class="pull-right margin_r15">
									<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
								</div>
							</div>
						</div>
						<!-- 搜索栏 end -->
						
						<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
						<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
						
						<div class="box box-border margin-bottom-none">
							<!-- 按钮栏 start -->
							<!--  
							<div class="box-header with-border">
								<h3 class="box-title pad-t5"><c:if test="${EXAM_TYPE==1}">网考</c:if><c:if test="${EXAM_TYPE==2}">笔考</c:if><c:if test="${EXAM_TYPE==3}">机考</c:if><c:if test="${EXAM_TYPE==4}">形考</c:if><c:if test="${EXAM_TYPE==5}">网考（省）</c:if>计划列表</h3>
								<div class="pull-right">
									<a href="create/0?examType=${EXAM_TYPE}" class="btn btn-default btn-sm">
										<i class="fa fa-fw fa-plus"></i> 新增开考科目
									</a>&nbsp;&nbsp;
									<a href="javascript:void(0);" class="btn btn-default btn-sm margin_r10" id="plan_export">
											<i class="fa fa-fw fa-download"></i> 
											导出<c:if test="${EXAM_TYPE==1}">网考</c:if><c:if test="${EXAM_TYPE==2}">笔考</c:if><c:if test="${EXAM_TYPE==3}">机考</c:if><c:if test="${EXAM_TYPE==4}">形考</c:if><c:if test="${EXAM_TYPE==5}">网考（省）</c:if>考试计划
									</a>
									<a href="#" class="btn btn-default btn-sm margin_r10" data-toggle="modal"  data-role="export">
												<i class="fa fa-fw fa-upload"></i> 批量设置考试安排</a>
								</div>
							</div>
							-->
							<div class="box-header with-border">
								<div class="fr">
						              <div class="btn-wrap fl">
						                <a href="#" class="btn btn-block btn-default btn-add" data-toggle="modal" data-target="#importModal">
						                    <i class="fa fa-fw fa-upload"></i> 批量导入考试安排</a>
						              </div>
									  <div class="btn-wrap fl">
										<a href="#" class="btn btn-block btn-default btn-add" data-toggle="modal" data-target="#setModal">
											<i class="fa fa-fw fa-upload"></i> 批量设置考试安排</a>
									 </div>
									<div class="btn-wrap fl">
										<a href="javascript:void(0);" class="btn btn-block btn-default btn-add" id="plan_export">
												<i class="fa fa-fw fa-download"></i> 
												导出考试科目
										</a>
									</div>
									<div class="btn-wrap fl">
										<a href="create/0?examType=${exam_type}" class="btn btn-block btn-default btn-add">
												<i class="fa fa-fw fa-plus"></i> 新增开考课程</a>
									</div>
								</div>
							</div>
							<!-- 按钮栏 end -->	
			
							<!-- 列表内容 start -->
							<div class="box-body">
								<!-- 
								<div class="filter-tabs clearfix">
									<ul class="list-unstyled">
										<li data-role='' <c:if test="${empty param.PSTATUS}">class="actived"</c:if>>全部(${PSTATUS_TYPE1+PSTATUS_TYPE2+PSTATUS_TYPE3+PSTATUS_TYPE4+PSTATUS_TYPE5+PSTATUS_TYPE6 })</li>
										<li data-role='1' <c:if test="${param.PSTATUS eq '1'}">class="actived"</c:if>>未设置(${PSTATUS_TYPE1})</li>
										<li data-role='2' <c:if test="${param.PSTATUS eq '2'}">class="actived"</c:if>>未开始(${PSTATUS_TYPE2})</li>
										<li data-role='3' <c:if test="${param.PSTATUS eq '3'}">class="actived"</c:if>>预约中(${PSTATUS_TYPE3})</li>
										<li data-role='4' <c:if test="${param.PSTATUS eq '4'}">class="actived"</c:if>>待考试(${PSTATUS_TYPE4})</li>
										<li data-role='5' <c:if test="${param.PSTATUS eq '5'}">class="actived"</c:if>>考试中(${PSTATUS_TYPE5})</li>
										<li data-role='6' <c:if test="${param.PSTATUS eq '6'}">class="actived"</c:if>>已结束(${PSTATUS_TYPE6})</li>
									</ul>
								</div>
								 -->
								<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
									<div class="row">
										<div class="col-sm-6"></div>
										<div class="col-sm-6"></div>
									</div>
		
									<div class="row">
										<div class="col-sm-12">
											<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
												<thead>
													<tr>
														<th><input type="checkbox" class="select-all"></th>
														<th>学年度</th>
														<th>考试期</th>
														<th>考试课程</th>
														<th>课程号</th>
														<th>考试方式</th>
														<th>试卷号</th>
														<th>形考比例</th>
														<th>考试预约时间</th>
														<th>考试时间</th>
														<th>状态</th>
														<th>操作</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td><input class="checkbox" type="checkbox" name="ids"
																	data-id="${entity.EXAM_PLAN_ID}" data-name="check-id"
																	value="${entity.EXAM_PLAN_ID}"></td>
																<td>${entity.STUDY_YEAR_NAME}</td>
																<td>${entity.EXAM_BATCH_NAME}</td>
																<td>${entity.KCMC}</td>
																<td>${entity.KCH}</td>
																<td>${entity.EXAM_TYPE}</td>
																<td>
																	${entity.EXAM_NO}
																</td>
																<td>${entity.XK_PERCENT}%</td>
																<td>
																	<fmt:formatDate value="${entity.BOOK_ST}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/><br>
																	<fmt:formatDate value="${entity.BOOK_END}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
																</td>
																<td>
																	<fmt:formatDate value="${entity.EXAM_ST}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/><br>
																	<fmt:formatDate value="${entity.EXAM_END}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
																</td>
																<td name="status_td" bs="${entity.BOOK_ST}" be="${entity.BOOK_END}"
																es="${entity.EXAM_ST}" ee="${entity.EXAM_END}" pid="${entity.EXAM_PLAN_ID}">
																	
																</td>
																<td>
																	<div class="data-operion">
																		<a href="javascript:void(0);" class="operion-item operion-edit" title="编辑" id="edit_${entity.EXAM_PLAN_ID}" editid="">
																			<i class="fa fa-fw fa-edit"></i>
																		</a> 
																	</div>
																</td> 
															</tr>
														</c:if>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</div>
									<tags:pagination page="${pageInfo}" paginationSize="5" />
								</div>
							</div>
							<!-- 列表内容 end -->
						</div>
					</form>	
				</div>
			</div>
		</div>	
	</section>
	
	<div id="importModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="uploadForm" name="uploadForm" action="import" method="post" target="temp_target" enctype="multipart/form-data">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">考试计划设置</h4>
	                </div>
	                <div class="modal-body">
	                	<div>
	                		<p>
	                		请选择需要设置考试时间的考试批次，并点击下载按钮下载未设置考试时间的《笔试科目考试计划设置表》
		                    </p>    
							<div class="col-sm-9">
								<select id="export_batchCode" class="selectpicker show-tick form-control" 
									data-size="10" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${batchMap}" var="map">
										<option value="${map.key}"<c:if test="${map.key==param['search_EQ_examBatchCode']}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
							</div>
							
						</div>
						<br><br>
						<div>
							<p>
								<a href="javascript:void(0);" target="_blank" class="btn btn-xs btn-success" id="export_set_plan">
		                            <i class="fa fa-fw fa-download"></i> 下载未设置考试时间的考试科目表
		                        </a>
		                   </p>
						</div>						
	                    
	                    <input name=file id="file" type="file" />
	                    <div class="progress">
	                        <div style="width: 0%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="0" role="progressbar" class="progress-bar progress-bar-success progress-bar-striped active">
	                            <span class="sr-only">0% Complete</span>
	                        </div>
	                    </div>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">关闭</button>
	                    <button type="button" class="btn btn-primary" id="setting_upload">上传</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	<form id="export_plan_form" action="export" method="POST" style="display:none;">
		<input type="hidden" name="EXAM_TYPE" value="${EXAM_TYPE }" />
		<input type="hidden" name="STUDY_YEAR_ID" value="${param.STUDY_YEAR_ID }" />
		<input type="hidden" name="EXAM_BATCH_CODE" value="${param.EXAM_BATCH_CODE }" />
		<input type="hidden" name="SUBJECT_CODE" value="${param.SUBJECT_CODE }" />
		<input type="hidden" name="EXAM_NO" value="${param.EXAM_NO }" />
		<input type="hidden" name="PSTATUS" value="${param.PSTATUS }" />
	</form>
	<script type="text/javascript">
		// filter tabs
		$(".filter-tabs li").click(function(event) {
			$(".filter-tabs li").removeClass('actived');
			$(this).addClass('actived');
			var vals = $(this).attr("data-role");
			$("#pstatus").val(vals);
			$("#listForm").submit();
		});
		
		//导出
		$("[data-role='export']").click(function(event) {
			event.preventDefault();
			$.mydialog({
			  id:'export',
			  width:540,
			  height:410,
			  zIndex:11000,
			  content: 'url:getExamPlanImport?EXAM_TYPE=${EXAM_TYPE}'
			});
		});
	</script>
</body>

<script src="${ctx}/static/js/exam/exam_plan_list.js?v=3"></script>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
