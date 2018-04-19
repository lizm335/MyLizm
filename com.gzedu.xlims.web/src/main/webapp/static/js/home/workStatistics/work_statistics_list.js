
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

	//按钮切换
	$("#data_btn_all,#data_btn_1,#data_btn_7,#data_btn_7_3,#data_btn_3_0,#data_btn_0").click(function () {
		var this_id =$(this).attr("id");
		switch (this_id){
			case "data_btn_all":
				$("#study_status").val("");
				break;
			case "data_btn_1":
				$("#study_status").val("0");
				break;
			case "data_btn_7":
				$("#study_status").val("1");
				break;
			case "data_btn_7_3":
				$("#study_status").val("2");
				break;
			case "data_btn_3_0":
				$("#study_status").val("3");
				break;
			case "data_btn_0":
				$("#study_status").val("4");
				break;
		}
		$("#submit_buttion").click();
	});
})
