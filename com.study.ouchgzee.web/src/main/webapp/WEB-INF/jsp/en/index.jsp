<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE HTML>
<!--[if lte IE 7]><html class="ie67"><![endif]-->
<!--[if IE 8]><html class="ie8"><![endif]-->
<!--[if gte IE 9]><html><![endif]-->
<!--[if !IE]><!--><html><!--<![endif]-->

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<title>学习空间</title>
<link type="text/css" rel="stylesheet" href="${css}/common/v2/common.css" />
<link type="text/css" rel="stylesheet" href="${css}/ouchgzee_com/person_center/v3.0.1/style/style.css" />
<link type="text/css" rel="stylesheet" href="${css}/ouchgzee_com/person_center/v3.0.1/style/page.css">
<link rel="shortcut icon" type="image/x-icon" href="${css}/ouchgzee_com/person_center/v3.0.1/favicon.ico">
<!--[if lt IE 9]>
<script type="text/javascript" src="${css}/common/js/resetHTML5_forIE.js"></script>
<![endif]-->
</head>
<body>
<!--站点内容容器-->
<div class="site-wrap">
	<div class="site-header">
		<div class="header">
			<div class="wrap-box clearfix">
				<h1 class="site-title">
					<span>学习空间</span>
				</h1>
		        <div class="user-panel">
		        	<div class="drowdown-tigger">
			        	<img class="user-img circle" src="http://css.gzedu.com/ouchgzee_com/person_center/v3.0.1/images/upload_p.png" width="50" height="50">
			        	<span class="user-name"></span>
		        	</div>
		        	<div class="pop">
		            	<ul class="perInfo_nav">
		                	<li data-role="pop-self-setting"><a href="#/grzlshezhi"><i class="ico01"></i>个人设置</a></li>
		                    <li><a data-id="logout" href="/logout"><i class="ico02"></i>退出平台</a></li>
		                </ul>
		            </div>
		        </div>
			</div>
		</div>
		<!--插入-->
	</div>
	<div class="position-relative main-content">
		<div id="app-container">
			<div class="main-box">
			  <div class="wrap-box clearfix">
			    <div class="main">
					<div class="bg-white border-e0e0e0 text-center">
						<div id="tabs-content-box">
							<table width="100%" height="400">
								<tr>
									<td valign="middle">
										
										<p>
											<i class="loading"></i>
									    </p>
									    <p class="font18 gray margin_t10">数据加载中...</p>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			  </div>		
			</div>
		</div>
		<!--浮动工具条-->
		
	</div>

	<div class="site-footer">
		<div class="wrap-box">
			<div class="bg-white border-e0e0e0">
				<div class="center-block clearfix gray6 padding15" style="width:80%" id="site-copyright">
					<div class="text-center padding_t15">
						站点底部信息未设置
					</div>
					<!--
					<table width="100%">
						<tr>
							<td width="100%" valign="top">
								主办：国家开放大学
							</td>
							<td class="text-nowrap" width="1%">
								<div>教育服务热线：</div>
								<div class="font24 line-height-1 margin_t5">400 096 9300</div>
							</td>
						</tr>
					</table>
					-->
				</div>
			</div>
		</div>
	</div>
</div>
<!--站点加载 提示层-->
<div class="overlay bg-center hide" data-role="page-loading"></div>

<script src="${css}/ouchgzee_com/person_center/v3.0.1/js/require.js" data-require-path="${css}/ouchgzee_com/person_center/v3.0.1/js/"></script>
<script type="text/javascript" src="${css}/ouchgzee_com/person_center/v3.0.1/js/app.js"></script>

