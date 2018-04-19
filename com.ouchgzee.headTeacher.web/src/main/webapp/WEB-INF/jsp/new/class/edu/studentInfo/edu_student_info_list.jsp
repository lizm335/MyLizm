<%@ page import="java.util.Date" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员管理</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>

    <script type="text/javascript">
        function choiceXJ(flag){
            //$('#listForm .btn-reset').trigger('click');
            $("#signupAuditState").val('');
            $("#perfectStatus").val('');
            $("#isEnteringSchool").val('');
            $("#signupAuditState").val(flag);
            $("#listForm").submit();
        }
        function choicePs(flag){
            //$('#listForm .btn-reset').trigger('click');
            $("#signupAuditState").val('');
            $("#perfectStatus").val('');
            $("#isEnteringSchool").val('');
            $("#perfectStatus").val(flag);
            $("#listForm").submit();
        }
        function choiceEs(flag){
            //$('#listForm .btn-reset').trigger('click');
            $("#signupAuditState").val('');
            $("#perfectStatus").val('');
            $("#isEnteringSchool").val('');
            $("#isEnteringSchool").val(flag);
            $("#listForm").submit();
        }
    </script>
</head>
<body class="inner-page-body">
    <section class="content-header">
        <div class="pull-left">
            您所在位置：
        </div>
        <ol class="breadcrumb">
            <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
            <li><a href="#">教务管理</a></li>
            <li class="active">学员管理</li>
        </ol>
    </section>

    <section class="content">
        <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
        <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

