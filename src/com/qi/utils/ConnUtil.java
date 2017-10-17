package com.qi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class ConnUtil {
	private static String url;
	private  static String user;
	private static String pwd;
	static Properties props;
	/**
	 * 静态代码块，只读取url,user,passWord一次
	 */
	static {
		try {
//			File file = new File("E:/workspace/MyUtils/src/db_oracle.properties");
//			FileInputStream in = new FileInputStream(file);
			InputStream in = ConnUtil.class.getClassLoader().
					getResourceAsStream("db_oracle.properties");
			props = new Properties();
			props.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 在properties文件里，根据key值查找对应的value值
	 * @param key
	 * @return
	 */
	public static String getValue(String key){
		String val = "";
		if(props!=null){
			String prop = props.getProperty(key);
			if(prop!=null){
				val=prop;
			}
		}
		return val;
	}
	
	/**
	 * 获取存放在properties文件里面的连接Oracle数据库必要数据
	 */
	public static void getParam(){
		url = getValue("url");
		user = getValue("user");
		pwd = getValue("passWord");
	}
	
	/**
	 * 与数据库获取连接
	 * @return
	 */
	public static Connection getConn(){
		getParam();
		Connection conn = null;
		try {
			conn=DriverManager.getConnection(url,user,pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭Connection
	 * @param conn
	 */
	public static void closeConn(Connection conn){
		try {
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭Statement
	 * @param stmt
	 */
	public static void closeStmt(Statement stmt){
		try{
			if(stmt!=null){
				stmt.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭PreparedStatement
	 * @param stmt
	 */
	public static void closeStmt(PreparedStatement stmt){
		try{
			if(stmt!=null){
				stmt.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭ResultSet
	 * @param rs
	 */
	public static void closeRs(ResultSet rs){
		try{
			if(rs!=null){
				rs.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取数据库的元数据
	 * @param conn
	 */
	public static void getMetaDate(Connection conn){
		try {
			DatabaseMetaData dmd = conn.getMetaData();
			String name = dmd.getDatabaseProductName();//获取数据库名字
			String version = dmd.getDatabaseProductVersion();//获取版本号
			String url = dmd.getURL();//获取连接字符串
			String userName = dmd.getUserName();//获取用户名
			System.out.println("数据库名字:"+name+"\n版本号:"+version+"\n连接字符串:"+url+"\n用户名:"+userName);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeConn(conn);
		}
	}
	
	/**
	 * 打印结果集获取的数据,可以获取表结构信息
	 * @param sql 查询语句
	 * @return 查询的每一条数据作为键值对存放到map里面，将所有的map存放到一个list里面
	 */
	public static List printTableData(ResultSet rs){
		List tableData = new ArrayList();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();//获取结果集的列数
			for(int i=1;i<=columnCount;i++){
				System.out.print(rsmd.getColumnName(i)+"\t");
			}
			System.out.println();
			System.out.println("----------------------------------------------------");
			
			while(rs.next()){
				Map rowData = new HashMap();
				for(int i=1;i<=columnCount;i++){
					//String value = rs.getString(i);//直接用列序取值
					//用列名取值
           		    String value = rs.getString(rsmd.getColumnName(i));
					System.out.print(value+"\t");
					String columnName = rsmd.getColumnName(i);
					rowData.put(columnName,value);
				}
				tableData.add(rowData);
				System.out.println();
			}
			return tableData;
		} catch (SQLException e) {
			e.printStackTrace();
			return tableData;
		}
	}
	
	/**
	 * 根据查询语句获取数据库结果集的数据
	 * @param dql 查询语句
	 * @param list 按顺序存放需要取代占位符？的内容
	 * @return 
	 */
	public static Object getTableData(String sql,List list){
		Connection conn = getConn();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			addSql(list,stmt);
			if(!sql.startsWith("select")){
				int n = stmt.executeUpdate();
				if(n>=1){
					System.out.println("成功执行:"+n+"条");
					return n;
				}else{
					System.out.println("语句未成功执行");
					return null;
				}
			}else{
				rs = stmt.executeQuery();
			 	return printTableData(rs);
				
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			closeRs(rs);
			closeStmt(stmt);
			closeConn(conn);
		}
	}
	
	/**
	 * getTableData方法的重载，根据完整的sql语句执行命令
	 * @param sql 不带有占位符的语句
	 */
	public static Object getTableData(String sql){
		Connection conn = getConn();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			if(!sql.startsWith("select")){
				int n = stmt.executeUpdate(sql);
				if(n>=1){
					System.out.println("成功执行:"+n+"条");
					return n;
				}else{
					System.out.println("语句未成功执行");
					return null;
				}
			}else{
				rs = stmt.executeQuery(sql);
				return printTableData(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			closeRs(rs);
			closeStmt(stmt);
			closeConn(conn);
		}
	}
	
	/**
	 * 判断集合元素类型，添加到sql语句占位符里面
	 * @param i 添加顺序
	 * @param list 需要添加占位符内容的集合
	 */
	private static void addSql(List list,PreparedStatement stmt) {
		try {
			for(int i=0;i<list.size();i++){
				String type = ""+list.get(i).getClass();
				if(type.endsWith("Integer")){
					stmt.setInt(i+1, (Integer) list.get(i));
				}else if(type.endsWith("String")){
					stmt.setString(i+1, (String) list.get(i));
				}else if(type.endsWith("Double")){
					stmt.setDouble(i+1, (Double) list.get(i));
				}else if(type.endsWith("Long")){
					stmt.setLong(i+1, (Long) list.get(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
//		String sql1 = "select * from user_info";
//		getTableData(sql1);
		
		String class_name = getValue("url");
		System.out.println(class_name);
	}
}
