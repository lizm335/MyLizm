<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统</title>

    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp" %>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
    <ol class="breadcrumb oh">
        <li><a href="homepage.html"><i class="fa fa-home"></i>首页</a></li>
        <li class="active">授课管理</li>
    </ol>
</section>
<section class="content">
    <form id="listForm" class="form-horizontal">
        <input type="hidden" name="search_lessonType" value="${param.search_lessonType}" id="search_lessonType"/>
        <div class="nav-tabs-custom no-margin">
            <ul class="nav nav-tabs nav-tabs-lg">
                <li class="active" >
                    <a href="#">直播管理</a></li>
                <li>
                    <a href="${ctx}/edumanage/coachdata/coachDateList">辅导资料</a></li>
            </ul>

            <div class="tab-content">
                <div class="box box-border">
                    <div class="box-body">
                        <div class="row pad-t15">
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">学期</label>
                                    <div class="col-sm-9">
                                        <select class="selectpicker show-tick form-control" name="search_termId">
                                            <option value="0" selected="selected">请选择</option>
                                            <c:forEach items="${grades}" var="map">
                                                <option value="${map.key}"  <c:if test="${param['search_termId'] eq map.key}">selected='selected'</c:if>>${map.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">直播名称</label>
                                    <div class="col-sm-9">
                                        <input class="form-control" type="text" name="search_onlineName"
                                               value="${param.search_onlineName}" placeholder="请输入直播名称">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">课程</label>
                                    <div class="col-sm-9">
                                        <input class="form-control" type="text" name="search_courseName"
                                               value="${param.search_courseName}" placeholder="请输入课程代码或课程名称">
                                    </div>
                                </div>
                            </div>

                        </div><!-- /.box-body -->
                        <div class="box-footer">
                            <div class="btn-wrap">
                                <button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
                            </div>
                            <div class="btn-wrap">
                                <button type="submit" class="btn min-width-90px btn-primary" id="btn_search">搜索</button>
                            </div>
                        </div><!-- /.box-footer-->
                    </div>


                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title pad-t5">直播列表</h3>
                            <div class="fr">
                                <div class="btn-wrap fl">
                                    <a href="toLesson?onlineId" class="btn btn-block btn-default btn-add">
                                        <i class="fa fa-fw fa-plus"></i> 新建直播</a>
                                </div>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="filter-tabs filter-tabs2 clearfix">
                                <ul class="list-unstyled">
                                    <li  value="3"
                                         class="<c:if test="${empty param['search_lessonType'] || param['search_lessonType'] eq '3'}">actived</c:if> lesson_type">
                                        全部(${lessonNum})
                                    </li>
                                    <li value="1"
                                        class=" <c:if test="${param['search_lessonType'] == '1' }">actived </c:if> lesson_type">
                                        直播中(${lessoningNum})
                                    </li>
                                    <li value="0"
                                        class="<c:if test="${param['search_lessonType'] == '0' }">actived</c:if> lesson_type">
                                        待开始(${needLessonNum})
                                    </li>
                                    <li value="2"
                                        class="<c:if test="${param['search_lessonType'] == '2' }">actived</c:if> lesson_type">
                                        已结束(${lessonedNum})
                                    </li>
                                </ul>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped vertical-mid text-center table-font" id="table">
                                    <thead>
                                    <tr>
                                        <th width="25%">直播名称</th>
                                        <th>直播时间</th>
                                        <th>学期</th>
                                        <th>课程</th>
                                        <th>需辅导人数</th>
                                        <th>参与辅导人数</th>
                                        <th>任课老师</th>
                                        <th>状态</th>
                                        <th>助教</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${not empty pageInfo.content}">
                                            <c:forEach items="${pageInfo.content}" var="entity">
                                                <tr>
                                                    <td data-oid="${entity.ONLINETUTOR_ID}-1">
                                                            ${entity.ONLINE_NAME}
                                                    </td>
                                                    <td data-oid="${entity.ONLINETUTOR_ID}-2">
                                                        <fmt:formatDate value="${entity.START_DT}" type="date"
                                                                        pattern="yyyy-MM-dd HH:mm"/>
                                                        <br/>~<br/>
                                                        <fmt:formatDate value="${entity.END_DT}" type="date"
                                                                        pattern="yyyy-MM-dd HH:mm"/>
                                                    </td>
                                                    <td>
                                                            ${entity.GRADE_NAME}
                                                    </td>
                                                    <td>

                                                            ${entity.KCMC}<br/>
                                                            (<span class="gray9">${entity.KCH}</span>)
                                                    </td>
                                                    <td>

                                                            ${entity.ALL_STU}
                                                    </td>
                                                    <td>

                                                            ${empty(entity.JOIN_STU)?0:entity.JOIN_STU}
                                                    </td>
                                                    <td data-oid="${entity.ONLINETUTOR_ID}-3">

                                                            ${entity.CLASS_TEACHER}
                                                    </td>
                                                    <td data-oid="${entity.ONLINETUTOR_ID}-4">
                                                            <c:choose>
                                                                <c:when test="${entity.LESSON_STATE eq 'needlesson'}">
                                                                    <div class="text-green">待开始</div>
                                                                    <small class="gray9">（${entity.DAY}天后开始）</small>
                                                                </c:when>
                                                                <c:when test="${entity.LESSON_STATE eq 'lessoned'  }"><div class="text-gray">已结束</div></c:when>
                                                                <c:otherwise><div class="text-orange">直播中</div></c:otherwise>
                                                            </c:choose>
                                                    </td>
                                                      <td data-oid="${entity.ONLINETUTOR_ID}">
														<c:if test="${entity.LESSON_STATE eq 'lessoning'}">
                           									<a href="${entity.TCH_URL}?nickname=${userName}&token=111111" target="_blank" >助教入口</a>
                           								</c:if>
													</td>
                           								
                                                    <td data-oid="${entity.ONLINETUTOR_ID}">
                                                        <div class="data-operion">
                                                            <c:if test="${entity.LESSON_STATE eq 'needlesson'}">
                                                                <a href="${ctx}/edumanage/openclass/toLesson?id=${entity.ONLINE_ID}" class="operion-item" data-toggle="tooltip" title="修改"><i class="fa fa-edit"></i></a>
                                                                <a href="#"   data-id="${entity.ONLINE_ID}"  class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove"><i class=" fa fa-trash-o text-red"></i></a>
                                                            </c:if>
                                                            <c:if test="${entity.LESSON_STATE eq 'lessoning'}">
                                                                <a href="${entity.TCH_URL}?nickname=${entity.CLASS_TEACHER}&token=${entity.TCH_TOKEN}" target="_blank" class="operion-item" data-toggle="tooltip" title="" data-original-title="进入直播"><i class="fa fa-fw fa-sign-in"></i>进入直播</a>
                                                            </c:if>
                                                                <a href="${ctx}/edumanage/openclass/toLessonDetail?id=${entity.ONLINE_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-view-more"></i></a>
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
                </div>
            </div>
    </form>
</section>


<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
<script type="text/javascript">
    $(function () {

        _w_table_rowspan("#table",1,"data-oid");
        _w_table_rowspan("#table",2,"data-oid");
        _w_table_rowspan("#table",8,"data-oid");
        _w_table_rowspan("#table",9,"data-oid");
        _w_table_rowspan("#table",10,"data-oid");
       /*  var numbers=[];
        $('td[data-oid]').each(function(){
			if(numbers.indexOf($(this).attr('data-oid'))<0){
			    numbers.push($(this).attr('data-oid'));
			}
        });
        $.each(numbers,function(index,num){
            $tds=$('td[data-oid="'+num+'"]');
            $('td[data-oid="'+num+'"]:first').attr('rowspan',$tds.length);
            $('td[data-oid="'+num+'"]').not(':first').remove();
        }); */
        //删除直播
        $("body").confirmation({
            selector: '[data-role="remove"]',
            html:true,
            placement:'top',
            content:'<div class="f12 gray9 margin_b10"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>确定要删除该直播？</div>',
            title:false,
            btnOkClass    : 'btn btn-xs btn-primary',
            btnOkLabel    : '确认',
            btnOkIcon     : '',
            btnCancelClass  : 'btn btn-xs btn-default margin_l10',
            btnCancelLabel  : '取消',
            btnCancelIcon   : '',
            popContentWidth:190,
            onShow:function(event,element){

            },
            onConfirm:function(event,element){

                postIngIframe=$.formOperTipsDialog({
                    text:'数据删除中...',
                    iconClass:'fa-refresh fa-spin'
                });
                var url = "/edumanage/openclass/delete?onlineId="+element.attr("data-id");
                $.post(url,{},function (data) {
                    console.log(data);
                    if(data.obj==1){
                        postIngIframe.find('[data-role="tips-text"]').html('数据删除成功...');
                        postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
                    }else{
                        postIngIframe.find('[data-role="tips-text"]').html('数据删除失败');
                        postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-exclamation-circle');
                    }
                    /*关闭弹窗*/
                    setTimeout(function(){
                        $.closeDialog(postIngIframe)
                    },1500);
                    // 刷新页面
                    window.location.href="/edumanage/openclass/schoolTeach";
                },"json");
            },
            onCancel:function(event, element){

            }
        });


        $(".lesson_type").click(function(event){// 状态选择
            event.preventDefault();
            $("#search_lessonType").val($(this).val());
            $('#btn_search').click();
        });


        $('[data-toggle="tab"]').click(function(event) {
            event.preventDefault();
            window.location.href=$(this).attr("href");
        });

    })

    // 函数说明：合并指定表格（表格id为_w_table_id）指定列（列数为_w_table_colnum）的相同文本的相邻单元格
    // 参数说明：_w_table_id 为需要进行合并单元格的表格的id。如在HTMl中指定表格 id="data" ，此参数应为 #data
    // 参数说明：_w_table_colnum 为需要合并单元格的所在列。为数字，从最左边第一列为1开始算起。
    // 参数说明: val 单元格tr 里面的赋值  根据这个来判断是不是同一个单元格
    function _w_table_rowspan(_w_table_id, _w_table_colnum,val) {

        _w_table_firsttd = "";

        _w_table_currenttd = "";

        _w_table_SpanNum = 0;

        _w_table_Obj = $(_w_table_id + " tr td:nth-child(" + _w_table_colnum + ")");

        _w_table_Obj.each(function(i) {

            if (i == 0) {

                _w_table_firsttd = $(this);

                _w_table_SpanNum = 1;

            } else {

                _w_table_currenttd = $(this);

                if (_w_table_firsttd.attr(val) == _w_table_currenttd.attr(val)) {

                    _w_table_SpanNum++;

                    _w_table_currenttd.hide(); // remove();

                    _w_table_firsttd.attr("rowSpan", _w_table_SpanNum);

                } else {

                    _w_table_firsttd = $(this);

                    _w_table_SpanNum = 1;

                }

            }

        });

    }

</script>
</body>
</html>
