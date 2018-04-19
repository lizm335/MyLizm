<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 发票管理</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <h1>
        发票管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">发票管理</li>
    </ol>
</section>
<section class="content">
    <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
    <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

    <form class="form-horizontal" id="listForm">
        <div class="box">
            <div class="box-body">
                <div class="row reset-form-horizontal pad-t15">
                	<div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">姓名</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" name="search_LIKE_xm" value="${param['search_LIKE_xm']}">
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">学号</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" name="search_LIKE_xh" value="${param['search_LIKE_xh']}">
                            </div>
                        </div>
                    </div>
                    <!-- 
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">学员账号</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" name="search_EQ_gjtUserAccount.loginAccount" value="${param['search_EQ_gjtUserAccount.loginAccount']}">
                            </div>
                        </div>
                    </div>
                     -->
                    <!--  
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">学员姓名</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" name="search_LIKE_xm" value="${param['search_LIKE_xm']}">
                            </div>
                        </div>
                    </div>
                    -->
                    <!-- 
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">报读产品</label>
                            <div class="col-sm-9">
                                <select name="search_EQ_gjtSpecialty.specialtyId" class="form-control selectpicker show-tick" data-size="10" data-live-search="true">
                                    <option value="">- 请选择 -</option>
                                    <c:forEach var="item" items="${specialtyMap}">
                                        <option value="${item.key}" <c:if test="${param['search_EQ_gjtSpecialty.specialtyId']==item.key}">selected</c:if>>${item.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                     -->
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">专业</label>
                            <div class="col-sm-9">
                                <select name="search_EQ_gjtSpecialty.specialtyId" class="form-control selectpicker show-tick" data-size="10" data-live-search="true">
                                    <option value="">全部</option>
                                    <c:forEach var="item" items="${specialtyMap}">
                                        <option value="${item.key}" <c:if test="${param['search_EQ_gjtSpecialty.specialtyId']==item.key}">selected</c:if>>${item.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">年级</label>
                            <div class="col-sm-9">
                                <select name="search_EQ_nj" class="form-control selectpicker show-tick" data-size="10" data-live-search="true">
                                    <option value="">全部</option>
                                    <c:forEach items="${gradeMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">层次</label>
                            <div class="col-sm-9">
                                <select name="search_EQ_pycc" class="form-control selectpicker show-tick" data-size="10" data-live-search="true">
                                    <option value="">全部</option>
                                    <c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="box-footer">
                <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
                <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
            </div>
        </div>
    </form>

    <div class="box margin-bottom-none">
        <div class="box-header with-border">
            <div class="fr">
                <a href="javascript:void(0);" class="btn btn-success" data-toggle="modal" data-target="#importModal"><i class="fa fa-fw fa-sign-in"></i> 导入发票信息</a>
                <a href="exportInfo?sortType=${sortType}&${searchParams}" target="_blank" class="btn btn-success margin_l10"><i class="fa fa-fw fa-sign-out"></i> 导出发票信息</a>
            </div>
            <h3 class="box-title pad-t5">发票列表</h3>
        </div>
        <div class="box-body">
        	<!--  
        	<div class="filter-tabs clearfix">
		        <ul class="list-unstyled">
		          <li <c:if test="${empty param['search_EQ_issueStatus']}">class="actived"</c:if>>全部(0)</li>
		          <li <c:if test="${param['search_EQ_issueStatus'] == 0}">class="actived"</c:if>>待发放(0)</li>
		          <li <c:if test="${param['search_EQ_issueStatus'] == 1}">class="actived"</c:if>>已发放(0)</li>
		        </ul>
	      	</div>
	      	-->
        	<div class="table-responsive">
            <table id="dtable" class="table table-bordered table-striped table-container">
                <thead>
                <tr>
                    <th><input type="checkbox" class="select-all"></th>
                    <!-- 
                    <th>学员姓名</th>
                    <th>年级</th>
                    -->
                    <th>个人信息</th>
                    <th>报读信息</th>
                    <!-- 
                    <th>报读产品</th>
                     -->
                    <th>缴费方式</th>
                    <th>发放次数/申请次数</th>
                    <th>发票状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty infos && infos.numberOfElements > 0}">
                        <c:forEach items="${infos.content}" var="info">
                            <c:if test="${not empty info}">
                                <tr>
                                    <td><input type="checkbox" name="ids"
                                               data-id="${info.studentId}" data-name="check-id"
                                               value="${info.studentId}"></td>
                                    <td>
                                    	姓名：${info.xm}(${info.xbm=='1'?"男":"女"})<br>
                                    	学号：${info.xh} <br>
                                    	手机：${info.sjh}
                                    </td>
                                    <td>
                                    	层次：${pyccMap[info.pycc]}<br>
                                    	年级：${info.gradeName} <br>
                                    	专业：${info.zymc }
                                    </td>
                                    <!-- 
                                    <td>${info.zymc}</td>
                                     -->
                                    <td>
                                        <c:if test="${info.gkxlPaymentTpye=='A'}">全额缴费</c:if>
                                        <c:if test="${info.gkxlPaymentTpye=='B'}">首年缴费</c:if>
                                        <c:if test="${info.gkxlPaymentTpye=='C'}">分期付款</c:if>
                                    </td>
                                    <td>${info.issueNum}/${info.totalApplyNum}</td>
                                    <td <c:if test="${info.state=='A'}">class="text-red"</c:if>>
                                        <c:if test="${info.state=='A'}">待发放</c:if>
                                        <c:if test="${info.state=='B'}">已开具</c:if>
                                        <c:if test="${info.state=='C'}">已发放</c:if>
                                        <c:if test="${info.state=='D'}">已领取</c:if>
                                    </td>
                                    <td>
                                        <div class="data-operion">
                                            <a href="view/${info.studentId}?gkxlPaymentTpye=${info.gkxlPaymentTpye}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td align="center" colspan="15">暂无数据</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <tags:pagination page="${infos}" paginationSize="10" />
        </div>
        </div>
    </div>
</section>

<div id="importModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <form id="uploadForm" name="uploadForm" action="importInvoice" method="post" enctype="multipart/form-data">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title">导入发票申请</h4>
                </div>
                <div class="modal-body">
                    <input name="invoiceFile" type="file" />
                    <div class="progress">
                        <div style="width: 0%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="0" role="progressbar" class="progress-bar progress-bar-success progress-bar-striped active">
                            <span class="sr-only">0% Complete</span>
                        </div>
                    </div>
                    <p>请选择保存有学员发票申请记录的excel文件上传</p>
                    <p>第一次上传？请先下载发票申请记录模板
                        <a href="downloadTemplate" target="_blank" class="btn btn-xs btn-success">
                            <i class="fa fa-fw fa-download"></i> 点此下载</a>
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">关闭</button>
                    <button type="submit" class="btn btn-primary">上传</button>
                </div>
            </div><!-- /.modal-content -->
        </form>
    </div><!-- /.modal-dialog -->
</div>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
    $(document).ready(function () {

        $('#uploadForm :input[name="invoiceFile"]').change(function () {
            var file = $(this).val();
            var strs = file.split('.');
            var suffix = strs [strs .length - 1];

            if (suffix != 'xls' && suffix != 'xlsx' && suffix != 'xlsm' && suffix != 'xlsb') {
                alert("你选择的不是EXCEL文件,请选择EXCEL文件！");
                this.outerHTML = this.outerHTML; //这样清空，在IE8下也能执行成功
            }
        });

        /* 文件上传 参考 http://malsup.com/jquery/form/#file-upload 例1#Progress Demo 1 */
        var bar = $(".progress>div");
        var percent = $(".progress>div>span");

        function initForm() {
            var percentVal = '0%';
            bar.attr('aria-valuenow', 0);
            bar.width(percentVal);
            percent.html(percentVal+' Complete');
        }
        var options = {
            beforeSend: initForm,
            uploadProgress: function(event, position, total, percentComplete) {
                var percentVal = percentComplete + '%';
                bar.attr('aria-valuenow', percentComplete);
                bar.width(percentVal);
                percent.html(percentVal);
            },
            success: function(data) {
                var percentVal = '100%';
                bar.attr('aria-valuenow', 100);
                bar.width(percentVal);
                percent.html(percentVal);

                if(data.successful){
                    showSueccess(data);

                    $('.modal').modal('hide');
                }else{
                    $.alert({
                        title: '失败',
                        icon: 'fa fa-exclamation-circle',
                        confirmButtonClass: 'btn-primary',
                        content: '导入失败！',
                        confirmButton: '确认',
                        confirm:function(){
                            showFail(data);
                        }
                    });
                }
            },
            complete: function(xhr) {
                initForm();
                // alert(xhr.responseText);
            },
            resetForm: true,
            dataType:  'json'
        };

        $('#uploadForm').ajaxForm(options);
        
        
        $(".filter-tabs li").click(function(event) {
    		var $li = $(this);
    		$(".filter-tabs li").each(function(index, el) {
    			if (el == $li.context && index == 0) {
    				window.location.href = "list";
    			} else if (el == $li.context && index == 1) {
    				window.location.href = "list?search_EQ_issueStatus=0";
    			} else if (el == $li.context && index == 2) {
    				window.location.href = "list?search_EQ_issueStatus=1";
    			}
    		});
    	});

    });
</script>
</body>
</html>
