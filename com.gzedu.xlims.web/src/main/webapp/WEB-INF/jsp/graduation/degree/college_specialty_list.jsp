<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-毕业管理</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">毕业管理</a></li>
			<li class="active">
				学位院校管理
			</li>
		</ol>
	</section>

	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="specialtyList.html">
		<input type="hidden" name="search_collegeId" value="${param['search_collegeId']}"/>
		
			<div class="nav-tabs-custom reset-nav-tabs-custom" id="notice-tabs" style="margin-bottom: 0px;">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li><a href="${ctx}/graduation/degreeCollege/list">学位院校列表</a></li>
					<li class="active"><a href="${ctx}/graduation/degreeCollege/specialtyList" data-toggle="tab">学位专业列表</a></li>
				</ul>
				<div class="tab-content">
					<div class="box box-border">
						<div class="box-body">
							<div class="row pad-t15">
								<div class="col-md-4 col-xs-6">
									<div class="form-group">
										<label class="control-label col-sm-3">院校名称</label>
										<div class="col-sm-9">
											<input type="text" class="form-control" name="search_collegeName" value="${param.search_collegeName}">
										</div>
									</div>
								</div>
								<div class="col-md-4 col-xs-6">
									<div class="form-group">
										<label class="control-label col-sm-3">专业</label>
										<div class="col-sm-9">
											<select name="search_specialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
												<option value="">请选择</option>
												<c:forEach items="${specialtyMap}" var="map">
													<option value="${map.key}"  <c:if test="${map.key==param['search_specialtyId']}">selected='selected'</c:if>>${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
						</div><!-- /.box-body -->
						<div class="box-footer text-right">
							<button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
			        		<button type="botton" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
					</div>
					
					<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
					<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
					<div class="box box-border no-shadow margin-bottom-none">
						<div class="box-body">
							<div class="filter-tabs clearfix">
								<ul class="list-unstyled">
									<li <c:if test="${empty(param.search_notSet)&&empty(param.search_isEnabled)}">class="actived"</c:if>>全部（${enable+disable}）</li>
									<li <c:if test="${not empty(param.search_notSet)}">class="actived"</c:if>>待设置（${notSet}）</li>
									<li <c:if test="${param.search_isEnabled=='0'}">class="actived"</c:if> data-status="0">已启用（${enable}）</li>
									<li <c:if test="${param.search_isEnabled=='1'}">class="actived"</c:if> data-status="1">已停用（${disable}）</li>
								</ul>
								<input type="hidden" name="search_isEnabled" value="${param.search_isEnabled}">
								<input type="hidden" name="search_notSet" value="${param.search_notSet}">
								<%-- <div class="pull-right no-margin">
									<a href="${ctx}/graduation/degreeCollege/create" class="btn btn-default btn-sm">
										<i class="fa fa-fw fa-plus"></i> 新增
									</a>
								</div> --%>
							</div>
							<div id="dtable_wrapper" class="table-responsive">
								<table id="dtable" class="table table-bordered table-hover table-font vertical-mid text-center">
									<thead>
										<tr>
											<th><input type="checkbox" class="select-all" id="selectAll"></th>
											<th>院校名称</th>
											<th>专业名称</th>
											<th>是否配置学位申请条件</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${pageInfo.content }" var="info">
											<tr>
												<td><input type="checkbox" value="${info.collegeSpecialtyId }" name="ids"></td>
												<td>${info.collegeName}</td>
												<td>${info.specialtyName}</td>
												<td>${info.degreeReq?'<font color="green">已配置</font>':'<font color="red">未配置</font>'}</td>
												<td>
													<div class="data-operion">
														<shiro:hasPermission name="/graduation/degreeCollege/list$setDegreeApplyCondition">
														<a href="viewDegreeReq/${info.collegeSpecialtyId}"
														   class="operion-item operion-view" title="查看">
															<i class="fa fa-fw fa-eye"></i></a>
														</shiro:hasPermission>
														<shiro:hasPermission name="/graduation/degreeCollege/list$setDegreeApplyCondition">
														<a href="updateDegreeReq/${info.collegeSpecialtyId}"
														   class="operion-item operion-edit" title="配置学位申请条件">
															<i class="fa fa-fw fa-cog"></i></a>
														</shiro:hasPermission>
														<shiro:hasPermission name="/graduation/degreeCollege/list$delete">
														<a href="javascript:void(0);"
															class="operion-item operion-del del-one" val="${info.collegeSpecialtyId}"
															title="删除" data-tempTitle="删除">
															<i class="fa fa-fw fa-trash-o"></i></a>
														</shiro:hasPermission>
													</div>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
			
								<tags:pagination page="${pageInfo}" paginationSize="10" />
			
							</div>
						</div>
					</div>
				</div>
			</div><!-- nav-tabs-custom -->
			
		

		

		
		</form>
	</section>
	<!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		$().ready( function() {
			//单行单条删除
			$('.del-one').unbind('click').click(function(){
				var $this = $(this);
				var id=$(this).attr('val');
				$.confirm({
					title: '提示',
					content: '确认删除？',
					confirmButton: '确认',
					icon: 'fa fa-warning',
					cancelButton: '取消',
					confirmButtonClass: 'btn-primary',
					closeIcon: true,
					closeIconClass: 'fa fa-close',
					confirm: function () {
						$.post("deleteSpecialtyDegreeCollege",{ids:id},function(data){
							if(data.successful){
								//showMessage(data);
								window.location.reload();
								//showSueccess(data);
								//$this.parent().parent().parent().remove();
							}else{
								//showMessage(data);
							}
						},"json");
					}
				});
			});

			//删除多个
			$('.del-checked').unbind('click').click(function(){
				var $checkedIds = $("table td input[name='ids']:enabled:checked");
				if ($checkedIds.size() ==0) {
					$.alert({
						title: '提示',
						icon: 'fa fa-exclamation-circle',
						confirmButtonClass: 'btn-primary',
						content: '至少删除一条数据！',
						confirmButton: '确认'
					});
					return false;
				}
				$.confirm({
					title: '提示',
					content: '确认删除？',
					confirmButton: '确认',
					icon: 'fa fa-warning',
					cancelButton: '取消',
					confirmButtonClass: 'btn-primary',
					closeIcon: true,
					closeIconClass: 'fa fa-close',
					confirm: function () {
						$.post("deleteSpecialtyDegreeCollege",$checkedIds.serialize(),function(data){
							if(data.successful){

								//showMessage(data);
								window.location.reload();

								//showSueccess(data);
								/*var checked = $("table td input[name='ids']:enabled:checked");
								 for(var i=0;i<checked.length;i++){
								 $(checked[i]).parent().parent().remove();
								 $('.select-all').prop("checked", false);
								 } */
							}else{
								//showMessage(data);
							}
						},"json");
					}
				});
			});

			$('.list-unstyled li').click(function(){
			   var index=$('.list-unstyled li').index(this);
			   if(index==0){
					$('[name="search_isEnabled"],[name="search_notSet"]').val('');
				}else if(index==1){
				    $('[name="search_isEnabled"]').val('');
				    $('[name="search_notSet"]').val('1');
				}else{
				    $('[name="search_notSet"]').val('');
				    $('[name="search_isEnabled"]').val($(this).data('status'));
				}
				$('#listForm').submit();
			});
		});
	</script>
</body>
</html>