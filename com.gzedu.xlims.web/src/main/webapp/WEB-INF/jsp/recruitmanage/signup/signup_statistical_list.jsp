<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>>报读信息</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
 <script src="${ctx}/static/plugins/echarts/echarts.min.js"></script>
 <script type="text/javascript" src="${ctx}/static/plugins/echarts/china.js"></script>
</head>
<body class="inner-page-body">
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li>招生管理</li>
		<li class="active">报读统计</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<!--
			<li><a href="#" data-toggle="tab" onclick="changType('1')">报读信息</a></li>
			<li class="active"><a href="#" data-toggle="tab" onclick="changType('2')">报读统计</a></li>
			-->
		<!-- 
			<li ><a href="${ctx}/recruitmanage/signup/list" >报读信息</a></li>
			<li class="active"><a href="${ctx}/recruitmanage/signup/statistics"  >报读统计</a></li>
		-->
		</ul>
		<div class="tab-content">			
			<div class="tab-pane active" id="tab_top_2">
				<div class="row">
		          <div class="col-md-6">
		            <!-- 报读资料统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">报读资料统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px" id="signupInfo">
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" id="gradeId" name="gradeId" data-live-search="true" style="width:100px">
		                          <option value="">全部学期</option>
		                          <c:forEach items="${gradeMap}" var="map">
				                  	<option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
				                  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="pycc" id="pycc" data-live-search="true" style="width:100px">
		                         <option value="">全部层次</option>
				                 <c:forEach items="${pyccMap}" var="map">
	                  				<option value="${map.key}">${map.value}</option>
	                  			 </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="signupSpecialtyId" id="signupSpecialtyId" data-live-search="true" style="width:100px">
		                           <option value="">全部专业</option>
				                  <c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}">${map.value}</option>
					  			  </c:forEach>		                          
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="ecBar4" class="sigunpinfo" data-role="chart" style="height:300px"></div>
		              </div>
		            </div>
		          </div>
		          <div class="col-md-6">
		            <!-- 报读缴费统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">报读缴费统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px" id="payCost">
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="gradeId" id="payCost_gradeId">
		                          <option value="">全部学期</option>
		                          <c:forEach items="${gradeMap}" var="map">
				                  <option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
				                  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="pycc" id="payCost_pycc">
		                          	<option value="">全部层次</option>
				                 	<c:forEach items="${pyccMap}" var="map">
	                  					<option value="${map.key}" >${map.value}</option>
	                  			 	</c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="signupSpecialtyId" id="payCost_specialtyId">
		                         <option value="">全部专业</option>
				                  <c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
					  			  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="ecBar5" data-role="chart" style="height:300px"></div>
		              </div>
		            </div>
		            
		          </div>
		        </div>

		        <div class="row">
		          <div class="col-md-6">
		            <!-- 学习中心统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">学习中心统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px" id="signUpCenter">
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                       <select class="form-control input-xs" name="CENTER_GRADE_ID" id="center_grade_id" data-live-search="true">
		                          <option value="">全部学期</option>
		                          <c:forEach items="${gradeMap}" var="map">
				                  		<option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
				                  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="CENTER_PYCC_ID" id="center_pycc_id" data-live-search="true">
		                          <option value="">全部层次</option>
		                          <c:forEach items="${pyccMap}" var="map">
	                  				<option value="${map.key}">${map.value}</option>
	                  			 </c:forEach>
		                        </select>
		                      </div>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="CENTER_SPECIALTY_ID" id="center_specialty_id" data-live-search="true">
		                          <option value="">全部专业</option>
		                          <c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
					  			  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="mapChart1" data-role="chart" style="height:300px"></div>
		              </div>
		            </div>
		          </div>

		          <div class="col-md-6">
		            <!-- 学历层次统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">学历层次统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px" id="signUpPycc">
		                  <form>
		                    <div class="col-sm-4 col-sm-offset-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="PYCC_GRADE_ID" id="pycc_grade_id" data-live-search="true">
		                          <option value="">全部学期</option>
		                          <c:forEach items="${gradeMap}" var="map">
				                  		<option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
				                  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="PYCC_SPECIALTY_ID" id="pycc_specialty_id" data-live-search="true">
		                          <option value="">全部专业</option>
		                          <c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
					  			  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    </form>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="pieChart2" data-role="chart" style="height:270px"></div>
		              </div>
		            </div>
		          </div>
		          
		        </div>

		        <div class="row">

		          <div class="col-md-6">
		            <!-- 用户属性统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">用户属性统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px" id="signUpUser">
		                  <form>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="USER_GRADE_ID" id="user_grade_id" data-live-search="true">
		                          <option value="">全部学期</option>
		                          <c:forEach items="${gradeMap}" var="map">
				                  		<option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
				                  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="USER_PYCC_ID" id="user_pycc_id" data-live-search="true">
		                          <option value="">全部层次</option>
		                          <c:forEach items="${pyccMap}" var="map">
	                  				<option value="${map.key}">${map.value}</option>
	                  			 </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="USER_SPECIALTY_ID" id="user_specialty_id" data-live-search="true">
		                          <option value="">全部专业</option>
		                          <c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
					  			  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    </form>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <h3 class="cnt-box-title f16 margin_b10">性别</h3>
		                <div class="row">
		                  <div class="col-xs-8 per-icon-box margin_t10" id="femalePic">
		                  <!--  
		                    <i class="fa fa-fw fa-female text-red"></i>
		                    <i class="fa fa-fw fa-female text-red"></i>
		                    <i class="fa fa-fw fa-female text-red"></i>
		                    <i class="fa fa-fw fa-female text-red"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                   -->
		                  </div>
		                  <div class="col-xs-2 text-red text-nowrap">
		                    <div class="f24"><span id="femalePercent">0</span>%</div>
		                    <div>女:<span id="female">0</span>人</div>
		                  </div>
		                </div>
		                <div class="row">
		                  <div class="col-xs-8 per-icon-box margin_t10" id="malePic">
		                  <!-- 
		                    <i class="fa fa-fw fa-male text-blue"></i>
		                    <i class="fa fa-fw fa-male text-blue"></i>
		                    <i class="fa fa-fw fa-male text-blue"></i>
		                    <i class="fa fa-fw fa-male text-blue"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    -->
		                  </div>
		                  <div class="col-xs-2 text-blue text-nowrap">
		                    <div class="f24"><span id="malePercent">0</span>%</div>
		                    <div>男:<span id="male">0</span>人</div>
		                  </div>
		                </div>

		                <h3 class="cnt-box-title f16 margin_t10">年龄</h3>
		                <div id="barChart" data-role="chart" style="height:130px;"></div>
		              </div>
		            </div>
		          </div>
		          
		          <div class="col-md-6">
		            <!-- 报读专业统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">报读专业统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px" id="specialInfo">
		                  <form>
		                    <div class="col-sm-4 col-sm-offset-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="SPECIAL_GRADE_ID" id="special_grade_id" data-live-search="true">
		                          <option value="">全部学期</option>
		                          <c:forEach items="${gradeMap}" var="map">
				                  		<option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
				                  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="SPECIAL_PYCC_ID" id="special_pycc_id" data-live-search="true">
		                          <option value="">全部层次</option>
		                          <c:forEach items="${pyccMap}" var="map">
	                  				<option value="${map.key}">${map.value}</option>
	                  			 </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    </form>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="specialChart" data-role="chart" style="height:270px;">
		                	<div style="height:270px;overflow:auto;" id="specialInfoChart">
		                	
		                	</div>
		                </div>
		              </div>
		            </div>
		          </div>
		        </div>

			</div>
		</div>
	</div>
