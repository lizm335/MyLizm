<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 5/12/2017
  Time: 3:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.gzedu.xlims.common.AppConfig" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>办学组织平台 - 学员学情</title>
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">学习管理</a></li>
        <li><a href="#">学情分析</a></li>
        <li><a href="#">学情分析</a></li>
        <li class="active">学员课程学情明细</li>
    </ol>
</section>
<section class="content">
    <div class="box">
        <div class="box-body">
            <div class="media pad">
                <div class="media-left" style="padding-right:25px;">
                    <img src="${ctx}/ouchgzee_com/platform/xllms_css/dist/img/user7-128x128.jpg" class="img-circle" style="width:112px;height:112px;" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'">
                </div>
                <div class="media-body">
                    <h3 class="margin_t10">
                        ${result.XM}
                        <small class="f14">（${result.SEX}）</small>
                    </h3>
                    <div class="row">
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>学号:</b> <span>${result.XH}</span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>手机:</b>
                            <span>
	                            <shiro:hasPermission name="/personal/index$privacyJurisdiction">
									${result.SJH}
								</shiro:hasPermission>
								<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
									<c:if test="${not empty result.SJH }">
									${fn:substring(result.SJH,0, 3)}******${fn:substring(result.SJH,8, (result.SJH).length())}
									</c:if>
								</shiro:lacksPermission>
	                            
                            </span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>专业层次:</b> <span>${result.PYCC_NAME}</span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>入学年级:</b> <span>${result.NAME}</span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <b>入学学期:</b> <span>${result.GRADE_NAME}</span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
                            <%--<shiro:hasPermission /studymanage/getCourseStudyList$schoolModel--%>
                            <b>报读专业:</b> <span>${result.ZYMC}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="box-footer">
            <div class="row stu-info-status">
                <div class="col-sm-2">
                    <div class="f24 text-center">${result.LOGIN_DT_BALANCE}<small>天</small> </div>
                    <div class="text-center gray6">未学习</div>
                </div>
                <div class="col-sm-2">
                    <div class="f24 text-center">${result.MY_PROGRESS}<small>%</small> </div>
                    <div class="text-center gray6">学习进度</div>
                </div>
                <div class="col-sm-2">
                    <div class="f24 text-center">${result.MY_POINT} </div>
                    <div class="text-center gray6">学习成绩</div>
                </div>
                <div class="col-sm-2">
                    <div class="f24 text-center">${result.ONLINE_COUNT} </div>
                    <div class="text-center gray6">学习次数</div>
                </div>
                <div class="col-sm-2">
                    <div class="f24 text-center">${result.ONLINE_TIME}<small>小时</small> </div>
                    <div class="text-center gray6">学习时长</div>
                </div>
                <div class="col-sm-2">
                    <div class="text-center gray6"><button class="btn btn-default btn-lg margin10" onclick="stuLogin(this)">模拟登录</button></div>
                </div>
            </div>
        </div>
    </div>
    <form action="<%=AppConfig.getProperty("oclassUrl","http://oclass.oucnet.cn")%>/book/index/student/urlLogin.do?${result.URL}" target="simulate" name="stuLoginForm" id="stuLoginForm" method="post">

    </form>
    <div class="box margin-bottom-none no-border no-shadow">
        <iframe name="rel-Iframe" src='<%=AppConfig.getProperty("oclassUrl","http://oclass.oucnet.cn")%>/opi/student/goStudDetailIndex.do?formMap.STUD_ID=${result.STUDENT_ID}&formMap.TERMCOURSE_ID=${result.TERMCOURSE_ID}&formMap.CLASS_ID=${result.CLASS_ID}' width="100%" height="100%" style="height:500px;" frameborder="0" scrolling="auto" allowtransparency="true"></iframe>
    </div>
</section>
<script>
    //单点登录
    function stuLogin(e){

        document.stuLoginForm.submit();
    }
</script>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>

