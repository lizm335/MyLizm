/**
 * Created by Administrator on 2016/10/10.
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
    // 确认入学
    $("body").confirmation({
        selector: "[data-role='sure-btn-1']",
        html:true,
        placement:'top',
        content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>是否已确认该学员入学？</div>',
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

                $.post(ctx+"/home/class/studentInfo/enteringSchool",{studentId:id},function(data){
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
        },
        onCancel:function(event, element){

        }
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

                $.post(ctx+"/home/class/studentInfo/signupSubmit",{studentId:id},function(data){
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

    /*确认发送*/
    var $theform=$(".theform");
    var htmlTemp='<div class="tooltip top" role="tooltip">'
        +'<div class="tooltip-arrow"></div>'
        +'<div class="tooltip-inner"></div>'
        +'</div>';
    $theform.find(":input[datatype]").each(function(index, el) {
        $(this).after(htmlTemp);
    });

    $.Tipmsg.r='';
    var postIngIframe;
    var postForm=$theform.Validform({
        btnReset:'.btn-cancel-edit',
        showAllError:true,
        tiptype:function(msg,o,cssctl){
            //msg：提示信息;
            //o:{obj:*,type:*,curform:*},
            //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
            //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态,
            //curform为当前form对象;
            //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
            var msgBox=o.obj.closest("div[class*='col-']").children('.tooltip');

            msgBox.children('.tooltip-inner').text(msg);
            if(msgBox.hasClass('left')){
                msgBox.css({
                    width:130,
                    left:-120,
                    top:5
                })
            }
            else{
                msgBox.css({
                    top:-30
                })
            }

            switch(o.type){
                case 3:
                    msgBox.addClass('in');
                    break;
                default:
                    msgBox.removeClass('in');
                    break;
            }
        },
        beforeSubmit:function(curform){
            /*if($theform.find(".Validform_error").length>0){
             return false;
             }*/
            postIngIframe=$.dialog({
                title: false,
                contentHeight:20,
                closeIcon:false,
                content: '<div class="text-center pad-t10">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
                columnClass:'col-xs-4 col-xs-offset-4'
            });


        },
        callback:function(data){
            //这里执行回调操作;
            //如果callback里明确return alse，则表单不会提交，如果return true或没有return，则会提交表单。
            var formBox=postForm.eq($('body').data("form-id")).forms;
            $(".table-row",formBox).each(function(){
                var $that=$(this);
                var $infoText=$that.find(".info-text");
                var $infoEdit=$that.find(".info-edit");
                var dataType=$infoEdit.attr("data-type");
                if($infoEdit.length>0){
                    var dataFormat=$infoEdit.attr("data-format");
                    if(!dataFormat){
                        dataFormat="#0#";
                    }

                    $infoEdit.find(":input").each(function(i,e){
                        var reCat = new RegExp("#"+i+"#","g");
                        dataFormat=dataFormat.replace(reCat,"<span>"+e.value+"<\/span>");
                        e.defaultValue=e.value;
                    });
                    $infoText.html(dataFormat);
                }
            });
            if(data.successful) {
                var rIframe=$.dialog({
                    title: false,
                    contentHeight:20,
                    closeIcon:false,
                    content: '<div class="text-center pad-t10">数据提交成功...<i class="icon fa fa-check-circle"></i></div>',
                    columnClass:'col-xs-4 col-xs-offset-4',
                });
                /*关闭弹窗*/
                setTimeout(function(){
                    postIngIframe.close();
                    rIframe.close();
                    $(".btn-cancel-edit",formBox).click();
                },800)
                window.location.reload();
            } else {
                var rIframe=$.dialog({
                    title: false,
                    contentHeight:20,
                    closeIcon:false,
                    content: '<div class="text-center text-red pad-t10">更新失败<i class="icon fa fa-times-circle"></i></div>',
                    columnClass:'col-xs-4 col-xs-offset-4',
                });
                /*关闭弹窗*/
                setTimeout(function(){
                    postIngIframe.close();
                    rIframe.close();
                    $(".btn-cancel-edit",formBox).click();
                },800)
            }
        }
    });
    //保存修改
    $(".btn-save-edit").click(function(){
        var formId=parseInt($(this).data('form-id'));
        $('body').data("form-id",formId);
        postForm.eq(formId).ajaxPost();
    });

    // 编辑信息
    $(".btn-edit-info").click(function(){
        var $panelBox=$(this).closest('.panel');
        $(this).hide();
        if(!$(".collapse",$panelBox).hasClass('in')){
            $(".collapse",$panelBox).collapse('show');
        }

        $(".content-group",$panelBox).addClass("editing");
        $(".btn-wraper",$panelBox).show();
    });

    //取消编辑
    $(".btn-cancel-edit").click(function(){
        var $panelBox=$(this).closest('.panel');
        $(".btn-edit-info",$panelBox).show();
        $(".content-group",$panelBox).removeClass("editing");
        $(".btn-wraper",$panelBox).hide();
        $(".tooltip",$panelBox).removeClass("in");
    });

    $("body").delegate("[data-form-id='2'],[data-form-id='3']","click",function(){
        objId = $(this).attr('val');
        if(objId != null) {
            var id = objId;
            objId = null;

            $.post(ctx+"/home/class/studentInfo/signupSubmit",{studentId:id},function(data){
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
                        content: '提交失败！',
                        confirmButton: '确认',
                        confirm:function(){
                            showFail(data);
                        }
                    });
                }
            },"json");
        }
    });

})