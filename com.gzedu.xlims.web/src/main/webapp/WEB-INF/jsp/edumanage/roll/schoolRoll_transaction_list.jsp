<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
 <meta charset="utf-8">
 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学籍异动</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学籍管理</a></li>
		<li class="#">学籍异动</li>
		<li class="active">信息更正</li>
	</ol>
</section>
<section class="content">
 <div class="nav-tabs-custom no-margin">
    <ul class="nav nav-tabs nav-tabs-lg">
      <li <c:if test="${transType==1}">class="active"</c:if>><a href="${ctx }/edumanage/rollTrans/changeSpecialty/list.html">转专业</a></li>
      <li <c:if test="${transType==2}">class="active"</c:if>><a href="${ctx }/edumanage/rollTrans/tranLeaveStudy/list.html">休学</a></li>
      <li> <a href="#tab_top_3" data-toggle="tab">复学</a></li>
      <li <c:if test="${transType==4}">class="active"</c:if>><a href="${ctx }/edumanage/rollTrans/dropOutStudy/list.html" >退学</a></li>
      <li <c:if test="${transType==5}">class="active"</c:if>><a href="${ctx }/edumanage/schoolRollTransaction/list.html" >信息更正</a></li>
    </ul>
<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
 <form class="form-horizontal" id="listForm" action="list.html" method="get">
 	<input id="auditOperator" type="hidden" name="search_EQ_auditOperatorRole" value="${param['search_EQ_auditOperatorRole']}">
  	<input id="transactionStatus" type="hidden" name="search_EQ_transactionStatus" value="${param['search_EQ_transactionStatus']}">
	<div class="tab-content">
	<div class="tab-pane active" id="tab_top_4">
	<div class="box box-border">
        <div class="box-body">
          <div class="form-horizontal">
            <div class="row pad-t15">
              <div class="col-sm-4">
                <div class="form-group">
                  <label class="control-label col-sm-3 text-nowrap">姓名</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm'] }">
                  </div>
                </div>
              </div>
              <div class="col-sm-4">
                <div class="form-group">
                  <label class="control-label col-sm-3 text-nowrap">学号</label>
                  <div class="col-sm-9">
                    <input type="text" class="form-control" name="search_EQ_gjtStudentInfo.xh" value="${param['search_EQ_gjtStudentInfo.xh'] }">
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
                    <select name="search_EQ_gjtStudentInfo.gjtGrade.gjtYear.gradeId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
                        <option value="" selected="selected">请选择</option>
                        <c:forEach items="${yearMap}" var="map">
                            <option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtGrade.gjtYear.gradeId']}">selected='selected'</c:if>>${map.value}</option>
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
              <div class="col-sm-4">
                <div class="form-group">
                  <label class="control-label col-sm-3 text-nowrap">学期</label>
                  <div class="col-sm-9">
                    <select  name="search_EQ_viewStudentInfo.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
                        <option value="" selected="selected">请选择</option>
                        <c:forEach items="${gradeMap}" var="map">
                            <option value="${map.key}" <c:if test="${map.key==((not empty param['search_EQ_viewStudentInfo.gradeId']) ? param['search_EQ_viewStudentInfo.gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
                        </c:forEach>
                    </select>
                  </div>
                </div>
              </div>
              <div class="col-sm-4">
                <div class="form-group">
                  <label class="control-label col-sm-3 text-nowrap">学籍状态</label>
                  <div class="col-sm-9">
                    <select name="search_EQ_gjtStudentInfo.xjzt" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
                        <option value="" selected="selected">请选择</option>
                        <c:forEach items="${rollTypeMap}" var="map">
                            <option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.xjzt']}">selected='selected'</c:if>>${map.value}</option>
                        </c:forEach>
                    </select>
                  </div>
                </div>
              </div>
              <div class="col-sm-4">
                <div class="form-group">
                  <label class="control-label col-sm-3 text-nowrap">学员类型</label>
                  <div class="col-sm-9">
                   <dic:selectBox name="search_EQ_gjtStudentInfo.userType" typeCode="USER_TYPE" code="${param['search_EQ_gjtStudentInfo.userType'] }" otherAttrs='class="selectpicker show-tick form-control" data-size="5" data-live-search="true"' />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="box-footer text-right">
          <button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
          <button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
        </div>
    </div>
    
    <div class="box box-border margin-bottom-none">
      <div class="box-header with-border">
        <h3 class="box-title pad-t5">学籍列表</h3>
        <div class="pull-right no-margin">
          <!-- <button class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 按条件批量导出</button> -->
        <a href="${ctx}/edumanage/schoolRollTransaction/transactionListExport/${pageInfo.getTotalElements()}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i>按条件批量导出</a>
        </div>
      </div>
      <div class="box-body">
        <div class="filter-tabs filter-tabs2 clearfix">
          <ul class="list-unstyled">                     	
        	<li <c:if test="${empty param.search_EQ_transactionStatus && empty param.search_EQ_auditOperatorRole}">class="actived"</c:if> value='' onclick="choiceStatus('')">全部（${totalNum}）</li>
            <%-- <li <c:if test="${param.search_EQ_transactionStatus ==0 && param.search_EQ_auditOperatorRole == 4}">class="actived"</c:if> value='0' onclick="choiceStudyCenter('0')">学习中心,待处理（${studyCenterAudit}）</li> --%>
            <li <c:if test="${param.search_EQ_transactionStatus ==0 && param.search_EQ_auditOperatorRole == 5}">class="actived"</c:if> value='0' onclick="choiceXJK('0')">学籍科,待处理（${rollAudit}）</li>
            <%-- <li <c:if test="${param.search_EQ_transactionStatus ==2 && param.search_EQ_auditOperatorRole == 4}">class="actived"</c:if> value='2' onclick="choiceStudyCenter('2')">学习中心,审核不通过（${studyCenterAuditPass}）</li> --%>
            <li <c:if test="${param.search_EQ_transactionStatus ==2 && param.search_EQ_auditOperatorRole == 5}">class="actived"</c:if> value='2' onclick="choiceXJK('2')">学籍科,审核不通过（${rollAuditPass}）</li>
            <li <c:if test="${param.search_EQ_transactionStatus ==1}">class="actived"</c:if> value='1' onclick="choiceStatus('1')">已完成（${finish}）</li>
          	
          </ul>
        </div>
        <div class="table-responsive">
          <table class="table table-bordered table-striped vertical-mid table-font text-center">
            <thead>
              <tr>
                <th>照片</th>
                <th>个人信息</th>
                <th>报读信息</th>
                <th>学籍状态</th>
                <th>学员类型</th>
                <th>学习中心</th>
                <th>申请时间</th>
                <th>异动类型</th>
                <th>异动状态</th>
                <th class="text-nowrap">操作</th>
              </tr>
            </thead>
            <tbody>
            <c:forEach items="${pageInfo.content}" var="transaction">
              <tr>
                <td>
                 <img src="${not empty transaction.gjtStudentInfo.avatar ? transaction.gjtStudentInfo.avatar : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>                
                  <a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
                    <i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
                    <span class="gray9">交流</span>
                  </a>
                </td>
                <td>
                  <div class="text-left">
						姓名：${transaction.gjtStudentInfo.xm}<br>
						学号：${transaction.gjtStudentInfo.xh}<br>
						 <shiro:hasPermission name="/personal/index$privacyJurisdiction">
							手机：${transaction.gjtStudentInfo.sjh}<br>
						 	身份证：${transaction.gjtStudentInfo.sfzh}
						 </shiro:hasPermission>
                  </div>
                </td>
                <td>
                  <div class="text-left">
				                    层次：<dic:getLabel typeCode="TrainingLevel" code="${transaction.gjtStudentInfo.pycc}" /><br>
				                    年级：${transaction.gjtStudentInfo.gjtGrade.gjtYear.name}<br>
				                    学期：${transaction.gjtStudentInfo.gjtGrade.gradeName}<br>
				                    专业：${transaction.gjtStudentInfo.gjtSpecialty.zymc}
                  </div>
                </td>
                <td>
                	<dic:getLabel typeCode="StudentNumberStatus" code="${transaction.gjtStudentInfo.xjzt}" />
                </td>
                <td>
                	 <dic:getLabel typeCode="USER_TYPE" code="${transaction.gjtStudentInfo.userType}" />
                </td>
                <td>
                  	${transaction.gjtStudentInfo.gjtStudyCenter.gjtOrg.orgName}<br>
                  <!-- <small>（张丽丽）</small> -->
                </td>
                <td>
                  <fmt:formatDate value="${transaction.createdDt}" pattern="yyyy-MM-dd" />
                </td>
                <td>
                	<c:choose>
                 		<c:when test="${transaction.transactionPartStatus==0}"><span class="text-green">性别民族变更</span><br></c:when>
                 		<c:when test="${transaction.transactionPartStatus==1}"><span class="text-green">入学文化程度更变</span><br></c:when>
                 		<c:when test="${transaction.transactionPartStatus==2}"><span class="text-green">姓名变更</span><br></c:when> 
                 		<c:when test="${transaction.transactionPartStatus==3}"><span class="text-green">身份证变更</span><br></c:when>
                 		<c:otherwise>                 	 	 
                   	 	</c:otherwise>             		
                 	</c:choose>
                </td>              
                <td class="text-orange">
                 	<c:choose>
                 		<c:when test="${transaction.transactionStatus==0}"><span class="text-green"> 待审核</span><br></c:when>
                 		<c:when test="${transaction.transactionStatus==1}"><span class="text-green"> 已完成</span><br></c:when>
                 		<c:when test="${transaction.transactionStatus==2}"><span class="text-green"> 审核不通过</span><br></c:when>
                 		<c:otherwise>
                   	 	 <span class="text-green">待访谈</span><br>
                   	 	</c:otherwise> 
                 	</c:choose>
                 	<c:choose>
                 	<c:when test="${transaction.auditOperatorRole==4}"><small class="gray9">（学习中心）</small></c:when>
                 	<c:when test="${transaction.auditOperatorRole==5}"><small class="gray9">（学籍科）</small></c:when>               	
                 	<c:otherwise><small class="gray9">（中央电大）</c:otherwise> 
                 	</c:choose>                 
                </td>
                <td>
                  <a href="${ctx }/edumanage/schoolRollTransaction/view/${transaction.transactionId}/${transaction.gjtStudentInfo.studentId}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                </td>
              </tr>
              </c:forEach>
            </tbody>
          </table>    
          <tags:pagination page="${pageInfo}" paginationSize="5" />
        </div>
      </div>
    </div>
   </div>
  </div>
  </form>
 </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
