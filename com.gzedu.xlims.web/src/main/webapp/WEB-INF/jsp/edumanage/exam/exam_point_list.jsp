<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>考点管理</title>
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


	<form class="form-horizontal" id="listForm" action="list.html">
		<section class="content" data-id="0">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">批次</label>
							<div class="col-sm-9">
								<select id="examBatchCode" name="search_EQ_examBatchCode" class="form-control select2">
						        	<option value="">请选择</option>
									<c:forEach items="${batchMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==examBatchCode}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div> 
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">考点</label>
							<div class="col-sm-9">
								<select id="examPointId" name="search_EQ_examPointId" class="form-control select2">
						        	<option value="">请选择</option>
						        	<c:forEach items="${pointMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param['search_EQ_examPointId']}">selected='selected'</c:if> >${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div> 
						
						<div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">所在区域</label>
				              <div class="col-sm-9">
				                	<div class="select-box"> <i class="select-ico"> <span class="caret"></span> </i>
									  	<div class="select-in"> 
											<ins data-seltype="Pro"> <span>省份</span>
											<input type="hidden">
											</ins><ins data-seltype="City"> <span>城市</span>
											<input type="hidden">
											</ins><ins data-seltype="District"> <span>区/县</span>
											<input type="hidden">
											</ins>  
									   </div>
									   <ul class="itemSelBox dropdown-menu" style="display: none;"></ul>
									</div>
				              </div>
				            </div>
				          </div>
				          
					</div>
				</div>
				<div class="box-footer">
					<div class="btn-wrap">
						<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
			</div>
	

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
	<div class="box">
		<div class="box-header with-border">
			<div class="fr">
				<div class="btn-wrap fl">
					<shiro:hasPermission name="/exam/new/point/list$create">
						<a href="create" class="btn btn-default btn-sm">
								<i class="fa fa-fw fa-plus"></i> 新增</a>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
		<div class="box-body">
			<div id="dtable_wrapper"
				class="dataTables_wrapper form-inline dt-bootstrap no-footer">
				<div class="row">
					<div class="col-sm-6"></div>
					<div class="col-sm-6"></div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
								<tr>
									<th><input type="checkbox" class="select-all" 	id="selectAll"></th>
									<th>考试批次</th>
									<th>考点名称</th>
									<th>所在区域</th>
									<th>详细地址</th>
									<th>笔考已预约人数</th>
									<th>机考已预约人数</th>
									<th>已设考场</th>
									<th>考场座位数</th>
									<th>笔考座位容量</th>
									<th>机考座位容量</th>
									<th>使用状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageInfo.content }" var="entity">
									<tr>
										<td><input type="checkbox" value="${entity.examPointId }"	name="ids" class="checkbox"></td>
										<td>
										${entity.examBatchNew.name}<br>
										<span class="gray9">${entity.examBatchNew.examBatchCode}</span>
										</td>
										<td>${entity.name}</td>
										<td>${entity.provinceName}-${entity.cityName}-${entity.districtName}</td>
										<td>${entity.address}</td> 
										<td>${entity.studentAppointCount2}</td>
										<td>${entity.studentAppointCount3}</td>
										<td>${entity.roomCount}</td>
										<td>${entity.seatCount}</td>
										<c:if test="${entity.studentAppointCount2-entity.seatCount>0}">
											<td class="text-red">座位不足</td> 
										</c:if>
										<c:if test="${entity.studentAppointCount2-entity.seatCount<=0}">
											<td class="text-green">座位充足</td>
										</c:if>
										<c:if test="${entity.studentAppointCount3-entity.seatCount>0}">
											<td class="text-red">座位不足</td> 
										</c:if>
										<c:if test="${entity.studentAppointCount3-entity.seatCount<=0}">
											<td class="text-green">座位充足</td>
										</c:if>
										<td> 
											<c:if test="${entity.isEnabled==true}">在用 </c:if>
											<c:if test="${entity.isEnabled==false}">停用 </c:if>
										</td>  
										<td>
											<div class="data-operion"> 
												<shiro:hasPermission name="/exam/new/point/list$update">
													 <a href="update/${entity.examPointId}"	 class="operion-item operion-edit" title="编辑">
														<i class="fa fa-fw fa-edit"></i></a> 
												</shiro:hasPermission>
												<%--<a href="javascript:void(0);" 	class="operion-item operion-del del-one" val="${entity.pointId}" title="删除" data-tempTitle="删除">
													<i class="fa fa-fw fa-trash-o"></i></a> --%>
											</div>
										</td>
									</tr>
								</c:forEach> 
							</tbody>
						</table>
					</div>
				</div>
				 <tags:pagination page="${pageInfo}" paginationSize="5" /> 
			</div>
		</div>
	</div>
	</section>
	</form>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>