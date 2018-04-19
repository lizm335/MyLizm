//login

window.onload = function(){

	var dc = document;
	var transition = 400;
	var loginSlideTransition = 400;
	
	
	//img load checked
	jns("img").imgsLoader(function(){
		$("#loading").fadeOut(transition);
		$("#mainBg").fadeIn(transition);
		$("#loginMain").delay(transition).fadeIn(transition);

		// window fixed
		s01OutsetFixed();
		s01ImgFixed();
		$(window).bind("resize",s01OutsetFixed);
		$(window).bind("resize",s01ImgFixed);
		
	})
	

	
	// window fixed
	function s01OutsetFixed(){
		var elm = dc.getElementById("bodyContainer");
		
		var nowWidth = dc.documentElement.clientWidth;
		var nowHeight = dc.documentElement.clientHeight;
		elm.style.height = nowHeight + "px";
	}
	
	function s01ImgFixed(){
		var bg = dc.getElementById("mainBg").children[0];
		var nowWidth = dc.documentElement.clientWidth;
		var nowHeight = dc.documentElement.clientHeight;
		if(!bg.getAttribute("orgWidth")||!bg.getAttribute("orgHeight")){
			bg.style.height = "auto";
			bg.style.width = "auto";
			bg.setAttribute("orgWidth",bg.offsetWidth);
			bg.setAttribute("orgHeight",bg.offsetHeight);
		}
		var bgOrgWidth = parseInt(bg.getAttribute("orgWidth"));
		var bgOrgHeight = parseInt(bg.getAttribute("orgHeight"));
		if(nowWidth > nowHeight){
			var fragHeight = nowWidth*bgOrgHeight/bgOrgWidth;
			var fragWidth = nowHeight*bgOrgWidth/bgOrgHeight;
			
			nowHeight <= fragHeight?(
				bg.style.width = nowWidth + "px",
				bg.style.height = fragHeight + "px",
				bg.style.marginTop = (nowHeight - fragHeight)/2 + "px",
				bg.style.marginLeft = 0
			):(
				bg.style.width = fragWidth + "px",
				bg.style.height = nowHeight + "px",
				bg.style.marginTop = 0,
				bg.style.marginLeft = (nowWidth - fragWidth)/2 + "px"
			);
		}
		else{
			var fragHeight = nowWidth*bgOrgHeight/bgOrgWidth;
			var fragWidth = nowHeight*bgOrgWidth/bgOrgHeight;
			nowWidth <= fragWidth?(
				bg.style.width = fragWidth + "px",
				bg.style.height = nowHeight + "px",
				bg.style.marginTop = 0,
				bg.style.marginLeft = (nowWidth - fragWidth)/2 + "px"
				
			):(
				bg.style.width = nowWidth + "px",
				bg.style.height = fragHeight + "px",
				bg.style.marginTop = (nowHeight - fragHeight)/2 + "px",
				bg.style.marginLeft = 0
			);
		}
	}

	

	
	function loginSlideDown(){
		var loginArea = jns.selector("#bodyContainer");
		var interval = 40;
		var T = loginSlideTransition / interval;
		var Tn = 0;
		var So = loginArea.offsetHeight;
		var Sn = So;
		var St = document.documentElement.clientHeight;

		(function slideDownEvent(){
			Tn < T?(
				St = document.documentElement.clientHeight,
				loginArea.style.height = jns.inertiaMotion(So,St,T).Sn(Tn) + "px",
				Tn++,
				window.scrollTo(0,0),
				setTimeout(arguments.callee,interval)
			):(
				loginArea.style.height = St + "px",
				$(window).bind("resize",s01OutsetFixed)	
			);
		})();
	}
}

