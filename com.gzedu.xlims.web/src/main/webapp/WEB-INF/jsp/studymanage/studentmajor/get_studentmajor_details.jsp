<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 5/11/2017
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>学期分析</title>
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">学习管理</a></li>
        <li><a href="#">学情分析</a></li>
        <li class="active">专业学情明细</li>
    </ol>
</section>
<section class="content">
    <div class="nav-tabs-custom no-margin">
        <form id="listForm" class="form-horizontal">
            <div class="tab-content">
                <div class="tab-pane active" id="tab_top_2">
                    <div class="box box-border">
                        <div class="box-body">
                            <div class="row pad-t15">

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">姓名</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="XM" value="${param.XM }">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">学号</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="XH" value="${param.XH }">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">教务班</label>
                                        <div class="col-sm-9">
                                            <select class="selectpicker show-tick form-control" name="classId" id="classId" data-size="5" data-live-search="true">
                                                <option value="" selected="selected">请选择</option>
                                                <c:forEach items="${classMap}" var="map">
                                                    <option value="${map.key}" <c:if test="${map.key==param.PYCC}">selected='selected'</c:if>>
                                                            ${map.value}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">完成课程比例</label>
                                        <div class="col-sm-9">
                                            <div class="input-group">
                                                <div class="input-group-btn">
                                                    <select class="form-control input-group-select bg-white" name="PASS_FLG">
                                                        <option value="EQ" <c:if test="${empty param.PASS_FLG || param.PASS_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
                                                        <option value="GT" <c:if test="${param.PASS_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
                                                        <option value="GTE" <c:if test="${param.PASS_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
                                                        <option value="LT" <c:if test="${param.PASS_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
                                                        <option value="LTE" <c:if test="${param.PASS_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
                                                    </select>
                                                </div>
                                                <input type="text" name="PASS_BL" class="form-control" value="${param.PASS_BL }">
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">完成课程比例</label>
                                        <div class="col-sm-9">
                                            <div class="input-group">
                                                <div class="input-group-btn">
                                                    <select class="form-control input-group-select bg-white" name="COURSE_PASS_FLG">
                                                        <option value="EQ" <c:if test="${empty param.COURSE_PASS_FLG || param.COURSE_PASS_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
                                                        <option value="GT" <c:if test="${param.COURSE_PASS_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
                                                        <option value="GTE" <c:if test="${param.COURSE_PASS_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
                                                        <option value="LT" <c:if test="${param.COURSE_PASS_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
                                                        <option value="LTE" <c:if test="${param.COURSE_PASS_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
                                                    </select>
                                                </div>
                                                <input type="text" name="COURSE_PASS_BL" class="form-control" value="${param.COURSE_PASS_BL }">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">已获学分比例</label>
                                            <div class="col-sm-9">
                                                <div class="input-group">
                                                    <div class="input-group-btn">
                                                        <select class="form-control input-group-select bg-white" name="PASS_FLG">
                                                            <option value="EQ" <c:if test="${empty param.XF_FLG || param.XF_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
                                                            <option value="GT" <c:if test="${param.XF_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
                                                            <option value="GTE" <c:if test="${param.XF_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
                                                            <option value="LT" <c:if test="${param.XF_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
                                                            <option value="LTE" <c:if test="${param.XF_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
                                                        </select>
                                                    </div>
                                                    <input type="text" name="XF_BL" class="form-control" value="${param.XF_BL }">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </shiro:lacksPermission>

                            </div>
                        </div><!-- /.box-body -->
                        <div class="box-footer text-right">
                            <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
                            <button type="button" class="btn min-width-90px btn-default">重置</button>
                        </div><!-- /.box-footer-->
                    </div>
                    <div class="box box-border margin-bottom-none">
                        <div class="box-body">
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped vertical-mid text-center table-font">
                                    <thead>
                                    <tr>
                                        <th>个人信息</th>
                                        <th width="250px">报读信息</th>
                                        <th>教务班</th>
                                        <th>当前学期</th>
                                        <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
                                            <th>已获学分/<br>最低毕业学分/<br>总学分</th>
                                            <th>是否满足<br>最低毕业学分</th>
                                        </shiro:lacksPermission>
                                        <th>已完成课程数/<br>课程总数</th>
                                        <th>已完成考试数/<br>考试科目总数</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pageInfo.content}" var="entity">
                                        <tr>
                                            <td>
                                                <div class="text-left">
                                                    姓名：${entity.XM }<br>
                                                    学号：${entity.XH }<br>
                                                    <shiro:hasPermission name="/personal/index$privacyJurisdiction">
                                                    手机：${entity.SJH }
                                                    </shiro:hasPermission>
                                                </div>
                                            </td>
                                            <td class="text-left">
                                                <div class="text-left">
                                                    层次：${entity.PYCC_NAME }<br>
                                                    年级：${entity.YEAR_NAME}<br>
                                                    学期：${entity.GRADE_NAME }<br>
                                                    专业：${entity.ZYMC }
                                                </div>
                                            </td>
                                            <td>
                                                ${entity.BJMC}
                                            </td>
                                            <td>
                                                ${entity.NOW_TERM}
                                            </td>
                                            <shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
                                                <td>
                                                    <div>
                                                        <span class="f18">${entity.SUM_GET_CREDITS }/</span>${entity.ZDBYXF }/${entity.ZXF }
                                                    </div>
                                                    <div class="gray9">
                                                        （已获得${entity.XF_BL }%）
                                                    </div>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${entity.SUM_GET_CREDITS >=entity.ZDBYXF}">
                                                            <span class="text-green">已满足</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-orange">不满足</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </shiro:lacksPermission>
                                            <td>
                                                <div>
                                                    <span class="f18">${entity.PASS_REC_COUNT }/</span>${entity.REC_COUNT }
                                                </div>
                                                <div class="gray9">
                                                    （已完成${entity.PASS_BL }%）
                                                </div>
                                            </td>
                                            <td>
                                                <div>
                                                    <span class="f18">${entity.PASS_REC_COUNT }/</span>${entity.REC_COUNT }
                                                </div>
                                                <div class="gray9">
                                                    （已完成${entity.PASS_BL }%）
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <tags:pagination page="${pageInfo}" paginationSize="5" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">

    function formatSeconds(value) {
        var theTime = parseInt(value);// 秒
        var theTime1 = 0;// 分
        var theTime2 = 0;// 小时
        if(theTime > 60) {
            theTime1 = parseInt(theTime/60);
            theTime = parseInt(theTime%60);
            if(theTime1 > 60) {
                theTime2 = parseInt(theTime1/60);
                theTime1 = parseInt(theTime1%60);
            }
        }
        var result = ""+parseInt(theTime)+"秒";
        if(theTime1 > 0) {
            result = ""+parseInt(theTime1)+"分"+result;
        }
        if(theTime2 > 0) {
            result = ""+parseInt(theTime2)+"小时"+result;
        }
        return result;
    }

    $(".login_time").each(function(){
        var login_time = $(this).html();
        login_time = formatSeconds(login_time);
        $(this).html(login_time);
    })
</script>
</body>
</html>