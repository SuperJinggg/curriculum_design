package design.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import design.controllerObject.Compressor;
import design.controllerObject.Drawers;
import design.controllerObject.SettingTem;
import design.controllerObject.SysParam;
import design.dao.CompressorDao;
import design.dao.DataFrameDao;
import design.dao.DeCodingDao;
import design.dao.DrawersDao;
import design.dao.SettingTemDao;
import design.dao.SysParamDao;
import design.entity.DataFrame;
import design.util.Util;

/**
 * 
 * 端口管理的Service
 * @author xuhui
 *
 */
@Service
public class PortService {
	
	@Autowired
	CompressorDao comdao;
	
	@Autowired
	DataFrameDao dfdao;
	
	@Autowired
	DrawersDao draDao;
	
	@Autowired
	SettingTemDao sTemDao;
	
	@Autowired
	SysParamDao spDao;
	
	@Autowired
	DeCodingDao dcDao;

	
	//帧号
	public static int frameSeq = 0;
	
	// 终端控制板参数
	// 设备编码 12-21
	private static char[] deviceId = new char[10];
	// 设备地址 22-23
	private static char[] deviceAddr = new char[2];
	// 1个字节备用，占位char[2] 24-25
	// 状态上传时间间隔 26-27
	private static char[] stateUpInter = new char[2];
	// 压缩机启动时延 28-29
	private static char[] startOutTime = new char[2];
	// 2个字节备用，占位char[4] 30-33
	// 设定温度 34-35
	private static char[] setTem = new char[2];
	// 控制温度偏差 36-37
	private static char[] TemCBias = new char[2];
	// 2个字节备用，占位char[4] 38-41
	// 2个字节备用，占位char[4] 42-45
	// 1个字节备用，占位char[2] 46-47

