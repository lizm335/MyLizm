<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<head>
<html class="reset"><meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>添加对象</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<body>
	<form id="listForm" action="findRole">
		<input type="hidden" name="type" value="${type }">
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">添加指派对象</h3>
			</div>
			<div class="scroll-box">
				<div class="box-body">
					<div class="row">
						<div class="col-xs-4">
							<select name="search_EQ_priRoleInfo.roleId" class="form-control selectpicker" data-size="6" data-live-search="true">
								<option value="">全部角色</option>
								<c:forEach items="${rolesMap }" var="map"> 
									<option value="${map.key }" <c:if test="${param['search_EQ_priRoleInfo.roleId'] eq map.key}">selected</c:if>>${map.value }</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-xs-6 no-padding">
							<input class="form-control" placeholder="搜索姓名" name="search_LIKE_realName" value="${param.search_LIKE_realName}">
						</div>
						<div class="col-xs-2">
							<button type="submit" class="btn btn-block btn-primary">搜索</button>
						</div>
					</div>
					<table class="table-gray-th text-center margin_t15">
						<thead>
							<tr>
								<th width="60">选择</th>
								<th>姓名</th>
								<th>帐号</th>
								<th>角色</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty pageInfo.content}">
									<c:forEach items="${pageInfo.content}" var="info">
										<tr>
											<td>
												<input type="checkbox" name="r1" data-id="sel-item"data-json='{ 
													"id":"${info.id }",
					                                "name":"${info.realName }",
					                                "username":"${info.loginAccount }",
					                                "role":"${info.roleName }"
					                            	}'>
											</td>
											<td>${info.realName }</td>
											<td>${info.loginAccount }</td>
											<td>${info.roleName }</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td align="center" colspan="4">暂无数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<p></p>
					<tags:pagination page="${pageInfo}" paginationSize="5" />
				</div>
			</div>
		</div>
		<div class="text-right pop-btn-box pad">
			<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
			<button type="button" class="btn btn-success min-width-90px" data-role="sure">确定</button>
		</div>
	</form>



	<script type="text/javascript">
	//关闭 弹窗
	$("[data-role='close-pop']").click(function(event) {
		parent.$.closeDialog(frameElement.api)
	});

	//设置内容主体高度
	$('.scroll-box').height($(frameElement).height()-106);

	$('[data-id="sel-all"]').click(function(event) {
	    if($(this).prop('checked')){
	        $('[data-id="sel-item"]').prop('checked',true);
	    }
	    else{
	        $('[data-id="sel-item"]').prop('checked',false);
	    }
	});

	//确定
	$('[data-role="sure"]').click(function(event) {
	    var $ck=$('[data-id="sel-item"]:checked');
	    if($ck.length>0){
	        var htmlTemp=['<li>',
	                        '<span>{0}（{1}）</span>',
	                        '<i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>',
	                        '<input type="hidden" name="users" value="{2}">',
	                      '</li>'].join('');
	        var result=[];
	        $ck.each(function(index, el) {
	            var obj=$(this).data('json');
	            result.push(htmlTemp.format(obj.name,obj.role,obj.id))
	        });

	        var json=frameElement.data;
	        if(json.frame=='parent'){
	            parent.$(json.acceptBox).append(result.join(''));
	        }
	        else{
	            parent.$('iframe[name="Iframe-'+json.frame+'"]').contents().find(json.acceptBox).append(result.join(''));
	        }
	    }
	    parent.$.closeDialog(frameElement.api)
	});
	</script>


</body>
</html>
