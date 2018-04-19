<%@page import="com.gzedu.xlims.pojo.status.GraduationProgressCodeEnum"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
})

function modifyGuideTeacher1(applyId) {
	$("#applyId1").val(applyId);
	$("#modifyGuideTeacherModal1").modal();
}

function confirm1() {
	var teacher = document.getElementById("teacherId1").value;
	if (teacher == "") 
	{
		alert("请选择指导老师！"); 
		document.getElementById("teacherId1").focus();
		return false;
	}
	
	$('#modifyForm1').submit();
}

function modifyGuideTeacher2(applyId) {
	$("#applyId2").val(applyId);
	$("#modifyGuideTeacherModal2").modal();
}

function confirm2() {
	var teacher = document.getElementById("teacherId2").value;
	if (teacher == "") 
	{
		alert("请选择指导老师！"); 
		document.getElementById("teacherId2").focus();
		return false;
	}
	
	$('#modifyForm2').submit();
}

function modifyDefence(applyId) {
	$("#applyId3").val(applyId);
	$("#modifyDefenceModal").modal();
}

function confirmDefence() {
	var defence = document.getElementById("defence").value;
	if (defence == "") 
	{
		alert("请选择答辩方式！"); 
		document.getElementById("defence").focus();
		return false;
	}
	
	$('#modifyDefenceForm').submit();
}

function confirm() {
	$.confirm({
        title: '提示',
        content: '确认定稿？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
        	 window.location.href = "${ctx}/graduation/apply/confirm.do?applyId=${thesisApply.applyId}";
        } 
    });
}

