package com.qi.mapper;

import java.util.List;
import java.util.Map;

import com.qi.model.Process;

public interface ProcessMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Process record);

    int insertSelective(Process record);

    Process selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Process record);

    int updateByPrimaryKey(Process record);
    
    List<Process> selectAll(Map map);
    
    int selectTableCount();
}