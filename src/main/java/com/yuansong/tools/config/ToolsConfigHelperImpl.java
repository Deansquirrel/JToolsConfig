package com.yuansong.tools.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.deansquirrel.tools.common.CommonTool;
import com.github.deansquirrel.tools.common.DateTool;
import com.yuansong.tools.config.log.LogEntity;
import com.yuansong.tools.config.log.LogLevel;
import com.yuansong.tools.config.log.LogRowMapper;

@Component
class ToolsConfigHelperImpl implements IToolsConfigHelper {

	@Autowired
	@Qualifier(value = IToolsConfigHelper.BEAN_JDBCTEMPLATE)
	private JdbcTemplate jdbcTemplate;

	@Override
	public void checkAndInitConfigDb() throws Exception {
		// 检查版本表是否存在
		if (!this.isTableExists("version")) {
			this.initConfigDb();
		}
		// 检查版本是否匹配当前版本
		long currVersion = this.getVersion();
		if (currVersion < IToolsConfigHelper.VERSION) {
			// TODO 运行升级脚本
		}
	}

	/**
	 * 检查SQLite表是否存在
	 * 
	 * @param name
	 * @return
	 */
	private boolean isTableExists(String name) {
		String sql = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name = ?";
		return this.jdbcTemplate.queryForObject(sql, new Object[] { name }, Integer.class) > 0;
	}

	private void initConfigDb() throws Exception {
		String createSql_0_version = "" + "DROP TABLE IF EXISTS 'version'; " + "CREATE TABLE 'version' ( "
				+ "  'version' INTEGER NOT NULL, " + "  'update_time' TEXT NOT NULL, "
				+ "  'create_time' TEXT NOT NULL, " + "  PRIMARY KEY ('version') ); ";

		this.jdbcTemplate.update(createSql_0_version);

		String initSql_0_version = "" + "INSERT INTO version(version, update_time,  create_time) "
				+ "VALUES (?, ?, ?); ";

		String curr = DateTool.GetDateTimeStr();
		this.jdbcTemplate.update(initSql_0_version, new Object[] { IToolsConfigHelper.VERSION, curr, curr });

		String createSql_0_config = "" + "DROP TABLE IF EXISTS 'config'; " + "CREATE TABLE 'config' ( "
				+ "  'name' text NOT NULL, " + "  'val' text, " + "  'description' text, "
				+ "  'last_update' text NOT NULL, " + "  PRIMARY KEY ('name') );";

		this.jdbcTemplate.update(createSql_0_config);
		this.saveConfig(KEY_CLIENT_ID, CommonTool.UUID());

		String createSql_0_log = "" + "DROP TABLE IF EXISTS 'log'; " + "CREATE TABLE 'log' ( "
				+ "  'id' INTEGER PRIMARY KEY AUTOINCREMENT, " + "  'level' text NOT NULL, "
				+ "  'type' text NOT NULL, " + "  'content' text NOT NULL, " + "  'time' DATETIME NOT NULL);";

		this.jdbcTemplate.update(createSql_0_log);
		this.saveLog(LogLevel.Info, "ToolsConfig", "SQLite init");
	}

	private long getVersion() {
		String sql = "select max(version) from version";
		Long v = this.jdbcTemplate.queryForObject(sql, Long.class);
		if (v == null || v > IToolsConfigHelper.VERSION) {
			return 0;
		} else {
			return v;
		}
	}

	@Override
	public String getClientId() {
		return this.getConfig(IToolsConfigHelper.KEY_CLIENT_ID);
	}

	@Override
	public void updateClientId(String clientId) {
		this.saveConfig(IToolsConfigHelper.KEY_CLIENT_ID, clientId);
	}

	@Override
	@Transactional(value = IToolsConfigHelper.BEAN_TX_MANAGER)
	public void saveConfig(String name, String val, String description) {
		String sqlUpdate = "" + "UPDATE config SET val=?,description=?,last_update=? WHERE name= ? ";
		String sqlInsert = "" + "INSERT INTO config (name, val, description, last_update) " + "SELECT ?, ?, ?, ? "
				+ "WHERE (Select Changes() = 0)";
		this.jdbcTemplate.update(sqlUpdate, val, description, DateTool.GetDateTimeStr(), name);
		this.jdbcTemplate.update(sqlInsert, name, val, description, DateTool.GetDateTimeStr());
	}

	@Override
	public String getConfig(String name, String def) {
		String sql = "select val from config where name = ?";
		List<String> list = this.jdbcTemplate.queryForList(sql, new Object[] { name }, String.class);
		if (list.size() > 0) {
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
		for (Map<String, Object> m : list) {
			result.put(m.get("name").toString(), m.get("val").toString());
		}
		return result;
	}

	@Override
	public void removeConfig(String name) {
		String sql = "DELETE FROM config WHERE name = ?";
		this.jdbcTemplate.update(sql, name);
	}

	@Override
	public void saveLog(LogLevel level, String type, String content) {
		String sql = "INSERT INTO log(level, type, content, time) SELECT ?, ?, ?, ?";
		this.jdbcTemplate.update(sql, this.getLogLevel(level), type, content, DateTool.GetDateTimeStr());
	}

	// 获取日志级别字符串
	private String getLogLevel(LogLevel level) {
		switch (level) {
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
	public LogEntity getLog(long id) {
		String sql = "" + "SELECT id, level, type, content, time " + "FROM log " + "WHERE id = ?";

		List<LogEntity> list = this.jdbcTemplate.query(sql, new Object[] { id }, new LogRowMapper());
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level, String type) {
		List<Object> arg = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT id, level, type, content, time " + "FROM log " + "WHERE 1=1 ");
		if (begTime != null) {
			sb.append(" and time >= ? ");
			arg.add(DateTool.GetDateTimeStr(begTime));
		}
		if (endTime != null) {
			sb.append(" and time < ? ");
			arg.add(DateTool.GetDateTimeStr(endTime));
		}
		if (level != null) {
			sb.append(" and level = ? ");
			arg.add(this.getLogLevel(level));
		}
		if (type != null && !"".equals(type)) {
			sb.append(" and type = ? ");
			arg.add(type);
		}
		return this.jdbcTemplate.query(sb.toString(), arg.toArray(), new LogRowMapper());
	}

	@Override
	public void clearLog(long id) {
		String sql = "delete from log where id = ?";
		this.jdbcTemplate.update(sql, id);
	}

	@Override
	public void clearLog(Date begTime, Date endTime, LogLevel level, String type) {
		List<Object> arg = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM log " + "WHERE 1=1 ");
		if (begTime != null) {
			sb.append(" and time >= ? ");
			arg.add(DateTool.GetDateTimeStr(begTime));
		}
		if (endTime != null) {
			sb.append(" and time < ? ");
			arg.add(DateTool.GetDateTimeStr(endTime));
		}
		if (level != null) {
			sb.append(" and level = ? ");
			arg.add(this.getLogLevel(level));
		}
		if (type != null && !"".equals(type)) {
			sb.append(" and type = ? ");
			arg.add(type);
		}
		this.jdbcTemplate.update(sb.toString(), arg.toArray());
	}

}
