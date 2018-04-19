/**
 * Created by Administrator on 2016/9/29.
 */
$(function() {

    /*********'图片放大'**********/
    //加载脚本
    $.getScript(css + '/ouchgzee_com/platform/xllms_css/plugins/custom-model/custom-model.js?t=123465',function(){
        $('[data-role="fancybox"]').click(function(event) {
            event.preventDefault();
            var self=this;
            var imgURL=$(self).attr('href');
            if(imgURL != null && imgURL != '#') {
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
            }
        });
    });

    //点击上传图片
    $('[data-role="upload-img"]').click(function(event) {
        var data=$(this).data('object');

        $('.cert-box').removeClass('actived');
        $(this).closest('.cert-box').addClass('actived');

        if( $.type(data)=="object" ){
            $.mydialog({
                id:'pop2',
                width:980,
                zIndex:1000,
                height:500,
                urlData:data,
                content: 'url:' + ctx + '/static/upload-picture-control/pop-upload-picture-cut.html'
            });
        }
    });

    function validateTag() {
        $('.panel-heading:eq(0),.panel-heading:eq(1),.panel-heading:eq(3)').each(function(i, item) {
            var pass = true;
            $(this).siblings('.collapse').find('table.table-gray-th td[required]').each(function(i, item) {
                if($(this).find(':input').val() == undefined) {
                    if(pass && $.trim(this.innerHTML) == '') {
                        pass = false;
                        return false;//实现break功能
                    }
                } else {
                    if(pass && $.trim($(this).find(':input').val()) == '') {
                        pass = false;
                        return false;//实现break功能
                    }
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

    // 审核
    $(".btn-audit:not([disabled])").click(function(){
        $(this).attr("disabled", "disabled");
        var auditContent = $("form#inputForm :input[name='auditContent']").val();
        if($.trim(auditContent) == '') {
            $("form#inputForm :input[name='auditContent']").focus();
            $(this).removeAttr("disabled");
            return;
        }
        var auditState = $(this).attr("val");
        $("form#inputForm :input[name='auditState']").val(auditState);
        if(auditState == 1) {
            if(confirm("确定审核通过？")) {
                $("form#inputForm").submit();
            } else {
                $(this).removeAttr("disabled");
            }
        } else if(auditState == 2) {
            if(confirm("确定审核不通过？")) {
                $("form#inputForm").submit();
            } else {
                $(this).removeAttr("disabled");
            }
        }
    });
    
})