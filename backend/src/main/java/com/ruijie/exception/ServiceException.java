package com.ruijie.exception;

/**
 * 业务异常捕获
 * @author joyin
 *
 */
@SuppressWarnings("serial")
public class ServiceException extends RuntimeException{
	//异常返回的信息
	private Object rspObj; 
	
	public ServiceException(){

	}
	
	public ServiceException(Object rspObj){
		super();
		this.rspObj = rspObj;
	}

	public Object getRspObj() {
		return rspObj;
	}

	public void setRspObj(Object rspObj) {
		this.rspObj = rspObj;
	}

	
}
