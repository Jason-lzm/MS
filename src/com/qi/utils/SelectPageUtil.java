package com.qi.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SelectPageUtil {
	
	/**
	 * 查询表数据，并将查询结果打印到控制台
	 * @param page 查询的页面
	 * @param pageSize 每页的行数
	 * @param table_name 需要查询的表
	 */
	public static void getPageData(int page,int pageSize,String table_name){
		int begin = (page-1)*pageSize+1;
		int end = page*pageSize;
		String sql = "select * from (select a.*,rownum rn from (select * from "
				+ table_name
				+ ") a) where rn between ? and ? ";
		List<Integer> ls = new ArrayList<Integer>();
		ls.add(begin);
		ls.add(end);
		List list = (List) ConnUtil.getTableData(sql,ls);
	}
	
	/**
	 * 基于缓存的分页，打印指定页的数据到控制台
	 * begin = (page-1)*pageSize+1 在结果集中第page页的记录起点
	 * @param pageSize 每页多少条记录
	 * @param page 第几页
	 * @param strpage 用户输入的指定页面
	 */
	public static void getBufferPage(int pageSize, String strpage,String table_name) {
		/*实现功能3，防止用户输入类似"abc@"等字符作为页数参数，强制转到第一页 */
		int page=1;
		try{
			page=Integer.parseInt(strpage);
		}catch(NumberFormatException e){
			page=1;
		}
		/*实现功能1，防止用户输入页数超过最大数 */
		int totalTableCount = getTotalTableCount(table_name);
		int totalpage = getTotalPage(pageSize,table_name,totalTableCount);//获取总页数
		if(page>totalpage){
			page=totalpage;//当用户输入页数超出时，默认为最后一页
		}
		if(page<1){
			page=1;//当用户输入页数为负数时，默认为第一页
		}
		
		int begin = (page-1)*pageSize+1;
		String sql="select * from "+table_name;
		Connection conn = ConnUtil.getConn();
		Statement stmt=null;
		ResultSet rs=null;
		try{
			stmt=conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,//参数一，设置类型为可滚动的结果集
				ResultSet.CONCUR_READ_ONLY);//参数二，设置并发性为其他用户只读
			rs=stmt.executeQuery(sql);
			rs.absolute(begin);//结果集跳到begin位置
			for(int i=1;i<=pageSize;i++){
//				System.out.print(rs.getInt("id")+" ");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				for(int j=1;j<=columnCount;j++){
					String value = rs.getString(j);//直接用列序取值
					System.out.print(value+" ");
				}
				System.out.println();
				if(!rs.next()){
					break;
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnUtil.closeRs(rs);
			ConnUtil.closeStmt(stmt);
			ConnUtil.closeConn(conn);
		}     
	}
	
	/**
	 * 获得表得到总页数TotalPage,根据每页的记录数，计算共有多少页
	 * @param pageSize 每页多少条
	 * @return 总页数
	 */
	public static int getTotalPage(int pageSize,String table_name,int totalTableCount) {
//		totalTableCount = 0;
//		totalTableCount = getTotalTableCount(table_name);
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
	 * 获得表的总记录数TotalTableCount
	 * @return 表的总记录数
	 */
	public static int getTotalTableCount(String table_name) {
		int count = 0;
		String sql = "select count(*) num from "+table_name;
		Connection conn = ConnUtil.getConn();
		Statement stmt=null;
		ResultSet rs=null;
		try{
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
			rs.next();
			count=rs.getInt("num");//从结果集中取到num（count(*)）总数的值
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ConnUtil.closeRs(rs);
			ConnUtil.closeStmt(stmt);
			ConnUtil.closeConn(conn);
		}
		return count;
	}
	
	public static void main(String[] args) {
//		String sql1 = "select * from user_info";
//		ConnUtil.getTableData(sql1);
		getPageData(2, 1, "t_sale");
	}

}
