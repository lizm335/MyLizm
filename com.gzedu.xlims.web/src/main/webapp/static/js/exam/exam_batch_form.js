$(function() {
	$('#btn-create').click(createBatch);
	$('#btn-update').click(updateBatch);
	
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('[data-role="back-off"]').removeAttr("disabled");
		$('[data-role="cancel"]').removeAttr("disabled");
		$('[data-role="btn-pass"]').removeAttr("disabled");
		$('[data-role="btn-not-pass"]').removeAttr("disabled");
		$('#examBatchId').removeAttr("disabled");
		$('#auditState').removeAttr("disabled");
		$('#auditContent').removeAttr("disabled");
	}
	
//	$('.datepickers').datepicker({
//	      autoclose: true,
//	      format : 'yyyy-mm-dd', //控件中from和to 显示的日期格式
//	      language: 'zh-CN',
//	      todayHighlight: true
//	});
	
	var singleDateTimePickerOpt = {
	        singleDatePicker: true,
	        showDropdowns : true,
	        timePicker : false, //是否显示小时和分钟
	        timePickerIncrement : 5, //时间的增量，单位为分钟
	        timePicker12Hour : false, //是否使用12小时制来显示时间
	        timePickerSeconds: true, 
	        format : 'YYYY-MM-DD', //控件中from和to 显示的日期格式
	        locale : {
	            applyLabel : '确定',
	            cancelLabel : '取消',
	            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
	            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
	            firstDay : 1
	        }
	    };
	
	    $('.single-datetime').daterangepicker(singleDateTimePickerOpt);
	    
	    $("[data-role='btn-pass']").click(function() {
			if ($("#auditContent").val() == "") {
				alert("请输入审批评语！");
				return false;
			}
			$("#auditState").val(1);
			$("#approvalForm").submit();
		})
		
		$("[data-role='btn-not-pass']").click(function() {
			if ($("#auditContent").val() == "") {
				alert("请输入审批评语！");
				return false;
			}
			$("#auditState").val(2);
			$("#approvalForm").submit();
		})
	    
});

function createBatch() {
	var data = getFormData();
	if(dateValid(data)) {
		$.ajax({
			  url: ctx + "/exam/new/batch/create",
			  dataType: 'json',
			  data: data,
			  type: 'post',
			  success: function(obj) {
				  if(obj.successful == true) {
					  alert('新增成功');
					  window.location.href = ctx + '/exam/new/batch/list';
				  } else {
					  alert(obj.message);
				  }
			  }
		});	
	}
}

function updateBatch() {
	var data = getFormData();
	if(dateValid(data)) {
		$.ajax({
			  url: ctx + "/exam/new/batch/update",
			  dataType: 'json',
			  data: data,
			  type: 'post',
			  success: function(obj) {
				  if(obj.successful == true) {
					  alert('更新成功');
					  window.location.href = ctx + '/exam/new/batch/list';
				  } else {
					  alert(obj.message);
				  }
			  }
		});
	}
		
}

function  getFormData() {
	var data = {
		examBatchId: $('#entity_examBatchId').val(),
		examBatchCode: $('#entity_examBatchCode').val(),
		name: $('#entity_name').val(),
		gradeId: $('#entity_gradeId').val(),
		planSt: $('#entity_planSt').val(),
		planEnd: $('#entity_planEnd').val(),
		bookSt: $('#entity_bookSt').val(),
		bookEnd: $('#entity_bookEnd').val(),
		arrangeSt: $('#entity_arrangeSt').val(),
		arrangeEnd: $('#entity_arrangeEnd').val(),
		//recordSt: $('#entity_recordSt').val(),
		recordEnd: $('#entity_recordEnd').val(),
		onlineSt: $('#entity_onlineSt').val(),
		onlineEnd: $('#entity_onlineEnd').val(),
		provinceOnlineSt: $('#entity_provinceOnlineSt').val(),
		provinceOnlineEnd: $('#entity_provinceOnlineEnd').val(),
		paperSt: $('#entity_paperSt').val(),
		paperEnd: $('#entity_paperEnd').val(),
		machineSt: $('#entity_machineSt').val(),
		machineEnd: $('#entity_machineEnd').val(),
		shapeEnd: $('#entity_shapeEnd').val(),
		thesisEnd: $('#entity_thesisEnd').val(),
		reportEnd: $('#entity_reportEnd').val(),
		booksSt: $('#entity_booksSt').val(),
		booksEnd: $('#entity_booksEnd').val(),
		bktkBookSt: $('#entity_bktkBookSt').val(),
		bktkBookEnd: $('#entity_bktkBookEnd').val(),
		xwyyBookSt: $('#entity_xwyyBookSt').val(),
		xwyyBookEnd: $('#entity_xwyyBookEnd').val(),
		bktkExamSt: $('#entity_bktkExamSt').val(),
		bktkExamEnd: $('#entity_bktkExamEnd').val(),
		xwyyExamSt: $('#entity_xwyyExamSt').val(),
		xwyyExamEnd: $('#entity_xwyyExamEnd').val()
	};
	console.log(data);
	return data;
}

