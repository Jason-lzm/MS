package com.qi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qi.mail.MailSendClient;
import com.qi.model.Person;

@Service
@Transactional
public class MailService {

	/**
	 * 注册成功则发邮件通知用户
	 * @param person
	 * @return
	 */
	public String sendRegistMail(Person person,Person loginPerson) {
		String msg = "";
		MailSendClient mail = new MailSendClient();
		String email = person.getEmail();
		String theme = "新用户注册提醒...";
		String content ="To:"+person.getName()
				+ ",\n您已成功注册婚庆流程系统账号"
				+ ",\n用户名："+person.getName()
				+ ",\n密码："+person.getPassword()
				+ ",\n负责人："+loginPerson.getName()
				+ ",\n详情访问：http://localhost:8088/MS/html/login.html";
		try {
			mail.sendMail(email, theme, content);
			msg =  "ok";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "fail";
		}
		return msg;
	}
	
	/**
	 * 修改用户信息成功则发邮件通知用户
	 * @param person
	 * @return
	 */
	public String sendUpdateMail(Person person,Person loginPerson){
		String msg = "";
		MailSendClient mail = new MailSendClient();
		String email = person.getEmail();
		String theme = "用户信息更改提醒...";
		String content = "To:"+person.getName()
				+ ",\n您的用户信息已被修改"
				+ ",\n用户名："+person.getName()
				+ ",\n密码："+person.getPassword()
				+ ",\n操作人："+loginPerson.getName()
				+ ",\n详情访问：http://localhost:8088/MS/html/login.html";
		try {
			mail.sendMail(email, theme, content);
			msg = "ok";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "fail";
		}
		return msg;
	}
	
	/**
	 * 删除用户信息成功则发邮件通知用户
	 * @param person
	 * @return
	 */
	public String sendDeleteMail(Person person,Person loginPerson){
		String msg = "";
		if(person!=null){
			MailSendClient mail = new MailSendClient();
			String email = person.getEmail();
			String theme = "用户账号删除提醒...";
			String content = "To:"+person.getName()
					+ ",\n您在婚庆流程系统注册的账号已被删除"
					+ ",\n操作人："+loginPerson.getName()
					+ ",\n如有疑问请联系相关负责人："+loginPerson.getPhone()
					+ ",\n详情访问：http://localhost:8088/MS/html/login.html";
			try {
				mail.sendMail(email, theme, content);
				msg = "ok";
			} catch (Exception e) {
				e.printStackTrace();
				msg = "fail";
			}
		}
		return msg;
	}
}
