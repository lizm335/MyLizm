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
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="${ctx }/admin/messageInfo/list"">通知公告</a></li>
		<li class="active">通知详情</li>
	</ol>
</section>

<section class="content">
	<div class="bg-white">
    	<ul class="nav nav-tabs bg-f2f2f2" data-role="top-nav">
	        <li class="active">
	          <a class="flat gray no-margin" href="#tab_top_1" data-toggle="tab" style="    border-left-color: transparent;">详情</a>
	        </li>
	        <li>
	          <a class="flat gray no-margin" href="${ctx }/admin/messageInfo/queryPutListById?id=${gjtMessageInfo.messageId}" >统计</a>
	        </li>
	        <li>
	          <a class="flat gray no-margin" href="${ctx }/admin/messageInfo/commentList?id=${gjtMessageInfo.messageId}" >评论</a>
	        </li>
	        <li>
	          <a class="flat gray no-margin" href="${ctx }/admin/messageInfo/likeList?id=${gjtMessageInfo.messageId}" >点赞</a>
	        </li>
	        <li>
	          <a class="flat gray no-margin" href="${ctx }/admin/messageInfo/feedbackList?id=${gjtMessageInfo.messageId}" >反馈</a>
	        </li>
	    </ul>
	    <div class="tab-content">
	    	<div class="tab-pane active" id="tab_top_1">
				<article class="article" style="padding:40px 0;">
					<div class="row">
						<div class="col-xs-10 col-xs-offset-1">
							<h1 class="text-center f24" style="margin-top:0;">
								 <c:if test="${isStick }"><small class="label label-warning text-no-bold f16">置顶 </small></c:if> 
								 ${gjtMessageInfo.infoTheme }</h1>
					        <section class="txt-info pad-t15 clearfix">
								${gjtMessageInfo.infoContent }
							</section>
							
							<c:if test="${not empty gjtMessageInfo.fileUrl  }">
								<div class="addon margin_t20">
						         <span>附件：</span>
						          <a href="${gjtMessageInfo.fileUrl }" target="_blank">
						          	<i class="fa fa-fw fa-download"></i>
						         		 ${gjtMessageInfo.fileName }
						          </a>
						        </div>
					        </c:if>

					       	  <table class="table table-bordered vertical-middle margin_t30">
					        	<colgroup>
					        		<col width="150"></col>
					        		<col></col>
					        	</colgroup>
					        	<tbody>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">类型</td>
					        			<td>${infoTypeMap[gjtMessageInfo.infoType] }</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">分类</td>
					        			<td>${gjtMessageInfo.gjtMessageClassify.name }</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">发布人</td>
					        			<td>${gjtMessageInfo.createdBy}（${gjtMessageInfo.createRoleName }）</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">发布时间</td>
					        			<td><fmt:formatDate value="${gjtMessageInfo.createdDt }" type="both"/></td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">已读/未读</td>
					        			<td>
					        				<a href="${ctx }/admin/messageInfo/queryPutListById?id=${gjtMessageInfo.messageId}" class="text-underline" >${readCount }/${putCount }</a>
					        			</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">接收</td>
					        			<td class="no-padding">
					        				<div style="margin:-1px;">
						        				<table class="table table-bordered margin-bottom-none vertical-middle">
						        					<colgroup>
						        						<col width="100"></col>
						        						<col></col>
						        					</colgroup>
						        					<tbody>
						        						<c:forEach items="${putObjectList }" var="putObject">
						        								<tr>
								        							<td align="center">${putObject.name }</td>
								        							<td class="no-padding">
								        								<div style="padding:10px 0;">
									        								<table class="c-tble vertical-middle" width="100%">
																    			<colgroup>
																    				<col width="100"></col>
																    			</colgroup>
																    			<tbody>
																    				<c:forEach items="${putObject.gjtMessageSearchs }" var="gjtMessageSearch">
																    					<tr>
																    					<td>
																    						${gjtMessageSearch.conditionName }：
																    					</td>
																    					<td>
																    						<div class="item-sel-box clearfix">
																    							<c:forEach items="${fn:split(gjtMessageSearch.conditionContent, ',')}" var="str">
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
															    		</div>
								        							</td>
								        						</tr>
						        						</c:forEach>
						        					</tbody>
						        				</table>
					        				</div>
					        			</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">是否置顶</td>
					        			<td>
					        				<c:if test="${isStick }">置顶（有效时长:  <fmt:formatDate value="${gjtMessageInfo.effectiveTime }" pattern="yyyy-MM-dd "/>）</c:if>
					        				<c:if test="${isStick eq false}">否</c:if>
					        			</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">重要程度</td>
					        			<td>
					        				${gjtMessageInfo.degree eq '0'?'一般':'重要'}
					        			</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">APP推送</td>
					        			<td>
					        				${gjtMessageInfo.appSend eq '0'?'不推送':'推送到首页' }
					        			</td>
					        		</tr>
					        		<tr>
					        			<td class="bg-f2f2f2 text-center">开放功能</td>
					        			<td>
					        				<c:if test="${gjtMessageInfo.isComment eq '1'}">评论、</c:if>
					        				<c:if test="${gjtMessageInfo.isLike eq '1'}">点赞、</c:if>
					        				<c:if test="${gjtMessageInfo.isFeedback eq '1'}">反馈</c:if>
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

</body>
</html>