<!--“网站菜单” 模板-->
<script type="text/template" id="site-menu-temp">
	<nav class="menu-box" data-role="log-status" data-id="site-menu">
		<div class="wrap-box clearfix">
			<ul class="menu">
				{{each siteMenu}}
					{{if $value.isVisible}}
						<!--院校模式 0:非院校，1：院校-->
						{{if schoolModel==1 || schoolModel==2}}
							<!--学籍管理、教务服务 板块不显示-->
							{{if $value.label!='xueji' && $value.label!='jiaowu'}}
								{{if $value.label=='banzhuren'}}
									<li data-hash="{{$value.hashPath}}"><a href="#/bjdayi">{{$value.name}}</a></li>
								{{else}}
									<li data-hash="{{$value.hashPath}}"><a href="{{$value.hashPath}}">{{$value.name}}</a></li>
								{{/if}}
								
							{{/if}}
						{{else}}
							<li data-hash="{{$value.hashPath}}"><a href="{{$value.hashPath}}">{{$value.name}}</a></li>
						{{/if}}
						
					{{/if}}
				{{/each}}
			</ul>
		</div>
	</nav>
</script>
<!--“右边浮动工具” 模板-->
<script type="text/template" id="help-sidebar-temp">
	<div class="suspend" data-role="log-status" data-id="suspend">
		<ul>
			<li>
				<a class="suspend_icon01" href="#/xuexi">
					<label>回到首页</label>
				</a>
			</li>
			{{if qcodePic}}
				<li>
					<a class="suspend_icon02" href="javascript:;">
						<label>移动下载</label>
						<span>
							<div class="ewm text-center padding_t15 padding_b15" style="width:188px;">
								<img src="{{qcodePic}}" width="162" height="162" class="vertical-middle">
							</div>
							<i>◆</i>
						</span>
					</a>
				</li>
			{{/if}}
			{{if schoolModel==='0' || schoolModel==5}}
				<li>
					<a class="suspend_icon03" href="javascript:;" data-role="ee-contact">
						<label>在线咨询</label>
						<!--<i class="dot"></i>-->
					</a>
				</li>
			{{/if}}
			<li id="video_ico">
				<a class="suspend_icon04" href="pop-ask-teacher.html" data-role="ask-teacher">
					<label>提问题</label>
				</a>
			</li>
			<li>
				<a class="suspend_icon05" href="javascript:;" id="return_top">
					<label>回到顶部</label>
				</a>
			</li>
		</ul>
	</div>
</script>

<!--无相关数据 全站使用-->
<script type="text/template" id="no-data-tips-temp">
	<div class="text-center margin-t30">
	  <img src="http://css.gzedu.com/ouchgzee_com/person_center/v3.0.1/images/qa_null.png" alt="">
	  <p class="font24 margin_b10" style="color:#b2b2b2;">{{message?message:'无相关数据'}}</p>
	</div>
</script>

<!--错误提醒 全站使用-->
<script type="text/template" id="error-tips-temp">
	<div class="text-center padding10">
		<i class="alert-icon alert-icon-0x125 margin_r5"></i>
		<span class="text-orange font16">{{message}}</span>
	</div>
</script>

<script type="text/template" id="error-tips-2-temp">
	<div class="text-center margin-t30">
	  <img src="http://css.gzedu.com/ouchgzee_com/person_center/v3.0.1/images/u-icon-3.png" alt="" class="vertical-middle">
	  <span class="font24 vertical-middle inline-block margin_l10 text-red">{{message}}</span>
	</div>
</script>

<script type="text/template" id="error-tips-3-temp">
	<div class="text-center">
        <i class="alert-icon alert-icon-123x63"></i>
        <span class="f14">{{message?message:'无数据'}}</span>
    </div>
</script>

<script type="text/template" id="page-error-temp">
	<div class="main-box">
	  <div class="wrap-box clearfix">
	    <div class="main">
			<div class="bg-white border-e0e0e0 text-center">
				<div id="tabs-content-box">
					<table width="100%" height="400">
						<tr>
							<td valign="middle">
								
								<p>
									<img src="http://css.gzedu.com/ouchgzee_com/person_center/v3.0.1/images/qa_null.png" class="vertical-middle">
							    </p>
							    <p class="font22 gray margin_t10">{{message}}</p>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	  </div>		
	</div>
</script>

<!--加载load 全站使用-->
<script type="text/template" id="loading-tips-temp">
	<div class="text-center"><i class="loading"></i>{{msg}}</div>
</script>

