$(function() {
	$('#btn-create').click(createRoom);
	$('#btn-update').click(updateRoom);
});

function createRoom() {
	var data = getFormData();
	if(dataValid(data)) {
	$.ajax({
		  url: ctx + "/exam/new/room/create",
		  dataType: 'json',
		  data: data,
		  type: 'post',
		  success: function(obj) {
			  if(obj.successful == true) {
				  alert('新增成功');
				  window.location.href = ctx + '/exam/new/room/list';
			  } else {
				  alert(obj.message);
			  }
		  }
	});	
	}
}

function updateRoom() {
	var data = getFormData();
	if(dataValid(data)) {
	$.ajax({
		  url: ctx + "/exam/new/room/update",
		  dataType: 'json',
		  data: data,
		  type: 'post',
		  success: function(obj) {
			  if(obj.successful == true) {
				  alert('更新成功');
				  window.location.href = ctx + '/exam/new/room/list';
			  } else {
				  alert(obj.message);
			  }
		  }
	});	
	}
}

function  getFormData() {
	var data = {
		examRoomId: $('#entity_examRoomId').val(),
		//examPointId: $('#entity_examPointId').val(),
		examPointId: $('#examPointId').val(),
		name: $('#entity_name').val(),
		seats: $('#entity_seats').val(),
		orderNo: $('#entity_orderNo').val()
	};
	return data;
}

function dataValid(data) {
	if(!data.name) {
		alert('请填写考场名称');
		return false;
	}
	if(!data.seats) {
		alert('请填写座位数');
		return false;
	}
	if(!(parseInt(data.seats) == data.seats) || parseInt(data.seats) <= 0){
		alert('座位数必须是大于0的整数');
		return false;
	}
	if(!(parseInt(data.orderNo) == data.orderNo) || parseInt(data.orderNo) <= 0){
		alert('顺序号必须是大于0的整数');
		return false;
	}
	return true;
}