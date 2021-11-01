package design.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import design.controllerObject.SysParam;

public class SysParamDao {

	protected JdbcTemplate jdbc;
	
	@Autowired
	public SysParamDao(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}
	
	
	public List<Map<String, Object>> getSysParam()
	{
		String sql = " select sp.deviceCoding, sp.deviceAddr, sp.stateUpInterval,"
				+ " sp.startOutTime, sp.settingTem, sp.TemCBias"
				+ " from sys_param sp"
				+ " where sp.sysParamId = 1";
		return jdbc.queryForList(sql);
	}
	
	public int updateSysParam(SysParam sysParam)
	{
		String sql = " update sys_param sp set"
				+ " sp.deviceCoding = ?, sp.deviceAddr = ?, sp.stateUpInterval = ?,"
				+ " sp.startOutTime = ?, sp.settingTem = ?, sp.TemCBias = ?"
				+ " where sp.sysParamId = ?";
		return jdbc.update(sql, sysParam.deviceCoding, sysParam.deviceAddr, sysParam.stateUpInterval, 
				sysParam.startOutTime, sysParam.settingTem, sysParam.TemCBias, 
				sysParam.sysParamId);
	}
	
}
