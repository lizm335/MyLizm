<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>工作计划</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
<link rel="stylesheet" href="${ctx}/static/plugins/fullcalendar/fullcalendar.min.css">
<link rel="stylesheet" href="${ctx}/static/plugins/fullcalendar/fullcalendar.print.css" media="print">
</head>
<body class="inner-page-body">
<section class="content-header">
  <h1>
  工作计划
  </h1>
  <ol class="breadcrumb">
    <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
    <li><a href="${ctx}/home/class/report/list">工作计划</a></li>
  </ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

  <div class="box no-border margin-bottom-none">
    <div class="nav-tabs-custom margin-bottom-none">
      <ul class="nav nav-tabs">
		<li><a href="${ctx}/home/class/report/list">日报</a></li>
		<li class="active"><a id="weekly" href="#" onclick="" data-toggle="tab">周报</a></li>
		
		<input type="hidden" name="beginTime" id="beginTime" value=""/>
              <input type="hidden" name="endTime" id="endTime" value=""/>
	  </ul>
      <div class="tab-content no-padding">        
        <div class="tab-pane active" id="tab_2">        
          <div class="week-calender">
          <div class="fc week-calender-top">
              <div class="fc-toolbar">
                <div class="fc-left"><button type="button" class="wc-prev fc-button fc-state-default fc-corner-left fc-corner-right"><span class="fc-icon fc-icon-left-single-arrow"></span></button></div>
                <div class="fc-right"><button type="button" class="wc-next fc-button fc-state-default fc-corner-left fc-corner-right"><span class="fc-icon fc-icon-right-single-arrow"></span></button></div>
                <div class="fc-center wc-center">
                  <div class="wc-t-txt">
                    <h2 class="wc-t">
                      <span class="wc-year">2016年</span>
                      第<span class="wc-wc-week">29周</span>
                    </h2>
                    <div class="wc-t-b">
                      <span class="wc-start-week">7月18日</span>
                      —
                      <span class="wc-end-week">7月24日</span>
                    </div>
                  </div>
                  <div class="wc-calender-box"></div>
                </div>
                <div class="fc-clear"></div>
              </div>
            </div>
            <div class="reportChoice">
          <c:choose>	        	
		  <c:when test="${not empty gjtReportList && gjtReportList.numberOfElements > 0}">
		  <c:forEach items="${gjtReportList.content}" var="info">
		  <c:if test="${not empty info}">
		  <c:if test="${not empty info.commententId}">
              <!-- 已点评状态 -->
              <div class="week-calender-has-comment">
                <div class="pad30 box-body">
                  <h5 class="text-bold">本周总结</h5>
                  <div class="gray6 summary">
                 ${info.summary}
                  </div>

                  <h5 class="text-bold margin_t20 pad-t15">下周计划</h5>
                  <div class="gray6 nextplan">
                    ${info.nextplan}
                  </div>

                  <div class="gray9 margin_t20 pad-t15 createdDt">
                  我   ${info.createdDt}
                  </div>
                </div>
                <div class="box-footer pad-l30 pad-r30">
                  <ul class="report-list list-unstyled">
                    <li>
                      <div>
                        <span class="label label-primary">点评</span>
                        <span class="gray9 margin_l10 commententName">${info.commententName} ${info.updatedDt}</span>
                      </div>
                      <div class="pad-t10 comments">${info.comments}</div>
                    </li>
                    
                  </ul>
                </div>
              </div>
			</c:if>
			<c:if test="${empty info.commententId}">
			<!-- 未点评状态 -->
              <div class="week-calender-no-comment">
                <div class="pad30 box-body">
                  <h5 class="text-bold">本周总结</h5>
                  <div class="gray6 summary">
                  ${info.summary}
                  </div>

                  <h5 class="text-bold margin_t20 pad-t15">下周计划</h5>
                  <div class="gray6 nextplan">
                    ${info.nextplan}
                  </div>

                  <div class="gray9 margin_t20 pad-t15 createdDt">
                      我  ${info.createdDt}
                  </div>
                </div>
                <div class="box-footer pad-r20 pad-l20">
                  <div class="text-right">
                    <button class="btn btn-primary min-width-90px" 
                    onclick="window.location.href='${ctx}/home/class/report/toUpdateReportWeekly/${info.id}'">编辑</button>
                  </div>
                </div>
              </div>
              </c:if>
			</c:if>
			</c:forEach>
			</c:when>
			<c:otherwise>			
            <div class="week-calender-body">
              <!-- 没写周报状态 -->
              <div class="text-center" style="padding: 20px 0 60px;">
                <h3 class="gray9">本周周报还未撰写！</h3>
                <a href="${ctx}/home/class/report/toCreateReportWeekly" role="button" class="btn btn-primary min-width-90px margin_t10">写周报</a>
              </div>		
              </div>	
              </c:otherwise>
              </c:choose>
              </div>
            </div>
          </div>
        </div><!-- /.tab-pane -->
      </div><!-- /.tab-content -->
    </div>
    
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/template" id="addReport">
<div class="week-calender-body">
   <!-- 没写周报状态 -->
    <div class="text-center" style="padding: 20px 0 60px;">
        <h3 class="gray9">本周周报还未撰写！</h3>
      <a href="${ctx}/home/class/report/toCreateReportWeekly" role="button" class="btn btn-primary min-width-90px margin_t10">写周报</a>
    </div>		
