<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
 <meta charset="utf-8">
 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学习异动明细</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<link rel="stylesheet" href="${ctx}/static/dist/css/signup-info.css">
</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学籍管理</a></li>
		<li><a href="#">学籍异动</a></li>
		<li class="active">学籍异动明细</li>
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
              <span>
              	<shiro:hasPermission name="/personal/index$privacyJurisdiction">
					${item.gjtStudentInfo.sjh}
				</shiro:hasPermission>
				<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
					<c:if test="${not empty item.gjtStudentInfo.sjh }">
					${fn:substring(item.gjtStudentInfo.sjh,0, 3)}******${fn:substring(item.gjtStudentInfo.sjh,8, (item.gjtStudentInfo.sjh).length())}
					</c:if>
				</shiro:lacksPermission>
              
              </span>
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
          <div class="f24 text-center text-orange line-height-1em">信息更正
          <c:choose>
       		<c:when test="${item.transactionPartStatus==0}"><div class="gray9 f14">（性别民族变更）</div></c:when>
       		<c:when test="${item.transactionPartStatus==1}"><div class="gray9 f14">（入学文化程度更变）</div></c:when>
       		<c:when test="${item.transactionPartStatus==2}"><div class="gray9 f14">（姓名变更）</div></c:when> 
       		<c:when test="${item.transactionPartStatus==3}"><div class="gray9 f14">（身份证变更）</div></c:when>
       		<c:otherwise>                 	 	 
         	</c:otherwise>             		
       	</c:choose>
       	</div>
        <div class="text-center gray6">异动类型</div>
        </div>
        <div class="col-sm-2_5 col-xs-6 no-margin no-pad-top">
          <div class="f24 text-center text-orange line-height-1em">
            <c:choose>
	       		<c:when test="${item.transactionStatus==0}">待审核</c:when>
	       		<c:when test="${item.transactionStatus==1}">审核通过</c:when>
	       		<c:when test="${item.transactionStatus==2}">审核不通过</c:when>
	       		<c:otherwise>待访谈</c:otherwise> 
        	</c:choose>
        	<c:choose>
        		<c:when test="${item.auditOperatorRole==2}"><div class="gray9 f14">（班主任）</div></c:when>
        		<c:when test="${item.auditOperatorRole==3}"><div class="gray9 f14">（学习中心）</div></c:when>
        		<c:when test="${item.auditOperatorRole==4}"><div class="gray9 f14">（招生办）</div></c:when>
        		<c:when test="${item.auditOperatorRole==5}"><div class="gray9 f14">（学籍科）</div></c:when>
        		<c:otherwise><div class="gray9 f14">（中央电大）</div></c:otherwise> 
        	</c:choose>           
          </div>
          <div class="text-center gray6">异动状态</div>
        </div>
        <div class="col-sm-2_5 col-xs-6 text-center">
          <button class="btn btn-default btn-sm margin_t10">
            <i class="fa fa-simulated-login f20 vertical-middle"></i>
            	模拟登录
          </button>
        </div>
      </div>
    </div>
  </div>

  <div class="box margin-bottom-none">
    <form id="inputForm" role="form" action="${ctx}/edumanage/schoolRollTransaction/transAudit.html" method="post">
   	<input type="hidden" name="action" value="${action}"/>
   	<input type="hidden" name="transactionType" value="${item.transactionType}"/>
   	<input type="hidden" name="transactionId" value="${item.transactionId}"/>
   	<input type="hidden" name="studentId" value="${item.studentId}"/>
   	<input type="hidden" name="auditOperatorRole" value="${item.auditOperatorRole}"/>
   	<input type="hidden" name="auditState" value=""/>
   	<input type="hidden" name="transactionPartStatus" value="${item.transactionPartStatus}"/>
    <div class="box-body">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-1" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">信息更正</span>
          </h3>
        </div>
        <div id="info-box-1" class="collapse in">
          <div class="panel-body content-group">
            <div class="cnt-box-body">
              <table width="100%">
                <tbody>
                  
                   <c:choose>
                    <c:when test="${item.transactionPartStatus==0}">
                    <tr>
                    <td>                   
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              	登记信息
                          </h3>
                        </div>
                    		<table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">性别： <dic:getLabel typeCode="Sex" code="${item.gjtStudentInfo.xbm}"/></td>
                            </tr>
                            <tr>
                              <td class="pad-l15 pad-r15">民族：<dic:getLabel typeCode="NationalityCode" code="${item.gjtStudentInfo.mzm}"/></td>
                            </tr>
                          </tbody>
                        </table>                                                                 
                      </div>                   
                    </td>                   
                    <td class="text-center">
                      <i class="fa fa-fw fa-angle-double-right f24"></i>
                    </td>
                    <td>
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              	更正信息
                          </h3>
                        </div>                       
                        <table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">姓名：${transactionContent['sex'] }</td>
                            </tr>
                            <tr>
                              <td class="pad-l15 pad-r15">民族：${transactionContent['nation']}</td>
                            </tr>
                          </tbody>
                        </table>                       
                      </div>
                    </td>
                    </tr>
                    </c:when>
                    <c:when test="${item.transactionPartStatus==1}">
                    <tr>
                    <td>                   
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              	登记信息
                          </h3>
                        </div>
                    		<table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">原学历层次： ${item.gjtStudentInfo.exedulevel}</td>
                            </tr>
                          </tbody>
                        </table>                                                                 
                      </div>                   
                    </td>                   
                    <td class="text-center">
                      <i class="fa fa-fw fa-angle-double-right f24"></i>
                    </td>
                    <td>
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              	更正信息
                          </h3>
                        </div>                       
                        <table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">原学历层次：${transactionContent['exedulevel']}</td>
                            </tr>                            
                          </tbody>
                        </table>                       
                      </div>
                    </td>
                    </tr>
                    </c:when>
                    <c:when test="${item.transactionPartStatus==2}">
                    <tr>
                    <td>                   
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              	登记信息
                          </h3>
                        </div>
                    		<table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">姓名： ${item.gjtStudentInfo.xm}</td>
                            </tr>
                          </tbody>
                        </table>                                                                 
                      </div>                   
                    </td>                   
                    <td class="text-center">
                      <i class="fa fa-fw fa-angle-double-right f24"></i>
                    </td>
                    <td>
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              	更正信息
                          </h3>
                        </div>                       
                        <table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">姓名：${transactionContent['xm']}</td>
                            </tr>                            
                          </tbody>
                        </table>                       
                      </div>
                    </td>
                    </tr>
                    </c:when>
                    <c:otherwise>
                    <tr>
                    <td>                   
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              	登记信息
                          </h3>
                        </div>
                    		<table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">身份证号： 
                              
                              
                              <shiro:hasPermission name="/personal/index$privacyJurisdiction">
										${item.gjtStudentInfo.sfzh}
									</shiro:hasPermission>
									<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
										<c:if test="${not empty item.gjtStudentInfo.sfzh }">
										${fn:substring(item.gjtStudentInfo.sfzh,0, 4)}******${fn:substring(item.gjtStudentInfo.sfzh,14, (item.gjtStudentInfo.sfzh).length())}
										</c:if>
									</shiro:lacksPermission>
                              
                              
                              </td>
                            </tr>
                          </tbody>
                        </table>                                                                 
                      </div>                   
                    </td>                   
                    <td class="text-center">
                      <i class="fa fa-fw fa-angle-double-right f24"></i>
                    </td>
                    <td>
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              	更正信息
                          </h3>
                        </div>                       
                        <table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">身份证号：
                              
                              
                              <shiro:hasPermission name="/personal/index$privacyJurisdiction">
										${transactionContent['sfzh']}
									</shiro:hasPermission>
									<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
										<c:if test="${not empty transactionContent['sfzh'] }">
										${fn:substring(transactionContent['sfzh'],0, 4)}******${fn:substring(transactionContent['sfzh'],14, (transactionContent['sfzh']).length())}
										</c:if>
									</shiro:lacksPermission>
                              
                              </td>
                            </tr>                            
                          </tbody>
                        </table>                       
                      </div>
                    </td>
                    </tr>
                   </c:otherwise>                                        
                   </c:choose>                                     
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      
      <c:choose>
      <c:when test="${item.transactionPartStatus!=1}">
      <c:if test="${transactionContent['proveFileType']=='1'}">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">身份证照片</span>
          </h3>
        </div>
        <div id="info-box-2" class="collapse in">
          <form class="theform">
            <div class="panel-body content-group overlay-wrapper position-relative">
              <div class="cnt-box-body">
                <div class="row">
                  <div class="col-sm-6 margin_b15">
                    <div class="cert-wrap">
                      <h4 class="cert-title text-center f16">信息更正后照片正面</h4>
                      <div class="cert-box has-upload cert-box-1">
                        <div class="cert-img-box table-block">
                          <div class="table-cell-block vertical-middle text-center">
                            <img src="${transactionContent['sfzz']}" alt="User Image">
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="col-sm-6 margin_b15">
                    <div class="cert-wrap">
                      <h4 class="cert-title text-center f16">信息更正后照片背面</h4>
                      <div class="cert-box has-upload cert-box-1">
                        <div class="cert-img-box table-block">
                          <div class="table-cell-block vertical-middle text-center">
                            <img src="${transactionContent['sfzf']}" alt="User Image">
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
      </c:if>
      <c:if test="${transactionContent['proveFileType']=='2'}">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">其他证明（户籍证明、更正事项告知函）</span>
          </h3>
        </div>
        <div id="info-box-2" class="collapse in">
          <form class="theform">
            <div class="panel-body content-group overlay-wrapper position-relative">
              <div class="cnt-box-body">
                <div class="row">
                  <div class="col-sm-6 margin_b15">
                    <div class="cert-wrap">
                      <h4 class="cert-title text-center f16">其他证明</h4>
                      <div class="cert-box has-upload cert-box-1">
                        <div class="cert-img-box table-block">
                          <div class="table-cell-block vertical-middle text-center">
                            <img src="${transactionContent['otherProve']}" alt="User Image">
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
      </c:if>
     </c:when>
     <c:otherwise>
     	<div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">学历认证报告/毕业证书</span>
          </h3>
        </div>
        <div id="info-box-2" class="collapse in">
          <form class="theform">
            <div class="panel-body content-group overlay-wrapper position-relative">
              <div class="cnt-box-body">
                <div class="row">
                  <div class="col-sm-6 margin_b15">
                    <div class="cert-wrap">
                      <h4 class="cert-title text-center f16">其他证明</h4>
                      <div class="cert-box has-upload cert-box-1">
                        <div class="cert-img-box table-block">
                          <div class="table-cell-block vertical-middle text-center">
                            <img src="${transactionContent['byzz']}" alt="User Image">
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
     </c:otherwise>
    </c:choose>
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
          <c:if test="${transAudit.auditOperatorRole==1}">
            <dl class="approval-item">
                  <dt class="clearfix">
                    <b class="a-tit gray6">${transAudit.gjtSchoolRollTran.gjtStudentInfo.xm}（学员）</b>
                    <span class="gray9 text-no-bold f12"><fmt:formatDate value="${transAudit.auditDt}" type="both"/></span>
                    <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                    <label class="state-lb">${s.index==0?'学员申请':'学员重新申请'}</label>
                  </dt>
              </dl>
             </c:if>
             <c:if test="${transAudit.auditOperatorRole!=1}">
             <c:choose>
              <c:when test="${transAudit.auditOperatorRole==2}">
              	<dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                  <dt class="clearfix">                    
                    <b class="a-tit gray6">班主任访谈</b>
                    <c:if test="${transAudit.auditState==0}">
	                    <span class="fa fa-fw fa-check-circle text-green"></span>
	                    <label class="state-lb text-green">待审核</label>
                    </c:if>
                    <c:if test="${transAudit.auditState==1}">
	                    <span class="fa fa-fw fa-check-circle text-green"></span>
	                    <label class="state-lb text-green">审核通过</label>	                
                    </c:if>
                    <c:if test="${transAudit.auditState==2}">
	                    <span class="fa fa-fw fa-check-circle text-green"></span>
	                    <label class="state-lb text-green">审核不通过</label>	                
                    </c:if>
                    <c:if test="${transAudit.auditState==3}">
	                    <span class="fa fa-fw fa-check-circle text-green"></span>
	                    <label class="state-lb text-green">待访谈</label>	                  
                    </c:if>
                  </dt>
                 <c:if test="${transAudit.auditState!=0}">
                <dd>
                    <div class="txt">
                        <div class="marin_b10">${transAudit.auditContent}</div>
                        <div class="text-right gray9">
                         	 审核人：${transAudit.auditOperator}<br>
                          <fmt:formatDate value="${transAudit.auditDt}"/>
                        </div>                        
                    </div>
                 </dd>
               </c:if> 
              </dl>              
              </c:when>
              
              <%-- <c:when test="${transAudit.auditOperatorRole==3}">
              	<dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                  <dt class="clearfix">                    
                    <b class="a-tit gray6">学习中心审批</b>
                    <c:if test="${transAudit.auditState==0}">
	                    <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
	                    <label class="state-lb text-green">待审核</label>
                    </c:if>
                    <c:if test="${transAudit.auditState==1}">
	                    <span class="fa fa-fw fa-check-circle text-green"></span>
	                    <label class="state-lb text-green">审核通过</label>	                
                    </c:if>
                    <c:if test="${transAudit.auditState==2}">
	                    <span class="fa fa-fw fa-times-circle text-red"></span>
	                    <label class="state-lb text-green">审核不通过</label>	                
                    </c:if>
                    <c:if test="${transAudit.auditState==3}">
	                    <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
	                    <label class="state-lb text-green">待访谈</label>	                  
                    </c:if>
                  </dt>
                 <c:if test="${transAudit.auditState!=0}">
                <dd>
                    <div class="txt">
                        <div class="marin_b10">${transAudit.auditContent}</div>
                        <div class="text-right gray9">
                         	 审核人：${transAudit.auditOperator}<br>
                          <fmt:formatDate value="${transAudit.auditDt}"/>
                        </div>                        
                    </div>
                 </dd>
               </c:if> 
               <c:if test="${transAudit.auditState==0 && code==3}">
                     <dd>
                    <div class="txt">
                        <table class="table no-border margin-bottom-none">
                          <tr>
                            <td width="110">审核意见：</td>
                            <td>
                              <textarea name="auditContent" class="form-control" rows="5" placeholder="请输入审核意见" datatype="*" nullmsg="请输入审核意见" errormsg="请输入审核意见"></textarea>
                            </td>
                          </tr>
                        </table>
                        <div class="text-right pad-t10 margin_t10 border-top" style="border-color:#ddd">
                          <button type="button" class="btn min-width-90px btn-warning margin_r10 btn-audit" val="2">审核不通过</button>
                          <button type="button" class="btn min-width-90px btn-success btn-audit"   val="1">审核通过</button>
                        </div>
                    </div>                    
                  </dd>
               </c:if> 
              </dl>              
              </c:when>  --%>
             
            <%-- <c:when test="${transAudit.auditOperatorRole==4}">
           	<dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
               <dt class="clearfix">                    
                 <b class="a-tit gray6">招生办审批</b>
                 <c:if test="${transAudit.auditState==0}">
                  <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
                  <label class="state-lb text-green">待审核</label>
                 </c:if>
                 <c:if test="${transAudit.auditState==1}">
                  <span class="fa fa-fw fa-check-circle text-green"></span>
                  <label class="state-lb text-green">审核通过</label>	                
                 </c:if>
                 <c:if test="${transAudit.auditState==2}">
                  <span class="fa fa-fw fa-times-circle text-red"></span>
                  <label class="state-lb text-green">审核不通过</label>	                
                 </c:if>
                 <c:if test="${transAudit.auditState==3}">
                  <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
                  <label class="state-lb text-green">待访谈</label>	                  
                 </c:if>
               </dt>
              <c:if test="${transAudit.auditState!=0}">
             <dd>
                 <div class="txt">
                     <div class="marin_b10">${transAudit.auditContent}</div>
                     <div class="text-right gray9">
                      	 审核人：${transAudit.auditOperator}<br>
                       <fmt:formatDate value="${transAudit.auditDt}"/>
                     </div>                        
                 </div>
              </dd>
            </c:if> 
           </dl>              
           </c:when> --%>
           
           <c:otherwise>
            	<dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                <dt class="clearfix">                    
                  <b class="a-tit gray6">学籍科审批</b>
                  <c:if test="${transAudit.auditState==0}">
                   <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
                   <label class="state-lb text-green">待审核</label>
                  </c:if>
                  <c:if test="${transAudit.auditState==1}">
                   <span class="fa fa-fw fa-check-circle text-green"></span>
                   <label class="state-lb text-green">审核通过</label>	                
                  </c:if>
                  <c:if test="${transAudit.auditState==16}">
                   <span class="fa fa-fw fa-check-circle text-green"></span>
                   <label class="state-lb text-green">待上传凭证</label>	                
                  </c:if>
                  <c:if test="${transAudit.auditState==2}">
                   <span class="fa fa-fw fa-times-circle text-red"></span>
                   <label class="state-lb text-green">审核不通过</label>	                
                  </c:if>
                  <c:if test="${transAudit.auditState==3}">
                   <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
                   <label class="state-lb text-green">待访谈</label>	                  
                  </c:if>
                </dt>
              <c:if test="${transAudit.auditState==2}">
              <dd>
                  <div class="txt">
                      <div class="marin_b10">${transAudit.auditContent}</div>
                      <div class="text-right gray9">
                       	 审核人：${transAudit.auditOperator}<br>
                        <fmt:formatDate value="${transAudit.auditDt}"/>
                      </div>                        
                  </div>
               </dd>
             </c:if>
             <c:if test="${transAudit.auditState==1}">
               <dd>
                <div class="txt">               
                    <div class="marin_b10">                   
                     ${transAudit.auditContent}
                      <ul class="list-inline img-gallery img-gallery-md margin_t10" style="margin-right:-5px;">
                        <li>
                            <img class="info-img" key="pz" src="${transAudit.auditVoucher}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                            <a href="${transAudit.auditVoucher}" data-role="img-fancybox" class="img-fancybox f12" data-large-img="${transAudit.auditVoucher}">点击放大</a>
                        </li>
                      </ul>
                      <div class=" upload-file-lbl vertical-mid margin_t10">
                        <a href="${transAudit.approvalBook}" class="text-underline" target="_blank">${transAudit.approvalBookName}</a>
                      </div>                     
                    </div>
                    
                    <div class="text-right gray9">
                      	审核人：${transAudit.auditOperator}<br>
                     <fmt:formatDate value="${transAudit.auditDt}"/>
                    </div>                      
                </div>
              </dd>
             </c:if>
             <c:if test="${transAudit.auditState==16 && code==5}">
             	<dd>
                    <div class="txt">
                        <table class="table no-border margin-bottom-none">
                          <tr>
                            <td width="110">审核意见：</td>
                            <td>
                              <textarea name="auditContent" class="form-control" rows="5" placeholder="请输入审核意见" datatype="*" nullmsg="请输入审核意见" errormsg="请输入审核意见" readonly="true">${transAudit.auditContent}</textarea>
                            </td>
                          </tr>
                          <tr>
                            <td><div class="pad-t5">上传凭证：</div></td>
                            <td>
                              <div style="margin-left:0px" class="cert-wrap">
                              <div class="cert-box has-upload cert-box-6">
								<a href="javascript:void(0);" class="info-img-box">
									<img class="info-img" key="pz" src="${transAudit.auditVoucher}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
									<div class="upload-btn" data-role="upload-img" data-object='{
					                	"title":"上传凭证",
					                	"sampleImg":[]
					                }'><i></i><span>点击上传</span>
					                </div>
								</a>
								<a href="${not empty transAudit.auditVoucher ? transAudit.auditVoucher : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
								<input type="hidden" class="img-val" id="auditVoucher" name="auditVoucher" value="${transAudit.auditVoucher}" />
							 </div>
                            </div>
                           </td>
                          </tr>
                          <tr>
                            <td><div class="pad-t5">上传审批表：</div></td>
                            <td>
                              <div>
                                <input type="hidden" id="fileUrl" name="approvalBook" class="form-control" value="${transAudit.approvalBook}"/>
								<input type="hidden" id="fileName" name="approvalBookName" class="form-control" value="${transAudit.approvalBookName}"/> 
                                <button id="uploadBtn" type="button" class="btn min-width-90px btn-default" onclick="uploadFile('fileName','fileUrl',null,null,uploadCallback)">选择文件</button>
                              </div>
                              <ul class="list-inline no-margin" id="data">                                
                              </ul>
                            </td>
                          </tr>
                        </table>
                        <div class="text-right pad-t10 margin_t10 border-top" style="border-color:#ddd">
                          <button type="button" class="btn min-width-90px btn-warning margin_r10 btn-audit" val="2">审核不通过</button>
                          <button type="button" class="btn min-width-90px btn-success btn-audit" val="1">审核通过</button>
                       
                        </div>
                    </div>                   
                  </dd>
             </c:if> 
             <c:if test="${transAudit.auditState==0 && code==5}">
                 <dd>
                    <div class="txt">
                        <table class="table no-border margin-bottom-none">
                          <tr>
                            <td width="110">审核意见：</td>
                            <td>
                              <textarea name="auditContent" class="form-control" rows="5" placeholder="请输入审核意见" datatype="*" nullmsg="请输入审核意见" errormsg="请输入审核意见"></textarea>
                            </td>
                          </tr>
                          <tr>
                            <td><div class="pad-t5">上传凭证：</div></td>
                            <td>
                              <div style="margin-left:0px" class="cert-wrap">
                              <div class="cert-box has-upload cert-box-6">
								<a href="javascript:void(0);" class="info-img-box">
									<img class="info-img" key="pz" src="${transAudit.auditVoucher}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
									<div class="upload-btn" data-role="upload-img" data-object='{
					                	"title":"上传凭证",
					                	"sampleImg":[]
					                }'><i></i><span>点击上传</span>
					                </div>
								</a>
								<a href="${not empty transAudit.auditVoucher ? transAudit.auditVoucher : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
								<input type="hidden" class="img-val" id="auditVoucher" name="auditVoucher" value="${transAudit.auditVoucher}" />
							 </div>
                            </div>
                           </td>
                          </tr>
                          <tr>
                            <td><div class="pad-t5">上传审批表：</div></td>
                            <td>
                              <div>
                                <input type="hidden" id="fileUrl" name="approvalBook" class="form-control" value="${transAudit.approvalBook}"/>
								<input type="hidden" id="fileName" name="approvalBookName" class="form-control" value="${transAudit.approvalBookName}"/> 
                                <button id="uploadBtn" type="button" class="btn min-width-90px btn-default" onclick="uploadFile('fileName','fileUrl',null,null,uploadCallback)">选择文件</button>
                              </div>
                              <ul class="list-inline no-margin" id="data">
                                 <%--<li class="margin_t10 margin_r10 no-padding" id="data">
                                
                                  <span class="btn bg-light-blue text-nowrap">
                                    <b class="text-no-bold" id="exampleA"></b>
                                    <c:if test="${transAudit.auditState!=1}">
                                    <i class="glyphicon glyphicon-remove" data-toggle="tooltip" title="删除" data-oper="remove"></i>
                                  	</c:if>
                                  </span> 
                                 
                                </li>--%>
                              </ul>
                            </td>
                          </tr>
                        </table>
                        <div class="text-right pad-t10 margin_t10 border-top" style="border-color:#ddd">
                          <button type="button" class="btn min-width-90px btn-warning margin_r10 btn-audit" val="2">审核不通过</button>
                          <button type="button" class="btn min-width-90px btn-success btn-audit" val="1">审核通过</button>
                       
                        </div>
                    </div>                   
                  </dd>
               </c:if> 
              </dl>
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
<jsp:include page="/eefileupload/upload.jsp" />
<script type="text/javascript">
//删除
$('body').on('click','[data-oper="remove"]',function(event) {
  event.preventDefault();
  $(this).closest('li').remove();
  
});

