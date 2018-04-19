$(function() {
	$('#auto_arrange').click(autoArrange);
	$('#arrange_export').click(exportArrange);
});

function autoArrange() {
	alert('待完善');
}

function exportArrange() {
	var examBatchCode = $('#examBatchCode').val();
	if(!examBatchCode) {
		alert('请先选择考试批次');
		return;
	}
	$('#uploadForm').submit();
}
