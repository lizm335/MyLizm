/**
 * Created by Administrator on 2016/10/8.
 */
$(function() {

    /*********'图片放大'**********/
    //加载脚本
    $.getScript(css + '/ouchgzee_com/platform/xllms_css/plugins/custom-model/custom-model.js?t=123465',function(){
        $('[data-role="lightbox"]').click(function(event) {
            event.preventDefault();
            var self=this;
            var imgURL=$(self).attr('data-large-img');
            var h=600;

            var t=[
                '<table width="100%" height="'+h+'">',
                '<tr>',
                '<td class="text-center" valign="middle">',
                '<span class="inline-block position-relative bg-white overlay-wrapper">',
                '<div class="overlay"><i class="fa fa-refresh fa-spin" style="color:#fff;"></i></div>',
                '</span>',
                '</td>',
                '</tr>',
                '</table>'
            ].join('');

            var pop=$.mydialog({
                id:'fancybox',
                width:600,
                height:h,
                zIndex:11000,
                content: t,
                //backdrop:false,
                //fade:false,
                showCloseIco:false,
                modalCss:'my-modal',
                onLoaded:function(){
                    var $img=$('<img src="'+imgURL+'">');
                    $img.on('load', function(event) {
                        pop.find('span.overlay-wrapper').html('<img src="'+imgURL+'" style="max-width:100%;max-height:'+h+'px;"><i data-dismiss="modal" class="fa fa-fw fa-times-circle"></i>');
                    });
                }
            });

        });
    });

    function validateTag() {
        $('.panel-heading:eq(0),.panel-heading:eq(1),.panel-heading:eq(3)').each(function(i, item) {
            var pass = true;
            $(this).siblings('.collapse').find('table.table-gray-th td').each(function(i, item) {
                if(pass && $.trim(this.innerHTML) == '') {
                    pass = false;
                    return false;//实现break功能
                }
            });
            if(pass) {
                $(this).find('small.label').removeClass('bg-yellow').addClass('bg-green').text('已完善');
            }
        });

        $('ul.nav-tabs li:eq(0),ul.nav-tabs li:eq(1)').each(function(i, item) {
            var pass = true;
            var delaySize = $('.tab-pane:eq('+i+')').find('small.label.bg-yellow').size();
            pass = (delaySize == 0);
            if(pass) {
                $(this).find('small.label').removeClass('bg-red').addClass('bg-green').text('已完善');
            }
        });
    }
    validateTag();

    var objId;
    // 重置密码
    $("body").confirmation({
        selector: ".btn-reset-pwd",
        html:true,
        placement:'top',
        content:'<div class="f12"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确认重置该学员密码？</div><div class="f12 gray9 margin_t5 margin_b10">重置后密码为888888</div>',
        title:false,
        btnOkClass    : 'btn btn-xs btn-primary',
        btnOkLabel    : '确认',
        btnOkIcon     : '',
        btnCancelClass  : 'btn btn-xs btn-default margin_l10',
        btnCancelLabel  : '取消',
        btnCancelIcon   : '',
        popContentWidth:180,
        onShow:function(event,element){
            if($(element).attr('val') != null) {
                objId = $(element).attr('val');
            }
        },
        onConfirm:function(event,element){
            if(objId != null) {
                var id = objId;
                objId = null;

                $.post(ctx+"/home/class/edu/roll/resetPwd",{studentId:id},function(data){
                    if(data.successful){
                        showSueccess(data);

                    }else{
                        $.alert({
                            title: '失败',
                            icon: 'fa fa-exclamation-circle',
                            confirmButtonClass: 'btn-primary',
                            content: '重置失败！',
                            confirmButton: '确认',
                            confirm:function(){
                                showFail(data);
                            }
                        });
                    }
                },"json");
            }
        },
        onCancel:function(event, element){

        }
    });

})