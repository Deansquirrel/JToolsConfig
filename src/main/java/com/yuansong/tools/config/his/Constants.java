package com.yuansong.tools.config.his;

import java.io.File;

public class Constants {
	
	public static final String idCodeRemark = "唯一身份码";
	
	public static final String dbName = "config";
	public static final String dbPath = "db";
	
	public static String getDbFullPath() {
		if(Constants.dbPath.equals("")) {
			return dbName;
		} else {
			return dbPath + File.separator + dbName;
		}
	}

}
