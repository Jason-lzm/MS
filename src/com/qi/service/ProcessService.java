package com.qi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qi.entity.PageInfo;
import com.qi.mapper.ProcessMapper;
import com.qi.model.Person;
import com.qi.model.Process;

@Service
@Transactional
public class ProcessService {
	@Resource
	private ProcessMapper processMapper;
	
	/**
	 * 保存操作流水
	 * @param operatetype
	 * @param loginPerson
	 * @param person
	 * @throws Exception
	 */
	public void saveProcess(String operatetype,Person loginPerson,Person person) throws Exception{
		Process process = new Process();
		process.setOperatorid(loginPerson.getId());
		process.setOperatetype(operatetype);
		process.setUserid(person.getId());
		process.setOperatetime(new Date());
		processMapper.insertSelective(process);
		
	}
	/**
	 * 保存操作流水
	 * @param operator
	 * @param userid
	 * @param operatetype
	 * @throws Exception
	 */
	public void saveProcess(Integer operator,Integer userid,String  operatetype) throws Exception{
		Process process = new Process();
		process.setOperatorid(operator);
		process.setOperatetype(operatetype);
		process.setUserid(userid);
		process.setOperatetime(new Date());
		processMapper.insertSelective(process);
		
	}
	
	public List<Process> findAll(PageInfo pageInfo,Integer page){
		pageInfo.setPagesize("pageSize");
		int totalPageCount = processMapper.selectTableCount();
		pageInfo.setTotalpages(pageInfo.getPagesize(), "process", totalPageCount);
		pageInfo.setPage(page, pageInfo.getTotalpages());
		
		int offset = pageInfo.getPagesize()*(pageInfo.getPage()-1);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("offset", offset);
		map.put("pagesize", pageInfo.getPagesize());
		List<Process> processes = processMapper.selectAll(map);
		return processes;
	}
}
