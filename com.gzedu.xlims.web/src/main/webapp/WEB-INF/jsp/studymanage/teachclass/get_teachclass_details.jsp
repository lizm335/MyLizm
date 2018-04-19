<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 5/15/2017
  Time: 4:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员学情</title>
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">学习管理</a></li>
        <li><a href="#">教务班学情</a></li>
        <li class="active">教务班学情明细</li>
    </ol>
</section>
<section class="content">
    <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
    <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>
    <div class="nav-tabs-custom margin-bottom-non">
        <%--
        <ul class="nav nav-tabs nav-tabs-lg">
            <li class="active"><a href="#" data-toggle="tab" onclick="changType('1')">学员学情</a></li>
            <li><a href="#"  data-toggle="tab" onclick="changType('2')">课程学情</a></li>
        </ul>
        --%>
        <div class="content">
            <form class="form-horizontal" method="post" id="listForm">
            <div class="box">
                    <div class="box-body">
                        <div class="row reset-form-horizontal pad-t15">

                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3">姓名</label>
                                    <div class="col-sm-9">
                                        <input class="form-control" type="text" name="xm" value="${param['xm']}">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3">学号</label>
                                    <div class="col-sm-9">
                                        <input class="form-control" type="text" name="xh" value="${param['xh']}">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">层次</label>
                                    <div class="col-sm-9">
                                        <select class="selectpicker show-tick form-control" name="pycc" id="pycc" data-size="5" data-live-search="true">
                                            <option value="" selected="selected">请选择</option>
                                            <c:forEach items="${pyccMap}" var="map">
                                                <option value="${map.key}" <c:if test="${map.key==param.pycc}">selected='selected'</c:if>>
                                                        ${map.value}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">专业</label>
                                    <div class="col-sm-9">
                                        <select class="selectpicker show-tick form-control" name="specialtyId" id="specialty_id" data-size="5" data-live-search="true">
                                            <option value="" selected="selected">请选择</option>
                                            <c:forEach items="${specialtyMap}" var="map">
                                                <option value="${map.key}" <c:if test="${param.specialtyId eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap control-label-2lines">
                                        完成<br>课程比例
                                    </label>
                                    <div class="col-sm-9">
                                        <div class="input-group">
                                            <div class="input-group-btn">
                                                <select class="form-control input-group-select bg-white" name="courseSign">
                                                    <option <c:if test="${param.courseSign eq '='}">selected</c:if> value="=">等于</option>
                                                    <option <c:if test="${param.courseSign eq '>'}">selected</c:if> value=">">大于</option>
                                                    <option <c:if test="${param.courseSign eq '>='}">selected</c:if> value=">=">大于等于</option>
                                                    <option <c:if test="${param.courseSign eq '<'}">selected</c:if> value="<">小于</option>
                                                    <option <c:if test="${param.courseSign eq '<='}">selected</c:if> value="<=">小于等于</option>
                                                </select>
                                            </div>
                                            <input type="text" class="form-control" name="course_percent" value="${param.course_percent}">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap control-label-2lines">
                                        完成<br>考试比例
                                    </label>
                                    <div class="col-sm-9">
                                        <div class="input-group">
                                            <div class="input-group-btn">
                                                <select class="form-control input-group-select bg-white" name="examSign">
                                                    <option <c:if test="${param.examSign eq '='}">selected</c:if> value="=">等于</option>
                                                    <option <c:if test="${param.examSign eq '>'}">selected</c:if> value=">">大于</option>
                                                    <option <c:if test="${param.examSign eq '>='}">selected</c:if> value=">=">大于等于</option>
                                                    <option <c:if test="${param.examSign eq '<'}">selected</c:if> value="<">小于</option>
                                                    <option <c:if test="${param.examSign eq '<='}">selected</c:if> value="<=">小于等于</option>
                                                </select>
                                            </div>
                                            <input type="text" class="form-control" name="exam_percent" value="${param.exam_percent}">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap control-label-2lines">
                                            已获<br>学分比例
                                        </label>
                                        <div class="col-sm-9">
                                            <div class="input-group">
                                                <div class="input-group-btn">
                                                    <select class="form-control input-group-select bg-white" name="creditsSign">
                                                        <option <c:if test="${param.creditsSign eq '='}">selected</c:if>  value="=">等于</option>
                                                        <option <c:if test="${param.creditsSign eq '>'}">selected</c:if>  value=">">大于</option>
                                                        <option <c:if test="${param.creditsSign eq '>='}">selected</c:if>  value=">=">大于等于</option>
                                                        <option <c:if test="${param.creditsSign eq '<'}">selected</c:if>  value="<">小于</option>
                                                        <option <c:if test="${param.creditsSign eq '<='}">selected</c:if>  value="<=">小于等于</option>
                                                    </select>
                                                </div>
                                                <input type="text" class="form-control" name="credits_percent" value="${param.credits_percent}">
                                                <input type="hidden" class="" name="passState" value="${param.passState}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </shiro:lacksPermission>
                            <%--
                                                        <div class="col-md-4">
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-3">学习进度</label>
                                                                <div class="col-sm-9">
                                                                    <select name="studyState" class="form-control selectpicker show-tick">
                                                                        <option value="">全部</option>
                                                                        <option value="1" <c:if test="${param['studyState']==1}">selected</c:if>>正常</option>
                                                                        <option value="2" <c:if test="${param['studyState']==2}">selected</c:if>>落后</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="form-group">
                                                                <label class="control-label col-sm-3">学习成绩</label>
                                                                <div class="col-sm-9">
                                                                    <select name="examState" class="form-control selectpicker show-tick">
                                                                        <option value="">全部</option>
                                                                        <option value="1" <c:if test="${param['examState']==1}">selected</c:if>>合格</option>
                                                                        <option value="2" <c:if test="${param['examState']==2}">selected</c:if>>不合格</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                            --%>
                        </div>
                    </div>
                    <div class="box-footer">
                        <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
                        <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary" id="submit_form">搜索</button></div>
                    </div>
            </div>
            <div class="box margin-bottom-none">
                <%--
                  <div class="box-header with-border">
                      <div class="fr">
                          <button class="btn btn-default btn-sm btn-outport" type="button" onclick="exportLeranInfo('${ctx}/home/class/learn/exportInfo?sortType=${sortType}&${searchParams}')"><i class="fa fa-fw fa-sign-out"></i> 导出学情信息</button>
                      </div>
                      <h3 class="box-title pad-t5">学情列表</h3>
                  </div>
                --%>
                <div class="box-body">
                    <%--
                      <div class="filter-tabs clearfix">
                          <ul class="list-unstyled">
                              <li id="search_actived_0" data-num="0" <c:if test="${empty param['studyState'] and empty param['examState']}">class="actived"</c:if>>全部(${infoNum.ALL_STUDENT})</li>
                              <li id="search_actived_1" data-num="1" <c:if test="${param['studyState']==2}">class="actived"</c:if>>学习进度落后(${infoNum.STUDY_BEHIND})</li>
                              <li id="search_actived_2" data-num="2" <c:if test="${param['studyState']==1}">class="actived"</c:if>>学习进度正常(${infoNum.STUDY_NORMAL})</li>
                              <li id="search_actived_3" data-num="3" <c:if test="${param['examState']==2}">class="actived"</c:if>>学习成绩不合格(${infoNum.SCORE_PASS})</li>
                              <li id="search_actived_4" data-num="4" <c:if test="${param['examState']==1}">class="actived"</c:if>>学习成绩合格(${infoNum.SCORE_UNPASS})</li>
                          </ul>
                      </div>
                    --%>
                    <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
                        <div class="filter-tabs clearfix">
                            <ul class="list-unstyled">
                                <li id="search_actived_0" data-num="0" <c:if test="${param['passState'] eq '0'}">class="actived"</c:if> >不满足最低毕业学分(${infoNum.unpass})</li>
                                <li id="search_actived_1" data-num="1" <c:if test="${param['passState'] eq '1'}">class="actived"</c:if> >已满足最低毕业学分(${infoNum.pass})</li>
                            </ul>
                        </div>
                    </shiro:lacksPermission>
                    <div class="table-responsive">
                        <table id="dtable" class="table table-bordered table-striped table-cell-ver-mid text-center">
                            <thead>
                            <tr>
                                <%--
                                    <th><input type="checkbox" class="select-all"></th>
                                    <th>学员姓名</th>
                                    <th>学习总次数</th>
                                    <th>学习总时长</th>
                                    <th>上次学习间隔天数</th>
                                    <th>全部课程</th>
                                    <th>学习进度落后课程</th>
                                    <th>学习进度正常课程</th>
                                    <th>学习成绩不合格课程</th>
                                    <th>学习成绩合格课程</th>
                                    <th>操作</th>
                                --%>
                                <th width="18%">个人信息</th>
                                <th width="24%">报读信息</th>
                                <th>当前学期</th>
                                <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
                                    <th>已获学分/<br>
                                        最低毕业学分/<br>
                                        总学分</th>
                                    <th>是否满足<br>
                                        最低毕业学分</th>
                                </shiro:lacksPermission>
                                <th>已完成课程数/<br>
                                    课程总数</th>
                                <th>已完成考试数/<br>
                                    考试科目总数</th>
                                <%--<th>操作</th>--%>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty result && result.numberOfElements > 0}">
                                    <c:forEach items="${result.content}" var="info">
                                        <c:if test="${not empty info}">
                                            <tr>
                                                    <%--<td><input type="checkbox" name="ids" data-id="${info.STUDENT_ID}" data-name="check-id" value="${info.STUDENT_ID}"></td>--%>
                                                <td>
                                                    <div class="text-left">
                                                        姓名： ${info.XM}<br>
                                                        学号： ${info.XH}<br>
                                                        <shiro:hasPermission name="/personal/index$privacyJurisdiction">
                                                        手机： ${info.SJH}
                                                        </shiro:hasPermission>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="text-left">
                                                        层次：${info.PYCC_NAME}<br>
                                                        年级：${info.NAME}<br>
                                                        学期：${info.GRADE_NAME}<br>
                                                        专业：${info.ZYMC}
                                                    </div>
                                                </td>
                                                <td>${info.CURRENT_TERM}</td>
                                                <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
                                                    <td class="">
                                                        <div>
                                                            <span class="f18">${info.CREDITS_COUNT}/</span>${info.CREDITS_MIN}/${info.ZXF}
                                                        </div>
                                                        <div class="gray9">
                                                            （已获得${info.CREDITS_PERCENT}%）
                                                        </div>
                                                    </td>
                                                    <td class="">
                                                        <c:if test="${info.CREDITS_COUNT lt info.CREDITS_MIN}"><span class="text-orange">不满足</span></c:if><!-- ==eq 等于!= ne 不等于> gt 大于< lt 小于>= ge 大于等于<= le 小于等于-->
                                                        <c:if test="${info.CREDITS_COUNT ge info.CREDITS_MIN}"><span class="text-green">已满足</span></c:if>

                                                    </td>
                                                </shiro:lacksPermission>
                                                <td class="">
                                                    <div>
                                                        <span class="f18">${info.COURSE_FINISH}/</span>${info.COURSE_COUNT}
                                                    </div>
                                                    <div class="gray9">
                                                        （已完成${info.COURSE_PERCENT}%）
                                                    </div>
                                                </td>
                                                <td class="">
                                                    <div>
                                                        <span class="f18">${info.EXAM_FINISH}/</span>${info.EXAM_COUNT}
                                                    </div>
                                                    <div class="gray9">
                                                        （已完成${info.EXAM_PERCENT}%）
                                                    </div>
                                                </td>
                                                    <%--
                                                    <td class="text-red text-bold">${info.SCORE_UNPASS}</td>
                                                    <td class="">${info.SCORE_PASS}</td>
                                                    --%>
                                                <%--
                                                <td>
                                                    <div class="data-operion">
                                                        <a href="view/${info.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                                    </div>
                                                </td>
                                                --%>
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
                    <tags:pagination page="${result}" paginationSize="10" />
                </div>
            </div>
            </form>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
    function exportLeranInfo(url) {
        var form = $("<form>");//定义form表单
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");
        form.attr("action",url);
        $("body").append(form);
        form.submit();//表单提交
    }

    $("#search_actived_0,#search_actived_1").click(function () {
        var data = Number($(this).attr("data-num"));
        switch (data){
            case 0:
                $("input[name='passState']").val("0");//不满足
                break;
            case 1:
                $("input[name='passState']").val("1");//满足
                break;
            default: $("input[name='passState']").val("");
        }
        $("#submit_form").click();
    });
    <%-- 原来的项目 据说优化，先注释，说不定哪天又要
        //切换到课程学情
        function changType(type) {
            if (type=="1") {
                window.location.href = "${ctx}/home/class/learn/list";
            } else {
                window.location.href = "${ctx}/home/class/learn/courseList";
            }
        }
    --%>
</script>
</body>
</html>