<div class="nav-tabs-custom no-margin">
    <ul class="nav nav-tabs nav-tabs-lg">
        <li class="active"><a href="#tab_top_1" >学籍信息</a></li>
        <li><a href="${ctx}/home/class/rollChange/list" >学籍异动</a></li>
        <!--<li><a href="#tab_top_3" >统计分析</a></li>-->
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="tab_top_1">
            <form class="form-horizontal" id="listForm" action="list.html" method="post">
            <input id="signupAuditState" type="hidden" name="search_EQ_signupAuditState" value="${param.search_EQ_signupAuditState}">
            <input id="perfectStatus" type="hidden" name="search_EQ_perfectStatus" value="${param.search_EQ_perfectStatus}">
            <input id="isEnteringSchool" type="hidden" name="search_EQ_isEnteringSchool" value="${param.search_EQ_isEnteringSchool}">
            <div class="box box-border no-shadow">
                <div class="box-body">
                    <div class="row pad-t15">
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">学号</label>
                                <div class="col-sm-9">
                                    <input class="form-control" type="text" name="search_EQ_xh" value="${param['search_EQ_xh']}">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">姓名</label>
                                <div class="col-sm-9">
                                    <input class="form-control" type="text" name="search_LIKE_xm" value="${param['search_LIKE_xm']}">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">学习中心</label>
                                <div class="col-sm-9">
                                    <select name="search_EQ_gjtStudyCenter.id" class="form-control selectpicker show-tick" <%--multiple--%> data-size="10" data-live-search="true" data-style="btn-default no-bg flat">
                                        <option value="">- 请选择 -</option>
                                        <c:forEach var="item" items="${studyCenterMap}">
                                            <option value="${item.key}" <c:if test="${param['search_EQ_gjtStudyCenter.id']==item.key}">selected</c:if>>${item.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">专业</label>
                                <div class="col-sm-9">
                                    <select name="search_EQ_gjtSpecialty.specialtyId" class="form-control selectpicker show-tick" <%--multiple--%> data-size="10" data-live-search="true" data-style="btn-default no-bg flat">
                                        <option value="">- 请选择 -</option>
                                        <c:forEach var="item" items="${specialtyMap}">
                                            <option value="${item.key}" <c:if test="${param['search_EQ_gjtSpecialty.specialtyId']==item.key}">selected</c:if>>${item.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">年级</label>
                                <div class="col-sm-9">
                                    <select name="search_EQ_gjtSignup.gjtEnrollBatch.gjtGrade.gradeId" class="form-control selectpicker show-tick" <%--multiple--%> data-size="10" data-live-search="true" data-style="btn-default no-bg flat">
                                        <option value="">- 请选择 -</option>
                                        <c:forEach var="item" items="${gradeMap}">
                                            <option value="${item.key}" <c:if test="${param['search_EQ_gjtSignup.gjtEnrollBatch.gjtGrade.gradeId']==item.key}">selected</c:if>>${item.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">层次</label>
                                <div class="col-sm-9">
                                    <select  name="search_EQ_pycc"  class="selectpicker show-tick form-control"	data-size="10" data-live-search="true" data-style="btn-default no-bg flat">
                                        <option value="" selected="selected">请选择</option>
                                        <c:forEach items="${pyccMap}" var="map">
                                            <option value="${map.key}" <c:if test="${map.key==param['search_EQ_pycc']}">selected='selected'</c:if>>${map.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-xs-6">
                            <div class="form-group">
                                <label class="control-label col-sm-3 text-nowrap">学籍状态</label>
                                <div class="col-sm-9">
                                    <select  name="search_EQ_xjzt"  class="selectpicker show-tick form-control"	data-size="10" data-live-search="true" data-style="btn-default no-bg flat">
                                        <option value="" selected="selected">请选择</option>
                                        <c:forEach items="${xjzzMap}" var="map">
                                            <option value="${map.key}" <c:if test="${map.key==param['search_EQ_xjzt']}">selected='selected'</c:if>>${map.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!-- /.box-body -->
                <div class="box-footer">
                    <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button></div>
                    <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
                </div><!-- /.box-footer-->
            </div>
        </form>

        <div class="box box-border no-shadow margin-bottom-none">
            <div class="box-header with-border">
                <h3 class="box-title pad-t5">学员列表</h3>
                <div class="fr">
                    <%--<button class="btn min-width-90px btn-success btn-add multi-entering"><i class="fa fa-fw fa-confirm-stu"></i> 批量入学确认</button>--%>
                    <a href="javascript:void(0);" data-role="export" class="btn btn-default btn-sm btn-export2"><i class="fa fa-fw fa-sign-out"></i> 导出学员信息</a>
                </div>
            </div>
            <div class="box-body">
                <div class="filter-tabs clearfix">
                    <ul class="list-unstyled">
                        <li <c:if test="${empty param.search_EQ_signupAuditState && empty param.search_EQ_perfectStatus && empty param.search_EQ_isEnteringSchool}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${not empty countAuditStateMap[''] ? countAuditStateMap[''] : 0})</li>
                        <li <c:if test="${param.search_EQ_isEnteringSchool == '0'}">class="actived"</c:if> value="0" onclick="choiceEs('0')">未确认入学(${not empty countEnteringSchoolMap['0'] ? countEnteringSchoolMap['0'] : 0})</li>
                        <li <c:if test="${param.search_EQ_perfectStatus == '0'}">class="actived"</c:if> value="0" onclick="choicePs('0')">未完善资料(${not empty countPerfectStatusMap['0'] ? countPerfectStatusMap['0'] : 0})</li>
                        <li <c:if test="${param.search_EQ_perfectStatus == '1'}">class="actived"</c:if> value="1" onclick="choicePs('1')">已完善资料(${not empty countPerfectStatusMap['1'] ? countPerfectStatusMap['1'] : 0})</li>
                        <li <c:if test="${param.search_EQ_signupAuditState == '4'}">class="actived"</c:if> value="4" onclick="choiceXJ('4')">未审核(${not empty countAuditStateMap['4'] ? countAuditStateMap['4'] : 0})</li>
                        <li <c:if test="${param.search_EQ_signupAuditState == '3'}">class="actived"</c:if> value="3" onclick="choiceXJ('3')">待审核(${not empty countAuditStateMap['3'] ? countAuditStateMap['3'] : 0})</li>
                        <li <c:if test="${param.search_EQ_signupAuditState == '2'}">class="actived"</c:if> value="2" onclick="choiceXJ('2')">审核中(${not empty countAuditStateMap['2'] ? countAuditStateMap['2'] : 0})</li>
                        <li <c:if test="${param.search_EQ_signupAuditState == '0'}">class="actived"</c:if> value="0" onclick="choiceXJ('0')">审核不通过(${not empty countAuditStateMap['0'] ? countAuditStateMap['0'] : 0})</li>
                        <li <c:if test="${param.search_EQ_signupAuditState == '1'}">class="actived"</c:if> value="1" onclick="choiceXJ('1')">审核通过(${not empty countAuditStateMap['1'] ? countAuditStateMap['1'] : 0})</li>
						<li <c:if test="${param.search_EQ_signupAuditState == '5'}">class="actived"</c:if> value="5" onclick="choiceXJ('5')">已退费，无需审核(${not empty countAuditStateMap['5'] ? countAuditStateMap['5'] : 0})</li>
                    </ul>
                </div>
                <div class="table-responsive">
                    <table class="table table-bordered table-striped table-cell-ver-mid">
                        <thead>
                        <tr>
                            <%--<th><input type="checkbox" class="select-all"></th>--%>
                            <th class="text-center">头像</th>
                            <th class="text-center">个人信息</th>
                            <th class="text-center">报读信息</th>
                            <th class="text-center">学员类型</th>
                            <th class="text-center">学籍状态</th>
                            <th class="text-center">入学确认</th>
                            <th class="text-center">资料状态</th>
                            <th class="text-center">资料审批</th>
                            <th class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty infos && infos.numberOfElements > 0}">
                                <c:forEach items="${infos.content}" var="info">
                                    <c:if test="${not empty info}">
                                        <tr>
                                            <%--<td>
                                                <input type="checkbox" name="ids"
                                                       data-id="${info.studentId}" data-name="check-id"
                                                       value="${info.studentId}">
                                            </td>--%>
                                            <td class="text-center">
                                                <img src="${not empty info.avatar ? info.avatar : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>
                                                <c:if test="${empty info.avatar}">
                                                    <p class="gray9">未提交</p>
                                                </c:if>
                                            </td>
                                            <td>
                                                姓名：${info.xm}<br/>
                                                学号：${info.xh}<br/>
                                                手机号：${info.sjh}<br/>
                                                身份证：${info.sfzh}<br/>
                                                所属单位：${info.scCo}<br/>
                                                学习中心：${info.xxzxName}
                                            </td>
                                            <td>
                                                层次：<dic:getLabel typeCode="TrainingLevel" code="${info.pycc }" /><br/>
                                                年级：${info.yearName}<br/>
                                                学期：${info.gradeName}<br/>
                                                专业：${info.specialtyName}
                                            </td>
                                            <td class="text-center">
                                                <dic:getLabel typeCode="USER_TYPE" code="${info.userType}" />
                                            </td>
                                            <td class="text-center">
                                                <dic:getLabel typeCode="StudentNumberStatus" code="${info.xjzt}" />
                                            </td>
                                            <td class="text-center">${info.isEnteringSchool=='1'?'<span class="text-green">已确认</span>':'<span class="text-orange">未确认</span>'}</td>
                                            <td class="${info.perfectStatus==1?'text-green':'text-red'} text-center">
                                                ${info.perfectStatus==1?'已完善':'未完善'}
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
													<c:when test="${info.xjzt=='5'}"><span class="text-orange">已退费，无需审核</span></c:when>
                                                    <c:when test="${info.signupAuditState=='1'}"><span class="text-green">审核通过</span></c:when>
                                                    <c:when test="${info.signupAuditState=='0'}"><span class="text-red">审核不通过<c:if test="${not empty info.flowAuditOperatorRole}"><br/>(${info.flowAuditOperatorRole-1}/3)</span></c:if></span></c:when>
                                                    <c:otherwise>
                                                        <c:if test="${empty info.flowAuditOperatorRole}"><span class="text-orange">未审核</span></c:if>
                                                        <c:if test="${not empty info.flowAuditOperatorRole}">
                                                            <c:if test="${info.flowAuditOperatorRole==2}">
                                                                <span class="text-orange">待审核</span>
                                                            </c:if>
                                                            <c:if test="${info.flowAuditOperatorRole!=2}">
                                                                <span class="text-orange">审核中<br/>(${info.flowAuditOperatorRole-1}/3)</span>
                                                            </c:if>
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <div class="data-operion">
                                                    <a href="view/${info.studentId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                                    <c:choose>
                                                        <c:when test="${info.isEnteringSchool=='1'}">
                                                        <a href="javascript:void(0);" val="${info.studentId}" class="operion-item" data-toggle="tooltip" title="查看入学确认记录" data-role="view-entrance-record"><i class="fa fa-fw fa-search-plus"></i></a>
                                                        </c:when>
                                                        <c:otherwise>
                                                        <a href="javascript:void(0);" val="${info.studentId}" class="operion-item" data-toggle="tooltip" title="确认入学" data-role="confirm-entrance"><i class="fa fa-fw fa-confirm-stu"></i></a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <shiro:hasPermission name="/home/class/studentInfo/list$reset">
                                                    <a href="javascript:void(0);" val="${info.studentId}" class="operion-item operion-reset" data-toggle="tooltip" title="重置密码"><i class="fa fa-fw fa-psw-reset"></i></a>
                                                    </shiro:hasPermission>

                                                    <shiro:hasPermission name="/home/class/studentInfo/list$resetPassword">
                                                        <a href="javascript:void(0);" val="${info.studentId}" class="operion-item operion-resetNew" data-toggle="tooltip" title="重置密码(办学模式)"><i class="fa fa-fw fa-psw-reset"></i></a>
                                                    </shiro:hasPermission>


                                                    <shiro:hasPermission name="/home/class/studentInfo/list$simulation">
                                                    <a href="${ctx}/home/class/edu/roll/simulationLogin?studentId=${info.studentId}" target="_blank" class="operion-item operion-login" data-toggle="tooltip" title="模拟登录"><i class="fa fa-fw fa-simulated-login"></i></a>
                                                    </shiro:hasPermission>

                                                    <shiro:hasPermission name="/home/class/studentInfo/list$analogLogin">
                                                        <a href="${ctx}/home/class/studentInfo/analogLogin?synUrl=${info.synUrl}&id_card=${info.sfzh}&phone=${info.sjh}&account=${info.xh}&organ=${info.xxCode}" class="operion-item operion-login" data-toggle="tooltip" title="模拟登录个人中心" target="_blank" data-role="data-analogLogin"><i class="fa fa-fw fa-share"></i></a>
                                                    </shiro:hasPermission>


                                                    <%--<span class="dropdown operion-item operion-more">
                                                      <a class="dropdown-toggle" data-toggle="dropdown" href="#"> 更多 <i class="fa fa-fw fa-caret-down"></i> </a>
                                                      <ul class="dropdown-menu">
                                                        <li role="presentation"><a role="menuitem" tabindex="-1" href="#">选项一</a></li>
                                                        <li role="presentation"><a role="menuitem" tabindex="-1" href="#">选项二</a></li>
                                                      </ul>
                                                    </span>--%>
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
        </div>
    </div>
</div>

    </section>

    <!-- 确认入学模态框 -->
    <div id="enterModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog">
            <iframe id="enterIFrame" src="" frameborder="0" scrolling="auto" allowtransparency="true" style="height: 310px;"></iframe>
        </div>
    </div>

    <%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

    <%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
    <script type="text/javascript" src="${ctx}/static/plugins/custom-model/custom-model.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/edu/studentInfo/edu_student_info_list.js"></script>
    <script type="text/javascript">
        function smsCallback() {
            window.open('${ctx}/home/class/studentInfo/exportInfo?sortType=${sortType}&${searchParams}');
        }
        $(function () {
            // 导出
            $('[data-role="export"]').click(function(event) {
                event.preventDefault();
                var self=this;
                $.mydialog({
                    id:'export',
                    width:600,
                    height:415,
                    zIndex:11000,
                    content: 'url:'+'${ctx }/home/common/toSmsValidateCode?totalNum=${infos.totalElements}'
                });
            });

            $('[data-role="confirm-entrance"],[data-role="view-entrance-record"]').click(function(event) {
                event.preventDefault();
                var self=this;
                $.mydialog({
                    id:'confirm-entrance',
                    width:700,
                    height:500,
                    zIndex:11000,
                    content: 'url:'+'${ctx }/home/class/studentInfo/toEnteringSchool?studentId=' + $(this).attr('val')
                });
                /*$('#enterIFrame').attr('src', '${ctx }/home/class/studentInfo/toEnteringSchool?studentId=' + $(this).attr('val'));
                $('#enterModal').modal('show');*/
            })
        });
    </script>
</body>
</html>
