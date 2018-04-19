<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统-免修免考设置</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>

<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li>学籍管理</li>
		<li>免修免考</li>
		<li class="active">学员申请</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<!-- <li class="active"><a data-toggle="tab" href="#tab_top_1">学员申请</a></li> -->
			<li <c:if test="${exemptExamType==1}">class="active"</c:if>><a href="${ctx}/edumanage/exemptExamInfo/list.html">学员申请</a></li>
			<!-- <li><a href="#tab_top_2" data-toggle="tab">免修免考课程设置</a></li> -->
			<li <c:if test="${exemptExamType==2}">class="active"</c:if>><a href="${ctx}/edumanage/exemptExamInstall/list.html">免修免考课程设置</a></li>
		</ul>
		
		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		
		<form class="form-horizontal" id="listForm" action="list.html" method="get">
		<input id="auditOperatorRole" type="hidden" name="search_EQ_auditOperatorRole" value="${param['search_EQ_auditOperatorRole']}">
  		<input id="auditStatus" type="hidden" name="search_EQ_auditStatus" value="${param['search_EQ_auditStatus']}">
		<div class="tab-content">
			<div class="tab-pane active" id="tab_top_1">
				<div class="box box-border">
				    <div class="box-body">
				      <form class="form-horizontal">
				        <div class="row pad-t15">
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">学号</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="search_EQ_gjtStudentInfo.xh" value="${param['search_EQ_gjtStudentInfo.xh']}">
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">姓名</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_EQ_gjtStudentInfo.xm']}">
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">学习中心</label>
				              <div class="col-sm-9">
				                <select name="search_EQ_gjtStudentInfo.gjtStudyCenter.gjtOrg.id" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
				                  <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${studyCenterMap}" var="map">
                                        <option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtStudyCenter.gjtOrg.id']}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
				                </select>
				              </div>
				            </div>
				          </div>

				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">专业</label>
				              <div class="col-sm-9">
				                <select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${specialtyMap}" var="map">
                                        <option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">年级</label>
				              <div class="col-sm-9">
				                <select  name="search_EQ_gjtStudentInfo.gjtGrade.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${gradeMap}" var="map">
                                        <option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">层次</label>
				              <div class="col-sm-9">
				                <select  name="search_EQ_gjtStudentInfo.pycc"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${pyccMap}" var="map">
                                        <option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                  </select>
                               </div>
				              </div>
				            </div>
				          </div>
				        </div>				     
				    </div>
				    <div class="box-footer">
				      <div class="pull-right"><button type="button" class="btn min-width-90px btn-default btn-reset">重置</button></div>
				      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary search">搜索</button></div>
				    </div>
				</div>
				<div class="box box-border margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">信息列表</h3>
						<div class="pull-right">
							<button class="btn btn-default btn-sm btn-import"><i class="fa fa-fw fa-sign-out"></i> 按条件批量导出</button>
						</div>
					</div>
					<div class="box-body">
						<div class="filter-tabs clearfix">
							<ul class="list-unstyled">								
								<li <c:if test="${empty param.search_EQ_auditStatus}">class="actived"</c:if> value='' onclick="choiceStatus('')">全部（${totalNum}）</li>
								<li <c:if test="${param.search_EQ_auditStatus ==0 }">class="actived"</c:if> value='0' onclick="choiceStatus('0')">学籍科,待审核（${stayAudit}）</li>
								<li <c:if test="${param.search_EQ_auditStatus ==2 }">class="actived"</c:if> value='2' onclick="choiceStatus('2')">学籍科,审核不通过（${auditNoPass}）</li>
								<li <c:if test="${param.search_EQ_auditStatus ==1 }">class="actived"</c:if> value='1' onclick="choiceStatus('1')">学籍科,审核通过（${auditPass}）</li>
							</ul>
						</div>
						<div class="table-responsive">
					        <table class="table table-bordered table-striped vertical-mid table-font text-center">
			                  <thead>
			                    <tr>
			                      <th width="80">照片</th>
			                      <th width="17%">个人信息</th>
			                      <th>报读信息</th>
			                      <th width="15%">免修课程</th>
			                      <th width="11%">申请时间</th>
			                      <th width="15%">状态</th>
			                      <th width="10%" class="text-nowrap">操作</th>
			                    </tr>
			                  </thead>
			                  <tbody>
			                  <c:choose>
			                   <c:when test="${not empty pageInfo.content}">
			                   <c:forEach items="${pageInfo.content}" var="item">
			                    <tr>
			                      <td>
			                        <img src="${not empty item.gjtStudentInfo.avatar ? item.gjtStudentInfo.avatar : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>                
                  					<a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
                    				<i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i></a>
			                      </td>
			                      <td>
			                        <div class="text-left">
			                          	姓名：${item.gjtStudentInfo.xm}<br>
			                          	学号：${item.gjtStudentInfo.xh}<br>
			                          	<shiro:hasPermission name="/personal/index$privacyJurisdiction">
											手机：${item.gjtStudentInfo.sjh}
						 				</shiro:hasPermission>
			                        </div>
			                      </td>
			                      <td>
			                        <div class="text-left">
			                         	 层次：<dic:getLabel typeCode="TrainingLevel" code="${item.gjtStudentInfo.pycc}"/><br>
			                         	 学期：${item.gjtStudentInfo.gjtGrade.gradeName}<br>
			                          	专业：${item.gjtStudentInfo.gjtSpecialty.zymc}
			                        </div>
			                      </td>
			                      <td>
			                        <div>${item.gjtCourse.kcmc}</div>
			                        <div class="gray9">${item.gjtCourse.kch}</div>
			                      </td>
			                      <td>
			                        <fmt:formatDate value="${item.createdDt}" pattern="yyyy-MM-dd" />
			                      </td>
			                      <td>			                        
									<c:choose>
                 						<c:when test="${item.auditStatus==0}"><span class="text-orange"> 待审核</span><br></c:when>
                 						<c:when test="${item.auditStatus==1}"><span class="text-green"> 已通过</span><br></c:when>
                 						<c:when test="${item.auditStatus==2}"><span class="text-red"> 审核不通过</span><br></c:when>
                 				   <c:otherwise>                  	 	 				
                   	 			   </c:otherwise> 
                 				   </c:choose>
			                      </td>
			                      <td>
			                      <c:if test="${item.auditStatus==0}">
			                        <a href="${ctx}/edumanage/exemptExamInfo/toView/${item.exemptExamId}/update" class="operion-item" data-toggle="tooltip" title="审核资料"><i class="fa fa-fw fa-shxxjl"></i></a>
			                      </c:if>
			                      <c:if test="${item.auditStatus==2}">
			                        <a href="${ctx}/edumanage/exemptExamInfo/toView/${item.exemptExamId}/view" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
			                      </c:if>
			                      <c:if test="${item.auditStatus==1}">
			                        <a href="${ctx}/edumanage/exemptExamInfo/toView/${item.exemptExamId}/view" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
			                        <a href="${ctx}/edumanage/exemptExamInfo/downloadApplication/${item.exemptExamId}" class="operion-item" data-toggle="tooltip" title="下载申请表"><i class="fa fa-fw fa-download"></i></a>
			                      </c:if>
			                      </td>
			                    </tr>			                    			                    
			                   </c:forEach>
			                   </c:when>
			                   <c:otherwise>
								<tr>
									<td align="center" colspan="9">暂无数据</td>
								</tr>
							  </c:otherwise>
			                  </c:choose>
			                  </tbody>
			                </table>
							 <tags:pagination page="${pageInfo}" paginationSize="5" />
					    </div>
					</div>
				</div>
			</div>
		</form>
	</div>	 
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
//select2
$(".select2").select2();
// filter tabs
$(".filter-tabs li").click(function(event) {
	if($(this).hasClass('actived')){
		$(this).removeClass('actived');
	}
	else{
		$(this).addClass('actived');
	}
});
function choiceStatus(flag){
	$("#auditOperatorRole").val('');
	$("#auditStatus").val('');
	$("#auditStatus").val(flag);
	$("#listForm").submit();
}
</script>
</body>
</html>
