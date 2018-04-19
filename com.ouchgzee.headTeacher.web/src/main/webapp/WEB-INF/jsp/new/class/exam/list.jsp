<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <h1>
        考试管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">考试管理</li>
    </ol>
</section>
<section class="content">
    <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
    <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>
        <form class="form-horizontal">
            <div class="box">
                <div class="box-body">
                    <div class="row reset-form-horizontal pad-t15">
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">姓名</label>
                                <div class="col-sm-9">
                                    <input class="form-control" type="text" name="xm" value="${param['xm']}">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">学号</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control" name="xh" value="${param['xh']}">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">课程</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control" name="kcmc" value="${param['kcmc']}">
                                </div>
                            </div>
                        </div>
<%--
                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="control-label col-sm-3">考点状态</label>
                                <div class="col-sm-9">
                                    <select name="search_examState" class="form-control selectpicker show-tick">
                                        <option value="">全部</option>
                                        <option value="1" <c:if test="${param['search_examState']==1}">selected</c:if>>已预约</option>
                                        <option value="2" <c:if test="${param['search_examState']==2}">selected</c:if>>未预约</option>
                                    </select>
                                </div>
                            </div>
                        </div>
--%>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">专业</label>
                                <div class="col-sm-9">
                                    <select class="form-control selectpicker show-tick bs-select-hidden" name="specialtyId" data-size="10" data-live-search="true">
                                        <option value="">请选择</option>
                                        <c:forEach items="${specialtyMap}" var="list">
                                            <option value="${list.key}" <c:if test="${list.key==param['specialtyId']}">selected='selected'</c:if> >${list.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">年级</label>
                                <div class="col-sm-9">
                                    <select class="form-control selectpicker show-tick bs-select-hidden" name="gradeId" data-size="10" data-live-search="true">
                                        <option value="">请选择</option>
                                        <c:forEach items="${gradeMap}" var="list">
                                            <option value="${list.key}" <c:if test="${list.key==param['gradeId']}">selected='selected'</c:if> >${list.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">层次</label>
                                <div class="col-sm-9">
                                    <select class="form-control selectpicker show-tick bs-select-hidden" name="pycc" data-size="10" data-live-search="true">
                                        <option value="">请选择</option>
                                        <c:forEach items="${pyccMap}" var="list">
                                            <option value="${list.key}" <c:if test="${list.key==param['pycc']}">selected='selected'</c:if> >${list.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box-footer">
                    <input type="hidden" name="chooseType" value="${param.chooseType}" >
                    <input type="hidden" name="ksfs" value="" >
                    <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
                    <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary" id="submit_form">搜索</button></div>
                </div>
            </div>
        </form>

    <div class="box margin-bottom-none">
        <div class="box-header with-border">
            <h3 class="box-title pad-t5">预约列表</h3>
            <div class="pull-right no-margin">
                <button class="btn btn-default btn-sm btn-import" type="button" onclick="exportInfo('/home/class/exam/exportInfo?sortType=${sortType}&${searchParams}')"><i class="fa fa-fw fa-sign-out"></i> 导出考试详情</button>
            </div>
        </div>
        <div class="box-body">
            <div class="filter-tabs clearfix">
                <ul class="list-unstyled">
                    <li <c:if test="${empty param.chooseType}">class="actived"</c:if>   id="search_actived_0" data-num="0">全部(${infoNum.num0})</li>
                    <li <c:if test="${ fn:contains(param.chooseType,'1')}">class="actived"</c:if> id="search_actived_1" data-num="1">待预约考试(${infoNum.num1})</li>
                    <li <c:if test="${ fn:contains(param.chooseType,'2')}">class="actived"</c:if> id="search_actived_3" data-num="2">已预约考试(${infoNum.num2})</li>
                    <li <c:if test="${ fn:contains(param.chooseType,'3')}">class="actived"</c:if> id="search_actived_4" data-num="3">待预约考点(${infoNum.num3})</li>
                    <li <c:if test="${ fn:contains(param.chooseType,'4')}">class="actived"</c:if> id="search_actived_2" data-num="4">已预约考点(${infoNum.num4})</li>
                </ul>
            </div>
            <div class="table-responsive">
                <table id="dtable" class="table table-bordered table-striped table-cell-ver-mid text-center">
                    <thead>
                    <tr>
                        <th><input type="checkbox" class="select-all no-margin"></th>
                        <th>个人信息</th>
                        <th>报读信息</th>
                        <th>学期</th>
                        <th>课程</th>
                        <%--<th>可预约考试数</th>--%>
                        <%--<th>已预约考试数</th>--%>
                        <%--<th>已预约考点</th>--%>
                        <th class="no-padding" data-role="menu-th">
                            <div class="dropdown custom-dropdown">
                                <a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
                                    考试方式
                                    <span class="caret"></span>
                                </a>
                                <div class="dropdown-menu">
                                    <ul class="list-unstyled">
                                        <c:forEach items="${infoNum.ksfs}" var="info">
                                            <li>
                                                <i class="fa fa-fw"></i> ${info.key}（${info.value[1]}）
                                                <input type="checkbox" name="selectKSFS" value="${info.value[0]}">
                                            </li>
                                        </c:forEach>
                                    </ul>
                                    <div class="text-center margin_t5">
                                        <button class="btn btn-primary btn-xs" data-role="sure-btn" id="selectKSFS_btn">确定</button>
                                        <button class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
                                    </div>
                                </div>

                            </div>
                        </th>
                        <th class="no-padding" data-role="menu-th">
                            <div class="dropdown custom-dropdown">
                                <a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
                                    考试预约状态
                                    <span class="caret"></span>
                                </a>
                                <div class="dropdown-menu">
                                    <ul class="list-unstyled">
                                        <li>
                                            <i class="fa fa-fw"></i> 待预约（${infoNum.num1}）
                                            <input type="checkbox" name="selectExamChooseType" value="1">
                                        </li>
                                        <li>
                                            <i class="fa fa-fw"></i> 已预约（${infoNum.num2}）
                                            <input type="checkbox" name="selectExamChooseType" value="2">
                                        </li>
                                    </ul>
                                    <div class="text-center margin_t5">
                                        <button class="btn btn-primary btn-xs" data-role="sure-btn" id="selectExamChooseType_btn">确定</button>
                                        <button class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
                                    </div>
                                </div>

                            </div>
                        </th>
                        <th class="no-padding" data-role="menu-th">
                            <div class="dropdown custom-dropdown">
                                <a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
                                    考点预约状态
                                    <span class="caret"></span>
                                </a>
                                <div class="dropdown-menu">
                                    <ul class="list-unstyled">
                                        <li>
                                            <i class="fa fa-fw"></i> 待预约（${infoNum.num3}）
                                            <input type="checkbox"  name="selectPointChooseType" value="3">
                                        </li>
                                        <li>
                                            <i class="fa fa-fw"></i> 已预约（${infoNum.num4}）
                                            <input type="checkbox"  name="selectPointChooseType" value="4">
                                        </li>
                                    </ul>
                                    <div class="text-center margin_t5">
                                        <button class="btn btn-primary btn-xs" data-role="sure-btn" id="selectPointChooseType_btn">确定</button>
                                        <button class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
                                    </div>
                                </div>

                            </div>
                        </th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty infos && infos.numberOfElements > 0}">
                            <c:forEach items="${infos.content}" var="info">
                                <c:if test="${not empty info}">
                                    <tr>
                                        <td><input type="checkbox" name="ids"
                                                   data-id="${info.STUDENT_ID}" data-name="check-id"
                                                   value="${info.STUDENT_ID}"></td>
                                        <td class="text-left">
                                            姓名：${info.XM}<br>
                                            学号：${info.XH}<br>
                                            手机：${info.SJH}
                                        </td>
                                        <td class="text-left">
                                            层次：${info.PYCC_NAME}<br>
                                            年级：${info.GRADE_NAME}<br>
                                            专业：${info.ZYMC}
                                        </td>
                                        <td>${info.TERM_NAME} </td>
                                        <td>${info.KCMC}</td>
                                        <td>${info.KSFS_NAME}</td>
                                        <c:if test="${not empty info.ALREADY_EXAMNUM}"><td>已预约</td></c:if>
                                        <c:if test="${empty info.ALREADY_EXAMNUM}"><td class="text-orange">待预约</td></c:if>
                                        <c:if test="${not empty info.EXAM_POINT_NAME}"><td>已预约</td></c:if>
                                        <c:if test="${empty info.EXAM_POINT_NAME}"><td class="text-orange">待预约</td></c:if>
                                        <td>
                                            <div class="data-operion">
                                                <a href="update/${info.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="预约"><i class="fa fa-fw fa-dating"></i></a>
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
            </div>
            <tags:pagination page="${infos}" paginationSize="10" />
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
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
            var btn_id = $(this).attr("id");
            if (btn_id === 'selectExamChooseType_btn') {//考试预约
                var $boxes = $("input[name='selectExamChooseType']:checked");
                var params = new Array();
                $boxes.each(function(){
                    params.push($(this).val());
                });
                var selectExamChooseType =params.join();
                if(selectExamChooseType!=''){
                    $("input[name='chooseType']").val(selectExamChooseType);
                    $("#submit_form").click();
                }

            } else if (btn_id === 'selectPointChooseType_btn') {//考点预约
                var $boxes = $("input[name='selectPointChooseType']:checked");
                var params = new Array();
                $boxes.each(function(){
                    params.push($(this).val());
                });
                var selectPointChooseType =params.join();
                if(selectPointChooseType!=''){
                    $("input[name='chooseType']").val(selectPointChooseType);
                    $("#submit_form").click();
                }
            } else if (btn_id === 'selectKSFS_btn'){//考试方式
                var $boxes = $("input[name='selectKSFS']:checked");
                var params = new Array();
                $boxes.each(function(){
                    params.push($(this).val());
                });
                var ksfs =params.join();
                if(ksfs!=''){
                    $("input[name='ksfs']").val(ksfs);
                    $("#submit_form").click();
                }
            }

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


    $(".table").on('click', 'tbody tr', function (event) {
        var $tbl=$(".table");
        var ck=$(this).find(":checkbox");
        $(this).toggleClass('selected');
        if(!$(event.target).is(":checkbox")){
            if(!ck.is(":checked")){
                ck.prop("checked",true);
            }
            else{
                ck.prop("checked",false);
            }
        }
        if(!ck.is(":checked")){
            $tbl.find(".select-all").prop("checked",false);
        }
    })
        .on("click",".select-all",function(){
            var $tbl=$(".table");
            if($(this).is(":checked")){
                $tbl.find('tr').addClass("selected");
                $tbl.find(':checkbox').prop("checked",true);
            }
            else{
                $tbl.find('tr').removeClass("selected");
                $tbl.find(':checkbox').prop("checked",false);
            }
        });


    function exportInfo(url) {
        var form = $("<form>");//定义form表单
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");
        form.attr("action",url);
        $("body").append(form);
        form.submit();//表单提交
    }

    $("#search_actived_0,#search_actived_1,#search_actived_2,#search_actived_3,#search_actived_4").click(function () {
        var data = Number($(this).attr("data-num"));
        switch (data){
            case 1:
                $("input[name='chooseType']").val("1");//待预约考试
                break
            case 2:
                $("input[name='chooseType']").val("2");//已预约考试
                break
            case 3:
                $("input[name='chooseType']").val("3");//待预约考点
                break
            case 4:
                $("input[name='chooseType']").val("4");//已预约考点
                break
            case 0:
                $("input[name='chooseType']").val("");//全部
                break
            default: $("input[name='chooseType']").val("");
        }
        $("#submit_form").click();
    });

</script>
</body>
</html>
