/**
 * 传递用户输入的页数到后台，查询后显示
 * @param obj
 */
function sub_page_p(obj) {
	// 两种方式均可
	// window.parent.document.getElementById("iframepage").src="http://localhost:8088/MS/main/listPerson?page="+obj;
	window.location.href = "http://localhost:8088/MS/person/listPerson?page="
			+ obj;
}


/**
 * 用form提交方式，将选项的value值，也就是数据的id，传到后台进行遍历删除
 */
/*
function deletePerson() { 
	var checkbox = document.getElementsByName("delete"); 
	var a = 0;
	for(i=0;i<checkbox.length;i++){ 
		var e = checkbox[i]; 
		if(!e.checked){ 
			a++;
		}else{ 
		 	a--; 
		} 
	} 
	if(a==checkbox.length){ 
		$('msg').innerHTML = "请选择删除项！";
	}else{ 
		$('f2').submit(); 
	} 
}
 */

/**
 * 获取session绑定的person数据
 * @returns
 */
function getSession(){
	var map;
	sendJsonRequest(config.getUserself_url,'get',null,false,function(data){
		map =  JSON.parse(data).map;
		 var loginPerson =  map.loginPerson;
	 	 var personRole =  map.personRole;
	});
	
	return map;
	
}
