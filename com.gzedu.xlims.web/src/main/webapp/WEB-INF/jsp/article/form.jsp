<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>文章管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<!-- ckeditor -->
	<script src="${ctx }/static/plugins/ckeditor/ckeditor.js"></script>
	<!-- iCheck 1.0.1 -->
	<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
	<script src="${ctx}/static/plugins/iCheck/icheck.min.js"></script>
	<script type="text/javascript">
		$(function() {
			if ($('#action').val() == 'view') {
				$(':input').attr("disabled", "disabled");
				$('#btn-back').removeAttr("disabled");
				$('#btn-submit').remove();
			}
			CKEDITOR.replace('editor4');
			//参考： http://bv.doc.javake.cn/examples/
			$('#inputForm').bootstrapValidator({
				excluded : [ ':disabled' ],
				fields : {
					articleCategoryId : {
						validators : {
							notEmpty: {
								message: "请选择文章分类"
							}
						}
					},
					menuId : {
						validators : {
							callback: {
								message: '请选择菜单',
								callback: function(value, validator) {
									var flag = true;
									if($(":input[name='articleCategoryId']").val() == '20000' && value == '') {
										flag = false;
									}
									return flag;
								}
							}
						}
					},
					title : {
						validators : {
							notEmpty: {
								message: "请填写标题"
							}
						}
					},
					specialtyId : {
						validators : {
							callback: {
								message: '请选择专业',
								callback: function(value, validator) {
									var flag = true;
									if($(":input[name='specialtyCk']:checked").val() == '1' && value == '') {
										flag = false;
									}
									return flag;
								}
							}
						}
					},
					gradeId : {
						validators : {
							callback: {
								message: '请选择学期',
								callback: function(value, validator) {
									var flag = true;
									if($(":input[name='gradeCk']:checked").val() == '1' && value == '') {
										flag = false;
									}
									return flag;
								}
							}
						}
					},
					pycc : {
						validators : {
							callback: {
								message: '请选择层次',
								callback: function(value, validator) {
									var flag = true;
									if($(":input[name='pyccCk']:checked").val() == '1' && value == '') {
										flag = false;
									}
									return flag;
								}
							}
						}
					}
				}
			});
			$('.datepickers').datepicker({
				autoclose : true,
				format : 'yyyy-mm-dd', //控件中from和to 显示的日期格式
				language : 'zh-CN',
				todayHighlight : true
			});
			//Flat red color scheme for iCheck
			$('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
				checkboxClass: 'icheckbox_flat-green',
				radioClass: 'iradio_flat-green'
			});

			/*$("#btn-submit").click(function(event) {
				postForm.submitForm();
			});*/
			$(":input[name='articleCategoryId']").change(function () {
				if($(this).val() ==	 '20000') {
					$(":input[name='menuId']").parents('.form-group').show();
				} else {
					$(":input[name='menuId'] option").removeAttr('selected');
					$(":input[name='menuId'] option:first").attr('selected', 'selected');
					$(":input[name='menuId']").selectpicker('refresh');
					$(":input[name='menuId']").parents('.form-group').hide();
				}
			});
			$(":input[name='specialtyCk'] ~ ins").click(function () {
				if($(":input[name='specialtyCk']")[0].checked) {
					$(":input[name='specialtyId']").parent().show();
				} else {
					$(":input[name='specialtyId'] option").removeAttr('selected');
					$(":input[name='specialtyId'] option:first").attr('selected', 'selected');
					$(":input[name='specialtyId']").selectpicker('refresh');
					$(":input[name='specialtyId']").parent().hide();
				}
			});
			$(":input[name='gradeCk'] ~ ins").click(function () {
				if($(":input[name='gradeCk']")[0].checked) {
					$(":input[name='gradeId']").parent().show();
				} else {
					$(":input[name='gradeId'] option").removeAttr('selected');
					$(":input[name='gradeId'] option:first").attr('selected', 'selected');
					$(":input[name='gradeId']").selectpicker('refresh');
					$(":input[name='gradeId']").parent().hide();
				}
			});
			$(":input[name='pyccCk'] ~ ins").click(function () {
				if($(":input[name='pyccCk']")[0].checked) {
					$(":input[name='pycc']").parent().show();
				} else {
					$(":input[name='pycc'] option").removeAttr('selected');
					$(":input[name='pycc'] option:first").attr('selected', 'selected');
					$(":input[name='pycc']").selectpicker('refresh');
					$(":input[name='pycc']").parent().hide();
				}
			});
			if ($('#action').val() == 'doUpdateArticle') {
				$(":input[name='articleCategoryId']").trigger('change');
			}
		})
	</script>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px"
			data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="${ctx}/article/list">文章管理</a></li>
			<li class="active">新增文章</li>
		</ol>
	</section>
	<section class="content">
		<div class="col-md-12">
			<div class="box margin-bottom-none">
				<form id="inputForm" class="form-horizontal" role="form"
					action="${ctx }/article/${action}" method="post">
					<input id="action" type="hidden" name="action" value="${action}">
					<input type="hidden" name="id" value="${gjtArticle.id}">
					<div class="box-body">
						<div class="form-group">
							<label class="col-sm-2 control-label"><small
								class="text-red">*</small>文章分类</label>
							<div class="col-sm-8">
								<select name="articleCategoryId"
									class="form-control selectpicker">
									<option value="">- 请选择 -</option>
									<c:forEach items="${articleCategoryMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==gjtArticle.articleCategoryId}">selected='selected'</c:if>>
											${map.value}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><small
								class="text-red">*</small>所属菜单</label>
							<div class="col-sm-8">
								<select name="menuId" class="form-control selectpicker">
									<option value="">- 请选择 -</option>
									<c:forEach items="${menuMap}" var="map">
										<option value="${map.key}"
											<c:if test="${map.key==gjtArticle.menuId}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><small
								class="text-red">*</small>标题</label>
							<div class="col-sm-8">
								<input type="text" name="title" class="form-control"
									value="${gjtArticle.title}" placeholder="请输入标题" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">有效日期</label>
							<div class="col-sm-8">
								<div class="input-group">
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" id="effectiveStimeStr"
											class="form-control datepickers" name="effectiveStimeStr"
											placeholder="有效开始时间"
											value="<fmt:formatDate value="${gjtArticle.effectiveStime}" pattern="yyyy-MM-dd"/>" />
									</div>
									<div class="input-group-addon no-border">至</div>
									<div class="input-group full-width">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" id="effectiveEtimeStr"
											class="form-control datepickers" name="effectiveEtimeStr"
											placeholder="有效结束时间"
											value="<fmt:formatDate value="${gjtArticle.effectiveEtime}" pattern="yyyy-MM-dd"/>" />
									</div>

								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><!-- <small
								class="text-red">*</small> -->专业</label>
							<%-- <label class="col-sm-1">
								<input type="checkbox" name="specialtyCk" class="flat-red" value="1" ${action=='doCreateArticle' || not empty gjtArticle.specialtyId ? 'checked' : ''} />
							</label> --%>
							<div class="col-sm-7" ${action=='doCreateArticle' || not empty gjtArticle.specialtyId ? '' : 'style="display:none;"'}>
								<select name="specialtyId" class="form-control selectpicker">
									<option value="">- 请选择 -</option>
									<c:forEach items="${specialtyMap}" var="map">
										<option value="${map.key}"
											<c:if test="${map.key==gjtArticle.specialtyId}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label"><!-- <small
								class="text-red">*</small> -->学期</label>
							<%-- <label class="col-sm-1">
								<input type="checkbox" name="gradeCk" class="flat-red" value="1" ${action=='doCreateArticle' || not empty gjtArticle.gradeId ? 'checked' : ''} />
							</label> --%>
							<div class="col-sm-7" ${action=='doCreateArticle' || not empty gjtArticle.gradeId ? '' : 'style="display:none;"'}>
								<select name="gradeId" class="form-control selectpicker">
									<option value="">- 请选择 -</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}"
											<c:if test="${map.key==gjtArticle.gradeId}">selected='selected'</c:if>>${map.value}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label"><!-- <small
								class="text-red">*</small> -->层次</label>
							<%-- <label class="col-sm-1">
								<input type="checkbox" name="pyccCk" class="flat-red" value="1" ${action=='doCreateArticle' || not empty gjtArticle.pycc ? 'checked' : ''} />
							</label> --%>
							<div class="col-sm-7" ${action=='doCreateArticle' || not empty gjtArticle.pycc ? '' : 'style="display:none;"'}>
								<select name="pycc" class="form-control selectpicker">
									<option value="">- 请选择 -</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}"
											<c:if test="${map.key==gjtArticle.pycc}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label"><small
								class="text-red">*</small>内容</label>
							<div class="col-sm-8">
								<textarea id="editor4" name="content" rows="10" cols="80">${gjtArticle.content}</textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">上传附件</label>
							<div class="col-sm-8">
								<input  type="text" id="fileName" name="fileName" value="${gjtArticle.fileName}" readonly="readonly">
								<input  type="hidden" id="fileUrl"  name="fileUrl" value="${gjtArticle.fileUrl}"> 
								<input value="选择文件" type="button"
									onclick="javascript:uploadFile('fileName','fileUrl','xls|xlsx|doc|docx|ppt|pptx|pdf|rar|zip');">
							</div>
						</div>
					</div>
					<div class="form-group margin-bottom-none">
						<label class="col-xs-2 control-label margin_b15 sr-only"></label>
						<div class="col-xs-5 margin_b15">
							<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
							<button id="btn-back" class="btn btn-primary" onclick="history.back()">返回</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</section>
	
	<jsp:include page="/eefileupload/upload.jsp"/>
</body>
</html>