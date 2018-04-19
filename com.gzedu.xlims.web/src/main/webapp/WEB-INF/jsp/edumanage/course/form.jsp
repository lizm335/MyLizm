<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<jsp:include page="/eefileupload/upload.jsp"/>
<script type="text/javascript">
$(function() {
	
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('[data-role="back-off"]').removeAttr("disabled");
		$('#btn-back').removeAttr("disabled");
		$('#btn-submit,.fa-remove').remove();  
	}
})
</script> 

</head>
<body class="inner-page-body">
<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">课程管理</a></li>
		<li class="active">新建课程</li>
	</ol>
</section>
<section class="content">
	<div class="box no-margin">
	<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/edumanage/course/${action}" method="post">
                <input id="action" type="hidden" name="action" value="${action }">
                <input type="hidden" name="courseId" value="${entity.courseId }">
                <input type="hidden" name="courseNature" value="${param.courseNature }">
		<div class="box-body pad-t15">
			<table class="table vertical-middle no-border table-fixed">
				<tbody>
					<tr>
						<%-- <th width="10%" class="text-right">
							<small class="text-red">*</small>所属院校
						</th>
						<td>
							<select name="gjtOrg.id"
							class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
								<c:forEach items="${orgMap}" var="org">
										<option value="${org.key}" <c:if test="${org.key==item.gjtOrg.id}">selected='selected'</c:if>>${org.value}</option>
								</c:forEach>
							</select>
						</td> --%>
						
						<th width="10%" class="text-right">
							<small class="text-red">*</small>课程代码
						</th>
						<td>
							<div id="kchDiv" class="position-relative" data-role="valid">
								<c:if test="${entity.isEnabled == 0 || entity.isEnabled == 2 || entity.isEnabled == 3 || action eq 'create'}">
								<input type="text" name="kch" id="kch" class="form-control" value="${entity.kch }"  datatype="*" nullmsg="请填写课程代码！">
								</c:if>
								<c:if test="${entity.isEnabled == 1 || entity.isEnabled == 5}">
									${entity.kch }
								</c:if>
								<%-- <input type="text" name="kch" id="kch" class="form-control" value="${entity.kch }"  datatype="*" nullmsg="请填写课程代码！" ajaxurl="${ctx}/edumanage/course/checkLogin?kch=this.value"> --%>
							</div>
						</td>
						
						<c:choose>
							<c:when test="${param.courseNature != 3}">
								<th width="10%" class="text-right">
									<small class="text-red">*</small>课程层次
								</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select name="pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" datatype="*" nullmsg="请选择课程层次！" errormsg="请选择课程层次！">
											<option value="">请选择</option>
											<c:forEach items="${pyccMap}" var="map">
												<option value="${map.key}" <c:if test="${map.key==entity.pycc}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</td>
							</c:when>
							<c:otherwise>
								<th width="10%" class="text-right">
									<small class="text-red">*</small>可替换课
								</th>
								<td>
									<div class="position-relative" data-role="valid">
										<select name="replaceCourseId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" multiple datatype="*" nullmsg="请选择可替换课！" errormsg="请选择可替换课！">
											<option value="">请选择</option>
											<c:forEach items="${replaceCourses}" var="course">
												<c:set var="flag" value="${false}"></c:set>
												<c:forEach items="${replaceCourseIds}" var="courseId">
													<c:if test="${course.courseId eq courseId}">
														<c:set var="flag" value="${true}"></c:set>
													</c:if>
												</c:forEach>
												<option value="${course.courseId}" <c:if test="${flag}">selected='selected'</c:if>>${course.kcmc}(${course.kch})</option>
											</c:forEach>
										</select>
									</div>
								</td>
							</c:otherwise>
						</c:choose>
						
					</tr>
					<tr>
						
						<th class="text-right">
							<small class="text-red">*</small>课程名称
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<input type="text" name="kcmc" class="form-control" value="${entity.kcmc }" datatype="*" nullmsg="请填写课程名称！" errormsg="请填写课程名称！"/>
							</div>
						</td>
						
						<th class="text-right">
							<small class="text-red">*</small>教学方式
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select name="wsjxzk" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" datatype="*" nullmsg="请选择教学方式！" errormsg="请选择教学方式！">
									<option value="">请选择</option>
									<c:forEach items="${wsjxzkMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==entity.wsjxzk}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</td>
						
						<%-- <th class="text-right">
							<small class="text-red">*</small>学科
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select id="subject" name="subject" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择学科！" errormsg="请选择学科！">
									<option value="">请选择</option>
									<c:forEach items="${subjectMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==entity.subject}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</td> --%>
					</tr>
					<!-- <tr> -->
						
						<%-- <th class="text-right">
							<small class="text-red">*</small>课程性质
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select name="courseNature" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" datatype="*" nullmsg="请选择课程性质！" errormsg="请选择课程性质！">
									<option value="">请选择</option>
									<c:forEach items="${courseNatureMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==entity.courseNature}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</td> --%>
					<!-- </tr> -->
					<%-- <tr>
						
						<th class="text-right">
							<small class="text-red">*</small>教学方式
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select name="wsjxzk" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" datatype="*" nullmsg="请选择教学方式！" errormsg="请选择教学方式！">
									<option value="">请选择</option>
									<c:forEach items="${wsjxzkMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==entity.wsjxzk}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</td>
						
						<th class="text-right">
							<small class="text-red">*</small>学科门类
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select id="category" name="category" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" datatype="*" nullmsg="请选择门类！" errormsg="请选择门类！">
									<option value="">请选择</option>
									<c:forEach items="${categoryMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==entity.category}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr> --%>
					<c:if test="${param.courseNature != 3}">
					<tr>
						
						<%-- <th class="text-right">
							<small class="text-red">*</small>课程类型
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select name="courseType" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择课程类型！" errormsg="请选择课程类型！">
									<option value="">请选择</option>
									<c:forEach items="${courseTypeMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==entity.courseType}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</td> --%>
						
						<%-- <th class="text-right">
							<small class="text-red">*</small>适用行业
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select name="syhy" class="selectpicker show-tick form-control" data-size="5" data-live-search="true"  datatype="*" nullmsg="请选择所属行业！" errormsg="请选择所属行业！">
									<option value="">请选择</option>
									<c:forEach items="${syhyMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==entity.syhy}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>               
				                </select>
			                </div>
						</td> --%>
						
						<th class="text-right">
							<small class="text-red">*</small>课程类型
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select name="courseCategory" class="selectpicker show-tick form-control" datatype="*" nullmsg="请选择课程类型！" errormsg="请选择课程类型！">
									<option value="">请选择</option>
									<c:forEach items="${courseCategoryMap }" var="map">
										<option value="${map.key }" <c:if test="${entity.courseCategory eq map.key}">selected='selected'</c:if>>${map.value }</option>
									</c:forEach>
				                </select>
			                </div>
						</td>
						
						<th class="text-right">
							<small class="text-red">*</small>适用专业
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<select name="syzy" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" multiple datatype="*" nullmsg="请选择所属专业！" errormsg="请选择所属专业！">
									<option value="">请选择</option>
									<c:forEach items="${syzyMap}" var="map">
										<c:set var="flag" value="${false}"></c:set>
										<c:forEach items="${syzys}" var="syzy">
											<c:if test="${map.key eq syzy}">
												<c:set var="flag" value="${true}"></c:set>
											</c:if>
										</c:forEach>
										<option value="${map.key}" <c:if test="${flag}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>   
				                </select>
			                </div>
						</td>
					</tr>
					<tr>
						
						<th class="text-right">
							<small class="text-red">*</small>课程学分
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<div class="input-group">
									<input type="text" class="form-control" name="credit"  value="${entity.credit }" datatype="n" nullmsg="请填写课程学分！" errormsg="请填写数字！">
									<div class="input-group-addon">学分</div>
								</div>
							</div>
						</td>
						
						<th class="text-right">
							<small class="text-red">*</small>课程学时
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<div class="input-group">
									<input type="text" class="form-control"  name="hour"  value="${entity.hour }" datatype="n" nullmsg="请填写课程学时！" errormsg="请填写数字！">
									<div class="input-group-addon">学时</div>
								</div>
							</div>
						</td>
					</tr>
					</c:if>
					<tr>
					
						<c:if test="${param.courseNature == 3}">
							<th class="text-right">
								<small class="text-red">*</small>课程类型
							</th>
							<td>
								<div class="position-relative" data-role="valid">
									<select name="courseCategory" class="selectpicker show-tick form-control" datatype="*" nullmsg="请选择课程类型！" errormsg="请选择课程类型！">
										<option value="">请选择</option>
										<c:forEach items="${courseCategoryMap }" var="map">
											<option value="${map.key }" <c:if test="${entity.courseCategory eq map.key}">selected='selected'</c:if>>${map.value }</option>
										</c:forEach>
				                </div>
							</td>
						</c:if>
						
						<th class="text-right">
							<small class="text-red">*</small>课程标签
						</th>
						<td>
							<div class="position-relative" data-role="valid">
								<input type="text" name="label" class="form-control" value="${entity.label }" placeholder="输入标签，多个用#号隔开" datatype="*" nullmsg="请填写课程标签！" errormsg="请填写课程标签！"/>
							</div>
						</td>
						
						<%-- <th class="text-right">
							<small class="text-red">*</small>课程状态
						</th>
						<td>
							<div class="radio">
		                        <label>
		                          <input type="radio" name="isEnabled" value="1" <c:if test="${entity.isEnabled == 1 or empty entity.isEnabled }">checked</c:if> />已启用
		                        </label>
		                        <label class="margin_l15">
		                          <input type="radio" name="isEnabled" value="0" <c:if test="${entity.isEnabled == 0 }">checked</c:if> />未启用
		                        </label>
		                    </div>
						</td> --%>
					</tr>
					<!-- <tr>

						<th class="text-right">
							
						</th>
						<td>
							
						</td>
					</tr> -->
				</tbody>
			</table>

			<hr>

			<table class="table vertical-middle no-border table-fixed">
				<tbody>
					<tr>
						<th width="10%" class="text-right">
							<small class="text-red">*</small>课程考核
						</th>
						<td>
							<div class="radio">
		                        <label>
		                          <input type="radio" name="assessment" class="minimal" value="1" <c:if test="${entity.assessment == 1 or empty entity.assessment }">checked</c:if> />双考核
		                        </label>
		                        <label class="margin_l15">
		                          <input type="radio" name="assessment" class="minimal" value="2"  <c:if test="${entity.assessment == 2 }">checked</c:if>/>达到分数合格
		                        </label>
		                        <label class="margin_l15">
		                          <input type="radio" name="assessment" class="minimal" value="3"  <c:if test="${entity.assessment == 3 }">checked</c:if>/>完成必修活动
		                        </label>
		                        <label class="margin_l15">
		                          <input type="radio" name="assessment" class="minimal" value="4"  <c:if test="${entity.assessment == 4 }">checked</c:if>/>无考核
		                        </label>
		                    </div>
						</td>

						<th width="10%" class="text-right">
							
						</th>
						<td>
							
						</td>
					</tr>
					<tr>
						<th class="text-right qualified">
							<small class="text-red">*</small>合格分
						</th>
						<td class="qualified">
							<div class="position-relative">
								<div class="input-group">
									<input type="text" class="form-control"  name="qualified"  value="${entity.qualified }" datatype="n" ignore="ignore" errormsg="请填写数字！">
									<div class="input-group-addon">分</div>
								</div>
							</div>
						</td>

						<th class="text-right activity">
							必修活动
						</th>
						<td class="activity">
							<div class="position-relative">
								<div class="input-group">
									<input type="text" class="form-control"  name="activity"  value="${entity.activity }" >
									<div class="input-group-addon">个</div>
								</div>
							</div>
						</td>
					</tr>
					<tr class="khsm">
						<th class="text-right" style="vertical-align:top !important;">
							<!-- <small class="text-red">*</small> -->考核说明
						</th>
						<td colspan="3">
							<div class="position-relative" data-role="valid">
								<textarea id="khsm" rows="5" class="form-control" name="khsm" placeholder="考核说明">${entity.khsm }</textarea>
							</div>
						</td>
					</tr>
					<tr>
						<th class="text-right" style="vertical-align:top !important;">
							<!-- <small class="text-red">*</small> -->课程简介
						</th>
						<td colspan="3">
							<div class="position-relative" data-role="valid">
								<textarea id="kcjj" rows="5" class="form-control" name="kcjj" placeholder="课程简介" >${entity.kcjj }</textarea>
							</div>
						</td>
					</tr>
					<%-- <tr>
						<th class="text-right" style="vertical-align:top !important;">
							<div class="pad-t5">
								<!-- <small class="text-red">*</small> -->主教材
							</div>
						</th>
						<td colspan="3">
							<div class="position-relative" data-role="valid">
								<div class="pull-right margin_l10">
									<a href="${ctx}/edumanage/specialty/choiceTextbookList/1" role="button" class="btn btn-default" data-role="select-pop" data-target="s3">选择教材</a>
								</div>
								<div class="select2-container select2-container--default show oh">
									<div class="select2-selection--multiple">
										<ul class="select2-selection__rendered select-container-ul" data-id="s3">
											<c:if test="${not empty entity.gjtTextbookList1 }">
												<c:forEach  items="${entity.gjtTextbookList1}" var="item">
												 <li class="select2-selection__choice">
												 <c:if test="${action!='view' }">
											      <span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>
											     </c:if>
											      <span class="select2-name" title="${item.textbookName}" data-toggle="tooltip" data-container="body"  data-order="${item.textbookCode}">${item.textbookName}</span>
											      <input type="hidden" name = "textbookIds" value="${item.textbookId}"/>			      
											      </li>
											   </c:forEach>
							      			</c:if>
										</ul>			
									</div>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th class="text-right" style="vertical-align:top !important;">
							<div class="pad-t5">
								<!-- <small class="text-red">*</small> -->复习资料
							</div>
						</th>
						<td colspan="3">
							<div class="position-relative" data-role="valid">
								<div class="pull-right margin_l10">
									<a href="${ctx}/edumanage/specialty/choiceTextbookList/2" role="button" class="btn btn-default" data-role="select-pop" data-target="s4">选择教材</a>
								</div>
								<div class="select2-container select2-container--default show oh">
									<div class="select2-selection--multiple">
										<ul class="select2-selection__rendered select-container-ul" data-id="s4">
											<c:if test="${not empty entity.gjtTextbookList2 }">
												<c:forEach  items="${entity.gjtTextbookList2}" var="item">
												 <li class="select2-selection__choice">
												 <c:if test="${action!='view' }">
											      <span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>
											      </c:if>
											      <span class="select2-name" title="${item.textbookName}" data-toggle="tooltip" data-container="body"  data-order="${item.textbookCode}">${item.textbookName}</span>
											      <input type="hidden" name = "textbookIds" value="${item.textbookId}"/>			      
											      </li>
											   </c:forEach>
							      			</c:if>
										</ul>			
									</div>
								</div>
							</div>
						</td>
					</tr> --%>
					<tr>
						<th class="text-right">
							<!-- <small class="text-red">*</small> -->课程封面
						</th>
						<td colspan="3">
							<div class="position-relative" data-role="valid" data-id="valid-img">
								<input type="button" class="btn  btn-default" value="添加封面" onclick="uploadImage('imgId','imgPath');" />
								<div class="inline-block vertical-middle">
									<ul class="img-list clearfix">
										<li>
											<img id="imgId" src="${entity.kcfm }" class="user-image" style="cursor: pointer;" >
											<input id="imgPath" type="hidden" value="${entity.kcfm }" name="kcfm">
											<!-- <button type="button" class="btn flat btn-default btn-sm btn-block">删除</button> -->
										</li>
									</ul>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th class="text-right">
							课程指引
						</th>
						<td colspan="3">
							<button type="button" class="btn  btn-default" onclick="uploadFile('filename','filepath','txt|rar|zip|xls|xlsx|doc|docx|ppt|pdf|jpg|png',50,uploadCallback)" data-role="add-person-btn">选择文件</button>
							<span id="fileBox">
								<c:if test="${not empty(entity.guidePath)}">
									<a href="${entity.guidePath}" target="_blank">
								      	<span >${entity.guideName}</span>
								      	<i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>
								    </a>
								</c:if>
							</span>
							<input type="hidden" id="filename" name="guideName" value="${entity.guideName}"/>
      						<input type="hidden" id="filepath" name="guidePath" value="${entity.guidePath}"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="box-footer text-center">
	      	<button id="btn-submit" type="submit"  class="btn btn-success min-width-90px margin_r15 btn-save-edit">确定</button>
			<button id="btn-back" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
	    </div>
		
		</form>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