<!--加载load 全站使用-->
<script type="text/template" id="loading-tips-temp2">
	<table width="100%" height="400" class="text-center">
		<tr>
			<td valign="middle">
				<p>
					<i class="loading"></i>
			    </p>
			    <p class="font18 gray margin_t10">数据加载中...</p>
			</td>
		</tr>
	</table>
</script>

<!--页面初始化-->
<script type="text/javascript">
//全站使用
var noDataTips=[
'<div class="text-center margin-t30">',
  '<img src="http://css.gzedu.com/ouchgzee_com/person_center/v3.0.1/images/qa_null.png" alt="">',
  '<p class="font24 margin_b10" style="color:#b2b2b2;">无相关数据</p>',
'</div>'
].join('');

require(['jquery','sammy','artTemplate','common'], function ($,sammy,template) {

	//检测是否欠费与完善资料
    function siteEntrance(qcodePic){
        //1.0.1 学员登录后获取相关状态
        $.get('/pcenter/getStatusByPC')
     	.done(function(renderData){
			if(renderData.msgCode==200){
				var data=renderData.data;

				if(data){
					var contentJson=data.content;
				  	//审核状态类型
				  	if( data.type===0 ){
				  		
				  		if( contentJson ){
				  			//已确认 入学通知书
				  			if( contentJson.isConfirm==1){
				  				appJSON.isNotCompleteInfo=false;
				  			}
				  			//未确认 入学通知书
				  			else{
				  				appJSON.isNotCompleteInfo=true;
				  			}
				  		}
				  	}
				  	//逾期状态类型
				  	else if( data.type==1 && contentJson.isOverdue==1){
				  		//欠费状态
				  		appJSON.isOwe=true;
				  	}
				  	//是否处于“申请退学”状态
				  	else if( data.type==2 ){
				  		appJSON.isApplyLeavingSchool=true;
				  	}

				  	if(!appJSON.isOwe && !appJSON.isNotCompleteInfo && !appJSON.isApplyLeavingSchool){
				    	$('.site-header').append(template('site-menu-temp',appJSON));

				    	//工具栏渲染
				        renderHelpbar({qcodePic:qcodePic});

				        window.pageApp.run(location.hash?location.hash:'#/xuexi');

				        //教材收货地址未确认时
				        if( data.type==6 ){
				        	$.mydialog({
					          id:'address-manage',
					          width:680,
					          zIndex:1000,
					          height:520,
					          content: 'pop-address-confirm.html'
					        });
				        }
				        else{
				        	var def=$.Deferred();
					        //播放“开学第一课”视频					        
							ajaxProcess({
							    url:'/pcenter/course/querySpecialtyVedio',//5.0 开学第一堂课
							    type:'GET',
							    success:function(renderData){
							      	if(renderData.msgCode==200 && renderData.data && renderData.data.isView){
							          	$.mydialog({
											id:'first-less',
											width:800,
											zIndex:1000,
											height:600,
											urlData:renderData.data,
											content: 'pop-first-lesson-single.html'
										});
							      	}
							      	else{
							      		def.resolve();
							      	}
							    },
							    fail:function(errorMsg){
							    	def.resolve();
							    }
							});

							//重要活动通知 强制阅览
							def.done(function(){
								//查询重要活动通知
								ajaxProcess({
							      	url:'/pcenter/home/message/findImportanceList',
							      	type:'GET',
							      	success:function(renderData){
							          	if(renderData.msgCode==200 && renderData.data && renderData.data.total>0){
							              
											var $win=$(window), w=$win.width(), h=$win.height();

											$.mydialog({
												id:'important-notice',
												width:(w>900?900:w*0.95),
												zIndex:1000,
												height:(h>600?600:h*0.95),
												urlData:renderData.data.list,
												content: 'pop-show-important-notice.html'
											});
							          	}
							      	}
							  	});
							});
						}
				    }
				    else{
				    	//关闭 个人设置 功能
				    	$('[data-role="pop-self-setting"]').remove();

				    	//是否欠费，为true是欠费
				    	if(appJSON.isOwe){
				    		//缓存查询结果
				    		$('body').data('owe-data',data);
				    		
				    		window.pageApp.run('#/qianfei');
				    	}
				    	//是否完善资料
				    	else if(appJSON.isNotCompleteInfo){
				    		if(contentJson.perfectStatus==1){
				    			$('body').data('complete-step',5);
				    		}
				    		window.pageApp.run('#/scdenglu');
				    	}
				    	else if(appJSON.isApplyLeavingSchool){
				    		window.pageApp.run('#/sqtuixue');
				    	}
				    }

				    sideFloat();
				}
			}
      	});        
    };

    //工具栏渲染
    function renderHelpbar(options){
    	$('.main-content').append(
    		template('help-sidebar-temp',
    			$.extend(true, options, {schoolModel:appJSON.schoolModel})    			
    		)
    	);
    }

    //初使化数据
	;(function(){
      	//站点头底部信息设置
	    ajaxProcess({
      		url:'/pcenter/home/copyright/getCopyright',
      		type:'GET',
      		success:function(renderData){
      			if(renderData.msgCode==200){
      				if(renderData.data){
      					//缓存 院校ID
      					appJSON.schoolID=renderData.data.xxId;

	      				var logo=renderData.data.homeHeadLogo;
	      				if(logo){
	      					if(logo.indexOf('eefile.download.eenet.com')!=-1){
	      						logo=logo.indexOf('?x-oss-process=image')==-1?(logo+'?x-oss-process=image/resize,h_52/auto-orient,1'):(logo+'/resize,h_52/auto-orien,1');
	      					}
	      					$('.site-title').html( '<img src="{0}" alt="">'.format( logo ) );
	      				}

	      				if(renderData.data.homeFooterCopyright){
	      					$('#site-copyright').html(renderData.data.homeFooterCopyright);
	      				}

	      				//设置院校模式字段 0:非院校，1：院校(有考试)，2：院校(无考试)，5：国开在线
	      				appJSON.schoolModel=(renderData.data.schoolModel?renderData.data.schoolModel:'0');
	      				//appJSON.schoolModel='5';

	      				//获取用户头像与姓名：接口（个人设置-1.2.查询个人资料）
					    $.get('/pcenter/gjtUser/edit')
				     	.done(function(renderData){
				          	if(renderData.msgCode==200 && renderData.data){
				            	//缓存学生的基础信息
				            	$('body').data('studentBaseInfo',renderData.data);

				            	var pic=renderData.data.pictureURL;
				            	if(pic!='' && pic!=null){
				            		var temp='{{imgurl | imgUrlFormat:[50,50]}}';
				            		var render=template.compile(temp);
				            		var imgPath=render({imgurl:pic});

				              		$('.user-img').attr('src',imgPath);
				              	}

				              	var name=renderData.data.userName;
				              	if(name!='' && name!=null){
				              		$('.user-name').text(name);
				              	}

				              	//网站客服接入系统 的全局设置
				              	window.qimoClientId = {
				              		userId: renderData.data.studentId,
				              		nickName: renderData.data.userName
				              	};

				              	require(['https://webchat.7moor.com/javascripts/7moorInit.js?accessId=5ce69f50-0731-11e8-9337-81e156236a04&autoShow=false']);
				              	
				          	}
				      	});

	      				//非院校模式
	      				if(appJSON.schoolModel==='0' || appJSON.schoolModel==5){
	      					siteEntrance(renderData.data.qcodePic);
	      				}
	      				else{
	      					$('.site-header').append(template('site-menu-temp',appJSON));

					        renderHelpbar({qcodePic:renderData.data.qcodePic});

					        window.pageApp.run(location.hash?location.hash:'#/xuexi');
	      				}
      				}
      				else{
      					$('#app-container').html(
	      					template('page-error-temp',{message:'数据异常'})
	      				);
      				}
      			}
      			else{
      				$('#app-container').html(
      					template('page-error-temp',{message:renderData.message})
      				);
      			}
      			//$('[data-role="page-loading"]').hide();        
      		},
      		fail:function(errorMessage){
      			$('#app-container').html(
  					template('page-error-temp',{message:errorMessage})
  				)
  				//$('[data-role="page-loading"]').hide();
      		}
      	});     	
	})();

	//问班主任
	function askTeacher(){
		//如果没有分配班主任就不能进行下一步
		if(!$.cookie('BZR_ID') && $.cookie('WEBSID')){
			$.resultDialog(0,'未分配班主任，不能操作',2000)
			return;
		}

		$.mydialog({
		  id:'pop2',
		  width:640,
		  zIndex:1000,
		  height:500,
		  content: 'pop-ask-teacher.html'
		});
	}

	$(document).on('click','[data-role="ask-teacher"]',function(event) {
		event.preventDefault();
		askTeacher();
	})

	//在线咨询
	.on('click', '[data-role="ee-contact"]:not(.disabled)', function(event) {
		event.preventDefault();

		var label=$(this).html();
		var ifm='<iframe id="service-online-check-iframe" name="service-online-check-iframe" src="service-online-check-iframe.html" style="display:none;"></iframe>';

		if(window.frames['service-online-check-iframexx']){
			$('#service-online-check-iframe').remove();
		}

		$('body').append(ifm);

		$(this).addClass('disabled')
		.data('label',label).html('连接中...');

		if($(this).is(':input')){
			$(this).prop('disabled', true);
		}
		//网站客服 功能方法
      	//qimoChatClick();


		/*
			var now=new Date();
		    var year=now.getFullYear()
		    var month=now.getMonth()+1;
		    var date=now.getDate();
		    
		    var startDate=new Date();
		    var startDate2=new Date();
		    var endDate=new Date();
		    var endDate2=new Date();

		    //工作日
		    startDate.setHours(9,0,0);
		    endDate.setHours(21,0,0);

		    //节假日
		    startDate2.setHours(10,0,0);
		    endDate2.setHours(18,0,0);

		    var nowTime=now.getTime();
		    var startDateTime=startDate.getTime();
		    var endDateTime=endDate.getTime();

		    var startDateTime2=startDate2.getTime();
		    var endDateTime2=endDate2.getTime();

		    var holidayType=holidayJson[year][(month<10?('0'+month):month)+''+(date<10?('0'+date):date)];

		    //ee联系条件 周一至周五：09:00~18:00 ；法定节假日：10:00~18:00
		    if(
		    	
		    	( (holidayType>0) && (nowTime<startDateTime2 || nowTime>endDateTime2 ) ) || 
		    	( nowTime<startDateTime || nowTime>endDateTime )
		    	
		    ){
			    	$.alertDialog({
					width:340,
				    height:270,
				    ok:false,
				    cancelLabel:'关闭',
				    content:template('ee-tips-temp',{})
				});
				event.preventDefault();
		    }
	    */
	    
	});

	//浏览器适配
	;(function(){
		var $html=$('html');
		if( $html.hasClass('ie67') || $html.hasClass('ie8') ){
			$(window).resize(function(event) {
				if($(window).width()<=1200){
					$('body').addClass('ie-adjust');
				}
				else{
					$('body').removeClass('ie-adjust');
				}
			});
		}
	})();

	//检测客服是否在线的回调方法
	window.chatOnlineCallback=function(result){
		if(typeof(result)=='undefined') return;

		var $btn=$('.disabled[data-role="ee-contact"]');
		if($btn.length>0){

			$btn.removeClass('disabled').html($btn.data('label'));

			if($btn.is(':input')){
				$btn.prop('disabled', false);
			}
		}

		if(result){
			console.info('客服在线');

			qimoChatClick();
		}
		else{
			console.info('客服离线');
			askTeacher();
		}

		$('#service-online-check-iframe').remove();
	}
});
</script>

