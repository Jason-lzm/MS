//用于缓存数据
var params = {};

/**
 * 关闭页面前发送请求到controller，关闭session
 */
function closePage() {
	window.onbeforeunload = function() {
		if (event.clientX > document.body.clientWidth && event.clientY < 0
				|| event.altKey) {
			// 判断关闭了浏览器，则发送请求到controller关闭session
			var xhr = getXmlHttpRequest();
			xhr.open("post", "http://localhost:8088/MS/visit/closePage", false);
			xhr.send();
		} else {
			// 刷新页面，不做处理
		}
	}
}

/**
 * 登陆成功，跳转到主页面，页面加载的时候发送请求到后台， 
 * 根据session绑定用户判断用户身份，显示不同菜单
 */
function listMainMenu() {
	closePage();

	var url=config.mainMenu_url;
	
	var xhr = getXmlHttpRequest();
	// application/json;charset=UTF-8
	xhr.open("get", url, false);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var txt = xhr.responseText;
				fillMenu(JSON.parse(txt));
			}
		}
	};
	xhr.send();
}

/**
 * 填充菜单数据
 * @param menu 
 */
function fillMenu(menu){
	for(i=0;i<menu.length;i++){
		mainMenu = menu[i];
		mainObj = createMainMenu(mainMenu,i);
		subMenu = mainMenu.childMenus;
		for(j=0;j<subMenu.length;j++){
			sub = subMenu[j];
			createSubMenu(sub,i,j);
		}
		mainObj.click();
	}
}


/**
 * 创建主菜单到id为 menu 的div上，前提是页面存在 menu 的div
 * @param mainMenu
 * @param i
 * @returns {___anonymous_div}
 */
function createMainMenu(mainMenu,i){
	div = document.createElement("li");
	div.id = 'mainMenu'+i;
	div.isOpen = false;
	div.onclick=function(){
		if(this.isOpen){ //是打开的，那么隐藏所有子菜单
			this.isOpen=false;
			openSubMenu(i);
		}else{ //是关闭的，那么打开显示所有子菜单
			this.isOpen=true;
			closeSubMenu(i);
		}
	};
	div.innerHTML="<a>"+mainMenu.parentMenuName+"</a>";
	try{
		$('menus').parentNode.appendChild(div);
		return div;
	}catch(e){
		alert("添加主菜单失败!请查看是否存在 menu 域");
	}
}

/**
 * 创建子菜单
 */
function createSubMenu(subMenu,i,j){
	div = document.createElement("li");
	div.id='subMenu'+i+j;
	div.hidden = false;
	div.onclick=function(){
		$('iframepage').src=subMenu.url;
	};
	div.style.backgroundColor = "#ADD8E6";
	div.innerHTML="<a>&nbsp;&nbsp;&nbsp;&nbsp;"+subMenu.menu+"</a>";
	$('menus').parentNode.appendChild(div);
}

/**
 * 打开id为 subMenu01 的子菜单
 * @param i
 */
function openSubMenu(i){
	for(m=0;;m++){
		id = "subMenu"+i+m;
		if($(id)){
			$(id).hidden=false;
		}else{
			return ;
		}
	}
}

/**
 * 关闭id为 subMenu01 的子菜单
 * @param i
 */
function closeSubMenu(i){
	for(m=0;;m++){
		id = "subMenu"+i+m;

		if($(id)){
			$(id).hidden=true;
		}else{
			return ;
		}
	}
}


