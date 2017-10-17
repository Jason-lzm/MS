package com.qi.mail;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogUtil {
	static Logger log = Logger.getLogger(LogUtil.class);
	
	public static void  logInfo(String msg){
		log.info(msg);
	}
	
	public static void  logDebug(String msg){
		log.debug(msg);
	}
	
	public static void  logWarn(String msg){
		log.warn(msg);
	}
	
	public static void  logError(String msg){
		log.error(msg);
	}
	
	public static void main(String[] args) {
		//debug<info<warn<error
		LogUtil.logDebug("你好啊");
		LogUtil.logInfo("你好啊");
		LogUtil.logWarn("你好啊");
		LogUtil.logError("你好啊");
	}
}
