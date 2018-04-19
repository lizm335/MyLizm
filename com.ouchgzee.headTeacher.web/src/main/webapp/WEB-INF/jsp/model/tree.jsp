<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script src="${ctx }/static/bootstrap/css/bootstrap-treeview.css"></script>
<script src="${ctx }/static/bootstrap/js/bootstrap-treeview.js"></script>

<script type="text/javascript"> 
	$(function() {  
		$.get("${ctx}/home/model/getTree",function(data){
			$('#treeview').treeview({
				    bootstrap2 : false,
					data: data,     
					levels : 5,
					selectedIcon : true,
					highlightSelected: true,  
					showBorder : true,
					
					onNodeSelected: function(event, data) {
						var nodeId = data.nodeId;
						if(data.parentId != null){
							var parent = $('#treeview').treeview('getParent', nodeId);
							$('#parentModelId').val(parent.id);
							$('#parentModelName').val(parent.text); 
						}else{
							$('#parentModelId').val('');
							$('#parentModelName').val('无'); 
						}
						
						$.get("get/"+data.id,function(data){
							$('#modelId').val(data.modelId);
							$('#modelCode').val(data.modelCode);
							$('#modelName').val(data.modelName);
							$('#modelAddress').val(data.modelAddress);
							$('#orderNo').val(data.orderNo);
							
							//重置验证
							$('#inputForm').data('bootstrapValidator').resetForm();
							
							if(data.isLeaf){
								$('#isLeaf').prop("checked", true);
								$('#modelAddressDiv').show();
								$('#modelOperateDiv').show();
								
								var checkboxs = $(":checkbox[name='operateId']");
								for(var i=0;i<checkboxs.length;i++){
									var checkId = $(checkboxs[i]).val();
									$(checkboxs[i]).prop("checked",false);
									for(var j=0;j<data.priOperateInfos.length;j++){
										if(data.priOperateInfos[j].operateId == checkId){
											$(checkboxs[i]).prop("checked",true);
										}
									}
								}
								
							}else{
								$('#isLeaf').prop("checked", false);
								$('#modelAddressDiv').hide();
								$('#modelOperateDiv').hide();
							}
						},'json');
					  }, 
					onNodeUnselected : function(event, data) {
						var nodeId = data.nodeId;
						resetForm();
					}
				});  
		  });
		
		$('#input-search').keyup(function(){
			var pattern = $('#input-search').val();
	          var options = {
	        		  ignoreCase: true,     // case insensitive 
	        		  exactMatch: false,    // like or equals 
	        		  revealResults: true,  // reveal matching nodes 
	          };
	          var results = $('#treeview').treeview('search', [ pattern, options ]);
	          
	          $('#treeview').clone(true);
	          
	          //重载树（有问题）
	          //$('#treeview').treeview({data: results});
	          
	          var output = '<p>查询到 ' + results.length + ' 个</p>';
	          $('#search-output').html(output); 
		});
		
		$('#inputForm').bootstrapValidator({
            fields: {
            	modelCode: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                modelName: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                orderNo: {
                    validators: {
                        notEmpty: "required",
                		digits:"required",
                    }
                }
            }
        });
		
		
		$('#isLeaf').change(function(){
			if($(this).prop("checked")){
				$('#modelAddressDiv').show();
				$('#modelOperateDiv').show();
			}else{
				$('#modelAddressDiv').hide();
				$('#modelOperateDiv').hide();
			}
		});
		
		$('#btn-del').click(function(){
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
	            	   $('#inputForm')[0].action ="delete";
	            	   $('#inputForm')[0].submit();
	               } 
	           });
	       });
		
	});
	
	function resetForm(){
		$('#inputForm')[0].reset();
		$('#modelId').val('');
		$('#parentModelId').val('');
		$('#parentModelName').val('无'); 
		$('#modelAddressDiv').hide();
		$('#modelOperateDiv').hide();
		
	}
	
	function createForm(){
		//当前选中的节点作为父节点
		var parentId = $('#modelId').val();
		var parentName = $('#modelName').val();
		
		//重置表单
		resetForm();
		//重置验证
		$('#inputForm').data('bootstrapValidator').resetForm();
		
		$('#inputForm')[0].action ="create";
		$('#parentModelId').val(parentId);
		$('#parentModelName').val(parentName); 
	}
	
</script>

