package com.qi.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.qi.model.Person;
import com.qi.service.PersonService;

@Controller
@RequestMapping("/visit")
public class LoginControl {

	@Autowired
	private PersonService personService;
	
	/**
	 * 根据用户登陆信息判断，用户名和密码正确才能登陆，否则提示错误
	 * 登陆成功绑定用户信息到session
	 * @param name
	 * @param passWord
	 */
	@RequestMapping("/login")
	public @ResponseBody String checkLogin(@RequestParam("name") String name,
			@RequestParam("password") String password,HttpSession session){
		String msg = "";
		try {
			msg = personService.checkOnline(name, password);
			if(msg.equals("ok")){
				//登陆成功，绑定用户信息到session
				Person person = personService.findByName(name);
				session.setAttribute("person", person);
				msg = "success"; 
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "登陆出错！请稍后重试";
		}
		//返回提示信息到ajax回调函数
		return msg;
	}
	
	/**
	 * 检查用户注册信息，如果已经注册，提示用户
	 * @return
	 */
	@RequestMapping(value="/register",method=RequestMethod.PUT)
	public @ResponseBody String checkRegist(@RequestBody Person person,HttpSession session){
		String msg = "";
		try {
			Person loginPerson = (Person) session.getAttribute("person");
			//保存用户信息
			msg = personService.savePerson(person, loginPerson);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统出错，请稍后重试!";
		}
		//返回提示信息到ajax回调函数
		return msg;
	}
	
	/**
	 * 退出系统或重新登陆，服务器立刻关闭session，
	 * 监听器将会执行，然后返回此方法，重定向到登陆页面
	 * @param session
	 */
	@RequestMapping("/logout")
	public String logOut(HttpSession session){
		session.invalidate();
		return "redirect:http://localhost:8088/MS/html/login.html";
	}
	
	/**
	 * 关闭页面或浏览器时立刻关闭session
	 * @param session
	 */
	@RequestMapping("/closePage")
	public void closePage(HttpSession session){
		session.invalidate();
	}
	
	
	
}
