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

function checkNodes(data){
	$('#treeview').treeview('checkNode', [ data.nodeId, { silent: true } ]);
	var nodes = data.nodes; 
	if(data.nodes!=null){
		for(var i=0;i<nodes.length;i++){
			if(nodes[i].nodes !=null){
				checkNodes(nodes[i]);
			}else{
				$('#treeview').treeview('checkNode', [ nodes[i].nodeId, { silent: true } ]);
			}
		}
	}
}

function checkParent(data){
	if(data.parentId!=null){
		$('#treeview').treeview('checkNode', [ data.parentId, { silent: true } ]);
		var pp = $('#treeview').treeview('getNode', data.parentId);
		checkParent(pp);
	}
}

function uncheckNodes(data){
	$('#treeview').treeview('uncheckNode', [ data.nodeId, { silent: true } ]);
	var nodes = data.nodes;
	if(data.nodes!=null){
		for(var i=0;i<nodes.length;i++){
			if(nodes[i].nodes !=null && nodes[i].nodes.length >0){
				uncheckNodes(nodes[i]);
			}else{
				$('#treeview').treeview('uncheckNode', [ nodes[i].nodeId, { silent: true } ]);
			}
		}
	}
}

$(function() {
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
	
	console.log($('#roleId').val());
	var roleId = $('#roleId').val();
	
	$.get("${ctx}/system/role/getTree/"+roleId,function(data){
		$('#treeview').treeview({
			    //bootstrap2 : false,
				data: data,      
				levels : 5, 
				highlightSelected: false,  
				showCheckbox : true, 
				//multiSelect : true,
				
				onNodeChecked : function(event, data) {
					var nodeId = data.nodeId;
					$('#treeview').treeview('checkNode', [ nodeId, { silent: true } ]);
					checkNodes(data);
					checkParent(data);
				},
				onNodeUnchecked : function(event, data) {
					var nodeId = data.nodeId;
					$('#treeview').treeview('uncheckNode', [ nodeId, { silent: true } ]);
					uncheckNodes(data);
				}
			});  
	  });
	
	//参考： http://bv.doc.javake.cn/examples/ 
     $('#inputForm').bootstrapValidator({
            fields: {
            	roleName: {
                    validators: {
                        notEmpty: "required",
                    }
                },
                roleCode: {
                    validators: {
                        notEmpty: "required",
                    }
                }
            }
        }); 
	
     $("#inputForm").submit(function(e){
    	 	var resultNode = $('#treeview').treeview('getChecked');
			var modelIds = "";
			for(var i=0;i<resultNode.length;i++){
				modelIds += resultNode[i].id+",";
			}
			$('#modelIds').val(modelIds);
     });
})
</script> 

</head>
<body class="inner-page-body">
<section class="content">
          <div class="row">
            <div class="col-md-12">
              <div class="box box-primary">
                <div class="box-header with-border"> 
                  <h3 class="box-title">角色管理</h3>
                </div>
                
                <form id="inputForm" class="form-horizontal" role="form" action="${ctx }/system/role/${action}" method="post">
                <div class="row">
	                <div>
						<label for="treeview"></label>
						<div id="treeview" class="col-sm-6" />
					</div>
					<div class="col-sm-6">
						
		                <input id="action" type="hidden" name="action" value="${action }">
		                <input id="roleId" type="hidden" name="roleId" value="${entity.roleId }">
		                <input id="modelIds" type="hidden" name="modelIds" value="">
		                  <div class="box-body">
									<div class="form-group"> 
										<label class="col-sm-2 control-label"><small class="text-red">*</small>角色名称:</label>
										<div class="col-sm-4">
											<input type="text" name="roleName" class="form-control" value="${entity.roleName }"/>
										</div>
									</div>
									
									<div class="form-group"> 
										<label class="col-sm-2 control-label"><small class="text-red">*</small>角色编号:</label>
										<div class="col-sm-4">
											<input type="text" name="roleCode" class="form-control" value="${entity.roleCode }"/>
										</div>
									</div>
		                  </div>
		
		                  <div class="box-footer">
		                  	<div class="col-sm-offset-1 col-sm-11">
			                    <button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
		    	                <button id="btn-back" class="btn btn-primary" onclick="history.back()">返回</button>
		                  	</div>
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