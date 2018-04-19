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
	<input name="customCourseId" id="customCourseId" type="hidden" value="${customCourse.customCourseId }"  >
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
                  <label <c:if test="${customCourse.planStatus ==2 }">class="certificate-bar" role="button"</c:if>>授课凭证</label>
                  <c:if test='${customCourse.planStatus ==2 }'>
                  	<c:if test='${customCourse.certificateStatus ==0 }'><small  class="gray9">未上传<span id="uploadCount"></span></small></c:if>
                  	<c:if test='${customCourse.certificateStatus ==1 }'><small  class="text-orange">上传中<span id="uploadCount"></span></small></c:if>
                  	<c:if test='${customCourse.certificateStatus ==2 }'><small  class="text-green">已上传<span id="uploadCount"></span></small></c:if>
                  </c:if>
                </div>
              </div>
              <div class="col-xs-4 <c:if test='${customCourse.certificateStatus ==2 }'>pas</c:if>">
                <div class="inner">
                  <label <c:if test='${customCourse.certificateStatus ==2 }'>class="score-bar" role="button"</c:if>>成绩</label>
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
        <!--授课成绩-->
        <c:choose>
        <c:when test="${customCourse.scoreStatus != 0 }">
	        <div class="margin_t20">
	          <div class="clearfix margin_b10">
	          	<c:if test="${customCourse.scoreStatus != 2 && customCourse.scoreStatus != 1}">
		           <a data-href="/edumanage/custom/course/upload?type=score&id=${customCourse.customCourseId }" role="button" 
		           	class="btn btn-primary btn-sm pull-right margin_l10" data-id="upload-score">上传成绩表</a>
		           	
	            </c:if>
	            <div class="margin_t5">
	              <h3 class="cnt-box-title f16 text-bold ">成绩表</h3>
	            </div>
	          </div>
	          <div class="margin-bottom-none">
	            <table class="table-gray-th text-center">
	              <thead>
	                <tr>
	                  <th>学生姓名</th>
	                  <th>正式学号</th>
	                  <th>平时成绩</th>
	                  <th>期末成绩</th>
	                </tr>
	              </thead>
	              <tbody>
	                <c:forEach items="${customCourse.scores }" var="v">
	                <tr>
	                  	<td>${v.studentName }</td>
						<td>${v.studentNo }</td>
						<td>${v.usualScore }</td>
						<td>${v.terminalScore }</td>
	                </tr>
	                </c:forEach>
	              </tbody>
	            </table>
	          </div>
	        </div>
	        
	        <!-- 成绩扫描件 -->
	        <div class="margin_t20">
	          <div class="clearfix">
	          	<c:if test="${customCourse.scoreStatus != 2 && customCourse.scoreStatus != 1}">
		            <a data-href="/edumanage/custom/course/toUploadScore/${customCourse.customCourseId }"  role="button" 
		           		class="btn btn-primary btn-sm pull-right margin_l10" data-id="upload-score">上传成绩扫描件</a>
	            </c:if>
	            <div class="margin_t5">
	            	<h3 class="cnt-box-title f16 text-bold ">成绩扫描件</h3>
	            </div>
	          </div>
	          <div class="margin-bottom-none">
	          	<form id="imageForm" class="form-horizontal"  method="post">
	            <ul class="list-inline img-gallery img-gallery-md upload-box" style="margin-right:-5px;">
	              <c:forEach items="${customCourse.images }" var="image">
					<li>
						<img src="${image }">
						<span class="glyphicon <c:if test='${customCourse.scoreStatus != 2 && customCourse.scoreStatus != 1}'>glyphicon-remove-sign</c:if>" data-toggle="tooltip" data-html="true" title="" data-original-title="<div style='width:40px;'>删除</div>"></span>
						<a href="#" class="img-fancybox f12" data-large-img="${image }">点击放大</a>
					</li>
	      		</c:forEach>
	            </ul>
	            </form>
	          </div>
	        </div>
	        <c:if test="${customCourse.scoreStatus == 6 or customCourse.scoreStatus == 3 }">
		        <div class="text-center">
		         	<a href="#" role="button" class="btn btn-lg btn-warning" 
		         		onclick="submitAudit()">提交审核</a>
		        </div>
	        </c:if>
        </c:when>
        <c:otherwise>
        	<!-- 授课成绩 -->
			 <div class="margin_t20">
	          <div class="clearfix margin_b10">
	            <h3 class="cnt-box-title f16 text-bold ">成绩</h3>
	          </div>
	          <div class="margin-bottom-none">
	            <table class="table-gray-th text-center margin-bottom-none" height="250">
	              <tbody>
	                <tr>
	                  <td>
	                    <div>
	                      <i class="fa fa-fw fa-exclamation-circle text-red vertical-middle" style="font-size:40px;"></i>
	                      <span class="inline-block vertical-middle f16">还未上传成绩，请点击上传按钮上传成绩</span>
	                    </div>
	                    <div class="gray6 margin_t30">
							${teachPlan.gjtCourse.kcmc }需要在开课结束:
							<c:choose>
								<c:when test="${not empty startDate }"><fmt:formatDate value="${startDate }" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
								<c:otherwise>--</c:otherwise>
							</c:choose>
							后，<br>登记平时成绩截止：
							<c:choose>
								<c:when test="${not empty endDate }"><fmt:formatDate value="${endDate }" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
								<c:otherwise>--</c:otherwise>
							</c:choose>
							前进行上传成绩操作
	                    </div>
	                    <div class="gray6 margin_t30">
	                    <c:if test="${isUploadScore }">
		                    <button type="button" class="btn btn-primary" data-href="/edumanage/custom/course/upload?type=score&id=${customCourse.customCourseId }" 
		                    	data-id="upload-score" style="width:150px;">上传成绩表</button>
		                    
		                    <button type="button" class="btn btn-primary" data-href="/edumanage/custom/course/toUploadScore/${customCourse.customCourseId }" 
		                    	data-id="upload-score" style="width:150px;">上传成绩扫描件</button>
	                    </c:if>
	                    </div>
	                  </td>
	                </tr>
	              </tbody>
	            </table>
	          </div>
	        </div>
        </c:otherwise>
        </c:choose>
        
        <!--审核记录-->
        <c:if test="${not empty customCourse.scoreAuditLines }">
        <div class="margin_t20">
          <div class="clearfix margin_b10 cnt-box-header with-border">
            <h3 class="cnt-box-title f16 text-bold ">审核记录</h3>
          </div>
          <div class="approval-list approval-list-2 approval-list-asc clearfix margin_b20">
			<c:forEach items="${customCourse.scoreAuditLines }" var="line">
			<dl class="approval-item">
				<c:choose>
					<c:when test="${empty line.auditContent }">
						<dt class="clearfix">
							<div class="a-tit">
								<b>${line.operate }</b>
								<small class="text-no-bold">${line.operatorRole }：${line.operatorName }</small>
							</div>
							<span class="time state-lb"><fmt:formatDate value="${line.operateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
						</dt>
					</c:when>
					<c:otherwise>
						<dt class="clearfix">
							<div class="a-tit">
								<b>${line.operate }</b>
								<c:choose>
									<c:when test="${line.auditStatus.name == '审核通过' }">
									<small class="label bg-green text-no-bold margin_l10">${line.auditStatus.name }</small>
									</c:when>
									<c:otherwise>
									<small class="label bg-red text-no-bold margin_l10">${line.auditStatus.name }</small>
									</c:otherwise>
								</c:choose>
							</div>
							<span class="time state-lb"><fmt:formatDate value="${line.operateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
						</dt>
						<dd>
						<div class="txt">
						<p>${line.auditContent }</p>
						<div class="gray9 text-right">审批人：${line.operatorName }</div>
						<i class="arrow-top"></i>
						</div>
						</dd>
					</c:otherwise>
				</c:choose>
				<dd class="decoration"></dd>
			</dl>
			</c:forEach>
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
	location.href="/edumanage/custom/course/toPlan/"+customCourse.orgId+"/"+customCourse.teachPlanId;
});

