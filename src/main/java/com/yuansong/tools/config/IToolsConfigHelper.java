package com.yuansong.tools.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yuansong.tools.config.log.LogEntity;
import com.yuansong.tools.config.log.LogLevel;

public interface IToolsConfigHelper {
	
	public static final long version = 0L;
	
	public void checkAndInitConfigDb() throws Exception;
	
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
	
	public void saveConfig(String name, String val, String description);
	default public void saveConfig(String name, String val) {
		this.saveConfig(name, val, null);
	}
	
	public String getConfig(String name);
	
	public Map<String, String> getConfig();
	
	//===============================================
	public void saveLog(LogLevel level, String type, String conent);
	public LogEntity getLog(long id);
	
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level, String type);
	
	default public List<LogEntity> getLog(Date begTime, Date endTime, String type) {
		return this.getLog(begTime, endTime, null, type);
	}
	default public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level) {
		return this.getLog(begTime, endTime, level, null);
	}
	default public List<LogEntity> getLog(Date begTime, Date endTime) {
		return this.getLog(begTime, endTime, null, null);
	}
	
	public void clearLog(long id);
	public void clearLog(Date begTime, Date endTime, LogLevel level, String type);
	default public void clearLog(Date endTime) {
		this.clearLog(null, endTime, null, null);
	}
	default public void clearLog(Date endTime, LogLevel level) {
		this.clearLog(null, endTime, level, null);
	}
	default public void clearLog(Date endTime, String type) {
		this.clearLog(null, endTime, null, type);
	}
	default public void clearLog(Date endTime, LogLevel level, String type) {
		this.clearLog(null, endTime, level, type);
	}
	default public void clearLog(Date begTime, Date endTime) {
		this.clearLog(begTime, endTime, null, null);
	}
	default public void clearLog(Date begTime, Date endTime, LogLevel level) {
		this.clearLog(begTime, endTime, level, null);
	}
	default public void clearLog(Date begTime, Date endTime, String type) {
		this.clearLog(begTime, endTime, null, type);
	}
	//===============================================

}
