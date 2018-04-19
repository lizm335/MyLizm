<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<div class="box-header with-border">
	<h3 class="box-title">打印学生信息</h3>
</div>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/static/js/install_lodop32.exe"></embed>
</object> 
</head>
<body>
<div id="form1">
<form>
<table width="345px">
  <tr>
    <td style="padding:0px 30px 15px 10px;height:40px; font-size: 13px" width="50%">${gjtStudentInfo.xm}</td>
  </tr>
  <tr>
    <td width="50%" style="padding:10px 30px 15px 10px;height:35px; font-size: 13px"><dic:getLabel typeCode="Sex" code="${gjtStudentInfo.xbm}"/></td>
  </tr>
  <tr>
    <td width="50%" style="height:35px;padding:10px 0px 0px 35px; font-size: 13px">${fn:substring(fn:trim(gjtStudentInfo.sfzh), 6, 14)}</td>
  </tr>
  <tr style=" display: none">
    <td style="height:43px;padding:15px 0px 0px 40px;font-size: 10px">
    		<input type="text" id="jxdmc"  value="${gjtStudentInfo.getGjtSchoolInfo().getXxmc()}"/>
    </td>
  </tr>
  <tr>
    <td width="50%" style="height:43px;padding:0px 0px 0px 10px; font-size: 13px" >
    <span id="qmzcjxd" style="height: 20px;margin:0px 0px 0px 35px"></span><br/>
    <span id="hmzcjxd" style="height: 20px;margin:0px 0px 0px 35px"></span></td>
    </tr>
  <tr>
	  <c:choose>
	 	 	<c:when test="${'8'<= fn:length(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc()))  &&  fn:length(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc()))<='11' }">
	   			 <td width="50%" style="height:43px;padding:15px 0px 0px 25px; font-size: 13px">${fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc()) }</td>
	 	 	</c:when>
	 	 	<c:when test="${fn:length(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc())) >'11' &&  fn:length(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc())) <='22' }">
	   			 <!-- <td width="50%" style="height: 58px;padding:13px 0px 0px 15px; font-size: 13px"> -->
	   			 <td width="50%" style="height:43px; padding:10px 0px 0px 25px;">
	   			 	<span  style="height: 20px;margin:15px 0px 0px 25px;font-size: 13px">${fn:substring(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc()), 0, 11)}</span><br />
	   			 	<span style="height: 20px;margin:15px 0px 0px 25px;font-size: 13px">${fn:substring(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc()), 11, 22)}</span>
	   			 </td>
	 	 	</c:when>
	 	 	
	 	 	<c:when test="${fn:length(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc())) >'22'  }">
	   			 <!-- <td width="50%" style="height: 58px;padding:13px 0px 0px 15px; font-size: 13px"> -->
	   			 <td width="50%" style="height:43px; padding:15px 0px 0px 25px;">
	   			 	<span  style="height: 20px;margin:15px 0px 0px 25px;font-size: 13px">${fn:substring(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc()), 0, 11)}</span><br />
	   			 	<span style="height: 20px;margin:15px 0px 0px 25px;font-size: 13px">${fn:substring(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc()), 11, 22)}</span><br />
	   			 	<span style="height: 20px;margin:15px 0px 0px 25px;font-size: 13px">${fn:substring(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc()), 22, fn:length(fn:trim(gjtStudentInfo.getGjtSpecialty().getZymc())))}</span>
	   			 </td>
	 	 	</c:when>
	 	 	
	  		<c:otherwise>
	    		<td width="50%" style="height: 58px;padding:15px 30px 0px 25px;">${gjtStudentInfo.getGjtSpecialty().getZymc()}</td>
	  		</c:otherwise>
	  </c:choose>
  </tr>
  <tr>
    <td width="50%" style="height:43px;padding:35px 0px 0px 25px;font-size: 13px">${gjtStudentInfo.xh}</td>
  </tr>
</table>
</form>
</div>
<div style=" padding-top: 20px; padding-left: 100px">
<p><a href="javascript:PreviewNoneStyle()">预览打印</a></p>
</div>
<script type="text/javascript"> 
    var LODOP; //声明为全局变量
	function PreviewNoneStyle(){
 		LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
   		var strFormHtml=document.getElementById("form1").innerHTML;
		LODOP.ADD_PRINT_HTM(48,325,300,400,strFormHtml); 
		LODOP.PREVIEW();//打印预览 
		
	};	

	
	$(document).ready(function(){   
	/* 	var jxd = $('.jxdmc').text().trim(); */
		var jxd=document.getElementById("jxdmc").value;
		if(jxd==''||jxd=='undefined'){
			document.getElementById("qmzcjxd").innerHTML="获取学院名称失败，请联系管理员";
		}else{
			var qmjxd = jxd.substring(0,6);
			var index = jxd.length;
			var hmjxd = jxd.substring(6,index);
			document.getElementById("qmzcjxd").innerHTML=qmjxd;
			document.getElementById("hmzcjxd").innerHTML=hmjxd;
		}
/* 		$('.qmzcjxd').text(qmjxd);
		$('.hmzcjxd').text(hmjxd); */
		//PreviewNoneStyle();
	});
	
	function MyPrint() {
		//LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
		if(!confirm("你确定要打印？")) return;
		PreviewNoneStyle();
		LODOP.PRINT();			
	}
</script> 
</body>
</html>
