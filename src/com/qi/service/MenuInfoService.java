package com.qi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qi.entity.MenusEntity;
import com.qi.entity.PageInfos;
import com.qi.mapper.MenuInfoMapper;
import com.qi.mapper.RoleMapper;
import com.qi.mapper.RoleMenuMapper;
import com.qi.mapper.UserMenuMapper;
import com.qi.model.MenuInfo;
import com.qi.model.Person;
import com.qi.model.Role;
import com.qi.model.RoleMenuKey;
import com.qi.model.UserMenuKey;
import com.qi.utils.Constant;

@Service
@Transactional
public class MenuInfoService {
	
	@Resource
	private MenuInfoMapper menuInfoMapper;
	@Resource
	private ProcessService processService;
	@Resource
	private RoleMenuMapper roleMenuMapper;
	@Resource
	private UserMenuMapper userMenuMapper;
	@Resource
	private RoleMapper roleMapper;

	
	/**
	 * 根据用户权限删选出不同的菜单，按顺序封装到集合里
	 * @param loginPerson
	 * @return
	 */
	public List<MenusEntity> findMenu(Person loginPerson){
		//封装父菜单名，和子菜单集合的实体类集合
		List<MenusEntity> list = new ArrayList<MenusEntity>();
		//所有父菜单集合
		List<MenuInfo> parentMenu = new ArrayList<MenuInfo>();
		//所有子菜单集合
		List<MenuInfo> childMenu = new ArrayList<MenuInfo>();
		
		//根据用户的id在角色表中找到角色名
		Role role = roleMapper.selectByUserId(loginPerson.getId());
		String roleName = role.getRoleName();
		//根据角色名在角色菜单关联表中找到对应的所有菜单名
		List<RoleMenuKey> roleMenus = roleMenuMapper.selectByRoleName(roleName);
		
		//根据用户id在用户菜单关联表中找到所有对应的菜单名
		String userId = loginPerson.getId().toString();
		List<UserMenuKey> userMenus = userMenuMapper.selectByUserId(userId);
		
		//遍历角色菜单，找出用户拥有的所有的菜单id
		for(RoleMenuKey roleMenu:roleMenus){
			int menuId = Integer.parseInt(roleMenu.getMenuId());
			//根据菜单id查找出该用户所拥有的所有菜单信息
			MenuInfo menuInfo = menuInfoMapper.selectByPrimaryKey(menuId);
			//如果该菜单上级菜单为空，则为主菜单，添加到父菜单集合里
			if(menuInfo.getHighermenu()==null){
				parentMenu.add(menuInfo);
			//反之，添加到子菜单集合里
			}else{
				childMenu.add(menuInfo);
			}
		}
		//遍历用户菜单，找出用户拥有的所有的菜单id
		for(UserMenuKey userMenu:userMenus ){
			int menuId = Integer.parseInt(userMenu.getMenuId());
			MenuInfo menuInfo = menuInfoMapper.selectByPrimaryKey(menuId);
			if(menuInfo.getHighermenu()==null){
				//如果角色菜单没有关联此菜单，则添加到父菜单集合里
				if(!parentMenu.contains(menuInfo)){
					parentMenu.add(menuInfo);
				}
			//反之，添加到子菜单集合里
			}else{
				if(!childMenu.contains(menuInfo)){
					childMenu.add(menuInfo);
				}
			}
		}
		
		

		//遍历父菜单，设置menus对象的父菜单属性为菜单名
		for(MenuInfo m1:parentMenu){
			MenusEntity parents = new MenusEntity();
			parents.setParentMenuName(m1.getMenu());
			//设置menus对象的子菜单集合为该父菜单从属子菜单的集合
			List<MenuInfo> childs = new ArrayList<MenuInfo>();
			for(MenuInfo m2:childMenu){
				if(m1.getId()==Integer.parseInt(m2.getHighermenu())){
					childs.add(m2);
				}
			}
			parents.setChildMenus(childs);
			list.add(parents);
		}
		return list;
	}
	
