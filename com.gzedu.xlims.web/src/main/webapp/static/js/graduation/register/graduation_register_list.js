$(function() {

    //高级搜索
    $("#more-search").on('shown.bs.collapse', function(event) {
        event.preventDefault();
        $('[data-target="#more-search"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
    }).on('hidden.bs.collapse', function(event) {
        event.preventDefault();
        $('[data-target="#more-search"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
    });

    $('.choiceState').click(function () {
        $(':input[name="search_EQ_expressSignState"]').val($(this).attr('val'));
        $('form#listForm').submit();
    });

    /**
     * 初始化本期毕业学员
     */
    $('a[data-role="import"]').click(function () {
        $('a[data-role="import"]').html('<i class="fa fa-fw fa-spinner"></i> 正在初始化，请稍等...');
        $.ajax({
            url: ctx + "/graduation/register/init",
            dataType: 'json',
            data: {},
            type: 'post',
            success: function(obj) {
                $('a[data-role="import"]').html('<i class="fa fa-fw fa-spinner"></i> 初始化本期毕业学员');
                if(obj.successful == true) {
                    alert('初始化完成！');
                    window.location.reload();
                } else {
                    alert(obj.message);
                }
            }
        });
    });

    /**
     * 已签收
     */
    $('a[action="express"]').click(function () {
        var id = $(this).attr('val');
        $.ajax({
            url: ctx + "/graduation/register/express",
            dataType: 'json',
            data: {id: id},
            type: 'post',
            success: function(obj) {
                if(obj.successful == true) {
                    window.location.reload();
                } else {
                    alert(obj.message);
                }
            }
        });
    });

    $('a[data-role="export"]').click(function () {
        $.getJSON(ctx + "/graduation/register/getGraduationAddress", {}, function(data) {
            if(data.successful == true) {
                if(data.obj != null) {
                    $(':input[name="receiver"]').val(data.obj.receiver);
                    $(':input[name="mobile"]').val(data.obj.mobile);
                    $(':input[name="address"]').val(data.obj.address);
                    $(':input[name="postcode"]').val(data.obj.postcode);

                    // 加载省市区联动查询
                    $('#province').select_district($('#city'), $("#district"), data.obj.areaCode);
                } else {
                    // 加载省市区联动查询
                    $('#province').select_district($('#city'), $("#district"));
                }
                $('#formModal').modal('show');
            } else {
                alert(data.message);
            }
        });
    });

    $('#save').click(function () {
		var address = $(':input[name="address"]').val();
		if($.trim(address) == '') {
			alert('地址不能为空');
			return;
		}
        $.ajax({
            url: ctx + "/graduation/register/updateGraduationAddress",
            dataType: 'json',
            data: $("form#addressManageForm").serialize(),
            type: 'post',
            success: function(obj) {
                if(obj.successful == true) {
                    $('#formModal').modal('hide');
                } else {
                    alert(obj.message);
                }
            }
        });
    });
	
});