function reject() {
	$.confirm({
        title: '提示',
        content: '确认审核不通过？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
        	 window.location.href = "${ctx}/graduation/apply/reject.do?applyId=${thesisApply.applyId}";
        } 
    });
}
</script>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li><a href="#">毕业学员</a></li>
		<li class="active">毕业学员详情</li>
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
								<c:when test="${not empty studentInfo.avatar}">
									<img src="${studentInfo.avatar}" class="img-circle" style="width:112px;height:112px;" alt="User Image">
								</c:when>
								<c:otherwise>
							        <img src="${ctx}/static/dist/img/images/user-placehoder.png" class="img-circle" style="width:112px;height:112px;" alt="User Image">
								</c:otherwise>
							</c:choose>
			          </div>
			          <div class="media-body">
				          	<h3 class="margin_t10">
			               		 ${studentInfo.xm}
			              	</h3>
			              	 <div class="per-info">
				                <div class="clearfix">
				                  <label class="pull-left margin_r10">学号:</label>
				                  <div class="oh">${studentInfo.xh}</div>
				                </div>
				                <div class="clearfix">
				                  <label class="pull-left margin_r10">层次:</label>
				                  <div class="oh">${trainingLevelMap[studentInfo.pycc]}</div>
				                </div>
				                <div class="clearfix">
				                  <label class="pull-left margin_r10">专业:</label>
				                  <div class="oh">${studentInfo.gjtSpecialty.zymc}</div>
				                </div>
				              </div>
				       </div>
			        </div>
			    </div> 
			    
			    <div class="col-sm-7" style="border-left:#e0e0e0 1px solid;">
		          <div class="row">
		            <div class="col-sm-4">
		              <dl class="center-block tea-item-dl text-center">
		                <dt class="text-no-bold">论文指导老师</dt>
		                <dd class="img-box pos-rel margin_t5">
							<c:choose>
								<c:when test="${not empty thesisApply}">
									<c:choose>
										<c:when test="${not empty thesisApply.guideTeacher1}">
											<img src="${thesisApply.guideTeacher1.zp}">
		                  					<div class="name"><span>${thesisApply.guideTeacher1.xm}</span></div>
										</c:when>
										<c:otherwise>
									        <img src="${ctx}/static/dist/img/images/user-placehoder.png">
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<img src="${ctx}/static/dist/img/images/user-placehoder.png">
								</c:otherwise>
							</c:choose>
		                </dd>
		                <dd class="margin_t10">
							<c:choose>
								<c:when test="${not empty thesisApply}">
									<c:choose>
										<c:when test="${not empty thesisApply.guideTeacher1}">
											<shiro:hasPermission name="/graduation/apply/list$update">
												<button type="button" class="btn btn-default btn-block" onclick="modifyGuideTeacher1('${thesisApply.applyId}')">修改</button>
											</shiro:hasPermission>
										</c:when>
										<c:otherwise>
											<shiro:hasPermission name="/graduation/apply/list$update">
												<button type="button" class="btn btn-primary btn-block" onclick="modifyGuideTeacher1('${thesisApply.applyId}')">分配老师</button>
											</shiro:hasPermission>
										</c:otherwise>
									</c:choose>
								</c:when>
							</c:choose>
		                </dd>
		              </dl>
		            </div>
		            <div class="col-sm-4">
		              <dl class="center-block tea-item-dl text-center">
		                <dt class="text-no-bold">社会实践指导老师</dt>
		                <dd class="img-box pos-rel margin_t5">
							<c:choose>
								<c:when test="${not empty practiceApply}">
									<c:choose>
										<c:when test="${not empty practiceApply.guideTeacher1}">
									        <img src="${practiceApply.guideTeacher1.zp}">
									        <div class="name"><span>${practiceApply.guideTeacher1.xm}</span></div>
										</c:when>
										<c:otherwise>
									        <img src="${ctx}/static/dist/img/images/user-placehoder.png">
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<img src="${ctx}/static/dist/img/images/user-placehoder.png">
								</c:otherwise>
							</c:choose>
		                </dd>
		                <dd class="margin_t10">
							<c:choose>
								<c:when test="${not empty practiceApply}">
									<c:choose>
										<c:when test="${not empty practiceApply.guideTeacher1}">
											<shiro:hasPermission name="/graduation/apply/list$update">
												<button type="button" class="btn btn-default btn-block" onclick="modifyGuideTeacher2('${practiceApply.applyId}')">修改</button>
											</shiro:hasPermission>
										</c:when>
										<c:otherwise>
											<shiro:hasPermission name="/graduation/apply/list$update">
												<button type="button" class="btn btn-primary btn-block" onclick="modifyGuideTeacher2('${practiceApply.applyId}')">分配老师</button>
											</shiro:hasPermission>
										</c:otherwise>
									</c:choose>
								</c:when>
							</c:choose>
		                </dd>
		              </dl>
		            </div>
		          </div>
		        </div>
      		</div>
    	</div>
    	
    	<div class="box-footer">
	      <div class="row stu-info-status">
	      	<c:if test="${not empty thesisApply}">
		        <div class="col-sm-2_6 col-xs-2">
		          <div class="f24 text-center text-orange">
		        	<c:if test="${thesisApply.applyDegree == 0}">
						无意向申请学位
					</c:if>
					<c:if test="${thesisApply.applyDegree == 1}">
						有意向申请学位
					</c:if>
		          </div>
		          <div class="text-center gray6">学位意向</div>
		        </div>
			</c:if>
	        <div class="col-sm-2_6 col-xs-2">
	          <div class="f24 text-center">
	          	<c:choose>
					<c:when test="${not empty thesisApply.reviewScore}">
						${thesisApply.reviewScore}
					</c:when>
					<c:otherwise>
						--
					</c:otherwise>
				</c:choose>
	          </div>
	          <div class="text-center gray6">论文初评</div>
	        </div>
	        <div class="col-sm-2_6 col-xs-2">
	          <div class="f24 text-center">
	          	<c:choose>
					<c:when test="${not empty thesisApply.defenceScore}">
						${thesisApply.defenceScore}
					</c:when>
					<c:otherwise>
						--
					</c:otherwise>
				</c:choose>
	          </div>
	          <div class="text-center gray6">论文答辩</div>
	        </div>
	        <div class="col-sm-2_6 col-xs-2">
	          <div class="f24 text-center">
	          	<c:choose>
					<c:when test="${not empty practiceApply.reviewScore}">
						${practiceApply.reviewScore}
					</c:when>
					<c:otherwise>
						--
					</c:otherwise>
				</c:choose>
	          </div>
	          <div class="text-center gray6">社会实践</div>
	        </div>
	        <div class="col-sm-2_6 col-xs-2">
	          <div class="f24 text-center" data-role="plea-style">
	           	 <c:choose>
					<c:when test="${thesisApply.needDefence == 0}">
						无需答辩
					</c:when>
					<c:when test="${thesisApply.needDefence == 1}">
						现场答辩
					</c:when>
					<c:when test="${thesisApply.needDefence == 2}">
						远程答辩
					</c:when>
				</c:choose>
	          </div>
	          <div class="text-center gray6">
	            	答辩方式
	            <c:if test="${studentInfo.pycc == 2 || studentInfo.pycc == 8}">
					<a href="javascript:modifyDefence('${thesisApply.applyId}')" data-role="set" data-toggle="tooltip" title="设置答辩方式">
		              <i class="fa fa-gear f16 vertical-middle"></i>
		            </a>
				</c:if>
	          </div>
	        </div>
	        <c:if test="${thesisApply.status == 5}">
	        	<div class="col-sm-2_6 col-xs-2">
		          <div class="f24 text-center" data-role="plea-style">
		           	 <button type="button" class="btn btn-sm min-width-60px btn-primary" onclick="confirm()">定稿</button>
			         <button type="button" class="btn btn-sm min-width-60px btn-primary" onclick="reject()">审核不通过</button>
		          </div>
		          <div class="text-center gray6">
		            	学院定稿操作
		          </div>
		        </div>
			</c:if>
	      </div>
	    </div>
	 </div>
	
	<div class="nav-tabs-custom">
		<ul id="myTab" class="nav nav-tabs nav-tabs-lg">
		    <li class="active">
		        <a href="#thesis" data-toggle="tab">毕业论文</a>
		    </li>
		    <li>
		    	<a href="#practice" data-toggle="tab">社会实践</a>
		    </li>
		</ul>
	
		<div id="myTabContent" class="tab-content" style="padding-top: 10px;">
			<div class="tab-pane fade in active" id="thesis">
				<c:if test="${not empty thesisApply}">
					<div class="panel panel-default">
			          <div class="panel-body">
			            <div class="approval-list approval-list-2 clearfix">
			              <dl class="approval-item">
			                <dt class="clearfix">
			                  <div class="a-tit">
			                    <b>申请论文写作 </b>
			                  </div>
			                  <span class="time state-lb"><fmt:formatDate value="${thesisApply.createdDt}" pattern="yyyy-MM-dd HH:mm"/></span>
			                </dt>
			              </dl>
			              <c:forEach items="${thesisProgList}" var="prog" varStatus="status">
				              <dl class="approval-item">
				                <dt class="clearfix">
				                  <div class="a-tit">
				                    <b>${thesisProgressCodeMap[prog.progressCode]} </b>
				                  </div>
				                  <span class="time state-lb"><fmt:formatDate value="${prog.createdDt}" pattern="yyyy-MM-dd HH:mm"/></span>
				                </dt>
				                <dd>
					                <c:if test="${prog.progressCode eq GraduationProgressCodeEnum.THESIS_STAY_OPEN.code}">
					                  <div class="txt media">
					                    <div class="media-left">
					                      	<c:choose>
			                					<c:when test="${not empty thesisApply.guideTeacher1.zp}">
			                						<img src="${thesisApply.guideTeacher1.zp }" class="img-circle" alt="User Image" width="45" height="45">
			                					</c:when>
			                					<c:otherwise>
			                						<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" alt="User Image" width="45" height="45">
			                					</c:otherwise>
		                					</c:choose>
					                      <div class="text-center f12">${thesisApply.guideTeacher1.xm }</div>
					                    </div>
					                  </div>
			                    	</c:if>
			                    	<c:forEach items="${thesisProgRecord[prog.progressId]}" var="record">
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
						                        <a href="${record.attachment}">${record.attachmentName}</a>
						                      </div>
						                    </div>
						                  </div>
			                    	</c:forEach>
				                </dd>
				              </dl>
				          </c:forEach>
			            </div>
			          </div>
			        </div>
				</c:if>
			</div>
		
			<div class="tab-pane fade" id="practice">
				<c:if test="${not empty practiceApply}">
					<div class="panel panel-default">
			          <div class="panel-body">
			            <div class="approval-list approval-list-2 clearfix">
			              <dl class="approval-item">
			                <dt class="clearfix">
			                  <div class="a-tit">
			                    <b>申请社会实践 </b>
			                  </div>
			                  <span class="time state-lb"><fmt:formatDate value="${practiceApply.createdDt}" pattern="yyyy-MM-dd HH:mm"/></span>
			                </dt>
			              </dl>
			              <c:forEach items="${practiceProgList}" var="prog" varStatus="status">
				              <dl class="approval-item">
				                <dt class="clearfix">
				                  <div class="a-tit">
				                    <b>${practiceProgressCodeMap[prog.progressCode]} </b>
				                  </div>
				                  <span class="time state-lb"><fmt:formatDate value="${prog.createdDt}" pattern="yyyy-MM-dd HH:mm"/></span>
				                </dt>
				                <dd>
					                <c:if test="${prog.progressCode eq GraduationProgressCodeEnum.PRACTICE_STAY_OPEN.code}">
					                  <div class="txt media">
					                    <div class="media-left">
					                      <img src="${practiceApply.guideTeacher1.zp }" class="img-circle" alt="User Image" width="45" height="45">
					                      <div class="text-center f12">${practiceApply.guideTeacher1.xm }</div>
					                    </div>
					                  </div>
			                    	</c:if>
			                    	<c:forEach items="${practiceProgRecord[prog.progressId]}" var="record">
						                  <div class="txt media">
						                    <div class="media-left">
						                    	<c:choose>
				                					<c:when test="${record.isStudent == 1}">
				                						<img src="${record.gjtStudentInfo.avatar}" class="img-circle" alt="User Image" width="45" height="45">
					                          			<div class="text-center f12">${record.gjtStudentInfo.xm}</div>
				                					</c:when>
				                					<c:otherwise>
				                						<img src="${record.teacher.zp}" class="img-circle" alt="User Image" width="45" height="45">
					                          			<div class="text-center f12">${record.teacher.xm}</div>
				                					</c:otherwise>
				                				</c:choose>
						                    </div>
						                    <div class="media-body pad-l15 f12">
						                      <div><fmt:formatDate value="${record.createdDt}" pattern="yyyy-MM-dd HH:mm"/></div>
						                      <div class="margin_t5">${record.content}</div>
						                      <div class="margin_t5">
						                        <a href="${record.attachment}">${record.attachmentName}</a>
						                      </div>
						                    </div>
						                  </div>
			                    	</c:forEach>
				                </dd>
				              </dl>
				          </c:forEach>
			            </div>
			          </div>
			        </div>
				</c:if>
			</div>
		</div>
	</div>
