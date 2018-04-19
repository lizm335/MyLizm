<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>入学确认</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body>
    <form id="theform" name="enteringSchoolForm" class="form-horizontal" action="enteringSchool" method="post">
        <input type="hidden" name="studentId" value="${info.studentId}" />
        <input type="hidden" id="action" value="${info.isEnteringSchool}" />
        <div class="box no-border no-shadow margin-bottom-none">
            <div class="box-header with-border">
                <h3 class="box-title">确认学生入学</h3>
            </div>
            <div class="scroll-box">
                <div class="box no-border no-shadow margin-bottom-none">
                    <div class="box-header with-border">
                        <div class="media">
                            <div class="media-left pad-r20 pad-l10">
                                <img id ="headImgId" src="${not empty info.avatar ? info.avatar : ctx.concat('/static/images/headImg04.png')}" class="img-circle" style="width:112px;height:112px;" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'">
                            </div>
                            <div class="media-body">
                                <h3 class="margin_t10">
                                    ${info.xm}
                                    <small class="f14">(<dic:getLabel typeCode="Sex" code="${info.xbm }" />)</small>
                                </h3>
                                <div class="row">
                                    <div class="col-xs-6 col-sm-4 pad-b5">
                                        <b>学号:</b> <span>${info.xh}</span>
                                    </div>
                                    <div class="col-xs-6 col-sm-4 pad-b5">
                                        <b>层次:</b> <span><dic:getLabel typeCode="TrainingLevel" code="${info.pycc }" /></span>
                                    </div>
                                    <div class="col-xs-6 col-sm-4 pad-b5">
                                        <b>手机:</b>
                                        <span>${info.sjh}</span>
                                    </div>
                                    <div class="col-xs-6 col-sm-4 pad-b5">
                                        <b>专业:</b> <span>${info.gjtSpecialty.zymc}</span>
                                    </div>
                                    <div class="col-xs-6 col-sm-4 pad-b5">
                                        <b>邮箱:</b> <span>${info.dzxx}</span>
                                    </div>
                                    <div class="col-xs-6 col-sm-4 pad-b5">
                                        <b>学习中心:</b> <span>${info.gjtStudyCenter.scName}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="box-body">
                        <table class="table-gray-th">
                            <tbody>
                            <tr>
                                <th width="130" class="text-right">
                                    <div class="pad-t5">
                                        <span class="text-red">*</span>
                                        联系确认时间
                                    </div>
                                </th>
                                <td>
                                    <div class="position-relative">
                                        <input type="text" name="enteringDtParam" class="form-control" placeholder="联系确认时间" datatype="*" nullmsg="请填写联系确认时间" errormsg="请填写联系确认时间" data-role="dtime">
                                    </div>
                                </td>
                                <th class="text-right">
                                    <div class="pad-t5">
                                        <span class="text-red">*</span>
                                        联系方式
                                    </div>
                                </th>
                                <td>
                                    <div class="position-relative">
                                        <label class="text-no-bold margin-bottom-none">
                                            <input type="radio" name="contact" value="1" datatype="*" nullmsg="请选择联系方式" errormsg="请选择联系方式"> 电话
                                        </label>
                                        <label class="text-no-bold margin-bottom-none margin_l10">
                                            <input type="radio" name="contact" value="2"> QQ
                                        </label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th class="text-right">
                                    <div class="pad-t5">
                                        <span class="text-red">*</span>
                                        学员QQ号码
                                    </div>
                                </th>
                                <td>
                                    <div class="position-relative">
                                        <input type="text" name="contactNumber" class="form-control" placeholder="学员QQ号码" datatype="*" nullmsg="请填写学员QQ号码" errormsg="请填写学员QQ号码">
                                    </div>
                                </td>
                                <th class="text-right">
                                    <div class="pad-t5">
                                        <span class="text-red">*</span>
                                        是否发短信
                                    </div>
                                </th>
                                <td>
                                    <div class="position-relative">
                                        <label class="text-no-bold margin-bottom-none">
                                            <input type="radio" name="isSendSms" value="0" datatype="*" nullmsg="请选择是否发短信" errormsg="请选择是否发短信"> 否
                                        </label>
                                        <label class="text-no-bold margin-bottom-none margin_l10">
                                            <input type="radio" name="isSendSms" value="1"> 是
                                        </label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th class="text-right">
                                    <div class="pad-t5">
                                        <span class="text-red">*</span>
                                        是否安装APP
                                    </div>
                                </th>
                                <td>
                                    <div class="position-relative">
                                        <label class="text-no-bold margin-bottom-none">
                                            <input type="radio" name="isInstallApp" value="0" datatype="*" nullmsg="请选择是否安装APP" errormsg="请选择是否安装APP"> 否
                                        </label>
                                        <label class="text-no-bold margin-bottom-none margin_l10">
                                            <input type="radio" name="isInstallApp" value="1"> 是
                                        </label>
                                    </div>
                                </td>
                                <th class="text-right">
                                    <div class="pad-t5">
                                        <span class="text-red">*</span>
                                        是否加入班级群
                                    </div>
                                </th>
                                <td>
                                    <div class="position-relative">
                                        <label class="text-no-bold margin-bottom-none">
                                            <input type="radio" name="isJoinClassEe" value="0" datatype="*" nullmsg="请选择是否加入班级群" errormsg="请选择是否加入班级群"> 否
                                        </label>
                                        <label class="text-no-bold margin-bottom-none margin_l10">
                                            <input type="radio" name="isJoinClassEe" value="1"> 是
                                        </label>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table class="table-gray-th" style="display: none;">
                            <tbody>
                            <tr>
                                <th width="130" class="text-right">
                                    联系确认时间
                                </th>
                                <td>
                                    <fmt:formatDate value="${studentEnteringSchool.enteringDt}" type="date" />
                                </td>
                                <th class="text-right">
                                    联系方式
                                </th>
                                <td>
                                    <c:if test="${studentEnteringSchool.contact==1}">电话</c:if>
                                    <c:if test="${studentEnteringSchool.contact==2}">QQ</c:if>
                                </td>
                            </tr>
                            <tr>
                                <th class="text-right">
                                    学员QQ号码
                                </th>
                                <td>
                                    ${studentEnteringSchool.contactNumber}
                                </td>
                                <th class="text-right">
                                    是否发短信
                                </th>
                                <td>
                                    <c:if test="${studentEnteringSchool.isSendSms==1}">是</c:if>
                                    <c:if test="${studentEnteringSchool.isSendSms==0}">否</c:if>
                                </td>
                            </tr>
                            <tr>
                                <th class="text-right">
                                    是否安装APP
                                </th>
                                <td>
                                    <c:if test="${studentEnteringSchool.isInstallApp==1}">是</c:if>
                                    <c:if test="${studentEnteringSchool.isInstallApp==0}">否</c:if>
                                </td>
                                <th class="text-right">
                                    是否加入班级群
                                </th>
                                <td>
                                    <c:if test="${studentEnteringSchool.isJoinClassEe==1}">是</c:if>
                                    <c:if test="${studentEnteringSchool.isJoinClassEe==0}">否</c:if>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="text-right pop-btn-box pad clearfix">
            <button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
            <c:choose>
                <c:when test="${info.isEnteringSchool=='1'}">
                    <button type="button" class="btn btn-success min-width-90px" data-role="close-pop">确定</button>
                </c:when>
                <c:otherwise>
                    <button type="submit" class="btn btn-primary min-width-90px" data-role="sure">确认入学</button>
                </c:otherwise>
            </c:choose>
        </div>
    </form>

    <%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
    <script type="text/javascript" src="${ctx}/static/plugins/custom-model/custom-model.js"></script>
    <script type="text/javascript">
        $(function() {
            var action = $('#action').val();
            if (action == '1') {
                $('table.table-gray-th:first').hide();
                $('table.table-gray-th:last').show();
            }
        });
    </script>
    <script type="text/javascript">
        //关闭 弹窗
        $("[data-role='close-pop']").click(function(event) {
            parent.$.closeDialog(frameElement.api)
        });

        //时间控件
        $('[data-role="dtime"]').datepicker({
            language:'zh-CN',
            format:'yyyy-mm-dd'
        });

        //设置内容主体高度
        $('.scroll-box').height($(frameElement).height()-106);

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
                //showAllError:true,
                tiptype:function(msg,o,cssctl){
                    if(!o.obj.is("form")){
                        var msgBox=o.obj.closest('.position-relative').find('.tooltip');

                        msgBox.css({
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
                    $.post(ctx+"/home/class/studentInfo/enteringSchool",curform.serialize(),function(data){
                        if(data.successful){
                            postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
                            postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');

                            /*关闭弹窗*/
                            setTimeout(function(){
                                parent.$.closeDialog(frameElement.api)
                            },1500)
                        }else{
                            postIngIframe.find('[data-role="tips-text"]').html('数据提交失败');
                            postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-exclamation-circle');

                            /*关闭内部弹窗*/
                            setTimeout(function(){
                                $.closeDialog(postIngIframe);
                            },1500)
                        }
                    },"json");

                    return false;//阻止表单自动提交
                },
                callback:function(data){

                }
            });

        })();
    </script>
</body>
</html>
