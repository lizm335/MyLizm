$(function() {	
	var singleDateTimePickerOpt = {
	        singleDatePicker: true,
	        showDropdowns : true,
	        timePicker : true, //是否显示小时和分钟
	        timePickerIncrement : 5,// 时间的增量，单位为分钟
	        timePicker12Hour : false,// 是否使用12小时制来显示时间
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
});


function updateBatch() {
	var data = getFormData();
	if(dateValid(data)) {
		$.ajax({
			  url: "/exam/new/batch/update",
			  dataType: 'json',
			  data: data,
			  type: 'post',
			  success: function(obj) {
				  if(obj.successful == true) {
					  alert('更新成功');
					  window.location.href = '/exam/new/batch/list';
				  } else {
					  alert(obj.message);
				  }
			  }
		});
	}
		
}