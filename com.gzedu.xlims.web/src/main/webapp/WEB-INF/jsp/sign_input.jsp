<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
<title>签名</title>
<link rel="stylesheet" type="text/css" href="${css}/ouchgzee_com/person_center/v3.0.1/mobile-signature/css/base.css">
</head>

<body>
   	<div class="page-box">
		<!--header-->
		<header class="header">
			<div class="header_in">
				<h3 class="page_tit">签名确认</h3>
			</div>
		</header>
      	
		<!--main-->
		<div class="wrap-page">
			<form id="myForm" name="myForm" action="${ctx}/qrcode/signConfirm" method="post">
				<input type="hidden" id="check_sign" value="0">
				<input type="hidden" id="token" name="token" value="${feedback.obj }" />
		       	<input type="hidden" id="autograph" name="autograph" />
		       	
				<div class="content">
					<div class="input-group">
						<div class="input-btn">
							<div id="preview_images" class="preview_images"></div>
							<input name="sign" id="signup" type="hidden" value="">
						</div>
						<div class="group-btn">
					      <button type="button" class="sign-btn btn-bg-white" id="reSign">重签</button>
					    </div>
					    <div class="group-btn">
					      <button type="button" class="sign-btn" id="confirm-btn">确认</button>
					    </div>
					</div>
		
					<div class="enroll_box_qianm">
						<div id="signature" class="signature"></div>
						<div class="tips">
							请在横线上签上你的姓名全称
						</div>
						<div class="mark-over" style="display: none;">
					      <table width="100%" height="100%">
					        <tbody><tr>
					          <td valign="middle" style="font-size:130%;">签名已确认，如需重签请点击“重签”按钮</td>
					        </tr>
					      </tbody></table>
					    </div>
					</div>
		
					<div class="checkbox">
					    <i id="category" class="icon-check"></i><span class="category_span">我承诺及确认以上信息正确无误并保证其真实性</span>
					  </div>
					  <div class="alert-panel pad-1em text-gray margin-t1em">
					    1、签名必须是你的姓名全称:<br>
					    2、签名尽量连续写完，不要逐字书写，避免无法签完姓名全称
					  </div>
					  <div class="text-center text-gray margin-t1em">
					    主办：国家开放大学
					  </div>
				</div>
			</form>
		</div>
		<!--footer-->
		<footer class="footer">
			<div class="footer_in">
				<button type="button" id="sub_signName" class="submit-btn">确认提交</button>
			</div>
		</footer>
	</div>
	
	<script type="text/javascript" src="${css }/common/js/jquery-1.9.0.min.js"></script>
	<script type="text/javascript" src="${css }/ouchgzee_com/person_center/complete-info-mobile/js/jSignature.min.noconflict.js"></script>
	<script type="text/javascript">

		//当前签名的字数
		var sign_count = 0;
		//最新的Base30
		var currnetSignBase30Content;
		
		function onPageLoad(signature){
			if(signature){
				//已存在签名数据,则点击重签后才进行签名初始化操作
				var isFirstReSign = true;
				$("#reSign").click(function(){
					if(isFirstReSign){
						$(".enroll_box_qianm").show();
						$(".sign_block").css("padding","0 0 40px");
						initSignature();
						
						isFirstReSign = false;
					}
					reSign();
				});
				
			}else{
				//尚未有签名,则正常初始化签名插件
				$(".sign_block").css("padding","0 0 40px");
				$(".enroll_box_qianm").show();
				initSignature();
				$("#reSign").click(function(){
					reSign();
				});
			}
		}
		
		function initSignature(){
			//初始化签名插件
			$("#signature").jSignature({
				width : '100%',
				height : '100%'
			});
			//启动输入检测
			//setInterval(checkSign, 500);
		}
		
		//确认签名
		$('#confirm-btn').click(function(){
			if($('#signup').val()!=''){ 
			  return;
			}
			else{
				var base30 =$("#signature").jSignature("getData", "base30");
				if(!base30 || (base30.length == 2 && base30[1])=="" ){
				  alert('请签名');
				  return;
				}
			}
			checkSign();
			addSign();
		});
		
		//检测用户的输入
		function checkSign(){
			//获取Base30数据
			var base30 = $("#signature").jSignature("getData", "base30");
			if(!base30 || base30.length != 2){
				return;
			}
			var base30Content = base30[1];
			if(!base30Content){
				//空内容 不处理
				return;
			}
			if(base30Content == currnetSignBase30Content){
				//如果当前的BASE30与最新的BASE30一致,则认为用户已经书写完成
				addSign();
			}else{
				//否则更新最新的BASE30内容
				currnetSignBase30Content = base30Content;
			}
		}
		
		//将写字板的内容添加至展示区
		function addSign(){
			var $signArea=$("#signature");
			var data = $signArea.jSignature("getData", "image");
			if(data && data.length > 1){
				sign_count++;
				var src = "data:" + data[0] + "," + data[1];
				//构建IMG标签
				var img = $("<img name=\"preview_image\" class=\"preview_image\" />")
				img.attr("id","preview_image_" + sign_count);
				img.attr("src", src);
				$(img).appendTo("#preview_images");
				//置签名标识
				$("#check_sign").val("1");
				$("#signup").val(src);
				//擦除写字板
				erase();
				
				$(".mark-over").show();
		        $signArea.jSignature("disable");
			}
		}
		
		//重签
		function reSign(){
			//if(sign_count > 0){
				var $signArea=$("#signature");
		        $(".mark-over").hide();
		        $signArea.jSignature("enable");
				
				$("#preview_images").empty();
				sign_count = 0;
				$("#check_sign").val("0");
				$("#signup").val('');
			//}
			erase();
		}
		
		//擦除
		function erase(){
			$("#signature").jSignature("reset");
		}
		
		$(".checkbox").on('click', function(event) {
			event.preventDefault();
			$(this).toggleClass('actived');
			/*if($("#sub_signName").prop("disabled")){
				$("#sub_signName").prop("disabled",false);
			}
			else{
				$("#sub_signName").prop("disabled",true);
			}*/
		});
		$('#sub_signName').click(function(){
			var that=this;
			if($('#signup').val()==''){
				var base30 =jQuery("#signature").jSignature("getData", "base30");
				if(!base30 || (base30.length == 2 && base30[1])!="" ){
				  alert('请确认签名');
				}
				else{
				  alert('请签名');
				}
				return;
			}
		
			if(!$('.checkbox').hasClass('actived')){
				alert('请勾选我承诺信息的真实性')
				return;
			}
			
			//提交业务
			if($("#check_sign").val() == "1") {
				$("#autograph").val($('#signup').val());
				$('#myForm').submit();
			}
		});
		$(function(){
			onPageLoad();
		});
		</script>
</body>
</html>
