package com.qi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.common.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qi.entity.PageInfos;
import com.qi.mapper.PersonMapper;
import com.qi.mapper.TaskMapper;
import com.qi.model.Person;
import com.qi.model.Task;

@Service
@Transactional
public class TaskService {

	Logger logger = Logger.getLogger(TaskService.class);
	
	@Resource
	private TaskMapper taskMapper;
	@Resource
	private PersonMapper personMapper;
	@Resource
	private AccountService accountService;
	
	/**
	 * 保存任务信息
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public String addTask(Task task) throws Exception{
		String msg = "";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", task.getUserid());
		map.put("startTime", task.getStarttime());
		map.put("endTime", task.getEndtime());
		map.put("goods", task.getGoods());
		map.put("state", task.getState());
		map.put("memo", task.getMemo());
		map.put("content", task.getContent());
		
		Task t = taskMapper.selectByTask(map);
		if(t!=null){
			msg = "已添加相同任务！";
			return msg;
		}else{
			int n = taskMapper.insertSelective(task);
			if(n>0){
				if(!task.getGoods().equals("")){
					Integer id = Integer.parseInt(task.getGoods());
					Double amount = task.getAmount();
					try {
						int count = accountService.updateAomunt(id, amount);
						if(count>0){
							msg = "添加任务成功,更新物品数量成功！";
						}else{
							msg = "添加任务成功,更新物品数量失败！";
						}
					} catch (Exception e) {
						e.printStackTrace();
						msg = "添加任务成功,更新物品时出错！";
					}
				}else{
					msg = "添加任务成功";
				}
			}else{
				msg = "添加任务失败！";
			}
		}
		return msg;
	}
	
	/**
	 * 分页查询任务，master可以查询所有任务，其他人只能查询分配给自己的任务
	 * @param pageInfos
	 * @param loginPerson
	 * @return
	 * @throws Exception
	 */
	public List<Task> findTaskByPage(PageInfos pageInfos,int loginPersonId) throws Exception{
		Map<String,Integer> map = new HashMap<String,Integer>();
		//如果不是管理员，则按照分配人员的id查找数据
		if(loginPersonId!=1){
			map.put("userId", loginPersonId);
		}
		Integer offset = pageInfos.getPagesize()*(pageInfos.getPage()-1);
		map.put("offset", offset);
		map.put("pagesize", pageInfos.getPagesize());
		//分页查询数据
		List<Task> taskInfos = taskMapper.selectByPage(map);
		//遍历查询结果，将保存的负责人id改为名字，显示到页面
		for(Task task:taskInfos){
			int userId = Integer.parseInt(task.getUserid());
			Person person = personMapper.selectByPrimaryKey(userId);
			task.setUserid(person.getName());
		}
		return taskInfos;
	}
	
	/**
	 * 获取任务表的总个数
	 * @param pageInfo
	 * @return
	 */
	public int getTotalPage(int pageSize,int userId) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		//如果不是master，则只能按照负责人id查找数据
		if(userId!=1){
			map.put("userId", userId);
		}
		int totalTableCount = taskMapper.selectTableCount(map);
		int totalpage=0;
		int mode=totalTableCount%pageSize;  
		if(mode==0){
			totalpage=totalTableCount/pageSize;
		}else{
			totalpage=totalTableCount/pageSize+1;
		}
		return totalpage;
	}

	/**
	 * 批量删除任务，只有任务状态是未开始或者已完成才能删除
	 * @param str
	 * @return
	 */
	public String deleteTaskByState(String str) {
		String msg = "";
		String[] ids = str.split(",");
		String stateInfo="";
		for(int i=0;i<ids.length;i++){
			int id = Integer.parseInt(ids[i]);
			//根据id查找任务信息
			Task task = taskMapper.selectByPrimaryKey(id);
			//如果被删除的任务正在进行中，则提示用户不能直接删除
			if(task.getState().equals("2")){
				stateInfo += ids[i]+",";
				msg = "任务"+stateInfo+"正在进行中，请通知相关人中止任何后再删除！";
				return msg;
			}
			
			//如果任务状态不是进行中("2")，则可以删除
			if(stateInfo.length()==0){
				taskMapper.deleteByPrimaryKey(id);
			}
		}
		msg = "成功删除"+ids.length+"条数据";
		return msg;
	}
	
	/**
	 * 更新任务状态
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int updateTaskById1(Integer id) throws Exception{
		Task task = taskMapper.selectByPrimaryKey(id);
		task.setState("2");
		int count = taskMapper.updateByPrimaryKeySelective(task);
		return count;
	}
	
	public int updateTaskById2(Integer id) throws Exception{
		Task task = taskMapper.selectByPrimaryKey(id);
		task.setState("3");
		int count = taskMapper.updateByPrimaryKeySelective(task);
		return count;
	}
}
