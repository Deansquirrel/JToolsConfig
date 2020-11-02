package com.yuansong.tools.config;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuansong.tools.common.CommonTool;
import com.yuansong.tools.common.DateTool;
import com.yuansong.tools.common.SQLTool;

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
		this.saveConfig("id", idCode, Constants.idCodeRemark);
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
	@Transactional(value="txManagerJToolsConfig")
	public void saveConfig(String name, String val, String description) {
		String sqlUpdate = ""
				+ "UPDATE config SET val=?,description=?,last_update=? WHERE name= ? ";
		String sqlInsert = ""
				+ "INSERT INTO config (name, val, description, last_update) "
				+ "SELECT ?, ?, ?, ? "
				+ "WHERE (Select Changes() = 0)";
		this.jdbcTemplate.update(sqlUpdate, val, description, DateTool.GetDatetimeStr(), name);
		this.jdbcTemplate.update(sqlInsert, name, val, description, DateTool.GetDatetimeStr());
	}
	
	@Override
	public void removeConfig(String name) {
		String sql = ""
				+ "DELETE FROM config "
				+ "WHERE name = ?";
		this.jdbcTemplate.update(sql, name);
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
		String sql = ""
				+ "SELECT id, level, type, content, time "
				+ "FROM log "
				+ "WHERE id = ?";
		
		List<LogEntity> list = this.jdbcTemplate.query(sql, new Object[] {id}, new LogEntityRowMapper());
		if(list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void saveLog(LogLevel level, String type, String content) {
		String sql = "INSERT INTO log(level, type, content, time) SELECT ?, ?, ?, ?";
		this.jdbcTemplate.update(sql, this.getLogLevel(level), type, content, DateTool.GetDatetimeStr());
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime) {
		String sql = ""
				+ "SELECT id, level, type, content, time "
				+ "FROM log "
				+ "WHERE time >= ? and time < ?";
		return this.jdbcTemplate.query(sql, 
				new Object[] {DateTool.GetDatetimeStr(begTime), DateTool.GetDatetimeStr(endTime)}, 
				new LogEntityRowMapper());
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level) {
		String sql = ""
				+ "SELECT id, level, type, content, time "
				+ "FROM log "
				+ "WHERE time >= ? and time < ? and level = ?";
		return this.jdbcTemplate.query(sql, 
				new Object[] {DateTool.GetDatetimeStr(begTime), DateTool.GetDatetimeStr(endTime), this.getLogLevel(level)}, 
				new LogEntityRowMapper());
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime, String type) {
		String sql = ""
				+ "SELECT id, level, type, content, time "
				+ "FROM log "
				+ "WHERE time >= ? and time < ? and type = ?";
		return this.jdbcTemplate.query(sql, 
				new Object[] {DateTool.GetDatetimeStr(begTime), DateTool.GetDatetimeStr(endTime), type}, 
				new LogEntityRowMapper());
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level, String type) {
		String sql = ""
				+ "SELECT id, level, type, content, time "
				+ "FROM log "
				+ "WHERE time >= ? and time < ? and level = ? and type = ?";
		return this.jdbcTemplate.query(sql, 
				new Object[] {DateTool.GetDatetimeStr(begTime), DateTool.GetDatetimeStr(endTime), this.getLogLevel(level), type}, 
				new LogEntityRowMapper());
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

	@Override
	public void clearLog(long id) {
		String sql = "delete from log where id = ?";
		this.jdbcTemplate.update(sql, id);
	}

	@Override
	public void clearLog(Date endTime) {
		String sql = ""
				+ "DELETE FROM log "
				+ "WHERE time < ?";
		this.jdbcTemplate.update(sql, DateTool.GetDatetimeStr(endTime));
	}

	@Override
	public void clearLog(Date endTime, LogLevel level) {
		this.clearLog(null, endTime, level, null);
	}

	@Override
	public void clearLog(Date endTime, String type) {
		this.clearLog(null, endTime, null, type);
	}

	@Override
	public void clearLog(Date endTime, LogLevel level, String type) {
		this.clearLog(null, endTime, level, type);
	}

	@Override
	public void clearLog(Date begTime, Date endTime) {
		this.clearLog(begTime, endTime, null, null);
	}

	@Override
	public void clearLog(Date begTime, Date endTime, LogLevel level) {
		this.clearLog(begTime, endTime, level, null);
	}

	@Override
	public void clearLog(Date begTime, Date endTime, String type) {
		this.clearLog(begTime, endTime, null, type);
	}

	@Override
	public void clearLog(Date begTime, Date endTime, LogLevel level, String type) {
		ArrayList<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM log WHERE 1=1 ");
		if(begTime != null) {
			sb.append("and time >= ? ");
			list.add(DateTool.GetDatetimeStr(begTime));
		}
		if(endTime != null) {
			sb.append("and time < ? ");
			list.add(DateTool.GetDatetimeStr(endTime));
		}
		if(level != null) {
			sb.append("and level = ? ");
			list.add(this.getLogLevel(level));
		}
		if(type != null) {
			sb.append("and type = ?");
			list.add(type);
		}
		this.jdbcTemplate.update(sb.toString(), list.toArray());
	}
}

class LogEntityRowMapper implements RowMapper<LogEntity> {

	@Override
	public LogEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		LogEntity d = new LogEntity();
		d.setId(SQLTool.getLong(rs, "id"));
		d.setLevel(this.getLogLevel(SQLTool.getString(rs, "level")));
		d.setType(SQLTool.getString(rs, "type"));
		d.setContent(SQLTool.getString(rs, "content"));
		try {
			d.setTime(DateTool.ParseDatetimeStr(SQLTool.getString(rs, "time")));
		} catch (ParseException e) {
			d.setTime(null);
		} catch (SQLException e) {
			throw e;
		}
		return d;
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