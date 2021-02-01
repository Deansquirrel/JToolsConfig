package com.yuansong.tools.config.conn;

import java.io.File;

public interface IConnConfig {

	public String getDbName();
	
	public String getDbPath();
	
	default public String getDbFullPath() {
		if("".equals(this.getDbPath().trim())) {
			return this.getDbName();
		} else {
			return this.getDbPath() + File.separator + this.getDbName();
		}
	}
	
}
