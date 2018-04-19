<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业列表查询</title>

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
				<a href="#">教务管理</a>
			</li>
			<li>
				<a href="#">开设专业</a>
			</li>
			<li class="active">新设专业</li>
		</ol>
	</section>
	<section class="content">
		<div class="box margin-bottom-none">
			<div class="box-header with-border">
				<div class="form-inline">
					<div class="form-group">
						<label class="margin_r10">开设学期</label><!-- selectpicker show-tick -->
						<select id="gradeId" name="gradeId" class="form-control " data-size="5" data-live-search="true">
							<c:forEach items="${gradeMap}" var="map">
								<option value="${map.key}" <c:if test="${map.key==gradeId}">selected='selected'</c:if>>${map.value}</option>
							</c:forEach>
						</select>

					</div>
				</div>
				<div class="form-inline margin_t10">
					<div class="form-group">
						<label class="margin_r10">开设方式</label>
						<button type="button" class="btn btn-default" val="${ctx}/edumanage/gradespecialty/addGradeSpecialty?gradeId=${gradeId}" data-role="select-pop">
							<i class="fa fa-fw fa-plus"></i> 添加专业
						</button>
						 或 
						<div class="input-group">
							<div class="input-group-addon">沿用以往学期中的专业</div>
							<!-- /btn-group -->
							<select class="form-control select2"  data-placeholder="选择一个已开设专业的年级来复制"  style="width: 250px;" id="copyGradeId" >
								<option value=""></option>
								<c:forEach items="${copyGradeMap}" var="map">
									<c:if test="${map.key!=gradeId}">
										<option value="${map.key}">${map.value}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>

					</div>
				</div>
			</div>
			<div class="box-body">
				<form id="inputForm" action="#">
				<table class="table table-bordered table-striped vertical-mid text-center table-font" style="table-layout:fixed;">
					<thead>
						<tr class="">
							<th width="12%">专业规则号</th>
							<th width="20%">专业名称</th>
							<th>专业属性</th>
							<th width="9%">最低毕业学分</th>
							<th width="9%">课程总数</th>
							<th width="9%">专业性质</th>
							<th width="15%">适用范围</th>
							<th width="9%">操作</th>
						</tr>
					</thead>
					<tbody data-role="class-box">
						<c:forEach items="${gjtGradeSpecialtyList }" var="list">
							<c:set var="item" value="${list.gjtSpecialty }" />
							<tr sid="${item.specialtyId}" gid="${list.id}">
								<td>${item.ruleCode}</td>
								<td>
									${item.zymc}
								
								</td>
								<td style="text-align: left">
									专业层次：${pyccMap[item.pycc]}<br>
									学科：${subjectMap[item.subject]}<br>
									学科门类：${subjectCategoryMap[item.subject][item.category]}<br>
									适用行业：${syhyMap[item.syhy]}
								</td>
								<td>${item.zdbyxf}</td>
								<td>${fn:length(item.gjtSpecialtyPlans)}</td>
								<td>
									<c:choose>
										<c:when test="${item.type == 1}">正式专业</c:when>
										<c:when test="${item.type == 2}">体验专业</c:when>
										<c:otherwise>--</c:otherwise>
									</c:choose>
								</td>
								<td>
									<select class="form-control select2 select2-hidden-accessible" name="applyRange" multiple="" data-placeholder="通用" style="width: 100%;">
										<c:forEach items="${studyCenterMap}" var="x">
											 <option <c:if test="${fn:contains(list.studyCenterIds,x.key)}">selected="selected"</c:if>  value="${x.key}">
											 ${fn:replace(x.value,'-', '')}
											 </option>
										</c:forEach>
									</select>
								</td>
								<td>
									<a href="${ctx}/edumanage/specialty/view/${item.specialtyId }" class="operion-item" title="查看详情" data-tempTitle="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
									<c:if test="${list.status==0}">
										<a href="#" class="operion-item" data-toggle="tooltip" val="${item.specialtyId }" title="删除" data-role="remove-item"><i class="fa fa-trash-o text-red"></i></a>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</form>
			</div>


			<div class="box-footer text-right margin_t30">
				<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="back-off">取消</button>
				<button type="button" class="btn btn-primary min-width-90px" data-role="sure">确认开设</button>
			</div>
		</div>

	</section>

	<script type="text/template" id="rowdata">
		<tr sid="{specialtyId}" gid="{gradeSpecialtyId}" copy="true">
			<td>{ruleCode}</td>
			<td>
				{zymc}
			</td>
			<td style="text-align: left">
				 专业层次：{pycc}<br />
				 学科：{subject}<br />
				 学科门类：{category}<br />
				 适用行业：{syhy}
			</td>
			<td>{zdbyxf}</td>
			<td>{specialtyPlanCount}</td>
			<td>{type}</td>

			<td class="range-temp">
				<select class="form-control select2" name="applyRange" temp="{studyCenterIds}" multiple="" data-placeholder="通用" style="width: 100%;">
					<c:forEach items="${studyCenterMap}" var="item">
						 <option value="${item.key}">${fn:replace(item.value,'-', '')}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<a href="${ctx}/edumanage/specialty/view/{specialtyId}" class="operion-item" title="查看详情" data-tempTitle="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
				<a href="#" class="operion-item" data-toggle="tooltip" val="{specialtyId}" title="删除" data-role="remove-item">
					<i class="fa fa-trash-o text-red"></i>
				</a>
			</td>
		</tr>
	</script>

	<script type="text/javascript">
	$(".filter-tabs li").click(function(event) {
	    if ($(this).hasClass('actived')) {
			$(this).removeClass('actived');
	    } else {
			$(this).addClass('actived');
	    }
	});

	//菜单控件
	$(".select2").select2({
	    language : "zh-CN"
	}); 

	//添加专业
	$('[data-role="select-pop"]').click(function(event) {
	    $.mydialog({
		id : 'select-course',
		width : 880,
		height : 550,
		zIndex : 11000,
		content : 'url:' + $(this).attr('val')
	    });
	});

	//删除专业
	$(document).on('click', '[data-role="remove-item"]', function(event) {
	    event.preventDefault();
	    $(this).closest('tr').remove();
	});
	//确认发布
	$('[data-role="sure"]').click(function(event) {
	    if ($('[data-role="class-box"] tr').length == 0) {
			alert('请先添加专业');
			return;
	    }
	    var postIngIframe = $.mydialog({
			id : 'dialog-1',
			width : 150,
			height : 50,
			backdrop : false,
			fade : false,
			showCloseIco : false,
			zIndex : 11000,
			content : '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
	    });
	    var gradeSpecialties=[];
	    $('[data-role="class-box"] tr').each(function(index, el) {
			var specialtyId = $(this).attr('sid');
			var gid = $(this).attr('gid');
			var applyRange = $(this).find('[name="applyRange"]').val();
			var copy = $(this).attr('copy');
			var item={
				id:gid,
				specialtyId:specialtyId
			}
			if(applyRange){
			    item.applyRange=applyRange;
			}
			gradeSpecialties.push(JSON.stringify(item));
	    });
	    var param ={
		    gradeSpecialties : gradeSpecialties,
			gradeId : $('#gradeId').val()
	    };
	    if($.trim($('#copyGradeId').val())!=''){
			param.copyGradeId=$.trim($('#copyGradeId').val());
		}
	    $.post('${ctx}/edumanage/gradespecialty/saveGradeSpecialty', param, function(data) {
			if (data.successful) {
			    postIngIframe.find(".text-center.pad-t15").html(data.message + '...<i class="icon fa fa-check-circle"></i>');
			    setTimeout(function() {
					location.href='${ctx}/edumanage/teachPlan/list?showCreate=true';
			    }, 1000)
			} else {
			    alert(data.message);
			    $.closeDialog(postIngIframe);
	
			}
	    }, 'json');

	});

	var pyccMap = ${pyccMap};
	var subjectMap = ${subjectMap};
	var subjectCategoryMap = ${subjectCategoryMap};
	var syhyMap = ${syhyMap}
	
	$('#copyGradeId').change(function() {
		$('[data-role="class-box"]').find('tr[copy="true"]').remove();
		$.get('${ctx}/edumanage/gradespecialty/findSpecialtyByGradeId',{gradeId:$(this).val()},function(data){
		    if(data){
				var html=[];
				$.each(data,function(index,item){
				    item.pycc=pyccMap[item.pycc];
				    item.subject=subjectMap[item.subject];
				    if(subjectCategoryMap[item.subject]){
						item.category=subjectCategoryMap[item.subject][item.category];
					 }else{
					     item.category='';
					 }
				    	
				    item.syhy=syhyMap[item.syhy];
					if(item.type==1){
						item.type='正式专业';
					}else if(item.type==2){
					    item.type='体验专业';
					}
				    $.each(item,function(name,value){
						item[name] = $.trim(value);
				    });
				    html.push($('#rowdata').html().format(item));
				});
				$('[data-role="class-box"]').append(html.join(''));
				$('[copy="true"] [name="applyRange"]').each(function(){
					var temp=$(this).attr('temp');
					temp=$.trim(temp).split(',')
					$(this).removeAttr('temp')
					$(this).select2().val(temp).select2();
				});
		    }
		},'json');
	});
	
	$('#gradeId').change(function(){
		location.href='${ctx}/edumanage/gradespecialty/querySpecialty?gradeId='+this.value;
	});
    </script>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>