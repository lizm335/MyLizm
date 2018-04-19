<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<div id="pieChart2" data-role="chart" style="height:270px"></div>
<script type="text/javascript">

//缴费状态统计
var ecPie2=(function(){
var obj={};
obj.chart=echarts.init(document.getElementById('pieChart2'));
obj.chart.setOption({
   legend: {
       top:10,
       right:10,
       orient:'vertical',
       data:['已交清','待确认','已欠费']
   },
   tooltip : {
       trigger: 'item',
       formatter: "{b} : {c} ({d}%)"
   },
   series : [
       {

           name: '缴费状态统计',
           type: 'pie',
           center: ['50%', '50%'],
           label:{
             normal:{
               formatter: "{b}:{c}({d}%)"
             }
           },
           data:[
               {value:335, name:'已交清',itemStyle:{
                   normal:{
                     color:'#00a65a'
                   }
                 }
               },
               {value:234, name:'待确认',itemStyle:{
                   normal:{
                     color:'#f39c12'
                   }
                 }
               },
               {value:134, name:'已欠费',itemStyle:{
                   normal:{
                     color:'#f56954'
                   }
                 }
               }
           ],
           itemStyle: {
               normal:{
                 borderWidth:1,
                 borderColor:'#fff'
               },
               emphasis: {
                   shadowBlur: 10,
                   shadowOffsetX: 0,
                   shadowColor: 'rgba(0, 0, 0, 0.5)'
               }
           }
       }
   ]
});
return obj;
})();

$(window).bind("resize",function(){
  ecPie2.chart.resize();
});

</script>
