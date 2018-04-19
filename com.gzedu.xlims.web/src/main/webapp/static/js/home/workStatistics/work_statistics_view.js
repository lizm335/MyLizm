
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


//查看答疑详情
$('[data-role="view-more"]').on('click', function(event) {
	event.preventDefault();
	var _this=this;
	$.mydialog({
		id:'view-more',
		width:900,
		height:600,
		zIndex:11000,
		content: 'url:'+$(_this).attr('href')
	});
});

/*日期控件*/
;(function(){
	var $dtTime=$('[data-role="datetime"]');
	$dtTime.datepicker({
		language:'zh-CN',
		format:'yyyy-mm-dd',
		autoclose:true,
		todayHighlight:true,
		beforeShowDay:function(date){
			var d=new Date(date);

			var result={
				enabled:false,
				tooltip:'点击查看该天工作动态',
				classes:''
			}

			var dateArray = getAllDate();

			$.each(dateArray, function(index, val) {
				var compare=new Date(val);
				if(compare.getFullYear()==d.getFullYear() && compare.getMonth()==d.getMonth() && compare.getDate()==d.getDate())
				{
					result.enabled=true;
					result.classes='active';
					return false;
				}
			});

			return result;
		}
	});

	var dtPicker=$dtTime.data('datepicker');//获取日期控件实例
	$dtTime.on('show', function(event) {
		var $today=$('.datepicker-days tbody td.today',dtPicker.picker);
		$today.html(
			[
				'<b class="show position-relative text-no-bold">',
				$today.text(),
				'<i class="position-absolute" style="border-width: 5px;border-color: red red transparent transparent;    border-style: solid;right: -5px;top: -4px;"></i>',
				'</b>'
			].join('')
		);
	})
	//点击选中某一日期
		.on('changeDate', function(event) {
			/*
			 event={
			 date:选中的日期
			 format:格式化选中的日期，并返回结果
			 }
			 如：event.format('yyyy-mm-dd')
			 */
			firstLoadUserBehavior();
		});

})();

/**
 * 获取有行为记录的时间
 * 加载只获取一次
 * @returns {Array}
 */
var dateArrayFlag = false;
var dateArray = [];
function getAllDate() {
	if(!dateArrayFlag) {
		$.ajax({
			url: ctx + "/admin/workStatistics/getAllDate/" + $(':input[name="userId"]').val(),
			dataType: 'json',
			data: {},
			type: 'get',
			async: false, // 默认设置下，所有请求均为异步请求（也就是说这是默认设置为 true ）
			success: function (data) {
				if (data.successful == true) {
					dateArray = data.obj;
				}
			}
		});
		dateArrayFlag = true;
	}
	return dateArray;
}

var page = 1; // 行为记录当前页标识
function firstLoadUserBehavior() {
	page = 1;
	$('.approval-list').empty();
	$('#earliestLoginDt').empty();
	$('#latestLogoutDt').empty();
	$('#totalLoginTime').empty();
	countLoginLog();
	queryByPage(page);
}
firstLoadUserBehavior();

/**
 * 获取更多行为记录
 */
function moreUserBehavior() {
	page = page + 1;
	queryByPage(page);
}

/**
 * 行为记录的登录情况
 */
function countLoginLog() {
	$.ajax({
		url: ctx + "/admin/workStatistics/countLoginLog/" + $(':input[name="userId"]').val(),
		dataType: 'json',
		data: {datetime:$(':input[name="datetime"]').val()},
		type: 'post',
		success: function(data) {
			if(data.successful == true) {
				if(data.obj.earliestLoginDt != null)
					$('#earliestLoginDt').text(new Date(data.obj.earliestLoginDt).Format("HH:mm:ss"));
				if(data.obj.latestLogoutDt != null)
					$('#latestLogoutDt').text(new Date(data.obj.latestLogoutDt).Format("HH:mm:ss"));
				if(data.obj.totalLoginTime != null)
					$('#totalLoginTime').text(parseFloat(data.obj.totalLoginTime/60).toFixed(1) + "小时");
			} else {
				alert(obj.message);
			}
		}
	});
}

/**
 * 行为记录分页
 * @param page
 */
function queryByPage(page) {
	$.ajax({
		url: ctx + "/admin/workStatistics/listUserBehaviorByPage/" + $(':input[name="userId"]').val(),
		dataType: 'json',
		data: {datetime:$(':input[name="datetime"]').val(), page:page},
		type: 'post',
		success: function(data) {
			if(data.successful == true) {
				for(var i=0;i<data.obj.content.length;i++) {
					var info = data.obj.content[i];
					var dl = '<dl class="approval-item">'
								+ '<dt class="clearfix">'
									+ '<div class="a-tit">'
										+ '<b>'+info.operation+'</b>'
									+ '</div>'
									+ '<span class="time state-lb margin_t5 f14">'+new Date(info.createDate).Format("HH:mm:ss")+'</span>'
								+ '</dt>'
								+ (info.remark!=null?'<dd class="no-margin pad-t5">'
									+ '<div class="gray9 f12">'+info.remark+'</div>'
								+ '</dd>':'')
								+ '<dd class="decoration"></dd>'
						+ '</dl>';
					$('.approval-list').append(dl);
				}
				if(!data.obj.lastPage) {
					var dl = '<dl class="approval-item">'
							+ '<dt class="clearfix">'
								+ '<div class="a-tit">'
									+ '<a href="javascript:moreUserBehavior();" class="btn btn-default moreUserBehavior">加载更多</a>'
								+ '</div>'
							+ '</dt>'
							+ '<dd class="decoration"></dd>'
						+ '</dl>';
					$('.approval-list').append(dl);
				}
			} else {
				alert(obj.message);
			}
		}
	});
}
