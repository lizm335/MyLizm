<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 服务记录</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		服务记录详情
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="${ctx}/home/class/serviceManager/list">服务记录</a></li>
		<li class="active">服务记录详情</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

	<div class="box margin-bottom-none">
		<div class="box-header with-border record-heading">


			<div class="pad15">
                <h1 class="article-title text-left2">
                    ${info.title}
				</h1>
				<div class="article-info2 margin_b15">
					<div>
						状态：${info.status=='1'?'已结束':'未结束'} <br>
						已耗时：
                        <c:if test="${info.totaltime/60/60 >= 1}">
                            <fmt:formatNumber value="${info.totaltime/60/60-0.49}" maxFractionDigits="0" />小时
                        </c:if>
						<fmt:formatNumber value="${info.totaltime/60%60}" maxFractionDigits="0" />分钟
					</div>
					<div class="clearfix gray9 f12 more-per-wrap">
						<div class="pull-left">服务对象：</div>
						<div class="oh">
                              <span class="stu_container" data-list="<c:forEach var='item' items='${info.gjtStudentInfoList}' varStatus='i'> ${item.xm} <c:if test='${ i.index lt fn:length(info.gjtStudentInfoList)}'>, </c:if></c:forEach>">
                                <c:forEach var='item' items='${info.gjtStudentInfoList}' varStatus='i'>
									<c:if test='i.index lt 3'>
										${item.xm}<c:if test='${i.index lt 2}'>, </c:if>
									</c:if>
								</c:forEach>
                              </span>
							  <span class="omit_txt">
                                  等<em> ${fn:length(info.gjtStudentInfoList)} </em>人
                              </span>
							<a href="javascript:void(0)" class="extra_oper">展开&gt;</a>
						</div>
					</div>
				</div>
			</div>
			<div class="clearfix margin_l15 margin_r15">
				<h3 class="box-title text-bold pad-t10">历史服务记录</h3>
			    <c:if test="${info.status != '1'}">
                    <div class="pull-right">
                        <button type="button" class="btn btn-primary btn-sm service-record">添加记录</button>
                        <button type="button" class="btn btn-default btn-sm margin_l10" onclick="window.location.href='${ctx}/home/class/serviceManager/over?id=${id}';">结束服务</button>
                    </div>
				</c:if>
			</div>
		</div>
		<div class="box-body">
			<ul class="list-unstyled margin_l15 margin_r15 record-list">
			<c:choose>
				<c:when test="${not empty info.gjtServiceRecordList && fn:length(info.gjtServiceRecordList) > 0}">
					<c:forEach items="${info.gjtServiceRecordList}" var="item">
					<li>
						<header>
							<div class="row">
								<div class="col-sm-4 col-md-3 record-start-time">
									开始时间:<fmt:formatDate value="${item.starttime}" pattern="yyyy-MM-dd HH:mm" />
								</div>
								<div class="col-sm-4 col-md-3 record-end-time">
									结束时间:<fmt:formatDate value="${item.endtime}" pattern="yyyy-MM-dd HH:mm" />
								</div>
								<div class="col-sm-3 col-md-3 record-tel">
									方式:<dic:getLabel typeCode="ServiceMethod" code="${item.way}" />
								</div>
							</div>
						</header>
						<div class="txt gray6">
							${item.content}
						</div>
					</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li align="center">
						暂无数据
					</li>
				</c:otherwise>
			</c:choose>
			</ul>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
	/*添加记录*/
	$(".service-record").click(function(event) {
		var path=encodeURI('${ctx }/home/class/serviceManager/toAddRecord?id=${id}');
		$.dialog({
			title: '<br/><b class="f18">  添加记录</b>',
			contentHeight:500,
			iframeId:'record-pop-iframe',
			content:'url:'+path,
			contentLoaded:function(){

			},
			onOpen:function(){

			},
			backgroundDismiss: true,
			animation: 'top',
			columnClass: 'col-md-7 col-md-offset-3 reset-jconfirm',
			closeAnimation: 'bottom'
		});
		return false;
	});
    //“接收人”操作
    $(".stu_container").each(function(index,ele){
        var container=$(this),
                stu_list=(container.attr("data-list")).split(",");
        if(stu_list.length>0){
            var strPrev3=stu_list.slice(0,3).join("、");
            container.html(strPrev3);
            container.siblings(".omit_txt").children("em").html(stu_list.length);

            if(stu_list.length<=3){
                container.siblings().hide();
            }
            else{
                container.siblings(".extra_oper").click(function(event) {
                    if(!$(this).hasClass('on')){
                        container.html(stu_list.join("、"));
                        $(this).html("收起");
                        $(this).addClass('on');
                        $(this).siblings(".omit_txt").hide();
                    }
                    else{
                        container.html(strPrev3);
                        $(this).html("展开>");
                        $(this).removeClass('on');
                        $(this).siblings(".omit_txt").show();
                    }
                });
            }
        }
    });

</script>
</body>
</html>
