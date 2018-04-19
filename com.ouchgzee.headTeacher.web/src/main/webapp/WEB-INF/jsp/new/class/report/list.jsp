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
    <li class="active">工作计划</li>
  </ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

  <div class="box no-border margin-bottom-none">
    <div class="nav-tabs-custom margin-bottom-none">
      <ul class="nav nav-tabs">	
		<li class="active"><a href="${ctx}/home/class/report/list" data-toggle="tab">日报</a></li>
		<li><a href="#" id="toWeeklyId">周报</a></li>	
		<input type="hidden" name="beginTime" id="beginTime" value=""/>
        <input type="hidden" name="endTime" id="endTime" value=""/>	
      </ul>
      <div class="tab-content no-padding">
        <div class="tab-pane active" id="tab_1">
          <div id='calendar'></div>

        </div><!-- /.tab-pane         
      </div><!-- /.tab-content -->
    </div>
    
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script src="${ctx}/static/plugins/fullcalendar/fullcalendar.min.js"></script>
<script src="${ctx}/static/plugins/fullcalendar/zh-cn.js"></script>
<script type="text/javascript">

(function($){
var vWeekOfDay=moment().format("E");//算出这周的周几
var vStartDate=moment().add(-vWeekOfDay+1,'days');
var vEndDate=moment().add(7-vWeekOfDay,'days');
$("#beginTime").val(vStartDate.format("YYYY")+"-"+vStartDate.format("MM-DD"));
$("#endTime").val(vStartDate.format("YYYY")+"-"+vEndDate.format("MM-DD"));
$("#toWeeklyId").attr("href","${ctx}/home/class/report/weeklyList/"+$("#beginTime").val());
var monthNum=0;

/**** 日报 ****/
var dateJSONS = ${dateJSON};
var dateTime = new Date();	
$('#calendar').fullCalendar({
  header: {
    left: 'prev',
    center: 'title',
    right: 'next'
  },
  weekMode:'liquid',  
  events: function(event) {	  
  	  if ($($(".fc-prev-button")[0].outerHTML).hasClass("fc-state-hover")){
  		  	monthNum--;
  		}
  	  if ($($(".fc-next-button")[0].outerHTML).hasClass("fc-state-hover")){
 			monthNum++;
 	    }
  	ajaxGetReportDaily();  
    },
  viewRender:function( view, element ){	  
    $(".fc-bg",element).remove();
    $(".fc-day-number",element).each(function(index,ele) {
      var $that=$(this);
      var strCls='fc-day-number-td-'+index
      $that.addClass(strCls);  	 
	 var flag = true;
      $.each(dateJSONS,function(i, n) {      	
        if(moment($that.data("date")).format("YYYY-MM-DD")==moment(n.date).format("YYYY-MM-DD")){

          $that.append($('<div class="view-more-daily"/>').html('<a href="toViewReport/'+n.id+'">查看日报</a>'));

          var tHtml=[
            '<div class="popover top">',
              '<div class="arrow"></div>',
              '<h3 class="popover-title">',
                '<span class="label text-no-bold pull-right '+(n.hasRemark?'bg-green':'label-default')+'">'+(n.hasRemark?'已':'未')+'点评</span>',
                n.date,
              ' 日报</h3>',
              '<div class="popover-content f12">',
                '<div class="gray9">'+n.eventDes+'</div>',
                '<div class="text-right">',
                  '<a href="toViewReport/'+n.id+'">详情&gt;</a>',
                '</div>',
              '</div>',
            '</div>'
          ];
          $that.append(tHtml.join(""));
        }
        if(moment(n.date).format("YYYY-MM-DD")==moment(dateTime).format("YYYY-MM-DD")){
        	flag = false;
        } 
      });
      if($that.hasClass('fc-today') && flag){
          $that.append($('<div class="add-daily margin_t10"></div>').html('<a href="${ctx}/home/class/report/toCreateReport" role="button" class="btn btn-block btn-success btn-xs">新建日报</a>'));
        }
      $that.wrapInner('<div class="extra-wrapper"></div>');
    });    
    element
    .on("mouseenter",".fc-day-number",function(){
      $(this).addClass('on');
    })            
    .on("mouseleave",".fc-day-number",function(){
      $(this).removeClass('on')
    });

    $(".fc-content-skeleton",element).each(function(index, el) {
      var $that=$(this);
      var $thead=$("thead",$that);

      $("table",$that)
      .append($("<tbody/>").html($thead.html()));
      
      $thead.remove();
      setTimeout(function(){
        if($("tbody",$that).length>1)
        $("tbody:last",$that).remove();
      },10);
    });
  },
  lang: 'zh-cn'
});
function ajaxGetReportDaily(){
	  $.ajax({ 
        type:"POST", 
        url:"${ctx}/home/class/report/ajaxList", 
        data:"monthNum="+monthNum,
        dataType: "json",
        async: false,
        success:function(data){ 
        	dateJSONS = JSON.parse(data); 
        }
    });
}
})(jQuery);
</script>
</body>
</html>