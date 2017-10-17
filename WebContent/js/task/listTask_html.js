/**
 * 页面初始化方法
 */
window.onload=function(){
	var map1 = getSession();
	var personRole = map1.personRole;
//	if(personRole.roleName!="master" && personRole.roleName!="manager"){
//		$('tfoot').hidden = true;
//	}
	
	var page = 1;
	var pageSize=8;
	var map = loadTask(page,pageSize);
	showPageInfo(map.pageInfos);
	showTaskHead(personRole);
	showTaskAll(map.taskInfos,personRole);
	
	
};

/**
 * 按指定页面查找页面
 * @param page
 */
function listTaskByPage(page){
	var totalpage = $('totalpage').innerHTML;
	if(page==null || page=="" || page<1 || totalpage==0){
		page=1; 
	}else if(totalpage>0 && page>totalpage){
		page=totalpage;
	}
	var pageSize=8;
	var map = loadTask(page,pageSize);
	showPageInfo(map.pageInfos);
	showTaskHead();
	showTaskAll(map.taskInfos);
	
};

/**
 * 分页查找数据
 * @param page
 * @param pageSize
 * @returns
 */
function loadTask(page,pageSize){
	var url = config.getTaskInfoByPage_url+"/"+page+"/"+pageSize;
	var map;
	sendJsonRequest(url,'get',null,false,function(data){
		map = JSON.parse(data).map;
	});
	return map;//返回值要拿到外面，作为整个函数的返回值
}


function showTaskHead(personRole){
	var table = $('table');
	var thead = $('thead');
	table.removeChild(table.getElementsByTagName('thead')[0]);
	var thead = document.createElement("thead");
		var tr = document.createElement("tr");
		
		var td1 = document.createElement("td");
		td1.innerHTML = '选项';
		var td2 = document.createElement("td");
		td2.innerHTML = '修改';
		var td3 = document.createElement("td");
		td3.innerHTML = '负责人';
		var td4 = document.createElement("td");
		td4.innerHTML = '开始时间';
		var td5 = document.createElement("td");
		td5.innerHTML = '结束时间';
		var td6 = document.createElement("td");
		td6.innerHTML = '明细';
		var td7 = document.createElement("td");
		td7.innerHTML = '物品';
		var td8 = document.createElement("td");
		td8.innerHTML = '数量';
		var td9 = document.createElement("td");
		td9.innerHTML = '状态';
		var td10 = document.createElement("td");
		td10.innerHTML = '备注';
		var td11 = document.createElement("td");
		td11.innerHTML = '领取任务';
		var td12 = document.createElement("td");
		td12.innerHTML = '完成状态';
		
		if(personRole.roleName==="master" || personRole.roleName==="manager"){
			tr.appendChild(td1);
			tr.appendChild(td2);
		}
		tr.appendChild(td3);
		tr.appendChild(td4);
		tr.appendChild(td5);
		tr.appendChild(td6);
		tr.appendChild(td7);
		tr.appendChild(td8);
		tr.appendChild(td9);
		tr.appendChild(td10);
		
		if(personRole.roleName!="master" && personRole.roleName!="manager"){
			tr.appendChild(td11);
			tr.appendChild(td12);
		}
		
		thead.appendChild(tr);
		table.appendChild(thead);
		
}




/**
 * 列出所有的菜单表数据
 * @param menus
 */
function showTaskAll(taskInfos,personRole){
	var table = $('table');
	var tbody = $('tbody');
	table.removeChild(table.getElementsByTagName('tbody')[0]);
	var tbody = document.createElement("tbody");
	for(i=0;i<taskInfos.length;i++){
		var taskInfo = taskInfos[i];
		var tr = document.createElement("tr");
		var td1 = document.createElement("td");
		td1.innerHTML = "<input type='checkbox' name='delete' value='" +taskInfo.id +"' />"
		var td2 = document.createElement("td");
		td2.innerHTML = "<input class='btn' type='button' value='" +taskInfo.id +"' onclick='updateTask(this.value);' />";
		var td3 = document.createElement("td");
		td3.innerHTML = taskInfo.userid;
		var td4 = document.createElement("td");
		td4.innerHTML = taskInfo.starttime;
		var td5 = document.createElement("td");
		td5.innerHTML = taskInfo.endtime;
		var td6 = document.createElement("td");
		td6.innerHTML = taskInfo.content;
		var td7 = document.createElement("td");
		td7.innerHTML = taskInfo.goods==""?"无":taskInfo.goods;
		var td8 = document.createElement("td");
		var amount = taskInfo.amount==null?"无":taskInfo.amount;
		td8.innerHTML = amount+taskInfo.unit;
		var td9 = document.createElement("td");
		td9.innerHTML = taskInfo.state=="1"?"未开始":taskInfo.state=="2"?"进行中":"已完成";
		var td10 = document.createElement("td");
		td10.innerHTML = taskInfo.memo==""?"无":taskInfo.memo;
		var td11 = document.createElement("td");
		td11.innerHTML = "<input class='btn' id='"+taskInfo.id+"' type='button' value='已领取' onclick='updateState1("+taskInfo.id+");' />";
		var td12 = document.createElement("td");
		var buttonId = taskInfo.id+parseInt("111");
		td12.innerHTML = "<input class='btn' id='"+buttonId+"' type='button' value='已完成' onclick='updateState2("+taskInfo.id+");' />";
		
		if(personRole.roleName==="master" || personRole.roleName==="manager"){
			tr.appendChild(td1);
			tr.appendChild(td2);
		}
		tr.appendChild(td3);
		tr.appendChild(td4);
		tr.appendChild(td5);
		tr.appendChild(td6);
		tr.appendChild(td7);
		tr.appendChild(td8);
		tr.appendChild(td9);
		tr.appendChild(td10);
		
		if(personRole.roleName!="master" && personRole.roleName!="manager"){
			tr.appendChild(td11);
			tr.appendChild(td12);
		}
		
		tbody.appendChild(tr);
		table.appendChild(tbody);
		
	}
}

/**
 * 显示分页查询数据
 * @param pages
 */
function showPageInfo(pages){
	$('page').value = pages.page;
	$('show_page').innerHTML=pages.page;
	$('totalpage').innerHTML=pages.totalpages;
	$('page_size').innerHTML=pages.pagesize;
}

function pre_page(){
	var page = $F('page');
	page = parseInt(page)-1;
	listTaskByPage(page);
}

function next_page(){
	var page = $F('page');
	page = parseInt(page)+1;
	listTaskByPage(page);
}


function deleteTask(){
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
		var url = config.deleteTask_url;
		sendAjax('DELETE',url,true,str,function(data){
			console.info(data);
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 5000);
		});
	}else{
		$('msg').innerHTML = "请选择要删除的项！";
	}
}


function updateState1(id){
	if($(id).style.color=='red'){
		return;
	}else{
		$(id).style.color='red';
		url = config.updateState1_url;
		sendJsonRequest(url,'put',id,false,function(data){
			console.info(JSON.parse(data));
			setTimeout(pagerefresh, 1000);
		});
	}
}

function updateState2(id){
	var buttonId = id+parseInt("111");
	if($(buttonId).style.color=='red'){
		return;
	}else{
		$(buttonId).style.color='red';
		url = config.updateState2_url;
		sendJsonRequest(url,'put',id,false,function(data){
			console.info(JSON.parse(data));
			setTimeout(pagerefresh, 1000);
		});
	}
}













