/**
 * 根据ID获取对象 document.getElementById(id)
 * @param id
 * @returns
 */
function $(id){
return document.getElementById(id);
}

/**
 * 根据id获取对象的值
 * @param id
 * @returns
 */
function $F(id){
	return $(id).value;
}

/**
 * 判断 obj 是否为空或者未定义或者内容为空，
 * @param obj
 * @returns {Boolean} 如果是空的返回 true,否则返回 false
 */
function isNull(obj){
	if(obj==null||(typeof obj)=='undefined'||obj.length===0) return true;
	return false;
}

/**
 * 判断2个值是否相等
 * @param obj
 * @param obj1
 * @returns {Boolean}
 */
function isEquals(obj,obj1){
	if(obj==obj1) return true;
	return false;
}

function checkIdno(obj){
	var reg = /^(\d{14}|\d{17})(x|X|\d)$/;
	return reg.test(obj);
}

/**
 * 设置当前页面刷新
 */
function pagerefresh() {
	//以下两种方法均可 
	//window.location.reload();
	location = location;
}


/**
 * 获取 ajax 对象
 */
function getXmlHttpRequest(){
	var xhr = null;
	if((typeof XMLHttpRequest)!='undefined'){
		xhr = new XMLHttpRequest();
	}else{
		xhr = ActiveXObject('Microsoft.XMLHttp');
	}
	return xhr;
}

/**
 * method 请求类型 默认是get <br/>
 * url 请求资源路径，get请求时后面携带参数 <br/>
 * async 是否同步 布尔类型，默认不同步 false <br/>
 * params post请求时，请求参数 <br/>
 * successHander 成功请求服务器之后返回数据给浏览器的处理函数 <br/>
 * errorHander 处理失败的函数 <br/>
 * waitHander 等待中的处理<br/>
 * author:guozl
 * date: 20160628
 */
function sendAjax(){
	var method="get";
	var url="";
	var async =false;
	var params="";
	var successHander = null;
	var errorHander = null;
	var waitHander = null;
	//初始化参数
	if(arguments[0]){
		method = arguments[0];
	}
	if(arguments[1]){
		url = encodeURI(arguments[1]);
	}
	if(arguments[2]){
		async = arguments[2];
	}
	if(arguments[3]){
		params = arguments[3];
	}
	if(arguments[4]){
		successHander = arguments[4];
	}
	if(arguments[5]){
		errorHander = arguments[5];
	}
	if(arguments[6]){
		waitHander = arguments[6];
	}
	
	var ajax = getXmlHttpRequest();
	ajax.open(method,url,async);
	ajax.onreadystatechange = function(){
		//step4 获取服务器返回的数据，更新页面
		if(ajax.readyState==4){
			//表示获取数据成功
			if(ajax.status==200){
				if(successHander){
					successHander(ajax.responseText);
				}
			}else{
				if(errorHander){
					errorHander();
				}
			}
		}else{
			waitHander();
		}
	};
	//get 请求
	if(method=='get'){
		ajax.send(null);
	}
	
	//post 请求
	if(method=='post'){
		ajax.setRequestHeader("content-Type","application/x-www-form-urlencoded")
		ajax.send(params);
	}
	ajax.setRequestHeader("content-Type","application/x-www-form-urlencoded")
	ajax.send(params);
	
}


/**
 * 判断是否是函数，是函数返回 true,else false
 * @returns {Boolean}
 */
 function isFunc(){
	if(arguments[0]&&typeof arguments[0] == 'function') return true;
	return false;
}
 
/**
 * 判断数据是否为空,是空返回true,否则 false
 */
 function isBlank(){
	var param = arguments[0];
	if(param){
		if(param.trim().length>0) return false;
	}
	return true;
}
	 
/**
 * 发送json请求
 * url：请求的资源路径
 * type：请求类型 POST/DELETE/GET/PUT
 * param:请求参数 json对象 {'name':'zs','age':22}
 * async：是否同步 true/false
 * successHandler：成功的回调函数
 * errorHandler：失败的回调函数
 * waitHandler：等待的回调函数
 */
function sendJsonRequest(){
	url = "";
	type = "get";//默认get
	param = "";
	async = false; //默认false
	successHandler  = null;
	errorHandler = null;
	waitHandler = null;
	
	if(!isBlank(arguments[0])){
		url = arguments[0];
	}
	if(!isBlank(arguments[1])){
		type = arguments[1];
	}
	if(arguments[2]){
		param = arguments[2];
	}
	if(!isBlank(arguments[3])){
		async = arguments[3];
	}
	if(isFunc(arguments[4])){
		successHandler  = arguments[4];
	}
	if(isFunc(arguments[5])){
		errorHandler = arguments[5];
	}
	if(isFunc(arguments[6])){
		waitHandler = arguments[6];
	}
	
	try{
		xhr = getXmlHttpRequest();
		xhr.open(type,url,async);
		xhr.onreadystatechange=function(){
			if(xhr.readyState == 4){
				if(xhr.status == 200){
					successHandler(xhr.responseText);
				}else{
					errorHandler('系统出错，请联系管理员！');
				}
			}else{
				waitHandler();
			}
		};
		
		xhr.setRequestHeader("Content-Type","application/json"); 
		console.info("请求资源路径："+url+"参数："+JSON.stringify(param));
		xhr.send(JSON.stringify(param));
	}catch(e){
		alert("调用ajax发送请求失败:"+e);
	}
}


/**
 * 设置反选
 */
function selectOther() {
	var checkbox = document.getElementsByName("delete");
	for (i = 0; i < checkbox.length; i++) {
		var e = checkbox[i];
		if (!e.checked) {
			e.checked = "checked";
		} else {
			e.checked = !e.checked;
		}
	}
}


/**
 * 设置全选
 */
function selectAll() {
	var checkbox = document.getElementsByName("delete");
	var a = 0;
	for (i = 0; i < checkbox.length; i++) {
		var e = checkbox[i];
		if (e.checked==true) {
			a++;
		} else {
			a--;
			e.checked = "checked";
		}
	}
	if (a == checkbox.length) {
		for (i = 0; i < checkbox.length; i++) {
			var e = checkbox[i];
			e.checked = !e.checked;
		}
	} 
}