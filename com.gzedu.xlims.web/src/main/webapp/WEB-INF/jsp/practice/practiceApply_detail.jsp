<%@page import="com.gzedu.xlims.pojo.status.PracticeProgressCodeEnum"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">实践管理</a></li>
		<li><a href="#">实践记录</a></li>
		<li class="active">实践记录详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
    <div class="box-body pad20">
      <div class="row">
        <div class="col-sm-5">
          <div class="media margin_t20">
            <div class="media-left" style="padding-right:25px;">
              	<c:choose>
					<c:when test="${not empty practiceApply.gjtStudentInfo.avatar}">
						<img src="${practiceApply.gjtStudentInfo.avatar}" class="img-circle" style="width:112px;height:112px;" alt="User Image">
					</c:when>
					<c:otherwise>
				        <img src="${ctx}/static/dist/img/images/user-placehoder.png" class="img-circle" style="width:112px;height:112px;" alt="User Image">
					</c:otherwise>
				</c:choose>
            </div>
            <div class="media-body">
              <h3 class="no-margin pad-b10">
                ${practiceApply.gjtStudentInfo.xm}
              </h3>
              <div class="per-info">
                <div class="clearfix">
                  <label class="pull-left margin_r10 text-no-bold">学号:</label>
                  <div class="oh">${practiceApply.gjtStudentInfo.xh}</div>
                </div>
                <div class="clearfix">
                  <label class="pull-left margin_r10 text-no-bold">学期:</label>
                  <div class="oh">${practiceApply.gjtStudentInfo.gjtGrade.gradeName}</div>
                </div>
                <div class="clearfix">
                  <label class="pull-left margin_r10 text-no-bold">层次:</label>
                  <div class="oh">${pyccMap[practiceApply.gjtStudentInfo.gjtSpecialty.pycc]}</div>
                </div>
                <div class="clearfix">
                  <label class="pull-left margin_r10 text-no-bold">专业:</label>
                  <div class="oh">
                    ${practiceApply.gjtStudentInfo.gjtSpecialty.zymc}
                    <small class="gray9">（${practiceApply.gjtStudentInfo.gjtSpecialty.ruleCode}）</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-sm-7">
          <table class="table no-border vertical-middle no-margin">
            <tr>
              <td width="25%" style="border-left:#e0e0e0 1px solid;">
                <dl class="center-block tea-item-dl text-center">   
                  <dd class="img-box pos-rel">
	                  <c:choose>
	       				<c:when test="${not empty practiceApply.guideTeacher1}">
	       					<img src="${practiceApply.guideTeacher1.zp}">
	       					<div class="name"><span>${practiceApply.guideTeacher1.xm}</span></div>
	       				</c:when>
	       				<c:otherwise>
	       					<img src="${ctx}/static/dist/img/images/user-placehoder.png">
	       				</c:otherwise>
	        		  </c:choose>
        		   </dd>
	                <dd class="margin_t10">
						<c:choose>
							<c:when test="${not empty practiceApply.guideTeacher1}">
								<shiro:hasPermission name="/practiceApply/list$updateGuideTeacher">
									<button type="button" class="btn btn-default btn-block" data-toggle="modal" data-target="#modifyGuideTeacherModal">修改</button>
								</shiro:hasPermission>
							</c:when>
							<c:otherwise>
								<shiro:hasPermission name="/practiceApply/list$updateGuideTeacher">
									<button type="button" class="btn btn-primary btn-block" data-toggle="modal" data-target="#modifyGuideTeacherModal">分配老师</button>
								</shiro:hasPermission>
							</c:otherwise>
						</c:choose>
	                </dd>
                  <dt class="text-no-bold margin_t5">社会实践指导老师</dt>
                </dl>
              </td>
              
              <td width="25%" style="border-left:#e0e0e0 1px solid;">
                <div class="f24 text-center">
                	<c:choose>
		     			<c:when test="${not empty practiceApply.reviewScore}">
		     				${practiceApply.reviewScore}
		    			</c:when>
		     			<c:otherwise>--</c:otherwise>
		        	</c:choose>
                </div>
                <div class="text-center gray6">评分</div>
              </td>

              <td width="25%" style="border-left:#e0e0e0 1px solid;">
                <div class="f18 text-center text-orange">${practiceStatusMap[practiceApply.status]}</div>
                <div class="text-center gray6">状态</div>
              </td>
              
              <td width="25%" class="no-pad-right text-center" style="border-left:#e0e0e0 1px solid;">
                <a href="${ctx}/usermanage/student/simulation.html?id=${practiceApply.studentId}" target="_blank" role="button" class="btn btn-default">
                  <i class="fa fa-simulated-login f18 vertical-middle"></i>
                 	 模拟登录个人学习空间
                </a>
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
  <div class="box">
    <div class="box-header with-border">
      <h3 class="box-title">社会实践详细记录</h3>
    </div>
    <div class="box-body">
      <div class="approval-list approval-list-2 approval-list-asc clearfix margin_b20 margin_t10">
        <c:forEach items="${studentProgs}" var="prog" varStatus="status">
             <dl class="approval-item">
               <dt class="clearfix">
                 <div class="a-tit">
                   <b>${practiceProgressCodeMap[prog.progressCode]} <c:if test="${prog.progressCode eq '4002'}"><span class="text-green">（得分：${practiceApply.reviewScore}分）</span></c:if>  </b>
                 </div>
                 <span class="time state-lb"><fmt:formatDate value="${prog.createdDt}" pattern="yyyy-MM-dd HH:mm"/></span>
               </dt>
               <dd>
                <c:if test="${prog.progressCode eq '2001'}"><!-- 如果是"分配指导老师"这个节点，需要显示指导老师名称和头像 -->
                  <div class="txt media">
                    <div class="media-left">
                      	<c:choose>
          					<c:when test="${not empty practiceApply.guideTeacher1.zp}">
          						<img src="${practiceApply.guideTeacher1.zp }" class="img-circle" alt="User Image" width="45" height="45">
          					</c:when>
          					<c:otherwise>
          						<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" alt="User Image" width="45" height="45">
          					</c:otherwise>
         				</c:choose>
                      <div class="text-center f12">${practiceApply.guideTeacher1.xm }</div>
                    </div>
                  </div>
                 </c:if>
	                <c:if test="${prog.progressCode eq '5001'}"><!-- 如果是"定稿已寄送"这个节点，需要显示快递信息 -->
	                  <div class="txt media">
	                    <div class="media-left">
	                      	<c:choose>
	          					<c:when test="${not empty practiceApply.gjtStudentInfo.avatar}">
	          						<img src="${practiceApply.gjtStudentInfo.avatar }" class="img-circle" alt="User Image" width="45" height="45">
	          					</c:when>
	          					<c:otherwise>
	          						<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" alt="User Image" width="45" height="45">
	          					</c:otherwise>
	         				</c:choose>
	                      <div class="text-center f12">${practiceApply.gjtStudentInfo.xm }</div>
	                    </div>
	                    <div class="media-body pad-l15 f12">
	                      <div><fmt:formatDate value="${prog.createdDt}" pattern="yyyy-MM-dd HH:mm"/></div>
	                      <div class="margin_t5">快递公司：${expressMap[practiceApply.expressCompany]}（<a class="show-express" href="javascript:void(0)">查看物流信息</a>）</div>
	                    </div>
	                  </div>
	                 </c:if>
                  	<c:forEach items="${studentProgRecord[prog.progressId]}" var="record" varStatus="var">
	                  <div class="txt media">
	                    <div class="media-left">
	                    	<c:choose>
               					<c:when test="${record.isStudent == 1}">
               						<c:choose>
	                					<c:when test="${not empty record.gjtStudentInfo.avatar}">
	                						<img src="${record.gjtStudentInfo.avatar}" class="img-circle" alt="User Image" width="45" height="45">
	                					</c:when>
	                					<c:otherwise>
	                						<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" alt="User Image" width="45" height="45">
	                					</c:otherwise>
                					</c:choose>
                					
                          			<div class="text-center f12">${record.gjtStudentInfo.xm}</div>
               					</c:when>
               					<c:otherwise>
               						
               						<c:choose>
	                					<c:when test="${not empty record.teacher.zp}">
	                						<img src="${record.teacher.zp}" class="img-circle" alt="User Image" width="45" height="45">
	                					</c:when>
	                					<c:otherwise>
	                						<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" alt="User Image" width="45" height="45">
	                					</c:otherwise>
                					</c:choose>
                					
                          			<div class="text-center f12">${record.teacher.xm}</div>
               					</c:otherwise>
               				</c:choose>
	                    </div>
	                    <div class="media-body pad-l15 f12">
	                      <div><fmt:formatDate value="${record.createdDt}" pattern="yyyy-MM-dd HH:mm"/></div>
	                      <div class="margin_t5">${record.content}</div>
	                      <div class="margin_t5">
	                        <a href="http://eezxyl.gzedu.com?furl=${record.attachment}" data-role="addon-preview">${record.attachmentName}</a>
	                      </div>
	                    </div>
	                  </div>
	                  
	                  <!-- 根据最后一条判断有没有附件，有的话是已经确认提交了，显示定稿或者返回修改按钮，反之则是回复 -->
	                  <c:if test="${var.last and not empty record.attachment}">
	                  		<c:set  var="bool" value="sure"></c:set>
	                  </c:if>
                  	</c:forEach>
                  	<c:if test="${param.isTeacher && practiceApply.status == 2 && prog.progressCode eq '4001'}"> <!-- 如果状态为"待定稿"，并且是指导老师进来这个页面，需要显示操作按钮 -->
                  		<div class="margin_t10">
                  			
                 			<c:if test="${bool eq 'sure' }">
			            	  <button class="btn btn-primary margin_r10" type="button" data-role="confirm-1">确认定稿 </button>
			            	  <button class="btn btn-warning" type="button" data-role="re-edit-1" data-id="1">发回修改</button>
			              	</c:if>
			              	
			              	<c:if test="${bool ne 'sure' }">
			              		<button class="btn btn-primary margin_r10" type="button" data-role="re-edit-1" data-id="2">回复</button>
			              	</c:if>
			              
			            </div>
                  	</c:if>
               </dd>
             </dl>
         </c:forEach>
      </div>
    </div>
  </div>
