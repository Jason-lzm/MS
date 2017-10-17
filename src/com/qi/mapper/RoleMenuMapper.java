package com.qi.mapper;

import java.util.List;
import java.util.Map;

import com.qi.model.RoleMenuKey;

public interface RoleMenuMapper {
    int deleteByPrimaryKey(RoleMenuKey key);

    int insert(RoleMenuKey record);

    int insertSelective(RoleMenuKey record);
    
    RoleMenuKey selectByKey(Map map);
    
    List<RoleMenuKey> selectByRoleName(String roleName);
    
    int selectByMenuId(String menuId);
}