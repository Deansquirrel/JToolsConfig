package com.yuansong.tools.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class ToolsConfigInit implements CommandLineRunner {
	
	@Autowired
	private IToolsConfigHelper toolsConfigHelper;

	@Override
	public void run(String... args) throws Exception {
		this.toolsConfigHelper.checkAndInitConfigDb();
	}

}
