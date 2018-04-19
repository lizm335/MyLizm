<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body>

<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">选择教材</h3>
	</div>
	<div class="box-body cn-container">
		<c:choose>
			<c:when test="${not empty errorMsg}">
				${errorMsg}
			</c:when>
			<c:otherwise>
				<div class="table-responsive">
					<table class="table table-bordered table-striped margin-bottom-none f12">
					  <thead>
					    <tr>
					      <th width="6%"><input type="checkbox" data-role="select-all"></th>
					      <th width="10%" class="text-center">学期</th>
					      <th width="27%">课程名称</th>
					      <th width="10%">教材类型</th>
					      <th width="27%">教材名称</th>
					      <th width="10%">教材价格</th>
					      <th width="10%">配送状态</th>
					    </tr>
					  </thead>
					  <tbody>
					  	<c:forEach items="${gradeSpecialtyPlanMap}" var="map">
					  		<c:set var="rowspan" value="0"></c:set>
					  		<c:forEach items="${map.value}" var="gradeSpecialtyPlan">
					  			<c:choose>
					      			<c:when test="${not empty gradeSpecialtyPlan.gjtCourse.gjtTextbookList}">
						      			<c:forEach items="${gradeSpecialtyPlan.gjtCourse.gjtTextbookList}" var="book">
						      				<c:set var="rowspan" value="${rowspan + 1}"></c:set>
						      				<c:set var="total" value="${total + book.discountPrice}"></c:set>
						      			</c:forEach>
					      			</c:when>
					      			<c:otherwise>
					      				<c:set var="rowspan" value="${rowspan + 1}"></c:set>
					      			</c:otherwise>
					      		</c:choose>
					  		</c:forEach>
					  		
					  		<c:forEach items="${map.value}" var="gradeSpecialtyPlan" varStatus="status">
						  		<c:choose>
					      			<c:when test="${not empty gradeSpecialtyPlan.gjtCourse.gjtTextbookList}">
						      			<c:forEach items="${gradeSpecialtyPlan.gjtCourse.gjtTextbookList}" var="book" varStatus="bookStatus">
						      				<c:choose>
						      					<c:when test="${status.first && bookStatus.first}">
							      					<tr>
							      					  <td width="6%"><input type="checkbox" class="chk-item" value="${book.textbookId}"></td>
											          <td width="10%" rowspan="${rowspan}" class="text-center">第${gradeSpecialtyPlan.kkxq}学期</td>
											          <td width="27%" rowspan="${fn:length(gradeSpecialtyPlan.gjtCourse.gjtTextbookList)}">${gradeSpecialtyPlan.gjtCourse.kcmc}</td>
											          <td width="10%">
											          	<c:choose>
											          		<c:when test="${book.textbookType == 1}">主教材</c:when>
											          		<c:otherwise>复习资料</c:otherwise>
											          	</c:choose>
											          </td>
											          <td class="material-name" width="27%">${book.textbookName}</td>
											          <td width="10%">￥${book.discountPrice}</td>
											          <td width="10%">
											          	<c:choose>
											          		<c:when test="${not empty statusMap[book.textbookId]}">
											          			<c:choose>
											          				<c:when test="${statusMap[book.textbookId] == 0}">
											          					<span class="gray9">未就绪</span>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 1}">
											          					<span class="text-orange">待配送</span>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 2}">
											          					<span class="text-light-blue">配送中</span>
											          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 3}">
											          					<span class="text-green">已签收</span>
											          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
											          				</c:when>
											          				<c:otherwise>--</c:otherwise>
											          			</c:choose>
											          		</c:when>
											          		<c:otherwise>
											          			<span class="gray9">未就绪</span>
											          		</c:otherwise>
											          	</c:choose>
											          </td>
											        </tr>
						      					</c:when>
						      					<c:when test="${bookStatus.first}">
							      					<tr>
							      					  <td width="6%"><input type="checkbox" class="chk-item" value="${book.textbookId}"></td>
											          <td width="27%" rowspan="${fn:length(gradeSpecialtyPlan.gjtCourse.gjtTextbookList)}">${gradeSpecialtyPlan.gjtCourse.kcmc}</td>
											          <td width="10%">
											          	<c:choose>
											          		<c:when test="${book.textbookType == 1}">主教材</c:when>
											          		<c:otherwise>复习资料</c:otherwise>
											          	</c:choose>
											          </td>
											          <td class="material-name" width="27%">${book.textbookName}</td>
											          <td width="10%">￥${book.discountPrice}</td>
											          <td width="10%">
											          	<c:choose>
											          		<c:when test="${not empty statusMap[book.textbookId]}">
											          			<c:choose>
											          				<c:when test="${statusMap[book.textbookId] == 0}">
											          					<span class="gray9">未就绪</span>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 1}">
											          					<span class="text-orange">待配送</span>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 2}">
											          					<span class="text-light-blue">配送中</span>
											          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 3}">
											          					<span class="text-green">已签收</span>
											          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
											          				</c:when>
											          				<c:otherwise>--</c:otherwise>
											          			</c:choose>
											          		</c:when>
											          		<c:otherwise>
											          			<span class="gray9">未就绪</span>
											          		</c:otherwise>
											          	</c:choose>
											          </td>
											        </tr>
						      					</c:when>
						      					<c:otherwise>
							      					<tr>
							      					  <td width="6%"><input type="checkbox" class="chk-item" value="${book.textbookId}"></td>
											          <td width="10%">
											          	<c:choose>
											          		<c:when test="${book.textbookType == 1}">主教材</c:when>
											          		<c:otherwise>复习资料</c:otherwise>
											          	</c:choose>
											          </td>
											          <td class="material-name" width="27%">${book.textbookName}</td>
											          <td width="10%">￥${book.discountPrice}</td>
											          <td width="10%">
											          	<c:choose>
											          		<c:when test="${not empty statusMap[book.textbookId]}">
											          			<c:choose>
											          				<c:when test="${statusMap[book.textbookId] == 0}">
											          					<span class="gray9">未就绪</span>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 1}">
											          					<span class="text-orange">待配送</span>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 2}">
											          					<span class="text-light-blue">配送中</span>
											          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
											          				</c:when>
											          				<c:when test="${statusMap[book.textbookId] == 3}">
											          					<span class="text-green">已签收</span>
											          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
											          				</c:when>
											          				<c:otherwise>--</c:otherwise>
											          			</c:choose>
											          		</c:when>
											          		<c:otherwise>
											          			<span class="gray9">未就绪</span>
											          		</c:otherwise>
											          	</c:choose>
											          </td>
											        </tr>
						      					</c:otherwise>
						      				</c:choose>
						      			</c:forEach>
					      			</c:when>
					      			<c:otherwise>
					      				<c:choose>
					      					<c:when test="${status.first}">
						      					<tr>
						      					  <td width="6%"><input type="checkbox" class="chk-item" value=""></td>
										          <td width="10%" rowspan="${rowspan}" class="text-center">第${gradeSpecialtyPlan.kkxq}学期</td>
										          <td width="27%">${gradeSpecialtyPlan.gjtCourse.kcmc}</td>
										          <td width="10%">--</td>
										          <td width="27%">--</td>
										          <td width="10%">--</td>
										          <td width="10%">--</td>
										        </tr>
					      					</c:when>
					      					<c:otherwise>
						      					<tr>
						      					  <td width="6%"><input type="checkbox" class="chk-item" value=""></td>
										          <td width="27%">${gradeSpecialtyPlan.gjtCourse.kcmc}</td>
										          <td width="10%">--</td>
										          <td width="27%">--</td>
										          <td width="10%">--</td>
										          <td width="10%">--</td>
										        </tr>
					      					</c:otherwise>
					      				</c:choose>
					      			</c:otherwise>
					      		</c:choose>
					  		</c:forEach>
					  	</c:forEach>
					  </tbody>
					</table>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<div class="text-right pop-btn-box pad">
	<span class="pull-left pad-t5">
		已选择 <b class="text-light-blue select-total">0</b> 门教材
	</span>
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>

