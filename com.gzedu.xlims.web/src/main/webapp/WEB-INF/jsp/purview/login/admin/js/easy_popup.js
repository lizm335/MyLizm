 // JavaScript Document
/* 
 *
 * easyPopup(popupElement,closeBtnElement)
 * --popupElement:弹出框对象
 * --closeBtnElement:关闭按钮对象
 *
 * easyPopup.setPopupElement(element):设置弹出框对象
 *
 * easyPopup.setCloseBtnElement(element):设置关闭按钮对象
 *
 * easyPopup.withBackground(bool):设置是否带背景
 *
 * easyPopup.canMove(bool,obj):设置可否移动
 * --bool:布尔值,控制是否可移动
 * --obj:对象,若第一参数为true,设置触发移动的区域
 *
 * easyPopup.init():初始化弹出框
 *
 * easyPopup.isFix(bool):设置是否固定在屏幕上
 *
 * easyPopup.popupShow(int):显示弹出框,int单位为ms,设置显示所需时间,默认为null
 *
 * easyPopup.popupHide(int):隐藏弹出框,int单位为ms,设置显示所需时间,默认为null
 *
 * power by jackness 
 * date:2011-2-26
 */

var easyPopup = function(){
	
	//用户设置 start
	var option = {
		
		//背景深度
		bg_zIndex:9999,
		
		//背景透明度
		bg_alpha:0.8,
		
		//背景颜色
		bg_color:"#000",
		
		//背景id名称
		bg_id:"jackness_bg" + Math.round( Math.random()*1000 ),
		
		//淡出淡入时间
		fadeInOut:500,
		
		//弹出框id名称
		pop_id:"jackness_popup" + Math.round( Math.random()*1000)	
	}
	//用户设置 end
	
	
	var _popupElement,_popupArea,_effectObj,_closeBtnElement,_canMove,_withBackground,_moveArea,_isFix;
	var bgElement = document.getElementById(option.bg_id);
	
	arguments[0]? _popupElement = arguments[0]:"";
	arguments[1]? _closeBtnElement = arguments[1]:"";
	
	//判断是否ie6
	function isIE6(){
		if(window.XMLHttpRequest)
			return false;	
		else
			return true;
	}
	
	function getCssValue(obj,attribute){
		return obj.currentStyle?obj.currentStyle[attribute]:document.defaultView.getComputedStyle(obj,false)[attribute];	
	}
	//添加事件
	function addEvent(type,obj,func){
		if(obj.addEventListener){
			obj.addEventListener(type,func,false)	
		}
		else{
			var oldFunc = obj["on"+type];
			if(!obj.jackNEss_funcGroup){obj.jackNEss_funcGroup = [];obj.jackNEss_funcGroup[0] = oldFunc}
			obj.jackNEss_funcGroup[obj.jackNEss_funcGroup.length] = func;
			obj["on"+type] = function(){
				for( var i = 0; i < obj.jackNEss_funcGroup.length; i++){
					if( typeof obj.jackNEss_funcGroup[i] == "function"){
						obj.jackNEss_funcGroup[i].call(this);	
					}
				}
			}
			
		}
		
	}
	//移除事件
	function removeEvent(type,obj,func){
		if(obj.removeEventListener){
			obj.removeEventListener(type,func,false)	
		}
		else if(obj.jackNEss_funcGroup){
			for( var i = 0; i < obj.jackNEss_funcGroup.length; i++){
				if(obj.jackNEss_funcGroup[i] == func)
					delete obj.jackNEss_funcGroup[i];
			}
				
		}
	}
	//固定屏幕上
	function positionFix(obj){
		if(!obj){return;}
		
		var obj_top = (document.documentElement.scrollTop||document.body.scrollTop) +( document.documentElement.clientHeight - _popupArea.offsetHeight )/2;
		
		obj.style.top = obj_top + "px";
		obj.style.left = (document.documentElement.clientWidth - obj.offsetWidth)/2 + "px";	
		/*--	
		if(isIE6()){
		--*/
			obj.style.position = "absolute";
			
			window.onscroll = function(){
					obj.style.top = obj_top + (document.documentElement.scrollTop||0) + "px";
			}
		/*--
		}
		else{
			obj.style.position = "fixed";
			
		}
		--*/
	}
	
	//背景显示
	function bgShow(func){
		if(!bgElement){
			bgElement = document.createElement("div");
			bgElement.setAttribute("id",option.bg_id);
			bgElement.style.background = option.bg_color;
			bgElement.style.filter = "alpha(opacity="+option.bg_alpha*100+")";
			bgElement.style.opacity = option.bg_alpha;
			bgElement.style.position = "absolute";
			bgElement.style.left = 0;
			bgElement.style.top = 0;
			bgElement.style.width = "100%";
			bgElement.style.height = (document.body.offsetHeight > document.documentElement.offsetHeight?document.body.offsetHeight:document.documentElement.offsetHeight) + "px";
			bgElement.style.zIndex = option.bg_zIndex;	
			document.body.appendChild(bgElement);
		}
		func?func():""
		//fadeIn(bgElement,option.fadeInOut)	
	}
	
	//背景移除
	function bgRemove(func){
		fadeOut(bgElement,option.fadeInOut,function(){
			if(bgElement){
				document.body.removeChild(bgElement);
				bgElement = null;
				func?func():"";
			}		
		})
		
	}
	
	//弹出框初始化
	function popupReset(){
		_popupArea.style.top = (document.documentElement.scrollTop||document.body.scrollTop) +( document.documentElement.clientHeight - _popupArea.offsetHeight )/2 + "px";
		
		_popupArea.style.left = (document.body.offsetWidth - _popupArea.offsetWidth)/2 + "px";
	}
	
	//匀加速运动当前时间的位移计算
	function acce_Event(So,St,T,Tn){
		return (St - So)*Math.pow(Tn/T,2);
	}
	
	//匀减速度当前时间的位移计算
	function reacce_Event(So,St,T,Tn){
		return So+2*Tn*(St-So)/T-Math.pow(Tn/T,2)*(St-So)
	}
	
	//淡入
	function fadeIn(obj,int,func){
		if(!obj){return;}
		var orgOpacity = getCssValue(obj,"opacity")||1;
		var T = int/10;
		var Tn = 0;
		function fIn(){
			Tn < T?(
				obj.style.opacity = reacce_Event(0,orgOpacity,T,Tn),
				obj.style.filter = "alpha(opacity=" + Math.round(reacce_Event(0,orgOpacity,T,Tn)*100) + ")",
				Tn++,
				setTimeout(fIn,10)
			):(
				obj.style.opacity = orgOpacity,
				obj.style.filter = "alpha(opacity=" + orgOpacity*100 + ")",
				func?func():""
			);
		}
		fIn();
		
	}
	//淡出
	function fadeOut(obj,int,func){
		if(!obj){return;}
		
		var orgOpacity = getCssValue(obj,"opacity");
		var T = int/10;
		var Tn = 0;
		function fOut(){
			Tn < T?(
				obj.style.opacity = 1 - Tn/T,
				obj.style.filter = "alpha(opacity=" + ( 1 - Tn/T )*100 + ")",
				Tn++,
				setTimeout(fOut,10)
			):(
				obj.style.display = "none",
				obj.style.opacity = orgOpacity,
				obj.style.filter = "alpha(opacity=" + orgOpacity*100 + ")",
				func?func():""
			);
		}
		fOut();
	}
	
	//渐进放大
	function sizeIn(target,org,int,func){
		 if(!org){return;}
		 var flag = org;
		 var T = option.fadeInOut/10 -1;
		 var Tn = 0;
		 var org_left = flag.offsetLeft,org_top = flag.offsetTop;
		 while(flag = flag.offsetParent){
			 org_left += flag.offsetLeft;
			 org_top += flag.offsetTop;
		 }
		 target.style.display = "block";
		 var tar_width = target.offsetWidth;
		 var tar_height = target.offsetHeight;
		 var tar_left = (document.body.offsetWidth - tar_width)/2;
		 var tar_top = (document.documentElement.scrollTop||document.body.scrollTop) +( document.documentElement.clientHeight - target.offsetHeight )/2;
		 target.style.display = "none";
		 var bgDiv = document.createElement("div");
		 bgDiv.style.background = "#fff";
		 bgDiv.style.width = 0;
		 bgDiv.style.height = 0;
		 bgDiv.style.overflow = "hidden";
		 bgDiv.style.position = "absolute";
		 bgDiv.style.zIndex = option.bg_zIndex + 1;
		 bgDiv.style.top = org_top + "px";
		 bgDiv.style.left = org_left + "px";
		 document.body.appendChild(bgDiv);
		 
		 function acShow(){
			 Tn < T?(
				 bgDiv.style.top = reacce_Event(org_top,tar_top,T,Tn) + "px",
				 bgDiv.style.left = reacce_Event(org_left,tar_left,T,Tn) + "px",
				 bgDiv.style.width = reacce_Event(0,tar_width,T,Tn) + "px",
				 bgDiv.style.height = reacce_Event(0,tar_height,T,Tn) + "px",
				 //bgDiv.style.filter = "Alpha(opacity="+ reacce_Event(0,100,T,Tn) +")",
				 bgDiv.style.opacity = reacce_Event(0,1,T,Tn),
				 Tn++,
				 setTimeout(acShow,10)
			 ):(
				document.body.removeChild(bgDiv),
				target.style.display = "block",
				func?func():""
			 );
		 }
		 acShow()
	}
	
	//渐进收缩
	function sizeOut(target,org,int,func){
		 var flag = org;
		 var T = option.fadeInOut/10;
		 var Tn = 0;
		 var org_left = flag.offsetLeft + flag.offsetWidth/2,org_top = flag.offsetTop + flag.offsetHeight/2;
		 while(flag = flag.offsetParent){
			 org_left += flag.offsetLeft;
			 org_top += flag.offsetTop;
		 }
		 target.style.display = "block";
		 var tar_width = target.offsetWidth;
		 var tar_height = target.offsetHeight;
		 var tar_left = parseInt(target.style.left);
		 var tar_top = parseInt(target.style.top);
		 target.style.display = "none";
		 var bgDiv = document.createElement("div");
		 bgDiv.style.background = "#fff";
		 bgDiv.style.width = target.offsetWidth;
		 bgDiv.style.height = target.offsetHeight;
		 bgDiv.style.overflow = "hidden";
		 bgDiv.style.position = "absolute";
		 bgDiv.style.zIndex = option.bg_zIndex + 1;
		 bgDiv.style.top = tar_top + "px";
		 bgDiv.style.left = tar_left + "px";
		 document.body.appendChild(bgDiv);
		 
		 function acShow(){
			 Tn < T?(
			 	 bgDiv.style.top = reacce_Event(tar_top,org_top,T,Tn) + "px",
				 
				 bgDiv.style.left = reacce_Event(tar_left,org_left,T,Tn) + "px",
				 bgDiv.style.width = reacce_Event(tar_width,0,T,Tn) + "px",
				 bgDiv.style.height = reacce_Event(tar_height,0,T,Tn) + "px",
				 //bgDiv.style.filter = "Alpha(opacity="+ reacce_Event(0,100,T,Tn) +")",
				 bgDiv.style.opacity = reacce_Event(1,0,T,Tn),
				 Tn++,
				 setTimeout(acShow,10)
			 ):(
			 	document.body.removeChild(bgDiv),
				_withBackground? bgRemove(): "",
				func?func():""
			 );
		 }
		 acShow()
	}
	
	//移动模块用 函数
	function move_event(){
		if(!_moveArea){return;}
		var orgX,orgY;
		var min_X,max_X,min_Y,max_Y;
		_moveArea.onmousedown = function(e){
			e = e || window.event;
			orgX = parseInt( getCssValue(_popupArea,"left") ) - e.clientX;
			orgY = parseInt( getCssValue(_popupArea,"top") ) - e.clientY;
			min_X = 0;
			max_X = document.body.offsetWidth - _popupArea.offsetWidth;
			min_Y = 0;
			max_Y = document.body.offsetHeight - _popupArea.offsetHeight;
			window.onmousemove = function(e){
				e = e||window.event;
				var posX = orgX + e.clientX;
				var posY = orgY + e.clientY;
				
				if(posX < min_X){posX = min_X};
				if(posX > max_X){posX = max_X};
				if(posY < min_Y){posY = min_Y};
				if(posY > max_Y){posY = max_Y};
				
				_popupArea.style.left = posX + "px";
				_popupArea.style.top = posY + "px";	
			}
			window.onmouseup = function(){
				window.onmousemove = null;
				window.onmouseup = null;	
			}
		}
	}
	return{
		
		//设置弹出框对象
		setPopupElement:function(obj){
			_popupElement = obj;
			return this;
		},
		
		//设置关闭按钮对象
		setCloseBtnElement:function(obj){
			_closeBtnElement = obj;
			return this;
		},
		
		//设置是否带背景
		withBackground:function(bool){
			_withBackground = bool;
			return this;
		},
		
		//设置是否可移动
		canMove:function(bool,obj){
			_canMove = bool;
			arguments[1]?_moveArea = arguments[1]:"";
			return this;
		},
		
		//设置是否fix
		isFix:function(bool){
			_isFix = bool;
			return this;
		},
		
		//弹出框显示
		popupShow:function(){
			if(typeof arguments[0] == "object"){
				parseInt(arguments[1])? option.fadeInOut = parseInt(arguments[1]):"";
				_effectObj = arguments[0]; 
				sizeIn(arguments[0]);	
			}
			
			else if(parseInt(arguments[0])){
				option.fadeInOut = parseInt(arguments[0]);
				
			}
			if(!_popupElement){return;}
			_withBackground?(
				bgShow(function(){
					edo()	
				})
			):(
				edo()
			);
			function edo(){
				_effectObj?(
					_popupArea.style.display = "block",
					sizeIn(_popupArea,_effectObj,option.fadeInOut,function(){
						e_reset();
					})
				):(
					fadeIn(_popupArea,option.fadeInOut,function(){
						e_reset();
					})
				)
				function e_reset(){
					popupReset();	
					if(_isFix){
						positionFix(_popupArea);
					}	
				}		
			}
			
		},
		
		//弹出框隐藏
		popupHide:function(){
			if(!_popupElement){return;}
			window.onscroll = null;
			if(typeof(arguments[0]) == "object" &&arguments[0].tagName){
				parseInt(arguments[1])? option.fadeInOut = parseInt(arguments[1]):"";
				_effectObj = arguments[0];
				
			}
			else if(parseInt(arguments[0])){
				option.fadeInOut = parseInt(arguments[0]);
				//_popupArea.style.display = "none";	
			}
			_withBackground?(
				_effectObj?(
					sizeOut(_popupArea,_effectObj,option.fadeInOut,function(){
						bgRemove()
					})
				):(
					fadeOut(_popupArea,option.fadeInOut,function(){
						bgRemove();
					})
				)	
				
				
			): (
				_effectObj?(
					sizeOut(_popupArea,_effectObj,option.fadeInOut)
				):(
					fadeOut(_popupArea,option.fadeInOut)
				)
			);
			
			
		},
		
		//数据初始化
		init:function(){
			if(!_popupElement){return;}
			
			//初始化pop框
			_popupArea = document.createElement("div");
			_popupArea.setAttribute("id",option.pop_id);
			_popupArea.style.position = "absolute";
			_popupArea.style.float = "left";
			_popupArea.style.display = "none";
			_popupArea.style.zIndex = option.bg_zIndex + 1;
			
			_popupElement.style.display = "block";
			_popupElement.style.left = 0;
			_popupElement.style.top = 0;
			_popupArea.style.width = _popupElement.offsetWidth + "px";
			_popupArea.style.height = _popupElement.offsetHeight + "px";
			
			document.body.appendChild(_popupArea);
			_popupArea.appendChild(_popupElement);
			
			if( _closeBtnElement){
				addEvent("click",_closeBtnElement,this.popupHide)
			}
			if(_canMove){
				move_event() 	
			}
		}
		
			
	}	
}