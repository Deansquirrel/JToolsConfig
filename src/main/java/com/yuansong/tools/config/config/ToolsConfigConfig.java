package com.yuansong.tools.config.config;

import java.text.MessageFormat;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuansong.tools.config.IToolsConfigHelper;

@Configuration
public class ToolsConfigConfig {
	
	@Autowired
	private IToolsConfigConnConfig connConfig;

	@Bean
	@ConditionalOnMissingBean(IToolsConfigConnConfig.class)
	public IToolsConfigConnConfig getConnConfig() {
		return new IToolsConfigConnConfig() {

			@Override
			public String getDbName() {
				return "config.db";
			}

			@Override
			public String getDbPath() {
				return "";
			}};
	}
	
	@Bean(name="ToolsDbDataSource")
	public DataSource getDataSource() {
		DruidDataSource ds = new DruidDataSource();
		ds.setName(IToolsConfigHelper.CONN_NAME);
		ds.setUrl(MessageFormat.format("jdbc:sqlite:{0}", connConfig.getDbFullPath()));
		ds.setMinIdle(0);
		ds.setInitialSize(1);
		ds.setMaxActive(30);
		ds.setMaxWait(10000);
		ds.setQueryTimeout(10);
		ds.setValidationQuery("SELECT 1");
		ds.setTimeBetweenEvictionRunsMillis(60000);
		ds.setMinEvictableIdleTimeMillis(30000);
		ds.setTimeBetweenConnectErrorMillis(15 * 1000);
		return ds;
	}
	
	@Bean(name=IToolsConfigHelper.BEAN_JDBCTEMPLATE)
	public JdbcTemplate getJdbcTemplate(@Qualifier("ToolsDbDataSource") DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	@Bean(name=IToolsConfigHelper.BEAN_TX_MANAGER)
	public PlatformTransactionManager getTxManager(@Qualifier("ToolsDbDataSource") DataSource ds) {
		return new DataSourceTransactionManager(ds);
	}
}
