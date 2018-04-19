/**
 * Created by Administrator on 2017/10/19.
 */

$(function() {

    //点击上传图片
    $('[data-role="upload-img"]').off('click').click(function(event) {
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
                content: 'url:' + ctx + '/static/upload-picture-control/pop-upload-picture.html'
            });
        }
    });
    
    // 下一步
    $('.btn-next').click(function(){
        $('ul.nav-tabs li:last a').trigger('click');
        $('html,body').animate({scrollTop:0}, 300); // 动态按需加载返回顶部，结合简单jQuery动画实现更好体验
    });

    // 保存
    $(".btn-save").click(function(){
        $("form#inputForm :input[name='nativeplace']").val($("#nativeplace .province").val() + " " + $("#nativeplace .city").val());
        $("form#inputForm :input[name='hkszd']").val($("#hkszd .province").val() + " " + $("#hkszd .city").val());
        $("form#inputForm").submit();
    });

})