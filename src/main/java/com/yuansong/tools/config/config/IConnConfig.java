package com.yuansong.tools.config.config;

import java.io.File;

public interface IConnConfig {

	public String getDbName();
	
	public String getDbPath();
	
	default public String getDbFullPath() {
		if("".equals(this.getDbPath().trim())) {
			return this.getDbName();
		} else {
			return this.getDbPath().trim() + File.separator + this.getDbName().trim();
		}
	}
	
}
