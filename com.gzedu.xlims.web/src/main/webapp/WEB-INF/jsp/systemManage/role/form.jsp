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
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit').remove();  
	}
	
	var roleId = '${entity.roleId }';
	
	$.get("${ctx}/system/role/getTree/"+roleId,function(data){
		$('#loading').remove();

		var roleMenu=(function createMenuTree(zTreeSelector, jsonData, keywordsInputSelector, searchButtonSelector ,resetButtonSelector){
			var setting={
				check: {
					enable: true,
					chkboxType: { "Y": "ps", "N": "s" }
				},

				data: {
					key: {
						name: "text",
						children: "nodes"
					},
					simpleData: {
						enable: true
					}
				},
		    view: {
		      showIcon: false
		    }
			}
			
			var zTreeObj=$.fn.zTree.init($(zTreeSelector), setting, jsonData);

		  //格式化数据
			$.each( zTreeObj.transformToArray(zTreeObj.getNodes()), function(i, n) {
				n.open=true;
				if(n.state && n.state.checked){
					n.checked=true;
				}
				else{
					n.checked=false;
				}
			});

		  //重新刷新数据显示
			zTreeObj.refresh();

			//搜索
			$(searchButtonSelector).click(function(event) {
				var keywords=$.trim( $(keywordsInputSelector).val() );
				var allNodes=zTreeObj.transformToArray(zTreeObj.getNodes());

				//处理符合条件的结点
		    if(keywords!=''){
		      //过滤出符合条件的结点
		      var $filterItems=zTreeObj.getNodesByParamFuzzy("text", keywords);
		      //显示符合条件的结点
		  		if( $filterItems.length>0 ){
		        //全部设置为隐藏
		        $.each(allNodes, function(index, node) {
		          node.isHidden=true;
		        });

		  			$.each($filterItems,function(index, node) {
		          node.isHidden=false;
		          factChild(node);
		  			  factParent( node );
		  			});

		        //显示符合条件的结点的子结点
		        $.each($filterItems, function(index,node) {
		            if(node.nodes && node.nodes.length>0){
		               $.each(node.nodes, function(i,n) {
		                  n.isHidden=false;
		               });
		            }
		        });

		        $.fn.zTree.init($(zTreeSelector), setting, zTreeObj.getNodes());
		  		}
		      else{
		        alert('无相关数据');
		      }
		    }
		    else{
		      zTreeObj.showNodes(allNodes);
		    }

				//递归向上遍历父级
				function factParent(node){
					var $p=node.getParentNode();
					if( $p ){
		        $p.isHidden=false;

					  factParent($p);
					}
				}

		    //递归向上遍历子级
		    function factChild(node){
		      var children=node.nodes;
		      if( children && children.length>0 ){
		        $.each(children, function(index, child) {
		           child.isHidden=false;
		           factChild(child);
		        });
		      }
		    }
			});

		  //展开所有结点
		  $(resetButtonSelector).click(function(event) {
		    var allNodes=zTreeObj.transformToArray(zTreeObj.getNodes());
		    zTreeObj.showNodes(allNodes);
		    zTreeObj.expandAll(true);
		  });

		})('#role', $.parseJSON(data), '[data-role="role-keywords"]', '[data-role="role-search-btn"]','[data-role="reset"]');

		
		;(function(){
			var $fixedPanel=$('[data-id="fixed-panel"]');
			var $fixedPanelBox=$('[data-id="fixed-panel-box"]');
			var $win=$(window);
			$(window).on('scroll resize',function(event) {
				var edageVal=$fixedPanelBox.offset().top;
				var scrollTopVal= $win.scrollTop();
				var differ=scrollTopVal-edageVal+45+10;
				var winHeight=$win.height();
				var siteFooterOffsetTop=$('.site-footer').offset().top;
				var differ2=winHeight+scrollTopVal-siteFooterOffsetTop

				$fixedPanel.css({
					'position':(differ>0?'absolute':'static'),
					'top': (differ>0?differ:0),
					'height':(differ2>0?(winHeight-differ2):winHeight)-80,
					'overflow-x':'hidden',
					'overflow-y':'auto'
				});
			}).trigger('scroll');
		})();
	  });
	
	
	
	$('[data-role="role-keywords"]').keydown(function(e){
		if(e.keyCode == 13){
		   $('[data-role="role-search-btn"]').click();
		}
	});
	
	//参考： http://bv.doc.javake.cn/examples/ 
     $('#inputForm').bootstrapValidator({
            fields: {
            	roleName: {
                    validators: {
                        notEmpty: "required"
                    }
                },
				roleCode: {
					validators: {
						notEmpty: "required"
					}
				}
            }
        }); 
	  
      $('[data-role="save"]').on('click',function(e){
    	  e.preventDefault();
    	  var bsValidatorObj=$('#inputForm').data('bootstrapValidator');//获取验证控件对象
    	  bsValidatorObj.validate();//手动启动表单验证
    	  
    	  //如果验证都通过
    	  if(bsValidatorObj.isValid()){    	  
	    	  var ztree=$.fn.zTree.getZTreeObj("role");
			  //var nodesList=ztree.getCheckedNodes();
	
			  var filter=ztree.getNodesByFilter(function(node){
			        return node.checked;
			      });
	
				var modelIds = [];
				var modelOperateIds = [];
				$.each(filter, function(i,n) {
					if (n.href == "#") {
						//modelIds += n.id+",";
						modelIds.push(n.id)
					} else if (n.href == "##") {
						modelOperateIds.push(n.id);
					}
				})
				$('#modelIds').val(modelIds.join(','));
				$('#modelOperateIds').val(modelOperateIds.join(','));
	
			 
			  //手动提交表单
			  bsValidatorObj.defaultSubmit();
		  
    	  }
     }); 
})
</script> 

