<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
  <title>班主任平台 - 督促提醒</title>
  <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
  <h1>
    提醒详情
  </h1>
  <ol class="breadcrumb">
    <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
    <li><a href="${ctx}/home/class/remind/${param.source==1?'eeList':'smsList'}">督促提醒</a></li>
    <li class="active">提醒详情</li>
  </ol>
</section>
<section class="content">
  <div class="box margin-bottom-none">
    <div class="box-body pad20">
      <article class="article">
        <h1 class="article-title text-left2">
          ${info.infoTheme}
        </h1>
        <div class="article-info2">
          <div>
            班主任${info.gjtUserAccount.realName}老师 <fmt:formatDate value="${info.createdDt}" type="date" /> 发送 <br>
          </div>
          <div class="clearfix gray9 f12 more-per-wrap">
            <div class="pull-left">接收人：</div>
            <div class="oh">
              <span class="stu_container" data-list="${info.memo}"></span>
                  <span class="omit_txt">
                      等<em></em>人
                  </span>
              <a href="javascript:void(0)" class="extra_oper">展开&gt;</a>
            </div>
          </div>
        </div>
        <section class="txt-info">
          ${info.infoContent}
        </section>
      </article>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
  //“接收人”操作
  $(".stu_container").each(function(index,ele){
    var container=$(this),
            stu_list=(container.attr("data-list")).split(",");
    if(stu_list.length>0){
      var strPrev3=stu_list.slice(0,3).join("、");
      container.html(strPrev3);
      container.siblings(".omit_txt").children("em").html(stu_list.length);

      if(stu_list.length<=3){
        container.siblings().hide();
      }
      else{
        container.siblings(".extra_oper").click(function(event) {
          if(!$(this).hasClass('on')){
            container.html(stu_list.join("、"));
            $(this).html("收起");
            $(this).addClass('on');
            $(this).siblings(".omit_txt").hide();
          }
          else{
            container.html(strPrev3);
            $(this).html("展开>");
            $(this).removeClass('on');
            $(this).siblings(".omit_txt").show();
          }
        });
      }
    }
  });
</script>
</body>
</html>
