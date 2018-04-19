<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 考试管理</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
    <div class="pull-left">
        您所在位置：
    </div>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="${ctx}/home/class/exam/list">考试管理</a></li>
        <li class="active">预约考试详情</li>
    </ol>
</section>
<section class="content">
    <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
    <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

    <div class="box">
        <div class="box-body">
            <div class="media pad">
                <div class="media-left" style="padding-right:25px;">
                    <c:choose>
                        <c:when test="${not empty info.avatar}">
                            <img id ="headImgId" src="${info.avatar}" class="img-circle" alt="" style="width: 128px; height: 128px;" onerror="this.src='${ctx }/static/images/headImg04.png'">
                        </c:when>
                        <c:otherwise>
                            <img id ="headImgId" src="${ctx }/static/images/headImg04.png" class="img-circle" alt="" style="width: 128px; height: 128px;">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="media-body">
                    <h3 class="margin_t10">${info.xm}（<dic:getLabel typeCode="Sex" code="${info.xbm}"/>）</h3>
                    <div class="row">
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>学号:</b> <span>${info.xh}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>手机:</b>
                            <span>${info.sjh}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>邮箱:</b>
                            <span>${info.dzxx}</span>

                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>层次:</b> <span><dic:getLabel typeCode="TrainingLevel" code="${info.pycc}"/></span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>年级:</b>
                            <span>${info.gjtSignup.gjtEnrollBatch.gjtGrade.gradeName}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>专业:</b>
                            <span>${info.gjtSpecialty.zymc}（<dic:getLabel typeCode="TrainingLevel" code="${info.pycc}" />）</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="box-footer">
            <div class="row stu-info-status">
                <div class="col-xs-4">
                    <div class="f24 text-center">${studentReserveSituation[0]-studentReserveSituation[1]}</div>
                    <div class="text-center gray6">待预约</div>
                </div>
                <div class="col-xs-4">
                    <div class="f24 text-center">${studentReserveSituation[0]}</div>
                    <div class="text-center gray6">全部考试</div>
                </div>
                <div class="col-xs-4">
                    <c:if test="${empty studentReserveSituation[2]}">
                        <div class="f24 text-center text-orange">待预约</div>
                    </c:if>
                    <c:if test="${not empty studentReserveSituation[2]}">
                        <div class="f24 text-center">已预约</div>
                    </c:if>
                    <div class="text-center gray6">考点预约</div>
                </div>
            </div>
        </div>
    </div>

    <div class="box no-border margin-bottom-none">
        <div class="nav-tabs-custom reset-nav-tabs-custom" id="notice-tabs">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#tab_notice_1" data-toggle="tab">预约考试</a></li>
                <li><a href="#tab_notice_2" data-toggle="tab">预约考点</a></li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_notice_1">
                    <div class="text-right">
                        <button class="btn min-width-90px btn-default btn-add multi-appoint"><i class="fa fa-fw fa-dating"></i> 批量预约</button>
                        <a href="javascript:;" onclick="exportInfoDetails('/home/class/exam/exportInfo_stuResExam?studentId=${studentId}')" target="_blank" class="btn min-width-90px margin_l10 btn-default btn-sm"><i class="fa fa-fw fa-sign-out"></i> 导出详情</a>
                    </div>
                    <table id="Merge_rows" class="table table-bordered table-striped table-cell-ver-mid text-center dataTable">
                        <thead>
                        <tr>
                            <th><input type="checkbox" class="select-all"></th>
                            <th>学期</th>
                            <th>考试科目</th>
                            <th>考试方式</th>
                            <th>距离预约结束天数</th>
                            <th>考试预约状态</th>
                            <th>考试预约操作时间</th>
                            <th>考试预约操作人</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty infos && fn:length(infos) > 0}">
                                <c:forEach items="${infos}" var="info" varStatus="s">
                                    <c:if test="${not empty info}">
                                        <!-- 考试方式：非网络考试 -->
                                        <c:set var="isNotOnlineExam" value="${info.KSFS=='1'||info.KSFS=='2'||info.KSFS=='3'||info.KSFS=='4'||info.KSFS=='8'}" />
                                        <tr>
                                            <td><input type="checkbox" name="ids"
                                                       data-id="${info.REC_ID}" data-name="check-id"
                                                       value="${info.REC_ID}"
                                                       <c:if test="${!(info.EXAM_STATE=='1'&&isNotOnlineExam)}">disabled="disabled"</c:if> />
                                            </td>
                                            <td class="td-mid text-center">${info.TERM_NAME}</td>
                                            <td>${info.KCMC}</td>
                                            <td>
                                                <dic:getLabel typeCode="ExaminationMode" code="${info.KSFS}" />
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${info.RESERVESTARTDAYS<0}">
                                                        未开始
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${info.RESERVEENDDAYS<0}">
                                                                已结束
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${info.RESERVEENDDAYS}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="examState">
                                                <dic:getLabel typeCode="EXAM_STATE" code="${info.EXAM_STATE}" />
                                            </td>
                                            <td><fmt:formatDate value="${info.UPDATED_DT}" type="both" /></td>
                                            <td>
                                                <dic:getLabel typeCode="UserType" code="${info.USER_TYPE}" />
                                            </td>
                                            <td>
                                                <div class="data-operion">
                                                    <c:choose>
                                                        <c:when test="${info.EXAM_STATE=='1'}">
                                                            <c:if test="${isNotOnlineExam}">
                                                                <a href="${ctx}/home/class/exam/appoint?recId=${info.REC_ID}&studentId=${studentId}" class="operion-item date-test" data-toggle="tooltip" title="预约考试"><i class="fa fa-fw fa-date-test"></i></a>
                                                            </c:if>
                                                        </c:when>
                                                        <c:when test="${info.EXAM_STATE=='2'}">
                                                            <c:if test="${info.RESERVEENDDAYS>=0}">
                                                                <a href="${ctx}/home/class/exam/cancel?recId=${info.REC_ID}&studentId=${studentId}" class="operion-item no-test" data-toggle="tooltip" title="不参与考试"><i class="fa fa-fw fa-no-test text-red"></i></a>
                                                            </c:if>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td align="center" colspan="15">暂无数据</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div><!-- /.tab-pane -->
                <div class="tab-pane" id="tab_notice_2">
