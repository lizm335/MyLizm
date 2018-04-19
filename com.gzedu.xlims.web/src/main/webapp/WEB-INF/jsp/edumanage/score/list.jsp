<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript" src="${ctx}/static/jquery/jquery.ajaxfileupload.js"></script>

<script type="text/javascript">
$(function() {
	$('#btn-import').change(function(){
		console.log("123");
		$('#importForm').submit();
	});
	
	$('#importForm').submit(function(){
		 $.ajaxFileUpload({  
               type: "post",  
               url: "${ctx}/export/testImportExcel",  
               secureuri:false,  
               fileElementId:"targetFile",  
               dataType: "json",  
               success: function(result,status) {  
                   if (result.success == "1") {  
                       alert("上传文件成功！");  
                       var filename=getFileNameFromFilePath(result.fileRelativePath);  
                       $("#target_upload_info").html("<div>"+"文件:"+filename+"   <a href='javascript:void(0)' onclick='deletefile("+"\""+result.fileRelativePath+"\",\"target\")'>删除</a>"+"<br/></div>");  
                       $("#target_upload_info").css("visibility", "visible");  
                       $("#targetFileRelativePath").val(result.fileRelativePath);  
                   } else {  
                       $("#target_upload_info").html("文件上传失败: " + result.msg);  
                       $("#target_upload_info").css({"visibility":"visible", "color":"red"});  
                   }  
               },  
               complete: function(xmlHttpRequest) {  
                   $("#targetFile").replaceWith('<input type="file" id="targetFile" name="upFile" style="display:none;"/>');  
                   $("#targetFile").on("change", function(){  
                       var filename = $(this).val();  
                       $("#originalTargetFileName").val(filename);  
                   });  
               },  
               error: function(data, status, e) {  
                   alert("文件上传失败!");  
               }  
           });  
           return false;  
	});
})
</script>

</head>
<body class="inner-page-body">
		
		<!-- Main content -->
		<section class="content-header">
			
			<ol class="breadcrumb">
				<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">成绩管理</li>
			</ol>
		</section>

		<section class="content">
			<form id="listForm" class="form-horizontal">
				<div class="box">
					<div class="box-body">
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_gjtStudentInfo.xh" value="${param['search_EQ_gjtStudentInfo.xh']}">
								</div>
							</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_gjtStudentInfo.xm" value="${param['search_EQ_gjtStudentInfo.xm']}">
								</div>
							</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">课程</label>
								<div class="col-sm-9">
									<input class="form-control" type="text" name="search_EQ_gjtCourse.kcmc" value="${param['search_EQ_gjtCourse.kcmc']}">
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer">
<!-- 						<div class="search-more-in">
							高级搜索<i class="fa fa-fw fa-caret-down"></i>
						</div> -->
						<div class="btn-wrap">
							<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="btn-wrap">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
				</div>
				
				
				
				<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
				<div class="box">
					
						<div class="box-header with-border">
							<div class="fr">
								<div class="btn-wrap fl">
									<a href="${ctx }/export/testExportExcel" class="btn btn-block btn-success btn-import">
											<i class="fa fa-fw fa-plus"></i> 成绩导出</a>
								</div>
								<form id="importForm" action="${ctx }/export/testImportExcel" method="post" enctype="multipart/form-data">
									<div class="btn-wrap fl">
										<input id="btn-import" class="btn btn-block btn-danger btn-import" type="file" accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" value="成绩导入">
									</div>
								</form>
								<!-- <div class="btn-wrap fl">
									<a href="javascript:void(0);" class="btn btn-block btn-danger btn-del del-checked">
											<i class="fa fa-fw fa-trash-o"></i> 删除
									</a>
								</div> -->
							</div>
						</div>
					

					<div class="box-body">
						<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
							<div class="row">
								<div class="col-sm-6"></div>
								<div class="col-sm-6"></div>
							</div>

							<div class="row">
								<div class="col-sm-12">
									<table class="table table-bordered table-striped table-container">
										<thead>
											<tr>
												<th>学号</th>
												<th>姓名</th>
												<th>年级</th>
												<th>学期</th>
												<th>课程名称</th>
												<th>层次</th>
												<th>专业</th>
												<th>学习成绩</th>
												<th>考试成绩</th>
												<th>总成绩</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty pageInfo.content}">
													<c:forEach items="${pageInfo.content}" var="entity">
														<c:if test="${not empty entity}">
															<tr>
																<td>${entity.gjtStudentInfo.xh}</td>
																<td>${entity.gjtStudentInfo.xm}</td> 
																<td>${entity.gjtTermInfo.gjtGrade.gradeName}</td>
																<td>${entity.gjtTermInfo.termName}</td>
																<td>${entity.gjtCourse.kcmc}</td>
																<td>${pyccMap[student.pycc]}</td>
																<td>${entity.gjtStudentInfo.gjtSpecialty.zymc}</td>
																<td>${entity.examScore}</td>
																<td>${entity.examScore1}</td>
																<td>${entity.examScore2}</td>
															</tr>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr>
														<td align="center" colspan="10">暂无数据</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</div>
							<tags:pagination page="${pageInfo}" paginationSize="5" />
						</div>
					</div>
				</div>
		</section>
		</form>
</body>
</html>
