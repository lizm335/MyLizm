//DataTable表格
var dTable=$('#dtable').DataTable({
  "paging": true,
  "lengthChange": false,
  "searching": false,
  "ordering": true,//是否支持排序
  "info": true,
  "autoWidth": false,
  "processing": true,  
  "pagingType": "simple_numbers",
  "language": {
	"zeroRecords": "暂无数据",
	"info": "共 _PAGES_ 页，到 <input type='text' class='form-control jump-page-input' value='_PAGE_'> 页 <button class='btn btn-block btn-default sure-btn'>确定</button>",
	"paginate": {
	  "previous": "上一页",
	  "next": "下一页"
	}
  }
});
//DataTable 单击跳转到某一页
$(dTable.table().container()).on("click",".sure-btn",function(){
	var pNum=parseInt($(this).siblings(".jump-page-input").val());
	if(pNum>0&&pNum<=dTable.page.info().pages)
	$(this).closest(".dataTables_wrapper").find(".paginate_button > a[data-dt-idx='"+pNum+"']").click();
}); 

//DataTable 单击选中行
$(dTable.table().body()).on('click', 'tr', function (event) {	
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
		$(dTable.table().header()).find(".select-all").prop("checked",false);
	}
});

//DataTable 单击选中所有行
$(dTable.table().node()).on("click",".select-all",function(){
	if($(this).is(":checked")){
		$(dTable.table().body()).find('tr').addClass("selected");
		$(dTable.table().body()).find(':checkbox').prop("checked",true);
	}
	else{
		$(dTable.table().body()).find('tr').removeClass("selected");
		$(dTable.table().body()).find(':checkbox').prop("checked",false);
	}
});


//删除选中的表格项
 $('.btn-del').click( function () {
	dTable.rows('.selected').remove().draw(false);
	$(".select-all").prop("checked",false);
} ); 
/*$('.operion-del').click(function(){  
    var id = $(this).attr("val");  
    var $modalTemplate=$("#modal-template");
    $modalTemplate.on('hide.bs.modal', function (e) {
    	$that.removeAttr('title');
	});
});*/
//多个删除
function deleteAll() {
	if ($("[data-name=check-id]:checked").length == 0) {
		alert('请选择删除的个人中心菜单！');
		return;
	}
	if (!window.confirm("确定删除？")) {
		return;
	}
	var url = '${pageContext.request.contextPath}'+ "/module/menu/delete";
	var array = [];
	$('input[data-name="check-id"]').each(function(e) {
		if ($(this).prop('checked')) {
			array.push($(this).attr('data-id'));
		}
	});
	var data = [ {
		name : 'id',
		value : array.join(',')
	} ];
	$.post(url, data, function(feedback) {
		alert(feedback.message);
		if (feedback.successful) {
			$("#all-check").prop('checked', false);
			window.location.href = '${pageContext.request.contextPath}' + "/module/menu/list";
		}
	});
}
//DataTable 删除单行数据
$(dTable.table().body()).on("click",".operion-del",function(){
	
	var $that=$(this);
	var dfTitle=this.title;
	var $modalTemplate=$("#modal-template");
	$modalTemplate.on("shown.bs.modal",function(e){
		$that.removeAttr('title');
	}).on("click","button[data-role='ok']",function(event){//确定删除
		$modalTemplate.modal('hide');
		dTable.row($that.parents('tr')).remove().draw(false);
	
		var id =  $that.closest("tr").find("input[name='id']").val();
		var url = "delete";
		var data = [ {
			name : 'id',
			value : id	
		} ];
		$.post(url, data, function(feedback) {
			alert(feedback.message);
			if (feedback.successful) {
				window.location.href = '${pageContext.request.contextPath}' + "/module/menu/list";
			}
		});
	}).on("click","button[data-role='cancel']",function(event){//取消删除
		$('#modal-template').modal('hide');
	});
}).on("mouseenter",".operion-del",function(){
	var $that=$(this);
	if(!$that.attr("title")){
		$that.attr("title",$that.attr("data-tempTitle"));
	}
}); 