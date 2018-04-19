var daterangepickerOpt={
	"singleDatePicker": true,
	"format": "MM/DD/YYYY",
	"locale": {
		"daysOfWeek": [
			"一",
			"二",
			"三",
			"四",
			"五",
			"六",
			"日"
		],
		"monthNames": [
			"一月",
			"二月",
			"三月",
			"四月",
			"五月",
			"六月",
			"七月",
			"八月",
			"九月",
			"十月",
			"十一月",
			"十二月"
		],
		"firstDay": 0
	}
}

var dataTableOpt={
	"dom":"<'row'<'col-sm-12'tr>>" +
		  "<'page-container clearfix'<'pageing-info'i><'pageing-list'p>>",
	"paging": true,
	"lengthChange": false,
	"searching": true,
	"ordering": false,
	"info": true,
	"autoWidth": false,
	"pagingType": "simple_numbers",
	"language": {
		"infoFiltered":"",
		"zeroRecords": "找不到符合条件的数据",
		"infoEmpty": "",
		"info": "共 _PAGES_ 页，到第 <input type='text' class='form-control jump-page-input' value='_PAGE_'> 页 <button class='btn btn-block btn-default sure-btn'>确定</button>",
		"paginate": {
		  "previous": "上一页",
		  "next": "下一页"
		}
	}
}

$(function(){
	$("a[href='#']").click(function(event) {
		event.preventDefault();
	});
	function resizeInnerContent(){
		if($(".inner-page-body",document).length>0){
			var $content=$(".inner-page-body > .content");
			var differ=$(window).height()-$(".content-header").outerHeight(true)-$(".site-footer").outerHeight(true);
			$content.css('min-height', differ);
			if(differ>$content.outerHeight(true)){
				$(".site-footer").css("position","fixed")
			}
		}
	}
	$(window).on('load resize', function(event) {
		resizeInnerContent()
	});

	$("[data-role='back-off']").click(function(event) {
		window.history.back();
	});
});

//内部页面点击链接跳转
$("[data-role='single-page']").click(function(event) {
	event.preventDefault();
	top.loadPage($(this).attr("href"));
});

//增加一天
function increaseOnedate(d){
	var date = new Date(d);
	date.setDate(date.getDate()+1);
	return date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
}

//减少一天
function decreaseOnedate(d){
	var date = new Date(d);
	date.setDate(date.getDate()-1);
	return date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
}