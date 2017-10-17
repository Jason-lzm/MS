package com.qi.entity;

import com.qi.utils.ConfigUtils;
import com.qi.utils.SelectPageUtil;


public class PageInfo {
	private Integer page;
	private Integer pagesize;
	private Integer totalpages;
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page,Integer totalPages) {
		if(page<1){
			this.page=1;
		}else if(totalPages>0 && page>totalPages){
			this.page=totalPages;
		}else{
			this.page = page;
		}
	}
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(String pageSize) {
		this.pagesize = Integer.parseInt(ConfigUtils.getConfig(pageSize));
	}
	public Integer getTotalpages() {
		return totalpages;
	}
	public void setTotalpages(Integer pageSize,String tableName,Integer totalPageCount) {
		this.totalpages = SelectPageUtil.getTotalPage(pageSize, tableName,totalPageCount);
	}
	
	
}
