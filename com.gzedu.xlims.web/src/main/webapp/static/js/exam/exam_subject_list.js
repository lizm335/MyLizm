$(function() {
	$('#export_subjects').click(exportSubjects);
	$('#setting_upload').click(settingUpload);
	
	$('#uploadForm :input[name="file"]').change(function () {
	    var file = $(this).val();
	    var strs = file.split('.');
	    var suffix = strs [strs .length - 1];

	    if (suffix != 'xls' && suffix != 'xlsx' && suffix != 'xlsm' && suffix != 'xlsb') {
	        alert("你选择的不是EXCEL文件,请选择EXCEL文件！");
	        this.outerHTML = this.outerHTML; //这样清空，在IE8下也能执行成功
	    }
	});
	
});

function exportSubjects() {
	if($('#dataExportForm').length == 0) {
		$('body').append('<form id="dataExportForm" action="'+ctx+'/exam/new/subject/export?examType='+examType+'" method="POST" style="display:none;" target="_blank"></form>');
	}
	$('#dataExportForm').submit();
}

function dataValid() {
	if($('#file').val() == '') {
		alert('请先选择文件');
		return false;
	}
	return true;
}

function settingUpload() {
	if(dataValid()) {
		$('#uploadForm').submit();
	}
}

