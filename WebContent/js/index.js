function sendRequest(){
	$('result').innerHTML='';
	sendAjax($F('url'),$F('type'),$F('param'),false,function(data){
		$('result').innerHTML=data;
	},function(data){
		$('result').innerHTML=data;
	});
}

function sendJson(){
	var item = $F('param1');
	item = {
			"name":'zs',
			"age":22
	};
	
	$('result1').innerHTML='';
	sendJsonRequest($F('url1'),$F('type1'),item,false,function(data){
		var json = JSON.parse(data);
		$('result1').innerHTML="姓名："+json.name+",年龄："+json.age;
//		$('result1').innerHTML=data;
	},function(data){
		$('result1').innerHTML=data;
	});
}
