/**
 * 用Ajax的方式，将要删除的id包装成字符串传递到后台
 */
function deletePerson() {
	var str = "";
	var sel = document.getElementsByName("delete");// 获取checkbox的值
	for (var i = 0; i < sel.length; i++) {
		if (sel[i].checked == true) {
			str += sel[i].value + ",";
		}
	}
	if (str.length > 1) {
		str = str.substring(0, str.length - 1);
		sendAjax("get",
				"http://localhost:8088/MS/person/deletePerson?str=" + str, false,
				null, function(data) {
					$('msg').innerHTML = data;
					setTimeout('pagerefresh()', 5000); // 指定1.5秒刷新一次
				}, function(data) {
					$('msg').innerHTML = data;
					setTimeout('pagerefresh()', 5000); // 指定1.5秒刷新一次
				});
	} else {
		$('msg').innerHTML = "请选择要删除的项目！";
	}

}


/**
 * iframe转到修改信息页面，传递id到后台进行查找用户信息并在页面显示
 */
function updatePerson(id){
	var url = "http://localhost:8088/MS/person/updatePerson?id="+id;
	window.parent.document.getElementById("iframepage").src=url;
}



