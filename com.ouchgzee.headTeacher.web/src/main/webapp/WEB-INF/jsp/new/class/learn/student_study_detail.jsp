<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员学情</title>
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
        <li><a href="${ctx}/home/main#">学习管理</a></li>
        <li><a href="${ctx}/home/class/learn/list">学情分析</a></li>
        <li class="active">学员专业学情明细</li>
    </ol>
</section>
<section class="content">
    <div class="box">
        <div class="box-body">
            <div class="media pad">
                <div class="media-left" style="padding-right:25px;">
                    <c:choose>
                        <c:when test="${not empty result}">
                            <img id ="headImgId" src="${result.ZP}" class="img-circle" alt="User Image" style="width: 128px; height: 128px;" onerror="this.src='${ctx }/static/images/headImg04.png'">
                        </c:when>
                        <c:otherwise>
                            <img id ="headImgId" src="${ctx }/static/images/headImg04.png" class="img-circle" alt="User Image" style="width: 128px; height: 128px;">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="media-body">
                    <h3 class="margin_t10">
                        ${result.XM}
                        <small class="f14">（${result.SEX}）</small>
                    </h3>
                    <div class="row">
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>学号:</b> <span>${result.XH}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>手机:</b> <span>${result.SJH}</span>
                        </div>


                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>层次:</b> <span>${result.PYCC_NAME}</span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>年级:</b> <span>${result.NAME}</span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>学期:</b> <span>${result.GRADE_NAME}</span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>专业:</b> <span>${result.ZYMC}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="box-footer">
            <div class="row stu-info-status">
                <shiro:hasRole name="班主任">
                    <div class="col-sm-2 col-xs-6">
                        <div class="f24 text-center">${result.ZXF}</div>
                        <div class="text-center gray6">总学分</div>
                    </div>
                    <div class="col-sm-1 col-xs-6">
                        <div class="f24 text-center">${result.CREDITS_COUNT}</div>
                        <div class="text-center gray6">已获得学分</div>
                    </div>
                </shiro:hasRole>
                <div class="col-sm-1 col-xs-6">
                    <div class="f24 text-center">${result.COURSE_COUNT}</div>
                    <div class="text-center gray6">课程总数</div>
                </div>
                <div class="col-sm-1 col-xs-6">
                    <div class="f24 text-center">${result.PASS_COURSE}</div>
                    <div class="text-center gray6">已通过课程</div>
                </div>
                <div class="col-sm-1 col-xs-6">
                    <div class="f24 text-center">${result.UNPASS_COURSE}</div>
                    <div class="text-center gray6">未通过课程</div>
                </div>
                <div class="col-sm-1 col-xs-6">
                    <div class="f24 text-center">${result.LEARNING_COURSE}</div>
                    <div class="text-center gray6">学习中课程</div>
                </div>
                <div class="col-sm-1 col-xs-6">
                    <div class="f24 text-center">${result.REGISTER_COURSE}</div>
                    <div class="text-center gray6">登记中课程</div>
                </div>
                <div class="col-sm-1 col-xs-6">
                    <div class="f24 text-center">${result.LOGIN_TIMES}次</div>
                    <div class="text-center gray6">学习总次数</div>
                </div>
                <div class="col-sm-2 col-xs-6">
                    <div class="f24 text-center">${result.LOGIN_TIME}小时</div>
                    <div class="text-center gray6">学习总时长</div>
                </div>
                <div class="col-sm-1 col-xs-6">
                    <div class="f24 text-center">
                        <c:choose>
                            <c:when test="${result.IS_ONLINE eq 'Y'}">
                                <div class="text-green">在线</div>
                            </c:when>
                            <c:when test="${result.IS_ONLINE eq 'N'}"><div class="gray9">离线</div></c:when>
                            <c:otherwise><div class="gray9">离线</div></c:otherwise>
                        </c:choose>
                    </div>
                    <div class="text-center gray6">当前在线状态</div>
                </div>
            </div>
        </div>

    </div>

    <div class="panel panel-default margin_t10">
        <div class="panel-heading">
            <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button">
                <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
                <span class="margin-r-5">成绩详情</span>
            </h3>
        </div>
        <div id="info-box-3" class="collapse in">
            <div class="panel-body content-group">
                <div class="pad-l10 pad-r10">
                    <div class="table-responsive">
                        <table id="Merge_rows" class="table table-bordered table-striped table-cell-ver-mid text-center">
                            <thead>
                            <tr>
                                <th>学期</th>
                                <th>课程模块</th>
                                <td>课程代码</td>
                                <th>课程名称</th>
                                <shiro:hasRole name="班主任">
                                    <th>学分</th>
                                    <th>已获学分</th>
                                </shiro:hasRole>
                                <td>学习进度</td>
                                <shiro:hasRole name="班主任,班主任（有考试院校模式）">
                                    <th>学习成绩</th>
                                    <th>考试成绩</th>
                                    <th>总成绩</th>
                                </shiro:hasRole>
                                <th>状态</th>
                                <th>学习次数</th>
                                <th>学习时长</th>
                                <th>当前学习状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty infos && fn:length(infos) > 0}">
                                    <c:forEach items="${infos}" var="item">
                                        <tr>
                                            <td>${item.TERM_NAME} </td>
                                            <td>${item.KCLBM_NAME}</td>
                                            <td>${item.KCH}</td>
                                            <td>${item.KCMC}</td>
                                            <shiro:hasRole name="班主任">
                                                <td>${item.XF}</td>
                                                <td>${item.GET_CREDITS}</td>
                                            </shiro:hasRole>
                                            <td><c:if test="${empty item.SCHEDULE}">--</c:if><c:if test="${not empty item.SCHEDULE}">${item.SCHEDULE}%</c:if></td>
                                            <shiro:hasRole name="班主任,班主任（有考试院校模式）">
                                                <td>
                                                    <span <c:if test="${item.EXAM_STATE eq '0'}">class="text-red"</c:if>>${item.EXAM_SCORE}</span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.EXAM_STATE eq '0'}"><span class="text-red">${item.EXAM_SCORE1}</span> <br><span class="gray9"><c:if test="${not empty item.KSFS_NAME}">(${item.KSFS_NAME})</c:if></span></c:when>
                                                        <c:when test="${item.EXAM_STATE eq '1'}">${item.EXAM_SCORE1}<br><span class="gray9"><c:if test="${not empty item.KSFS_NAME}">(${item.KSFS_NAME})</c:if></span></c:when>
                                                        <c:when test="${item.EXAM_STATE eq '2'}">--</c:when>
                                                        <c:when test="${item.EXAM_STATE eq '3'}">${item.EXAM_SCORE1}<br><span class="gray9"><c:if test="${not empty item.KSFS_NAME}">(${item.KSFS_NAME})</c:if></span></c:when>
                                                        <c:otherwise>--</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.EXAM_STATE eq '0'}"><span class="text-red">${item.EXAM_SCORE2}</span><br><span class="gray9"><c:if test="${not empty item.XK_PERCENT}">(形考比例${item.XK_PERCENT}%)</c:if></span></c:when>
                                                        <c:when test="${item.EXAM_STATE eq '1'}">${item.EXAM_SCORE2}<br><span class="gray9"><c:if test="${not empty item.XK_PERCENT}">(形考比例${item.XK_PERCENT}%)</c:if></span></c:when>
                                                        <c:when test="${item.EXAM_STATE eq '2'}">--</c:when>
                                                        <c:when test="${item.EXAM_STATE eq '3'}">--</c:when>
                                                        <c:otherwise>--</c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </shiro:hasRole>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.EXAM_STATE eq '0'}"><span class="text-red">未通过</span></c:when>
                                                    <c:when test="${item.EXAM_STATE eq '1'}"><span class="text-green">已通过</span></c:when>
                                                    <c:when test="${empty item.LOGIN_COUNT or item.LOGIN_COUNT eq '0'}"><span class="gray9">未学习</span></c:when>
                                                    <c:when test="${item.EXAM_STATE eq '2'}"><span class="text-orange">学习中</span></c:when>
                                                    <c:when test="${item.EXAM_STATE eq '3'}"><span class="text-red">登记中</span></c:when>
                                                    <c:otherwise><span class="gray9">学习中</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty item.LOGIN_COUNT}">${item.LOGIN_COUNT}</c:when>
                                                    <c:otherwise>--</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty item.LOGIN_TIME}">${item.LOGIN_TIME}</c:when>
                                                    <c:otherwise>--</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty item.LOGIN_COUNT or item.LOGIN_COUNT eq '0'}"><span class="gray9">离线</span><br><span class="gray9">(从未学习)</span></c:when>
                                                    <c:when test="${item.IS_ONLINE eq 'N'}"><span class="gray9">离线</span><br><c:if test="${not empty item.LEFT_DATE}"><span class="gray9">(${item.LEFT_DATE}天未学习)</span></c:if></c:when>
                                                    <c:when test="${item.IS_ONLINE eq 'Y'}"><span class="text-green">在线</span><br></c:when>
                                                    <c:otherwise><span class="gray9">离线</span><br><span class="gray9">(从未学习)</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${not empty item.TERMCOURSE_ID}">
                                                    <a href="${ctx}/home/class/learn/courseCondition/${item.TERMCOURSE_ID}/${result.STUDENT_ID}/${item.COURSE_ID}" class="operion-item" data-toggle="tooltip" title="查看课程学情明细"><i class="fa fa-fw fa-view-more"></i></a>
                                                </c:if>
                                                <c:if test="${empty item.TERMCOURSE_ID}">--</c:if>
                                            </td>
                                        </tr>
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
                </div>
            </div>
        </div>
    </div>
    <shiro:hasRole name="班主任">
        <div class="panel panel-default margin_t10">
            <div class="panel-heading">
                <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button">
                    <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
                    <span class="margin-r-5">学分详情</span>
                </h3>
            </div>
            <div id="info-box-4" class="collapse in">
                <div class="panel-body content-group">
                    <div class="pad-l10 pad-r10">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped vertical-mid text-center margin_b10 table-font">
                                <thead>
                                <tr>
                                    <th>课程模块</th>
                                    <th>总学分</th>
                                    <th>最低毕业学分</th>
                                    <th>已获学分</th>
                                    <th>课程总数</th>
                                    <th>通过课程数</th>
                                    <th>未通过课程数</th>
                                    <th>状态</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${studentand }" var="modulelist">
                                    <tr>
                                        <td>${modulelist.NAME }</td>
                                        <td>${modulelist.XF_COUNT }</td>
                                        <td>${modulelist.LIMIT_SCORE }</td>
                                        <td>${modulelist.GET_POINT }</td>
                                        <td>${modulelist.COUNT_COURSE }</td>
                                        <td>${modulelist.PASS_COURSE }</td>
                                        <td>${modulelist.UN_PASS_COURSE }</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${modulelist.GET_POINT>=modulelist.LIMIT_SCORE }">
                                                    <span class="text-green">已达标</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-red">未达标</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div class="text-orange">注：为了能够顺利毕业，以上各课程模块所得学分都必须满足最低毕业学分要求</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </shiro:hasRole>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<!--合并第一行重复项 -->
<script type="application/javascript">
    $(function autoRowSpan() {
        var lastValue = " ";
        var value = "";
        var pos = 1;
        var row= 1;
        var col = 0;
        if(Merge_rows.rows.length==2)return;
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
    });
  
    
</script>
<!--合并第一行重复项 -->
<script type="application/javascript">
/*
    (function($){

        var COUNTLONGINCOUNT = 0, COUNTUSERTOTALTIME = 0;
        $('.LONGINCOUNT').each(function(i, item) {
            var val = $(this).attr('val');
            if(val != '') {
                COUNTLONGINCOUNT = COUNTLONGINCOUNT + parseInt($(this).attr('val'));
            }
        });
        $('.USERTOTALTIME').each(function(i, item) {
            var val = $(this).attr('val');
            if(val != '') {
                COUNTUSERTOTALTIME = COUNTUSERTOTALTIME + parseInt($(this).attr('val'));
            }
        });
        $('.COUNTLONGINCOUNT').text(COUNTLONGINCOUNT);
        $('.COUNTUSERTOTALTIME').text(COUNTUSERTOTALTIME);

    })(jQuery);
*/
</script>
</body>
</html>
