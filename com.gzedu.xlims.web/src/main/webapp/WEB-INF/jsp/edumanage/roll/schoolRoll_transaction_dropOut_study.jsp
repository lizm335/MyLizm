<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
 <meta charset="utf-8">
 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>退学</title>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<link rel="stylesheet" href="${ctx}/static/dist/css/signup-info.css">
</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学籍管理</a></li>
		<li><a href="#">学籍资料</a></li>
		<li class="active">学籍资料明细</li>
	</ol>
</section>
<section class="content">
	<div class="box">
    <div class="box-body">
      <div class="media pad">
        <div class="media-left" style="padding-right:25px;">
          <img src="${not empty item.gjtStudentInfo.avatar ? item.gjtStudentInfo.avatar : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>
          <a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
            <i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
            <span class="gray9">
            	交流
            </span>
          </a>
        </div>
        <div class="media-body">
          <h3 class="margin_t10">
            	${item.gjtStudentInfo.xm}
            <small class="f14">(<dic:getLabel typeCode="Sex" code="${item.gjtStudentInfo.xbm}"/>)</small>
          </h3>
          <div class="row">
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>学号:</b> <span>${item.gjtStudentInfo.xh}</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>手机:</b>
              <shiro:hasPermission name="/personal/index$privacyJurisdiction">
					${item.gjtStudentInfo.sjh}
				</shiro:hasPermission>
				<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
					<c:if test="${not empty item.gjtStudentInfo.sjh }">
					${fn:substring(item.gjtStudentInfo.sjh,0, 3)}******${fn:substring(item.gjtStudentInfo.sjh,8, (item.gjtStudentInfo.sjh).length())}
					</c:if>
				</shiro:lacksPermission>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>层次:</b> <span><dic:getLabel typeCode="TrainingLevel" code="${item.gjtStudentInfo.pycc }" /></span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>年级:</b> <span>${item.gjtStudentInfo.gjtGrade.gjtYear.name}</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>学期:</b> <span>${item.gjtStudentInfo.gjtGrade.gradeName}</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>学习中心:</b> <span>${item.gjtStudentInfo.gjtStudyCenter.gjtOrg.orgName}</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>专业:</b> <span>${item.gjtStudentInfo.gjtSpecialty.zymc}</span>
            </div>
          </div>
        </div>
      </div>
      
    </div>
    <div class="box-footer">
      <div class="row stu-info-status">
        <div class="col-sm-2_5 col-xs-6">
          <div class="f24 text-center"><dic:getLabel typeCode="StudentNumberStatus" code="${item.gjtStudentInfo.xjzt}"/></div>
          <div class="text-center gray6">学籍状态</div>
        </div>
        <div class="col-sm-2_5 col-xs-6">
          <div class="f24 text-center"><dic:getLabel typeCode="USER_TYPE" code="${item.gjtStudentInfo.userType}"/></div>
          <div class="text-center gray6">学员类型</div>
        </div>
        <div class="col-sm-2_5 col-xs-6">
          <c:choose>
         	 <c:when test="${item.transactionType==1}"><div class="f24 text-center">转专业</div></c:when>
         	 <c:when test="${item.transactionType==2}"><div class="f24 text-center">休学</div></c:when>
         	 <c:when test="${item.transactionType==3}"><div class="f24 text-center">复学</div></c:when>
         	 <c:when test="${item.transactionType==4}"><div class="f24 text-center">退学</div></c:when>
         	 <c:when test="${item.transactionType==5}"><div class="f24 text-center">信息更正</div></c:when>
         	 <c:otherwise>
         	 	 <div class="f24 text-center">转学</div>
         	 </c:otherwise>                  
         </c:choose>
          <div class="text-center gray6">异动类型</div>
        </div>
        <div class="col-sm-2_5 col-xs-6 no-margin no-pad-top">         
          <c:choose>
              <c:when test="${item.transactionStatus==3 && item.auditOperatorRole == 4}">
              	<div class="f24 text-center text-orange line-height-1em">
            		劝学中<div class="gray9 f14">（学习中心）</div>
          		</div>
              </c:when>
              <c:when test="${item.transactionStatus==3 && item.auditOperatorRole == 2}">
                <div class="f24 text-center text-orange line-height-1em">
            		劝学中<div class="gray9 f14">（学生事务部）</div>
          		</div>
              </c:when>
              <c:when test="${item.transactionStatus==3 && item.auditOperatorRole == 5}">
            	<div class="f24 text-center text-orange line-height-1em">
            		劝学中<div class="gray9 f14">（学籍科）</div>
          		</div>
              </c:when>
              <c:when test="${item.transactionStatus==6 && item.auditOperatorRole == 6}">             
              	 <div class="f24 text-center text-orange line-height-1em">
            		待核算<div class="gray9 f14">（财务服务部）</div>
          		</div>
              </c:when>
              <c:when test="${item.transactionStatus==8 && item.auditOperatorRole == 1}">             	
              	<div class="f24 text-center text-orange line-height-1em">
            		待确认<div class="gray9 f14">（学员）</div>
          		</div>
              </c:when>
              <c:when test="${item.transactionStatus==8 && item.auditOperatorRole == 0}">           	 
            	<div class="f24 text-center text-orange line-height-1em">
            		待处理<div class="gray9 f14">（院长）</div>
          		</div>
              </c:when>
              <c:when test="${item.transactionStatus==10 && item.auditOperatorRole == 7}">             	      
              	<div class="f24 text-center text-orange line-height-1em">
            		  待登记<div class="gray9 f14">（财务）</div>
          		</div>
              </c:when>
              <c:when test="${item.transactionStatus==8 && item.auditOperatorRole == 5}">             	
              	<div class="f24 text-center text-orange line-height-1em">
            		 待处理<div class="gray9 f14">（学籍科）</div>
          		</div>
              </c:when>
              <c:when test="${item.transactionStatus==12}">
              		已完成
              </c:when>
              <c:otherwise>
              <div class="f24 text-center text-orange line-height-1em">
            		 撤销退学
          		</div>
              </c:otherwise> 
             </c:choose>
          <div class="text-center gray6">退学状态</div>
        </div>
        <div class="col-sm-2_5 col-xs-6 text-center">
          <button type="button" class="btn btn-default btn-sm margin_t10">
            <i class="fa fa-simulated-login f20 vertical-middle"></i>
            	
             <a href="${ctx}/edumanage/roll/simulation.html?id=${item.studentId}" target="_blank">模拟登录</a>
          </button>        
        </div>
      </div>
    </div>
  </div>
 	
  <div class="box margin-bottom-none">
    <form id="inputForm" role="form" action="${ctx}/edumanage/rollTrans/dropOutStudy/auditDropOutStudy.html" method="post">
  	<input type="hidden" name="action" value="${action}"/>
   	<input type="hidden" name="transactionId" value="${item.transactionId}"/>
   	<input type="hidden" name="studentId" value="${item.studentId}"/>
   	<input type="hidden" name="auditState" value=""/>
    <div class="box-body">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-1" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">银行卡信息</span>
          </h3>
        </div>
        <div id="info-box-1" class="collapse in">
          <div class="panel-body">
              <table class="table-gray-th text-center">
                <colgroup>
                  <col width="12%"></col>
                  <col width="21%"></col>

                  <col width="12%"></col>
                  <col width="21%"></col>

                  <col width="12%"></col>
                  <col width="22%"></col>
                </colgroup>
                <tbody>
                  <tr>
                    <th>退费方式</th>
                    <td colspan="5">转账</td>
                  </tr>
                  <tr>
                    <th>银行卡号</th>
                    <td>${transactionContent.bankCardNumber}</td>

                    <th>帐户名</th>
                    <td>${transactionContent.accountName}</td>

                    <th>开户银行</th>
                    <td>${transactionContent.openBank}</td>
                  </tr>
                </tbody>
              </table>
          </div>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <button type="button" class="btn btn-warning btn-xs pull-right min-width-90px download">下载</button>
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">退学申请表</span>
          </h3>
        </div>
        <div id="info-box-3" class="collapse in">

          <div class="panel-body">
            <table class="table-gray-th text-center">
              <colgroup>
                <col width="10%"></col>
                <col width="15%"></col>

                <col width="10%"></col>
                <col width="15%"></col>

                <col width="10%"></col>
                <col width="15%"></col>

                <col width="10%"></col>
                <col width="15%"></col>
              </colgroup>
              <tbody>
                <tr>
                  <th>姓名</th>
                  <td>${item.gjtStudentInfo.xm}</td>

                  <th>性别</th>
                  <td><dic:getLabel typeCode="Sex" code="${item.gjtStudentInfo.xbm}"/></td>

                  <th>班号</th>
                  <td>${item.gjtStudentInfo.bh}</td>

                  <th>学号</th>
                  <td>${item.gjtStudentInfo.xh}</td>
                </tr>

                <tr>
                  <th>学生类别</th>
                  <td><dic:getLabel typeCode="USER_TYPE" code="${item.gjtStudentInfo.userType}"/></td>

                  <th>学习层次</th>
                  <td><dic:getLabel typeCode="TrainingLevel" code="${item.gjtStudentInfo.pycc }"/></td>

                  <th>专业</th>
                  <td>${item.gjtStudentInfo.gjtSpecialty.zymc}</td>

                  <th>入学时间</th>
                  <td>${item.gjtStudentInfo.gjtGrade.gradeName}</td>
                </tr>
                <tr>
                  <th>退学原因</th>
                  <td colspan="7">
                      <div class="text-left">
                      	${transactionContent.cause}
                      </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">签名确认</span>
          </h3>
        </div>
        <div id="info-box-3" class="collapse in">
          <div class="panel-body content-group">
            <div class="cnt-box-body">
              <div class="text-center pad15">
               	<img src="${transactionContent['sign'] }" alt="User Image">
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">审核流程</span>
          </h3>
        </div>
        
        <div id="info-box-4" class="collapse in">
          <div class="panel-body">
            <div class="approval-list clearfix">
             <c:forEach var="transAudit" items="${transAuditList}" varStatus="s">
              <c:if test="${transAudit.auditOperatorRole==-1}">
                <dl class="approval-item">
                    <dt class="clearfix">
                      <b class="a-tit gray6">${transAudit.gjtSchoolRollTran.gjtStudentInfo.xm}（学员）</b>
                      <span class="gray9 text-no-bold f12"><fmt:formatDate value="${transAudit.auditDt}" type="both"/></span>
                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                      <label class="state-lb">学员申请</label>
                    </dt>
                </dl>
               </c:if>
               <c:if test="${transAudit.auditOperatorRole!=-1}">
               <c:choose>
               <c:when test="${transAudit.auditOperatorRole eq 4 && transAudit.auditState==3}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix">
                      <b class="a-tit gray6">学习中心劝学</b>
                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                      <label class="state-lb">劝学中</label>
                    </dt>
                </dl>
				</c:when>
				<c:when test="${transAudit.auditOperatorRole eq 4 && transAudit.auditState==5}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-green">                     
                      <b class="a-tit">学习中心劝学成功</b>
                      <span class="fa fa-fw fa-check-circle"></span>
                      <label class="state-lb">劝学成功</label> 
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                          	劝学情况： <br>
				              ${transAudit.auditContent}
                          </div>
                          <div class="text-right gray9">
                            	学习中心：${transAudit.auditOperator}<br>
                            <fmt:formatDate value="${transAudit.auditDt}"/>
                          </div>
                      </div>
                    </dd>
                </dl>
                </c:when>
                <c:when test="${transAudit.auditOperatorRole eq 4 && transAudit.auditState==4}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-red">                     
                      <b class="a-tit">学习中心劝学失败</b>
                      <span class="fa fa-fw fa-times-circle"></span>
                      <label class="state-lb">劝学失败</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                            	劝学情况： <br>
                           	 ${transAudit.auditContent}
                          </div>
                          <div class="text-right gray9">
                           		 学习中心：${transAudit.auditOperator}<br>
                            <fmt:formatDate value="${transAudit.auditDt}"/>
                          </div>
                      </div>
                    </dd>
                </dl>
               </c:when>
                <c:when test="${transAudit.auditState==13}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix">
                      <b class="a-tit gray6">${transAudit.gjtSchoolRollTran.gjtStudentInfo.xm}（学员）已撤销退学申请</b>
                      <span class="gray9 text-no-bold f12"><fmt:formatDate value="${transAudit.auditDt}"/></span>
                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                      <label class="state-lb">撤销退学</label>
                    </dt>
                </dl>
                </c:when>               
				<c:when test="${transAudit.auditOperatorRole eq 2 && transAudit.auditState==3 && code==2}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-orange">
                      <b class="a-tit">学生事务部劝学</b>
                      <span class="fa fa-fw fa-dot-circle-o"></span>
                      <label class="state-lb">劝学中</label>
                    </dt>
                     <dd>
                      <div class="txt">
                          <table class="table no-border margin-bottom-none">
                            <tr>
                              <td width="100">劝学情况：</td>
                              <td>
                                <textarea name="auditContent" class="form-control" rows="5" placeholder="请填写劝学情况！" datatype="*" nullmsg="请填写劝学情况！" errormsg="请填写劝学情况！"></textarea>
                              </td>
                            </tr>
                          </table>
                          <div class="text-right pad-t10 margin_t10 border-top" style="border-color:#ddd">
                            <button type="button" class="btn min-width-90px btn-warning margin_r10 btn-save-edit btn-audit"  val="4">劝学失败</button>
                            <button type="button" class="btn min-width-90px btn-success btn-audit" data-role="btn-pass" val="5">劝学成功</button>
                          </div>
                      </div>                     
                    </dd>                  
                </dl>
				</c:when>
				<c:when test="${transAudit.auditOperatorRole eq 2&& transAudit.auditState==5}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-green">                     
                      <b class="a-tit">学生事务部劝学成功</b>
                      <span class="fa fa-fw fa-check-circle"></span>
                      <label class="state-lb">劝学成功</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                           	 劝学情况： <br>
                           	 ${transAudit.auditContent}
                          </div>
                          <div class="text-right gray9">
                          	  学生事务部：${transAudit.auditOperator}<br>
                            <fmt:formatDate value="${transAudit.auditDt}"/>
                          </div>
                      </div>
                    </dd>
                </dl>
                </c:when>
                
                <c:when test="${transAudit.auditOperatorRole eq 2 && transAudit.auditState==4}">               
                <dl class="approval-item"  <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-red">
                      
                      <b class="a-tit">学生事务部劝学失败</b>
                      <span class="fa fa-fw fa-times-circle"></span>
                      <label class="state-lb">劝学失败</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                            	劝学情况： <br>
                            	${transAudit.auditContent}
                          </div>
                          <div class="text-right gray9">
                                                                                     学生服务部：${transAudit.auditOperator}<br>
                            <fmt:formatDate value="${transAudit.auditDt}"/>
                          </div>
                      </div>
                    </dd>
                </dl>
				</c:when>
				<c:when test="${transAudit.auditOperatorRole eq 5 && transAudit.auditState==3 }">
                <dl class="approval-item"  <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-orange">
                      <b class="a-tit">学籍科劝学</b>
                      <span class="fa fa-fw fa-dot-circle-o"></span>
                      <label class="state-lb">劝学中</label>
                    </dt>
                    <c:if test="${code==5}">                
                    <dd>
                      <div class="txt">
                          <table class="table no-border margin-bottom-none">
                            <tr>
                              <td width="100">劝学情况：</td>
                              <td>
                                <textarea name="auditContent" class="form-control" rows="5" placeholder="请填写劝学情况！" datatype="*" nullmsg="请填写劝学情况！" errormsg="请填写劝学情况！"></textarea>
                              </td>
                            </tr>
                          </table>
                          <div class="text-right pad-t10 margin_t10 border-top" style="border-color:#ddd">
                            <button type="button" class="btn min-width-90px btn-warning margin_r10 btn-save-edit btn-audit"  val="4">劝学失败</button>
                            <button type="button" class="btn min-width-90px btn-success btn-audit" data-role="btn-pass" val="5">劝学成功</button>
                          </div>
                      </div>                     
                    </dd>
                    </c:if> 
                 </dl>
				</c:when>
				<c:when test="${transAudit.auditOperatorRole eq 5 && transAudit.auditState==5}">
                <dl class="approval-item"  <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-green">                     
                      <b class="a-tit">学籍科劝学成功</b>
                      <span class="fa fa-fw fa-check-circle"></span>
                      <label class="state-lb">劝学成功</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                            	劝学情况： <br>
                            	${transAudit.auditContent}
                          </div>
                          <div class="text-right gray9">
                                                                                      学籍科：${transAudit.auditOperator}<br>
                            <fmt:formatDate value="${transAudit.auditDt}"/>
                          </div>
                      </div>
                    </dd>
                </dl>
				</c:when>
               <c:when test="${transAudit.auditOperatorRole eq 5 && transAudit.auditState==4}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-red">
                      
                      <b class="a-tit">学籍科劝学失败</b>
                      <span class="fa fa-fw fa-times-circle"></span>
                      <label class="state-lb">劝学失败</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                            	劝学情况： <br>
                            	${transAudit.auditContent}
                          </div>
                          <div class="text-right gray9">
                                                                                    学籍科：${transAudit.auditOperator}<br>
                            <fmt:formatDate value="${transAudit.auditDt}"/>
                          </div>
                      </div>
                    </dd>
                </dl>
				</c:when>
				<c:when test="${transAudit.auditOperatorRole eq 6 && transAudit.auditState==6}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix">
                      <b class="a-tit gray6">财务服务部待核算实收、应扣、应退费用</b>
                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                      <label class="state-lb">待核算</label>
                    </dt>
                </dl>
				</c:when>
				
				<c:when test="${transAudit.auditOperatorRole eq 6 && transAudit.auditState==7}">
                <dl class="approval-item">
                    <dt class="clearfix text-green">
                      
                      <b class="a-tit">财务服务部已核算实收、应扣、应退费用</b>
                      <span class="fa fa-fw fa-check-circle"></span>
                      <label class="state-lb">已核算</label>
                    </dt>
                    <dd>
                      <div class="box-border pad">
                          <table class="table-gray-th text-center f12">
                            <thead>
                              <tr>
                                <th>专业</th>
                                <th>学费</th>
                                <th>实收费用</th>
                                <th>应扣费用</th>
                                <th>应退费用</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <td>
                                  <ul class="list-unstyled text-left">
                                    <li>专业名称：${item.gjtStudentInfo.gjtSpecialty.zymc}</li>
                                    <li>专业层次：<dic:getLabel typeCode="TrainingLevel" code="${item.gjtStudentInfo.pycc }"/></li>
                                    <li>报读学期：${item.gjtStudentInfo.gjtGrade.gradeName}</li>
                                  </ul>
                                </td>
                                <td>
                                  <ul class="list-unstyled text-left">
                                    <li>原价：${not empty transCost.originalPrice ? transCost.originalPrice : 0.0}元</li>
                                    <li>优惠：${not empty transCost.reducedPrice ? transCost.reducedPrice : 0.0}元</li>
                                    <li>应缴：${not empty transCost.payablePrice ? transCost.payablePrice : 0.0}元</li>
                                    <li>已缴：${not empty transCost.paidinPrice ? transCost.paidinPrice : 0.0}元</li>
                                  </ul>
                                </td>
                                <td>${not empty transCost.receivedPrice ? transCost.receivedPrice : 0.0}元</td>
                                <td>
                                  <ul class="list-unstyled text-left text-orange">
                                    <li>学籍注册费：${not empty transCost.rollRegisterPrice ? transCost.rollRegisterPrice : 0.0}元</li>
                                    <li>网络通讯费：${not empty transCost.networkMessagePrice ? transCost.networkMessagePrice : 0.0}元</li>
                                    <li>学习费：${not empty transCost.studyPrice ? transCost.studyPrice : 0.0}元</li>
                                    <li>教材费：${not empty transCost.shouldTextbookPrice ? transCost.shouldTextbookPrice : 0.0}元</li>
                                    <li>合计：${not empty transCost.totalPrice ? transCost.totalPrice : 0.0}元</li>
                                  </ul>
                                </td>
                                <td>${not empty transCost.shouldBackPrice ? transCost.shouldBackPrice : 0.0}元</td>
                              </tr>
                            </tbody>
                          </table>
                          <ul class="list-unstyled margin_t10 text-right">
                            <li>财务服务部：${transAudit.auditOperator}</li>
                            <li><fmt:formatDate value="${transAudit.auditDt}"/></li>
                          </ul>
                      </div>
                    </dd>
                </dl>
				</c:when>
				
				<c:when test="${transAudit.auditOperatorRole eq 1 && transAudit.auditState==8}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix">
                      <b class="a-tit gray6">学员待确认应扣、应退金额</b>
                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                      <label class="state-lb">待确认</label>
                    </dt>
                </dl>
				</c:when>
				<c:when test="${transAudit.auditOperatorRole eq 1 && transAudit.auditState==9}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-green">
                      <b class="a-tit">学员已确认应扣、应退金额</b>
                      <span class="gray9 text-no-bold f12"><fmt:formatDate value="${transAudit.auditDt}"/></span>
                      <span class="fa fa-fw fa-check-circle"></span>
                      <label class="state-lb">已确认</label>
                    </dt>
                </dl>
				</c:when>
				
				<c:when test="${transAudit.auditOperatorRole eq 0 && transAudit.auditState==8}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-orange">
                      <b class="a-tit">院长确认退学</b>
                      <span class="fa fa-fw fa-dot-circle-o"></span>
                      <label class="state-lb">待处理</label>
                    </dt>
                    <c:if test="${code==0}">
                    <dd>
                      <div class="txt">
                          <table class="table no-border margin-bottom-none">
                            <tr>
                              <td width="100">确认意见：</td>
                              <td>
                                <textarea name="auditContent" class="form-control" rows="5" placeholder="请填写确认意见！" datatype="*" nullmsg="请填写确认意见！" errormsg="请填写确认意见！"></textarea>
                              </td>
                            </tr>
                          </table>
                          <div class="text-right pad-t10 margin_t10 border-top" style="border-color:#ddd">
                            <button type="button" class="btn min-width-90px btn-success btn-audit" data-role="btn-pass" val="9">确认</button>
                          </div>
                      </div>
                    </dd>
                   </c:if>
                </dl>
				</c:when>
				<c:when test="${transAudit.auditOperatorRole eq 0 && transAudit.auditState==9}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-green">                    
                      <b class="a-tit">院长已确认退学</b>
                      <span class="fa fa-fw fa-check-circle"></span>
                      <label class="state-lb">已确认</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                          	  确认意见： <br>
                           	${transAudit.auditContent}
                          </div>
                          <div class="text-right gray9">
                                                                                        院长：${transAudit.auditOperator}<br>
                             <fmt:formatDate value="${transAudit.auditDt}"/>
                          </div>
                      </div>
                    </dd>
                </dl>
				</c:when>
				
				<c:when test="${transAudit.auditOperatorRole eq 7 && transAudit.auditState==10}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix">
                      <b class="a-tit">财务待登记退费信息</b>
                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                      <label class="state-lb">待登记</label>
                    </dt>
                </dl>
				</c:when>
								
				<c:when test="${transAudit.auditOperatorRole eq 7 && transAudit.auditState==11}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-green">
                      <b class="a-tit">财务已登记退费信息</b>
                      <span class="fa fa-fw fa-check-circle"></span>
                      <label class="state-lb">已登记</label>
                    </dt>
                    <dd>
                      <div class="box-border pad">
                          <table class="table-gray-th text-center f12">
                            <thead>
                              <tr>
                                <th>专业</th>
                                <th>学费</th>
                                <th>实收费用</th>
                                <th>应扣费用</th>
                                <th>应退费用</th>
                                <th>退费凭证</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <td>
                                  <ul class="list-unstyled text-left">
                                    <li>专业名称：${item.gjtStudentInfo.gjtSpecialty.zymc}</li>
                                    <li>专业层次：<dic:getLabel typeCode="TrainingLevel" code="${item.gjtStudentInfo.pycc}"/></li>
                                    <li>报读学期：${item.gjtStudentInfo.gjtGrade.gradeName}</li>
                                  </ul>
                                </td>
                                <td>
                                  <ul class="list-unstyled text-left">
                                    <li>原价：${not empty transCost.originalPrice ?transCost.originalPrice:0.0}元</li>
                                    <li>优惠：${not empty transCost.reducedPrice ? transCost.reducedPrice:0.0}元</li>
                                    <li>应缴：${not empty transCost.payablePrice ? transCost.payablePrice:0.0}元</li>
                                    <li>已缴：${not empty transCost.paidinPrice ? transCost.paidinPrice:0.0}元</li>
                                  </ul>
                                </td>
                                <td>${not empty transCost.receivedPrice ?transCost.receivedPrice:0.0}元</td>
                                <td>
                                  <ul class="list-unstyled text-left text-orange">
                                    <li>学籍注册费：${not empty transCost.rollRegisterPrice ? transCost.rollRegisterPrice:0.0}元</li>
                                    <li>网络通讯费：${not empty transCost.networkMessagePrice ? transCost.networkMessagePrice:0.0}元</li>
                                    <li>学习费：${not empty transCost.studyPrice ? transCost.studyPrice : 0.0}元</li>
                                    <li>教材费：${not empty transCost.shouldTextbookPrice ? transCost.shouldTextbookPrice : 0.0}元</li>
                                    <li>合计：${not empty transCost.totalPrice ? transCost.totalPrice : 0.0}元</li>
                                  </ul>
                                </td>
                                <td>${not empty transCost.realBackPrice ? transCost.realBackPrice : 0.0}元</td>
                                <td>
                                 <a href="${not empty transCost.backPriceVoucher ?transCost.backPriceVoucher : '#'}" id="backPriceVoucher" data-role="view-cert">查看凭证</a>                                	
                                </td>
                              </tr>
                            </tbody>
                          </table>
                          <ul class="list-unstyled margin_t10 text-right">
                            <li>财务：${transAudit.auditOperator}</li>
                            <li><fmt:formatDate value="${transAudit.auditDt}"/></li>
                          </ul>
                      </div>
                    </dd>
                </dl>
				</c:when>
				
				<c:when test="${transAudit.auditOperatorRole eq  5 && transAudit.auditState==8}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-orange">
                      <b class="a-tit">学籍科确认退学</b>
                      <span class="fa fa-fw fa-dot-circle-o"></span>
                      <label class="state-lb">待处理</label>
                    </dt>
                    <c:if test="${code=='5'}">
                    <dd>
                      <div class="txt">
                          <table class="table no-border margin-bottom-none">
                            <tr>
                              <td width="100">确认意见：</td>
                              <td>
                                <textarea name="auditContent" class="form-control" rows="5" placeholder="请填写确认意见！" datatype="*" nullmsg="请填写确认意见！" errormsg="请填写确认意见！"></textarea>
                              </td>
                            </tr>
                            <c:if test="${item.gjtStudentInfo.xjzt=='2'}">
                            <tr>
                            <td><div class="pad-t5">上传退学<br>申请表照片：</div></td>
                            <td>
                              <div style="margin-left:0px" class="cert-wrap">
                              <div class="cert-box has-upload cert-box-6">
								<a href="javascript:void(0);" class="info-img-box">
									<img class="info-img" key="pz" src="${transactionContent.dropSchoolPhoto}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
									<div class="upload-btn" data-role="upload-img" data-object='{
					                	"title":"上传退学申请表照片",
					                	"sampleImg":[]
					                }'><i></i><span>点击上传</span>
					                </div>
								</a>
								<a href="${not empty transactionContent.dropSchoolPhoto ? transactionContent.dropSchoolPhoto : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
								<input type="hidden" class="img-val" id="dropSchoolPhoto" name="dropSchoolPhoto" value="${transactionContent.dropSchoolPhoto}" />
							 </div>
                            </div>
                           </td>
                          </tr>
                          </c:if>
                          </table>
                          <div class="text-right pad-t10 margin_t10 border-top" style="border-color:#ddd">
                            <button type="button" class="btn min-width-90px btn-success btn-audit" data-role="btn-pass" val="9">确认</button>
                          </div>
                      </div>                     
                    </dd>
                   </c:if>
                </dl>
                </c:when>
                
                <c:when test="${transAudit.auditOperatorRole eq  5 && transAudit.auditState==9}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-green">
                      <b class="a-tit">学籍科已确认退学</b>
                      <span class="fa fa-fw fa-check-circle"></span>
                      <label class="state-lb">已确认</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                           	 确认意见：<br>
                            <div>${transAudit.auditContent}</div>
                           	<c:if test="${not empty transactionContent.dropSchoolPhoto}">
                            <ul class="list-inline img-gallery img-gallery-md margin_t10" style="margin-right:-5px;">
                              <li>
                                  <img class="info-img" key="pz" src="${transactionContent.dropSchoolPhoto}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                            	  <a href="${not empty transactionContent.dropSchoolPhoto ? transactionContent.dropSchoolPhoto : '#" style="display: none'}" data-role="img-fancybox" class="img-fancybox f12" data-large-img="${transactionContent.dropSchoolPhoto}">点击放大</a>                            	 
                              </li>
                            </ul>
                            </c:if>
                          </div>
                          <div class="text-right gray9">
                                                                                              审核人：${transAudit.auditOperator}<br>
                               <fmt:formatDate value="${transAudit.auditDt}"/>
                          </div>                          
                      </div>
                    </dd>
                </dl>                
			   </c:when>
			   <c:when test="${transAudit.auditState==12}">
                 <dl class="approval-item white-border">
                    <dt class="clearfix">
                      <b class="a-tit">${item.gjtStudentInfo.xm}（学员）已退学</b>
                      <span class="gray9 text-no-bold f12"><fmt:formatDate value="${transAudit.auditDt}"/></span>
                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                      <label class="state-lb">完成退学</label>
                    </dt>
                 </dl>                 
                </c:when>
                <c:otherwise>                
                </c:otherwise>
                </c:choose>               
               </c:if>
              </c:forEach>
            </div>
          </div>
        </div>
      </div>
    </div>
   </form>
  </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<form id="downloadForm" action="${ctx}/edumanage/rollTrans/dropOutStudy/downloadOutStudyApplication.html" method="post" target="_blank">
	<input type="hidden" name="studentId" value="${item.studentId}"/>
	<input type="hidden" name="transactionId" value="${item.transactionId}"/>
