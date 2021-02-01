package com.yuansong.tools.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.yuansong.tools.config.log.LogEntity;
import com.yuansong.tools.config.log.LogLevel;

@Component
class ToolsConfigHelperImpl implements IToolsConfigHelper {

	@Override
	public void checkAndInitConfigDb() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getClientId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateClientId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveConfig(String name, String val, String description) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getConfig(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveLog(LogLevel level, String type, String conent) {
		// TODO Auto-generated method stub

	}

	@Override
	public LogEntity getLog(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LogEntity> getLog(Date begTime, Date endTime, LogLevel level, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearLog(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearLog(Date begTime, Date endTime, LogLevel level, String type) {
		// TODO Auto-generated method stub

	}

}