</section>
		
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<div id="modifyGuideTeacherModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="modifyGuideTeacherLabel" aria-hidden="true"> 
  	<div class="modal-dialog">
  		<div class="modal-content">
	      <div class="modal-header"> 
	          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	          <h4 class="modal-title" id="modifyGuideTeacherLabel">修改指导老师</h4>
	      </div> 
	      <div class="modal-body"> 
	      	<form method="post" id="modifyForm" class="form-horizontal" role="form" action="${ctx}/practiceApply/updateGuideTeacher">
				<input type="hidden" name="applyId" value="${practiceApply.applyId}"/>
				<div class="box-body">
					<div class="form-group">
						<label class="col-sm-3 control-label">社会实践指导老师:</label>
						<div class="col-sm-8">
							<select id="teacherId" name="teacherId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
								<c:forEach items="${guideTeachers}" var="guideTeacher">
									<option value="${guideTeacher.employeeId}"  <c:if test="${guideTeacher.employeeId==practiceApply.guideTeacher}">selected='selected'</c:if>>${guideTeacher.xm}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
	        </form>
	      </div> 
	      <div class="modal-footer"> 
	      	<button type="button" id="confirmB" onclick="confirm()" class="btn btn-success">确定</button>
	        <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
	      </div> 
      	</div>
     </div>
