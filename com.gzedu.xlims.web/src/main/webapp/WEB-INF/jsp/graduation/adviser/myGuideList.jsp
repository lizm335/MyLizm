<%@page import="com.gzedu.xlims.pojo.status.GraduationApplyStatusEnum"%>
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

function view(studentId, batchId) {
	/* $ .ajax({
	  type: "GET",
	  url: "${ctx}/graduation/adviser/viewStudentRecord",
	  data: {"studentId":studentId, "batchId":batchId, "applyType":1},
	  cache: false,
	  success: function(html) {
		  $("#modalBody").html(html);
		  $("#viewStudentRecord").modal();
	  },
	  error: function() {
		  alert("请求超时！");
	  }
	}); */
	var url="${ctx}/graduation/adviser/viewStudentRecord?studentId="+studentId+"&batchId="+batchId+"&applyType=${param.applyType}";
	$.mydialog({
		id:'export',
		width:'80%',
		height:550,
		zIndex:11000,
		content: 'url:'+url
	});
}

function submitRecord(studentId, batchId) {
	$("#studentId").val(studentId);
	$("#batchId").val(batchId);
	$("#example").val("");
	$("#exampleName").val("");
	$("#exampleA").attr("href", "");
	$("#exampleA").html("");
	$("#proposeContent").val("");
	$("#submitRecordModal").modal();
}

function submit() {
	/* var example = document.getElementById("example").value;
	if (example == "") 
	{
		alert("请选择要上传的文档！"); 
		return false;
	}

	var exampleName = document.getElementById("exampleName").value;
	if (exampleName == "") 
	{
		alert("请选择要上传的文档！"); 
		return false;
	} */

	var content = document.getElementById("proposeContent").value;
	if (content == "") 
	{
		alert("请输入留言！"); 
		document.getElementById("proposeContent").focus();
		return false;
	}
	
	$("#submitProposeB").attr({"disabled":"disabled"});
	$("#submitProposeB").removeClass("btn-success");
	$("#submitProposeB").addClass("btn-default");
	
	$ .ajax({
	  type: "POST",
	  url: $('#submitRecordF').attr("action"),
	  data: $('#submitRecordF').serialize(),
	  cache: false,
	  success: function(msg) {
		  $('#submitRecordModal').modal('hide');
		  alert(msg);
		  $("#submitProposeB").removeAttr("disabled");
		  $("#submitProposeB").removeClass("btn-default");
		  $("#submitProposeB").addClass("btn-success");
	  },
	  error: function() {
		  alert("请求超时！");
		  $("#submitProposeB").removeAttr("disabled");
		  $("#submitProposeB").removeClass("btn-default");
		  $("#submitProposeB").addClass("btn-success");
	  }
	});
	
}

function confirmDialog(studentId, batchId) {
	$("#confirmStudentId").val(studentId);
	$("#confirmBatchId").val(batchId);
	$("#confirmModal").modal();
}

function confirm() {
	$("#confirmB").attr({"disabled":"disabled"});
	$("#confirmB").removeClass("btn-success");
	$("#confirmB").addClass("btn-default");
	$("#confirmF").submit();
}

function uploadCallback() {
	$("#exampleA").attr("href", $("#example").val());
	$("#exampleA").html($("#exampleName").val());
}
</script>

