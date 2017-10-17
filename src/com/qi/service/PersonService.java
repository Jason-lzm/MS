package com.qi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.common.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qi.entity.PageInfo;
import com.qi.mapper.PersonMapper;
import com.qi.model.Person;
import com.qi.model.Role;

@Service
@Transactional
public class PersonService {

	Logger logger = Logger.getLogger(PersonService.class);
	
	@Resource
	private PersonMapper personMapper;
	@Resource
	private MailService mailService;
	@Resource
	private ProcessService processService;
	@Resource
	private RoleService roleService;

	/**
	 * 用户登陆检查，用户名和密码正确，成功登陆，并设置用户状态为在线
	 * 否则提示错误信息
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String checkOnline(String name, String password) throws Exception {
		String msg = "";
		// 根据用户名查找是否存在
		Person person = personMapper.selectByName(name);
		// 如果不存在，提示用户先注册
		if (person == null) {
			msg = "请注册用户名！";
			// 如果存在用户，再判断密码是否正确，如果不正确，提示错误信息
		} else if (!person.getPassword().equals(password)) {
			msg = "密码不正确！";
			// 用户名和密码都正确，再判断是否已经登陆，不能重复登陆
		} else if (person.getIslogin().equals("t")) {
			msg = "请勿重复登陆！";
		} else {
			// 如果未登陆，则允许登陆，修改状态为已登录，并绑定用户信息到session
			person.setIslogin("t");
			personMapper.updateByPrimaryKey(person);
			msg = "ok";
		}
		return msg;
	}

	/**
	 * 根据用户提交的用户名查找用户信息
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Person findByName(String name) throws Exception {
		Person person = personMapper.selectByName(name);
		return person;
	}
	
	/**
	 * 根据id查找用户信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Person findById(Integer id) throws Exception{
		Person person = personMapper.selectByPrimaryKey(id);
		return person;
	}

	/**
	 * 用户注册检查，成功则保存信息
	 * 并发电邮通知联系人，同时保存流水
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String savePerson(Person person, Person loginPerson)
			throws Exception {
		String msg = "";
		// 根据用户提交的用户名查询，判断是否已经注册过
		Person p = personMapper.selectByName(person.getName());
		if (p != null) {
			msg = "用户已经注册！";
		} else {
			//设置上级管理人为添加用户的该操作人员的name
			person.setHead(loginPerson.getName());
			//设置新用户的默认登陆状态为未登陆
			person.setIslogin("f");
			//保存注册信息
			int n1 = personMapper.insertSelective(person);
			if(n1>0){
				Person newPerson = personMapper.selectByName(person.getName());
				int n2 = roleService.saveRole(newPerson, loginPerson);
				if(n2>0){
					msg=mailService.sendRegistMail(person, loginPerson);
					try {
						processService.saveProcess("1", loginPerson, person);
						if(msg.equals("ok")){
							msg="注册成功，邮件发送成功，流水保存成功！";
						}else{
							msg="注册成功，邮件发送失败，流水保存成功！";
						}
					} catch (Exception e) {
						e.printStackTrace();
						if(msg.equals("ok")){
							msg="注册成功，邮件发送成功，流水保存失败！";
						}else{
							msg="注册成功，邮件发送失败，流水保存失败！";
						}
					}
					
				}else{
					msg="添加用户成功,保存角色失败！";
				}
				
			}else{
				msg="添加用户失败！";
			}
		}
		return msg;
	}
	
	/**
	 * 分页查询用户信息
	 * master可以查询所有信息，manager只能查看他所负责的下级用户信息
	 * @param status
	 * @return
	 */
	public List<Person> findPerson(Person loginPerson,PageInfo pageInfo,Integer page){
		Map<String, Object> map = new HashMap<String, Object>();
		int totalTableCount = 0;
		//获取操作员的id，查出对应的角色
		Role role = roleService.findByUserId(loginPerson.getId());
		
		//master按照用户身份查找全部联系人
		if(role.getId()==1){
			totalTableCount = personMapper.selectTotalCount(map);
			logger.info("总页数"+totalTableCount);
		//其他用户只能查找自己添加的子用户
		}else{
			map.put("head", loginPerson.getName());
			totalTableCount = personMapper.selectTotalCount(map);
			logger.info("总页数"+totalTableCount);
		}
		pageInfo.setPagesize("pageSize");
		pageInfo.setTotalpages(pageInfo.getPagesize(), "person",totalTableCount);
		pageInfo.setPage(page, pageInfo.getTotalpages());
		
		int offset = pageInfo.getPagesize()*(pageInfo.getPage()-1);
		map.put("offset", offset);
		map.put("pagesize", pageInfo.getPagesize());
		List<Person> persons = personMapper.selectByPage(map);
		return persons;
	}
	
	
	/**
	 * 批量删除数据
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public String deletePerson(String str,Person loginPerson) throws Exception{
		String msg = "";
		String[] ids = str.split(","); 
		int count = ids.length;
		int n = 0;
		int m = 0;
		for(int i=0;i<ids.length;i++){
			int id = Integer.parseInt(ids[i]);
			Person person = personMapper.selectByPrimaryKey(id);
			personMapper.deleteByPrimaryKey(id);
			try {
				roleService.deleteRoles(id);
			} catch (Exception e1) {
				e1.printStackTrace();
				msg = "删除角色出错！";
			}
			msg = mailService.sendDeleteMail(person, loginPerson);
			if(msg.equals("ok")){
				n++;
			}else{
				n--;
			}
			//流水：删除用户信息
			try {
				processService.saveProcess("3", loginPerson, person);
				m++;
			} catch (Exception e) {
				e.printStackTrace();
				m--;
			}
		}
		
		msg = "成功删除"+count+"条数据,邮件发送成功"+n+"条,保存流水成功"+m+"条";
		
		return msg;
	}
	
	/**
	 * 更新用户信息，用户名未修改的情况
	 * @param loginPerson
	 * @param person
	 * @return
	 */
	public String updatePerson(Person loginPerson,Person person,Role role){
		String msg = "";
		Person p = personMapper.selectByName(person.getHead());
		if(p==null){
			msg = "找不到负责人信息，请先注册！";
			return msg;
		}
		int i = personMapper.updateByPrimaryKeySelective(person);
		if(i>0){
			try {
				int j = roleService.updateRole(person.getId(),role.getRoleName());
				if(j>0){
					msg=mailService.sendRegistMail(person, loginPerson);
					try {
						//流水：更新用户信息
						processService.saveProcess("2", loginPerson, person);
						if(msg.equals("ok")){
							msg="信息更新成功，邮件发送成功，流水保存成功！";
						}else{
							msg="信息更新成功，邮件发送失败，流水保存成功！";
						}
					} catch (Exception e) {
						e.printStackTrace();
						if(msg.equals("ok")){
							msg="信息更新成功，邮件发送成功，流水保存失败！";
						}else{
							msg="信息更新成功，邮件发送失败，流水保存失败！";
						}
					}
				}else{
					msg = "信息更新成功，角色保存失败！";
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				msg = "信息更新成功，保存角色出错！";
			}
		}
		return msg;
	}

	/**
	 * 查找全部用户信息
	 * @return
	 * @throws Exception
	 */
	public List<Person> findAll() throws Exception {
		List<Person> persons = personMapper.selectAllPerson();
		return persons;
	}

	/**
	 * 根据用户身份查找用户信息
	 * @param loginPerson
	 * @return
	 */
	public List<Person> findByRole(Person loginPerson) {
		List<Person> persons = personMapper.selectAllByHead(loginPerson.getName());
		return persons;
	}
}
