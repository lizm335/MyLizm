<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任管理系统-操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
	$(function() {

		$('#inputForm').bootstrapValidator({
			excluded : [ ':disabled' ],//验证下拉必需加
			fields : {
				gjtSchoolInfoId : {
					validators : {
						notEmpty : {}
					}
				},
				gradeCode : {
					validators : {
						notEmpty : "required"
					}
				},
				gradeName : {
					validators : {
						notEmpty : "required"
					}
				},
				isEnabled : {
					validators : {
						notEmpty : "required"
					}
				},
				enterDts : {
					validators : {
						notEmpty : "required"
					}
				}
			}
		});

		$('#btnXh').click(function() {
			var $xh = $('#xh').val();
			if ($xh == '') {
				$('#checkError').css("display", "block");
				return false;
			}
			$('#checkError').removeAttr("display");
			$('#checkError').css("display", "none");
			$.ajax({
				url : "queryByXh.html",
				data : {
					xh : $xh
				},
				async : false,
				type : 'post',
				dataType : 'json',
				success : function(data) {
					if (data.successful) {
						$('#error').removeAttr("display");
						$('#error').css("display", "none");
						$('#show').css("display", "block");
						
						//学员信息
						$('.xh').html(data.obj.xh);
						$('.xm').html(data.obj.xm);
						$('.xbm').html(data.obj.xbm);
						$('.pycc').html(data.obj.pycc);
						$('.gradeName').html(data.obj.gradeName);
						$('.major').html(data.obj.major);
						$('.studyCenter').html(data.obj.studyCenter);
						$('#studentId').val(data.obj.studentId);
						
					} else {
						$('#error').css("display", "block");
						$('#show').removeAttr("display");
						$('#show').css("display", "none");
					}
				}
			});
		});
		
		var $div=$('.sel-row-item');
		$('input[name="changeType"]').each(function(i){
			$(this).click(function(){
				$div.hide();//隐藏所有内容
				$div.eq(i).show();//显示选中项对应内容
			});
		});
		
		 $('#btnSubmit').click(function(){
			var $value =$('input[name="changeType"]:enabled:checked').val();
			if($value==101){
				var $check=$('#specialtyId').val();
				if($check==""){
					alert('请选择要转的专业');
					return false;
				}
			}
			if($value==103){
				var $check=$('#gradeId').val();
				if($check==""){
					alert('请选择要转的年级');
					return false;
				}
			}
			if($value==102){
				var $check=$('#studyId').val();
				if($check==""){
					alert('请选择要转的学习中心');
					return false;
				}
			}
		}); 
	})
</script>

