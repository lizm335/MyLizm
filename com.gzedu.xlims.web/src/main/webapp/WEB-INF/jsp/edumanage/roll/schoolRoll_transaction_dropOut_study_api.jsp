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
</head>
<body class="inner-page-body">
<section class="content">

<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
 
  <div class="box margin-bottom-none">
    <div class="box-body">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">退学审核流程</span>
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
				<c:when test="${transAudit.auditOperatorRole eq 2&& transAudit.auditState==3}">
                <dl class="approval-item" <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-orange">
                      <b class="a-tit">学生事务部劝学</b>
                      <span class="fa fa-fw fa-dot-circle-o"></span>
                      <label class="state-lb">劝学中</label>
                    </dt>                   
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
                
                <c:when test="${transAudit.auditOperatorRole eq 2&& transAudit.auditState==4}">               
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
				<c:when test="${transAudit.auditOperatorRole eq 5 && transAudit.auditState==3}">
                <dl class="approval-item"  <c:if test="${s.index==(fn:length(transAuditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                    <dt class="clearfix text-orange">
                      <b class="a-tit">学籍科劝学</b>
                      <span class="fa fa-fw fa-dot-circle-o"></span>
                      <label class="state-lb">劝学中</label>
                    </dt>                                   
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
                                    <li>专业层次：<dic:getLabel typeCode="TrainingLevel" code="${item.gjtStudentInfo.pycc}"/></li>
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
                                <td>${transCost.receivedPrice}元</td>
                                <td>
                                  <ul class="list-unstyled text-left text-orange">
                                    <li>学籍注册费：${not empty transCost.rollRegisterPrice ? transCost.rollRegisterPrice : 0.0}元</li>
                                    <li>网络通讯费：${not empty transCost.networkMessagePrice ? transCost.networkMessagePrice: 0.0}元</li>
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
                            	  <a href="${not empty transactionContent.dropSchoolPhoto ? transactionContent.dropSchoolPhoto : '#' }" data-role="img-fancybox" class="img-fancybox f12" data-large-img="${transactionContent.dropSchoolPhoto}">点击放大</a>
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
  </div>
</section>
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
})();
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
        	var auditVoucher=$('#auditVoucher').val();
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
<c:if test="${not empty feedback && feedback.successful}">
setTimeout(function() {
	/*自动关闭当前窗口*/
	window.close();
}, 3000);
</c:if>
</script>
</body>
</html>