</div>
</script>
<script type="text/template" id="infoReportCommants">
				<!-- 已点评状态 -->
              <div class="week-calender-has-comment">
                <div class="pad30 box-body">
                  <h5 class="text-bold">本周总结</h5>
                  <div class="gray6 summary">
                 ${info.summary}
                  </div>

                  <h5 class="text-bold margin_t20 pad-t15">下周计划</h5>
                  <div class="gray6 nextplan">
                    ${info.nextplan}
                  </div>

                  <div class="gray9 margin_t20 pad-t15 createdDt">
                    ${info.createdDt}
                  </div>
                </div>
                <div class="box-footer pad-l30 pad-r30">
                  <ul class="report-list list-unstyled">
                    <li>
                      <div>
                        <span class="label label-primary">点评</span>
                        <span class="gray9 margin_l10 commententName">${info.commententName} ${info.updatedDt}</span>
                      </div>
                      <div class="pad-t10 comments">${info.comments}！</div>
                    </li>
                    
                  </ul>
                </div>
              </div>
</script>
<script type="text/template" id="infoReportNotCommants">
<div class="week-calender-no-comment">
                <div class="pad30 box-body">
                  <h5 class="text-bold">本周总结</h5>
                  <div class="gray6 summary">
                  ${info.summary}
                  </div>

                  <h5 class="text-bold margin_t20 pad-t15">下周计划</h5>
                  <div class="gray6 nextplan">
                    ${info.nextplan}
                  </div>

                  <div class="gray9 margin_t20 pad-t15 createdDt">
                       ${info.createdDt}
                  </div>
                </div>
              </div>
</script>
<script type="text/template" id="infoReportNotCommantsCurrent">
<div class="week-calender-no-comment">
                <div class="pad30 box-body">
                  <h5 class="text-bold">本周总结</h5>
                  <div class="gray6 summary">
                  ${info.summary}
                  </div>

                  <h5 class="text-bold margin_t20 pad-t15">下周计划</h5>
                  <div class="gray6 nextplan">
                    ${info.nextplan}
                  </div>

                  <div class="gray9 margin_t20 pad-t15 createdDt">
                       ${info.createdDt}
                  </div>
                </div>
                <div class="box-footer pad-r20 pad-l20">
                  <div class="text-right">
                    <a class="btn btn-primary min-width-90px" id="updateWeeklyReport"
                    >编辑</a>
                  </div>
                </div>
              </div>
