<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<style type="text/css">
html, body {
	overflow: hidden;
	height: 100%;
}
</style>
</head>
<body>
	<div class="box no-border no-shadow margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title">答疑详情</h3>
		</div>
		<div class="box-body scroll-box ">
			<div class="nav-tabs-custom no-margin no-shadow">
				<ul class="nav nav-tabs">
					<li <c:if test="${param.type eq 'N'}"> class="active"</c:if>>
						<a href="${ctx}/lcmsubject/homeList.html?type=N&userId=${userId}">待回答（${noSolve}）</a>
					</li>
					<li <c:if test="${param.type eq 'Y'}"> class="active"</c:if>>
						<a href="${ctx}/lcmsubject/homeList.html?type=Y&userId=${userId}"" >已解答（${yesSolve}）</a>
					</li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="pane1">
						<div class="box no-border no-shadow no-margin">
							<div class="box-header with-border no-pad-left no-pad-right">
								<div class="form-inline">
									<div class="form-group">
										<select class="form-control margin_r5 select2" id="isTimeout">
											<option value="">全部</option>
											<option value="24" <c:if test="${isTimeout eq '24' }">selected</c:if>>
											超过24小时
											<c:if test="${param.type eq 'N'}">未</c:if>
											解答</option>
										</select>
									</div>
									<div class="form-group margin_r5">
										<input type="text" class="form-control" placeholder="输入标题的关键字查询" id="search_LIKE_subjectTitle" value="${param.search_LIKE_subjectTitle}">
									</div>
									<button type="button" class="btn btn-primary" id="btnSearch">搜索</button>
								</div>
							</div>

							<div class="box-body no-padding">
								<ul class="list-unstyled news-list clearfix">
									<c:choose>
										<c:when test="${not empty pageInfo && pageInfo.numberOfElements > 0}">
											<c:forEach items="${pageInfo.content}" var="info">
												<c:if test="${not empty info}">
													<li class="news-item question-item">
														<div class="media">
															<div class="media-left">
																<c:choose>
																	<c:when test="${not empty info.avatar}">
																		<img src="${info.avatar}" class="media-object img-circle" alt="User Image"
																			onerror="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png'" />
																	</c:when>
																	<c:otherwise>
																		<img src="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="media-object img-circle" alt="User Image" />
																	</c:otherwise>
																</c:choose>
															</div><!-- media-left -->
															<div class="media-body">
																<div class="the-topic-ask">
																	<h4 class="media-heading text-yellow text-bold f16">
																		<span class="label label-warning margin_r10">问</span>${info.title}
																	</h4>
																	<div class="q-info">${info.studentXm}&nbsp;&nbsp; ${info.createDt} &nbsp; &nbsp; 提问</div>
																	<div class="txt">${info.content}</div>
																	<c:if test="${not empty info.imgUrls }">
																		<ul class="list-inline img-gallery">
																			<c:forEach items="${info.imgUrls }" var="item">
																				<li>
																					<img src="${item }"   data-role="lightbox" role="button">
																				</li>
																			</c:forEach>
																		</ul>
																	</c:if>
																</div>
																<c:if test="${param.type eq 'Y' }">
																	<c:if test="${not empty info.replyList }">
																		<c:forEach items="${info.replyList }" var="item" varStatus="var">
																			<c:choose>
																				<c:when test="${var.first}">
																					<div class="the-topic-ans margin_t20">
																						<div>
																							<span class="label label-primary margin_r10">答</span>
																							<span>${item.teacherName }</span>
																							<span class="gray9"> ${item.teacherType }&nbsp; &nbsp; ${item.createDt}</span>
																						</div>
																						<div class="txt">${item.content }</div>
																						<c:if test="${not empty item.imgUrls }">
																							<ul class="list-inline img-gallery">
																								<c:forEach items="${item.imgUrls }" var="teacherPicture">
																									<li>
																										<img src="${teacherPicture }"   data-role="lightbox" role="button">
																									</li>
																								</c:forEach>
																							</ul>
																						</c:if>
																					</div>
																				</c:when>
																			</c:choose>
																		</c:forEach>
																	</c:if>
																</c:if>
															</div><!-- media-body -->
														</div><!-- media -->
													</li><!-- 每一条 -->
												</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<li class="news-item question-item" style="text-align: center;">暂无数据</li>
										</c:otherwise>
									</c:choose>
								</ul>
								<div class="page-container margin_t15 margin_b10 clearfix">
									<form class="form-horizontal" id="listForm">
										<input type="hidden" name="type" value="${param.type}"> <input type="hidden" name="userId" value="${userId}">
										 <input type="hidden" name="search_LIKE_subjectTitle" id="subjectTitle" value="${param.search_LIKE_subjectTitle}">
										 <input type="hidden" name="isTimeout" id="searchisTimeout" value="${isTimeout}">
										<tags:pagination page="${pageInfo}" paginationSize="10" />
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="text-right pop-btn-box pad">
		<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
	</div>
<script type="text/javascript">
$(".box-body").height($(window).height()-126);
//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//简略文字的控制
function summaryControl(){
    $('.question-item .txt').each(function(index, ele) {
      ele=$(ele);
      if(ele.height()>40&&ele.children(".dotdot").length<=0){ 
          ele.height(40).append( $("<span>",{
              "class":"dotdot",
              html:"...<em>[ + ]</em>"
          }) );
      }
    });
    
};

$(".nav-tabs").on('shown.bs.tab', 'a', function(event) {
  event.preventDefault();
  summaryControl();
});

$(window).on('resize load',function(event) {
  summaryControl();
});

$(".question-item").on("click",'.dotdot',function(){
    if($(this).parent().height()==40){
        $(this).html("<em>[ - ]</em>").parent().css("height","auto");
    }
    else{
        $(this).html("...<em>[ + ]</em>").parent().height(40)
    }
});

//表单搜索
$('#btnSearch').click(function(){
	$('#subjectTitle').val($('#search_LIKE_subjectTitle').val());
	$('#searchisTimeout').val($('#isTimeout').val());
	$('#page').val(1);
	$('#listForm').submit();
});

</script>
</body>
</html>
