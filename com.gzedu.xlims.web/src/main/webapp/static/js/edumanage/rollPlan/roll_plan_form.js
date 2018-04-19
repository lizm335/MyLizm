/**
 * Created by Administrator on 2016/9/29.
 */
$(function() {

    // 审核
    $(".btn-audit").click(function(){
        var auditContent = $("form#inputForm :input[name='auditContent']").val();
        if($.trim(auditContent) == '') {
            $("form#inputForm :input[name='auditContent']").focus();
            return;
        }
        var auditState = $(this).attr("val");
        $("form#inputForm :input[name='auditState']").val(auditState);
        if(auditState == 1) {
            if(confirm("确定审核通过？")) {
                $("form#inputForm").submit();
            }
        } else if(auditState == 2) {
            if(confirm("确定审核不通过？")) {
                $("form#inputForm").submit();
            }
        }
    });
    
})