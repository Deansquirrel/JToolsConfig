package com.yuansong.tools.config;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuansong.tools.common.CommonTool;
import com.yuansong.tools.common.DateTool;

@Component
public class ConfigHelperImpl implements ConfigHelper {
	
	@Autowired
	@Qualifier("jdbcTemplateJToolsConfig")
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ConfigDbHelper configDbHelper;

	@Override
	public String createIdCode() {
		return CommonTool.UUID();
	}

	@Override
	public void saveIdCode(String idCode) {
		this.saveConfig("id", idCode);
	}

	@Override
	public String getIdCode() {
		return this.getConfig("id");
	}

	@Override
	public void saveConfig(String name, String val) {
		this.saveConfig(name, val, "");
	}
	
	@Override
	@Transactional
	public void saveConfig(String name, String val, String description) {
		String sqlUpdate = ""
				+ "UPDATE config SET val=?,description=?,last_update=? WHERE name= ?; ";
		String sqlInsert = ""
				+ "INSERT INTO config (name, val, description, last_update) "
				+ "SELECT ?, ?, ?, ? "
				+ "WHERE (Select Changes() = 0);";
		this.jdbcTemplate.update(sqlUpdate, val, description, DateTool.GetDatetimeStr(), name);
		this.jdbcTemplate.update(sqlInsert, name, val, description, DateTool.GetDatetimeStr());
	}
	
	@Override
	public String getConfig(String name) {
		return this.getConfig(name, "");
	}

	@Override
	public String getConfig(String name, String def) {
		String sql = "select val from config where name = ?";
		List<String> list = this.jdbcTemplate.queryForList(sql, new Object[] {name}, String.class);
		if(list.size() > 0) {
			return list.get(0);
		} else {
			return def;
		}
	}

	@Override
	public Map<String, String> getConfig() {
		String sql = "select name, val from config";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		HashMap<String, String> result = new HashMap<String, String>();
		for(Map<String, Object> m : list) {
			result.put(m.get("name").toString(), m.get("val").toString());
		}
		return result;
	}

	@Override
	public LogEntity getLog(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveLog(LogLevel level, String type, String content) {
		String sql = "INSERT INTO log(level, type, content, time) SELECT ?, ?, ?, ?";
		this.jdbcTemplate.update(sql, this.getLogLevel(level), type, content, DateTool.GetDatetimeStr());
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkAndInitDb() throws Exception {
		if(!Constants.dbPath.equals("")) {
			this.checkDbPath();
		}
		this.configDbHelper.checkAndUpdate();
	}
	
	//检查数据库路径是否存在，如果不存在则创建
	private void checkDbPath() {
		File file = new File(Constants.dbPath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	//获取日志级别字符串
	private String getLogLevel(LogLevel level) {
		switch(level) {
		case Debug:
			return "DEBUG";
		case Info:
			return "INFO";
		case Warn:
			return "WARN";
		case Error:
			return "ERROR";
			default:
				return "DEBUG";
		}
	}
	//获取日志级别
	private LogLevel getLogLevel(String level) {
		switch(level) {
		case "DEBUG":
			return LogLevel.Debug;
		case "INFO":
			return LogLevel.Info;
		case "WARN":
			return LogLevel.Warn;
		case "ERROR":
			return LogLevel.Error;
			default:
				return LogLevel.Debug;
		}
	}

}
