package com.ruijie.model.query;

public class BaseQuery {
	private Integer page;
	private Integer size;
	private String orderType;
	private String orderName;
	
	public Integer getPage() {
		if(this.page == null) this.page = 1;
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		if(this.size == null) this.size = 1;
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	
}
