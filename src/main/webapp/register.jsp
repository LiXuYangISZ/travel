<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<head>
	<meta charset="utf-8">
	<title>注册</title>
	<link rel="stylesheet" type="text/css" href="css/common.css">
	<link rel="stylesheet" href="css/register.css">
	<!--导入jquery-->
	<script src="js/jquery-3.3.1.js"></script>
	<script>
		//图片点击事件
		function changeCheckCode() {
			$("#checkCode").attr("src","checkCode?"+new Date().getTime());
			//$("#check").src="checkCode?"+new Date().getTime();
		}
		/*
         表单校验:
           1.用户名:单词字符,长度8--20
            2.密码:单词字符,长度8--20
             3.email:邮箱格式
             4.姓名:非空
             5.手机号:手机号格式
             6.出生日期:非空
             7.验证码:非空
                         */


		//校验用户名
		function checkUsername() {
			//1.获取用户名
			var username = $('#username').val();
			//2.定义正则  以字母开头,由字母数字下划线组成, 长度是8-20
			var reg_username = /^[a-zA-Z]\w{7,19}$/;

			//3.判断,给与提示信息
			var flag = reg_username.test(username);
			if (flag) {
				$("#username").css("border", "");
			} else {
				$("#username").css("border", "1px solid red");
			}

			return flag;
		}
		//校验密码
		function checkPassword() {
			//1.获取用户名
			var password = $('#password').val();
			//2.定义正则  以字母开头,由字母数字下划线组成, 长度是8-20
			var reg_password = /^[a-zA-Z]\w{7,19}$/;

			//3.判断,给与提示信息
			var flag = reg_password.test(password);
			if (flag) {
				$("#password").css("border", "");
			} else {
				$("#password").css("border", "1px solid red");
			}

			return flag;
		}
		//校检邮箱
		function checkEmail() {
			//获取邮箱
			var email = $("#email").val();
			//2.定义正则
			var reg_email = /^[A-Za-z0-9._%-]+@([A-Za-z0-9-]+.)+[A-Za-z]{2,4}$/;
			//3.进行判断
			var flag = reg_email.test(email);
			if (flag) {
				$("#email").css("border", "");
			} else {
				$("#email").css("border", "1px solid red");
			}
			return flag;


		}
		//校检姓名
		function checkName() {
			let name = $("#name").val();
			// 姓名由 字符 和中文字符组成.
			var reg_name = /^[u4E00-u9FA5A-Za-z0-9_]+$/;
			var flag = reg_name.test(name);
			if (flag) {
				$("#name").css("border", "");
			} else {
				$("#name").css("border", "1px solid red");
			}
			return flag;
		}
		//校检手机号
		function checkTelephone() {
			var telephone = $("#telephone").val();
			var reg_telephone = /^1[3|4|5|7|8]\d{9}$/;
			var flag = reg_telephone.test(telephone);
			if (flag) {
				$("#telephone").css("border", "");
			} else {
				$("#telephone").css("border", "1px solid red");
			}
			return flag;
		}
		//校检出生日期
		function checkBirthday() {
			//var flag
			var birthday = $("#birthday").val();
			if (birthday != "") {
				$("#birthday").css("border", "");
				return true;
			} else {
				$("#birthday").css("border", "1px solid red");
				return false;
			}

		}
		//校检验证码
		function checkCheckCode() {
			let check = $("#check").val();
			if (check != "") {
				$("#check").css("border", "");
				return true;
			} else {
				$("#errorMsg").html("验证码不能为空!");
				$("#check").css("border", "1px solid red");
				return false;
			}
		}
		$(function() {
			//当表单提交时,调用所有的校检方法
			$("#registerForm").submit(function() {


				if (checkUsername() && checkPassword() && checkEmail() && checkName() && checkTelephone() && checkBirthday()&&checkCheckCode() ) {
					//每次提交都刷新验证码.
					//changeCheckCode();
					//校检通过,发送AJAX请求,提交表单的数据  username=张三&password=123
					$.post("user/regist", $(this).serialize(), function(data) {

						//处理服务器相应的数据. data
						if(data.flag){//
							//注册成功,发生跳转
							location.href="register_ok.jsp";
						}else{

							//注册失败,给errorMsg增加提示信息
							$("#errorMsg").html(data.errorMsg);
						}
					})
				}
				//没有通过则不让页面发生跳转.
				return false;
			});
			//当某一个组件失去焦点时,调用相应的校检方法
			$("#username").blur(checkUsername);
			$("#password").blur(checkPassword);
			$("#email").blur(checkEmail);
			$("#name").blur(checkName);
			$("#telephone").blur(checkTelephone);
			$("#birthday").blur(checkBirthday);
			$("#check").blur(checkCheckCode);
		});
	</script>

