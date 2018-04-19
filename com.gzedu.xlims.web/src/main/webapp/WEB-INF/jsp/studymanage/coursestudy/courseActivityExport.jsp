<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/5/22
  Time: 10:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<form id="formId" action="${ctx}/studymanage/downCourseActivityExport" method="post">
<div class="box no-border no-shadow">
    <div class="box-header with-border">
        <h3 class="box-title">导出数据</h3>
    </div>
    <div class="box-body pos-rel overlay-wrapper">
        <div class="text-center" style="margin-top:30px;">
            <i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;"></i>
            <div class="inline-block vertical-mid text-left">
                <div class="text-bold f16">请选择你需要导出的数据</div>
            </div>
        </div>
        <div class=" pad-t30 margin_t10 col-xs-8 col-xs-offset-2 pos-rel">
				<table class="table table-bordered table-striped vertical-mid  table-font">
					<tr>
						<td>时间:</td>
						<td><input type="text" name="startDate" class="form-control" value="${startDate }" data-role="datetime" /></td>
						<td><input type="text" name="endDate" class="form-control" value="${endDate}"  data-role="datetime"/></td>
					</tr>
					<tr>
						<td>类型:</td>
						<td colspan="2">
							<select name="activityType" class="selectpicker show-tick form-control">
								<option value="1">答疑数据</option>
								<option value="2">主题讨论活动数据</option>
							</select>
						</td>
					</tr>
				</table>
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
    <button type="button" class="btn btn-success min-width-90px " data-role="export" >导出</button>
</div>
</form>
<script type="text/javascript">
    $(".box-body").height($(window).height()-126);
    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
        parent.$.closeDialog(frameElement.api)
    });

    //导出
    $('[data-role="export"]').click(function(event) {
        $('#formId').submit();//表单提交

        var $overlay=$(".overlay");
        var $that=$(this);
        $overlay.show();
        $(this).addClass("disabled").prop('disabled', true);
        setTimeout(function(){
            $overlay.hide();
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
            setTimeout(function(){
                $.closeDialog($("[data-id='dialog-1']"));
                $that.removeClass("disabled").prop('disabled', false);
            },5000);

        },2000);
    });

    $('[data-role="datetime"]').datepicker({
		language:'zh-CN',
		format:'yyyy-mm-dd'
	});
    
    
</script>
</body>
</html>