<!--ee联系提醒-->
<script type="text/template" id="ee-tips-temp">
	<div class="padding10 text-center">
		<div>
			<i class="alert-icon alert-icon-0x125 margin_r5"></i>
			<span class="text-orange font16">现在非服务时间，无法在线咨询</span>
		</div>
		<div class="margin_t15">
			（一） 周一至周五：09:00~21:00
		</div>
		<div class="margin_t15">
			（二） 法定节假日：10:00~18:00
		</div>
	</div>
</script>

<!--国家节假日json数据-->
<script type="text/javascript">
/*
//http://tool.bitefu.net/jiari/vip.php?d=2017&type=0&apikey=123456
//工作日对应结果为 0, 休息日对应结果为 1, 节假日对应的结果为 2
var holidayJson={"2017":{"0101":2,"0102":2,"0107":1,"0108":1,"0114":1,"0115":1,"0121":1,"0127":2,"0128":2,"0129":2,"0130":2,"0131":2,"0201":2,"0202":2,"0205":1,"0211":1,"0212":1,"0218":1,"0219":1,"0225":1,"0226":1,"0304":1,"0305":1,"0311":1,"0312":1,"0318":1,"0319":1,"0325":1,"0326":1,"0402":1,"0403":2,"0404":2,"0408":1,"0409":1,"0415":1,"0416":1,"0422":1,"0423":1,"0429":1,"0430":1,"0501":2,"0506":1,"0507":1,"0513":1,"0514":1,"0520":1,"0521":1,"0528":1,"0529":2,"0530":2,"0603":1,"0604":1,"0610":1,"0611":1,"0617":1,"0618":1,"0624":1,"0625":1,"0701":1,"0702":1,"0708":1,"0709":1,"0715":1,"0716":1,"0722":1,"0723":1,"0729":1,"0730":1,"0805":1,"0806":1,"0812":1,"0813":1,"0819":1,"0820":1,"0826":1,"0827":1,"0902":1,"0903":1,"0909":1,"0910":1,"0916":1,"0917":1,"0923":1,"0924":1,"1001":2,"1002":2,"1003":2,"1004":2,"1005":2,"1006":2,"1007":1,"1008":1,"1014":1,"1015":1,"1021":1,"1022":1,"1028":1,"1029":1,"1104":1,"1105":1,"1111":1,"1112":1,"1118":1,"1119":1,"1125":1,"1126":1,"1202":1,"1203":1,"1209":1,"1210":1,"1216":1,"1217":1,"1223":1,"1224":1,"1230":1,"1231":1},"2018":{"0101":2,"0102":1,"0106":1,"0107":1,"0113":1,"0114":1,"0120":1,"0121":1,"0127":1,"0128":1,"0203":1,"0204":1,"0210":1,"0211":1,"0215":1,"0216":2,"0217":1,"0218":1,"0219":1,"0220":1,"0221":1,"0224":1,"0225":1,"0303":1,"0304":1,"0310":1,"0311":1,"0317":1,"0318":1,"0324":1,"0325":1,"0331":1,"0401":1,"0405":2,"0406":1,"0407":1,"0408":1,"0414":1,"0415":1,"0421":1,"0422":1,"0428":1,"0429":1,"0430":1,"0501":2,"0505":1,"0506":1,"0512":1,"0513":1,"0519":1,"0520":1,"0526":1,"0527":1,"0528":1,"0529":1,"0530":2,"0602":1,"0603":1,"0609":1,"0610":1,"0616":1,"0617":1,"0623":1,"0624":1,"0630":1,"0701":1,"0707":1,"0708":1,"0714":1,"0715":1,"0721":1,"0722":1,"0728":1,"0729":1,"0804":1,"0805":1,"0811":1,"0812":1,"0818":1,"0819":1,"0825":1,"0826":1,"0901":1,"0902":1,"0908":1,"0909":1,"0915":1,"0916":1,"0922":1,"0923":1,"0924":2,"0929":1,"0930":1,"1001":2,"1002":2,"1003":2,"1004":2,"1005":1,"1006":1,"1007":1,"1013":1,"1014":1,"1020":1,"1021":1,"1027":1,"1028":1,"1103":1,"1104":1,"1110":1,"1111":1,"1117":1,"1118":1,"1124":1,"1125":1,"1201":1,"1202":1,"1208":1,"1209":1,"1215":1,"1216":1,"1222":1,"1223":1,"1229":1,"1230":1}}
*/
</script>
</body>
</html>