<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>定制课程</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li><a href="#">定制课程</a></li>
		<li class="active">定制课程详情</li>
	</ol>
</section>
<section class="content">
  	<div class="box">
      <div class="box-header with-border">
        <div class="pad-l20 pad-r20 pad-b10">
          <h4><strong>${teachPlan.gjtReplaceCourse.kcmc } (${teachPlan.gjtReplaceCourse.kch }) </strong></h2>
          <ul class="list-inline gray6 margin-bottom-none">
            <li class="text-nowrap margin_r15">
              开课学期：${teachPlan.gjtGrade.gradeName }
            </li>
            <li class="text-nowrap margin_r15">
              选课人数：<span id="chooseCount"></span>
            </li>
            <li class="text-nowrap">
              学院课程：${teachPlan.gjtCourse.kcmc } (${teachPlan.gjtCourse.kch })
            </li>
            <li class="text-nowrap">
              所属专业：<span id="specialty"></span>
            </li>
            <li class="text-nowrap">
              学习中心：<span id="orgName"></span>
            </li>
          </ul>
        </div>
      </div>
      <div class="box-body">
        <div class="row margin_t20 pad-b10">
          <div class="col-sm-10 col-sm-offset-1">
            <div class="progress-bar-h progress-blue clearfix">
              <div class="col-xs-4 pas">
                <div class="inner">
                	<label class="plan-bar" role="button">授课计划</label>
					<c:if test='${customCourse.planStatus ==0 }'><small  class="gray9">未上传</small></c:if>
					<c:if test='${customCourse.planStatus ==1 }'><small  class="text-orange">待审核</small></c:if>
					<c:if test='${customCourse.planStatus ==2 }'><small  class="text-green">审核通过</small></c:if>
					<c:if test='${customCourse.planStatus ==3 }'><small  class="text-orange">审核不通过</small></c:if>
                </div>
              </div>
              <div class="col-xs-4 <c:if test='${customCourse.planStatus ==2 }'>pas</c:if>">
                <div class="inner">
                  <label <c:if test='${customCourse.planStatus ==2 }'>class="certificate-bar" role="button"</c:if>>授课凭证</label>
                  <c:if test='${customCourse.planStatus ==2 }'>
                  <c:if test='${customCourse.certificateStatus ==0 }'><small  class="gray9">未上传<span id="uploadCount"></span></small></c:if>
                  <c:if test='${customCourse.certificateStatus ==1 }'><small  class="text-orange">上传中<span id="uploadCount"></span></small></c:if>
                  <c:if test='${customCourse.certificateStatus ==2 }'><small  class="text-green">已上传<span id="uploadCount"></span></small></c:if>
                  </c:if>
                </div>
              </div>
              <div class="col-xs-4 <c:if test='${customCourse.certificateStatus ==2 }'>pas</c:if>">
                <div class="inner">
                  <label <c:if test='${customCourse.scoreStatus !=0 }'>class="score-bar" role="button"</c:if>>授课成绩</label>
                  <c:if test='${customCourse.certificateStatus ==2 }'>
                  	<c:if test='${customCourse.scoreStatus ==0 }'><small  class="gray9">未上传</small></c:if>
                  	<c:if test='${customCourse.scoreStatus ==1 }'><small  class="text-orange">待审核</small></c:if>
                  	<c:if test='${customCourse.scoreStatus ==2 }'><small  class="text-green">审核通过</small></c:if>
                  	<c:if test='${customCourse.scoreStatus ==3 }'><small  class="text-orange">审核不通过</small></c:if>
                  	<c:if test='${customCourse.scoreStatus ==6 }'><small  class="text-orange">上传中</small></c:if>
                  </c:if>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- ################################################################################## -->
        <!-- 授课凭证 -->
        <c:if test="${not empty customCourse.plans }">
        <div class="margin_t20">
          <div class="clearfix margin_b10">
            <h3 class="cnt-box-title f16 text-bold ">授课凭证</h3>
            <input type="hidden" name="customCourseId" id="customCourseId" value="${customCourse.customCourseId }">
			
          </div>

          <div class="margin-bottom-none">
            <table class="table-gray-th text-center margin-bottom-none">
              <thead>
                <tr>
                  <th>课程主题</th>
                  <th>授课地点</th>
                  <th width="20%">授课时间</th>
                  <th>授课老师</th>
                  <th>授课凭证状态</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${customCourse.plans }" var="info">
                <tr>
                  <td>${info.courseTheme }</td>
                  <td>${info.addr }</td>
                  <td>
                  	<%-- <fmt:formatDate value="${info.startDate }" pattern="yyyy-MM-dd HH:mm"/> --%>
                  	<fmt:formatDate value="${info.startDate }" pattern="yyyy-MM-dd HH:mm"/>
                  	<br>~<br>
                  	<fmt:formatDate value="${info.endDate }" pattern="yyyy-MM-dd HH:mm"/>                
                  </td>
                  <td>${info.teacher }</td>
                  <td>
		          <c:choose>
		          	<c:when test="${info.certificateStatus == 0 }"><small class="gray9 statusItem">未上传</small></c:when>
		          	<c:when test="${info.certificateStatus == 1 }"><small class="gray9 statusItem">上传中</small></c:when>
		          	<c:when test="${info.certificateStatus == 2 }"><small class="text-green statusItem uploaded">已上传</small></c:when>
		          	<c:otherwise>--</c:otherwise>
		          </c:choose> 
                  </td>
                  <td>
                  	<!-- 0:未上传,1:上传中, 2:已上传 -->
                    <a href="/edumanage/custom/audit/toUploadCertificate/${info.grantCoursePlanId}" class="text-nowrap" data-role="upload-cert">
                    	<c:choose>
				          	<c:when test="${info.certificateStatus == 1 or info.certificateStatus == 2}">查看授课凭证</c:when>
				          	<c:otherwise>--</c:otherwise>
				        </c:choose> 
                    </a>
                  </td>
                </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
        </c:if>
    </div><!-- box-body end -->
