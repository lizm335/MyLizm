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
                  <label <c:if test='${customCourse.certificateStatus ==2 }'>class="score-bar" role="button"</c:if> >成绩</label>
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

        <!--授课计划-->
        <!-- ##########################--------未上传/审核不通过 -->
        <form class="theform" action="/edumanage/custom/course/createPlan" method="post">
		<input name="customCourseId" id="customCourseId" type="hidden" value="${customCourse.customCourseId }"  >
        <c:choose>
        	<c:when test="${customCourse.planStatus == 2 or customCourse.planStatus == 1}">
        		<!-- 授课计划 -->
        		<div class="margin_t20">
		          <div class="clearfix margin_b10">
		            <h3 class="cnt-box-title f16 text-bold ">授课计划</h3>
		          </div>
		
		          <div class="margin-bottom-none">
		            <table class="table-gray-th text-center margin-bottom-none">
		              <thead>
		                <tr>
		                  <th>课程主题</th>
		                  <th>授课地点</th>
		                  <th width="30%">授课时间</th>
		                  <th>授课老师</th>
		                </tr>
		              </thead>
		              <tbody>
		              	<c:forEach items="${customCourse.plans }" var="info">
		                <tr>
		                  <td>${info.courseTheme }</td>
		                  <td>${info.addr }</td>
		                  <td><fmt:formatDate value="${info.startDate }" pattern="yyyy-MM-dd HH:mm"/>  <br>~<br>
		                  		<fmt:formatDate value="${info.endDate }" pattern="yyyy-MM-dd HH:mm"/>                    
		                  </td>
		                  <td>${info.teacher }</td>
		                </tr>
		                </c:forEach>
		              </tbody>
		            </table>
		          </div>
		        </div>
        	</c:when>
        	<c:otherwise><!-- 未上传/审核不通过 -->
		        <div class="margin_t20">
		          <div class="clearfix margin_b10">
		            <h3 class="cnt-box-title f16 text-bold ">授课计划</h3>
		          </div>
		
		          <div class="margin-bottom-none">
		            <table class="table-gray-th text-center margin-bottom-none" data-role="set-model">
		              <thead>
		                <tr>
		                  <th>课程主题</th>
		                  <th>授课地点</th>
		                  <th width="30%">授课时间</th>
		                  <th>授课老师</th>
		                  <th width="10%">操作</th>
		                </tr>
		              </thead>
		              <tbody data-id="plan-container">
		              	<c:choose>
		              		<c:when test="${not empty customCourse.plans }" >
		              			<c:forEach items="${customCourse.plans }" var="info" varStatus="i">
		                		<tr>
				                  <td>
				                    <div class="position-relative">
				                      <input type="hidden"  name="plans[${i.index }].grantCoursePlanId" value="${info.grantCoursePlanId }" />
				                      <input name="plans[${i.index }].courseTheme" value="${info.courseTheme }" class="form-control" placeholder="请填写课程主题" datatype="*" nullmsg="请填写课程主题" errormsg="请填写课程主题">
				                    </div>
				                  </td>
				                  <td>
				                    <div class="position-relative">
				                      <input name="plans[${i.index }].addr" value="${info.addr }" class="form-control" placeholder="授课地点" datatype="*" nullmsg="请填写授课地点" errormsg="请填写授课地点">
				                    </div>
				                  </td>
				                  <td>
				                   <div class="input-group-custom" data-role="daterangetime-group">
				                      <div class="form-control-box position-relative">
				                        
				                        <input name="plans[${i.index }].startDateStr" 
				                        	value="<fmt:formatDate value='${info.startDate }' pattern='yyyy-MM-dd HH:mm'/>" type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写起始时间" errormsg="日期格式不对" data-role="date-start" readonly="">
				                      </div>
				                      <p class="input-group-addon">~</p>
				                      <div class="form-control-box position-relative">
				                        <input name="plans[${i.index }].endDateStr" 
				                        	value="<fmt:formatDate value='${info.endDate }' pattern='yyyy-MM-dd HH:mm'/>" type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写结束时间" errormsg="日期格式不对" data-role="date-end" readonly="">
				                      </div>
				                    </div>
				                  </td>
				                  <td>
				                    <div class="position-relative">
				                      <input name="plans[${i.index }].teacher" value="${info.teacher }" class="form-control" placeholder="授课老师" datatype="*" nullmsg="请填写授课老师" errormsg="请填写授课老师">
				                    </div>
				                  </td>
				                  <td></td>
				                </tr>
				                <c:set var="planSize" value="${i.count }"/>
				                </c:forEach>
				                <i id="planSize" data-i="${planSize }"></i>
		              		</c:when>
		              		<c:otherwise>
		              			<i id="planSize" data-i="3"></i>
		              			<c:forEach var="i" begin="0" end="2">
				              	<tr>
				                  <td>
				                    <div class="position-relative">
				                      <input name="plans[${i }].courseTheme" class="form-control" placeholder="请填写课程主题" datatype="*" nullmsg="请填写课程主题" errormsg="请填写课程主题">
				                    </div>
				                  </td>
				                  <td>
				                    <div class="position-relative">
				                      <input name="plans[${i }].addr"  class="form-control" placeholder="授课地点" datatype="*" nullmsg="请填写授课地点" errormsg="请填写授课地点">
				                    </div>
				                  </td>
				                  <td>
				                   <div class="input-group-custom" data-role="daterangetime-group">
				                      <div class="form-control-box position-relative">
				                        <input name="plans[${i }].startDateStr" type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写起始时间" errormsg="日期格式不对" data-role="date-start" readonly="">
				                      </div>
				                      <p class="input-group-addon">~</p>
				                      <div class="form-control-box position-relative">
				                        <input name="plans[${i }].endDateStr" type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写结束时间" errormsg="日期格式不对" data-role="date-end" readonly="">
				                      </div>
				                    </div>
				                  </td>
				                  <td>
				                    <div class="position-relative">
				                      <input name="plans[${i }].teacher" class="form-control" placeholder="授课老师" datatype="*" nullmsg="请填写授课老师" errormsg="请填写授课老师">
				                    </div>
				                  </td>
				                  <td></td>
				                </tr>	
				                </c:forEach>
		              		</c:otherwise>
		              	</c:choose>
		              </tbody>
		            </table>
		            <a href="javascript:;" role="button" class="btn btn-block text-green nobg margin_t5" data-role="add-plan"><i class="fa fa-fw fa-plus"></i>添加授课计划</a>
		          </div>
		        </div>
		
				<div class="box-footer">
				   <div class="pull-right"><button type="button" class="btn min-width-90px btn-default">取消</button></div>
				   <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">提交审核</button></div>
				</div>
        	</c:otherwise>
        </c:choose>
		</form>
        <!--审核记录-->
        <c:if test="${not empty customCourse.planAuditLines }">
        <div class="margin_t20">
          <div class="clearfix margin_b10 cnt-box-header with-border">
            <h3 class="cnt-box-title f16 text-bold ">审核记录</h3>
          </div>
          <div class="approval-list approval-list-2 approval-list-asc clearfix margin_b20">
			<c:forEach items="${customCourse.planAuditLines }" var="line">
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
        
        
      </div>
    </div>
