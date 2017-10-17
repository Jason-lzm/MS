package com.qi.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qi.mapper.UserMenuMapper;
import com.qi.model.Role;
import com.qi.model.UserMenuKey;

@Service
@Transactional
public class UserMenuKeyService {

	@Resource
	private UserMenuMapper userMenuMapper;
	@Autowired
	private RoleService roleservice;
	@Autowired
	private RoleMenuKeyService roleMenuKeyService;
	
	public String addUserMenuInfo(UserMenuKey userMenu) throws Exception{
		String msg = "";
		//根据用户id找到用户角色，在角色菜单关联表查询，此类用户是否已经有了相同的菜单
		int userId = Integer.parseInt(userMenu.getUserId());
		Role role = roleservice.findByUserId(userId);
		boolean flag = roleMenuKeyService.findByKey
				(role.getRoleName(),userMenu.getMenuId());
		if(!flag){
			msg = "该用户的角色已添加相同菜单！";
			return msg;
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", userMenu.getUserId());
		map.put("menuId", userMenu.getMenuId());
		UserMenuKey infos = userMenuMapper.selectByKey(map);
		if(infos!=null){
			msg = "该用户已添加相同菜单！";
			return msg;
		}else{
			int n = userMenuMapper.insert(userMenu);
			if(n>0){
				msg = "添加成功！";
			}else{
				msg = "添加失败！";
			}
		}
		return msg;
	}
}