</section>
<!-- 底部 -->
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>


<script type="text/javascript">

$(".box-body").find(".plan-bar").unbind("click").bind("click",function() {
	var customCourse = getCustomCourse();
	location.href="/edumanage/custom/audit/toAuditPlan/"+customCourse.customCourseId;
});
$(".box-body").find(".certificate-bar").unbind("click").bind("click",function() {
	var customCourse = getCustomCourse();
	location.href="/edumanage/custom/audit/toCertificate/"+customCourse.customCourseId;
});
$(".box-body").find(".score-bar").unbind("click").bind("click",function() {
	var customCourse = getCustomCourse();
	location.href="/edumanage/custom/audit/toAuditScore/"+customCourse.customCourseId;
});
function getCustomCourse() {
	var customCourseId = $("#customCourseId").val();
	var customCourse = JSON.parse(sessionStorage.getItem(customCourseId));
	if(customCourse == undefined) {
		customCourse = JSON.parse(sessionStorage.getItem("custom"));
	}
	return customCourse;
}
$(document).ready(function() {
	var customCourse = getCustomCourse();
	console.info(customCourse);
	$("#chooseCount").html(customCourse.chooseCount);
	$("#uploadCount").html(customCourse.uploadCount);
	$("#specialty").html(customCourse.specialty);
	$("#orgName").html(customCourse.orgName);
});

function audit(pass) {
	$("#pass").val(pass);
	$(".theform").submit();
}
//上传授课凭证
$('[data-role="upload-cert"]').on('click', function(event) {
  event.preventDefault();
  var _this=this;
  $.mydialog({
    id:'upload-cert',
    width:660,
    height:550,
    zIndex:11000,
    content: 'url:'+$(_this).attr('href')
  });
});
var toolTipsTemp='<div class="tooltip top" role="tooltip">'
    +'<div class="tooltip-arrow"></div>'
    +'<div class="tooltip-inner"></div>'
    +'</div>';

;
(function(){
  /*确认发送*/
  var $theform=$(".theform");
  $theform.find(":input[datatype]").each(function(index, el) {
    $(this).after(toolTipsTemp);
  });

   $.Tipmsg.r='';

  $theform.Validform({
    ajaxPost:true,
    showAllError:true,
    tiptype:function(msg,o,cssctl){
      //msg：提示信息;
      //o:{obj:*,type:*,curform:*},
      //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
      //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
      //curform为当前form对象;
      //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
      if(!o.obj.is("form")){
        var msgBox=o.obj.closest(".position-relative").children('.tooltip');

        msgBox.css({
            top:-30
          })
        .children('.tooltip-inner').text(msg);

        switch(o.type){
          case 3:
            msgBox.addClass('in');
            break;
          default:
            msgBox.removeClass('in');
            break;
        }
      }
    },
    beforeSubmit:function(curform){
      window.postIngIframe=$.mydialog({
  		  id:'dialog-1',
  		  width:150,
  		  height:50,
  		  backdrop:false,
  		  fade:true,
  		  showCloseIco:false,
  		  zIndex:11000,
  		  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
  		});
      
    },
    callback:function(data){
      //这里执行回调操作;
      //如果callback里明确return alse，则表单不会提交，如果return true或没有return，则会提交表单。
	  var result = data.result;
      if(result == "success") {
	   	  setTimeout(function(){//此句模拟交互，程序时请去掉
	   	        postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>')
	   		      /*关闭弹窗*/
	   		      setTimeout(function(){
	   		        $.closeDialog(postIngIframe);
	   		        $(".btn-cancel-edit").click();
	   		     	location.reload();
	   		      },2000)
	   	      },2000);//此句模拟交互，程序时请去掉  
      } else {
    	  setTimeout(function(){//此句模拟交互，程序时请去掉
	   	        postIngIframe.find(".text-center.pad-t15").html(data.message+'...<i class="icon fa fa-check-circle"></i>')
	   		      /*关闭弹窗*/
	   		      setTimeout(function(){
	   		        $.closeDialog(postIngIframe);
	   		        $(".btn-cancel-edit").click();
	   		        location.reload();
	   		      },2000)
	   	      },2000);//此句模拟交互，程序时请去掉 
      }
    }
  });
})();
</script>
</body>
</html>
