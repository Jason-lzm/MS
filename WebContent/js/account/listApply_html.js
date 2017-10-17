/**
 * 页面初始化方法
 */
window.onload=function(){
	var page = 1;
	var pageSize=8;
	var map = loadTask(page,pageSize);
	showPageInfo(map.pageInfos);
	showApplyAll(map.applyInfos);
	
	
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
	showApplyAll(map.applyInfos);
	
};

/**
 * 分页查找数据
 * @param page
 * @param pageSize
 * @returns
 */
function loadTask(page,pageSize){
	var url = config.getApplyInfoByPage_url+"/"+page+"/"+pageSize;
	var map;
	sendJsonRequest(url,'get',null,false,function(data){
		map = JSON.parse(data).map;
	});
	return map;//返回值要拿到外面，作为整个函数的返回值
}




/**
 * 列出所有的菜单表数据
 * @param menus
 */
function showApplyAll(applyInfos){
	var table = $('table');
	var tbody = $('tbody');
	table.removeChild(table.getElementsByTagName('tbody')[0]);
	var tbody = document.createElement("tbody");
	for(i=0;i<applyInfos.length;i++){
		var applyInfo = applyInfos[i];
		var tr = document.createElement("tr");
		var td1 = document.createElement("td");
		td1.innerHTML = applyInfo.id;
		var td2 = document.createElement("td");
		td2.innerHTML = applyInfo.username;
		var td3 = document.createElement("td");
		td3.innerHTML = applyInfo.goods;
		var td4 = document.createElement("td");
		var amount = applyInfo.amount==null?"无":applyInfo.amount;
		td4.innerHTML = amount+applyInfo.unit;
		var td5 = document.createElement("td");
		td5.innerHTML = applyInfo.price;
		var td6 = document.createElement("td");
		td6.innerHTML = applyInfo.total;
		var td7 = document.createElement("td");
		td7.innerHTML = applyInfo.usefor;
		var td8 = document.createElement("td");
		td8.innerHTML = applyInfo.askdate;
		var td9 = document.createElement("td");
		td9.innerHTML = applyInfo.state=="1"?"申请中":applyInfo.state=="2"?"已批准":applyInfo.state=="3"?"已购买":"未批准";
		var td10 = document.createElement("td");
		td10.innerHTML = applyInfo.memo==""?"无":applyInfo.memo;
		var td11 = document.createElement("td");
		td11.innerHTML = "<select id='"+applyInfo.id+"' onchange='updateState("+applyInfo.id+");'>" +
							"<option value='1'>申请中</option>" +
							"<option value='2'>已批准</option>" +
							"<option value='3'>已购买</option>" +
							"<option value='4'>未批准</option>" +
						 "</select>";
		
		
		tr.appendChild(td1);
		tr.appendChild(td2);
		tr.appendChild(td3);
		tr.appendChild(td4);
		tr.appendChild(td5);
		tr.appendChild(td6);
		tr.appendChild(td7);
		tr.appendChild(td8);
		tr.appendChild(td9);
		tr.appendChild(td10);
		tr.appendChild(td11);
		
		tbody.appendChild(tr);
		table.appendChild(tbody);
		
		var options = $(applyInfo.id).options;
		for(j=0;j<options.length;j++){
			if(options[j].value===applyInfo.state){
				options[j].selected = true;
			}
		}
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



function updateState(id){
	var applyInfo = {
			'id':id,
			'state':$F('selectState')
	};
	url = config.updateState_url;
	sendJsonRequest(url,'put',applyInfo,false,function(data){
		console.info(JSON.parse(data));
		setTimeout(pagerefresh, 1000);
	});
}












