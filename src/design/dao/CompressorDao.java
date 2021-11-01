package design.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import design.controllerObject.Compressor;

public class CompressorDao {
	
	protected JdbcTemplate jdbc;
	
	@Autowired
	public CompressorDao(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}
	
	/**
	 * sql语句查询当前抽屉状态（初始为全部关闭）
	 * @return
	 */
	public List<Map<String, Object>> getComState()
	{
		String sql = " select c.compressorState from compressor c where c.compressorId = 1";
		return jdbc.queryForList(sql);
	}
	

	/**
	 * 根据用户需求设置压缩机状态
	 * @param compressor
	 * @return
	 */
	public int updateComState(Compressor compressor)
	{
		String sql = " update compressor c set c.compressorState = ? where c.compressorId = ?";
		return jdbc.update(sql, compressor.compressorState, compressor.compressorId);
	}
}