<%--
                    <div class="row bg-info">
                        <div class="form-group col-sm-4">
                            <label class="col-sm-3 control-label">当前学期:</label>
                            <div class="col-sm-9">
                                ${currentTermInfo.termName}
                            </div>
                        </div>
                        <div class="form-group col-sm-4">
                            <label class="col-sm-3 control-label">当前考点:</label>
                            <div class="col-sm-9">
                                ${currentExamPoint.name}
                            </div>
                        </div>
                    </div>
--%>
                    <form id="yykdForm" role="form" action="${ctx }/home/class/exam/pointList" method="post">
                        <div class="row">
                            <div class="col-sm-4 margin_b15">
                                <div class="form-group margin-bottom-none">
                                    <div class="input-group">
                                    <span class="input-group-addon no-border text-left2" style="padding-right: 20px;">
                                        <b>区域</b>
                                    </span>
                                        <div class="pad no-pad-top no-pad-bottom">
                                            <div class="col-sm-4" style="padding-left: 0px; padding-right: 5px;">
                                                <select name="search_province" class="form-control selectpicker show-tick bs-select-hidden" data-size="10">
                                                    <option value="${info.province}" selected="selected"></option>
                                                </select>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0px; padding-right: 5px;">
                                                <select name="search_city" class="form-control selectpicker show-tick bs-select-hidden" data-size="10">
                                                    <option value="${info.city}" selected="selected"></option>
                                                </select>
                                            </div>
                                            <div class="col-sm-4" style="padding-left: 0px; padding-right: 5px;">
                                                <select name="search_area" class="form-control selectpicker show-tick bs-select-hidden" data-size="10">
                                                    <option value="${info.area}" selected="selected"></option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4 margin_b15">
                                <div class="form-group margin-bottom-none">
                                    <div class="input-group full-width">
                                    <span class="input-group-addon no-border text-left2">
                                        <b>名称</b>
                                    </span>
                                        <input type="text" name="search_LIKE_name" value="${param.search_LIKE_name}" class="form-control text-left">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4 margin_b15">
                                <button type="submit" class="btn min-width-90px btn-primary">搜索</button>
                                <button type="reset" class="btn btn-default margin_l10">重置</button>
                            </div>
                        </div>
                    </form>
                    <div class="row test-list">
                        <div class="test-item" style="text-align: center;">
                            请根据条件查询
                        </div>
                    </div>
                    <div class="row text-right btn-wrap-width-border">
                        <div class="col-xs-12">
                            <button type="button" class="btn btn-success min-width-90px margin_l15 margin_r15 btn-save-edit">保存</button>
                            <button type="button" class="btn btn-default min-width-90px btn-cancel-edit">取消</button>
                        </div>
                    </div>
                </div><!-- /.tab-pane -->
            </div><!-- /.tab-content -->
        </div><!-- nav-tabs-custom -->
    </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<!--合并第一行重复项 -->
