<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="http://eefile.gzedu.com/js/lhgdialog.min.js"></script>
<script type="text/javascript" src="http://eefile.gzedu.com/js/json2.js"></script>
<c:set var="ctx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>

<script type="text/javascript">

/**
 * imgId : img标签ID
 * filetype 上传的类型 默认为 "bmp|jpg|gif|png"
 * filesize 限制大小 单位是 M 默认为 2 M
 * callback: 回调函数
 *filenum :允许上传的文件数量
 *keepName：false返回MD5文件名，true返回上传文件名
 *imgPaths：上传多张时候，给页面点击层添加 input 隐藏域数组
 *调用参考：<a onclick="javascript:uploadImages.call(this,'headImgId','headImgUrl','png,jpg','100',addPost,'1000','true','imgPaths');" data-role="importPhoto"><i class="fa fa-fw fa-sign-in"></i> 批量导入照片</a>
 */
function uploadImages(imgId,imgPath,filetype,filesize,callback,filenum,keepName,imgPaths){
	 var _this=this;
	 if(typeof(filetype) == "undefined" || filetype == null){
		 filetype = "bmp|jpg|gif|png";
	 }
	 if(typeof(filesize) == "undefined" || filesize == null){
		 filesize = 2;
	 }
	 if(typeof(filenum) == "undefined" || filenum == null){
		 filenum = 1;
	 }
	 if(typeof(keepName) == "undefined" || keepName == null){
		 keepName = false;
	 }
	var filecwd = "/files2/xlims/file";
    var appId = "APP038";
	var hostport = document.location.host;
	var url = "http://eefile.gzedu.com/upload/toUpload.do?formMap.keepName="+keepName
			+"&formMap.filenum="+filenum+"&formMap.target=newpage&formMap.filetype="
			+filetype+"&formMap.filecwd="+filecwd+"&formMap.appId="
			+appId+"&formMap.filesize="+filesize+
			"&formMap.origin=${ctx}/eefileupload/uploadIframe.jsp";
	$("#uploadModalBody").find("iframe").attr("src", url);
	$("#uploadModal").find(".btn-success").unbind("click");
	$("#uploadModal").find(".btn-success").bind("click", function() {
		var fileList = window['FILELIST'], 
		cFileName = [],
		filelist = [],
		NameMD5List = [];
		if(fileList && fileList.length > 0){
			for(var i = 0; i < fileList.length; i++){
				cFileName.push(fileList[i].CFileName);
				filelist.push(fileList[i].SFileName);
				NameMD5List.push(fileList[i].FileMD5);
				if(imgPaths.length>0 && imgPaths != "undefined"){
					$(_this.parentNode).append('<input type="hidden" name="'+imgPaths+'" value="'+ fileList[i].SFileName+'"/>');
				 }
			}
    		$('#'+imgId).attr("src",filelist.join(''));
    		$('#'+imgId).val(filelist.join(''));
    		$('#'+imgPath).val(filelist.join(''));
			window['FILELIST'] = [];
		}
		$("#uploadModal").modal("hide");
		if (typeof callback == "function") {
			callback();
		}
	});
	$("#uploadModal").modal();
}


/**
 * imgId : img标签ID
 * filetype 上传的类型 默认为 "bmp|jpg|gif|png"
 * filesize 限制大小 单位是 M 默认为 2 M
 * callback: 回调函数
 */
function uploadImage(imgId,imgPath,filetype,filesize,callback){
	/**
	 * filetype 上传的类型
	 * filesize 限制大小 单位是 M
	 * filecwd 文件上传路径
	 */
	 if(typeof(filetype) == "undefined" || filetype == null){
		 filetype = "bmp|jpg|gif|png";
	 }
	 if(typeof(filesize) == "undefined" || filesize == null){
		 filesize = 2;
	 }
	 var filecwd = "/files2/xlims/file";
	
    var appId = "APP038";
	var hostport = document.location.host;
	var url = "http://eefile.gzedu.com/upload/toUpload.do?formMap.target=newpage&formMap.filetype="+filetype+"&formMap.filecwd="+filecwd+"&formMap.appId="+appId+"&formMap.filesize="+filesize+"&formMap.origin=${ctx}/eefileupload/uploadIframe.jsp";

	$("#uploadModalBody").find("iframe").attr("src", url);
	
	$("#uploadModal").find(".btn-success").unbind("click");
	$("#uploadModal").find(".btn-success").bind("click", function() {
		var fileList = window['FILELIST'], 
		cFileName = [],
		filelist = [],
		NameMD5List = [];
		if(fileList && fileList.length > 0){
			for(var i = 0; i < fileList.length; i++){
				cFileName.push(fileList[i].CFileName);
				filelist.push(fileList[i].SFileName);
				NameMD5List.push(fileList[i].FileMD5);
			}
    		$('#'+imgId).attr("src",filelist.join(''));
    		$('#'+imgId).val(filelist.join(''));
    		
    		$('#'+imgPath).val(filelist.join(''));
    		
			window['FILELIST'] = [];
		}
		$("#uploadModal").modal("hide");
		
		if (typeof callback == "function") {
			callback();
		}
	});
	
	$("#uploadModal").modal();
}

