package com.qi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.common.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qi.dubbo.service.AccountApply;
import com.qi.entity.PageInfos;
import com.qi.mapper.ApplyInfoMapper;
import com.qi.model.ApplyInfo;
import com.qi.model.Person;
import com.qi.model.Task;

@Service
@Transactional
public class AccountService {

	Logger logger = Logger.getLogger(AccountService.class);
	
	@Resource
	private ApplyInfoMapper applyInfoMapper;
	@Resource
	private AccountApply accountApply;
	
	/**
	 * 检查同一物品是否已经申请过
	 * @param applyInfo
	 * @return
	 * @throws Exception
	 */
	public String checkApply(ApplyInfo applyInfo) throws Exception{
		String msg = "";
		List<ApplyInfo> a = applyInfoMapper.selectByGoods(applyInfo.getGoods());
		if(a.size()>0){
			msg = "yes";
		}else{
			msg = "no";//未申请过同样的物品
		}
		return msg;
	}
	
	/**
	 * 保存物料申请信息
	 * @param applyInfo
	 * @return
	 * @throws Exception
	 */
	public String addApply(ApplyInfo applyInfo) throws Exception{
		String msg = "";
		int n = applyInfoMapper.insertSelective(applyInfo);
		if(n>0){
			try {
				msg = accountApply.accountRequest(applyInfo);
				if(msg=="ok"){
					applyInfo.setState("2");
					int m = applyInfoMapper.updateByPrimaryKeySelective(applyInfo);
					if(m>0){
						msg = "信息已保存，审批通过！";
					}else{
						msg = "审批通过，信息更新失败！";
					}
				}else{
					msg = "信息已保存，审批未通过！";
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "信息已保存，但发送申请失败！";
			}
		}else{
			msg = "申请信息保存出错！";
		}
		return msg;
	}

	/**
	 * 根据物品状态查找物品信息
	 * @return
	 * @throws Exception
	 */
	public List<ApplyInfo> findApplyInfoByState() throws Exception{
		//找出所有已购买的物品信息
		List<ApplyInfo> goodsInfos = applyInfoMapper.selectByState("3");
		return goodsInfos;
	}
	
	/**
	 * 根据id查找物品信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ApplyInfo findById(int id) throws Exception{
		ApplyInfo applyInfo = applyInfoMapper.selectByPrimaryKey(id);
		return applyInfo;
	}
	
	/**
	 * 分配任务时需要分配物资，根据物资数量总量需要减少
	 * @param id
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public int updateAomunt(Integer id,Double amount) throws Exception{
		ApplyInfo applyInfo = applyInfoMapper.selectByPrimaryKey(id);
		Double totalAmount = applyInfo.getAmount();
		Double newAmount = totalAmount-amount;
		applyInfo.setAmount(newAmount);
		int count = applyInfoMapper.updateByPrimaryKeySelective(applyInfo);
		return count;
	}
	
	/**
	 * 更新状态
	 * @param applyInfo
	 * @return
	 */
	public int updateTaskById(ApplyInfo applyInfo) {
		int count = applyInfoMapper.updateByPrimaryKeySelective(applyInfo);
		return count;
	}
	
	
	/**
	 * 获取总页数
	 * @param pageSize
	 * @param id
	 * @return
	 */
	public int getTotalPage(Integer pageSize) {
		int totalTableCount = applyInfoMapper.selectTableCount();
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
	 * 分页查询信息
	 * @param pageInfos
	 * @param id
	 * @return
	 */
	public List<ApplyInfo> findTaskByPage(PageInfos pageInfos) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		Integer offset = pageInfos.getPagesize()*(pageInfos.getPage()-1);
		map.put("offset", offset);
		map.put("pagesize", pageInfos.getPagesize());
		//分页查询数据
		List<ApplyInfo> applyInfos = applyInfoMapper.selectByPage(map);
		return applyInfos;
	}
	
	
	
	
	
	
	
	public AccountApply getAccountApply() {
		return accountApply;
	}

	public void setAccountApply(AccountApply accountApply) {
		this.accountApply = accountApply;
	}


	
	
}