</form>
<script type="text/javascript">

//图片放大
;(function(){
  //加载图片放大的相关皮肤
  $('<link/>',{
    rel:"stylesheet",
    type:"text/css",
    href:'http://css.gzedu.com/common/js/fancybox/jquery.fancybox.css',
    'data-id':'require-css'
  }).appendTo($('head'));
  $.getScript('http://css.gzedu.com/common/js/fancybox/jquery.fancybox.pack.js',function(){

    $(".img-gallery").on('click', '.img-fancybox', function(event) {
      event.preventDefault();
      var $img=$(this).closest('.img-gallery').find('.img-fancybox');
          var index=$img.index(this);
          var imgUrls=$.map($img, function(item, index) {
            return $(item).attr('data-large-img');
          });

          $.fancybox(imgUrls, {
            'transitionIn'    : 'none',
            'transitionOut'   : 'none',
            'type'            : 'image',
            'index'           : index,
            'scrollOutside' :false,
            'changeFade'      : 0,
            'loop'      :false,
            beforeShow    :function(){
              this.wrap.parent().css('overflow', 'hidden');
            }
          });
    });
  });
//点击上传图片
  $('[data-role="upload-img"]').click(function(event) {
      var data=$(this).data('object');

      $('.cert-box').removeClass('actived');
      $(this).closest('.cert-box').addClass('actived');

      if( $.type(data)=="object" ){
          $.mydialog({
              id:'pop2',
              width:980,
              zIndex:1000,
              height:500,
              urlData:data,
              content: 'url:' + ctx + '/static/upload-picture-control/pop-upload-picture.html'
          });
      }
  });
})();


