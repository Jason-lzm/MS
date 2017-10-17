package com.qi.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.qi.entity.BaseReturn;
import com.qi.model.RoleMenuKey;
import com.qi.service.RoleMenuKeyService;
import com.qi.service.RoleService;
import jxl.common.Logger;

@Controller
@RequestMapping("/role")
public class RoleControl {
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private RoleService roleservice;
	@Autowired
	private RoleMenuKeyService roleMenuKeyService;
	
	
	/**
	 * 获取用户身份角色信息显示到下拉列表页面，按照用户角色分配菜单
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getRoles",method=RequestMethod.POST)
	public @ResponseBody BaseReturn getRoles(HttpSession session){
		BaseReturn baseReturn = new BaseReturn();
		try {
			List<String> roleInfos = roleservice.findAllRole();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取角色成功");
			baseReturn.addObject("roleInfos", roleInfos);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("获取角色失败");
		}
		logger.info(baseReturn);
		return baseReturn;
	}
	
	/**
	 * 添加数据到-角色菜单关联表
	 * @param roleMenu
	 * @return
	 */
	@RequestMapping(value="/addRoleMenu",method=RequestMethod.POST)
	public @ResponseBody BaseReturn addRoleMenu(@RequestBody RoleMenuKey roleMenu){
		BaseReturn baseReturn = new BaseReturn();
		try {
			String msg = roleMenuKeyService.addRoleMenuInfo(roleMenu);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统出错！");
		}
		return baseReturn;
	}
	
	
}
