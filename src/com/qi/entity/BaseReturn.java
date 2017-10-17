package com.qi.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BaseReturn {

	/**
	 * 响应码
	 */
	private String responseCode;
	
	/**
	 * 响应描述
	 */
	private String responseDesc;
	
	/**
	 * 响应数据主体
	 */
	private Map<String,Object> map = new HashMap<String,Object>();
	
	/**
	 * 业务成功
	 */
	public static String RESPONSE_SUCCESS="0000"; 
	
	/**
	 * 处理异常
	 */
	public static String RESPONSE_ERROR="0001";
	
	/**
	 * 业务失败
	 */
	public static String RESPONSE_FAIL="0002";
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public String getResponseDesc() {
		return responseDesc;
	}
	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	
	/**
	 * 添加一个对象
	 * @param key
	 * @param obj
	 */
	public void addObject(String key,Object obj){
		map.put(key, obj);
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	
}
