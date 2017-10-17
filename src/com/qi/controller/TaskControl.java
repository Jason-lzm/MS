package com.qi.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qi.entity.BaseReturn;
import com.qi.entity.PageInfos;
import com.qi.model.Person;
import com.qi.model.Task;
import com.qi.service.TaskService;

@Controller
@RequestMapping("/task")
public class TaskControl {
	
	Logger logger = Logger.getLogger(TaskControl.class);

	@Autowired
	private TaskService taskService;
	
	
	/**
	 * 保存任务
	 * @param task
	 * @return
	 */
	@RequestMapping(value="/askAddTask", method=RequestMethod.POST)
	@ResponseBody
	public BaseReturn askAddTask(@RequestBody Task task){
		BaseReturn baseReturn = new BaseReturn();
		try {
			String msg = taskService.addTask(task);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统出错，保存任务失败！");
		} 
		logger.info(baseReturn);
		return baseReturn;
	}
	
	
	/**
	 * 分页查询用户信息
	 * @param task
	 * @return
	 */
	@RequestMapping(value="/getTaskInfoByPage/{page}/{pageSize}", 
			method=RequestMethod.GET)
	@ResponseBody
	public BaseReturn getTaskInfoByPage(@PathVariable Integer page,
			@PathVariable Integer pageSize,
			HttpSession session){
		//获取登陆用户信息
		Person loginPerson = (Person) session.getAttribute("person");
		//获取后台传送的page信息，封装到对象里
		PageInfos pageInfos = new PageInfos();
		pageInfos.setPage(page);
		pageInfos.setPagesize(pageSize);
		int totalPage = taskService.getTotalPage(pageSize, loginPerson.getId());
		pageInfos.setTotalpages(totalPage);
		
		BaseReturn baseReturn = new BaseReturn();
		try {
			List<Task> taskInfos = taskService.findTaskByPage(pageInfos, loginPerson.getId());
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取数据成功");
			baseReturn.addObject("taskInfos", taskInfos);
			baseReturn.addObject("pageInfos", pageInfos);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统出错，获取任务信息失败！");
		} 
		logger.info(baseReturn);
		return baseReturn;
	}
	
	/**
	 * 批量删除任务
	 * @param str
	 * @return
	 */
	@RequestMapping(value="/deleteTask",method=RequestMethod.DELETE)
	public @ResponseBody BaseReturn deleteTask(@RequestBody String str){
		BaseReturn baseReturn = new BaseReturn();
		String msg;
		try {
			msg = taskService.deleteTaskByState(str);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("删除任务出错！");
		}
		logger.info(baseReturn);
		return baseReturn;
	}
	
	/**
	 * 更新任务状态1
	 * @param str
	 * @return
	 */
	@RequestMapping(value="/updateState1",method=RequestMethod.PUT)
	public @ResponseBody BaseReturn updataState1(@RequestBody Integer id ){
		BaseReturn baseReturn = new BaseReturn();
		try {
			int count = taskService.updateTaskById1(id);
			if(count>0){
				baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
				baseReturn.setResponseDesc("更新状态成功");
			}else{
				baseReturn.setResponseCode(BaseReturn.RESPONSE_FAIL);
				baseReturn.setResponseDesc("更新状态失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("更新状态出错");
		}
		return baseReturn;
	}
	
	/**
	 * 更新任务状态2
	 * @param str
	 * @return
	 */
	@RequestMapping(value="/updateState2",method=RequestMethod.PUT)
	public @ResponseBody BaseReturn updataState2(@RequestBody Integer id ){
		BaseReturn baseReturn = new BaseReturn();
		try {
			int count = taskService.updateTaskById2(id);
			if(count>0){
				baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
				baseReturn.setResponseDesc("更新状态成功");
			}else{
				baseReturn.setResponseCode(BaseReturn.RESPONSE_FAIL);
				baseReturn.setResponseDesc("更新状态失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("更新状态出错");
		}
		return baseReturn;
	}
}