</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" id="btn-back" onclick="history.back()">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">系统管理</a></li>
		<li class="active">角色管理</li>
	</ol>
</section>
<section class="content">
  
  <div class="box no-margin">
		<div class="box-body pad20">
	        <div class="row">
	        	<div class="col-sm-6">
	        		<div class="panel panel-default margin-bottom-none">
	        			<div class="panel-heading">
		                  <div class="media">
		                    <div class="media-body">
		                      <div class="input-group input-group-sm">
		                        <input class="form-control" placeholder="输入名称搜索" data-role="role-keywords">
		                        <div class="input-group-btn">
		                          <button type="button" class="btn btn-primary" data-role="role-search-btn">搜索</button>
		                        </div>
		                      </div>
		                    </div>
		                    <div class="media-right">
		                      <button type="button" class="btn btn-default btn-sm" data-role="reset">展开所有</button>
		                    </div>
		                  </div>
					    </div>
	        			<div class="panel-body">
	        				<div class="scroll-box" style="max-height:600px;">
	        					<div class="text-center" id="loading">
	        						<i class="fa fa-refresh fa-spin" style="font-size:36px;"></i>
	        					</div>
	        					<ul class="ztree" id="role"></ul>
	        				</div>
	        				
	        			</div>
	        		</div>
	        	</div>
	        	<div class="col-sm-6" data-id="fixed-panel-box">
	        		<div class="position-relative">
		        		<div data-id="fixed-panel" class="full-width bg-white">
						    <form id="inputForm" role="form" action="${ctx }/system/role/${action}" method="post">
							 	<input id="action" type="hidden" name="action" value="${action }">
						     	<input id="roleId" type="hidden" name="roleId" value="${entity.roleId }">
						     	<input id="modelIds" type="hidden" name="modelIds" value="">
						     	<input id="modelOperateIds" type="hidden" name="modelOperateIds" value="">
				            	<div class="pad-l15 pad-r15">
					              	<div class="form-horizontal">
						                <div class="form-group">
						                  <label class="col-sm-3 control-label text-nowrap">
						                    <small class="text-red">*</small>角色名称:
						                  </label>
						                  <div class="col-sm-7">
						                    <input type="text" name="roleName" id='checkName' class="form-control" value="${entity.roleName }"/>
						                  </div>
						                </div>
						                <div class="form-group">
						                  <label class="col-sm-3 control-label text-nowrap">
						                    <small class="text-red">*</small>角色编号:
						                  </label>
						                  <div class="col-sm-7">
						                    <input type="text" name="roleCode" class="form-control" value="${entity.roleCode }" ${isUpdateCode?'':'readonly="readonly"'} />
						                  </div>
						                </div>
										<div class="form-group">
											  <label class="col-sm-3 control-label text-nowrap">
												  <small class="text-red">*</small>所属平台:
											  </label>
											  <div class="col-sm-7">
												  <dic:selectBox name="platformType" typeCode="PLATFORM_TYPE" code="${entity.platformType }" 
												  otherAttrs='class="selectpicker show-tick form-control" data-size="8" data-live-search="true"' />
											  </div>
										</div>
						                <div class="form-group">
						                  <label class="col-sm-3 control-label">
						                    	是否在用户管理下拉列表中显示:
						                  </label>
						                  <div class="col-sm-7">
						                    <div class="radio">
						                        <label>
						                          <input type="radio" name="isMng" value="1" checked />是
						                        </label>
						                        <label class="margin_l15">
						                          <input type="radio" name="isMng" value="0" />否
						                        </label>
						                    </div>
						                  </div>
						                </div>
						                
						                <%-- <div class="form-group">
											  <label class="col-sm-2 control-label text-nowrap">
												  管理下属角色:
											  </label>
											  <div class="col-sm-6">
										  			<table>
											  			<c:forEach items="${roleList }" var="item">
										  				<tr>
												  			<td>
												  				
														  		<input type="checkbox" value="${item.roleId }" name ="roleIds" <c:if test="${item.isCheck}">checked="checked"</c:if>>${item.roleName }
														  	</td>
										  				</tr>
											  			</c:forEach>
										  			</table>
											  </div>
										  </div> --%>
						                
						                <div class="box-footer no-pad-left no-pad-right form-group">
						                  <div class="col-sm-offset-3 col-sm-7">
						                    <button type="submit" class="btn btn-primary margin_r10" data-role="save">确定</button>
						                    <button type="button" class="btn btn-default" onclick="history.back()">取消</button>
						                  </div>
						                </div>
					              	</div>
				              	</div>
				            </form>
			            </div>
		            </div>
	            </div>
	        </div>
		</div>
	</div>

</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>