package com.qi.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qi.mapper.RoleMapper;
import com.qi.model.Person;
import com.qi.model.Role;

@Service
@Transactional
public class RoleService {

	@Resource
	private RoleMapper roleMapper;
	
	/**
	 * 查找全部不重复的角色名
	 * @return
	 * @throws Exception
	 */
	public List<String> findAllRole() throws Exception{
		List<String> roleInfos = roleMapper.selectAllRole();
		return roleInfos;
	}
	
	/**
	 * 保存用户信息的同时，默认保存角色表
	 * @param person
	 * @throws Exception
	 */
	public int saveRole(Person person,Person loginPerson) throws Exception{
		Role role = new Role();
		role.setUserId(person.getId());
		//如果添加用户的是master，则用户角色只能是manager
		if(loginPerson.getId()==1){//id=1是初始化赋予超级管理员master的角色
			role.setRoleName("manager");
		}else{
			role.setRoleName("customer");
		}
		int n = roleMapper.insertSelective(role);
		return n;
		
	}
	
	/**
	 * 根据用户id查找角色
	 * @param userId
	 * @return
	 */
	public Role findByUserId(int userId){
		Role role = roleMapper.selectByUserId(userId);
		return role;
	}
	
	
	/**
	 * 更新用户提交的角色信息
	 * @param userId
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	public int updateRole(int userId,String roleName) throws Exception{
		int i = 0;
		Role role = roleMapper.selectByUserId(userId);
		//如果用户没有保存角色，则直接保存角色
		if(role==null){
			role = new Role();
			role.setRoleName(roleName);
			role.setUserId(userId);
			i = roleMapper.insertSelective(role);
		//如果用户已有角色，则更新角色
		}else{
			role.setRoleName(roleName);
			i = roleMapper.updateByPrimaryKeySelective(role);
		}
		return i;
	}

	public void deleteRoles(int userId) throws Exception{
		Role role = roleMapper.selectByUserId(userId);
		//如果用户拥有角色，则一并删除
		if(role!=null){ 
		roleMapper.deleteByPrimaryKey(role.getId());
		}
	}
}