<script type="text/javascript">

$(function(){
	//确定选择
	$("[data-role='save-data']").click(function(event) {
	  event.preventDefault();
	  if(self!==parent){
		  var $container=parent.$(".select-container-ul");
		  $container.html("");
		  var html=[
		    '<li class="select2-selection__choice">',
		      '<span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>',
		      '<span title="#name#" data-toggle="tooltip" data-container="body">#name#</span>',
		      '<input name="textbookIds" type="hidden" value="#textbookId#" />',
		    '</li>'
		  ];
		  $(".chk-item:checked").each(function(index, el) {
			var value = $(this).val();
			if (value != "") {
				var tmp=html.join("");
			    var name=$.trim($(this).closest("tr").find(".material-name").text());
			    tmp=tmp.replace(/#name#/g,name);
			    tmp=tmp.replace(/#textbookId#/g,value);
			    $container.append(tmp);
			}
		  });
		}

	 	parent.$.closeDialog(frameElement.api);
	});

	//全部选择
	$("[data-role='select-all']").change(function(event) {
	  var $ckCollector=$(".chk-item");
	  var $txt=$(".select-total");
	  if($(this).prop('checked')){
	    $ckCollector.prop('checked', true);
	    $txt.text($ckCollector.length);
	  }
	  else{
	    $ckCollector.prop('checked', false);
	    $txt.text(0);
	  }
	});

	$(".chk-item").click(function(event) {
	  event.stopPropagation();
	  var $txt=$(".select-total");
	  if($(this).prop('checked')){
	    $txt.text(parseInt($txt.text())+1);
	  }
	  else{
	    $txt.text(parseInt($txt.text())-1);
	  }
	});

	$(".table-bordered tbody tr").click(function(event) {
	  var $chk=$(this).find(".chk-item");
	  var $txt=$(".select-total");
	  if($chk.prop('checked')){
	    $chk.prop('checked',false);
	    $txt.text(parseInt($txt.text())-1);
	  }
	  else{
	    $chk.prop('checked',true);
	    $txt.text(parseInt($txt.text())+1);
	  }
	});

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

	$(".cn-container").slimScroll({
    	height:$(window).height()-106
    });

})
</script>
</body>
</html>