//Initialize Select2 Elements
$(".select2").select2({
	placeholder: "--请选择--",
	minimumResultsForSearch: 5//结果数如果超过5个，就出现搜索框
});

CKEDITOR.replace('khsm');
CKEDITOR.replace('kcjj');

//表单验证
;(function(){
	var oldKch = $("#kch").val();
	var $theform=$("#inputForm");

	var htmlTemp='<div class="tooltip top" role="tooltip" style="white-space: nowrap;">'
          +'<div class="tooltip-arrow"></div>'
          +'<div class="tooltip-inner"></div>'
          +'</div>';
	$theform.find('[data-role="valid"]').each(function(index, el) {
		$(this).append(htmlTemp);
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
		        bottom:'100%'
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
         var validator=true;     
         if(oldKch != $("#kch").val() && $("#kch").val()!==" "){
             $.ajax({  
                 type : "post",  
                 url : "${ctx}/edumanage/course/checkLogin.html",  
                 dataType:'json',
                 data : {kch:$('#kch').val()},  
                 async : false,  
                 success : function(data){  
                     if(data.successful){
                         validator=false;
                     }  
                 }
             });
         }
    	 var $kchDiv=$("#kchDiv");
         if (!validator) {
        	 $kchDiv.find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').text('课程代码已存在');
        	 return false;
         } else {
        	 $kchDiv.find('.tooltip').removeClass('in');
         }
         
         var assessment = $("input[name='assessment']:checked").val();
         if (assessment == 1) {
 			if ($("input[name='qualified']").val() == "") {
 				alert("请填写合格分");
 				$("input[name='qualified']").focus();
 				return false;
 			}
 			/* if ($("input[name='activity']").val() == "") {
 				alert("请填写必修活动");
 				$("input[name='activity']").focus();
 				return false;
 			} */
 		 } else if (assessment == 2) {
 			if ($("input[name='qualified']").val() == "") {
 				alert("请填写合格分");
 				$("input[name='qualified']").focus();
 				return false;
 			}
 		 } else if (assessment == 3) {
 			if ($("input[name='activity']").val() == "") {
 				alert("请填写必修活动");
 				$("input[name='activity']").focus();
 				return false;
 			}
 		 }
         
        $("#khsm").val(CKEDITOR.instances.khsm.getData());
        $("#kcjj").val(CKEDITOR.instances.kcjj.getData());
		  
	  	//验证 课程封面
	  	var $validImgBox=$('[data-id="valid-img"]');
	  	var $imgList=$('.img-list',$validImgBox).children('li');
	  	if($imgList.length<=0){
	  		$validImgBox.find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').text('请上传“课程封面”图');
	  		return false;
	  	}
	  	else{
	  		$validImgBox.find('.tooltip').removeClass('in');
	  	}

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
	    //这里执行回调操作;
	    //如果callback里明确return alse，则表单不会提交，如果return true或没有return，则会提交表单。

	    //if("成功提交") 就执行下面语句
		if(data.successful){
			window.location.href = ctx + '/edumanage/course/list?search_EQ_courseNature=${param.courseNature}';
        } else {
        	$.closeDialog(postIngIframe);
          	alert(data.message);
        }  
		
	  }
	});

})();