	// 终端控制板状态
	// 5个字节，设备编码，重复，占位char[10] 48-57
	// 系统状态 58-59
	private static char[] sysStateC = new char[2];
	// 1个字节备用，占位char[2] 60-61
	// 压缩机运行状态 62-63
	private static char[] runState = new char[2];
	// 1个字节，设定温度，重复，占位char[2] 64-65
	// 采集温度 66-67
	private static char[] percepTem = new char[2];
	// 2个字节备用，占位char[4] 68-71
	// 抽屉开关状态 72-75
	private static char[] switchState = new char[4];
	// 2个字节备用，占位char[4] 76-79

	
	/**
	 * 分析串口读取出来的数据帧，将其转换为数据库中数据字段格式，并更新数据库内容
	 * @param frame
	 */
	public void dataLoad(String frame) {
		char[] fourBit = frame.toCharArray();

		// 对给参数赋值，并传给对应处理函数

		//设备编号
		dataSegSet(deviceId, fourBit, 12);
		long deId = disDataSet(deviceId);

		//设备地址
		dataSegSet(deviceAddr, fourBit, 22);
		long deAddr = disDataSet(deviceAddr);

		//状态上传间隔
		dataSegSet(stateUpInter, fourBit, 26);
		long upInternal = disDataSet(stateUpInter);

		//压缩机启动时延
		dataSegSet(startOutTime, fourBit, 28);
		long outTime = disDataSet(startOutTime);

		//控制温度
		dataSegSet(setTem, fourBit, 34);
		double CTem = disTemSet(setTem);
		System.out.println("我是控制温度                         "+ CTem +"！！！！！！！！！！！！");

		//温度控制偏差
		dataSegSet(TemCBias, fourBit, 36);
		long temConBias = disDataSet(TemCBias);

		//系统状态
		dataSegSet(sysStateC, fourBit, 58);
		long sysStateL = disDataSet(sysStateC);

		//压缩机状态
		dataSegSet(runState, fourBit, 62);
		long compreState = disDataSet(runState);

		//采集温度
		dataSegSet(percepTem, fourBit, 66);
		double currentTemD = disTemSet(percepTem);
		System.out.println("我是采集到的温度                         "+ currentTemD +"！！！！！！！！！！！！");

		//抽屉开关状态
		dataSegSet(switchState, fourBit, 72);
		String drawersS = disDrawersSet(switchState);

		
		long correctDeCoding = Long.decode("68736253953");
		List<Map<String, Object>> result = dcDao.getDeCoding();
		for (Map<String, Object> map : result) {
			correctDeCoding = Long.decode((String) map.get("deviceCoding"));
		}
		System.out.println("我是通过串口传过来的设备编址：" + deId);
		System.out.println("我是数据库表中的设备编址：" + correctDeCoding);
		
		int correctDeAddr = 1;
		result = dcDao.getDeAddress();
		for (Map<String, Object> map : result) {
			correctDeAddr = (int) map.get("deviceAddress");
		}
		System.out.println("我是通过串口传过来的设备地址：" + deAddr);
		System.out.println("我是数据库表中的设备地址：" + correctDeAddr);
		
		if(deId == correctDeCoding && deAddr == correctDeAddr) {
			//更新数据库中数据帧信息
			DataFrame dataFrame = new DataFrame();
			dataFrame.dataFrameId = 1;
			dataFrame.deviceCoding = String.valueOf(deId);
			dataFrame.deviceAddr = (int) deAddr;
			dataFrame.stateUpInterval = (int) upInternal;
			dataFrame.startOutTime = (int) outTime;
			dataFrame.settingTem = CTem;
			dataFrame.TemCBias = (int) temConBias;
			dataFrame.deviceState = (int) sysStateL;
			dataFrame.compreRunState = (int) compreState;
			dataFrame.percepTem = currentTemD;
			char[] switchB = drawersS.toCharArray();
			dataFrame.drawer1 = Integer.valueOf(String.valueOf(switchB[7]));
			dataFrame.drawer2 = Integer.valueOf(String.valueOf(switchB[6]));
			dataFrame.drawer3 = Integer.valueOf(String.valueOf(switchB[5]));
			dataFrame.drawer4 = Integer.valueOf(String.valueOf(switchB[4]));
			dataFrame.drawer5 = Integer.valueOf(String.valueOf(switchB[3]));
			dataFrame.drawer6 = Integer.valueOf(String.valueOf(switchB[2]));
			dataFrame.drawer7 = Integer.valueOf(String.valueOf(switchB[1]));
			dataFrame.drawer8 = Integer.valueOf(String.valueOf(switchB[0]));
			dataFrame.drawer9 = Integer.valueOf(String.valueOf(switchB[15]));
			dataFrame.drawer10= Integer.valueOf(String.valueOf(switchB[14]));
			System.out.println("uuuuuuuDataFrame    " + dfdao.updateDataFrame(dataFrame));
			
			//更新数据库中压缩机状态信息
			Compressor compressor = new Compressor();
			compressor.compressorId = 1;
			compressor.compressorState = (int) compreState;
			System.out.println("uuuuuuuCompressorState    " + comdao.updateComState(compressor));
			
			//更新数据库中抽屉开关状态信息
			Drawers drawers = new Drawers();
			drawers.seq = 1;
			drawers.drawer1 = Integer.valueOf(String.valueOf(switchB[7]));
			drawers.drawer2 = Integer.valueOf(String.valueOf(switchB[6]));
			drawers.drawer3 = Integer.valueOf(String.valueOf(switchB[5]));
			drawers.drawer4 = Integer.valueOf(String.valueOf(switchB[4]));
			drawers.drawer5 = Integer.valueOf(String.valueOf(switchB[3]));
			drawers.drawer6 = Integer.valueOf(String.valueOf(switchB[2]));
			drawers.drawer7 = Integer.valueOf(String.valueOf(switchB[1]));
			drawers.drawer8 = Integer.valueOf(String.valueOf(switchB[0]));
			drawers.drawer9 = Integer.valueOf(String.valueOf(switchB[15]));
			drawers.drawer10= Integer.valueOf(String.valueOf(switchB[14]));
			System.out.println("uuuuuuuDrawersState    " + draDao.updateDawers(drawers));
			
			//更新数据库中控制温度信息
			SettingTem sTem = new SettingTem();
			sTem.settingTemId = 1;
			sTem.settingTemNum = (int) CTem;
			System.out.println("uuuuuuuSettingTem    " + sTemDao.updateSettingTem(sTem));
			
			//更新数据库中系统参数信息
			SysParam sysParam = new SysParam();
			sysParam.sysParamId = 1;
			sysParam.deviceCoding = String.valueOf(deId);
			sysParam.deviceAddr = (int) deAddr;
			sysParam.stateUpInterval = (int) upInternal;
			sysParam.startOutTime = (int) outTime;
			sysParam.settingTem = (int) CTem;
			sysParam.TemCBias = (int) temConBias;
			System.out.println("uuuuuuuSysParam    " + spDao.updateSysParam(sysParam));
		}else {
			System.out.println("设备编码或设备地址不正确，丢弃");
		}
	}