</head>
<body class="inner-page-body">
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">学籍异动</h3>
					</div>
					<form id="inputForm" class="form-horizontal" role="form"	action="${ctx }/edumanage/changespecialty/create" method="post">
						<div class="box-body pad20">
							<div class="row " >
								<div class="col-sm-5 container">
									<div class="panel panel-default overlay-wrapper pos-rel">
										<div class="panel-heading">
											<b>查找学员</b>
										</div>
										<div class="panel-body">
											<div class="min-height-140">
												<div class="table-block full-width"
													style="min-height: inherit;">
													<div class="table-cell-block vertical-mid text-center">
														<div class="f16 text-bold text-red" id="error"
															style="display: none;">
															<i class="icon fa fa-warning"></i> 找不到该学号对应的学员
														</div>
														<p class="f16 gray6 text-bold">请先输入学号查找对应学员</p>
														<div class="pad20">
															<div class="input-group input-group-sm">
																<input type="text" class="form-control" name="xh" value=""	id="xh" placeholder="学号"> <span
																	class="input-group-btn">
																	<button class="btn btn-flat" type="button" id="btnXh">
																		<i class="fa fa-search"></i>
																	</button>
																</span>
															</div>
															<div class="input-group input-group-sm">
																<div id="checkError" style="display: none; color: red;">请输入学号，学号不能为空</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="overlay hide">
											<i class="fa fa-refresh fa-spin"></i>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row" id="show" style="display: none">
								<div class="col-sm-5">
									<div class="panel panel-default overlay-wrapper pos-rel">
										<div class="panel-heading">
											<div class="form-horizontal">
												<div class="form-group margin-bottom-none">
													<div class="col-xs-5">
														<b>学员信息</b>
													</div>
												</div>
											</div>

										</div>
										<div class="panel-body">
											<div class="min-height-240">
												<div class="table-block full-width stu-info-change-tbl">
													<div class="table-row">
														<div class="table-cell-block">
															<b>学号:</b>
														</div>
														<div class="table-cell-block xh"></div>
													</div>
													<div class="table-row">
														<div class="table-cell-block">
															<b>姓名:</b>
														</div>
														<div class="table-cell-block xm"></div>
													</div>
													<div class="table-row">
														<div class="table-cell-block">
															<b>性别:</b>
														</div>
														<div class="table-cell-block xbm"></div>
													</div>
													<div class="table-row">
														<div class="table-cell-block">
															<b>年级:</b>
														</div>
														<div class="table-cell-block gradeName"></div>
													</div>
													<div class="table-row">
														<div class="table-cell-block">
															<b>层次:</b>
														</div>
														<div class="table-cell-block pycc"></div>
													</div>
													<div class="table-row">
														<div class="table-cell-block">
															<b>专业:</b>
														</div>
														<div class="table-cell-block major">
														</div>
													</div>
													<div class="table-row">
														<div class="table-cell-block">
															<b>学习中心:</b>
														</div>
														<div class="table-cell-block studyCenter"></div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-2">
									<div class="text-center text-light-blue stu-info-change-arrow">
										<i class="fa fa-fw fa-arrow-right"></i>
									</div>
								</div>
								<div class="col-sm-5">
									<div class="panel panel-default overlay-wrapper pos-rel">
										<div class="panel-heading">
											<b>异动变更</b>
										</div>
										<div class="panel-body">
											<div class="min-height-240">
												<div class="table-block full-width stu-info-change-tbl">
													<div class="table-row">
														<div class="table-cell-block">
															<b>异动类型:</b>
														</div>
														<div class="table-cell-block">
															<label class="text-no-bold margin_r10">
																<input  type="radio" name="changeType" checked="checked" value="101" />
																<input type="hidden" name="studentId" value="" id="studentId"/>
																 <span class="text-nowrap">转专业</span>
															</label>
															 <label class="text-no-bold margin_r10">
																<input  type="radio" name="changeType" value="103"/>
																<span class="text-nowrap">转年级</span>
															</label> <label class="text-no-bold margin_r10">
																<input  type="radio" name="changeType" value="102"/>
																<span class="text-nowrap">转学习中心</span>
															</label>
														</div>
													</div>
													<div class="table-row sel-row-item " >
														<div class="table-cell-block">
															<b>专业:</b>
														</div>
														<div class="table-cell-block">
															<select  class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" id="specialtyId" name="specialtyId">
																<option value="">请选择专业</option>
																<c:forEach items="${specialtyMap}" var="map">
																	<option value="${map.key}">${map.value}</option>
																</c:forEach>
															</select>
														</div>
													</div>
													<div class="table-row sel-row-item " style="display: none" > 
														<div class="table-cell-block">
															<b>年级:</b>
														</div>
														<div class="table-cell-block">
															<select class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" id="gradeId" name="gradeId">
																<option value="">请选择年级</option>
																<c:forEach items="${gradeMap}" var="map">
																	<option value="${map.key}">${map.value}</option>
																</c:forEach>
															</select>
														</div>
													</div>
													<div class="table-row sel-row-item " style="display: none" >
														<div class="table-cell-block">
															<b>学习中心:</b>
														</div>
														<div class="table-cell-block">
															<select class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" id="studyId" name="studyId">
																<option value="">请选择学习中心</option>
																<c:forEach items="${studyCenterMap}" var="map">
																	<option value="${map.key}">${map.value}</option>
																</c:forEach>
															</select>
														</div>
													</div>
													<div class="table-row">
														<div class="table-cell-block">
															<b>异动原因:</b>
														</div>
														<div class="table-cell-block">
															<textarea class="form-control" rows="3" name="remark"></textarea>
														</div>
													</div>
													<div class="table-row">
														<div class="table-cell-block"></div>
														<div class="table-cell-block">
															<div class="margin_t10">
																<button type="submit" class="btn btn-success margin_r15" id="btnSubmit">保存</button>
																<button type="reset" class="btn btn-primary"onclick="history.back()">返回</button>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>


							<!-- <div class="box-footer">
								<div class="col-sm-offset-1 col-sm-11">
									<button id="btn-submit" type="submit" class="btn btn-primary">确定</button>
									<button type="reset" class="btn btn-primary"
										onclick="history.back()">返回</button>
								</div>
							</div> -->
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>