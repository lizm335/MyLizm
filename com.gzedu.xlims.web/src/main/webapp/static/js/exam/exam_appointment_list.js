$(function() {
	// 导出
	$('[data-role="export"]').click(function(event) {
		event.preventDefault();
		var self=this;
		$.mydialog({
			id:'export',
			width:600,
			height:415,
			zIndex:11000,
			content: 'url:'+$(this).attr('href')
		});
	});
	
	$('#examBatchCode').change(function(){
		var examBatchCode = $('#examBatchCode').val();
		if(examBatchCode!=''){
			$.get(ctx+"/exam/new/plan/queryExamPlansByExamBatchCode",{examBatchCode:examBatchCode},function(data,status){
				$('#examPlanId').empty();
				$("#examPlanId").append("<option value=''>请选择</option>");
				$.each(data, function (i) {
					if (examPlanId == data[i].id) {
						$("#examPlanId").append("<option selected='selected' value='"+data[i].id+"'>"+data[i].name+"</option>");
					} else {
						$("#examPlanId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
					}
	        	});
				$('#examPlanId').selectpicker('refresh'); 
			  },"json");
		} else {
			$('#examPlanId').empty();
			$("#examPlanId").append("<option value=''>请选择</option>");
			$('#examPlanId').selectpicker('refresh'); 
		}
	});
	$('#examBatchCode').change();
	
	$('#point_export').click(exportPoint);
	$('#plan_export').click(exportPlan);

	$(".btn-import").click(function(event) {
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:importForm'
		});
	});

	$(".btn-import2").click(function(event) {
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:importPointForm'
		});
	});
	
});

function exportPoint() {
	var examBatchCode = $('#m_examBatchCode2').val();
	if(!examBatchCode) {
		alert('请先选择考试计划');
		return;
	}
	$('#uploadForm2').submit();
}

function exportPlan() {
	/*var examBatchCode = $('#m_examBatchCode').val();
	if(!examBatchCode) {
		alert('请先选择考试计划');
		return;
	}*/
	$('#uploadForm').submit();
}