$("#subject").change(function(){
	var $category = $("#category");
	$category.empty();
	$category.append('<option value="">请选择</option>');
	var value = $(this).children('option:selected').val();
	if (value != "") {
		$.getJSON(ctx+'/edumanage/course/changeSubject', {subject:value}, function (data) {
			$.each(data.obj, function(i,item){
            	if (item.code == '${entity.category}') {
            		$category.append('<option value="'+item.code+'" selected="selected">'+item.name+'</option>');
            	} else {
            		$category.append('<option value="'+item.code+'">'+item.name+'</option>');
            	}
            });
			$category.selectpicker('refresh');
        });
	}
});

//选择课程
$("[data-role='select-pop']").click(function(event) {
	event.preventDefault();

	var $target=$('[data-id="'+$(this).data('target')+'"]');
	$(".select-container-ul").removeClass('on');
	$target.addClass('on')

	$.mydialog({
	  id:'select-course',
	  width:800,
	  height:550,
	  zIndex:11000,
	  content: 'url:'+$(this).attr("href")
	});
});

//删除课程
$(".select-container-ul").on('click', '.select2-selection__choice__remove', function(event) {
	event.preventDefault();
	$("#"+$(this).attr("aria-describedby")).remove();
	$(this).parent().remove();
});

$("#imgId").click(function() {
	var src = $(this).attr("src");
	if (src != "") {
		window.open(src);
	}
});

