<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<%--
  Created by IntelliJ IDEA.
  User: paul
  Date: 2017/2/15
  Time: 9:46
  To change this template use File | Settings | File Templates.
--%>
<html class="reset">
<head>
    <title> 班主任平台 - 服务记录 - 添加学员 </title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border no-shadow full-width position-absolute" style="z-index:20;top:0;">
    <div class="box-header with-border">
        <h3 class="box-title f16 text-bold">添加学员</h3>
    </div>
</div>
<div class="slim-Scroll position-relatvie" style="height:370px;">
    <div class="distribut-teacher table-block full-width pad" style="margin-top:51px;">
        <div class="table-cell-block" style="width:40%;">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div class="form-horizontal offset-margin-tb-15">
                        <div class="form-group margin-bottom-none">
                            <div class="col-xs-4 pad-t5"><b>通讯录</b></div>
                            <div class="col-xs-8">
                                <div class="input-group input-group-sm panel-search-group">
                                    <input type="text" class="form-control" placeholder="请输入搜索内容">
                                    <div class="input-group-addon"><i class="fa fa-search"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="scroll-sim">
                        <ul class="list-unstyled distribut-teacher-list" id="unchooseStus" data-role="all-person">

                        </ul>
                    </div>
                </div>
                <div class="panel-footer">
                    <label class="text-no-bold"><input type="checkbox" data-role="select-all"> 全选</label>
                </div>

            </div>
        </div>
        <div class="table-cell-block vertical-middle text-center">
            <div class="form-group">
                <label class="sr-only">添加</label>
                <button class="btn btn-primary" data-role="add"><span class="pad">添加&gt;</span></button>
            </div>
            <div class="form-group margin-bottom-none">
                <label class="sr-only">移除</label>
                <button class="btn btn-danger" data-role="remove"><span class="pad">移除&gt;</span></button>
            </div>
        </div>
        <div class="table-cell-block" style="width:40%;">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div class="form-horizontal offset-margin-tb-15">
                        <div class="form-group margin-bottom-none">
                            <div class="col-xs-4 pad-t5 pad-b5"><b>接收人</b></div>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="scroll-sim">
                        <ul class="list-unstyled distribut-teacher-list" id="chooseStus" data-role="receive-person">

                        </ul>
                    </div>
                </div>
                <div class="panel-footer">
                    <label class="text-no-bold"><input type="checkbox" data-role="select-all"> 全选</label>
                </div>

            </div>
        </div>
    </div>
</div>
<div class="dialog-btn-box">
    <span class="pull-left pad-t10 gray6 caculate-txt">已选择 <b class="text-light-blue">0</b> 个接收人</span>
    <button class="btn btn-default min-width-90px btn-cancel-edit margin_r15">取消</button>
    <button class="btn btn-primary min-width-90px  btn-sure-edit">确定</button>
</div>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
    
    $(function () {

        initselect();

    });

    var initselect = function () {// 异步加载学生列表

        var oldStus = new Array();
        parent.$("ul").children('li').each(function () {
            var temp = $(this).attr("data-val");
            if(temp != "" &&  temp != undefined){
                oldStus.push(temp.split("_")[0]);
            }
        });
        $.post("${ctx}/headTeacher/webservice/stuList",{"classid":"${classid}","oldStus":oldStus.join(",")},function (re) {
            var arr = new Array();
            var no_phone_class =" class='has-remid-stu' ";
            $.each(re.unchooseStus,function (index,stu) { // 未选择学生
                var val = stu.STUID+"_"+stu.STUNAME+"_"+stu.STUPHONE;
                var thatClass = "";
                if(stu.STUPHONE="" ) {
                    thatClass = no_phone_class;
                }
                arr.push("<li data-val='"+val+"' "+thatClass+"><div class='checkbox'><label><input type='checkbox'><span class='gray9'>[学员]</span>");
                arr.push("<span class='name'>"+stu.STUNAME+"</span>");
                arr.push("</label></div></li>");
            });
            $("#unchooseStus").html(arr.join(""));
            arr = new Array();
            $.each(re.chooseStus,function (index,stu) { // 已选择学生
                var val = stu.STUID+"_"+stu.STUNAME+"_"+stu.STUPHONE;
                var thatClass = "";
                if(stu.STUPHONE="" ) {
                    thatClass = no_phone_class;
                }
                arr.push("<li data-val='"+val+"' "+thatClass+"><div class='checkbox'><label><input type='checkbox'><span class='gray9'>[学员]</span>");
                arr.push("<span class='name'>"+stu.STUNAME+"</span>");
                arr.push("</label></div></li>");
            });
            $("#chooseStus").html(arr.join(""));
        },"json");
    }

    $('.scroll-sim').slimScroll({
        height: '270px',
        size: '5px',
        alwaysVisible:true
    });

    //添加教师
    $(".distribut-teacher")
            .on("click",".btn[data-role='add']",function(){
                var $containerLeft=$("ul[data-role='all-person']");
                var $containerRight=$("ul[data-role='receive-person']");
                var $ckCollectors=$containerLeft.find(":checkbox");
                $ckCollectors.each(function(i,e){
                    if($(this).is(":checked")){
                        $containerRight.prepend($(e).prop("checked",false).closest("li"));
                    }
                });
                $(".caculate-txt > b").text($containerRight.children('li').length);
            })
            //移除教师
            .on("click",".btn[data-role='remove']",function(){
                var $containerLeft=$("ul[data-role='all-person']");
                var $containerRight=$("ul[data-role='receive-person']");
                var $ckCollectors=$containerRight.find(":checkbox");
                $ckCollectors.each(function(i,e){
                    if($(this).is(":checked")){
                        $containerLeft.prepend($(e).prop("checked",false).closest("li"));
                    }
                });
                $(".caculate-txt > b").text($containerRight.children('li').length);
            })
            //全选/全不选教师
            .on("click","input[data-role='select-all']",function(){
                var $ckCollectors=$(this).closest(".panel").find(".distribut-teacher-list :checkbox");
                if($(this).is(":checked")){
                    $ckCollectors.prop("checked",true);
                }
                else{
                    $ckCollectors.prop("checked",false);
                }
            })
            //搜索教师
            .on("keyup",".panel-search-group > input",function(){
                var $that=$(this);
                var $liCollectors=$(this).closest(".panel").find(".distribut-teacher-list > li");
                $liCollectors.each(function(i,e){
                    var searchTxt=$(this).text();
                    if(searchTxt.indexOf($that.val())==-1){
                        $(this).hide();
                    }
                    else{
                        $(this).show();
                    }
                });
            });


    /*取消*/
    $(".btn-cancel-edit").click(function(event) {
        parent.servicePerson.close();
    });

    /*确认*/
    $(".btn-sure-edit").click(function(event) {
        /*插入业务逻辑*/
        var $liCollectors=$("ul[data-role='receive-person']").children('li');
        var $container=parent.$('.stu-list-box');
        $container.html("");// 置空原选择
        var html='<li data-val="{dataval}">'
                +'<span class="name">{name}</span>'
                +'<i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>'
                +'</li>';
        $liCollectors.each(function(index, el) {
            var tmp=html;
            tmp=tmp.replace('{name}',$(this).find(".name").text()).replace('{dataval}',$(this).attr("data-val"));
            $container.append(tmp);
        });

        parent.window.checkAddPer();
        parent.servicePerson.close();
    });
</script>
</body>
</html>
