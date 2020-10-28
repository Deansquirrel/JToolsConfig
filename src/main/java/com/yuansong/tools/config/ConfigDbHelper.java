package com.yuansong.tools.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.yuansong.tools.common.CommonTool;
import com.yuansong.tools.common.DateTool;

@Component
class ConfigDbHelper {
	
	@Autowired
	@Qualifier("jdbcTemplateJToolsConfig")
	private JdbcTemplate jdbcTemplate;
	
	private static final String currVersion = "0.0.0";
	
	private String createSql_0_0_0_version = ""
			+ "DROP TABLE IF EXISTS 'version'; "
			+ "CREATE TABLE 'version' ( "
			+ "  'version' TEXT NOT NULL, "
			+ "  'update_time' TEXT NOT NULL, "
			+ "  'create_time' TEXT NOT NULL, "
			+ "  PRIMARY KEY ('version') ); ";
	
	private String initSql_0_0_0_version = ""
			+ "INSERT INTO version(version, update_time,  create_time) "
			+ "VALUES (?, ?, ?); ";
	
	private String createSql_0_0_0_config = ""		
			+ "DROP TABLE IF EXISTS 'config'; "
			+ "CREATE TABLE 'config' ( "
			+ "  'name' text NOT NULL, "
			+ "  'val' text, "
			+ "  'description' text, "
			+ "  'last_update' text NOT NULL, "
			+ "  PRIMARY KEY ('name') );";
	
	private String initSql_0_0_0_config = ""
			+ "INSERT INTO config(name, val, description, last_update) "
			+ "VALUES ('id', ?, ?, ?); ";
	
	private String createSql_0_0_0_log = ""
			+ "DROP TABLE IF EXISTS 'log'; "
			+ "CREATE TABLE 'log' ( "
			+ "  'id' INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "  'level' text NOT NULL, "
			+ "  'type' text NOT NULL, "
			+ "  'content' text NOT NULL, "
			+ "  'time' DATETIME NOT NULL);";
	
	/**
	 * 检查并更新配置库结构
	 * @param currVersion
	 * @param version
	 */
	public void checkAndUpdate() {
		if(!this.checkTables()) {
			this.initTables();
		}
	}
	
	private boolean checkTables() {
		if(!this.isTableExists("version")) {
			return false;
		}
		if(!this.isColumnExists("version", "version")) {
			return false;
		}
		String sql = "select count(*) from version";
		Integer vCount = this.jdbcTemplate.queryForObject(sql, Integer.class);
		if(vCount == null || vCount != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 初始化数据库表
	 */
	private void initTables() {
		this.jdbcTemplate.update(this.createSql_0_0_0_version);
		this.jdbcTemplate.update(this.createSql_0_0_0_config);
		this.jdbcTemplate.update(this.createSql_0_0_0_log);
		String curr = DateTool.GetDatetimeStr();
		this.jdbcTemplate.update(initSql_0_0_0_version, new Object[] {currVersion, curr, curr});
		this.jdbcTemplate.update(initSql_0_0_0_config, new Object[] {CommonTool.UUID(), Constants.idCodeRemark, curr});
	}

	/**
	 * 检查SQLite表是否存在
	 * @param name
	 * @return
	 */
	public boolean isTableExists(String name) {
		String sql = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name = ?";
		return this.jdbcTemplate.queryForObject(sql, new Object[] {name}, Integer.class) > 0;
	}
	
	/**
	 * 检查SQLite表中的列是否存在
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public boolean isColumnExists(String tableName, String columnName) {
		List<String> columnList = this.jdbcTemplate.query("PRAGMA table_info(" + tableName + ")", new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("name");
			}
			
		});
		return columnList.contains(columnName);
	}
}
