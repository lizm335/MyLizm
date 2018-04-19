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

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">机构管理</a></li>
		<li class="active">院校管理</li>
	</ol>
</section>
<section class="content" data-id="0">
	<form class="form-horizontal" id="listForm" action="list.html">
		<div class="box">
		  <div class="box-body">
			  <div class="row pad-t15">
				<div class="col-sm-4 col-xs-6">
				  <div class="form-group">
					<label class="control-label col-sm-3 text-nowrap">院校编码</label>
					<div class="col-sm-9">
					 <input class="form-control" type="text" name="search_LIKE_code" value="${param.search_LIKE_code}">
					</div>
				  </div>
				</div>
				<div class="col-sm-4 col-xs-6">
				  <div class="form-group">
					<label class="control-label col-sm-3 text-nowrap">院校名称</label>
					<div class="col-sm-9">
					  <select name="search_EQ_id" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="">--请选择--</option>
						<c:forEach items="${orgMap}" var="org">
							<option value="${org.key}"  <c:if test="${org.key==param['search_EQ_id']}">selected='selected'</c:if>>${org.value}</option>
						</c:forEach>
					 </select>
					</div>
				  </div>
				</div>
			  </div>
		  </div><!-- /.box-body -->
		  <div class="box-footer">
			<shiro:hasPermission name="/organization/org/list$reset">
				<div class="pull-right"><button type="button" id="reset_data" class="btn min-width-90px btn-default btn-reset">重置</button></div>
			</shiro:hasPermission>
			<div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary" id="search_submit_button">搜索</button></div>
		  </div><!-- /.box-footer-->
		</div>

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box">
			<div class="box-header with-border">
			  <h3 class="box-title pad-t5">院校列表</h3>

			 <shiro:hasPermission name="/organization/org/list$create">
			  <div class="pull-right no-margin">
				<a role="button" href="toCreate" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新增院校</a>
			  </div>
			 </shiro:hasPermission>
			</div>
			<div class="box-body">
				<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
					<table class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
					  <thead>
						<tr>
							<th>院校编码</th>
							<th>院校名称</th>
							<th>联系人</th>
							<th>联系电话</th>
							<th>用户数</th>
							<th>分校数</th>
							<th>学习中心数</th>
							<th>操作</th>
						</tr>
					  </thead>
					  <tbody>
					  <c:choose>
						<c:when test="${not empty pageInfo.content}">
						  <c:forEach items="${pageInfo.content}" var="entity">
						  <c:if test="${not empty entity}">
						<tr>
						  <td>${entity.code}</td>
						  <td>${entity.orgName}</td>
						  <td>${entity.gjtSchoolInfo.linkMan}</td>
						  <td>${entity.gjtSchoolInfo.linkTel}</td>
						  <td>${userAcountNum.size()}</td>
						  <td>${entity.getChildGjtOrgSchools()}</td>
						  <td>${entity.getChildGjtOrgsChilds()}</td>
						  <td>
							<div class="data-operion">
								<shiro:hasPermission name="/organization/org/list$edit">
									<a href="update/${entity.id}" class="operion-item operion-view" data-toggle="tooltip" title="编辑"><i class="fa fa-fw fa-edit"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/organization/org/list$view">
									<a href="view/${entity.id}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
								</shiro:hasPermission>
							  	<shiro:hasPermission name="/organization/org/list$delete">
									<a href="${ctx}/organization/org/deleteOrg/${entity.id}" val="${entity.id}" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="sure-btn-1"><i class="fa fa-fw fa-trash-o text-red"></i></a>
							  	</shiro:hasPermission><!--先注释，以后可能会用到-->
								<shiro:hasPermission name="/organization/org/list$settings">
									<a href="orgSet/${entity.id}" class="operion-item operion-view" data-toggle="tooltip" title="设置"><i class="fa fa-fw fa-gear"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/organization/org/list$users">
									<a href="${ctx}/organization/userManager/list?orgId=${entity.id}" class="operion-item operion-edit" title="用户"><i class="fa fa-fw fa-user"></i></a>
								</shiro:hasPermission>
							</div>
						  </td>
						</tr>
						</c:if>
					   </c:forEach>
					  </c:when>
					  <c:otherwise>
						<tr>
							<td align="center" colspan="8">暂无数据</td>
						</tr>
					 </c:otherwise>
					 </c:choose>
					  </tbody>
					</table>
				   <tags:pagination page="${pageInfo}" paginationSize="10"/>
				</div>
			</div>
		</div>
	</form>
</section>
<script type="text/javascript">
  var url = "";
$("body").confirmation({
  selector: "[data-role='sure-btn-1']",
  html:true,
  placement:'top',
  content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定要删除该院校？</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:190,
  onShow:function(event,element){
    url = $(event.target).attr("href");
  },
  onConfirm:function(event,element){
      $.ajax({
          type: "POST",
          dataType: "json",
          url: url,
          success: function (data) {
			  alert(data.message);
              if(data.successful==true){
              	$("#search_submit_button").click();
			  }
          },
          complete: function (request,status) {

          }
      });
  },
  onCancel:function(event, element){

  }
});

  $("#reset_data").off().on('click',function () {
	 $(this).closest("form").find("input,select,checkbox,radio").each(function (i) {
		$(this).val("");
     });
  });

</script>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
