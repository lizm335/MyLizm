<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/8/17
  Time: 16:38
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
        <li class="active">计划详情</li>
    </ol>
</section>
<section class="content">
    <div class="box">
        <div class="box-header with-border pad20">
            <div class="row">
                <div class="col-sm-4">
                    <div class="text-no-bold f20 margin_b10">${result.PLAN_TITLE}</div>
                    <ul class="list-unstyled gray6">
                        <li>计划创建人：${result.REAL_NAME}（${result.ROLE_NAME}）</li>
                        <li>发布时间：${result.CREATED_DT} </li>
                        <li>计划完成时间：${result.PLAN_FINISH_TIME} </li>
                    </ul>
                </div>
                <div class="col-sm-8">
                    <table class="task-static text-center vertical-middle pull-right" height="100">
                        <tbody>
                        <tr>
                            <td width="140">
                                <c:choose>
                                    <c:when test="${not empty result.START_DATE}">
                                        <div class="f20" style="line-height:1">
                                            ${result.START_DATE}<br>
                                            <span class="gray9">${result.START_TIME}</span>
                                        </div>
                                        <div class="margin_t5">开始时间</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="f20" style="line-height:1">
                                            --
                                        </div>
                                        <div class="margin_t5">开始时间</div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td width="140">
                                <c:choose>
                                    <c:when test="${not empty result.END_DATE}">
                                        <div class="f20">
                                            <div style="line-height:1">
                                                ${result.END_DATE}<br>
                                                <span class="gray9">${result.END_TIME}</span>
                                            </div>
                                        </div>
                                        <div>结束时间</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="f20">--</div>
                                        <div>结束时间</div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td width="110">
                                <c:choose>
                                    <c:when test="${not empty result.USE_TIME}">
                                        <div class="f20">${result.USE_TIME}</div>
                                        <div>用时</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="f20">--</div>
                                        <div>用时</div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td width="110">
                                <c:choose>
                                    <c:when test="${result.PLAN_LEVEL eq '1'}"><div class="f24">一般</div></c:when>
                                    <c:when test="${result.PLAN_LEVEL eq '2'}"><div class="text-orange f24">优先</div></c:when>
                                    <c:when test="${result.PLAN_LEVEL eq '3'}"><div class="text-orange f24">紧急</div></c:when>
                                    <c:otherwise><div class="f24">--</div></c:otherwise>
                                </c:choose>
                                <div>级别</div>
                            </td>
                            <td width="110">
                                <c:choose>
                                    <c:when test="${result.PLAN_STATUS eq '0'}"><div class="f24">未开始</div></c:when>
                                    <c:when test="${result.PLAN_STATUS eq '1'}"><div class="text-orange f24">进行中</div></c:when>
                                    <c:when test="${result.PLAN_STATUS eq '2'}"><div class="text-green f24">已完成</div></c:when>
                                    <c:otherwise><div class="f24">--</div></c:otherwise>
                                </c:choose>
                                <div>状态</div>
                            </td>
                            <td width="140">
                                <button class="btn btn-default min-width-90px margin5" type="button" data-role="remove-plan" data-value="${result.PLAN_ID}">删除</button>
                                <c:choose>
                                    <c:when test="${result.PLAN_STATUS eq '0'}">
                                        <button class="btn btn-default min-width-90px margin5" type="button" data-role="start-confirm" data-value="${result.PLAN_ID}">开始计划</button>
                                    </c:when>
                                    <c:when test="${result.PLAN_STATUS eq '1'}">
                                        <button class="btn btn-default min-width-90px margin5" type="button" data-role="finish-confirm" data-value="${result.PLAN_ID}">我已完成</button>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="box-body pad20">
            <div>
               ${result.PLAN_CONTENT}
            </div>
            <div class="margin_t15">
                <c:if test="${not empty result.ATTACHMENT_URI}">
                    <a href="${result.ATTACHMENT_URI}">
                        <i class="fa fa-fw fa-download"></i>
                        ${result.ATTACHMENT_NAME}
                    </a>
                </c:if>
            </div>
            <div class="panel panel-default margin_t20">
                <form method="post" action="${ctx}/myPlan/addPlanRemark/${result.PLAN_ID}">
                    <div class="panel-heading">
                        备注（${fn:length(result.REMARK_LIST)}）
                    </div>
                    <div class="panel-body">
                        <c:choose>
                            <c:when test="${not empty result.REMARK_LIST}">
                                <ul class="list-group">
                                    <c:forEach items="${result.REMARK_LIST}" var="item">
                                        <li class="list-group-item">
                                            <dl class="no-margin">
                                                <dt>
                                                    我
                                                    <small class="text-no-bold">${item.CREATED_DT} 记录</small>
                                                    <div class="pull-right">
                                                        <a href="#" class="operion-item operion-view" data-value="${item.REMARK_ID}" data-toggle="tooltip" title="删除" data-role="sure-btn-1"><i class="fa fa-fw fa-trash-o text-red"></i></a>
                                                        <a href="#" class="operion-item operion-view" data-value="${item.REMARK_ID}" data-remark="${item.REMARK_CONTENT}" data-toggle="tooltip" title="编辑" data-role="edit-remark-content"><i class="fa fa-fw fa-edit"></i></a>
                                                    </div>
                                                </dt>
                                                <dd class="margin_t5" id="content_${item.REMARK_ID}">
                                                    ${item.REMARK_CONTENT}
                                                </dd>
                                            </dl>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:otherwise></c:otherwise>
                        </c:choose>
                        <div>
                            <textarea class="form-control" rows="5" placeholder="请输入备注内容" name="REMARK_CONTENT"></textarea>
                            <div class="margin_t10 text-right">
                                <button type="button" class="btn btn-default min-width-90px" data-role="back-off">取消</button>
                                <button class="btn btn-primary min-width-90px margin_l10">发表</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">

    //修改备注的文本域模版
    function textareaTemplate(id,content) {
        var template =  '<div>'
                            +'<textarea class="form-control" rows="5" placeholder="请输入备注内容" id="remark_content_'+id+'">'+content+'</textarea>'
                            +   '<div class="margin_t10 text-right">'
                            +    '<button type="button" class="btn btn-default min-width-90px" id="cancel_'+id+'" onclick="cancelEdite(\''+id+'\')">取消</button>'
                            +    '<button type="button" class="btn btn-primary min-width-90px margin_l10" id="submit_'+id+'" onclick="publishRemark(\''+id+'\')">发表</button>'
                            +   '</div>'
                       +'</div>';
        return template;
    }


    //初始化 展开的数据
    $('[data-cache]').each(function(index, el) {
        var cache=$(this).data('cache');
        var arr;
        if(cache.length>1){
            arr=cache.split(',');
            $(this).html(arr.join('、'))
            if(arr.length>3){
                $(this).html(arr.slice(0, 3).join('、')+'<a href="#" data-role="expand" class="text-underline f12">展开&gt;&gt;</a>');
            }
        }
    });

    //展开
    $('body').on('click', '[data-role="expand"]', function(event) {
        event.preventDefault();
        var cache=$(this).parent().data('cache');
        var arr=cache.split(',');
        if(arr.length>3){
            $(this).parent().html(arr.join('、')+'<a href="#" data-role="close" class="text-underline f12">收起&lt;&lt;</a>');
        }
    })
    //收起
        .on('click', '[data-role="close"]', function(event) {
            event.preventDefault();
            var cache=$(this).parent().data('cache');
            var arr=cache.split(',');
            if(arr.length>3){
                $(this).parent().html(arr.slice(0, 3).join('、')+'<a href="#" data-role="expand" class="text-underline f12">展开&gt;&gt;</a>');
            }
        });

    //开始计划
    $('[data-role="start-confirm"]').on('click', function(event) {
        var plan_id = $(this).data('value');
        $.alertDialog({
            id:'start-confirm',
            width:400,
            height:280,
            zIndex:11000,
            ok:function(){//“确定”按钮的回调方法
                //这里 this 指向弹窗对象
                $.ajax({
                    type:"post",
                    dataType:"json",
                    url:"${ctx}/myPlan/startPlan/"+plan_id,
                    success:function (data) {
                        if(data.successful==true){
                            window.location.reload();
                        }else {
                            alert("开始计划失败，请重试！");
                        }
                    }
                });
                $.closeDialog(this);
            },
            content:[
                '<div class="text-center">',
                '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
                '<span class="f16 inline-block vertical-mid text-left">',
                '确定开始该计划项？',
                '</span>',
                '</div>'].join('')
        })
    });

    //我已完成
    $('[data-role="finish-confirm"]').on('click', function(event) {
        var plan_id = $(this).data('value');
        $.alertDialog({
            id:'finish-confirm',
            width:400,
            height:280,
            zIndex:11000,
            ok:function(){//“确定”按钮的回调方法
                //这里 this 指向弹窗对象
                $.ajax({
                    type:"post",
                    dataType:"json",
                    url:"${ctx}/myPlan/finishPlan/"+plan_id,
                    success:function (data) {
                        if(data.successful==true){
                            window.location.reload();
                        }else {
                            alert("完成计划失败，请重试！");
                        }
                    }
                });
                $.closeDialog(this);
            },
            content:[
                '<div class="text-center">',
                '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
                '<span class="f16 inline-block vertical-mid text-left">',
                '确定已完成该计划项？',
                '</span>',
                '</div>'].join('')
        })
    });

    //删除该计划
    $('[data-role="remove-plan"]').on('click',function(event) {
        var plan_id = $(this).data('value');
        $.alertDialog({
            id:'remove-plan',
            width:400,
            height:280,
            zIndex:11000,
            ok:function(){//“确定”按钮的回调方法
                //这里 this 指向弹窗对象
                $.ajax({
                    type:"post",
                    dataType:"json",
                    url:"${ctx}/myPlan/deletePlan/"+plan_id,
                    success:function (data) {
                        if(data.successful==true){
                            window.location.href='${ctx}/myPlan/planList';
                        }else {
                            alert("删除计划失败，请重试！");
                        }
                    }
                });
                $.closeDialog(this);
            },
            content:[
                '<div class="text-center">',
                '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
                '<span class="f16 inline-block vertical-mid text-left">',
                '确定删除该计划项？',
                '</span>',
                '</div>'].join('')
        })
    });
    
    $('[data-role="edit-remark-content"]').on('click',function (event) {
        var remark_id = $(this).data('value');
        var remark_content = $(this).data('remark');
        $("#content_"+remark_id).html(textareaTemplate(remark_id,remark_content));
    });

    //取消修改
    function cancelEdite(id) {
        $("#content_"+id).html($($("#content_"+id).html()).find("textarea").val());
    }

    //修改发表备注
    function publishRemark(id) {
        var REMARK_ID = id;
        var REMARK_CONTENT = $("#remark_content_"+id).val();
        $.ajax({
            type:"post",
            dataType:"json",
            data:{REMARK_ID:REMARK_ID,REMARK_CONTENT:REMARK_CONTENT},
            url:"${ctx}/myPlan/editPlanRemark",
            success:function (data) {
                if(data.successful==true){
                    window.location.reload();
                }else {
                    alert("修改备注失败，请重试！");
                }
            }
        });
    }

    //删除备注
    $(".panel-body").confirmation({
        selector: '[data-role="sure-btn-1"]',
        html:true,
        placement:'top',
        content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定删除该备注？</div>',
        title:false,
        btnOkClass    : 'btn btn-xs btn-primary',
        btnOkLabel    : '确认',
        btnOkIcon     : '',
        btnCancelClass  : 'btn btn-xs btn-default margin_l10',
        btnCancelLabel  : '取消',
        btnCancelIcon   : '',
        popContentWidth:190,
        onShow:function(event,element){
        },
        onConfirm:function(event,element){
            var remark_id = element[0].getAttribute("data-value");
            $.ajax({
                type:"post",
                url:"${ctx}/myPlan/deletePlanRemark/"+remark_id,
                dataType:"json",
                success:function (data) {
                    if(data.successful==true){
                        window.location.reload();
                    }else {
                        alert("删除备注失败，请重试！");
                    }
                }
            });
        },
        onCancel:function(event, element){
        }
    });

</script>
</body>
</html>
