window.onload = function(){
	$('selectByChild').hidden = true;
	$('sub_child').hidden = true;
	
	$('menu_type').onchange=function(){
		if($F('menu_type')==='1'){
		selectMenus();
			$('selectByChild').hidden = false;
			$('sub_child').hidden = false;
			$('selectByParent').hidden = true;
			$('sub_parent').hidden = true;
		}else{
			$('selectByChild').hidden = true;
			$('sub_child').hidden = true;
			$('selectByParent').hidden = false;
			$('sub_parent').hidden = false;
		}
		
	}
}

function selectMenus(){
	var menuInfos;
	sendJsonRequest(config.getParentMenus_url,'POST',null,false,function(data){
		var map =  JSON.parse(data).map;
		menuInfos = map.menuInfos;
	});
	//显示菜单信息的下拉框
	var selectMenus = $('highermenu');
	var selectOptions = selectMenus.options;//得到下拉框选项的数组
	selectOptions.length = 1;//默认选择只有一个
	for(i=0;i<menuInfos.length;i++){
		var menuInfo = menuInfos[i];//迭代出每一个用户
		//设置选项内容为用户名，value值为用户id
		var optionUser = new Option(menuInfo.menu,menuInfo.id);
		selectOptions[i+1] = optionUser;
	}
}

function addParentMenu(){
	var menuName = $F('menu1');
	if(menuName==null || menuName==""){
		$('warn').style.color = 'red';
		return false;
	}else{
		$('warn').style.color = '';
		var menuObj = {
				'menu':menuName,
		}
		console.info('url:'+config.addMenu_url+',params:'+JSON.stringify(menuObj));
		sendJsonRequest(config.addMenu_url,'POST',menuObj,false,function(data){
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 30000);
		});
	}
}

function addChildMenu(){
	var menuName = $F('menu2');
	var url = $F('url');
	var highermenu = $F('highermenu');
	if(menuName==null || menuName==""){
		$('warn').style.color = 'red';
		return false;
	}else if(url==null || url==""){
		$('warn').style.color = 'red';
		return false;
	}else if(highermenu==="none"){
		$('warn').style.color = 'red';
		return false;
	}else{
		$('warn').style.color = '';
		var menuObj = {
				'menu':menuName,
				'url':url,
				'highermenu':highermenu
		}
		console.info('url:'+config.addMenu_url+',params:'+JSON.stringify(menuObj));
		sendJsonRequest(config.addMenu_url,'POST',menuObj,false,function(data){
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 30000);
		});
	}
}