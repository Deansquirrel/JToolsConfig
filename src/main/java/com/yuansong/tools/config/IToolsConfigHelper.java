package com.yuansong.tools.config;

public interface IToolsConfigHelper {
	
	public static final long version = 0L;
	
	/**
	 * 获取客户端ID
	 * @return
	 */
	public String getClientId();
	
	/**
	 * 更新客户端ID
	 * @return
	 */
	public String updateClientId();

}
