/**
 * 添加任务请求
 */
function addTask() {
	$('warn').style.color = '';
	
	var starttime = parseInt($F('starttime').replace(/[-,T,:]/g,''));
	var endtime = parseInt($F('endtime').replace(/[-,T,:]/g,''));
	console.info(starttime);
	console.info(starttime-endtime);
	
	if ($F('userid') === "none"|| $F('content').length==0) {
		$('warn').style.color = 'red';
		return false;
	}else if($F('starttime').length==0 || $F('endtime').length==0){
		$('msg').innerHTML = "时间日期需填写完整！";
		return false;
	}else if(starttime-endtime>0){
		$('msg').innerHTML = "开始时间不能在结束时间之后！";
		return false;
	}else {
		var url = config.addTask_url;
		var task = {
				'userid' : $F('userid'),
				'starttime' : $F('starttime'),
				'endtime' : $F('endtime'),
				'content' : $F('content'),
				'goods' : $F('goods'),
				'amount' : $F('amount'),
				'unit' : $F('unit'),
				'state' : $F('state'),
				'memo' : $F('memo')
		};
		
		sendJsonRequest(url,'post',task,false,function(data){
			console.info(data);
			$('msg').innerHTML = JSON.parse(data).responseDesc;
			setTimeout(pagerefresh, 5000);
		});
	}
}

window.onload = function(){
	selectUserInfo();
	findAvailableGoods();
}


/**
 * 查找子用户，给子用户分配任务
 * @returns
 */
function selectUserInfo(){
	var userInfos;//从后台查找所有子用户信息
	sendJsonRequest(config.getUsersByRole_url,'POST',null,false,function(data){
//		console.info("获取用户响应："+data);
		map =  JSON.parse(data).map;
		userInfos =  map.userInfos;
	});
	
	//显示用户信息的下拉框
	var selectUsers = $('userid');
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
 * 申请采购的物料获得批准后可以从下拉列表查出，然后安排人员购买或使用
 */

/**
 * 分配任务时可领取已经购买过的物品
 */
function findAvailableGoods(){
	var goodsInfos;
	url = config.getGoodsInfos_url;
	sendJsonRequest(url,'get',null,false,function(data){
		console.info(data);
		map = JSON.parse(data).map;
		goodsInfos = map.goodsInfos;
	});
	
	//显示物品信息的下拉框
	var selectGoods = $('goods');
	var selectOptions = selectGoods.options;//得到下拉框选项的数组
	selectOptions.length = 1;//默认选择只有一个
	for(i=0;i<goodsInfos.length;i++){
		var goodsInfo = goodsInfos[i];//迭代出每一个用户
		//设置选项内容为用户名，value值为用户id
		var optionGoods = new Option(goodsInfo.goods,goodsInfo.id);
		selectOptions[i+1] = optionGoods;
	}
	
}

/**
 * 根据选择的物品，显示其总数和单位，当用户输入的总数大于默认值时，显示为默认值
 */
function selectGoodsDetail(){
	//将所选物品的value值，也就是品名传递到后台，查找到对应的所有信息
	//注意，物品状态必须是3-购买完成
	var goodsValue = $F('goods');//物品value值就是id值
	var total_amount = $('total_amount');
	var amount = $('amount');
	var unit = $('unit');
	
	if(goodsValue!=""){
		url = config.getGoodsDetail_url+"?id="+goodsValue;
		var applyInfo;
		
		sendJsonRequest(url,'get',null,false,function(data){
			console.info(data);
			map = JSON.parse(data).map;
			applyInfo = map.applyInfo;
		});
		
		total_amount.value = applyInfo.amount;
		amount.value = applyInfo.amount;
		unit.value = applyInfo.unit;
	}else{
		total_amount.value = null;
		amount.value = null;
		unit.value = "";
	}
}

/**
 * 检查输入的数量，大于总数，则默认为总数，小于0，则默认为1
 */
function checkAmount(){
	var amount = $('amount');
	var total_amount = $('total_amount');
	if(isNaN(amount.value)){
		$('msg').innerHTML = "物品数量格式不正确！";
		return false;
	}
	if(total_amount.value==""){
		amount.value = null;
	}else{
		if(parseInt($F('amount'))>parseInt($F('total_amount'))){
			amount.value = $F('total_amount');
		}else if(parseInt($F('amount'))<=0){
			amount.value = 1;
		}
	}
	
}