</head>
<body class="inner-page-body">
		
		<!-- Main content -->
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">论文管理</a></li>
				<li class="active">我的指导</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<input type="hidden" name="applyType" value="${param.applyType}">
				<div class="box">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">学号</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xh" value="${param['search_LIKE_gjtStudentInfo.xh']}">
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">姓名</label>
									<div class="col-sm-9">
										<input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">论文批次</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtGraduationBatch.batchId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${batchMap}" var="batch">
												<option value="${batch.key}"  <c:if test="${batch.key==param['search_EQ_gjtGraduationBatch.batchId']}">selected='selected'</c:if>>${batch.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">专业</label>
									<div class="col-sm-9">
										<select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
											<option value="" selected='selected'>请选择</option>
											<c:forEach items="${specialtyMap}" var="specialty">
												<option value="${specialty.key}"  <c:if test="${specialty.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${specialty.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">未指导</label>
									<div class="col-sm-9">
										<select name="search_EQ_isGuide" class="form-control">
											<option value="" <c:if test="${empty param['search_EQ_isGuide']}">selected='selected'</c:if>>是</option>
											<option value="1" <c:if test="${param['search_EQ_isGuide'] == 1}">selected='selected'</c:if>>否</option>
											<option value="2" <c:if test="${param['search_EQ_isGuide'] == 2}">selected='selected'</c:if>>全部</option>
										</select>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer">
						<div class="pull-right">
							<button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="pull-right margin_r15">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
				</div>
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="nav-tabs-custom">
					<ul class="nav nav-tabs nav-tabs-lg">
				      <li <c:if test="${param.applyType == 1}">class="active"</c:if>><a href="viewMyGuideList?applyType=1" target="_self">论文指导</a></li>
				      <li <c:if test="${param.applyType == 2}">class="active"</c:if>><a href="viewMyGuideList?applyType=2" target="_self">社会实践指导</a></li>
				    </ul>
					
				    <div class="tab-content">
						
								<c:if test="${param.applyType == 1}">
									<div class="margin_b10">
										<img alt="需要学位" title="需要学位" src="${ctx}/static/images/degree.png">&nbsp;<font color="red">带该标记的学员有学位申请意向，请对这类学员的论文提高标准进行指导。</font>
									</div>
								</c:if>
							
								<div class="table-responsive">
									<table class="table table-bordered table-striped vertical-mid text-center table-font"  style="word-break:break-all; word-wrap:break-all;">
										<thead>
											<tr>
												<th width="180">个人信息</th>
											    <th width="180">报读信息</th>
											    <th width="80">状态</th>
											    <th width="150">最后提交时间</th>
											    <th>文件</th>
											    <th>留言</th>
											    <th width="150">操作</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td>
																	<div class="text-left">
																		姓名：${entity.gjtStudentInfo.xm}
																		<c:if test="${entity.applyDegree == 1}">
																			<img alt="需要学位" title="需要学位" src="${ctx}/static/images/degree.png">
																		</c:if>
																		<br/>
																		学号：${entity.gjtStudentInfo.xh}<br/>
																		<shiro:hasPermission name="/personal/index$privacyJurisdiction">
																		手机：${entity.gjtStudentInfo.sjh}<br/>
																		</shiro:hasPermission>
																		邮箱：${entity.gjtStudentInfo.dzxx}
																	</div>
																</td>
																<td>
																	<div class="text-left">
																		层次：${trainingLevelMap[entity.gjtStudentInfo.pycc]}<br/>
																		年级：${entity.gjtStudentInfo.gjtGrade.gradeName}<br/>
																		专业：${entity.gjtStudentInfo.gjtSpecialty.zymc}
																	</div>
																</td>
																<td>
																	${statusMap[entity.status]}
																</td>
																<td>
																	<c:choose>
																		<c:when test="${not empty records[entity.applyId]}">
																			<fmt:formatDate value="${records[entity.applyId].createdDt}" pattern="yyyy-MM-dd HH:mm"/>
																		</c:when>
																		<c:otherwise>--</c:otherwise>
																	</c:choose>
																</td>
																<td>
																	<c:choose>
																		<c:when test="${not empty records[entity.applyId]}">
																			<a href="${records[entity.applyId].attachment}">${records[entity.applyId].attachmentName}</a>
																		</c:when>
																		<c:otherwise>--</c:otherwise>
																	</c:choose>
																</td>
																<td>
																	<c:choose>
																		<c:when test="${not empty records[entity.applyId]}">
																			${records[entity.applyId].content}
																		</c:when>
																		<c:otherwise>--</c:otherwise>
																	</c:choose>
																</td>
																<td>
																	<div class="data-operion">
																		<shiro:hasPermission name="/graduation/adviser/viewMyGuideList?applyType=1$guide">
																			<a href="javascript:submitRecord('${entity.gjtStudentInfo.studentId}', '${entity.gjtGraduationBatch.batchId}')"
																				class="operion-item" title="指导">
																				指导</a> 
																		</shiro:hasPermission>
																		
																		<shiro:hasPermission name="/graduation/adviser/viewMyGuideList?applyType=1$confirmThesis">	
																			<c:choose>
																				<c:when test="${param.applyType == 1}">
																					<c:choose>
																						<c:when test="${entity.status == 2}">
																							<a href="javascript:confirmDialog('${entity.gjtStudentInfo.studentId}', '${entity.gjtGraduationBatch.batchId}')"
																								class="operion-item" title="开题通过">
																								开题通过</a>
																						</c:when>
																						<c:when test="${entity.status == 4}">
																							<a href="javascript:confirmDialog('${entity.gjtStudentInfo.studentId}', '${entity.gjtGraduationBatch.batchId}')"
																								class="operion-item" title="论文定稿">
																								论文定稿</a>
																						</c:when>
																					</c:choose>
																				</c:when>
																				<c:when test="${param.applyType == 2}">
																					<a href="javascript:confirmDialog('${entity.gjtStudentInfo.studentId}', '${entity.gjtGraduationBatch.batchId}')"
																						class="operion-item" title="定稿">
																						定稿</a>
																				</c:when>
																			</c:choose>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="/graduation/adviser/viewMyGuideList?applyType=1$view">
																			<a href="javascript:view('${entity.gjtStudentInfo.studentId}', '${entity.gjtGraduationBatch.batchId}')"
																				class="operion-item operion-view" title="查看">
																				<i class="fa fa-fw fa-view-more"></i></a> 
																		</shiro:hasPermission>
																	</div>
																</td> 
															</tr>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr>
														<td align="center" colspan="8">暂无数据</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
									<tags:pagination page="${pageInfo}" paginationSize="5" />
								</div>
							
					</div>
				</div>
		</form>
		</section>
		
		<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
		
	    <div id="viewStudentRecord" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
	    	<div class="modal-dialog">
	    		<div class="modal-content">
	        <div class="modal-header"> 
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">指导详情</h4>
	        </div> 
	        <div id="modalBody" class="modal-body"> 
	        </div> 
	        <div class="modal-footer"> 
	            <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
	        </div> 
	        	</div>
	        </div>
	    </div> 
		
	    <div id="submitRecordModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="submitRecordLabel" aria-hidden="true"> 
	    	<div class="modal-dialog">
	    		<div class="modal-content">
	        <div class="modal-header"> 
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="submitRecordLabel">
                	<c:choose>
                		<c:when test="${param.applyType == 1}">论文指导</c:when>
                		<c:when test="${param.applyType == 2}">社会实践指导</c:when>
                	</c:choose>
                </h4>
	        </div> 
	        <div id="modalBody" class="modal-body"> 
	        	<form method="post" name="submitRecordF" id="submitRecordF" class="form-horizontal" role="form" action="${ctx}/graduation/adviser/submitRecord.do">
					<input type="hidden" name="recordType" value="${param.applyType}"/>
					<input type="hidden" id="studentId" name="gjtStudentInfo.studentId" value="">
					<input type="hidden" id="batchId" name="gjtGraduationBatch.batchId" value="">
					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>指导附件:</label>
							<div class="col-sm-8">
								<input type="hidden" id="example" name="attachment" class="form-control" value=""/>
								<input type="hidden" id="exampleName" name="attachmentName" class="form-control" value=""/>
								<a id="exampleA" href="#" target="_blank"></a>
								<button class="btn btn-default" name="headPortrait" type="button" onclick="javascript:uploadFile('exampleName','example','doc|docx|wps', null, uploadCallback)">上传</button>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>指导意见:</label>
							<div class="col-sm-8">
								<textarea id="proposeContent" name="content" class="textareaInput box-sizing-border" rows="5" cols="50" placeholder="请输入指导意见"></textarea>
							</div>
						</div>
					</div>
                </form>
	        </div> 
	        <div class="modal-footer"> 
	        	<button type="button" id="submitProposeB" onclick="submit()" class="btn btn-success">确定</button>
	            <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
	        </div> 
	        	</div>
	        </div>
	    </div>
		
	    <div id="confirmModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="confirmLabel" aria-hidden="true"> 
	    	<div class="modal-dialog">
	    		<div class="modal-content">
	        <div class="modal-header"> 
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="confirmLabel">确认定稿</h4>
	        </div> 
	        <div id="modalBody" class="modal-body"> 
	        	<form method="post" name="confirmF" id="confirmF" class="form-horizontal" role="form" action="${ctx}/graduation/adviser/confirm.do">
					<input type="hidden" name="applyType" value="${param.applyType}"/>
					<input type="hidden" id="confirmStudentId" name="studentId" value="">
					<input type="hidden" id="confirmBatchId" name="batchId" value="">
					<div class="box-body">
						<p>是否确认定稿？</p>
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

<jsp:include page="/eefileupload/upload.jsp" />

</body>
</html>
