<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">转给其他人回答</h3>
			</div>
			<div class="scroll-box">
				<div class="box-body">
					<form id="listForm" action="findAnswerUser" method="get">
					<div class="row">
						<div class="col-xs-3">
							<select name="roleId" class="select2 show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="">全部角色</option>
								<c:forEach items="${roleMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param.roleId}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-xs-3 ">
							<input  type="text" class="form-control" name="userName" placeholder="搜索姓名" value="${param.userName }">
							<input type="hidden" name="studentId" id="studentId" value="${studentId }">
						</div>
						<div class="col-xs-3 no-padding">
							<input  type="text" class="form-control" name="className" placeholder="搜索班级名称" value="${param.className }">
						</div>
						
						<div class="col-xs-2">
							<button class="btn btn-block btn-primary">搜索</button>
						</div>
					</div>
					</form>
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
						                                "teachId":"${map.TERMCOURSE_ID }",
						                                "account":"${map.LOGIN_ACCOUNT }",
						                                "name":"${map.REAL_NAME }",
						                                "role":"${map.ROLE_NAME }",
						                                "course":"${map.KCMC}"
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
		<div class="text-right pop-btn-box pad clearfix">
		<%-- <c:if test="${zhuanfaId ne  chushiId  }">
			<button type="button" class="btn btn-primary min-width-90px pull-left" data-role="transfer-orient" data-id="${chushiId }">转回原处（${chushiName }班主任）</button>
		</c:if> --%>
			<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
			<button type="button" class="btn btn-primary min-width-90px" data-role="sure">确定</button>
		</div>


	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

	<script type="text/javascript">
	
	$('.select2').select2();
	
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
	    if($ck.length>0){
	        var $container=parent.$('[data-id="object-box"]');
	        var obj=$ck.data('json');
	        var template='<tr><td>{1} <input type ="hidden" name="answerUserId" value="{0}" id="answerUserId">'
	        				+'<input type ="hidden" name="classId" value="{2}"><input type ="hidden" name="teachId" value="{3}">'
	        				+'</td><td><span style="word-break: break-all;"> {4} </span></td><td>{5}</td>'
	        				+' <td>{6}</td>  <td><a href="#" data-role="remove">删除</a></td> </tr>';

	        $container.html(
	            template.format(
	                obj.id,
	                obj.name,
	                obj.classId,
	                obj.teachId,
	                obj.name,
	                obj.account,
	                obj.role,
	                obj.course
	            )
	        );

	        parent.$.closeDialog(frameElement.api);
	        
	    }
	    else{
	        parent.$.closeDialog(frameElement.api);
	    }    
	    
	});

	</script>
</body>
</html>
