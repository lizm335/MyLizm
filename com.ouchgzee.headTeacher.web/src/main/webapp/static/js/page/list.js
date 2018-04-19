$().ready( function() {
	var stime = 4000;//定时器毫秒
	var time;//定时器

	//高级搜索
	$(".search-more-in").click(
		function() {
			var $that = $(this);
			var $child = $that.children(".fa-fw");
			if ($child.hasClass("fa-caret-down")) {
				$child.addClass("fa-caret-up").removeClass(
					"fa-caret-down");
			} else if ($child.hasClass("fa-caret-up")) {
				$child.addClass("fa-caret-down").removeClass(
					"fa-caret-up");
			}
			$(this).closest(".box").find(".form-search-more")
				.slideToggle("fast")
		});

	// //单行单条删除
	// $('.del-one').click(function(){
	// 	var $this = $(this);
	// 	var id=$(this).attr('val');
	// 	$.confirm({
	// 		title: '提示',
	// 		content: '确认删除？',
	// 		confirmButton: '确认',
	// 		icon: 'fa fa-warning',
	// 		cancelButton: '取消',
	// 		confirmButtonClass: 'btn-primary',
	// 		closeIcon: true,
	// 		closeIconClass: 'fa fa-close',
	// 		confirm: function () {
	// 			$.post("delete",{ids:id},function(data){
	// 				if(data.successful){
	// 					showSueccess(data);
	// 					$this.parent().parent().parent().remove();
	// 					/*$.alert({
	// 					 title: '成功',
	// 					 icon: 'fa fa-exclamation-circle',
	// 					 confirmButtonClass: 'btn-primary',
	// 					 content: '数据删除成功！',
	// 					 confirmButton: '确认',
	// 					 confirm:function(){
	// 					 //$("#listForm").submit();
	// 					 //删除当前行
	// 					 }
	// 					 });*/
	// 				}else{
	// 					showFail(data);
	// 					/*$.alert({
	// 					 title: '失败',
	// 					 icon: 'fa fa-exclamation-circle',
	// 					 confirmButtonClass: 'btn-primary',
	// 					 content: '数据删除失败！',
	// 					 confirmButton: '确认',
	// 					 confirm:function(){
	// 					 showFail(data);
	// 					 }
	// 					 });*/
	// 				}
	// 			},"json");
	// 		}
	// 	});
	// });
    //
	// //删除多个
	// $('.del-checked').click(function(){
	// 	var $checkedIds = $("table td input[name='ids']:enabled:checked");
	// 	if ($checkedIds.size() ==0) {
	// 		$.alert({
	// 			title: '提示',
	// 			icon: 'fa fa-exclamation-circle',
	// 			confirmButtonClass: 'btn-primary',
	// 			content: '至少删除一条数据！',
	// 			confirmButton: '确认'
	// 		});
	// 		return false;
	// 	}
	// 	$.confirm({
	// 		title: '提示',
	// 		content: '确认删除？',
	// 		confirmButton: '确认',
	// 		icon: 'fa fa-warning',
	// 		cancelButton: '取消',
	// 		confirmButtonClass: 'btn-primary',
	// 		closeIcon: true,
	// 		closeIconClass: 'fa fa-close',
	// 		confirm: function () {
	// 			$.post("delete",$checkedIds.serialize(),function(data){
	// 				if(data.successful){
	// 					showSueccess(data);
    //
	// 					var checked = $("table td input[name='ids']:enabled:checked");
	// 					for(var i=0;i<checked.length;i++){
	// 						$(checked[i]).parent().parent().remove();
	// 						$('.select-all').prop("checked", false);
	// 					}
	// 					/*$.alert({
	// 					 title: '成功',
	// 					 icon: 'fa fa-exclamation-circle',
	// 					 confirmButtonClass: 'btn-primary',
	// 					 content: '数据删除成功！',
	// 					 confirmButton: '确认',
	// 					 confirm:function(){
	// 					 //$("#listForm").submit();
	// 					 }
	// 					 });*/
	// 				}else{
	// 					showFail(data);
	// 					/*$.alert({
	// 					 title: '失败',
	// 					 icon: 'fa fa-exclamation-circle',
	// 					 confirmButtonClass: 'btn-primary',
	// 					 content: '数据删除失败！',
	// 					 confirmButton: '确认',
	// 					 confirm:function(){
	// 					 showFail(data);
	// 					 }
	// 					 });*/
	// 				}
	// 			},"json");
	// 		}
	// 	});
	// });

	//选中全部--公共
	$('.select-all').click( function() {
		var $enabledIds = $("table input[name='ids']:enabled");
		if ($(this).prop("checked")) {
			$enabledIds.prop("checked", true);
		} else {
			$enabledIds.prop("checked", false);
		}
	});

	//单个checkbox点击事件--公共
	$(".checkbox").click( function() {
		var checkSize = $("table input[name='ids']:enabled:checked").size();
		var boxSize = $("table input[name='ids']:enabled").size();
		if(checkSize == boxSize){
			$('.select-all').prop("checked", true);
		}else{
			$('.select-all').prop("checked", false);
		}
	});

	//Date picker 日期范围
	$('.input-daterange').datepicker({
		autoclose: true,
		format : 'yyyy-mm-dd', //控件中from和to 显示的日期格式
		language: 'zh-CN',
		todayHighlight: true
	});

	//重置按钮
	$(".btn-reset").click(function(){
		$("form").find(':input').val('');
		//下拉列表初始化(待完善)
		$("form").find('.selectpicker').each(function(index,element){
			//$(element).val("").text("请选择");;
		});
	});

	$("#page").keyup(function(){
		var page = $("#page").val();
		var totalPages = $("#totalPages").val();
		if(isNaN(page)){
			$("#page").val(1);
		}else if(page <= 0){
			$("#page").val(1);
		} else if(page > totalPages){
			$("#page").val(totalPages);
		}
	});

    //搜索按钮提交
    $(".btn-primary").click(function() {
        $("#page").val(1);//点击查询
    });

    // 页码输入
    $("#page").keypress(function(event) {
        var key = event.keyCode ? event.keyCode : event.which;
        if ((key == 13 && $(this).val().length > 0) || (key >= 48 && key <= 57)) {
            return true;
        } else {
            return false;
        }
    });

	//定时器
	setTimeout(function(){$(".alert:visible:last").hide()},3000);
	
	//filter tabs
	$(".filter-tabs2 li").click(function(event) {
		$(""+$(this).attr("role")+"").val($(this).attr("value"));
		$(""+$(this).attr("lang")+"").val("");
		$("#listForm").submit();
	});
	
	
	//添加对selectpicker重置状态的支持，由于没有统一重置规范，临时解决
	$("#listForm").on("reset", function(){
		var $curForm = $(this);
		setTimeout(function(){
			$curForm.find(".selectpicker").selectpicker("refresh");//同步状态
		},0);
	});
	
});

	//顶部提示
	function showMessage(data){
		var tmp=top.$("#msgBoxDIV");
		if(data.successful){
			tmp.removeClass("alert-danger").addClass("alert-success");
			tmp.find('.no-margin').html("<div class='no-margin f16'><i class='icon fa fa-check'></i>"+data.message+"</div>");
		}else{
			tmp.removeClass("alert-success").addClass("alert-danger");
			tmp.find('.no-margin').html("<div class='no-margin f16'><i class='icon fa fa-warning'></i>"+data.message+"</div>");
		}
		tmp.show();
		setTimeout(function(){ top.$("#msgBoxDIV").hide()},3000);
	};
	
	
	function showSueccess(data){
		$('.alert-success').show();
		$('.alert-success').html("<button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>"+data.message+"</div>");
		setTimeout(function(){ $(".alert:visible:last").hide()},3000);
	}
	
	function showFail(data){
		$('.alert-danger').show();
		$('.alert-danger').html("<button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i> "+data.message+"</div>");
		setTimeout(function(){ $(".alert:visible:last").hide()},3000);
	}
	//页码跳转
	function pageSkip(page) {
		$("#page").val(page);
		//因为不想改旧的form，在这里写了一个page参数
		if($("#listForm").find("input[name='page']").val()==undefined){
			$("#listForm").append('<input type="hidden" name="page" value="'+page+'">');
		}
		$("#listForm").submit();
		return false;
	}
	
	