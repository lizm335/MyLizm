$(function() {

    $('#examBatchCode').change(function(){
        var examBatchCode = $('#examBatchCode').val();
        if(examBatchCode!=''){
            $.get(ctx+"/exam/new/plan/queryExamPlansByExamBatchCode",{examBatchCode:examBatchCode},function(data,status){
                $('#examPlanId').empty();
                $("#examPlanId").append("<option value=''>请选择</option>");
                $.each(data, function (i) {
                    if (examPlanId == data[i].id) {
                        $("#examPlanId").append("<option selected='selected' value='"+data[i].id+"'>"+data[i].name+"</option>");
                    } else {
                        $("#examPlanId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
                    }
                });
                $('#examPlanId').selectpicker('refresh');
            },"json");
        } else {
            $('#examPlanId').empty();
            $("#examPlanId").append("<option value=''>请选择</option>");
            $('#examPlanId').selectpicker('refresh');
        }
    });
    $('#examBatchCode').change();

    //附件预览
    $('body').on('click', '[data-role="addon-preview"]', function(event) {
        event.preventDefault();
        var _this=this;
        var $pop=$.alertDialog({
            id:'addon-preview',
            title:'在线预览',
            width:$(window).width(),
            height:$(window).height(),
            zIndex:11000,
            content: '',
            cancelLabel:'关闭',
            cancelCss:'btn btn-default min-width-90px margin_r15',
            okLabel:'下载文档',
            okCss:'btn btn-primary min-width-90px'
        });

        //载入附件内容
        $('.box-body',$pop).addClass('overlay-wrapper position-relative').html([
            '<iframe src="'+$(_this).attr('href')+'" id="Iframe-addon-preview" name="Iframe-addon-preview" frameborder="0" scrolling="auto" style="width:100%;height:100%;position:absolute;left:0;top:0;"></iframe>',
            '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>'
        ].join(''));

        $('#Iframe-addon-preview').on('load',function(){
            $('.overlay',$pop).hide();
        });
    });

    $('.choiceState').click(function () {
        $(':input[name="search_EQ_correctState"]').val($(this).attr('val'));
        $('form#listForm').submit();
    });

    $('a[action="audit"]').click(function () {
		$(':input[name="id"]').val($(this).attr('val'));
		$(':input[name="score"]').val('');

        $('#formModal').find('span[name="avatar"]').html($(this).parents('tr').find('span[name="avatar"]').html());
        $('#formModal').find('span[name="xm"]').text($(this).parents('tr').find('span[name="xm"]').text());
        $('#formModal').find('span[name="xh"]').text($(this).parents('tr').find('span[name="xh"]').text());
        $('#formModal').find('span[name="sjh"]').text($(this).parents('tr').find('span[name="sjh"]').text());
        $('#formModal').find('span[name="pycc"]').text($(this).parents('tr').find('span[name="pycc"]').text());
        $('#formModal').find('span[name="yearName"]').text($(this).parents('tr').find('span[name="yearName"]').text());
        $('#formModal').find('span[name="gradeName"]').text($(this).parents('tr').find('span[name="gradeName"]').text());
        $('#formModal').find('span[name="zymc"]').text($(this).parents('tr').find('span[name="zymc"]').text());
        $('#formModal').find('span[name="examBatchName"]').text($(this).parents('tr').find('span[name="xm"]').text());
        $('#formModal').find('span[name="examBatchCode"]').text($(this).parents('tr').find('span[name="examBatchCode"]').text());
        $('#formModal').find('span[name="examPlanName"]').text($(this).parents('tr').find('span[name="examPlanName"]').text());
        $('#formModal').find('span[name="examNo"]').text($(this).parents('tr').find('span[name="examNo"]').text());
        $('#formModal').find('span[name="type"]').text($(this).parents('tr').find('span[name="type"]').text());
        $('#formModal').find('span[name="papersFile"]').html($(this).parents('tr').find('span[name="papersFile"]').html());
        $('#formModal').modal('show');
    });

    $('#save').click(function () {
		var score = $(':input[name="score"]').val();
		if(score == '' || isNaN(score) || score < 0 || score > 100) {
			alert('成绩应该为0-100分之间');
			return;
		}
        $.ajax({
            url: ctx + "/exam/new/correctPapers/approval",
            dataType: 'json',
            data: $("form#correctPapersForm").serialize(),
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
	
});