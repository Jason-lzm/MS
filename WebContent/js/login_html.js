/**
 * 发送用户名和密码到LoginControl做判断， 
 * 如果正确，重定向到主页main.jsp 如果不正确，提示错误信息
 */
function askLogin() {
	$('msg').innerHTML = "";
	if ($F('name') == null || $F('name') == "" || $F('password') == null
			|| $F('password') == "") {
		$('msg').innerHTML = "请填写用户名和密码！";
	} else {
		
		var url = config.login_url;
		
		var xhr = getXmlHttpRequest();
		xhr.open("post", url, false);
		xhr.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					var txt = xhr.responseText;
					if (txt == "success") {
						window.location.href = config.msMain_url;
					} else {
						$('msg').innerHTML = txt;
					}
				} else {
					$('msg').innerHTML = txt;
				}
			}
		};
		xhr.send('name=' + $F('name') + '&password=' + $F('password'));
	}
}



