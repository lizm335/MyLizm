<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
 <meta charset="utf-8">
 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>免修免考</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<link rel="stylesheet" href="${ctx}/static/dist/css/signup-info.css">
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学籍管理</a></li>
		<li><a href="#">免修免考</a></li>
		<li class="active">免修免考详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
    <div class="box-body">
      <div class="row">
        <div class="col-sm-7">
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
            <div class="media-body pad-t10">
              <table width="100%">
                <colgroup>
                  <col width="1%">
                  <col width="35%">
                  <col width="1%">
                  <col width="65%">
                </colgroup>
                <tbody>
                  <tr>
                    <td class="text-nowrap pad5" valign="top">学员姓名：</td>
                    <td class="pad5" valign="top">${item.gjtStudentInfo.xm}(<dic:getLabel typeCode="Sex" code="${item.gjtStudentInfo.xbm}"/>)</td>

                    <td class="text-nowrap pad5" valign="top">层次：</td>
                    <td class="pad5" valign="top"><dic:getLabel typeCode="TrainingLevel" code="${item.gjtStudentInfo.pycc }" /></td>
                  </tr>
                  <tr>
                    <td class="text-nowrap pad5" valign="top">学员学号：</td>
                    <td class="pad5" valign="top">${item.gjtStudentInfo.xh}</td>

                    <td class="text-nowrap pad5" valign="top">年级：</td>
                    <td class="pad5" valign="top">${item.gjtStudentInfo.gjtGrade.gradeName}</td>
                  </tr>
                  <tr>
                    <td class="text-nowrap pad5" valign="top">学习中心：</td>
                    <td class="pad5" valign="top">${item.gjtStudentInfo.gjtStudyCenter.gjtOrg.orgName}</td>

                    <td class="text-nowrap pad5" valign="top">专业：</td>
                    <td class="pad5" valign="top">${item.gjtStudentInfo.gjtSpecialty.zymc}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div class="col-sm-5">
          <table width="100%" height="112" class="text-center margin_t10">
            <tbody>
              <tr>
                <td style="border-left:1px solid #e5e5e5">
                  <div class="f20">${item.gjtCourse.kcmc}</div>
                  <div class="gray9 margin_t5">申请免修免考课程</div>
                </td>
                <td style="border-left:1px solid #e5e5e5">
                  <div class="f20"><fmt:formatDate value="${item.createdDt}"/></div>
                  <div class="gray9 margin_t5">申请时间</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

  <div class="box margin-bottom-none">
  <form id="inputForm" role="form" action="${ctx}/edumanage/exemptExamInfo/exemptExamAudit.html" method="post">
  <input type="hidden" name="auditState" value=""/>
  <input type="hidden" name="exemptExamId" value="${item.exemptExamId}"/>
  <input type="hidden" name="courseId" value="${item.courseId}"/>
  <input type="hidden" name="studentId" value="${item.studentId}"/>
    <div class="box-body">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">材料证明</span>
          </h3>
        </div>
        <div id="info-box-3" class="collapse in">
         <c:forEach var="prove" items="${proveList}">
          <div class="panel-body">
            <div class="nav-tabs-custom box-border margin-bottom-none">
           
              <ul class="nav nav-tabs">
                <li class="active"><a href="#tab_top_1" data-toggle="tab">${prove.gjtExemptExamMaterial.materialName}</a></li>
                <%-- <li><a href="#tab_top_2" data-toggle="tab">${item.gjtCourse.kcmc}</a></li> --%>
              </ul>
              <div class="tab-content">
                <div class="tab-pane active" id="tab_top_1">

                  <div class="alert bg-f2f2f2">
                    <i class="icon fa fa-exclamation-circle f20"></i>${prove.gjtExemptExamMaterial.memo}
                  </div>
                  <div class="row margin_t15">
                    <div class="col-sm-6 col-sm-offset-3 margin_b15">
                      <div class="cert-wrap">
                        <div class="cert-box has-upload cert-box-1">
                         <a href="javascript:void(0);" class="info-img-box"><img class="info-img" src="${prove.url}" alt="No Image"></a>
                         <a href="${prove.url}" data-role="img-fancybox" data-large-img="${prove.url}" class="light-box">点击放大</a>                                                      
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="text-center">
                    <span class="margin_r15">颁证时间：${prove.awardDate}</span>
                    <span>颁证单位：${prove.awardUnit}</span>
                  </div>
                </div>
                <div class="tab-pane" id="tab_top_2">
                </div>
              </div>
              
            </div>
          </div>
          </c:forEach>
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
                	<img src="${item.sign}" alt="User Image">
              </div>
            </div>
          </div>
        </div>
      </div>
      <c:if test="${action=='view'}">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">审核记录</span>
          </h3>
        </div>
        <div id="info-box-4" class="collapse in">
          <div class="panel-body">
            <div class="approval-list clearfix">
            <c:forEach var="audit" items="${auditList}" varStatus="s">
            <c:if test="${audit.auditOperatorRole==1}">
               <dl class="approval-item">
                  <dt class="clearfix">
                    <b class="a-tit gray6">${audit.gjtExemptExamInfo.gjtStudentInfo.xm}（学员）</b>
                    <span class="gray9 text-no-bold f12"><fmt:formatDate value="${audit.auditDt}" type="both"/></span>
                    <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                    <label class="state-lb">${s.index==0?'学员申请':'学员重新申请'}</label>
                  </dt>
              </dl>
              </c:if>
               <c:if test="${audit.auditOperatorRole!=1}">
               	<dl class="approval-item" <c:if test="${s.index==(fn:length(auditList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                <dt class="clearfix">                    
                  <b class="a-tit gray6">学籍科审批</b>
                  <c:if test="${audit.auditState==0}">
                   <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
                   <label class="state-lb text-green">待审核</label>
                  </c:if>
                  <c:if test="${audit.auditState==1}">
                   <span class="fa fa-fw fa-check-circle text-green"></span>
                   <label class="state-lb text-green">审核通过</label>	                
                  </c:if>
                  <c:if test="${audit.auditState==2}">
                   <span class="fa fa-fw fa-times-circle text-red"></span>
                   <label class="state-lb text-green">审核不通过</label>	                
                  </c:if>
                </dt>
              <c:if test="${audit.auditState==2 || audit.auditState==1}">
              <dd>
                <div class="txt">
                    <div class="marin_b10">${audit.auditContent}</div>
                    <div class="text-right gray9">
                     	 审核人：${audit.auditOperator}<br>
                      <fmt:formatDate value="${audit.auditDt}"/>
                    </div>                        
                </div>
              </dd>
              </c:if>
              </dl>
             </c:if>
             </c:forEach>            
            </div>
          </div>
        </div>
      </div>
      </c:if>
      <c:if test="${action=='update'}">
        <c:if test="${item.auditStatus==0 && code==5}">
	      <div class="panel panel-default margin_t10">
	        <div class="panel-heading">
	          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-5" role="button"> 
	              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
	              <span class="margin-r-5">审核</span>
	          </h3>
	        </div>
	        <div id="info-box-5" class="collapse in">
	          <div class="panel-body">
	            <textarea class="form-control" name="auditContent" rows="5" placeholder="请填写通过或不通过原因"></textarea>
	            <div class="margin_t10 text-right">
	                <button type="button" class="btn min-width-90px btn-warning margin_r10 btn-audit"  val="2">审核不通过</button>
	                <button type="button" class="btn min-width-90px btn-success btn-audit" val="1">审核通过</button>
	            </div>
	          </div>
	        </div>
      	 </div>
       </c:if>
      </c:if>
    </div>
   </form>
  </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
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

    $(".light-box").on('click', function(event) {
      var imgUrl=$(this).data('large-img');

      $.fancybox({
        'type'      : 'image',
        'href'      :imgUrl
        
      });
    });
  });
})();

//删除
$('body').on('click','[data-oper="remove"]',function(event) {
  event.preventDefault();
  $(this).closest('li').remove();
})
.on('click', '[data-role="view-cert"]', function(event) {
  event.preventDefault();
  $.alertDialog({
    id:'view-cert',
    title:'查看凭证',
    width:600,
    height:450,
    zIndex:11000,
    cancelLabel:'关闭',
    okLabel:'下载',
    ok:function(){//“确定”按钮的回调方法
      //这里 this 指向弹窗对象
      $.closeDialog(this);
    },
    content:'\
      <div class="text-center">\
        <img src="http://172.16.170.119:801/ouchgzee_com/platform/xllms_css/dist/img/user7-128x128.jpg" style="max-width:580px;max-height:320px;">\
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
    if(auditState == 1) {
        if(confirm("确定审核通过？")) {
            $("form#inputForm").submit();
        }          
    } else if(auditState == 2) {
        if(confirm("确定审核不通过？")) {
            $("form#inputForm").submit();
        }
    }
});
</script>
</body>
</html>
