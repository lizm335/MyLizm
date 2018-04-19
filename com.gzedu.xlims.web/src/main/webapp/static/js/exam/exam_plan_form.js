$(function() {
	$('#btn-update').click(updatePlan);
	$('#btn-create').click(createPlan);
	
	var singleDateTimePickerOpt = {
        singleDatePicker: true,
        showDropdowns : true,
        timePicker : true, //是否显示小时和分钟
        timePickerIncrement : 5, //时间的增量，单位为分钟
        timePicker12Hour : false, //是否使用12小时制来显示时间
        timePickerSeconds: false, 
        format : 'YYYY-MM-DD HH:mm', //控件中from和to 显示的日期格式
        locale : {
            applyLabel : '确定',
            cancelLabel : '取消',
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
    };

    $('.single-datetime').daterangepicker(singleDateTimePickerOpt);
    
    //选择课程
	$("[data-role='select-pop']").click(function(event) {
		var $subjectType=$('#subjectType').val();
		event.preventDefault();
		$.mydialog({
		  id:'select-course',
		  width:1000,
		  height:750,
		  zIndex:11000,
		  content: 'url:' + ctx + '/edumanage/course/choiceMultiCourseList?subjectType='+$subjectType
		});
	});

	$('#entity_type').change(function () {
		var value = this.value;
		if(value == 20) { // 大作业需要上传文档
			$('#document').show();
		} else {
            $('#document').hide();
		}
    });
    $('#entity_type').trigger('change');

	//删除课程
	$(".select-container-ul").on('click', '.select2-selection__choice__remove', function(event) {
		event.preventDefault();
		$("#"+$(this).attr("aria-describedby")).remove();
		$(this).parent().remove();
	});
	
});

//选择课程后回调
function choiceCourseCallback(courseIds, names, orders){
	var $container = $(".select-container-ul");
	var html = [
	  '<li class="select2-selection__choice">',
	    '<span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>',
	    '<span class="select2-name" title="#name#" data-toggle="tooltip" data-container="body"  data-order="#order#">#name#</span>',
	    '<input type="hidden" name = "courseIds" value="#courseId#"/>',			      
	    '</li>'
	];
	
	for (var i = 0; i < courseIds.length; i++) {
		var tmp=html.join("");
	    tmp=tmp.replace(/#name#/g, names[i]);
	    tmp=tmp.replace(/#order#/g, orders[i]);
	    tmp=tmp.replace(/#courseId#/g, courseIds[i]);
	    $container.append(tmp);
	}
	
}

function updatePlan() {
	var data = getFormData();
	if(dateValid(data)) {
		$.ajax({
			  url: ctx + "/exam/new/plan/update",
			  dataType: 'json',
			  data: data,
			  type: 'post',
			  success: function(obj) {
				  if(obj.successful == true) {
					  alert('更新成功');
					  window.location.href = ctx + '/exam/new/plan/list';
				  } else {
					  alert(obj.message);
				  }
			  }
		});	
	}
}

function createPlan() {
	var data = getFormData();
	if(dateValid(data)) {
		$.ajax({
			  url: ctx + "/exam/new/plan/create",
			  dataType: 'json',
			  data: data,
			  type: 'post',
			  success: function(obj) {
				  if(obj.successful == true) {
					  alert('创建成功');
					  window.location.href = ctx + '/exam/new/plan/list';
				  } else {
					  alert(obj.message);
				  }
			  }
		});
	}
}

function  getFormData() {
	var courseIds = new Array();
	$('input[name="courseIds"]').each(function() {
		courseIds.push($(this).val());
	});
	var data = {
		examPlanId: $('#entity_examPlanId').val(),
		examBatchCode: $('#entity_examBatchCode').val(),
		examPlanName: $('#entity_examPlanName').val(),
		courseIds: courseIds,
		examNo: $('#entity_examNo').val(),
        type: $('#entity_type').val(),
        documentFileName: $('#documentFileName').val(),
        documentFilePath: $('#documentFilePath').val(),
		examStyle: $('#entity_examStyle').val(),
		specialtyIds: $(':input[name="specialtyIds"]').val(),
		xkPercent:$("#entity_xkPercent").val(),
		examSt: $('#entity_examSt').val(),
		examEnd: $('#entity_examEnd').val(),
		examPlanOrder: $(':input[name="examPlanOrder"]:checked').val(),
		examPlanLimit:$('#entity_examPlanLimit').val(),
		subjectType: $('#subjectType').val()
	};
	
	return data;
}


function dateValid(data){
	if(!data.examBatchCode) {
		alert('请选择考试计划');
		return false;
	}
	if(!data.examPlanName) {
		alert('请填写开考科目名称');
		return false;
	}
	if(data.courseIds.length == 0) {
		alert('请添加课程');
		return false;
	}
	if(!data.examNo) {
		alert('请填写试卷号');
		return false;
	}
	if(!data.type) {
		alert('请选择考试形式');
		return false;
	}
	if(!data.examStyle) {
		alert('请选择考试方式');
		return false;
	}
	/*if(!data.xkPercent) {
		alert('请填写形考比例');
		return false;
	}*/
	if(!data.examSt || !data.examEnd) {
		alert('请填写考试时间');
		return false;
	}
	if(!data.examPlanOrder) {
		alert('请选择考试预约方式');
		return false;
	}

	var es = new Date(data.examSt);
	var ee = new Date(data.examEnd);
	if(es > ee) {
		alert('考试开始时间不能大于考试结束时间');
		return false;
	}
	
	return true;
}

function uploadCallback() {
    var fileName = $("#documentFileName").val();
    $("#uploadBtn").parent().html('<div class="alert alert-success"><h4><i class="icon fa fa-check"></i> '+fileName+'</h4></div>');
}