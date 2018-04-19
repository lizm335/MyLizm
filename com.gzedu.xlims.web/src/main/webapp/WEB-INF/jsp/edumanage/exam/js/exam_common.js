

$(function() {
	//批次联动批次下的考点
	$('#examBatchCode').change(function(){
		var examBatchCode = $('#examBatchCode').val();
		if(examBatchCode!=''){
			$.get("${ctx}/exam/new/batch/queryexamPointId",{examBatchCode:examBatchCode},function(data,status){
				$('#examPointId').empty();
				$("#examPointId").append("<option value=''>请选择</option>");
				$.each(data, function (i) {
					$("#examPointId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
	        	});
			  },"json");
		}else{
			$('#examPointId').empty();
			$("#examPointId").append("<option value=''>请选择</option>");
			$("#examPointId").val("").trigger("change");
		}
	});
});

