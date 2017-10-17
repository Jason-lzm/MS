package com.qi.mapper;

import java.util.List;
import java.util.Map;

import com.qi.model.Task;

public interface TaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Integer id);
    
    Task selectByTask(Map map);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKeyWithBLOBs(Task record);

    int updateByPrimaryKey(Task record);

	int selectTableCount(Map map);

	List<Task> selectByPage(Map<String, Integer> map);
}