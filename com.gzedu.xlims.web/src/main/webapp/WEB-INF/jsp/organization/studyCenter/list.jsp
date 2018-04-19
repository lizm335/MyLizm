<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理员管理系统-列表查询</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">机构管理</a></li>
			<li class="active">学习中心</li>
		</ol>
	</section>
	<section class="content" data-id="0">
		<form class="form-horizontal" id="listForm" action="list.html">			
		<div class="box">
		  <div class="box-body">
		      <div class="row pad-t15">
		        <div class="col-sm-4 col-xs-6">
		          <div class="form-group">
		            <label class="control-label col-sm-3 text-nowrap">机构代码：</label>
		            <div class="col-sm-9">
		              <input class="form-control" type="text" name="search_LIKE_scCode" value="${param.search_LIKE_scCode}">
		            </div>
		          </div>
		        </div>
		        <div class="col-sm-4 col-xs-6">
		          <div class="form-group">
		            <label class="control-label col-sm-3 text-nowrap">机构名称：</label>
		            <div class="col-sm-9">
		              <input class="form-control" type="text" name="search_LIKE_scName" value="${param.search_LIKE_scName}">
		              <input class="form-control" type="hidden" id="isEnabled" name="search_EQ_isEnabled" value="${param.search_EQ_isEnabled}">
		              <input class="form-control" type="hidden" id="auditStatus" name="search_EQ_auditStatus" value="${param.search_EQ_auditStatus}">
		              </select>
		            </div>
		          </div>
		        </div>
		      </div>
		  </div><!-- /.box-body -->
		  <div class="box-footer">
		    <div class="pull-right"><button type="button" id="reset_data" class="btn min-width-90px btn-default btn-reset">重置</button></div>
		    <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary" id="clickSubmit">搜索</button></div>
		  </div><!-- /.box-footer-->
		</div>
	
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
				
		<div class="box margin-bottom-none">
				<div class="box-header with-border">
		  		<h3 class="box-title pad-t5">学习中心列表</h3>
				<shiro:hasPermission name="/organization/studyCenter/list$create">
		  			<div class="pull-right no-margin">
		    			<a role="button" href="toCreate" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新增学习中心</a>
		  			</div>
				</shiro:hasPermission>
			</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li onclick="cutTab('all','all')" <c:if test="${empty param.search_EQ_isEnabled && empty param.search_EQ_auditStatus}">class="actived"</c:if>>
						全部(${allcount })
					</li>
					<li onclick="cutTab('isEnabled','1')" value="1" <c:if test="${param.search_EQ_isEnabled == '1' }">class="actived"</c:if>>已启用(${yesIsEnabled })</li>
					<li onclick="cutTab('isEnabled','0')" value="0" <c:if test="${param.search_EQ_isEnabled == '0' }">class="actived"</c:if>>未启用(${noIsEnabled })</li>
					<li onclick="cutTab('auditStatus','0')" value="0" <c:if test="${param.search_EQ_auditStatus =='0' }">class="actived"</c:if>>待审核(${waitAuditStatus })</li>
					<li onclick="cutTab('auditStatus','2')" value="2" <c:if test="${param.search_EQ_auditStatus == '2' }">class="actived"</c:if>>审核不通过(${noAuditStatus })</li>
				</ul>
			</div>	
			 <div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
				<table id="dtable" class="table table-bordered table-striped table-cell-ver-mid text-center table-font">
					<thead>
						<tr>
							<th>学习中心代码</th>
							<th>学习中心名称</th>
							<th>类型</th>
<!-- 							<th>服务范围</th> -->
							<th>区域</th>
							<th>联系电话</th>
							<th>联系人</th>
							<th>所属院校</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${not empty pageInfo.content}">
						<c:forEach items="${pageInfo.content}" var="item">
							<tr>
								<td>${item.SC_CODE}</td>
								<td>${item.NAME}</td>
								<td>
									<c:if test="${item.ORG_TYPE eq '3'}">学习中心</c:if> 
									<c:if test="${item.ORG_TYPE eq '6'}">招生点</c:if> 
								</td>
