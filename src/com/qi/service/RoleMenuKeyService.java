package com.qi.service;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qi.mapper.RoleMenuMapper;
import com.qi.model.RoleMenuKey;

@Service
@Transactional
public class RoleMenuKeyService {

	@Resource
	private RoleMenuMapper roleMenuMapper;
	
	public String addRoleMenuInfo(RoleMenuKey roleMenu) throws Exception{
		String msg = "";
		Map<String,String> map = new HashMap<String,String>();
		map.put("ROLE_NAME", roleMenu.getRoleName());
		map.put("MENU_ID", roleMenu.getMenuId());
		RoleMenuKey infos = roleMenuMapper.selectByKey(map);
		if(infos!=null){
			msg = "该角色已添加相同菜单！";
			return msg;
		}else{
			int n = roleMenuMapper.insert(roleMenu);
			if(n>0){
				msg = "添加成功！";
			}else{
				msg = "添加失败！";
			}
		}
		return msg;
	}
	
	public boolean findByKey(String roleName,String menuId){
		boolean flag = false;
		Map<String,String> map = new HashMap<String,String>();
		map.put("roleName", roleName);
		map.put("menuId", menuId);
		RoleMenuKey infos = roleMenuMapper.selectByKey(map);
		//如果同一角色，没有被关联同一菜单，返回true
		if(infos==null){
			flag=true;
		}
		return flag;
	}
}
