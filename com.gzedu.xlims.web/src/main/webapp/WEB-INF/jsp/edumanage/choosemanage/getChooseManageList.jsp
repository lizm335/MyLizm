<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统-学籍信息</title>
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
    <script type="text/javascript">
    </script>
</head>
<body class="inner-page-body">
<section class="content-header clearfix">
    <ol class="breadcrumb oh">
        <li><a href="#"><i class="fa fa-home"></i>首页</a></li>
        <li><a href="#">教学管理</a></li>
        <li class="active">选课信息</li>
    </ol>
</section>

<section class="content" data-id="0">
    <form class="form-horizontal" action="getChooseManageList" id="listForm">
    	<input type="hidden" name="XXW_SYNC_STATUS" id="xxw_sync_status" value="${param.XXW_SYNC_STATUS }">
        <div class="box">
            <div class="box-body">
                <div class="row pad-t15">
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学号</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="XH" value="${param.XH }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">姓名</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="XM" value="${param.XM }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">身份证</label>
                            <div class="col-sm-9">
                                <select name="SFZH" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${studyCenterMap}" var="map">
                                        <option value="${map.key}"  <c:if test="${map.key==param.SFZH}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">专业层次</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="PYCC" id="pycc" data-size="5" data-live-search="true">
				                  	<option value="" selected="selected">请选择</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param.PYCC}">selected='selected'</c:if>>
											${map.value}
										</option>
									</c:forEach>
				                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">开课学期</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="OPEN_GRADE_ID" id="open_grade_id" data-size="5" data-live-search="true">
									<option value="all" selected="">全部学期</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}" <c:if test="${CURRENT_GRADE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
									</c:forEach>
								</select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">报读专业</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="SPECIALTY_ID" id="specialty_id" data-size="5" data-live-search="true">
				                	<option value="" selected="selected">请选择</option>
				                	<c:forEach items="${specialtyMap}" var="map">
				                  		<option value="${map.key}" <c:if test="${param.SPECIALTY_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
				                  	</c:forEach>
				                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="more-search" class="row collapse">
                    
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学习中心</label>
                            <div class="col-sm-9">
                                <select name="XXZX_ID" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${studyCenterMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.XXZX_ID}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">课程班级</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="BJMC" value="${param.BJMC }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">辅导老师</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="TEACH_NAME" value="${param.TEACH_NAME }">
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">入学学期</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="GRADE_ID" id="grade_id" data-size="5" data-live-search="true">
									<option value="" selected="">全部学期</option>
									<c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}" <c:if test="${param.GRADE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
									</c:forEach>
								</select>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">选课课程</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="KCMC" value="${param.KCMC }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">选课方式</label>
                            <div class="col-sm-9">
                            	<select name="REBUILD_STATE"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <option value="1">新增</option>
                                    <option value="2">重修</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6" style="display: none;">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">性别</label>
                            <div class="col-sm-9">
                                <dic:selectBox typeCode="Sex" name="XBM" code="${param.XBM}" otherAttrs='class="selectpicker show-tick form-control"' />
                            </div>
                        </div>
                    </div>
                </div>

            </div><!-- /.box-body -->
            <div class="box-footer text-right">
                <button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
                <button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
                <div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">高级搜索<i class="fa fa-fw fa-angle-up"></i> </div>
            </div><!-- /.box-footer-->
        </div>

        <div class="box margin-bottom-none">
            <div class="box-header with-border">
                <h3 class="box-title pad-t5">信息列表</h3>
                <div class="pull-right no-margin">
	                <a href="javascript:;" class="btn btn-default btn-sm margin_l10" data-role="export">
	                	<i class="fa fa-fw fa-download"></i>批量导出选课信息
	                </a>
	                <a href="javascript:;" class="btn btn-default btn-sm margin_l10" data-role="syncRec">
	                	<i class="fa fa-exchange"></i>同步选课
	                </a>
	                <a href="/edumanage/choosemanage/getAddChooseInfo" class="btn btn-default btn-sm margin_l10" data-role="addchoose">
	                	<i class="fa fa-fw fa-plus"></i>新增选课
	                </a>
                </div>
            </div>
            <div class="box-body">
                <div class="filter-tabs clearfix">
                	<input type="hidden" name="ALL_COUNT" id="all_count" value="0">
                    <ul class="list-unstyled">
                        <li class="<c:if test="${empty param.XXW_SYNC_STATUS}">actived</c:if>" onclick="choiceXJ('')">全部(<span id="chooseCountAll">0</span>)</li>
                        <li class="<c:if test="${param.XXW_SYNC_STATUS eq '0'}">actived</c:if>" onclick="choiceXJ('0')">未同步(<span id="choose0Count">0</span>)</li>
                        <li class="<c:if test="${param.XXW_SYNC_STATUS eq '1'}">actived</c:if>" onclick="choiceXJ('1')">已同步(<span id="choose1Count">0</span>)</li>
                        <li class="<c:if test="${param.XXW_SYNC_STATUS eq '2'}">actived</c:if>" onclick="choiceXJ('2')">同步失败(<span id="choose2Count">0</span>)</li>
                    </ul>
                </div>
                <div class="table-responsive" style="overflow: hidden">
                    <table class="table table-bordered table-striped vertical-mid table-font">
                        <thead>
                        <tr>
                            <th width="70" class="text-center">照片</th>
                            <th width="160" class="text-center">学生信息</th>
                            <th class="text-center">报读信息</th>
                            <th class="text-center">选课信息</th>
                            <th width="100" class="text-center">选课类型</th>
                            <th width="150" class="text-center">课程班级</th>
                            <th width="100" class="text-center">辅导老师</th>
                            <th width="100" class="text-center">状态</th>
                            <th width="100" class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${pageInfo.content }" var="entity">
                        	<tr>
                            <td class="text-center">
								<c:if test="${not empty entity.ZP }">
									<img src="${entity.ZP }" class="img-circle" width="50" height="50" onerror="this.src='${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png'">
								</c:if>
								<c:if test="${empty entity.ZP }">
									<img src="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" width="50" height="50">
								</c:if>
							</td>
							<td>
								<div class="text-left">
									姓名：${entity.XM } <br>
									学号：${entity.XH } <br>
									手机：${entity.SJH }
									<!-- 身份证号：${entity.SFZH } -->
								</div>
							</td>
							<td width="220px">
								<div class="text-left">
									专业层次：${entity.PYCC_NAME }<br>
									入学学期：${entity.GRADE_NAME }<br>
									报读专业：${entity.ZYMC }
								</div>
							</td>
							<td width="220px">
								<div class="text-left">
									开课学期：${entity.OPEN_GRADE_NAME }<br>
									课程代码：${entity.KCH }<br>
									课程名称：${entity.KCMC }<br>
								</div>
							</td>
							<td class="text-center">
								新增
							</td>
							<td class="text-center">
								${entity.BJMC }
							</td>
							<td class="text-center">
								${entity.TEACH_NAME }
							</td>
							<td class="text-center">
								<span class="text-green">已选课</span><br>
								<c:choose>
									<c:when test="${entity.XXW_SYNC_STATUS eq '1' }">
										<span class="text-green">(同步成功)</span>
									</c:when>
									<c:when test="${entity.XXW_SYNC_STATUS eq '2' }">
										<span class="text-orange" title="${entity.XXW_SYNC_MSG }">(同步失败)</span>
									</c:when>
									<c:otherwise>
										<span class="gray9">(未同步)</span>
									</c:otherwise>
								</c:choose>
							</td>
							<td class="text-center">
								<c:choose>
									<c:when test="${entity.SYNC_STATUS eq '0' }">
										<a href="javascript:void(0);" class="operion-item operion-del" data-role="del-one" data-val="${entity.REC_ID }" title="删除" data-temptitle="删除">
											<i class="fa fa-fw fa-trash-o"></i>
										</a>
									</c:when>
									<c:otherwise>
										--
									</c:otherwise>
								</c:choose>
							</td>
							</tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <tags:pagination page="${pageInfo}" paginationSize="5" />
                </div>
            </div>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
    //高级搜索
    $("#more-search").on('shown.bs.collapse', function(event) {
        event.preventDefault();
        $('[data-target="#more-search"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
    }).on('hidden.bs.collapse', function(event) {
        event.preventDefault();
        $('[data-target="#more-search"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
    });
    
 	// 导出
    $('[data-role="export"]').click(function(event) {
        event.preventDefault();
        var data = $("#listForm").serialize();
        var self=this;
        $.mydialog({
            id:'expRecResult',
            width:600,
            height:415,
            zIndex:11000,
            content: 'url:${ctx}/edumanage/choosemanage/expRecResult?'+data
        });
    });
 	
 	// 同步
    $('[data-role="syncRec"]').click(function(event) {
        event.preventDefault();
        var data = $("#listForm").serialize();
        var self=this;
        $.mydialog({
            id:'expRecResult',
            width:650,
            height:455,
            zIndex:11000,
            content: 'url:${ctx}/edumanage/choosemanage/getSyncRecList?'+data
        });
    });
    
  	// 删除选课
    $("[data-role='del-one']").click(function(event) {
    	 event.preventDefault();
		 var $this = $(this);
		 var id=$(this).attr('data-val');
         $.confirm({
             title: '提示',
             content: '是否删除该选课？',
             confirmButton: '确认',
             icon: 'fa fa-warning',
             cancelButton: '取消',  
             confirmButtonClass: 'btn-primary',
             closeIcon: true,
             closeIconClass: 'fa fa-close',
             confirm: function () { 
             	 $.get("/edumanage/choosemanage/delRecResult?REC_ID="+id,function(data){
             		$(".search").click();
             	},"json"); 
             } 
         });
     });
  	
  	 function choiceXJ(xxw_sync_status) {
  		 $("#xxw_sync_status").val(xxw_sync_status);
  		 $("#listForm").submit();
  	 }
  	 
  	 function getChooseManageCount() {
  		$.ajax({
            type: "POST",
            dataType: "json",
            url: "/edumanage/choosemanage/getChooseManageCount",
            data: $('#listForm').serialize(),
            success: function (result) {
            	$("#all_count").val(result.chooseCountAll);
            	$("#chooseCountAll").html(result.chooseCountAll);
            	$("#choose0Count").html(result.choose0Count);
            	$("#choose1Count").html(result.choose1Count);
            	$("#choose2Count").html(result.choose2Count);
                console.log(result);
            },
            error : function() {
            }
        });
  	 }
  	getChooseManageCount();
</script>
</body>
</html>
