/**
 * 页面初始化方法
 */
window.onload=function(){
	var page = 1;
	var pageSize=8;
	var map = loadMenu(page,null,pageSize);
	showPage(map.pageInfos);
	showMenu(map.menus);
	
};

/**
 * 按指定页面查找页面
 * @param page
 */
function listMenus(page){
	var totalpage = $('totalpage').innerHTML;
	if(page==null || page=="" || page<1 || totalpage==0){
		page=1; 
	}else if(totalpage>0 && page>totalpage){
		page=totalpage;
	}
	var pageSize=8;
	var map = loadMenu(page,null,pageSize);
	showPage(map.pageInfos);
	showMenu(map.menus);
	
};


function loadMenu(page,totalPage,pageSize){
	var url = config.listMenuTable_url;
	var page = {
			'page':page,
			'totalpages':totalPage,
			'pagesize':pageSize
	}
	var map;
	sendJsonRequest(url,'put',page,false,function(data){
		map = JSON.parse(data).map;
	});
	return map;//返回值要拿到外面，作为整个函数的返回值
}

/**
 * 列出所有的菜单表数据
 * @param menus
 */
function showMenu(menus){
	var table = $('table');
	var tbody = $('tbody');
	table.removeChild(table.getElementsByTagName('tbody')[0]);
	var tbody = document.createElement("tbody");
	for(i=0;i<menus.length;i++){
		var menu = menus[i];
		var tr = document.createElement("tr");
		var td1 = document.createElement("td");
		td1.innerHTML = "<input type='checkbox' name='delete' value='" +menu.id +"' />"
		var td2 = document.createElement("td");
		td2.innerHTML = menu.menu;
		var td3 = document.createElement("td");
		td3.innerHTML = menu.url;
		var td4 = document.createElement("td");
		td4.innerHTML = menu.highermenu;
		var td5 = document.createElement("td");
		td5.innerHTML = "<input class='btn' type='button' value='" +menu.id +"' onclick='askUpdateMenu(this.value);'></a>";
		
		tr.appendChild(td1);
		tr.appendChild(td5);
		tr.appendChild(td2);
		tr.appendChild(td3);
		tr.appendChild(td4);
		
		tbody.appendChild(tr);
		table.appendChild(tbody);
		
	}
}

/**
 * 显示分页查询数据
 * @param pages
 */
function showPage(pages){
	$('page').value = pages.page;
	$('show_page').innerHTML=pages.page;
	$('totalpage').innerHTML=pages.totalpages;
	$('page_size').innerHTML=pages.pagesize;
}

function pre_page(){
	var page = $F('page');
	page = parseInt(page)-1;
	listMenus(page);
}

function next_page(){
	var page = $F('page');
	page = parseInt(page)+1;
	listMenus(page);
}


function deleteMenu(){
	var checkbox = document.getElementsByName("delete");
	var str = "";
	for(i=0;i<checkbox.length;i++){
		var e = checkbox[i];
		if(e.checked){
			str += e.value+",";
		}
	}
	if(str.length>1){
		str = str.substring(0,str.length-1);
		console.info("删除选项："+str);
		var url = config.deleteMenu_url;
		sendAjax('DELETE',url,true,str,function(data){
//			console.info(data);
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 5000);
		});
	}else{
		$('msg').innerHTML = "请选择要删除的项！";
	}
}

function askUpdateMenu(id){
	parent.params={};
	parent.params.id=id;
//	console.info(parent.params.id);
	window.location.href = config.askUpdateMenu_url;
	
}