</section>
<!-- 底部 -->
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script src="${ctx}/static/plugins/select2/select2.full.min.js"></script>

<script type="text/javascript">

var geoCoordMap = {
		"广州麓湖体验中心（广州总部）":[113.282728,23.151831],
		"广州市开发区学习中心":[113.532061,23.065869],
		"佛山电大":[113.117012,23.037045],
		"深圳市宝安区东方培训中心":[113.828171,22.677558],
		"广州市番禺区培贤教育培训中心":[113.389465,22.949517],
		"中山职业技术学院":[113.387713,22.534285],
		"深圳科讯人才培训中心":[114.091082,22.547011],
		"广州麓湖学习中心":[113.282728,23.151831]
};

//select2
//$(".select2").select2();
	// filter tabs
	$(".filter-tabs li").click(function(event) {
		if($(this).hasClass('actived')){
			$(this).removeClass('actived');
		}
		else{
			$(this).addClass('actived');
		}
	});

	


	// 标签页
	$('.nav-tabs-lg a').click(function (e) {
	  e.preventDefault();
	  $(this).tab('show');
	  if($(this).attr("href")=='#tab_top_2'){
	    var $tabItem=$(".tab-pane:last");
	    if($tabItem.find('[data-role="chart"]').children('div').width()==0){
	      $(window).resize();
	    }
	  }
	});
	/* echarts 图表 */

	// 年龄
	var ecBar=(function(){
	  var obj={};
	  var user_grade_id = $('#user_grade_id').val();
	  var user_pycc_id = $('#user_pycc_id').val();
	  var user_specialty_id = $('#user_specialty_id').val();
	  $.ajax({
		  type: "GET",
		  url:"${ctx}/recruitmanage/signup/searchSignUserAge",
		  data:{GRADE_ID:user_grade_id,PYCC_ID:user_pycc_id,SPECIALTY_ID:user_specialty_id},
		  dataType:"json",
		  async: true,
		  success:function(datas){
			  if(datas!=null){
				  $("#male").html(datas.MALE_COUNT);
				  $("#female").html(datas.FEMALE_COUNT);
				  $("#femalePercent").html(datas.FEMALE_PERCENT);
				  $("#malePercent").html(datas.MALE_PERCENT);
				  var male = parseInt(datas.MALE_PERCENT)/10;
				  var female = parseInt(datas.FEMALE_PERCENT)/10;
				  $("#malePic").empty();
				  $("#femalePic").empty();
				  for(var i=1;i<=male;i++){
					  
					  $("#malePic").append("<i class='fa fa-fw fa-male text-blue'></i>");
				  }
				  for(var i=1;i<=female;i++){
					  $("#femalePic").append("<i class='fa fa-fw fa-female text-red'></i>");
				  }
				  
				  var data = [
					  {
						  name:'00后',
						  value:datas.AGE_FLAG_00s,
						  itemStyle:{
							  normal:{
								  color:'#f56954'
							  }
						  }
					  },
					  {
						  name:'90后',
						  value:datas.AGE_FLAG_90s,
						  itemStyle:{
							  normal:{
								  color:'#00a65a'
							  }
						  }
					  },
					  {
						  name:'80后',
					      value:datas.AGE_FLAG_80s,
					      itemStyle:{
					        normal:{
					          color:'#00c0ef'
					        }
					      }
					  },
					  {
						  name:'70后',
					      value:datas.AGE_FLAG_70s,
					      itemStyle:{
					        normal:{
					          color:'#ffa412'
					        }
					      }
					  },
					  {
						  name:'60后',
					      value:datas.AGE_FLAG_60s,
					      itemStyle:{
					        normal:{
					          color:'#3c8dbc'
					        }
					      }
					  },
					  {
						  name:'60前',
					      value:datas.AGE_FLAG_60s_ago,
					      itemStyle:{
					        normal:{
					          color:'#8b52cc'
					        }
					      }
					  }
				  ];
				  obj.chart.hideLoading();
				  obj.chart.setOption({
					  xAxis:[
						  {
							  data:$.map(data,function(n){ return n.name })
						  }
					  ],
					  series:[
						  {
							  data : data
						  }
					  ]
				  });
			  }
		  }
	  });
	  /*
	  obj.data=[
	    {
	      name:'00后',
	      value:10,
	      itemStyle:{
	        normal:{
	          color:'#f56954'
	        }
	      }
	    },
	    {
	      name:'90后',
	      value:30,
	      itemStyle:{
	        normal:{
	          color:'#00a65a'
	        }
	      }
	    },
	    {
	      name:'80后',
	      value:20,
	      itemStyle:{
	        normal:{
	          color:'#00c0ef'
	        }
	      }
	    },
	    {
	      name:'70后',
	      value:20,
	      itemStyle:{
	        normal:{
	          color:'#ffa412'
	        }
	      }
	    },
	    {
	      name:'60后',
	      value:10,
	      itemStyle:{
	        normal:{
	          color:'#3c8dbc'
	        }
	      }
	    },
	    {
	      name:'60前',
	      value:10,
	      itemStyle:{
	        normal:{
	          color:'#8b52cc'
	        }
	      }
	    }
	  ];
	  */
	  /*function sum(arr){
	    var r=0;
	    for (var i = 0; i < arr.length; i++) {
	      r+=arr[i].value;
	    };
	    return r;
	  };*/

	  obj.chart=echarts.init(document.getElementById('barChart'));
	  obj.chart.setOption({
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter:'{b}：{c}%'
	        /*formatter:function (params, ticket, callback) {
	          return params[0].name+'：'+params[0].value
	          +' '+Math.ceil(params[0].value/sum(obj.data)*100)+'%';
	        }*/
	    },
	    grid: {
	        left:0,
	        right:20,
	        bottom: 0,
	        top:20,
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'category',
	         //   data : ['00后', '90后', '80后', '70后', '60后', '60前'],
	            data : [],
	            axisTick: {
	                alignWithLabel: true
	            },
	            axisLine:{
	              lineStyle:{
	                color:'#d2d6de',
	                width:1
	              }
	            },
	            axisLabel:{
	              textStyle:{
	                color:'#666666'
	              }
	            },
	            splitArea:{
	              show:true
	            }

	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            min:0,
	            max:100,
	            axisLine:{
	              //show:false
	              lineStyle:{
	                color:'#d2d6de',
	                width:1
	              }
	            },
	            axisLabel:{
	              textStyle:{
	                color:'#666666'
	              },
	              formatter:'{value}%'
	            },
	            axisTick:{
	              //show:false
	            },
	            splitLine:{
	              //show:false
	            }
	        }
	    ],
	    series : [
	        {
	            name:'直接访问',
	            type:'bar',
	            barWidth: '50%',
	            label:{
	              normal:{
	                position:'top',
	                show:true,
	                formatter: '{c}%'
	              }
	            },
	           data:obj.data
	          // data:[]
	        }
	    ]
	  });
	  
	  $('#signUpUser').delegate(':input','change',function(){
		  var user_grade_id = $('#user_grade_id').val();
		  var user_pycc_id = $('#user_pycc_id').val();
		  var user_specialty_id = $('#user_specialty_id').val();
		  $.ajax({
			  type: "GET",
			  url:"${ctx}/recruitmanage/signup/searchSignUserAge",
			  data:{GRADE_ID:user_grade_id,PYCC_ID:user_pycc_id,SPECIALTY_ID:user_specialty_id},
			  dataType:"json",
			  async: true,
			  success:function(datas){
				  if(datas!=null){
					  $("#male").html(datas.MALE_COUNT);
					  $("#female").html(datas.FEMALE_COUNT);
					  $("#femalePercent").html(datas.FEMALE_PERCENT);
					  $("#malePercent").html(datas.MALE_PERCENT);
					  var male = parseInt(datas.MALE_PERCENT)/10;
					  var female = parseInt(datas.FEMALE_PERCENT)/10;
					  $("#malePic").empty();
					  $("#femalePic").empty();
					  for(var i=1;i<=male;i++){
						  $("#malePic").append("<i class='fa fa-fw fa-male text-blue'></i>");
					  }
					  for(var i=1;i<=female;i++){
						  $("#femalePic").append("<i class='fa fa-fw fa-female text-red'></i>");
					  }
					  var data = [
						  {
							  name:'00后',
							  value:datas.AGE_FLAG_00s,
							  itemStyle:{
								  normal:{
									  color:'#f56954'
								  }
							  }
						  },
						  {
							  name:'90后',
							  value:datas.AGE_FLAG_90s,
							  itemStyle:{
								  normal:{
									  color:'#00a65a'
								  }
							  }
						  },
						  {
							  name:'80后',
						      value:datas.AGE_FLAG_80s,
						      itemStyle:{
						        normal:{
						          color:'#00c0ef'
						        }
						      }
						  },
						  {
							  name:'70后',
						      value:datas.AGE_FLAG_70s,
						      itemStyle:{
						        normal:{
						          color:'#ffa412'
						        }
						      }
						  },
						  {
							  name:'60后',
						      value:datas.AGE_FLAG_60s,
						      itemStyle:{
						        normal:{
						          color:'#3c8dbc'
						        }
						      }
						  },
						  {
							  name:'60前',
						      value:datas.AGE_FLAG_60s_ago,
						      itemStyle:{
						        normal:{
						          color:'#8b52cc'
						        }
						      }
						  }
					  ];
					  obj.chart.hideLoading();
					  obj.chart.setOption({
						  xAxis:[
							  {
								  data:$.map(data,function(n){ return n.name })
							  }
						  ],
						  series:[
							  {
								  data : data
							  }
						  ]
					  });
				  }
			  }
		  });
	  });
	  $('#signUpUser :input:eq(0)').trigger('change');
	  return obj;
	})();
	// 学习中心统计
	var ecMap2=(function(){
	  var obj={};
	  var center_grade_id = $("#center_grade_id").val();
	  var center_pycc_id = $("#center_pycc_id").val();
	  var center_specialty_id = $("#center_specialty_id").val();
	  $.ajax({
		  type: "GET",
		  url:"${ctx}/recruitmanage/signup/queryStudyCenter",
		  data:{CENTER_GRADE_ID:center_grade_id,CENTER_PYCC_ID:center_pycc_id,CENTER_SPECIALTY_ID:center_specialty_id},
		  dataType:"json",
		  async: true,
		  success:function(datas){
			  if(datas!=null){
				  var data = [
      				{
      				    name:datas.SC_NAME_1,
      				    value:datas.STUDY_COUNT_1,
      				    itemStyle:{
      				      normal:{
      				        color:'#f56954'
      				      }
      				    }
      				  },
      				  {
      					name:datas.SC_NAME_2,
      				    value:datas.STUDY_COUNT_2,
      				    itemStyle:{
      				      normal:{
      				        color:'#00a65a'
      				      }
      				    }
      				  },
      				  {
      					name:datas.SC_NAME_3,
      				    value:datas.STUDY_COUNT_3,
      				    itemStyle:{
      				      normal:{
      				        color:'#00c0ef'
      				      }
      				    }
      				  },
      				  {
      					name:datas.SC_NAME_4,
      				    value:datas.STUDY_COUNT_4,
      				    itemStyle:{
      				      normal:{
      				        color:'#ffa412'
      				      }
      				    }
      				  },
      				  {
      					name:datas.SC_NAME_5,
      				    value:datas.STUDY_COUNT_5,
      				    itemStyle:{
      				      normal:{
      				        color:'#3c8dbc'
      				      }
      				    }
      				  }
      			];
      			obj.chart.hideLoading();
				obj.chart.setOption({
					  xAxis:[
						  {
							  data:$.map(data,function(n){ return n.name })
						  }
					  ],
					  series:[
						  {
							  data : data
						  }
					  ]
				});
			  }
		  }
	  });
	  
	  obj.chart=echarts.init(document.getElementById('mapChart1'));
	  obj.chart.setOption({
		  tooltip : {
		      trigger: 'axis',
		      axisPointer : {
		          type : 'shadow'        
		      },
		      formatter:'{b}：{c}'
		  },
		  grid: {
		      left:10,
		      right:10,
		      bottom: 10,
		      top:20,
		      containLabel: true
		  },
		  xAxis : [
		      {
		          type : 'category',
		          data:[],
		          axisTick: {
		              alignWithLabel: true
		          },
		          axisLine:{
		            lineStyle:{
		              color:'#d2d6de',
		              width:1
		            }
		          },
		          axisLabel:{
		            textStyle:{
		              color:'#666666'
		            },

					  formatter:function(value,index){
						  if(index<6 && value!=null){
							  if(value.length>4){
								  //return value.substring(0,5)+'\n'+value.substring(5,value.length);
								  return value.substring(0,5)+"...";
							  }else{
								  return value;
							  }
						  }else{
							  return value;
						  }
					  }
		          }


			  }
		  ],
		  yAxis : [
		      {
		          type : 'value',
		          axisLine:{
		            lineStyle:{
		              color:'#d2d6de',
		              width:1
		            }
		          },
		          axisLabel:{
		            textStyle:{
		              color:'#666666'
		            },
		            formatter:'{value}'
		          }
		      }
		  ],
		  series : [
		      {
		          name:'报读资料统计',
		          type:'bar',
		          barWidth: '50%',
		          label:{
		            normal:{
		              position:'top',
		              show:true,
		              formatter: '{c}'
		            }
		          },		          
		          data:[]		          
		      }
		  ],
		});
	  	$('#signUpCenter').delegate(':input','change',function(){
	  		var center_grade_id = $("#center_grade_id").val();
			var center_pycc_id = $("#center_pycc_id").val();
			var center_specialty_id = $("#center_specialty_id").val();
			$.ajax({
				type: "GET",
				url:"${ctx}/recruitmanage/signup/queryStudyCenter",
				data:{CENTER_GRADE_ID:center_grade_id,CENTER_PYCC_ID:center_pycc_id,CENTER_SPECIALTY_ID:center_specialty_id},
				dataType:"json",
				async: true,
				success:function(datas){
					if(datas!=null){
						  var data = [
		      				{
		      				    name:datas.SC_NAME_1,
		      				    value:datas.STUDY_COUNT_1,
		      				    itemStyle:{
		      				      normal:{
		      				        color:'#f56954'
		      				      }
		      				    }
		      				  },
		      				  {
		      					name:datas.SC_NAME_2,
		      				    value:datas.STUDY_COUNT_2,
		      				    itemStyle:{
		      				      normal:{
		      				        color:'#00a65a'
		      				      }
		      				    }
		      				  },
		      				  {
		      					name:datas.SC_NAME_3,
		      				    value:datas.STUDY_COUNT_3,
		      				    itemStyle:{
		      				      normal:{
		      				        color:'#00c0ef'
		      				      }
		      				    }
		      				  },
		      				  {
		      					name:datas.SC_NAME_4,
		      				    value:datas.STUDY_COUNT_4,
		      				    itemStyle:{
		      				      normal:{
		      				        color:'#ffa412'
		      				      }
		      				    }
		      				  },
		      				  {
		      					name:datas.SC_NAME_5,
		      				    value:datas.STUDY_COUNT_5,
		      				    itemStyle:{
		      				      normal:{
		      				        color:'#3c8dbc'
		      				      }
		      				    }
		      				  }
		      			];
		      			obj.chart.hideLoading();
						obj.chart.setOption({
							  xAxis:[
								  {
									  data:$.map(data,function(n){ return n.name })
								  }
							  ],
							  series:[
								  {
									  data : data
								  }
							  ]
						});
					  }
				  }
			});
	  	});
	 	$('#signUpCenter :input:eq(0)').trigger('change');
	 	return obj;
	})();
	
	

	// 区域统计
