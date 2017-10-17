var id = parent.params.id;

window.onload = function(){
//	console.info(parent.params.id);
	var menuInfoById = getMenuInfoById();
	
	var selectMenuType = $('menu_type');
	var options = selectMenuType.options;
	//如果修改的是主菜单，子菜单相关信息隐藏
	if(menuInfoById.highermenu==null){
		$('selectByChild').hidden = true;
		$('sub_child').hidden = true;
		options[0] = new Option('主菜单','0');
		$('menu1').value = menuInfoById.menu;
	}else{
		$('selectByParent').hidden = true;
		$('sub_parent').hidden = true;
		options[0] = new Option('子菜单','0');
		selectMenus(menuInfoById);
		$('menu2').value = menuInfoById.menu;
		$('url').value = menuInfoById.url;
	}
}

/**
 * 根据id查找菜单信息显示到修改页面
 * @param id
 */
function getMenuInfoById(){
	url = config.getMenuInfoById_url+"?id="+id;
	var menuInfoById;
	sendJsonRequest(url,'get',null,false,function(data){
		var map = JSON.parse(data).map;
		menuInfoById = map.menuInfoById;
	});
	return menuInfoById;
}


/**
 * 获取所有主菜单，供子菜单选择
 */
function selectMenus(menuInfoById){
	var menuInfos;
	sendJsonRequest(config.getParentMenus_url,'POST',null,false,function(data){
		var map =  JSON.parse(data).map;
		menuInfos = map.menuInfos;
	});
	//显示菜单信息的下拉框
	var selectMenus = $('highermenu');
	var selectOptions = selectMenus.options;//得到下拉框选项的数组
	selectOptions.length = 0;//默认选择为0
	for(i=0;i<menuInfos.length;i++){
		var menuInfo = menuInfos[i];//迭代出每一个主菜单
		//设置选项内容为用户名，value值为用户id
		var optionUser = new Option(menuInfo.menu,menuInfo.id);
		selectOptions[i] = optionUser;
		if(menuInfo.id==parseInt(menuInfoById.highermenu)){
			selectOptions[i].selected = true;
		}
	}
}

function updateParentMenu(){
	var menuName = $F('menu1');
	if(menuName==null || menuName==""){
		$('warn').style.color = 'red';
		return false;
	}else{
		$('warn').style.color = '';
		var menuObj = {
				'id':id,
				'menu':menuName,
		}
//		console.info('url:'+config.UpdateMenu_url+',params:'+JSON.stringify(menuObj));
		sendJsonRequest(config.updateMenu_url,'put',menuObj,false,function(data){
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 5000);
		});
	}
}

function updateChildMenu(){
	var menuName = $F('menu2');
	var url = $F('url');
	var highermenu = $F('highermenu');
	if(menuName==null || menuName==""){
		$('warn').style.color = 'red';
		return false;
	}else if(url==null || url==""){
		$('warn').style.color = 'red';
		return false;
	}else{
		$('warn').style.color = '';
		var menuObj = {
				'id':id,
				'menu':menuName,
				'url':url,
				'highermenu':highermenu
		}
//		console.info('url:'+config.addMenu_url+',params:'+JSON.stringify(menuObj));
		sendJsonRequest(config.updateMenu_url,'put',menuObj,false,function(data){
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 5000);
		});
	}
}

/**
 * 更改信息页面返回到菜单列表
 */
function goBack(){
	var url = config.listMenu_url;
	window.parent.document.getElementById("iframepage").src=url;
}