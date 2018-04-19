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
		班级活动
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">班级活动</li>
	</ol>
</section>
<section class="content">

	<div class="nav-tabs-custom reset-nav-tabs-custom margin-bottom-none" id="notice-tabs">
		<div class="pull-right margin_r10 margin_t10">
			<a href="${ctx}/home/class/activity/toCreateActivity" role="button" class="btn btn-default btn-add"><i class="fa fa-fw fa-plus"></i>  新增活动</a>
		</div>
		<ul class="nav nav-tabs nav-tabs-lg">
			<c:choose>
				<c:when test="${flag eq '1'}">
					<li><a href="${ctx}/home/class/activity/list">未结束(${unCount})</a></li>
					<li class="active"><a href="${ctx}/home/class/activity/overList" data-toggle="tab">已结束(${overCount})</a></li>
				</c:when>
				<c:otherwise>
					<li class="active"><a href="${ctx}/home/class/activity/list" data-toggle="tab">未结束(${unCount})</a></li>
					<li><a href="${ctx}/home/class/activity/overList">已结束(${overCount})</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
		<div class="tab-content">
			<form class="form-horizontal">
				<div class="box box-border no-shadow">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-sm-5">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">活动名称</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_activityTitle" value="${param['search_LIKE_activityTitle']}" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
					              <label class="control-label col-sm-3 text-nowrap">报名审核</label>
					              <div class="col-sm-9">
					                <select class="form-control" name="search_EQ_auditStatus">
					                  <option value="">全部</option>
					                  <option value="0" <c:if test="${param.search_EQ_auditStatus=='0'}">selected='selected'</c:if> >待审核人员
					                  </option>
					                </select>
					              </div>
					            </div>
							</div>
						</div>
					</div><!-- /.box-body -->
					<div class="box-footer">
						<div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
						<div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
					</div><!-- /.box-footer-->
				</div>
			</form>
			<div class="box box-border no-shadow margin-bottom-none">
				<div class="box-body">
					<ul class="news-list list-unstyled">
			        	<c:choose>	        	
							<c:when test="${not empty infos && infos.numberOfElements > 0}">
								<c:forEach items="${infos.content}" var="info">
									<c:if test="${not empty info}">
										<li class="news-item">
											<div class="media">
								                <div class="media-left media-middle pad-r20 xs-block xs-center">
								                  <a href="view/${info.id}">
								                    <img src="${info.publicityPicture}" alt="Image" style="width:210px;height:120px">
								                  </a>
								                </div>
								                <div class="media-body xs-block margin_t10">
								                    <h4 class="media-heading act-tit">
								                    <a href="view/${info.id}">
								                      ${info.activityTitle}
								                    </a>
								                  </h4>
								                  <div class="row act-info gray9">
								                    <div class="col-xs-6 col-sm-6 col-md-4">
								                      <i class="fa fa-fw fa-clock-o"></i>
								                      <span class="txt"><fmt:formatDate value="${info.endTime}" pattern="yyyy-MM-dd " /></span>
								                    </div>
								                    <div class="col-xs-6 col-sm-6 col-md-4">
								                    	 <i class="fa fa-fw fa-act-1"></i>
		                  								<span class="txt">人数限制（${info.ceilingNum}）</span>
								                    </div>
								                    <div class="col-xs-6 col-sm-6 col-md-4">
								                    	<i class="fa fa-fw fa-act-2"></i>
		                  								<span class="txt">报名人数（${info.joinNum}）</span>
								                    </div>
								                    
								                    <div class="col-xs-6 col-sm-6 col-md-4">
								                   		 <i class="fa fa-fw fa-act-3"></i>
		                 								 <span class="txt">费用（<c:if test="${info.isFree==1}">免费</c:if><c:if test="${info.isFree==2}">收费</c:if>）</span>
								                    </div>
								                    <div class="col-xs-6 col-sm-6 col-md-4">
								                    	<i class="fa fa-fw fa-commenting-o"></i>
		                 								<span class="txt">评论（${info.commentNum}）</span>
								                    </div>
								                    <div class="col-xs-6 col-sm-6 col-md-4">
								                    	<i class="fa fa-fw fa-act-4"></i>
		                 							 	<span class="txt">待审核人数（${info.colWaitActivityNum}）</span>
								                    </div>
								                    
								                    <div class="col-xs-12">
								                      	<i class="fa fa-fw fa-act-5"></i>
		                  								<span class="txt">${info.activityAddress}</span>
								                    </div>
								                  </div>
								                </div>
								                <div class="media-right media-middle margin_t20 pad-l20 sm-center sm-block">
								                   <a href="view/${info.id}" role="button" class="btn btn-primary min-width-90px">查看</a>
								                </div>
								              </div>
										</li>
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li class="news-item">
									<div align="center">暂无数据</div>
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
					<div class="margin_t15">
			        	<tags:pagination page="${infos}" paginationSize="10" />
			        </div>
		        </div>
	        </div>
		</div>
	</div><!-- nav-tabs-custom -->
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
</html>
