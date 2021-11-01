package design.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import design.controllerObject.Drawers;

public class DrawersDao {
	
	protected JdbcTemplate jdbc;
	
	@Autowired
	public DrawersDao(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}
	

	/**
	 * 获取抽屉状态
	 * @return
	 */
	public List<Map<String, Object>> getDrawers()
	{
		String sql = " select"
				+ " d.drawer1, d.drawer2, d.drawer3, d.drawer4, d.drawer5, d.drawer6,"
				+ " d.drawer7, d.drawer8, d.drawer9, d.drawer10"
				+ " from drawers d"
				+ " where d.seq = 1";
		return jdbc.queryForList(sql);
	}
	
	/**
	 * 更新抽屉状态信息
	 * @param drawers
	 * @return
	 */
	public int updateDawers(Drawers drawers)
	{
		String sql = " update drawers d set "
				+ " d.drawer1 = ?, d.drawer2 = ?, d.drawer3 = ?, d.drawer4 = ?, d.drawer5 = ?,"
				+ " d.drawer6 = ?, d.drawer7 = ?, d.drawer8 = ?, d.drawer9 = ?, d.drawer10 = ?"
				+ " where d.seq = ?";
		return jdbc.update(sql,
				  drawers.drawer1, drawers.drawer2, drawers.drawer3, drawers.drawer4, 
				  drawers.drawer5, drawers.drawer6, drawers.drawer7, drawers.drawer8, 
				  drawers.drawer9, drawers.drawer10, drawers.seq);
	}
}