</section>
<!-- 底部 -->
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<!--增加 授课计划 模板-->
<script type="text/template" id="tech-plan-temp">
  <tr>
    <td>
      <div class="position-relative">
        <input name="plans[#index#].courseTheme" class="form-control" placeholder="请填写课程主题" datatype="*" nullmsg="请填写课程主题" errormsg="请填写课程主题">
      </div>
    </td>
    <td>
      <div class="position-relative">
        <input name="plans[#index#].addr" class="form-control" placeholder="授课地点" datatype="*" nullmsg="请填写授课地点" errormsg="请填写授课地点">
      </div>
    </td>
    <td>
      <div class="input-group-custom" data-role="daterangetime-group">
        <div class="form-control-box position-relative">
          <input name="plans[#index#].startDateStr" type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写起始时间" errormsg="日期格式不对" data-role="date-start" readonly="">
        </div>
        <p class="input-group-addon">~</p>
        <div class="form-control-box position-relative">
          <input name="plans[#index#].endDateStr" type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写结束时间" errormsg="日期格式不对" data-role="date-end" readonly="">
        </div>
      </div>
    </td>
    <td>
      <div class="position-relative">
        <input  name="plans[#index#].teacher" class="form-control" placeholder="授课老师" datatype="*" nullmsg="请填写授课老师" errormsg="请填写授课老师">
      </div>
    </td>
    <td>
      <a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove-item"><i class="fa fa-trash-o text-red"></i></a>
    </td>
  </tr>
