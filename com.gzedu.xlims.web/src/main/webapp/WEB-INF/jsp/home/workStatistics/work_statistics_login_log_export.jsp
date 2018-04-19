<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/5/22
  Time: 10:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>导出页面</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>

<input type="hidden" name="mobileNumber" value="${mobileNumber}" />
<input type="hidden" name="formAction" value="${formAction}" />
<div class="box no-border no-shadow">
    <div class="box-header with-border">
        <h3 class="box-title">导出登录情况明细表</h3>
    </div>
    <div class="box-body pos-rel overlay-wrapper">
        <div style="padding-top:80px;">
            <div class="text-center">
                <i class="fa fa-exclamation-circle vertical-mid margin_r10" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>
				<span class="inlineblock" style="font-size:18px;">
					请选择导出数据的时间段
				</span>
            </div>
            <div class="form-group" style="margin-top:50px;">
                <div class="center-block" style="width:65%;">
                    <div class="input-group input-daterange" data-role="date-group">
                        <span class="input-group-addon nobg no-border">起止时间 </span>
                        <input type="text" name="search_GTE_createdDt" class="form-control" data-role="date-start">
                        <span class="input-group-addon nobg">－</span>
                        <input type="text" name="search_LTE_createdDt" class="form-control flat" data-role="date-end">
                    </div>
                </div>
            </div>
        </div>

        <div class="overlay" style="display:none;">
            <div class="uploading-txt text-center">
                <i class="fa fa-refresh fa-spin vertical-mid"></i>
                <span class="inlineblock left10">数据正在导出中，请稍后...</span>
            </div>
        </div>
    </div>
</div>

<div class="text-right pop-btn-box pad">
    <button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
    <button type="button" class="btn btn-success min-width-90px" data-role="export">导出</button>
</div>
<script type="text/javascript">
    $(".box-body").height($(window).height()-126);
    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
        parent.$.closeDialog(frameElement.api)
    });

    //导出
    $('[data-role="export"]').click(function(event) {
        var search_GTE_createdDt = $(':input[name="search_GTE_createdDt"]').val();
        var search_LTE_createdDt = $(':input[name="search_LTE_createdDt"]').val();
        if(search_GTE_createdDt == '' || search_LTE_createdDt == '') {
            alert('请选择起止时间');
            return;
        }
        var url = ctx + $(':input[name="formAction"]').val();
        var form = $("<form>");//定义form表单
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");
        form.attr("action",url);
        form.append('<input type="hidden" name="search_GTE_createdDt" value="'+search_GTE_createdDt+'"/>');
        form.append('<input type="hidden" name="search_LTE_createdDt" value="'+search_LTE_createdDt+'"/>');
        $("body").append(form);
        form.submit();//表单提交

        var $overlay=$(".overlay");
        var $that=$(this);
        $overlay.show();
        $that.addClass("disabled").prop('disabled', true);
        setTimeout(function(){
            $overlay.hide();
            /*失败状态
             $.mydialog({
             id:'dialog-1',
             width:150,
             height:50,
             backdrop:false,
             fade:true,
             transparent:true,
             showCloseIco:false,
             content: '<div class="alert alert-danger alert-dismissable no-margin pad15"><h4 class="no-margin text-no-bold"><i class="icon fa fa-ban"></i> <span class="f16">导出失败！</span></h4></div>'
             });
             */
            /*成功状态*/
            $.mydialog({
                id:'dialog-1',
                width:350,
                height:50,
                backdrop:false,
                fade:true,
                transparent:true,
                showCloseIco:false,
                content: '<div class="alert alert-success alert-dismissable no-margin pad15"><h4 class="no-margin text-no-bold"><i class="icon fa fa-check"></i> <span class="f16">导出成功，请耐心等待下载结果返回...</span></h4></div>'
            });

            /*服务器繁忙状态
             $.mydialog({
             id:'dialog-1',
             width:265,
             height:50,
             backdrop:false,
             fade:true,
             transparent:true,
             showCloseIco:false,
             content: '<div class="alert alert-warning alert-dismissable no-margin pad15"><h4 class="no-margin text-no-bold"><i class="icon fa fa-warning"></i> <span class="f16">服务器繁忙，请稍后再试！</span></h4></div>'
             });
             */
            setTimeout(function(){
                $.closeDialog($("[data-id='dialog-1']"));
                $that.removeClass("disabled").prop('disabled', false);
            },5000);

        },2000);
    });

    /*日期控件*/
    $('[data-role="date-group"]').each(function(i,e){
        var startDate=$('[data-role="date-start"]',e);
        var endDate=$('[data-role="date-end"]',e);
        //开始时间
        startDate.datepicker({
            language:'zh-CN',
            format:'yyyy-mm-dd'
        }).on('changeDate', function(e) {
            //var add=increaseOnedate(e.target.value);
            endDate.datepicker('setStartDate',e.target.value);
        });
        //结束时间
        endDate.datepicker({
            language:'zh-CN',
            format:'yyyy-mm-dd'
        }).on('changeDate', function(e) {
            //var d=decreaseOnedate(e.target.value);
            startDate.datepicker('setEndDate',e.target.value);
        }).on('focus',function(){
            if(this.value==""&&startDate.val()==""){
                startDate.focus();
                endDate.datepicker('hide');
            }
        });
    });
</script>
</body>
</html>

