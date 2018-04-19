<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/8/17
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>我的计划</title>
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">我的计划</a></li>
        <li class="active">新建计划</li>
    </ol>
</section>
<section class="content">
    <form id="theform" method="post" action="${ctx}/myPlan/createPlan">
        <div class="box no-margin school-set-box">
            <div class="box-body">
                <div class="form-horizontal reset-form-horizontal margin_t10">
                    <div class="form-group">
                        <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>标题</label>
                        <div class="col-md-8 col-sm-10">
                            <div class="position-relative">
                                <input type="text" class="form-control" placeholder="标题" datatype="*" nullmsg="请填写标题" errormsg="请填写标题" name="PLAN_TITLE" value="${param.PLAN_TITLE}">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>级别</label>
                        <div class="col-md-8 col-sm-10">
                            <div class="position-relative">
                                <select class="form-control" datatype="*" nullmsg="请选择级别" errormsg="请选择级别" name="PLAN_LEVEL">
                                    <option value="">--请选择级别型--</option>
                                    <option value="1" <c:if test="${param.PLAN_TITLE eq '1'}">selected</c:if>>一般</option>
                                    <option value="2" <c:if test="${param.PLAN_TITLE eq '1'}">selected</c:if>>优先</option>
                                    <option value="3" <c:if test="${param.PLAN_TITLE eq '1'}">selected</c:if>>紧急</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>内容</label>
                        <div class="col-md-8 col-sm-10">
                            <div class="position-relative">
                                <textarea id="PLAN_CONTENT" name="PLAN_CONTENT" rows="10" class="full-width position-absolute show" datatype="*" nullmsg="请填写内容" errormsg="请填写内容">${param.PLAN_CONTENT}</textarea>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>计划完成时间</label>
                        <div class="col-md-8 col-sm-10">
                            <div class="position-relative">
                                <input type="text" class="form-control" placeholder="完成时间" datatype="*" nullmsg="请填写时间" errormsg="时间格式不对" data-role="datetime" name="PLAN_FINISH_TIME" id="PLAN_FINISH_TIME" value="${param.PLAN_FINISH_TIME}">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 col-sm-2 control-label">附件</label>
                        <div class="col-md-8 col-sm-10">
                            <div class="table-block control-group">
                                <div class="table-cell-block">
                                    <input class="form-control" name="ATTACHMENT_NAME" id="fileName" value="${param.ATTACHMENT_NAME}">
                                    <input class="form-control" type="hidden" name="ATTACHMENT_URI" id="filePath" value="${param.ATTACHMENT_URI}">
                                </div>
                                <div class="table-cell-block pad-l10">
                                    <button type="button" class="btn btn-block btn-default" onclick="uploadFile('fileName','filePath',null,null,callbackFunciton)">添加附件</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 col-sm-2 control-label"></label>
                        <div class="col-md-8 col-sm-10">
                            <%--data-role="submit"--%>
                            <button type="submit" class="btn btn-success min-width-90px margin_r15" data-role="submit">发布</button>
                            <button type="button" class="btn btn-default min-width-90px" data-role="back-off">取消</button>
                        </div>
                    </div>
                </div>
            </div><!-- /.box-body -->
        </div>
    </form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<jsp:include page="/eefileupload/upload.jsp"/>
<script>
    //上传返回
    function callbackFunciton() {

    }
    
    
    /*日期控件*/
    $('[data-role="datetime"]').datepicker({
        language:'zh-CN',
        format:'yyyy-mm-dd'
    });

    //编辑器
    CKEDITOR.replace('PLAN_CONTENT',{
        on:{
            change:function(evt){
                evt.sender.element.$.value=evt.editor.getData();
            }
        }
    });

    /*失败状态
    $.resultDialog(
        {
            type:2,
            msg:'发布失败！',
            timer:1500,
            width:150,
            height:50
        }
    );
    */
    /*成功状态
    $.resultDialog(
        {
            type:1,
            msg:'发布成功！',
            timer:1500,
            width:150,
            height:50
        }
    );
    */
    /*服务器繁忙状态
    $.resultDialog(
        {
            type:0,
            msg:'服务器繁忙，请稍后再试！',
            timer:1500,
            width:265,
            height:50
        }
    );
    */

    //确认
    ;(function(){
        var $theform=$("#theform");

        var htmlTemp='<div class="tooltip top" role="tooltip">'
            +'<div class="tooltip-arrow"></div>'
            +'<div class="tooltip-inner"></div>'
            +'</div>';
        $theform.find(".position-relative").each(function(index, el) {
            $(this).append(htmlTemp);
        });

        $.Tipmsg.r='';
        var postIngIframe;
        var postForm=$theform.Validform({
            showAllError:true,
            ignoreHidden:true,//是否忽略验证不可以见的表单元素
            tiptype:function(msg,o,cssctl){
                //msg：提示信息;
                //o:{obj:*,type:*,curform:*},
                //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
                //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态,
                //curform为当前form对象;
                    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
                    if(!o.obj.is("form")){
                        var msgBox=o.obj.closest('.position-relative').find('.tooltip');

                    msgBox.css({
                        'z-index':2,
                        bottom:"100%",
                        'margin-bottom':-5
                    }).children('.tooltip-inner').text(msg);

                    switch(o.type){
                        case 3:
                            msgBox.addClass('in');
                            break;
                        default:
                            msgBox.removeClass('in');
                            break;
                    }
                }
            },
            beforeSubmit:function(curform){

                postIngIframe=$.formOperTipsDialog({
                    text:'数据提交中...',
                    iconClass:'fa-refresh fa-spin'
                });

                setTimeout(function(){//此句模拟交互，程序时请去掉
                    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
                    postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');

                    /*关闭弹窗*/
                    setTimeout(function(){
                        $.closeDialog(postIngIframe);
                    },2000)
                },2000);//此句模拟交互，程序时请去掉

//                return false;//阻止表单自动提交
            },
            callback:function(data){

            }
        });

    })();
</script>
</body>
</html>
