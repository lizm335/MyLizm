/**
 * Created by Administrator on 2016/10/8.
 */
$(function() {
    // 标签页
    $('.nav-tabs-lg a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
        if($(this).attr("href")=='#tab_top_3'){
            var $tabItem=$(".tab-pane:last");
            if($tabItem.find('[data-role="chart"]').children('div').width()==0){
                $(window).resize();
            }
        }
    });

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

                $.post("resetPwd",{studentId:id},function(data){
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