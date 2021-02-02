package com.yuansong.tools.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.lang.NonNull;

import com.yuansong.tools.config.log.LogEntity;
import com.yuansong.tools.config.log.LogLevel;

public interface IToolsConfigHelper {
	
	public static final long VERSION = 1L;
	
	public static final String CONN_NAME = "conn_config";
	public static final String BEAN_JDBCTEMPLATE = "jdbcTemplateToolsConfig";
	public static final String BEAN_TX_MANAGER = "txManagerToolsConfig";
	public static final String KEY_CLIENT_ID = "clientId";
	
	public void checkAndInitConfigDb() throws Exception;
	
	/**
	 * 获取客户端ID
	 * @return
	 */
	public String getClientId();
	
	/**
	 * 更新客户端ID
	 * @param cliendId
	 */
	public void updateClientId(String cliendId);
	
	public void saveConfig(String name, String val, String description);
	default public void saveConfig(String name, String val) {
		this.saveConfig(name, val, null);
	}
	
	public String getConfig(String name, String def);
	default public String getConfig(String name) {
		return this.getConfig(name, null);
	};
	
	public void removeConfig(String name);
	
	public Map<String, String> getConfig();
	
	//===============================================
	public void saveLog(LogLevel level, String type, String content);
	public LogEntity getLog(long id);
	
	public List<LogEntity> getLog(@NonNull Date begTime, @NonNull Date endTime, LogLevel level, String type);
	
	default public List<LogEntity> getLog(@NonNull Date begTime, @NonNull Date endTime, String type) {
		return this.getLog(begTime, endTime, null, type);
	}
	default public List<LogEntity> getLog(@NonNull Date begTime, @NonNull Date endTime, LogLevel level) {
		return this.getLog(begTime, endTime, level, null);
	}
	default public List<LogEntity> getLog(@NonNull Date begTime, @NonNull Date endTime) {
		return this.getLog(begTime, endTime, null, null);
	}
	
	public void clearLog(long id);
	public void clearLog(Date begTime, @NonNull Date endTime, LogLevel level, String type);
	default public void clearLog(@NonNull Date endTime) {
		this.clearLog(null, endTime, null, null);
	}
	default public void clearLog(@NonNull Date endTime, @NonNull LogLevel level) {
		this.clearLog(null, endTime, level, null);
	}
	default public void clearLog(@NonNull Date endTime, @NonNull String type) {
		this.clearLog(null, endTime, null, type);
	}
	default public void clearLog(@NonNull Date endTime, @NonNull LogLevel level, @NonNull String type) {
		this.clearLog(null, endTime, level, type);
	}
	default public void clearLog(@NonNull Date begTime, @NonNull Date endTime) {
		this.clearLog(begTime, endTime, null, null);
	}
	default public void clearLog(@NonNull Date begTime, @NonNull Date endTime, @NonNull LogLevel level) {
		this.clearLog(begTime, endTime, level, null);
	}
	default public void clearLog(@NonNull Date begTime, @NonNull Date endTime, @NonNull String type) {
		this.clearLog(begTime, endTime, null, type);
	}
	//===============================================

}