</head>
<body class="inner-page-body">

	<!-- Main content -->
	<section class="content-header">
		<h1>菜单列表</h1>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">系统管理</a></li>
			<li class="active">个人中心菜单管理</li>
		</ol>
	</section>

	<section class="content">
		<%-- <form id="listForm" class="form-horizontal">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">菜单名称</label>
							<div class="col-sm-9">
								<input class="form-control" type="text"
									name="search_LIKE_modelName"
									value="${param.search_LIKE_modelName}">
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer">
					<div class="search-more-in">
						高级搜索<i class="fa fa-fw fa-caret-down"></i>
					</div>
					<div class="btn-wrap">
						<button type="button" class="btn btn-default">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
			</div>
		</form> --%>


		<div class="alert alert-success"
			<c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>>
			<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger"
			<c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>>
			<button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

		<div class="box">
			<div class="box-header with-border">
				<div class="fr">
					<div class="btn-wrap fl">
						<a href="javascript:void(0);" onclick="createForm()"
							class="btn btn-default btn-sm"> <i
							class="fa fa-fw fa-plus"></i> 新增
						</a>
					</div>
					<div class="btn-wrap fl">
						<a id="btn-del" href="javascript:void(0);"
							class="btn btn-block btn-danger btn-del"> <i
							class="fa fa-fw fa-trash-o"></i> 删除
						</a>
					</div>
				</div>
			</div>

			<div class="box-body">
				<div class="row">
					<div class="col-sm-6"></div>
					<div class="col-sm-6"></div>
				</div>

				<div class="row">
					 <div class="col-sm-6 form-inline"  >
			              <input type="input" class="form-control col-sm-8" id="input-search" placeholder="搜索..." value="">
			              <div id="search-output" class="col-sm-4"></div>
			          </div>
				</div>
				
				<div class="row">
					<div>
						<label for="treeview"></label>
						<div id="treeview" class="col-sm-6" />
					</div>

					<div class="col-sm-6">

						<form id="inputForm" class="form-horizontal" role="form" action="update" method="post">
							
							<input type="hidden" id="modelId" name="modelId">
							<input type="hidden" id="parentModelId" name="parentModel.modelId">
									
								<div class="form-group"> 
									<label class="col-sm-2 control-label">上级菜单:</label>
									<div class="col-sm-4">
										<input readonly="readonly" type="text" id="parentModelName" name="parentModelName" class="form-control" value="无"/>
									</div>
								</div>
								
								<div class="form-group"> 
									<label class="col-sm-2 control-label"><small class="text-red">*</small>菜单编码:</label>
									<div class="col-sm-4">
										<input type="text" id="modelCode" name="modelCode" class="form-control" placeholder="请输入菜单编码"/>
									</div>
								</div>
								
								<div class="form-group"> 
									<label class="col-sm-2 control-label"><small class="text-red">*</small>菜单名称:</label>
									<div class="col-sm-4">
										<input type="text" id="modelName" name="modelName" class="form-control" placeholder="请输入菜单名称"/>
									</div>
								</div>
								 
								<div class="form-group"> 
									<label class="col-sm-2 control-label"><small class="text-red">*</small>顺序:</label>
									<div class="col-sm-2">
										<input type="text" id="orderNo" name="orderNo" class="form-control" placeholder="请输入数字"/>
									</div>
								</div>
								
								<div class="form-group"> 
									<label class="col-sm-2 control-label">节点:</label>
									<div class="col-sm-6">
										<label class="checkbox-inline">
									      <input id="isLeaf" type="checkbox" name="isLeaf" value="1"/> 开启
									   </label>
									</div>
								</div>
								
								<div id="modelAddressDiv" class="form-group" hidden="hidden"> 
									<label class="col-sm-2 control-label">链接地址:</label>
									<div class="col-sm-6">
										<input type="text" id="modelAddress" name="modelAddress" class="form-control" placeholder="/xxx/xxx/xxx"/>
									</div>
								</div>
								
								<div id="modelOperateDiv" class="form-group" hidden="hidden"> 
									<label class="col-sm-2 control-label">当前操作:</label>
									<div class="col-sm-6 checkbox">
											<c:forEach items="${operateList}" var="item" varStatus="vs">
									                <label>
									                    <input type="checkbox" name="operateId" value="${item.operateId }" /> ${item.operateName }
									                </label>
											</c:forEach>
									</div>
								</div>
								
								<div class="form-group">
							      <div class="col-sm-offset-2 col-sm-10">
							         <button type="submit" class="btn btn-primary">确 定</button>
							         <!-- <button type="reset" class="btn btn-default">重 置</button> -->
							      </div>
							   </div>
								
							</div>
						</form>
 
				</div>
			</div>


		</div>
	</section>
</body>


</html>
