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
		<li><a href="${ctx }/admin/teachMessageInfo/list"">通知公告</a></li>
		<li class="active">通知详情</li>
	</ol>
</section>
<section class="content">
	<div class="box no-margin">
		<div class="box-body pad20">
			<article class="article">
				<h1 class="article-title">【${infoTypeMap[gjtMessageInfo.infoType]}】${gjtMessageInfo.infoTheme }</h1>
		        <section class="txt-info margin_t20">
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
		        <div class="clearfix margin_t20">
		          <ul class="list-unstyled">
		            <li>
		              发送人：${gjtMessageInfo.createdBy }
		            </li>
		            <li>
		              发送时间：<fmt:formatDate value="${gjtMessageInfo.createdDt }" pattern="yyyy-MM-dd HH:mm:ss"/>
		            </li>
		            <li>
		             	<c:if test="${view }">
			        			已读/未读：<a href="javaScript:;" class="text-underline" data-id="${gjtMessageInfo.messageId }" data-name="more-note">${readCount }/${putCount }</a>
		        		</c:if>
		            </li>
		            <li>
		              <%-- 接收身份：${gjtMessageInfo.getUserRole } --%>
		              
		              <div class="media">
						<div class="media-left text-nowrap no-pad-right"> 接收身份：</div>
						<div class="media-body" data-cache="${gjtMessageInfo.getUserRole }">
							<!--
               黄小米（教务管理员）、张晓明（教学管理员）
               <a href="#" data-role="see-more" class="text-underline f12">展开&gt;&gt;</a>
               -->
						</div>
					</div>
		            </li>
		            <li>
						<c:if test="${isStick }">
							置顶有效时长:  <fmt:formatDate value="${gjtMessageInfo.effectiveTime }" pattern="yyyy-MM-dd "/>
						</c:if>
		            </li>
		          </ul>
		        </div>
			</article>
		</div>
	</div>
</section>
<!-- 底部 -->
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

//已读/未读
$('[data-name="more-note"]').click(function(event) {
	event.preventDefault();
	var id=$(this).data('id');
	$.mydialog({
	  id:'more-note',
	  width:1000,
	  height:700,
	  zIndex:11000,
	  content: 'url:${ctx}/admin/teachMessageInfo/queryPutListById.html?id='+id
	});
});

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

</script>
</body>
</html>
