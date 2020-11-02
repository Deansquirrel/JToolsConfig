package com.yuansong.tools.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.yuansong.tools.common.ExceptionTool;
import com.yuansong.tools.db.DBConfigHelper;
import com.yuansong.tools.db.DataSourceHelper;
import com.yuansong.tools.db.DataSourceType;
import com.yuansong.tools.db.IDBConfig;

@Component
class DbConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(DbConfig.class);
	
	@Autowired
	private DBConfigHelper dbConfigHelper;
	
	@Autowired
	private DataSourceHelper dataSourceHelper;
	
	@Bean(name="dataSourceJToolsConfig")
	public DataSource getDataSource() {
		IDBConfig config = this.dbConfigHelper.getSQLiteDbConfig(Constants.getDbFullPath());
		try {
			return this.dataSourceHelper.getDataSource(config, DataSourceType.SQLite);
		} catch (Exception e) {
			logger.error(ExceptionTool.getStackTrace(e));
			return null;
		}
	}
	
	@Bean(name="jdbcTemplateJToolsConfig")
	public JdbcTemplate jdbcTemplate(@Qualifier("dataSourceJToolsConfig") DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	@Bean(name="txManagerJToolsConfig")
	public PlatformTransactionManager txManager(@Qualifier("dataSourceJToolsConfig") DataSource ds) {
		return new DataSourceTransactionManager(ds);
	}

}
