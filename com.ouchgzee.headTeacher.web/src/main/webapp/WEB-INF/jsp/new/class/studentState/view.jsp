<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员状态</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <h1>
        学员状态详情
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="${ctx}/home/class/studentState/list">学员状态</a></li>
        <li class="active">学员状态详情</li>
    </ol>
</section>
<section class="content">
    <div class="box">
        <div class="box-body">
            <div class="media pad15">
                <div class="media-left" style="padding-right:25px;">
                    <c:choose>
                        <c:when test="${not empty info.avatar}">
                            <img id ="headImgId" src="${info.avatar}" class="img-circle" alt="User Image" style="width: 128px; height: 128px;" onerror="this.src='${ctx }/static/images/headImg04.png'">
                        </c:when>
                        <c:otherwise>
                            <img id ="headImgId" src="${ctx }/static/images/headImg04.png" class="img-circle" alt="User Image" style="width: 128px; height: 128px;">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="media-body">
                    <h3 class="margin_t10">${info.xm}</h3>
                    <div class="row">
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>手机:</b> <span>${info.sjh}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>身份证:</b> <span>${info.sfzh}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>报读产品:</b> <span>${info.gjtSpecialty.zymc}（<dic:getLabel typeCode="TrainingLevel" code="${info.pycc}" />）</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>邮箱:</b> <span>${info.dzxx}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>地址:</b> <span><dic:getLabel typeCode="Province" code="${info.province}" /><dic:getLabel typeCode="${info.province}_City" code="${info.city}" /><dic:getLabel typeCode="${info.province}_${info.city}_Area" code="${info.area}" />${info.txdz}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="box ${learningState==0?'box-danger':'box-primary'}">
        <div class="box-header with-border">
            <div class="pull-right">
                <c:choose>
                    <c:when test="${learningState==0}"><button class="btn btn-danger btn-xs f14">未激活学习</button></c:when>
                    <c:otherwise><button class="btn btn-primary btn-xs f14">已激活学习</button></c:otherwise>
                </c:choose>
            </div>
            <h3 class="box-title">
                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-fw fa-chevron-circle-right reset-fa-chevron-circle"></i></button>
                <label>报读状态</label></h3>
        </div>
        <div class="box-body">
            <div class="row gray6">
                <div class="col-xs-5 col-xs-offset-1">
                    <label>报读时间:</label> <span><fmt:formatDate value="${info.gjtSignup.createdDt}" type="date" /></span>
                </div>
                <div class="col-xs-5">
                    <label>首次登录:</label> <span><fmt:formatDate value="${firstLogin}" type="both" /></span>
                </div>
            </div>
            <%--<div class="row gray6">
                <div class="col-xs-5 col-xs-offset-1">
                    <label>线上资料:</label> <span>已完善</span>
                </div>
                <div class="col-xs-5">
                    <label>线下资料:</label> <span>未完善</span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4 col-xs-offset-1">
                    <table class="table table-striped table-bordered reset-table-striped">
                        <tr>
                            <td>职业技能鉴定个人申请表</td>
                            <td><i class="fa fa-fw fa-check text-green"></i></td>
                        </tr>
                        <tr>
                            <td>学历证明复印件</td>
                            <td><i class="fa fa-fw fa-check text-green"></i></td>
                        </tr>
                        <tr>
                            <td>资格证书复印件</td>
                            <td><i class="fa fa-fw fa-check text-green"></i></td>
                        </tr>
                        <tr>
                            <td>专业论文（三级以上级别）</td>
                            <td><i class="fa fa-fw fa-check text-green"></i></td>
                        </tr>
                    </table>
                </div>
                <div class="col-xs-4 col-xs-offset-1">
                    <table class="table table-striped table-bordered reset-table-striped">
                        <tr>
                            <td>职业技能鉴定个人申请表</td>
                            <td><i class="fa fa-fw fa-check text-green"></i></td>
                        </tr>
                        <tr>
                            <td>学历证明复印件</td>
                            <td><i class="fa fa-fw fa-check text-green"></i></td>
                        </tr>
                        <tr>
                            <td class="text-red">资格证书复印件</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td class="text-red">专业论文（三级以上级别）</td>
                            <td></td>
                        </tr>
                    </table>
                </div>
            </div>--%>
        </div>
    </div>

    <div class="box ${feeStatus==1?'box-primary':'box-danger'}">
        <div class="box-header with-border">
            <div class="pull-right">
                <c:choose>
                    <c:when test="${feeStatus==1}"><button class="btn btn-primary btn-xs f14">已缴</button></c:when>
                    <c:otherwise><button class="btn btn-danger btn-xs f14">欠费</button></c:otherwise>
                </c:choose>
            </div>
            <h3 class="box-title">
                <button class="btn btn-box-tool nopadding" data-widget="collapse"><i class="fa fa-fw fa-chevron-circle-right reset-fa-chevron-circle"></i></button>
                <label>缴费状态</label></h3>
        </div>
        <div class="box-body">
            <div class="row gray6">
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                    <label>
                        <c:if test="${paymentDetail.GKXL_PAYMENT_TPYE=='A'}">全额缴费</c:if>
                        <c:if test="${paymentDetail.GKXL_PAYMENT_TPYE=='B'}">首年缴费</c:if>
                        <c:if test="${paymentDetail.GKXL_PAYMENT_TPYE=='C'}">分期付款</c:if>
                    </label>
                </div>
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
            </div>
            <c:if test="${fn:length(paymentDetail.ORDER_PAY_RECORD_LIST) > 0}">
                <div class="row">
                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                    <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                        <table class="table table-striped table-bordered reset-table-striped">
                            <tbody>
                            <c:forEach var="item" items="${paymentDetail.ORDER_PAY_RECORD_LIST}">
                                <tr>
                                    <td width="30%">
                                        <c:choose>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='A'}">全额</c:when>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='B'}">第一年学费</c:when>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='C'}">第二年学费</c:when>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='D'}">第三年学费</c:when>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='E'}">首付</c:when>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='F'}">分期</c:when>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='G'}">入学测试费</c:when>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='H'}">第二年教材费</c:when>
                                            <c:when test="${item.PAY_RECORD_TYPE_CODE=='I'}">第三年教材费</c:when>
                                        </c:choose>
                                        还款日:<fmt:formatDate value="${item.REC_DATE}" type="date" />
                                    </td>
                                    <td width="30%"><fmt:formatNumber value="${item.REC_AMT}" type="number" minFractionDigits="2" />元</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.PAY_STATUS=='Y'}">
                                                <span class="text-green pull-right">已缴费</span>
                                            </c:when>
                                            <c:when test="${item.PAY_STATUS=='N'&&item.PAY_OVERDUE_SIGN=='N'}">
                                                <span class="pull-right">未开始</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-red pull-right">欠费</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                </div>
            </c:if>
        </div>
    </div>

    <div class="box">
        <div class="box-header with-border">
            <div class="pull-right">
                <button class="btn btn-default btn-xs f14"><dic:getLabel typeCode="StudentNumberStatus" code="${info.xjzt}" /></button>
            </div>
            <h3 class="box-title">
                <button class="btn btn-box-tool nopadding" data-widget="collapse"><i class="fa fa-fw fa-chevron-circle-right reset-fa-chevron-circle"></i></button>
                <label>学籍状态</label></h3>
        </div>
        <div class="box-body gray6">
            <br>
            <div class="row">
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                    <table class="table table-striped table-bordered reset-table-striped">
                        <tbody>
                        <tr>
                            <td width="30%"><fmt:formatDate value="${info.gjtSignup.createdDt}" type="date" /></td>
                            <td>报读</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
            </div>
        </div>
    </div>

    <div class="box ${graduationStuInfo.receiveStatus==1||graduationStuInfo.receiveStatus==2?'box-success':'box-primary'}">
        <div class="box-header with-border">
            <div class="pull-right">
                <c:choose>
                    <c:when test="${graduationStuInfo.receiveStatus==1||graduationStuInfo.receiveStatus==2}"><button class="btn btn-success btn-xs f14">毕业</button></c:when>
                    <c:otherwise><button class="btn btn-primary btn-xs f14">未毕业</button></c:otherwise>
                </c:choose>
            </div>
            <h3 class="box-title">
                <button class="btn btn-box-tool nopadding" data-widget="collapse"><i class="fa fa-fw fa-chevron-circle-right reset-fa-chevron-circle"></i></button>
                <label>毕业状态</label></h3>
        </div>
        <div class="box-body gray6">
            <br>
            <div class="row">
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                    <table class="table table-striped table-bordered reset-table-striped">
                        <tbody>
                        <tr>
                            <td width="30%">当前已获学分 / 毕业要求学分</td>
                            <td><div class="pull-right"><span class="${yxxf==graduateStandard.byxf?'text-green':'text-red'}">${yxxf}</span> / ${graduateStandard.byxf}</div></td>
                        </tr>
                        <tr>
                            <td width="30%">当前学习学时 / 毕业要求学时</td>
                            <td><div class="pull-right"><span class="${termNum*0.5==graduateStandard.byxs?'text-green':'text-red'}">${termStudyNum*0.5}年</span> / ${graduateStandard.byxs}年</div></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
            </div>
        </div>
    </div>

    <div class="box nomargin">
        <div class="box-header with-border">
            <div class="pull-right">
                <%--<button class="btn btn-default btn-xs f14"></button>--%>
            </div>
            <h3 class="box-title">
                <button class="btn btn-box-tool nopadding" data-widget="collapse"><i class="fa fa-fw fa-chevron-circle-right reset-fa-chevron-circle"></i></button>
                <label>学位证书</label></h3>
        </div>
        <div class="box-body gray6">
            <br>
            <div class="row">
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
                <div class="col-xs-10 col-sm-10 col-md-10 col-lg-10">
                    <table class="table table-striped table-bordered reset-table-striped">
                        <thead>
                        <tr>
                            <th width="30%">已获得证书</th>
                            <th>获得时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${graduationStuInfo.receiveStatus==1||graduationStuInfo.receiveStatus==2}">
                            <tr>
                                <td>毕业证书</td>
                                <td><fmt:formatDate value="${graduationStuInfo.hbyrq}" type="date" /></td>
                            </tr>
                        </c:if>
                        <c:if test="${graduationStuInfo.receiveStatus==2}">
                            <tr>
                                <td>学士学位证书</td>
                                <td><fmt:formatDate value="${graduationStuInfo.hxwrq}" type="date" /></td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1"></div>
            </div>
        </div>
    </div>

</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
</html>
