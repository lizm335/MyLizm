<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<title>这个.. 服务器出错了！！！</title>
		
		<style type="text/css">
			/**btn css**/
						
			.btn {
			    display: inline-block;
			    padding: 6px 12px;
			    margin-bottom: 0;
			    font-size: 14px;
			    font-weight: 400;
			    line-height: 1.42857143;
			    text-align: center;
			    white-space: nowrap;
			    vertical-align: middle;
			    -ms-touch-action: manipulation;
			    touch-action: manipulation;
			    cursor: pointer;
			    -webkit-user-select: none;
			    -moz-user-select: none;
			    -ms-user-select: none;
			    user-select: none;
			    background-image: none;
			    border: 1px solid transparent;
			    border-radius: 4px;
			    text-decoration:none;
			}
			
			.btn.active.focus,.btn.active:focus,.btn.focus,.btn:active.focus,.btn:active:focus,.btn:focus {
			    outline: thin dotted;
			    outline: 5px auto -webkit-focus-ring-color;
			    outline-offset: -2px
			}
			
			.btn.focus,.btn:focus,.btn:hover {
			    color: #333;
			    text-decoration: none
			}
			
			.btn.active,.btn:active {
			    background-image: none;
			    outline: 0;
			    -webkit-box-shadow: inset 0 3px 5px rgba(0,0,0,.125);
			    box-shadow: inset 0 3px 5px rgba(0,0,0,.125)
			}
			
			.btn.disabled,.btn[disabled],fieldset[disabled] .btn {
			    cursor: not-allowed;
			    filter: alpha(opacity=65);
			    -webkit-box-shadow: none;
			    box-shadow: none;
			    opacity: .65
			}
			
			a.btn.disabled,fieldset[disabled] a.btn {
			    pointer-events: none
			}
			
			.btn-default {
			    color: #333;
			    background-color: #fff;
			    border-color: #ccc
			}
			
			.btn-default.focus,.btn-default:focus {
			    color: #333;
			    background-color: #e6e6e6;
			    border-color: #8c8c8c
			}
			
			.btn-default:hover {
			    color: #333;
			    background-color: #e6e6e6;
			    border-color: #adadad
			}
			
			.btn-default.active,.btn-default:active,.open>.dropdown-toggle.btn-default {
			    color: #333;
			    background-color: #e6e6e6;
			    border-color: #adadad
			}
			
			.btn-default.active.focus,.btn-default.active:focus,.btn-default.active:hover,.btn-default:active.focus,.btn-default:active:focus,.btn-default:active:hover,.open>.dropdown-toggle.btn-default.focus,.open>.dropdown-toggle.btn-default:focus,.open>.dropdown-toggle.btn-default:hover {
			    color: #333;
			    background-color: #d4d4d4;
			    border-color: #8c8c8c
			}
			
			.btn-default.active,.btn-default:active,.open>.dropdown-toggle.btn-default {
			    background-image: none
			}
			
			.btn-default.disabled,.btn-default.disabled.active,.btn-default.disabled.focus,.btn-default.disabled:active,.btn-default.disabled:focus,.btn-default.disabled:hover,.btn-default[disabled],.btn-default[disabled].active,.btn-default[disabled].focus,.btn-default[disabled]:active,.btn-default[disabled]:focus,.btn-default[disabled]:hover,fieldset[disabled] .btn-default,fieldset[disabled] .btn-default.active,fieldset[disabled] .btn-default.focus,fieldset[disabled] .btn-default:active,fieldset[disabled] .btn-default:focus,fieldset[disabled] .btn-default:hover {
			    background-color: #fff;
			    border-color: #ccc
			}
			
			.btn-default .badge {
			    color: #fff;
			    background-color: #333
			}
			
			.btn-primary {
			    color: #fff;
			    background-color: #337ab7;
			    border-color: #2e6da4
			}
			
			.btn-primary.focus,.btn-primary:focus {
			    color: #fff;
			    background-color: #286090;
			    border-color: #122b40
			}
			
			.btn-primary:hover {
			    color: #fff;
			    background-color: #286090;
			    border-color: #204d74
			}
			
			.btn-primary.active,.btn-primary:active,.open>.dropdown-toggle.btn-primary {
			    color: #fff;
			    background-color: #286090;
			    border-color: #204d74
			}
			
			.btn-primary.active.focus,.btn-primary.active:focus,.btn-primary.active:hover,.btn-primary:active.focus,.btn-primary:active:focus,.btn-primary:active:hover,.open>.dropdown-toggle.btn-primary.focus,.open>.dropdown-toggle.btn-primary:focus,.open>.dropdown-toggle.btn-primary:hover {
			    color: #fff;
			    background-color: #204d74;
			    border-color: #122b40
			}
			
			.btn-primary.active,.btn-primary:active,.open>.dropdown-toggle.btn-primary {
			    background-image: none
			}
			
			.btn-primary.disabled,.btn-primary.disabled.active,.btn-primary.disabled.focus,.btn-primary.disabled:active,.btn-primary.disabled:focus,.btn-primary.disabled:hover,.btn-primary[disabled],.btn-primary[disabled].active,.btn-primary[disabled].focus,.btn-primary[disabled]:active,.btn-primary[disabled]:focus,.btn-primary[disabled]:hover,fieldset[disabled] .btn-primary,fieldset[disabled] .btn-primary.active,fieldset[disabled] .btn-primary.focus,fieldset[disabled] .btn-primary:active,fieldset[disabled] .btn-primary:focus,fieldset[disabled] .btn-primary:hover {
			    background-color: #337ab7;
			    border-color: #2e6da4
			}
			
			.btn-primary .badge {
			    color: #337ab7;
			    background-color: #fff
			}
			
			.btn-success {
			    color: #fff;
			    background-color: #5cb85c;
			    border-color: #4cae4c
			}
			
			.btn-success.focus,.btn-success:focus {
			    color: #fff;
			    background-color: #449d44;
			    border-color: #255625
			}
			
			.btn-success:hover {
			    color: #fff;
			    background-color: #449d44;
			    border-color: #398439
			}
			
			.btn-success.active,.btn-success:active,.open>.dropdown-toggle.btn-success {
			    color: #fff;
			    background-color: #449d44;
			    border-color: #398439
			}
			
			.btn-success.active.focus,.btn-success.active:focus,.btn-success.active:hover,.btn-success:active.focus,.btn-success:active:focus,.btn-success:active:hover,.open>.dropdown-toggle.btn-success.focus,.open>.dropdown-toggle.btn-success:focus,.open>.dropdown-toggle.btn-success:hover {
			    color: #fff;
			    background-color: #398439;
			    border-color: #255625
			}
			
			.btn-success.active,.btn-success:active,.open>.dropdown-toggle.btn-success {
			    background-image: none
			}
			
			.btn-success.disabled,.btn-success.disabled.active,.btn-success.disabled.focus,.btn-success.disabled:active,.btn-success.disabled:focus,.btn-success.disabled:hover,.btn-success[disabled],.btn-success[disabled].active,.btn-success[disabled].focus,.btn-success[disabled]:active,.btn-success[disabled]:focus,.btn-success[disabled]:hover,fieldset[disabled] .btn-success,fieldset[disabled] .btn-success.active,fieldset[disabled] .btn-success.focus,fieldset[disabled] .btn-success:active,fieldset[disabled] .btn-success:focus,fieldset[disabled] .btn-success:hover {
			    background-color: #5cb85c;
			    border-color: #4cae4c
			}
			
			.btn-success .badge {
			    color: #5cb85c;
			    background-color: #fff
			}
			
			.btn-info {
			    color: #fff;
			    background-color: #5bc0de;
			    border-color: #46b8da
			}
			
			.btn-info.focus,.btn-info:focus {
			    color: #fff;
			    background-color: #31b0d5;
			    border-color: #1b6d85
			}
			
			.btn-info:hover {
			    color: #fff;
			    background-color: #31b0d5;
			    border-color: #269abc
			}
			
			.btn-info.active,.btn-info:active,.open>.dropdown-toggle.btn-info {
			    color: #fff;
			    background-color: #31b0d5;
			    border-color: #269abc
			}
			
			.btn-info.active.focus,.btn-info.active:focus,.btn-info.active:hover,.btn-info:active.focus,.btn-info:active:focus,.btn-info:active:hover,.open>.dropdown-toggle.btn-info.focus,.open>.dropdown-toggle.btn-info:focus,.open>.dropdown-toggle.btn-info:hover {
			    color: #fff;
			    background-color: #269abc;
			    border-color: #1b6d85
			}
			
			.btn-info.active,.btn-info:active,.open>.dropdown-toggle.btn-info {
			    background-image: none
			}
			
			.btn-info.disabled,.btn-info.disabled.active,.btn-info.disabled.focus,.btn-info.disabled:active,.btn-info.disabled:focus,.btn-info.disabled:hover,.btn-info[disabled],.btn-info[disabled].active,.btn-info[disabled].focus,.btn-info[disabled]:active,.btn-info[disabled]:focus,.btn-info[disabled]:hover,fieldset[disabled] .btn-info,fieldset[disabled] .btn-info.active,fieldset[disabled] .btn-info.focus,fieldset[disabled] .btn-info:active,fieldset[disabled] .btn-info:focus,fieldset[disabled] .btn-info:hover {
			    background-color: #5bc0de;
			    border-color: #46b8da
			}
			
			.btn-info .badge {
			    color: #5bc0de;
			    background-color: #fff
			}
			
			.btn-warning {
			    color: #fff;
			    background-color: #f0ad4e;
			    border-color: #eea236
			}
			
			.btn-warning.focus,.btn-warning:focus {
			    color: #fff;
			    background-color: #ec971f;
			    border-color: #985f0d
			}
			
			.btn-warning:hover {
			    color: #fff;
			    background-color: #ec971f;
			    border-color: #d58512
			}
			
			.btn-warning.active,.btn-warning:active,.open>.dropdown-toggle.btn-warning {
			    color: #fff;
			    background-color: #ec971f;
			    border-color: #d58512
			}
			
			.btn-warning.active.focus,.btn-warning.active:focus,.btn-warning.active:hover,.btn-warning:active.focus,.btn-warning:active:focus,.btn-warning:active:hover,.open>.dropdown-toggle.btn-warning.focus,.open>.dropdown-toggle.btn-warning:focus,.open>.dropdown-toggle.btn-warning:hover {
			    color: #fff;
			    background-color: #d58512;
			    border-color: #985f0d
			}
			
			.btn-warning.active,.btn-warning:active,.open>.dropdown-toggle.btn-warning {
			    background-image: none
			}
			
			.btn-warning.disabled,.btn-warning.disabled.active,.btn-warning.disabled.focus,.btn-warning.disabled:active,.btn-warning.disabled:focus,.btn-warning.disabled:hover,.btn-warning[disabled],.btn-warning[disabled].active,.btn-warning[disabled].focus,.btn-warning[disabled]:active,.btn-warning[disabled]:focus,.btn-warning[disabled]:hover,fieldset[disabled] .btn-warning,fieldset[disabled] .btn-warning.active,fieldset[disabled] .btn-warning.focus,fieldset[disabled] .btn-warning:active,fieldset[disabled] .btn-warning:focus,fieldset[disabled] .btn-warning:hover {
			    background-color: #f0ad4e;
			    border-color: #eea236
			}
			
			.btn-warning .badge {
			    color: #f0ad4e;
			    background-color: #fff
			}
			
			.btn-danger {
			    color: #fff;
			    background-color: #d9534f;
			    border-color: #d43f3a
			}
			
			.btn-danger.focus,.btn-danger:focus {
			    color: #fff;
			    background-color: #c9302c;
			    border-color: #761c19
			}
			
			.btn-danger:hover {
			    color: #fff;
			    background-color: #c9302c;
			    border-color: #ac2925
			}
			
			.btn-danger.active,.btn-danger:active,.open>.dropdown-toggle.btn-danger {
			    color: #fff;
			    background-color: #c9302c;
			    border-color: #ac2925
			}
			
			.btn-danger.active.focus,.btn-danger.active:focus,.btn-danger.active:hover,.btn-danger:active.focus,.btn-danger:active:focus,.btn-danger:active:hover,.open>.dropdown-toggle.btn-danger.focus,.open>.dropdown-toggle.btn-danger:focus,.open>.dropdown-toggle.btn-danger:hover {
			    color: #fff;
			    background-color: #ac2925;
			    border-color: #761c19
			}
			
			.btn-danger.active,.btn-danger:active,.open>.dropdown-toggle.btn-danger {
			    background-image: none
			}
			
			.btn-danger.disabled,.btn-danger.disabled.active,.btn-danger.disabled.focus,.btn-danger.disabled:active,.btn-danger.disabled:focus,.btn-danger.disabled:hover,.btn-danger[disabled],.btn-danger[disabled].active,.btn-danger[disabled].focus,.btn-danger[disabled]:active,.btn-danger[disabled]:focus,.btn-danger[disabled]:hover,fieldset[disabled] .btn-danger,fieldset[disabled] .btn-danger.active,fieldset[disabled] .btn-danger.focus,fieldset[disabled] .btn-danger:active,fieldset[disabled] .btn-danger:focus,fieldset[disabled] .btn-danger:hover {
			    background-color: #d9534f;
			    border-color: #d43f3a
			}
			
			.btn-danger .badge {
			    color: #d9534f;
			    background-color: #fff
			}
		</style>
		
		
		
		
	<style type="text/css">
					
		body {
			margin: 0;
			padding: 0;
			background: #efefef;
			font-family: Georgia, Times, Verdana, Geneva, Arial, Helvetica, sans-serif;
		}
		
		div#mother {
			margin: 0 auto;
			width: 80%;
			padding:15px;
			position: relative;
		}
		
		div#errorBox {
			background-color:#fff;
			min-width:80px;
			width: 80%;
			margin: auto;
			padding:20px;
			border:solid 1px #ccc;
			border-radius:4px;
			-webkit-box-shadow:1px 1px 6px rgba(0,0,0,0.2);
			box-shadow:1px 1px 6px rgba(0,0,0,0.2);
			
		}
		
		.error-table{
			width:100%;
			border-collapse:collapse;
			border:none;
		}
		
		.error-table-one {
			width:60%;
			color: #39351e;
			font-size: 14px;
			line-height: 26px;
		}
		
		.error-table-two{
			font-size:120px;
			color:#666;
			vertical-align:top;
		}
		
		div.link { /*background:#f90;*/
			height: 50px;
			width: 145px;
			float: left;
		}
		
		div#home {
			margin: 20px 0 0 444px;
		}
		
		div#contact {
			margin: 20px 0 0 25px;
		}
		
		h1 {
		
			font-size: 40px;
			margin-bottom: 35px;
		}
		
		.error-footer{
			margin-top:20px;
			padding:10px 15px;
			text-align:right;
		}
		
		</style>

</head>
<body>
<div class="adcenter">
 &nbsp;
</div>
		<div id="mother">
			<div id="errorBox">
				<h1 style="color:#39351e;">Sorry..服务器内部错误！</h1>
				<table class="error-table">
					<tr>
						<td class="error-table-one">
							<p>
								服务器压力山大，无法正确响应 
							</p>
							<p>请与管理员联系。</p>
						</td>
						<td class="error-table-two">
							500
						</td>
					</tr>
				</table>
				
				
				<div class="error-footer">
					<a href="${ctx}" title="返回首页" class="btn btn-primary">
						返回首页
					</a>
					<a href="javascript:history.back()" title="返回上一页" class="btn btn-default">
						返回上一页
					</a>
				</div>
				
			</div>
		</div>
	</body>
</html>