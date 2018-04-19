<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统-年级管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
    
</script>

</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">学期管理</a>
			</li>
			<li class="active">开设新学期</li>
		</ol>
	</section>
	<section class="content">
		<form id="theform"  action="${ctx}/edumanage/grade/update" method="post">
			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title">开设新学期</h3>
				</div>
				<div class="box-body">

					<div class="form-horizontal reset-form-horizontal">
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>开设年级
							</label>
							<div class="col-sm-7 position-relative">
								<select class="form-control" <c:if test="${action=='update'}">disabled="disabled"</c:if> datatype="*" nullmsg="请选择年级" errormsg="请选择年级" id="yearId">
									<option value="">请选择</option>
									<c:if test="${action!='update'}">
										<c:forEach items="${years}" var="item">
											<option value="${item.gradeId}">${item.name}</option>
										</c:forEach>
									</c:if>
									<c:if test="${action=='update'}">
										<option selected="selected" value="${grade.gjtYear.gradeId}">${grade.gjtYear.name}</option>
									</c:if>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>选择学期
							</label>
							<div class="col-sm-7 position-relative">
								<select class="form-control" name="gradeId" datatype="*" nullmsg="选择学期" errormsg="选择学期" id="gradeId" <c:if test="${action=='update'}">disabled="disabled"</c:if>>
									<option value="">请选择</option>
									<c:if test="${not empty(grade.gradeId)}">
										<option selected="selected"  value="${grade.gradeId}">${grade.gradeName}</option>
									</c:if>
								</select>
								<c:if test="${action=='update'}">
									<input type="hidden" name="gradeId" value="${grade.gradeId}" />
								</c:if>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">学期编号</label>
							<div class="col-sm-7 position-relative">
								<p class="form-control-static" id="gradeCode">${grade.gradeCode}</p>
							</div>
						</div>
					</div>
					<div class="box-header with-border no-pad-top pad-b5 margin_b10">
						<h3 class="box-title text-no-bold">学期时间计划</h3>
					</div>

					<div class="form-horizontal reset-form-horizontal">
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>学期时间
							</label>
							<div class="col-sm-7 position-relative">
								<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
									<div class="form-control-box position-relative">
										<input type="text" name="startDate" value="${grade.startDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly  <c:if test="${!editable}">disabled="disabled"</c:if> >
									</div>
									<p class="input-group-addon">~</p>
									<div class="form-control-box position-relative">
										<input type="text" name="endDate" value="${grade.endDate}"  class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly <c:if test="${!editable}">disabled="disabled"</c:if>>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>课程教学周期
							</label>
							<div class="col-sm-7 position-relative">
								<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
									<div class="form-control-box position-relative">
										<input type="text" name="courseStartDate" value="${grade.courseStartDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
									</div>
									<p class="input-group-addon">~</p>
									<div class="form-control-box position-relative">
										<input type="text" name="courseEndDate" value="${grade.courseEndDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
									</div>
								</div>
							</div>
						</div>
						<c:if test="${!permitted}">
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>上传授课计划截止时间
							</label>
							<div class="col-sm-7 position-relative">
								<input type="text" name="upCourseEndDate" value="${grade.upCourseEndDate}" class="form-control" placeholder="上传授课计划截止时间" datatype="*" nullmsg="请填写时间" errormsg="时间格式不对" data-role="datetime">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>上传成绩截止时间
							</label>
							<div class="col-sm-7 position-relative">
								<input type="text" name="upAchievementDate" value="${grade.upAchievementDate}" class="form-control" placeholder="上传成绩截止时间" datatype="*" nullmsg="请填写时间" errormsg="时间格式不对" data-role="datetime">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>老生缴费日期
							</label>
							<div class="col-sm-7 position-relative">
								<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
									<div class="form-control-box position-relative">
										<input type="text" name="payStartDate" value="${grade.payStartDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
									</div>
									<p class="input-group-addon">~</p>
									<div class="form-control-box position-relative">
										<input type="text" name="payEndDate" value="${grade.payEndDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>老生开学日期
							</label>
							<div class="col-sm-7 position-relative">
								<input type="text" name="oldStudentEnterDate" value="${grade.oldStudentEnterDate}" class="form-control" placeholder="老生开学日期" datatype="*" nullmsg="请填写时间" errormsg="时间格式不对" data-role="datetime">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<small class="text-red">*</small>新生开学日期
							</label>
							<div class="col-sm-7 position-relative">
								<input type="text" name="newStudentEnterDate"  value="${grade.newStudentEnterDate}" class="form-control" placeholder="新生开学日期" datatype="*" nullmsg="请填写时间" errormsg="时间格式不对" data-role="datetime">
							</div>
						</div>
						</c:if>
					</div>
					<c:if test="${!permitted}">
					<div class="box-header with-border no-pad-top pad-b5 margin_b10">
						<h3 class="box-title text-no-bold">学期工作计划安排</h3>
					</div>

					<div class="row">
						<div class="col-sm-9 col-sm-offset-1">
							<div class="table-responsive">
								<table class="table table-bordered vertical-mid text-center">
									<thead class="with-bg-gray">
										<tr>
											<th width="15%">计划类型</th>
											<th>要求制定时间</th>
											<th width="25%">责任岗位</th>
											<th width="25%">责任人</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>招生计划</td>
											<td>
												<div class="position-relative">
													<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
														<div class="form-control-box position-relative">
															<input type="text" name="enrollStartDate" value="${grade.enrollStartDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
														</div>
														<p class="input-group-addon">~</p>
														<div class="form-control-box position-relative">
															<input type="text" name="enrollEndDate" value="${grade.enrollEndDate}"  class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
														</div>
													</div>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control role" name="enrollResponsible" datatype="*" nullmsg="请选择责任岗位" errormsg="请选择责任岗位">
														<option value="">请选择责任岗位</option>
														<c:forEach items="${roleMap}" var="map">								
															<option value="${map.key}" <c:if test="${map.key==grade.enrollResponsible}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control user" temp="${grade.enrollResponsiblePer}" name="enrollResponsiblePer" datatype="*" nullmsg="请选择责任人" errormsg="请选择责任人">
														<option value="">请选择责任人</option>
													</select>
												</div>
											</td>
										</tr>
										<tr>
											<td>学籍计划</td>
											<td>
												<div class="position-relative">
													<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
														<div class="form-control-box position-relative">
															<input type="text" name="schoolrollStartDate" value="${grade.schoolrollStartDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
														</div>
														<p class="input-group-addon">~</p>
														<div class="form-control-box position-relative">
															<input type="text" name="schoolrollEndDate" value="${grade.schoolrollEndDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
														</div>
													</div>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control role" name="schoolrollResponsible" datatype="*" nullmsg="请选择责任岗位" errormsg="请选择责任岗位">
														<option value="">请选择责任岗位</option>
														<c:forEach items="${roleMap}" var="map">								
															<option value="${map.key}" <c:if test="${map.key==grade.schoolrollResponsible}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control user" name="schoolrollResponsiblePer" temp="${grade.schoolrollResponsiblePer}" datatype="*" nullmsg="请选择责任人" errormsg="请选择责任人">
														<option value="">请选择责任人</option>
													</select>
												</div>
											</td>
										</tr>
										<tr>
											<td>教材计划</td>
											<td>
												<div class="position-relative">
													<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
														<div class="form-control-box position-relative">
															<input type="text" name="textbookStartDate" value="${grade.textbookStartDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
														</div>
														<p class="input-group-addon">~</p>
														<div class="form-control-box position-relative">
															<input type="text" name="textbookEndDate" value="${grade.textbookEndDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
														</div>
													</div>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control role" name="textbookResponsible" datatype="*" nullmsg="请选择责任岗位" errormsg="请选择责任岗位">
														<option value="">请选择责任岗位</option>
														<c:forEach items="${roleMap}" var="map">								
															<option value="${map.key}" <c:if test="${map.key==grade.textbookResponsible}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control user" name="textbookResponsiblePer" temp="${grade.textbookResponsiblePer}" datatype="*" nullmsg="请选择责任人" errormsg="请选择责任人">
														<option value="">请选择责任人</option>
													</select>
												</div>
											</td>
										</tr>
										<tr>
											<td>教学计划</td>
											<td>
												<div class="position-relative">
													<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
														<div class="form-control-box position-relative">
															<input type="text" name="teachingStartDate"  value="${grade.teachingStartDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
														</div>
														<p class="input-group-addon">~</p>
														<div class="form-control-box position-relative">
															<input type="text" name="teachingEndDate" value="${grade.teachingEndDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
														</div>
													</div>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control role" name="teachingResponsible" datatype="*" nullmsg="请选择责任岗位" errormsg="请选择责任岗位">
														<option value="">请选择责任岗位</option>
														<c:forEach items="${roleMap}" var="map">								
															<option value="${map.key}" <c:if test="${map.key==grade.teachingResponsible}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control user" name="teachingResponsiblePer" temp="${grade.teachingResponsiblePer}"  datatype="*" nullmsg="请选择责任人" errormsg="请选择责任人">
														<option value="">请选择责任人</option>
													</select>
												</div>
											</td>
										</tr>
										<tr>
											<td>考试计划</td>
											<td>
												<div class="position-relative">
													<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
														<div class="form-control-box position-relative">
															<input type="text" name="examStartDate" value="${grade.examStartDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
														</div>
														<p class="input-group-addon">~</p>
														<div class="form-control-box position-relative">
															<input type="text" name="examEndDate" value="${grade.examEndDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
														</div>
													</div>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control role" name="examResponsible" datatype="*" nullmsg="请选择责任岗位" errormsg="请选择责任岗位">
														<option value="">请选择责任岗位</option>
														<c:forEach items="${roleMap}" var="map">								
															<option value="${map.key}" <c:if test="${map.key==grade.examResponsible}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control user" name="examResponsiblePer" temp="${grade.examResponsiblePer}" datatype="*" nullmsg="请选择责任人" errormsg="请选择责任人">
														<option value="">请选择责任人</option>
													</select>
												</div>
											</td>
										</tr>
										<tr>
											<td>毕业计划</td>
											<td>
												<div class="position-relative">
													<div class="input-group-custom" data-role="daterangetime-group" data-id="1">
														<div class="form-control-box position-relative">
															<input type="text" name="graduationStartDate" value="${grade.graduationStartDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
														</div>
														<p class="input-group-addon">~</p>
														<div class="form-control-box position-relative">
															<input type="text" name="graduationEndDate" value="${grade.graduationEndDate}" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
														</div>
													</div>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control role" name="graduationResponsible" datatype="*" nullmsg="请选择责任岗位" errormsg="请选择责任岗位">
														<option value="">请选择责任岗位</option>
														<c:forEach items="${roleMap}" var="map">								
															<option value="${map.key}" <c:if test="${map.key==grade.graduationResponsible}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control user" name="graduationResponsiblePer" temp="${grade.graduationResponsiblePer}" datatype="*" nullmsg="请选择责任人" errormsg="请选择责任人">
														<option value="">请选择责任人</option>
													</select>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					</c:if>
				</div>
				<div class="box-footer text-center">
					<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="back-off">取消</button>
					<button type="submit" class="btn btn-success min-width-90px " data-role="save1">确认</button>
				</div>
			</div>
		</form>
	</section>
	<script type="text/javascript">
	;(function(){
		var $theform=$("#theform");

		var htmlTemp='<div class="tooltip top" role="tooltip" style="white-space: nowrap;">'
              +'<div class="tooltip-arrow"></div>'
              +'<div class="tooltip-inner"></div>'
              +'</div>';
		$theform.find(":input[datatype]").each(function(index, el) {
			$(this).after(htmlTemp);
		});

		$.Tipmsg.r='';
		var postIngIframe;
		var postForm=$theform.Validform({
		  //showAllError:true,
		  ajaxPost:true,
		  tiptype:function(msg,o,cssctl){
		    //msg：提示信息;
		    //o:{obj:*,type:*,curform:*},
		    //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
		    //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
		    //curform为当前form对象;
		    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
		    if(!o.obj.is("form")){
			    var msgBox=o.obj.siblings('.tooltip');
			    if(msgBox.length<=0){
			    	var $t=$(htmlTemp);
			    	o.obj.after($t);
			    	msgBox=$t;
			    }

			    msgBox.children('.tooltip-inner').text(msg);
			    if(msgBox.hasClass('left')){
			      msgBox.css({
			        width:130,
			        left:-120,
			        top:5
			      })
			    }
			    else{
			      msgBox.css({
			        bottom:28
			      })
			    }

			    switch(o.type){
			      case 3:
			        msgBox.addClass('in');
			        break;
			      default:
			        msgBox.removeClass('in');
			        break;
			    }
		    }
		  },
		  beforeSubmit:function(curform){
		    postIngIframe=$.mydialog({
			  id:'dialog-1',
			  width:150,
			  height:50,
			  backdrop:false,
			  fade:true,
			  showCloseIco:false,
			  zIndex:11000,
			  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
			});
		  },
		  callback:function(data){
		      if(data.successful){
			  	postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>');
			  	location.href='${ctx}/edumanage/grade/list';
			  }else{
					alert(data.message);
					$.closeDialog(postIngIframe);
			  }
		      
		  }
		});

	})();

	;(function(){
		/*日期控件*/
		$('[data-role="daterangetime-group"]').each(function(i,e){
			var startDate=$('[data-role="date-start"]',e);
			var endDate=$('[data-role="date-end"]',e);
			//开始时间			
			startDate.datepicker({
			  language:'zh-CN',
			  format:'yyyy-mm-dd',
			  todayHighlight:true,
			  //orientation:'top'		  
			  //autoclose:true
			}).on('changeDate', function(e) {
				var add=increaseOnedate(e.target.value);
				endDate.datepicker('setStartDate',add);
			});
			//结束时间
			endDate.datepicker({
			  language:'zh-CN',
			  format:'yyyy-mm-dd',
			  todayHighlight:true,
			  //orientation:'top'		  
			  //autoclose:true
			}).on('changeDate', function(e) {
				var d=decreaseOnedate(e.target.value);
				startDate.datepicker('setEndDate',d);
			}).on('focus',function(){
				if(this.value==""&&startDate.val()==""){
					startDate.focus();
					endDate.datepicker('hide');
				}
			});
		});

		/*日期控件*/
		$('[data-role="datetime"]').datepicker({
			language:'zh-CN',
			format:'yyyy-mm-dd'
		});

	})();

	$('#yearId').change(function(){
		if($.trim(this.value)=='') return;
		$.get('${ctx}/edumanage/grade/findGradeByYearId',{
			yearId:$(this).val()
		},function(data){
		    $('#gradeId').find('option').not(':first').remove();
			var html=[];
			for(var key in data){
			    html.push('<option value="'+key+'">'+data[key]+'</option>');
			}
			$('#gradeId').append(html.join(''));
		},'json');
	});

	$('#gradeId').change(function(){
	    $.get('${ctx}/edumanage/grade/getGradeCodeById',{
			gradeId:this.value
		},function(data){
			$('#gradeCode').text(data);
		},
	'json');
	});

	$('.role').change(function(){
		$user= $(this).closest('tr').find('.user');
		$user.find('option').not(':first').remove();
		var roleId=this.value;
		var $this=$(this);
	    $.get('${ctx}/system/user/getUserByRoleId',{roleId:roleId},function(data){
		   if(data){
		       $user= $this.closest('tr').find('.user');
		       var html = [];
		       for(var key in data){
			  	 html.push('<option value="' +key+ '">' +data[key]+ '</option>');
		       };
		       $user.append(html.join(''));
		        if($.trim($user.attr('temp'))!=''){
			   		$user.val($user.attr('temp'));
			   		$user.removeAttr('temp');
			   }
		   }
		},'JSON');
	});

	$(".role").each(function(){
	    if($(this).val()){
			$(this).trigger("change");
	    }
	}); 

	
</script>
</body>
</html>