$(".box-body").find(".certificate-bar").unbind("click").bind("click",function() {
	var customCourse = getCustomCourse();
	location.href="/edumanage/custom/course/toCertificate/"+customCourse.customCourseId;
});
$(".box-body").find(".score-bar").unbind("click").bind("click",function() {
	var customCourse = getCustomCourse();
	location.href="/edumanage/custom/course/toScore/"+customCourse.customCourseId;
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
});



function submitAudit() {
	$.ajax({
		url:"/edumanage/custom/course/submitAudit",
		async:false,
		data:"customCourseId="+$("#customCourseId").val(),
		dataType:"text",
		type:"post",
		success:function(data, textStatus, jqXHR) {
			console.info(data)
			if(data =='\"success\"') {
				alert("提交成功!");
				location.reload();
			} else {
				alert(data);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			alert("请求失败!");
		}
	});
}


var toolTipsTemp='<div class="tooltip top" role="tooltip">'
    +'<div class="tooltip-arrow"></div>'
    +'<div class="tooltip-inner"></div>'
    +'</div>';

;(function(){
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

      //if("成功提交") 就执行下面语句
      setTimeout(function(){//此句模拟交互，程序时请去掉
        postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>')
	      /*关闭弹窗*/
	      setTimeout(function(){
	        $.closeDialog(postIngIframe);
	        $(".btn-cancel-edit").click();
	      },2000)
      },2000);//此句模拟交互，程序时请去掉
    }
  });
})();

//上传成绩
$('[data-id="upload-score"]').on('click', function(event) {
  event.preventDefault();
  var _this=this;
  $.mydialog({
    id:'upload-score',
    width:660,
    height:550,
    zIndex:11000,
    content: 'url:'+$(_this).attr('data-href')
  });
});


//图片放大
(function(){
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
            	var $p=this.wrap.parent();
            	if(!$p.is('body')){
            		$p.css('overflow', 'hidden');
            	}
            }
          });
    });
  });
})();
/*删除图片*/
$(".img-gallery").on('click', '.glyphicon-remove-sign', function(event) {
	event.preventDefault();
	var imgSrc = $(this).parent().find("img").attr("src");
	var t = $(this);
	$.ajax({
		url:"/edumanage/custom/course/removeScoreImage",
		async:false,
		data:{customCourseId:$("#customCourseId").val(),"imgSrc":imgSrc},
		dataType:"text",
		type:"post",
		success:function(data, textStatus, jqXHR) {
			//successTip(postIngIframe);
  			t.closest('li').remove();
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			alert("请求失败!");
		}
	});
		
})

function errorTip(postIngIframe) {
	setTimeout(function(){
		$.closeDialog(postIngIframe);
		/*失败状态*/
		$.when(
			$.resultDialog(
				{
					type:2,
					msg:'操作失败！',
					timer:1500,
					width:150,
					height:50
				}
			)
		)
		.done(function(){
            //parent.$.closeDialog(frameElement);
            parent.location.reload();
        });
	},2000);
}

function successTip(postIngIframe) {
	setTimeout(function(){
		$.closeDialog(postIngIframe);
		/*成功状态*/
		$.when(
			$.resultDialog(
				{
					type:1,
					msg:'操作成功！',
					timer:1500,
					width:150,
					height:50
				}
			)
		)
		.done(function(){
          location.href=location.href;
        });
	},2000);
}
</script>
</body>
</html>
