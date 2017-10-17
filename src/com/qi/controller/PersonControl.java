package com.qi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qi.entity.PageInfo;
import com.qi.entity.PersonRoleView;
import com.qi.model.Person;
import com.qi.model.Role;
import com.qi.service.PersonService;
import com.qi.service.RoleService;

@Controller
@RequestMapping("/person")
public class PersonControl {
	Logger logger = Logger.getLogger(PersonControl.class);
	
	@Autowired
	private PersonService personService;
	@Autowired
	private RoleService roleService;
	
	/**
	 * 显示联系人信息，
	 * 如果是master，显示所有联系人信息
	 * 如果是manager，只显示由其负责的联系人信息
	 * @return
	 */
	@RequestMapping("/listPerson")
	public String listPerson(Model model,@RequestParam Integer page,HttpSession session ){
		//根据session获取当前登陆的用户，根据不同用户权限显示不同的表数据
		PageInfo pageInfo = new PageInfo();
		Person loginPerson = (Person) session.getAttribute("person");
		List<Person> persons  = personService.findPerson(loginPerson,pageInfo,page);
		//绑定查找的用户信息到model上，在jsp页面获取数据显示
		model.addAttribute("persons", persons);
		//绑定page参数到model，从jsp页面可以获取当前页面
		model.addAttribute("pageInfo", pageInfo);
		
		Map<Integer,String> personRole = new HashMap<Integer,String>(); 
		//找出每个用户对应的角色
		for(Person p:persons){
			Role r = roleService.findByUserId(p.getId());
			if(r!=null){
				personRole.put(p.getId(), r.getRoleName());
			}
		}
		model.addAttribute("personRole", personRole);
		
		return "listPerson";
	}
	
	
	/**
	 * 接收Ajax请求批量删除数据，返回结果到页面
	 * @param str
	 * @return
	 */
	@RequestMapping("/deletePerson")
	public @ResponseBody String deletePerson(@RequestParam String str,HttpSession session){
		String msg = "";
		try {
			Person loginPerson = (Person) session.getAttribute("person");
			msg = personService.deletePerson(str,loginPerson);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "删除失败！";
		}
		return msg;
	}
	
	/**
	 * 管理员请求修改用户信息，返回提交修改数据的页面，等待数据修改后提交
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/updatePerson")
	public String updatePerson(@RequestParam Integer id,Model model){
		try {
			Person person = personService.findById(id);
			Role role = roleService.findByUserId(id);
			if(role!=null){
				model.addAttribute("updatePersonRole", role);
			}
			model.addAttribute("updatePerson", person);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "update";
	}
	
	/**
	 * 检查用户修改信息，并发送邮件，保存流水
	 * @return
	 */
	@RequestMapping(value="/checkUpdate",method=RequestMethod.PUT)
	public @ResponseBody String checkUpdate(@RequestBody PersonRoleView personRoleView,HttpSession session){
		String msg = "";
		try {
			if(null!=personRoleView){
				Person person = personRoleView.getPerson();
				Role role = personRoleView.getRole();
				Person loginPerson = (Person) session.getAttribute("person");
				//保存用户信息
				msg = personService.updatePerson(loginPerson,person,role);
			}else{
				msg = "参数为空";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "用户信息修改失败，请稍后重试!";
		}
		//返回提示信息到ajax回调函数
		return msg;
	}
}