<%-- 								<td><dic:getLabel typeCode="ServiceArea" code="${item.SERVICE_AREA}"/></td> --%>
								<td>${item.DISTRICTNAME}</td>
								<td>${item.LINK_TEL}</td>
								<td>${item.LINKMAN}</td>
								<td>${item.SCHOOLNAME }</td>
								<td>
									<%-- <c:if test="${item.IS_ENABLED eq '1'}">已启用</c:if>
									<c:if test="${item.IS_ENABLED eq '0'}"><span class="text-red">已停用</span></c:if> --%>
									<c:choose>
										<c:when test="${item.AUDIT_STATUS eq '2'}">
											<span class="text-red">审核不通过 /
											<c:if test="${item.IS_ENABLED eq '1'}">启用</c:if>
											<c:if test="${item.IS_ENABLED eq '0'}"><span class="text-red">停用</span></c:if> 
											</span>
										</c:when>
										<c:when test="${item.AUDIT_STATUS eq '0'}">
											待审核 /
											<c:if test="${item.IS_ENABLED eq '1'}">启用</c:if>
											<c:if test="${item.IS_ENABLED eq '0'}"><span class="text-red">停用</span></c:if> 
										</c:when>
										<c:otherwise>
											<span class="text-green">审核通过 /
											<c:if test="${item.IS_ENABLED eq '1'}">启用</c:if>
											<c:if test="${item.IS_ENABLED eq '0'}"><span class="text-red">停用</span></c:if> 
											</span>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<div class="data-operion">
										<shiro:hasPermission name="/organization/studyCenter/list$edit">
											<a href="update/${item.ID}" class="operion-item operion-view" data-toggle="tooltip" title="编辑">
												<i class="fa fa-fw fa-edit"></i>
											</a>
										</shiro:hasPermission>
										 <shiro:hasPermission name="/organization/studyCenter/list$stop">
											<c:if test="${item.IS_ENABLED eq '1'}">
												<a href="javascript:;" onclick="changeStatus('${ctx}/organization/studyCenter/changeStatus/${item.ID}/${item.IS_ENABLED}',$(this),'${item.IS_ENABLED}')" 
													class="operion-item operion-view" data-toggle="tooltip" title="停用">
													<i class="fa fa-fw fa-pause"></i>
												</a>
											</c:if>
											<c:if test="${item.IS_ENABLED eq '0'  and item.AUDIT_STATUS eq '1'}">
												<a href="javascript:;" onclick="changeStatus('${ctx}/organization/studyCenter/changeStatus/${item.ID}/${item.IS_ENABLED}',$(this),'${item.IS_ENABLED}')"
													 class="operion-item operion-view" data-toggle="tooltip" title="启用">
													<i class="fa fa-fw fa-play"></i>
												</a>
											</c:if>
										</shiro:hasPermission> 
										<shiro:hasPermission name="/organization/studyCenter/list$view">
											<a href="view/${item.ID}?schoolName=${item.SCHOOLNAME }" class="operion-item operion-view" data-toggle="tooltip" title="查看详情">
												<i class="fa fa-fw fa-view-more"></i>
											</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="/organization/studyCenter/list$delete">
											<a href="${ctx}/organization/studyCenter/deleteCenter/${item.ID}" class="operion-item operion-del" 
												data-toggle="tooltip" val="${item.ID}" title="删除" data-role="sure-btn-1">
												<i class="fa fa-fw fa-trash-o text-red"></i>
											</a>
										</shiro:hasPermission>
									</div>
								</td>
							</tr>
						</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td align="center" colspan="9">暂无数据</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			   </table>
			  <tags:pagination page="${pageInfo}" paginationSize="5" />
			  </div>
			 </div>
		</div>
	</form>
  </section>	
  <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>

</html>
<script type="text/javascript">
	var url = "";
    $("body").confirmation({
        selector: "[data-role='sure-btn-1']",
        html:true,
        placement:'top',
        content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定要删除该学习中心？</div>',
        title:false,
        btnOkClass    : 'btn btn-xs btn-primary',
        btnOkLabel    : '确认',
        btnOkIcon     : '',
        btnCancelClass  : 'btn btn-xs btn-default margin_l10',
        btnCancelLabel  : '取消',
        btnCancelIcon   : '',
        popContentWidth:190,
        onShow:function(event,element){
            url =$(event.target).attr("href");
        },
        onConfirm:function(event,element){
            $.ajax({
                type: "POST",
                dataType: "json",
                url: url,
                success: function (data) {
                    alert(data.message);
                    if(data.successful==true){
                        $("#clickSubmit").click();
                    }
                },
                complete: function (request,status) {

                }
            });
        },
        onCancel:function(event, element){

        }
    });

	function changeStatus(url,obj,status) {
		$.ajax({
		   	type:"post",
		   	dataType:"json",
			url:url,
			success:function (data) {
		   	    if(data.successful){
/*		   	        if(status=='1'){//启用切换到停用
                        obj.html('<i class="fa fa-fw fa-play"></i>');
                        obj.attr('title','启用');
					}else{
                        obj.html('<i class="fa fa-fw fa-pause"></i>');
                        obj.attr('title','停用')
					}*/
					$("#clickSubmit").click();
				}
            }
		});
    }

    $("#reset_data").off().on('click',function () {
        $(this).closest("form").find("input,select,checkbox,radio").each(function (i) {
            $(this).val("");
        });
    });
    
    
    function cutTab(flg, vals) {
    	$("#isEnabled").val("");
    	$("#auditStatus").val("");
    	if (flg=="isEnabled") {
    		$("#isEnabled").val(vals);
    	} else if (flg=="auditStatus") {
    		$("#auditStatus").val(vals);
    	}
    }

</script>