
/**初使化一级菜单**/
$(".operion-more-menu > ul > li").each(function(i,ele){
	var $that=$(this);
	$(this).attr("data-id",i+1);
	$(this).find(".menu-temp a").each(function(i,n){
		$(this).attr("data-id",$that.attr("data-id")+"_"+(i+1));
	});
	$(".first-level-menu").append([
		'<li data-id="'+$that.attr("data-id")+'" data-url="'+$that.find('.menu-temp a:first').attr('href')+'">',
			$that.children('a:first').text(),
		'</li>'
	].join(''));
}).on("click",function(){
	$(this).addClass("cur").siblings().removeClass("cur");
	$(".sidebar-menu").html($(this).find(".menu-temp").html()).children('.treeview').first().addClass('active');
	$(this).closest('.dropdown').children('.dropdown-toggle').click();

	var url=$(".menu-temp a",this).attr("href");
	if(url!="#"){
		loadPage(url);
	}

	$(".first-level-menu > li[data-id='"+$(this).data("id")+"']").addClass('cur').siblings().removeClass('cur');
	return false;
});
$(".sidebar-menu").html($(".operion-more-menu > ul > li[data-id]:eq(0)").find(".menu-temp").html());

$(".first-level-menu").on("click",'li',function(){
	$(this).addClass('cur').siblings().removeClass('cur');
	$(".dropdown-menu > li[data-id='"+$(this).attr("data-id")+"']").click();
	if($(this).data('url')){
		loadPage($(this).data('url'));
	}
});

/* 页面加载方法 */
function loadPage(url){
	var $contentWrapper=$(".content-wrapper");
	var $container=$(".page");
	var $loading=$contentWrapper.children(".overlay-wrapper");

	if($loading.length<=0){
		$loading=$('<div class="overlay-wrapper loading"><div class="overlay"><i class="fa fa-refresh fa-spin" style="top:200px !important"></i></div></div>');
		$contentWrapper.prepend($loading);
	}

	$loading.show();

	if (navigator.userAgent.indexOf('MSIE') >= 0){
		url=encodeURI(url);
	}
	
	var $iframe=$container.children("iframe");
	$iframe.prop({src:url});
	$iframe.on('load',function(){
		$loading.hide();
	})

	resetPageHeight();//页的高度设置
}

/**侧边菜单**/
$(document).on("click",".sidebar-menu a",function(event){
	event.preventDefault();

	if($(this).attr("href")=="#"){
		return false;
	}
	var domThis=this;
	var domId=$(domThis).attr("data-id");
	var $parentLi=$(domThis).closest("li");
	var $sidebarList=$(".sidebar-menu > li");
	
	$("li",$sidebarList).removeClass("active");
	$parentLi.addClass("active");

	$sidebarList.filter(function(){
		return $(this).children("a").attr("href")!="#";
	}).not($parentLi.get(0)).removeClass("active");

	loadPage(domThis.href);
});

/**其它**/
$(window).on('resize load',function(){
	resetPageHeight();
});


/* 每一页的高度重设 */ 
function resetPageHeight(){
	$(".page").height($(window).height()-parseInt($(".content-wrapper").css("padding-top")));
}