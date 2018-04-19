// JavaScript Document

$().ready(function(){
	$(".tree_menu_list ul > li:first-child > .tree_box").addClass('first-child');
	$(".tree_menu_list ul > li:last-child > .tree_box").addClass('last-child').parent().addClass('last-li')
	$(".tree_menu_list li > .tree_box:only-child > i").removeClass('btn');
	$(".tree_menu_list .btn").click(function(){
		$(this).toggleClass('show').parent().siblings('ul').toggle(200);	
	})
	$(".tree_menu_list").find("input[type='checkbox']:checked").parent().addClass('checked').siblings(".btn").toggleClass('show').parent().siblings('ul').toggle(200);
	
	$(".tree_menu_list input:checkbox").click(function(){
		if($(this).attr("checked")){
			$(this).parent().parent().parent().find("input[type='checkbox']").attr('checked',true)
			.parent().addClass('checked')
			.siblings(".btn").addClass('show')
			.parent().siblings('ul').show(200),
			
			$(this).parents("ul").siblings(".tree_box").children(".tree_txt").addClass('checked').children("input[type='checkbox']").attr('checked',true)
		}
		else{
			$(this).parent().parent().parent().find("input[type='checkbox']").attr('checked',false).parent().removeClass('checked');
			
			 //存在问题
			var here = this.parentNode.parentNode;
			while((here = here.parentNode.parentNode) && (here.className != "tree_menu_list") ){
				var is = here.getElementsByTagName("i");
				var end_enable_num = 0;
				for(var i = 0; i < is.length; i++){
					if( is[i].className ==""){
						if(is[i].parentNode.getElementsByTagName("input")[0].checked){
							end_enable_num++;	
						}	
					}
				}
				if(end_enable_num ==0){
					
					$(here).parent().children(".tree_box").children(".tree_txt").removeClass('checked').children("input[type='checkbox']").attr('checked',false)	
					
				}
			}
			
			
		}	
	})	
})