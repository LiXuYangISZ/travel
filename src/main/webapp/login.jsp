<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="css/common.css">
    <link rel="stylesheet" type="text/css" href="css/login.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
      <!--导入angularJS文件-->
    <!--<script src="js/angular.min.js"></script>-->
	<!--导入jquery-->
	<script src="js/jquery-3.3.1.js"></script>
	<script>
		//图片点击事件
		function changeCheckCode() {
			$("#checkCode").attr("src","checkCode?"+new Date().getTime());
			//$("#check").src="checkCode?"+new Date().getTime();
		}
		//校验用户名
		function checkUsername() {
			//1.获取用户名
			var username = $('#username').val();

			if (username == "") {
				$("#username").css("border", "1px solid red");
				$("#errorMsg").html("请您输入用户名")
				return false;
			} else {
				$("#username").css("border", "");
				$("#errorMsg").html("");
				return true;
			}


		}
		//校验密码
		function checkPassword() {

			var password = $('#password').val();

			if (password == "") {
				$("#password").css("border", "1px solid red");
				$("#errorMsg").html("请您输入密码")
				return false;
			} else {
				$("#password").css("border", "");
				$("#errorMsg").html("");
				return true;
			}

		}
		//校检验证码
		function checkCheckCode() {
			let check = $("#check").val();
			if (check != "") {
				$("#check").css("border", "");
				$("#errorMsg").html("");
				return true;
			} else {
				$("#errorMsg").html("验证码不能为空!");
				$("#check").css("border", "1px solid red");
				return false;
			}
		}
		$(function () {
			//1.给登录按钮绑定单击事件
			$("#btn_sub").click(function(){  //因为只是个普通按钮,所以不能使用submit事件
				if(checkUsername()&&checkPassword()&&checkCheckCode()){
					//2.发送AJAX请求,提交表单数据
					$.post("user/login",$("#loginForm").serialize(),function(data){
						//3.   data:{flag:false,errorMsg:''}  处理响应结果
						if(data.flag){
							location.href="index.jsp";
						}else{
							$("#errorMsg").html(data.errorMsg);
						}
					});
				}
				return false;//没有通过则不让页面跳转.  不然会...

			});
			$("#username").blur(checkUsername);
			$("#password").blur(checkPassword);
			$("#check").blur(checkCheckCode);

		})
	</script>
</head>

<body>
<!--引入头部-->
<!--利用include.js方式-->
	<div id="header"></div>
    <!-- 头部 end -->
    <section id="login_wrap">
        <div class="fullscreen-bg" style="background: url(images/login_bg.png);height: 532px;">
        	
        </div>
        <div class="login-box">
        	<div class="title">
        		<img src="images/login_logo.png" alt="">
        		<span>欢迎登录旅游账户</span>
        	</div>
        	<div class="login_inner">
				
				<!--登录错误提示消息-->
        		<div id="errorMsg" class="alert alert-danger" ></div>
				<form id="loginForm" action="" method="post" accept-charset="utf-8">
        			<input type="hidden" name="action" value="login"/>
					<input name="username" type="text" placeholder="请输入账号" autocomplete="off" id="username">
        			<input name="password" type="password" placeholder="请输入密码" autocomplete="off" id="password">
        			<div class="verify">
					<input name="check" type="text" placeholder="请输入验证码" autocomplete="off" id="check">
					<span><img src="checkCode" alt="" onclick="changeCheckCode()" id="checkCode"></span>

			</div>
			<div class="submit_btn">
        				<button type="button" id="btn_sub">登录</button>
        				<div class="auto_login">
        					<input type="checkbox" name="" class="checkbox">
        					<span>自动登录</span>
        				</div>        				
        			</div>        			       		
        		</form>
        		<div class="reg">没有账户？<a href="javascript:;">立即注册</a></div>
        	</div>
        </div>
    </section>
    <!--引入尾部-->
    <div id="footer"></div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery-1.11.0.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
    <!--导入布局js，共享header和footer-->
    <script type="text/javascript" src="js/include.js"></script>
</body>

</html>