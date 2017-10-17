package com.qi.utils;

import java.io.IOException;
import java.util.Properties;

import com.qi.mail.LogUtil;

public class ConfigUtils {

	public static Properties prop = new Properties();
	
	public static String getConfig(String key){
		String file = "db_oracle.properties";
		prop.clear();
		try {
			prop.load(ConfigUtils.class.getClassLoader().getResourceAsStream(file));
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.logError("加载配置文件"+file+"出错");
		}
		LogUtil.logDebug(key+":"+prop.getProperty(key));
		return prop.getProperty(key);
	}
	
	public static void main(String[] args) {
		System.out.println(ConfigUtils.getConfig("master"));
	}
}
