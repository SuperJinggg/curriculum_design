package design.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import design.controllerObject.DeCoding;

public class DeCodingDao {
	protected JdbcTemplate jdbc;
	
	@Autowired
	public DeCodingDao(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}
	
	/**
	 * 查看应进行通信的设备编码
	 * @return
	 */
	public List<Map<String, Object>> getDeCoding()
	{
		String sql = " select d.deviceCoding from decoding d where d.deviceCodingId = 1";
		return jdbc.queryForList(sql);
	}
	
	/**
	 * 根据用户要求更改设备编码
	 * @param deCoding
	 * @return
	 */
	public int updateDeviceCoding(DeCoding deCoding)
	{
		String sql = " update decoding d set d.deviceCoding = ? where d.deviceCodingId = ?";
		return jdbc.update(sql, deCoding.deviceCoding, deCoding.deviceCodingId);
	}
	

	public List<Map<String, Object>> getDeAddress()
	{
		String sql = " select d.deviceAddress from decoding d where d.deviceCodingId = 1";
		return jdbc.queryForList(sql);
	}
	

	public int updateDeAddress(DeCoding deCoding)
	{
		String sql = " update decoding d set d.deviceAddress = ? where d.deviceCodingId = ?";
		return jdbc.update(sql, deCoding.deviceAddress, deCoding.deviceCodingId);
	}
}
