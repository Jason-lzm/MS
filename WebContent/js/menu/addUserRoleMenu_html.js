/**
 * 页面初始化方法
 */
window.onload = function() {
	$('userMenu').hidden = true;
	$('roleMenu').hidden = true;
	$('allMenu').hidden = true;
	$('sub_userMenu').hidden = true;
	$('sub_roleMenu').hidden = true;
	// 初始化下拉框
	initSelect();
};

function initSelect() {
	var selectObjcet = $('addType');
	// 清除之前的元素
	selectObjcet.options.length = 1;
	var data = config.addType;
//	console.info("加载添加菜单类型：" + data);
	for (i = 0; i < data.length; i++) {
		var o = new Option(data[i].text, data[i].value);
		selectObjcet.options[selectObjcet.options.length] = o;
	}
	
	// 监听改变选项的事件
	selectObjcet.onchange = selectType;
	selectType(); // 初始点击事件
}

function selectType() {
	var selectObjcet = $('addType');
	var addType = selectObjcet.options[selectObjcet.selectedIndex].value;
	if (addType === '1') { // 给用户添加菜单，生成给用户添加菜单的form
		addUsers(); // 给下拉框添加用户
		addMenus(); // 给下拉框添加菜单
		$('userMenu').hidden = false;
		$('allMenu').hidden = false;
		$('roleMenu').hidden = true;
		$('sub_userMenu').hidden = false;
		$('sub_roleMenu').hidden = true;
		
	} else if((addType === '2')){
		addRoles(); // 给下拉框添加角色
		addMenus(); // 给下拉框添加菜单
		$('roleMenu').hidden = false;
		$('allMenu').hidden = false;
		$('userMenu').hidden = true;
		$('sub_userMenu').hidden = true;
		$('sub_roleMenu').hidden = false;
	} else{
		$('userMenu').hidden = true;
		$('roleMenu').hidden = true;
		$('allMenu').hidden = true;
		$('sub_userMenu').hidden = true;
		$('sub_roleMenu').hidden = true;
	}
};

/**
 * 给下拉框添加用户
 * @returns
 */
function addUsers(){
	var userInfos;//从后台查找所有用户信息
	sendJsonRequest(config.getUsers_url,'POST',null,false,function(data){
//		console.info("获取用户响应："+data);
		map =  JSON.parse(data).map;
		userInfos =  map.userInfos;
	});
	
	//显示用户信息的下拉框
	var selectUsers = $('users');
	var selectOptions = selectUsers.options;//得到下拉框选项的数组
	selectOptions.length = 1;//默认选择只有一个
	for(i=0;i<userInfos.length;i++){
		var userInfo = userInfos[i];//迭代出每一个用户
		//设置选项内容为用户名，value值为用户id
		var optionUser = new Option(userInfo.name,userInfo.id);
		selectOptions[i+1] = optionUser;
	}
}
/**
 * 给下拉框添加菜单
 * @returns
 */
function addMenus(){
	var menuInfos;
	sendJsonRequest(config.getMenus_url,'POST',null,false,function(data){
//		console.info("获取菜单响应："+data);
		var map =  JSON.parse(data).map;
		menuInfos = map.menuInfos;
	});
	//显示菜单信息的下拉框
	var selectMenus = $('p_menus');
	var selectOptions = selectMenus.options;//得到下拉框选项的数组
	selectOptions.length = 1;//默认选择只有一个
	for(i=0;i<menuInfos.length;i++){
		var menuInfo = menuInfos[i];//迭代出每一个用户
		if(menuInfo.highermenu==null){
			//设置选项内容为用户名，value值为用户id
			var optionUser = new Option(menuInfo.menu,menuInfo.id);
			selectOptions[selectOptions.length] = optionUser;
		}
	}
	
}

function addChildMenus(){
		var menuInfos;
		sendJsonRequest(config.getMenus_url,'POST',null,false,function(data){
			var map =  JSON.parse(data).map;
			menuInfos = map.menuInfos;
		});
		//显示菜单信息的下拉框
		//得到父菜单下拉框选项的数组
		var selectParentMenus = $('p_menus');
		var selectParentOptions = selectParentMenus.options;
		//得到子下拉框选项的数组
		var selectChildMenus = $('c_menus');
		var selectChildOptions = selectChildMenus.options;
		
		selectChildOptions.length = 1;//默认选择只有一个
		
		//遍历父菜单选项
		for(m=0;m<menuInfos.length;m++){
			var menuInfo = menuInfos[m];
			if(menuInfo.highermenu!=null){
				//如果上级菜单选中的项的value值(也就是id)与子菜单的选项一样，则添加到子菜单选项
				if(selectParentMenus.value===menuInfo.highermenu){
					optionUser_c = new Option(menuInfo.menu,menuInfo.id);
					selectChildOptions[selectChildOptions.length] = optionUser_c;
				}
			}
		}
}
/**
 * 给下拉框添加角色
 * @returns
 */
function addRoles(){
	var roleInfos;
	sendJsonRequest(config.getRoles_url,'POST',null,false,function(data){
//		console.info("获取角色响应："+data);
		var map =  JSON.parse(data).map;
		roleInfos = map.roleInfos;
	});
	//显示角色信息的下拉框
	var selectRoles = $('roles');
	var selectOptions = selectRoles.options;//得到下拉框选项的数组
	selectOptions.length = 1;//默认选择只有一个
	for(i=0;i<roleInfos.length;i++){
		var roleInfo = roleInfos[i];//迭代出每一个用户
		//设置选项内容为用户名，value值为用户id
		var optionUser = new Option(roleInfo.roleName,roleInfo.roleName);
		selectOptions[i+1] = optionUser;
	}
}

/**
 * 提交用户，菜单分配信息
 * @returns {Boolean}
 */
function submitUserMenu(){
	if($F('users')==='none' || $F('p_menus')==='none' || $F('c_menus')==='none'){
		$('msg').innerHTML = "请选择项目！";
		return false;
	}else{
		var url = config.addUserMenu_url;
		var userMenu1 = {
				'userId':$F('users'),
				'menuId':$F('p_menus'),
		};
		var userMenu2 = {
				'userId':$F('users'),
				'menuId':$F('c_menus'),
		};
		
//		console.info("提交用户菜单信息："+JSON.stringify(userMenu));
		sendJsonRequest(url,'post',userMenu1,false,function(data){
			console.info("获取响应："+data);
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 30000);
		});
		sendJsonRequest(url,'post',userMenu2,false,function(data){
			console.info("获取响应："+data);
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 30000);
		});
	}
	
}

/**
 * 提交角色，菜单分配信息
 * @returns {Boolean}
 */
function submitRoleMenu(){
	if($F('roles')==='none' || $F('p_menus')==='none'|| $F('c_menus')==='none'){
		$('msg').innerHTML = "请选择项目！";
		return false;
	}else{
		var url = config.addRoleMenu_url;
		var roleMenu1 = {
				'roleName':$F('roles'),
				'menuId':$F('p_menus'),
		};
		var roleMenu2 = {
				'roleName':$F('roles'),
				'menuId':$F('c_menus'),
		};
		
//		console.info("提交用户菜单信息："+JSON.stringify(roleMenu));
		sendJsonRequest(url,'post',roleMenu1,false,function(data){
			console.info("获取响应："+data);
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 30000);
		});
		sendJsonRequest(url,'post',roleMenu2,false,function(data){
			console.info("获取响应："+data);
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 30000);
		});
	}
	
}
