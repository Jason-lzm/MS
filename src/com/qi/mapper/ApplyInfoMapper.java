package com.qi.mapper;

import java.util.List;
import java.util.Map;

import com.qi.model.ApplyInfo;

public interface ApplyInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyInfo record);

    int insertSelective(ApplyInfo record);

    ApplyInfo selectByPrimaryKey(Integer id);
    
    List<ApplyInfo> selectByGoods(String goods);
    
    List<ApplyInfo> selectByState(String state);
    
    List<ApplyInfo> selectByPage(Map map);

    int updateByPrimaryKeySelective(ApplyInfo record);

    int updateByPrimaryKey(ApplyInfo record);

	int selectTableCount();
}