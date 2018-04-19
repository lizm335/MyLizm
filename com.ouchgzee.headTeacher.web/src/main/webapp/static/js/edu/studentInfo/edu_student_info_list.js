/**
 * Created by Administrator on 2016/10/9.
 */
$(function() {
    // filter tabs
    $(".filter-tabs li").click(function(event) {
        if($(this).hasClass('actived')){
            $(this).removeClass('actived');
        }
        else{
            $(this).addClass('actived');
        }
    });

// filter menu
    $(".custom-dropdown")
        .on('click', "[data-role='sure-btn']", function(event) {
            var $box=$(this).closest('.custom-dropdown');
            $(this).closest('th').removeClass('on');
            $box.find("li").removeClass('actived');
            $box.find(":checkbox").attr("checked",false);
        })
        .on('click', "[data-role='close-btn']", function(event) {
            $(this).closest('th').removeClass('on');
        })
        .on('click', 'li', function(event) {
            if($(this).hasClass('actived')){
                $(this).removeClass('actived')
                    .find(":checkbox").attr("checked",false);
            }
            else{
                $(this).addClass('actived')
                    .find(":checkbox").attr("checked",true);
            }
        });

    $('th[data-role="menu-th"]')
        .on('mouseover', function(event) {
            $(this).addClass('on');
        })
        .on('mouseout', function(event) {
            if(!$(this).children('.custom-dropdown').hasClass('open')){
                $(this).removeClass('on');
            }
        });

    var objId;
    // 重置密码
    $("body").confirmation({
        selector: ".operion-reset",
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

                $.post(ctx+"/home/class/edu/roll/resetPwd",{studentId:id,type:1},function(data){
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


    var objIdNew;
    $(".box-body").confirmation({
        selector: ".operion-resetNew",
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
                objIdNew = $(element).attr('val');
            }
        },
        onConfirm:function(event,element){
            if(objIdNew != null) {
                var id = objIdNew;
                objIdNew = null;

                $.post(ctx+"/home/class/edu/roll/resetPwd",{studentId:id,type:2},function(data){
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


    // 批量入学确认
    $('.multi-entering').off().click(function(){
        var $checkedIds = $("table td input[name='ids']:enabled:checked");
        if ($checkedIds.size() == 0) {
            $.alert({
                title: '',
                content: '<div class="f20 row" style="margin-bottom:-15px;"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span>至少选择一个学员！</div>',
                confirmButton: '确认',
                confirmButtonClass: 'btn-primary'
            });
            return false;
        }
        $.confirm({
            title: '提示',
            content: '您确定批量选中的学员入学？',
            icon: 'fa fa-warning',
            closeIcon: true,
            closeIconClass: 'fa fa-close',
            confirmButton: '确认',
            confirmButtonClass: 'btn-primary',
            cancelButton: '取消',
            confirm: function () {
                var ids = new Array();
                $checkedIds.each(function (i,item) {
                    ids[i] = item.value;
                });
                $.post("enteringSchool",{studentId:ids.join(",")},function(data){
                    if(data.successful){
                        showSueccess(data);

                        setTimeout(function () {
                            window.location.reload();
                        }, 800);
                    }else{
                        $.alert({
                            title: '失败',
                            icon: 'fa fa-exclamation-circle',
                            confirmButtonClass: 'btn-primary',
                            content: '确认失败！',
                            confirmButton: '确认',
                            confirm:function(){
                                showFail(data);
                            }
                        });
                    }
                },"json");
            }
        });
    });

    // 提交资料
    $("html").confirmation({
        selector: "[data-role='sure-btn-2']",
        html:true,
        placement:'top',
        content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>是否提交该学员的资料？</div>',
        title:false,
        btnOkClass    : 'btn btn-xs btn-primary',
        btnOkLabel    : '确认',
        btnOkIcon     : '',
        btnCancelClass  : 'btn btn-xs btn-default margin_l10',
        btnCancelLabel  : '取消',
        btnCancelIcon   : '',
        popContentWidth:190,
        onShow:function(event,element){
            if($(element).attr('val') != null) {
                objId = $(element).attr('val');
            }
        },
        onConfirm:function(event,element){
            if(objId != null) {
                var id = objId;
                objId = null;

                $.post("signupSubmit",{studentId:id},function(data){
                    if(data.successful){
                        showSueccess(data);

                    }else{
                        $.alert({
                            title: '失败',
                            icon: 'fa fa-exclamation-circle',
                            confirmButtonClass: 'btn-primary',
                            content: '提交失败！',
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