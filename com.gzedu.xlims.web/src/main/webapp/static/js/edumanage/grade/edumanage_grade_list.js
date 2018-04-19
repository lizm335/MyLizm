/**
 * Created by Administrator on 2016/12/22.
 */
(function($) {

    //删除
    $("body").confirmation({
        selector: "[data-role='sure-btn-1']",
        html:true,
        placement:'top',
        content:'<div class="margin_b10"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>确定要删除？</div><div class="f12 gray9 margin_b10 text-center">名称：{0}</div>',
        title:false,
        btnOkClass    : 'btn btn-xs btn-primary',
        btnOkLabel    : '确认',
        btnOkIcon     : '',
        btnCancelClass  : 'btn btn-xs btn-default margin_l10',
        btnCancelLabel  : '取消',
        btnCancelIcon   : '',
        popContentWidth:220,
        onShow:function(event,element){
            var delName = $(element).attr('delName');
            this.content = this.content.replace(/\{0\}/g, delName);
        },
        onConfirm:function(event,element){
            $.post("delete", {"ids":$(element).attr('val')}, function (data) {
                if(data.successful){
                    showMessage(data);
                    window.location.reload();
                }else{
                    showMessage(data);
                }
            }, 'json');
        },
        onCancel:function(event, element){

        }
    });

})(jQuery);