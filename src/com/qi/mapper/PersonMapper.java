package com.qi.mapper;

import java.util.List;
import java.util.Map;

import com.qi.model.Person;

public interface PersonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Person record);

    int insertSelective(Person record);

    Person selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);
    
    Person selectByName(String name);
    
    List<Person> selectByPage(Map map);
    
    List<Person> selectAllPerson();
    
    List<Person> selectAllByHead(String head);
    
    int selectTotalCount(Map map);
}