$(function() {
	
     /*********'图片放大'**********/
    //加载脚本
    /*
    $.getScript(css + '/ouchgzee_com/platform/xllms_css/plugins/custom-model/custom-model.js?t=123465',function(){
        $('[data-role="img-fancybox"]').click(function(event) {
            event.preventDefault();
            var self=this;
            var imgURL=$(self).attr('href');
            if(imgURL != null && imgURL != '#') {
	            var h=600;
	
	            var t=[
	                '<table width="100%" height="'+h+'">',
	                '<tr>',
	                '<td class="text-center" valign="middle">',
	                '<span class="inline-block position-relative bg-white overlay-wrapper">',
	                '<div class="overlay"><i class="fa fa-refresh fa-spin" style="color:#fff;"></i></div>',
	                '</span>',
	                '</td>',
	                '</tr>',
	                '</table>'
	            ].join('');
	
	            var pop=$.mydialog({
	                id:'fancybox',
	                width:600,
	                height:h,
	                zIndex:11000,
	                content: t,
	                //backdrop:false,
	                //fade:false,
	                showCloseIco:false,
	                modalCss:'my-modal',
	                onLoaded:function(){
	                    var $img=$('<img src="'+imgURL+'">');
	                    $img.on('load', function(event) {
	                        pop.find('span.overlay-wrapper').html('<img src="'+imgURL+'" style="max-width:100%;max-height:'+h+'px;"><i data-dismiss="modal" class="fa fa-fw fa-times-circle"></i>');
	                    });
	                }
	            });
            }
        });
    });
    */

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
	
  //审核
  $(".btn-audit").click(function(){
     var auditContent = $("form#inputForm :input[name='auditContent']").val();
      if($.trim(auditContent) == '') {
          $("form#inputForm :input[name='auditContent']").focus();
          return;
      }      
      var auditState = $(this).attr("val");     
      $("form#inputForm :input[name='auditState']").val(auditState);
    
      if(auditState == 1) {
          if(confirm("确定审核通过？")) {
          /* 	var auditVoucher=$('#auditVoucher').val();
          	var approvalBook=$('#fileUrl').val();
          	if(auditVoucher==''){
          		alert("请上传凭证！");
          		return;
          	}
          	if(approvalBook==''){
          		alert("请上传审批表!");
          		return;
          	}*/
              $("form#inputForm").submit();
          }          
      } else if(auditState == 2) {
          if(confirm("确定审核不通过？")) {
              $("form#inputForm").submit();
          }
      }
  });
  if($('#action').val() == 'view'){
		$("#exampleA").html($("#fileName").val());
		$("#exampleA").attr("href", $("#fileUrl").val());
	}
});
function uploadCallback() {
	var fileName = $("#fileName").val();
	if(fileName!=null){
		$('#data').html('<li class="margin_t10 margin_r10 no-padding" id="data">'+
				'<span class="btn bg-light-blue text-nowrap">'+
                '<b class="text-no-bold" id="exampleA"></b>'+
                '<c:if test="${transAudit.auditState!=1}">'+
                '<i class="glyphicon glyphicon-remove" data-toggle="tooltip" title="删除" data-oper="remove"></i>'+
              	'</c:if>'+
              '</span></li>')
	}
	$("#exampleA").html( '<b class="text-no-bold" id="exampleA">'+fileName+'</b>');
}
</script>
</body>
</html>