<script type="application/javascript">
    $(function autoRowSpan() {
        var lastValue = "";
        var value = "";
        var pos = 1;
        var row= 1;
        var col = 2;
        for ( var i = row; i < Merge_rows.rows.length; i++) {
            value = Merge_rows.rows[i].cells[col].innerText;
            if (lastValue == value) {
                Merge_rows.rows[i].deleteCell(col);
                Merge_rows.rows[i - pos].cells[col].rowSpan = Merge_rows.rows[i - pos].cells[col].rowSpan + 1;
                pos++;
            } else {
                lastValue = value;
                pos = 1;
            }
        }
    })
</script>
<!--合并第一行重复项 -->
<script>

    //加载省市区联动查询
    $(":input[name='search_province']").select_area($(":input[name='search_city']"), $(":input[name='search_area']"), $(":input[name='search_province']").val(), $(":input[name='search_city']").val(), $(":input[name='search_area']").val());

    $('.examState').each(function (i, item) {
        if(this.innerHTML.indexOf('未预约') != -1) {
            this.style.color = '#dd4b39';
        }
    });

    //预约考试
    $('.dataTable').on('click',".date-test",function(){
        var $this = $(this);
        $.confirm({
            title: '',
            content: '<div class="f20 row" style="margin-bottom:-15px;"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span>确认预约该考试？</div>',
            confirm: function(){
                //$.alert('Confirmed!');
                window.location.href = $this.attr('href');
            },
            confirmButton:'确认',
            confirmButtonClass:'btn-primary',
            cancel: function(){
                //$.alert('Canceled!')
            },
            cancelButton:'取消'
        });
        return false;
    })
    //不参与考试
            .on('click',".no-test",function(){
                var $this = $(this);
                $.confirm({
                    title: '',
                    content: '<div class="f20 row" style="margin-bottom:-15px;"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span>确认不参与该考试？</div>',
                    confirm: function(){
                        //$.alert('Confirmed!');
                        window.location.href = $this.attr('href');
                    },
                    confirmButton:'确认',
                    confirmButtonClass:'btn-primary',
                    cancel: function(){
                        //$.alert('Canceled!')
                    },
                    cancelButton:'取消'
                });
                return false;
            });

    //预约多个
    $('.multi-appoint').off().click(function(){
        var $checkedIds = $("table td input[name='ids']:enabled:checked");
        if ($checkedIds.size() == 0) {
            $.alert({
                title: '',
                content: '<div class="f20 row" style="margin-bottom:-15px;"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span>至少选择一个课程！</div>',
                confirmButton: '确认',
                confirmButtonClass: 'btn-primary'
            });
            return false;
        }
        $.confirm({
            title: '提示',
            content: '您确定要预约考试的选中课程吗？',
            icon: 'fa fa-warning',
            closeIcon: true,
            closeIconClass: 'fa fa-close',
            confirmButton: '确认',
            confirmButtonClass: 'btn-primary',
            cancelButton: '取消',
            confirm: function () {
                var ids = '';
                $checkedIds.each(function (i,item) {
                    ids = ids + 'recId=' + item.value + '&';
                });
                window.location.href = '${ctx}/home/class/exam/appoint?'+ids+'studentId=${studentId}';
            }
        });
    });

    //选中
    $('.test-list').delegate(".test-item:not([disabled]) > .panel", 'click', function(event) {
        $(this).parent().siblings().removeClass('on');
        $(this).parent().toggleClass('on');
    });

    // 获取考点信息
    $('#yykdForm').submit(function () {
        $('.test-list').empty();
        if($(":input[name='search_LIKE_name']").val() == "" && $(":input[name='search_province']").val() == "") {
/**
            $.alert({
                title: '',
                content: '<div class="f20 row" style="margin-bottom:-15px;"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span>查询前请先筛选条件！</div>',
                confirmButton: '确认',
                confirmButtonClass: 'btn-primary'
            });
            return false;
 **/
        }
        $.getJSON('${ctx}/home/class/exam/pointList', $(this).serialize(), function (data) {
            if(data.successful && data.obj.length > 0) {
                $.each(data.obj, function (i, item) {
                    $('.test-list').append('<div class="col-sm-6 col-md-4 test-item" val="'+item.id+'" pointName="'+item.name+'"><div class="panel panel-default"><div class="panel-heading"><div class="table-block"><div class="table-row"><div class="table-cell-block td1"><i class="raido-circle"></i></div><div class="table-cell-block td2"><h3 class="panel-title text-bold">'+item.name+'</h3></div><div class="table-cell-block td3" style="display:none;"><span class="text-green">400</span>/500</div></div></div></div><div class="panel-body"><div class="table-block full-width"><div class="table-row"><div class="table-cell-block">区域：</div><div class="table-cell-block">'+item.colAreaAllName+'</div></div><div class="table-row"><div class="table-cell-block">地址：</div><div class="table-cell-block">'+item.address+'</div></div></div></div></div></div>');
                });
            } else {
                $('.test-list').append('<div class="test-item" style="text-align: center;">暂无数据</div>');
            }
        });
        return false;
    });

    // 预约考点
    $('.btn-save-edit').off().click(function(){
        var examPointId = $('.test-list .test-item.on').attr('val');
        var pointName = $('.test-list .test-item.on').attr('pointName');
        if (examPointId == undefined) {
            $.alert({
                title: '',
                content: '<div class="f20 row" style="margin-bottom:-15px;"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span>请选择一个考点！</div>',
                confirmButton: '确认',
                confirmButtonClass: 'btn-primary'
            });
            return false;
        }
        $.confirm({
            title: '提示',
            content: '您现在预约考试的考点是：[' + pointName + ']，确定吗？',
            icon: 'fa fa-warning',
            closeIcon: true,
            closeIconClass: 'fa fa-close',
            confirmButton: '确认',
            confirmButtonClass: 'btn-primary',
            cancelButton: '取消',
            confirm: function () {
                window.location.href='${ctx}/home/class/exam/saveExamPoint?examPointId='+examPointId+'&studentId=${studentId}&termId=${currentTermId}';
            }
        });
    });

    function exportInfoDetails(url) {
        var form = $("<form>");
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");
        form.attr("action",url);
        $("body").append(form);
        form.submit();//表单提交
    }

</script>
</body>
</html>
