<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html  class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>学分查询</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="javascript:">学习管理</a></li>
        <li class="active">成绩查询</li>
    </ol>
</section>
<section class="content">
    <div class="nav-tabs-custom no-margin">
        <ul class="nav nav-tabs nav-tabs-lg">
            <li><a href="${ctx}/home/student/scorespoint/list" >成绩查询</a></li>
            <li class="active"><a href="${ctx}/home/student/scorespoint/creditList" >学分查询</a></li>
        </ul>

        <form id="listForm" class="form-horizontal">
            <div class="tab-content" id="scoreContent">
                <div class="tab-pane active" id="tab_top_2">
                    <div class="box box-border">
                        <div class="box-body">
                            <div class="row pad-t15">
                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">姓名</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="XM" value="${param.XM }">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">学号</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="XH" value="${param.XH }">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">身份证</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" name="SFZH" value="${param.SFZH }">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">入学学期</label>
                                        <div class="col-sm-9">
                                            <select class="selectpicker show-tick form-control" name="NJ" id="nj" data-size="5" data-live-search="true">
                                                <option value="">请选择</option>
                                                <c:forEach items="${gradeMap}" var="map">
                                                    <option value="${map.key}" <c:if test="${param.NJ eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">专业</label>
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

                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">层次</label>
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
                            </div>
                        </div><!-- /.box-body -->
                        <div class="box-footer text-right">
                            <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
                            <button type="button" class="btn min-width-90px btn-default">重置</button>
                        </div><!-- /.box-footer-->
                    </div>
                    <div class="box box-border margin-bottom-none">
                        <div class="box-body">
                            <div class="filter-tabs clearfix">
                                <input type="hidden" name="SCORE_TYPE" id="score_type" value="${param.SCORE_TYPE }">
                                <ul class="list-unstyled">
                                    <li class="js-scoreType"  data-value="">全部</li>
                                    <li class="js-scoreType"  data-value="1">不满足</li>
                                    <li class="js-scoreType"  data-value="2">满足</li>
                                </ul>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped table-cell-ver-mid text-center f14">
                                    <thead>
                                    <tr>
                                        <th>头像</th>
                                        <th>个人信息</th>
                                        <th>报读信息</th>
                                        <th>总学分</th>
                                        <th>最低毕业学分</th>
                                        <th>已获得学分</th>
                                        <th>已通过课程</th>
                                        <th>是否满足毕业学分</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty pageInfo.content}">
                                                <c:forEach items="${pageInfo.content}" var="entity">
                                                    <tr>
                                                        <td class="text-center">
                                                            <c:if test="${not empty entity.ZP }">
                                                                <img src="${entity.ZP }" class="img-circle" width="50" height="50">
                                                            </c:if>
                                                            <c:if test="${empty entity.ZP }">
                                                                <img src="${ctx}/static/images/headImg04.png" class="img-circle" width="50" height="50">
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <div class="text-left">
                                                                姓名：${entity.XM } <br>
                                                                学号：${entity.XH } <br>
                                                                手机：${entity.SJH }
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="text-left">
                                                                专业层次：${entity.PYCC_NAME } <br>
                                                                入学年级：${entity.YEAR_NAME } <br>
                                                                入学学期：${entity.GRADE_NAME } <br>
                                                                报读专业：${entity.ZYMC }
                                                            </div>
                                                        </td>
                                                        <td>${entity.ZXF }</td>
                                                        <td>${entity.ZDBYXF }</td>
                                                        <td>${entity.SUM_CREDITS }</td>
                                                        <td>${entity.PASS_COURSE_COUNT }/${entity.COUNT_COURSE }</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${entity.SUM_CREDITS>0 && entity.SUM_CREDITS>=entity.ZDBYXF }">
                                                                    <span class="text-green">满足</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="text-orange">不满足</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <a href="${ctx}/home/student/scorespoint/scoresPointDetail?id=${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr><td colspan="9">暂无数据！</td></tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                                <tags:pagination page="${pageInfo}" paginationSize="5" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
    $(function() {
        $("#nj").selectpicker();
        $("#specialty_id").selectpicker();
        $("#pycc").selectpicker();
    });
    //Initialize Select2 Elements
    $(".select2").select2();
    // filter tabs
    $(".filter-tabs li").click(function(event) {
        if($(this).hasClass('actived')){
            $(this).removeClass('actived');
        }
        else{
            $(this).addClass('actived');
        }
    });

    //高级搜索
    $("[id^='more-search']").on('shown.bs.collapse', function(event) {
        event.preventDefault();
        var index=$("[id^='more-search']").index(this);
        $('[data-target="#more-search-'+index+'"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
    }).on('hidden.bs.collapse', function(event) {
        event.preventDefault();
        var index=$("[id^='more-search']").index(this);
        $('[data-target="#more-search-'+index+'"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
    });


    
    $(function(){
    	//init
    	var currentScoreType = "${param.SCORE_TYPE}";
    	
    	var $scoreTypes = $(".js-scoreType");
    	$scoreTypes.on("click", function(){
    		 var $this = $(this);
    		 var type = $this.data("value");
    		 $("#score_type").val(type);
    	     $("#listForm").submit();
    	});
    	
    	var $listForm = $("#listForm");
    	$.getJSON("creditListCounts", $listForm.serialize()).done(function(data){
    		$scoreTypes.each(function(index, dom){
    			var $this = $(dom);
    			var type = $this.data("value");
    			var key = "SCORE_COUNT" + type;
    			$this.append("("+data[key]+")");
    			
    			//选中当前标签页
    			if(type == currentScoreType){
    				$this.addClass("actived");
    			}
    		});
    	});
    	
    });
    
    
</script>
</body>
</html>
