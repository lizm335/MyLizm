<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<div class="box-header with-border">
	<h3 class="box-title">打印学生信息</h3>
</div>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<object id="LODOP" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/static/js/install_lodop32.exe" ></embed>
</object> 
</head>
<body>
<div id="form1">
<form>
<table width="350">
  <tr>
    <td><span class="jxdmc" style="padding-top: 15px; font-size: 16px" >${gjtStudentInfo.getGjtSchoolInfo().getXxmc()}</td>
  </tr>
  <tr>
    <td width="50%" style="height: 40px; padding-bottom: 10px;font-size: 16px">${gjtStudentInfo.xm}</td>
  </tr>
  <tr>
    <td width="50%"  style="height: 40px; padding-bottom:15px;font-size: 16px"><dic:getLabel typeCode="Sex" code="${gjtStudentInfo.xbm}"/></td>
  </tr>
  <tr style=" display: none">
    <td width="50%"><span class="ndcsrq" style="padding-bottom:10px;">${fn:substring(fn:trim(gjtStudentInfo.sfzh), 6, 14)}</span></td>
  </tr>
  <tr>
    <td width="50%"  style="padding-bottom:13px;font-size: 16px"><span class="years" style=" padding-left: 45px"></span><span class="month" style=" padding-left: 42px"></span><span class="day" style=" padding-left: 52px"></span></td>
  </tr>
  <tr style=" display: none">
    <td width="50%"><span class="qmzcjxd"></span><br /><span class="hmzcjxd" style="padding-left: 22px"></span></td>
  </tr>
  <tr>
	  <c:choose>
	  		<c:when test="${gjtStudentInfo.getGjtSpecialty().getZymc() == '工商管理（市场营销方向）'}">
	   			 <td width="50%" style=" padding-top:0px;font-size: 15px">${gjtStudentInfo.getGjtSpecialty().getZymc()}</td>
	 	 	</c:when>
	  		<c:when test="${gjtStudentInfo.getGjtSpecialty().getZymc() == '工商管理（工商企业管理方向）'}">
	   			 <td width="50%" style="padding-top:0px;font-size: 15px">${gjtStudentInfo.getGjtSpecialty().getZymc()}</td>
	 	 	</c:when>
	 	 	<c:when test="${fn:length(gjtStudentInfo.getGjtSpecialty().getZymc()) >'16' }">
	   			 <!-- <td width="50%" style="height: 58px;padding:13px 0px 0px 15px; font-size: 13px"> -->
	   			 <td width="50%" style="padding-bottom:10px;font-size: 13px;">
	   			 	${fn:substring(gjtStudentInfo.getGjtSpecialty().getZymc(), 0, 9)}<br />${fn:substring(gjtStudentInfo.getGjtSpecialty().getZymc(), 10, fn:length(gjtStudentInfo.getGjtSpecialty().getZymc()))}
	   			 </td>
	 	 	</c:when>
	  		<c:otherwise>
	    		<td width="50%"  style=" padding-top: 5px;font-size: 15px">${gjtStudentInfo.getGjtSpecialty().getZymc() }</td>
	  		</c:otherwise>
	  </c:choose>
  </tr>
  <tr>
    <%-- <td width="50%"  style=" padding-top: 9px;font-size: 13px">${ map.XH}</td> --%>
    <c:choose>
    	<c:when test="${fn:length(gjtStudentInfo.getGjtSpecialty().getZymc()) >'16' }">
	   			 <td width="50%"  style="padding-bottom:13px;font-size: 14px">${gjtStudentInfo.xh}</td>
	 	 	</c:when>
	  		<c:otherwise>
	    		<td width="50%"  style=" padding-top: 20px;font-size: 15px">${gjtStudentInfo.xh}</td>
	  		</c:otherwise>
	</c:choose>
  </tr>
</table>
</form>
</div>
<div style=" padding-top: 20px; padding-left: 100px">
<p><a href="javascript:PreviewNoneStyle()">预览打印</a></p>
</div>
<script language="javascript" type="text/javascript"> 
        var LODOP; //声明为全局变量
	function PreviewNoneStyle(){
		LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));
   		var strFormHtml=document.getElementById("form1").innerHTML;
		LODOP.ADD_PRINT_HTM(50,145,300,400,strFormHtml);
		/* LODOP.ADD_PRINT_HTM(50,145,300,400,strFormHtml); */
		if(!confirm("你确定要打印？")) return;
		LODOP.PREVIEW();
	};	
	
	function showjiaowmc(){
		var jxd = $('.jxdmc').text();
		var qmjxd = jxd.substring(0,6);
		var index = jxd.length;
		var hmjxd = jxd.substring(6,index);
		$('.qmzcjxd').text(qmjxd);
		$('.hmzcjxd').text(hmjxd);
	}
	
	function showndcsrq(){
		var ndrq = $('.ndcsrq').text();
		if(ndrq!=null && ndrq !=""){
			$('.years').text(ndrq.substr(0, 4));
			$('.month').text(ndrq.substr(4, 2));
			$('.day').text(ndrq.substr(6, 2));
		}
	}
	
	$(function gljxdmc() {
		showndcsrq();
		showjiaowmc();
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
