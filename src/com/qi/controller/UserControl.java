package com.qi.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qi.entity.BaseReturn;
import com.qi.model.Person;
import com.qi.model.Role;
import com.qi.model.UserMenuKey;
import com.qi.service.PersonService;
import com.qi.service.RoleService;
import com.qi.service.UserMenuKeyService;

@Controller
@RequestMapping("/user")
public class UserControl {
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PersonService personService;
	@Autowired
	private UserMenuKeyService userMenuKeyService;
	@Autowired
	private RoleService roleService;
	
	
	/**
	 * 获取全部用户信息显示到下拉列表页面，给用户分配菜单
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getUsers",method=RequestMethod.POST)
	public @ResponseBody BaseReturn getUsers(HttpSession session){
		BaseReturn baseReturn = new BaseReturn();
		try {
			List<Person> userInfos = personService.findAll();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取用户成功");
			baseReturn.addObject("userInfos", userInfos);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("获取用户失败");
		}
		logger.info(baseReturn);
		return baseReturn;
	}
	
	
	/**
	 * 按身份获取用户信息显示到下拉列表页面，给用户分配任务
	 * master只能选择给manager分配任务
	 * manager只能给其负责的子用户分配任务
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getUsersByRole",method=RequestMethod.POST)
	public @ResponseBody BaseReturn getUsersByRole(HttpSession session){
		BaseReturn baseReturn = new BaseReturn();
		try {
			Person loginPerson = (Person) session.getAttribute("person");
			List<Person> userInfos = personService.findByRole(loginPerson);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取用户成功");
			baseReturn.addObject("userInfos", userInfos);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("获取用户失败");
		}
		logger.info(baseReturn);
		return baseReturn;
	}
	
	
	/**
	 * 保存用户菜单关联信息到数据库
	 * @param userMenu
	 * @return
	 */
	@RequestMapping(value="/addUserMenu",method=RequestMethod.POST)
	public @ResponseBody BaseReturn addUserMenu(@RequestBody UserMenuKey userMenu){
		BaseReturn baseReturn = new BaseReturn();
		try {
			String msg = userMenuKeyService.addUserMenuInfo(userMenu);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统出错！");
		}
		
		return baseReturn;
	}
	
	/**
	 * html发送Ajax请求，获取session绑定的用户信息
	 */
	@RequestMapping(value="/getUserself",method=RequestMethod.GET)
	public @ResponseBody BaseReturn getUserself(HttpSession session){
		BaseReturn baseReturn = new BaseReturn();
		Person loginPerson = (Person) session.getAttribute("person");
		//获取登陆用户的角色
		Role personRole = roleService.findByUserId(loginPerson.getId());
		baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
		baseReturn.setResponseDesc("获取session");
		baseReturn.addObject("loginPerson", loginPerson);
		baseReturn.addObject("personRole", personRole);
		
		return baseReturn;
	}
}

