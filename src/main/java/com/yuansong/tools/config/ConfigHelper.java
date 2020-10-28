package com.yuansong.tools.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 配置组件接口
 * @author yuansong
 *
 */

public interface ConfigHelper {
	
	/**
	 * 生成唯一码（默认guid）
	 * @return
	 */
	public String createIdCode();
	
	/**
	 * 保存唯一码
	 * @param idCode
	 */
	public void saveIdCode(String idCode);
	
	/**
	 * 已默认方式保存唯一码
	 */
	public default void saveIdCode() {
		this.saveIdCode(this.createIdCode());
	}
	
	/**
	 * 获取唯一码
	 * @return
	 */
	public String getIdCode();
	
	/**
	 * 保存配置项
	 * @param name 配置名称
	 * @param val 配置值
	 */
	public void saveConfig(String name, String val);
	
	/**
	 * 保存配置项
	 * @param name
	 * @param val
	 * @param description
	 */
	public void saveConfig(String name, String val, String description);
	
	/**
	 * 删除配置项
	 * @param name
	 */
	public void removeConfig(String name);

	/**
	 * 获取配置项
	 * @param name
	 * @return
	 */
	public String getConfig(String name);
	
	/**
	 * 获取配置项
	 * @param name
	 * @param def
	 * @return
	 */
	public String getConfig(String name, String def);
	
	/**
	 * 获取所有配置项
	 * @return
	 */
	public Map<String, String> getConfig();
	
	/**
	 * 保存日志
	 * @param level
	 * @param type
	 * @param content
	 */
	public void saveLog(LogLevel level, String type, String content);
	
	/**
	 * 获取指定日志
	 * @param id
	 * @return
	 */
	public LogEntity getLog(long id);
	
	/**
	 * 获取日志列表
	 * @param begTime
	 * @param endTime
	 * @return
	 */
	public List<LogEntity> getLog(Date begTime, Date endTime);
	/**
	 * 获取日志列表
	 * @param begTime
	 * @param endTime
	 * @param level
	 * @return
	 */
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level);
	/**
	 * 获取日志列表
	 * @param begTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	public List<LogEntity> getLog(Date begTime, Date endTime, String type);
	/**
	 * 获取日志列表
	 * @param begTime
	 * @param endTime
	 * @param level
	 * @param type
	 * @return
	 */
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level, String type);
	
	/**
	 * 清理日志
	 * @param id
	 */
	public void clearLog(long id);
	/**
	 * 清理日志
	 * @param endTime
	 */
	public void clearLog(Date endTime);
	/**
	 * 清理日志
	 * @param endTime
	 * @param level
	 */
	public void clearLog(Date endTime, LogLevel level);
	/**
	 * 清理日志
	 * @param endTime
	 * @param type
	 */
	public void clearLog(Date endTime, String type);
	/**
	 * 清理日志
	 * @param endTime
	 * @param level
	 * @param type
	 */
	public void clearLog(Date endTime, LogLevel level, String type);
	/**
	 * 清理日志
	 * @param begTime
	 * @param endTime
	 */
	public void clearLog(Date begTime, Date endTime);
	/**
	 * 清理日志
	 * @param begTime
	 * @param endTime
	 * @param level
	 */
	public void clearLog(Date begTime, Date endTime, LogLevel level);
	/**
	 * 清理日志
	 * @param begTime
	 * @param endTime
	 * @param type
	 */
	public void clearLog(Date begTime, Date endTime, String type);
	/**
	 * 清理日志
	 * @param begTime
	 * @param endTime
	 * @param level
	 * @param type
	 */
	public void clearLog(Date begTime, Date endTime, LogLevel level, String type);
	
	/**
	 * 检查并初始化配置库
	 * @throws Exception
	 */
	public void checkAndInitDb() throws Exception;
	
	
}
