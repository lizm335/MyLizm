$(function() {
	$('.operion-del').click(function(){
		deleteBatch($(this).attr('bid'));
	});
	
	//状态选择事件
	$('#bstatus').change(function(){
		$('#search_LT_bookSt').val('');
		$('#search_GT_bookSt').val('');
		$('#search_LT_bookEnd').val('');
		$('#search_GT_bookEnd').val('');
		
		var d = new Date();
		var ymd = d.toISOString().split('T')[0];
		var hms = (d.getHours()>9?d.getHours():"0"+d.getHours())+":"+(d.getMinutes()>9?d.getMinutes():"0"+d.getMinutes())+":"+(d.getSeconds()>9?d.getSeconds():"0"+d.getSeconds());
		var dStr = ymd+" "+hms;
		var s = $('#bstatus').val();
		if(s == 1) {
			$('#search_GT_bookSt').val(dStr);
		} else if(s == 2) {
			$('#search_LT_bookSt').val(dStr);
			$('#search_GT_bookEnd').val(dStr);
		} else if(s == 3) {
			$('#search_LT_bookEnd').val(dStr);
		} else {
			$('#bstatus').val('');
		}
	});
	
	var singleDateTimePickerOpt = {
	        singleDatePicker: true,
	        showDropdowns : true,
	        timePicker : true, //是否显示小时和分钟
	        timePickerIncrement : 5, //时间的增量，单位为分钟
	        timePicker12Hour : false, //是否使用12小时制来显示时间
	        timePickerSeconds: true, 
	        format : 'YYYY-MM-DD HH:mm:ss', //控件中from和to 显示的日期格式
	        locale : {
	            applyLabel : '确定',
	            cancelLabel : '取消',
	            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
	            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
	            firstDay : 1
	        }
	    };
	
	    $('.single-datetime').daterangepicker(singleDateTimePickerOpt);
	
	//初始化批次状态与操作按钮
	statusInit();
});

function statusInit() {
	var tdArr = $("td[name='status_td']");
	var now = new Date();
	for(var i=0;i<tdArr.length;i++) {
		var bookSt = $(tdArr[i]).attr('bs');
		var bookEnd = $(tdArr[i]).attr('be');
		if(bookSt != '') {
			if(now < new Date(bookSt)) {
				$(tdArr[i]).html('未开始预约');
//				$('#edit_'+$(tdArr[i]).attr('pid')).attr('editid', $(tdArr[i]).attr('pid'));
				$('#edit_'+$(tdArr[i]).attr('pid')).show();
				$('#del_'+$(tdArr[i]).attr('pid')).show();
				continue;
			}
		}
		if(bookEnd != '') {
			if(now < new Date(bookEnd)) {
				$(tdArr[i]).html('预约中');
				continue;
			} else {
				$(tdArr[i]).html('已结束预约');
			}
		}
	}
}

function deleteBatch(id) {
	if(window.confirm('删除批次会把其关联的考试计划同步删除, 是否确认删除?')) {
		$.ajax({
			  url: "/exam/new/batch/delete?id="+id,
			  dataType: 'json',
			  type: 'get',
			  success: function(obj) {
				  if(obj.successful == true) {
					  alert(obj.message);
					  window.location.href = '/exam/new/batch/list';
				  } else {
					  alert(obj.message);
				  }
			  }
		});
	}
}