<script>
    	var getQueryString = function(url) {
		var noQuery = 1, paramStr;
		paramStr = url && (url.split('?')[1] || noQuery) || 				 	document.location.search.slice(1) || noQuery;
		if (paramStr === noQuery) return false;
		var params = paramStr.split('&'),queryParam = {},param;
		for (var i = 0; i < params.length; i++) {
			param = params[i].split('=');				
			try {
				queryParam[param[0]] = decodeURIComponent(param[1]);
			} catch(e) {
				queryParam[param[0]] = null;
			}
		};
		return queryParam;
	}
	var data = getQueryString(location.href);
	parent.parent.process(data);
 </script>
