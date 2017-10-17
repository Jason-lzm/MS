package com.qi.mail;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {

	public static Properties prop = new Properties();
	
	public static String getConfig(String key,String file){
		prop.clear();
		try {
			prop.load(ConfigUtil.class.getClassLoader().getResourceAsStream(file));
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.logError("加载配置文件"+file+"出错");
		}
		LogUtil.logDebug(key+":"+prop.getProperty(key));
		return prop.getProperty(key);
	}
	
	public static void main(String[] args) {
		ConfigUtil.getConfig("mail.host", "mail.config.properties");
		ConfigUtil.getConfig("sms_port", "sms.config.properties");
	}
}
