package design.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import design.controllerObject.SettingTem;

public class SettingTemDao {
	
	protected JdbcTemplate jdbc;
	
	@Autowired
	public SettingTemDao(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}
	
	public List<Map<String, Object>> getSettingTem()
	{
		String sql = " select s.settingTemNum from settingtem s where s.settingId = 1";
		return jdbc.queryForList(sql);
	}
	
	public int updateSettingTem(SettingTem sTem)
	{
		String sql = " update settingtem s set s.settingTemNum = ? where s.settingId = ?";
		return jdbc.update(sql, sTem.settingTemNum, sTem.settingTemId);
	}
	
}
