<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<div id="pieChart3" data-role="chart" style="height:270px"></div>
<script type="text/javascript">

//缴费方式统计
var ecPie3=(function(){
  var obj={};
  obj.chart=echarts.init(document.getElementById('pieChart3'));
  obj.chart.setOption({
      legend: {
          top:10,
          right:10,
          orient:'vertical',
          data:['全额缴费','分期缴费','分学年缴费']
      },
      tooltip : {
          trigger: 'item',
          formatter: "{b} : {c} ({d}%)"
      },
      series : [
          {

              name: '缴费方式统计',
              type: 'pie',
              center: ['50%', '50%'],
              label:{
                normal:{
                  formatter: "{b}:{c}({d}%)"
                }
              },
              data:[
                  {value:335, name:'全额缴费',itemStyle:{
                      normal:{
                        color:'#3c8dbc'
                      }
                    }
                  },
                  {value:234, name:'分期缴费',itemStyle:{
                      normal:{
                        color:'#00a65a'
                      }
                    }
                  },
                  {value:134, name:'分学年缴费',itemStyle:{
                      normal:{
                        color:'#8b52cc'
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
	ecPie3.chart.resize();
});

</script>
