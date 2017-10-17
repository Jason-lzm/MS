package com.qi.mapper;

import java.util.List;
import java.util.Map;

import com.qi.model.UserMenuKey;

public interface UserMenuMapper {
    int deleteByPrimaryKey(UserMenuKey key);

    int insert(UserMenuKey record);

    int insertSelective(UserMenuKey record);
    
    UserMenuKey selectByKey(Map map);
    
    List<UserMenuKey> selectByUserId(String userId);
    
    int selectByMenuId(String menuId);
}