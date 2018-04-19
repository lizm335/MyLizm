<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 班级活动</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		班级活动介绍
	</h1>
	<button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="${ctx}/home/class/activity/list">班级活动</a></li>
		<li class="active">班级活动介绍</li>
	</ol>
</section>
<section class="content">
	<div class="box">
	    <div class="box-body pad20">
	      <div class="media">
	        <div class="media-left pad-r20">
	          <img class="media-object" src="${info.publicityPicture}" style="width: 320px; height: 212px;">
	        </div>
	        <div class="media-body">
	          <h4 class="media-heading text-bold">
	            ${info.activityTitle}
	          </h4>
	          <div class="act-info-list">
	            <div class="table-block full-width">
	              <div class="table-cell-block">
	                <div>
	                  <span class="text-yellow"><c:if test="${info.chargeMoney == null}">
								0
						</c:if>
						<c:if test="${info.chargeMoney != null}">
								${info.chargeMoney}
						</c:if></span>
	                  <span class="f12">元/人</span>
	                </div>
	                <div class="txt">费用</div>
	              </div>
	              <div class="table-cell-block">
	                <div>
	                  <span class="text-yellow">${passNum}</span>
	                  <span class="f12">人</span>
	                </div>
	                <div class="txt">已报名</div>
	              </div>
	              <div class="table-cell-block">
	                <div>
	                  <span class="text-yellow">${info.ceilingNum}</span>
	                  <span class="f12">人</span>
	                </div>
	                <div class="txt">名额</div>
	              </div>
	              <div class="table-cell-block">
	                <div>
	                  <span class="text-yellow">
	                  	<c:if test="${info.joinNum == null}">
								${info.ceilingNum}
						</c:if>
						<c:if test="${info.joinNum != null}">
								${info.ceilingNum-passNum}
						</c:if></span>
	                  <span class="f12">人</span>
	                </div>
	                <div class="txt">剩余</div>
	              </div>
	            </div>
	          </div>
	          <div class="row">
	            <div class="col-xs-4 col-md-3">
	              <div class="gray9 pad-t5">时间：<fmt:formatDate value="${info.beginTime}" pattern="yyyy-MM-dd HH:mm" /></div>
	            </div>
	            <div class="col-xs-4 col-md-3">
	              <div class="gray9 pad-t5">地点：${info.activityAddress}</div>
	            </div>
	            <div class="col-xs-3 col-md-5 text-right">
	              <a href="${ctx}/home/class/activity/toUpdateActivity/${info.id}" role="button" class="btn btn-default">修改活动</a>
	            </div>
	          </div>
			</div>
		</div>		
	</div>
	</div>
	<div class="box no-border margin-bottom-none">
    <div class="nav-tabs-custom reset-nav-tabs-custom margin-bottom-none" id="notice-tabs">
      <ul class="nav nav-tabs">
        <li class="active"><a href="#tab_notice_1" data-toggle="tab">活动介绍</a></li>
        <li><a href="#tab_notice_2" data-toggle="tab">报名人员(${info.joinNum})</a></li>
        <li><a href="#tab_notice_3" data-toggle="tab">活动评论(${info.commentNum})</a></li>
      </ul>
      <div class="tab-content">
        <div class="tab-pane active" id="tab_notice_1">
          <div class="pad20 act-des">
            ${info.activityIntroduce}
          </div>
        </div><!-- /.tab-pane -->
        <div class="tab-pane" id="tab_notice_2">
          <div class="box box-primary">
             <div class="box-header with-border">
               <div class="pull-right"><button class="btn btn-primary f14 multi-auditWait">批量审核</button></div>
               <h3 class="box-title">
                 <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-fw fa-chevron-circle-right reset-fa-chevron-circle"></i></button>
                 <label>未审核</label>
                 <span>（${waiNum}人）</span>
               </h3>
             </div>
             <div class="box-body gray6">
	             <div class="row reg-user-list">
	             <c:choose>	        	
					<c:when test="${not empty waitList}">
						<c:forEach items="${waitList}" var="waitListItems">						
			             	<div class="col-sm-4 col-lg-3">
			                  <div class="panel panel-default">
			                    <div class="panel-body">
			                      <div class="media">
			                        <div class="media-left">
			                          <img src="${waitListItems.avatar}" class="media-object img-circle" alt="User Image">
			                        </div>
			                        <div class="media-body">
			                          <h4 class="media-heading">
			                           		${waitListItems.name} 
			                           		<c:if test="${waitListItems.sex eq '男'}"><i class="fa fa-fw fa-mars"></i></c:if>
			                            <c:if test="${waitListItems.sex eq '女'}"><i class="fa fa-fw fa-venus"></i></c:if>
			                          </h4>
			                          <div class="f12 gray9">
			                            <fmt:formatDate value="${waitListItems.joinDt}" pattern="yyyy-MM-dd HH:mm:ss"/>  报名
			                          </div>
			                        </div>
			                      </div>
			                    </div>
			                    <%-- <div class="panel-footer no-bg">
			                      <div class="table-block full-width">
			                        <div class="table-cell-block">
			                          	<a href="${ctx}/home/class/activity/auditUnpassByStudentId?activityId=${info.id}&auditStatus=0&studentId=${waitListItems.studentId}" class="btn btn-block text-light-blue">通过</a>
			                        </div>
			                        <div class="table-cell-block">
			                          <a href="${ctx}/home/class/activity/auditUnpassByStudentIdtoUnpass?activityId=${info.id}&auditStatus=0&studentId=${waitListItems.studentId}" class="btn btn-block text-light-blue">不通过</a>
			                        </div>
			                      </div>
			                    </div> --%>
			                    
			                    <div class="panel-footer no-bg">
			                        <div class="table-block full-width">
			                          <div class="table-cell-block">
			                            <a href="${ctx}/home/class/activity/auditUnpassByStudentId?activityId=${info.id}&auditStatus=0&studentId=${waitListItems.studentId}" 
			                            class="btn btn-block text-light-blue text-bold no-bg no-padding">通过</a>
			                          </div>
			                          <div class="table-cell-block">
			                            <a href="${ctx}/home/class/activity/auditUnpassByStudentIdtoUnpass?activityId=${info.id}&auditStatus=0&studentId=${waitListItems.studentId}"
			                             class="btn btn-block gray">不通过</a>
			                          </div>
			                        </div>
			                      </div>
			                  </div>
			                </div>
						   </c:forEach>
					    </c:when>
					</c:choose>
	             </div>
             </div><!-- /.box-body gray6 -->
             </div>
             <div class="box box-success">
	            <div class="box-header with-border">
	              <div class="pull-right">
		              <a href="${ctx}/home/class/activity/exportActivityNumber?activityId=${info.id}" target="_blank" class="btn btn-success f14">
		              	<i class="fa fa-fw fa-sign-out"></i> 导出活动人员
		              </a></div>
		              <h3 class="box-title">
		                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-fw fa-chevron-circle-right reset-fa-chevron-circle"></i></button>
		                <label>已通过</label>
		                <span>（${passNum}人）</span>
		              </h3>
	             </div>
	             <div class="box-body gray6">
	               <div class="row reg-user-list">
	               <c:choose>	        	
					<c:when test="${not empty passList}">
						<c:forEach items="${passList}" var="passListItems">
	               		<div class="col-sm-4 col-lg-3">
		                  <div class="panel panel-default">
		                    <div class="panel-body">
		                      <div class="media">
		                        <div class="media-left">
		                          <img src="${passListItems.avatar}" class="media-object img-circle" alt="User Image">
		                        </div>
		                        <div class="media-body">
		                          <h4 class="media-heading">
		                            	${passListItems.name}
		                            	<c:if test="${passListItems.sex eq '男'}"><i class="fa fa-fw fa-mars"></i></c:if>
			                            <c:if test="${passListItems.sex eq '女'}"><i class="fa fa-fw fa-venus"></i></c:if>
		                          </h4>
		                          <div class="f12 gray9">
		                               <fmt:formatDate value="${passListItems.joinDt}" pattern="yyyy-MM-dd HH:mm:ss"/> 报名
		                          </div>
		                        </div>
		                      </div>
		                    </div>
		                 </div>
	              	 </div>
	               </c:forEach>
	               </c:when>
	               </c:choose>
	          	 </div>
             </div>  
             
            <div class="box box-danger">
            <div class="box-header with-border">
              <div class="pull-right"><button class="btn btn-danger f14 multi-auditUnpass">批量审核</button></div>
              <h3 class="box-title">
                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-fw fa-chevron-circle-right reset-fa-chevron-circle"></i></button>
                <label>不通过</label>
                <span>（${unpassNum}人）</span>
              </h3>
            </div>
            <div class="box-body gray6">
              <div class="row reg-user-list">
              <c:choose>	        	
					<c:when test="${not empty unpassList}">
						<c:forEach items="${unpassList}" var="unpassListItems">
		              	<div class="col-sm-4 col-lg-3">
		                  <div class="panel panel-default">
		                    <div class="panel-body">
		                      <div class="media">
		                        <div class="media-left">
		                          <img src="${unpassListItems.avatar}" class="media-object img-circle" alt="User Image">
		                        </div>
		                        <div class="media-body">
		                          <h4 class="media-heading">
		                              ${unpassListItems.name}
		                              <c:if test="${unpassListItems.sex eq '男'}"><i class="fa fa-fw fa-mars"></i></c:if>
			                            <c:if test="${unpassListItems.sex eq '女'}"><i class="fa fa-fw fa-venus"></i></c:if>
		                          </h4>
		                          <div class="f12 gray9">
		                              <fmt:formatDate value="${unpassListItems.joinDt}" pattern="yyyy-MM-dd HH:mm:ss"/>  报名
		                          </div>
		                        </div>
		                      </div>
		                    </div>
		                    <%-- <div class="panel-footer no-bg">
		                      <a href="${ctx}/home/class/activity/auditUnpassByStudentId?activityId=${info.id}&auditStatus=2&studentId=${unpassListItems.studentId}" 
		                      class="btn btn-block text-light-blue text-bold no-bg no-padding">通过</a>
		                    </div> --%>
		                    <div class="panel-footer no-bg">
	                        	<a href="${ctx}/home/class/activity/auditUnpassByStudentId?activityId=${info.id}&auditStatus=2&studentId=${unpassListItems.studentId}" 
		                      class="btn btn-block text-light-blue text-bold no-bg no-padding">通过</a>
	                      	</div>
		                  </div>
		                </div>
		               </c:forEach>
	                 </c:when>
	               </c:choose>
              </div> 
            </div>   
          </div>       
          </div>
        </div><!-- /.tab-pane --> 
        <div class="tab-pane" id="tab_notice_3">  
        <table id="dtable" class="table table-bordered table-striped table-container">
        	<tr>
			<td>
        	<ul class="list-unstyled news-list act-comments">  
        		  <c:choose>	        	
					<c:when test="${not empty commentsInfos && commentsInfos.numberOfElements > 0}">
						<c:forEach items="${commentsInfos.content}" var="commentsInfosItems">
						<c:if test="${not empty commentsInfosItems}">
							<li class="news-item">
				              <div class="media">
				                <div class="media-left">
				                  <img src="${commentsInfosItems.avatar}" class="media-object img-circle" alt="User Image">
				                </div>
				                <div class="media-body">
				                  <h4 class="media-heading">
				                   		${commentsInfosItems.name}
				                  </h4>
				                  <div class="f12 gray9 comment-time">
				                    <fmt:formatDate value="${commentsInfosItems.commentDt}" pattern="yyyy-MM-dd HH:mm:ss"/>   发表
				                  </div>
				                  <div class="txt">
				                   	${commentsInfosItems.comments}
				                  </div>
				                </div>
				              </div>
				            </li>
				            </c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td align="center" colspan="15">暂无评论</td>
						</tr>
					</c:otherwise>
				</c:choose>
        	</ul>   
        	</td>
        	</tr>
        	</table>
        	 <tags:pagination page="${commentsInfos}" paginationSize="10" />
        </div><!-- /.tab-pane --> 
      </div><!-- /.tab-content -->
    </div><!-- nav-tabs-custom -->
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script>
   //批量审核
   $('.multi-auditWait').off().click(function(){
       $.confirm({
           title: '提示',
           content: '您确定要批量审核吗？',
           icon: 'fa fa-warning',
           closeIcon: true,
           closeIconClass: 'fa fa-close',
           confirmButton: '确认',
           confirmButtonClass: 'btn-primary',
           cancelButton: '取消',
           confirm: function () {               
               window.location.href = '${ctx}/home/class/activity/auditWait?activityId=${info.id}';
           }
       });
   });
   $('.multi-auditUnpass').off().click(function(){
       $.confirm({
           title: '提示',
           content: '您确定要批量审核吗？',
           icon: 'fa fa-warning',
           closeIcon: true,
           closeIconClass: 'fa fa-close',
           confirmButton: '确认',
           confirmButtonClass: 'btn-primary',
           cancelButton: '取消',
           confirm: function () {               
               window.location.href = '${ctx}/home/class/activity/auditUnpass?activityId=${info.id}';
           }
       });
   });
   
</script>

</body>
</html>
