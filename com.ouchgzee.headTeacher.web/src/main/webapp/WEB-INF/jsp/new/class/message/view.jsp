<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
  <title>班主任平台 - 通知公告</title>
  <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
  <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
    <li><a href="#">班级服务</a></li>
    <li><a href="${ctx}/home/class/message/${param.source==1?'list':'publishList'}">通知公告</a></li>
    <li class="active">通知公告详情</li>
  </ol>
</section>
<section class="content">
  <div class="box margin-bottom-none">
    <div class="box-body pad20">
      <article class="article">
        <h1 class="article-title">【${infoTypeMap[info.infoType]}】${info.infoTheme}</h1>
          <section class="txt-info margin_t20">
          	${info.infoContent }
         </section>
        <c:if test="${not empty info.fileUrl  }">
			<div class="addon margin_t20">
	         <span>附件：</span>
	          <a href="${info.fileUrl }" target="_blank">
	          	<i class="fa fa-fw fa-download"></i>
	         		 ${info.fileName }
	          </a>
	        </div>
        </c:if>
        <div class="clearfix margin_t20">
          <ul class="list-unstyled">
            <li>
              发送人：${info.createdBy }
            </li>
            <li>
              发送时间：<fmt:formatDate value="${info.createdDt }" pattern="yyyy-MM-dd HH:mm:ss"/>
            </li>
            <li>
            	<c:if test="${param.source=='2' }">
              		已读/未读：<a href="javaScript:;" class="text-underline" data-id="${info.messageId }" data-name="more-note">${readCount }/${putCount }</a>
              	</c:if>
            </li>
            <li class="clearfix">
              <div class="pull-left">接收对象：${info.getUserRole }</div>
              <!-- <div class="oh">
                <span class="stu_container" data-list="梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国,梁志斌,黄晓明,李晓国">梁志斌、黄晓明、李晓国</span>
                <span class="omit_txt">
                    等<em>15</em>人
                </span>
                <a href="javascript:void(0)" class="extra_oper text-underline">展开&gt;</a>
              </div> -->
            </li>
            <li>
				<c:if test="${param.source=='2' }">
					<c:if test="${info.isStick }">
						置顶有效时长:  <fmt:formatDate value="${info.effectiveTime }" pattern="yyyy-MM-dd "/>
					</c:if>
				</c:if>
			</li>
          </ul>
        </div>
      </article>
    </div>
  </div>
</section>
<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">

//已读/未读
$('[data-name="more-note"]').click(function(event) {
	event.preventDefault();
	var id=$(this).data('id');
	$.mydialog({
	  id:'more-note',
	  width:1000,
	  height:600,
	  zIndex:11000,
	  content: 'url:${ctx}/home/class/message/queryPutListById.html?id='+id
	});
});

</script>
</body>
</html>