$("input[name='assessment']").click(function() {
	var value = $(this).val();
	if (value == 1) {
		$(".qualified").show();
		$(".activity").show();
		$(".khsm").show();
	} else if (value == 2) {
		$(".qualified").show();
		$(".activity").hide();
		$(".khsm").show();
	} else if (value == 3) {
		$(".qualified").hide();
		$(".activity").show();
		$(".khsm").show();
	} else if (value == 4) {
		$(".qualified").hide();
		$(".activity").hide();
		$(".khsm").hide();
	}
});
var assessment = '${entity.assessment}';
if (assessment == 2) {
	$(".activity").hide();
} else if (assessment == 3) {
	$(".qualified").hide();
} else if (assessment == 4) {
	$(".qualified").hide();
	$(".activity").hide();
	$(".khsm").hide();
}
$("#fileBox").on("click", ".fa-remove", function() {
    $(this).parent().remove();
    $('#filepath').val('');
    $('#filename').val('');
    return false;
});
var uploadCallback=function(){
    var html=[
    	'<a href="{0}" target="_blank">',
      	'<span >{1}</span>',
      	'<i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>',
    	'</a>'
	];
	html=html.join('').format($('#filepath').val(),$('#filename').val());
	$('#fileBox').html(html);
};

</script>
</body>
</html>