/*
	var ecMap=(function(){
	  	var obj={};
		var area_grade_id = $("#area_grade_id").val();
		var area_pycc_id = $("#area_pycc_id").val();
		var area_specialty_id = $("#area_specialty_id").val();
		var mapArray = [];
		$.ajax({
			type: "GET",
			url:"${ctx}/recruitmanage/signup/queryAreaCount",
			data:{AREA_GRADE_ID:area_grade_id,AREA_PYCC_ID:area_pycc_id,AREA_SPECIALTY_ID:area_specialty_id},
			dataType:"json",
			async: true,
			success:function (area) {
				if (area!=null){
					var areas = area.AREA_LIST;
					var temp = {};
					for(var k=0;k<areas.length;k++){
						temp = {name:areas[k].NAME,value:areas[k].STU_COUNT};
						mapArray.push(temp);
					}

				}
			}
		});
		obj.mapChartData=mapArray;
	  obj.mapChartData=[
	    {name: '北京',value: 7 },
	    {name: '天津',value: 2 },
	    {name: '上海',value: 12 },
	    {name: '重庆',value: 14 },
	    {name: '河北',value: 8 },
	    {name: '河南',value: 1 },
	    {name: '云南',value: 15 },
	    {name: '辽宁',value: 5 },
	    {name: '黑龙江',value: 9 },
	    {name: '湖南',value: 7 },
	    {name: '安徽',value: 17 },
	    {name: '山东',value: 2 },
	    {name: '新疆',value: 4 },
	    {name: '江苏',value: 14 },
	    {name: '浙江',value: 2 },
	    {name: '江西',value: 7},
	    {name: '湖北',value: 9 },
	    {name: '广西',value: 19 },
	    {name: '甘肃',value: 7 },
	    {name: '山西',value: 3 },
	    {name: '内蒙古',value: 5 },
	    {name: '陕西',value: 7 },
	    {name: '吉林',value: 8 },
	    {name: '福建',value: 12 },
	    {name: '贵州',value: 10 },
	    {name: '广东',value: 24 },
	    {name: '青海',value: 4 },
	    {name: '西藏',value: 7 },
	    {name: '四川',value: 2 },
	    {name: '宁夏',value: 18 },
	    {name: '海南',value: 14 },
	    {name: '台湾',value: 16 },
	    {name: '香港',value: 4 },
	    {name: '澳门',value: 7 }
	  ];
	  obj.chart=echarts.init(document.getElementById('mapChart2'));
	  obj.chart.setOption({
	    tooltip: {
	        trigger: 'item',
	        formatter:'{b}：{c}k'
	    },
	    visualMap: {
	        type:'piecewise',
	        inverse:true,
	        itemGap:1,
	        textStyle:{color:'#a96158'},
	        pieces:[
	          {min:0,max:5,label:'0-5k',color:'#ffe9e7'},
	          {min:5,max:10,label:'5k-10k',color:'#ffc9c2'},
	          {min:10,max:15,label:'10k-15k',color:'#ffada3'},
	          {min:15,max:20,label:'15k-20k',color:'#ff9184'},
	          {min:20,label:'20k+',color:'#ff5844'}
	        ],
	        left: 'left',
	        top: 'bottom',
	    },
	    series: [
	        {
	            name: '区域统计',
	            type: 'map',
	            mapType: 'china',
	            roam: true,
	            layoutCenter: ['50%', '50%'],
	            layoutSize:'120%',
	            label: {
	                normal: {
	                    show: false
	                },
	                emphasis: {
	                    show: true

	                }
	            },
	            data:obj.mapChartData
	        }
	    ]
	  });
	  return obj;
	})();


	setTimeout(function(){
	  mapChartData=[
	    {name: '北京',value: 17 },
	    {name: '天津',value: 12 },
	    {name: '上海',value: 12 },
	    {name: '重庆',value: 14 },
	    {name: '河北',value: 8 },
	    {name: '河南',value: 1 },
	    {name: '云南',value: 15 },
	    {name: '辽宁',value: 5 },
	    {name: '黑龙江',value: 9 },
	    {name: '湖南',value: 7 },
	    {name: '安徽',value: 17 },
	    {name: '山东',value: 2 },
	    {name: '新疆',value: 14 },
	    {name: '江苏',value: 14 },
	    {name: '浙江',value: 2 },
	    {name: '江西',value: 7},
	    {name: '湖北',value: 9 },
	    {name: '广西',value: 19 },
	    {name: '甘肃',value: 7 },
	    {name: '山西',value: 3 },
	    {name: '内蒙古',value: 5 },
	    {name: '陕西',value: 7 },
	    {name: '吉林',value: 8 },
	    {name: '福建',value: 12 },
	    {name: '贵州',value: 10 },
	    {name: '广东',value: 24 },
	    {name: '青海',value: 4 },
	    {name: '西藏',value: 7 },
	    {name: '四川',value: 2 },
	    {name: '宁夏',value: 18 },
	    {name: '海南',value: 14 },
	    {name: '台湾',value: 16 },
	    {name: '香港',value: 4 },
	    {name: '澳门',value: 7 }
	  ];
	  ecMap.mapChart.setOption({
	    series:[{
	      data:mapChartData
	    }]
	  });
	},2000);*/

	// 学历层次统计
	var ecPie2=(function(){
	  var obj={};
	  var pycc_grade_id = $('#pycc_grade_id').val();
	  var pycc_specialty_id = $('#pycc_specialty_id').val();
	  $.ajax({
		  type: "GET",
		  url:"${ctx}/recruitmanage/signup/userPyccCount",
		  data:{PYCC_GRADE_ID:pycc_grade_id,PYCC_SPECIALTY_ID:pycc_specialty_id},
		  dataType:"json",
		  async: true,
		  success:function(datas){
			  if(datas!=null){
				  obj.chart.setOption({
				      legend: {
				          top:10,
				          right:10,
				          orient:'vertical',
				          data:['专科','本科']
				      },
				      tooltip : {
				          trigger: 'item',
				          formatter: "{b} : {c} ({d}%)"
				      },
				      series : [
				          {

				              name: '学历层次统计',
				              type: 'pie',
				              center: ['50%', '50%'],
				              label:{
				                normal:{
				                  formatter: "{b}:{c}({d}%)"
				                }
				              },
				              data:[
				                  {value:datas.HIGHSPIN, name:'专科',itemStyle:{
				                      normal:{
				                        color:'#3c8dbc'
				                      }
				                    }
				                  },
				                  {value:datas.SPECIALE_DITION, name:'本科',itemStyle:{
				                      normal:{
				                        color:'#00a65a'
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
			  }
		  }
		  
	  });
	  obj.chart=echarts.init(document.getElementById('pieChart2'));
	  
	  $('#signUpPycc').delegate(':input','change',function(){
		  var pycc_grade_id = $('#pycc_grade_id').val();
		  var pycc_specialty_id = $('#pycc_specialty_id').val();
		  $.ajax({
			  type: "GET",
			  url:"${ctx}/recruitmanage/signup/userPyccCount",
			  data:{PYCC_GRADE_ID:pycc_grade_id,PYCC_SPECIALTY_ID:pycc_specialty_id},
			  dataType:"json",
			  async: true,
			  success:function(datas){
				  if(datas!=null){
					  obj.chart.setOption({
					      legend: {
					          top:10,
					          right:10,
					          orient:'vertical',
					          data:['专科','本科']
					      },
					      tooltip : {
					          trigger: 'item',
					          formatter: "{b} : {c} ({d}%)"
					      },
					      series : [
					          {

					              name: '学历层次统计',
					              type: 'pie',
					              center: ['50%', '50%'],
					              label:{
					                normal:{
					                  formatter: "{b}:{c}({d}%)"
					                }
					              },
					              data:[
					                  {value:datas.HIGHSPIN, name:'专科',itemStyle:{
					                      normal:{
					                        color:'#3c8dbc'
					                      }
					                    }
					                  },
					                  {value:datas.SPECIALE_DITION, name:'本科',itemStyle:{
					                      normal:{
					                        color:'#00a65a'
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
				  }
			  }
			  
		  });
	  });
	  
	  return obj;
	})();
	
	var specialInfoChart = (function(){
		var obj = {};
		var special_grade_id = $("#special_grade_id").val();
		var special_pycc_id = $("#special_pycc_id").val();
		$.ajax({
			type: "GET",
			url:"${ctx}/recruitmanage/signup/userCountBySpecial",
			data:{SPECIAL_GRADE_ID:special_grade_id,SPECIAL_PYCC_ID:special_pycc_id},
			dataType:"json",
			async: true,
			success:function(datas){
				if(datas!=null){
					//var info = $("#specialInfo");
					$("#specialInfoChart").empty();
					var html = "";
					html +="<dl class='spe-list'>";
					for(var i=0;i<datas.SPECIALLIST.length;i++){
						html +="<dt class='text-no-bold f12'>"+datas.SPECIALLIST[i].ZYMC+"</dt>";
						html +="<dd class='margin_t5'>";
						html +="<div class='progress'>";
						var s = "width: "+datas.SPECIALLIST[i].ZYMC_COUNT_PERCENT+"%";
						html +="<div class='progress-bar progress-bar-green' style='"+s+"'>";
						html +="<span>"+datas.SPECIALLIST[i].ZYMC_COUNT+"("+datas.SPECIALLIST[i].ZYMC_COUNT_PERCENT+"%)"+"</span>";
						html +="</div>";
						html +="</div>";
						html +="</dd>";
					}
					html += "</dl>";
					$("#specialInfoChart").append(html);
				}else{
					$("#specialInfoChart").append("暂无数据！");
				}
			}
		});
		obj.chart=echarts.init(document.getElementById('specialInfoChart'));
		
		$("#specialInfo").delegate(':input','change',function(){
			var special_grade_id = $("#special_grade_id").val();
			var special_pycc_id = $("#special_pycc_id").val();
			$.ajax({
				type: "GET",
				url:"${ctx}/recruitmanage/signup/userCountBySpecial",
				data:{SPECIAL_GRADE_ID:special_grade_id,SPECIAL_PYCC_ID:special_pycc_id},
				dataType:"json",
				async: true,
				success:function(datas){
					if(datas!=null){
						//var info = $("#specialInfo");
						$("#specialInfoChart").empty();
						var html = "";
						html +="<dl class='spe-list'>";
						for(var i=0;i<datas.SPECIALLIST.length;i++){
							html +="<dt class='text-no-bold f12'>"+datas.SPECIALLIST[i].ZYMC+"</dt>";
							html +="<dd class='margin_t5'>";
							html +="<div class='progress'>";
							var s = "width: "+datas.SPECIALLIST[i].ZYMC_COUNT_PERCENT+"%";
							html +="<div class='progress-bar progress-bar-green' style='"+s+"'>";
							html +="<span>"+datas.SPECIALLIST[i].ZYMC_COUNT+"("+datas.SPECIALLIST[i].ZYMC_COUNT_PERCENT+"%)"+"</span>";
							html +="</div>";
							html +="</div>";
							html +="</dd>";
						}
						html += "</dl>";
						$("#specialInfoChart").append(html);
					}else{
						$("#specialInfoChart").append("暂无数据！");
					}
				}
			});
		});
		
		
		return obj;
	})();
	
	//报读资料统计
	var ecBar4=(function(){
		var obj={};
		var gradeId=$('#gradeId').val();
		var pycc=$('#pycc').val();
		var signupSpecialtyId=$('#signupSpecialtyId').val();
		$.ajax({
			type: "GET",
			url:"${ctx}/recruitmanage/signup/queryEnrolmentCount",
			data:{gradeId:gradeId,pycc:pycc,signupSpecialtyId:signupSpecialtyId},
       	 	dataType: "json",
        	async: true,	// 是否异步
        	success:function(datas){
        		if(datas!=null){
        			var data = [
        				{
        				    name:'未提交',
        				    value:datas.AUDIT_STATE_4,
        				    itemStyle:{
        				      normal:{
        				        color:'#f56954'
        				      }
        				    }
        				  },
        				  {
        				    name:'待审核',
        				    value:datas.AUDIT_STATE_3,
        				    itemStyle:{
        				      normal:{
        				        color:'#00a65a'
        				      }
        				    }
        				  },
        				  {
        				    name:'审核通过',
        				    value:datas.AUDIT_STATE_1,
        				    itemStyle:{
        				      normal:{
        				        color:'#00c0ef'
        				      }
        				    }
        				  },
        				  {
        				    name:'审核中',
        				    value:datas.AUDIT_STATE_2,
        				    itemStyle:{
        				      normal:{
        				        color:'#ffa412'
        				      }
        				    }
        				  },
        				  {
        				    name:'审核不通过',
        				    value:datas.AUDIT_STATE_0,
        				    itemStyle:{
        				      normal:{
        				        color:'#3c8dbc'
        				      }
        				    }
        				  }
        			];
        			obj.chart.hideLoading();
  				  	obj.chart.setOption({
  					  xAxis:[
  						  {
  							  data:$.map(data,function(n){ return n.name })
  						  }
  					  ],
  					  series:[
  						  {
  							  data : data
  						  }
  					  ]
  				  });
        		}
        	}
		});
		
		
		obj.chart=echarts.init(document.getElementById('ecBar4'));
		obj.chart.setOption({
		  tooltip : {
		      trigger: 'axis',
		      axisPointer : {
		          type : 'shadow'        
		      },
		      formatter:'{b}：{c}'
		  },
		  grid: {
		      left:10,
		      right:10,
		      bottom: 10,
		      top:20,
		      containLabel: true
		  },
		  xAxis : [
		      {
		          type : 'category',
		        //  data : ['未提交', '待审核', '审核通过','审核中','审核不通过'],
		          data:[],
		          axisTick: {
		              alignWithLabel: true
		          },
		          axisLine:{
		            lineStyle:{
		              color:'#d2d6de',
		              width:1
		            }
		          },
		          axisLabel:{
		            textStyle:{
		              color:'#666666'
		            }
		          }

		      }
		  ],
		  yAxis : [
		      {
		          type : 'value',
		          axisLine:{
		            lineStyle:{
		              color:'#d2d6de',
		              width:1
		            }
		          },
		          axisLabel:{
		            textStyle:{
		              color:'#666666'
		            },
		            formatter:'{value}'
		          }
		      }
		  ],
		  series : [
		      {
		          name:'报读资料统计',
		          type:'bar',
		          barWidth: '50%',
		          label:{
		            normal:{
		              position:'top',
		              show:true,
		              formatter: '{c}'
		            }
		          },		          
		          data:[]		          
		      }
		  ],
		});
		
		 $('#signupInfo').delegate(':input', 'change', function () {
			// $(".sigunpinfo").empty();		
			 var gradeId=$('#gradeId').val();
				var pycc=$('#pycc').val();
				var signupSpecialtyId=$('#signupSpecialtyId').val();
				$.ajax({
					type: "GET",
					url:"${ctx}/recruitmanage/signup/queryEnrolmentCount",
					data:{gradeId:gradeId,pycc:pycc,signupSpecialtyId:signupSpecialtyId},
		       	 	dataType: "json",
		        	async: true,	// 是否异步
		        	success:function(datas){
		        		if(datas!=null){
		        			var data = [
		        				{
		        				    name:'未提交',
		        				    value:datas.AUDIT_STATE_4,
		        				    itemStyle:{
		        				      normal:{
		        				        color:'#f56954'
		        				      }
		        				    }
		        				  },
		        				  {
		        				    name:'待审核',
		        				    value:datas.AUDIT_STATE_3,
		        				    itemStyle:{
		        				      normal:{
		        				        color:'#00a65a'
		        				      }
		        				    }
		        				  },
		        				  {
		        				    name:'审核通过',
		        				    value:datas.AUDIT_STATE_1,
		        				    itemStyle:{
		        				      normal:{
		        				        color:'#00c0ef'
		        				      }
		        				    }
		        				  },
		        				  {
		        				    name:'审核中',
		        				    value:datas.AUDIT_STATE_2,
		        				    itemStyle:{
		        				      normal:{
		        				        color:'#ffa412'
		        				      }
		        				    }
		        				  },
		        				  {
		        				    name:'审核不通过',
		        				    value:datas.AUDIT_STATE_0,
		        				    itemStyle:{
		        				      normal:{
		        				        color:'#3c8dbc'
		        				      }
		        				    }
		        				  }
		        			];
		        			obj.chart.hideLoading();
		  				  	obj.chart.setOption({
		  					  xAxis:[
		  						  {
		  							  data:$.map(data,function(n){ return n.name })
		  						  }
		  					  ],
		  					  series:[
		  						  {
		  							  data : data
		  						  }
		  					  ]
		  				  });
		        		}
		        	}
				});
	 });
		$('#signupInfo :input:eq(0)').trigger('change');
		return obj;
	})();

	//报读缴费统计
	var ecBar5=(function(){
		var obj={};
		var gradeId=$('#payCost_gradeId').val();
		var pycc=$('#payCost_pycc').val();
		var signupSpecialtyId=$('#payCost_specialtyId').val();
		$.ajax({
			type: "GET",
			url: "${ctx}/recruitmanage/signup/queryPaymentCount",
        	data:{gradeId:gradeId,pycc:pycc,signupSpecialtyId:signupSpecialtyId},
       	 	dataType: "json",
        	async: true,	// 是否异步
        	success:function(datas){
        		if(datas!=null){
        			var data = [
        				{
        				    name:'已全额缴费 ',
        				    value:datas.CHARGE_0,
        				    itemStyle:{
        				      normal:{
        				        color:'#00a65a'
        				      }
        				    }
        				  },
        				  {
        				    name:'已部分缴费',
        				    value:datas.CHARGE_1,
        				    itemStyle:{
        				      normal:{
        				        color:'#f56954'
        				      }
        				    }
        				  },
						  {
							name:'待缴费',
							value:datas.CHARGE_2,
							itemStyle:{
								normal:{
									color:'#ffa412'
								}
							}
						 },
						{
							name:'已欠费',
							value:datas.CHARGE_3,
							itemStyle:{
								normal:{
									color:'#ffa412'
								}
							}
						}
        			];
        			obj.chart.hideLoading();
  				  	obj.chart.setOption({
  					  xAxis:[
  						  {
  							  data:$.map(data,function(n){ return n.name })
  						  }
  					  ],
  					  series:[
  						  {
  							  data : data
  						  }
  					  ]
  				  });
        		}
        		
        	}
			
		});
		
		obj.chart=echarts.init(document.getElementById('ecBar5'));
		obj.chart.setOption({
			  tooltip : {
			      trigger: 'axis',
			      axisPointer : {
			          type : 'shadow'        
			      },
			      formatter:'{b}：{c}'
			  },
			  grid: {
			      left:10,
			      right:10,
			      bottom: 10,
			      top:20,
			      containLabel: true
			  },
			  xAxis : [
			      {
			          type : 'category',
			          data : ['已全额缴费 ','已部分缴费','待缴费','已欠费'],
			          axisTick: {
			              alignWithLabel: true
			          },
			          axisLine:{
			            lineStyle:{
			              color:'#d2d6de',
			              width:1
			            }
			          },
			          axisLabel:{
			            textStyle:{
			              color:'#666666'
			            }
			          }

			      }
			  ],
			  yAxis : [
			      {
			          type : 'value',
			          axisLine:{
			            lineStyle:{
			              color:'#d2d6de',
			              width:1
			            }
			          },
			          axisLabel:{
			            textStyle:{
			              color:'#666666'
			            },
			            formatter:'{value}'
			          }
			      }
			  ],
			  series : [
			      {
			          name:'报读缴费统计',
			          type:'bar',
			          barWidth: '50%',
			          label:{
			            normal:{
			              position:'top',
			              show:true,
			              formatter: '{c}'
			            }
			          },
			          data:[]
			      }
			  ]
			});
		 $('#payCost').delegate(':input', 'change', function () {
				// $(".sigunpinfo").empty();
				 var names=[];
				 var values=[];
				 var gradeId=$('#payCost_gradeId').val();
				 var pycc=$('#payCost_pycc').val();
				 var signupSpecialtyId=$('#payCost_specialtyId').val();
				 $.ajax({
						type: "GET",
						url: "${ctx}/recruitmanage/signup/queryPaymentCount",
			        	data:{gradeId:gradeId,pycc:pycc,signupSpecialtyId:signupSpecialtyId},
			       	 	dataType: "json",
			        	async: true,	// 是否异步
			        	success:function(datas){
			        		if(datas!=null){
			        			var data = [
			        				{
			        				    name:'已全额缴费',
			        				    value:datas.CHARGE_0,
			        				    itemStyle:{
			        				      normal:{
			        				        color:'#00a65a'
			        				      }
			        				    }
			        				  },
			        				  {
			        				    name:'已部分缴费',
			        				    value:datas.CHARGE_1,
			        				    itemStyle:{
			        				      normal:{
			        				        color:'#f56954'
			        				      }
			        				    }
			        				  },
									{
										name:'待缴费',
										value:datas.CHARGE_2,
										itemStyle:{
											normal:{
												color:'#ffa412'
											}
										}
									},
									{
										name:'已欠费',
										value:datas.CHARGE_3,
										itemStyle:{
											normal:{
												color:'#ffa412'
											}
										}
									}
			        			];
			        			obj.chart.hideLoading();
			  				  	obj.chart.setOption({
			  					  xAxis:[
			  						  {
			  							  data:$.map(data,function(n){ return n.name })
			  						  }
			  					  ],
			  					  series:[
			  						  {
			  							  data : data
			  						  }
			  					  ]
			  				  });
			        		}
			        		
			        	}
						
					});
		 });
		$('#payCost :input:eq(0)').trigger('change');
		return obj;
	})();
	$(window).bind("resize",function(){
	  ecBar.chart.resize();
	  ecPie2.chart.resize();
	 // ecMap.chart.resize();
	  ecMap2.chart.resize();
	  ecBar4.chart.resize();
	  ecBar5.chart.resize();
	  specialInfoChart.chart.resize();
	});
	
	function showStudyCenter(str){
		var obj = {};
		obj.chart=echarts.init(document.getElementById('mapChart1'));
		
		obj.chart.convertData = function (data) {
	        var res = [];
	        for (var i = 0; i < data.length; i++) {
	          var geoCoord = geoCoordMap[data[i].name];
	          if (geoCoord) {
	            res.push({
	              name: data[i].name,
	              value: geoCoord.concat(data[i].value)
	            });
	          }
	        }
	        return res;
	      };
	      obj.chart.setOption({
	        tooltip: {
	          trigger: 'item',
	          formatter: function (params) {
	            return params.name + ' : ' + params.value[2];
	          }
	        },

	        geo: {
	          map: 'china',
	          zoom:1.2,
	          roam: true,
	          label: {
	            emphasis: {
	              show: true
	            }
	          },
	          itemStyle:{
	            normal:{
	              color:'#ffebe9'
	            },
	            emphasis:{
	              color:'#ff5945'
	            }
	          }
	        },
	        series: [
	          {
	        	name: '学习中心统计',
		        type: 'scatter',
	            coordinateSystem: 'geo',
	            data: [],
	            symbol:'pin',
	            symbolSize: 20,
	            label: {
	              normal: {
	                show: false
	              },
	              emphasis: {
	                show: false
	              }
	            },
	            itemStyle: {
	              emphasis: {
	                borderColor: '#fff',
	                borderWidth: 1
	              }
	            }
	          }
	        ]
	      });
	      obj.chart.hideLoading();
	      setTimeout(function () {
	        obj.chart.setOption({
	          series : [
	            {
	              data:obj.chart.convertData(str)
	            }
	          ]
	        });
	      },1000);
	}

	/*
	function changType(type) {
		if (type=="1") {
			window.location.href = "list";
		} else {
			window.location.href = "statistics";
		}
	}
	*/

</script>
</body>
</html>
