<%@page import="com.gzedu.xlims.pojo.status.EmployeeTypeEnum"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">教务管理</a></li>
        <li class="active">教职人员</li>
    </ol>
</section>
<section class="content">
    <div class="nav-tabs-custom no-margin">
    	<c:set var="teachers" value="<% EmployeeTypeEnum.values() %>"></c:set>
        <ul class="nav nav-tabs nav-tabs-lg">
        <shiro:hasPermission name="/edumanage/teachEmployee/list$headTeacher">
       		 <li <c:if test="${action=='headTeacher'}">class="active"</c:if>><a href="${ctx }/edumanage/teachEmployee/list.html?action=headTeacher">班主任</a></li>
        </shiro:hasPermission> 
        <shiro:hasPermission name="/edumanage/teachEmployee/list$coachTeacher">
           <li <c:if test="${action=='coachTeacher'}">class="active"</c:if>><a href="${ctx }/edumanage/teachEmployee/list.html?action=coachTeacher">辅导教师</a></li>
	     </shiro:hasPermission>
	      <shiro:hasPermission name="/edumanage/teachEmployee/list$supervisorTeacher">
	      	 <li <c:if test="${action=='supervisorTeacher'}">class="active"</c:if>><a href="${ctx }/edumanage/teachEmployee/list.html?action=supervisorTeacher">督导教师</a></li>
	      </shiro:hasPermission>
	      
	        <%-- <shiro:hasPermission name="/edumanage/teachEmployee/list$guideTeacher">
	            <li <c:if test="${action=='guideTeacher'}">class="active"</c:if>reply><a href="${ctx }/edumanage/teachEmployee/list.html?action=guideTeacher">论文指导教师</a></li>
	        </shiro:hasPermission>
	   		<shiro:hasPermission name="/edumanage/teachEmployee/list$replyTeacher">       
	            <li <c:if test="${action=='replyTeacher'}">class="active"</c:if>><a href="${ctx }/edumanage/teachEmployee/list.html?action=replyTeacher">论文答辩教师</a></li>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="/edumanage/teachEmployee/list$practiceTeacher">       
	            <li <c:if test="${action=='practiceTeacher'}">class="active"</c:if>><a href="${ctx }/edumanage/teachEmployee/list.html?action=practiceTeacher">社会实践教师</a></li>
	      	</shiro:hasPermission> --%>
	        <shiro:hasPermission name="/edumanage/teachEmployee/list$paperTeacher">
            	<li <c:if test="${action=='paperTeacher'}">class="active"</c:if>><a href="${ctx }/edumanage/teachEmployee/list.html?action=paperTeacher">论文教师</a></li>
	      	</shiro:hasPermission>
            <shiro:hasPermission name="/edumanage/teachEmployee/list$classTeacher">       
	            <li <c:if test="${action=='classTeacher'}">class="active"</c:if>><a href="${ctx }/edumanage/teachEmployee/list.html?action=classTeacher">任课教师</a></li>
	      	</shiro:hasPermission>
            
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_top_1">
                <form id="listForm" class="form-horizontal" action="${ctx }/edumanage/teachEmployee/list.html" method="post">
                    <input type="hidden" name="action" value="${action}">
                    <div class="box box-border">
                        <div class="box-body">
                                <div class="row pad-t15">
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">账号</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" value="${param.search_LIKE_zgh}" name="search_LIKE_zgh">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">姓名</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm}">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">手机号码</label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" name="search_LIKE_sjh" value="${param.search_LIKE_sjh}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                             <!-- 院校模式不适用 -->   
					        <shiro:lacksPermission name="/edumanage/teachEmployee/list$schoolModel">
                               <div class="row pad-t15">
                               		<div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">学习中心</label>
                                            <div class="col-sm-9">
                                                <select name="orgId" class="form-control selectpicker show-tick" data-size="8"
														data-live-search="true">
                                                	<option value="">请选择</option>
                                                	<c:forEach items="${orgMap }" var="map">
                                                		<option value="${map.key }"<c:if test="${param.orgId eq map.key}">selected</c:if>>${map.value }</option>	
                                                	</c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                               	</div>
                        </shiro:lacksPermission>  	
                        </div><!-- /.box-body -->
                        <div class="box-footer">
                            <div class="pull-right"><button type="button" class="btn min-width-90px btn-default btn-reset">重置</button></div>
                            <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
                        </div><!-- /.box-footer-->
                    </div>
                    
                    <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>>
						<button data-dismiss="alert" class="close">×</button>${feedback.message}
					</div>
					<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>>
						<button data-dismiss="alert" class="close">×</button>${feedback.message}
					</div>
                    
                    <div class="box box-border margin-bottom-none">
                        <div class="box-header with-border">
                           <h3 class="box-title pad-t5">${teacher}列表</h3>
                           <div class="pull-right">
                           		<shiro:hasPermission name="/edumanage/teachEmployee/list$export">
                           			<c:if test="${action eq 'headTeacher' }">
                               		<a href="${ctx}/edumanage/teachEmployee/exportTeacher"
                               	   		class="btn btn-default btn-sm margin_r10" >
                               	 	 <i class="fa fa-fw fa-sign-in"></i> 批量导出班主任信息</a>
                               	 	 </c:if>
                               	  </shiro:hasPermission>
                           		<shiro:hasPermission name="/edumanage/teachEmployee/list$import">
                               		<a href="${ctx}/edumanage/teachEmployee/importTeacher.html?type=${action}" role="button" class="btn btn-default btn-sm margin_r10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 批量导入${teacher}</a>
                               	</shiro:hasPermission>
                               	<shiro:hasPermission name="/edumanage/teachEmployee/list$create">
                               		<a href="${ctx}/edumanage/teachEmployee/addTeacher.html?type=${action}" role="button" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 新增${teacher}</a>
                               	</shiro:hasPermission>
                           </div>
                        </div>
                        <c:if test="${action ne 'headTeacher' }">
                            <div class="box-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-striped vertical-mid text-center table-font">
                                        <thead>
                                        <tr>
                                            <th width="80">头像</th>
                                            <th>账号</th>
                                            <th>姓名</th>
                                            <th>性别</th>
                                            <th>手机号码</th>
                                            <th>邮箱</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                            <c:when test="${ not empty pageInfo.getContent()}">
                                                <c:forEach items="${pageInfo.getContent()}" var="data">
                                                    <tr>
                                                        <td>
                                                            <c:if test="${not empty data.zp}">
                                                                <img src="${data.zp}" class="img-circle" width="50" height="50">
                                                            </c:if>
                                                            <c:if test="${empty data.zp}">
                                                                <img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" width="50" height="50">
                                                                <p class="gray9">未提交</p>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            ${data.zgh}
                                                        </td>
                                                        <td>
                                                            ${data.xm}
                                                        </td>
                                                        <td>
                                                            <c:if test="${data.xbm eq '1'}">男</c:if><c:if test="${data.xbm eq '2'}">女</c:if>
                                                        </td>
                                                        <td>
                                                            ${data.sjh}
                                                        </td>
                                                        <td>
                                                            ${data.dzxx}
                                                        </td>
                                                        <td>
                                                        	<div class="data-operion">
                                                        		<shiro:hasPermission name="/edumanage/teachEmployee/list$update">
																	<a href="updateTeacher?id=${data.employeeId}&type=${action}"
																		class="operion-item operion-edit" title="编辑">
																		<i class="fa fa-fw fa-edit"></i></a>
																</shiro:hasPermission>
																<shiro:hasPermission name="/edumanage/teachEmployee/list$reset">
																	<a href="#" class="operion-item operion-view" val="${data.employeeId}" data-toggle="tooltip" title="重置密码" data-role="reset-psw"><i class="fa fa-fw fa-psw-reset"></i></a>
																</shiro:hasPermission>
																<shiro:hasPermission name="/edumanage/teachEmployee/list$delete">
																	<a href="javascript:void(0);"
																		class="operion-item operion-del del-one" val="${data.employeeId}"
																		title="删除" data-tempTitle="删除">
																	<i class="fa fa-fw fa-trash-o text-red"></i></a>
																</shiro:hasPermission>
																<c:if test="${action=='coachTeacher' or  action=='supervisorTeacher' }">
																<a href="${ctx}/edumanage/courseclass/simulation?id=${data.employeeId}&termcourseId=&classId=" 
				            									target="_blank" class="operion-item" data-toggle="tooltip" title="模拟登录"><i class="fa fa-fw fa-simulated-login"></i></a>
																</c:if>
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
                        </c:if>
                        <c:if test="${action eq 'headTeacher'}">
                            <div class="box-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-striped vertical-mid text-center table-font">
                                        <thead>
                                        <tr>
                                            <th width="80">头像</th>
                                            <th>账号</th>
                                            <th>姓名</th>
                                            <th>性别</th>
                                            <th>手机号码</th>
                                            <th>邮箱</th>
                                            <shiro:lacksPermission name="/edumanage/teachEmployee/list$schoolModel">
                                            <th>学习中心</th>
                                            </shiro:lacksPermission>
                                            <th>所属院校</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                            <c:when test="${ not empty pageInfo.getContent()}">
                                                <c:forEach items="${pageInfo.getContent()}" var="data">
                                                    <tr>
                                                        <td>
                                                            <c:if test="${not empty data.zp}">
                                                                <img src="${data.zp}" class="img-circle" width="50" height="50">
                                                            </c:if>
                                                            <c:if test="${empty data.zp}">
                                                                <img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" width="50" height="50">
                                                                <p class="gray9">未提交</p>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            ${data.zgh}
                                                        </td>
                                                        <td>
                                                            ${data.xm}
                                                        </td>
                                                        <td>
                                                            <c:if test="${data.xbm eq '1'}">男</c:if><c:if test="${data.xbm eq '2'}">女</c:if>
                                                        </td>
                                                        <td>
                                                            ${data.sjh}
                                                        </td>
                                                        <td>
                                                            ${data.dzxx}
                                                        </td>
                                                         <shiro:lacksPermission name="/edumanage/teachEmployee/list$schoolModel">
                                                        <td>${data.gjtStudyCenter.scName}</td>
                                                        </shiro:lacksPermission>
                                                        <td>
                                                            ${data.gjtSchoolInfo.xxmc}
                                                        </td>
														<td>
                                                        	<div class="data-operion">
                                                        		<shiro:hasPermission name="/edumanage/teachEmployee/list$update">
																<a href="updateTeacher?id=${data.employeeId}&type=${action}"
																	class="operion-item operion-edit" title="编辑">
																	<i class="fa fa-fw fa-edit"></i></a>
																	</shiro:hasPermission>
																<shiro:hasPermission name="/edumanage/teachEmployee/list$reset">
																	<a href="#" class="operion-item operion-view" val="${data.employeeId}" data-toggle="tooltip" title="重置密码" data-role="reset-psw"><i class="fa fa-fw fa-psw-reset"></i></a>
																</shiro:hasPermission>
																<shiro:hasPermission name="/edumanage/teachEmployee/list$delete">
																	<a href="javascript:void(0);"
																		class="operion-item operion-del del-one" val="${data.employeeId}"
																		title="删除" data-tempTitle="删除">
																		<i class="fa fa-fw fa-trash-o text-red"></i></a>
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
                        </c:if>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">

    $("[data-role='import']").click(function(event) {
        event.preventDefault();
        $.mydialog({
            id:'import',
            width:600,
            height:415,
            zIndex:11000,
            content: 'url:'+$(this).attr("href")
        });
    });

    // filter tabs
    $(".filter-tabs li").click(function(event) {
        if($(this).hasClass('actived')){
            $(this).removeClass('actived');
        }
        else{
            $(this).addClass('actived');
        }
    });
    
    //重置密码
    $("[data-role='reset-psw']").click(function(){
		 var $this = $(this);
		 var id=$(this).attr('val');
         $.confirm({
             title: '提示',
             content: '是否将密码重置为888888？',
             confirmButton: '确认',
             icon: 'fa fa-warning',
             cancelButton: '取消',  
             confirmButtonClass: 'btn-primary',
             closeIcon: true,
             closeIconClass: 'fa fa-close',
             confirm: function () { 
             	 $.post("resetPassword",{id:id},function(data){
             		alert(data.message);
             },"json"); 
             } 
         });
     });

</script>
</body>
</html>

