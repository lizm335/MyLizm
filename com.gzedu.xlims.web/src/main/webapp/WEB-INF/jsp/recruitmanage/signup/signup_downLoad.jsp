<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>导出报名统计表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>

<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">导出报名统计表</h3>
	</div>
	<div class="box-body pos-rel overlay-wrapper">
		<div class="box-body pad-t15">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-xs-3 control-label">学习中心</label>
					<div class="col-xs-7">
						<select class="form-control select2" name="STUDY_ID" id="study_id" data-size="5" data-live-search="true">
				            <c:forEach items="${schoolInfoMap}" var="map">
								<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_studyId}">selected='selected'</c:if>>${map.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-3 control-label">学期</label>
					<div class="col-xs-7">
						<select class="form-control select2" name="GRADE_ID" id="grade_id" data-size="5" data-live-search="true">
							<option value="">请选择</option>
							<c:forEach items="${gradeMap}" var="map">
								<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']}">selected='selected'</c:if> >${map.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-3 control-label">层次</label>
					<div class="col-xs-7">
						<select class="form-control select2" name="PYCC_ID" id="pycc_id" data-size="5" data-live-search="true">
							<option value="">请选择</option>
							<c:forEach items="${pyccMap}" var="map">
								<option value="${map.key}" <c:if test="${map.key==param.PYCC}">selected='selected'</c:if>>${map.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-3 control-label">专业</label>
					<div class="col-xs-7">
						<select class="form-control select2" name="SPECIALTY_ID" id="specialty_id" data-size="5" data-live-search="true">
							<option value="">请选择</option>
							<c:forEach items="${specialtyMap}" var="map">
								<option value="${map.key}" <c:if test="${param.SPECIALTY_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
					  		</c:forEach>
						</select>
					</div>
				</div>
				<!--  
				<div class="form-group">
					<label class="col-xs-3 control-label">报读渠道</label>
					<div class="col-xs-7">
					
						<select class="form-control select2" name="AUDIT_SOURCE" id="audit_source" data-size="5" data-live-search="true">
							<option value="">请选择</option>
						</select>
					</div>
				</div>
				-->
				<div class="form-group">
					<label class="col-xs-3 control-label">资料状态</label>
					<div class="col-xs-7">
						<select class="form-control select2" name="AUDIT_STATE" id="audit_state" data-size="5" data-live-search="true">
							<option value="">请选择</option>
							<c:forEach items="${auditMap}" var="map">
								<option value="${map.key}">${map.value}</option>
							</c:forEach>
							<!--
							<option value="0">审核不通过</option>
							<option value="1">审核通过</option>
							<option value="2">待审核</option>
							<option value="3">审核中</option>
							<option value="4">未审核</option>
							-->
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-3 control-label">学籍状态</label>
					<div class="col-xs-7">
						<select class="form-control select2" name="XJZT" id="xjzt" data-size="5" data-live-search="true">
							<option value="">请选择</option>
							<c:forEach items="${xjztMap}" var="map">
								<option value="${map.key}">${map.value}</option>
							</c:forEach>
							<!--
							<option value="1">开除学籍</option>
							<option value="2">正常注册</option>
							<option value="3">未正常注册</option>
							<option value="4">休学</option>
							<option value="5">退学</option>
							<option value="6">学习期限已过</option>
							<option value="7">延期</option>
							<option value="8">已毕业</option>
							-->
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-xs-3 control-label">缴费状态</label>
					<div class="col-xs-7">
						<select class="form-control select2" name="CHARGE" id="charge" data-size="5" data-live-search="true">
							<option value="">请选择</option>
							<c:forEach items="${chargeMap}" var="map">
								<option value="${map.key}">${map.value}</option>
							</c:forEach>
							<!--
							<option value="0">未缴费</option>
							<option value="1">缴费</option>
							-->
						</select>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="export">导出</button>
</div>

<script type="text/javascript">
$(function(){
	$("#study_id").selectpicker();
	$("#specialty_id").selectpicker();
	$("#pycc_id").selectpicker();
	$("#grade_id").selectpicker();
	$("#xjzt").selectpicker();
	$("#audit_state").selectpicker();
	$("#audit_source").selectpicker();
	$("#charge").selectpicker();
});


$(".box-body").height($(window).height()-126);
//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//导出
$('[data-role="export"]').click(function(event){
	var $overlay=$(".overlay");
	var $that=$(this);
	$overlay.show();
	$that.addClass("disabled").prop('disabled', true);
	exportData();
	setTimeout(function(){
		var postIngIframe=$.mydialog({
            id:'process',
            width:150,
            height:50,
            backdrop:false,
            fade:true,
            showCloseIco:false,
            zIndex:11000,
            content: '<div class="text-center pad-t15">数据导出中请稍后...<i class="fa fa-refresh fa-spin"></i></div>'
        });
		postIngIframe.find(".text-center.pad-t15").html('数据导出中请稍后...<i class="icon fa fa-check-circle"></i>')
		 setTimeout(function(){
             parent.$.closeDialog(frameElement.api);
         },5000);
	},1000);
	
});


function exportData(){
	var study_id = $("select[name='STUDY_ID']").val();
	var grade_id = $("select[name='GRADE_ID']").val();
	var pycc_id = $("select[name='PYCC_ID']").val();
	var specialty_id = $("select[name='SPECIALTY_ID']").val();
	var audit_state = $("select[name='AUDIT_STATE']").val();
	var xjzt = $("select[name='XJZT']").val();
	var charge = $("select[name='CHARGE']").val();
	
	var form = $("<form>");
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","get");
	form.attr("action","${ctx}/recruitmanage/signup/exportSignUpList");
	$("body").append(form);
	form.append("<input type='text' name='STUDY_ID' value='"+study_id+"'/>'");
	form.append("<input type='text' name='GRADE_ID' value='"+grade_id+"'/>'");
	form.append("<input type='text' name='PYCC_ID' value='"+pycc_id+"'/>'");
	form.append("<input type='text' name='SPECIALTY_ID' value='"+specialty_id+"'/>'");
	form.append("<input type='text' name='AUDIT_STATE' value='"+audit_state+"'/>'");
	form.append("<input type='text' name='XJZT' value='"+xjzt+"'/>'");
	form.append("<input type='text' name='CHARGE' value='"+charge+"'/>'");
	form.submit();
	
}


</script>
</body>
</html>