// jns Library 0.1
var jns = (function(){
	_jns = function(){
		
		var dc = document;
		var _target = _jns.selector(arguments[0]);
		if(!_target){return;}
		if(typeof _target.length == "undefined"){_target = [_target];}
		
		return{
			// 
			each:function(func){
				for( var i = 0, len = _target.length; i < len; i++){
					if(typeof func == "function"){func.call(this)};
				}
				return this;
			},

			// img load status check 
			imgsLoader:function(callback){
				var loadedNum = 0;
				var cloneImgs = [];

				for( var i = 0, len = _target.length; i < len; i++){

					var fragSource = dc.createElement("IMG");
					fragSource.src = _target[i].src;
					fragSource.style.cssText = "position:absolute; top:0; left:0; visibility:hidden;"
					
					if(fragSource.readyState){
						
						fragSource.onreadystatechange = function(){
							alert("change")
							if( this.readyState == "loaded" || this.readyState == "complete" ){
								this.onreadystatechange = null;
								//onloadEvent.call(this);
							}
						}
						fragSource.onload = function(){alert(1)}
					}
					else{
						

						fragSource.onload = function(){
							onloadEvent.call(this);
						}

					}
					
					dc.body.appendChild(fragSource);
					if(fragSource.offsetWidth !=0){onloadEvent.call(fragSource)}
					cloneImgs[i] = fragSource;
				}
				function onloadEvent(){
					this.parentNode?this.parentNode.removeChild(this):'';
					loadedNum++;
					
					if(loadedNum >= len){
						typeof callback == "function"?callback():"";
					}
				}

				return this;
			}

		}
	}

	//惯性运动
	_jns.inertiaMotion = function(So,St,T){
		var S = Math.abs(St - So);
		var a = S/Math.pow(T/2,2);
		var Vt = a*T/4;
		
		
		return{
			Sn:function(Tn){
				var _Sn;
				Tn < T/2?(
					_Sn = So + a*Math.pow(Tn,2)/2 * ( parseInt( (St - So)/Math.abs(St - So) )||0 )
				):(
					Tn < T?(
						_Sn = So +  ( a*Math.pow(T/2,2)  - a*Math.pow(T - Tn,2)/2 )*( parseInt( (St - So)/Math.abs(St - So) )||0 )
					):(
						_Sn =  St
					)
				);
				
				return _Sn;
			},
			Vn:function(Tn){
				var _Vn;
				Tn < T/2?(
					_Vn = a*Tn/2
				):(
					Tn < T?(
						_Vn = Vt - a*(Tn - T/2)/2
					):(
						_Vn = 0
					)
				);
				return _Vn;
			}	
		}
	}
	
	_jns.selector = function(source){
		var dc = document;
			
		if( typeof source == "Object"){
			return source;
		}
		else if( typeof source == "string"){
			
			var strGroup = source.split(" ");
			var strGroup_len = strGroup.length;
			var fragSource;
			var fragSource_len;
			var result = dc;
			if(strGroup_len == 1 && strGroup[0].substring(0,1) == "#"){
				return dc.getElementById( strGroup[0].substring(1) )
			}
			else{
				if(dc.querySelectorAll){
					result = dc.querySelectorAll(source); 
				}
				else{
					for( var i = 0; i < strGroup_len; i++){
						fragSource = strGroup[i];
						fragSource_len = fragSource.length;
						if(fragSource_len == 0){continue;}
						switch(fragSource.substring(0,1)){
							case "#": result = idSelector(result,fragSource.substring(1)); break;
							case ".": result = classSelector(result,fragSource.substring(1)); break;
							default : result = tagSelector(result,fragSource); break;
						}
					}
					result = [].concat(result);
					
				}
				if(result.length == 0){
					return false;
				}
				
				else if(result.length == 1){
					return result[0];
				}
				
				else{
					return result;
				}
			}
		}
		
		function idSelector(target,idStr){
			return dc.getElementById(idStr);
		}
		
		function classSelector(target,className){
			var targetGroup = [].concat(target);
			var targetGroup_len = targetGroup.length;
			var result = [];
			var fragTarget;
			var fragGroup;
			var fragTarget_cells;
			var fragTarget_cells_len;
			var fragClassNames;
			var fragClassNames_len;
			for( var i = 0; i < targetGroup_len; i++){
				fragTarget = targetGroup[i];
				if(!fragTarget){continue;}
				fragTarget_cells = fragTarget.getElementsByTagName("*");
				fragTarget_cells_len = fragTarget_cells.length;
				
				for( var k = 0; k < fragTarget_cells_len; k++){
					fragClassNames = fragTarget_cells[k].className.split(" ");
					fragClassNames_len = fragClassNames.length;
					
					for( var j = 0; j < fragClassNames_len; j++){
						if(fragClassNames[j] == className){
							result = result.concat(fragTarget_cells[k]);
							break;
						}
					}
				}
				
				
			}
			return result;
		}
		
		function tagSelector(target,tagStr){
			var targetGroup = [].concat(target);
			var targetGroup_len = targetGroup.length;
			var result = [];
			var fragTarget;
			var fragGroup;
			var fragGroup_len;
			for( var i = 0; i < targetGroup_len; i++){
				fragTarget = targetGroup[i];
				if(!fragTarget){continue;}
				fragGroup = fragTarget.getElementsByTagName(tagStr);
				fragGroup_len = fragGroup.length;
				for( var j = 0; j < fragGroup_len;j++){
					result = result.concat(fragGroup[j]);
				}
			}
			return result;
		}
	}
	return _jns;
})()