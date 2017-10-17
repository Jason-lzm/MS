var host = {
		ip:'localhost',
		port:8088
}

var config = {
		//登陆页面
		mslogin_url:'http://'+host.ip+':'+host.port+'/MS/html/login.html',
		//注册页面
		msRegist_url:'http://'+host.ip+':'+host.port+'/MS/html/regist.html',
		//主页面
		msMain_url:'http://'+host.ip+':'+host.port+'/MS/html/main.html',
		//更新菜单页面
		askUpdateMenu_url:'http://'+host.ip+':'+host.port+'/MS/html/menu/updateMenu.html',
		//菜单列表显示页面
		listMenu_url:'http://'+host.ip+':'+host.port+'/MS/html/menu/listMenu.html',
		
		//判断用户登陆信息
		login_url:'http://'+host.ip+':'+host.port+'/MS/visit/login',
		//判断用户注册信息
		regist_url:'http://'+host.ip+':'+host.port+'/MS/visit/register',
		//获取全部用户
		getUsers_url:'http://'+host.ip+':'+host.port+'/MS/user/getUsers',
		//获取子用户
		getUsersByRole_url:'http://'+host.ip+':'+host.port+'/MS/user/getUsersByRole',
		//获取登陆用户自己
		getUserself_url:'http://'+host.ip+':'+host.port+'/MS/user/getUserself',
		
		
		
		//登陆主页显示不同菜单列表
		mainMenu_url:'http://'+host.ip+':'+host.port+'/MS/menu/mainMenus',
		//添加菜单提交url
		addMenu_url:'http://'+host.ip+':'+host.port+'/MS/menu/addMenu',
		//分页查找菜单
		listMenuTable_url:'http://'+host.ip+':'+host.port+'/MS/menu/listMenuTable',
		//获取所有菜单
		getMenus_url:'http://'+host.ip+':'+host.port+'/MS/menu/getMenusAll',
		//获取主菜单
		getParentMenus_url:'http://'+host.ip+':'+host.port+'/MS/menu/parentAll',
		//删除菜单
		deleteMenu_url:'http://'+host.ip+':'+host.port+'/MS/menu/deleteMenu',
		//根据id查找菜单
		getMenuInfoById_url:'http://'+host.ip+':'+host.port+'/MS/menu/getMenuInfoById',
		//更新菜单
		updateMenu_url:'http://'+host.ip+':'+host.port+'/MS/menu/updateMenu',
		
		
		
		//获取角色
		getRoles_url:'http://'+host.ip+':'+host.port+'/MS/role/getRoles',
		
		
		
		//添加权限类型 
		addType:[
		         {'text':'给用户添加菜单','value':'1'},
		         {'text':'给角色添加菜单','value':'2'}
		         ],
		//给用户分配菜单提交数据到后台URL
		addUserMenu_url:'http://'+host.ip+':'+host.port+'/MS/user/addUserMenu',
 		//给角色分配菜单提交数据到后台URL
		addRoleMenu_url:'http://'+host.ip+':'+host.port+'/MS/role/addRoleMenu',
		
		
		
		//新增任务
		addTask_url:'http://'+host.ip+':'+host.port+'/MS/task/askAddTask',
		//分页查询任务
		getTaskInfoByPage_url:'http://'+host.ip+':'+host.port+'/MS/task/getTaskInfoByPage',
		//删除任务
		deleteTask_url:'http://'+host.ip+':'+host.port+'/MS/task/deleteTask',
		//更改任务状态
		updateState1_url:'http://'+host.ip+':'+host.port+'/MS/task/updateState1',
		updateState2_url:'http://'+host.ip+':'+host.port+'/MS/task/updateState2',
		
		
		
		
		//检查提交物料申请的信息
		subApply_url:'http://'+host.ip+':'+host.port+'/MS/account/subApplyInfo',
		//请求保存申请信息
		subApplyAgain_url:'http://'+host.ip+':'+host.port+'/MS/account/subApplyAgain',
		//获取已购买的物品
		getGoodsInfos_url:'http://'+host.ip+':'+host.port+'/MS/account/getGoodsInfos',
		//获取已购买的物品的数量和单位
		getGoodsDetail_url:'http://'+host.ip+':'+host.port+'/MS/account/getGoodsDetail',
		//分页查询申请信息
		getApplyInfoByPage_url:'http://'+host.ip+':'+host.port+'/MS/account/getApplyInfoByPage',
		//更改任务状态
		updateState_url:'http://'+host.ip+':'+host.port+'/MS/account/updateState',
		
}