</script>
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
	console.info(customCourse.chooseCount);
	$("#chooseCount").html(customCourse.chooseCount);
	$("#uploadCount").html(customCourse.uploadCount);
	$("#specialty").html(customCourse.specialty);
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
      var $planList=$('[data-id="plan-container"]').children('tr');

      /* if($planList.length<3){
        $.resultDialog(
          {
            type:0,
            msg:'授课计划不能少于三个',
            timer:2000,
            width:250,
            height:50
          }
        );
        return false;
      } */

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
	        location.reload();
	      },2000)
      },2000);//此句模拟交互，程序时请去掉
    }
  });
})();

/*日期控件*/
$('[data-role="daterangetime-group"]').each(function(i,e){
  createDaterangetime(e);
});

//生成日期效果
function createDaterangetime($container){
  /*
    $container：日期容器
  */
  var startDate=$('[data-role="date-start"]',$container);
  var endDate=$('[data-role="date-end"]',$container);
  var config=$.extend(true,daterangepickerOpt,
    {
      format: "YYYY-MM-DD HH:mm",
      timePicker: true,
      timePicker12Hour: false,
      //drops:'up',
      buttonClasses:'btn btn-sm margin_l5 margin_r5'
    }
  );
  //开始时间      
  startDate.daterangepicker(config);

  startDate
  .on('show.daterangepicker', function(ev, picker) {
    picker.container.find('.ranges').css({
      width: 'auto',
      'text-align': 'center'
    });
  })
  .on('apply.daterangepicker', function(ev, picker) {
    $(ev.target).removeClass('Validform_error')
    .next('.tooltip').removeClass('in')
    .children('.tooltip-inner').empty();

    var endDaterangepicker=endDate.data('daterangepicker');
    endDaterangepicker.minDate=picker.startDate;
    endDaterangepicker.updateView();
    endDaterangepicker.updateCalendars();
  });

  //结束时间
  endDate.daterangepicker(config);

  endDate
  .on('show.daterangepicker', function(ev, picker) {
    picker.container.find('.ranges').css({
      width: 'auto',
      'text-align': 'center'
    });
  })
  .on('apply.daterangepicker',function(ev, picker) {
    $(ev.target).removeClass('Validform_error')
    .next('.tooltip').removeClass('in')
    .children('.tooltip-inner').empty();

    var startDaterangepicker=startDate.data('daterangepicker');
    startDaterangepicker.maxDate=picker.startDate;
    startDaterangepicker.updateView();
    startDaterangepicker.updateCalendars();
  });
}

//添加授课计划
var i = parseInt($("#planSize").attr("data-i"));

$('[data-role="add-plan"]').on('click', function(event) {
  event.preventDefault();
  var html = $('#tech-plan-temp').html().replace(/#index#/g,i);
  i += 1;
  var $item=$(html);
  $('[data-id="plan-container"]').append($item);
  createDaterangetime($item.find('[data-role="daterangetime-group"]'));

  $item.find(":input[datatype]").each(function(index, el) {
    $(this).after(toolTipsTemp);
  });
});

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

//查看授课凭证
$('[data-role="view-cert"]').on('click', function(event) {
  event.preventDefault();
  var _this=this;
  $.mydialog({
    id:'view-cert',
    width:660,
    height:550,
    zIndex:11000,
    content: 'url:'+$(_this).attr('href')
  });
});

//上传成绩
$('[data-id="upload-score"]').on('click', function(event) {
  event.preventDefault();
  var _this=this;
  $.mydialog({
    id:'view-cert',
    width:660,
    height:550,
    zIndex:11000,
    content: 'url:'+$(_this).attr('href')
  });
});

//删除授课计划
$('body').on('click', '[data-role="remove-item"]', function(event) {
  event.preventDefault();
  i-=1;
  $(this).closest('tr').remove();
});

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
            	var $p=this.wrap.parent();
            	if(!$p.is('body')){
            		$p.css('overflow', 'hidden');
            	}
            }
          });
    });
  });
})();
</script>
</body>
</html>