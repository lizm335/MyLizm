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

$(function() {
	
	$('#examBatchCode').change(function(){
		var examBatchCode = $('#examBatchCode').val();
		if(examBatchCode!=''){
			$.get("${ctx}/exam/new/batch/queryExamPoint",{examBatchCode:examBatchCode},function(data,status){
				$('#examPointId').empty();
				$("#examPointId").append("<option value=''>请选择</option>");
				$.each(data, function (i) {
					$("#examPointId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
	        	});
			  },"json");
		}else{
			$('#examPointId').empty();
			$("#examPointId").append("<option value=''>请选择</option>");
			$("#examPointId").val("").trigger("change");
		}
	}); 
	
	// filter tabs
	$(".filter-tabs li").click(function(event) {
		debugger;
		if($(this).hasClass('actived')){
			$(this).removeClass('actived');
		}
		else{
			$(this).addClass('actived');
		}
	});
	
	//状态单选
	$(".list-unstyled li").click(function () {
		debugger;
		$(this).addClass("actived").siblings().removeClass("actived");
		$("select[name=search_EQ_status]").val($(this).attr("data-val"));
		$("#search").click();
	});
	
	//导出
	$("[data-role='export']").click(function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'export',
		  width:540,
		  height:400,
		  zIndex:11000,
		  content: 'url:'+$(this).attr('href')
		});
	});
	
	//批量导入
	$("[data-role='set']").click(function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:'+$(this).attr('href')
		});
	});


})

</script>
	
</head>

<body class="inner-page-body">
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li class="active">考试考点</li>
	</ol>
</section>
	<section class="content">
		<form id="listForm" class="form-horizontal">
			<!-- 搜索栏 start -->
			<div class="box">
				<div class="row pad-t15">

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">考试批次</label>
			              <div class="col-sm-8">
			                <select id="examBatchCode" name="search_EQ_examPonitNew.examBatchCode" class="form-control select2">
						        	<option value="">请选择</option>
									<c:forEach items="${batchMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param['search_EQ_examPonitNew.examBatchCode']}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
							</select>
			              </div>
			            </div>
			          </div>			          
		
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">考点</label>
			              <div class="col-sm-8">
			                <select id="examPointId" name="search_EQ_examPointId" class="form-control select2">
						        	<option value="">请选择</option>
						        	<c:forEach items="${pointMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param['search_EQ_examPointId']}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
							</select>
			              </div>
			            </div>
			          </div>
		
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">考场名称</label>
			              <div class="col-sm-8">
			                <input class="form-control" type="text"
									name="search_LIKE_name" id="search_LIKE_name"
									value="${param.search_LIKE_name}">
			              </div>
			            </div>
			          </div>
					 
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">考场状态</label>
			              <div class="col-sm-8">
			                <select class="selectpicker form-control" name="search_EQ_status">
			                	<option value=""  <c:if test="${empty param['search_EQ_status']}">selected="selected"</c:if> >请选择</option>
								<option value="1" <c:if test="${param['search_EQ_status'] == 1}">selected="selected"</c:if> >已启用</option>
								<option value="0" <c:if test="${param['search_EQ_status'] == 0}">selected="selected"</c:if> >已停用</option>
			                </select>
			              </div>
			            </div>
			          </div>
			          
			        </div>

				<div class="box-footer">
					<div class="btn-wrap">
						<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary" id="search">搜索</button>
					</div>
				</div>
			</div>
			<!-- 搜索栏 end -->
			
			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	


			<div class="box">
				<!-- 按钮栏 start -->
				<div class="box-header with-border">
					<div class="fr">
						<div class="btn-wrap fl">
							<!-- 
							<a href="exportRoomData" role="button" class="btn btn-default btn-sm margin_r10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出考场信息</a>
							
							<a href="importRoomData" role="button" class="btn btn-default btn-sm margin_r10" data-role="set"><i class="fa fa-fw fa-sign-in"></i> 批量导入考场</a>
							 -->
							<shiro:hasPermission name="/exam/new/room/list$create">
								<a href="create/0" class="btn btn-default btn-sm"> <i class="fa fa-fw fa-plus"></i> 新增</a>
							</shiro:hasPermission>
						</div>

						<!-- <div class="btn-wrap fl">
								<a href="javascript:void(0);" class="btn btn-block btn-danger btn-del del-checked">
										<i class="fa fa-fw fa-trash-o"></i> 删除
								</a>
							</div> -->
					</div>
				</div>
				<!-- 按钮栏 end -->

				<!-- 列表内容 start -->
				<div class="box-body">
				<!-- 
					<div class="filter-tabs clearfix">
						<ul class="list-unstyled">
							<li <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if> >全部(${sumRoomData })</li>
							<li <c:if test="${param['search_EQ_status'] == 1}">class="actived"</c:if> data-val="1">已启用(${openOperation })</li>
							<li <c:if test="${param['search_EQ_status'] == 0}">class="actived"</c:if> data-val="0">已停用(${closeOperation })</li>
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
								<table
									id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
										<tr>
											<!-- <th><input type="checkbox" class="select-all"></th> -->
											<th>考试批次</th>
											<th>考点</th>
											<th>考场名称</th>
											<th>顺序号</th>
											<th>所属考点</th>
											<th>座位数</th>
											<th>考场状态</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${pageInfo.content}" var="entity">
											<c:if test="${not empty entity}">
												<tr>
													<!-- 
													<td><input class="checkbox" type="checkbox"
														name="ids" data-id="${entity.examRoomId}"
														data-name="check-id" value="${entity.examRoomId}"></td>
													 -->
													<td>${entity.examPonitNew.examBatchCode}</td>
													<td>${entity.examPonitNew.name}</td>
													<td>${entity.name}</td>
													<td>${entity.orderNo}</td>
													<td>${entity.examPonitNew.name}</td> 
													<td>${entity.seats}</td>
													<td>
														<c:if test="${!entity.status}"><span class="gray9">已停用</span></c:if>
														<c:if test="${entity.status}"><span class="text-green">已启用</span></c:if>
													</td> 
													<td>
														<div class="data-operion">
															<c:if test="${user.gjtOrg.id == entity.xxId }">
																<c:if test="${entity.status}">
																<shiro:hasPermission name="/exam/new/room/list$update">
																	<a href="update/${entity.examRoomId}"
																		class="operion-item operion-edit" title="编辑"> <i
																		class="fa fa-fw fa-edit"></i></a>
																</shiro:hasPermission>
																</c:if>
																<shiro:hasPermission name="/exam/new/room/list$view">
																	<a href="view/${entity.examRoomId}"
																		class="operion-item operion-view" title="查看"> <i
																		class="fa fa-fw fa-eye"></i></a>
																</shiro:hasPermission>
																<c:if test="${!entity.status}">
																<shiro:hasPermission name="/exam/new/room/list$delete">
																	<a href="javascript:void(0);"
																		class="operion-item operion-del del-one"
																		val="${entity.examRoomId}" title="删除"
																		data-tempTitle="删除"> <i
																		class="fa fa-fw fa-trash-o"></i></a>
																</shiro:hasPermission>
																</c:if>  
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
	</section>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
