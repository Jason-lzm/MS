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
import com.qi.model.ApplyInfo;
import com.qi.model.Person;
import com.qi.model.Task;
import com.qi.service.AccountService;

@Controller
@RequestMapping("/account")
public class AccountControl {
	
	Logger logger = Logger.getLogger(AccountControl.class);

	@Autowired
	private AccountService accountService;
	
	/**
	 * 提交物料申请，检查是否已经提交过，返回结果到Ajax回调函数再做判断
	 * @param applyInfo
	 * @return
	 */
	@RequestMapping(value="/subApplyInfo", method=RequestMethod.POST)
	@ResponseBody
	public BaseReturn subApplyInfo(@RequestBody ApplyInfo applyInfo){
		BaseReturn baseReturn = new BaseReturn();
		try {
			String msg = accountService.checkApply(applyInfo);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统出错，检查申请信息失败！");
		} 
		logger.info(baseReturn);
		return baseReturn;
	}
	
	/**
	 * 从前台获取申请信息保存到数据库，并发送请求到账务系统
	 * @param applyInfo
	 * @return
	 */
	@RequestMapping(value="/subApplyAgain", method=RequestMethod.POST)
	@ResponseBody
	public BaseReturn subApplyAgain(@RequestBody ApplyInfo applyInfo){
		BaseReturn baseReturn = new BaseReturn();
		try {
			String msg = accountService.addApply(applyInfo);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc(msg);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统出错，保存申请信息失败！");
		} 
		logger.info(baseReturn);
		return baseReturn;
	}
	
	/**
	 * 获取所有已购买完成的物品信息
	 * @return
	 */
	@RequestMapping(value="/getGoodsInfos", method=RequestMethod.GET)
	@ResponseBody
	public BaseReturn getGoodsInfos(){
		BaseReturn baseReturn = new BaseReturn();
		try {
			List<ApplyInfo> goodsInfos = accountService.findApplyInfoByState();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取已购买物品信息成功");
			baseReturn.addObject("goodsInfos", goodsInfos);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统出错，获取已购买物品信息失败！");
		}
		logger.info(baseReturn);
		return baseReturn;
	}
	
	
	/**
	 * 通过id获取物品信息
	 * @return
	 */
	@RequestMapping(value="/getGoodsDetail", method=RequestMethod.GET)
	@ResponseBody
	public BaseReturn getGoodsDetail(int id){
		BaseReturn baseReturn = new BaseReturn();
		try {
			ApplyInfo applyInfo = accountService.findById(id);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取已购买物品信息成功");
			baseReturn.addObject("applyInfo", applyInfo);
		} catch (Exception e) {
			e.printStackTrace();
			baseReturn.setResponseCode(BaseReturn.RESPONSE_ERROR);
			baseReturn.setResponseDesc("系统出错，获取已购买物品信息失败！");
		}
		logger.info(baseReturn);
		return baseReturn;
	}

	
	/**
	 * 分页查询用户信息
	 * @param task
	 * @return
	 */
	@RequestMapping(value="/getApplyInfoByPage/{page}/{pageSize}", 
			method=RequestMethod.GET)
	@ResponseBody
	public BaseReturn getApplyInfoByPage(@PathVariable Integer page,
			@PathVariable Integer pageSize){
		//获取后台传送的page信息，封装到对象里
		PageInfos pageInfos = new PageInfos();
		pageInfos.setPage(page);
		pageInfos.setPagesize(pageSize);
		int totalPage = accountService.getTotalPage(pageSize);
		pageInfos.setTotalpages(totalPage);
		
		BaseReturn baseReturn = new BaseReturn();
		try {
			List<ApplyInfo> applyInfos = accountService.findTaskByPage(pageInfos);
			baseReturn.setResponseCode(BaseReturn.RESPONSE_SUCCESS);
			baseReturn.setResponseDesc("获取数据成功");
			baseReturn.addObject("applyInfos", applyInfos);
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
	 * 更新任务状态
	 * @param str
	 * @return
	 */
	@RequestMapping(value="/updateState",method=RequestMethod.PUT)
	public @ResponseBody BaseReturn updataState(@RequestBody ApplyInfo applyInfo ){
		BaseReturn baseReturn = new BaseReturn();
		try {
			int count = accountService.updateTaskById(applyInfo);
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
