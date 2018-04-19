<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>教务班级系统-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body">
	<section class="content-header">

		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">基础信息</a>
			</li>
			<li class="active">教务班级</li>
		</ol>
	</section>
	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html">
			<div class="box">
				<div class="box-body">
					<div class="row pad-t15">
						<div class="col-xs-6 col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">学期</label>
								<div class="col-sm-9">
									<select  id="gradeId" name="search_EQ_gjtGrade.gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${gradeMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']||map.key==currentGradeId}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="col-xs-6 col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">班级名称</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_bjmc" value="${param['search_LIKE_bjmc']}">
								</div>
							</div>
						</div>
						<div class="col-xs-6 col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">班主任</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_gjtBzr.xm" value="${param['search_LIKE_gjtBzr.xm']}">
								</div>
							</div>
						</div>
					</div>
					<div class="row pad-t15">
						<div class="col-xs-6 col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">所属机构</label>
								<div class="col-sm-9">
									<select id="orgId" name="search_EQ_gjtOrg.id" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${orgMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtOrg.id']}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
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


			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>>
				<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>>
				<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">班级列表</h3>
					<div class="pull-right no-margin">
						<shiro:hasPermission name="/edumanage/teachclass/list$export">
	                   		<a class="btn btn-default btn-sm margin_r10"  data-role="export" target= "_blank" >
	                   	 	 <i class="fa fa-fw fa-sign-in"></i> 导出教务班级列表</a>
	                    </shiro:hasPermission>
						<shiro:hasPermission name="/edumanage/teachclass/list$syncClassToEeChat">
							<a href="javascript:void(0);" role="button" class="btn btn-default btn-sm margin_r10" data-role='sync'><i class="fa fa-fw fa-sign-out"></i>同步EE平台</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="/edumanage/teachclass/list$create">
							<a href="create" role="button" class="btn btn-default btn-sm margin_r10"><i class="fa fa-fw fa-plus"></i> 新增班级</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="/edumanage/teachclass/list$settingRule">
							<a href="${ctx}/edumanage/rulesClass/setRule" role="button" class="btn btn-default btn-sm" data-role="class-manage"><i
								class="fa fa-fw fa-gear"></i> 设置自动分班规则</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="/edumanage/teachclass/list$settingRule">
						<c:if test="${param.judge == '1' }">
							<a href="javascript:;" role="button" class="btn btn-default btn-sm" id="allot">
								<i	class="fa fa-rule-set"></i> 批量分配班主任
							</a>
						</c:if>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="box-body">
					<div class="table-responsive">
						<div class="filter-tabs clearfix">
							<input type="hidden" name="judge" value="${param.judge}">
							<ul class="list-unstyled">
								<li <c:if test="${empty param.judge}">class="actived"</c:if>>全部(${isSet+unSet})</li>
								<li judge="1" <c:if test="${param.judge == '1' }">class="actived"</c:if>>待分配班主任(${unSet})</li>
								<li judge="0" <c:if test="${param.judge == '0' }">class="actived"</c:if>>已分配班主任(${isSet})</li>
							</ul>
						</div>
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
								<tr>
									<th><input type="checkbox" class="select-all" id="selectAll"></th>
									<th>学期</th>
									<th>班级</th>
									<th>班主任</th>
									<th>班级人数</th>
									<th>所属机构</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty pageInfo.getContent() }">
										<c:forEach items="${pageInfo.getContent() }" var="item">
											<tr>
												<td><input type="checkbox" id="syncClass" value="${item.classId }" name="ids"></td>
												<td>${item.gjtGrade.gradeName}</td>
												<td>${item.bjmc}</td>
												<c:if test="${item.gjtBzr != null}">
													<td>${item.gjtBzr.xm}</td>
												</c:if>
												<c:if test="${item.gjtBzr == null}">
													<td class="text-orange">未设置</td>
												</c:if>
												<td>${empty(item.num)?0:item.num}</td>
												<td><c:if test="${item.gjtOrg != null}">
												${item.gjtOrg.orgName}
											</c:if></td>
												<td>
													<div class="data-operion">
														<shiro:hasPermission name="/edumanage/teachclass/list$update">
															<a href="update/${item.classId}" class="operion-item operion-edit" title="编辑"> <i class="fa fa-fw fa-edit"></i></a>
														</shiro:hasPermission>
														<shiro:hasPermission name="/edumanage/teachclass/list$allocation">
															<a href="${ctx}/edumanage/teachclass/setHeadTeacher?classId=${item.classId}&classType=teach" class="operion-item" data-toggle="tooltip"
																title="分配班主任" data-role="configure-teacher"><i class="fa fa-rule-set"></i></a>
														</shiro:hasPermission>
														<shiro:hasPermission name="/edumanage/teachclass/list$viewClassStudent">
															<a href="${ctx }/edumanage/classstudent/list?classId=${item.classId}&classType=teach&action=student" class="operion-item operion-edit"
																title="班级人员管理" data-tempTitle="班级人员管理"> <i class="fa fa-group-person"></i></a>
														</shiro:hasPermission>
														<c:if test="${empty item.num or item.num==0 }">
															<shiro:hasPermission name="/edumanage/teachclass/list$delete">
																<a href="javascript:void(0);" class="operion-item operion-del del-one" val="${item.classId}" title="删除" data-tempTitle="删除"> <i
																	class="fa fa-fw fa-trash-o"></i></a>
															</shiro:hasPermission>
														</c:if>
													</div>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="7">暂无数据</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
					<tags:pagination page="${pageInfo}" paginationSize="5" />
				</div>
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		//配置班主任
		$('[data-role="configure-teacher"]').click(function(event) {
			event.preventDefault();
			$.mydialog({
				id : 'configure-teacher',
				width : 760,
				height : 500,
				zIndex : 11000,
				content : 'url:' + $(this).attr('href')
			});
		});
		
		//批量分配班主任
		$('#allot').click(function(event) {
			var $checkedIds = $("table td input[name='ids']:enabled:checked");
			if ($checkedIds.size() == 0) {
				$.alert({
							title : '提示',
							icon : 'fa fa-exclamation-circle',
							confirmButtonClass : 'btn-primary',
							content : '请选择要分配的班级！',
							confirmButton : '确认'
						});
				return false;
			} else {
				event.preventDefault();
				$.mydialog({
					id : 'configure-teacher',
					width : 760,
					height : 500,
					zIndex : 11000,
					content : 'url:${ctx}/edumanage/teachclass/editeAllot?' + $checkedIds.serialize()
				});
			}
		});
		
		//设置分班规则
		$('[data-role="class-manage"]').click(function(event) {
			event.preventDefault();
			$.mydialog({
				id : 'class-manage',
				width : 750,
				height : 550,
				zIndex : 11000,
				content : 'url:' + $(this).attr('href')
			});
		});
		$('.list-unstyled li').click(function() {
			$('[name="judge"]').val($(this).attr('judge'));
			$('#listForm').submit();
		});
		//同步至EE平台
		$("[data-role='sync']").click(function(event) {
			event.preventDefault();
			var ids =[];
			$('#syncClass:checked').each(function(){
				ids.push($(this).val());
			});
			if(ids.length==0){
				alert('请选择一个教务班级');
				return;
			}
			if(ids.length>1){
				alert('暂不支持同步多个班级');
				return;
			}
			var postIngIframe = $.mydialog({
				id : 'dialog-1',
				width : 150,
				height : 50,
				backdrop : false,
				fade : false,
				showCloseIco : false,
				zIndex : 11000,
				content : '<div class="text-center pad-t15">数据同步中...<i class="fa fa-refresh fa-spin"></i></div>'
			});
			$.post("${ctx}/edumanage/teachclass/syncClassToEeChat",{
				classId:ids[0]
				},function(data,status){
					//$.closeDialog(frameElement.api);
					alert(data.message);	
					window.location.reload();
				},'json'
			);
		});
		
		$('[data-role="export"]').click(function(event) {
			var gradeId=$('#gradeId').val();
			var orgId=$('#orgId').val();
			var url="${ctx}/edumanage/teachclass/export?search_EQ_gjtGrade.gradeId="+gradeId+'&search_EQ_gjtOrg.id='+orgId
			$(this).attr('href',url);
		});
		
		
	</script>
</body>
</html>
