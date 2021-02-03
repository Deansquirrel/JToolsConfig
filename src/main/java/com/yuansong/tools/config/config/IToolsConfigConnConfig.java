package com.yuansong.tools.config.config;

import java.io.File;

public interface IToolsConfigConnConfig {

	public String getDbName();
	
	public String getDbPath();
	
	default public String getDbFullPath() {
		if("".equals(this.getDbPath().trim())) {
			return this.getDbName();
		} else {
			File file = new File(this.getDbPath());
			if(!file.exists()) {
				file.mkdirs();
			}
			return this.getDbPath().trim() + File.separator + this.getDbName().trim();
		}
	}
	
}