</section>

		<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

		<div id="modifyGuideTeacherModal1" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="modifyGuideTeacherLabel1" aria-hidden="true"> 
	    	<div class="modal-dialog">
	    		<div class="modal-content">
	        <div class="modal-header"> 
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modifyGuideTeacherLabel1">修改论文指导老师</h4>
	        </div> 
	        <div class="modal-body"> 
	        	<form method="post" id="modifyForm1" class="form-horizontal" role="form" action="${ctx}/graduation/apply/modifyGuideTeacher.do">
					<input type="hidden" id="applyId1" name="applyId" value=""/>
					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-3 control-label">论文指导老师:</label>
							<div class="col-sm-8">
								<select id="teacherId1" name="teacherId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<c:forEach items="${thesisGuideTeacherList}" var="thesisGuideTeacher">
										<option value="${thesisGuideTeacher.employeeId}"  <c:if test="${thesisGuideTeacher.employeeId==thesisApply.guideTeacher}">selected='selected'</c:if>>${thesisGuideTeacher.xm}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
                </form>
	        </div> 
	        <div class="modal-footer"> 
	        	<button type="button" id="confirmB1" onclick="confirm1()" class="btn btn-success">确定</button>
	            <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
	        </div> 
	        	</div>
	        </div>
	    </div>
	    
		<div id="modifyGuideTeacherModal2" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="modifyGuideTeacherLabel2" aria-hidden="true"> 
	    	<div class="modal-dialog">
	    		<div class="modal-content">
	        <div class="modal-header"> 
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modifyGuideTeacherLabel2">修改社会实践指导老师</h4>
	        </div> 
	        <div class="modal-body"> 
	        	<form method="post" id="modifyForm2" class="form-horizontal" role="form" action="${ctx}/graduation/apply/modifyGuideTeacher.do">
					<input type="hidden" id="applyId2" name="applyId" value=""/>
					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-3 control-label">社会实践指导老师:</label>
							<div class="col-sm-8">
								<select id="teacherId2" name="teacherId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<c:forEach items="${practiceGuideTeacherList}" var="practiceGuideTeacher">
										<option value="${practiceGuideTeacher.employeeId}"  <c:if test="${practiceGuideTeacher.employeeId==practiceApply.guideTeacher}">selected='selected'</c:if>>${practiceGuideTeacher.xm}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
                </form>
	        </div> 
	        <div class="modal-footer"> 
	        	<button type="button" id="confirmB2" onclick="confirm2()" class="btn btn-success">确定</button>
	            <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
	        </div> 
	        	</div>
	        </div>
	    </div>
	    
		<div id="modifyDefenceModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="modifyDefenceLabel" aria-hidden="true"> 
	    	<div class="modal-dialog">
	    		<div class="modal-content">
	        <div class="modal-header"> 
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modifyDefenceLabel">修改答辩方式</h4>
	        </div> 
	        <div class="modal-body"> 
	        	<form method="post" id="modifyDefenceForm" class="form-horizontal" role="form" action="${ctx}/graduation/apply/modifyDefence.do">
					<input type="hidden" id="applyId3" name="applyId" value=""/>
					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-3 control-label">答辩方式:</label>
							<div class="col-sm-8">
								<select id="defence" name="defence" class="selectpicker show-tick form-control" data-size="5">
									<option value="1">现场答辩</option>
									<option value="2">远程答辩</option>
								</select>
							</div>
						</div>
					</div>
                </form>
	        </div> 
	        <div class="modal-footer"> 
	        	<button type="button" id="confirmDefenceB" onclick="confirmDefence()" class="btn btn-success">确定</button>
	            <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
	        </div> 
	        	</div>
	        </div>
	    </div>

</body>
</html>
