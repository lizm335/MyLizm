<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>短息通知详情</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="${ctx }/admin/messageInfo/list"">短信通知</a></li>
		<li class="active">短信通知详情</li>
	</ol>
</section>

<section class="content">
	<div class="bg-white">
    	<ul class="nav nav-tabs bg-f2f2f2" data-role="top-nav">
	        <li class="active">
	          <a class="flat gray no-margin" href="#tab_top_1" data-toggle="tab" style="    border-left-color: transparent;">详情</a>
	        </li>
	        <li>
	          <a class="flat gray no-margin" href="${ctx }/admin/mobileMessage/queryPutListById?mobileMessageId=${info.id}" >统计</a>
	        </li>
	    </ul>
	    <div class="tab-content">
	    	<div class="tab-pane active" id="tab_top_1">
				<article class="article" style="padding:40px 0;">
					<div class="row">
						<div class="col-xs-10 col-xs-offset-1">
					       	  <table class="table table-bordered vertical-middle margin_t30">
					        	<colgroup>
					        		<col width="150"></col>
					        		<col></col>
					        	</colgroup>
					        	<tbody>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">类型</td>
					        			<td>${typeMap[info.type] }</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">签名</td>
					        			<td>${signatureMap[info. signature]}</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">内容</td>
					        			<td>${info.content}</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">发布人</td>
					        			<td>${info.createUsername}（${info.createRoleName }）</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">发布时间</td>
					        			<td><fmt:formatDate value="${info.createdDt }" type="both"/></td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">发送条数</td>
					        			<td>${info.sendCount }</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">发送成功/失败条数</td>
					        			<td>
					        				<a href="${ctx }/admin/messageInfo/queryPutListById?mobileMessageId=${info.id}" class="text-underline" >${info.sendSuccessCount }/${info.sendFailedCount }</a>
					        			</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">费用</td>
					        			<td>￥ ${info.sendCount*0.05 }</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">接收</td>
					        			<td>
					        				<table class="c-tble vertical-middle" width="100%">
								    			<colgroup>
								    				<col width="95">
								    				<col>
								    			</colgroup>
								    			<tbody>
								    				<c:forEach items="${searchList }" var="searchList">
								    				<tr>
								    					<td>
								    						${searchList.conditionName }
								    					</td>
								    					<td>
								    						<div class="item-sel-box clearfix">
															  <c:forEach items="${fn:split(searchList.conditionContent, ',')}" var="str">
							    								<span class="btn btn-sm btn-default">
														    		${str }	
														  		</span>
							    							</c:forEach>
															</div>
								    					</td>
								    				</tr>
								    				</c:forEach>
								    			</tbody>
								    		</table>
					        			</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">状态</td>
					        			<td>
					        				
					        				<c:if test="${info.auditStatus eq '0'}">
												<span class="text-orange">待审核</span>
											</c:if>
											
											<c:if test="${info.auditStatus eq '1'}">
												<span class="text-green">已发送</span>
											</c:if>
											
											<c:if test="${info.auditStatus eq '2'}">
											<span class="text-red">审核不通过</span>
											</c:if>
					        				
					        			</td>
					        		</tr>
					        		
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">审核</td>
					        			<td>
					        				<div class="approval-list full-width margin_b20 pad-r15 clearfix">
								              <!-- 状态2 -->
								              <c:forEach items="${auditList }" var="audit" varStatus="var">
									              <c:if test="${not empty audit.submitUserName }">
									              	<dl class="approval-item">
									                    <dt class="clearfix">
									                      <b class="a-tit gray6">${audit.submitRoleName}：${audit.submitUserName }</b>
									                      <span class="gray9 text-no-bold f12"><fmt:formatDate value="${audit.createdDt }" type="both"/> 
									                     	<c:if test="${var.first }">
									                     	 发布
									                     	 </c:if>
									                      </span>
									                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
									                      <c:if test="${var.first }">
									                     	 <label class="state-lb">发布通知</label>
									                      </c:if>
									                      <c:if test="${!var.first }">
									                      	 <label class="state-lb">重新发布通知</label>
									                      </c:if>
									                    </dt>
									              </dl>
									              </c:if>
									                <c:if test="${not empty audit.auditUserName }">
										               <dl class="approval-item">
										                  <dt class="clearfix">
										                    <b class="a-tit gray6">${audit.auditRoleName }审核</b>
										                    <span class="gray9 text-no-bold f12"><fmt:formatDate value="${audit.auditTime }" type="both"/> </span>
										                    <c:if test="${audit.auditStatus eq '2' }">
										                    	<span class="fa fa-fw fa-times-circle text-red"></span>
										                   		 <label class="state-lb text-red">审核不通过</label>
										                    </c:if>
										                     <c:if test="${audit.auditStatus eq '1' }">
										                     	<span class="fa fa-fw fa-check-circle text-green"></span>
										                   		 <label class="state-lb text-green">审核通过</label>
										                    </c:if>
										                  </dt>
										                  <dd>
										                    <div class="txt">
										                        <div>${audit.auditContent }</div>
										                        <div class="gray9 text-right">审批人：${audit.auditUserName }</div>
										                        <i class="arrow-top"></i>
										                    </div>
										                  </dd>
										              </dl>
										              </c:if>
								              </c:forEach>
								             	
											<c:if test="${info.auditStatus ne '1' and isDean}">
											<input type ="hidden" value="${info.id}" id="messageId">
								              <dl class="approval-item white-border">
								                  <dt class="clearfix">
								                    <b class="a-tit gray6">院长审核</b>
								                    <span class="fa fa-fw fa-dot-circle-o text-yellow"></span>
								                    <label class="state-lb pending-state">待审核</label>
								                  </dt>
								                  <dd>
								                    <form class="theform">
								                      <div class="col-xs-12 no-padding position-relative">
								                        <textarea class="form-control" name="content" rows="3" placeholder="请输入审批评语或重交、不通过的原因和指引"></textarea>
								                      </div>
								                      <div>
								                      	<button type="button" data-id="1" class="btn min-width-90px btn-success margin_r10 margin_t10" data-role="btnSubmit">审核通过</button>
								                        <button type="button"  data-id="2" class="btn min-width-90px btn-warning margin_t10 btn-save-edit" data-role="btnSubmit">审核不通过</button>
								                      </div>
								                    </form>
								                  </dd>
								              </dl>
								              </c:if>
								            </div>
					        			</td>
					        		</tr>
					        		
					        		
					        		
					        		
					        	</tbody>
					        </table>
			        	</div>
			        </div>
				</article>
	    	</div>
	    </div>
    </div>
</section>

<!-- 底部 -->
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
	$('[data-role="btnSubmit"]').click(function(event){
		var auditStatus=$(this).data('id');
		var messageId=$('#messageId').val();
		var content=$('[name="content"]').val();
		if(content.length<1){
			if(auditStatus==2){
				alert('请填写审核不通过的原因！');
				return false;
			}else{
				content='审核通过';
			}
		}
		
		$.post('${ctx}/admin/mobileMessage/updateAudit',{messageId:messageId,content:content,auditStatus:auditStatus},function(data){
				if(!data.successful){
					alert(data.message);
				}else{
					location.reload();
				}
		},'json');
	});
</script>
</body>
</html>

