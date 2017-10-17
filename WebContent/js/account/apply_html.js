function addTotal(){
	var total = $('total');
	total.value = $F('amount')*$F('price');
}

/**
 * 添加任务请求
 */
function subApply() {
	$('warn').style.color = '';
	if ($F('goods').length==0 ||$F('usefor').length==0 ) {
		$('warn').style.color = 'red';
		return false;
	}else if(isNaN($F('price'))|| isNaN($F('amount'))){
		$('msg').innerHTML = "数量和价格输入格式不对！";
		return false;
	}else {

		var url1 = config.subApply_url;
		var url2 = config.subApplyAgain_url;
		var apply = {
				'username' : $F('username'),
				'goods' : $F('goods'),
				'amount' : $F('amount'),
				'unit' : $F('unit'),
				'price' : $F('price'),
				'total' : $F('total'),
				'usefor' : $F('usefor'),
				'askdate' : $F('askdate'),
				'state' : $F('state'),
				'memo' : $F('memo')
		};
		
		sendJsonRequest(url1,'post',apply,false,function(data){
			console.info(data);
			var state = JSON.parse(data).responseCode;
			var msg = JSON.parse(data).responseDesc;
			//信息返回成功
			if(state==="0000"){
				//且已经申请过
				if(msg==="yes"){
					//提示用户，如果确定继续申请，则重新发送请求，保存数据
					if(confirm('该物品已申请过，是否再次申请？')){
						sendJsonRequest(url2,'post',apply,false,function(data){
							console.info(data);
							$('msg').innerHTML = JSON.parse(data).responseDesc;
							setTimeout(pagerefresh, 5000);
						});
					}
				//未申请过，则再次发送请求，保存数据
				}else{
					sendJsonRequest(url2,'post',apply,false,function(data){
						console.info(data);
						$('msg').innerHTML = JSON.parse(data).responseDesc;
						setTimeout(pagerefresh, 5000);
					});
				}
			//出错信息
			}else{
				$('msg').innerHTML = msg;
			}
		});
		
	}
}

window.onload = function(){
	//默认操作时间为当前时间
	var askDate = $('askdate');
	askDate.value = formatDateTime(new Date());
	console.info(askDate.value);
	
	findLoginPerson();
	
}


/**
 * 默认申请人为登陆用户
 * @returns
 */
function findLoginPerson(){
	var loginPerson;
	sendJsonRequest(config.getUserself_url,'get',null,false,function(data){
		map =  JSON.parse(data).map;
		loginPerson =  map.loginPerson;
	});
	
	var userName = $('username');
	userName.value = loginPerson.name;
	
}

/**
 * 将new Date标准日期转换成指定格式
 * @param date
 * @returns {String}
 */
function formatDateTime(date) {  
    var y = date.getFullYear();  
    var m = date.getMonth() + 1;  
    m = m < 10 ? ('0' + m) : m;  
    var d = date.getDate();  
    d = d < 10 ? ('0' + d) : d;  
    var h = date.getHours();  
    var minute = date.getMinutes();  
    minute = minute < 10 ? ('0' + minute) : minute;  
    return y + '-' + m + '-' + d+' '+h+':'+minute;  
}; 

