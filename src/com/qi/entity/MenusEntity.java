package com.qi.entity;

import java.util.List;
import com.qi.model.MenuInfo;

public class MenusEntity {
	private String parentMenuName;
	private List<MenuInfo> childMenus;
	
	public String getParentMenuName() {
		return parentMenuName;
	}
	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}
	public List<MenuInfo> getChildMenus() {
		return childMenus;
	}
	public void setChildMenus(List<MenuInfo> childs) {
		this.childMenus = childs;
	}
	
}
