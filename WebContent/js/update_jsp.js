/**
 * 更新用户信息传递到后台，更新数据库
 */
function askUpdate() {
	if ($F('name') == null || $F('name') == "" || $F('password') == null
			|| $F('password') == "" || $F('phone') == null || $F('phone') == ""
			|| $F('email') == null || $F('email') == "") {
		$('warn').style.color = 'red';
	} else {
		var url = "http://localhost:8088/MS/person/checkUpdate";
		var obj = {
				'person':{
					'id':$F('id'),
					'name' : $F('name'),
					'password' : $F('password'),
					'phone' : $F('phone'),
					'email' : $F('email'),
					'address' : $F('address'),
					'head' : $F('head')
					
				},'role':{
					'roleName':$F('roleName')
				}
		};
		console.info("提交数据："+JSON.stringify(obj));
		sendJsonRequest(url,'PUT',obj,false,function(data){
			console.info("获取用户响应："+data);
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 30000);
		},function(){
			$('msg').innerHTML ="提交错误";
		});
	}
}

/**
 * 更改信息页面返回到查询页面
 */
function goBack(){
	var url = "http://localhost:8088/MS/person/listPerson?page=1";
	window.parent.document.getElementById("iframepage").src=url;
}