	/**
	 * master可以添加菜单，菜单名不能重复
	 * @param menu
	 * @param loginPerson
	 * @return
	 */
	public String addMenu(MenuInfo menu, Person loginPerson){
		String msg;
		//保存菜单
		MenuInfo m = menuInfoMapper.selectByName(menu.getMenu());
		if(m!=null){
			msg = "菜单名已存在！";
			return msg;
		}else{
			try {
				menuInfoMapper.insertSelective(menu);
				try {
					processService.saveProcess(loginPerson.getId(),null,Constant.OPERATOR_TYPE_4);
					msg = "添加菜单，并保存流水成功！";
				} catch (Exception e) {
					msg = "添加菜单，保存流水失败！";
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "添加菜单失败！";
			}
			return msg;
		}
		
	}
	
	/**
	 * 获取菜单总个数
	 * @param pageInfo
	 * @return
	 */
	public int getTotalPage(PageInfos pageInfo) {
		int totalTableCount = menuInfoMapper.selectTableCount();
		int totalpage=0;
		int mode=totalTableCount%pageInfo.getPagesize();  
		if(mode==0){
			totalpage=totalTableCount/pageInfo.getPagesize();
		}else{
			totalpage=totalTableCount/pageInfo.getPagesize()+1;
		}
		return totalpage;
	}
	
	/**
	 * 分页查询菜单表
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<MenuInfo> showMenuByPage(PageInfos pageInfo) throws Exception{
		Map<String,Integer> map = new HashMap<String,Integer>();
		Integer offset = pageInfo.getPagesize()*(pageInfo.getPage()-1);
		map.put("offset", offset);
		map.put("pagesize", pageInfo.getPagesize());
		List<MenuInfo> menus = menuInfoMapper.selectByPage(map);
		//遍历查询结果，将保存上级菜单的id，设置为名字显示到页面
		for(MenuInfo menu:menus){
			if(menu.getHighermenu()!=null){
				int m_id = Integer.parseInt(menu.getHighermenu());
				MenuInfo m = menuInfoMapper.selectByPrimaryKey(m_id);
				menu.setHighermenu(m.getMenu());
			}
		}
		return menus;
	}

	/**
	 * 查找全部父菜单信息
	 * @return
	 * @throws Exception
	 */
	public List<MenuInfo> findParentAll() throws Exception{
		List<MenuInfo> menuInfos = menuInfoMapper.selectParentAll();
		return menuInfos;
	}
	
	
	/**
	 * 查找全部菜单信息
	 * @return
	 * @throws Exception
	 */
	public List<MenuInfo> findAll() throws Exception{
		List<MenuInfo> menuInfos = menuInfoMapper.selectAll();
		return menuInfos;
	}
	
	public String deleteMenus(String str) throws Exception{
		String msg = "";
		String[] ids = str.split(",");
		String menuStr = "";
		for(int i=0;i<ids.length;i++){
			int rnum = roleMenuMapper.selectByMenuId(ids[i]);
			int unum = userMenuMapper.selectByMenuId(ids[i]);
			if(rnum>0||unum>0){
				menuStr += ids[i]+",";
				msg = "菜单"+menuStr+"已被用户关联过，不能直接删除！";
				return msg;
			}
			if(menuStr.length()==0){
				int id = Integer.parseInt(ids[i]);
				menuInfoMapper.deleteByPrimaryKey(id);
			}
		}
		msg = "成功删除"+ids.length+"条数据";
		return msg;
	}

	/**
	 * 根据id查找数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public MenuInfo findMenuById(Integer id) throws Exception{
		MenuInfo menuInfoById = menuInfoMapper.selectByPrimaryKey(id);
		return menuInfoById;
	}

	
	/**
	 * 更新数据
	 * @param menu
	 * @return
	 */
	public String updateMenu(MenuInfo menu) {
		String msg = "";
		int count = menuInfoMapper.updateByPrimaryKeySelective(menu);
		if(count>0){
			msg = "成功更新数据";
		}else{
			msg = "更新失败";
		}
		return msg;
	}
}