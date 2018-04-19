<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page"
	required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="paginationSize" type="java.lang.Integer"
	required="true"%> 


 <%
	int current = page.getNumber() + 1;
	int begin = Math.max(1, current - paginationSize / 2);
	int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());

	request.setAttribute("current", current);
	request.setAttribute("begin", begin);
	request.setAttribute("end", end);
%> 

<script type="text/javascript">

	function changeNumber(){
		var page = parseInt($("#page").val());
		var totalPages = parseInt($("#totalPages").val());
		if(isNaN(page) || page <= 0){
			page = 1;
		}
		if(page > totalPages){
			page = totalPages;
		}
		if(isNaN(page) || page <= 0){
			page = 1;
		}
		if($("#listForm").find("input[name='page']").val()==undefined) {
            $("#listForm").append('<input type="hidden" name="page" value="' + page + '">');
        }
		$("#page").val(page);
	}
	
	function gotoPage(){
		var page = parseInt($("#page").val());
		var totalPages = parseInt($("#totalPages").val());
		if(isNaN(page) || page <= 0){
			page = 1;
		}
		if(page > totalPages){
			page = totalPages;
		}
		if(isNaN(page) || page <= 0){
			page = 1;
		}
		window.location = "?page="+page+"&sortType=${sortType}&${searchParams}";
	}
</script>

<div class="page-container page-no-break clearfix">
	<input type="hidden" id="totalPages" value="<%=page.getTotalPages() %>">
	<div class="pageing-info">
		<div class="dataTables_info" id="dtable_info" role="status" aria-live="polite">
			<%=page.getTotalElements() %>条，共 <%=page.getTotalPages() %> 页，到第 <input id="page" name="page" type="text" class="form-control jump-page-input"
				value="<%=page.getNumber()+1 %>" onkeyup="changeNumber()" onblur="changeNumber()"> 页
			<button class="btn btn-block btn-default sure-btn" onclick="gotoPage()">确定</button>
		</div>
	</div>

	<div class="pageing-list">
		<div class="dataTables_paginate paging_simple_numbers"
			id="dtable_paginate">
			<ul class="pagination">
				
				<% if (page.hasPreviousPage()){%>
		                <li class="paginate_button previous"><a href="javascript: $.pageSkip('${current-1}');">上一页</a></li>
		                <%-- <li><a href="?page=${current-1}&sortType=${sortType}&${searchParams}">上一页</a></li> --%>
		         <%}else{%>
		                <li class="paginate_button previous disabled"><a href="#">上一页</a></li>
		         <%} %> 
				
				<c:forEach var="i" begin="${begin}" end="${end}">
		            <c:choose>
		                <c:when test="${i == current}">
		                    <%-- <li class="active"><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li> --%>
		                    <li class="paginate_button active"><a href="javascript: $.pageSkip('${i}');">${i}</a></li>
		                </c:when>
		                <c:otherwise>
		                    <%-- <li><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li> --%>
		                    <li class="paginate_button"><a href="javascript: $.pageSkip('${i}');">${i}</a></li>
		                </c:otherwise>
		            </c:choose>
		        </c:forEach>
					
				<% if (page.hasNextPage()){%>
		               	<%-- <li><a href="?page=${current+1}&sortType=${sortType}&${searchParams}">下一页</a></li> --%>
		               	<li class="paginate_button next"><a href="javascript: $.pageSkip('${current+1}');">下一页</a></li>
		         <%}else{%>
		                <li class="paginate_button next disabled"><a href="#">下一页</a></li>
		         <%} %>
			</ul>
		</div>
	</div>
</div>