// filter tabs
$(".filter-tabs li").click(function(event) {
  if($(this).hasClass('actived')){
    $(this).removeClass('actived');
  }
  else{
    $(this).addClass('actived');
  }
});

// filter menu
$(".custom-dropdown")
.on('click', "[data-role='sure-btn']", function(event) {
  var $box=$(this).closest('.custom-dropdown');
  $(this).closest('th').removeClass('on');
  $box.find("li").removeClass('actived');
  $box.find(":checkbox").attr("checked",false);
})
.on('click', "[data-role='close-btn']", function(event) {
  $(this).closest('th').removeClass('on');
})
.on('click', 'li', function(event) {
  if($(this).hasClass('actived')){
    $(this).removeClass('actived')
    .find(":checkbox").attr("checked",false);
  }
  else{
    $(this).addClass('actived')
    .find(":checkbox").attr("checked",true);
  }
});

$('th[data-role="menu-th"]')
.on('mouseover', function(event) {
  $(this).addClass('on');
})
.on('mouseout', function(event) {
  if(!$(this).children('.custom-dropdown').hasClass('open')){
    $(this).removeClass('on');
  }
});
//导出
$('[data-role="export"]').click(function(event) {
    event.preventDefault();
    var self=this;
    $.mydialog({
        id:'export',
        width:600,
        height:415,
        zIndex:11000,
        content: 'url:'+$(this).attr('href')
    });
});
$('.list-unstyled li').click(function() {
	$('[name="search_EQ_transactionType"]').val($(this).attr('transactionType'));
	$('#listForm').submit();
});
//学习中心处理状态
function choiceStudyCenter(flag){
	$("#auditOperator").val('');
	$("#transactionStatus").val('');
	$("#auditOperator").val('4');
	$("#transactionStatus").val(flag);
	$("#listForm").submit();
}
//学籍科处理状态
function choiceXJK(flag){
	$("#auditOperator").val('');
	$("#transactionStatus").val('');
	$("#auditOperator").val('5');
	$("#transactionStatus").val(flag);
	$("#listForm").submit();
}
function choiceStatus(flag){
	$("#auditOperator").val('');
	$("#transactionStatus").val('');
	$("#transactionStatus").val(flag);
	$("#listForm").submit();
}
</script>
</body>
</html>