	/**
	 * 根据数据帧确定跟字段的内容
	 * 
	 * @param dataSeg
	 * @param fourBit
	 * @param i
	 * @return
	 */
	public static int dataSegSet(char[] dataSeg, char[] fourBit, int i) {

		int j = 0;

		for (; j < dataSeg.length; j++, i++) {
			dataSeg[j] = fourBit[i];
		}

		return i;
	}

	/**
	 * 将数据帧中的开关状态装换为二进制字符串
	 * 
	 * @param switchState
	 * @return
	 */
	private static String disDrawersSet(char[] switchState) {
		// Drawers drawers = new Drawers();
		// 将开关数据转换为十六进制字符串
		String switchHS = String.copyValueOf(switchState);
		// 将开关数据转换为十六进制数据
		Integer switchH = Integer.parseInt(switchHS, 16);
		// 将十六进制数据转换为二进制字符串
		String switchBS = Integer.toBinaryString(switchH).toString();
		// 将二进制字符串转换为二进制字符数组
		char[] switchB = switchBS.toCharArray();
		// 对少于16位的数组进行补0操作
		while (switchB.length < 16) {
			switchBS = '0' + switchBS;
			switchB = switchBS.toCharArray();
		}
		return switchBS;
	}

	/**
	 * 对非温度、非开关数据的处理-->将其转换为十进制数据
	 * 
	 * @param dataSet
	 * @return
	 */
	public static long disDataSet(char[] dataSet) {
		long result = 0;
		// 数据转换为十六进制字符串
		String dataSetS = String.valueOf(dataSet);
		// 十六进制字符串转换为十进制数据
		result = Util.hexToDec(dataSetS);
		return result;
	}

	/**
	 * 对温度数据的处理-->根据最高位和最低位，分析温度数值，将其转换为十进制数
	 * @param Tem
	 * @return
	 */
	public static double disTemSet(char[] Tem) {
		double result = 0;
		// 将温度转换为十六进制字符串
		String TemHS = String.valueOf(Tem);
		// 将温度转换为十六进制数据
		Integer TemH = Integer.parseInt(TemHS, 16);
		// 将温度转换为二进制字符串
		String TemBS = Integer.toBinaryString(TemH);
		// 将温度转换为二进制数字数组
		Tem = TemBS.toCharArray();
		// 对少于8位的数组进行补0操作
		while (Tem.length < 8) {
			TemBS = '0' + TemBS;
			Tem = TemBS.toCharArray();
		}
		// 将温度数组掐头去尾得到温度核心数组
		char[] TemPayLaod = new char[6];
		// 温度核心数组赋值
		for (int i = 0, j = 1; i < TemPayLaod.length; i++, j++) {
			TemPayLaod[i] = Tem[j];
		}
		// 温度核心数组转换成二进制字符串
		String TempayS = String.valueOf(TemPayLaod);
		// 二进制字符串转换为十进制数
		result = Integer.parseInt(TempayS, 2);
		// 根据温度最低位判断，温度是否有小数（最高位为1时，小数位置为0.5）
		if (Tem[7] == '1') {
			result += 0.5;
		}
		// 根据温度最高位判断，温度是否为负（最高位为1时，温度为负）
		if (Tem[0] == '1') {
			result *= (-1);
		}
		return result;
	}
}
