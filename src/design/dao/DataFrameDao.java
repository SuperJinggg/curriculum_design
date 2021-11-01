package design.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import design.entity.DataFrame;

/**
 * 
 * 进行数据帧管理的DAO
 * @author xuhui
 *
 */
public class DataFrameDao {
	
	protected JdbcTemplate jdbc;
	
	@Autowired
	public DataFrameDao(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}
	
	/**
	 * sql语句查询当前除固定位和备用位之外的所有有用字段
	 * @return
	 */
	public List<Map<String, Object>> getDataFrame()
	{
		String sql = " select df.deviceCoding, df.deviceAddr, df.stateUpInterval, df.startOutTime,"
				+ " df.settingTem, df.TemCBias, df.deviceState, df.compreRunState, df.percepTem,"
				+ " df.drawer1, df.drawer2, df.drawer3, df.drawer4, df.drawer5, df.drawer6,"
				+ " df.drawer7, df.drawer8, df.drawer9, df.drawer10"
				+ " from dataframe df"
				+ " where df.dataFrameId = 1";
		return jdbc.queryForList(sql);
	}
	
	
	/**
	 * sql语句更改数据帧内容
	 * @param dataFrame
	 * @return
	 */
	public int updateDataFrame(DataFrame dataFrame)
	{
		String sql = " update dataframe df set df.deviceCoding = ?, df.deviceAddr = ?,"
				+ " df.stateUpInterval = ?, df.startOutTime = ?, df.settingTem = ?,"
				+ " df.TemCBias = ?, df.deviceState = ?, df.compreRunState = ?, df.percepTem = ?,"
				+ " df.drawer1 = ?, df.drawer2 = ?, df.drawer3 = ?, df.drawer4 = ?, df.drawer5 = ?,"
				+ " df.drawer6 = ?, df.drawer7 = ?, df.drawer8 = ?, df.drawer9 = ?, df.drawer10 = ?"
				+ " where df.dataFrameId = ?";
		return jdbc.update(sql, dataFrame.deviceCoding, dataFrame.deviceAddr, dataFrame.stateUpInterval,
				  dataFrame.startOutTime, dataFrame.settingTem,dataFrame.TemCBias,
				  dataFrame.deviceState, dataFrame.compreRunState, dataFrame.percepTem,
				  dataFrame.drawer1, dataFrame.drawer2, dataFrame.drawer3, dataFrame.drawer4, 
				  dataFrame.drawer5, dataFrame.drawer6, dataFrame.drawer7, dataFrame.drawer8, 
				  dataFrame.drawer9, dataFrame.drawer10, dataFrame.dataFrameId);
	}
	
	/**
	 * 获取当前设备的地址
	 * @return
	 */
	public List<Map<String, Object>> getDeviceAddr()
	{
		String sql = " select df.deviceAddr"
				+ " from dataframe df"
				+ " where df.dataFrameId = 1";
		return jdbc.queryForList(sql);
	}
	
}
