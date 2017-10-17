package com.qi.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qi.dubbo.service.HelloDubbo;
import com.qi.entity.BaseReturn;
import com.qi.entity.MenusEntity;
import com.qi.entity.PageInfos;
import com.qi.model.MenuInfo;
import com.qi.model.Person;
import com.qi.service.MenuInfoService;

@Controller
@RequestMapping("/menu")
public class MenuControl {
	
	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private MenuInfoService menuInfoService;
	
	
	/**
	 * 显示主页菜单
	 * @param session
	 * @return
	 */
	@RequestMapping("/mainMenus")
	public @ResponseBody List<MenusEntity> listMainMenus(HttpSession session){
		logger.info("用户登陆，获取菜单");
		Person loginPerson = (Person) session.getAttribute("person");
		List<MenusEntity> menus  = menuInfoService.findMenu(loginPerson);
		return menus;
	}
	
	/**
	 * 添加菜单信息
	 * @param menu
	 * @param session
	 * @return
	 */
	@RequestMapping("/addMenu")
	public @ResponseBody BaseReturn addMenu(@RequestBody MenuInfo menu,HttpSession session){
		logger.info("添加菜单"+menu);
		BaseReturn baseReturn = new BaseReturn();
		try {
			Person loginPerson = (Person) session.getAttribute("person");
			String msg = menuInfoService.addMenu(menu,loginPerson);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统错误，请联系管理员");
		}
		
		return baseReturn;
	}
	
	
	/**
	 * 分页查看所有菜单
	 * @param pageInfo
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/listMenuTable",method=RequestMethod.PUT)
//	@RequestMapping("/listMenus")
	public @ResponseBody BaseReturn getMenus(@RequestBody PageInfos pageInfo,HttpSession session){
		logger.info("获取菜单输入参数："+pageInfo);
		BaseReturn baseReturn = new BaseReturn();
		try {
			int totalpages = menuInfoService.getTotalPage(pageInfo);
			pageInfo.setTotalpages(totalpages);
			baseReturn.addObject("pageInfos",pageInfo);
			List<MenuInfo> menus = menuInfoService.showMenuByPage(pageInfo);
			baseReturn.addObject("menus", menus);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取菜单成功");
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("获取菜单失败");
		}
		return baseReturn;
		
	}
	
	/**
	 * 获取所有菜单信息，显示在下拉框页面
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getMenusAll",method=RequestMethod.POST)
	public @ResponseBody BaseReturn getMenusAll(HttpSession session){
		BaseReturn baseReturn = new BaseReturn();
		try {
			List<MenuInfo> menuInfos = menuInfoService.findAll();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取菜单成功");
			baseReturn.addObject("menuInfos", menuInfos);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取菜单成功");
		}
		
		logger.info(baseReturn);
		return baseReturn;
	}
	
	
	/**
	 * 获取所有父菜单信息，当添加子菜单时，显示在上级菜单下拉框页面，供用户选择
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/parentAll",method=RequestMethod.POST)
	public @ResponseBody BaseReturn getParentAll(HttpSession session){
		BaseReturn baseReturn = new BaseReturn();
		try {
			List<MenuInfo> menuInfos = menuInfoService.findParentAll();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取菜单成功");
			baseReturn.addObject("menuInfos", menuInfos);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取菜单成功");
		}
		
		logger.info(baseReturn);
		return baseReturn;
	}
	
	/**
	 * 批量删除菜单
	 * @param str
	 * @return
	 */
	@RequestMapping(value="/deleteMenu",method=RequestMethod.DELETE)
	public @ResponseBody BaseReturn deleteMenu(@RequestBody String str){
		BaseReturn baseReturn = new BaseReturn();
		String msg;
		try {
			msg = menuInfoService.deleteMenus(str);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("删除菜单出错！");
		}
		logger.info(baseReturn);
		return baseReturn;
	}

	/**
	 * 获取数据，显示到更新菜单页面
	 * @param str
	 * @return
	 */
	@RequestMapping(value="/getMenuInfoById",method=RequestMethod.GET)
	public @ResponseBody BaseReturn deleteMenu(@RequestParam Integer id){
		BaseReturn baseReturn = new BaseReturn();
		try {
			MenuInfo menuInfoById = menuInfoService.findMenuById(id);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("根据id获取数据成功");
			baseReturn.addObject("menuInfoById", menuInfoById);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("根据id获取数据出错");
		}
		return baseReturn;
	}
	
	/**
	 * 更新菜单信息
	 * @param menu
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/updateMenu",method=RequestMethod.PUT)
	public @ResponseBody BaseReturn updateMenu(@RequestBody MenuInfo menu){
		logger.info("更新菜单"+menu);
		BaseReturn baseReturn = new BaseReturn();
		try {
			String msg = menuInfoService.updateMenu(menu);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("更新数据出错");
		}
		
		return baseReturn;
	}
	
}
