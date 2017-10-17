/**
 * 注册用户信息时先判断必填项目提交的内容是否为空，
 * 如果为空，返回页面提示用户必填 提交到controller进行判断，返回结果
 */
function askRegist() {
	$('warn').style.color = '';
	if ($F('name') == null || $F('name') == "" || $F('password') == null
			|| $F('password') == "" || $F('phone') == null || $F('phone') == ""
			|| $F('email') == null || $F('email') == "") {
		$('warn').style.color = 'red';
	} else {
		
		var url = config.regist_url;
		
		var xhr = getXmlHttpRequest();
		xhr.open("put", url, false);
		xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					var txt = xhr.responseText;
					$('msg').innerHTML = txt;
					setTimeout(pagerefresh, 30000);
				}
			}
		};
		// controller接收参数为对象，则提交方式要用json数据提交
		xhr.send(JSON.stringify({
			'name' : $F('name'),
			'password' : $F('password'),
			'phone' : $F('phone'),
			'email' : $F('email'),
			'address' : $F('address'),
		}));
	}
}


