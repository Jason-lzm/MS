package com.qi.listener;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import com.qi.model.Person;
import com.qi.utils.ConnUtil;

public class SessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		System.out.println("session创建");
	}

	/**
	 * 用户无操作导致session超时，或关闭浏览器等待session超时，
	 * 服务器调用session销毁方法，将用户登陆状态改为未登录。
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
        Person p = (Person) event.getSession().getAttribute("person");
        if(p!=null){
        	p.setIslogin("f");
        	try {
        		String sql = "update person set isLogin=? where id=?";
        		List<Object> list = new ArrayList<Object>();
        		list.add(p.getIslogin());
        		list.add(p.getId());
        		ConnUtil.getTableData(sql, list);
        		System.out.println("销毁session");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
	}

}