//删除
$('body').on('click','[data-oper="remove"]',function(event) {
  event.preventDefault();
  $(this).closest('li').remove();
})
.on('click', '[data-role="view-cert"]', function(event) {
 var tex=document.getElementById('backPriceVoucher');
 var url=tex.getAttribute("href");
  event.preventDefault();
  $.alertDialog({
    id:'view-cert',
    title:'查看凭证',
    width:600,
    height:450,
    zIndex:11000,
    cancelLabel:'关闭',
    //okLabel:'下载',
    ok:function(){//“确定”按钮的回调方法
      //这里 this 指向弹窗对象
      $.closeDialog(this);
    },
    content:'\
      <div class="text-center">\
        <img src="'+url+'" style="max-width:580px;max-height:320px;">\
      </div>\
    '
  });
});
//审核
$(".btn-audit").click(function(){
   var auditContent = $("form#inputForm :input[name='auditContent']").val();
    if($.trim(auditContent) == '') {
        $("form#inputForm :input[name='auditContent']").focus();
        return;
    }      
    var auditState = $(this).attr("val");     
    $("form#inputForm :input[name='auditState']").val(auditState);
  
    if(auditState == 9) {
        if(confirm("确定同意该学员退学？")) {
        	var auditVoucher=$('#dropSchoolPhoto').val();
        	if(auditVoucher==''){
        		alert("请上传退学申请表照片！");
        		return;
        	}
            $("form#inputForm").submit();
        }
    } else{        
       $("form#inputForm").submit();
    }
});
//下载退学申请表
$(".download").click(function() {
	 $("#downloadForm").submit();	
});
</script>
</body>
</html>
