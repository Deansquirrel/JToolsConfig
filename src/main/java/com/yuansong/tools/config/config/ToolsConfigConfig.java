package com.yuansong.tools.config.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfigConfig {

	@Bean
	@ConditionalOnMissingBean(IConnConfig.class)
	public IConnConfig getConnConfig() {
		return new IConnConfig() {

			@Override
			public String getDbName() {
				return "config.db";
			}

			@Override
			public String getDbPath() {
				return "";
			}};
	}
}
