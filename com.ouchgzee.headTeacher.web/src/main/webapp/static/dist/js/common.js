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

function closeSelfIframe(iframeName){
  top.$("iframe[name='"+iframeName+"']").closest('.jconfirm-box').find('.closeIcon').click();
}

/* inner page */
if($('body').hasClass('inner-page-body')){
	/* jump to Top */
	var htmlTmp=[
		'<div class="jump-top">',
		'<a href="#" role="button" class="btn-block">',
		'<i class="fa fa-fw fa-jump-top"></i>',
		'<span class="f12">TOP</span>',
		'</a>',
		'</div>'
	];
	if($('.jump-top').length<=0){
		$('body')
		.append(htmlTmp.join(""));
	}
	$('body').on('click', '.jump-top a', function(event) {
		event.preventDefault();
		$("html,body").animate({
			scrollTop: 0},300);
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

}

/* 禁止键盘的"Backspace"键事件 */
$(document).on('keydown', function(e) {
	var keyEvent;   
   if(e.keyCode==8){   
       var d=e.srcElement||e.target;   
        if(d.tagName.toUpperCase()=='INPUT'||d.tagName.toUpperCase()=='TEXTAREA'){   
            keyEvent=d.readOnly||d.disabled;   
        }else{   
            keyEvent=true;   
        }   
    }else{   
        keyEvent=false;   
    }   
    if(keyEvent){   
        e.preventDefault();   
    }
});
