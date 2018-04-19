<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/8/17
  Time: 15:36
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
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">我的计划</li>
    </ol>
</section>
<section class="content">
    <form id="listForm" class="form-horizontal" action="${ctx}/myPlan/planList">
        <div class="box">
            <div class="box-body">
                    <div class="row pad-t15">
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">标题</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control" placeholder="标题" name="PLAN_TITLE" value="${param.PLAN_TITLE}" id="PLAN_TITLE">
                                    <input type="hidden" name="PLAN_STATUS" value="${param.PLAN_STATUS}" id="PLAN_STATUS">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">日期</label>
                                <div class="col-sm-9">
                                    <div class="input-group input-daterange" data-role="date-group">
                                        <input type="text" class="form-control" data-role="date-start" name="START_DATE" value="${param.START_DATE}" id="START_DATE">
                                        <span class="input-group-addon nobg">－</span>
                                        <input type="text" class="form-control" data-role="date-end" name="END_DATE" value="${param.END_DATE}" id="END_DATE">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
            </div>
            <div class="box-footer">
                <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
                <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary" id="submit_buttion">搜索</button></div>
            </div>
        </div>
        <div class="box box-border margin-bottom-none">
            <div class="box-header with-border">
                <h3 class="box-title pad-t5">计划列表</h3>
                <div class="pull-right no-margin">
                	<shiro:hasPermission name="/myPlan/planList$create">
                    	<a role="button" href="${ctx}/myPlan/createPlan" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新建计划</a>
                	</shiro:hasPermission>
                </div>
            </div>
            <div class="box-body">
                <div class="filter-tabs clearfix">
                    <ul class="list-unstyled">
                        <li <c:if test="${param.PLAN_STATUS eq ''}">class="actived"</c:if> onclick="submitform('')">全部(<span id="plan_status_">0</span>)</li>
                        <li <c:if test="${param.PLAN_STATUS eq '0'}">class="actived"</c:if> onclick="submitform('0')">未开始(<span id="plan_status_0">0</span>)</li>
                        <li <c:if test="${param.PLAN_STATUS eq '1'}">class="actived"</c:if> onclick="submitform('1')">进行中(<span id="plan_status_1">0</span>)</li>
                        <li <c:if test="${param.PLAN_STATUS eq '2'}">class="actived"</c:if> onclick="submitform('2')">已完成(<span id="plan_status_2">0</span>)</li>
                    </ul>
                </div>
                <div class="table-responsive">
                    <table class="table table-bordered table-striped vertical-mid text-center table-font">
                        <thead>
                        <tr>
                            <th>级别</th>
                            <th>标题</th>
                            <th width="12%">开始时间</th>
                            <th width="12%">结束时间</th>
                            <th width="12%">计划完成时间</th>
                            <th width="6%">用时</th>
                            <th width="8%">状态</th>
                            <th width="11%">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty pageInfo.content}">
                                <c:forEach items="${pageInfo.content}" var="entity">
                                    <tr>
                                        <td>
                                            <c:choose>
                                                <c:when test="${entity.PLAN_LEVEL eq '1'}"><small class="label label-default text-no-bold">一般</small></c:when>
                                                <c:when test="${entity.PLAN_LEVEL eq '2'}"><small class="label label-warning text-no-bold">优先</small></c:when>
                                                <c:when test="${entity.PLAN_LEVEL eq '3'}"><small class="label label-danger text-no-bold">紧急</small></c:when>
                                                <c:otherwise>--</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${entity.PLAN_TITLE}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty entity.START_DATE}">${entity.START_DATE}</c:when>
                                                <c:otherwise>--</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty entity.END_DATE}">${entity.END_DATE}</c:when>
                                                <c:otherwise>--</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${entity.PLAN_FINISH_TIME}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty entity.USE_TIME}">${entity.USE_TIME}</c:when>
                                                <c:otherwise>--</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <c:choose>
                                            <c:when test="${entity.PLAN_STATUS eq '0'}"><td class="gray9">未开始</td></c:when>
                                            <c:when test="${entity.PLAN_STATUS eq '1'}"><td class="text-orange">进行中</td></c:when>
                                            <c:when test="${entity.PLAN_STATUS eq '2'}"><td class="text-green">已完成</td></c:when>
                                            <c:otherwise>--</c:otherwise>
                                        </c:choose>
                                        <td>
                                            <c:choose>
                                                <c:when test="${entity.PLAN_STATUS eq '0'}">
                                                    <a href="#" class="operion-item" data-toggle="tooltip" data-role="start-plan" data-value="${entity.PLAN_ID}" title="开始计划" data-container="body"><i class="fa fa-play-circle-o"></i></a>
                                                </c:when>
                                                <c:when test="${entity.PLAN_STATUS eq '1'}">
                                                    <a href="#" class="operion-item" data-toggle="tooltip" data-role="finish" data-value="${entity.PLAN_ID}" title="我已完成" data-container="body"><i class="fa fa-check-circle"></i></a>
                                                </c:when>
                                                <c:otherwise></c:otherwise>
                                            </c:choose>
                                            
                                            <a href="${ctx}/myPlan/viewPlan/${entity.PLAN_ID}" class="operion-item" data-toggle="tooltip" title="查看详情" data-container="body"><i class="fa fa-view-more"></i></a>
                                            
                                            <shiro:hasPermission name="/myPlan/planList$delete">
                                            	<a href="#" class="operion-item" data-toggle="tooltip" data-role="remove-item" data-value="${entity.PLAN_ID}" title="删除"><i class="fa fa-trash-o text-red"></i></a>
                                        	</shiro:hasPermission>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise></c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                    <tags:pagination page="${pageInfo}" paginationSize="5" />
                </div>
            </div>
        </div>
    </form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
    //开始
    $(".box").confirmation({
        selector: '[data-role="start-plan"]',
        html:true,
        placement:'top',
        content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定开始该计划项？</div>',
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
            var plan_id = element[0].getAttribute("data-value");
            $.ajax({
                type:"post",
                url:"${ctx}/myPlan/startPlan/"+plan_id,
                dataType:"json",
                success:function (data) {
                    if(data.successful==true){
                        $("#submit_buttion").click();
                    }else {
                        alert("开始计划失败，请重试！");
                    }
                }
            });
        },
        onCancel:function(event, element){

        }
    });

    //确认完成
    $(".table").confirmation({
        selector: '[data-role="finish"]',
        html:true,
        placement:'top',
        content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定已完成该计划项？</div>',
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
            var plan_id = element[0].getAttribute("data-value");
            $.ajax({
                type:"post",
                url:"${ctx}/myPlan/finishPlan/"+plan_id,
                dataType:"json",
                success:function (data) {
                    if(data.successful==true){
                        $("#submit_buttion").click();
                    }else {
                        alert("完成计划失败，请重试！");
                    }
                }
            });
        },
        onCancel:function(event, element){

        }
    });

    //删除
    $("body").confirmation({
        selector: '[data-role="remove-item"]',
        html:true,
        placement:'top',
        content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定删除该计划项？</div>',
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
            var plan_id = element[0].getAttribute("data-value");
            $.ajax({
                type:"post",
                url:"${ctx}/myPlan/deletePlan/"+plan_id,
                dataType:"json",
                success:function (data) {
                    if(data.successful==true){
                        $("#submit_buttion").click();
                    }else {
                        alert("删除计划失败，请重试！");
                    }
                }
            });
        },
        onCancel:function(event, element){

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

    /*日期控件*/
    $('[data-role="date-group"]').each(function(i,e){
        var startDate=$('[data-role="date-start"]',e);
        var endDate=$('[data-role="date-end"]',e);
        //开始时间
        startDate.datepicker({
            language:'zh-CN',
            format:'yyyy-mm-dd'
        }).on('changeDate', function(e) {
            var add=increaseOnedate(e.target.value);
            endDate.datepicker('setStartDate',add);
        });
        //结束时间
        endDate.datepicker({
            language:'zh-CN',
            format:'yyyy-mm-dd'
        }).on('changeDate', function(e) {
            var d=decreaseOnedate(e.target.value);
            startDate.datepicker('setEndDate',d);
        }).on('focus',function(){
            if(this.value==""&&startDate.val()==""){
                startDate.focus();
                endDate.datepicker('hide');
            }
        });
    });


    function submitform(status) {
        $("#PLAN_STATUS").val(status);
        $("#submit_buttion").click();
    }

    //统计项
    (function asyncCount() {
        var PLAN_TITLE = $("#PLAN_TITLE").val();
        var START_DATE = $("#START_DATE").val();
        var END_DATE = $("#END_DATE").val();
        var STATUS = ',0,1,2';
        $.ajax({
            type:"post",
            dataType:"json",
            data:{PLAN_TITLE:PLAN_TITLE,START_DATE:START_DATE,END_DATE:END_DATE,STATUS:STATUS},
            url:"${ctx}/myPlan/getPlanCount",
            success:function (data) {
                if(data.successful==true){
                    $("#plan_status_").text(data.obj.all==undefined?0:data.obj.all);
                    $("#plan_status_0").text(data.obj.unstart==undefined?0:data.obj.unstart);
                    $("#plan_status_1").text(data.obj.starting==undefined?0:data.obj.starting);
                    $("#plan_status_2").text(data.obj.finish==undefined?0:data.obj.finish);
                }
            }
        });
    })();
    
</script>
</body>
</html>