function dateValid(data){
	if(!data.name) {
		alert('请填写批次名称');
		return false;
	}
	if(!data.gradeId) {
		alert('请选择学期');
		return false;
	}
	if(!data.planSt || !data.planEnd) {
		alert('请填写开考科目设置时间');
		return false;
	}
	if(!data.bookSt || !data.bookEnd) {
		alert('请填写考试预约时间');
		return false;
	}
	if(!data.arrangeSt || !data.arrangeEnd) {
		alert('请填写排考时间');
		return false;
	}
	if(!data.recordEnd) {
		alert('请填写学习成绩登记截止时间');
		return false;
	}
	/*if(!data.onlineSt || !data.onlineEnd) {
		alert('请填写网考、大作业考试时间');
		return false;
	}
	if(!data.provinceOnlineSt || !data.provinceOnlineEnd) {
		alert('请填写省网考考试时间');
		return false;
	}
	if(!data.paperSt || !data.paperEnd) {
		alert('请填写笔考考试时间');
		return false;
	}
	if(!data.machineSt || !data.machineEnd) {
		alert('请填写机考考试时间');
		return false;
	}
	if(!data.shapeEnd) {
		alert('请填写形考任务登记截止时间');
		return false;
	}
	if(!data.thesisEnd) {
		alert('请填写论文截止时间');
		return false;
	}
	if(!data.reportEnd) {
		alert('请填写报告截止时间');
		return false;
	}*/

	var ps = new Date(data.planSt);
	var pe = new Date(data.planEnd);
	if(ps > pe) {
		alert('开考科目设置开始时间不能大于开考科目设置结束时间');
		return false;
	}
	var bs = new Date(data.bookSt);
	var be = new Date(data.bookEnd);
	if(bs <= pe) {
		alert('考试预约开始时间必须大于开考科目设置结束时间');
		return false;
	}
	if(bs > be) {
		alert('考试预约开始时间不能大于考试预约结束时间');
		return false;
	}
	var as = new Date(data.arrangeSt);
	var ae = new Date(data.arrangeEnd);
	if(as <= be) {
		alert('排考开始时间必须大于考试预约结束时间');
		return false;
	}
	if(as > ae) {
		alert('排考开始时间不能大于排考结束时间');
		return false;
	}
	/*var rs = new Date(data.recordSt);
	var re = new Date(data.recordSt);
	if(rs > re) {
		alert('成绩登记开始时间不能大于成绩登记结束时间');
		return false;
	}*/

	var os = new Date(data.onlineSt);
	var oe = new Date(data.onlineEnd);
	if (data.onlineSt && data.onlineEnd) {
		if(os <= be) {
			alert('网考考试开始时间必须大于考试预约结束时间');
			return false;
		}
		if(os > oe) {
			alert('网考考试开始时间不能大于网考考试结束时间');
			return false;
		}
	}
	var pos = new Date(data.provinceOnlineSt);
	var poe = new Date(data.provinceOnlineEnd);
	if (data.provinceOnlineSt && data.provinceOnlineEnd) {
		if(pos <= be) {
			alert('省网考考试开始时间必须大于考试预约结束时间');
			return false;
		}
		if(pos > poe) {
			alert('省网考考试开始时间不能大于省网考考试结束时间');
			return false;
		}
	}
	var ps1 = new Date(data.paperSt);
	var pe1 = new Date(data.paperEnd);
	if (data.paperSt && data.paperEnd) {
		if(ps1 <= ae) {
			alert('笔考考试开始时间必须大于排考结束时间');
			return false;
		}
		if(ps1 > pe1) {
			alert('笔考考试开始时间不能大于笔考考试结束时间');
			return false;
		}
	}
	if (data.machineSt && data.machineEnd) {
		var ms = new Date(data.machineSt);
		var me = new Date(data.machineEnd);
		if(ms <= ae) {
			alert('机考考试开始时间必须大于排考结束时间');
			return false;
		}
		if(ms > me) {
			alert('机考考试开始时间不能大于机考考试结束时间');
			return false;
		}
	}
	if (data.shapeEnd) {
		var se = new Date(data.shapeEnd);
		if(se <= be) {
			alert('形考任务登记截止时间必须大于考试预约结束时间');
			return false;
		}
	}
	if (data.thesisEnd) {
		var te = new Date(data.thesisEnd);
		if(te <= be) {
			alert('论文截止时间必须大于考试预约结束时间');
			return false;
		}
	}
	if (data.reportEnd) {
		var re1 = new Date(data.reportEnd);
		if(re1 <= be) {
			alert('报告截止时间必须大于考试预约结束时间');
			return false;
		}
	}

	/*if(rs <= oe) {
		alert('成绩登记开始时间必须大于网考考试结束时间');
		return false;
	}
	if(rs <= poe) {
		alert('成绩登记开始时间必须大于省网考考试结束时间');
		return false;
	}
	if(rs <= pe1) {
		alert('成绩登记开始时间必须大于笔考考试结束时间');
		return false;
	}
	if(rs <= me) {
		alert('成绩登记开始时间必须大于机考考试结束时间');
		return false;
	}
	if(rs <= se) {
		alert('成绩登记开始时间必须大于形考任务登记截止时间');
		return false;
	}
	if(rs <= te) {
		alert('成绩登记开始时间必须大于论文截止时间');
		return false;
	}
	if(rs <= re1) {
		alert('成绩登记开始时间必须大于报告截止时间');
		return false;
	}*/
	
	return true;
}