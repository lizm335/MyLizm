<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<form id="listForm" action="findRole" method="get">
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">转给其他人回答</h3>
			</div>
			<div class="scroll-box">
				<div class="box-body">
					<div class="row">
						<div class="col-xs-4">
							<select name="roleId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="">全部角色</option>
								<c:forEach items="${roleMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param.roleId}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-xs-6 no-padding">
							<input  type="text" class="form-control" name="userName" placeholder="搜索姓名" value="${param.userName }">
							<input type="hidden" name="id" id="subId" value="${subjectId }">
							<input type="hidden" name="studentId" id="studentId" value="${studentId }">
						</div>
						<div class="col-xs-2">
							<button class="btn btn-block btn-primary">搜索</button>
						</div>
					</div>
					<table class="table-gray-th text-center margin_t15">
						<thead>
							<tr>
								<th width="60">选择</th>
								<th>姓名</th>
								<th>帐号</th>
								<th>角色</th>
								<th>课程班级</th>
								<th>任教课程</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty page && page.numberOfElements > 0}">
									<c:forEach items="${page.content}" var="map">
										<c:if test="${not empty map}">
											<tr>
												<td><input type="radio" name="r1" data-id="sel-item"
													data-json='{
						                                "id":"${map.ID }",
						                                "classId":"${map.CLASS_ID }",
						                                "teachId":"${map.TEACH_PLAN_ID }",
						                                "name":"${map.REAL_NAME }",
						                                "role":"${map.ROLE_NAME }",
						                                "roleId":"${map.ROLE_ID}"
						                            }'>
												</td>
												<td>${map.REAL_NAME }</td>
												<td>${map.LOGIN_ACCOUNT }</td>
												<td>${map.ROLE_NAME }</td>
												<td>${map.BJMC }</td>
												<td>${map.KCMC }</td>
											</tr>
										</c:if>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr  style="text-align: center;">
										<td colspan="6">暂无数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
				<div style="padding: 10px 0 10px 0;">
					<tags:pagination page="${page}" paginationSize="10" />
				</div>
			</div>
		</div>
	</form>
	<div class="text-right pop-btn-box pad clearfix">
	<c:if test="${zhuanfaId ne  chushiId  }">
		<button type="button" class="btn btn-primary min-width-90px pull-left" data-role="transfer-orient" data-id="${chushiId }">转回原处（${chushiName }班主任）</button>
	</c:if>
		<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
		<button type="button" class="btn btn-primary min-width-90px" data-role="sure">确定</button>
	</div>

	<script type="text/javascript">
	/*不需要置顶按钮*/
	$('.jump-top').remove();
	//关闭 弹窗
	$("[data-role='close-pop']").click(function(event) {
		parent.$.closeDialog(frameElement.api)
	});

	//设置内容主体高度
	$('.scroll-box').height($(frameElement).height()-106);

	
	//确定
	$('[data-role="sure"]').click(function(event) {
	    var $ck=$('[data-id="sel-item"]:checked');
	    var subId=$('#subId').val();
	    if($ck.length>0){
	        var obj=$ck.data('json');
	        $.alertDialog({
	            id:'transfer',
	            width:400,
	            height:280,
	            zIndex:11000,
	            ok:function(){//“确定”按钮的回调方法
	                //这里 this 指向弹窗对象
	                //$.closeDialog(this);
	                var dialog1=$.formOperTipsDialog({
	                  text:'数据提交中...',
	                  iconClass:'fa-refresh fa-spin'
	                });
	                $.post('updateForward.html',{id:subId,zhuanfaId:obj.id,classId:obj.classId,teachId:obj.teachId,roleId:obj.roleId},function(data){
	                	dialog1.find('[data-role="tips-text"]').html(data.message);
                        dialog1.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
	                      /*关闭弹窗*/
	                      setTimeout(function(){
	                        parent.location.href=parent.location.href;
	                      },1000)
	                },'json')
	            },
	            content:[
	              '<div class="text-center">',
	                  '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
	                  '<span class="f16 inline-block vertical-mid text-left">',
	                    '确定将该疑问转给？',
	                    '<div class="gray9 margin_t5">'+obj.name+'（'+obj.role+'）</div>',
	                  '</span>',

	              '</div>'].join('')
	        });
	    }
	    else{
	        alert('请选择一名用户');
	    }    
	});

	//转回原处
	$('[data-role="transfer-orient"]').click(function(event) {
		var $chushiId=$(this).data('id');
		 var subId=$('#subId').val();
	    $.alertDialog({
	        id:'transfer',
	        width:400,
	        height:280,
	        zIndex:11000,
	        ok:function(){//“确定”按钮的回调方法
	            var dialog1=$.formOperTipsDialog({
	              text:'数据提交中...',
	              iconClass:'fa-refresh fa-spin'
	            });
	            $.post('updateInitial.html',{id:subId,chushiId:$chushiId},function(data){
                	//setTimeout(function(){//此句模拟交互，程序时请去掉
                		dialog1.find('[data-role="tips-text"]').html(data.message);
                   		dialog1.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
                      /*关闭弹窗*/
                      setTimeout(function(){
                        parent.location.href=parent.location.href;
                      },1000)
                    //},2000);//此句模拟交互，程序时请去掉
                },'json');
	        },
	        content:[
	          '<div class="text-center">',
	              '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
	              '<span class="f16 inline-block vertical-mid text-left">',
	                '确定将该疑问转回原处？',
	                '<div class="gray9 margin_t5">${chushiName}（班主任）</div>',
	              '</span>',
	          '</div>'].join('')
	    });
	});


	</script>
</body>
</html>
