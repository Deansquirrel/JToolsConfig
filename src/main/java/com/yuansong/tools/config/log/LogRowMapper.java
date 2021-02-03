package com.yuansong.tools.config.log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.jdbc.core.RowMapper;

import com.github.deansquirrel.tools.common.DateTool;
import com.github.deansquirrel.tools.common.SQLTool;

public class LogRowMapper implements RowMapper<LogEntity> {

	@Override
	public LogEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		LogEntity d = new LogEntity();
		d.setId(SQLTool.getLong(rs, "id"));
		d.setLevel(SQLTool.getString(rs, "level"));
		d.setType(SQLTool.getString(rs, "type"));
		d.setContent(SQLTool.getString(rs, "content"));
		try {
			d.setTime(DateTool.ParseDateTimeStr(SQLTool.getString(rs, "time")));
		} catch (ParseException e) {
			d.setTime(null);
		} catch (SQLException e) {
			throw e;
		}
		return d;
	}
}