</head>

<body>
<!--引入头部-->
<div id="header"></div>
<!-- 头部 end -->
<div class="rg_layout">
	<div class="rg_form clearfix">
		<div class="rg_form_left">
			<p>新用户注册</p>
			<p>USER REGISTER</p>
		</div>
		<div class="rg_form_center">

			<!--注册表单-->
			<form id="registerForm">
				<!--提交处理请求的标识符-->
				<input type="hidden" name="action" value="register">
				<table style="margin-top: 25px;">
					<tr>
						<td class="td_left">
							<label for="username">用户名</label>
						</td>
						<td class="td_right">
							<input type="text" id="username" name="username" placeholder="请输入账号">

						</td>

					</tr>
					<tr>
						<td class="td_left">
							<label for="password">密码</label>
						</td>
						<td class="td_right">
							<input type="text" id="password" name="password" placeholder="请输入密码">
						</td>
					</tr>
					<tr>
						<td class="td_left">
							<label for="email">Email</label>
						</td>
						<td class="td_right">
							<input type="text" id="email" name="email" placeholder="请输入Email">
						</td>
					</tr>
					<tr>
						<td class="td_left">
							<label for="name">姓名</label>
						</td>
						<td class="td_right">
							<input type="text" id="name" name="name" placeholder="请输入真实姓名">
						</td>
					</tr>
					<tr>
						<td class="td_left">
							<label for="telephone">手机号</label>
						</td>
						<td class="td_right">
							<input type="text" id="telephone" name="telephone" placeholder="请输入您的手机号">
						</td>
					</tr>
					<tr>
						<td class="td_left">
							<label for="sex">性别</label>
						</td>
						<td class="td_right gender">
							<input type="radio" id="sex" name="sex" value="男" checked> 男
							<input type="radio" name="sex" value="女"> 女
						</td>
					</tr>
					<tr>
						<td class="td_left">
							<label for="birthday">出生日期</label>
						</td>
						<td class="td_right">
							<input type="date" id="birthday" name="birthday" placeholder="年/月/日">
						</td>
					</tr>
					<tr>
						<td class="td_left">
							<label for="check">验证码</label>
						</td>
						<td class="td_right check">
							<input type="text" id="check" name="check" class="check">
							<img src="checkCode" height="32px" alt="" id="checkCode" onclick="changeCheckCode()">

						</td>
					</tr>
					<tr>
						<td class="td_left">
						</td>
						<td class="td_right check">
							<input type="submit" class="submit" value="注册">
							<span id="msg" style="color: red;"></span>
						</td>
					</tr>
				</table>
			</form>
			<div id="errorMsg" style="text-align: center; color: red; font-size: 20px"></div>
		</div>
		<div class="rg_form_right">
			<p>
				已有账号？
				<a href="#">立即登录</a>
			</p>
		</div>
	</div>
</div>
<!--引入尾部-->
<div id="footer"></div>
<!--导入布局js，共享header和footer-->
<script type="text/javascript" src="js/include.js"></script>

</body>

</html>