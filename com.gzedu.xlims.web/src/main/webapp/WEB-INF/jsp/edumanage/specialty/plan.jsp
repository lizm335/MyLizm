<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>专业操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">专业规则</a></li>
		<li class="active">设置专业规则</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom">
		<ul class="nav nav-tabs nav-tabs-lg">
	      <li data-role="show"><a href="javascript:void(0)">1.设置专业基础信息</a></li>
	      <li data-role="show" class="active"><a href="javascript:void(0)">2.设置专业规则</a></li>
	      <li data-role="show"><a href="javascript:void(0)">3.发布专业</a></li>
	    </ul>
	    <div class="tab-content no-padding">
	    	<div class="tab-pane active" id="tab_top_2">
	    		<form id="inputForm" class="theform" action="${ctx }/edumanage/specialty/savePlan" method="post">
	    			<input type="hidden" name="specialtyId" value="${entity.specialtyId}">
		    		<div class="pad">
			    		<h3 class="cnt-box-title f16 text-bold margin_b10">学分设置</h3>
						<div class="margin-bottom-none">
							<table class="table-gray-th">
								<tr>
									<th width="13%" class="text-right"><small class="text-red">*</small> 最低毕业学分：</th>
									<td width="20%">
										<div id="zdbyxfDiv" class="position-relative" data-role="valid">
											<input id="zdbyxf" type="text" class="form-control" placeholder="请输入最低毕业学分" name="zdbyxf"	value='<fmt:formatNumber value="${entity.zdbyxf}" pattern="#.##" type="number"/>' datatype="/^\d+(\.\d+)?$/" nullmsg="请填写最低毕业学分！" errormsg="请填写数字！"/>
										</div>
									</td>

									<th width="14%" class="text-right"><small class="text-red">*</small> 中央电大考试学分：</th>
									<td width="20%">
										<div class="position-relative" data-role="valid">
											<input id="zyddksxf" type="text" class="form-control" placeholder="请输入中央电大考试学分" name="zyddksxf"	value='<fmt:formatNumber value="${entity.zyddksxf}" pattern="#.##" type="number"/>' datatype="/^\d+(\.\d+)?$/" nullmsg="请填写中央电大考试学分！" errormsg="请填写数字！"/>
										</div>
									</td>
									
									<th width="13%" class="text-right"><small class="text-red">*</small> 必修学分：</th>
									<td width="20%">
										<div class="position-relative" data-role="valid">
											<input id="bxxf" type="text" class="form-control" placeholder="请输入必修学分" name="bxxf"	value='<fmt:formatNumber value="${entity.bxxf}" pattern="#.##" type="number"/>' datatype="/^\d+(\.\d+)?$/" nullmsg="请填写必修学分！" errormsg="请填写数字！"/>
										</div>
									</td>
								</tr>
							</table>
						</div>
						
						<div class="clearfix margin_b10 margin_t20">
							<div class="pull-right">
								<a href="#" role="button" class="btn btn-default btn-sm margin_r10" data-role="import-model"><i class="fa fa-fw fa-sign-in"></i> 导入课程模块</a>
							</div>
							<h3 class="cnt-box-title f16 text-bold ">课程模块设置</h3>
						</div>		
						<div class="margin-bottom-none">
							<table class="table table-bordered table-striped vertical-mid text-center margin-bottom-none" data-role="set-model">
								<thead>
									<tr>
										<th width="23%">课程模块</th>
										<th width="23%">模块最低学分</th>
										<th width="23%">模块最低毕业学分</th>
										<th width="23%">模块中央电大考试最低学分</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list }" var="obj">
										<tr>
											<td>
												<div class="position-relative" data-role="valid">
									                 <select name="courseType" class="form-control center-block width-80-per" datatype="*" nullmsg="请选择课程模块！" errormsg="请选择课程模块！">
													 	<option value="">请选择</option>
														<c:forEach items="${courseTypeMap2}" var="map">
															<option value="${map.key}"  <c:if test="${map.key==obj.id}">selected='selected'</c:if>>${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</td>
											<td>
												<div class="position-relative" data-role="valid">
													<input type="text" class="form-control text-center center-block width-80-per" placeholder="模块最低学分" value='<fmt:formatNumber value="${obj.totalScore}" pattern="#.##" type="number"/>' name="totalScore"
														 datatype="/^\d+(\.\d+)?$/" nullmsg="请填写模块最低学分！" errormsg="请填写数字！" />
												</div>
											</td>
											<td>
												<div class="position-relative" data-role="valid">
													<input type="text" class="form-control text-center center-block width-80-per" placeholder="模块最低毕业学分" value='<fmt:formatNumber value="${obj.score}" pattern="#.##" type="number"/>' name="score"
														 datatype="/^\d+(\.\d+)?$/" nullmsg="请填写模块最低毕业学分！" errormsg="请填写数字！" />
												</div>
											</td>
											<td>
												<div class="position-relative" data-role="valid">
													<input type="text" class="form-control text-center center-block width-80-per" placeholder="模块中央电大考试最低学分" value='<fmt:formatNumber value="${obj.crtvuScore}" pattern="#.##" type="number"/>' name="crtvuScore"
														 datatype="/^\d+(\.\d+)?$/" nullmsg="请填写模块中央电大考试最低学分！" errormsg="请填写数字！" />
												</div>
											</td>
											<td>
												<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove-item"><i class="fa fa-trash-o text-red"></i></a>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<a href="javascript:;" role="button" class="btn btn-block text-green nobg margin_t5" data-role="add-model" ><i class="fa fa-fw fa-plus"></i>添加课程模块</a>
						</div>

						<div class="clearfix margin_b10 margin_t20">
							<div class="pull-right">
								<a href="#" role="button" class="btn btn-default btn-sm margin_r10" data-role="add-semester"><i class="fa fa-fw fa-plus"></i> 新增学期</a>
								<a href="#" role="button" class="btn btn-default btn-sm margin_r10" data-role="import-plan"><i class="fa fa-fw fa-sign-in"></i> 导入专业规则</a>
							</div>
							<h3 class="cnt-box-title f16 text-bold ">专业规则</h3>
						</div>

						<div class="box-footer no-pad-left no-pad-right">
							<div data-role="sort-tbl-box">
						      	<div class="table-responsive">
						      		<table id="planTable" class="plan-table tea-plan-tbl table table-bordered table-hover table-font vertical-mid text-center margin-bottom-none" data-semester="第一学期">
							      		<thead>
							      			<tr>
							      				<th>序号</th>
							      				<th>建议学期</th>
							      				<th>课程模块</th>
	      										<th>课程代码</th>
							      				<th>课程名称</th>
							      				<th>课程性质</th>
							      				<th>课程类型</th>
							      				<th>考试单位</th>
							      				<th>学分</th>
							      				<th>学时</th>
							      				<th>操作</th>
							      			</tr>
							      		</thead>
							      		
							      		<tbody id="plan1">
							      		<%-- <c:forEach items="${gjtSpecialtyPlan}" var="item"> --%>
							      		<c:forEach items="${gjtSpecialtyPlan}" varStatus="i" var="item">
							      			<tr> 		      				
							      				<c:forEach items="${countMap}" var="countMapItem">
							      					<c:if test="${(item.termTypeCode!=gjtSpecialtyPlan[i.index-1].termTypeCode) && (item.termTypeCode==countMapItem.key)}">	      					
							      						<td class="order" rowspan="${countMapItem.value}" ></td>
									      				<td class="semester" rowspan="${countMapItem.value}">
									      					第${item.termTypeCode }学期
									      				</td>
							      					</c:if>
							      				</c:forEach>		
							      				<td>
													<%-- ${courseTypeMap[item.courseTypeId]} --%>
													<div class="position-relative" data-role="valid">
														<select name="courseTypeIds" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择课程模块！" errormsg="请选择课程模块！">
															<option value="">请选择</option>
															<c:forEach items="${courseTypeMap}" var="map">
																<option value="${map.key}" <c:if test="${map.key==item.courseTypeId}">selected='selected'</c:if>>${map.value}</option>
															</c:forEach>
														</select>
													</div>
												</td>
												<td>${item.gjtCourse.kch }</td>
							      				<td>
													${item.gjtCourse.kcmc }
													<br>
													<c:choose>
														<c:when test="${item.gjtCourse.isEnabled == 1 }">
															<span class="text-green">
									      						(已启用)
									      					</span>
														</c:when>
														<c:otherwise>
															<span class="text-orange">
									      						(未启用)
									      					</span>
														</c:otherwise>
													</c:choose>
							      					<input type="hidden" name="courseIds" value="${item.courseId}">
							      					<%-- <input type="hidden" name="courseTypeIds" value="${item.courseTypeId}"> --%>
							      					<input type="hidden" name="termTypeCodes" value="${item.termTypeCode}">
							      				</td>
							      				<td>
							      					<select class="form-control" name="courseCategorys">
									                  <option value="0" <c:if test="${item.courseCategory=='0'}"> selected="selected"</c:if>>必修</option>
									                  <option value="1" <c:if test="${item.courseCategory=='1'}"> selected="selected"</c:if>>选修</option>
									                  <option value="2" <c:if test="${item.courseCategory=='2'}"> selected="selected"</c:if>>补修</option>
									                </select>
							      				</td>
							      				<td>
							      					<select class="form-control" name="coursetypes">
									                  <option value="0" <c:if test="${item.coursetype=='0'}"> selected="selected"</c:if>>统设</option>
									                  <option value="1" <c:if test="${item.coursetype=='1'}"> selected="selected"</c:if>>非统设</option>
									                </select>
							      				</td>
							      				<td>
							      					<select class="form-control" name="examUnits">
									                  <option value="1" <c:if test="${item.examUnit=='1'}"> selected="selected"</c:if>>省</option>
									                  <option value="2" <c:if test="${item.examUnit=='2'}"> selected="selected"</c:if>>中央</option>
									                  <option value="3" <c:if test="${item.examUnit=='3'}"> selected="selected"</c:if>>分校</option>
									                </select>
							      				</td>
							      				<td>
							      					<input type="text" class="form-control text-center center-block module${item.courseTypeId}" value='<fmt:formatNumber value="${item.score}" pattern="#.##" type="number"/>' name="scores"
															datatype="/^\d+(\.\d+)?$/" />
															
							      				</td>
							      				<td>
							      					<input type="text" class="form-control text-center center-block" value='<fmt:formatNumber value="${item.hours}" pattern="#" type="number"/>' name="hours"
															onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" />
							      				</td>
							      				<td>
							      					<%-- <a href="../courseFormUpdate?id=${item.id}&termTypeCode=${item.termTypeCode }&specialtyId=${entity.specialtyId }" class="operion-item" data-toggle="tooltip" title="编辑" data-role="add-course"><i class="fa fa-edit"></i></a>
							      					<a href="../courseFormView?id=${item.id}" data-role="add-course" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a> --%>
							      					<a href="javascript:void(0);" class="operion-item delete-plan" val="${item.id}" title="删除" data-tempTitle="删除"><i class="fa fa-fw fa-trash-o"></i></a>
							      				</td>	      				
							      			</tr>	
						      				<c:if test="${item.termTypeCode!=gjtSpecialtyPlan[i.index+1].termTypeCode}">
						      				 	<tr>
									      			<td colspan="14" align="center">
									      			<a href="../choiceCourseList?termTypeCode=${item.termTypeCode }&specialtyId=${entity.specialtyId }" role="button" class="btn btn-block text-green nobg margin_t5" data-role="add-course"><i class="fa fa-fw fa-plus"></i>添加课程</a>
									      			</td>
								      			 </tr> 		    		
							      				</tbody>   	 
							      				<tbody id="plan${item.termTypeCode + 1}">          				
						      				</c:if>	
							      		</c:forEach>	
							      		</tbody>   		
							      	</table>	
							      	<input type="hidden" id = "count" value ="${countMap.size()}"/>	    
						      	</div>
					      	</div>
						</div>
					</div>
					<div class="box-footer text-right">
						<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="cancel" onclick="history.back()">上一步</button>
						<c:if test="${isBtnSetRule }">
						<button id="btn-submit" type="submit" class="btn btn-primary min-width-90px" data-role="sure">保存，进入下一步</button>
						</c:if>
					</div>
				</form>
	    	</div>
	    </div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<!--课程模块 模板-->
<script type="text/template" id="model-tmp">
	<tr>
		<td>
			<div class="position-relative" data-role="valid">
				<select name="courseType" class="form-control center-block width-80-per" datatype="*" nullmsg="请选择课程模块！" errormsg="请选择课程模块！">
								 	<option value="">请选择</option>
									<c:forEach items="${courseTypeMap2}" var="map">
										<option value="${map.key}" >${map.value}</option>
									</c:forEach>
								</select>
			</div>
		</td>
		<td>
			<div class="position-relative" data-role="valid">
				<input type="text" class="form-control text-center center-block width-80-per" placeholder="总学分" value='<fmt:formatNumber value="${maps.key}" pattern="#" type="number"/>' name="totalScore"
					datatype="/^\d+(\.\d+)?$/" nullmsg="请填写总学分！" errormsg="请填写数字！" />
			</div>
		</td>
		<td>
			<div class="position-relative" data-role="valid">
				<input type="text" class="form-control text-center center-block width-80-per" placeholder="最低毕业学分" value='<fmt:formatNumber value="${maps.key}" pattern="#" type="number"/>' name="score"
					 datatype="/^\d+(\.\d+)?$/" nullmsg="请填写最低毕业学分！" errormsg="请填写数字！" />
			</div>
		</td>
		<td>
			<div class="position-relative" data-role="valid">
				<input type="text" class="form-control text-center center-block width-80-per" placeholder="模块中央电大考试最低学分" value='<fmt:formatNumber value="${obj.crtvuScore}" pattern="#" type="number"/>' name="crtvuScore"
					 datatype="/^\d+(\.\d+)?$/" nullmsg="请填写模块中央电大考试最低学分！" errormsg="请填写数字！" />
			</div>
		</td>
		<td>
			<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove-item"><i class="fa fa-trash-o text-red"></i></a>
		</td>
	</tr>
</script>

<!--专业规则 模板-->
<script type="text/template" id="plan-tmp">
<tr>
	<td class="order" rowspan="1"></td>
	<td class="semester" rowspan="1">第{termTypeCode}学期</td>
	<td>
		<div class="position-relative" data-role="valid">
			<select name="courseTypeIds" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" datatype="*" nullmsg="请选择课程模块！" errormsg="请选择课程模块！">
				<option value="">请选择</option>
				<c:forEach items="${courseTypeMap}" var="map">
					<option value="${map.key}">${map.value}</option>
				</c:forEach>
			</select>
		</div>
	</td>
	<td>{kch}</td>
	<td>{kcmc} <br> 
		<span class="{enabledColor}"> ({isEnabledText}) </span> 
		<input type="hidden" name="courseIds" value="{courseId}"> 
		<input type="hidden" name="termTypeCodes" value="{termTypeCode}">
	</td>
	<td>
		<select class="form-control" name="courseCategorys">
			<option value="0">必修</option>
			<option value="1">选修</option>
			<option value="2">补修</option>
		</select></td>
	<td>
		<select class="form-control" name="coursetypes">
			<option value="0">统设</option>
			<option value="1">非统设</option>
		</select></td>
	<td>
		<select class="form-control" name="examUnits">
			<option value="1">省</option>
			<option value="2">中央</option>
			<option value="3">分校</option>
		</select></td>
	<td><input value="{score}" type="text" class="form-control text-center center-block module5" value="3" name="scores" ></td>
	<td><input value="{hour}" type="text" class="form-control text-center center-block" value="22" name="hours" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></td>
	<td><a href="javascript:void(0);" class="operion-item delete-plan"  title="删除" data-temptitle="删除">
			<i class="fa fa-fw fa-trash-o"></i>
		</a></td>
</tr>
</script>

<script type="text/javascript">
//添加课程模块
$('[data-role="add-model"]').click(function(event) {
	event.preventDefault();
	$('[data-role="set-model"] tbody').append($('#model-tmp').html());
});

//删除课程模块
$('[data-role="set-model"]').on('click', '[data-role="remove-item"]', function(event) {
	event.preventDefault();
	$(this).closest('tr').remove();
});

$("#planTable").on("click", ".delete-plan", function(event) {
	event.preventDefault();
	$tr = $(this).closest('tr');
	$prevTr = $tr.prev();
	if ($prevTr.length > 0) {
		$firsrTr = $tr.parent().find("tr:first");
		var rowspan = parseInt($firsrTr.find("td:eq(0)").attr("rowspan"));
		$firsrTr.find("td:eq(0)").attr("rowspan", rowspan-1);
		$firsrTr.find("td:eq(1)").attr("rowspan", rowspan-1);
		$tr.remove();
	} else {
		$tr.find("td").empty();
	}
});

$("#planTable").find("tbody:last").remove();
var count = $("#count").val();
$('[data-role="add-semester"]').click(function(event) {	
	var canAdd = false;
	if ($("#planTable").find("tbody").length == 0) {
		canAdd = true;
	} else {
		if ($("#planTable").find("tbody:last").find(":input[name='courseIds']").length > 0) {
			canAdd = true;
		}
	}
	
	if(canAdd){
	    addSemesterFn();
	} else {
		alert("请先添加课程！");
	}
	
});

function addSemesterFn(){
    count++;
	var html = "<tbody id=\"plan"+count+"\">"+
		"<tr>"+
	"<td class=\"order\"></td>"+
	"<td class=\"semester\">"+
		
	"</td>"+
	"<td></td>"+
	"<td>"+
		
	"</td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"<td></td>"+
	"</tr>"+
	"<tr>"+
	"<td colspan=\"14\" align=\"center\">"+
	"<a href=\"../choiceCourseList?termTypeCode="+count+"&specialtyId=${entity.specialtyId }\" role=\"button\" class=\"btn btn-block text-green nobg margin_t5\" data-role=\"add-course\"><i class=\"fa fa-fw fa-plus\"></i>添加课程</a>"+
	"</td>"+
	"</tr>"+	
	"</tbody>";
	
	$(".plan-table").append(html);
}
//新增课程
$("body").on('click', '[data-role="add-course"]', function(event) {
	event.preventDefault();
	var courseTypeIds = "";
	var flag = false;
	var $courseTypes = $(":input[name='courseType']");
	if ($courseTypes.length == 0) {
		alert("请先添加课程模块！");
		return false;
	}
	var len = $courseTypes.length;
	$courseTypes.each(function(index, el) {
		if ($(this).val() != "") {
			courseTypeIds += $(this).val();
			flag = true;
		}
     	if (index != len - 1) {
     		courseTypeIds += ",";
     	}
    });
	if (!flag) {
		alert("请先选择课程模块！");
		return false;
	}
	
	var courseIdStr = "";
	var $courseIds = $(":input[name='courseIds']");
	var len1 = $courseIds.length;
	$courseIds.each(function(index, el) {
		if ($(this).val() != "") {
			courseIdStr += $(this).val();
		}
     	if (index != len1 - 1) {
     		courseIdStr += ",";
     	}
    });
	
	$(".tea-plan-box").removeClass('on');
	$(this).parent().addClass('on');
	$.mydialog({
	  id:'add-course',
	  width:'90%',
	  height:700,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')+"&courseTypeIds="+courseTypeIds+"&courseIds="+courseIdStr
	});
});


//导入课程模块
$('[data-role="import-model"]').click(function(event) {
	var actionName = ctx+"/edumanage/specialty/importCourseModel";
	var downloadFileUrl = ctx+"/excelImport/downloadModel?name=课程模块信息导入模板.xls";
	var content1 = "为了方便你的工作，我们已经准备好了《课程模块信息导入模板》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《课程模块信息导入模板》";
	excelImport(actionName, "file", "courseModel", downloadFileUrl, null, "批量导入课程模块信息", null, null, null, content1, content2,null,'importModelCallback');
});

//导入课程模块回调
function importModelCallback(data){
	if(!data.successful){
		return true;
	}
	if(data.failedCount==0){
		alert('导入成功');
	}
	$.each(data.result,function(i,model){
	    var $tr=null;
	    var $allTr=$('[data-role="set-model"] tbody tr');
	    $allTr.each(function(index,el){
			if($(this).find('[name="courseType"]').val()==model.id){
			    $tr=$(this);
			    return false;
			}
		});
		
		if($tr==null){
		    $('[data-role="add-model"]').click();
		    $tr=$('[data-role="set-model"] tbody tr:last');
		}
		$tr.find('[name="courseType"]').val(model.id);
		$tr.find('[name="totalScore"]').val(model.totalScore);
		$tr.find('[name="score"]').val(model.score);
		$tr.find('[name="crtvuScore"]').val(model.crtvuScore);
	});

	if(data.failedCount>0){
		return true;
	}
	return false;
}


//导入专业规则
$('[data-role="import-plan"]').click(function(event) {
	var actionName = ctx+"/edumanage/specialty/importPlan";
	var downloadFileUrl = ctx+"/excelImport/downloadModel?name=专业规则信息导入模板.xls";
	var content1 = "为了方便你的工作，我们已经准备好了《专业规则信息导入模板》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《专业规则信息导入模板》";
	excelImport(actionName, "file", "secialtyPlan", downloadFileUrl, null, "批量导入专业规则信息", null, null, null, content1, content2,null,'importPlanCallback');
});

function importPlanCallback(data){
    if(!data.successful){
		return true;
    }
    
    if(data.failedCount==0){
		alert('导入成功');
	}
    $.each(data.result,function(i,plan){
	    plan.isEnabledText=plan.isEnabled==1?'已启用':'未启用';
	    plan.enabledColor=plan.isEnabled==1?'text-green':'text-orange';
	    var $courseInput=$('#planTable tbody tr input[name="courseIds"][value="'+plan.courseId+'"]');
	    if($courseInput.length>0){
			$courseInput.closest('tr').find('.delete-plan').click();
		 }
		 for(var key in plan){
			if($.trim(plan[key])==''){
			    plan[key]='';
			}
		 }
		 var html=$('#plan-tmp').html().format(plan);
		 var term=plan.termTypeCode;
		 var $addTr = $(html);
		 while($('#plan'+term).length==0){
		     addSemesterFn();
		 }
		 var $tbody=$('#plan'+term);
		 if($tbody.find(":input[name='courseTypeIds']").length==0){
		     $tbody.find("tr:first").remove();
		 }else{
		     $addTr.find('.order,.semester').remove();
		 }
		 $tbody.find('tr:last').before($addTr);
		 $addTr=$($tbody.find('tr:last').prev());
		 $addTr.find(':input[name="courseTypeIds"]').selectpicker('refresh').selectpicker('val',plan.courseTypeId);
		 $addTr.find(':input[name="courseCategorys"]').val(plan.courseCategory);
		 $addTr.find(':input[name="coursetypes"]').val(plan.coursetype);
		 $addTr.find(':input[name="examUnits"]').val(plan.examUnit);
		 resetTable();
	});
	if(data.failedCount>0){
		return true;
	}
	return false;

}

function resetTable(){
    $('#planTable tbody').each(function(){
		var length=$(this).find('tr').not(':last').length
		$(this).find('.order').attr('rowspan',length);
		$(this).find('.semester').attr('rowspan',length);
		
	});
}

function formatNum(obj){   
    
}

$('#planTable').on('keyup afterpaste','[name="scores"]',function(){
    var val = $(this).val();
    val = val.replace(/[^\d.]/g,'');  //清除“数字”和“.”以外的字符  
    val = val.replace(/^\./g,'');  //验证第一个字符是数字而不是. 
    val = val.replace(/\.{2,}/g,''); //只保留第一个. 清除多余的.
    val = val.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
   
    $(this).val(val);
});

//表单验证
;(function(){
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
       //验证学分
       /* if (parseInt($("#zdbyxf").val()) > parseInt($("#zxf").val())) {
      	 $("#zxfDiv").find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').text('最低毕业学分不能大于总学分');
      	 $("#zxf").focus();
      	 return false;
       } else if (parseInt($("#bxxf").val()) + parseInt($("#xxxf").val()) > parseInt($("#zxf").val())) {
      	 $("#zxfDiv").find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').text('必修学分+选修学分不能大于总学分');
      	 $("#zxf").focus();
      	 return false;
       } else {
      	 $("#zxfDiv").find('.tooltip').removeClass('in');
       } */
       
       var $totalScore = $("input[name='totalScore']");
       if ($totalScore.length == 0) {
      	 alert("请添加课程模块！");
      	 return false;
       }
       /*var $score = $("input[name='score']");
       var totalScore = 0;
       var score = 0;
       $totalScore.each(function(index, el) {
      	 totalScore += parseInt($(this).val());
      	 score += parseInt($score.eq(index).val());
       });
       if (totalScore > parseInt($("#zxf").val())) {
      	 $("#zxfDiv").find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').css('max-width', '230px').text('课程模块总学分之和不能大于专业总学分');
      	 $("#zxf").focus();
      	 return false;
       }  else if (score > $("#zdbyxf").val()) {
      	 $("#zdbyxfDiv").find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').text('课程模块最低毕业学分之和不能大于专业最低毕业学分');
      	 $("#zdbyxf").focus();
      	 return false;
       }  else {
      	 $("#zxfDiv").find('.tooltip').removeClass('in');
       }*/
       
       var flag = true;
	   	$("input[name='scores']").each(function(index, el) {
	   		if (flag && $(this).val() == "") {
	   			alert("请填写学分");
	   			flag = false;
	   			$(this).focus();
	   		}
	   	});
	   	if (!flag) {
	   		return flag;
	   	}
	   	
	   	$("input[name='hours']").each(function(index, el) {
	   		if (flag && $(this).val() == "") {
	   			alert("请填写学时");
	   			flag = false;
	   			$(this).focus();
	   		}
	   	});
	   	if (!flag) {
	   		return flag;
	   	}
	   	
        if ($("input[name='scores']").length == 0) {
      	  alert("请添加专业规则！");
      	  return false;
        }

        var moduleIdCodeMap = {};
        var $courseTypes = $(":input[name='courseType']");
        $(':input[name="courseTypeIds"]').each(function(index, el) {
        	var flag2 = false;
        	$courseTypes.each(function(index1, el1) {
        		var moduleId = $(el1).val();
        		var moduleCode = $(el).val();
        		if (moduleIdCodeMap[moduleId]) {
        			if (moduleIdCodeMap[moduleId] == moduleCode) {
        				flag2 = true;
        			}
        		} else {
            		$.ajax({  
                        type : "post",  
                        url : "${ctx}/edumanage/specialty/getModuleCodeById",  
                        dataType:'json',
                        data : {id:moduleId},  
                        async : false,  
                        success : function(data){  
                        	moduleIdCodeMap[moduleId] = data;
                        	if (moduleCode == data) {
                    			flag2 = true;
                    		}
                        }
                	});
        		}
        		if (flag2) {
                 	return false; //break
                }
        		
        	});
        	if (!flag2) {
        		flag = false;
             	return false; //break
            }
		});
        if (!flag) {
        	alert("专业规则里的课程模块必须与课程模块设置里的一致！");
        	return false;
        }
        
        //添加的课程总学分不能少于课程模块设置的模块最低学分
    	$courseTypes.each(function(index, el) {
    		var moduleId = $(el).val();
    		if (moduleId != "") {
    			var totalScore1 = parseFloat($(el).closest('td').next().find('input[name="totalScore"]').val());
    			var totalScore2 = 0;
    			var totalScore3 = parseFloat($(el).closest('td').next().next().next().find('input[name="crtvuScore"]').val());
    			var totalScore4 = 0;
    			
    			$(':input[name="courseTypeIds"]').each(function(index, el) {
    		  		if ($(this).val() == moduleIdCodeMap[moduleId]) {
    		  			totalScore2 += parseFloat($(this).closest('tr').find('input[name="scores"]').val());
    		  			
    		  			var examUnits = $(this).closest('tr').find(':input[name="examUnits"]').val();
        				if (examUnits == 2) {
        					totalScore4 += parseFloat($(this).closest('tr').find('input[name="scores"]').val());
        				}
    		  		}
    			});
    			if (totalScore2 < totalScore1) {
    				alert('课程模块"' + $(el).find("option:selected").text() + '"下的课程总学分不能少于模块最低学分');
    				flag = false;
    			}
    			if (totalScore4 < totalScore3) {
    				alert('课程模块"' + $(el).find("option:selected").text() + '"下考试单位为中央的课程总学分不能少于模块中央电大考试最低学分');
    				flag = false;
    			}
    			/* $.ajax({  
                    type : "post",  
                    url : "${ctx}/edumanage/specialty/getModuleCodeById",  
                    dataType:'json',
                    data : {id:$(el).val()},  
                    async : false,  
                    success : function(data){  
            			$(':input[name="courseTypeIds"]').each(function(index, el) {
            		  		if ($(this).val() == data) {
            		  			totalScore2 += parseInt($(this).closest('tr').find('input[name="scores"]').val());
            		  			
            		  			var examUnits = $(this).closest('tr').find(':input[name="examUnits"]').val();
                				if (examUnits == 2) {
                					totalScore4 += parseInt($(this).closest('tr').find('input[name="scores"]').val());
                				}
            		  		}
            			});
            			if (totalScore2 < totalScore1) {
            				alert('课程模块"' + $(el).find("option:selected").text() + '"下的课程总学分不能少于模块最低学分');
            				flag = false;
            			}
            			if (totalScore4 < totalScore3) {
            				alert('课程模块"' + $(el).find("option:selected").text() + '"下考试单位为中央的课程总学分不能少于模块中央电大考试最低学分');
            				flag = false;
            			}
                    }
            	}); */
    		}
        });
    	if (!flag) {
    		return false;
    	}

    	var zdbyxf = $("#zdbyxf").val();
    	var zyddksxf = $("#zyddksxf").val();
    	var bxxf = $("#bxxf").val();
    	var totalScore5 = 0;  //设置的总学分
    	var totalScore6 = 0;  //设置的必须总学分
    	var totalScore7 = 0;  //设置的中央总学分
    	$("input[name='scores']").each(function(index, el) {
    		totalScore5 += parseFloat($(this).val());
    		var courseCategory = $(this).closest('tr').find(':input[name="courseCategorys"]').val();
    		if (courseCategory == 0) {
    			totalScore6 += parseFloat($(this).val());
    		}
    		var examUnit = $(this).closest('tr').find(':input[name="examUnits"]').val();
    		if (examUnit == 2) {
    			totalScore7 += parseFloat($(this).val());
    		}
	   	});
    	if (totalScore5 < zdbyxf) {
    		alert("专业规则设置的总学分不能低于学分设置中的最低毕业学分!");
    		return false;
    	}
    	if (totalScore6 < bxxf) {
    		alert("专业规则设置的必修课程的总学分不能低于学分设置中的必修学分！");
    		return false;
    	}
    	if (totalScore7 < zyddksxf) {
    		alert("专业规则设置的考试单位为中央的总学分不能低于学分设置中的中央电大考试学分！");
    		return false;
    	}
    	
    	//检查除最后一个学期外的学期是否有添加课程
    	var $tbody = $("#planTable").find("tbody");
    	var len2 = $tbody.length;
    	$tbody.each(function(index, el) {
         	if (index != len2 - 1) {
         		if ($(this).find(":input[name='courseIds']").length == 0) {
         			flag = false;
        		}
         	}
        });
    	if (!flag) {
    		alert("部分学期还未添加课程！");
    		return false;
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
	       window.location.href = ctx + "/edumanage/specialty/publish/${entity.specialtyId}";
		}else{
			alert('系统异常');
			$.closeDialog(postIngIframe);
		}
		//window.location.reload();
	  }
	});

})();

</script>
</body>
</html>
