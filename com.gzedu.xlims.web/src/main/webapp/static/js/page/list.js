
$().ready( function() {
	
	//如果搜索列表为0，显示暂无数据
	var $tabel = $('#list_table');
	var trLength = $tabel.find('tr').length;
	if(trLength<=1){
		$tabel.append("<tr><td align='center' colspan='"+$tabel.find('thead th').length+"'>暂无数据</td></tr>");
	}  
	  
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
	
	//单行单条删除
	 $('.del-one').click(function(){
		 var $this = $(this);
		 var id=$(this).attr('val');
           $.confirm({
               title: '提示',
               content: '确认删除？',
               confirmButton: '确认',
               icon: 'fa fa-warning',
               cancelButton: '取消',  
               confirmButtonClass: 'btn-primary',
               closeIcon: true,
               closeIconClass: 'fa fa-close',
               confirm: function () { 
               	 $.post("delete",{ids:id},function(data){
               		if(data.successful){
               			showMessage(data);
               			window.location.reload();
               			//showSueccess(data);
               			//$this.parent().parent().parent().remove();
               		}else{
               			showMessage(data);
               		}
               },"json"); 
               } 
           });
       });
	 
	 //删除多个
	 $('.del-checked').click(function(){
		 var $checkedIds = $("table td input[name='ids']:enabled:checked");
	  		if ($checkedIds.size() ==0) {
	  			$.alert({
             	    title: '提示',
             	    icon: 'fa fa-exclamation-circle',
             	  	confirmButtonClass: 'btn-primary',
             	    content: '至少删除一条数据！',
             	    confirmButton: '确认'
             });
	  			return false;
			}
             $.confirm({
                 title: '提示',
                 content: '确认删除？',
                 confirmButton: '确认',
                 icon: 'fa fa-warning',
                 cancelButton: '取消',  
                 confirmButtonClass: 'btn-primary',
                 closeIcon: true,
                 closeIconClass: 'fa fa-close',
                 confirm: function () {
                 	$.post("delete",$checkedIds.serialize(),function(data){
                 		if(data.successful){ 
                 			
                 			showMessage(data);
                 			window.location.reload();
                 			
                 			//showSueccess(data);
                   			/*var checked = $("table td input[name='ids']:enabled:checked");
                	    	for(var i=0;i<checked.length;i++){
                	    		$(checked[i]).parent().parent().remove();
                	    		$('.select-all').prop("checked", false);
                	    	} */
                   		}else{
                   			showMessage(data);
                   		}
                 	},"json"); 
                 }
             });
	 });
	 
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
	 
	// 单击选中所有行
	 $("body").on("click",".select-all",function(){
	   var $tbl=$(this).closest('.table');
	   if($(this).is(":checked")){
	     $tbl.find('tr').addClass("selected");
	     $tbl.find(':checkbox').prop("checked",true);
	   }
	   else{
	     $tbl.find('tr').removeClass("selected");
	     $tbl.find(':checkbox').prop("checked",false);
	   }
	 });
	 /*.on('click', 'tbody tr', function (event) { 
	   var $tbl=$(this).closest(".table");
	   var ck=$(this).find(":checkbox");
	   $(this).toggleClass('selected');
	   if(!$(event.target).is(":checkbox")){
	     if(!ck.is(":checked")){
	       ck.prop("checked",true);
	     }
	     else{
	       ck.prop("checked",false); 
	     }
	   }
	   if(!ck.is(":checked")){
	     $tbl.find(".select-all").prop("checked",false);
	   }
	 });*/
	 
	//Date picker 日期范围
	$('.input-daterange').datepicker({
		  autoclose: true,
	      format : 'yyyy-mm-dd', //控件中from和to 显示的日期格式
	      language: 'zh-CN',
	      todayHighlight: true
	});
	
/*	$('.single-datetime').daterangepicker({
        singleDatePicker: true,
        showDropdowns : true,
        timePicker : true, //是否显示小时和分钟
        timePickerIncrement : 5, //时间的增量，单位为分钟
        timePicker12Hour : false, //是否使用12小时制来显示时间
        format : 'YYYY-MM-DD HH:mm', //控件中from和to 显示的日期格式
        locale : {
            applyLabel : '确定',
            cancelLabel : '取消',
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
    });*/
	
	/*日期控件*/
	$('.reservation').datepicker({
		language:'zh-CN',
		todayHighlight:true,
		format:'yyyy-mm-dd'
	});
	  
	//重置按钮
	$(".btn-reset").click(function(){
		//重置除隐藏域的input 
		$("form").find(':text').val('');
		//下拉列表初始化
		$("form").find('select').val('').selectpicker('refresh');
		$("input[name^='search_']").each(function(){
			$(this).val('');
		});
		$("select[name^='search_']").each(function(){
			$(this).val('');
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
	 
	// 页码跳转
	$.pageSkip = function(page) {
		$("#page").val(page);
		if($("#listForm").find("input[name='page']").val()==undefined){
			$("#listForm").append('<input type="hidden" name="page" value="'+page+'">');
		}
		$("#listForm").submit();
		return false;
	}
	
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
	//setTimeout(function(){$(".alert:visible:last").hide()},3000);
	setTimeout(function(){ top.$("#msgBoxDIV").hide()},3000);
	setTimeout(function(){$(".alert-success:visible:last").hide()},3000);
	setTimeout(function(){$(".alert-danger:visible:last").hide()},3000);
	
	//filter tabs
	$(".filter-tabs2 li").click(function(event) {
		$(""+$(this).attr("role")+"").val($(this).attr("value"));
		$(""+$(this).attr("lang")+"").val("");
		$("#listForm").submit();
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

/**
 * 返回文件导入框
 * @param actionName 导入文件的方法
 * @param fileParamName  接收文件参数名
 * @param modelName  模块名，用于生成单独的文件夹保存导入结果，默认为"default"
 * @param downloadFileUrl  下载模板的路径
 * @param downloadButton  下载模板的按钮内容
 * @param title  导入框显示的标题
 * @param step1    第一步的标题
 * @param step2    第二步的标题
 * @param step3    第三步的标题
 * @param content1  第一步的提示内容
 * @param content2 第二步的提示内容
 * @param alert  第一步中的标注内容
 * @return
 */
function excelImport(actionName, fileParamName, modelName, downloadFileUrl, downloadButton,
		title, step1, step2, step3, content1, content2, alert,callbackFun) {
	if (typeof(actionName) == "undefined" || actionName == null) {
		return false;
	}
	if (typeof(fileParamName) == "undefined" || fileParamName == null) {
		return false;
	}
	if (typeof(modelName) == "undefined" || modelName == null) {
		modelName = "default";
	}
	if (typeof(downloadFileUrl) == "undefined" || downloadFileUrl == null) {
		downloadFileUrl = "javascript:void(0)";
	}
	if (typeof(downloadButton) == "undefined" || downloadButton == null) {
		downloadButton = "下载标准模板";
	}
	if (typeof(title) == "undefined" || title == null) {
		title = "批量导入";
	}
	if (typeof(step1) == "undefined" || step1 == null) {
		step1 = "下载模板";
	}
	if (typeof(step2) == "undefined" || step2 == null) {
		step2 = "导入数据";
	}
	if (typeof(step3) == "undefined" || step3 == null) {
		step3 = "导入结果反馈";
	}
	if (typeof(content1) == "undefined" || content1 == null) {
		content1 = "为了方便你的工作，我们已经准备好了标准模板<br>你可以点击下面的下载按钮，下载标准模板。";
	}
	if (typeof(content2) == "undefined" || content2 == null) {
		content2 = "请选择你要导入的文件";
	}
	if (typeof(alert) == "undefined" || alert == null) {
		alert = "注：为了能够准确的导入数据，请务必按照标准模板的要求进行填写";
	}
	var url = ctx+"/excelImport/importForm?actionName="+actionName+"&fileParamName="+fileParamName+"&modelName="+modelName
	+"&downloadFileUrl="+downloadFileUrl+"&downloadButton="+downloadButton+"&title="+title+"&step1="+step1+"&step2="+step2
	+"&step3="+step3+"&content1="+content1+"&content2="+content2+"&alert="+alert+"&callbackFun="+callbackFun;
	
	$.mydialog({
	  id:'import',
	  width:600,
	  height:415,
	  zIndex:11000,
	  content: 'url:'+url
	});
}


$.ajaxSetup({
    statusCode : {
	401 : function(data) {
	    if($('.import-modal').length>0){
		$.closeDialog($('.import-modal'));
	    }
	    top.loginTimeout(); 
	}
    }
});