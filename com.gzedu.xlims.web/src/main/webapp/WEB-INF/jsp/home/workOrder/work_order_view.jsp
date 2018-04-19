<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">我的工单</a>
			</li>
			<li class="active">工单详情</li>
		</ol>
	</section>
	<section class="content">
		<div class="box">
			<div class="box-header with-border pad20">
				<div class="row">
					<div class="col-sm-7">
						<input type="hidden" value="${info.id }" id="workOrderId">
						<div class="text-no-bold f20 margin_b10">[${workTypeMap[info.workOrderType] }] ${info.title }</div>
						<ul class="list-unstyled gray6">
							<li>指派人：${info.createUser.realName}（${info.createUser.priRoleInfo.roleName}）</li>
							<li>指派时间：<fmt:formatDate value="${info.createdDt }" type="both"/></li>
							<li>要求完成时间：${info.demandDate }</li>
							<li>
								<div class="media">
									<div class="media-left text-nowrap no-pad-right">指派给：</div>
									<div class="media-body" data-cache="${assignPersons }"></div>
								</div>
							</li>
							<li>
								<div class="media">
									<div class="media-left text-nowrap no-pad-right">抄送给：</div>
									<div class="media-body" data-cache="${users }">
										<!--
                  黄小米（教务管理员）、张晓明（教学管理员）
                  <a href="#" data-role="see-more" class="text-underline f12">展开&gt;&gt;</a>
                  -->
									</div>
								</div>
							</li>
						</ul>
					</div>
					<div class="col-sm-5">
						<table class="task-static text-center vertical-middle margin_t10 pull-right" height="120">
							<tbody>
								<tr>
									<td width="150">
										<c:if test="${info.priority eq '1' }">
											<div class="text-orange f24">
												优先
											</div>
										</c:if>
										<c:if test="${info.priority eq '0' }">
											<div class="text-gray f24">
												一般
											</div>
										</c:if>
										<c:if test="${info.priority eq '2' }">
											<div class="text-red f24">
												紧急
											</div>
										</c:if>
										<div>级别</div>
									</td>
									<td width="150">
										<c:if test="${info.isState eq '0' }">
											<div class="text-orange f24">
											待完成
											</div>
										</c:if>
										<c:if test="${info.isState eq '1' }">
											<div class="text-green f24">
												已完成
											</div>
										</c:if>
										<c:if test="${info.isState eq '2' }">
											<div class="text-gray f24">
												已关闭
											</div>
										</c:if>
										<div>状态</div>
									</td>
									<c:if test="${type eq '1' }">
										<td width="150">
											<button class="btn btn-default min-width-90px margin5" type="button" data-role="confirm-sure">我已完成</button>
										</td>
									</c:if>
									<c:if test="${type eq '0' }">
										<td width="150">
											<c:if test="${info.isState ne '2' }"> 
												<button class="btn btn-default min-width-90px margin5" type="button" data-role="confirm-close"  data-id="2">关闭</button>
												<a href="${ctx }/home/workOrder/update?id=${info.id }" class="btn btn-default min-width-90px margin5"  title="编辑" >
												编辑
											</a>
											</c:if>
												<button class="btn btn-default min-width-90px margin5" type="button" data-role="confirm-close" data-id="0">打回</button>
										</td>
									</c:if>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="box-body pad20">
				<div>${info.content }</div>
				<div class="margin_t15">
					<a href="${info.fileUrl }"> <i class="fa fa-fw fa-download"></i> ${info.fileName }
					</a>
				</div>

				<div class="panel panel-default margin_t20">
					<div class="panel-heading">讨论（${commentCount }）</div>
					<div class="panel-body">
						<ul class="list-group">
							<c:forEach items="${commentList }" var="item">
								<li class="list-group-item">
								<dl class="no-margin">
									<dt>
										${item.gjtUserAccount.realName }（${item.gjtUserAccount.priRoleInfo.roleName}）
										<small class="text-no-bold"><fmt:formatDate value="${item.createdDt }" type="both"/> 发表</small>
										<c:if test="${item.gjtUserAccount.id eq currentUser.id }">
										<div class="pull-right">
											<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="btn-delete" data-id="${item.id }">
												<i class="fa fa-fw fa-trash-o text-red"></i>
											</a> 
												<!-- <a href="#" class="operion-item operion-view" data-toggle="tooltip" title="编辑"><i	class="fa fa-fw fa-edit"></i></a> -->
										</div>
										</c:if>
									</dt>
									<dd class="margin_t5">${item.content }</dd>
								</dl>
							</li>
						</c:forEach>
						</ul>
						<div>
							<textarea class="form-control" rows="5" placeholder="请输入讨论内容" id="comment"></textarea>
							<div class="margin_t10 text-right">
								<!-- <button class="btn btn-default min-width-90px">取消</button> -->
								<button class="btn btn-primary min-width-90px margin_l10 addComment">发表</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
		var workOrderId=$('#workOrderId').val();
		//初始化 展开的数据
		$('[data-cache]').each(function(index, el) {
		  var cache=$(this).data('cache');
		  var arr;
		  if(cache.length>1){
		    arr=cache.split(',');
		    $(this).html(arr.join('、'))
		    if(arr.length>3){
		      $(this).html(arr.slice(0, 3).join('、')+'<a href="#" data-role="expand" class="text-underline f12">展开&gt;&gt;</a>');
		    }
		  }
		});
	
		//展开
		$('body').on('click', '[data-role="expand"]', function(event) {
		  event.preventDefault();
		  var cache=$(this).parent().data('cache');
		  var arr=cache.split(',');
		  if(arr.length>3){
		    $(this).parent().html(arr.join('、')+'<a href="#" data-role="close" class="text-underline f12">收起&lt;&lt;</a>');
		  }
		})
		//收起
		.on('click', '[data-role="close"]', function(event) {
		  event.preventDefault();
		  var cache=$(this).parent().data('cache');
		  var arr=cache.split(',');
		  if(arr.length>3){
		    $(this).parent().html(arr.slice(0, 3).join('、')+'<a href="#" data-role="expand" class="text-underline f12">展开&gt;&gt;</a>');
		  }
		});
		
		$('.addComment').click(function(){
			var comment=$('#comment').val();
			if(comment.length<1){
				alert('讨论内容不能为空！');
			}else{
				 $.get('${ctx}/home/workOrder/createComment',{id:workOrderId,content:comment},function(data){
					if(data.successful==false){
						alert(data.message);
					}else{
						window.location.href=window.location.href;
					}
				},'json');  
			}
		});
		
		$('[data-role="confirm-sure"]').click(function(){
			 $.alertDialog({
	               id:'colseWork',
	               width:400,
	               height:200,
	               zIndex:11000,
	               ok:function(){//“确定”按钮的回调方法
	            	   $.get('${ctx}/home/workOrder/updateState',{id:workOrderId,state:1},function(data){
	           	 		if(data.successful==false){
	           	 			alert(data.message);
	           	 		}else{
	           	 			window.location.href=window.location.href;
	           	 		}
	           		  },'json'); 
	               },
	               content:'<div class="margin_b10 text-center f18"><i class="fa fa-fw fa-exclamation-circle text-orange vertical-mid" style="font-size:38px;"></i><span class="inline-block vertical-middle">确定设置为已完成？</span></div>'
	           });
		});
		
		$('[data-role="confirm-close"]').click(function(){
			var $state=$(this).data('id');
			var message;
			if($state=='0'){
				message="确认将工单设置未待开始？";
			}else{
				message="确定工单关闭？";
			}
			 $.alertDialog({
	               id:'colseWork',
	               width:400,
	               height:200,
	               zIndex:11000,
	               ok:function(){//“确定”按钮的回调方法
	            	   $.get('${ctx}/home/workOrder/updateState',{id:workOrderId,state:$state},function(data){
	           	 		if(data.successful==false){
	           	 			alert(data.message);
	           	 		}else{
	           	 			window.location.href=window.location.href;
	           	 		}
	           		  },'json'); 
	               },
	               content:'<div class="margin_b10 text-center f18"><i class="fa fa-fw fa-exclamation-circle text-orange vertical-mid" style="font-size:38px;"></i><span class="inline-block vertical-middle">'+message+'</span></div>'
	           });
		});
			 
		$('[data-role="btn-delete"]').click(function(){
			 var $id=$(this).data('id');
	           $.alertDialog({
	               id:'delete',
	               width:400,
	               height:200,
	               zIndex:11000,
	               ok:function(){//“确定”按钮的回调方法
	            	   $.get("${ctx}/home/workOrder/deleteComment",{id:$id},function(data){
		               		if(data.successful==false){
								alert(data.message);
							}else{
								window.location.href=window.location.href;
							}
		               },"json"); 
	               },
	               content:'<div class="margin_b10 text-center f18"><i class="fa fa-fw fa-exclamation-circle text-orange vertical-mid" style="font-size:38px;"></i><span class="inline-block vertical-middle">确定删除？</span></div>'
	           });
		});
</script>
</body>
</html>
