$(function() {
	$('#btn-create').click(createSubject);
	$('#btn-update').click(updateSubject);
	
	$('#entity_kch').change(function(){
		var cid=$("#entity_kch").find("option:selected").attr('cid');
		$('#entity_courseId').val(cid);
	});
});

function createSubject() {
	var data = getFormData();
	if(dataValid(data)) {
		$.ajax({
			  url: $("#inputForm").attr("action"),
			  dataType: 'json',
			  data: data,
			  type: 'post',
			  success: function(obj) {
				  if(obj.successful == true) {
					  alert('新增成功');
					  window.location.href = ctx + '/exam/new/subject/list';
				  } else {
					  alert(obj.message);
				  }
			  }
		});
	}
}

function updateSubject() {
	var data = getFormData();
	if(dataValid(data)) {
		$.ajax({
			  url: ctx + "/exam/new/subject/update",
			  dataType: 'json',
			  data: data,
			  type: 'post',
			  success: function(obj) {
				  if(obj.successful == true) {
					  alert('更新成功');
					  window.location.href = ctx + '/exam/new/subject/list';
				  } else {
					  alert(obj.message);
				  }
			  }
		});
	}
}

function  getFormData() {
	var data = {
		subjectId: $('#entity_subjectId').val(),
		subjectCode: $('#entity_subjectCode').val(),
		courseId: $('#entity_courseId').val(),
		kch: $('#entity_kch').val(), 
		name: $('#entity_name').val(),
		examNo: $('#entity_exam_no').val(),
		memo: $('#entity_memo').val(),
		type: $('#entity_type').val()
	};
	return data;
}

function dataValid(data) {
	if(!data.subjectCode || !data.type) return false;	
	if(!data.courseId) {
		alert('请选择课程');
		return false;
	}
	if(!data.name) {
		alert('请填写考试科目名称');
		return false;
	}
	if(!data.examNo) {
		alert('请填写试卷号');
		return false;
	}
	return true;
}