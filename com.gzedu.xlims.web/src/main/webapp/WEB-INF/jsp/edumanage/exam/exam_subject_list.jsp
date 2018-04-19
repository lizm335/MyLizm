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
var examType = "${exam_type}";
$(function() {
})
</script>

</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">考试管理</a></li>
		<li class="active">考试科目</li>
	</ol>
</section>
		
		<section class="content">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
				  <li <c:if test="${exam_type==1}">class="active"</c:if>><a href="list?search_EQ_type=1" target="_self" >网考</a></li>
				  <li <c:if test="${exam_type==2}">class="active"</c:if>><a href="list?search_EQ_type=2" target="_self" >笔试</a></li>
				  <li <c:if test="${exam_type==3}">class="active"</c:if>><a href="list?search_EQ_type=3" target="_self" >机考</a></li>
				  <li <c:if test="${exam_type==4}">class="active"</c:if>><a href="list?search_EQ_type=4" target="_self" >形考</a></li>
				  <li <c:if test="${exam_type==5}">class="active"</c:if>><a href="list?search_EQ_type=5" target="_self" >省网考</a></li>
				  <li <c:if test="${exam_type==6}">class="active"</c:if>><a href="list?search_EQ_type=6" target="_self" >全国统考</a></li>
				  <li <c:if test="${exam_type==7}">class="active"</c:if>><a href="list?search_EQ_type=7" target="_self" >省统考</a></li>
				  <li <c:if test="${exam_type==8}">class="active"</c:if>><a href="list?search_EQ_type=8" target="_self" >论文</a></li>
				  <li <c:if test="${exam_type==9}">class="active"</c:if>><a href="list?search_EQ_type=9" target="_self" >报告</a></li>
				  <li <c:if test="${exam_type==10}">class="active"</c:if>><a href="list?search_EQ_type=10" target="_self" >大作业</a></li>
				  <li <c:if test="${exam_type==11}">class="active"</c:if>><a href="list?search_EQ_type=11" target="_self" >论文报告</a></li>
				</ul>
		
			<form id="listForm" class="form-horizontal">
				<input type="hidden" name="search_EQ_type" id="search_EQ_type" value="${exam_type}"  />
				<!-- 搜索栏 start -->
				<div class="box box-border">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-md-4">
								<label class="control-label col-sm-3">科目编号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_subjectCode" id="search_LIKE_subjectCode" value="${param.search_LIKE_subjectCode}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">科目名称</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_name" id="search_LIKE_name" value="${param.search_LIKE_name}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">试卷状态</label>
								<div class="col-sm-9">
									<select name="search_EQ_status" class="selectpicker show-tick form-control" 
										data-size="5" data-live-search="true">
										<option value="" <c:if test="${empty param.search_EQ_status}">selected="selected"</c:if> >请选择</option>
										<option value="0"<c:if test="${param.search_EQ_status eq '0'}">selected='selected'</c:if> >未同步</option>
										<option value="1"<c:if test="${param.search_EQ_status eq '1'}">selected='selected'</c:if> >已同步</option>
										<option value="2"<c:if test="${param.search_EQ_status eq '2'}">selected='selected'</c:if> >制作中</option>
										<option value="3"<c:if test="${param.search_EQ_status eq '3'}">selected='selected'</c:if> >已发布</option>
									</select>
								</div>
							</div>
							
						</div>
						<%-- 
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">试卷号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_LIKE_examNo" id="search_LIKE_examNo" value="${param.search_LIKE_examNo}">
								</div>
							</div>
							
							<div class="col-md-4">
								<label class="control-label col-sm-3">课程号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_kch" id="search_EQ_kch" value="${param.search_EQ_kch}">
								</div>
							</div>
							
							
						</div>
						--%>
					</div>
					<div class="box-footer">
						<div class="btn-wrap">
							<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="btn-wrap">
							<button type="submit" class="btn min-width-90px btn-primary" id="search_submit_button">搜索</button>
						</div>
					</div>
					
				</div>
				<!-- 搜索栏 end -->
				
				<div class="box box-border margin-bottom-none">
					<!-- 按钮栏 start -->
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">科目列表</h3>
						<div class="pull-right">
							<!--  
							<a href="#" class="btn btn-default btn-sm margin_r10"  data-toggle="modal" data-target="#importModal"><i class="fa fa-fw fa-sign-in"></i> 导入创建考试科目</a>
							-->
							<button type="button" class="btn btn-default btn-sm margin_r10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 导入创建考试科目</button>
							<a href="create/0?examType=${exam_type}" role="button" class="btn btn-default btn-sm btn-add""><i class="fa fa-fw fa-plus"></i> 
								<c:if test="${exam_type==1}">新增考试科目</c:if>
								<c:if test="${exam_type==2}">新增考试科目</c:if>
								<c:if test="${exam_type==3}">新增考试科目</c:if>
								<c:if test="${exam_type==4}">新增考试科目</c:if>
								<c:if test="${exam_type==5}">新增考试科目</c:if>
								<c:if test="${exam_type==6}">新增考试科目</c:if>
								<c:if test="${exam_type==7}">新增考试科目</c:if>
								<c:if test="${exam_type==8}">新增考试科目</c:if>
								<c:if test="${exam_type==9}">新增考试科目</c:if>
								<c:if test="${exam_type==10}">新增考试科目</c:if>
								<c:if test="${exam_type==11}">新增考试科目</c:if>
							</a>
						</div>
					</div>
					<!-- 按钮栏 end -->	
	
					<!-- 列表内容 start -->
					<div class="box-body">
						<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
							<div class="row">
								<div class="col-sm-6"></div>
								<div class="col-sm-6"></div>
							</div>

							<div class="box-body"> 
								<div class="table-responsive">
									<table id="list_table"  class="table table-bordered table-striped vertical-mid text-center table-font">
 										<thead>
											<tr>
												<!-- <th><input type="checkbox" class="select-all"></th> -->
												<th>考试科目</th>
												<th>对应课程</th>
												<th>课程号</th>
												<th>试卷状态</th>
												<th>试卷号</th>
												<th>已安排考试</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pageInfo.content}" var="entity">
												<c:if test="${not empty entity}">
													<tr>
														<%-- <td><input class="checkbox" type="checkbox" name="ids"
															data-id="${entity.subjectId}" data-name="check-id"
															value="${entity.subjectId}"></td> --%>
														<td>
															${entity.name}<br>
															<span class="gray9">${entity.subjectCode}</span>
														</td>
														<td>${entity.gjtCourse.kcmc}</td>
														<td>${courseCodeMap[entity.subjectId]}</td>
														<td>
															<c:if test="${entity.status==0}"><span class="text-red">未同步</span></c:if>
															<c:if test="${entity.status==1}"><span class="text-green">已同步</span></c:if>
															<c:if test="${entity.status==2}"><span class="text-red">制作中</span></c:if>
															<c:if test="${entity.status==3}"><span class="text-green">已发布</span></c:if>
														</td>
														<td>
															${entity.examNo}
														</td>
														<td>
															<c:if test="${not empty countMap[entity.subjectCode]}">${countMap[entity.subjectCode]}</c:if>
															<c:if test="${empty countMap[entity.subjectCode]}">0</c:if>
														</td>
														<td style="">
															<div class="data-operion">
																	<c:if test="${entity.status==0}">
																		<a href="update/${entity.subjectId}?examType=${exam_type}"
																		class="operion-item operion-edit" title="编辑">
																		<i class="fa fa-fw fa-edit"></i></a> 
																	</c:if>
																	<a href="view/${entity.subjectId}?examType=${exam_type}" 
																		class="operion-item operion-view" title="查看">
																		<i class="fa fa-fw fa-eye"></i></a>
																	<c:if test="${entity.status==0}">	
																		<a href="#" class="operion-item" data-toggle="tooltip" title="删除" data-name="${entity.name}" data-val="${entity.subjectId}" data-role="remove-item"><i class="fa fa-fw fa-trash-o text-red"></i></a>
																	</c:if>
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
	</section>
	
	<!-- upload form start -->
	<div id="importModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="uploadForm" name="uploadForm" action="import" method="post" target="temp_target" enctype="multipart/form-data">
	        	<input type="hidden" name="examType" id="examType" value="${exam_type}"  />
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">考试计划设置</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div> 
	                		<p>
	                		导入创建考试科目前，建议先下载《未创建考试科目的课程目录表》，根据表设置好相关参数再导入创建考试科目
		                    </p>    
							
						</div>
						<br><br>
						<div>
							<p>
								<a href="javascript:void(0);" target="_blank" class="btn btn-xs btn-success" id="export_subjects">
		                            <i class="fa fa-fw fa-download"></i> 下载未创建考试科目的课程目录表
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
	<!-- upload form end -->
</body>

<script src="${ctx}/static/js/exam/exam_subject_list.js"></script>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
$("body").confirmation({
	  selector: "[data-role='remove-item']",
	  html:true,
	  placement:'top',
	  content:'<div class="margin_b10"><i class="fa fa-fw fa-exclamation-circle text-red" style="font-size:24px;"></i>确定要删除该考试科目？</div>',
	  title:false,
	  btnOkClass    : 'btn btn-xs btn-primary',
	  btnOkLabel    : '确认',
	  btnOkIcon     : '',
	  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	  btnCancelLabel  : '取消',
	  btnCancelIcon   : '',
	  popContentWidth:220,
	  onShow:function(event,element){
	    
	  },
	  onConfirm:function(event,element){
		var subjectId = $(element).attr("data-val");
		$.post("delete?ids="+subjectId, function(data){
		  	$("#listForm").submit();
		});
	  },
	  onCancel:function(event, element){
	  }
	});

	$("[data-role='import']").click(function(event) {
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:getSubjectImport?EXAM_TYPE=${exam_type}'
		});
	});
</script>
</html>
