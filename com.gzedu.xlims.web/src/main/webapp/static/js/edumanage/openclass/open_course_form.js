/**
 * Created by hyf on 2017/05/31.
 */
$(function() {
    // 选择学期
    $("#termId").on('change', function(){
        var termId = $(this).val();
        if($.trim(termId) == '') {
            $("form#inputForm :input[name='termId']").focus();
            return;
        }
        $('#courseTbody').empty();
        $.getJSON(ctx+'/edumanage/openclassCollege/getCourses', {termId:termId}, function (data) {
            $.each(data.obj, function(i,item) {
                var trHtml = '<tr>'
                    + '<td><input type="checkbox" name="ids" data-id="'+item.courseId+'" data-name="check-id" value="'+item.courseId+'"></td>'
                    + '<td>'+item.kch+'</td>'
                    + '<td>'+item.kcmc+'</td>'
                    + '<td style="text-align: left;">教学方式：'+item.wsjxzkName+'<br/>课程性质：'+item.courseNature+'<br/>课程层次：'+item.pyccName+'<br/>课程门类：'+item.category+'<br/>课程学科：'+item.subject
                    + '<br/>适用行业：'+item.syhy
                    + '<br/>适用专业：'+item.syzy
                    + '<br/>课程学时：'+(item.hour!=null?item.hour:'')+'</td>'
                    + '<td>'+(item.isEnabled=='1'?'<span class="text-green">已启用</span>':'<span class="text-red">未启用</span>')+'</td>'
                    + '</tr>';
                $('#courseTbody').append(trHtml);
            });
        });
    }).trigger('change');

    // 确认
    $("#btn-submit").click(function(){
        if($(':input[name="ids"]:checked').length==0) {
            alert('请选择课程');
            return false;
        }
        var ids = '';
        $(':input[name="ids"]:checked').each(function(i, v) {
            ids = ids + v.value + ',';
        });
        $(':input[name="courseIds"]').val(ids.substring(0, ids.length - 1));
        return true;
    });
    
})