</script>
<script src="${ctx}/static/plugins/fullcalendar/fullcalendar.min.js"></script>
<script src="${ctx}/static/plugins/fullcalendar/zh-cn.js"></script>
<script type="text/javascript">
(function($){
/**** 周报 ****/
  var vWeekOfYear=moment().format("W");//算出这年的第几周
  var vWeekOfDay=moment().format("E");//算出这周的周几
  var weekIndex=vWeekOfYear;  
  function jumpWeek(n){

    n=n==undefined?vWeekOfYear:n;
    weekIndex=n;

    var vStartDate=moment().add(-vWeekOfDay+1+(n-vWeekOfYear)*7,'days');
    var vEndDate=moment().add((7-vWeekOfDay)+(n-vWeekOfYear)*7,'days');
    $("#beginTime").val(vStartDate.format("YYYY")+"-"+vStartDate.format("MM-DD"));
    $("#endTime").val(vStartDate.format("YYYY")+"-"+vEndDate.format("MM-DD"));    
    $(".wc-start-week",$(".week-calender")).html(vStartDate.format("M月D日"));
    $(".wc-end-week").html(vEndDate.format("M月D日"));
    $(".wc-wc-week").html(vStartDate.format("W周"));
    $(".wc-year").html(vStartDate.format("YYYY年"));

    if(n>=vWeekOfYear){
      $(".week-calender-top .fc-right").hide();
    }
    else{
      $(".week-calender-top .fc-right").show();
    }
  }

  jumpWeek();

  var $wcCalenderBox=$('.wc-calender-box');

  $(".wc-prev").click(function(event) {
    jumpWeek(--weekIndex);
    $wcCalenderBox.datepicker('setDate',moment().add((weekIndex-vWeekOfYear)*7,'days').format("YYYY-MM-DD"));
    ajaxGetReport();
  });
  $(".wc-next").click(function(event) {
    jumpWeek(++weekIndex);
    $wcCalenderBox.datepicker('setDate',moment().add((weekIndex-vWeekOfYear)*7,'days').format("YYYY-MM-DD"));
    ajaxGetReport();
  });
  function ajaxGetReport(){
	  $.ajax({ 
	        type:"POST", 
	        url:"${ctx}/home/class/report/weeklyInfo", 
	        data:"beginTime="+$("#beginTime").val()+"&endTime="+$("#endTime").val(),
	        dataType: "json",
	        success:function(data){ 	        	
	        	if(data!="{}"){
		        	var obj = JSON.parse(data);
		        	if(moment().format("W周")==$(".wc-wc-week").html()){
	        			$(".reportChoice").html($("#infoReportNotCommantsCurrent").html());
	        		}else{
		        		$(".reportChoice").html($("#infoReportNotCommants").html());
	        		}
		        	
		        	//JSON.stringify(data);
		        	var row = "summary_nextplan_createdDt_beginTime_endTime_commententName_updatedDt_comments";
		        	showData(obj,row);
		        	$("#updateWeeklyReport").attr("href","${ctx}/home/class/report/toUpdateReportWeekly/"+obj.id);
	        	}else{
	        		if(moment().format("W周")==$(".wc-wc-week").html()){
	        			$(".reportChoice").html($("#addReport").html());
	        		}
	        		else{
	        			$(".reportChoice").html($("#infoReportNotCommants").html());	        			
	        		}
	        	}
	        }
      });
  }
  function showData(obj,row){
	  var rows = row.split("_");
	  for(var i=0;i<rows.length;i++){
		  if(obj[rows[i]]!=undefined){
			  if(rows[i]=="createdDt"){
				  $("."+rows[i]).html("我"+obj[rows[i]]);
			  }else{
		 		$("."+rows[i]).html(obj[rows[i]]);
			  }
		  }
	  	  else{	  		  
	  		$("."+rows[i]).html("");
	  	  }
	  }
  }
  /*日期控件*/
  $wcCalenderBox.datepicker({
    language:'zh-CN',
    todayHighlight:true,
    endDate:"+"+(7-vWeekOfDay)+'d',
    format:'yyyy-mm-dd'
  }).on("changeDate", function(e) {
      jumpWeek(moment(e.date).format('W'));
      $(this).hide();
      $(this).datepicker('update', '');
      ajaxGetReport();
      
  });

  $(".wc-center").hover(
    function(event) {
      $wcCalenderBox.stop(true,true).fadeIn("fast");
    },
    function(event) {
      $wcCalenderBox.hide();
    }
  );

})(jQuery);

</script>
</body>
</html>