String.prototype.format= function(){
	var args = arguments;
	return this.replace(/\{(\d+)\}/g,function(s,i){
		return args[i];
	});
}
/**
 * container:容器对象
 *formControlName：隐藏域控件名称
 */
function uploadImageNew(container,formControlName,callback){
	/**
	 * filetype 上传的类型
	 * filesize 限制大小 单位是 M
	 * filecwd 文件上传路径
	 */
	var filetype = "bmp|jpg|gif|png";
	var filesize = 2;
	var filecwd = "/files2/xlims/file";
    var appId = "APP038";
	var hostport = document.location.host;
	var url = "http://eefile.gzedu.com/upload/toUpload.do?formMap.target=newpage&formMap.filetype="+filetype+"&formMap.filecwd="+filecwd+"&formMap.appId="+appId+"&formMap.filesize="+filesize+"&formMap.origin=${ctx}/eefileupload/uploadIframe.jsp";

	$("#uploadModalBody").find("iframe").attr("src", url);
	$("#uploadModal").find(".btn-success").unbind('click').bind("click", function() {
		var fileList = window['FILELIST'], 
		cFileName = [],
		filelist = [],
		NameMD5List = [];
		if(fileList && fileList.length > 0){
			var htmlTemp=[
			              	'<li>',
	                           ' <img src="{0}">',
	                           '<input type="hidden" name="{1}" value="{0}">',
	                           ' <span class="glyphicon glyphicon-remove-sign remove-addon" data-toggle="tooltip" title="删除"></span>',
	                        '</li>'
			              ].join('')
			var result=[];
			for(var i = 0; i < fileList.length; i++){
				cFileName.push(fileList[i].CFileName);
				filelist.push(fileList[i].SFileName);
				NameMD5List.push(fileList[i].FileMD5);
				var t=htmlTemp;
				result.push( htmlTemp.format( fileList[i].SFileName , formControlName ));
			}
			
    		$(container).append(result.join(''));
			window['FILELIST'] = [];
			if (typeof callback == "function") {
				callback();
			}
			
		}
		$("#uploadModal").modal("hide");
	});
	$("#uploadModal").modal();
	
}


/**
 * fileName : 标签ID
 * filePath : 标签ID
 * filetype: 上传的类型 ，默认为"xls|doc|docx"
 * filesize: 限制大小 单位是 M， 默认为 2 M
 * callback: 回调函数
 */
function uploadFile(fileName,filePath,filetype,filesize,callback){
	/**
	 * filetype 上传的类型
	 * filesize 限制大小 单位是 M
	 * filecwd 文件上传路径
	 */
	if(typeof(filetype) == "undefined" || filetype == null){
		 filetype =  "xls|doc|docx|zip|rar";
	 }
	 if(typeof(filesize) == "undefined" || filesize == null){
		 filesize = 50;
	 }
	 var filecwd = "/files2/xlims/file";
	
    var appId = "APP038";
	var hostport = document.location.host;
	var url = "http://eefile.gzedu.com/upload/toUpload.do?formMap.target=newpage&formMap.filetype="+filetype+"&formMap.filecwd="+filecwd+"&formMap.appId="+appId+"&formMap.filesize="+filesize+"&formMap.origin=${ctx}/eefileupload/uploadIframe.jsp";
	
	$("#uploadModalBody").find("iframe").attr("src", url);
	
	$("#uploadModal").find(".btn-success").unbind("click");
	$("#uploadModal").find(".btn-success").bind("click", function() {
		var fileList = window['FILELIST'], 
		cFileName = [],
		filelist = [],
		NameMD5List = [];
		if(fileList && fileList.length > 0){
			for(var i = 0; i < fileList.length; i++){
				cFileName.push(fileList[i].CFileName);
				filelist.push(fileList[i].SFileName);
				NameMD5List.push(fileList[i].FileMD5);
			}
			$('#'+fileName).val(cFileName.join(''));
			$('#'+filePath).val(filelist.join(''));
			window['FILELIST'] = [];
		}
		$("#uploadModal").modal("hide");
		
		if (typeof callback == "function") {
			callback();
		}
	});
	
	$("#uploadModal").modal();
}

function process(result){
	window['FILELIST'] = JSON.parse(result['data']);
}
</script>

<div id="uploadModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="uploadLabel" aria-hidden="true"> 
 	<div class="modal-dialog">
 		<div class="modal-content" style="height: 550px; width: 500px; z-index: 9999">
		     <div class="modal-header"> 
		         <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		            <h4 class="modal-title" id="uploadLabel">文件上传</h4>
		     </div> 
		     <div id="uploadModalBody" class="modal-body" style="height: 400px; width: 500px;"> 
		     	<iframe src="" width="100%" height="100%"  scrolling="no" frameborder="0" ></iframe>
		     </div> 
		     <div class="modal-footer"> 
		     	<button type="button"  class="btn btn-success">确定</button>
		         <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
		     </div> 
     	</div>
     </div>
 </div>
