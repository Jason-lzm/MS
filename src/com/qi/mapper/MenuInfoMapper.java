package com.qi.mapper;

import java.util.List;
import java.util.Map;
import com.qi.model.MenuInfo;

public interface MenuInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MenuInfo record);

    int insertSelective(MenuInfo record);

    MenuInfo selectByPrimaryKey(Integer id);
    
    MenuInfo selectByName(String name);

    int updateByPrimaryKeySelective(MenuInfo record);

    int updateByPrimaryKey(MenuInfo record);
    
    int selectTableCount();
    
    List<MenuInfo> selectByPage(Map map);
    
    List<MenuInfo> selectAll();
    
    List<MenuInfo> selectParentAll();
}