</div>

<script type="text/javascript">

function confirm() {
	var teacher = document.getElementById("teacherId").value;
	if (teacher == "") 
	{
		alert("请选择指导老师！"); 
		document.getElementById("teacherId").focus();
		return false;
	}
	
	$('#modifyForm').submit();
}

//附件预览
$('[data-role="addon-preview"]').click(function(event) {
  event.preventDefault();
  var _this=this;
  var $pop=$.alertDialog({
    id:'addon-preview',
    title:'在线预览',
    width:$(window).width(),
    height:$(window).height(),
    zIndex:11000,
    content: '',
    cancelLabel:'关闭',
    cancelCss:'btn btn-default min-width-90px margin_r15',
    okLabel:'下载文档',
    okCss:'btn btn-primary min-width-90px'
  });

  //载入附件内容
  $('.box-body',$pop).addClass('overlay-wrapper position-relative').html([
    '<iframe src="'+$(_this).attr('href')+'" id="Iframe-addon-preview" name="Iframe-addon-preview" frameborder="0" scrolling="auto" style="width:100%;height:100%;position:absolute;left:0;top:0;"></iframe>',
    '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>'
  ].join(''));

  $('#Iframe-addon-preview').on('load',function(){
    $('.overlay',$pop).hide();
  });
});

//发回修改
$('body').on('click', '[data-role="re-edit-1"]', function(event) {
	var type =$(this).data('id');
  event.preventDefault();
  $.mydialog({
    id:'re-edit-1',
    width:600,
    height:600,
    zIndex:11000,
    content: 'url:submitRecord?applyId=${practiceApply.applyId}&type='+type
  });
})

//确认定稿
.on('click', '[data-role="confirm-1"]', function(event) {
  event.preventDefault();
  $.mydialog({
    id:'confirm-1',
    width:400,
    height:300,
    zIndex:11000,
    content: 'url:confirmPractice?applyId=${practiceApply.applyId}'
  });
})

//查询物流
.on('click', '.show-express', function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'show-express',
	  width:600,
	  height:700,
	  zIndex:11000,
	  content: 'url:queryLogistics?applyId=${practiceApply.applyId}'
	});
});

</script>
</body>
</html>
