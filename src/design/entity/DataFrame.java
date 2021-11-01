package design.entity;

public class DataFrame {
	
	//数据库中帧序列号
	public int dataFrameId;
	//设备编码
	public String deviceCoding;
	//设备地址
	public int deviceAddr;
	//状态上传时间间隔
	public int stateUpInterval;
	//压缩机启动时延
	public int startOutTime;
	//设定温度
	public double settingTem;
	//控制温度偏差
	public int TemCBias;
	//系统状态
	public int deviceState;
	//压缩机运行状态
	public int compreRunState;
	//采集温度
	public double percepTem;
	
	//各抽屉开关状态
	public int drawer1;
	public int drawer2;
	public int drawer3;
	public int drawer4;
	public int drawer5;
	public int drawer6;
	public int drawer7;
	public int drawer8;
	public int drawer9;
	public int drawer10;
}
